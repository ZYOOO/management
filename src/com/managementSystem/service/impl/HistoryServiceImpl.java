package com.managementSystem.service.impl;

import com.managementSystem.dao.HistoryDao;
import com.managementSystem.dao.impl.HistoryDaoImpl;
import com.managementSystem.domain.History;
import com.managementSystem.service.HistoryService;

import java.util.List;

/**
 * @author ZYOOO
 * @date 2021-09-13 13:56
 */
public class HistoryServiceImpl implements HistoryService {
    HistoryDao historyDao = new HistoryDaoImpl();

    @Override
    public void showAllHistory() {
        historyDao.showAll();;
    }

    @Override
    public void delUserHistoryById(int id) {
        historyDao.delById(id);
    }

    @Override
    public List<History> findUserHistoryById(int id) {
        return historyDao.findById(id);
    }

    @Override
    public void addHistory(History history) {
        historyDao.add(history);
    }
}
