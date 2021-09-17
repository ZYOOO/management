package com.managementSystem.dao.impl;

import com.managementSystem.dao.UserDao;
import com.managementSystem.domain.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * @author ZYOOO
 * @date 2021-09-12 20:11
 */
public class UserDaoImpl implements UserDao {
    public static final String USER_PATH = "/files/user.txt";
    @Override
    public boolean verifyById(int id) {
        List<User> list = findAllUser();
        for (User user : list) {
            if(id == user.getID()){
                return true;
            }
        }
        return false;
    }

    @Override
    public User getById(int id) {
        List<User> list = findAllUser();
        for (User user : list) {
            if(id == user.getID()){
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> findAllUser() {
        ObjectInputStream ois = null;
        File userFile = new File(USER_PATH);
        List<User>  list = new ArrayList<User>();
        try{
            if(userFile.length() != 0 ) {
                ois = new ObjectInputStream(new FileInputStream(USER_PATH));
                list = (List<User>) ois.readObject();
            }
        }catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if(ois != null){
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public void showAllUsers() {
        List<User> list = findAllUser();
        if(list != null){
            for (User user : list) {
                System.out.println(user.toString());
            }
        }else{
            System.out.println("暂无数据");
        }
    }

    @Override
    public void addUser(User user) {
        ObjectOutputStream oos = null;
        try{
            List<User> list = findAllUser();
            for (User user1 : list) {
                if(user1.getID() == user.getID()){
                    return;
                }
            }
            list.add(user);
            oos = new ObjectOutputStream(new FileOutputStream(USER_PATH));
            oos.writeObject(list);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }finally {
            try {
                if(oos != null){
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteById(int id) {
        if(id == 0){
            return;
        }
        ObjectOutputStream oos = null;
        int i = 0;
        int index = -1;
        try{
            List<User> list = findAllUser();
            for (User user : list) {
                if(user.getID() == id){
                    index = i;
                }
                i++;
            }
            if(index != -1){
                list.remove(index);
            }
            oos = new ObjectOutputStream(new FileOutputStream(USER_PATH));
            oos.writeObject(list);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }finally {
            try {
                if(oos != null){
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
