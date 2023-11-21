package com.weareadaptive.auctionhouse;

public class StringUtil {
  private StringUtil() {
  }

  private static final String ALPHANUMERIC_PATTERN = "^[a-zA-Z0-9]+$";

  public static boolean isNullOrEmpty(String theString) {
    return theString == null || theString.isBlank();
  }

  public static boolean isAlphanumeric(final String input) {
    return input.matches(ALPHANUMERIC_PATTERN);
  }
}
