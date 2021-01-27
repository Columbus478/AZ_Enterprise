/**
 * 
 */
package com.example.AZ_Enterprise.Utility;

import java.util.Random;

/**
 * @author Samuel Columbus Jan 27, 2021
 */
public class CharacterGenerator {

  public static String charGenerator() {
    String chars = "ABC";
    Random rand = new Random();
    char c = chars.charAt(rand.nextInt(chars.length()));
    return Character.toString(c);
  }
}
