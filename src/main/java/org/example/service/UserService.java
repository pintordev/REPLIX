package org.example.service;


import static org.example.Container.userRepository;

public class UserService {



    public int signUp(String loginId, String loginPw, String name, String birthDate, String gender, String e_mail) {
        return userRepository.signUp(loginId, loginPw, name, birthDate, gender, e_mail);
    }

    public static boolean isLoginDup(String loginId) {
        return userRepository.isLoginIdDup(loginId);
    }
}
