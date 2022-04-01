package com.teddy.component;

import javax.swing.*;

import java.awt.*;



public class ImageButton extends JButton{

    public ImageButton(ImageIcon icon){
        this.setDoubleBuffered(true);

        //设置图标
        setIcon(icon);
        //设置空白间距
        setMargin(new Insets(0, 0, 0, 0));
        //设置文本与图标之间的间隔
        setIconTextGap(0);
        //指定是否绘制边框
        setBorderPainted(false);
        //设置边框
        setBorder(null);
        //设置文本
        setText(null);
        setSize(icon.getImage().getWidth(null), icon.getImage().getHeight(null));
    }

}
