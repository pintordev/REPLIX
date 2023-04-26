package org.example.controller;

import org.example.Container;

import java.util.HashMap;
import java.util.Map;

public class SystemController {
    public Map<String, String> menuMap;
    public void menu() {
        int menuCount = 1;
        menuMap = new HashMap<>();
        System.out.println("-".repeat(24));
        // 로그아웃 상태일 때
        if (Container.session.getSessionState() == 0) {
            System.out.println("https://replix.io/\n");
            System.out.println("[ REPLIX ]       ");
            menuMap.put(menuCount + "", "로그인");
            System.out.printf("%d. 로그인\n", menuCount++);
            menuMap.put(menuCount + "", "회원가입");
            System.out.printf("%d. 회원가입\n", menuCount++);
            menuMap.put(menuCount + "", "아이디찾기");
            System.out.printf("%d. 아이디찾기\n", menuCount++);
            menuMap.put(menuCount + "", "비밀번호찾기");
            System.out.printf("%d. 비밀번호찾기\n", menuCount);
        }
        // 로그인 상태에서 메인 메뉴 상태일 때
        if (Container.session.getSessionState() == 1) {
            System.out.println("https://replix.io/logined\n");
            System.out.println("[ REPLIX ]       ");
            menuMap.put(menuCount + "", "로그아웃");
            System.out.printf("%d. 로그아웃\n", menuCount++);
            menuMap.put(menuCount + "", "마이페이지");
            System.out.printf("%d. 마이페이지\n", menuCount++);
            menuMap.put(menuCount + "", "컨텐츠검색");
            System.out.printf("%d. 컨텐츠검색\n", menuCount);
        }
        if (Container.session.getSessionState() == 2) {
            System.out.println("https://replix.io/mypage\n");
            System.out.println("[ MY PAGE ]       ");
            menuMap.put(menuCount + "", "회원정보확인");
            System.out.printf("%d. 회원정보확인\n", menuCount++);
            menuMap.put(menuCount + "", "회원정보수정");
            System.out.printf("%d. 회원정보수정\n", menuCount++);
            menuMap.put(menuCount + "", "내가남긴리뷰");
            System.out.printf("%d. 내가남긴리뷰\n", menuCount++);
            menuMap.put(menuCount + "", "내가좋아요한컨텐츠");
            System.out.printf("%d. 내가좋아요한컨텐츠\n", menuCount++);
            menuMap.put(menuCount + "", "내가찜한컨텐츠");
            System.out.printf("%d. 내가찜한컨텐츠\n", menuCount++);
            menuMap.put(menuCount + "", "회원탈퇴");
            System.out.printf("%d. 회원탈퇴\n", menuCount);
        }
        // 로그인 상태에서 컨텐츠 게시판 접속 상태일 때
        if (Container.session.getSessionState() == 3) {
            menuMap.put(menuCount + "", "리뷰확인하기");
            System.out.printf("%d. 리뷰확인하기\n", menuCount++);
            menuMap.put(menuCount + "", "리뷰남기기");
            System.out.printf("%d. 리뷰남기기\n", menuCount++);
            menuMap.put(menuCount + "", "리뷰삭제하기");
            System.out.printf("%d. 리뷰삭제하기\n", menuCount++);
            menuMap.put(menuCount + "", "좋아요" + (Container.contentController.isUserLike() ? "취소하기" : "누르기"));
            System.out.printf("%d. 좋아요%s\n", menuCount++, Container.contentController.isUserLike() ? "취소하기" : "누르기");
            menuMap.put(menuCount + "", "찜" + (Container.contentController.isUserDibs() ? "취소" : "") + "하기");
            System.out.printf("%d. 찜%s하기\n", menuCount++, Container.contentController.isUserDibs() ? "취소" : "");
        }
        // 로그인 상태에서 리뷰 게시판 접속 상태일 때
        if (Container.session.getSessionState() == 4) {
//            System.out.printf("  %d. 리뷰평가하기\n", menuCount++);
            if(Container.session.getSessionReviewPage() > 1) {
                menuMap.put(menuCount + "", "이전페이지");
                System.out.printf("%d. 이전페이지\n", menuCount++);
            }
            if(Container.reviewController.hasNextPage()) {
                menuMap.put(menuCount + "", "다음페이지");
                System.out.printf("%d. 다음페이지\n", menuCount);
            }
        }
        if (Container.session.getSessionState() == 0 || Container.session.getSessionState() == 1) {
            menuMap.put("0", "종료");
            System.out.println("0. 종료");
        }
        if (Container.session.getSessionState() == 2 || Container.session.getSessionState() == 3 || Container.session.getSessionState() == 4) {
            menuMap.put("0", "돌아가기");
            System.out.println("0. 돌아가기");
        }
    }

    public String getCommand() {
        System.out.println("-".repeat(24));
        System.out.printf(">> ");
        return Container.scanner.nextLine().trim();
    }

    public void exit() {
        if (Container.session.getSessionState() != 0 && Container.session.getSessionState() != 1) {
            return;
        }
        System.out.println("-".repeat(24));
        System.out.println("REPLIX 앱을 종료합니다.");
        Container.session.logout();
        System.exit(0);
    }

    public void commandError() {
        System.out.println("-".repeat(24));
        System.out.println("올바르지 않은 입력입니다.");
    }

    public void adComment() {
        int adId = (int) (Math.random() * 4) + 1;
        System.out.println("=".repeat(60));
        if (adId == 1) System.out.println("\n        방방곡곡 낭만여행, 방랑!\n        여행 지원 비용이 무려 100만원!\n");
        if (adId == 2) System.out.println("\n        화장품 요기있어 일루와!\n        100만원 상당의 스킨케어 제품을 무료로 드립니다.\n");
        if (adId == 3) System.out.println("\n        LostArk 스케줄은 LSS에서!\n        처음 한달 간 돌 깎는 비용 전액 지원합니다.\n");
        if (adId == 4) System.out.println("\n        여러분의 매장을 등록해보세요. 뭐잡솨YOU~!\n        100만원 외식 상품권을 드립니다.\n");
        System.out.println("=".repeat(60));
    }

    public void goBack() {
        int sessionState = Container.session.getSessionState();

        if (sessionState < 2) return;
        if (sessionState == 4) {
            Container.session.setSessionReviewPage(1);
            Container.session.setSessionState(3);
            Container.contentController.detail();
            return;
        }
        Container.session.setSessionState(1);
    }

    public boolean isQuit(String command) {
        if (command.equals("q;")) {
            System.out.println("입력을 종료합니다");
            return true;
        }
        return false;
    }
}
