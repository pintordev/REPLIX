package com.mysql.jdbc.test;

class Article {
  public int id;
  public String regDate;
  public String updateDate;
  public String title;
  public String body;

  public Article(int id, String regDate, String updateDate, String title, String body) {
    this.id = id;
    this.regDate = regDate;
    this.updateDate = updateDate;
    this.title = title;
    this.body = body;
  }

  public Article(int id, String title, String body) {
    this.id = id;
    this.title = title;
    this.body = body;
  }

  @Override
  public String toString() {
    return "Article{" +
        "id=" + id +
        ", regDate='" + regDate + '\'' +
        ", updateDate='" + updateDate + '\'' +
        ", title='" + title + '\'' +
        ", body='" + body + '\'' +
        '}';
  }
}