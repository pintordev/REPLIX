package org.example.exception;

// 여기는 일단 라이브러리라고 생각하고 넘기자
public class SQLErrorException extends RuntimeException {
  private Exception origin;

  public SQLErrorException(String message, Exception origin) {
    super(message);
    this.origin = origin;
  }

  public Exception getOrigin() {
    return origin;
  }
}
