package tr.com.agem.arya.interpreter.components;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;

import tr.com.agem.arya.R;
import tr.com.agem.arya.interpreter.components.base.AryaMain;
import tr.com.agem.arya.interpreter.components.base.AryaNavBar;
import tr.com.agem.arya.interpreter.script.ElementFunctions;
import tr.com.agem.arya.interpreter.script.ScriptHelper;
import tr.com.agem.core.interpreter.IAryaComponent;
import tr.com.agem.core.utils.AryaUtils;

public class AryaListCell extends TextView implements IAryaComponent {

    private String componentId;
    private String componentValue;
    private String componentClassName;
    private String componentAttribute;
    private String database;
    private String attribute;
    private String attributeValue;
    private String attributeLabel;
    private String type;

    public AryaListCell(Attributes attributes, AryaMain main, final String type) {
        super(main.getAryaWindow().getContext());

        if (AryaUtils.isNotEmpty(attributes)) {
            this.type = type;
            this.componentId = attributes.getValue("id");
            this.componentClassName = attributes.getValue("class");
            this.componentValue = attributes.getValue("value");
            this.componentAttribute = attributes.getValue("attribute");
            this.database = attributes.getValue("database");

            this.attribute = attributes.getValue("attribute");
            this.attributeValue = attributes.getValue("attributeValue");
            this.attributeLabel = attributes.getValue("attributeLabel");


            this.setText(attributes.getValue("label"));
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!("listheader".equalsIgnoreCase(type))) {
                        detailView(new AlertDialog.Builder(AryaNavBar.context));
                        final String onSelect = ((AryaListBox) getParent().getParent()).getOnSelect();

                        v.setDrawingCacheBackgroundColor(Color.LTGRAY);

                        if ((((AryaListItem) v.getParent()).getComponentValue()) != null && onSelect != null) {
                            JSONObject j = null;
                            try {
                                j = new JSONObject(((AryaListItem) v.getParent()).getComponentValue());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ElementFunctions.setJsonObj(j);
                            ScriptHelper.executeScript(onSelect, null, ((AryaListItem) getParent()).getMain());
                        }
                    }

                }

            });

            // this.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            //this.setTextSize(15, TypedValue.COMPLEX_UNIT_DIP);
        }

        if ("listheader".equalsIgnoreCase(type)) {
            this.setTextColor(Color.BLACK);
            this.setBackgroundColor(Color.LTGRAY);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        Paint paint = new Paint();

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        if (!getText().equals("")) {
            paint.setStrokeWidth(2);
            getLocalVisibleRect(rect);
            canvas.drawLine(0, 0, rect.width(), 0, paint);
        } else {
            setVisibility(INVISIBLE);
            paint.setStrokeWidth(2);
        }


        // canvas.drawRect(rect, paint);
    }

    @Override
    public void setComponentParent(Object o) {

        AryaListItem li = (AryaListItem) o;
        li.addView(this);
        AryaListItem parent = (AryaListItem) this.getParent();
// Arguments here: width, height, weight
        if ("listheader".equalsIgnoreCase(type)) {
            ((AryaListBox) li.getParent()).getSpinnerItems().add(getText().toString());
            ((AryaListBox) li.getParent()).getAdapter().notifyDataSetChanged();


        }

        AryaListItem item = getListHead(li);
        if (li != null && li.getChildCount() - 1 == item.getMasterCol()) { //If current cell is element of master column

            WindowManager wm = (WindowManager) AryaNavBar.context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            this.setLayoutParams(new TableRow.LayoutParams(width - 100, TableRow.LayoutParams.WRAP_CONTENT, 1f)); //MATCH_PARENT is not working here, thats why I used pixels
        } else {
            this.setLayoutParams(new TableRow.LayoutParams(0, 0, 0f));
        }
    }

    public static AryaListItem getListHead(AryaListItem li) {
        AryaListBox listbox = (AryaListBox) li.getParent();
        for (int i = 0; i < listbox.getChildCount(); i++) {
            if (listbox.getChildAt(i) instanceof AryaListItem)
                return (AryaListItem) listbox.getChildAt(i);
        }
        return null;
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
        return "listcell";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private void detailView(AlertDialog.Builder alertDialog) {
        AryaListItem parent = (AryaListItem) this.getParent();
        alertDialog.setTitle("Bilgiler");
        //alertDialog.setMessage("");
        TableLayout tbl = new TableLayout(AryaNavBar.context);

        for (int i = 0; i < parent.getChildCount(); i++) {

            TableRow tr = new TableRow(AryaNavBar.context);
            final TextView columnName = new TextView(AryaNavBar.context);
            columnName.setText(((TextView) getListHead(parent).getChildAt(i)).getText() + ":\t");

            final TextView info = new TextView(AryaNavBar.context);
            info.setText(((TextView) parent.getChildAt(i)).getText());
            final float scale = getContext().getResources().getDisplayMetrics().density;
            int pixels = (int) (200 * scale + 0.5f);
            info.setWidth(pixels);

            TableLayout.LayoutParams tableRowParams =
                    new TableLayout.LayoutParams
                            (TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1.0f);



            tr.setLayoutParams(tableRowParams);

            tr.addView(columnName);
            tr.addView(info);


            tbl.addView(tr);
            View v = new View(AryaNavBar.context);
            v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
            v.setBackgroundColor(Color.rgb(51, 51, 51));
            tbl.addView(v);

        }
        alertDialog.setView(tbl);


        alertDialog.setPositiveButton("Geri",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.setIcon(R.drawable.info);
        //alertDialog.setIconAttribute(android.R.attr.alertDialogIcon);
        alertDialog.show();

    }
}
