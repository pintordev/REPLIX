package org.example.service;


import org.example.dto.User;

import static org.example.Container.userRepository;

public class UserService {



    public int signUp(String loginId, String loginPw, String name, String birthDate, String gender, String email) {
        return userRepository.signUp(loginId, loginPw, name, birthDate, gender, email);
    }

    public static boolean duplicateId(String loginId) {
        return userRepository.duplicateId(loginId);
    }

    public User findByLoginId(String loginId){
        return  userRepository.findByLoginId(loginId);
    }

    public User findByLoginPw(String loginPw){
        return userRepository.findByLoginPw(loginPw);
    }

    public void update(String newPw, String newEmail){
        userRepository.update(newPw,newEmail);
    }

}
