package tr.com.agem.arya.interpreter.components.menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;

import tr.com.agem.arya.MainActivity;
import tr.com.agem.arya.interpreter.components.base.AryaMain;
import tr.com.agem.arya.interpreter.components.base.AryaNavBar;
import tr.com.agem.arya.interpreter.parser.IAryaMenu;
import tr.com.agem.arya.interpreter.script.ScriptHelper;
import tr.com.agem.core.interpreter.IAryaComponent;


public class AryaPopupMenu implements IAryaComponent,IAryaMenu, MenuItem {//TODO

    private String label;

    private OnMenuItemClickListener onMenuItemClickListener;
    public List<String> choice = new ArrayList<String>();
    public ArrayList<AryaMenuItem> menuItems = new ArrayList<>();
    int selectedItem=0;
    public AryaPopupMenu(Attributes attributes, final AryaMain main) {

        this.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(AryaNavBar.context);
                builder.setTitle("Alt Menu Seciniz");

                builder.setSingleChoiceItems(
                        choice.toArray(new String[choice.size()]),
                        0,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedItem = which;
                                //menuItems.get(selectedItem).getOnMenuItemClickListener();
                                ScriptHelper.executeScript( menuItems.get(selectedItem).getOnClick(), null, main);


                                dialog.dismiss();
                            }
                        });
                MainActivity.setAlertDialog(builder.create());
                MainActivity.getAlertDialog().show();
                return false;
            }
        });
    }


    @Override
    public OnMenuItemClickListener getOnMenuItemClickListener() {
        return onMenuItemClickListener;
    }


    @Override
    public void setComponentParent(Object o) {
        if(o instanceof AryaMenuBar){
            AryaMenuBar menu = (AryaMenuBar) o;
            menu.addItem(this);

        }
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }



    @Override
    public int getItemId() {
        return 0;
    }

    @Override
    public int getGroupId() {
        return 0;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public MenuItem setTitle(CharSequence charSequence) {
        return null;
    }

    @Override
    public MenuItem setTitle(int i) {
        return null;
    }

    @Override
    public CharSequence getTitle() {
        return null;
    }

    @Override
    public MenuItem setTitleCondensed(CharSequence charSequence) {
        return null;
    }

    @Override
    public CharSequence getTitleCondensed() {
        return null;
    }

    @Override
    public MenuItem setIcon(Drawable drawable) {
        return null;
    }

    @Override
    public MenuItem setIcon(int i) {
        return null;
    }

    @Override
    public Drawable getIcon() {
        return null;
    }

    @Override
    public MenuItem setIntent(Intent intent) {
        return null;
    }

    @Override
    public Intent getIntent() {
        return null;
    }

    @Override
    public MenuItem setShortcut(char c, char c1) {
        return null;
    }

    @Override
    public MenuItem setNumericShortcut(char c) {
        return null;
    }

    @Override
    public char getNumericShortcut() {
        return 0;
    }

    @Override
    public MenuItem setAlphabeticShortcut(char c) {
        return null;
    }

    @Override
    public char getAlphabeticShortcut() {
        return 0;
    }

    @Override
    public MenuItem setCheckable(boolean b) {
        return null;
    }

    @Override
    public boolean isCheckable() {
        return false;
    }

    @Override
    public MenuItem setChecked(boolean b) {
        return null;
    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public MenuItem setVisible(boolean b) {
        return null;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public MenuItem setEnabled(boolean b) {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean hasSubMenu() {
        return false;
    }

    @Override
    public SubMenu getSubMenu() {
        return null;
    }

    @Override
    public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener=onMenuItemClickListener;
        return null;
    }

    @Override
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return null;
    }

    @Override
    public void setShowAsAction(int i) {

    }

    @Override
    public MenuItem setShowAsActionFlags(int i) {
        return null;
    }

    @Override
    public MenuItem setActionView(View view) {
        return null;
    }

    @Override
    public MenuItem setActionView(int i) {
        return null;
    }

    @Override
    public View getActionView() {
        return null;
    }

    @Override
    public MenuItem setActionProvider(ActionProvider actionProvider) {
        return null;
    }

    @Override
    public ActionProvider getActionProvider() {
        return null;
    }

    @Override
    public boolean expandActionView() {
        return false;
    }

    @Override
    public boolean collapseActionView() {
        return false;
    }

    @Override
    public boolean isActionViewExpanded() {
        return false;
    }

    @Override
    public MenuItem setOnActionExpandListener(OnActionExpandListener onActionExpandListener) {
        return null;
    }

    @Override
    public void setComponentId(String s) {

    }

    @Override
    public String getComponentId() {
        return null;
    }

    @Override
    public void setComponentClassName(String s) {

    }

    @Override
    public String getComponentClassName() {
        return null;
    }

    @Override
    public void setComponentValue(String s) {

    }

    @Override
    public String getComponentValue() {
        return null;
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
        return "menuitem";
    }


}