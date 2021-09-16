package com.managementSystem.service;

import com.managementSystem.domain.History;
import com.managementSystem.domain.User;

import java.util.List;

/**
 * @author ZYOOO
 * @date 2021-09-12 20:09
 */
public interface UserService {
    boolean verifyUserById(int id);
    List<User> findAllUsers();
    void showAllUsers();
    void addUser(User user);
    void deleteUserById(int id);

}
