package org.example.dto;

import lombok.Data;

import java.util.Map;

@Data // @Data annotation으로 equals, toString, getter, setter 메서드 선언 없이 사용 가능
public class Review {
    private int id; // 리뷰 고유 ID
    private String comment; // 리뷰 댓글
    private int score; // 리뷰 별점
    private boolean replayFlag; // 리뷰 재관람 의사
    private String regDate; // 리뷰 작성 시간
    private String updateDate; // 리뷰 수정 시간
    private int userId; // 리뷰 작성한 서비스 사용자 고유 ID
    private int contentId; // 리뷰가 작성된 컨텐츠 고유 ID

    public Review(Map<String, Object> reviewMap) {
        this.id = (int) reviewMap.get("id");
        this.comment = (String) reviewMap.get("comment");
        this.score = (int) reviewMap.get("score");
        this.replayFlag = (boolean) reviewMap.get("replayFlag");
        this.regDate = (String) reviewMap.get("regDate");
        this.updateDate = (String) reviewMap.get("updateDate");
        this.userId = (int) reviewMap.get("userId");
        this.contentId = (int) reviewMap.get("contentId");
    }
}
