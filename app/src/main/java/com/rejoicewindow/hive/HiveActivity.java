package com.rejoicewindow.hive;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rejoicewindow.hive.util.LoadingActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HiveActivity extends AppCompatActivity {

    private SimpleAdapter adapter;
    private Button InitModel;
    Myapplication myapplication;//声明一个对象
    private Dialog mDialog;
    private Spinner CarNumber;
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter02;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hive);
        InitModel=findViewById(R.id.init_model);
        CarNumber = findViewById(R.id.car_number);
        myapplication = (Myapplication) getApplication();

//        InitModel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDialog = LoadingActivity.createLoadingDialog(CarActivity.this, "请确认位置是否正确……然后直接返回");
////                Toast.makeText(SettingsActivity.this, "请查看地图所示位置是否正确……然后直接返回", Toast.LENGTH_SHORT).show();
//                mHandler.sendEmptyMessageDelayed(1, 5000);
//                myapplication.setInitModule(true);
//                Intent i = new Intent(CarActivity.this, AmapActivity.class);
//                startActivity(i);
//            }
//        });

        String[] ls = {"100001", "100002", "100003"};
        list.clear();
        //获取XML中定义的数组
        for (int i = 0; i < ls.length; i++) {
            list.add(ls[i]);
        }
        //把数组导入到ArrayList中
        adapter02 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        //设置下拉菜单的风格
        adapter02.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定适配器
        CarNumber.setAdapter(adapter02);
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

        //图片
        int[] images = {R.drawable.hive_false, };
        //内容
        String[] names = {"编号：100001", };
        // this = your fragment
        SharedPreferences settings = this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String sex = "";
        if (settings.getBoolean("sex", true)) {
            sex = "男";
        } else {
            sex = "女";
        }
        String[] things = {"未初始化", "请点击上面按钮进行初始化"};
        //参数一：上下文对象  参数二：数据源List<Map<String,Object>> 参数三：item对应的布局文件
        //参数四：表示由map中定义的key组成的字符串类型的数字  参数五：需要显示的控件id组成的的数组
        //保证参数四和参数五一一对应，否则控件属性会对换（张冠李戴）
        adapter = new SimpleAdapter(this, getData(images, names, things),
                R.layout.three_item, new String[]{"img", "key", "value"},
                new int[]{R.id.image, R.id.three_key, R.id.three_value});

        ListView listView = (ListView) findViewById(R.id.car_list);
        listView.setAdapter(adapter);
    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(myapplication.getInitModule()){
//            AlertDialog alertDialog = new AlertDialog.Builder(CarActivity.this)
//                    .setTitle("初始化模块")
//                    .setMessage("地图所示位置是否正确？")
//                    .setPositiveButton("正确", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
////                            MyMqttService.publish(Helper.CarInitsuccess);
//                            Toast.makeText(CarActivity.this, "模块初始化成功，可放心使用", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .setNegativeButton("不正确", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Toast.makeText(CarActivity.this, "模块初始化失败，请确认原因", Toast.LENGTH_SHORT).show();
//                        }
//                    }).create();
//            alertDialog.show();
//        }
//        myapplication.setInitModule(false);
//    }

    //对数据进行加载
    private List<? extends Map<String, ?>> getData(int[] images, String[] key, String[] value) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < images.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", images[i]);
            map.put("key", key[i]);
            map.put("value", value[i]);
            list.add(map);
        }
        return list;
    }
}
