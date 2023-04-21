package org.example;

import org.example.controller.*;
import org.example.repository.*;
import org.example.service.*;
import org.example.session.Session;

import java.sql.Connection;
import java.util.Scanner;

public class Container {
    // Container 클래스에 서비스 도중 한번만 선언해서 프로그램 여러 메소드 상에서 계속해서 호출하여 사용할 객체들을 선언해둔다.

    // 이하에서 entity 는 사용할 entity name 들에 대한 일반화 표현
    // (e.g. entity name == "article", then entityRepository == articleRepository)
    //  entityRepository 선언
    public static UserRepository userRepository;
    public static ContentRepository contentRepository;
    public static ReviewRepository reviewRepository;
    public static ReviewEvaluationRepository reviewEvaluationRepository;

    // entityService 선언
    public static UserService userService;
    public static ContentService contentService;
    public static ReviewService reviewService;
    public static ReviewEvaluationService reviewEvaluationService;

    // entityController 선언
    public static UserController userController;
    public static ContentController contentController;
    public static ReviewController reviewController;
    public static ReviewEvaluationController reviewEvaluationController;
    public static SystemController systemController;

    // CLI 입력을 위한 scanner 변수 선언
    public static Scanner scanner;

    // 현재 서비스에 접속해서 사용하는 사용자에 대한 정보를 담아두는 session 변수 선언
    public static Session session;

    // SQL 연결을때 위한 conn 변수 선언, 실체화는 run 메소드 내에서 실행
    public static Connection connection;

    // Container 내 변수들을 실체화하는 메서드, 단 한번 실행해서 프로그램 상에 올려둔다.
    public static void init() {
        // controller 초기화
        userController = new UserController();
        contentController = new ContentController();
        reviewController = new ReviewController();
        reviewEvaluationController = new ReviewEvaluationController();
        systemController = new SystemController();

        // service 초기화
        userService = new UserService();
        contentService = new ContentService();
        reviewService = new ReviewService();
        reviewEvaluationService = new ReviewEvaluationService();

        // repository 초기화
        userRepository = new UserRepository();
        contentRepository = new ContentRepository();
        reviewRepository = new ReviewRepository();
        reviewEvaluationRepository = new ReviewEvaluationRepository();

        // scanner 초기화
        scanner = new Scanner(System.in);

        // session 초기화
        session = new Session();
    }
}