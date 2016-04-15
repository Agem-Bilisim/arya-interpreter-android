package tr.com.agem.arya.interpreter.script;

import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeFunction;
import org.mozilla.javascript.NativeJSON;
import org.mozilla.javascript.Scriptable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import tr.com.agem.arya.MainActivity;
import tr.com.agem.arya.gateway.AryaInterpreterHelper;
import tr.com.agem.arya.interpreter.components.AryaChart;
import tr.com.agem.arya.interpreter.components.AryaCheckbox;
import tr.com.agem.arya.interpreter.components.AryaComboBox;
import tr.com.agem.arya.interpreter.components.AryaListBox;
import tr.com.agem.arya.interpreter.components.AryaListItem;
import tr.com.agem.arya.interpreter.components.AryaRadio;
import tr.com.agem.arya.interpreter.components.AryaRadiogroup;
import tr.com.agem.arya.interpreter.components.base.AryaMain;
import tr.com.agem.core.gateway.model.AryaResponse;
import tr.com.agem.core.interpreter.IAryaComponent;



public class ElementFunctions extends AnnotatedScriptableObject {

    private static final long serialVersionUID = 2251889177219110859L;
    private static final Logger logger = Logger.getLogger(ElementFunctions.class.getName());

    private Context context;
    private Scriptable scope;
    private AryaMain main;

    private static String lastPage;
    private static String reqType;

    private static JSONObject jsonObj;

    private LineGraphSeries lineSeries;
    private BarGraphSeries<DataPoint> barSeries;

    public ElementFunctions(Context context, Scriptable scope, AryaMain main) {
        this.context = context;
        this.scope = scope;
        this.main = main;
    }

