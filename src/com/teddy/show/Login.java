package com.teddy.show;

import com.teddy.util.GameKey;
import com.teddy.component.Music;
import com.teddy.component.ImageButton;
import com.teddy.element.ElementObj;
import com.teddy.manager.ElementManager;
import com.teddy.manager.GameElement;
import com.teddy.manager.Message;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;


/**
 * 游戏主要面板
 * @author 熊师意
 * 主要进行元素的显示，同时进行界面的刷新（多线程）
 */
public class Login extends JPanel implements Runnable{
    private static ElementManager manager;
    private ImageButton loginButton;
    private static JTextField user;
    private static JPasswordField password;
    private GameJFrame parentFrame;

    private ObjectInputStream in;
    private ImageIcon back = new ImageIcon("images/login/login_background.png");
    private Music music;

    public Login(){
        init();
    }
    public Login(GameJFrame parentFrame){
        this.parentFrame = parentFrame;
        this.in = GameJFrame.getIn();
        init();
    }
    /**
     * 导入素材文件
     */

    public void init(){
        manager = ElementManager.getManager();//获得元素管理器对象
        repaint();
        music = new Music("music/loginBg.wav");
        music.playLoopMusic();
    }

    /**
     * 用于绘画
     * @param g
     */
    @Override
    public void paint(Graphics g){
        super.paint(g);
        //Map<GameElement, List<ElementObj>> all = manager.getGameElements();
        //按枚举类中的顺序绘画，MAPS作为背景，应该首先绘画
        for(GameElement key:GameElement.values()){
            List<ElementObj> list = manager.getElementsByKey(key);
            for(int i = 0; i < list.size(); i++){
                ElementObj obj = list.get(i);
                obj.showElement(g);
            }
        }
    }

    @Override
    public void run() {
        this.setLayout(null);//设置绝对布局
        this.loginButton = new ImageButton(new ImageIcon("images/login/login_button.png"));
        loginButton.setPressedIcon(new ImageIcon("images/login/login_button_2.png"));
        loginButton.setRolloverIcon(new ImageIcon("images/login/login_button_2.png"));
        loginButton.setLocation(430,530);
        GameJFrame.addButton(loginButton);
        manager.setLoginButton(loginButton);

        user = new JTextField();
        Border border = BorderFactory.createEmptyBorder(1, 0,0,0);
        user.setBorder(border);
        user.setLocation(325, 463);
        user.setSize(115,20);
        user.setBackground(new Color(233,247,248));
        this.add(user);


        password = new JPasswordField();
        password.setBorder(border);
        password.setLocation(325, 493);
        password.setSize(115,20);
        password.setEchoChar('*');
        password.setBackground(new Color(233,247,248));
        this.add(password);

        while(true){
            try {
                Message message = (Message) in.readObject();
                if(message.type == GameKey.TYPE_PLAYER_LOGINTRUE){

                    music.stopLoopMusic();
                     new GameHall(parentFrame);
                    GameJFrame.setUser(message.user);

                    break;
                }else{
                    System.out.println("密码错误");
//                    JOptionPane.showMessageDialog( null , "密码错误" ,"警告" , JOptionPane.ERROR_MESSAGE) ;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    public static char[] getPassword() {
        return password.getPassword();
    }
    public static String getID(){
        return user.getText();
    }
    protected void paintComponent(Graphics g) {
        g.drawImage(back.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
    }
}
