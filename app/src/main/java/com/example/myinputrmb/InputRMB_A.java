package com.example.myinputrmb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InputRMB_A extends AppCompatActivity {
    private static final String Tag = "InputRMB_A";
    double dollarToRMB;
    double euroToRMB;
    double wonToRMB;

    TextView input,text;
    Button dollar,euro,won,save;
    double num;

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
}

