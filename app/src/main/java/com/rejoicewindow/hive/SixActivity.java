package com.rejoicewindow.hive;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.rejoicewindow.hive.util.Constant;
import com.rejoicewindow.hive.util.DbHelper;
import com.rejoicewindow.hive.util.PollingUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SixActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Vibrator vibrator;
    private String AlarmMsg;
    Myapplication myapplication;//声明一个对象
    private Switch SoundAlert;
    private Switch NetConnect;

    private Switch Vibrationalert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_six);
        myapplication = (Myapplication) getApplication();
        InitUI();
        final SharedPreferences settings = getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = settings.edit();

        // 设置开关更新监听
        SoundAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean status) {
                editor.putBoolean("SoundAlert",status);
                editor.apply();
//                myapplication.setAppSettings(true);
//                Toast.makeText(getApplicationContext(), String.valueOf(status), Toast.LENGTH_SHORT).show();
//                if(isChecked) {
////                    //选中时 do some thing
////                    editor.putBoolean("SoundAlert",true);
////                    Toast.makeText(getApplicationContext(), "on", Toast.LENGTH_SHORT).show();
////                } else {
////                    //非选中时 do some thing
////                    editor.putBoolean("SoundAlert",false);
////                    Toast.makeText(getApplicationContext(), "off", Toast.LENGTH_SHORT).show();
////                }
                Toast.makeText(SixActivity.this, "设置已保存", Toast.LENGTH_SHORT).show();
            }
        });
        NetConnect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean status) {
                editor.putBoolean("NetConnect",status);
                editor.apply();
//                myapplication.setAppSettings(true);
                Toast.makeText(SixActivity.this, "设置已保存", Toast.LENGTH_SHORT).show();
            }});
        Vibrationalert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean status) {
                editor.putBoolean("VibrationAlert",status);
                editor.apply();
//                myapplication.setAppSettings(true);
                Toast.makeText(SixActivity.this, "设置已保存", Toast.LENGTH_SHORT).show();
            }});
    }
    void  InitUI(){
        SoundAlert =findViewById(R.id.soundalert);
//        CarSoundAlert=view.findViewById(R.id.carsoundalert);
        NetConnect=findViewById(R.id.net_connect);
        Vibrationalert=findViewById(R.id.vibrationalert);
        SharedPreferences settings =getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
        SoundAlert.setChecked(settings.getBoolean("SoundAlert", false));
//        CarSoundAlert.setChecked(settings.getBoolean("CarSoundAlert", false));
        NetConnect.setChecked(settings.getBoolean("NetConnect", false));
        Vibrationalert.setChecked(settings.getBoolean("VibrationAlert", false));
//        Toast.makeText(getApplicationContext(), String.valueOf(settings.getBoolean("ToastAlert", false)), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT).show();
    }


}
