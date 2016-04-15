package tr.com.agem.arya.interpreter.parser;

import android.view.MenuItem;

public interface IAryaMenu {

    String getLabel();
    void setLabel(String label);
    MenuItem.OnMenuItemClickListener getOnMenuItemClickListener();



}
