package com.rejoicewindow.hive.ui;

import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rejoicewindow.hive.MyMqttService;
import com.rejoicewindow.hive.Myapplication;
import com.rejoicewindow.hive.R;
import com.rejoicewindow.hive.util.Constant;
import com.rejoicewindow.hive.util.PollingUtil;
import com.rejoicewindow.hive.util.TaskCenter;


public class Fragment_two extends Fragment {
    private ImageView PressureImage;
//    private TextView PressureInfo;
    private ImageView AccelerationImage;
    private TextView AccelerationInfo;
    private ImageView GyroImage;
    private TextView GyroInfo;
//    private ImageView GPSImage;
//    private TextView GPSInfo;
    private Button RefreshPressure;
    private Button RefreshAcceleration;
    private Button RefreshGyro;
    private int ImageRes;
    private Boolean SensorStatus;
    Myapplication myapplication;//声明一个对象

    private Boolean Refresh=false;
    private int ColRefresh=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.view_two, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitUI(view);
        Refresh=true;
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

        pollingUtil.startPolling(runnable, 1000, true);

        RefreshPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMqttService.publish(Constant.CarGetPressure);
//                sendMessage(Constant.CarGetPressure);
                Refresh=true;
            }
        });
        RefreshAcceleration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMqttService.publish(Constant.CarGetStatus);
//                sendMessage(Constant.CarGetStatus);
                Refresh=true;
            }
        });
        RefreshGyro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMqttService.publish(Constant.CarGetStatus);
//                sendMessage(Constant.CarGetStatus);
                Refresh=true;
            }
        });

    }

    private void InitUI(View view) {
        PressureImage = view.findViewById(R.id.pressure);
//        PressureInfo = view.findViewById(R.id.pressureinfo);
        AccelerationImage = view.findViewById(R.id.acceleration);
        AccelerationInfo = view.findViewById(R.id.accelerationinfo);
        GyroImage = view.findViewById(R.id.gyro);
        GyroInfo = view.findViewById(R.id.gyroinfo);
        RefreshPressure = view.findViewById(R.id.refresh_pressure);
        RefreshAcceleration = view.findViewById(R.id.refresh_acceleration);
        RefreshGyro = view.findViewById(R.id.refresh_gyro);

        //获得对象
        myapplication = (Myapplication) getActivity().getApplication();


//        PressureInfo.setText(R.string.PressureInfo);
//        PressureInfo.setTextColor(getContext().getResources().getColor(R.color.sensor_not_connected));


        AccelerationInfo.setText(R.string.AccelerationInfo);
        AccelerationInfo.setTextColor(getContext().getResources().getColor(R.color.sensor_not_connected));

        GyroInfo.setText(R.string.GyroInfo);
        GyroInfo.setTextColor(getContext().getResources().getColor(R.color.sensor_not_connected));
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
        SensorStatus = myapplication.GetAccelerationStatus();
        ImageRes = SensorStatus ? R.drawable.acceleration_true : R.drawable.acceleration_false;
        AccelerationImage.setImageResource(ImageRes);
        if (SensorStatus&&myapplication.GetAccelerationValue()!=null) {
            AccelerationInfo.setText(myapplication.GetAccelerationValue());
            AccelerationInfo.setTextColor(getContext().getResources().getColor(R.color.sensor_connected));
        }
        //Gyro
        SensorStatus = myapplication.GetGyroStatus();
        ImageRes = SensorStatus ? R.drawable.gyro_true : R.drawable.gyro_false;
        GyroImage.setImageResource(ImageRes);
        if (SensorStatus&&myapplication.GetGyroValue()!=null) {
            GyroInfo.setText(myapplication.GetGyroValue());
            GyroInfo.setTextColor(getContext().getResources().getColor(R.color.sensor_connected));
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
        if (Refresh) {
            ColRefresh++;
                if (myapplication.getShow()) {//包含
                    myapplication.setShow(false);
                    Refresh = false;
                    RefreshSensorStatus();
//                    Toast.makeText(getContext(), "未刷新，正在等待……", Toast.LENGTH_SHORT).show();
                }
//                else {//不包含
//                    Toast.makeText(getContext(), "未刷新，正在等待……", Toast.LENGTH_SHORT).show();
//                }
        }
    }
//    private void sendMessage(String msg) {
//        TaskCenter.sharedCenter().send(msg.getBytes());
//    }
}
