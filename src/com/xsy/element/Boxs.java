package com.xsy.element;

import javax.swing.*;
import java.awt.*;

public class Boxs extends ElementObj{
    public static final int BASE_WIDTH = 40;
    public final boolean destroyValid;
    private int i;
    private int j;
    private ImageIcon[] wave;
    private String type;

    public Boxs(int i,int j,ImageIcon icon,String type,boolean valid){
        super(i * BASE_WIDTH + 5,j * BASE_WIDTH + 15,BASE_WIDTH,BASE_WIDTH,icon);
        this.i = i;
        this.j = j;
        this.type = type;
        this.destroyValid = valid;
    }

    @Override
    public void setWave(ImageIcon[] wave){
        this.wave = wave;
        setIcon(wave[0]);
    }


    public int getI() {
        return this.i;
    }

    public int getJ() {
        return this.j;
    }

    @Override
    public boolean getValid(){
        return this.destroyValid;
    }
    @Override
    public void showElement(Graphics g) {
        if(this.icon !=null){
            if(type.equals("UP")){
                g.drawImage(this.icon.getImage(),this.getX(),this.getY() - BASE_WIDTH,this.getW(),this.getH(),null);
                return;
            }
            g.drawImage(this.icon.getImage(),this.getX(),this.getY(),this.getW(),this.getH(),null);

        }

    }

    @Override
    public void KeyClick(boolean isClicked, int key) {

    }



}
