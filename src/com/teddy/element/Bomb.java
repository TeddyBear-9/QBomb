package com.teddy.element;

import com.teddy.util.GameKey;
import com.teddy.component.Music;
import com.teddy.manager.ElementManager;
import com.teddy.manager.GameElement;
import com.teddy.manager.Message;
import com.teddy.show.GameJFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bomb extends ElementObj implements Runnable {
    private int i;
    private int j;
    private int time;
    private ImageIcon[] icons;
    private ImageIcon[][] waveImg;
    private ImageIcon[][] boxBomb;
    private ImageIcon[][] playerBomb;
    private int MAXDISTANCE = 1;


    public Bomb(int i, int j,ImageIcon[] icons){
        super(i * Boxs.BASE_WIDTH - 5 ,j * Boxs.BASE_WIDTH + 5,60,60,icons[0]);
        loadWave();
        this.i = i;
        this.j = j;
        this.icons = icons;
    }
    public void loadWave(){
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("images/bubble/bomb_img.png"));
            int imgWidth  = bufferedImage.getWidth();
            int imgHeight = bufferedImage.getHeight();
            this.waveImg = new ImageIcon[9][3];

            for(int m=0;m<9;m++){
                this.waveImg[m] = new ImageIcon[3];
                for(int n=0;n<3;n++){
                    this.waveImg[m][n] = new ImageIcon(bufferedImage.getSubimage(n*imgWidth/3, m*imgHeight/9, imgWidth/3, imgHeight/9));
                }
            }
            bufferedImage = ImageIO.read(new File("images/bubble/boxBomb1.png"));
            imgWidth = bufferedImage.getWidth();
            imgHeight = bufferedImage.getHeight();
            this.boxBomb = new ImageIcon[1][3];
            for(int n=0;n<3;n++){
                this.boxBomb[0][n] = new ImageIcon(bufferedImage.getSubimage(n*imgWidth/3, 0, imgWidth/3, imgHeight));
            }
            bufferedImage = ImageIO.read(new File("images/bubble/player_redBombpng.png"));
            imgWidth = bufferedImage.getWidth();
            imgHeight = bufferedImage.getHeight();
            this.playerBomb = new ImageIcon[1][3];
            for(int n=0;n<3;n++){
                this.playerBomb[0][n] = new ImageIcon(bufferedImage.getSubimage(n*imgWidth/3, 0, imgWidth/3, imgHeight));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @Override
    public void showElement(Graphics g) {
        g.drawImage(this.icon.getImage(),this.getX(),this.getY(),this.getW(),this.getH(),null);
    }

    @Override
    public void KeyClick(boolean isClicked, int key) {

    }

    @Override
    public void updateImage(){
        int index = time % 3;
        this.setIcon(icons[index]);

    }

    @Override
    public void run() {
        try {
            for(time = 0; time <9; time++ ){
                Thread.sleep(250);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Music.playSingleMusic("music/explode.wav");
        death();
    }



    public void death(){
        final int m = this.i;
        final int n = this.j;

        ElementManager manager = ElementManager.getManager();
        List<ElementObj> list = manager.getElementsByKey(GameElement.MAPS_DOWN);
        List<ElementObj> playerList = manager.getElementsByKey(GameElement.PLAYER);
        List<Wave> waveBox = new ArrayList<>();
        int distance = 1;

        boolean leftFinish = false;
        boolean rightFinish = false;
        boolean upFinish = false;
        boolean downFinish = false;

        ElementObj lastLeft = null;
        ElementObj lastRight = null;
        ElementObj lastUp =null;
        ElementObj lastDown = null;
        for(boolean flag = true;flag && distance <= MAXDISTANCE;distance ++){

            if(leftFinish && rightFinish && upFinish && downFinish){
                flag = false;
            }
            for(ElementObj box:list){
                boolean isTarget = false;
                if(box.getI() == m && box.getJ() == n){
                    Wave wave = new Wave(box.getI(), box.getJ(), waveImg[0]);
                    manager.addElement(GameElement.WAVE,wave);
                    waveBox.add(wave);
                }
                if(!rightFinish && box.getI() == m + distance && box.getJ() == n){
                    if(box.getIcon() == null){
                        Wave wave = new Wave(box.getI(), box.getJ(), waveImg[3]);
                        manager.addElement(GameElement.WAVE,wave);
                        lastRight = wave;
                        waveBox.add(wave);
                    }else{
                        if(box.getValid()) {
                            isTarget = true;
                            rightFinish = true;
                            Wave wave = new Wave(box.getI(), box.getJ(), waveImg[7]);
                            manager.addElement(GameElement.WAVE, wave);
                            waveBox.add(wave);
                        }
                        else {
                            rightFinish = true;
                            if(lastRight != null){
                                lastRight.setIcon(waveImg[7][0]);
                            }
                        }
                    }
                }
                if(!leftFinish && box.getI() == m - distance && box.getJ() == n){
                    if(box.getIcon() == null){
                        Wave wave = new Wave(box.getI(), box.getJ(), waveImg[4]);
                        manager.addElement(GameElement.WAVE,wave);
                        lastLeft = wave;
                        waveBox.add(wave);
                    }else{
                        if(box.getValid()){
                            isTarget = true;
                            leftFinish = true;
                            Wave wave = new Wave(box.getI(),box.getJ(),waveImg[8]);
                            manager.addElement(GameElement.WAVE,wave);
                            waveBox.add(wave);
                        }else{
                            leftFinish = true;
                            if(lastLeft != null){
                                lastLeft.setWave(waveImg[8]);
                            }
                        }
                    }
                }
                if(!upFinish && box.getI() == m  && box.getJ() == n - distance){
                    if(box.getIcon() == null){
                        Wave wave = new Wave(box.getI(), box.getJ(), waveImg[1]);
                        manager.addElement(GameElement.WAVE,wave);
                        lastUp = wave;
                        waveBox.add(wave);

                    }else{
                        if(box.getValid()){
                            isTarget = true;
                            upFinish = true;
                            Wave wave = new Wave(box.getI(),box.getJ(),waveImg[5]);
                            manager.addElement(GameElement.WAVE,wave);
                            waveBox.add(wave);
                        }else{
                            upFinish = true;
                            if(lastUp != null){
                                lastUp.setWave(waveImg[5]);
                            }
                        }
                    }
                }
                if(!downFinish && box.getI() == m  && box.getJ() == n + distance){
                    if(box.getIcon() == null){
                        Wave wave = new Wave(box.getI(), box.getJ(), waveImg[2]);
                        manager.addElement(GameElement.WAVE,wave);
                        lastDown = wave;
                        waveBox.add(wave);
                    }else{
                        if(box.getValid()){
                            isTarget = true;
                            downFinish = true;
                            Wave wave = new Wave(box.getI(),box.getJ(),waveImg[6]);
                            manager.addElement(GameElement.WAVE,wave);
                            waveBox.add(wave);
                        }else{
                            downFinish = true;
                            if(lastDown != null){
                                lastDown.setWave(waveImg[6]);
                            }
                        }
                    }
                }

                if(distance == MAXDISTANCE){
                    if(lastRight != null) lastRight.setWave(waveImg[7]);
                    if(lastLeft != null) lastLeft.setWave(waveImg[8]);
                    if(lastUp != null)lastUp.setWave(waveImg[5]);
                    if(lastDown != null)lastDown.setWave(waveImg[6]);
                }
                if(m + distance > 14){
                    rightFinish = true;
                    if(lastRight != null) lastRight.setWave(waveImg[7]);
                }
                if(m - distance < 0) {
                    leftFinish = true;
                    if(lastLeft != null) lastLeft.setWave(waveImg[8]);
                }
                if(n + distance > 13){
                    downFinish = true;
                    if(lastDown != null)lastDown.setWave(waveImg[6]);
                }
                if(n - distance < 0) {
                    upFinish = true;
                    if(lastUp != null)lastUp.setWave(waveImg[5]);
                }

                if(isTarget){
//
                    List<ElementObj> upList = manager.getElementsByKey(GameElement.MAPS_UP);
                    for(ElementObj obj:upList){
                        if(box.getI() == obj.getI() && box.getJ() == obj.getJ()){
//
                            obj.setIcon(null);
                            manager.getElementsByKey(GameElement.MAPS_UP).remove(obj);
                            break;
                        }
                    }
                    box.setIcon(null);
                }
            }

            for(ElementObj player:playerList){

                boolean isPlayer =false;
                //右边
                if(player.getI() == m + distance && player.getJ() == n) isPlayer = true;
                if(player.getI() == m - distance && player.getJ() == n) isPlayer = true;
                if(player.getI() == m && player.getJ() == n + distance) isPlayer = true;
                if(player.getI() == m && player.getJ() == n - distance) isPlayer = true;
                if(player.getI() == m && player.getJ() == n) isPlayer = true;
                if(isPlayer){
                    Wave wave = new Wave(player.getI(),player.getJ(),playerBomb[0]);
                    manager.addElement(GameElement.WAVE,wave);
                    waveBox.add(wave);
                    Message message = new Message();
                    message.number = player.getNumber();
                    message.type = GameKey.TYPE_PLAYER_DEATH;
                    message.team = player.getTeam();
                    GameJFrame.sendMessage(message);
                }
            }
        }
        for(Wave wave: waveBox){
            new Thread(wave).start();
        }
        manager.getGameElements().get(GameElement.BOMB).remove(this);
    }
}
