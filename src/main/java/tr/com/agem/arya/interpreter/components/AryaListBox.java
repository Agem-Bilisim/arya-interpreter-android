package tr.com.agem.arya.interpreter.components;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tr.com.agem.arya.interpreter.components.base.AryaMain;
import tr.com.agem.arya.interpreter.components.base.AryaNavBar;
import tr.com.agem.core.interpreter.IAryaComponent;
import tr.com.agem.core.interpreter.IAryaTemplate;
import tr.com.agem.core.utils.AryaUtils;

public class AryaListBox extends TableLayout implements IAryaComponent, IAryaTemplate {

    private String componentClassName;
    private String componentId;
    private String componentAttribute;
    private String componentValue;
    private String database;
    private String attribute;
    private String attributeValue;
    private String attributeLabel;
    private Spinner listBoxSpinner = new Spinner(AryaNavBar.context,Spinner.MODE_DIALOG);
    private TextView label = new TextView(AryaNavBar.context);
    private List<String> spinnerItems = new ArrayList<>();
    private ImageView sort_icon = new ImageView(AryaNavBar.context);
    private EditText search_editText = new EditText(AryaNavBar.context);
    private Button search_button = new Button(AryaNavBar.context);
    private AryaTemplate template;
    private LinearLayout linLayout = new LinearLayout(AryaNavBar.context);
    private ArrayAdapter<String> adapter;
    private String onSelect;

