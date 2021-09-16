/**
 * @author ZYOOO
 * @date 2021-09-13 18:59
 */
package com.managementSystem;

import com.managementSystem.domain.User;
import com.managementSystem.service.UserService;
import com.managementSystem.service.impl.UserServiceImpl;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class deleteDialog extends JDialog {
    //construct method 构造方法初始化弹窗样式
    JPanel mainPanel = new JPanel();

    GridLayout mainGrid = new GridLayout(3,1);
    JLabel msg = new JLabel("");
    JLabel ID = new JLabel("工号");

    JButton delete = new JButton("删除");

    JTextComponent idText = new JTextPane();

    UserService userService = new UserServiceImpl();

    public deleteDialog(){
        this.setTitle("删除用户");
        this.setVisible(true);
        this.setLocation(800,400);
        this.setSize(300,150);
        this.setResizable(true);

        mainPanel.setLayout(mainGrid);
        mainGrid.setVgap(3);
        idText.setSize(300,100);
        mainPanel.add(msg);
        mainPanel.add(ID);
        mainPanel.add(idText);
        mainPanel.add(delete);
        setLayout(new FlowLayout());
        add(mainPanel);
        validate();

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str_id = idText.getText();
                msg.setText("");
                if(str_id.length() > 4 ||str_id == null || str_id.equals("") ){
                    msg.setText("请输入已存在的工号!!!");
                    idText.setText("");
                    return;
                }
                List<User> allUsers = userService.findAllUsers();
                boolean flag = false;
                for (User user : allUsers) {
                    if(user.getID() == Integer.parseInt(str_id)){
                        flag = true;
                    }
                }
                if(flag){
                    userService.deleteUserById(Integer.parseInt(str_id));
                    msg.setText("删除成功");
                }else{
                    msg.setText("请输入已存在的工号!!!");
                    idText.setText("");
                    return;
                }
            }
        });
    }
}
