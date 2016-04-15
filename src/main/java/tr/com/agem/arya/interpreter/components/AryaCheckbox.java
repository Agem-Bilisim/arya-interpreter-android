package tr.com.agem.arya.interpreter.components;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableRow;
import android.widget.Toast;

import org.xml.sax.Attributes;

import tr.com.agem.arya.interpreter.components.base.AryaMain;
import tr.com.agem.arya.interpreter.script.ScriptHelper;
import tr.com.agem.core.interpreter.IAryaComponent;
import tr.com.agem.core.utils.AryaUtils;

public class AryaCheckbox extends CheckBox implements IAryaComponent {

    private String componentId;
    private String componentClassName;
    private boolean mandatory;
    private String componentValue;

    private String database;
    private String attribute;
    private String attributeValue;
    private String attributeLabel;

    public AryaCheckbox(Attributes attributes, final AryaMain main) {
        super(main.getAryaWindow().getContext());

        String height=null;
        String mandatory=null;
        String readonly=null;

        if(AryaUtils.isNotEmpty(attributes)){

            this.componentId = attributes.getValue("id");
            this.componentClassName = attributes.getValue("class");
            this.componentValue = attributes.getValue("value");

            this.database = attributes.getValue("database");
            this.attribute = attributes.getValue("attribute");
            this.attributeValue = attributes.getValue("attributeValue");
            this.attributeLabel = attributes.getValue("attributeLabel");

            this.setText(attributes.getValue("label"));
            final String tooltiptext = attributes.getValue("tooltiptext");
            this.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    if(tooltiptext!=null) {
                        Toast.makeText(v.getContext(), tooltiptext, Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
            this.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    Toast.makeText(v.getContext(), tooltiptext, Toast.LENGTH_SHORT).show();
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
            this.setText(attributes.getValue("label"));
            height =attributes.getValue("height");
            mandatory = attributes.getValue("mandatory");
            readonly = attributes.getValue("readonly");


            final String onCheck =attributes.getValue("onCheck");
            if (onCheck != null && !onCheck.equals("")) {
                this.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        ScriptHelper.executeScript(onCheck, null, main);
                    }
                });
            }
        }

        this.setHeight(height != null ? Integer.parseInt(height) : 100);
        this.mandatory = mandatory != null && Boolean.parseBoolean(mandatory);
        this.setEnabled(AryaUtils.isNotEmpty(readonly)?Boolean.parseBoolean(readonly):true);

        main.getAryaWindow().addView(this);
    }

    @Override
    public String validate(){ return null;}

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

    public boolean isMandatory() { return mandatory;}

    public void setMandatory(boolean mandatory) {this.mandatory = mandatory;}

    public String getComponentId() {return componentId;}
    public void setComponentId(String componentId) {this.componentId = componentId;}

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
        return "checkbox";
    }

}
