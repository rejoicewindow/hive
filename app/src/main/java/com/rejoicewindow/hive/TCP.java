package com.rejoicewindow.hive;

import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.rejoicewindow.hive.util.TaskCenter;

import java.io.IOException;

public class TCP extends AppCompatActivity {


    public EditText editText;
    public TextView textView_send;
    public TextView textView_receive;

    public EditText Tcp_IPV4;
    public EditText Tcp_Port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp);

//        editText = findViewById(R.id.send_editText);
//        textView_send = findViewById(R.id.send_textView);
        textView_receive = findViewById(R.id.receive_textView);
//        textView_send.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView_receive.setMovementMethod(ScrollingMovementMethod.getInstance());
        TaskCenter.sharedCenter().setDisconnectedCallback(new TaskCenter.OnServerDisconnectedCallbackBlock() {
            @Override
            public void callback(IOException e) {
                textView_receive.setText(textView_receive.getText().toString() + "断开连接" + "\n");
            }
        });
        TaskCenter.sharedCenter().setConnectedCallback(new TaskCenter.OnServerConnectedCallbackBlock() {
            @Override
            public void callback() {
                textView_receive.setText(textView_receive.getText().toString() + "连接成功" + "\n");
            }
        });
        TaskCenter.sharedCenter().setReceivedCallback(new TaskCenter.OnReceiveCallbackBlock() {
            @Override
            public void callback(String receicedMessage) {
                textView_receive.setText(textView_receive.getText().toString() + receicedMessage + "\n");
            }
        });
    }


    public void sendMessage(View view) {
        String msg = editText.getText().toString();
        textView_send.setText(textView_send.getText().toString() + msg + "\n");
        TaskCenter.sharedCenter().send(msg.getBytes());
    }

    public void connect(View view) {
        Tcp_IPV4=findViewById(R.id.tcp_ipv4);
        Tcp_Port=findViewById(R.id.tcp_port);
        TaskCenter.sharedCenter().connect(Tcp_IPV4.getText().toString(), Integer.parseInt(Tcp_Port.getText().toString()));
        Button Connect=findViewById(R.id.connect);
        Connect.setEnabled(false);
    }

    public void disconnect(View view) {
        TaskCenter.sharedCenter().disconnect();
        Button Connect=findViewById(R.id.connect);
        Connect.setEnabled(true);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void clear1(View view) {
        textView_send.setText("");
    }

    public void clear2(View view) {
        textView_receive.setText("");
    }
}
