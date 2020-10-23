package com.example.myinputrmb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioGroup;

public class Fragment2 extends FragmentActivity {

    private Fragment myFragments[];
    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    protected void onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        setContentView(R.layout.fragment_2);
        //配置fragment
        myFragments = new Fragment[3];
        myFragments[0] = fragmentManager.findFragmentById(R.id.fragment1);
        myFragments[1] = fragmentManager.findFragmentById(R.id.fragment2);
        myFragments[2] = fragmentManager.findFragmentById(R.id.fragment3);
        fragmentTransaction = fragmentManager.beginTransaction().hide(myFragments[0]).hide(myFragments[1]).hide(myFragments[2]);
        fragmentTransaction.show(myFragments[1]).commit();

    }
}