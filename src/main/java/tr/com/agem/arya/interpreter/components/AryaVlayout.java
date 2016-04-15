package tr.com.agem.arya.interpreter.components;


import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableRow;

import org.xml.sax.Attributes;

import tr.com.agem.arya.interpreter.components.base.AryaMain;
import tr.com.agem.core.interpreter.IAryaComponent;
import tr.com.agem.core.utils.AryaUtils;

// NOT USED
public class AryaVlayout extends LinearLayout implements IAryaComponent {


    private String componentClassName;
    private String componentId;
    private String componentValue;

    public AryaVlayout(Attributes attributes, final AryaMain main) {
        super(main.getAryaWindow().getContext());

        if(AryaUtils.isNotEmpty(attributes)){
            this.setOrientation(LinearLayout.VERTICAL);
            this.componentId = attributes.getValue("id");
            this.componentClassName = attributes.getValue("class");
            this.componentValue = attributes.getValue("value");
            this.setVerticalScrollBarEnabled(true);
            this.setHorizontalScrollBarEnabled(true);
            this.setMinimumHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        }
        ScrollView scrollViewParent = new ScrollView(main.getAryaWindow().getContext());
        main.getAryaWindow().addView(scrollViewParent, main.getAryaWindow().getChildCount());
        scrollViewParent.addView(this);

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
        return null;
    }

    @Override
    public void setDatabase(String s) {

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
        return "vlayout";
    }


}
