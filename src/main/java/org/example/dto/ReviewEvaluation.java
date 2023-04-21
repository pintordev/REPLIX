package org.example.dto;

import lombok.Data;

import java.util.Map;

@Data // @Data annotation으로 equals, toString, getter, setter 메서드 선언 없이 사용 가능
public class ReviewEvaluation {
    private int id; // 리뷰 평가 고유 ID
    private boolean likeDislike; // 리뷰에 대한 호오 여부
    private String regDate; // 리뷰 평가 작성 시간
    private String updateDate; // 리뷰 평가 수정 시간
    private int userId; // 리뷰 평가 작성한 서비스 사용자 고유 ID
    private int reviewId; // 리뷰 평가가 작성된 리뷰 고유 ID

    public ReviewEvaluation(Map<String, Object> reviewEvaluationMap) {
        this.id = (int) reviewEvaluationMap.get("id");
        this.likeDislike = (boolean) reviewEvaluationMap.get("likeDislike");
        this.regDate = (String) reviewEvaluationMap.get("regDate");
        this.updateDate = (String) reviewEvaluationMap.get("updateDate");
        this.userId = (int) reviewEvaluationMap.get("userId");
        this.reviewId = (int) reviewEvaluationMap.get("reviewId");
    }
}
