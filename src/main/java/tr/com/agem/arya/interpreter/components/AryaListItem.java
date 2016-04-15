package tr.com.agem.arya.interpreter.components;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TableRow;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;

import tr.com.agem.arya.interpreter.components.base.AryaMain;
import tr.com.agem.arya.interpreter.script.ElementFunctions;
import tr.com.agem.arya.interpreter.script.ScriptHelper;
import tr.com.agem.core.interpreter.IAryaComponent;
import tr.com.agem.core.utils.AryaUtils;

public class AryaListItem extends TableRow implements IAryaComponent {
    private String componentId;
    private String componentValue;
    private String componentClassName;
    private String database;
    private String attribute;
    private String attributeValue;
    private String attributeLabel;


    private int masterCol=0;
    private AryaMain main;

    public AryaListItem(Attributes attributes, final AryaMain main) {

        super(main.getAryaWindow().getContext());

        if(AryaUtils.isNotEmpty(attributes)){
            this.componentId = attributes.getValue("id");
            this.componentClassName = attributes.getValue("class");
            this.componentValue = attributes.getValue("value");
            this.database = attributes.getValue("database");

            this.attribute = attributes.getValue("attribute");
            this.attributeValue = attributes.getValue("attributeValue");
            this.attributeLabel = attributes.getValue("attributeLabel");

            if(attributes.getValue("masterCol")!=null){
                this.masterCol=Integer.parseInt(attributes.getValue("masterCol"));
            }
        }
        this.main = main;

    }


    @Override
    public void setComponentParent(Object o) {
        AryaListBox lb = (AryaListBox) o;

       // this.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 10.0f));

        WindowManager wm = (WindowManager) tr.com.agem.arya.interpreter.components.base.AryaNavBar.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size= new Point();
        display.getSize(size);
        int width = size.x;
        this.setLayoutParams(new TableRow.LayoutParams(width-100, TableRow.LayoutParams.WRAP_CONTENT, 1f)); //MATCH_PARENT is not working here, thats why I used pixels
       // this.setPadding(1,1,1,1);

        lb.addView(this);

        //onClick is defined to listbox in arya files,
        // so before clicklistener, listitem must know its parent
        final String onSelect = lb.getOnSelect();

        this.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                v.setDrawingCacheBackgroundColor(Color.LTGRAY);

                if((((AryaListItem)v).getComponentValue()) != null) {
                    JSONObject j = null;
                    try {
                        j = new JSONObject(((AryaListItem) v).getComponentValue());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ElementFunctions.setJsonObj(j);

                    ScriptHelper.executeScript(onSelect, null, getMain());
                }
            }
        });

    }

    @Override
    public String getComponentId() {
        return componentId;
    }

    @Override
    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    @Override
    public String getComponentValue() {
        return componentValue;
    }

    @Override
    public void setComponentValue(String componentValue) {
        this.componentValue = componentValue;
    }

    @Override
    public String getComponentClassName() {
        return componentClassName;
    }

    @Override
    public void setComponentClassName(String componentClassName) {
        this.componentClassName = componentClassName;
    }

    @Override
    public String validate() {
        return null;
    }

    public AryaMain getMain() {
        return main;
    }

    public void setMain(AryaMain main) {
        this.main = main;
    }

    @Override
    public Object getComponentParent() {
        return this.getComponentParent();
    }

    @Override
    public String getDatabase() {
        return database;
    }

    @Override
    public void setDatabase(String database) {
        this.database = database;
    }

    @Override
    public String getAttribute() {
        return attribute;
    }

    @Override
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    @Override
    public String getAttributeLabel() {
        return attributeLabel;
    }

    @Override
    public void setAttributeLabel(String attributeLabel) {
        this.attributeLabel = attributeLabel;
    }

    @Override
    public String getComponentTagName() {
        return "listitem";
    }
    public int getMasterCol() {
        return masterCol;
    }

    public void setMasterCol(int masterCol) {
        this.masterCol = masterCol;
    }

}
