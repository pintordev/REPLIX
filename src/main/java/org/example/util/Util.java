package org.example.util;

import java.text.SimpleDateFormat;
import java.util.*;

public class Util {

  public static String f(String format, Object... args) {
    return String.format(format, args);
  }

  public static Map<String, Object> mapOf(Object... args) {
    if (args.length % 2 != 0) {
      throw new IllegalArgumentException("인자를 짝수개 입력해주세요.");
    }

    int size = args.length / 2;

    Map<String, Object> map = new LinkedHashMap<>();

    for (int i = 0; i < size; i++) {
      int keyIndex = i * 2;
      int valueIndex = keyIndex + 1;

      String key;
      Object value;

      try {
        key = (String) args[keyIndex];
      } catch (ClassCastException e) {
        throw new IllegalArgumentException("키는 String으로 입력해야 합니다. " + e.getMessage());
      }

      value = args[valueIndex];

      map.put(key, value);
    }

    return map;
  }

  public static String getNowDateStr() {
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String dateStr = format1.format(System.currentTimeMillis());

    return dateStr;
  }

  public static Map<String, String> getUrlParamsFromUrl(String url) {
    Map<String, String> params = new HashMap<>();
    String[] urlBits = url.split("\\?", 2);

    if (urlBits.length == 1) {
      return params;
    }

    for (String bit : urlBits[1].split("&")) {
      String[] bits = bit.split("=", 2);

      if (bits.length == 1) {
        continue;
      }

      params.put(bits[0], bits[1]);
    }

    return params;
  }

  public static String getUrlPathFromUrl(String url) {
    return url.split("\\?", 2)[0];
  }

  public static <T> List<T> reverseList(List<T> list) {
    List<T> reverse = new ArrayList<>(list.size());

    for (int i = list.size() - 1; i >= 0; i--) {
      reverse.add(list.get(i));
    }
    return reverse;
  }

  public static int getRandomInt(int start, int end) {
    int size = end - start + 1;
    return start + (int) (Math.random() * size);
  }
}