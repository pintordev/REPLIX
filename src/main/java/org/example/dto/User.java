package org.example.dto;

import lombok.Data;

import java.util.Map;

@Data // @Data annotation으로 equals, toString, getter, setter 메서드 선언 없이 사용 가능
public class User {
    private int id; // 사용자 고유 ID
    private String loginId; // 사용자가 서비스에 로그인할 때 사용하는 ID
    private String loginPw; // 사용자가 서비스에 로그인할 때 사용하는 PW
    private String name; // 사용자 성명
    private String birthDate; // 사용자 생일
    private String gender; // 사용자 성별
    private String email; // 사용자 이메일 주소
    private String regDate; // 사용자 서비스 가입 시간
    private String updateDate; // 사용자 회원 정보 최신 수정 시간

    public User(Map<String, Object> userMap) {
        this.id = (int) userMap.get("id");
        this.loginId = (String) userMap.get("loginId");
        this.loginPw = (String) userMap.get("loginPw");
        this.name = (String) userMap.get("name");
        this.birthDate = (String) userMap.get("birthDate");
        this.gender = (String) userMap.get("gender");
        this.email = (String) userMap.get("email");
        this.regDate = (String) userMap.get("regDate");
        this.updateDate = (String) userMap.get("updateDate");
    }
}
