package com.example.myinputrmb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class TestList extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);

        listView = findViewById(R.id.listView);

        List<String> mylist = new ArrayList<>();
        for(int i=0;i<50;i++){
            mylist.add("item "+i);
        }

        ListAdapter ad = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mylist);
        listView.setAdapter(ad);
    }
}