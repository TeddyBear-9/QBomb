package com.xsy.show;

import com.xsy.component.Music;
import com.xsy.component.ImageButton;
import com.xsy.element.ElementObj;
import com.xsy.manager.ElementManager;
import com.xsy.manager.GameElement;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class GameHall extends JPanel implements Runnable{
    private ElementManager manager;
    private GameJFrame parentFrame;
    public static JTextArea chatAllField;
    public static JTextField inputArea;
    public static ImageButton readyButton;
    public static ImageButton cancelButton;
    public static ImageButton redTeamButton;
    public static ImageButton grayTeamButton;
    public static  ImageButton sendButton;

    private static JTextField[] userName = new JTextField[8];
    private ImageIcon back = new ImageIcon("images/hall/hall_background.png");

    public static Music music;



    public GameHall(GameJFrame parentFrame){
        this.parentFrame = parentFrame;
        manager = ElementManager.getManager();

        this.setLayout(null);
        init();
    }
    public void init(){
        //初始化各个玩家的名字框
        for(int n = 0; n < 8 ;n++){
            int x = n;
            int y = 0;
            userName[n] = new JTextField();
            if(x > 3){
                x = x - 4;
                y = 1;
            }
            userName[n].setBounds(45 + x * 123,58 + y * 155,70,18);
            userName[n].setFont(new Font("楷体",Font.BOLD,12));
            userName[n].setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
            userName[n].setOpaque(false);
            userName[n].setEditable(false);
            add(userName[n]);
        }

        //重新初始化JFrame
        parentFrame.depositJPanel();
        parentFrame.setVisible(false);
        GameJFrame.setGAMEX(820);
        GameJFrame.setGAMEY(640);
        parentFrame.setJPanel(this);
        parentFrame.restart();
        parentFrame.getThread().setOver(true);
        parentFrame.getThread().setTypeOfGame(1);

        //初始化对话框和输入框
        chatAllField = new JTextArea();
        chatAllField.setEditable(false);
        chatAllField.setFont(new Font("楷体",Font.BOLD,15));
        chatAllField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
        chatAllField.setForeground(Color.white);
        chatAllField.setOpaque(false);
        chatAllField.setBounds(25,365,450,150);
        this.add(chatAllField);

        inputArea = new JTextField();
        Border border = BorderFactory.createEmptyBorder(1, 0,0,0);
        inputArea.setBorder(border);
        inputArea.setForeground(Color.black);
        inputArea.setEditable(true);
        inputArea.setOpaque(false);
        inputArea.setBounds(95,532,280,20);
        GameJFrame.addText(inputArea);
        this.add(inputArea);

        //开始播放音乐
        music = new Music("music/loginBg.wav");
        music.playLoopMusic();


    }



    @Override
    public void run() {
        //对按钮进行初始化
        readyButton = new ImageButton(new ImageIcon("images/hall/ready.png"));
        readyButton.setPressedIcon(new ImageIcon("images/hall/ready_pressed.png"));
        readyButton.setRolloverIcon(new ImageIcon("images/hall/ready_pressed.png"));
        readyButton.setLocation(575,538);
        GameJFrame.addButton(readyButton);

        cancelButton = new ImageButton(new ImageIcon("images/hall/cancel.png"));
        cancelButton.setPressedIcon(new ImageIcon("images/hall/cancel_pressed.png"));
        cancelButton.setRolloverIcon(new ImageIcon("images/hall/cancel_pressed.png"));
        cancelButton.setLocation(660,538);
        GameJFrame.addButton(cancelButton);


        redTeamButton = new ImageButton(new ImageIcon("images/hall/redTeam.png"));
        redTeamButton.setPressedIcon(new ImageIcon("images/hall/redTeam_pressed.png"));
        redTeamButton.setRolloverIcon(new ImageIcon("images/hall/redTeam_pressed.png"));
        redTeamButton.setLocation(655,182);
        GameJFrame.addButton(redTeamButton);

        grayTeamButton = new ImageButton(new ImageIcon("images/hall/grayTeam.png"));
        grayTeamButton.setPressedIcon(new ImageIcon("images/hall/grayTeam_pressed.png"));
        grayTeamButton.setRolloverIcon(new ImageIcon("images/hall/grayTeam_pressed.png"));
        grayTeamButton.setLocation(520,180);
        GameJFrame.addButton(grayTeamButton);

        sendButton = new ImageButton(new ImageIcon("images/hall/send.png"));
        sendButton.setPressedIcon(new ImageIcon("images/hall/send_pressed.png"));
        sendButton.setRolloverIcon(new ImageIcon("images/hall/send_pressed.png"));
        sendButton.setLocation(408,528);
        GameJFrame.addButton(sendButton);

        if(GameJFrame.getNumber() % 2 == 1){
            redTeamButton.setEnabled(false);
            redTeamButton.setVisible(false);
        }else {
            grayTeamButton.setVisible(false);
            grayTeamButton.setEnabled(false);
        }

        //游戏大厅的重绘频率无需很高
        while(true){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //重绘
            repaint();
        }
    }


    @Override
    public void paint(Graphics g){
        super.paint(g);
        Map<GameElement, java.util.List<ElementObj>> all = manager.getGameElements();
        //按枚举类中的顺序绘画，MAPS作为背景，应该首先绘画

        for(GameElement key:GameElement.values()){
            List<ElementObj> list = all.get(key);
            for(int i = 0; i < list.size(); i++){
                ElementObj obj = list.get(i);
                if(obj!=null) obj.showElement(g);
            }
        }
    }
    public static void setUserName(int number,String Name){
        userName[number - 1].setText(Name);
    }
    protected void paintComponent(Graphics g) {
        g.drawImage(back.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
    }
    public static ImageButton getReadyButton(){
        return  readyButton;
    }
    public static ImageButton getCancelButton(){
        return  cancelButton;
    }
}
