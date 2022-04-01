package com.xsy.element;

import com.xsy.component.Music;
import com.xsy.show.GameJFrame;
import com.xsy.util.GameKey;
import com.xsy.controller.GameThread;
import com.xsy.manager.ElementManager;
import com.xsy.manager.GameElement;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Player extends ElementObj {
    //记录四个方向是否摁下
    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;
    private static final int BASE_WIDTH = 40;

    //第一行是向上跑，第二行是向下跑，第三行是向左跑，第四行是向右跑
    private ImageIcon[][] img = new ImageIcon[4][6];
    //记录摁键时长，若玩家连续摁下，则pressedNum累加，更换游戏玩家图片，形成动画效果
    private double pressedNum = 0;
    private int number;//玩家编号
    private int team = GameKey.TYPE_TEAM_RED;//玩家队伍
    private boolean isReady = false;//准备状态
    private boolean isLive = true;//生存状态
    private String userName;//玩家用户名
    private int type;//玩家类型，0为空位，1为红队，2为灰队


    //该构造器用于构建大厅中的空位玩家，即假玩家
    public Player(int number,int type){
        super(25 + (number - 1) * 123,75,102,105,new ImageIcon("images/hall/player" + type + ".png"));
        this.number = number;
    }
    //该构造器用于游戏大厅中真玩家的创立
    public Player(int number,int type,String userName){
        super(25 + (number - 1) * 123,75,102,105,new ImageIcon("images/hall/player" + type + ".png"));
        this.number = number;
        this.userName = userName;
        this.team = type;
    }

    //用于游戏主要界面中游戏玩家的创立
    public Player(int number,int i,int j,ImageIcon [][] img,String userName){
        super(i * BASE_WIDTH + 3 , j * BASE_WIDTH ,48,64,img[1][0]);
        this.setIcon(img[1][0]);
        this.number = number;
        this.img = img;
        this.userName = userName;
        if(number % 2 == 1){
            this.team = GameKey.TYPE_TEAM_RED;
        }else{
            this.team = GameKey.TYPE_TEAM_BLUE;
        }
    }



    /**
     * 自主绘画
     */
    @Override
    public void showElement(Graphics g) {
        if(this.icon != null){
            g.drawImage(this.icon.getImage(),this.getX(),this.getY(),this.getW(),this.getH(),null);
        }
    }

    @Override
    public void KeyClick(boolean is, int key) {
        if(is){
            this.pressedNum+=0.2;
            switch (key){
                //37为左
                case 37:{
                    if(isCanGet("X", -1)){
                        this.setX(this.getX() - 5);
                    }
                    this.left = true;
                    break;
                }
                //38为向上
                case 38:{
                    if(isCanGet("Y", -1)) {
                        this.setY(this.getY() - 5);
                    }
                    this.up = true;
                    break;
                }
                //39为向右
                case 39:{
                    if(isCanGet("X", 1)) {
                        this.setX(this.getX() + 5);
                    }
                    this.right = true;
                    break;
                }
                //40为向下
                case 40:{
                    if(isCanGet("Y", 1)) {
                        this.setY(this.getY() + 5);
                    }
                    this.down  = true;
                    break;
                }
            }
            if(pressedNum >= 6){
                pressedNum = 1;
            }
        }else{
            //37为左
            //38为向上
            //39为向右
            //40为向下
            switch (key) {
                case 37 -> {
                    this.left = true;

                }
                case 38 -> {
                    this.up = true;

                }
                case 39 -> {
                    this.right = true;

                }
                case 40 -> {
                    this.down = true;

                }
            }

            pressedNum = 0;
            this.down = false;
            this.up = false;
            this.right = false;
            this.left = false;
        }

    }
    @Override
    public void move(){
        //人物移动方法
    }
    @Override
    public void updateImage(){
        if(this.left){
            this.setIcon(img[2][(int)pressedNum]);
        }else if(this.right){
            this.setIcon(img[3][(int)pressedNum]);
        }else if(this.up){
            this.setIcon(img[0][(int)pressedNum]);
        }else if(this.down){
            this.setIcon(img[1][(int)pressedNum]);
        }
    }

    public int getI(){
        return ( getX() - 5 + 24) / BASE_WIDTH;
    }

    public int getJ(){
        return(getY() - 15 + 55) / BASE_WIDTH;
    }

    @Override
    public void setTeam(int team){
        this.team = team;
        if(GameThread.typeOfGame == 1) setIcon(new ImageIcon("images/hall/player" + team + ".png"));

    }
    public int getTeam(){
        return this.team;
    }

    //判断路上是否障碍
    //障碍包括：非空方块
    private boolean isCanGet(String direction, int addition) {
        ElementManager manager = ElementManager.getManager();
        //当玩家走到游戏边界时
        if(direction.equals("X")){
            if(addition == -1 && this.getX() - 5 < 2) {
                this.setX(2);
                return false;
            }
            if(addition == 1 && this.getX() + 5 > 560){
                this.setX(560);
                return false;
            }
        }
        if(direction.equals("Y")){
            if(addition == -1 && this.getY() - 5 < -10){
                this.setY(-10);
                return false;
            }
            if(addition == 1 && this.getY() + 5 > 475){
                this.setY(475);
                return false;
            }
        }

    //判断是否有空方块
        for ( ElementObj obj : manager.getGameElements().get(GameElement.MAPS_DOWN)) {
            if(direction.equals("X") && obj.getI() == this.getI() + addition && obj.getJ() == this.getJ()){
                if(obj.getIcon()!= null){

                    if(addition == 1 && obj.getX() - this.getX() < 50){

                        return false;
                    }

                    if(addition == -1 && this.getX() - obj.getX() < 40){
                        return false;
                    }
                    return true;
                }
                return true;
            }

            if(direction.equals("Y")&& obj.getI() == this.getI() && obj.getJ() == this.getJ()+ addition ){

                if(obj.getIcon()!= null){

                    if(addition == 1 && obj.getY() - this.getY() < 70){
                        return false;
                    }
                    if(addition == -1 && this.getY() - obj.getY() < 15){

                        return false;
                    }
                    return true;
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public int getNumber(){
        return this.number;
    }

    @Override
    public boolean getReady(){
        return this.isReady;
    }
    @Override
    public void setReady(boolean isReady){
        //如果不需要改变状态值则直接返回
        if(this.isReady == isReady) return;
        String imgPath = this.getIcon().toString();
        String[] temp;
        if(isReady){
            temp = imgPath.split("\\.");
            temp[0] += "_ready";
        }else{
            temp = imgPath.split("_");
        }
        imgPath = temp[0] + ".png";
        this.setIcon(new ImageIcon(imgPath));
        this.isReady = isReady;
    }

    //死亡
    @Override
    public void death(){
        isLive = false;
        this.setIcon(null);
    }

    @Override
    public String getUserName(){
        return this.userName;
    }

    //放置炸弹泡泡
    @Override
    public void putBomb(){
        //处理炸弹图片
        BufferedImage bombJump = null;
        try {
            bombJump = ImageIO.read(new File("images/bubble/base_bomb.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int imgWidth  = bombJump.getWidth();
        int imgHeight = bombJump.getHeight();
        ImageIcon[] bombIcon = new ImageIcon[3];
        for(int i=0;i<3;i++){
            bombIcon[i]= new ImageIcon(bombJump.getSubimage(i*imgWidth/3, 0, imgWidth/3, imgHeight));
        }
        //加入炸弹
        //开启炸弹线程
        Bomb bomb = new Bomb(this.getI(),this.getJ(),bombIcon);
        ElementManager.getManager().addElement(GameElement.BOMB,bomb);
        new Thread(bomb).start();
        //如果炸弹为本机放置，则播放放置炸弹音效
        if(this.getNumber() == GameJFrame.getNumber()) Music.playSingleMusic("music/putBomb.wav");
    }
}
