package com.rejoicewindow.hive;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.rejoicewindow.hive.util.Constant;
import com.rejoicewindow.hive.util.DbHelper;
import com.rejoicewindow.hive.util.Helper;
import com.rejoicewindow.hive.util.PollingUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OneActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Vibrator vibrator;
    private String AlarmMsg;
    Myapplication myapplication;//声明一个对象
    private boolean Temp=false;
    private TextView sel_hint;
    private EditText UserAccount;
    private EditText txtPassword;
    private TextView txtResult;

    private int id;
    private String token;

    private static final int PERMISSION_REQ_ID = 22;
    // App 运行时确认麦克风和摄像头设备的使用权限。
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        myapplication = (Myapplication) getApplication();
        //        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        sel_hint = findViewById(R.id.sel_hint);
        UserAccount = findViewById(R.id.account);
        txtPassword = findViewById(R.id.et_password);
        SharedPreferences settings = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        UserAccount.setText(settings.getString("u_phone", "").toString());
        txtPassword.setText(settings.getString("u_password", "").toString());
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });

        // 获取权限后，初始化 RtcEngine，并加入频道。
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
//            initEngineAndJoinChannel();
        }

    }
    public void btn_login(View view) {
//        RadioButton rbtnLoginByUserName = (RadioButton) findViewById(R.id.rbtnLoginByUserName);
        UserAccount = (EditText) findViewById(R.id.account);
        txtPassword = (EditText) findViewById(R.id.et_password);
        txtResult = (TextView) findViewById(R.id.sel_hint);
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Map<String, String> merchant = new HashMap<String, String>();
        merchant.put("u_phone", UserAccount.getText().toString());
        merchant.put("u_password", txtPassword.getText().toString());
        JSONObject jsonObject = new JSONObject(merchant);
//        System.out.println("---First-----------------------------------"+UserAccount.getText().toString()+txtPassword.getText().toString()+"----------------------------");
//        Toast.makeText(LoginActivity.this, "登录中，请稍等…………", Toast.LENGTH_SHORT).show();
        final String url = Helper.LoginUrl;
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    token = response.get("token").toString();
                    String msg = response.get("msg").toString();
                    String status = response.get("status").toString();
                    id = response.getInt("user_id");
                    if (status.equals("200")) {
                        txtResult.setText(msg);
                        SharedPreferences settings = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("u_phone", UserAccount.getText().toString());
                        editor.putString("u_password", txtPassword.getText().toString());
                        editor.putInt("u_id", id);
                        editor.putString("u_token", token);
                        editor.apply();
                        loginSuccess();
                    }
                    else {
                        txtResult.setText(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    txtResult.setText("登录失败");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtResult.setText("未连接到服务器");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        requestQueue.add(jsonRequest);

//        if (UserAccount.getText().toString().equals(DbAccount)) {
//            if (txtPassword.getText().toString().equals(DbPassword)) {
//                txtResult.setText("登录成功");
//                success = 1;
//                SharedPreferences settings = getSharedPreferences("UserInfo", Context.MODE_PRIVATE );
//                SharedPreferences.Editor editor = settings.edit();
//                editor.putString("account",UserAccount.getText().toString());
//                editor.putString("password",txtPassword.getText().toString());
//                editor.putString("token","");
//                editor.apply();
//            } else {
//                //密码错误
//                txtResult.setText("密码有误");
//            }
//        } else {
//            if (rbtnLoginByUserName.isChecked()) {
//                //用户名不存在
//                txtResult.setText("用户名不存在");
//            } else {
//                txtResult.setText("此邮箱未绑定账号");
//            }
//        }
    }


    void loginSuccess(){
        txtResult.setText("登录成功");
//        GetUserInfo();
        Intent i = new Intent(this, TwoActivity.class);
        startActivity(i);
    }

    public void click_regester(View view) {

        Intent intent = new Intent(this, RegesterActivity.class);
        startActivityForResult(intent, 1);
    }
    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }

        return true;
    }

}
