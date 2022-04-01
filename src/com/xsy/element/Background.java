package com.xsy.element;

import javax.swing.*;
import java.awt.*;

public class Background extends ElementObj{



    public Background(int x, int y, int w, int h, ImageIcon icon) {
       super(x, y, w, h, icon);
    }

    @Override
    public void showElement(Graphics g) {

        g.drawImage(this.getIcon().getImage(),this.getX(),this.getY(),this.getW(),this.getH(),null);
    }

    @Override
    public void KeyClick(boolean isClicked, int key) {

    }
}
