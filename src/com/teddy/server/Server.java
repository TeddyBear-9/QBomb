package com.teddy.server;


import com.teddy.element.ElementObj;
import com.teddy.manager.ElementManager;
import com.teddy.manager.GameElement;
import com.teddy.util.GameKey;
import com.teddy.util.JDBC;
import com.teddy.manager.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

public class Server {
    public static ArrayList<Integer> numbers = new ArrayList<Integer>(4);
    private static ServerSocket serverSocket;
    private static Hashtable<Socket, ObjectOutputStream> allclient = new Hashtable<>();
    private static int number = 1;
    private static Statement statement;
    private ObjectOutputStream out;
    private Socket clientSocket;

    public static void main(String[] args) {
        new Server();
    }
    public Server(){
        //编号数集，1-4，每次都要进行排序，自动把小的编号给新的客户端。
        for(int n = 1;n <= 4; n++){
            numbers.add(n);
        }
        try {
            JDBC jdbc = new JDBC();
            Connection connection = jdbc.getCon();
            statement = connection.createStatement();
            serverSocket = new ServerSocket(4321);
            while(true){

                //连接客户端
                clientSocket = serverSocket.accept();
                System.out.println("Accept a client connection");
                out = new ObjectOutputStream(clientSocket.getOutputStream());

                //分配编号并将编号和编号对应的队伍传给客户端
                Message message = new Message();
                //每次排序，拿出最小的那个编号
                Collections.sort(numbers);
                number = numbers.get(0);
                numbers.remove(0);
                message.number = number;
                if(number % 2 == 1){
                    message.team = GameKey.TYPE_TEAM_RED;
                }else{
                    message.team = GameKey.TYPE_TEAM_BLUE;
                }
                out.writeObject(message);

                //加入所有的客户端集
                allclient.put(clientSocket,out);
                //创建新的线程并开启新的线程
                ServerThread serverThread = new ServerThread(number,clientSocket,allclient,statement);
                new Thread(serverThread).start();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
class ServerThread implements Runnable{
    private Hashtable<Socket,ObjectOutputStream> allclient;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int number;
    private Socket client;
    private Statement statement;


    public ServerThread(int num, Socket client, Hashtable<Socket,ObjectOutputStream> allclient,Statement statement){
        this.number = num;
        this.client = client;
        this.allclient = allclient;
        this.statement = statement;
        try {
            out = allclient.get(client);
            in = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void run() {
        //开始持续接受来自客户端的消息
        while(true){
            try {
                Message message = (Message) in.readObject();
                //处理消息
                doMessage(message);
            }catch(SocketException e){
                //当有客户端断开时，发送信息给其他客户端
                System.out.println("一个客户端断开了！");
                Message message = new Message();
                message.number = this.number;
                message.type = GameKey.TYPE_PLAYER_EXIT;
                for(ElementObj player: ElementManager.getManager().getElementsByKey(GameElement.PLAYER)){
                    if(player.getNumber() == this.number) message.team = player.getTeam();
                }
                allclient.remove(client);
                //将编号回收
                Server.numbers.add(number);
                for(ObjectOutputStream out :allclient.values()){
                    try {
                        out.writeObject(message);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                break;
            } catch (IOException | ClassNotFoundException e) {
                if (!this.client.isConnected()) break;
            }
        }
    }

    private void doMessage(Message message) {
        int type = message.type;
        switch (type){

            //TYPE_PLAYER_BEFORFALSE = 1
            //即客户端准备登陆
            case 1:{
                String password = message.password;
                int id = message.id;

                ResultSet set;
                try {
                    //检测是否账号密码正确
                    set = statement.executeQuery("select * from login where id = " + id);
                    if(set.isBeforeFirst()){
                        set.next();
                        if(password.equals(set.getString("password"))){
                            message.type = GameKey.TYPE_PLAYER_LOGINTRUE;
                            message.user = set.getString("username");
                            if(this.number % 2 == 1){
                                message.team = GameKey.TYPE_TEAM_RED;
                            }else {
                                message.team = GameKey.TYPE_TEAM_BLUE;
                            }

                        }
                    }else{
                        System.out.println("the set is empty.");
                    }
                    out.writeObject(message);
                } catch (SQLException | IOException throwables) {
                    throwables.printStackTrace();
                }
                //如果登录成功，则发送信息给所有客户端，新的游戏玩家登录游戏大厅
                if(message.type == GameKey.TYPE_PLAYER_LOGINTRUE){

                    message.type = GameKey.TYPE_PLAYER_HALLWAIT;
//
                }
                break;
            }

            case 5:{
                break;
            }
            case 6:{
                System.out.println("ready");
                break;
            }
            case 7:{
                System.out.println("changeTeam");
            }
            case 8:{
                System.out.println("chat");
            }
            case 9:{
                System.out.println("player" + message.number + "die");
            }
            case 10:{
                System.out.println("player" + message.number + "move");
            }
            case 11:{
                System.out.println("put bomb");
            }
            case 13:{
                System.out.println("cancelReady");
            }
        }

        for(ObjectOutputStream out :allclient.values()){
            try {
                out.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}