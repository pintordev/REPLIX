package org.example.service;


import static org.example.Container.userRepository;

public class UserService {



    public int signUp(String loginId, String loginPw, String name, String birthDate, String gender, String email) {
        return userRepository.signUp(loginId, loginPw, name, birthDate, gender, email);
    }

    public static boolean isLoginDup(String loginId) {
        return userRepository.isLoginIdDup(loginId);
    }
}
