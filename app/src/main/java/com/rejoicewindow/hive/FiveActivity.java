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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rejoicewindow.hive.util.Constant;
import com.rejoicewindow.hive.util.DbHelper;
import com.rejoicewindow.hive.util.PollingUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FiveActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Vibrator vibrator;
    private String AlarmMsg;
    Myapplication myapplication;//声明一个对象

    private TextView LocationInfo;
    private TextView AccelerationInfo;
    private TextView GyroInfo;
    private Spinner HiveNumberShow;
    private String[] HiveNumberList = {"hive0001", };
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five);
        myapplication = (Myapplication) getApplication();

        LocationInfo = findViewById(R.id.location_info);
        AccelerationInfo = findViewById(R.id.acceleration);
        GyroInfo = findViewById(R.id.gyroinfo);
        HiveNumberShow = (Spinner) findViewById(R.id.hive_number_show);
        list.clear();
        if (HiveNumberList != null) {
            //获取XML中定义的数组
            for (int i = 0; i < HiveNumberList.length; i++) {
                list.add(HiveNumberList[i]);
            }
        }

        //把数组导入到ArrayList中
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        //设置下拉菜单的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        HiveNumberShow.setPrompt("请选择你要查看的蜂箱");
        //绑定适配器
        HiveNumberShow.setAdapter(adapter);

        //每3秒打印一次日志
        PollingUtil pollingUtil = new PollingUtil(new Handler(this.getMainLooper()));
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                trace();
                UIUpdate();
//                Log.e("A", "----------handler 定时。询任务----------");
            }
        };
        pollingUtil.startPolling(runnable, 2000, true);
    }

    private void RefreshSensorStatus() {
//        //Pressure
//        SensorStatus = myapplication.GetPressureStatus();
//        ImageRes = SensorStatus ? R.drawable.pressure_true : R.drawable.pressure_false;
//        PressureImage.setImageResource(ImageRes);
//        if (SensorStatus&&myapplication.GetPressureValue()!=null) {
//            PressureInfo.setText(myapplication.GetPressureValue());
//            PressureInfo.setTextColor(getContext().getResources().getColor(R.color.sensor_connected));
//        }
        //Acceleration
        if (myapplication.GetAccelerationValue()!=null) {
            AccelerationInfo.setText(myapplication.GetAccelerationValue());
//            AccelerationInfo.setTextColor(getResources().getColor(R.color.sensor_connected));
        }
        //Gyro
        if (myapplication.GetGyroValue()!=null) {
            GyroInfo.setText(myapplication.GetGyroValue());
//            GyroInfo.setTextColor(getResources().getColor(R.color.sensor_connected));
        }
//        //GPS
//        SensorStatus = myapplication.GetGPSStatus();
//        ImageRes = SensorStatus ? R.drawable.gps_true : R.drawable.gps_false;
//        GPSImage.setImageResource(ImageRes);
//        if (SensorStatus) {
//            GPSInfo.setText(myapplication.GetGPSValue());
//            GPSInfo.setTextColor(getContext().getResources().getColor(R.color.sensor_connected));
////            GPSInfo.setTextSize();
//        } else {
//            GPSInfo.setText(R.string.GPSInfo);
//            GPSInfo.setTextColor(getContext().getResources().getColor(R.color.sensor_not_connected));
//        }
    }

    private void UIUpdate() {
                RefreshSensorStatus();
//                 Toast.makeText(getContext(), "未刷新，正在等待……", Toast.LENGTH_SHORT).show();
//                else {//不包含
//                    Toast.makeText(getContext(), "未刷新，正在等待……", Toast.LENGTH_SHORT).show();
//                }
        }


}
