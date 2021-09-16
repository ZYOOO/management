package com.managementSystem.dao;

import com.managementSystem.domain.History;
import com.managementSystem.domain.User;

import java.util.List;
import java.util.Map;

/**
 * @author ZYOOO
 * @date 2021-09-12 20:09
 */
public interface UserDao {
    boolean verifyById(int id);
    List<User> findAllUser();
    void showAllUsers();
    void addUser(User user);
    void deleteById(int id);

}
