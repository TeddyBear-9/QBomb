package com.xsy.element;

import com.xsy.component.Music;
import com.xsy.controller.GameThread;
import com.xsy.manager.ElementManager;
import com.xsy.manager.GameElement;
import com.xsy.show.GameJFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Special extends ElementObj implements Runnable{
    private ImageIcon[] icons;
    private int type;//0为游戏开始动画；1为游戏胜利；2为游戏失败
    private int n;

    public Special(int type){
        super(200,200,200,200,null);
        this.type = type;
        if(type == 0){
            try {
                BufferedImage gameStart = ImageIO.read(new File("images/game/special/0.png"));
                int imgWidth  = gameStart.getWidth();
                int imgHeight = gameStart.getHeight();
                icons = new ImageIcon[4];
                for(int i=0;i<4;i++){
                    icons[i] = new ImageIcon(gameStart.getSubimage(0, i *imgHeight/4, imgWidth, imgHeight/4));
                }
                this.setIcon(icons[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            this.setIcon(new ImageIcon("images/game/special/" + type + ".png"));
        }

        //设置该动画效果在画面居中
        this.setX((GameJFrame.GAMEX - this.getIcon().getIconWidth()) / 2);
        this.setY((GameJFrame.GAMEY - this.getIcon().getIconHeight()) / 2);
        this.setW(this.getIcon().getIconWidth());
        this.setH(this.getIcon().getIconHeight());
    }


    @Override
    public void showElement(Graphics g) {
        if(this.icon != null){
            g.drawImage(this.icon.getImage(),this.getX(),this.getY(),this.getW(),this.getH(),null);
        }
    }

    @Override
    public void KeyClick(boolean isClicked, int key) {

    }
    @Override
    public void updateImage(){
        if(type != 0) return;
        this.setIcon(icons[n]);
    }

    @Override
    public void run() {
        //当线程跑完才重新启用keylistener
        GameThread.keyListener = false;
        //当为游戏开场动画时，用线程控制动画播放
        //结局提示不需要动画，沉睡一段时间即可
        if(type == 0){
            for(this.n = 0;n < icons.length;n++){
                try {
                    Thread.sleep(1000);
                    updateImage();
                    int musicIndex = 3 - n;
                    Music.playSingleMusic("music/"+  musicIndex + ".wav");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }else{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.setIcon(null);
        GameThread.keyListener = true;
        ElementManager.getManager().getElementsByKey(GameElement.SPECIAL).clear();
    }
}
