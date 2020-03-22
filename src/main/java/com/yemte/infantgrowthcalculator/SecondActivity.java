package com.yemte.infantgrowthcalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SecondActivity extends AppCompatActivity {
    private static RadioGroup Radio_g;
    private  static RadioButton RButton_Weight,RButton_Hight,RButton_Head;
    private  static RadioButton RadioButton_b;
    private static Button Button_Go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        onClickButtonGoListener();
    }
    public void onClickButtonGoListener(){

        Radio_g =(RadioGroup)findViewById(R.id.radioGroup2);
        RButton_Weight=(RadioButton)findViewById(R.id.RadioButton_weight);
        RButton_Hight=(RadioButton)findViewById(R.id.radioButton_hight);
        RButton_Head=(RadioButton)findViewById(R.id.radioButton_head);
        Button_Go=(Button)findViewById(R.id.button_Go);
        Button_Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected_id = Radio_g.getCheckedRadioButtonId();

                RadioButton_b =(RadioButton)findViewById(selected_id);
                if(RadioButton_b == RButton_Weight){
                    Intent weight = new Intent("com.yemte.infantgrowthcalculator.WeightGainActivity");
                    startActivity(weight);


                }
                else if (RadioButton_b == RButton_Hight){

                    Intent Hight = new Intent("com.yemte.infantgrowthcalculator.HightIncreaseActivity");
                    startActivity(Hight);

                }

                else {

                    Intent Head = new Intent("com.yemte.infantgrowthcalculator.HeadCircumferenceActivity");
                    startActivity(Head);

                }
            }
        });
    }
}
