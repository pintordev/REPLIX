package org.example.controller;

import org.example.Container;
import org.example.dto.User;
import org.example.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserController {
    private ArrayList<User> userList;
    private UserService userService;

    public UserController() {
        this.userList = new ArrayList<>();
        this.userService = new UserService();
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

        User user = userService.findByLoginId(loginId);

        if(user == null){
            System.out.println("  존재하지 않는 아이디입니다."); // 아이디가 다르면
            System.out.println("  REPLIX 앱 로그인에 실패하였습니다."); // 두 경우 모두 이 구문은 출력'
            login();
            return;
        }

        int loginlimit = 3;
        int loginTry = 0 ;

        while (true){

            System.out.println("  비밀번호를 입력해주세요.");
            System.out.printf("  >> ");
            String loginPw = Container.scanner.nextLine().trim();

            if(loginTry == loginlimit){
                System.out.println("  REPLIX 앱 로그인에 실패하였습니다.");
                System.out.println("비밀번호 5회 오류 비밀번호 확인 후 다음에 다시 시도해주세요");
                break;
            }
            if(user.getLoginPw().equals(loginPw)== false){
                System.out.println("  일치하지 않은 비밀번호입니다."); // 비밀번호가 다르면
                System.out.println("-".repeat(24));
                loginTry++;
                continue;
            }else if(user.getLoginPw().equals(loginPw)==true){
                System.out.println("  REPLIX 앱 로그인에 성공하였습니다.");
            }

            System.out.printf("  %s님 환영합니다.\n", user.getName());
            Container.session.login(user);
            break;
        }
    }



    public void logout() {
        System.out.println("  REPLIX 앱에서 로그아웃합니다.");
        Container.session.logout();
        System.out.println("로그아웃 되었습니다.");
    }

    public void signUp() {
        String loginId;
        String loginPw;
        String loginPwConfirm;
        String name;
        String birthDate;
        String gender;
        String email;

        while (true){
            System.out.println("-".repeat(24));
            System.out.println("사용하실 로그인 아이디를 입력해주세요. : ");
            loginId = Container.scanner.nextLine().trim();

            if(loginId.length() == 0){
                System.out.println("로그인 아이디를 입력하지 않았습니다. ");
                continue;
            }

            if(loginId.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")){
                System.out.println("아이디는 영문 또는 숫자로만 입력해주세요.");

                continue;
            }

            boolean duplicateId = userService.duplicateId(loginId);

            if(duplicateId){
                System.out.println(loginId + "(은)는 이미 사용중인 아이디입니다.");
                continue;
            }
            break;
        }

        while (true){
            System.out.println("-".repeat(24));
            System.out.println("사용하실 로그인 비밀번호를 입력해주세요. : ");
            loginPw = Container.scanner.nextLine().trim();

            if(loginPw.length() == 0){
                System.out.println("로그인 비밀번호를 입력하지 않았습니다.");
                continue;
            }else if(loginPw.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")){
                System.out.println("로그인 비밀번호는 영문 또는 숫자로만 입력해주세요. ");
                continue;
            }else if(loginPw.length() < 8){
                System.out.println("비밀번호는 8글자 이상으로 입력해주세요.");
                continue;
            }

            boolean loginPwConfirmIssame = true;

            while (true){
                System.out.println("로그인 비밀번호를 확인합니다.");
                loginPwConfirm = Container.scanner.nextLine().trim();

                if(loginPwConfirm.length()==0){
                    System.out.println("비밀번호를 입력하지 않았습니다.");
                    continue;
                }

                if(loginPw.equals(loginPwConfirm) == false){
                    System.out.println("입력하신 비밀번호가 일치하지 않습니다.");
                    loginPwConfirmIssame = false;
                    break;
                }
            break;
            }
            if(loginPwConfirmIssame){
                break;
            }
        }

        while (true){
            System.out.printf("이름 : ");
            name = Container.scanner.nextLine().trim();

            if(name.length()==0){
                System.out.println("이름을 입력하지 않았습니다.");
                continue;
            }
            break;
        }

        while (true){
            System.out.printf("생년월일 : ");
            birthDate = Container.scanner.nextLine().trim();

            if(birthDate.length()==0){
                System.out.println("생년월일을 입력하지 않았습니다.");
                continue;
            }
            break;
        }

        while (true){
            System.out.printf("성별 : ");
            gender = Container.scanner.nextLine().trim();

            if(gender.length()==0){
                System.out.println("성별을 입력하지 않았습니다.");
                continue;
            }
            break;
        }

        while (true){
            System.out.printf("이메일주소 : ");
            email = Container.scanner.nextLine().trim();

            if(email.length()==0){
                System.out.println("이메일주소를 입력하지 않았습니다.");
                continue;
            }
            break;
        }


        int id = userService.signUp(loginId, loginPw, name, birthDate, gender, email);




    }


    public void myPage() {
        System.out.println("  기능 구현 예정입니다.");
    }
}

