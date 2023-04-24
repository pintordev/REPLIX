package org.example.controller;

import org.example.Container;
import org.example.dto.Content;
import org.example.util.DBUtil;
import org.example.util.SecSql;

import java.util.*;

public class ContentController {
    public void search() {
        // 컨텐츠 검색 과정
        System.out.println("-".repeat(24));
        System.out.println("  검색어를 입력해주세요.");
        System.out.printf("  >> ");
        String searchKeyward = Container.scanner.nextLine().trim();

        System.out.println("-".repeat(24));
        System.out.printf("  입력하신 검색어 \"%s\"에 대한 검색 결과...\n\n", searchKeyward);

        SecSql sql = new SecSql();

        sql.append("SELECT A.`id`, A.`name`, A.`releaseDate`, A.`productionCompany`, A.`director`, A.`plot`");
        sql.append(", GROUP_CONCAT(DISTINCT C.`name` ORDER BY C.`id` DESC SEPARATOR ', ') AS 'cast'");
        sql.append(", GROUP_CONCAT(DISTINCT E.`name` ORDER BY E.`id` DESC SEPARATOR ', ') AS 'genre'");
        sql.append(", IFNULL(GROUP_CONCAT(DISTINCT G.`name` ORDER BY G.`id` DESC SEPARATOR ', '), '없음') AS 'ott'");
        sql.append("FROM `content` AS A");
        sql.append("INNER JOIN `cast` as B");
        sql.append("ON A.`id` = B.`contentId`");
        sql.append("INNER JOIN `actor` as C");
        sql.append("ON B.`actorId` = C.`id`");
        sql.append("INNER JOIN `contentGenre` AS D");
        sql.append("ON A.`id` = D.`contentId`");
        sql.append("INNER JOIN `genre` AS E");
        sql.append("ON D.`genreId` = E.`id`");
        sql.append("LEFT JOIN `serviceOtt` as F");
        sql.append("ON A.`id` = F.`contentId`");
        sql.append("LEFT JOIN ott as G");
        sql.append("ON F.`ottId` = G.`id`");
        sql.append("WHERE A.`name` Like CONCAT('%', ?, '%')", searchKeyward);
        sql.append("GROUP BY A.`id`");

        List<Map<String, Object>> contentMapList = DBUtil.selectRows(Container.connection, sql);

        sql = new SecSql();

        sql.append("SELECT COUNT(B.`id`) as 'like'");
        sql.append("FROM `content` AS A");
        sql.append("LEFT JOIN `likeContent` as B");
        sql.append("ON A.`id` = B.`contentId`");
        sql.append("WHERE A.`name` Like CONCAT('%', ?, '%')", searchKeyward);
        sql.append("GROUP BY A.`id`");

        List<Map<String, Object>> contentMapListB = DBUtil.selectRows(Container.connection, sql);

        sql = new SecSql();

        sql.append("SELECT COUNT(B.`id`) as 'dibs'");
        sql.append("FROM `content` AS A");
        sql.append("LEFT JOIN `dibsContent` as B");
        sql.append("ON A.`id` = B.`contentId`");
        sql.append("WHERE A.`name` Like CONCAT('%', ?, '%')", searchKeyward);
        sql.append("GROUP BY A.`id`");

        List<Map<String, Object>> contentMapListC = DBUtil.selectRows(Container.connection, sql);

        sql = new SecSql();

        sql.append("SELECT COUNT(B.`id`) as 'review'");
        sql.append("FROM `content` AS A");
        sql.append("LEFT JOIN `review` as B");
        sql.append("ON A.`id` = B.`contentId`");
        sql.append("WHERE A.`name` Like CONCAT('%', ?, '%')", searchKeyward);
        sql.append("GROUP BY A.`id`");

        List<Map<String, Object>> contentMapListD = DBUtil.selectRows(Container.connection, sql);

        List<Content> contentList = new ArrayList<>();

        for (int i = 0; i < contentMapList.size(); i++) {
            contentMapList.get(i).putAll(contentMapListB.get(i));
            contentMapList.get(i).putAll(contentMapListC.get(i));
            contentMapList.get(i).putAll(contentMapListD.get(i));
        }

        for (Map<String, Object> contentMap : contentMapList) {
            contentList.add(new Content(contentMap));
        }

        System.out.println("-".repeat(50));
        System.out.println("  번호 / 컨텐츠이름 / 장르 / 개요 / 릴리즈");
        System.out.println("-".repeat(50));
        for (Content content : contentList) {
            System.out.printf("  %d / %s / %s / %s / %s\n", content.getId(), content.getName(), content.getGenre(), content.getPlot(), content.getReleaseDate());
        }

        while (true) {
            try {
                System.out.println("-".repeat(50));
                System.out.println("  접속할 컨텐츠 번호를 입력해주세요.");
                System.out.printf("  >> ");
                int searchId = Container.scanner.nextInt();
                Container.scanner.nextLine();

                Content searchedContent = this.findContentById(searchId, contentList);

                if (searchedContent == null) {
                    System.out.println("  검색된 컨텐츠 번호가 아닙니다.");
                    continue;
                }

                System.out.printf("  \"%s\" 컨텐츠 게시판으로 이동합니다...\n\n", searchedContent.getName());
                Container.session.setSessionContent(searchedContent);
                break;

            } catch (InputMismatchException e) {
                System.out.println("  컨텐츠 번호를 제대로 입력해주세요.");
                Container.scanner.nextLine();
            }
        }
    }

