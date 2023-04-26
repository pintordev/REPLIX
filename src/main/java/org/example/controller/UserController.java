package org.example.controller;

import org.example.Container;
import org.example.dto.Review;
import org.example.dto.User;
import org.example.service.UserService;
import org.example.util.DBUtil;
import org.example.util.MailSender;
import org.example.util.SecSql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class UserController {
    private UserService userService;

    public UserController() {
        userService = new UserService();
    }

    // 로그인 코드
    public void login() {
        int loginLimit = 3;
        int loginTry = 0;

        System.out.println("-".repeat(30));

        while (true) {
            if (loginTry >= loginLimit) {
                System.out.println("로그인 과정에서 오류가 3회 발생하였습니다.");
                System.out.println("아이디와 비밀번호를 확인 후 다시 시도해주세요.");
                break;
            }

            System.out.println("아이디를 입력해주세요. [입력 종료: \"q;\"]");
            System.out.printf(">> ");
            String loginId = Container.scanner.nextLine().trim();

            if (Container.systemController.isQuit(loginId)) return;

            System.out.println("비밀번호를 입력해주세요. [입력 종료: \"q;\"]");
            System.out.printf(">> ");
            String loginPw = Container.scanner.nextLine().trim();

            if (Container.systemController.isQuit(loginPw)) return;

            User user = userService.findByLoginId(loginId);

            if (user == null) {
                loginTry++;
                System.out.println("존재하지 않는 아이디입니다."); // 아이디가 다르면
                System.out.println("REPLIX 앱 로그인에 실패하였습니다.");
                continue;
            }

            if (!user.getLoginPw().equals(loginPw)) {
                loginTry++;
                System.out.println("옳바르지 않은 비밀번호입니다."); // 비밀번호가 다르면
                System.out.println("REPLIX 앱 로그인에 실패하였습니다.");
                System.out.println("-".repeat(24));
                continue;
            }

            System.out.println("REPLIX 앱 로그인에 성공하였습니다.");
            System.out.printf("%s님 환영합니다.\n", user.getName());
            Container.session.login(user);
            break;
        }
    }

    // 로그 아웃 코드
    public void logout() {
        System.out.println("REPLIX 앱에서 로그아웃합니다.");
        Container.session.logout();
    }

    // 회원 가입 코드
    public void signUp() {
        String loginId;
        String loginPw;
        String loginPwConfirm;
        String name;
        String birthDate;
        String gender;
        String email;
        String favoriteGenre;

        System.out.println("-".repeat(30));
        while (true) {
            System.out.println("사용하실 로그인 아이디를 입력해주세요. (영문, 숫자) [입력 종료: \"q;\"]");
            System.out.printf(">> ");
            loginId = Container.scanner.nextLine().trim();

            if (Container.systemController.isQuit(loginId)) return;

            if (loginId.length() == 0) {
                System.out.println("로그인 아이디를 입력하지 않았습니다.\n");
                continue;
            }

            if (loginId.replaceAll("[0-9a-zA-Z]", "").length() != 0) {
                System.out.println("로그인 아이디는 영문, 숫자로만 입력해주세요.\n");
                continue;
            }

            if (loginId.replaceAll("[0-9]", "").equals(loginId)) {
                System.out.println("로그인 아이디에 숫자는 반드시 포함되어야 합니다.\n");
                continue;
            }

            if (loginId.replaceAll("[a-zA-Z]", "").equals(loginId)) {
                System.out.println("로그인 아이디에 영문은 반드시 포함되어야 합니다.\n");
                continue;
            }

            if (userService.duplicateId(loginId)) {
                System.out.printf("\"%s\" 는 이미 사용중인 로그인 아이디입니다. 다른 로그인 아이디로 회원가입을 시도해주세요.\n\n", loginId);
                continue;
            }

            System.out.println("");

            break;
        }

        while (true) {
            System.out.println("사용하실 로그인 비밀번호를 입력해주세요. (영문, 숫자, 특수문자(!, @, #, $, %, ^, &, *), 8자리 이상) [입력 종료: \"q;\"]");
            System.out.printf(">> ");
            loginPw = Container.scanner.nextLine().trim();

            if (Container.systemController.isQuit(loginPw)) return;

            if (loginPw.length() == 0) {
                System.out.println("로그인 비밀번호를 입력하지 않았습니다.\n");
                continue;
            }

            if (loginPw.length() < 8) {
                System.out.println("로그인 비밀번호는 8자리 이상으로 입력해주세요.\n");
                continue;
            }

            if (loginPw.replaceAll("[a-zA-z0-9$!$@#$$%^&*]", "").length() != 0) {
                System.out.println("로그인 비밀번호는 영문, 숫자, 특수문자(!, @, #, $, %, ^, &, *)로만 입력해주세요.\n");
                continue;
            }

            if (loginPw.replaceAll("[0-9]", "").equals(loginPw)) {
                System.out.println("로그인 비밀번호에 숫자는 반드시 포함되어야 합니다.\n");
                continue;
            }

            if (loginPw.replaceAll("[a-zA-Z]", "").equals(loginPw)) {
                System.out.println("로그인 비밀번호에 영문은 반드시 포함되어야 합니다.\n");
                continue;
            }

            if (loginPw.replaceAll("[$!$@#$$%^&*]", "").equals(loginPw)) {
                System.out.println("로그인 비밀번호에 특수문자(!, @, #, $, %, ^, &, *)는 반드시 포함되어야 합니다.\n");
                continue;
            }

            System.out.println("");

            boolean isSameLoginPw = true;

            while (true) {
                System.out.println("입력하신 로그인 비밀번호를 다시 입력해주세요. [입력 종료: \"q;\"]");
                System.out.printf(">> ");
                loginPwConfirm = Container.scanner.nextLine().trim();

                if (Container.systemController.isQuit(loginPwConfirm)) return;

                if (loginPwConfirm.length() == 0) {
                    System.out.println("로그인 비밀번호를 입력하지 않았습니다.\n");
                    continue;
                }

                if (!loginPw.equals(loginPwConfirm)) {
                    System.out.println("입력하신 로그인 비밀번호가 일치하지 않습니다.\n");
                    isSameLoginPw = false;
                    continue;
                }

                System.out.println("");

                break;
            }

            if (isSameLoginPw) {
                break;
            }
        }

        while (true) {
            System.out.println("이름을 입력해주세요. [입력 종료: \"q;\"]");
            System.out.printf(">> ");
            name = Container.scanner.nextLine().trim();

            if (Container.systemController.isQuit(name)) return;

            if (name.length() == 0) {
                System.out.println("이름을 입력하지 않았습니다.\n");
                continue;
            }

            System.out.println("");

            break;
        }

        while (true) {
            System.out.println("생년월일을 입력해주세요. (yyyy-mm-dd) [입력 종료: \"q;\"]");
            System.out.printf(">> ");
            birthDate = Container.scanner.nextLine().trim();

            if (Container.systemController.isQuit(birthDate)) return;

            if (birthDate.length() == 0) {
                System.out.println("생년월일을 입력하지 않았습니다.\n");
                continue;
            }

            if (!Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", birthDate)) {
                System.out.println("올바른 생년월일 양식으로 입력해주세요. (yyyy-mm-dd)\n");
                continue;
            }

            System.out.println("");

            break;
        }

        while (true) {
            System.out.println("성별을 입력해주세요. (남자/여자) [입력 종료: \"q;\"]");
            System.out.printf(">> ");
            gender = Container.scanner.nextLine().trim();

            if (Container.systemController.isQuit(gender)) return;

            if (gender.length() == 0) {
                System.out.println("성별을 입력하지 않았습니다.\n");
                continue;
            }

            if (!gender.equals("남자") && !gender.equals("여자")) {
                System.out.println("남자 또는 여자만 입력해주세요.\n");
                continue;
            }

            System.out.println("");

            break;
        }

        String certificationCode = "";
        String inputCode;
        while (true) {
            System.out.println("이메일 주소를 입력해주세요. [입력 종료: \"q;\"]");
            System.out.printf(">> ");
            email = Container.scanner.nextLine().trim();

            if (Container.systemController.isQuit(email)) return;

            if (email.length() == 0) {
                System.out.println("이메일 주소를 입력하지 않았습니다.\n");
                continue;
            }

            if (!Pattern.matches("^\\w+\\.\\w+@\\w+\\.\\w+(\\.\\w+)?$", email) && !Pattern.matches("^\\w+@\\w+\\.\\w+(\\.\\w+)?$", email)) {
                System.out.println("올바른 이메일 양식으로 입력해주세요.\n");
                continue;
            }

            certificationCode = new MailSender().emailCertification(email, "가입");

            System.out.println("\n입력하신 이메일로 인증코드를 발송했습니다.");
            System.out.println("발송된 인증코드를 입력해주세요.");
            System.out.printf(">> ");
            inputCode = Container.scanner.nextLine().trim();

            if (!inputCode.equals(certificationCode)) {
                System.out.println("인증코드가 일치하지 않습니다. 다시 메일을 입력해주세요.\n");
                continue;
            }

            System.out.println("인증코드가 일치합니다. 이메일 인증이 완료되었습니다.\n");

            break;
        }

        while (true) {
            userService.printGenre();
            System.out.println("선호 장르를 입력해주세요. [입력 종료: \"q;\"]");
            System.out.printf(">> ");
            favoriteGenre = Container.scanner.nextLine().trim();

            if (Container.systemController.isQuit(favoriteGenre)) return;

            boolean isInGenre = userService.isIngenre(favoriteGenre);

            if (favoriteGenre.length() == 0) {
                System.out.println("선호 장르를 입력하지 않았습니다.\n");
                continue;
            }

            if (!isInGenre) {
                System.out.println("입력하신 장르는 선택할 수 없습니다.\n");
                continue;
            }

            System.out.println("");

            break;
        }

        System.out.println("회원가입이 완료되었습니다.");
        System.out.printf("%s님 환영합니다.\n", name);

        int id = userService.signUp(loginId, loginPw, name, birthDate, gender, email);
        userService.genreSignup(id, userService.findGenreIdByName(favoriteGenre));
    }

    // 회원 정보 확인 코드
    public void getUserInformation() {
        if (Container.session.getSessionState() != 2) {
            Container.systemController.commandError();
            return;
        }
        userService.getUserInformation();
    }

    // 회원 정보 수정 코드
    public void modify() {
        if (Container.session.getSessionState() != 2) {
            Container.systemController.commandError();
            return;
        }

        User user;

        while (true) {
            System.out.println("로그인 비밀번호를 입력해주세요. [입력 종료: \"q;\"]");
            System.out.printf(">> ");
            String loginPw = Container.scanner.nextLine().trim();

            if (Container.systemController.isQuit(loginPw)) return;

            user = userService.findByLoginPw(loginPw);

            if (user == null) {
                System.out.println("로그인 비밀번호가 일치하지 않습니다.\n");
                continue;
            }

            System.out.println("");

            break;
        }

        String modifyLoginPw = "";
        String modifyLoginPwConfrim;
        String modifyEmail = "";

        System.out.println("회원정보 수정을 시작합니다.\n");

        String answer;
        boolean isLoginPwModified = false;

        while (true) {

            while (true) {
                System.out.println("로그인 비밀번호를 수정하시겠습니까? (Y/N) [입력 종료: \"q;\"]");
                System.out.printf(">> ");
                answer = Container.scanner.nextLine().trim().toLowerCase();

                if (Container.systemController.isQuit(answer)) return;

                if (!answer.equals("y") && !answer.equals("n")) {
                    System.out.println("Y 또는 N 으로 입력해주세요.\n");
                }

                break;
            }

            if (answer.equals("n")) break;

            System.out.println("사용하실 새로운 로그인 비밀번호를 입력해주세요. (영문, 숫자, 특수문자(!, @, #, $, %, ^, &, *), 8자리 이상) [입력 종료: \"q;\"]");
            System.out.printf(">> ");
            modifyLoginPw = Container.scanner.nextLine().trim();

            if (Container.systemController.isQuit(modifyLoginPw)) return;

            if (modifyLoginPw.length() == 0) {
                System.out.println("로그인 비밀번호를 입력하지 않았습니다.\n");
                continue;
            }

            if (modifyLoginPw.length() < 8) {
                System.out.println("로그인 비밀번호는 8자리 이상으로 입력해주세요.\n");
                continue;
            }

            if (modifyLoginPw.replaceAll("[a-zA-z0-9$!$@#$$%^&*]", "").length() != 0) {
                System.out.println("로그인 비밀번호는 영문, 숫자, 특수문자(!, @, #, $, %, ^, &, *)로만 입력해주세요.\n");
                continue;
            }

            if (modifyLoginPw.replaceAll("[0-9]", "").equals(modifyLoginPw)) {
                System.out.println("로그인 비밀번호에 숫자는 반드시 포함되어야 합니다.\n");
                continue;
            }

            if (modifyLoginPw.replaceAll("[a-zA-Z]", "").equals(modifyLoginPw)) {
                System.out.println("로그인 비밀번호에 영문은 반드시 포함되어야 합니다.\n");
                continue;
            }

            if (modifyLoginPw.replaceAll("[$!$@#$$%^&*]", "").equals(modifyLoginPw)) {
                System.out.println("로그인 비밀번호에 특수문자(!, @, #, $, %, ^, &, *)는 반드시 포함되어야 합니다.\n");
                continue;
            }

            System.out.println("");

            boolean isSameLoginPw = true;

            while (true) {
                System.out.println("입력하신 로그인 비밀번호를 다시 입력해주세요. [입력 종료: \"q;\"]");
                System.out.printf(">> ");
                modifyLoginPwConfrim = Container.scanner.nextLine().trim();

                if (Container.systemController.isQuit(modifyLoginPwConfrim)) return;

                if (modifyLoginPwConfrim.length() == 0) {
                    System.out.println("로그인 비밀번호를 입력하지 않았습니다.\n");
                    continue;
                }

                if (!modifyLoginPw.equals(modifyLoginPwConfrim)) {
                    System.out.println("입력하신 로그인 비밀번호가 일치하지 않습니다.\n");
                    isSameLoginPw = false;
                    continue;
                }

                System.out.println("");

                break;
            }

            if (isSameLoginPw) {
                isLoginPwModified = true;
                break;
            }
        }

        String certificationCode = "";
        String inputCode;
        while (true) {

            while (true) {
                System.out.println("이메일을 수정하시겠습니까? (Y/N) [입력 종료: \"q;\"]");
                System.out.printf(">> ");
                answer = Container.scanner.nextLine().trim().toLowerCase();

                if (Container.systemController.isQuit(answer)) return;

                if (!answer.equals("y") && !answer.equals("n")) {
                    System.out.println("Y 또는 N 으로 입력해주세요.\n");
                }

                break;
            }

            if (answer.equals("n")) break;

            System.out.println("새로운 이메일 주소를 입력해주세요. [입력 종료: \"q;\"]");
            System.out.printf(">> ");
            modifyEmail = Container.scanner.nextLine().trim();

            if (Container.systemController.isQuit(modifyEmail)) return;

            if (modifyEmail.length() == 0) {
                System.out.println("이메일 주소를 입력하지 않았습니다.\n");
                continue;
            }

            if (!Pattern.matches("^\\w+\\.\\w+@\\w+\\.\\w+(\\.\\w+)?$", modifyEmail) && !Pattern.matches("^\\w+@\\w+\\.\\w+(\\.\\w+)?$", modifyEmail)) {
                System.out.println("올바른 이메일 양식으로 입력해주세요.\n");
                continue;
            }

            certificationCode = new MailSender().emailCertification(modifyEmail, "이메일 수정");

            System.out.println("\n입력하신 이메일로 인증코드를 발송했습니다.");
            System.out.println("발송된 인증코드를 입력해주세요.");
            System.out.printf(">> ");
            inputCode = Container.scanner.nextLine().trim();

            if (!inputCode.equals(certificationCode)) {
                System.out.println("인증코드가 일치하지 않습니다. 다시 메일을 입력해주세요.\n");
                continue;
            }

            System.out.println("인증코드가 일치합니다. 이메일 인증이 완료되었습니다.\n");

            break;
        }

        userService.update(modifyLoginPw, modifyEmail);
        Container.session.setSessionUser(userService.findByLoginId(Container.session.getSessionUser().getLoginId()));

        System.out.println("개인정보변경이 완료되었습니다.");

        if (isLoginPwModified) {
            System.out.println("로그인 비밀번호가 변경되어 로그아웃됩니다. 다시 로그인 해주세요.");
            Container.session.logout();
        }
    }

    public void deleteUser() {
        if (Container.session.getSessionState() != 2) return;

        String answer;

        while (true) {
            System.out.println("정말 탈퇴하시겠습니까? (Y/N) [입력 종료: \"q;\"]");
            System.out.printf(">> ");
            answer = Container.scanner.nextLine().trim().toLowerCase();

            if (Container.systemController.isQuit(answer)) return;

            if (!answer.equals("y") && !answer.equals("n")) {
                System.out.println("Y 또는 N 으로 입력해주세요.\n");
                continue;
            }

            break;
        }

        if (answer.equals("n")) return;

        System.out.printf("%s님이 남겨주신 \"REPLIX\" 와의 좋은 추억 간직할게요. T.T\n", Container.session.getSessionUser().getName());

        userService.deleteUser(Container.session.getSessionUser().getId());
        Container.session.setSessionUser(null);
        Container.session.setSessionState(0);
    }

    public void userFindId() {
        System.out.println("이름을 입력해주세요. [입력 종료: \"q;\"]");
        System.out.printf(">> ");
        String name = Container.scanner.nextLine().trim();

        if (Container.systemController.isQuit(name)) return;

        while (true) {
            System.out.println("가입 시 사용했던 이메일을 입력해주세요. [입력 종료: \"q;\"]");
            System.out.printf(">> ");
            String email = Container.scanner.nextLine().trim();

            if (Container.systemController.isQuit(email)) return;

            if (!Pattern.matches("^\\w+\\.\\w+@\\w+\\.\\w+(\\.\\w+)?$", email) && !Pattern.matches("^\\w+@\\w+\\.\\w+(\\.\\w+)?$", email)) {
                System.out.println("올바른 이메일 양식으로 입력해주세요.\n");
                continue;
            }

            User user = userService.findByUserId(name, email);

            if (user == null) {
                System.out.println("입력하신 정보와 일치하는 아이디가 존재하지 않습니다.\n");
                continue;
            }

            System.out.println(user.getLoginId());

            break;
        }
    }

    public void userFindPw() {
        System.out.println("이름을 입력해주세요. [입력 종료: \"q;\"]");
        System.out.printf(">> ");
        String name = Container.scanner.nextLine().trim();

        if (Container.systemController.isQuit(name)) return;

        System.out.println("로그인 아이디를 입력해주세요. [입력 종료: \"q;\"]");
        System.out.printf(">> ");
        String loginId = Container.scanner.nextLine().trim();

        if (Container.systemController.isQuit(loginId)) return;

        String certificationCode = "";
        String inputCode;
        while (true) {
            System.out.println("이메일주소를 입력해주세요. [입력 종료: \"q;\"]");
            String email = Container.scanner.nextLine().trim();

            if (Container.systemController.isQuit(email)) return;

            if (!Pattern.matches("^\\w+\\.\\w+@\\w+\\.\\w+(\\.\\w+)?$", email) && !Pattern.matches("^\\w+@\\w+\\.\\w+(\\.\\w+)?$", email)) {
                System.out.println("올바른 이메일 양식으로 입력해주세요.\n");
                continue;
            }

            User user = userService.findByUserPw(name, loginId, email);

            if (user == null) {
                System.out.println("입력하신 정보와 일치하는 아이디가 존재하지않습니다.");
                continue;
            }

            certificationCode = new MailSender().emailCertification(email, "비밀번호 찾기");

            System.out.println("\n입력하신 이메일로 인증코드를 발송했습니다.");
            System.out.println("발송된 인증코드를 입력해주세요.");
            System.out.printf("  >> ");
            inputCode = Container.scanner.nextLine().trim();

            if (!inputCode.equals(certificationCode)) {
                System.out.println("인증코드가 일치하지 않습니다. 다시 메일을 입력해주세요.\n");
                continue;
            }

            System.out.println("인증코드가 일치합니다. 이메일 인증이 완료되었습니다.");
            System.out.printf("%s님의 비밀번호: %s\n", user.getName(), user.getLoginPw());

            break;

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
            System.out.printf("\"%s\"님이 남기신 리뷰가 없습니다.\n", Container.session.getSessionUser().getName());
            System.out.println("리뷰를 남겨보세요.");
            return;
        }

        List<Review> reviewList = new ArrayList<>();
        for (Map<String, Object> reviewMap : reviewMapList) {
            reviewList.add(new Review(reviewMap));
        }

        System.out.println("https://replix.io/contents?id=%d&review?page=\n\n");
        Container.systemController.adComment();
        System.out.printf("\n\"%s\"님이 남기신 리뷰...\n\n", Container.session.getSessionUser().getName());
        System.out.println("번호 / 컨텐츠 / 내용 / 별점 / 재관람의사 / 작성시간");
        System.out.println("-".repeat(50));
        for (Review review : reviewList) {
            System.out.printf("%2d / %s / %s / %.1f / %s / %s\n", review.getId(), review.getContentName(), review.getComment(), review.getScore(), review.isReplayFlag() ? "있음" : "없음", review.getRegDate());
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
            System.out.printf("\n\"%s\"님이 좋아요한 컨텐츠가 없습니다.\n", Container.session.getSessionUser().getName());
            System.out.println("컨텐츠 게시판에서 좋아요를 눌러보세요");
            return;
        }

        System.out.printf("\n\"%s\"님이 좋아요한 컨텐츠...\n\n", Container.session.getSessionUser().getName());

        int id = 1;
        for (Map<String, Object> userLikedContentMap : userLikedContentMapList) {
            System.out.printf("%d. %s\n", id++, userLikedContentMap.get("name"));
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
            System.out.printf("\n\"%s\"님이 찜한 컨텐츠가 없습니다.\n", Container.session.getSessionUser().getName());
            System.out.println("컨텐츠 게시판에서 찜하기를 눌러보세요");
            return;
        }

        System.out.printf("\n\"%s\"님이 찜한 컨텐츠...\n\n", Container.session.getSessionUser().getName());

        int id = 1;
        for (Map<String, Object> userDibsContentMap : userDibsContentMapList) {
            System.out.printf("%d. %s\n", id++, userDibsContentMap.get("name"));
        }
    }
}
