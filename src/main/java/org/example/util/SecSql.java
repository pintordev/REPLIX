package org.example.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SecSql {
  private StringBuilder sqlBuilder;
  private List<Object> datas;

  @Override
  public String toString() {
    return "sql=" + getFormat() + ", data=" + datas;
  }

  public SecSql() {
    sqlBuilder = new StringBuilder();
    datas = new ArrayList<>();
  }

  public boolean isInsert() {
    return getFormat().startsWith("INSERT");
  }

  public SecSql append(Object... args) {
    // sql.append(String, ...) 와 같이 들어오니까 첫번째는 일단 추가할 sql 문
    if (args.length > 0) { // 값이 있으면
      String sqlBit = (String) args[0];
      sqlBuilder.append(sqlBit + " "); // 첫번째 들어온 매개변수는 sql 문이니까 이걸 반영
    }

    for (int i = 1; i < args.length; i++) {
      datas.add(args[i]); // 여기는 ? 를 대체할 변수들이 담기는 곳
    }

    return this;
  }

  public PreparedStatement getPreparedStatement(Connection dbConn) throws SQLException {
    PreparedStatement stmt = null;

    if (isInsert()) {
      stmt = dbConn.prepareStatement(getFormat(), Statement.RETURN_GENERATED_KEYS);
    } else {
      stmt = dbConn.prepareStatement(getFormat());
    }

    for (int i = 0; i < datas.size(); i++) {
      Object data = datas.get(i);
      int parameterIndex = i + 1;

      if (data instanceof Integer) {
        stmt.setInt(parameterIndex, (int) data);
      } else if (data instanceof String) {
        stmt.setString(parameterIndex, (String) data);
      }
    }

    return stmt;
  }

  public String getFormat() {
    return sqlBuilder.toString();
  }

  public static SecSql from(String sql) {
    return new SecSql().append(sql);
  }
}
