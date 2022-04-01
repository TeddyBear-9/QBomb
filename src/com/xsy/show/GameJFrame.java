package com.xsy.show;

import com.xsy.component.ImageButton;
import com.xsy.controller.GameThread;
import com.xsy.element.ElementObj;
import com.xsy.manager.ElementManager;
import com.xsy.manager.GameElement;
import com.xsy.manager.Message;

import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 游戏窗体 主要实现的功能：关闭，显示，最大最小化
 * @author 熊师意
 * 实现功能：需要嵌入面板，启动主线程等等
 * 窗体说明 窗体大小
 * 分析：1、面板绑定在窗体
 *      2、监听绑定在窗体
 *      3、游戏主线程启动
 *      4、显示窗体
 */
public class GameJFrame extends JFrame {
    public static int GAMEX = 820;
    public static int GAMEY = 623;

    private static JPanel jPanel = null;//正在显示的面板
    private static KeyListener keyListener = null;//键盘监听
    private static MouseMotionListener mouseMotionListener = null;//鼠标监听
    private static MouseListener mouseListener = null;
    private static int number = -1;
    private static String user;
    private static GameThread thread = null;//游戏主线程
    private static Socket socket = null;
    private static ObjectInputStream in;
    private static ObjectOutputStream out;

    public GameThread getThread() {
        return thread;
    }
    //怎么运行，怎么给别人，怎么获取
    public static int getNumber(){return number;}
    public GameJFrame(){
        connect();
        init();
    }

    public static void sendMessage(Message message){
        ElementManager manager = ElementManager.getManager();
        ElementObj me = null;

        for(ElementObj obj:manager.getElementsByKey(GameElement.PLAYER)){
            if(obj.getNumber() == GameJFrame.getNumber()) me = obj;
        }

        if(me != null && me.getIcon() == null) return;
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * set注入
     */
    public void setKeyListener(KeyListener keyListener) {
        GameJFrame.keyListener = keyListener;
    }

    public void setMouseMotionListener(MouseMotionListener mouseMotionListener) {
        GameJFrame.mouseMotionListener = mouseMotionListener;
    }

    public void setMouseListener(MouseListener mouseListener) {
        GameJFrame.mouseListener = mouseListener;
    }

    public void setJPanel(JPanel jPanel) {
        GameJFrame.jPanel = jPanel;
    }

    public void setThread(GameThread thread){
        this.thread = thread;
    }

    public static void setGAMEX(int GAMEX) {
        GameJFrame.GAMEX = GAMEX;
    }

    public static void setGAMEY(int GAMEY) {
        GameJFrame.GAMEY = GAMEY;
    }

    public void depositJPanel(){
        remove(GameJFrame.jPanel);
    }

    public void init(){
        this.setSize(GAMEX,GAMEY);//设置窗体大小
        this.setTitle("Q版泡泡堂-TeddyBear-09");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置退出并且关闭
        this.setLocationRelativeTo(null);//设置居中
    }

    //窗体布局
    public static void addButton(JButton button){
        button.addMouseListener(GameJFrame.mouseListener);
        button.addMouseMotionListener(GameJFrame.mouseMotionListener);
        if(jPanel!=null){
            jPanel.add(button);
        }
        System.out.println("frame.addButton");

    }

    public static void addText(JTextField textField){
        textField.addKeyListener(GameJFrame.keyListener);
    }
    /**
     * 启动方法
     */
    public void start() {
        if(jPanel != null){
            this.add(jPanel);
        }
        if(keyListener != null){
            this.addKeyListener(keyListener);
        }
        if(mouseListener != null){
            this.addMouseListener(mouseListener);
        }
        if(mouseMotionListener != null){
            this.addMouseMotionListener(mouseMotionListener);
        }
        if(thread !=null){
            new Thread(this.thread).start();
        }

        //界面的刷新
        if(GameJFrame.jPanel instanceof Runnable){
            new Thread((Runnable) GameJFrame.jPanel).start();
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("setVisible true");
        this.setVisible(true);
    }

    //重新启动JPanel线程
    public void restart(){
        init();
        this.add(GameJFrame.jPanel);
        if(GameJFrame.jPanel instanceof Runnable){
            new Thread((Runnable) GameJFrame.jPanel).start();
        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.setVisible(true);
    }

    //连接服务器
    public void connect(){
        try {
            socket = new Socket("localhost",4321);
            //获得服务器赋予的编号
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) in.readObject();
            number = message.number;
            System.out.println("编号：" + number);
        } catch (IOException e) {
            System.out.println("Connection to server error");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static ObjectInputStream getIn(){
        return in;
    }
    public static Socket getSocket(){
        return socket;
    }
    public static String getUser(){
        return user;
    }
    public static void setUser(String user){
        GameJFrame.user = user;
    }

}
