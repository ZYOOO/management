package com.managementSystem.service;

import com.managementSystem.domain.History;

import java.util.List;

/**
 * @author ZYOOO
 * @date 2021-09-13 13:56
 */
public interface HistoryService {
    void addHistory(History history);
    void showAllHistory();
    void delUserHistoryById(int id);
    List<History> findUserHistoryById(int id);
}
