package com.xsy.util;

import java.sql.*;

public class JDBC {
    private static Connection con;
    private static Statement stm;
    private static ResultSet rs;
    private static String classname="com.mysql.cj.jdbc.Driver";
    private static String url="jdbc:mysql://localhost:3306/bomb?useSSL=false&serverTimezone=GMT%2B8&characterEncoding=utf8&allowPublicKeyRetrieval=true";
    //连接数据库
    public  Connection getCon() {
        try{
            Class.forName(classname);
            System.out.println("驱动加载成功");
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        try{
            con= DriverManager.getConnection(url,"root","123456");
            System.out.println("数据库连接成功");
        }
        catch(Exception e){
            e.printStackTrace(System.err);
            con=null;
        }

        return con;
    }
    public static void close(Statement stm, Connection conn) {
        if(stm!=null) {
            try {
                stm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn!=null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(ResultSet rs, Statement stm, Connection con) {
        if(rs!=null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(stm!=null) {
            try {
                stm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(con!=null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Statement getStm(){
        try {
            stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return stm;
    }
}
