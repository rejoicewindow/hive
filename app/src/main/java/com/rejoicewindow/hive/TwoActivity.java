package com.rejoicewindow.hive;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rejoicewindow.hive.util.Constant;
import com.rejoicewindow.hive.util.DbHelper;
import com.rejoicewindow.hive.util.PollingUtil;


import java.text.SimpleDateFormat;
import java.util.Date;

public class TwoActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Vibrator vibrator;
    private String AlarmMsg;
    Myapplication myapplication;//声明一个对象


    private LinearLayout TwoLay1;
    private LinearLayout TwoLay2;
    private LinearLayout TwoLay3;
    private LinearLayout TwoLay4;

    private int Alarm=0;
    private int Time=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        myapplication = (Myapplication) getApplication();
        MyMqttService.startService(this); //开启服务
        //每3秒打印一次日志
        PollingUtil pollingUtil = new PollingUtil(new Handler(this.getMainLooper()));
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                trace();
                AlarmUpdate();
//                Log.e("A", "----------handler 定时。询任务----------");
                if(Alarm>0){
                    switch (Alarm){
                        case 1:MyMqttService.publish(Constant.SureYes);
                            break;
                        case 2:MyMqttService.publish(Constant.SureNo);
                            break;
                    }
//                    if(myapplication.getMsg().contains("hiveok"))
//                    {
//                        Alarm=0;
//
//                        System.out.println("包含该字符串");
//                    }
//                    else if(myapplication.getShow()){
                        Alarm=0;
//                        System.out.println("包含该字符串");
//                    }

                }
            }
        };
        pollingUtil.startPolling(runnable, 100, true);
        TwoLay1=findViewById(R.id.two_lay1);
        TwoLay2=findViewById(R.id.two_lay2);
        TwoLay3=findViewById(R.id.two_lay3);
        TwoLay4=findViewById(R.id.two_lay4);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else{
            initMediaPlayer();  //初始化MediaPlayer
        }

        TwoLay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TwoActivity.this, ThreeActivity.class);
                startActivity(i);
            }
        });
        TwoLay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TwoActivity.this, FourActivity.class);
                startActivity(i);
            }
        });
        TwoLay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TwoActivity.this, FiveActivity.class);
                startActivity(i);
            }
        });
        TwoLay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TwoActivity.this, SixActivity.class);
                startActivity(i);
            }
        });
    }

    void  AlarmUpdate(){
        if(0<myapplication.GetAlarmType()&&myapplication.GetAlarmType()<5){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");// HH:mm:ss
//获取当前时间
            Date date = new Date(System.currentTimeMillis());
            SQLiteDatabase db;
            ContentValues values;
            DbHelper dbHelper = new DbHelper(getApplication());
            db = dbHelper.getWritableDatabase();
            if(myapplication.GetAlarmType()==1){
                values = new ContentValues();
                values.put("number", "hive0001");
                values.put("time",simpleDateFormat.format(date));
                values.put("is_1","1");
                db.insert("rdump", null, values);
                db.close();
            }else {
                values = new ContentValues();
                values.put("number", "hive0001");
                values.put("time",simpleDateFormat.format(date));
                values.put("is","1");
                db.insert("rsteal", null, values);
                db.close();
            }
            SharedPreferences settings = getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
//            System.out.println(settings.getBoolean("ToastAlert", false));
            if( settings.getBoolean("VibrationAlert", false)) {
                vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
                long[] patter = {1000, 1000, 2000, 50};
                vibrator.vibrate(patter, 0);
            }
            if(settings.getBoolean("SoundAlert", false)){
                //如果媒体没有在播放，则开始播放
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }
            }
            if(myapplication.GetAlarmType()==4){
                AlarmMsg="警报类型：\n"+ "第"+myapplication.getHeivPeer()+"个"+ Constant.AlarmType[myapplication.GetAlarmType()]+"          \n确认是否为本人操作？";
            }else {
                AlarmMsg="警报类型：\n"+Constant.AlarmType[myapplication.GetAlarmType()]+"\n确认是否为本人操作？";
            }

            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("来自蜂箱 hive0001 的警报")
                    .setMessage(AlarmMsg)
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if(mediaPlayer.isPlaying()){
                                mediaPlayer.reset(); //停止播放
                                initMediaPlayer();
                            }

                            SharedPreferences settings = getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
                            if( settings.getBoolean("VibrationAlert", false)) {
                                vibrator.cancel();
                            }
                            Toast.makeText(getBaseContext(), "报警已经结束，请前往处理", Toast.LENGTH_SHORT).show();
                            Alarm=1;
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if(mediaPlayer.isPlaying()){
                                mediaPlayer.reset(); //停止播放
                                initMediaPlayer();
                            }
                            SharedPreferences settings = getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
                            if( settings.getBoolean("VibrationAlert", false)) {
                                vibrator.cancel();
                            }
                            Toast.makeText(getBaseContext(), "报警已经结束，请前往处理", Toast.LENGTH_SHORT).show();
                            Alarm=2;
                        }
                    }).create();
            alertDialog.show();
            myapplication.SetAlarmType(0);
        }
    }

    private void initMediaPlayer() {
        try{
            //2、从文件系统播放
//            File file = new File(Environment.getExternalStorageDirectory(),"music.mp3");
//            mediaPlayer.setDataSource(file.getPath());  //为音频指定路径
//            mediaPlayer.prepare();  //使音频加入准备状态
            mediaPlayer = MediaPlayer.create(this, R.raw.music);  //添加本地资源
            mediaPlayer.setLooping(true);//设置循环
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch(requestCode){
            case 1:
                if(grantResults.length>=0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    initMediaPlayer();
                }else {
                    Toast.makeText(this,"未获得授权",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }
    /*在程序关闭时，停止音乐播放并清空资源*/
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        MyMqttService.stop();
    }
}
