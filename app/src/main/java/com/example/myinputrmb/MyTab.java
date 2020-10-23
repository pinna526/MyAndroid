package com.example.myinputrmb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MyTab extends FragmentActivity {

    private Fragment myFragments[];
    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RadioButton button1,button2,button3;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tab);

        //配置button
        button1 = (RadioButton)findViewById(R.id.button1);
        button2 = (RadioButton)findViewById(R.id.button2);
        button3 = (RadioButton)findViewById(R.id.button3);
        radioGroup = (RadioGroup)findViewById(R.id.buttonGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fragmentTransaction = fragmentManager.beginTransaction().hide(myFragments[0]).hide(myFragments[1]).hide(myFragments[2]);
                switch (checkedId){
                    case R.id.button1:
                        fragmentTransaction.show(myFragments[0]).commit();
                    break;
                    case R.id.button2:
                        fragmentTransaction.show(myFragments[1]).commit();
                        break;
                    case R.id.button3:
                        fragmentTransaction.show(myFragments[2]).commit();
                        break;
                    default:
                        break;
                }
            }
        });



    }
}