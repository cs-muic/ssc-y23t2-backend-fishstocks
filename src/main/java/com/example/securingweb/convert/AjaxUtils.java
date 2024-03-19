package com.example.securingweb.convert;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

public class AjaxUtils {
    public static String converttoString(Object object){
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        try{
            mapper.writeValue(writer, object);
            return writer.toString();
        } catch (IOException e) {
            return  "bad object conversion";
        }
    }
}
