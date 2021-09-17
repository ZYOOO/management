package com.managementSystem.dao.impl;

import com.managementSystem.dao.HistoryDao;
import com.managementSystem.domain.History;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZYOOO
 * @date 2021-09-13 13:45
 */
public class HistoryDaoImpl implements HistoryDao {
    public static final String HISTORY_PATH = "src/com/managementSystem/files/history.txt";

    @Override
    public void add(History history) {
        Map<Integer,List<History>> map = new HashMap<>();
        List<History>  list = new ArrayList<History>();
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        File historyFile = new File(HISTORY_PATH);
        try{
            if(historyFile.length() != 0) {
                ois = new ObjectInputStream(new FileInputStream(HISTORY_PATH));
                map = (Map<Integer, List<History>>) ois.readObject();
            }
            list = map.get(history.getId());
            list.add(history);
            map.put(history.getId(),list);
            oos = new ObjectOutputStream(new FileOutputStream(HISTORY_PATH));
            oos.writeObject(map);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }finally {
            try {
                if(ois != null){
                    ois.close();
                }
                if(oos != null){
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showAll() {
        Map<Integer,List<History>> map = new HashMap<>();
        List<History>  list = new ArrayList<History>();
        ObjectInputStream ois = null;
        File historyFile = new File(HISTORY_PATH);
        try{
            if(historyFile.length() != 0) {
                ois = new ObjectInputStream(new FileInputStream(HISTORY_PATH));
                map = (Map<Integer, List<History>>) ois.readObject();
            }
            for (Integer key : map.keySet()) {
                for (History history : map.get(key)) {
                    System.out.println(history.toString());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }finally {
            try {
                if(ois != null){
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delById(int id) {
        Map<Integer,List<History>> map = new HashMap<>();
        List<History>  list = new ArrayList<History>();
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try{
            ois = new ObjectInputStream(new FileInputStream(HISTORY_PATH));
            map = (Map<Integer, List<History>>) ois.readObject();
            list = map.get(id);
            if(list != null){
                list.clear();
            }
            map.put(id,list);
            oos = new ObjectOutputStream(new FileOutputStream(HISTORY_PATH));
            oos.writeObject(map);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }finally {
            try {
                if(ois != null){
                    ois.close();
                }
                if(oos != null){
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<History> findById(int id) {
        Map<Integer,List<History>> map = new HashMap<>();
        List<History>  list = new ArrayList<History>();
        ObjectInputStream ois = null;
        try{
            ois = new ObjectInputStream(new FileInputStream(HISTORY_PATH));
            map = (Map<Integer, List<History>>) ois.readObject();
            list = map.get(id);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }finally {
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
}
