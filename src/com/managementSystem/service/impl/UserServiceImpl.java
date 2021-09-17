package com.managementSystem.service.impl;

import com.managementSystem.dao.HistoryDao;
import com.managementSystem.dao.UserDao;
import com.managementSystem.dao.impl.HistoryDaoImpl;
import com.managementSystem.dao.impl.UserDaoImpl;
import com.managementSystem.domain.History;
import com.managementSystem.domain.User;
import com.managementSystem.service.HistoryService;
import com.managementSystem.service.UserService;

import java.util.List;

/**
 * @author ZYOOO
 * @date 2021-09-12 20:11
 */
public class UserServiceImpl implements UserService {
    UserDao userDao = new UserDaoImpl();
    HistoryService historyService = new HistoryServiceImpl();

    @Override
    public boolean verifyUserById(String id) {
        return userDao.verifyById(Integer.parseInt(id));
    }

    @Override
    public boolean verifyUserById(int id) {
        return userDao.verifyById(id);
    }

    @Override
    public User getUserById(int id) {
        return userDao.getById(id);
    }

    @Override
    public List<User> findAllUsers() {
        return userDao.findAllUser();
    }

    @Override
    public void showAllUsers() {
        userDao.showAllUsers();
    }

    @Override
    public void addUser(User user) {
        userDao.addUser(user);
    }

    @Override
    public void deleteUserById(int id) {
        userDao.deleteById(id);
        historyService.delUserHistoryById(id);
    }

}
