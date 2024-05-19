package org.silver.utils;

import org.silver.exceptions.GenericException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HeaderUtils {

    private HeaderUtils(){}

    private static final Map<String, String> headers = new HashMap<>();


    public static void saveHeader(String key, String value){
        headers.put(key, value);
    }

    public static String getHeader(String key){
        if (headers.containsKey(key)){
            return headers.get(key);
        } else throw new GenericException("No se especificó un userId en los Headers de la petición");
    }

    public static void deleteHeader(){
        headers.clear();
    }

}
