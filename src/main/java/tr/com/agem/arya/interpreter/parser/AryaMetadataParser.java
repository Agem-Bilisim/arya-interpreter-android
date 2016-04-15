package tr.com.agem.arya.interpreter.parser;

import android.view.View;
import android.view.ViewGroup;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Stack;

import tr.com.agem.arya.MainActivity;
import tr.com.agem.arya.gateway.AryaInterpreterHelper;
import tr.com.agem.arya.interpreter.components.AryaListBox;
import tr.com.agem.arya.interpreter.components.AryaListItem;
import tr.com.agem.arya.interpreter.components.AryaScript;
import tr.com.agem.arya.interpreter.components.AryaTemplate;
import tr.com.agem.arya.interpreter.components.ComponentFactory;
import tr.com.agem.arya.interpreter.components.base.AryaMain;
import tr.com.agem.arya.interpreter.components.command.AryaFill;
import tr.com.agem.arya.interpreter.components.menu.AryaMenu;
import tr.com.agem.arya.interpreter.components.menu.AryaMenuItem;
import tr.com.agem.arya.interpreter.components.menu.AryaPopupMenu;
import tr.com.agem.core.gateway.model.AryaResponse;
import tr.com.agem.core.interpreter.IAryaComponent;
import tr.com.agem.core.interpreter.IAryaTemplate;

public class AryaMetadataParser extends DefaultHandler {

    private AryaMain main=null;
    private Stack<IAryaComponent> currentComponent = null;
    private  IAryaComponent comp;
    public AryaMetadataParser(AryaMain main) {
        this.main = main;
        this.currentComponent = new Stack<>();
    }

    @Override
    public void startElement (String uri, String localName,String tagName, Attributes attributes){

       comp = ComponentFactory.getComponent(tagName,main, attributes);

        if (comp != null && !(comp instanceof AryaFill)) {
            if (comp instanceof AryaTemplate) {
                if (currentComponent.peek() instanceof AryaListBox) {
                    ((AryaListBox)currentComponent.peek()).setAryaTemplate(comp);
                    ((AryaListBox)currentComponent.peek()).getListBoxSpinner().setVisibility(View.VISIBLE);
                    ((AryaListBox)currentComponent.peek()).getLinLayout().setVisibility(View.VISIBLE);
                    ((AryaListBox)currentComponent.peek()).getLabel().setVisibility(View.VISIBLE);
                }
                currentComponent.push(comp);
            }
            else if(currentComponent.size() > 0 ){
                if((currentComponent.peek() instanceof AryaTemplate)) {
                    AryaTemplate template = (AryaTemplate) currentComponent.peek();
                    template.getChildren().add(comp);
                    currentComponent.push(template);
                }
                else if((!(comp instanceof IAryaMenu)) && (!(comp instanceof IAryaTemplate))){
                    if( ((View)comp).getParent()!=null) { //have to check whether it is instanceof AryaMenuItem or not because MenuItems cannot be casted to View
                        ((ViewGroup) ((View) comp).getParent()).removeView((View) comp);
                    }
                    comp.setComponentParent(currentComponent.peek());

                } else{
                    if(comp instanceof AryaPopupMenu){
                        ((AryaPopupMenu) comp).setLabel(((AryaMenu)currentComponent.peek()).getLabel());
                        main.getAryaNavBar().getMenuBar().getMenuItems().remove(main.getAryaNavBar().getMenuBar().getMenuItems().size()-1); //Remove Menu if it has a popupchild, just delegate it's name to popup menu
                        comp.setComponentParent(main.getAryaNavBar().getMenuBar());
                    }
                    else if(comp instanceof AryaMenuItem){
                        if(currentComponent.peek() instanceof AryaPopupMenu){
                            ((AryaPopupMenu)currentComponent.peek()).choice.add(((AryaMenuItem) comp).getLabel());
                            ((AryaPopupMenu)currentComponent.peek()).menuItems.add(((AryaMenuItem) comp));
                        }
                        else {
                            comp.setComponentParent(main.getAryaNavBar().getMenuBar());
                        }
                    }
                    else if(comp instanceof AryaMenu){
                        if(((AryaMenu) comp).getLabel()!=null){
                            comp.setComponentParent(main.getAryaNavBar().getMenuBar());
                        }
                    }
                }
            }
            else {      //this part assigns MenuBar's parent.
                if (comp instanceof IAryaMenu) {
                    comp.setComponentParent(main.getAryaNavBar());
                }
            }

            if (!(comp instanceof AryaTemplate)) {

                if(!((currentComponent.size() > 0) && (currentComponent.peek() instanceof AryaTemplate))) {

                    if(!(currentComponent.isEmpty()) && (currentComponent.peek() instanceof AryaListBox) && !(comp instanceof AryaListItem)){
                        //not to get wrong components after listbox
                    }
                    else {
                        currentComponent.push(comp);
                    }
                    if (main.getAryaWindow().getComponents() == null) {
                        main.getAryaWindow().setComponents(new ArrayList<IAryaComponent>());
                    }
                    main.getAryaWindow().getComponents().add(comp);
                }
            }
        }
        else if(comp != null && (comp instanceof AryaFill)) {

            StringBuilder request = new StringBuilder("{ \"params\": ")
                    .append("{\"json\":\"1\"}")
                    .append(", \"requestType\": \"")
                    .append("DATA_ONLY")
                    .append("\", \"action\": \"")
                    .append(((AryaFill)comp).getFrom())
                    .append("\" }");

            String result = AryaInterpreterHelper.callUrl("http://"+ MainActivity.inetAddr+":8080/arya/rest/asya", request.toString());

            AryaResponse response = new AryaResponse();
            response.fromXMLString(result);

            AryaInterpreterHelper.populateToFill(response.getData(), comp, main);

        }
    }

    @Override
    public void endElement (String uri, String localName, String qName){

        if (comp!=null && !currentComponent.isEmpty() && isSame(currentComponent.peek().getComponentTagName(), localName)) {
                currentComponent.pop();
            }
    }

    public boolean isSame(String tagName, String localName){
        if(tagName.equals("textbox")){
            if(localName.equals("textbox") || localName.equals("intbox") || localName.equals("doublebox")
                    || localName.equals("longbox") || localName.equals("decimalbox") || localName.equals("timebox")) {
                return true;
            }
        }
        else if(tagName.equals("listbox")){
            if(localName.equals("listbox") || localName.equals("grid"))
                return true;
        }
        else if(tagName.equals("listcell")){
            if(localName.equals("listcell") || localName.equals("listheader"))
                return true;
        }
        else if(tagName.equals("listitem")){
            if(localName.equals("listitem") || localName.equals("row") || localName.equals("listhead"))
                return true;
        }
        else if(tagName.equals("vlayout")){
            if(localName.equals("vlayout") || localName.equals("hlayout"))
                return true;
        }
        else {
            return true;
        }

        return false;
    }


    @Override
    public void characters (char ch[], int start, int length){
        if (!currentComponent.isEmpty() && currentComponent.peek() instanceof AryaScript) {

            String scriptPart = new String(ch, start, length);
            AryaScript scriptObj = (AryaScript) currentComponent.peek();

            scriptObj.setScript(scriptObj.getScript() != null ? scriptObj.getScript() + scriptPart : scriptPart);

        }
    }

}