    public AryaListBox(Attributes attributes, final AryaMain main) {

        super(main.getAryaWindow().getContext());

        if (AryaUtils.isNotEmpty(attributes)) {


            this.componentId = attributes.getValue("id");
            this.componentClassName = attributes.getValue("class");
            this.componentValue = attributes.getValue("value");
            this.componentAttribute = attributes.getValue("attribute");
            this.database = attributes.getValue("database");

            this.attribute = attributes.getValue("attribute");
            this.attributeValue = attributes.getValue("attributeValue");
            this.attributeLabel = attributes.getValue("attributeLabel");

            onSelect = attributes.getValue("onSelect");
        }


       //setupLinearLayout();
        //main.getAryaWindow().addView(this);
        this.setMinimumWidth(LayoutParams.MATCH_PARENT);
        HorizontalScrollView HorizontalScrollViewParent = new HorizontalScrollView(main.getAryaWindow().getContext());
        main.getAryaWindow().addView(HorizontalScrollViewParent);
        HorizontalScrollViewParent.addView(this);

        label.setText("Listeleme kriterini seçiniz:");
        label.setTypeface(null, Typeface.ITALIC);
        label.setTextSize(16);
        this.addView(label);
        this.addView(listBoxSpinner);
        this.addView(linLayout);


        label.setVisibility(GONE);
        listBoxSpinner.setVisibility(GONE);
        adapter = new ArrayAdapter<>(AryaNavBar.context, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listBoxSpinner.setAdapter(adapter);


        listBoxSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                listBoxSpinner.post(new Runnable() {
                    @Override
                    public void run() {
                        listBoxSpinner.setSelection(getListHead().getMasterCol(), false);
                    }
                });
                getListHead().setMasterCol(position);   //change master column according to spinner\

                for (int i = 2; i < getChildCount(); i++) {
                    if (getChildAt(i) instanceof AryaListItem) {
                        for (int j = 0; j < ((AryaListItem) getChildAt(i)).getChildCount(); j++) {

                            if (j == position) {
                                WindowManager wm = (WindowManager) AryaNavBar.context.getSystemService(Context.WINDOW_SERVICE);
                                Display display = wm.getDefaultDisplay();
                                Point size = new Point();
                                display.getSize(size);
                                int width = size.x;
                                if (!(((AryaListCell) ((AryaListItem) getChildAt(i)).getChildAt(j)).getText().toString().equals(""))) {
                                    (((AryaListItem) getChildAt(i)).getChildAt(j)).setVisibility(VISIBLE);
                                    (((AryaListItem) getChildAt(i)).getChildAt(j)).setLayoutParams(new TableRow.LayoutParams(width - 100, TableRow.LayoutParams.WRAP_CONTENT, 1f)); //MATCH_PARENT is not working here, thats why I used pixels
                                } else {
                                    (((AryaListItem) getChildAt(i)).getChildAt(j)).setVisibility(GONE);
                                    (((AryaListItem) getChildAt(i)).getChildAt(j)).setLayoutParams(new TableRow.LayoutParams(0, 0, 0f));
                                }
                            } else {
                                (((AryaListItem) getChildAt(i)).getChildAt(j)).setVisibility(GONE);
                                (((AryaListItem) getChildAt(i)).getChildAt(j)).setLayoutParams(new TableRow.LayoutParams(0, 0, 0f));
                            }

                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    public AryaListItem getListHead(){
        for(int i=0; i<getChildCount(); i++){
            if(getChildAt(i) instanceof AryaListItem)
                return (AryaListItem) getChildAt(i);
        }
        return null;
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
    public Object getAryaTemplate() {
        return template;
    }

    @Override
    public void setAryaTemplate(Object template) {
        this.template = (AryaTemplate) template;
    }

    public String getOnSelect() {
        return onSelect;
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
        return "listbox";
    }

    public List<String> getSpinnerItems() {
        return spinnerItems;
    }

    public void setSpinnerItems(List<String> spinnerItems) {
        this.spinnerItems = spinnerItems;
    }

    public Spinner getListBoxSpinner() {
        return listBoxSpinner;
    }

    public void setListBoxSpinner(Spinner listBoxSpinner) {
        this.listBoxSpinner = listBoxSpinner;
    }

    public TextView getLabel() {
        return label;
    }

    public void setLabel(TextView label) {
        this.label = label;
    }

    public ArrayAdapter<String> getAdapter() {
        return adapter;
    }

    public void setAdapter(ArrayAdapter<String> adapter) {
        this.adapter = adapter;
    }
    public LinearLayout getLinLayout() {
        return linLayout;
    }

    public void setLinLayout(LinearLayout linLayout) {
        this.linLayout = linLayout;
    }

    public void setupLinearLayout(){
        LinearLayout.LayoutParams params = new
                LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params.gravity=Gravity.CENTER;

//create a button
        search_editText.setHint("Aranacak kelimeyi giriniz.");
        search_editText.setVisibility(VISIBLE);
        search_editText.setLayoutParams(params);

//create text view widget
        search_button.setText("ARA");
        search_button.setLayoutParams(params);
        search_button.setVisibility(VISIBLE);
        search_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                int position =getListHead().getMasterCol();
                for (int i = 2; i < getChildCount(); i++) {
                    if (getChildAt(i) instanceof AryaListItem) {
                        for (int j = 0; j < ((AryaListItem) getChildAt(i)).getChildCount(); j++) {
                            if (j == position) {
                                if ((((AryaListCell) ((AryaListItem) getChildAt(i)).getChildAt(j)).getText().toString().toLowerCase().contains(search_editText.getText().toString().toLowerCase()))) {
                                    WindowManager wm = (WindowManager) AryaNavBar.context.getSystemService(Context.WINDOW_SERVICE);
                                    Display display = wm.getDefaultDisplay();
                                    Point size = new Point();
                                    display.getSize(size);
                                    int width = size.x;
                                    (((AryaListItem) getChildAt(i)).getChildAt(j)).setVisibility(VISIBLE);
                                    (((AryaListItem) getChildAt(i)).getChildAt(j)).setLayoutParams(new TableRow.LayoutParams(width - 100, TableRow.LayoutParams.WRAP_CONTENT, 1f)); //MATCH_PARENT is not working here, thats why I used pixels

                                } else {
                                    (((AryaListItem) getChildAt(i)).getChildAt(j)).setVisibility(GONE);
                                    (((AryaListItem) getChildAt(i)).getChildAt(j)).setLayoutParams(new TableRow.LayoutParams(0, 0, 0f));
                                }
                            }

                        }
                    }
                }

            }
        });





        String uri = "@drawable/sort_icon";  // where myresource.png is the file
        int imageResource = getResources().getIdentifier(uri, null, AryaNavBar.context.getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        sort_icon.setImageDrawable(res);
        sort_icon.setVisibility(VISIBLE);
        sort_icon.setLayoutParams(params);


        sort_icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<AryaListItem> itemsToSort = new ArrayList<>();
                for(int i=0;i<getChildCount();i++){
                    if(getChildAt(i) instanceof AryaListItem){
                        itemsToSort.add(((AryaListItem)getChildAt(i)));
                    }
                }
                Collections.sort(itemsToSort,new ListItemComparator());
            }
        });
        linLayout.setOrientation(LinearLayout.HORIZONTAL);
        // creating LayoutParams

        linLayout.addView(sort_icon);
        linLayout.addView(search_editText);
        linLayout.addView(search_button);

        linLayout.setVisibility(GONE);


    }
    class ListItemComparator implements Comparator<AryaListItem> {
        @Override
        public int compare(AryaListItem o1, AryaListItem o2) {
            int position = getListHead().getMasterCol();
            return ((AryaListCell)o1.getChildAt(position)).getText().toString().compareTo(((AryaListCell) o2.getChildAt(position)).getText().toString());
        }
    }
}



