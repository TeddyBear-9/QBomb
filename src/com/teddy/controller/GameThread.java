package com.teddy.controller;

import com.teddy.util.GameKey;
import com.teddy.component.Music;
import com.teddy.element.*;
import com.teddy.manager.ElementManager;
import com.teddy.manager.GameElement;
import com.teddy.manager.Message;
import com.teddy.show.GameHall;
import com.teddy.show.GameJFrame;
import com.teddy.show.GameMain;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * 游戏主线程，用于控制游戏加载
 * @author 熊师意
 */
public class GameThread implements Runnable{
    public static int typeOfGame = 0;
    public static boolean keyListener = true;
    private final ElementManager manager;
    private boolean isOver = false;
    private int type;
    private final GameJFrame parentFrame;
    private GameMain main;
    private static int team;
    static {
        if(GameJFrame.getNumber() % 2 == 1) team = GameKey.TYPE_TEAM_RED;
        else team = GameKey.TYPE_TEAM_BLUE;
    }

//    private boolean loginFirstLoad = true;
//    private boolean hallFirstLoad = true;
//    private boolean gameRunLoad = true;
    //0为登录，1为游戏大厅，2为游戏正式开始


    public GameThread(GameJFrame jFrame){
        this.parentFrame = jFrame;
        manager = ElementManager.getManager();
    }

    @Override

