package com.rejoicewindow.hive;

import android.app.Application;
import android.widget.Button;

public class Myapplication extends Application {

    public boolean Show;
    public String Msg;

    public boolean InitModule;
    public boolean AppSettings;
    public boolean CarSettings;
    //声明一个变量
    public double latitude;
    public double longitude;
    public Boolean CarStatus;
    //sensor
    public String PressureValue;
    public boolean PressureStatus;
    public String AccelerationValue;
    public boolean AccelerationStatus ;
    public String GyroValue;
    public boolean GyroStatus;
    public String GPSValue;
    public boolean GPSStatus;

    public int AlarmType=0;
    public int HeivPeer=0;
    @Override
    public void onCreate() {

        // TODO Auto-generated method stub
    super.onCreate();
        setlocation(29.999231, 105.301192);
//        SetCarStatus(false);
        setMsg("mqtt");
        setShow(false);
        setInitModule(false);
        setAppSettings(false);
        setCarSettings(true);
        setPressureStatus(false);
        setAccelerationStatus(false);
        setGyroStatus(false);
        setGPSStatus(false);
        SetAlarmType(0);
    }


    public void setShow(boolean s) {
        this.Show=s;
    }
    public boolean getShow(){
        return  Show;
    }
    public int getHeivPeer(){
        return  this.HeivPeer;
    }
    public void setHeivPeer(int HeivPeer) {
        this.HeivPeer=HeivPeer;
    }
    public void setMsg(String s) {
        this.Msg=s;
    }
    public String getMsg(){
        return  Msg;
    }

    public void setInitModule(Boolean InitModule) {
        this.InitModule=InitModule;
    }
    public Boolean getInitModule(){
        return  this.InitModule;
    }

    public void setAppSettings(Boolean AppSettings) {
        this.AppSettings=AppSettings;
    }
    public Boolean getAppSettings(){
        return  this.AppSettings;
    }

    public void setCarSettings(Boolean CarSettings) {
        this.CarSettings=CarSettings;
    }
    public Boolean getCarSettings(){
        return  this.CarSettings;
    }
    public void SetCarStatus(Boolean CarStatus) { this.CarStatus=CarStatus; };
    public Boolean GetCarStatus(){
        return  CarStatus;
    }
    public void SetAlarmType(int AlarmType) { this.AlarmType=AlarmType; };
    public int GetAlarmType(){
        return  this.AlarmType;
    }

    public void setlocation(double latitude,double longitude) {
        this.latitude=latitude;
        this.longitude=longitude;
    }
    //实现getname()方法，获取变量的值
    public double get_latitude(){
        return  latitude;
    }
    public double get_longitude(){
        return  longitude;
    }

    public void setPressureStatus(Boolean PressureStatus){ this.PressureStatus=PressureStatus;};
    public void setAccelerationStatus(Boolean AccelerationStatus){this.AccelerationStatus=AccelerationStatus;};
    public void setGyroStatus(Boolean GyroStatus){this.GyroStatus=GyroStatus;};
    public void setGPSStatus(Boolean GPSStatus){this.GPSStatus=GPSStatus;};
    public Boolean GetPressureStatus(){ return PressureStatus;};
    public Boolean GetAccelerationStatus(){ return AccelerationStatus;};
    public Boolean GetGyroStatus(){ return GyroStatus;};
    public Boolean GetGPSStatus(){ return GPSStatus;};


    public void setPressureValue(String PressureValue){this.PressureValue=PressureValue;};
    public void setAccelerationValue(String AccelerationValue){this.AccelerationValue=AccelerationValue;};
    public void setGyroValue(String GyroValue){this.GyroValue=GyroValue;};
    public void setGPSValue(String GPSValue){this.GPSValue=GPSValue;};

    public String GetPressureValue(){ return PressureValue;};
    public String GetAccelerationValue(){ return AccelerationValue;};
    public String GetGyroValue(){ return GyroValue;};
    public String GetGPSValue(){ return GPSValue;};


}
