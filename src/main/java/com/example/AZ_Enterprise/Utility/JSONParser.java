/**
 * 
 */
package com.example.AZ_Enterprise.Utility;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
}
