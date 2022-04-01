package com.teddy.controller;

import com.teddy.util.GameKey;
import com.teddy.element.ElementObj;
import com.teddy.manager.ElementManager;
import com.teddy.manager.GameElement;
import com.teddy.manager.Message;
import com.teddy.show.GameHall;
import com.teddy.show.GameJFrame;
import com.teddy.show.Login;

import java.awt.event.*;
import java.util.List;

/**
 * 用于监听用户的操作
 */
public class GameListener implements KeyListener, MouseMotionListener, MouseListener {
    private final ElementManager manager = ElementManager.getManager();

    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * 按下
     */
    @Override
    public void keyPressed(KeyEvent e) {
        //当为开场动画或者结局动画时，玩家不允许移动，则直接返回

        if(!GameThread.keyListener) return;
        if(e.getKeyCode() == 32){
            List<ElementObj> player = manager.getElementsByKey(GameElement.PLAYER);
            for(ElementObj obj:player){
                if(obj.getNumber() == GameJFrame.getNumber()){
                    //将放炸弹的消息发送给服务器
                    Message message = new Message();
                    message.type = GameKey.TYPE_BUBBLE_PUT;
                    message.number = GameJFrame.getNumber();
                    message.i = obj.getI();
                    message.j = obj.getJ();
                    GameJFrame.sendMessage(message);

                    obj.putBomb();
                }
            }
            return;
        }

        //当摁下回车键
        if(e.getKeyCode() == 10){
            if(GameThread.typeOfGame == 1){
                String chatString = GameHall.inputArea.getText();
                if(chatString != null){
                    GameHall.inputArea.setText("");
                    Message message = new Message();
                    message.number = GameJFrame.getNumber();
                    message.user = GameJFrame.getUser();
                    message.type = GameKey.TYPE_PLAYER_CHAT;
                    message.chatText = chatString;
                    GameJFrame.sendMessage(message);
                }
            }
        }

        if(e.getKeyCode() == 37 || e.getKeyCode() == 38 || e.getKeyCode() == 39 ||e.getKeyCode() == 40){
            //摁下方向键
            List<ElementObj> player = manager.getElementsByKey(GameElement.PLAYER);
            for(ElementObj obj:player){
                if(obj.getNumber() == GameJFrame.getNumber()){
                    Message message = new Message();
                    message.type = GameKey.TYPE_PLAYER_MOVE;
                    message.number = GameJFrame.getNumber();
                    message.keyCode = e.getKeyCode();
                    message.isClicked = true;
                    GameJFrame.sendMessage(message);
                    obj.KeyClick(true,e.getKeyCode());
                    return;
                }
            }
        }


    }

    /**
     * 松开
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == 37 || e.getKeyCode() == 38 || e.getKeyCode() == 39 ||e.getKeyCode() == 40){
            List<ElementObj> player = manager.getElementsByKey(GameElement.PLAYER);
            for(ElementObj obj:player){
                if(obj.getNumber() == GameJFrame.getNumber()){
                    Message message = new Message();
                    message.type = GameKey.TYPE_PLAYER_MOVE;
                    message.number = GameJFrame.getNumber();
                    message.keyCode = e.getKeyCode();
                    message.isClicked = false;
                    GameJFrame.sendMessage(message);
                    obj.KeyClick(false,e.getKeyCode());
                    return;
                }

            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if(e.getSource() == manager.getLoginButton()){
            Message message = new Message();
            message.type = GameKey.TYPE_PLAYER_LOGINFALSE;
            char password[] = Login.getPassword();
            String id = Login.getID();
            message.password = String.copyValueOf(password);
            message.id = Integer.parseInt(id);
            message.number = GameJFrame.getNumber();
            GameJFrame.sendMessage(message);
        }

        if(e.getSource() == GameHall.getReadyButton()){
            System.out.println("readyButton pressed");
            Message message = new Message();
            message.type = GameKey.TYPE_PLAYER_READY;
            message.number = GameJFrame.getNumber();
            GameJFrame.sendMessage(message);
            System.out.println("message send out.");
        }
        if(e.getSource() == GameHall.getCancelButton()){
            Message message = new Message();
            message.type = GameKey.TYPE_PLAYER_CANCELREDAY;
            message.number = GameJFrame.getNumber();
            GameJFrame.sendMessage(message);
        }
        if(e.getSource() == GameHall.redTeamButton){
            System.out.println("redTeamButton clicked");
            GameHall.redTeamButton.setVisible(false);
            GameHall.redTeamButton.setEnabled(false);
            GameHall.grayTeamButton.setVisible(true);
            GameHall.grayTeamButton.setEnabled(true);
            Message message = new Message();
            message.number = GameJFrame.getNumber();
            message.type = GameKey.TYPE_PLAYER_CHANGETEAM;
            message.team = GameKey.TYPE_TEAM_RED;
            GameJFrame.sendMessage(message);
        }
        if(e.getSource() == GameHall.grayTeamButton){
            System.out.println("grayTeamButton clicked");
            GameHall.redTeamButton.setVisible(true);
            GameHall.redTeamButton.setEnabled(true);
            GameHall.grayTeamButton.setVisible(false);
            GameHall.grayTeamButton.setEnabled(false);
            Message message = new Message();
            message.number = GameJFrame.getNumber();
            message.type = GameKey.TYPE_PLAYER_CHANGETEAM;
            message.team = GameKey.TYPE_TEAM_BLUE;
            GameJFrame.sendMessage(message);

        }
        if(e.getSource() == GameHall.sendButton){
            String chatString = GameHall.inputArea.getText();
            if(chatString != null){
                GameHall.inputArea.setText("");
                Message message = new Message();
                message.number = GameJFrame.getNumber();
                message.user = GameJFrame.getUser();
                message.type = GameKey.TYPE_PLAYER_CHAT;
                message.chatText = chatString;
                GameJFrame.sendMessage(message);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
