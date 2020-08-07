package com.rejoicewindow.hive.ui;

import android.app.Dialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.rejoicewindow.hive.HiveActivity;
import com.rejoicewindow.hive.Myapplication;
import com.rejoicewindow.hive.R;
import com.rejoicewindow.hive.util.Constant;
import com.rejoicewindow.hive.util.LoadingActivity;
import com.rejoicewindow.hive.util.PollingUtil;
import com.rejoicewindow.hive.util.TaskCenter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_three extends Fragment {


    Myapplication myapplication;//声明一个对象

//    private Switch CarSoundAlert;
private Switch SoundAlert;
    private Switch ToastAlert;

    private Switch Vibrationalert;
    private Button ManageCar;
    private Dialog mDialog;
    private  String msg;
    private Boolean HiveSetting=false;
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1:
//                    LoadingActivity.closeDialog(mDialog);
//                    break;
//            }
//        }
//    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        //每3秒打印一次日志
//        PollingUtil pollingUtil = new PollingUtil(new Handler(getActivity().getMainLooper()));
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
////                trace();
//                UIUpdate();
////                Log.e("A", "----------handler 定时轮询任务----------");
//            }
//        };
//        pollingUtil.startPolling(runnable, 5000, true);


    }

    void  InitUI(View view){
        SoundAlert =view.findViewById(R.id.soundalert);
//        CarSoundAlert=view.findViewById(R.id.carsoundalert);
        ToastAlert=view.findViewById(R.id.toastalert);
        ManageCar=view.findViewById(R.id.manager_car);
        Vibrationalert=view.findViewById(R.id.vibrationalert);
        SharedPreferences settings = getContext().getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
        SoundAlert.setChecked(settings.getBoolean("SoundAlert", false));
//        CarSoundAlert.setChecked(settings.getBoolean("CarSoundAlert", false));
        ToastAlert.setChecked(settings.getBoolean("ToastAlert", false));
        Vibrationalert.setChecked(settings.getBoolean("VibrationAlert", false));
        myapplication = (Myapplication) getActivity().getApplication();
//        Toast.makeText(getApplicationContext(), String.valueOf(settings.getBoolean("ToastAlert", false)), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT).show();
        LinearLayout Setting01=view.findViewById(R.id.setting01);
        Setting01.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.view_three, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myapplication = (Myapplication) getActivity().getApplication();
        InitUI(view);
        final SharedPreferences settings = getContext().getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
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
                Toast.makeText(getContext(), "设置已保存", Toast.LENGTH_SHORT).show();
            }
        });
        ToastAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean status) {
                editor.putBoolean("ToastAlert",status);
                editor.apply();
//                myapplication.setAppSettings(true);
                Toast.makeText(getContext(), "设置已保存", Toast.LENGTH_SHORT).show();
            }});
        Vibrationalert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean status) {
                editor.putBoolean("VibrationAlert",status);
                editor.apply();
//                myapplication.setAppSettings(true);
                Toast.makeText(getContext(), "设置已保存", Toast.LENGTH_SHORT).show();
            }});
//        CarSoundAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean status) {
////                myapplication.setCarSettings(true);
//                editor.putBoolean("CarSoundAlert",status);
//                editor.apply();
//                System.out.println("-----------------------------"+myapplication.getCarSettings());
//                    mDialog = LoadingActivity.createLoadingDialog(getContext(), "正在同步");
////                Toast.makeText(SettingsActivity.this, "请查看地图所示位置是否正确……然后直接返回", Toast.LENGTH_SHORT).show();
//                mHandler.sendEmptyMessageDelayed(1, 5000);
//                HiveSetting=true;
//                msg=Constant.CarSettingOff;
//                if(status)msg= Constant.CarSettingOn;
//                sendMessage(msg);
//                if (myapplication.getShow() && myapplication.getMsg().contains(Constant.CarOk)) {//包含
//                    Toast.makeText(getContext(), "同步成功\n设置已保存", Toast.LENGTH_SHORT).show();
//                    HiveSetting=false;
//                }
//            }});

        ManageCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), HiveActivity.class);
                startActivity(i);
            }
        });

    }
    private void sendMessage(String msg) {
        TaskCenter.sharedCenter().send(msg.getBytes());
    }
//
//    private void UIUpdate() {
//if(HiveSetting){
//    if (myapplication.getShow() && myapplication.getMsg().contains(Constant.CarOk)) {//包含
//        Toast.makeText(getContext(), "同步成功\n设置已保存", Toast.LENGTH_SHORT).show();
//        HiveSetting=false;
//         }
//    else {
//        Toast.makeText(getContext(), "设置失败，正在重试……", Toast.LENGTH_SHORT).show();
//    }
//}
////        Toast.makeText(getContext(), "同步成功\n设置已保存", Toast.LENGTH_SHORT).show();
//    }
}