    public void detail() {
        Content content = Container.session.getSessionContent();
        System.out.printf("https://replix.io/content?id=%d\n\n", content.getId());
        System.out.printf("       [%s]\n\n", content.getName());
        System.out.printf("  제작사: %s\n", content.getProductionCompany());
        System.out.printf("  감독: %s / 배우: %s\n", content.getDirector(), content.getCast());
        System.out.printf("  장르: %s\n", content.getGenre());
        System.out.printf("  개요: %s\n", content.getPlot());
        System.out.printf("  릴리즈: %s\n", content.getReleaseDate());
        System.out.printf("  OTT: %s\n", content.getOtt());
        System.out.printf("  리뷰: %d / 좋아요(%s): %d / 찜(%s): %d\n", content.getReview(), isUserLike() ? "♥" : "♡", content.getLike(), isUserDibs() ? "★" : "☆", content.getDibs());

        Container.session.goToContent();
    }

    private Content findContentById(int id, List<Content> contentList) {
        for (Content content : contentList) {
            if (content.getId() == id) {
                return content;
            }
        }
        return null;
    }

    public boolean isUserLike() {
        SecSql sql = new SecSql();

        sql.append("SELECT COUNT(*) as 'count'");
        sql.append("FROM `likeContent` as A");
        sql.append("WHERE A.`userId` = ? && A.`contentId` = ?", Container.session.getSessionUser().getId(), Container.session.getSessionContent().getId());

        if((int) DBUtil.selectRow(Container.connection, sql).get("count") == 0) {
            return false;
        }

        return true;
    }

    public boolean isUserDibs() {
        SecSql sql = new SecSql();

        sql.append("SELECT COUNT(*) as 'count'");
        sql.append("FROM `dibsContent` as A");
        sql.append("WHERE A.`userId` = ? && A.`contentId` = ?", Container.session.getSessionUser().getId(), Container.session.getSessionContent().getId());

        if((int) DBUtil.selectRow(Container.connection, sql).get("count") == 0) {
            return false;
        }

        return true;
    }

    public void like() {
        SecSql sql = new SecSql();

        sql.append("INSERT INTO `likeContent`");
        sql.append("SET `userId` = ?", Container.session.getSessionUser().getId());
        sql.append(", `contentId` = ?", Container.session.getSessionContent().getId());

        DBUtil.insert(Container.connection, sql);

        refresh();
    }

    public void cancelLike() {
        SecSql sql = new SecSql();

        sql.append("DELETE FROM `likeContent`");
        sql.append("WHERE `userId` = ?", Container.session.getSessionUser().getId());
        sql.append("&& `contentId` = ?", Container.session.getSessionContent().getId());

        DBUtil.delete(Container.connection, sql);

        refresh();
    }

