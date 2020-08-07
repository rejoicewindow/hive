package com.rejoicewindow.hive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rejoicewindow.hive.util.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegesterActivity extends AppCompatActivity {
    private String sex = "";
    private int u_id ;
    private String result="0";
    private int code=0;

    private TextView reView;
    private EditText et_nickname ;
    private EditText et_name ;
    private EditText etPhone;
    private EditText etPassword;
    private RadioButton rbtnStdMan ;
    private RadioButton rbtnStdWomen ;

    public   String CLIENTID= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regester);
        reView= findViewById(R.id.returnView);
        et_nickname = findViewById(R.id.et_nickname);
        et_name = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_reg_password);
        rbtnStdMan = (RadioButton) findViewById(R.id.et_sex1);
        rbtnStdWomen = (RadioButton) findViewById(R.id.et_sex0);
    }


    public void btn_submit_register_onclick(View view) {
        reView= (TextView)findViewById(R.id.returnView);
        et_nickname = (EditText)findViewById(R.id.et_nickname);
        et_name = (EditText)findViewById(R.id.et_name);
        etPhone = (EditText)findViewById(R.id.et_phone);
        etPassword = (EditText)findViewById(R.id.et_reg_password);
        rbtnStdMan = (RadioButton) findViewById(R.id.et_sex1);
        rbtnStdWomen = (RadioButton) findViewById(R.id.et_sex0);

        if (rbtnStdMan.isChecked()) {
            sex = "1";
        } else if (rbtnStdWomen.isChecked()) {
            sex = "0";
        }
        CLIENTID=etPhone.getText().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Helper.RegisterUrl;

        Map<String, String> merchant = new HashMap<String, String>();
        merchant.put("u_nickname", et_nickname.getText().toString());
        merchant.put("u_phone", etPhone.getText().toString());
        merchant.put("u_password", etPassword.getText().toString());
        merchant.put("u_appid",CLIENTID);
        merchant.put("u_name",et_name.getText().toString());
//        Toast.makeText(getApplicationContext(),CLIENTID, Toast.LENGTH_LONG).show();
        merchant.put("u_sex",sex);
        JSONObject jsonObject = new JSONObject(merchant);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String token = null;
                try {
                    u_id = response.getInt("id");
                    String u_addtime=response.getString("u_addtime");
                    SharedPreferences settings = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("u_phone", etPhone.getText().toString());
                    editor.putString("u_password", etPassword.getText().toString());
                    editor.putInt("u_id", u_id);
                    editor.putString("u_token", token);
                    editor.putString("u_name",et_name.getText().toString());
                    editor.putString("u_nickname",  et_nickname.getText().toString());
                    editor.putString("u_appid",CLIENTID);
                    editor.putString("u_email",token);
                    editor.putBoolean("u_isactive",false);
                    editor.putBoolean("u_super",false);
                    editor.putString("u_addtime",token);
                    editor.putString("u_icon",token);
                    editor.apply();
                    result="注册成功! \n"+"ID："+u_id+"\n"+"昵称："+et_nickname.getText().toString()+"\n"+"姓名:"+et_name.getText().toString()+"\n"+"性别："+sex+"\n"+"手机号："+etPhone.getText().toString()+"\n"+"加入时间:"+u_addtime+"\n";
                    reView.setText(result);
                    code=1;
//                    reView.setText(response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                result="注册失败： \n"+"姓名："+et_name.getText().toString()+"\n"+"昵称："+et_nickname.getText().toString()+"\n"+"性别："+sex+"\n"+"手机号："+etPhone.getText().toString()+"\n";
                reView.setText(result);
//                reView.setText("请求失败" + volleyError);
                code=0;
            }
        });

        // 3 将创建的请求添加到请求队列中
        requestQueue.add(jsonObjectRequest);
    }

    public void re_Back(View view) {
        Intent intent = new Intent();
        etPhone = (EditText)findViewById(R.id.et_phone);
        etPassword = (EditText)findViewById(R.id.et_reg_password);
        intent.putExtra("phone",etPhone.getText().toString());
        intent.putExtra("password", etPassword.getText().toString());
        setResult(code, intent);
        finish();
    }
}
