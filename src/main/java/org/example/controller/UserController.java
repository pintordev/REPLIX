package org.example.controller;

import org.example.Container;
import org.example.dto.Review;
import org.example.dto.User;
import org.example.service.UserService;
import org.example.util.DBUtil;
import org.example.util.SecSql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserController {
    private UserService userService;

    public UserController() {
        userService = new UserService();
    }

    public void login() {
        int loginLimit = 3;
        int loginTry = 0;

        System.out.println("-".repeat(24));

        while (true) {
            if (loginTry >= loginLimit) {
                System.out.println("  로그인 과정에서 오류가 3회 발생하였습니다.");
                System.out.println("  아이디와 비밀번호를 확인 후 다시 시도해주세요.");
                break;
            }

            System.out.println("  아이디를 입력해주세요.");
            System.out.printf("  >> ");
            String loginId = Container.scanner.nextLine().trim();
            System.out.println("  비밀번호를 입력해주세요.");
            System.out.printf("  >> ");
            String loginPw = Container.scanner.nextLine().trim();

            User user = userService.findByLoginId(loginId);

            if (user == null) {
                loginTry++;
                System.out.println("  존재하지 않는 아이디입니다."); // 아이디가 다르면
                System.out.println("  REPLIX 앱 로그인에 실패하였습니다.");
                continue;
            }

            if (!user.getLoginPw().equals(loginPw)) {
                loginTry++;
                System.out.println("  옳바르지 않은 비밀번호입니다."); // 비밀번호가 다르면
                System.out.println("  REPLIX 앱 로그인에 실패하였습니다.");
                System.out.println("-".repeat(24));
                continue;
            }

            System.out.println("  REPLIX 앱 로그인에 성공하였습니다.");
            System.out.printf("  %s님 환영합니다.\n", user.getName());
            Container.session.login(user);
            break;

        }

    }

    public void logout() {
        System.out.println("  REPLIX 앱에서 로그아웃합니다.");
        Container.session.logout();
    }

    public void signUp() {
        String loginId;
        String loginPw;
        String loginPwConfirm;
        String name;
        String birthDate;
        String gender;
        String email;
        String inputGenre;

        while (true) {
            System.out.println("-".repeat(24));
            System.out.println("  사용하실 로그인 아이디를 입력해주세요. (영문, 숫자)");
            System.out.printf("  >> ");
            loginId = Container.scanner.nextLine().trim();

            if (loginId.length() == 0) {
                System.out.println("  로그인 아이디를 입력하지 않았습니다.");
                continue;
            }

            if (loginId.replaceAll("[0-9a-zA-Z]", "").length() != 0) {
                System.out.println("  아이디는 영문 또는 숫자로만 입력해주세요.");
                continue;
            }

            boolean duplicateId = userService.duplicateId(loginId);

            if (duplicateId) {
                System.out.printf("  %s(은)는 이미 사용중인 아이디입니다.", loginId);
                continue;
            }
            break;
        }

        while (true) {
            System.out.println("-".repeat(24));
            System.out.println("  사용하실 로그인 비밀번호를 입력해주세요. (영문, 숫자, 특수문자, 8자리 이상)");
            System.out.printf("  >> ");
            loginPw = Container.scanner.nextLine().trim();

            if (loginPw.length() == 0) {
                System.out.println("  로그인 비밀번호를 입력하지 않았습니다.");
                continue;
            } else if (loginPw.replaceAll("[0-9a-zA-Z\s~!@#$%^&*()_+=]", "").length() != 0) {
                System.out.println("  로그인 비밀번호는 영문, 숫자, 특수문자로만 입력해주세요.");
                continue;
            } else if (loginPw.length() < 8) {
                System.out.println("  비밀번호는 8자리 이상으로 입력해주세요.");
                continue;
            }

            boolean loginPwConfirmIssame = true;

            while (true) {
                System.out.println("  로그인 비밀번호를 확인합니다.");
                System.out.printf("  >> ");
                loginPwConfirm = Container.scanner.nextLine().trim();

                if (loginPwConfirm.length() == 0) {
                    System.out.println("  비밀번호를 입력하지 않았습니다.");
                    continue;
                }

                if (loginPw.equals(loginPwConfirm) == false) {
                    System.out.println("  입력하신 비밀번호가 일치하지 않습니다.");
                    loginPwConfirmIssame = false;
                    break;
                }
                break;
            }
            if (loginPwConfirmIssame) {
                break;
            }
        }

        while (true) {
            System.out.println("  이름을 입력해주세요.");
            System.out.printf("  >> ");
            name = Container.scanner.nextLine().trim();

            if (name.length() == 0) {
                System.out.println("  이름을 입력하지 않았습니다.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.println("  생년월일을 입력해주세요. (yyyy-mm-dd)");
            System.out.printf("  >> ");
            birthDate = Container.scanner.nextLine().trim();

            if (birthDate.length() == 0) {
                System.out.println("  생년월일을 입력하지 않았습니다.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.println("  성별을 입력해주세요. (남자/여자)");
            System.out.printf("  >> ");
            gender = Container.scanner.nextLine().trim();

            if (gender.length() == 0) {
                System.out.println("  성별을 입력하지 않았습니다.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.println("  이메일 주소를 입력해주세요.");
            System.out.printf("  >> ");
            email = Container.scanner.nextLine().trim();

            if (email.length() == 0) {
                System.out.println("  이메일 주소를 입력하지 않았습니다.");
                continue;
            }
            break;
        }


        while (true) {
            userService.printGenre();
            System.out.println("-".repeat(24));
            System.out.println("  선호 장르를 입력해주세요.");
            System.out.printf("  >> ");
            inputGenre = Container.scanner.nextLine().trim();

            boolean isIngenre = userService.isIngenre(inputGenre);

            if (inputGenre.length() == 0) {
                System.out.println("  선호 장르를 입력하지 않았습니다.");
                continue;
            }

            if (!isIngenre) {
                System.out.println("  입력하신 장르는 선택할 수 없습니다.");
                continue;
            }

            break;
        }

        int id = userService.signUp(loginId, loginPw, name, birthDate, gender, email);
        userService.genreSignup(id, userService.findGenreIdByName(inputGenre));

    }

    public void getUserInformation() {
        if (Container.session.getSessionState() > 0) {
            userService.getUserInformation();
        }
    }

    public void modify() {
        if (Container.session.getSessionState() != 2) {
            Container.systemController.commandError();
            return;
        }

        System.out.println("  비밀번호를 입력해주세요.");
        System.out.printf("  >> ");
        String loginPw = Container.scanner.nextLine().trim();

        User user = userService.findByLoginPw(loginPw);

        if (user == null) {
            System.out.println("  비밀번호가 일치하지 않습니다.");
        }

        String newPw;
        String newEmail;
        while (true) {
            if (user.getLoginPw().equals(loginPw)) {
                System.out.println("  개인정보 수정을 시작합니다.");
                System.out.println("  사용하실 새로운 비밀번호를 입력해주세요. (영문, 숫자, 특수문자, 8자리 이상)");
                System.out.printf("  >> ");
                newPw = Container.scanner.nextLine().trim();

                if (newPw.replaceAll("[0-9a-zA-Z\s~!@#$%^&*()_+=]", "").length() != 0) {
                    System.out.println("  로그인 비밀번호는 영문, 숫자, 특수문자로만 입력해주세요.");
                    continue;
                }
                if (loginPw.length() < 8) {
                    System.out.println("  비밀번호는 8자리 이상으로 입력해주세요.");
                    continue;
                }

                System.out.println("  사용하실 새 이메일주소를 입력해주세요.");
                System.out.printf("  >> ");
                newEmail = Container.scanner.nextLine().trim();

                if (newEmail.length() == 0) {
                    System.out.println("  사용하실 새 이메일 주소를 입력하지 않았습니다.");
                    continue;
                }

                userService.update(newPw, newEmail);
                System.out.println("  개인정보변경이 완료되었습니다.");

                Container.session.setSessionUser(userService.findByLoginId(Container.session.getSessionUser().getLoginId()));
                break;
            }

        }
    }

    public void userPostedReview() {
        SecSql sql = new SecSql();

        sql.append("SELECT A.`id`, A.`comment`, A.`score`, A.`replayFlag`, A.regDate, A.updateDate, B.`name` AS 'userName', C.`name` AS 'contentName'");
        sql.append("FROM `review` as A");
        sql.append("INNER JOIN `user` as B");
        sql.append("ON A.`userId` = B.`id`");
        sql.append("INNER JOIN `content` as C");
        sql.append("ON A.`contentId` = C.`id`");
        sql.append("WHERE A.`userId` = ?", Container.session.getSessionUser().getId());
        sql.append("ORDER BY regDate DESC");

        List<Map<String, Object>> reviewMapList = DBUtil.selectRows(Container.connection, sql);

        if (reviewMapList.isEmpty()) {
            System.out.printf("  \"%s\"님이 남기신 리뷰가 없습니다.\n", Container.session.getSessionUser().getName());
            System.out.println("  리뷰를 남겨보세요.");
            return;
        }

        List<Review> reviewList = new ArrayList<>();
        for (Map<String, Object> reviewMap : reviewMapList) {
            reviewList.add(new Review(reviewMap));
        }

        System.out.println("https://replix.io/contents?id=%d&review?page=\n\n");
        Container.systemController.adComment();
        System.out.printf("\n  \"%s\"님이 남기신 리뷰...\n", Container.session.getSessionUser().getName());
        System.out.println("  번호 / 컨텐츠 / 내용 / 별점 / 재관람의사 / 작성시간");
        System.out.println("-".repeat(50));
        for (Review review : reviewList) {
            System.out.printf("  %2d / %s / %s / %.1f / %s / %s\n", review.getId(), review.getContentName(), review.getComment(), review.getScore(), review.isReplayFlag() ? "있음" : "없음", review.getRegDate());
        }
    }

    public void userLikedContent() {
        SecSql sql = new SecSql();

        sql.append("SELECT B.`name`");
        sql.append("FROM `likeContent` AS A");
        sql.append("INNER JOIN `content` AS B");
        sql.append("ON A.`contentId` = B.`id`");
        sql.append("WHERE A.`userId` = ?", Container.session.getSessionUser().getId());
        sql.append("ORDER BY A.`id` DESC");

        List<Map<String, Object>> userLikedContentMapList = DBUtil.selectRows(Container.connection, sql);

        if (userLikedContentMapList.isEmpty()) {
            System.out.printf("\n  \"%s\"님이 좋아요한 컨텐츠가 없습니다.\n", Container.session.getSessionUser().getName());
            System.out.println("  컨텐츠 게시판에서 좋아요를 눌러보세요");
            return;
        }

        System.out.printf("\n  \"%s\"님이 좋아요한 컨텐츠...\n\n", Container.session.getSessionUser().getName());

        int id = 1;
        for (Map<String, Object> userLikedContentMap : userLikedContentMapList) {
            System.out.printf("  %d. %s\n", id++, userLikedContentMap.get("name"));
        }
    }

    public void userDibsContent() {
        SecSql sql = new SecSql();

        sql.append("SELECT B.`name`");
        sql.append("FROM `dibsContent` AS A");
        sql.append("INNER JOIN `content` AS B");
        sql.append("ON A.`contentId` = B.`id`");
        sql.append("WHERE A.`userId` = ?", Container.session.getSessionUser().getId());
        sql.append("ORDER BY A.`id` DESC");

        List<Map<String, Object>> userDibsContentMapList = DBUtil.selectRows(Container.connection, sql);

        if (userDibsContentMapList.isEmpty()) {
            System.out.printf("\n  \"%s\"님이 찜한 컨텐츠가 없습니다.\n", Container.session.getSessionUser().getName());
            System.out.println("  컨텐츠 게시판에서 찜하기를 눌러보세요");
            return;
        }

        System.out.printf("\n  \"%s\"님이 찜한 컨텐츠...\n\n", Container.session.getSessionUser().getName());

        int id = 1;
        for (Map<String, Object> userDibsContentMap : userDibsContentMapList) {
            System.out.printf("  %d. %s\n", id++, userDibsContentMap.get("name"));
        }
    }
}
