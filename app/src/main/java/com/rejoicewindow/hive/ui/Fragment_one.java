package com.rejoicewindow.hive.ui;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.ContentFrameLayout;
import androidx.fragment.app.Fragment;

import com.rejoicewindow.hive.MainActivity;
import com.rejoicewindow.hive.MyMqttService;
import com.rejoicewindow.hive.Myapplication;
import com.rejoicewindow.hive.R;
import com.rejoicewindow.hive.util.Constant;
import com.rejoicewindow.hive.util.LoadingActivity;
import com.rejoicewindow.hive.util.PollingUtil;
import com.rejoicewindow.hive.util.TaskCenter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Fragment_one extends Fragment {

    Myapplication myapplication;//声明一个对象
    private ImageView btnCar;
    private boolean mCar = false;//false 电动车非保护模式
    private int ColCarProtect = 1;
    private ImageView TempSound;
    private boolean mTemp = true;
    private int ColCarSoundCount = 1;
    private Boolean ColCarSound = false;
    private Boolean ColCarSoundTemp = false;
    private Boolean Complete = true;
    private TextView CarProtect;
    private Spinner CarNumber;

    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private Dialog mDialog;
    private LinearLayout One_lin_02;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    LoadingActivity.closeDialog(mDialog);
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.view_one, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnCar = view.findViewById(R.id.btn_car);
        TempSound = view.findViewById(R.id.car_sound);
        CarProtect = view.findViewById(R.id.carprotect);
        CarNumber = view.findViewById(R.id.car_number);
        One_lin_02=view.findViewById(R.id.one_lin_02);

        myapplication = (Myapplication) getActivity().getApplication();

        One_lin_02.setVisibility(View.GONE);

        CarProtect.setText(R.string.CarNoProtection);
        CarProtect.setTextColor(getContext().getResources().getColor(R.color.car_not_protection));

        //每3秒打印一次日志
        PollingUtil pollingUtil = new PollingUtil(new Handler(getActivity().getMainLooper()));
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                trace();
                UIUpdate();
//                Log.e("A", "----------handler 定时轮询任务----------");
            }
        };
        pollingUtil.startPolling(runnable, 5000, true);

        String[] ls = {"100001", "100002", "100003"};
        list.clear();
        //获取XML中定义的数组
        for (int i = 0; i < ls.length; i++) {
            list.add(ls[i]);
        }
        //把数组导入到ArrayList中
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        //设置下拉菜单的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定适配器
        CarNumber.setAdapter(adapter);
        //设置对话框标题栏
        CarNumber.setPrompt("请选择你要操作的车");
        //Spinner加载监听事件
        CarNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(),String.valueOf(parent.getSelectedItem()),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(!mCar){MyMqttService.publish(Constant.CarProtected);}else {
                MyMqttService.publish(Constant.CarUnProtected);}
                // TODO Auto-generated method stub
                if (ColCarProtect % 4 == 0) {
                    mDialog = LoadingActivity.createLoadingDialog(getContext(), "不能频繁操作哦(^_^)");
                    mHandler.sendEmptyMessageDelayed(1, 5000);
                    ColCarProtect++;
                    return;
                }
                Complete = false;
                mDialog = LoadingActivity.createLoadingDialog(getContext(), "正在切换模式...");
                mHandler.sendEmptyMessageDelayed(1, 2000);
            }
        });

//        public void showMsg(){
//            //获得对象
//            myapplication = (Myapplication) getApplication();
//            if (myapplication.getShow()){
////        textView = (TextView) findViewById(R.id.textView);
////        textView.setText(myapplication.getMsg());
//                myapplication.setShow(false);
//                mHandler.sendEmptyMessage(1);
////        Toast.makeText(getApplicationContext(), "测试", Toast.LENGTH_LONG).show();
//            }
//        }

        TempSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (ColCarSoundCount % 3 == 0) {
                    mDialog = LoadingActivity.createLoadingDialog(getContext(), "不能频繁操作哦(^_^)\n非保护模式不能一直响铃");
                    mHandler.sendEmptyMessageDelayed(1, 5000);
                    ColCarSoundCount++;
                    return;
                }
                mTemp = !mTemp;
                int res = mTemp ? R.drawable.car_sound_false : R.drawable.car_sound_true;
                TempSound.setImageResource(res);
                if (!mTemp) {
                    ColCarSound = true;
                    ColCarSoundTemp = true;
                    MyMqttService.publish(Constant.CarSoundOn);
                    ColCarSoundCount++;
                } else {
                    ColCarSound = true;
                    ColCarSoundTemp = false;
                    MyMqttService.publish(Constant.CarSoundOff);
                    ColCarSoundCount++;
                }
            }
        });
    }

    private void UIUpdate() {
        if (ColCarSound) {
            if (myapplication.getShow() && myapplication.getMsg().contains(Constant.CarOk)) {//包含
                myapplication.setShow(false);
                ColCarSound = false;
            } else {
                if (ColCarSoundTemp) {
                    MyMqttService.publish(Constant.CarSoundOn);
                } else {
                    MyMqttService.publish(Constant.CarSoundOff);
                }
            }
        }
        if (!Complete) {
            ColCarProtect++;
            if (!mCar) {
                if (myapplication.getShow() && myapplication.getMsg().contains(Constant.CarOk)) {//包含
                    CarProtect.setText(R.string.CarProtection);
                    CarProtect.setTextColor(getContext().getResources().getColor(R.color.car_protection));
                    mCar = !mCar;
                    myapplication.setShow(false);
                    Complete = true;
                } else {//不包含
                    Toast.makeText(getContext(), "开启失败，正在重试……", Toast.LENGTH_SHORT).show();
                }
                MyMqttService.publish(Constant.CarProtected);
            } else {
                if (myapplication.getShow() && myapplication.getMsg().contains(Constant.CarOk)) {//包含
                    CarProtect.setText(R.string.CarNoProtection);
                    CarProtect.setTextColor(getContext().getResources().getColor(R.color.car_not_protection));
                    mCar = !mCar;
                    myapplication.setShow(false);
                    Complete = true;
                } else {//不包含
                    Toast.makeText(getContext(), "关闭失败，正在重试……", Toast.LENGTH_SHORT).show();
                }
                MyMqttService.publish(Constant.CarUnProtected);
            }
            int res = mCar ? R.drawable.hive_true : R.drawable.hive_false;
            btnCar.setImageResource(res);
        }
    }

//    private void sendMessage(String msg) {
//        TaskCenter.sharedCenter().send(msg.getBytes());
//    }

}

