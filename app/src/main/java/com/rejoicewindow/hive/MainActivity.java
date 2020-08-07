package com.rejoicewindow.hive;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONObject;
import com.rejoicewindow.hive.ui.Fragment_one;
import com.rejoicewindow.hive.ui.Fragment_three;
import com.rejoicewindow.hive.ui.Fragment_two;
import com.rejoicewindow.hive.ui.MyFragmentPagerAdapter;
import com.rejoicewindow.hive.util.Constant;
import com.rejoicewindow.hive.util.PollingUtil;
import com.rejoicewindow.hive.util.TaskCenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    // 底部标签的文本
    private ImageButton tab_1, tab_2, tab_3;
    //设置被Fragment填充的ViewPager控件及适配器
    private ViewPager myViewPager;
    private List<Fragment> list;
    private MyFragmentPagerAdapter adapter;

    private TextView TopTab;

    Myapplication myapplication;//声明一个对象

    public TextView textView_Text;
    private LinearLayout TCPConnect;

    public EditText Tcp_IPV4;
    public EditText Tcp_Port;
    private String AlarmMsg;

    private MediaPlayer mediaPlayer = new MediaPlayer();

    private Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initTab();
        MyMqttService.startService(this); //开启服务
        myapplication = (Myapplication) getApplication();
        //每3秒打印一次日志
        PollingUtil pollingUtil = new PollingUtil(new Handler(this.getMainLooper()));
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                trace();
                AlarmUpdate();
