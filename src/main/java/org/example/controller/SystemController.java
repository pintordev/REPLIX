package org.example.controller;

import org.example.Container;

public class SystemController {
    public void menu() {
        int menuCount = 1;
        System.out.println("-".repeat(24));
        // 로그아웃 상태일 때
        if (Container.session.getSessionState() == 0) {
            System.out.println("https://replix.io/\n\n");
            System.out.println("       [ REPLIX ]       ");
            System.out.printf("  %d. 로그인\n", menuCount++);
            System.out.printf("  %d. 회원가입\n", menuCount++);
            System.out.printf("  %d. 아이디찾기\n", menuCount++);
            System.out.printf("  %d. 비밀번호찾기\n", menuCount);
        }
        // 로그인 상태에서 메인 메뉴 상태일 때
        if (Container.session.getSessionState() == 1) {
            System.out.println("https://replix.io/logined\n\n");
            System.out.println("       [ REPLIX ]       ");
            System.out.printf("  %d. 로그아웃\n", menuCount++);
            System.out.printf("  %d. 마이페이지\n", menuCount++);
            System.out.printf("  %d. 컨텐츠검색\n", menuCount);
        }
        // 로그인 상태에서 컨텐츠 게시판 접속 상태일 때
        if (Container.session.getSessionState() == 3) {
            System.out.println("https://replix.io/contents/review\n\n");
            System.out.printf("  %d. 리뷰확인하기\n", menuCount++);
            System.out.printf("  %d. 리뷰남기기\n", menuCount++);
            System.out.printf("  %d. 돌아가기\n", menuCount);
        }
        System.out.println("  0. 종료");
    }

    public String getCommand() {
        System.out.println("-".repeat(24));
        System.out.printf("  >> ");
        return Container.scanner.nextLine().trim();
    }

    public void exit() {
        System.out.println("-".repeat(24));
        System.out.println("  REPLIX 앱을 종료합니다.");
        Container.session.logout();
        System.exit(0);
    }

    public void commandError() {
        System.out.println("-".repeat(24));
        System.out.println("  올바르지 않은 입력입니다.");
    }
}
