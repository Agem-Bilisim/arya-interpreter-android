package tr.com.agem.arya.interpreter.components;

import android.view.ViewGroup;
import android.widget.TableRow;

import com.jjoe64.graphview.GraphView;

import org.xml.sax.Attributes;

import tr.com.agem.arya.interpreter.components.base.AryaMain;
import tr.com.agem.core.interpreter.IAryaComponent;
import tr.com.agem.core.utils.AryaUtils;

public class AryaChart extends GraphView implements IAryaComponent {

    private String componentClassName;
    private String componentId;
    private String componentValue;
    private String database;

    private String attribute;
    private String attributeValue;
    private String attributeLabel;

    private String type;

    public AryaChart(Attributes attributes, final AryaMain main){
        super(main.getAryaWindow().getContext());

        if(AryaUtils.isNotEmpty(attributes)) {

            this.componentId = attributes.getValue("id");
            this.componentClassName = attributes.getValue("class");
            this.componentValue = attributes.getValue("value");
            this.database = attributes.getValue("database");

            this.attribute = attributes.getValue("attribute");
            this.attributeValue = attributes.getValue("attributeValue");
            this.attributeLabel = attributes.getValue("attributeLabel");

            this.type = attributes.getValue("type");
            this.setVisibility(GONE);
            this.setMinimumHeight(250);

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getComponentTagName() {
        return "chart";
    }
}
