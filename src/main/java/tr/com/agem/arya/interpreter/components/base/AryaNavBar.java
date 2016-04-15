package tr.com.agem.arya.interpreter.components.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.ArrayList;

import tr.com.agem.arya.MainActivity;
import tr.com.agem.arya.interpreter.components.menu.AryaMenuBar;
import tr.com.agem.arya.interpreter.parser.IAryaMenu;
import tr.com.agem.core.utils.AryaUtils;

public class AryaNavBar extends ActionBarActivity {


    private AryaMenuBar menuBar = null;

    ActionBar actionBar;
    public static Context context;

    public AryaNavBar(MainActivity mainActivity, LinearLayout mainLayout) {
        context=mainLayout.getContext();
        actionBar = ((ActionBarActivity) mainLayout.getContext()).getSupportActionBar();

    }

    public Menu fillMenuOptions(Menu menu) {

        ArrayList<IAryaMenu> menuItemList = null;

        if(AryaUtils.isNotEmpty(menuBar)){
            menuItemList = menuBar.getMenuItems();
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
            actionBar.setTitle("ARYA");
            actionBar.show();
        }


        if(AryaUtils.isNotEmpty(menuItemList)){
            if(menu!=null) {
                menu.clear();
            }
            for (int i=0; i<menuItemList.size();i++){
                IAryaMenu aryaMenuItem = menuItemList.get(i);

                MenuItem
                        item = menu.add(aryaMenuItem.getLabel());
                item.setOnMenuItemClickListener(aryaMenuItem.getOnMenuItemClickListener());
            }
        }
        return menu;
    }

    public AryaMenuBar getMenuBar() {
        return menuBar;
    }


    public void setMenuBar(AryaMenuBar menuBar) {
        this.menuBar = menuBar;
    }
}