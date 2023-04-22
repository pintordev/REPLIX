package org.example.repository;

import org.example.Container;
import org.example.dto.User;
import org.example.util.DBUtil;
import org.example.util.SecSql;

import java.util.Map;

public class UserRepository {
    public boolean duplicateId(String loginId){
        SecSql sql = new SecSql();

        sql.append("SELECT COUNT(*) > 0");
        sql.append("FROM `user`");
        sql.append("WHERE loginId = ?", loginId);

        return DBUtil.selectRowBooleanValue(Container.connection, sql);
    }

    public int signUp(String loginId, String loginPw, String name, String birthDate, String gender, String email){
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

        int id = DBUtil.insert(Container.connection, sql);

        return id;
    }

    public User findByLoginId(String loginId){
        SecSql sql = new SecSql();

        sql.append("SELECT *");
        sql.append("FROM `user`");
        sql.append("WHERE loginId = ?", loginId);

        Map<String, Object> userMap = DBUtil.selectRow(Container.connection, sql);

        if(userMap.isEmpty()){
            return null;
        }
        return new User(userMap);
    }




}
