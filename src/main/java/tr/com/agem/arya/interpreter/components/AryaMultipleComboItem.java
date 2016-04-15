package tr.com.agem.arya.interpreter.components;

import android.view.View;
import android.widget.ArrayAdapter;

import org.xml.sax.Attributes;

import tr.com.agem.arya.interpreter.components.base.AryaMain;
import tr.com.agem.core.interpreter.IAryaComponent;
import tr.com.agem.core.utils.AryaUtils;

public class AryaMultipleComboItem extends View implements IAryaComponent {


    private String componentClassName;
    private String componentId;
    private String componentValue;
    private String database;

    private String attribute;
    private String attributeValue;
    private String attributeLabel;


    private String label;

    public AryaMultipleComboItem(Attributes attributes, AryaMain main) {
        super(main.getAryaWindow().getContext());

        if(AryaUtils.isNotEmpty(attributes)){
            this.componentId = attributes.getValue("id");
            this.componentClassName = attributes.getValue("class");
            this.componentValue = attributes.getValue("value");
            this.database = attributes.getValue("database");

            this.attribute = attributes.getValue("attribute");
            this.attributeValue = attributes.getValue("attributeValue");
            this.attributeLabel = attributes.getValue("attributeLabel");


            this.label=attributes.getValue("label");
        }

        main.getAryaWindow().addView(this);
    }

    public String toString(){
        return this.label;
    }
    public String getComponentId() { return componentId; }

    public void setComponentId(String componentId) {this.componentId = componentId; }

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

    @Override
    public void setComponentParent(Object o) {
        AryaMultipleComboBox mcb = (AryaMultipleComboBox) o;

        if(mcb.getAdapter()!=null){
            ((ArrayAdapter<AryaMultipleComboItem>) mcb.getAdapter()).add(this);
            AryaMultipleComboBox.setListViewHeightBasedOnChildren(mcb);
        }
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
        return "multiplecomboitem";
    }

}
