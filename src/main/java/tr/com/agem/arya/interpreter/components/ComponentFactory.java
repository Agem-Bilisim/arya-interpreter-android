package tr.com.agem.arya.interpreter.components;


import org.xml.sax.Attributes;

import tr.com.agem.arya.interpreter.components.base.AryaMain;
import tr.com.agem.arya.interpreter.components.command.AryaFill;
import tr.com.agem.arya.interpreter.components.menu.AryaMenu;
import tr.com.agem.arya.interpreter.components.menu.AryaMenuBar;
import tr.com.agem.arya.interpreter.components.menu.AryaMenuItem;
import tr.com.agem.arya.interpreter.components.menu.AryaPopupMenu;
import tr.com.agem.core.interpreter.IAryaComponent;

public class ComponentFactory {

    public static IAryaComponent getComponent(String tagName,AryaMain main,Attributes attributes) {

        IAryaComponent comp = null;

        if ("label".equalsIgnoreCase(tagName)) {
            comp = new AryaLabel(attributes, main);
        } else if (("textbox").equalsIgnoreCase(tagName)|| "intbox".equalsIgnoreCase(tagName)||"doublebox".equalsIgnoreCase(tagName)||"longbox".equalsIgnoreCase(tagName)||"decimalbox".equalsIgnoreCase(tagName)||"timebox".equalsIgnoreCase(tagName)) {
            comp = new AryaTextbox(attributes, main,tagName);
        }else if ("checkbox".equalsIgnoreCase(tagName)) {
            comp = new AryaCheckbox(attributes, main);
        } else if ("datebox".equalsIgnoreCase(tagName)) {
            comp = new AryaDatebox(attributes, main);
        } else if ("button".equalsIgnoreCase(tagName)) {
            comp = new AryaButton(attributes, main);
        } else if ("combobox".equalsIgnoreCase(tagName)) {
            comp = new AryaComboBox(attributes, main);
        }else if ("comboitem".equalsIgnoreCase(tagName)) {
            comp = new AryaComboItem(attributes, main);
        }else if ("multiplecombobox".equalsIgnoreCase(tagName)) {
            comp = new AryaMultipleComboBox(attributes, main);
        }else if ("mcomboitem".equalsIgnoreCase(tagName)) {
            comp = new AryaMultipleComboItem(attributes, main);
        }else if ("listbox".equalsIgnoreCase(tagName) || "grid".equalsIgnoreCase(tagName)) {
            comp = new AryaListBox(attributes, main);
        }else if ("listheader".equalsIgnoreCase(tagName)||"listcell".equalsIgnoreCase(tagName)) {
            comp = new AryaListCell(attributes, main,tagName);
        }else if ("listitem".equalsIgnoreCase(tagName) || "listhead".equalsIgnoreCase(tagName) || "row".equalsIgnoreCase(tagName)) {
            comp = new AryaListItem(attributes, main);
        }else if ("script".equalsIgnoreCase(tagName)) {
            comp = new AryaScript(attributes,main);
        } else if ("menubar".equalsIgnoreCase(tagName)) {
            comp = new AryaMenuBar(attributes, main);
        }else if ("menu".equalsIgnoreCase(tagName)) {
            comp = new AryaMenu(attributes,main);
        }else if ("menupopup".equalsIgnoreCase(tagName)) {
            comp = new AryaPopupMenu(attributes,main);
        }else if ("menuitem".equalsIgnoreCase(tagName)) {
            comp = new AryaMenuItem(attributes,main);
        }else if ("progressmeter".equalsIgnoreCase(tagName)) {
            comp = new AryaProgressmeter(attributes,main);
        }else if ("calendar".equalsIgnoreCase(tagName)) {
            comp = new AryaCalendar(attributes,main);
        }else if ("radio".equalsIgnoreCase(tagName)) {
            comp = new AryaRadio(attributes,main);
        }else if ("radiogroup".equalsIgnoreCase(tagName)) {
            comp = new AryaRadiogroup(attributes,main);
        }else if ("slider".equalsIgnoreCase(tagName)) {
            comp = new AryaSlider(attributes, main);
        }else if ("template".equalsIgnoreCase(tagName)) {
            comp = new AryaTemplate(attributes,main);
        }else if ("fill".equalsIgnoreCase(tagName)) {
            comp = new AryaFill(attributes,main);
        }else if ("chart".equalsIgnoreCase(tagName)) {
            comp = new AryaChart(attributes,main);
        }

        return comp;
    }

}