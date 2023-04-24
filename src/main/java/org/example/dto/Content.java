package org.example.dto;

import lombok.Data;

import java.util.Map;

@Data // @Data annotation으로 equals, toString, getter, setter 메서드 선언 없이 사용 가능
public class Content {
    private int id; // 컨텐츠 고유 ID
    private String name; // 컨텐츠 이름
    private String releaseDate; // 컨텐츠 최초 서비스 시간
    private String productionCompany; // 컨텐츠 제작사
    private String director; // 컨텐츠 제작자
    private String plot; // 컨텐츠 줄거리
    private String genre; // 컨텐츠 장르
    private String ott; // 컨텐츠 OTT
    private String cast; // 컨텐츠 출연진
    private int like;
    private int dibs;
    private int review;
    private double score;

    public Content(Map<String, Object> contentMap) {
        this.id = (int) contentMap.get("id");
        this.name = (String) contentMap.get("name");
        this.releaseDate = (String) contentMap.get("releaseDate");
        this.productionCompany = (String) contentMap.get("productionCompany");
        this.director = (String) contentMap.get("director");
        this.plot = (String) contentMap.get("plot");
        this.genre = (String) contentMap.get("genre");
        this.ott = (String) contentMap.get("ott");
        this.cast = (String) contentMap.get("cast");
        this.like = (int) contentMap.get("like");
        this.dibs = (int) contentMap.get("dibs");
        this.review = (int) contentMap.get("review");
        this.score = (double) contentMap.get("score");
    }
}