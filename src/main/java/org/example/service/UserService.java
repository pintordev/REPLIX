package org.example.service;

import org.example.dto.User;

import static org.example.Container.userRepository;

public class UserService {
    public int signUp(String loginId, String loginPw, String name, String birthDate, String gender, String email) {
        return userRepository.signUp(loginId, loginPw, name, birthDate, gender, email);

    }

    public int findGenreIdByName(String inputGenre){
        return userRepository.findGenreIdByName(inputGenre);
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
        userRepository.update(newPw, newEmail);
    }

    public void printGenre(){
        userRepository.printGenre();
    }

    public boolean isIngenre(String inputGenre){
        return userRepository.isIngenre(inputGenre);
    }

    public void genreSignup(int userId, int genreId) {
        userRepository.genreSignup(userId, genreId);
    }

    public void getUserInformation(){
        userRepository.getUserInformation();
    }

    public void deleteUser(int id){
        userRepository.deleteUser(id);
    }

    public User findByUserId(String name, String email) {
        return userRepository.findByUserId(name,email);
    }

    public User findByUserPw(String name, String loginId, String email) {
        return userRepository.findByUserPw(name,loginId,email);
    }

}
