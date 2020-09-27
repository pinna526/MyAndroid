package com.example.myinputrmb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class InputRMB_B extends AppCompatActivity {

    double dollarToRMB;
    double euroToRMB;
    double wonToRMB;

    TextView dollar_rate,euro_rate,won_rate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inputrmb_b_activity_main);
        Intent newB = getIntent();
        Bundle bdl = newB.getExtras();
        dollarToRMB = bdl.getDouble("dollar_rate_key",0.0d);
        euroToRMB = bdl.getDouble("euro_rate_key",0.0d);
        wonToRMB = bdl.getDouble("won_rate_key",0.0d);

        dollar_rate = findViewById(R.id.dollar_rate);
        euro_rate = findViewById(R.id.euro_rate);
        won_rate = findViewById(R.id.won_rate);

        dollar_rate.setText(String.valueOf(dollarToRMB));
        euro_rate.setText(String.valueOf(euroToRMB));
        won_rate.setText(String.valueOf(wonToRMB));
    }

    public void backToA(View v){
        Intent newA = getIntent();
        Bundle bdl = new Bundle();
        
        dollarToRMB = Double.parseDouble(dollar_rate.getText().toString());
        euroToRMB = Double.parseDouble(euro_rate.getText().toString());
        wonToRMB = Double.parseDouble(won_rate.getText().toString());
        bdl.putDouble("dollar_rate_key",dollarToRMB);
        bdl.putDouble("euro_rate_key",euroToRMB);
        bdl.putDouble("won_rate_key",wonToRMB);

        newA.putExtras(bdl);
        setResult(520,newA);

        finish();






    }


}