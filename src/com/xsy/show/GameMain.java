package com.xsy.show;

import com.xsy.component.Music;
import com.xsy.component.ImageButton;
import com.xsy.element.Bomb;
import com.xsy.element.ElementObj;
import com.xsy.manager.ElementManager;
import com.xsy.manager.GameElement;
import com.xsy.manager.Message;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class GameMain extends JPanel implements Runnable{
    private ElementManager manager;
    private GameJFrame parentFrame;
    private Socket socket;
    public static Music music;
    private JButton gameStart;
    private boolean flag = true;

    public GameMain(){
        init();
    }
    public GameMain(GameJFrame parentFrame){
        this.parentFrame = parentFrame;
        this.socket = GameJFrame.getSocket();
        this.setLayout(null);
        init();
    }
    public void init(){
        manager = ElementManager.getManager();
        parentFrame.depositJPanel();
        parentFrame.setVisible(false);
        GameJFrame.setGAMEY(630);
        GameJFrame.setGAMEX(620);
        parentFrame.setJPanel(this);
        parentFrame.restart();
        parentFrame.getThread().setTypeOfGame(2);
        gameStart = new ImageButton(new ImageIcon("images/game/gameStart.gif"));
        gameStart.setBounds(180,220,gameStart.getIcon().getIconWidth(),gameStart.getIcon().getIconHeight());
        gameStart.setVisible(false);
        gameStart.setOpaque(false);
        gameStart.setContentAreaFilled(false);
        this.add(gameStart);

        music = new Music("music/mainBg.wav");

    }


    @Override
    public void run() {

        while(true){
            try {

                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint();
        }
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        Map<GameElement, java.util.List<ElementObj>> all = manager.getGameElements();
//        按枚举类中的顺序绘画，MAPS作为背景，应该首先绘画
        for(GameElement key:GameElement.values()){
            List<ElementObj> list = all.get(key);
            for(int i = 0; i < list.size(); i++){
                ElementObj obj = list.get(i);
                if(obj!=null) {
                    obj.model();
                    obj.showElement(g);
                }
            }
        }

    }

}
