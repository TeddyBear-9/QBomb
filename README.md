# Q版泡泡堂-TeddyBear-09
## 前言
本项目为在校大二期间个人独立开发的Q版泡泡堂项目，当时刚学完面向对象编程、数据结构和算法。
当时为了降低代码耦合性，在短期内快速自学MVC架构，做的很粗糙，希望对大家的学习会有所帮助。
## 创新点
将所有游戏美术素材进行分层，将自动根据枚举类GameElement中的枚举顺序进行绘制。

同时将地图元素素材切割为两份，分为背景和前景，将绘制顺序调整为背景->人物->前景,使得游戏画面在2D的基础上增添一些立体感。
<table>
    <tr>
        <td ><center><img src="https://raw.githubusercontent.com/TeddyBear-9/QBomb/master/png/before.png" width="100%"></center></td>
        <td ><center><img src="https://raw.githubusercontent.com/TeddyBear-9/QBomb/master/png/after.png"  width="100%"></center></td>
    </tr>
  <tr>
        <td ><center>实现之前</center></td>
        <td ><center>实现之后</center></td>
    </tr>
</table>

## 系统结构设计
![系统设计.png](https://raw.githubusercontent.com/TeddyBear-9/QBomb/master/png/%E7%B3%BB%E7%BB%9F%E8%AE%BE%E8%AE%A1.png)
## 数据库设计
当时还没学过数据库，所以只是很简单的做了用户登录验证。
### 具体设计
![数据库设计.png](https://raw.githubusercontent.com/TeddyBear-9/QBomb/master/png/image.png)
### 如何更改设置连接到你自己的MySQL数据库
在JDBC.java文件中，修改url，表名，字段名。

默认数据库：bomb

默认表名：login
## 效果图和用户手册图解
