package tr.com.agem.arya.interpreter.components;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableRow;

import org.xml.sax.Attributes;

import java.util.ArrayList;

import tr.com.agem.arya.interpreter.components.base.AryaMain;
import tr.com.agem.core.interpreter.IAryaComponent;
import tr.com.agem.core.utils.AryaUtils;


public class AryaMultipleComboBox extends ListView implements IAryaComponent {

    private String componentClassName;
    private String componentId;
    private String componentValue;
    private String database;

    private String attribute;
    private String attributeValue;
    private String attributeLabel;


    public AryaMultipleComboBox(Attributes attributes, AryaMain main) {

        super(main.getAryaWindow().getContext());

        if(AryaUtils.isNotEmpty(attributes)){
            this.componentId = attributes.getValue("id");
            this.componentClassName = attributes.getValue("class");
            this.componentValue = attributes.getValue("value");
            this.database = attributes.getValue("database");

            this.attribute = attributes.getValue("attribute");
            this.attributeValue = attributes.getValue("attributeValue");
            this.attributeLabel = attributes.getValue("attributeLabel");

        }

        createAdapter();
        main.getAryaWindow().addView(this);
    }

    private void createAdapter() {
        ArrayAdapter<AryaMultipleComboItem> listAdapter = new ArrayAdapter<>(this.getContext(),android.R.layout.simple_list_item_checked , new ArrayList<AryaMultipleComboItem>());
        listAdapter.setDropDownViewResource(android.R.layout.simple_list_item_multiple_choice);
        this.setAdapter(listAdapter);
        this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        this.setChoiceMode(CHOICE_MODE_MULTIPLE);

        setListViewHeightBasedOnChildren(this);
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
    public String getComponentId() {
        return componentId;
    }

    @Override
    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    @Override
    public String validate() {
        return null;
    }

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

    @Override
    public String getComponentValue() {
        return componentValue;
    }

    @Override
    public void setComponentValue(String componentValue) {
        this.componentValue = componentValue;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount()-1; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;// + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

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
        return "multiplecombobox";
    }

}
