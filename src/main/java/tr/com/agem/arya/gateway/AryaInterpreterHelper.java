package tr.com.agem.arya.gateway;

import android.app.Activity;
import android.widget.ImageView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import tr.com.agem.arya.R;
import tr.com.agem.arya.interpreter.components.AryaCheckbox;
import tr.com.agem.arya.interpreter.components.AryaComboBox;
import tr.com.agem.arya.interpreter.components.AryaComboItem;
import tr.com.agem.arya.interpreter.components.AryaListBox;
import tr.com.agem.arya.interpreter.components.AryaListCell;
import tr.com.agem.arya.interpreter.components.AryaListItem;
import tr.com.agem.arya.interpreter.components.AryaRadiogroup;
import tr.com.agem.arya.interpreter.components.AryaTemplate;
import tr.com.agem.arya.interpreter.components.AryaTextbox;
import tr.com.agem.arya.interpreter.components.base.AryaMain;
import tr.com.agem.arya.interpreter.components.base.AryaNavBar;
import tr.com.agem.arya.interpreter.components.command.AryaFill;
import tr.com.agem.arya.interpreter.parser.AryaMetadataParser;
import tr.com.agem.arya.interpreter.parser.AryaParserAttributes;
import tr.com.agem.core.gateway.model.AryaRequest;
import tr.com.agem.core.gateway.model.AryaResponse;
import tr.com.agem.core.interpreter.IAryaComponent;
import tr.com.agem.core.interpreter.IAryaTemplate;
import tr.com.agem.core.utils.AryaUtils;


public class AryaInterpreterHelper {

    private static final String MIME_TYPE = "application/json";
    private static final String TAG = "AryaInterpreterHelper";

    private static String jSessionId = null;

    public static String callUrl(String url, AryaRequest request) {
        return callUrl(url, request.toJSON());
    }

    public static String callUrl(String url, String request) {
        try {

            URL urlObj = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", MIME_TYPE);
            urlConnection.setRequestProperty("Accept", MIME_TYPE);

            if (jSessionId != null)
                urlConnection.setRequestProperty("Cookie", jSessionId);

            urlConnection.setConnectTimeout(4000);
            urlConnection.setDoOutput(true);


            DataOutputStream writer = new DataOutputStream(urlConnection.getOutputStream());
            writer.writeBytes(request);
            writer.flush();
            writer.close();


            Map<String, List<String>> hdrs = urlConnection.getHeaderFields();
            Set<String> hdrKeys = hdrs.keySet();

            if (hdrs != null) {
                if (hdrs.get("Set-Cookie") != null) {
                    String header = hdrs.get("Set-Cookie").get(0);
                    jSessionId = header.substring(0, header.indexOf(";"));
                }
            }

            int responseCode = urlConnection.getResponseCode();

            String responseStr = null;

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                String line = null;
                StringBuffer responseText = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    responseText.append("\n");
                    responseText.append(line);
                }
                reader.close();
                responseStr = responseText.toString();
            }

            return responseStr;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void interpretResponse(AryaResponse response, String action, boolean login, AryaMain main) { //Overloaded function


        if (login) {
            main.getAryaNavBar().getMenuBar().getMenuItemsCopy().clear();
            for (int i = 0; i < main.getAryaNavBar().getMenuBar().getMenuItems().size(); i++) {    //If it is a login page, delete menu and have a backup
                main.getAryaNavBar().getMenuBar().getMenuItemsCopy().add(main.getAryaNavBar().getMenuBar().getMenuItems().get(i));
            }
            while (main.getAryaNavBar().getMenuBar().getMenuItems().size() > 0) {        //Determine which menu items remain on login screen
                main.getAryaNavBar().getMenuBar().getMenuItems().remove(0);
            }
        } else {
            if (main.getAryaNavBar().getMenuBar().getMenuItems().size() != main.getAryaNavBar().getMenuBar().getMenuItemsCopy().size()) {
                for (int i = 0; i < main.getAryaNavBar().getMenuBar().getMenuItemsCopy().size(); i++) {    //reload menu items to show in master
                    main.getAryaNavBar().getMenuBar().setMenuItems(main.getAryaNavBar().getMenuBar().getMenuItemsCopy());
                }
                ((Activity) (AryaNavBar.context)).invalidateOptionsMenu();
            }
        }
        if (AryaUtils.isNotEmpty(response.getView())) {

            if (main.getAryaWindow().getComponents() != null) {// TODO bu alan yönetilmeli neler kaldırılacak ekrandan

          /*      while ((main.getAryaWindow().getChildCount()) != 1)
                    if(!(main.getAryaWindow().getChildAt(1) instanceof IAryaMenu)) {
                        main.getAryaWindow().removeView(main.getAryaWindow().getChildAt(1));
                   }
*/
                main.getAryaWindow().clearPageExceptMenu();
            }

            AryaInterpreterHelper.drawView(response.getView(), main, false);
        }

        if (AryaUtils.isNotEmpty(response.getData())) {

            //to remove old listbox items at same page
            if (response.getView().isEmpty()) {
                if (getElementById(action, main) instanceof AryaListBox) {

                    AryaListBox listbox = (AryaListBox) getElementById(action, main);

                    while (listbox.getChildAt(1) != null) {
                        listbox.removeViewAt(1);
                    }
                }
            }
            AryaInterpreterHelper.populateView(response.getData(), action, main);
        }

        if (AryaUtils.isNotEmpty(response.getAttributes())) {

            List<IAryaComponent> comps = main.getAryaWindow().getComponents();
            for (IAryaComponent component : comps) {

                if (component.getAttribute() != null) {
                    populateToFill((String) response.getAttributes(), component, main);
                }
            }

        }
    }

