package org.example;

import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    public static void run() {
        Container.init(); // 구동 시작과 함께 static으로 사용할 변수, 객체들을 선언한다.

        while (true) {
            // DB 연결
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("MySQL 드라이버 로딩 실패! 프로그램을 종료합니다.");
                break;
            }

            // sql 연결 설정이 담긴 url
            String url = "jdbc:mysql://127.0.0.1:3306/REPLIX?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

            try {
                // 연결 설정과 함께 관리자 계정으로 로그인
                Container.connection = DriverManager.getConnection(url, "root", "12345qwerty");

                Container.systemController.menu(); // 입력 가능한 메뉴 출력. session의 상태에 따라 출력하는 메뉴가 다르다

                // action 메서드 실행
                // 입력받은 명령어를 넘겨주고 doAction 메서드 내에서 적절한 수행이 이루어지도록 한다
                doAction(Container.systemController.getCommand());

            } catch (SQLException e) {
                System.out.println("MySQL 연결 실패! 프로그램을 종료합니다.");
                break;
            } finally {
                try {
                    if (Container.connection != null && !Container.connection.isClosed()) {
                        Container.connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } // DB 연결 끝
        } // 시스템 종료는 doAction 메서드 내에서 명령어가 종료일 때 수행
    }

    private static void doAction(String command) {
        switch(command) {
            case "종료":
                Container.systemController.exit();
                break;
            case "로그인":
                Container.userController.login();
                break;
            case "로그아웃":
                Container.userController.logout();
                break;
            case "회원가입":
                Container.userController.signUp();
                break;
            case "마이페이지":
                Container.userController.myPage();
                break;
            case "컨텐츠검색":
                Container.contentController.search();
                break;
            case "개인정보수정":
                Container.userController.modify();
                break;
            default:
                Container.systemController.commandError();
                break;
        }
    }
}
//