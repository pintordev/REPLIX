package org.example;

import org.example.exception.SQLErrorException;

public class Main {
  public static void main(String[] args) {
    try {
      App.run(); // 프로그램 구동 시작
    }
    catch (SQLErrorException e) { // SQL error 발생 시 에러 내용 출력
      System.out.println(e.getMessage());
      e.getOrigin().printStackTrace();
    }
  }
}