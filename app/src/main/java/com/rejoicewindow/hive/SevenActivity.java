package com.rejoicewindow.hive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SevenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seven);
        Button but =findViewById(R.id.hive_confirm);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText HiveNum=findViewById(R.id.hive_num);
                EditText HiveToken=findViewById(R.id.hive_token);
                EditText HiveRemarks=findViewById(R.id.hive_remarks);
                TextView HiveInfo=findViewById(R.id.hive_info);
                Toast.makeText(getApplicationContext(),"添加成功",Toast.LENGTH_SHORT).show();
                HiveInfo.setText("编号："+HiveNum.getText().toString()+"\n密钥：******"+"\n备注："+HiveRemarks.getText().toString()+"\n");
            }
        });
    }
}
