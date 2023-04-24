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
        if (Container.session.getSessionState() == 2) {
            System.out.println("https://replix.io/mypage\n\n");
            System.out.println("       [ REPLIX ]       ");
            System.out.printf("  %d. 회원정보확인\n", menuCount++);
            System.out.printf("  %d. 회원정보수정\n", menuCount);
        }
        // 로그인 상태에서 컨텐츠 게시판 접속 상태일 때
        if (Container.session.getSessionState() == 3) {
//            adComment();
            System.out.printf("  %d. 리뷰확인하기\n", menuCount++);
            System.out.printf("  %d. 리뷰남기기\n", menuCount++);
            System.out.printf("  %d. 좋아요%s\n", menuCount++, Container.contentController.isUserLike() ? "취소하기" : "누르기");
            System.out.printf("  %d. 찜%s하기\n", menuCount++, Container.contentController.isUserDibs() ? "취소" : "");
        }
        // 로그인 상태에서 리뷰 게시판 접속 상태일 때
        if (Container.session.getSessionState() == 4) {
//            adComment();
            System.out.printf("  %d. 리뷰평가하기\n", menuCount++);
            System.out.printf("  %d. 리뷰삭제하기\n", menuCount++);
            if(Container.session.getSessionReviewPage() > 1) System.out.printf("  %d. 이전페이지\n", menuCount++);
            if(Container.reviewController.hasNextPage()) System.out.printf("  %d. 다음페이지\n", menuCount);
        }
        if (Container.session.getSessionState() == 0 || Container.session.getSessionState() == 1) {
            System.out.println("  0. 종료");
        }
        if (Container.session.getSessionState() == 2 || Container.session.getSessionState() == 3 || Container.session.getSessionState() == 4) {
            System.out.println("  0. 돌아가기");
        }
    }

    public String getCommand() {
        System.out.println("-".repeat(24));
        System.out.printf("  >> ");
        return Container.scanner.nextLine().trim();
    }

    public void exit() {
        if (Container.session.getSessionState() != 0 && Container.session.getSessionState() != 1) {
            return;
        }
        System.out.println("-".repeat(24));
        System.out.println("  REPLIX 앱을 종료합니다.");
        Container.session.logout();
        System.exit(0);
    }

    public void commandError() {
        System.out.println("-".repeat(24));
        System.out.println("  올바르지 않은 입력입니다.");
    }

    public void adComment() {
        System.out.println("=".repeat(50));
        System.out.println("\n        LostArk 스케쥴은 LSS에서!\n");
        System.out.println("=".repeat(50));
    }

    public void goBack() {
        int sessionState = Container.session.getSessionState();

        if (sessionState < 2) return;

        Container.session.setSessionReviewPage(1);
        Container.session.setSessionState(1);
    }
}
