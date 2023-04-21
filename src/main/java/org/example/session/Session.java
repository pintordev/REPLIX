package org.example.session;

import org.example.dto.User;

public class Session {
    // 로그인한 사용자에 대한 정보
    // 현재 이용중인 상태에 따라 sessionState 값을 다르게 적용
    // 0: 로그아웃 상태
    // 1: 로그인 상태 - 메인
    // 2: 로그인 상태 - 마이페이지
    // 3: 로그인 상태 - 컨텐츠게시판
    private int sessionState;
    public User sessionUser;

    public Session() {
        this.sessionState = 0;
    }

    public int getSessionState() {
        return this.sessionState;
    }

    public void login(User user) {
        this.sessionState = 1;
        this.sessionUser = user;
    }

    public void logout() {
        this.sessionState = 0;
        this.sessionUser = null;
    }

    public void goToMyPage() {
        this.sessionState = 2;
    }

    public void goToReview() {
        this.sessionState = 3;
    }

}
