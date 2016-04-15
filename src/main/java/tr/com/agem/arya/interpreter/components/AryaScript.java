package tr.com.agem.arya.interpreter.components;

import android.view.View;

import org.xml.sax.Attributes;
import java.util.Arrays;
import java.util.List;

import tr.com.agem.arya.interpreter.components.base.AryaMain;
import tr.com.agem.core.interpreter.IAryaComponent;
import tr.com.agem.core.utils.AryaUtils;

public class AryaScript extends View implements IAryaComponent{

    private String script;
    private List<String> srcList;

    public AryaScript(Attributes attributes, AryaMain main) {
       super(main.getAryaWindow().getContext());

        if(AryaUtils.isNotEmpty(attributes)){
            this.srcList=parseSrc(attributes.getValue("src"));
        }

        main.getAryaWindow().addView(this);
    }

    private List<String> parseSrc(String src) {
        srcList=null;
        if(AryaUtils.isNotEmpty(src))
            srcList= Arrays.asList(src.split(";"));
        return srcList;
    }

    public String getScript() {return script;}

    public void setScript(String script) {this.script = script;}

    public List<String> getSrcList() {
        return srcList;
    }

    public void setSrcList(List<String> srcList) {
        this.srcList = srcList;
    }

    @Override
    public void setComponentId(String s) {

    }

    @Override
    public String getComponentId() {
        return "aryaScript";
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
    public void setComponentParent(Object o) {

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
        return "script";
    }

}
