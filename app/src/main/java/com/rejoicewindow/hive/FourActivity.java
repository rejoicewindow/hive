package com.rejoicewindow.hive;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.rejoicewindow.hive.util.Constant;
import com.rejoicewindow.hive.util.DbHelper;
import com.rejoicewindow.hive.util.PollingUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FourActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Vibrator vibrator;
    private String AlarmMsg;
    Myapplication myapplication;//声明一个对象
    private ImageView DumpImg;
    private Boolean ImgStatus = false;
    private ImageView StealImg;
    private Spinner HiveNumberDump;
    private Spinner RecordDump;
    private Spinner HiveNumberSteal;
    private Spinner RecordSteal;
    private String[] HiveNumberList = {"hive0001"};
    private String[] RecordList = {"2020年06月13日 18:45","2020年06月13日 18:56","2020年06月13日 19:05",};
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> r_list = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> r_adapter;
    ArrayList<String> D_list = new ArrayList<String>();
    ArrayAdapter<String> D_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);
        DumpImg = findViewById(R.id.img_dump);
        StealImg = findViewById(R.id.img_steal);
        HiveNumberDump = (Spinner) findViewById(R.id.hive_number_dump);
        RecordDump = (Spinner) findViewById(R.id.record_dump);
        HiveNumberSteal = (Spinner) findViewById(R.id.hive_number_steal);
        RecordSteal = (Spinner) findViewById(R.id.record_steal);


        myapplication = (Myapplication) getApplication();

        DrawUI();
//
////        每3秒打印一次日志
//        PollingUtil pollingUtil = new PollingUtil(new Handler(this.getMainLooper()));
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                trace();
//
////                Log.e("A", "----------handler 定时。询任务----------");
//            }
//        };
//        pollingUtil.startPolling(runnable, 2000, true);

        //Spinner加载监听事件
        RecordDump.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(FourActivity.this,String.valueOf(parent.getSelectedItem()),Toast.LENGTH_SHORT).show();
                Manage(String.valueOf(parent.getSelectedItem()), 0);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Manage(String.valueOf(parent.getSelectedItem()), 0);
            }
        });
        //Spinner加载监听事件
        RecordSteal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(FourActivity.this,String.valueOf(parent.getSelectedItem()),Toast.LENGTH_SHORT).show();
                Manage(String.valueOf(parent.getSelectedItem()), 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Manage(String.valueOf(parent.getSelectedItem()), 1);
            }
        });
    }

    private void Manage(String time, int location) {

        SQLiteDatabase db;
        DbHelper dbHelper = new DbHelper(this);
        db = dbHelper.getReadableDatabase();
        Cursor cursor;
        switch (location) {
            case 0:
                cursor = db.query("rdump", new String[]{"number,time"}, "time=?", new String[]{"time"}, null, null, null);
                if (cursor.getCount() == 0) {
//            Toast.makeText(getContext(), "没有数据", Toast.LENGTH_SHORT).show();
                } else {
                    cursor.moveToFirst();
                    HiveNumberList = insert(HiveNumberList, cursor.getString(cursor.getColumnIndex("number")));
                }
                cursor.close();


                list.clear();
                if (HiveNumberList != null) {
                    //获取XML中定义的数组
                    for (int i = 0; i < HiveNumberList.length; i++) {
                        list.add(HiveNumberList[i]);
                    }
                }

                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
                //设置下拉菜单的风格
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //绑定适配器
                HiveNumberSteal.setAdapter(adapter);
//        HiveNumberSteal.setPrompt("请选择你要查看的蜂箱");

                break;
            case 1:
                cursor = db.query("rsteal", new String[]{"number,time"}, "time=?", new String[]{time}, null, null, null);
                if (cursor.getCount() == 0) {
//            Toast.makeText(getContext(), "没有数据", Toast.LENGTH_SHORT).show();
                } else {
                    cursor.moveToFirst();
                    HiveNumberList = insert(HiveNumberList, cursor.getString(cursor.getColumnIndex("number")));
                }
                cursor.close();

                D_list.clear();
                if (HiveNumberList != null) {
                    //获取XML中定义的数组
                    for (int i = 0; i < HiveNumberList.length; i++) {
                        D_list.add(HiveNumberList[i]);

                    }
                }

                D_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, D_list);

                D_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //绑定适配器
                HiveNumberDump.setAdapter(D_adapter);
                //设置对话框标题栏
//        HiveNumberDump.setPrompt("请选择你要查看的蜂箱");
                //把数组导入到ArrayList中
                break;
        }
        db.close();
    }

    //往字符串数组追加新数据
    private static String[] insert(String[] arr, String str) {
        int size=0;
        if(arr==null){
            size=0;
        }else {
            size= arr.length;  //获取数组长度
        }
        String[] tmp = new String[size + 1];  //新建临时字符串数组，在原来基础上长度加一
        for (int i = 0; i < size; i++) {  //先遍历将原来的字符串数组数据添加到临时字符串数组
            tmp[i] = arr[i];
        }
        tmp[size] = str;  //在最后添加上需要追加的数据
        return tmp;  //返回拼接完成的字符串数组
    }

    private void DrawUI() {

        SQLiteDatabase db;
        DbHelper dbHelper = new DbHelper(this);
        db = dbHelper.getReadableDatabase();
        Cursor cursor;
        cursor = db.query("rsteal", new String[]{"number,time,is_1"}, "is_1=?", new String[]{"1"}, null, null, null);
        if (cursor.getCount() == 0) {
//            Toast.makeText(getContext(), "没有数据", Toast.LENGTH_SHORT).show();
        } else {
            cursor.moveToFirst();
            RecordList = insert(RecordList, cursor.getString(cursor.getColumnIndex("time")));
            System.out.println(cursor.getString(cursor.getColumnIndex("time")));
        }
        cursor.close();
        r_list.clear();
        if (RecordList != null) {
            //获取XML中定义的数组
            for (int i = 0; i < RecordList.length; i++) {
                r_list.add(RecordList[i]);
            }
        }

        //把数组导入到ArrayList中
        r_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, r_list);
        //设置下拉菜单的风格
        r_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //设置对话框标题栏
        RecordDump.setPrompt("选择报警时间");
        //绑定适配器
        RecordDump.setAdapter(r_adapter);

        RecordList = insert(RecordList, "2020年06月13日 19:25");

        r_list.clear();
        if (RecordList != null) {
            //获取XML中定义的数组
            for (int i = 0; i < RecordList.length; i++) {
                r_list.add(RecordList[i]);
            }
        }

        //把数组导入到ArrayList中
        r_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, r_list);
        //设置下拉菜单的风格
        r_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //设置对话框标题栏
        RecordSteal.setPrompt("选择报警时间");
        //绑定适配器
        RecordSteal.setAdapter(r_adapter);
    }


}
