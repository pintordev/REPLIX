package com.application.dto;

import org.example.dto.User;
import org.example.util.DBUtil;

import java.util.HashMap;
import java.util.Map;

public class UserTest {
    public static void main(String[] args) {
        Map<String, Object> userMap = new HashMap<>();

        userMap.put("id", 1);
        userMap.put("loginId", "pintor.dev");
        userMap.put("loginPw", "qwerty");
        userMap.put("name", "HoHyun Kim");
        userMap.put("birthDate", "2023-04-20");
        userMap.put("gender", "male");
        userMap.put("email", "unknown@replix.io");
        userMap.put("regDate", "2023-04-20");

        User user = new User(userMap);

        System.out.println("id: " + user.getId());
        System.out.println("name: " + user.getName());
    }

}
