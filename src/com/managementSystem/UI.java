package com.managementSystem;

import com.managementSystem.domain.History;
import com.managementSystem.domain.User;
import com.managementSystem.service.HistoryService;
import com.managementSystem.service.UserService;
import com.managementSystem.service.impl.HistoryServiceImpl;
import com.managementSystem.service.impl.UserServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.util.List;

/**
 * @author ZYOOO
 * @date 2021-09-12 20:06
 */
public class UI extends JFrame{
    JPanel mainLeftPanel = new JPanel();
    JPanel mainRightPanel = new JPanel();

    JPanel firstLeft = new JPanel();
    JPanel secondLeft = new JPanel();

    JPanel rightPanel = new JPanel();

    GridLayout leftGrid = new GridLayout(2,1);
    GridLayout rightGrid = new GridLayout(1,1);

    JTextArea usersText = new JTextArea(25,30);
    JTextArea exceptionalUsersText = new JTextArea(40,30);
    JTextArea searchText = new JTextArea(1,10);
    JButton add = new JButton("添加用户");
    JButton delete = new JButton("删除用户");
    JButton refresh = new JButton("刷新界面");
    JButton search = new JButton("查找人员历史体温记录");

    JLabel msg = new JLabel("");
    JLabel labelID = new JLabel("工号");

    String userLine = new String();
    UserService userService = new UserServiceImpl();
    HistoryService historyService = new HistoryServiceImpl();

    UI (){
        setSize(800,800);
        setLocation(600,150);
        setVisible(true);
        setTitle("门禁管理系统");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainLeftPanel.setBorder(BorderFactory.createTitledBorder("用户信息"));
        mainLeftPanel.setBackground(Color.lightGray);
        leftGrid.setVgap(10);
        mainLeftPanel.setLayout(leftGrid);
        usersText.setEditable(false);
        usersText.setLineWrap(true);
        showUsers();
        add.setPreferredSize(new Dimension(120,50));
        delete.setPreferredSize(new Dimension(120,50));
        refresh.setPreferredSize(new Dimension(120,50));
        search.setPreferredSize(new Dimension(225,50));
        firstLeft.setBackground(Color.lightGray);
        secondLeft.setBackground(Color.lightGray);
        firstLeft.add(usersText);
        secondLeft.add(add);
        secondLeft.add(delete);
        secondLeft.add(refresh);
        secondLeft.add(labelID);
        secondLeft.add(searchText);
        secondLeft.add(search);
        secondLeft.add(msg);
        mainLeftPanel.add(firstLeft);
        mainLeftPanel.add(secondLeft);
        exceptionalUsersText.setEditable(false);
        exceptionalUsersText.setLineWrap(true);

        mainRightPanel.setBorder(BorderFactory.createTitledBorder("体温不正常用户历史信息"));
        mainRightPanel.setLayout(rightGrid);
        mainRightPanel.setBackground(Color.lightGray);
        rightPanel.setBackground(Color.lightGray);



        rightPanel.add(exceptionalUsersText);
        mainRightPanel.add(rightPanel);


        setLayout(new GridLayout());
        add(mainLeftPanel);
        add(mainRightPanel);
        validate();

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new addDialog();
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new deleteDialog();
            }
        });
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUsers();
            }
        });
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean flag = false;
                String str_id = searchText.getText();
                msg.setText("");
                if(str_id.length() > 4 ||str_id == null || str_id.equals("") ){
                    msg.setText("请输入已存在的工号!!!");
                    exceptionalUsersText.setText("");
                    searchText.setText("");
                    return;
                }
                List<User> allUsers = userService.findAllUsers();
                for (User user : allUsers) {
                    if(user.getID() == Integer.parseInt(str_id)){
                        flag = true;
                        msg.setText("查询成功");
                        break;
                    }
                }
                if(flag){
                    flag = false;
                    showHistoryById(Integer.parseInt(searchText.getText()));
                }else{
                    msg.setText("请输入已存在的工号!!!");
                    exceptionalUsersText.setText("");
                    searchText.setText("");
                    return;
                }
            }
        });
    }
    public void showUsers(){
        List<User> UsersList = userService.findAllUsers();
        usersText.setText("--------------工号---------------姓名---------------公司单位--------------\n");
        for (User user : UsersList) {
            String id = new Integer(user.getID()).toString();
            switch (id.length()){
                case 1: usersText.append("                  000"+id+"                 "+user.getName()+"                   "+user.getUnit()+"\n");
                    break;
                case 2: usersText.append("                  00"+id+"                 "+user.getName()+"                   "+user.getUnit()+"\n");
                    break;
                case 3: usersText.append("                  0"+id+"                 "+user.getName()+"                   "+user.getUnit()+"\n");
                    break;
                case 4: usersText.append("                  "+id+"                 "+user.getName()+"                   "+user.getUnit()+"\n");
                    break;
            }
        }
    }
    public void showHistoryById(int id){
        List<History> historyList = null;
        historyList =  historyService.findUserHistoryById(id);
        if (historyList == null){
            exceptionalUsersText.setText("该人员暂无刷卡记录");
            return;
        }
        exceptionalUsersText.setText("");
        if(id == 0){
            exceptionalUsersText.append("");
        }else {
            for (History history : historyList) {
                exceptionalUsersText.append("工号: " + history.getId() + "    " + history.getName() + "    体温:" + history.getTemperature() + "℃    " + history.getStr_date()+"\n");
            }
        }
    }
}