    @AryaJsFunction
    public void populate(String data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(data);
            if (rootNode != null) {
                Iterator<Entry<String, JsonNode>> fields = rootNode.fields();
                if (fields != null) {
                    while (fields.hasNext()) {
                        Entry<String, JsonNode> entry = fields.next();
                        logger.log(Level.FINE, "JSON property: {0}:{1}", new Object[]{entry.getKey(), entry.getValue()});

                        IAryaComponent comp = (IAryaComponent) getElementById(entry.getKey());
                        if (comp != null) {
                            comp.setComponentValue(entry.getValue().asText());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @AryaJsFunction
    public void post(String action, String requestType, Object params, String tabValue, NativeFunction onSuccess, NativeFunction onFailure) {

        if (!(params instanceof String)) {
            Object jsonParam = NativeJSON.stringify(context, scope, params, null, null);
            params = jsonParam;
        }

        StringBuilder request = new StringBuilder("{ \"params\": ")
                .append(params)
                .append(", \"requestType\": \"")
                .append(requestType)
                .append("\", \"action\": \"")
                .append(action)
                .append("\" }");

        lastPage = action;
        reqType = requestType;
        String result = AryaInterpreterHelper.callUrl("http://" + MainActivity.inetAddr + ":8080/arya/rest/asya", request.toString());

        logger.log(Level.FINE, "Post result: {0}", result);


        AryaResponse response = new AryaResponse();
        response.fromXMLString(result);//TODO result check

        if (tabValue != null && tabValue.equals("login")) {
            AryaInterpreterHelper.interpretResponse(response, action, true, main);
        } else {
            AryaInterpreterHelper.interpretResponse(response, action, false, main);
        }

        //TODO response fail condition add

        if (onSuccess != null) {
            scope.put(onSuccess.getFunctionName(), scope, onSuccess);
            Context.call(null, onSuccess, scope, this, new Object[]{response});
        }
        if (onFailure != null) {
            scope.put(onFailure.getFunctionName(), scope, onFailure);
            Context.call(null, onFailure, scope, this, new Object[]{response});
        }
    }

    @AryaJsFunction
    public void renderSelectedItem(String elementId, String id, String action, NativeArray comps, NativeArray values,
                                   String tabValue) {

        String params = "{\"id\":\"" + splitId(id, jsonObj) + "\"}";

        for (int i = 0; i < comps.size(); i++) {

            String value = splitId(values.get(i).toString(), jsonObj);

            String comp = (String) comps.get(i);

            ((IAryaComponent) getElementById(comp)).setComponentValue(value);

        }

        if (!action.isEmpty())
            post(action, "ALL", params, tabValue, null, null);

    }

    @AryaJsFunction
    public void renderAtSamePage(String elementId, String id, String action, String tabValue) {

        if (!action.isEmpty() && action.endsWith("list")) {
            //TODO isyeriIdParam -> idParam !!!
            String params = "{\"isyeriIdParam\":\"" + splitId(id, jsonObj) + "\",\"id\":\"" + splitId(id, jsonObj) + "\"}";
            post(action, "DATA_ONLY", params, tabValue, null, null);
        }
    }

    @AryaJsFunction
    public void setChartModel(String chartId){   // This method behaves differently than Zkoss.

        IAryaComponent comp = (IAryaComponent) getElementById(chartId);

        if(comp instanceof AryaChart) {

            if (((AryaChart) comp).getVisibility() == View.VISIBLE) {
                ((AryaChart) comp).removeAllSeries();
            }
        }
    }

    @AryaJsFunction
    public void setChartValue(String chartId, String category, String secondCategory, Double value){

        IAryaComponent comp = (IAryaComponent) getElementById(chartId);

        Random rand = new Random();

        if(comp instanceof AryaChart){

            if(((AryaChart) comp).getType() != null){
                if(((AryaChart) comp).getType().equals("line")){

                    lineSeries = new LineGraphSeries<DataPoint>(new DataPoint[] {
                            new DataPoint(0, value)
                    });
                    lineSeries.setColor(rand.nextInt());
                    lineSeries.setTitle(category);
                    ((AryaChart) comp).addSeries(lineSeries);

                } else if(((AryaChart) comp).getType().equals("pie") || ((AryaChart) comp).getType().equals("stackbar") ||
                        ((AryaChart) comp).getType().equals("bar") || ((AryaChart) comp).getType().equals("column")){

                    barSeries = new BarGraphSeries<DataPoint>(new DataPoint[] {
                            new DataPoint(0, value)
                    });
                    barSeries.setColor(rand.nextInt());
                    barSeries.setTitle(category);
                    ((AryaChart) comp).addSeries(barSeries);
                }
            }
            ((AryaChart) comp).getViewport().setScrollable(true);
            ((AryaChart) comp).setVisibility(View.VISIBLE);
            ((AryaChart) comp).getLegendRenderer().setVisible(true);
            ((AryaChart) comp).getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
        }
    }

    @AryaJsFunction
    public void clean(String parentCompId){


        List<IAryaComponent> components = main.getAryaWindow().getComponents();

        for (IAryaComponent parentComp : components) {
            if (parentComp.getComponentId() != null) {
                if (parentComp.getComponentId().equals(parentCompId)) {
                    for (int i = 0; i < ((ViewGroup) parentComp).getChildCount(); i++) {
                        if (((ViewGroup) parentComp).getChildAt(i) instanceof IAryaComponent) {
                            IAryaComponent cmp = ((IAryaComponent) ((ViewGroup) parentComp).getChildAt(i));
                            if(cmp instanceof AryaListBox || cmp instanceof AryaListItem){
                                clean(cmp);    //depth-first search
                            }
                            else if (AryaInterpreterHelper.isInputElement(cmp)) {
                                if (cmp instanceof AryaRadiogroup) {
                                    for (int j = 0; j < ((AryaRadiogroup) cmp).getChildCount(); j++) {
                                        ((AryaRadio) ((AryaRadiogroup) cmp).getChildAt(j)).setChecked(false);
                                    }
                                } else if (cmp instanceof AryaCheckbox) {
                                    ((AryaCheckbox) cmp).setChecked(false);
                                } else if (cmp instanceof AryaComboBox) {
                                    ((AryaComboBox) cmp).setSelection(0, true);
                                }
                                cmp.setComponentValue("");

                            }
                        }
                    }
                    break; //no need to check further
                }
            }
        }
    }

    //please do NOT tag it with AryaJSFunction, it's an overloaded version of clean which helps the original one in a recursive way.
    public void clean(IAryaComponent parentComp){
                    for (int i = 0; i < ((ViewGroup) parentComp).getChildCount(); i++) {
                        if (((ViewGroup) parentComp).getChildAt(i) instanceof IAryaComponent) {
                            IAryaComponent cmp = ((IAryaComponent) ((ViewGroup) parentComp).getChildAt(i));
                            if(cmp instanceof AryaListBox || cmp instanceof AryaListItem){
                                clean(cmp);    //depth-first search
                            }
                            else if (AryaInterpreterHelper.isInputElement(cmp)) {
                                if (cmp instanceof AryaRadiogroup) {
                                    for (int j = 0; j < ((AryaRadiogroup) cmp).getChildCount(); j++) {
                                        ((AryaRadio) ((AryaRadiogroup) cmp).getChildAt(j)).setChecked(false);
                                    }
                                } else if (cmp instanceof AryaCheckbox) {
                                    ((AryaCheckbox) cmp).setChecked(false);
                                } else if (cmp instanceof AryaComboBox) {
                                    ((AryaComboBox) cmp).setSelection(0, true);
                                }
                                cmp.setComponentValue("");

                            }
                        }
                    }
    }

    @AryaJsFunction
    public void send(String action, String requestType, String parentObjectId, String objectIdProp, String tabName) throws JsonProcessingException {

        List<IAryaComponent> components = main.getAryaWindow().getComponents();
        Map<String, Object> m = new HashMap<String, Object>();
        for (IAryaComponent cmp : components) {
            if (AryaInterpreterHelper.isInputElement(cmp)) {

                String v = cmp.getComponentValue();
                String d = cmp.getDatabase();

                if (d != null)
                    m.put(d, v);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(m);

        post(action, requestType, json, null, null, null);
    }

    @AryaJsFunction
    public Object getElementById(String id) {

        for (int i = 0; i < main.getAryaWindow().getComponents().size(); i++) {

            if (main.getAryaWindow().getComponents().get(i) instanceof IAryaComponent) {
                IAryaComponent child = main.getAryaWindow().getComponents().get(i);

                if (id.trim().equals(child.getComponentId())) {
                    return child;
                }
            }
        }
        return null;
    }

    @AryaJsFunction
    public Object[] getElementsByName(String name) {
        List objList = new ArrayList();
        View child = null;
        for (int i = 0; i < main.getAryaWindow().getChildCount(); i++) {

            child = main.getAryaWindow().getChildAt(i);
            if (child instanceof IAryaComponent) {
                IAryaComponent o = (IAryaComponent) child;
                if (name.equalsIgnoreCase(
                        o.getClass().toString().replace("class tr.com.agem.arya.interpreter.components.Arya", ""))) {
                    objList.add(o);
                }
            }
        }
        return objList.toArray(new Object[objList.size()]);
    }

    @AryaJsFunction
    public Object[] getElementsByClass(String className) {
        List objList = new ArrayList();
        View child = null;
        for (int i = 0; i < main.getAryaWindow().getChildCount(); i++) {

            child = main.getAryaWindow().getChildAt(i);
            if (child instanceof IAryaComponent) {
                IAryaComponent o = (IAryaComponent) child;
                if (className.equalsIgnoreCase(o.getComponentClassName())) {
                    objList.add(o);
                }
            }
        }
        return objList.toArray(new Object[objList.size()]);
    }

    @AryaJsFunction
    public String serializeForm() {
        String strSerialize = "";
        View child = null;
        for (int i = 0; i < main.getAryaWindow().getChildCount(); i++) {
            child = main.getAryaWindow().getChildAt(i);
            if (child instanceof IAryaComponent) {
                IAryaComponent o = (IAryaComponent) child;
                strSerialize += ",\"" + o.getComponentId() + "\":"
                        + (o.getComponentValue() == null ? null : "\"" + o.getComponentValue() + "\"");
            }
        }

        if (strSerialize.length() > 0)
            return "{" + strSerialize.substring(1, strSerialize.length()) + "}";
        return "{}";
    }

    public static String splitId(String id, JSONObject jsonObj) {

        String retVal = null;
        JSONObject obj = null;

        if (jsonObj != null) {

            String[] spl;

            if (id.contains("-")) {

                String[] temp = id.split("-");
                spl = temp[1].split("\\.");
            } else {
                spl = id.split("\\.");
            }

            for (int i = 0; i < spl.length - 1; i++)
                obj = (JSONObject) jsonObj.get(spl[i]);

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

    public static JSONObject getJsonObj() {
        return jsonObj;
    }

    public static void setJsonObj(JSONObject jsonObj) {
        ElementFunctions.jsonObj = jsonObj;
    }

    public static String getLastPage() {
        return lastPage;
    }

    public static void setLastPage(String lastPage) {
        ElementFunctions.lastPage = lastPage;
    }

    public static String getReqType() {
        return reqType;
    }

    public static void setReqType(String reqType) {
        ElementFunctions.reqType = reqType;
    }

}
