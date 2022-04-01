package com.xsy.manager;

import com.xsy.component.ImageButton;
import com.xsy.element.ElementObj;
import com.xsy.show.GameHall;

import javax.swing.*;
import java.util.*;

/**
 * 元素管理器，专门负责存储所有的元素，同时，提供方法给与V和C获取数据
 * @author xsy
 */
public class ElementManager {
    //存储游戏中的所有元素
    private HashMap<GameElement, List<ElementObj>> gameElements;
    private ImageButton loginButton;




    //单例化设计模式
    private static ElementManager manager = new ElementManager();

    //构造方法私有
    private  ElementManager(){
        init();
    }

    //获得游戏中的各个元素
    public  HashMap<GameElement, List<ElementObj>> getGameElements(){
        return gameElements;
    }

    //获得唯一一个对象
    public static ElementManager getManager(){
        return manager;
    }

    /**
     * 为将来可能出现的功能扩展，重写init方法
     */
    //实例化
    public void init(){
        //hash散列
        gameElements = new HashMap<GameElement, List<ElementObj>>();
        //将每种元素集合都放入到map中
        for(GameElement key:GameElement.values()){
            gameElements.put(key, new ArrayList<ElementObj>());
        }
    }

    //添加元素（由加载器调用）
    public void addElement(GameElement key,ElementObj obj ){
        //添加对象到集合中，按key值进行存储
        gameElements.get(key).add(obj);
    }

    //删除元素



    //依据key返回list集合，取出某一类元素
    public List<ElementObj> getElementsByKey(GameElement key){
        return gameElements.get(key);
    }

    public ImageButton getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(ImageButton loginButton) {
        this.loginButton = loginButton;
    }


    public void clearAll(){
        for(GameElement key:GameElement.values()){
            gameElements.get(key).clear();
        }
    }
}


