package com.rejoicewindow.hive;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rejoicewindow.hive.util.Constant;
import com.rejoicewindow.hive.util.DbHelper;
import com.rejoicewindow.hive.util.PollingUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreeActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Vibrator vibrator;
    private String AlarmMsg;
    Myapplication myapplication;//声明一个对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        myapplication = (Myapplication) getApplication();
        LinearLayout AddHive=findViewById(R.id.hive_add);
        AddHive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ThreeActivity.this, SevenActivity.class);
                startActivity(i);
            }
        });
    }
}
