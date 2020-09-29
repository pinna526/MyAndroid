package com.example.myinputrmb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class InputRMB_A extends AppCompatActivity implements Runnable{
    private static final String TAG = "InputRMB_A";
    double dollarToRMB;
    double euroToRMB;
    double wonToRMB;

    TextView input,text;
    Button dollar,euro,won,save;
    double num;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inputrmb_a_activity_main);

        input = findViewById(R.id.input);
        text = findViewById(R.id.text);
        dollar = findViewById(R.id.dollar);
        euro = findViewById(R.id.euro);
        won = findViewById(R.id.won);
        save = findViewById(R.id.open);

        dollarToRMB = 6.83;
        euroToRMB = 7.97;
        wonToRMB = 0.0058;

        num = 0;
        text.setText(String.valueOf(num));

        Thread t = new Thread(this);
        t.start();

        //获取线程的消息
        handler = new Handler(){
            public void handleMessage(Message msg){
                if(msg.what==2){
                    String str = (String)msg.obj;
                    Log.i(TAG, "handleMessage: "+str);
                }
                super.handleMessage(msg);

            }
        };

    }

    public void convert(View btn){
        if(input.getText().toString().isEmpty()){
            Toast.makeText(this, "pls input a number", Toast.LENGTH_SHORT).show();
        }else{
            double rmb = Double.parseDouble(input.getText().toString());
            switch (btn.getId()){
                case R.id.dollar:
                    num = rmb*dollarToRMB;
                    break;
                case R.id.euro:
                    num = rmb*euroToRMB;
                    break;
                case R.id.won:
                    num = rmb*wonToRMB;
                    break;
            }
            text.setText(String.valueOf(num));

        }

    }

    private void getNetSource(){
        URL url = null;
        try {
            url = new URL("https://www.swufe.edu.cn/");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            InputStream inputStream = http.getInputStream();

            String html = inputStream2String(inputStream);
            Log.i(TAG, "getNetSource: html"+html);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"gb2312");
        while (true){
            int size = in.read(buffer,0,buffer.length);
            if(size<0)
                break;
            out.append(buffer,0,size);
        }

        return out.toString();
    }

    public void open(View btn){
        Intent newA = new Intent(this, InputRMB_B.class);
        Bundle bdl = new Bundle();
        bdl.putDouble("dollar_rate_key",dollarToRMB);
        bdl.putDouble("euro_rate_key",euroToRMB);
        bdl.putDouble("won_rate_key",wonToRMB);
        newA.putExtras(bdl);
        startActivityForResult(newA,526);

    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        if(requestCode==526&&resultCode==520){
            Bundle bdl = data.getExtras();
            dollarToRMB = bdl.getDouble("dollar_rate_key",0.0d);
            euroToRMB = bdl.getDouble("euro_rate_key",0.0d);
            wonToRMB = bdl.getDouble("won_rate_key",0.0d);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void run() {
        getNetSource();
        Log.i(TAG, "run: ");
        Message msg = handler.obtainMessage(2);
        msg.obj = "hello from another thread";
        handler.sendMessage(msg);
    }



}

