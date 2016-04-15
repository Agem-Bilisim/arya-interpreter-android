package tr.com.agem.arya.interpreter.components.menu;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import org.xml.sax.Attributes;

import tr.com.agem.arya.interpreter.components.base.AryaMain;
import tr.com.agem.arya.interpreter.parser.IAryaMenu;
import tr.com.agem.arya.interpreter.script.ScriptHelper;
import tr.com.agem.core.interpreter.IAryaComponent;
import tr.com.agem.core.utils.AryaUtils;

public class AryaMenu implements IAryaComponent,IAryaMenu, MenuItem {//TODO


    private String label;
    private OnMenuItemClickListener onMenuItemClickListener;


    public AryaMenu(Attributes attributes, final AryaMain main) {

        if(AryaUtils.isNotEmpty(attributes)){
            this.label=attributes.getValue("label");

            final String onClick =  attributes.getValue("onClick");

            if(AryaUtils.isNotEmpty(onClick)){
                this.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        ScriptHelper.executeScript(onClick, null,main );

                        return false;
                    }
                });
            }
        }
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