package com.rejoicewindow.hive;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.alibaba.fastjson.JSONObject;
import com.rejoicewindow.hive.util.Constant;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Random;

public class MyMqttService extends Service {

    Myapplication myapplication;//声明一个对象

    private Intent intent;
    private int flags;
    private int startId;
    private String TempMsg;


    public MyMqttService() {
    }

    //    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
    public final String TAG = MyMqttService.class.getSimpleName();
    private static MqttAndroidClient mqttAndroidClient;
    private MqttConnectOptions mMqttConnectOptions;
    public String HOST = "tcp://123.56.133.12:1883";
    //            Helper.ProtocolTcp+ Helper.HostAliyun+ Helper.PortAliyun1883;//服务器地址（协议+地址+端口号）
    public String USERNAME = "";//用户名
    public String PASSWORD = "";//密码

    public static String PUBLISH_TOPIC = "user/app/0001";//发布主题
    public static String Will_TOPIC = "user/will/0001";// 最后的遗嘱
    public static String Sub_TOPIC = "user/hive/0001";//订阅主题
    //    @RequiresApi(api = 21)
//    SharedPreferences settings = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
//    public   String CLIENTID=settings.getString("account", "").toString();//getSeralNumber() ;
    public String CLIENTID = "hive_app"+(int)(1+Math.random()*(100-1+1));

