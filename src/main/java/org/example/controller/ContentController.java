package org.example.controller;

import org.example.Container;
import org.example.dto.Content;
import org.example.util.DBUtil;
import org.example.util.SecSql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContentController {
    private List<Content> contentList;
    public void search() {
        // 컨텐츠 검색 과정
        System.out.println("  검색어를 입력해주세요.");
        System.out.printf("  >> ");
        String searchKeyward = Container.scanner.nextLine().trim();

        System.out.printf("  입력하신 검색어: %s\n\n", searchKeyward);

        SecSql sql = new SecSql();

        sql.append("SELECT A.`id`, A.`name`, A.`releaseDate`, A.`productionCompany`, A.`director`, A.`plot`,");
        sql.append("GROUP_CONCAT(DISTINCT C.`name` ORDER BY C.`id` DESC SEPARATOR ', ') AS \'genre\'");
        sql.append("FROM `content` AS A");
        sql.append("INNER JOIN `contentGenre` AS B");
        sql.append("ON A.`id` = B.`contentId`");
        sql.append("INNER JOIN `genre` AS C");
        sql.append("ON B.`genreId` = C.`id`");
        sql.append("WHERE A.`name` Like CONCAT('%', ?, '%')", searchKeyward);
        sql.append("GROUP BY A.`id`");

        List<Map<String, Object>> contentMapList = DBUtil.selectRows(Container.connection, sql);
        this.contentList = new ArrayList<>();

        for (Map<String, Object> contentMap : contentMapList) {
            this.contentList.add(new Content(contentMap));
        }

        System.out.println("  번호 / 컨텐츠이름 / 장르 / 개요 / 릴리즈");
        System.out.println("-".repeat(50));
        for (Content content : this.contentList) {
            System.out.printf("  %d / %s / %s / %s / %s\n", content.getId(), content.getName(), content.getGenre(), content.getPlot(), content.getReleaseDate());
        }

        System.out.println("\n\n  접속할 컨텐츠 번호를 입력해주세요.");
        System.out.printf("  >> ");
        int searchId = Container.scanner.nextInt();
        Container.scanner.nextLine();

        System.out.printf("  컨텐츠 게시판으로 이동합니다....\n");

        detail();


    }

    public void detail() {
        System.out.printf("https://replix.io/contents?id=%d\n\n",1);
        Container.session.goToReview();
    }
}
