package com.yemte.infantgrowthcalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class StartPage extends AppCompatActivity {



    private static Button Button_Start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        onClickButtonStartListener();
    }

    public void onClickButtonStartListener(){

        Button_Start = (Button)findViewById(R.id.ButtonStart);
        Button_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent("com.yemte.infantgrowthcalculator.SecondActivity");
                startActivity(intent2);
            }
        });



    }

}
