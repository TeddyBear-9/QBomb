package com.teddy.element;

import com.teddy.manager.ElementManager;
import com.teddy.manager.GameElement;

import javax.swing.*;
import java.awt.*;

public class Wave extends ElementObj implements Runnable{


    @Override
    public void setWave(ImageIcon[] waveFade) {
        this.icon = waveFade[0];
    }

    public Wave(int i, int j, ImageIcon[] icons){
        super(i * Boxs.BASE_WIDTH + 5,j * Boxs.BASE_WIDTH + 10,40,40,icons[0]);

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
    }
    @Override
    public void run() {
        try {
            //沉睡900毫秒后，自动消失
            Thread.sleep(900);
            setIcon(null);
            ElementManager.getManager().getElementsByKey(GameElement.WAVE).remove(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