//                Log.e("A", "----------handler 定时。询任务----------");
            }
        };
        pollingUtil.startPolling(runnable, 2000, true);

        TCPConnect.setVisibility(View.GONE);
    }
    /**
     * 初始化UI
     */
    private void initUI() {
        tab_1 =  findViewById(R.id.btn_tab1);
        tab_2 =  findViewById(R.id.btn_tab2);
        tab_3 =  findViewById(R.id.btn_tab3);
        TopTab=findViewById(R.id.top_tab);
        TCPConnect=findViewById(R.id.tcp_connect);
        textView_Text = findViewById(R.id.receive_text);
//        SharedPreferences settings = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString("name","名字");
//        editor.putString("nickname","昵称");
//        editor.putString("phone","12345678900");
//        editor.putString("email","sda@fjkfj.cc");
//        editor.putBoolean("sex",true);
//        editor.apply();
        myViewPager = (ViewPager) findViewById(R.id.viewpager);
        tab_1.setOnClickListener(this);
        tab_2.setOnClickListener(this);
        tab_3.setOnClickListener(this);
        myViewPager.addOnPageChangeListener(new MyPagerChangeListener());

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else{
            initMediaPlayer();  //初始化MediaPlayer
        }
    }
    /**
     * 初始化第一个Fragment页面及底部标签
     */
    private void initTab() {
        list = new ArrayList<>();
        list.add(new Fragment_one());
        list.add(new Fragment_two());
        list.add(new Fragment_three());
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), list);
        myViewPager.setAdapter(adapter);
        myViewPager.setCurrentItem(0);  //初始化显示第一个页面
        tab_1.setBackgroundColor(Color.BLUE);//被选中就为蓝色
    }

    void  AlarmUpdate(){
        if(0<myapplication.GetAlarmType()&&myapplication.GetAlarmType()<5){
            SharedPreferences settings = getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
            System.out.println(settings.getBoolean("ToastAlert", false));
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
                AlarmMsg="警报类型："+Constant.AlarmType[myapplication.GetAlarmType()]+"\n      第"+myapplication.getHeivPeer()+"个被拿走 \n确认是否为本人操作？";
                }else {
                AlarmMsg="警报类型："+Constant.AlarmType[myapplication.GetAlarmType()]+"\n确认是否为本人操作？";
            }
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("来自峰箱的警报")
                    .setMessage(AlarmMsg)
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AlertDialog alertDialog_1 = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("提示")
                                    .setMessage("是否开启非保护模式（在您操作过程将不在收到警报信息）？")
                                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            MyMqttService.publish(Constant.SureYes);
                                            if(mediaPlayer.isPlaying()){
                                                mediaPlayer.reset(); //停止播放
                                                initMediaPlayer();
                                            }
                                            SharedPreferences settings = getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
                                            if( settings.getBoolean("VibrationAlert", false)) {
                                                vibrator.cancel();
                                            }
                                            Toast.makeText(getBaseContext(), "已开启\n 需要手动开启保护模式", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            MyMqttService.publish(Constant.SureNo);
                                            if(mediaPlayer.isPlaying()){
                                                mediaPlayer.reset(); //停止播放
                                                initMediaPlayer();
                                            }
                                            SharedPreferences settings = getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
                                            if( settings.getBoolean("VibrationAlert", false)) {
                                                vibrator.cancel();
                                            }
                                            Toast.makeText(MainActivity.this, "报警已经结束，请前往处理", Toast.LENGTH_SHORT).show();
                                        }
                                    }).create();
                            alertDialog_1.show();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MyMqttService.publish(Constant.SureNo);
                            if(mediaPlayer.isPlaying()){
                                mediaPlayer.reset(); //停止播放
                                initMediaPlayer();
                            }
                            SharedPreferences settings = getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
                            if( settings.getBoolean("VibrationAlert", false)) {
                                vibrator.cancel();
                            }
                            Toast.makeText(getBaseContext(), "报警已经结束，请前往处理", Toast.LENGTH_SHORT).show();
                        }
                    }).create();
            alertDialog.show();
            myapplication.SetAlarmType(0);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_tab1: // 第一个Page
                myViewPager.setCurrentItem(0);
                tab_1.setBackgroundColor(Color.BLUE);
                tab_2.setBackgroundColor(Color.WHITE);
                tab_3.setBackgroundColor(Color.WHITE);
                TopTab.setText(R.string.title_oneFragment);
                break;
            case R.id.btn_tab2: // 第二个Page
                myViewPager.setCurrentItem(1);
                tab_1.setBackgroundColor(Color.WHITE);
                tab_2.setBackgroundColor(Color.BLUE);
                tab_3.setBackgroundColor(Color.WHITE);
                TopTab.setText(R.string.title_twoFragment);
                break;
            case R.id.btn_tab3: // 第三个Page
                myViewPager.setCurrentItem(2);
                tab_1.setBackgroundColor(Color.WHITE);
                tab_2.setBackgroundColor(Color.WHITE);
                tab_3.setBackgroundColor(Color.BLUE);
                TopTab.setText(R.string.title_thereFragment);
                break;
            default:
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch(requestCode){
            case 1:
                if(grantResults.length>=0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
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

    public void connect(View view) {
        Tcp_IPV4=findViewById(R.id.tcp_ipv4);
        Tcp_Port=findViewById(R.id.tcp_port);
        TaskCenter.sharedCenter().connect(Tcp_IPV4.getText().toString(), Integer.parseInt(Tcp_Port.getText().toString()));
        Button Connect=findViewById(R.id.connect);
        Connect.setEnabled(false);
    }

    public void sendMessage(String Msg) {
        TaskCenter.sharedCenter().send(Msg.getBytes());
    }

    public void hide_tcp(View view) {
//        TaskCenter.sharedCenter().disconnect();
//        Button Connect=findViewById(R.id.connect);
//        Connect.setEnabled(true);
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        TCPConnect.setVisibility(View.GONE);
    }

    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    tab_1.setBackgroundColor(Color.RED);
                    tab_2.setBackgroundColor(Color.WHITE);
                    tab_3.setBackgroundColor(Color.WHITE);
                    TopTab.setText(R.string.title_oneFragment);
                    break;
                case 1:
                    tab_1.setBackgroundColor(Color.WHITE);
                    tab_2.setBackgroundColor(Color.RED);
                    tab_3.setBackgroundColor(Color.WHITE);
                    TopTab.setText(R.string.title_twoFragment);
                    break;
                case 2:
                    tab_1.setBackgroundColor(Color.WHITE);
                    tab_2.setBackgroundColor(Color.WHITE);
                    tab_3.setBackgroundColor(Color.RED);
                    TopTab.setText(R.string.title_thereFragment);
                    break;
            }
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

    /*在程序关闭时，停止音乐播放并清空资源*/
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
