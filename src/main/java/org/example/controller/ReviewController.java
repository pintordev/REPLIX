package org.example.controller;

import org.example.Container;
import org.example.dto.Review;
import org.example.util.DBUtil;
import org.example.util.SecSql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewController {
    public void pagedList() {
        if (Container.session.getSessionState() != 3 && Container.session.getSessionState() != 4) return;

        SecSql sql = new SecSql();

        sql.append("SELECT A.`id`, A.`comment`, A.`score`, A.`replayFlag`, A.regDate, A.updateDate, B.`name` AS 'userName', C.`name` AS 'contentName'");
        sql.append("FROM `review` as A");
        sql.append("INNER JOIN `user` as B");
        sql.append("ON A.`userId` = B.`id`");
        sql.append("INNER JOIN `content` as C");
        sql.append("ON A.`contentId` = C.`id`");
        sql.append("WHERE A.`contentId` = ?", Container.session.getSessionContent().getId());
        sql.append("ORDER BY regDate DESC");
        sql.append("LIMIT ?, ?", (Container.session.getSessionReviewPage() - 1)*Container.session.getSessionReviewItemsPerPage(), Container.session.getSessionReviewItemsPerPage());

        List<Map<String, Object>> reviewMapList = DBUtil.selectRows(Container.connection, sql);

        if (reviewMapList.isEmpty()) {
            System.out.printf("  \"%s\" 게시판에 남겨진 리뷰가 없습니다.\n", Container.session.getSessionContent().getName());
            System.out.println("  리뷰를 남겨보세요.");
            return;
        }

        List<Review> reviewList = new ArrayList<>();
        for (Map<String, Object> reviewMap : reviewMapList) {
            reviewList.add(new Review(reviewMap));
        }

        System.out.println("https://replix.io/contents?id=%d&review?page=\n\n");
        Container.systemController.adComment();
        System.out.println("  번호 / 글쓴이 / 내용 / 별점 / 재관람의사 / 작성시간");
        System.out.println("-".repeat(50));
        for (Review review : reviewList) {
            System.out.printf("  %2d / %s / %s / %.1f / %s / %s\n", review.getId(), review.getUserName(), review.getComment(), review.getScore(), review.isReplayFlag() ? "있음" : "없음", review.getRegDate());
        }

        Container.session.goToReview();
    }

    public void post() {
        String score;
        while (true) {
            System.out.println("-".repeat(30));
            System.out.println("  별점을 입력해주세요");
            System.out.printf("  >> ");
            score = Container.scanner.nextLine().trim();

            Map<String, Double> map = new HashMap<>();

            for (int i = 0; i <= 10; i++) {
                map.put(i * 0.5 + "", i * 0.5);
            }

            if (!map.containsKey(score)) {
                System.out.println("0.5점 단위로 입력해주세요");
                continue;
            }

            break;
        }

        System.out.println("리뷰에 대한 솔직한 평가를 남겨주세요");
        String comment = Container.scanner.nextLine();
        System.out.println(comment);
        System.out.println("재관람 의사\nY / N");
        String replayFlagAnswer = Container.scanner.nextLine().trim().toLowerCase();
        System.out.println(replayFlagAnswer);
        int replayFlag = 0;
        if (replayFlagAnswer.equals("y")) {
            replayFlag = 1;
            System.out.println("=재관람 의사가 있습니다.=\n작성하신 리뷰가 등록되었습니다.\n소중한 리뷰 감사합니다");
        } else {
            System.out.println("=재관람 의사가 없습니다.=\n작성하신 리뷰가 등록되었습니다.\n소중한 리뷰 감사합니다");
        }


        SecSql sql = new SecSql();

        sql.append("INSERT INTO `review`");
        sql.append("SET `comment` = ?", comment);
        sql.append(", `score` = ?", score);
        sql.append(", `replayFlag` = ?", replayFlag);
        sql.append(", regDate = NOW()");
        sql.append(", updateDate = NOW()");
        sql.append(", `userId` = ?", Container.session.getSessionUser().getId());
        sql.append(", `contentId` = ?", 1);

        DBUtil.insert(Container.connection, sql);
    }

    public void delete() {
        // 유저아이디와 컨텐츠아이디만 가지고 삭제를 하면 그 유저가 컨텐츠에 쓴 모든 리뷰가 사라진다.
        // 그래서 리뷰 하나에 아이디를 하나씩 부여해서 하나만 지울수 있게 한다.
        // 삭제를 한다고 하면 그 유저가 컨텐츠에 쓴 모든 리뷰를 불러온다.
        // 그리고 내가 실제하고싶은 리뷰가 뭐냐 물어본다.
        // 입력을 제대로 해주면 그 리뷰를 삭제한다.

        SecSql sql = new SecSql();

        sql.append("SELECT *");
        sql.append("FROM `review`");
        sql.append("WHERE userId = ? && contentId = ?", Container.session.getSessionUser().getId(), Container.session.getSessionContent().getId());

        List<Map<String, Object>> reviewMapList = DBUtil.selectRows(Container.connection, sql);
        List<Review> reviewList = new ArrayList<>();
        for (Map<String, Object> reviewMap : reviewMapList) {
            reviewList.add(new Review(reviewMap));
        }

        System.out.println("번호 / 내용 / 별점 / 재관람의사");
        System.out.println("-".repeat(50));
        for (Review review : reviewList) {
            System.out.printf("%d / %s / %.1f / %s\n", review.getId(), review.getComment(), review.getScore(), review.isReplayFlag() ? "있음" : "없음");
        }

        System.out.println("삭제할 리뷰 번호를 입력해주세요");

        int i = Container.scanner.nextInt();
        Container.scanner.nextLine();

        boolean isInReviewList = false;

        for (Review review : reviewList) {
            if (review.getId() == i) {
                isInReviewList = true;
            }
        }

        if (!isInReviewList) {
            System.out.println("삭제 가능한 리뷰 번호가 아닙니다.");
            return;
        }

        sql = new SecSql();
        sql.append("DELETE FROM `review`");
        sql.append("WHERE `id` = ?", i);

        DBUtil.delete(Container.connection, sql);

        System.out.printf("%d번째 리뷰가 삭제되었습니다.\n", i);
        pagedList();
    }

    //리뷰 수정
//    public void modify() {
//
//
//        // 지금까지 작상한 리뷰들을 불러온다.
//        //선택한다 수정한다.
//
//
//        SecSql sql = new SecSql();
//
//        sql.append("select *");
//        sql.append("from review");
//        sql.append("WHERE userId = 1 && contentId = 1");
//
//        List<Map<String, Object>> reviewMapList = DBUtil.selectRows(Container.connection, sql);
//        List<Review> reviewList = new ArrayList<>();
//        for (Map<String, Object> reviewMap : reviewMapList) {
//            reviewList.add(new Review(reviewMap));
//        }
//
//        System.out.println("번호 / 내용 / 별점 / 재관람의사");
//        System.out.println("=".repeat(50));
//        for (Review review : reviewList) {
//            System.out.printf("%d / %s / %.1f / %s\n", review.getId(), review.getComment(), review.getScore(), review.isReplayFlag() ? "있음" : "없음");
//        }
//
//        System.out.println("수정할 리뷰 번호를 입력해주세요");
//
//        int i = Container.scanner.nextInt();
//        Container.scanner.nextLine();
//
//        boolean isInReviewList = false;
//
//        Review selectedReview;
//
//        for (Review review : reviewList) {
//            if (review.getId() == i) {
//                isInReviewList = true;
//                selectedReview = review;
//            }
//        }
//
//        if (!isInReviewList) {
//            System.out.println("  수정 가능한 리뷰 번호가 아닙니다.");
//            return;
//        }
//
//        System.out.printf("  현재 별점: %.1f", selectedReview.getScore());
//        System.out.println("  별점을 수정하시겠습니까? (Y/N)");
//        System.out.printf("  >> ");
//        String answer = Container.scanner.nextLine().trim().toLowerCase();
//
//        String score = "";
//        if (answer.equals("y")) {
//            while (true) {
//                System.out.println("-".repeat(30));
//                System.out.printf("별점을 입력해주세요\n>>");
//                score = Container.scanner.nextLine().trim();
//
//                Map<String, Double> map = new HashMap<>();
//
//                for (int a = 0; a <= 10; a++) {
//                    map.put(a * 0.5 + "", a * 0.5);
//                }
//
//                if (!map.containsKey(score)) {
//                    System.out.println("0.5점 단위로 입력해주세요");
//                    continue;
//                }
//
//                break;
//            }
//        }
//
//        System.out.printf("  현재 내용: %s", selectedReview.getComment());
//        System.out.println("  내용을 수정하시겠습니까? (Y/N)");
//        System.out.printf("  >> ");
//        String answer = Container.scanner.nextLine().trim().toLowerCase();
//
//        String comment = "";
//        if (answer.equals("y")) {
//            System.out.println("  수정할 내용을 입력해주세요.");
//            System.out.printf("  >> ");
//            comment = Container.scanner.nextLine();
//        }
//
//        System.out.println("  현재 재관람 의사: %s", selectedReview.isReplayFlag() ? "있음" : "없음");
//        System.out.println("  내용을 수정하시겠습니까? (Y/N)");
//        System.out.println("  >>");
//        String answer = Container.scanner.nextLine().trim().toLowerCase();
//
//        String replayFlagAnswer = "";
//        if (replayFlagAnswer.equals("y")) {
//            System.out.println("  수정할 내용을 입력해주세요.");
//            System.out.println("  >>");
//            replayFlagAnswer = Container.scanner.nextLine();
//        }
//
//
//        System.out.println("재관람 의사\nY / N");
//        String replayFlagAnswer = Container.scanner.nextLine().trim().toLowerCase();
//        System.out.println(replayFlagAnswer);
//        int replayFlag = 0;
//        if (replayFlagAnswer.equals("y")) {
//            replayFlag = 1;
//            System.out.println("=재관람 의사가 있습니다.=\n작성하신 리뷰가 등록되었습니다.\n소중한 리뷰 감사합니다");
//        } else {
//            System.out.println("=재관람 의사가 없습니다.=\n작성하신 리뷰가 등록되었습니다.\n소중한 리뷰 감사합니다");
//        }
//
//
//        SecSql sql = new SecSql();
//
//        sql.append("UPDATE `review`");
//        sql.append("SET updateDate = NOW()");
//        sql.append(", `comment` = ?", comment);
//        sql.append(", `score` = ?", score);
//        sql.append(", `replayFlag` = ?", replayFlag);
//        sql.append("WHERE `userId` = ?", Container.session.getSessionUser().getId());
//        sql.append("&& `contentId` = ?", 1);
//
//        DBUtil.update(Container.connection, sql);
//    }

    public void prevPage() {
        if(Container.session.getSessionReviewPage() <= 1) return;
        Container.session.setSessionReviewPage(Container.session.getSessionReviewPage() - 1);
        pagedList();
    }

    public void nextPage() {
        if(!hasNextPage()) return;
        Container.session.setSessionReviewPage(Container.session.getSessionReviewPage() + 1);
        pagedList();
    }

    public boolean hasNextPage() {
        SecSql sql = new SecSql();

        sql.append("SELECT *");
        sql.append("FROM `review` as A");
        sql.append("WHERE A.`contentId` = ?", Container.session.getSessionContent().getId());
        sql.append("ORDER BY regDate DESC");
        sql.append("LIMIT ?, ?", Container.session.getSessionReviewPage()*Container.session.getSessionReviewItemsPerPage(), Container.session.getSessionReviewItemsPerPage());

        return DBUtil.selectRows(Container.connection, sql).size() != 0;
    }
}
