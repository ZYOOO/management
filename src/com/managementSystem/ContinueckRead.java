package com.managementSystem;

import com.managementSystem.domain.History;
import com.managementSystem.service.HistoryService;
import com.managementSystem.service.UserService;
import com.managementSystem.service.impl.HistoryServiceImpl;
import com.managementSystem.service.impl.UserServiceImpl;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author ZYOOO
 * @date 2021-09-16 12:08
 */
public class ContinueckRead extends Thread implements SerialPortEventListener{
    static UI ui = new UI();

    // 监听器,我的理解是独立开辟一个线程监听串口数据
    static CommPortIdentifier portId; // 串口通信管理类
    static Enumeration<?> portList; // 有效连接上的端口的枚举
    InputStream inputStream; // 从串口来的输入流
    static OutputStream outputStream;// 向串口输出的流
    static SerialPort serialPort; // 串口的引用
    // 堵塞队列用来存放读到的数据
    private BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>();

    int lastUserId = -1;
    boolean flag = false;
    String currentCOM;
    StringBuffer temperature = new StringBuffer();
    UserService userService = new UserServiceImpl();
    HistoryService historyService = new HistoryServiceImpl();

    @Override
    /**
     * SerialPort EventListene 的方法,持续监听端口上是否有数据流
     */
    public void serialEvent(SerialPortEvent event) {//

        switch (event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:// 当有可用数据时读取数据
                byte[] readBuffer = new byte[3];
                byte[] sendBuffer = new byte[3];
                try {
                    int numBytes = -1;
                    while (inputStream.available() > 0) {
                        numBytes = inputStream.read(readBuffer);
                        if (numBytes > 0) {
                            msgQueue.add(new Date() + "收到的数据为："+ readBuffer[0] + readBuffer[1] + readBuffer[2]);
                            if(readBuffer.length == 3 &&readBuffer[0] == -1){
                                int i = readBuffer[1]*256 + readBuffer[2];
                                flag = userService.verifyUserById(i);
                                if(flag) {
                                    sendBuffer[0] = -1;
                                    sendBuffer[1] = 0;
                                    sendBuffer[2] = 1;
                                    lastUserId = i;
                                    i = -1;
                                    outputStream.write(sendBuffer, 0, sendBuffer.length);
                                }else{
                                    lastUserId = -1;
                                    sendBuffer[0] = -1;
                                    sendBuffer[1] = 0;
                                    sendBuffer[2] = 2;
                                    outputStream.write(sendBuffer, 0, sendBuffer.length);
                                }
                            }else if(readBuffer.length == 3 && readBuffer[0] == -18 && lastUserId != -1){
                                temperature.append(readBuffer[1]);
                                temperature.append(".");
                                temperature.append(readBuffer[2]);
                                String strTemp = temperature.toString();
                                temperature.delete(0,temperature.length());
                                History history = new History();
                                history.setId(lastUserId);
                                history.setName(userService.getUserById(lastUserId).getName());
                                history.setTemperature(strTemp);
                                historyService.addHistory(history);
                                if(strTemp.compareTo("37.5")<0){
                                    sendBuffer[0] = -1;
                                    sendBuffer[1] = 0;
                                    sendBuffer[2] = 3;
                                    outputStream.write(sendBuffer, 0, sendBuffer.length);
                                }else{
                                    sendBuffer[0] = -1;
                                    sendBuffer[1] = 0;
                                    sendBuffer[2] = 4;
                                    outputStream.write(sendBuffer, 0, sendBuffer.length);
                                    //调出历史记录在UI上显示
                                    ui.showHistoryById(lastUserId);
                                }
                                lastUserId = -1;
                            }
                            readBuffer = new byte[3];// 重新构造缓冲对象，否则有可能会影响接下来接收的数据
                        }
                    }
                } catch (IOException e) {
                }
                break;
        }
    }

    public int startComPort() {
        // 通过串口通信管理类获得当前连接上的串口列表
        portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            // 获取相应串口对象
            portId = (CommPortIdentifier) portList.nextElement();
            // 判断端口类型是否为串口
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                ui.coms.addItem(portId.getName());
            }
        }
        ui.portMsg.setText("串口搜寻完毕");
        currentCOM = (String) ui.coms.getSelectedItem();
        while (currentCOM.equals("请选择串口")) {
            System.out.println(currentCOM);
            currentCOM = (String) ui.coms.getSelectedItem();
            if(!currentCOM.equals("请选择串口")){
                ui.portMsg.setText("正在配置串口");
                System.out.println("正在配置串口");
                portList = CommPortIdentifier.getPortIdentifiers();
                while (portList.hasMoreElements()) {
                    portId = (CommPortIdentifier) portList.nextElement();
                    if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                        if(currentCOM.equals(portId.getName())){
                            try {
                                // 打开串口名字为currentCOM,延迟为2毫秒
                                serialPort = (SerialPort) portId.open(currentCOM, 2000);
                            } catch (PortInUseException e) {
                                e.printStackTrace();
                                return 0;
                            }
                            // 设置当前串口的输入输出流
                            try {
                                inputStream = serialPort.getInputStream();
                                outputStream = serialPort.getOutputStream();
                            } catch (IOException e) {
                                e.printStackTrace();
                                return 0;
                            }
                            // 给当前串口添加一个监听器
                            try {
                                serialPort.addEventListener(this);
                            } catch (TooManyListenersException e) {
                                e.printStackTrace();
                                return 0;
                            }
                            // 设置监听器生效，即：当有数据时通知
                            serialPort.notifyOnDataAvailable(true);

                            // 设置串口的一些读写参数
                            try {
                                // 比特率、数据位、停止位、奇偶校验位
                                serialPort.setSerialPortParams(9600,
                                        SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                                        SerialPort.PARITY_NONE);
                            } catch (UnsupportedCommOperationException e) {
                                e.printStackTrace();
                                return 0;
                            }
                            return 1;
                        }
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            System.out.println("--------------任务处理线程运行了--------------");
            while (true) {
                // 如果堵塞队列中存在数据就将其输出
                if (msgQueue.size() > 0) {
                    System.out.println(msgQueue.take());
                }
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ContinueckRead cRead = new ContinueckRead();
        int i = cRead.startComPort();
        //串口配置完毕启动线程
        if (i == 1) {
            i = 0;
            //UI显示串口配置完毕
            ui.portMsg.setText("串口配置完毕");
            // 启动线程来处理收到的数据
            cRead.start();
//            try {
//                String st = "串口连接成功................";
//                System.out.println("发出字节数：" + st.getBytes("gbk").length);
//                outputStream.write(st.getBytes("gbk"), 0,
//                        st.getBytes("gbk").length);
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
        } else {
            return;
        }
    }

}
