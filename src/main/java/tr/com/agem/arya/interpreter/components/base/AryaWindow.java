package tr.com.agem.arya.interpreter.components.base;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tr.com.agem.arya.interpreter.parser.IAryaMenu;
import tr.com.agem.core.interpreter.IAryaComponent;

public class AryaWindow extends LinearLayout {

    private List<IAryaComponent> components;

    public AryaWindow(Context context, LinearLayout parent) {
        super(context);

        parent.removeAllViews();
        parent.addView(this);
        components = new ArrayList<>();

        this.setOrientation(VERTICAL);

        //TODO setSize part might need to be updated in future because it ignores the component size if component is a direct child of window (if it is not a direct child I fixed this issue on custom arya layouts)-bener
        this.setMinimumWidth(LayoutParams.MATCH_PARENT);
        this.setMinimumHeight(LayoutParams.MATCH_PARENT);

      addAryalogo(context);
    }

    public List<IAryaComponent> getComponents() {
        return components;
    }

    public void setComponents(List<IAryaComponent> components) {
        this.components = components;
    }
    private void addAryalogo(Context context){
        TextView label = new TextView(context);
        label.setText("ARYA");
        label.setTextColor(0xFFFFFFFF);
        label.setTextSize(20);
        label.setTypeface(null, Typeface.BOLD);
        label.setGravity(Gravity.CENTER);
        label.setBackgroundColor(0xFF6485CF);
        label.setHeight(150);
        label.setWidth(400);

        this.addView(label);
    }
    public void clearPageExceptMenu(){
              List<IAryaComponent> newList = new ArrayList<>();
        for(IAryaComponent comp : getComponents()){
            if(comp instanceof IAryaMenu){
                newList.add(comp);
            }
        }
        setComponents(newList);
        this.removeAllViews();
        addAryalogo(getContext());
        for(IAryaComponent comp: getComponents()){
            comp.setComponentParent(this);
        }
    }
}