    public void dibs() {
        SecSql sql = new SecSql();

        sql.append("INSERT INTO `dibsContent`");
        sql.append("SET `userId` = ?", Container.session.getSessionUser().getId());
        sql.append(", `contentId` = ?", Container.session.getSessionContent().getId());

        DBUtil.insert(Container.connection, sql);

        refresh();
    }

    public void cancelDibs() {
        SecSql sql = new SecSql();

        sql.append("DELETE FROM `dibsContent`");
        sql.append("WHERE `userId` = ?", Container.session.getSessionUser().getId());
        sql.append("&& `contentId` = ?", Container.session.getSessionContent().getId());

        DBUtil.delete(Container.connection, sql);

        refresh();
    }

    public void refresh() {
        SecSql sql = new SecSql();

        sql.append("SELECT A.`id`, A.`name`, A.`releaseDate`, A.`productionCompany`, A.`director`, A.`plot`");
        sql.append(", GROUP_CONCAT(DISTINCT C.`name` ORDER BY C.`id` DESC SEPARATOR ', ') AS 'cast'");
        sql.append(", GROUP_CONCAT(DISTINCT E.`name` ORDER BY E.`id` DESC SEPARATOR ', ') AS 'genre'");
        sql.append(", IFNULL(GROUP_CONCAT(DISTINCT G.`name` ORDER BY G.`id` DESC SEPARATOR ', '), '없음') AS 'ott'");
        sql.append("FROM `content` AS A");
        sql.append("INNER JOIN `cast` as B");
        sql.append("ON A.`id` = B.`contentId`");
        sql.append("INNER JOIN `actor` as C");
        sql.append("ON B.`actorId` = C.`id`");
        sql.append("INNER JOIN `contentGenre` AS D");
        sql.append("ON A.`id` = D.`contentId`");
        sql.append("INNER JOIN `genre` AS E");
        sql.append("ON D.`genreId` = E.`id`");
        sql.append("LEFT JOIN `serviceOtt` as F");
        sql.append("ON A.`id` = F.`contentId`");
        sql.append("LEFT JOIN ott as G");
        sql.append("ON F.`ottId` = G.`id`");
        sql.append("WHERE A.`id` = ?", Container.session.getSessionContent().getId());
        sql.append("GROUP BY A.`id`");

        Map<String, Object> contentMap = DBUtil.selectRow(Container.connection, sql);

        sql = new SecSql();

        sql.append("SELECT COUNT(B.`id`) as 'like'");
        sql.append("FROM `content` AS A");
        sql.append("LEFT JOIN `likeContent` as B");
        sql.append("ON A.`id` = B.`contentId`");
        sql.append("WHERE A.`id` = ?", Container.session.getSessionContent().getId());
        sql.append("GROUP BY A.`id`");

        Map<String, Object> contentMapB = DBUtil.selectRow(Container.connection, sql);

        sql = new SecSql();

        sql.append("SELECT COUNT(B.`id`) as 'dibs'");
        sql.append("FROM `content` AS A");
        sql.append("LEFT JOIN `dibsContent` as B");
        sql.append("ON A.`id` = B.`contentId`");
        sql.append("WHERE A.`id` = ?", Container.session.getSessionContent().getId());
        sql.append("GROUP BY A.`id`");

        Map<String, Object> contentMapC = DBUtil.selectRow(Container.connection, sql);

        sql = new SecSql();

        sql.append("SELECT COUNT(B.`id`) as 'review'");
        sql.append("FROM `content` AS A");
        sql.append("LEFT JOIN `review` as B");
        sql.append("ON A.`id` = B.`contentId`");
        sql.append("WHERE A.`id` = ?", Container.session.getSessionContent().getId());
        sql.append("GROUP BY A.`id`");

        Map<String, Object> contentMapD = DBUtil.selectRow(Container.connection, sql);

        contentMap.putAll(contentMapB);
        contentMap.putAll(contentMapC);
        contentMap.putAll(contentMapD);

        Container.session.setSessionContent(new Content(contentMap));
    }
}
