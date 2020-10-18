package com.example.myinputrmb;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;


public class InputRMB_A extends AppCompatActivity implements Runnable{
    private static final String TAG = "InputRMB_A";
    double dollarToRMB;
    double euroToRMB;
    double wonToRMB;

    TextView input,text;
    Button dollar,euro,won,save;
    double num;
    Handler handler;
    ListView listView;
    Map<String,Float> mp = new HashMap<>();
    ArrayList<Map<String, String>> list;

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
        listView = findViewById(R.id.listView);

//        dollarToRMB = 6.83;
//        euroToRMB = 7.97;
//        wonToRMB = 0.0058;

        num = 0;
        text.setText(String.valueOf(num));

        Thread t = new Thread(this);
        t.start();

        //获取线程的消息

        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 2) {
                    mp = (Map<String, Float>) msg.obj;
                }

                list = new ArrayList<Map<String, String>>();
                for (Map.Entry<String, Float> entry : mp.entrySet()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("name", entry.getKey());
                    map.put("rate", String.valueOf(entry.getValue()));
                    list.add(map);
//                    list.add(entry.getKey() + " 今日的汇率是：" + entry.getValue());
                }

                //设置列表数据
                SimpleAdapter listItemAdapter = new SimpleAdapter(InputRMB_A.this,
                        list,
                        R.layout.my_listview,
                        new String[]{"name", "rate"},
                        new int[]{R.id.name, R.id.rate}
                );
                listView.setAdapter(listItemAdapter);

//                ListAdapter ad = new ArrayAdapter<>(InputRMB_A.this,
//                        android.R.layout.simple_list_item_1,list);
//                listView.setAdapter(ad);

                //存储特定的几个汇率
                Float dollarRate = mp.get("美元");
                Float euroRate = mp.get("欧元");
                Float wonRate = mp.get("韩元");
                SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putFloat("dollar_rate", dollarRate);
                editor.putFloat("euro_rate", euroRate);
                editor.putFloat("won_rate", wonRate);
                editor.apply();

                super.handleMessage(msg);
            }
        };

        //获取存储的汇率
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        PreferenceManager.getDefaultSharedPreferences(this);
        dollarToRMB = sharedPreferences.getFloat("dollar_rate", 683f);
        euroToRMB = sharedPreferences.getFloat("euro_rate", 797f);
        wonToRMB = sharedPreferences.getFloat("won_rate", 0.58f);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //输出一下熟悉参数
                Log.i(TAG, "onItemClick: parent:"+parent);
                Log.i(TAG, "onItemClick: view:"+view);
                Log.i(TAG, "onItemClick: position"+position);
                Log.i(TAG, "onItemClick: id"+id);

                //指定输出
                HashMap<String,String> map = (HashMap<String,String>)listView.getItemAtPosition(position);
                Log.i(TAG, "onItemClick: name:"+map.get("name"));
                Log.i(TAG, "onItemClick: rate:"+map.get("rate"));

                Intent inputItem = new Intent(InputRMB_A.this, inputItem.class);
                inputItem.putExtra("name",map.get("name"));
                inputItem.putExtra("rate",map.get("rate"));
                startActivity(inputItem, ActivityOptions.makeSceneTransitionAnimation(InputRMB_A.this).toBundle());

            }
        });


    }

    public void convert(View btn){
        if(input.getText().toString().isEmpty()){
            Toast.makeText(this, "pls input a number", Toast.LENGTH_SHORT).show();
        }else{
            double rmb = Double.parseDouble(input.getText().toString());
            switch (btn.getId()){
                case R.id.dollar:
                    num = rmb/dollarToRMB;
                    break;
                case R.id.euro:
                    num = rmb/euroToRMB;
                    break;
                case R.id.won:
                    num = rmb/wonToRMB;
                    break;
            }
            text.setText(String.valueOf(num));

        }

    }

    private Map<String,Float> getNetSource(){
        //用jsoup从网络获取汇率信息
        String url = null;
        Map<String, Float> map = new HashMap <>();
        try {
            url = "http://www.usd-cny.com/bankofchina.htm";
            Document doc = Jsoup.connect(url).get();
            Log.i(TAG,"jsoup.url:"+doc.title());
            Elements tables = doc.getElementsByTag("table");

            Elements tds = tables.select("td");
            for(int i=0;i<tds.size();i+=6){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);

                String str1 = td1.text();
                String str2 = td2.text();
                //除以100是因为网站上的汇率是每100元人民币
                map.put(str1,Float.parseFloat(str2)/100);
//                Log.i(TAG, "getNet:"+str1+"  "+str2);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
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
        //获取当前时间和data中上次打开的时间，对比是否超过一天
        Date now = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String now_string = format.format(now);

        //获取上一次登录时间
        SharedPreferences sharedPreferences = getSharedPreferences(
                "mytime", Activity.MODE_PRIVATE);
        PreferenceManager.getDefaultSharedPreferences(this);
        String last_string = sharedPreferences.getString(
                "login_time", null);

        System.out.println(now_string);
        System.out.println(last_string);


        //若为第一次登录或查询为null的话，将当前日期写入配置文件中
        if(last_string==null){
            SharedPreferences sp = getSharedPreferences(
                    "mytime",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("login_time",now_string);
            editor.apply();
            System.out.println("日期为空");
        }else {
            //先将字符串格式转换成日期
            try {
                Date n = format.parse(now_string);
                Date l = format.parse(last_string);
                long now_date = n.getTime();
                long last_date = l.getTime();
//                int days = (int) ((now_date- last_date) / (1000 * 60 * 60 * 24));
                int days = (int) ((now_date- last_date) / (1000 * 60));
                System.out.println("两个时间之间的天数差为：" + days);

                //若登录时间相隔超过一天，则重新从网络中获取资源
                if(days>=1){
                    Map<String,Float> mp = getNetSource();
                    Message msg = handler.obtainMessage(2);
                    msg.obj = mp;
                    handler.sendMessage(msg);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

}

