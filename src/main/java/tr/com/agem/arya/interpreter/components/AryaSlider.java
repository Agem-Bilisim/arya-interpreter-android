package tr.com.agem.arya.interpreter.components;

import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.Toast;

import org.xml.sax.Attributes;

import tr.com.agem.arya.interpreter.components.base.AryaMain;
import tr.com.agem.arya.interpreter.script.ScriptHelper;
import tr.com.agem.core.interpreter.IAryaComponent;
import tr.com.agem.core.utils.AryaUtils;

public class AryaSlider extends SeekBar implements IAryaComponent {

    private String componentClassName;
    private String componentId;
    private String componentValue;
    private String database;


    public AryaSlider(Attributes attributes, final AryaMain main) {
        super(main.getAryaWindow().getContext());

        if(AryaUtils.isNotEmpty(attributes)){

            this.componentId = attributes.getValue("id");
            this.componentClassName = attributes.getValue("class");
            this.componentValue = attributes.getValue("value");
            this.database = attributes.getValue("database");


            final String tooltiptext = attributes.getValue("tooltiptext");
            this.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    if(tooltiptext!=null) {
                        Toast.makeText(v.getContext(), tooltiptext, Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
            if (attributes.getValue("visible") != null) {
                if(attributes.getValue("visible").equals("true")){
                    this.setVisibility(VISIBLE);
                }
                else{
                    this.setVisibility(INVISIBLE);
                }
            }
            if (attributes.getValue("disabled") != null) {
                this.setEnabled(!Boolean.parseBoolean(attributes.getValue("disabled")));
            }

            if(attributes.getValue("value") != null){
                this.setProgress(Integer.parseInt(attributes.getValue("value")));
            }
            final String onClick =  attributes.getValue("onClick");

            if (onClick != null) {
                this.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScriptHelper.executeScript(onClick, null, main);
                    }
                });
            }
        }

        main.getAryaWindow().addView(this);
    }

    @Override
    public String validate(){ return null; }

    @Override
    public void setComponentParent(Object o) {
        if(o instanceof AryaListItem){
            AryaListItem li = (AryaListItem) o;

            this.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 10.0f));
            li.addView(this);
        }
        else {
            ((ViewGroup)o).addView(this);
        }
    }

    public String getComponentId() { return componentId; }
    public void setComponentId(String componentId) { this.componentId = componentId;}

    @Override
    public String getComponentClassName() {
        return componentClassName;
    }

    @Override
    public void setComponentClassName(String componentClassName) {
        this.componentClassName = componentClassName;
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
        return null;
    }

    @Override
    public void setAttribute(String s) {

    }

    @Override
    public String getAttributeValue() {
        return null;
    }

    @Override
    public void setAttributeValue(String s) {

    }

    @Override
    public String getAttributeLabel() {
        return null;
    }

    @Override
    public void setAttributeLabel(String s) {

    }

    @Override
    public String getComponentTagName() {
        return "slider";
    }

}