    public void run() {
        while(true){
            //游戏开始前,加载游戏资源（场景资源）
            load();
            //游戏进行时
            gameRun();
            //游戏资源释放
            gameOver();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    private void load() {
        System.out.println("typeOfGame is "+ typeOfGame);
        switch(typeOfGame){
            case 0:{
                loginload();
                break;
            }
            case 1:{
                hallLoad();
                break;
            }
            case 2:{
                mainLoad();
                break;
            }
        }

    }

    public void loginload(){
//        ImageIcon iconBackground = new ImageIcon("images/login/login_background.png");
//        Background background = new Background(0,0,iconBackground.getIconWidth(), iconBackground.getIconHeight(),iconBackground);
//        //将对象放入到元素管理器中
//        //manager.addElement(GameElement.BACK,background);
//        //图片导入

    }

    public void hallLoad(){

        System.out.println("hall Load");
//        ImageIcon iconBackground = new ImageIcon("images/hall/hall_background.png");
//        Background background = new Background(0,0,iconBackground.getIconWidth(), iconBackground.getIconHeight(),iconBackground);
//        //将对象放入到元素管理器中
//        //manager.addElement(GameElement.BACK,background);

        for(int number = 1;number <= 4; number++){
            boolean isHas = false;
            for(ElementObj player:manager.getElementsByKey(GameElement.PLAYER)){
                if(player.getNumber() == number) {
                    GameHall.setUserName(player.getNumber(),player.getUserName());
                    isHas = true;
                    break;
                }
            }

            if(!isHas){
                manager.addElement(GameElement.PLAYER,new Player(number,0));
            }
        }

    }

    public void mainLoad(){

        System.out.println("main load");

        this.main = new GameMain(parentFrame);
        ImageIcon iconBackground = new ImageIcon("images/game/game_back.png");
        Background background = new Background(0,0,iconBackground.getIconWidth(), iconBackground.getIconHeight(),iconBackground);
        manager.addElement(GameElement.BACK,background);

        ImageIcon base2_down = new ImageIcon("images/map/lowtree_down.png");
        ImageIcon base2_up = new ImageIcon("images/map/lowtree_up.png");
        ImageIcon base1_down = new ImageIcon("images/map/tree_down.png");
        ImageIcon base1_up = new ImageIcon("images/map/tree_up.png");
        ImageIcon special1_up = new ImageIcon("images/map/mushroom_up.png");
        ImageIcon special1_down = new ImageIcon("images/map/mushroom_down.png");
        ImageIcon special2_up = new ImageIcon("images/map/mushroomblue_up.png");
        ImageIcon special2_down = new ImageIcon("images/map/mushroomblue_down.png");

        //之后改为接受服务器的消息
        File map = new File("maps/map1.txt");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(map)));
            Scanner scanner;
            String temp;

            int i ,j;

            for(j = 0;(temp = reader.readLine())!= null;j++){
                scanner= new Scanner(temp);
                for(i = 0;scanner.hasNext();i++){
                    int num = scanner.nextInt();


                    switch (num) {
                        case 0 -> {
                            manager.addElement(GameElement.MAPS_DOWN, new Boxs(i, j, null, "DOWN", false));
                            manager.addElement(GameElement.MAPS_UP, new Boxs(i, j, null, "UP", false));
                            break;
                        }
                        case 1 -> {
                            manager.addElement(GameElement.MAPS_DOWN, new Boxs(i, j, base1_down, "DOWN", false));
                            manager.addElement(GameElement.MAPS_UP, new Boxs(i, j, base1_up, "UP", false));
                            break;
                        }
                        case 2 -> {
                            manager.addElement(GameElement.MAPS_DOWN, new Boxs(i, j, base2_down, "DOWN", false));
                            manager.addElement(GameElement.MAPS_UP, new Boxs(i, j, base2_up, "UP", false));
                            break;
                        }
                        case 3 -> {
                            manager.addElement(GameElement.MAPS_DOWN, new Boxs(i, j, special1_down, "DOWN", true));
                            manager.addElement(GameElement.MAPS_UP, new Boxs(i, j, special1_up, "UP", true));
                            break;
                        }
                        case 4 -> {
                            manager.addElement(GameElement.MAPS_DOWN, new Boxs(i, j, special2_down, "DOWN", true));
                            manager.addElement(GameElement.MAPS_UP, new Boxs(i, j, special2_up, "UP", true));
                            break;
                        }
                    }

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            BufferedImage bodyMoveRed = ImageIO.read(new File("images/players/player_red.png"));


            int imgWidth  = bodyMoveRed.getWidth();
            int imgHeight = bodyMoveRed.getHeight();
            ImageIcon[][] player_red = new ImageIcon[4][6];

            for(int i=0;i<4;i++){
                player_red[i] = new ImageIcon[6];
                for(int j=0;j<6;j++){
                    player_red[i][j] = new ImageIcon(bodyMoveRed.getSubimage(j*imgWidth/6, i*imgHeight/4, imgWidth/6, imgHeight/4));
                }
            }

            BufferedImage bodyMoveGray = ImageIO.read(new File("images/players/player_gray.png"));
            imgWidth = bodyMoveGray.getWidth();
            imgHeight = bodyMoveGray.getHeight();

            ImageIcon[][] player_gray = new ImageIcon[4][6];
            for(int i=0;i<4;i++){
                player_gray[i] = new ImageIcon[6];
                for(int j=0;j<6;j++){
                    player_gray[i][j] = new ImageIcon(bodyMoveGray.getSubimage(j*imgWidth/6, i*imgHeight/4, imgWidth/6, imgHeight/4));
                }
            }

            List<ElementObj> players = manager.getElementsByKey(GameElement.PLAYER);
            List<ElementObj> copy = new ArrayList<>(players);
            players.clear();

            for(int index = copy.size() - 1;index >= 0;index--){
                ElementObj player = copy.get(index);
//                System.out.println("the play number is " + player.getNumber());
//                System.out.println("this team :" + player.getTeam());
                if(!player.getIcon().toString().equals("images/hall/player0.png")){
                    Player player1 = null;

                    switch (player.getNumber()) {
                        case 1 -> {
                            if(player.getTeam() == GameKey.TYPE_TEAM_RED){player1 = new Player(player.getNumber(), 0, 0, player_red, player.getUserName());}
                            else{player1 = new Player(player.getNumber(), 0, 0, player_gray, player.getUserName());}
                            player1.setTeam(player.getTeam());
                        }
                        case 2 -> {
                            if(player.getTeam() == GameKey.TYPE_TEAM_RED){player1 = new Player(player.getNumber(), 14, 0, player_red, player.getUserName());}
                            else{player1 = new Player(player.getNumber(), 14, 0, player_gray, player.getUserName());}

                            player1.setTeam(player.getTeam());
                        }
                        case 3 ->{
                            if(player.getTeam() == GameKey.TYPE_TEAM_RED){player1 = new Player(player.getNumber(), 0, 12, player_red, player.getUserName());}
                            else{player1 = new Player(player.getNumber(), 0, 14, player_gray, player.getUserName());}
                            player1.setTeam(player.getTeam());
                        }
                        case 4 ->{
                            if(player.getTeam() == GameKey.TYPE_TEAM_RED){player1 = new Player(player.getNumber(), 14, 12, player_red, player.getUserName());}
                            else{player1 = new Player(player.getNumber(), 14, 14, player_gray, player.getUserName());}
                            player1.setTeam(player.getTeam());
                        }
                    }

                    manager.addElement(GameElement.PLAYER,player1);
                }
            }
            copy.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
//
//        gameMain.gameEnd();
    }

    private void gameRun(){
        switch(typeOfGame){
            case 0:{
                break;
            }
            case 1:{
                hallRun();
                break;
            }
            case 2:{
                mainRun();
                break;
            }
        }
    }
    //游戏资源释放
    private void gameOver() {
        switch(typeOfGame){
            case 0:{
                loginOver();
                break;
            }
            case 1:{
                hallOver();
                break;
            }
            case 2:{
                mainOver();
                break;
            }
        }
    }

    private void loginOver(){

        //如果没有结束，则一直循环
        while(!isOver){
            try {

                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("login clear");
        for(GameElement key: GameElement.values()){
            List<ElementObj> list = manager.getElementsByKey(key);
            list.clear();
        }
        isOver = false;
    }
    /**
     * 游戏进行时
     * 游戏过程中需要做：1、自动化玩家的移动，放置泡泡，死亡
     *               2、地图元素被炸掉后，新元素的增加（道具掉落）
     *               3、游戏暂停
     */

    private void hallOver(){
        GameHall.music.stopLoopMusic();
        typeOfGame = 2;
    }
    private void mainOver(){

        new GameHall(parentFrame);
        List<ElementObj> players = manager.getElementsByKey(GameElement.PLAYER);
        //复制一份游戏玩家信息
        List<ElementObj> copyPlayers = new ArrayList<>(players);
        //将所有资源清空
        for(GameElement key:GameElement.values()){
            List<ElementObj> list = manager.getElementsByKey(key);
            list.clear();
        }

        //根据复制信息对游戏大厅进行初始化
        for(ElementObj player:copyPlayers){
            manager.addElement(GameElement.PLAYER,new Player(player.getNumber(),player.getTeam(),player.getUserName()));
//            GameHall.setUserName(player.getNumber());
        }


    }

    private void hallRun() {
        ObjectInputStream in = GameJFrame.getIn();
        boolean flag = true;
        while (flag) {
                try {
                    System.out.println("hallRun wait for message");
                    Message message = (Message) in.readObject();
//                    allMessage.put(message.number,message);
                    if(message.number == GameJFrame.getNumber()){
                        this.type = message.type;
                    }
                    System.out.println("hallRun get message");
                    flag = doMessage(message);


                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
        }

    }

    private void mainRun()  {
        keyListener = false;
        Special start = new Special(0);
        manager.addElement(GameElement.SPECIAL,start);
        new Thread(start).start();

        ObjectInputStream in = GameJFrame.getIn();
        boolean flag = true;
        while(flag){

            try {
                Message message = (Message) in.readObject();
                flag = doMessage(message);
                Thread.sleep(20);

            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println("游戏结束");
        isOver = false;
    }

    //游戏的加载

    public boolean doMessage(Message message){

        int type = message.type;
        List<ElementObj> players = manager.getElementsByKey(GameElement.PLAYER);

        for(ElementObj player: players){
            if(player.getNumber() == message.number){
                //为空玩家
                if(player.getIcon()!= null && player.getIcon().toString().equals("images/hall/player0.png")){
                    player.setIcon(null);
                    players.remove(player);
                    System.out.println("message.team:" + message.team);
                    manager.addElement(GameElement.PLAYER,new Player(message.number,message.team,message.user));
                    GameHall.setUserName(message.number,message.user);
                    break;
                }
            }

        }

        switch (type) {

            //当检测到有玩家退出
            case 3 -> {
                if (typeOfGame == 1) {
                    int number = message.number;

                    for (ElementObj player : players) {
                        if (player.getNumber() == number) {
                            players.remove(player);
                            players.add(new Player(number, 0));
                            GameHall.setUserName(number, "");
                            break;
                        }
                    }
                } else if (typeOfGame == 2) {
                    //判断游戏是否结束
                    boolean flag = isOver(message);
                    //无论游戏是否结束，都要将该玩家从玩家列表中移除
                    int number = message.number;
                    for (ElementObj player : players) {
                        if (player.getNumber() == number) {
                            players.remove(player);
                            break;
                        }
                    }
                    return flag;
                }
            }

            //当有新玩家添加时
            case 5 -> {
                if(typeOfGame != 1) break;
                int number = message.number;
                GameHall.setUserName(number, message.user);
                //当加入的玩家不是自身时，则意味着有新玩家加入，那么就向该新玩家发送信息，发送自己的信息，使自己的信息也能显示在别人的公屏上
                if (number != GameJFrame.getNumber()) {
                    Message message1 = new Message();
                    message1.number = GameJFrame.getNumber();
                    message1.user = GameJFrame.getUser();
                    message1.team = team;
                    message1.type = this.type;
                    GameJFrame.sendMessage(message1);
                    System.out.println("send new message to new player.");
                } else {
                    System.out.println("my message and init teamButton");
                    team = message.team;
                    if (team == GameKey.TYPE_TEAM_RED) {
                        GameHall.redTeamButton.setVisible(false);
                        GameHall.redTeamButton.setEnabled(false);
                    } else {
                        GameHall.grayTeamButton.setVisible(false);
                        GameHall.grayTeamButton.setEnabled(false);
                    }
                }
            }
            //玩家准备
            case 6 -> {

                int number = message.number;
                for (ElementObj player : players) {
                    if (player.getNumber() == number) {
                        System.out.println("change icon.");
                        player.setReady(true);
                    }
                }

                //判断是否可以开始游戏
                boolean flag = false;
                int validPlayer = 0;
                int teamRed = 0;
                int teamGray = 0;
                for (ElementObj player : players) {
                    if (!player.getIcon().toString().equals("images/hall/player0.png")) {
                        validPlayer++;
                        //若有玩家没有准备，则不能开始游戏
                        //准备玩家中统计每个队的队员数量
                        if (!player.getReady()) {
                            flag = true;
                            break;
                        }else if(player.getTeam() == GameKey.TYPE_TEAM_RED){
                            teamRed++;
                        }else if(player.getTeam() == GameKey.TYPE_TEAM_BLUE){
                            teamGray++;
                        }
                    }
                }
                //当有效玩家小于等于1，游戏不能开始
                if (validPlayer <= 1) flag = true;
                //有一个队一个队员也没有时，游戏同样不能开始
                if(teamGray == 0 || teamRed == 0) flag =true;
                return flag;
            }
            //玩家修改游戏队伍
            case 7 -> {
                for (ElementObj player : players) {
                    if (player.getNumber() == message.number) {
                        player.setReady(false);
                        player.setTeam(message.team);
                    }
                }

                //当为自身想要修改队伍时，修改自身线程的team值
                if(message.number == GameJFrame.getNumber()){
                    team = message.team;
                }
            }
            //有玩家想要聊天
            case 8 -> {
                String chat = "[" + message.number + "]" + message.user + ":" + message.chatText + "\n";
                GameHall.chatAllField.append(chat);
            }
            //当有玩家死亡
            case 9 -> {
                if (typeOfGame != 2) break;
                boolean flag = isOver(message);
////                boolean flag = false;
//                for (ElementObj player : players) {
//                    if (player.getNumber() == message.number) {
//                        player.death();
//                        break;
//                    }
//                }
//
//                for (ElementObj player : players) {
//
//                    if (player.getTeam() == message.team && player.getIcon() != null) {
//                        flag = true;
//                    }
//                }
//
//                //当判定游戏结束时，播放提示结局音效
//                if (!flag) {
//                    if (message.team == team) {
//                        Music.playSingleMusic("music/lose.wav");
//                        Special lose = new Special(2);
//                        manager.addElement(GameElement.SPECIAL,lose);
//                        new Thread(lose).start();
//                        try {
//                            Thread.sleep(2200);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        System.out.println("lose");
//                    } else {
//                        Music.playSingleMusic("music/win.wav");
//                        Special win = new Special(1);
//                        manager.addElement(GameElement.SPECIAL,win);
//                        new Thread(win).start();
//                        try {
//                            Thread.sleep(2200);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        System.out.println("win");
//                    }
//
//                }
                return flag;
            }
            //有玩家发生移动
            case 10 -> {
                if (message.number != GameJFrame.getNumber()) {
                    for (ElementObj player : players) {
                        if (player.getNumber() == message.number) {
                            player.KeyClick(message.isClicked, message.keyCode);
                        }
                    }
                }
            }

            //有玩家放置泡泡
            case 11 -> {
                if (message.number != GameJFrame.getNumber()) {
                    for(ElementObj player:manager.getElementsByKey(GameElement.PLAYER)){
                        if(player.getNumber() == message.number){
                            player.putBomb();
                        }
                    }
                }
            }

            //玩家取消准备
            case 13 -> {
                for (ElementObj player : players) {
                    if (player.getNumber() == message.number) {
                        player.setReady(false);
                    }
                }
            }
        }
        return true;
    }
    private boolean isOver(Message message){
        List<ElementObj> players = manager.getElementsByKey(GameElement.PLAYER);
        boolean flag = false;

        for (ElementObj player : players) {
            if (player.getNumber() == message.number) {
                player.death();
                break;
            }
        }

        for (ElementObj player : players) {

            if (player.getTeam() == message.team && player.getIcon() != null) {
                flag = true;
            }
        }

        //当判定游戏结束时，播放提示结局音效
        if (!flag) {
            if (message.team == team) {
                Music.playSingleMusic("music/lose.wav");
                Special lose = new Special(2);
                manager.addElement(GameElement.SPECIAL,lose);
                new Thread(lose).start();
                try {
                    Thread.sleep(2200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("lose");
            } else {
                Music.playSingleMusic("music/win.wav");
                Special win = new Special(1);
                manager.addElement(GameElement.SPECIAL,win);
                new Thread(win).start();
                try {
                    Thread.sleep(2200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("win");
            }

        }
        return flag;
    }

    public void setOver(boolean isOver){
        this.isOver = isOver;
    }
    public void setTypeOfGame(int type){
        typeOfGame = type;
    }
}
