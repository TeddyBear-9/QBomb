package com.teddy.element;

import javax.swing.*;
import java.awt.*;

public abstract class  ElementObj {
    private int x = 0;
    private int y = 0;
    private int w = 0;
    private int h = 0;
    //ImageIcon 可以获取原始图片的宽和高；
    ImageIcon icon = null;
    private int i;
    private int j;



//以及必要的状态值


    public ElementObj(int x, int y, int w, int h, ImageIcon icon) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.icon = icon;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public int getTeam(){
        return 0;
    }
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public int getI(){
        return this.i;
    }
    public int getJ(){
        return this.j;
    }
    /**
     * 可以移动的子类实现
     */
    public void move(){

    }
    //更换图片状态
    public void updateImage(){

    }
    public boolean getValid(){
        return false;
    }
    public final void model(){
        updateImage();
        move();
    }
    /**
     * 抽象方法，用于显示元素
     * @param g 用于绘画
     */
    public abstract void showElement(Graphics g);
    public abstract void KeyClick(boolean isClicked, int key);
    public void setWave(ImageIcon[] wave){

    }
    public int getNumber(){
        return -1;
    }
    public int compareTo(ElementObj obj) {
        return this.getNumber() - obj.getNumber();
    }
    public boolean getReady(){
        return false;
    }
    public void setReady(boolean isReady){

    }
    public void death(){ }
    public String getUserName(){
        return "";
    }
    public void setTeam(int team){ }
    public void putBomb(){}
}
