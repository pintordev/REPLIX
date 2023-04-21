package org.example.controller;

import org.example.Container;
import org.example.dto.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserController {
    private ArrayList<User> userList;

    public UserController() {
        this.userList = new ArrayList<>();

        Map<String, Object> userMap = new HashMap<>();

        userMap.put("id", 1);
        userMap.put("loginId", "daeun_1214");
        userMap.put("loginPw", "qwerty");
        userMap.put("name", "Daeun Kim");
        userMap.put("birthDate", "2023-04-20");
        userMap.put("gender", "female");
        userMap.put("email", "unknown@replix.io");
        userMap.put("regDate", "2023-04-20");

        User user = new User(userMap);

        this.userList.add(user);
    }

    public User findById(int id) {
        for (User user : this.userList) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public User findByLoginId(String loginId) {
        for (User user : this.userList) {
            if (user.getLoginId().equals(loginId)) {
                return user;
            }
        }
        return null;
    }

    public User findByLoginPw(String loginPw) {
        for (User user : this.userList) {
            if (user.getLoginPw().equals(loginPw)) {
                return user;
            }
        }
        return null;
    }

    public void login() {
        System.out.println("-".repeat(24));
        System.out.println("  아이디를 입력해주세요.");
        System.out.printf("  >> ");
        String loginId = Container.scanner.nextLine().trim();
        System.out.println("  비밀번호를 입력해주세요.");
        System.out.printf("  >> ");
        String loginPw = Container.scanner.nextLine().trim();
        System.out.println("-".repeat(24));

        if (this.findByLoginId(loginId) == null) {
            System.out.println("  존재하지 않는 아이디입니다."); // 아이디가 다르면
            System.out.println("  REPLIX 앱 로그인에 실패하였습니다."); // 두 경우 모두 이 구문은 출력'
            return;
        }

        if (this.findByLoginPw(loginPw) == null) {
            System.out.println("  올바르지 않은 비밀번호입니다."); // 비밀번호가 다르면
            System.out.println("  REPLIX 앱 로그인에 실패하였습니다."); // 두 경우 모두 이 구문은 출력
            return;
        }

        System.out.println("  REPLIX 앱 로그인에 성공하였습니다.");
        System.out.printf("  %s님 환영합니다.\n", this.findByLoginId(loginId).getName());
        Container.session.login(this.findByLoginId(loginId));
    }

    public void logout() {
        System.out.println("  REPLIX 앱에서 로그아웃합니다.");
        Container.session.logout();
    }

    public void signUp() {
        System.out.println("  기능 구현 예정입니다.");
    }


    public void myPage() {
        System.out.println("  기능 구현 예정입니다.");
    }
}
