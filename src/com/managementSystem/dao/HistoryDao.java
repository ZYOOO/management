package com.managementSystem.dao;

import com.managementSystem.domain.History;

import java.util.List;

/**
 * @author ZYOOO
 * @date 2021-09-13 13:45
 */
public interface HistoryDao {
    void add(History history);
    void showAll();
    void delById(int id);
    List<History> findById(int id);
}