    public static void stop() {
        try {
            mqttAndroidClient.disconnect(); //断开连接
            System.out.println("断开连接");
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
//            = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
//            ? Build.getSerial() : Build.SERIAL;//客户端ID，一般以客户端唯一标识符表示，这里用设备序列号表示


//    /**
//     * 获取手机序列号
//     * @return 手机序列号
//     */
////    @SupperessLint({"NewApi","MissingPermission"})
//    private String getSeralNumber() {
//        String serial = "";
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { //9.0+
//                if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    Activity#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for Activity#requestPermissions for more details.
//                    return getSeralNumber();
//                }
//                serial = Build.getSerial();
//            }
//            else if(Build.VERSION.SDK_INT>Build.VERSION_CODES.N){ //8.0+
//                serial=Build.SERIAL;
//            }
//            else {//8.0-
//                Class<?> c =Class.forName("android.os.SystemProperties");
//                Method get =c.getMethod("get", String.class);
//                serial = (String)get.invoke(c, "ro.serialno");
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            Log.e("e","读取设备序列号异常："+e.toString());
//        }
//        return serial;
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;
        this.flags = flags;
        this.startId = startId;
        init();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 开启服务
     */
    public static void startService(Context mContext) {
        mContext.startService(new Intent(mContext, MyMqttService.class));
    }

    /**
     * 发布 （模拟其他客户端发布消息）
     *
     * @param message 消息
     */
    public static void publish(String message) {
        String topic = PUBLISH_TOPIC;
        Integer qos = 2;
        Boolean retained = false;
        try {
            //参数分别为：主题、消息的字节数组、服务质量、是否在服务器保留断开连接后的最后一条消息
            mqttAndroidClient.publish(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 响应 （收到其他客户端的消息后，响应给对方告知消息已到达或者消息有问题等）
     *
     * @param message 消息
     */
    public void response(String message) {
        String topic = PUBLISH_TOPIC;
        Integer qos = 2;
        Boolean retained = false;
        try {
            //参数分别为：主题、消息的字节数组、服务质量、是否在服务器保留断开连接后的最后一条消息
            mqttAndroidClient.publish(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化
     */
    private void init() {
        String serverURI = HOST; //服务器地址（协议+地址+端口号）
        mqttAndroidClient = new MqttAndroidClient(this, serverURI, CLIENTID);
        mqttAndroidClient.setCallback(mqttCallback); //设置监听订阅消息的回调
        mMqttConnectOptions = new MqttConnectOptions();
//        mMqttConnectOptions.setCleanSession(true); //设置是否清除缓存
        mMqttConnectOptions.setConnectionTimeout(10); //设置超时时间，单位：秒
        mMqttConnectOptions.setKeepAliveInterval(60); //设置心跳包发送间隔，单位：秒
//        mMqttConnectOptions.setUserName(USERNAME); //设置用户名
//        mMqttConnectOptions.setPassword(PASSWORD.toCharArray()); //设置密码

        // last will message
        boolean doConnect = true;
        String message = "{\"terminal_uid\":\"" + CLIENTID + "\"}";
        String topic = Will_TOPIC;
        Integer qos = 2;
        Boolean retained = false;
        if ((!message.equals("")) || (!topic.equals(""))) {
            // 最后的遗嘱
            try {
                mMqttConnectOptions.setWill(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
            } catch (Exception e) {
//                Log.i(TAG, "Exception Occured", e);
                doConnect = false;
                iMqttActionListener.onFailure(null, e);
            }
        }
        if (doConnect) {
            doClientConnection();
        }
    }

    /**
     * 连接MQTT服务器
     */
    private void doClientConnection() {
        if (!mqttAndroidClient.isConnected() && isConnectIsNomarl()) {
            try {
                mqttAndroidClient.connect(mMqttConnectOptions, null, iMqttActionListener);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断网络是否连接
     */
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
//            Log.i(TAG, "当前网络名称：" + name);
            return true;
        } else {
//            Log.i(TAG, "没有可用网络");
            /*没有可用网络的时候，延迟3秒再尝试重连*/
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doClientConnection();
                }
            }, 3000);
            return false;
        }
    }

    //MQTT是否连接成功的监听
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i(TAG, "连接成功 ");
            try {
                mqttAndroidClient.subscribe(Sub_TOPIC, 2);//订阅主题，参数：主题、服务质量
//                Toast.makeText(getApplicationContext(), "连接服务器成功", Toast.LENGTH_SHORT).show();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
//            Log.i(TAG, "连接失败 ");
            Toast.makeText(getApplicationContext(), "连接服务器失败", Toast.LENGTH_LONG).show();
            doClientConnection();//连接失败，重连（可关闭服务器进行模拟）
        }
    };


    //订阅主题的回调
    private MqttCallback mqttCallback = new MqttCallback() {
        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
//            Log.i(TAG, "收到消息： " + new String(message.getPayload()));
            //获得对象
            myapplication = (Myapplication) getApplication();

            myapplication.setShow(true);
            myapplication.setMsg(new String(message.getPayload()));
            //收到消息，这里弹出Toast表示。如果需要更新UI，可以使用广播或者EventBus进行发送
//            Toast.makeText(getApplicationContext(), "收到: " + new String(message.getPayload()), Toast.LENGTH_SHORT).show();
            TempMsg = new String(message.getPayload());
            JSONObject jsonObject = JSONObject.parseObject(TempMsg);
            if (jsonObject.getIntValue("alarm") != 0) {
                myapplication.SetAlarmType(jsonObject.getIntValue("alarm"));
                if (jsonObject.getIntValue("alarm") == 4) {
                    myapplication.setHeivPeer(jsonObject.getIntValue("position"));
                }
            }  else if (jsonObject.getIntValue("0A") != 0 || jsonObject.getIntValue("1A") != 0 || jsonObject.getIntValue("3A") != 0 ) {
                myapplication.setAccelerationStatus(true);
                myapplication.setGyroStatus(true);
                myapplication.setAccelerationValue("三轴：\n" + jsonObject.getIntValue("0A") + "\n" + jsonObject.getIntValue("1A") + "\n" + jsonObject.getIntValue("2A"));
                myapplication.setGyroValue("三轴：\n" + jsonObject.getIntValue("0G") + "\n" + jsonObject.getIntValue("1G") + "\n" + jsonObject.getIntValue("2G"));
            }
                else if (jsonObject.getIntValue("0G") != 0 || jsonObject.getIntValue("1G") != 0 || jsonObject.getIntValue("3G") != 0) {
                myapplication.setAccelerationStatus(true);
                myapplication.setGyroStatus(true);

                myapplication.setGyroValue("三轴：\n" + jsonObject.getIntValue("0G") + "\n" + jsonObject.getIntValue("1G") + "\n" + jsonObject.getIntValue("2G"));
            }
//            System.out.println("++++++++++" + jsonObject.getDouble("lo"));
////            System.out.println("==========" + jsonObject.getIntValue("pr"));
//            System.out.println("==========" + jsonObject.getIntValue("0A"));
//            System.out.println("$$$$$$$$$$" + jsonObject.getIntValue("alarm"));

            //收到其他客户端的消息后，响应给对方告知消息已到达或者消息有问题等
//            response("appok");
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }

        @Override
        public void connectionLost(Throwable arg0) {
//            Log.i(TAG, "连接断开 ");
            doClientConnection();//连接断开，重连
        }
    };


    @Override
    public void onDestroy() {
        try {
            mqttAndroidClient.disconnect(); //断开连接
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
