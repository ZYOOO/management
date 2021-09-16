/**
 * @author ZYOOO
 * @date 2021-09-13 18:59
 */
package com.managementSystem;

import com.managementSystem.domain.User;
import com.managementSystem.service.HistoryService;
import com.managementSystem.service.UserService;
import com.managementSystem.service.impl.UserServiceImpl;
import javafx.scene.control.Alert;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class addDialog extends JDialog {
    //construct method 构造方法初始化弹窗样式
    JPanel mainPanel = new JPanel();

    GridLayout mainGrid = new GridLayout(8,1);
    JLabel msg = new JLabel("");
    JLabel ID = new JLabel("工号");
    JLabel USER = new JLabel("姓名");
    JLabel UNIT = new JLabel("单位");

    JButton submit = new JButton("提交");

    JTextComponent idText = new JTextPane();
    JTextComponent userText = new JTextPane();
    JTextComponent unitText = new JTextPane();

    UserService userService = new UserServiceImpl();

    public addDialog(){
        this.setTitle("添加用户");
        this.setVisible(true);
        this.setLocation(800,400);
        this.setSize(300,300);
        this.setResizable(true);

        mainPanel.setLayout(mainGrid);
        mainGrid.setVgap(3);
        idText.setSize(300,100);
        mainPanel.add(msg);
        mainPanel.add(ID);
        mainPanel.add(idText);
        mainPanel.add(USER);
        mainPanel.add(userText);
        mainPanel.add(UNIT);
        mainPanel.add(unitText);
        mainPanel.add(submit);
        setLayout(new FlowLayout());
        add(mainPanel);
        validate();

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str_id = idText.getText();
                String str_user = userText.getText();
                String str_unit = unitText.getText();
                msg.setText("");
                if(str_id.length() > 4){
                    msg.setText("请输入四位数的工号!!!");
                    idText.setText("");
                    return;
                }
                if(str_id == null || str_id.equals("") || str_user == null || str_user.equals("") || str_unit == null || str_unit.equals("")){
                    msg.setText("请输入完整信息");
                    idText.setText("");
                    userText.setText("");
                    unitText.setText("");
                    return;
                }
                User user = new User();
                user.setID(Integer.parseInt(str_id));
                user.setName(str_user);
                user.setUnit(str_unit);
                List<User> allUsers = userService.findAllUsers();
                for (User user1 : allUsers) {
                    if(user1.getID() == user.getID()){
                        msg.setText("请不要输入已存在的工号");
                        return;
                    }
                }
                userService.addUser(user);
                msg.setText("添加成功");
                idText.setText("");
                userText.setText("");
                unitText.setText("");
            }
        });
    }
}
