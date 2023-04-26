package org.example.service;

import org.example.Container;
import org.example.dto.Review;
import org.example.repository.ReviewRepository;
import org.example.util.DBUtil;
import org.example.util.SecSql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReviewService {
    private ReviewRepository reviewRepository;

    public ReviewService() {
        this.reviewRepository = new ReviewRepository();
    }

    public void post(String comment, String score, int replayFlag) {
        this.reviewRepository.post(comment, score, replayFlag);
    }

    public List<Review> reviewByUserIdContentId() {
        List<Map<String, Object>> reviewMapList = this.reviewRepository.reviewByUserIdContentId();
        List<Review> reviewList = new ArrayList<>();
        for (Map<String, Object> reviewMap : reviewMapList) {
            reviewList.add(new Review(reviewMap));
        }
        return reviewList;
    }

    public void deleteById(int deleteId) {
        this.reviewRepository.deleteById(deleteId);
    }
}
