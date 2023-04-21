package com.mysql.jdbc.test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCSelectTest {
  public static void main(String[] args) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    List<Article> articles = new ArrayList<>();

    try{
      Class.forName("com.mysql.jdbc.Driver");

      String url = "jdbc:mysql://127.0.0.1:3306/text_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

      conn = DriverManager.getConnection(url, "sbsst", "sbs123414");

      String sql = "SELECT *";
      sql += " FROM article";
      sql += " ORDER BY id DESC";

      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery(sql);

      while (rs.next()) {
        int id = rs.getInt("id");

        String regDate = rs.getString("regDate");
        String updateDate = rs.getString("updateDate");
        String title = rs.getString("title");
        String body = rs.getString("body");

        Article article = new Article(id, regDate, updateDate, title, body);
        articles.add(article);
      }

      System.out.println("결과 : " + articles);

    }
    catch(ClassNotFoundException e){
      System.out.println("드라이버 로딩 실패");
    }
    catch(SQLException e){
      System.out.println("에러: " + e);
    }
    finally{
      try{
        if( rs != null && !rs.isClosed()){
          rs.close();
        }
      }
      catch( SQLException e){
        e.printStackTrace();
      }
      try{
        if( pstmt != null && !pstmt.isClosed()){
          pstmt.close();
        }
      }
      catch( SQLException e){
        e.printStackTrace();
      }
      try{
        if( conn != null && !conn.isClosed()){
          conn.close();
        }
      }
      catch( SQLException e){
        e.printStackTrace();
      }
    }
  }
}
