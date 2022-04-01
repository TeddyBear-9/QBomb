package com.xsy.util;

public class GameKey {
    //在注册界面的状态
    public static int TYPE_PLAYER_LOGINFALSE = 1;
    public static int TYPE_PLAYER_LOGINTRUE = 2;
    public static int TYPE_PLAYER_EXIT = 3;
    public static int TYPE_PLAYER_REGISTER = 4;

    //在游戏大厅界面的状态
    public static int TYPE_PLAYER_HALLWAIT = 5;
    public static int TYPE_PLAYER_READY = 6;
    public static int TYPE_PLAYER_CHANGETEAM = 7;
    public static int TYPE_PLAYER_CHAT = 8;
    public static int TYPE_PLAYER_DEATH = 9;

    //在游戏界面的状态
    public static int TYPE_PLAYER_MOVE = 10;
    public static int TYPE_BUBBLE_PUT = 11;
    public static int TYPE_PROP_DROP = 12;

    public static int TYPE_PLAYER_CANCELREDAY = 13;

    public static int TYPE_TEAM_RED = 1;
    public static int TYPE_TEAM_BLUE = 2;
}
