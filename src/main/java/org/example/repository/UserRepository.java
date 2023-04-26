package org.example.repository;

import org.example.Container;
import org.example.dto.User;
import org.example.util.DBUtil;
import org.example.util.SecSql;

import java.util.List;
import java.util.Map;

public class UserRepository {
    public boolean duplicateId(String loginId) {
        SecSql sql = new SecSql();

        sql.append("SELECT COUNT(*) > 0");
        sql.append("FROM `user`");
        sql.append("WHERE loginId = ?", loginId);

        return DBUtil.selectRowBooleanValue(Container.connection, sql);
    }

    public void genreSignup(int userId, int genreId) {
        SecSql sql = new SecSql();
        sql.append("INSERT INTO `favoriteGenre`");
        sql.append("SET userId = ?", userId);
        sql.append(", genreId = ?", genreId);

        DBUtil.insert(Container.connection, sql);
    }

    public int signUp(String loginId, String loginPw, String name, String birthDate, String gender, String email) {
        SecSql sql = new SecSql();
        sql.append("INSERT INTO `user`");
        sql.append("SET regDate = NOW()");
        sql.append(", updateDate = NOW()");
        sql.append(", loginId = ?", loginId);
        sql.append(", loginPw = ?", loginPw);
        sql.append(", name = ?", name);
        sql.append(", birthDate = ?", birthDate);
        sql.append(", gender = ?", gender);
        sql.append(", email = ?", email);

        return DBUtil.insert(Container.connection, sql);
    }

    public User findByLoginId(String loginId) {
        SecSql sql = new SecSql();

        sql.append("SELECT *");
        sql.append("FROM `user`");
        sql.append("WHERE loginId = ?", loginId);

        Map<String, Object> userMap = DBUtil.selectRow(Container.connection, sql);

        if (userMap.isEmpty()) {
            return null;
        }
        return new User(userMap);
    }

    public List<Map<String, Object>> findGenreAll() {
        SecSql sql = new SecSql();
        sql.append("SELECT * FROM `genre`");

        return DBUtil.selectRows(Container.connection, sql);
    }

    public void printGenre() {
        System.out.println("  [장르 목록]");
        System.out.printf("  ");
        List<Map<String, Object>> genreMapList = findGenreAll();
        for (int i = 0; i < genreMapList.size(); i++) {
            System.out.printf("%s", genreMapList.get(i).get("name"));
            if (i < genreMapList.size() - 1) {
                System.out.printf(", ");
            }
        }
        System.out.println("");
    }

    public boolean isIngenre(String inputGenre) {
        for (Map<String, Object> genreMap : findGenreAll()) {
            if (genreMap.get("name").equals(inputGenre)) return true;
        }
        return false;
    }

    public int findGenreIdByName(String inputGenre) {
        for (Map<String, Object> genreMap : findGenreAll()) {
            if (genreMap.get("name").equals(inputGenre)) return (int) genreMap.get("id");
        }
        return -1;
    }


    public User findByLoginPw(String loginPw) {
        SecSql sql = new SecSql();

        sql.append("SELECT *");
        sql.append("FROM `user`");
        sql.append("WHERE loginPw = ?", loginPw);

        Map<String, Object> userMap = DBUtil.selectRow(Container.connection, sql);

        if (userMap.isEmpty()) {
            return null;
        }
        return new User(userMap);
    }

    public void update(String newPw, String newEmail) {
        SecSql sql = new SecSql();

        sql.append("UPDATE `user`");
        sql.append("SET updateDate = NOW()");
        sql.append(", loginPw = ?", newPw);
        sql.append(", email = ?", newEmail);
        sql.append("WHERE `id` = ?", Container.session.getSessionUser().getId());

        DBUtil.update(Container.connection, sql);
    }

    public void getUserInformation() {
        User user = Container.session.getSessionUser();
        System.out.printf("\n  [%s님의 회원정보]       last updated at %s\n\n", user.getName(), user.getUpdateDate().substring(0, 11));
        System.out.printf("  성명: %s\n", user.getName());
        System.out.printf("  생년월일: %s\n", user.getBirthDate());
        System.out.printf("  성별: %s\n", user.getGender());
        System.out.printf("  이메일: %s\n", user.getEmail());
        System.out.printf("  가입일: %s\n", user.getRegDate().substring(0, 11));
    }

    public void deleteUser(int id){
        SecSql sql = new SecSql();

        sql.append("DELETE FROM `review`");
        sql.append("WHERE `review`.`userId` = ?", id);

        DBUtil.delete(Container.connection, sql);

        sql = new SecSql();
        sql.append("DELETE FROM `reviewEvaluation`");
        sql.append("WHERE `reviewEvaluation`.`userId` = ?", id);

        DBUtil.delete(Container.connection, sql);

        sql = new SecSql();
        sql.append("DELETE FROM `favoriteGenre`");
        sql.append("WHERE `favoriteGenre`.`userId` = ?", id);

        DBUtil.delete(Container.connection, sql);

        sql = new SecSql();
        sql.append("DELETE FROM `likeContent`");
        sql.append("WHERE `likeContent`.`userId` = ?", id);

        DBUtil.delete(Container.connection, sql);

        sql = new SecSql();
        sql.append("DELETE FROM `dibsContent`");
        sql.append("WHERE `dibsContent`.`userId` = ?", id);

        DBUtil.delete(Container.connection, sql);

        sql = new SecSql();
        sql.append("DELETE FROM `user`");
        sql.append("WHERE `id` = ?", id);

        DBUtil.delete(Container.connection, sql);
    }

    public User findByUserId(String name, String email){
        SecSql sql = new SecSql();

        sql.append("SELECT *");
        sql.append("FROM `user`");
        sql.append("WHERE name = ?", name);
        sql.append("AND email = ?", email);


        Map<String, Object> userMap = DBUtil.selectRow(Container.connection, sql);

        if(userMap.isEmpty()){
            return null;
        }
        return new User(userMap);
    }

    public User findByUserPw(String name, String loginId, String email) {
        SecSql sql = new SecSql();

        sql.append("SELECT *");
        sql.append("FROM `user`");
        sql.append("WHERE name = ?", name);
        sql.append("AND email = ?", email);
        sql.append("AND loginId = ?",loginId);
        Map<String, Object> userMap = DBUtil.selectRow(Container.connection, sql);

        if(userMap.isEmpty()){
            return null;
        }
        return new User(userMap);
    }
}
