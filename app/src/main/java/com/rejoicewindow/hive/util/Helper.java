package com.rejoicewindow.hive.util;



public final class Helper {
    private Helper() {}
    public static final String ProtocolHttp="http://";
    public static final String ProtocolTcp="tcp://";
    public static final String HostOray01="2684gi6381.qicp.vip";
    public static final String HostOray02="colorful-wolf-x.wicp.top";
    public static final String HostAliyun="123.56.133.12";
    public static final String Port80=":80";
    public static final String Port8080=":8080";
    public static final String PortAliyun1883=":1883";
    public static final String PortOray18439=":18439";

    public static final String RegisterUrl=ProtocolHttp+HostAliyun+Port8080+"/petfeeder/appserver/users/?action=register";
    public static final String LoginUrl=ProtocolHttp+HostAliyun+Port8080+"/petfeeder/appserver/users/?action=login";
    public static final String GtUserInfoUrl=ProtocolHttp+HostAliyun+Port8080+"/petfeeder/appserver/users/";
    public static final String StartChatUrl=ProtocolHttp+HostAliyun+Port8080+"/petfeeder/mediaserver/startchat/";
    public static final String EndChatUrl=ProtocolHttp+HostAliyun+Port8080+"/petfeeder/mediaserver/endchat/";
    public static final String AddAnimalUrl=ProtocolHttp+HostAliyun+Port8080+"/petfeeder/appserver/animals/";
    public static final String AddFeederUrl=ProtocolHttp+HostAliyun+Port8080+"/petfeeder/feederserver/bind/?action=bind";
    public static final String HeadUrl=ProtocolHttp+HostAliyun+Port8080+"/petfeeder/appserver/animals/";
    public static final String GatHistoryRecordsUrl=ProtocolHttp+HostAliyun+Port8080+"/petfeeder/appserver/history/";
//    public static final String GatHistoryRecordsUrl=ProtocolHttp+"127.0.0.1:8000/petfeeder/appserver/history/";
    public static final String ShareUrl=ProtocolHttp+HostAliyun+Port8080+"/petfeeder/mediaserver/shares/";
    public static final String AddRecordUrl=ProtocolHttp+HostAliyun+Port8080+"/petfeeder/appserver/records/";


    //按钮：
    public static final String OpenTray="tray_on";
    public static final String CloseTray="tray_off";
    public static final String FoodOut="food_out";
    public static final String FoodGet="food_get";
    public static final String FoodOutCount="food_out$";
    public static final String ChatStart="chat_start";
    public static final String ChatToken="";
    public static final String[] AlarmType={"a","余粮不足","水不足","食物卡住，未放出","喂食器掉线"};
}





