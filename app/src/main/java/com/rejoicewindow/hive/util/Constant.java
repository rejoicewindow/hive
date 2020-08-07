package com.rejoicewindow.hive.util;



public final class Constant {
    private Constant() {}
    public static final String ProtocolHttp="http://";
    public static final String ProtocolTcp="tcp://";
    public static final String HostOray01="2684gi6381.qicp.vip";
    public static final String HostOray02="colorful-wolf-x.wicp.top";
    public static final String HostAliyun="123.56.133.12";
    public static final String Port80=":80";
    public static final String Port8080=":8080";
    public static final String PortAliyun1883=":1883";
    public static final String PortOray18439=":18439";

    public static final String RegisterUrl=ProtocolHttp+HostOray02+Port80+"/warningsys/appserver/users/?action=register";
    public static final String LoginUrl=ProtocolHttp+HostOray02+Port80+"/warningsys/appserver/users/?action=login";
//    MQTT
    public static final String CarInitsuccess="init_success";//------初始化成功
    public static final String CarProtected="protected";//---------1
    public static final String CarUnProtected="unprotected";//-------2
    public static final String CargetGPS="getGPS";//------------3
    public static final String CarGetPressure="getpressure";//-------4
    public static final String CarGetStatus="getstatus";//---------5
    public static final String CarTrace="trace";//---------5
//    车主设置电瓶车声音报警：
    public static final String CarSettingOff="settingoff";//--------关
    public static final String CarSettingOn="settingon";//---------开
//    车主控制电瓶车发出声音：
    public static final String CarSoundOn="soundon";//-----------开
    public static final String CarSoundOff="soundoff";//----------关
//电动车反馈
   public static final String CarOk="hiveok";//-----------开
   public static final String SureYes="sueryes";//-----------开
   public static final String SureNo="suerno";//-----------开
   //
   public static final String[] AlarmType={"与模块断开连接","蜂箱发生倾倒","蜂箱被移动","蜂箱盖被开启","巢脾被拿出"};
}


//巢框 巢脾
