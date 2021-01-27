/**
 * 
 */
package com.example.AZ_Enterprise.Utility;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Samuel Columbus Jan 20, 2021
 */
public class JSONParser {
  // JSON to Map object
  @SuppressWarnings("unchecked")
  public static Map<String, String> JSONStringtoMap(String json) {
    ObjectMapper mapper = new ObjectMapper();
    Map<String, String> map_obj = new HashMap<>();
    try {
      // convert JSON string to Map
      map_obj = mapper.readValue(json, Map.class);
      // Map<String, String> map = mapper.readValue(json, new TypeReference<Map<String, String>>()
      // {});
    } catch (IOException e) {
      e.printStackTrace();
    }
    return map_obj;
  }

  public static String MaptoJSONString(Map<String, String> map_obj) {
    ObjectMapper mapper = new ObjectMapper();
    String json = null;
    try {
      // convert map to JSON string
      json = mapper.writeValueAsString(map_obj);
      // System.out.println(json); // compact-print
      json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map_obj);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return json;
  }

  public static String Object_to_JSONString(Object obj) {
    ObjectMapper mapper = new ObjectMapper();
    String json = null;
    try {
      json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return json;
  }

  public static String prettyPrint(JSONObject jo, String pre) {
    try {
      Iterator<?> keys = jo.keys();
      StringBuilder pretty = new StringBuilder();
      while (keys.hasNext()) {
        String key = (String) keys.next();
        pretty.append(String.format("%s%s: ", pre, StringUtils.capitalize(key)));
        if (jo.get(key) instanceof JSONObject) {
          pretty.append(prettyPrint(jo.getJSONObject(key), pre + "\t"));
        } else if (jo.get(key) instanceof JSONArray) {
          int sno = 1;
          org.json.JSONArray JSON_array = jo.getJSONArray(key);
          for (int i = 0; i < JSON_array.length(); i++) {
            pretty.append(String.format("\n%s\t%d:\n", pre, sno++));
            pretty.append(prettyPrint((JSONObject) JSON_array.getJSONObject(i), pre + "\t\t"));
          }
        } else {
          pretty.append(String.format("%s\n", jo.getString(key)));
        }
      }
      return pretty.toString();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return null;
  }
}
