package org.example.repository;

import org.example.Container;
import org.example.util.DBUtil;
import org.example.util.SecSql;

import java.util.List;
import java.util.Map;

public class ReviewRepository {
    public void post(String comment, String score, int replayFlag) {
        int userId = Container.session.getSessionUser().getId();
        int contentId = Container.session.getSessionContent().getId();

        SecSql sql = new SecSql();

        sql.append("INSERT INTO `review`");
        sql.append("SET `comment` = ?", comment);
        sql.append(", `score` = ?", score);
        sql.append(", `replayFlag` = ?", replayFlag);
        sql.append(", regDate = NOW()");
        sql.append(", updateDate = NOW()");
        sql.append(", `userId` = ?", userId);
        sql.append(", `contentId` = ?", contentId);

        DBUtil.insert(Container.connection, sql);
    }

    public void deleteById(int deleteId) {
        SecSql sql = new SecSql();
        sql.append("DELETE FROM `review`");
        sql.append("WHERE `id` = ?", deleteId);

        DBUtil.delete(Container.connection, sql);
    }

    public List<Map<String, Object>> reviewByUserIdContentId() {
        int userId = Container.session.getSessionUser().getId();
        int contentId = Container.session.getSessionContent().getId();

        SecSql sql = new SecSql();

        sql.append("SELECT *");
        sql.append("FROM `review`");
        sql.append("WHERE userId = ? && contentId = ?", userId, contentId);

        return DBUtil.selectRows(Container.connection, sql);
    }
}