    public static void interpretResponseMenu(AryaResponse response, AryaMain main) {
        if (AryaUtils.isNotEmpty(response.getView())) {// Remove previous
            // components before
            // adding new ones!
            if (AryaUtils.isNotEmpty(main.getAryaNavBar().getMenuBar())) {
                main.getAryaNavBar().getMenuBar().getMenuItems().clear();
            }

            drawView(response.getView(), main, true);
        }
    }

    private static void drawView(String view, AryaMain main, Boolean isMenu) {

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser parser = null;

        try {
            parser = saxParserFactory.newSAXParser();
            parser.parse(new InputSource(new StringReader(view)), new AryaMetadataParser(main));

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!isMenu) {
            ImageView image = new ImageView(main.getAryaWindow().getContext());
            image.setImageResource(R.drawable.agem_logo);
            image.setPadding(700, 0, 1, 0);

            main.getAryaWindow().addView(image);
        }
    }

    public static void populateToFill(String data, IAryaComponent cmp, AryaMain main) {

        for (int i = 0; i < getJSONArray(data).length(); i++) {

            JSONObject jsonObj = getJSONArray(data).getJSONObject(i);

            String comp = null;
            String value = null;

            if (cmp instanceof AryaFill) {
                comp = new String(((AryaFill) cmp).getTo());
                value = new String(((AryaFill) cmp).getValue());
            } else {
                JSONArray jsonArray = (JSONArray) jsonObj.get(cmp.getAttribute());

                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(j);

                    if (cmp instanceof AryaComboBox) {
                        AryaComboBox combo = (AryaComboBox) cmp;

                        AryaParserAttributes attr = new AryaParserAttributes();
                        String label = new String(splitId(combo.getAttributeLabel(), jsonObject));
                        String id = System.currentTimeMillis() + "" + splitId("id", jsonObject);

                        if (label != null) {
                            attr.setValue("label", label);
                            attr.setValue("id", id);
                            AryaComboItem comboItem = new AryaComboItem(attr, main);
                            comboItem.setComponentParent(combo);
                        }
                    }
                }
            }

            if (cmp instanceof AryaFill && comp != null) {

                if (getElementById(comp, main) instanceof AryaComboBox) {
                    AryaComboBox combo = (AryaComboBox) getElementById(comp, main);

                    AryaParserAttributes attr = new AryaParserAttributes();
                    String label = splitId(value, jsonObj);
                    String id = System.currentTimeMillis() + "" + splitId("id", jsonObj);

                    if (label != null) {
                        attr.setValue("label", label);
                        attr.setValue("id", id);
                        AryaComboItem comboItem = new AryaComboItem(attr, main);
                        comboItem.setComponentParent(combo);
                    }
                }
            }
        }
    }

    private static void populateAryaTemplate(AryaMain main, IAryaTemplate masterComponent, JSONArray jsonArrayData) {

        for (int i = 0; i < jsonArrayData.length(); i++) {

            JSONObject jsonObj = jsonArrayData.getJSONObject(i);

            AryaListItem item = new AryaListItem(null, main);

            if (masterComponent instanceof AryaListBox) {
                item.setComponentValue(jsonObj.toString());
                item.setComponentParent(masterComponent);
            }

            for (IAryaComponent comp : ((AryaTemplate) masterComponent.getAryaTemplate()).getChildren()) {

                if (!(comp instanceof AryaListItem)) {

                    AryaParserAttributes attr = new AryaParserAttributes();
                    attr.setValue("id", comp.getComponentId() + "" + (i));
                    if (masterComponent instanceof AryaListBox) {
                        attr.setValue("label", splitId(comp.getDatabase(), jsonObj));
                        AryaListCell cell = new AryaListCell(attr, main, null);
                        cell.setComponentParent(item);
                    }
                }
            }
        }
    }


    public static void populateView(String data, String action, AryaMain main) {

        try {
            String message = "";
            if (action.endsWith("list")) {
                if (getJSONArray(data).length() > 0)
                    message = getJSONArray(data).length() + " adet kayıt bulundu.";
                else
                    message = "Hiçbir kayıt bulunamadı.";

                populateAryaTemplate(main, (IAryaTemplate) getElementById(action, main), getJSONArray(data));

            } else if (getJSONArray(data).length() == 1) {

                JSONObject jsonObj = getJSONArray(data).getJSONObject(0);
                List<IAryaComponent> comps = main.getAryaWindow().getComponents();

                for (IAryaComponent cmp : comps) {
                    if (cmp.getComponentId() != null) {

                        IAryaComponent comp = (IAryaComponent) getElementById(cmp.getComponentId(), main);

                        if (isInputElement(comp)) {

                            String key = comp.getDatabase();
                            if (key != null) {
                                comp.setComponentValue(getJSONValue(jsonObj, key).toString());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public static JSONArray getJSONArray(String data) {

        //System.out.println(data);
        JSONArray jsonArray = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(data);
            if (rootNode != null) {
                Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
                if (fields != null) {
                    while (fields.hasNext()) {
                        Map.Entry<String, JsonNode> entry = fields.next();
                        if ("results".equals(entry.getKey().toString())) {
                            jsonArray = new JSONArray(entry.getValue().toString());
                        } else if ("session".equals(entry.getKey().toString())) {
                            jsonArray = new JSONArray(entry.getValue().toString());
                        }
                    }
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    public static boolean isInputElement(IAryaComponent comp) {
        return (comp instanceof AryaTextbox) ||
                (comp instanceof AryaRadiogroup) ||
                (comp instanceof AryaComboBox) ||
                (comp instanceof AryaCheckbox);
    }

    private static Object getJSONValue(JSONObject jsonObj, String key) {

        String[] temp = key.split("\\.");

        if (temp.length == 1) {
            Object v = "";
            try {
                v = jsonObj.get(temp[0].toString());
            } catch (Exception e) {
            }
            if (AryaUtils.isEmpty(v) || v.equals(null)) {
                return "";
            }
            return v;
        }
        Object obj = jsonObj.get(temp[0].toString());
        if (!(obj instanceof JSONObject)) {
            obj = null;
        }
        return obj == null ? "" : getJSONValue((JSONObject) obj, temp[1]);
    }


    public static Object getElementById(String id, AryaMain main) {

        IAryaComponent child = null;
        for (int i = 0; i < main.getAryaWindow().getComponents().size(); i++) {

            child = main.getAryaWindow().getComponents().get(i);
            if (child instanceof IAryaComponent) {
                IAryaComponent o = child;

                if (id.equals(o.getComponentId())) {
                    return o;
                }
            }
        }
        return null;
    }

    public static String splitId(String id, JSONObject jsonObj) {

        String retVal = null;
        JSONObject obj = jsonObj;

        if (jsonObj != null) {

            String[] spl;

            if (id.contains("-")) {

                String[] temp = id.split("-");
                spl = temp[1].split("\\.");
            } else {
                spl = id.split("\\.");
            }

            for (int i = 0; i < spl.length - 1; i++)
                obj = (JSONObject) obj.get(spl[i]);

            Object ret;

            if (obj != null)
                ret = obj.get(spl[spl.length - 1]);
            else
                ret = jsonObj.get(spl[0]);
            if (!ret.equals(JSONObject.NULL)) {
                retVal = ret.toString();
            }
        }
        return retVal;
    }

    public static String getjSessionId() {
        return jSessionId;
    }

}