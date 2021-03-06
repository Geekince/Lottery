package com.wyzk.lottery.constant;


public interface IConst {
    String TOKEN = "token";
    String USER_ID = "userId";
    String IS_ADMIN = "isAdmin";
    String USER_INFO_KEY = "userInfo";
    String ROW_INFO = "rowModel";

    //mqtt
    String BROKEURL = "tcp://120.77.252.48:1883";
    //    String BROKEURL = "tcp://103.56.118.154:1883";
    String USERNAME = "admin";
    String PASSWORD = "public";
    String TOPIC = "game/";
    int QOS = 1;

    //状态
    int STATUS1 = 1;//下注中
    int STATUS2 = 2;//封盘
    int STATUS3 = 3;//结算中
    int STATUS4 = 4;//结算完成
    int STATUS5 = 5;//场次作废

    int GAME_TIME = 60;//游戏时间 60秒

}
