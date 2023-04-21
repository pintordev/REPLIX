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
    private String contentGenre; // 컨텐츠 장르
    private String serviceOtt; // 컨텐츠 OTT
    // 장르, OTT 등은 어떻게 할지에 대한 해결 필요.. ArrayList?

    public Content(Map<String, Object> contentMap) {
        this.id = (int) contentMap.get("id");
        this.name = (String) contentMap.get("name");
        this.releaseDate = (String) contentMap.get("releaseDate");
        this.productionCompany = (String) contentMap.get("productionCompany");
        this.director = (String) contentMap.get("director");
        this.plot = (String) contentMap.get("plot");
        this.contentGenre = (String) contentMap.get("contentGenre");
        this.serviceOtt = (String) contentMap.get("serviceOtt");
    }
}