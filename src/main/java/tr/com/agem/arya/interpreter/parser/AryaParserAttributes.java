package tr.com.agem.arya.interpreter.parser;

import org.xml.sax.Attributes;

import java.util.HashMap;
import java.util.Map;

import tr.com.agem.core.utils.AryaUtils;

public class AryaParserAttributes implements Attributes {

    Map<String,String> attributesMap = new HashMap<>();


    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public String getURI(int index) {
        return null;
    }

    @Override
    public String getLocalName(int index) {
        return null;
    }

    @Override
    public String getQName(int index) {
        return null;
    }

    @Override
    public String getType(int index) {
        return null;
    }

    @Override
    public String getValue(int index) {
        return null;
    }

    @Override
    public int getIndex(String uri, String localName) {
        return 0;
    }

    @Override
    public int getIndex(String qName) {
        return 0;
    }

    @Override
    public String getType(String uri, String localName) {
        return null;
    }

    @Override
    public String getType(String qName) {
        return null;
    }

    @Override
    public String getValue(String uri, String localName) {
        return null;
    }

    @Override
    public String getValue(String qName) {
        return attributesMap.get(qName);
    }


    public void setValue(String key,String value){
        if(AryaUtils.isNotEmpty(attributesMap))
            attributesMap.put(key,value);
    }
}
