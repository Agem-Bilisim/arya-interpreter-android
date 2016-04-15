package tr.com.agem.arya.interpreter.components.menu;

import android.view.MenuItem;

import org.xml.sax.Attributes;

import java.util.ArrayList;

import tr.com.agem.arya.interpreter.components.base.AryaMain;
import tr.com.agem.arya.interpreter.components.base.AryaNavBar;
import tr.com.agem.arya.interpreter.parser.IAryaMenu;
import tr.com.agem.core.interpreter.IAryaComponent;
import tr.com.agem.core.utils.AryaUtils;


public class AryaMenuBar implements IAryaMenu,IAryaComponent {

    private ArrayList<IAryaMenu> menuItems;
    private ArrayList<IAryaMenu> menuItems_copy;
    public AryaMenuBar(Attributes attributes, AryaMain main) {

        menuItems =new ArrayList<>();
        menuItems_copy = new ArrayList<>();
    }


    public ArrayList<IAryaMenu> getMenuItems() {
        return menuItems;
    }
    public ArrayList<IAryaMenu> getMenuItemsCopy() {
        return menuItems_copy;
    }



    public void setMenuItems(ArrayList<IAryaMenu> menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public void setComponentParent(Object o) {

        if(AryaUtils.isNotEmpty(o)){
            if(o instanceof AryaNavBar){
                AryaNavBar navBar = (AryaNavBar) o;
                navBar.setMenuBar(this);
            }
        }
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public void setLabel(String label) {
    }

    @Override
    public MenuItem.OnMenuItemClickListener getOnMenuItemClickListener() {
        return null;
    }

    public void addItem(IAryaMenu menuItem) {
        menuItems.add(menuItem);
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
        return "menubar";
    }


}