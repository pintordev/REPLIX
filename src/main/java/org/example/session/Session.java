package org.example.session;

import org.example.dto.Content;
import org.example.dto.User;

public class Session {
    // 로그인한 사용자에 대한 정보
    // 현재 이용중인 상태에 따라 sessionState 값을 다르게 적용
    // 0: 로그아웃 상태
    // 1: 로그인 상태 - 메인
    // 2: 로그인 상태 - 마이페이지
    // 3: 로그인 상태 - 컨텐츠게시판
    // 4: 로그인 상태 - 리뷰게시판
    private int sessionState;
    private User sessionUser;
    private Content sessionContent;

    public Session() {
        this.sessionState = 0;
    }

    public int getSessionState() {
        return sessionState;
    }

    public void setSessionState(int sessionState) {
        this.sessionState = sessionState;
    }

    public User getSessionUser() {
        return sessionUser;
    }

    public void setSessionUser(User sessionUser) {
        this.sessionUser = sessionUser;
    }

    public Content getSessionContent() {
        return sessionContent;
    }

    public void setSessionContent(Content sessionContent) {
        this.sessionContent = sessionContent;
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

    public void goToContent() {
        this.sessionState = 3;
    }

    public void goToReview() {
        this.sessionState = 4;
    }

}
