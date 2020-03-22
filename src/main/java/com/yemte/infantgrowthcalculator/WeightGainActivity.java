package com.yemte.infantgrowthcalculator;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.DecimalFormat;

public class WeightGainActivity extends AppCompatActivity//this method  does the calculation for boys weight per age in weeks  growth rate and displays the result on another layout
{

    Button bCalc_w;//button for calculate
    EditText etC_w;//current weight input text editer
    EditText etB_w;//birth weight input text editer
    EditText etAge_w;//age input text editer
    RadioGroup rgGender_w;//radio group for gender
    RadioButton rbMale_w;//radio button for gender male
    RadioButton rbFemale_w;//radio button for gender female
    RadioGroup rgAge_w;//radio group for age
    RadioButton rbweeks_w;//radio button for weeks
    RadioButton rbmonths_w;//radio button for months
    String Age_week,Age_month;//Age_week_f,Age_month_f;
    Double BW = 0.0;//birth weight
    Double CW = 0.0;//current weight
    Double AG = 0.0;//average gain
    Double AGPW = 0.0;//average gain per week
    Double AGPM = 0.0;//average gain per month
    Double GPM = 0.0;//gain per month
    Double GPW = 0.0;//gain per week
    Double Percentw = 1.0 ;//weight gain percentile
    Double p1=0.0,p3=0.0,p15=0.0,p50=0.0,p85=0.0,p97=0.0;//1st percent,3rd percent,15th percent,50th percent,85th percent,97th percent
    int Age_w = 0;//age given in weeks
    int Age_m = 0;//age given in months


    DatabaseAccess databaseAccess;//declaring the databaseAccess

    DecimalFormat rate = new DecimalFormat(".#%");//formats the decimal point of the result.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_gain);

        rgGender_w =  findViewById(R.id.Radiogroupe_W_gender);
        etB_w = findViewById(R.id.editText_BirthWight);
        etC_w = findViewById(R.id.editText_currentWight);
        etAge_w = findViewById(R.id.editText_wAge);
        rbMale_w =  findViewById(R.id.radioButton_Wmale);
        rbFemale_w = findViewById(R.id.radioButton_wfemale);
        bCalc_w = findViewById(R.id.calcButton_weight);
        rgAge_w = findViewById(R.id.radiogroup_age);
        rbweeks_w = findViewById(R.id.radioButton_weeks_w);
        rbmonths_w =  findViewById(R.id.radioButton_months_w);

        bCalc_w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseAccess = DatabaseAccess.getInstance(getApplicationContext());//to get instances from the database
                databaseAccess.open();//opens the database connection
                // the below if statement is to check if none of the radiobuttons for gender is not checked or any of the text input fields is empty or none of the age radiobuttons are not checked
                // or if there is an empty field which a user must put values in  and should not be left unfilled
                if((!rbMale_w.isChecked()&&!rbFemale_w.isChecked())||(etB_w.getText().toString().equals(""))||
                        (etC_w.getText().toString().equals(""))||(!rbweeks_w.isChecked()&&!rbmonths_w.isChecked())||
                        (etAge_w.getText().toString().equals(""))){

                  Toast.makeText(getApplicationContext(),"ENTER ALL FIELDS !!!",Toast.LENGTH_LONG).show();//makes a pop up message
                }
                else if ((rbMale_w.isChecked()&& rbweeks_w.isChecked()))//if statement checks whether gender male radiobutton is checked and age radiobutton weeks is cheked
                {
                    MaleWeightWeeks();//this method is called which does the calculation for boys weight per age in weeks  growth rate and displays the result on another layout
                }
                else if((rbMale_w.isChecked()&& rbmonths_w.isChecked())){//if statement checks whether gender male radiobutton is checked and age radiobutton months is checked
                    maleWeightMonths();//this method is called which does the calculation for boys weight per age in months  growth rate and displays the result on another layout
                }
                else if(rbFemale_w.isChecked()&& rbmonths_w.isChecked()){//if statement checks whether gender Female radiobutton is checked and age radiobutton months is checked
                    femaleWeightMonths();//this method is called which does the calculation for girls weight per age in months growth rate  and displays the result on another layout
                }

                else if (rbFemale_w.isChecked()&&rbweeks_w.isChecked()){//if statement checks whether gender Female radiobutton is checked and age radiobutton weeks is checked
                    femaleWeightWeeks();//this method is called which does the calculation for girls weight per age in weeks  growth rate and displays the result on another layout
                }

             else {

                     Toast.makeText(getApplicationContext()," ERORR TRY AGAIN!!!!!!!!!!!",Toast.LENGTH_LONG).show();// makes pop up message
                }
            }
        });

    }
    public void femaleWeightWeeks()//this method does the calculation for girls weight per age in weeks  growth rate and displays the result on another layout
        {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());////gets database instances
            databaseAccess.open();// opens the database connection
            etAge_w =  findViewById(R.id.editText_wAge);;// gets the id of the editText_wAge(from the xml layout using this method
            BW = Double.parseDouble(etB_w.getText().toString());//BW is birth_weight changes the string value of user input  to double
            CW = Double.parseDouble(etC_w.getText().toString());//CW is current_weight changes the string value of user input to double

            Age_week = etAge_w.getText().toString();//get text from user input in age text editer
            Age_w = Integer.valueOf(Age_week);//convert the string  value of age_week in to integer


            if(Age_w<=13)//if age given in the input is less than or equal to 13 weeks
            {

                String p1st = databaseAccess.getGww1p(Age_week);//we used the getGwm1p method to get 1%
                String p3rd = databaseAccess.getGww3p(Age_week);//we used thegetgetGwm3p method to get 3%
                String p15th = databaseAccess.getGww15p(Age_week);//we used the getGww15P method to get 15%
                String p50th = databaseAccess.getGww50p(Age_week);//we used the getGww50p method to get 50%
                String p85th = databaseAccess.getGww85p(Age_week);//we used the getGww85p method to get 85%
                String p97th = databaseAccess.getGww97p(Age_week);//we used the getGww97p method to get 97%
                p1 = Double.parseDouble(p1st);//changes the string value of 1st % weight in kg to double type
                p3 = Double.parseDouble(p3rd);//changes the string value of 3% weight in kg to double type
                p15 = Double.parseDouble(p15th);//changes the string value of 15% weight in kg to double type
                p50 = Double.parseDouble(p50th);//changes the string value of 50% weight in kg to double type
                p85 = Double.parseDouble(p85th);//changes the string value of 85% weight in kg to double type
                p97 = Double.parseDouble(p97th);//changes the string value of 97% weight in kg to double type
                switch (Age_week) {
                    case "0": {//case 0 is when age is given 0 week
                        if (BW <= p1)//if BW weight input is less than 1%
                        {
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 0 %" );
                            GraphView g = findViewById(R.id.graph);
                            PointsGraphSeries<DataPoint> series4 = new PointsGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_w, BW)});
                            g.addSeries(series4);
                            series4.setShape(PointsGraphSeries.Shape.TRIANGLE);
                            series4.setSize(10);
                            series4.setColor(Color.BLACK);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));

//                            series4.setDrawDataPoints(true);
//                            series4.setDataPointsRadius(10);
//                            series4.setThickness(8);
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (BW >p1 &&BW <= p3) {
                            AG = (BW - p1);
                            AGPW = p3-p1;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.02)+0.01;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                            GraphView g = findViewById(R.id.graph);
                            PointsGraphSeries<DataPoint> series4 = new PointsGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_w, BW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            //series4.setDrawDataPoints(true);
                            graphGirls();//WHO girls weight to age growth  chart
                        }

                        else if (BW >  p3 && BW <= p15) {
                            AG = (BW - p3);
                            AGPW = p15-p3;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.03;

                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                            GraphView g = findViewById(R.id.graph);
                            PointsGraphSeries<DataPoint> series4 = new PointsGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_w, BW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
//                            series4.setDrawDataPoints(true);
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (BW > p15 && BW <= p50) {
                            AG = (BW - p15);
                            AGPW = p50-p15;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.15;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                            GraphView g = findViewById(R.id.graph);
                            PointsGraphSeries<DataPoint> series4 = new PointsGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_w, BW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
//                            series4.setDrawDataPoints(true);
                            graphGirls();//WHO girls weight to age growth  chart
                            //graph();
                        }
                        else if (BW > p50 && BW <=p85){
                            AG = (BW - p50);
                            AGPW = p85-p50;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.5;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                            GraphView g = findViewById(R.id.graph);
                            PointsGraphSeries<DataPoint> series4 = new PointsGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_w, BW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            //series4.setDrawDataPoints(true);
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (BW > p85 && BW <=p97){
                            AG = (BW - p85);
                            AGPW = p97-p85;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.85;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                            GraphView g = findViewById(R.id.graph);
                            PointsGraphSeries<DataPoint> series4 = new PointsGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_w, BW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
//                            series4.setDrawDataPoints(true);
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else {
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 100 %" );
                            GraphView g = findViewById(R.id.graph);
                            PointsGraphSeries<DataPoint> series4 = new PointsGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_w, BW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
//                            series4.setDrawDataPoints(true);
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                    }
                    break;
                    case "1": {
                        if (CW <= p1){
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 0 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            series4.setDrawDataPoints(true);
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW >p1 &&CW <= p3) {
                            AG = (CW - p1);
                            AGPW = p3-p1;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.02)+0.01;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }

                        else if (CW >  p3 && CW <= p15) {
                            AG = (CW - p3);
                            AGPW = p15-p3;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.03;

                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);

                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            //Toast.makeText(this, "Growth rate is : " + Percentw, Toast.LENGTH_LONG).show();

                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p15 && CW <= p50) {
                            AG = (CW - p15);
                            AGPW = p50-p15;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.15;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart

                        }
                        else if (CW > p50 && CW <=p85){
                            AG = (CW - p50);
                            AGPW = p85-p50;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.5;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p85 && CW <=p97){
                            AG = (CW - p85);
                            AGPW = p97-p85;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.85;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else {
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 100 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                    }

                    break;
                    case "2": {
                        if (CW <= p1){
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 0 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW >p1 &&CW <= p3) {
                            AG = (CW - p1);
                            AGPW = p3-p1;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.02)+0.01;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            series4.setDrawDataPoints(true);
                            graphGirls();//WHO girls weight to age growth  chart
                        }

                        else if (CW >  p3 && CW <= p15) {
                            AG = (CW - p3);
                            AGPW = p15-p3;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.03;

                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);

                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p15 && CW <= p50) {
                            AG = (CW - p15);
                            AGPW = p50-p15;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.15;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p50 && CW <=p85){
                            AG = (CW - p50);
                            AGPW = p85-p50;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.5;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p85 && CW <=p97){
                            AG = (CW - p85);
                            AGPW = p97-p85;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.85;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else {
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 100 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                    }
                    break;
                    case "3":{
                        if (CW <= p1){
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 0 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW >p1 &&CW <= p3) {
                            AG = (CW - p1);
                            AGPW = p3-p1;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.02)+0.01;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }

                        else if (CW >  p3 && CW <= p15) {
                            AG = (CW - p3);
                            AGPW = p15-p3;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.03;

                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p15 && CW <= p50) {
                            AG = (CW - p15);
                            AGPW = p50-p15;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.15;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p50 && CW <=p85){
                            AG = (CW - p50);
                            AGPW = p85-p50;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.5;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p85 && CW <=p97){
                            AG = (CW - p85);
                            AGPW = p97-p85;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.85;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else {
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 100 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                    }
                    break;
                    case "4": {
                        if (CW <= p1){
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 0 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW >p1 &&CW <= p3) {
                            AG = (CW - p1);
                            AGPW = p3-p1;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.02)+0.01;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }

                        else if (CW >  p3 && CW <= p15) {
                            AG = (CW - p3);
                            AGPW = p15-p3;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.03;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p15 && CW <= p50) {
                            AG = (CW - p15);
                            AGPW = p50-p15;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.15;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p50 && CW <=p85){
                            AG = (CW - p50);
                            AGPW = p85-p50;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.5;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p85 && CW <=p97){
                            AG = (CW - p85);
                            AGPW = p97-p85;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.85;
                            setContentView(R.layout.activity_result);
                            TextView tv =findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else {
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 100 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                    }

                    break;
                    case "5": {
                        if (CW <= p1){
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 0 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW >p1 &&CW <= p3) {
                            AG = (CW - p1);
                            AGPW = p3-p1;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.02)+0.01;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW >  p3 && CW <= p15) {
                            AG = (CW - p3);
                            AGPW = p15-p3;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.03;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p15 && CW <= p50) {
                            AG = (CW - p15);
                            AGPW = p50-p15;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.15;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p50 && CW <=p85){
                            AG = (CW - p50);
                            AGPW = p85-p50;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.5;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p85 && CW <=p97){
                            AG = (CW - p85);
                            AGPW = p97-p85;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.85;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else {
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 100 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                    }
                    break;
                    case "6": {
                        if (CW <= p1){
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 0 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW >p1 &&CW <= p3) {
                            AG = (CW - p1);
                            AGPW = p3-p1;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.02)+0.01;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW >  p3 && CW <= p15) {
                            AG = (CW - p3);
                            AGPW = p15-p3;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.03;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p15 && CW <= p50) {
                            AG = (CW - p15);
                            AGPW = p50-p15;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.15;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p50 && CW <=p85){
                            AG = (CW - p50);
                            AGPW = p85-p50;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.5;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p85 && CW <=p97){
                            AG = (CW - p85);
                            AGPW = p97-p85;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.85;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else {
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 100 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                    }
                    break;
                    case "7": {
                        if (CW <= p1){
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 0 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW >p1 &&CW <= p3) {
                            AG = (CW - p1);
                            AGPW = p3-p1;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.02)+0.01;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW >  p3 && CW <= p15) {
                            AG = (CW - p3);
                            AGPW = p15-p3;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.03;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p15 && CW <= p50) {
                            AG = (CW - p15);
                            AGPW = p50-p15;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.15;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p50 && CW <=p85){
                            AG = (CW - p50);
                            AGPW = p85-p50;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.5;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p85 && CW <=p97){
                            AG = (CW - p85);
                            AGPW = p97-p85;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.85;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else {
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 100 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                    }
                    break;
                    case "8": {
                        if (CW <= p1){
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 0 %" );
                            GraphView g = findViewById(R.id.graph);
                            PointsGraphSeries<DataPoint> series4 = new PointsGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.YELLOW);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW >p1 &&CW <= p3) {
                            AG = (CW - p1);
                            AGPW = p3-p1;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.02)+0.01;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW >  p3 && CW <= p15) {
                            AG = (CW - p3);
                            AGPW = p15-p3;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.03;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p15 && CW <= p50) {
                            AG = (CW - p15);
                            AGPW = p50-p15;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.15;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p50 && CW <=p85){
                            AG = (CW - p50);
                            AGPW = p85-p50;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.5;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p85 && CW <=p97){
                            AG = (CW - p85);
                            AGPW = p97-p85;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.85;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else {
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 100 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                    }
                    break;
                    case "9":{
                        if (CW <= p1){
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 0 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW >p1 &&CW <= p3) {
                            AG = (CW - p1);
                            AGPW = p3-p1;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.02)+0.01;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }

                        else if (CW >  p3 && CW <= p15) {
                            AG = (CW - p3);
                            AGPW = p15-p3;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.03;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p15 && CW <= p50) {
                            AG = (CW - p15);
                            AGPW = p50-p15;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.15;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p50 && CW <=p85){
                            AG = (CW - p50);
                            AGPW = p85-p50;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.5;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p85 && CW <=p97){
                            AG = (CW - p85);
                            AGPW = p97-p85;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.85;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else {
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 100 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                    }
                    break;
                    case "10": {
                        if (CW <= p1){
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 0 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW >p1 &&CW <= p3) {
                            AG = (CW - p1);
                            AGPW = p3-p1;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.02)+0.01;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }

                        else if (CW >  p3 && CW <= p15) {
                            AG = (CW - p3);
                            AGPW = p15-p3;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.03;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p15 && CW <= p50) {
                            AG = (CW - p15);
                            AGPW = p50-p15;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.15;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p50 && CW <=p85){
                            AG = (CW - p50);
                            AGPW = p85-p50;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.5;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p85 && CW <=p97){
                            AG = (CW - p85);
                            AGPW = p97-p85;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.85;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else {
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 100 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                    }
                    break;
                    case "11": {
                        if (CW <= p1){
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 0 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW >p1 &&CW <= p3) {
                            AG = (CW - p1);
                            AGPW = p3-p1;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.02)+0.01;
                            setContentView(R.layout.activity_result);
                            TextView tv = (TextView)findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = (GraphView)findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }

                        else if (CW >  p3 && CW <= p15) {
                            AG = (CW - p3);
                            AGPW = p15-p3;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.03;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p15 && CW <= p50) {
                            AG = (CW - p15);
                            AGPW = p50-p15;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.15;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p50 && CW <=p85){
                            AG = (CW - p50);
                            AGPW = p85-p50;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.5;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p85 && CW <=p97){
                            AG = (CW - p85);
                            AGPW = p97-p85;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.85;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else {
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 100 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                    }
                    break;
                    case "12":{
                        if (CW <= p1){
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 0 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW >p1 &&CW <= p3) {
                            AG = (CW - p1);
                            AGPW = p3-p1;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.02)+0.01;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW >  p3 && CW <= p15) {
                            AG = (CW - p3);
                            AGPW = p15-p3;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.03;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p15 && CW <= p50) {
                            AG = (CW - p15);
                            AGPW = p50-p15;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.15;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p50 && CW <=p85){
                            AG = (CW - p50);
                            AGPW = p85-p50;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.5;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p85 && CW <=p97){
                            AG = (CW - p85);
                            AGPW = p97-p85;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.85;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else {
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 100 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                    }
                    break;
                    case "13": {
                        if (CW <= p1){
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 0 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(3, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW >p1 &&CW <= p3) {
                            AG = (CW - p1);
                            AGPW = p3-p1;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.02)+0.01;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(3, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }

                        else if (CW >  p3 && CW <= p15) {
                            AG = (CW - p3);
                            AGPW = p15-p3;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.03;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(3, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p15 && CW <= p50) {
                            AG = (CW - p15);
                            AGPW = p50-p15;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.15;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(3, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p50 && CW <=p85){
                            AG = (CW - p50);
                            AGPW = p85-p50;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.35)+0.5;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(3, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO girls weight to age growth  chart
                        }
                        else if (CW > p85 && CW <=p97){
                            AG = (CW - p85);
                            AGPW = p97-p85;
                            GPW = (AG) / AGPW;
                            Percentw = (GPW * 0.12)+0.85;
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(3, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO Girls weight to age growth  chart
                        }
                        else {
                            setContentView(R.layout.activity_result);
                            TextView tv = findViewById(R.id.result_h);
                            tv.setText(" growth rate is : 100 %" );
                            GraphView g = findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(3, CW)});
                            g.addSeries(series4);
                            series4.setColor(Color.BLACK);
                            series4.setDrawDataPoints(true);
                            series4.setTitle(String.valueOf(rate.format(Percentw)));
                            graphGirls();//WHO Girls weight to age growth  chart;
                        }
                    }
                    break;
                }
            }

            else{

                Toast.makeText(getApplicationContext(),"Fill Age In Months ",Toast.LENGTH_LONG).show();//pop up message
            }
            databaseAccess.close();//database connection closed
        }
    public void femaleWeightMonths()//this method does the calculation for girls weight per age in months  growth rate and displays the result on another layout
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        etAge_w =  findViewById(R.id.editText_wAge);
        BW = Double.parseDouble(etB_w.getText().toString());
        CW = Double.parseDouble(etC_w.getText().toString());
        Age_month = etAge_w.getText().toString();//get text from user input in age text editer
        Age_m = Integer.valueOf(Age_month);//convert the string in to integer
        if(Age_m<=12) //if age is less than or equal to 12 months
        {
            String p1st = databaseAccess.getGwm1p(Age_month);//we used the getGwm1p method to get  the value of 1st percent in  string format
            String p3rd = databaseAccess.getGwm3p(Age_month);//we used thegetgetGwm3p method to get the value of  3rd percent in  string format
            String p15th = databaseAccess.getGwm15p(Age_month);//we used the getGwm15P method to the value of   15th percent in  string format
            String p50th = databaseAccess.getGwm50p(Age_month);//we used the getGwm50p method to the value of   50th percent in  string format
            String p85th = databaseAccess.getGwm85p(Age_month);//we used the getGwm85p method to the value of  85th percent in  string format
            String p97th = databaseAccess.getGwm97p(Age_month);//we used the getGwm97p method to the value of  97th percent in  string format
            p1 = Double.parseDouble(p1st);//changing string to double
            p3 = Double.parseDouble(p3rd);//changing string to double
            p15 = Double.parseDouble(p15th);//changing string to double
            p50 = Double.parseDouble(p50th);//changing string to double
            p85 = Double.parseDouble(p85th);//changing string to double
            p97 = Double.parseDouble(p97th);//changing string to double
            switch (Age_month) {
                case "0": {//case 0 is when age is given 0 month
                    if (CW <= p1){
                        setContentView(R.layout.activity_result); //changing the contentview to another layout to display the result in there
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );//setting text to textview tv
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart

                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "1": {
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "2": {
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }

                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart

                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "3":{
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "4": {
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "5": {
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "6": {
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "7": {
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart

                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "8": {
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "9":{
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart

                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;

                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "10": {
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;

                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "11": {
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));

                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "12":{
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graphGirls();//WHO girls weight to age growth  chart
                    }
                }
                break;
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"This app is valid  only  for age up to 12 months",Toast.LENGTH_LONG).show();
        }
        databaseAccess.close();//database connection closed
    }
    public void maleWeightMonths()//this method  does the calculation for boys weight per age in months  growth rate and displays the result on another layout
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        etAge_w =  findViewById(R.id.editText_wAge);
        BW = Double.parseDouble(etB_w.getText().toString());
        CW = Double.parseDouble(etC_w.getText().toString());
        //else if((rbMale_w.isChecked()&& rbmonths_w.isChecked())){
        Age_month = etAge_w.getText().toString();//get text from user input in age text editer
        Age_m = Integer.valueOf( Age_month);//convert the string in to integer
        //if age is less than or equal to 12 months
        String p1st = databaseAccess.getBwm1p(Age_month);//we used the getBwm1p method to get address
        String p3rd = databaseAccess.getBwm3p(Age_month);//we used the getBwm3p method to get address
        String p15th = databaseAccess.getBwm15p(Age_month);//we used the getBwm15p method to get address
        String p50th = databaseAccess.getBwm50p(Age_month);//we used the getBwm50p method to get address
        String p85th = databaseAccess.getBwm85p(Age_month);//we used the getBwm85p method to get address
        String p97th = databaseAccess.getBwm97p(Age_month);//we used the getBwm97p method to get address
        if(Age_m<=12)  {
            p1 = Double.parseDouble(p1st);
            p3 = Double.parseDouble(p3rd);
            p15 = Double.parseDouble(p15th);
            p50 = Double.parseDouble(p50th);
            p85 = Double.parseDouble(p85th);
            p97 = Double.parseDouble(p97th);
            switch ( Age_month) {
                case "0": {//case 0 is when age is given 0 month
                    if (BW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series);
                        series.setColor(Color.BLACK);
                        series.setDrawDataPoints(true);
                        series.setTitle(String.valueOf(rate.format(Percentw)));
                        g.getViewport().setMinX(0);//sets minimum x-axis to 0
                        g.getViewport().setMaxX(15);//sets maximum x axis to 14
                        g.getViewport().setMinY(0);//sets mimimum y axis to 0
                        g.getViewport().setMaxY(15);//sets maximum y axis
                        g.setTitle("WHO growth Chart");//sets graph title
                        g.getViewport().setYAxisBoundsManual(true);//allows to set manual y bounds
                        g.getViewport().setXAxisBoundsManual(true);//allows to set manual x bounds
                        //GridLabelRenderer gridLabel = g.getGridLabelRenderer();
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (BW >p1 &&BW <= p3) {
                        AG = (BW - p1);
                        AGPM = p3-p1;
                        GPM = (AG) / AGPM;
                        Percentw = (GPM* 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series);
                        series.setColor(Color.BLACK);
                        series.setDrawDataPoints(true);
                        series.setTitle(String.valueOf(rate.format(Percentw)));
                        g.getViewport().setMinX(0);//sets minimum x-axis to 0
                        g.getViewport().setMaxX(15);//sets maximum x axis to 14
                        g.getViewport().setMinY(0);//sets mimimum y axis to 0
                        g.getViewport().setMaxY(15);//sets maximum y axis
                        g.setTitle("WHO growth Chart");//sets graph title
                        g.getViewport().setYAxisBoundsManual(true);//allows to set manual y bounds
                        g.getViewport().setXAxisBoundsManual(true);//allows to set manual x bounds
                        GridLabelRenderer gridLabel = g.getGridLabelRenderer();
                        graph();//WHO boys weight to age growth  chart
                    }

                    else if (BW>  p3 && BW <= p15) {
                        AG = (BW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;

                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));

                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series3);
                        series3.setColor(Color.BLACK);
                        series3.setDrawDataPoints(true);
                        series3.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (BW> p15 && BW<= p50) {
                        AG = (BW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        g.getViewport().setMinX(0);//sets minimum x-axis to 0
                        g.getViewport().setMaxX(15);//sets maximum x axis to 14
                        g.getViewport().setMinY(0);//sets mimimum y axis to 0
                        g.getViewport().setMaxY(15);//sets maximum y axis
                        g.setTitle("WHO growth Chart");//sets graph title
                        g.getViewport().setYAxisBoundsManual(true);//allows to set manual y bounds
                        g.getViewport().setXAxisBoundsManual(true);//allows to set manual x bounds

                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (BW > p50 && BW <=p85){
                        AG = (BW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (BW > p85 && BW <=p97){
                        AG = (BW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
                case "1": {//case 0 is when age is given 0 month
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
                case "2": {//case 2 is when age is given 2 month
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;

                case "3": {//case 0 is when age is given 0 month
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }

                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv =findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
                case "4": {//case 0 is when age is given 0 month
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  char
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
                case "5": {//case 0 is when age is given 0 month
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPM = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
                case "6": {//case 0 is when age is given 0 month
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
                case "7": {//case 7 is when age is given 7 month
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }

                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;

                case "8":{//case 0 is when age is given 0 month
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }

                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
                case "9":{//case 0 is when age is given 0 month
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
                case "10":{//case 0 is when age is given 0 month
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));


                        graph();//WHO boys weight to age growth  chart

                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
                case "11":{//case 0 is when age is given 0 month
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }

                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
                case "12": {//case 12 is when age is given 12 month
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }

                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        PointsGraphSeries<DataPoint> series4 = new PointsGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setShape(PointsGraphSeries.Shape.TRIANGLE);
                        series4.setSize(15);
                        //series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPM = p85-p50;
                        GPM = (AG) / AGPM;
                        Percentw = (GPM * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPM = p97-p85;
                        GPM= (AG) / AGPM;
                        Percentw = (GPM* 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_m, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
            }
        }
        else{

            Toast.makeText(getApplicationContext(),"The calculator is valid for age up ti 12 months",Toast.LENGTH_LONG).show();
        }
        databaseAccess.close();//database connection closed
    }
    public void MaleWeightWeeks() {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        etAge_w =  findViewById(R.id.editText_wAge);
        BW = Double.parseDouble(etB_w.getText().toString());
        CW = Double.parseDouble(etC_w.getText().toString());
        Age_week = etAge_w.getText().toString();//get text from user input in age text editer
        Age_w = Integer.valueOf(Age_week);//convert the string in to integer
        //if age is less than or equal to 13 weeks
        String p1st = databaseAccess.getBww1p(Age_week);//we used the getBww1p method to get 1st percent from database
        String p3rd = databaseAccess.getBww3p(Age_week);//we used thegetBww3p method to get 3rd percent from database
        String p15th = databaseAccess.getBww15P(Age_week);//we used the getBww15P method to get 15% from database
        String p50th = databaseAccess.getBww50p(Age_week);//we used the getBww50p method to get 50% from database
        String p85th = databaseAccess.getBww85p(Age_week);//we used the getBww85p method to get 85% from database
        String p97th = databaseAccess.getBww97p(Age_week);//we used the getBww97p method to get 97% from database
        if(Age_w<=13)  {
            p1 = Double.parseDouble(p1st); //changes string to double
            p3 = Double.parseDouble(p3rd);//changes string value of 3rd percent to double
            p15 = Double.parseDouble(p15th);//changes string value of 15% to double
            p50 = Double.parseDouble(p50th);// changes string value of 50% to double
            p85 = Double.parseDouble(p85th);//changes string value of 85% to double
            p97 = Double.parseDouble(p97th);// chnages strinf value of 97% to double
            switch (Age_week) { // if age is given in week
                case "0": {//case 0 is when age is  0 week
                    if (BW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_w, BW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
//                        series4.setDataPointsRadius(12);
//                        series4.setThickness(10);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (BW >p1 &&BW <= p3) {
                        AG = (BW- p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_w, BW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (BW >  p3 && BW <= p15) {
                        AG = (BW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_w, BW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (BW > p15 && BW<= p50) {
                        AG = (BW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_w, BW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (BW > p50 && BW <=p85){
                        AG = (BW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_w, BW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (BW > p85 && BW <=p97){
                        AG = (BW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_w, BW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(Age_w, BW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
                case "1": {
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart;
                    }
                }

                break;
                case "2": {
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }

                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart;
                    }
                }

                break;
                case "3":{
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart;
                    }
                }

                break;
                case "4": {
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
                case "5": {
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }

                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
                case "6": {
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }

                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart

                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
                case "7": {
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }

                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  charT
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
                case "8": {
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }

                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = (TextView)findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);;
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
                case "9":{
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
                case "10": {
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }

                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
                case "11": {
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = (GraphView)findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
                case "12":{
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series);
                        series.setColor(Color.BLACK);
                        series.setTitle(String.valueOf(rate.format(Percentw)));
                        series.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series);
                        series.setColor(Color.BLACK);
                        series.setTitle(String.valueOf(rate.format(Percentw)));
                        series.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }

                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series3);
                        series3.setColor(Color.BLACK);
                        series3.setTitle(String.valueOf(rate.format(Percentw)));
                        series3.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series3);
                        series3.setColor(Color.BLACK);
                        series3.setTitle(String.valueOf(rate.format(Percentw)));
                        series3.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series5 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series5);
                        series5.setColor(Color.BLACK);
                        series5.setTitle(String.valueOf(rate.format(Percentw)));
                        series5.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW > p85 && CW <=p97){
                        AG = (CW - p85);
                        AGPW = p97-p85;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.85;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart

                    }
                    else {
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 100 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart;
                    }
                }
                break;
                case "13": {
                    if (CW <= p1){
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : 0 %" );
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(3, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }
                    else if (CW >p1 &&CW <= p3) {
                        AG = (CW - p1);
                        AGPW = p3-p1;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.02)+0.01;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(3, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart
                    }

                    else if (CW >  p3 && CW <= p15) {
                        AG = (CW - p3);
                        AGPW = p15-p3;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.12)+0.03;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(3, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart

                    }
                    else if (CW > p15 && CW <= p50) {
                        AG = (CW - p15);
                        AGPW = p50-p15;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.15;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(3, CW)});
                        g.addSeries(series4);
                        series4.setColor(Color.BLACK);
                        series4.setTitle(String.valueOf(rate.format(Percentw)));
                        series4.setDrawDataPoints(true);
                        graph();//WHO boys weight to age growth  chart

                    }
                    else if (CW > p50 && CW <=p85){
                        AG = (CW - p50);
                        AGPW = p85-p50;
                        GPW = (AG) / AGPW;
                        Percentw = (GPW * 0.35)+0.5;
                        setContentView(R.layout.activity_result);
                        TextView tv = findViewById(R.id.result_h);
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));
                        GraphView g = findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(3, CW)});
                        g.addSeries(series4);//adds datapoints to the graph series
                        series4.setColor(Color.BLACK);//sets the color
                        series4.setTitle(String.valueOf(rate.format(Percentw)));//displays the result on the legend as title
                        series4.setDrawDataPoints(true);//displays datapoints on the chart
                        graph();//WHO boys weight to age growth  chart

                    }
                    else if (CW > p85 && CW <=p97)//if the user input current weight(CW) is greater than the value of the WHO 85% and less than or equal to WHO 97%
                        //do the following calculation to get the growth rate percentile
                    {
                        AG = (CW - p85);//AG(average gain) weight difference between the given current weight and WHO  85% value (weight in kg)
                        AGPW = p97-p85;//AGPW(average gain WHO percentile weight) is weight gain difference between WHO standard growth rate 97% and 85%
                        GPW = (AG) / AGPW;//GPW (gain per weight difference ratio)is weight gain above 85% value  per weight difference of the 97% and 85%
                        Percentw = (GPW * 0.12)+0.85; // final result in percentage is calculated  by adding  the fraction of the 12 %(97%-85%) to the 85%
                        setContentView(R.layout.activity_result);// sets the content view  layout_result to display the result on that layout
                        TextView tv = findViewById(R.id.result_h);// gets the id of the textView_R2 (from the layout_result  xml)
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(Percentw)));////sets the result text to textview tv
                        GraphView g = findViewById(R.id.graph);// gets the id of the graph (from the layout_result  xml)
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(3, CW)});//sets the datapoints  x axis to  age equals 3 month and
                        // current weight given by the user to the y axis ,draws line graph as a dot point for the result and compares it with WHO  boy's chart.
                        g.addSeries(series4);//adds datapoints to the graph series
                        series4.setColor(Color.BLACK);//sets the color
                        series4.setTitle(String.valueOf(rate.format(Percentw)));//displays the result on the legend as title
                        series4.setDrawDataPoints(true);//displays datapoints on the chart
                        graph();//calls WHO boys weight to age growth  chart;
                    }
                    else {
                        setContentView(R.layout.activity_result);// sets the content view to  layout_result to display the result on that layout
                        TextView tv = findViewById(R.id.result_h);// gets the id of the textView_R2 (from the layout_result  xml)
                        tv.setText(" growth rate is : 100 %" );//sets the result text to textview
                        GraphView g = findViewById(R.id.graph);// gets the id of the graph (from the layout_result  xml)
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {//used to draw line graph for the result growth rate on the  who boys chart as a dot point to compare it
                                new DataPoint(3, CW)});//sets the datapoints  x axis to  age equals 3 month and current weight user input to the y axis to the graph
                        g.addSeries(series4);//adds datapoints to the graph series
                        series4.setColor(Color.BLACK);//sets the color
                        series4.setTitle(String.valueOf(rate.format(Percentw)));//displays the result on the legend
                        series4.setDrawDataPoints(true);//displays datapoints on the chart
                        graph();//calls WHO boys weight to age growth  chart;
                    }
                }
                break;
            }
        }

        else{

            Toast.makeText(getApplicationContext(),"Fill Age In Months",Toast.LENGTH_LONG).show();
        }
        //database connection closed
        databaseAccess.close();
    }

    public void
    graph()//a method to draw a WHO  growth chart for boys (displaying 3%,50% and 97% growth rate standards)
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());//gets database instances
        databaseAccess.open();//opens the database
        GraphView graph = (GraphView) findViewById(R.id.graph);// gets the id of the graph in a view(from the xml layout_result using this method
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(databaseAccess.getDatawmb3p());//used to draw line graph for 3 percent growth rate and gets the datapoints(who standard values weight per age) from the database
        graph.addSeries(series);//adds the datapoints series to the line graph
        series.setTitle("3%");//sets the line graph title
        series.setColor(Color.RED);//sets the line graph color
       //series.setDrawDataPoints(true);

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(databaseAccess.getDatawmb15p());//used to draw line graph for 3 percent growth rate and gets the datapoints(who standard values weight per age) from the database
        graph.addSeries(series2);//adds the datapoints series to the line graph
        series2.setTitle("15%");//sets the line graph title
        series2.setColor(Color.LTGRAY);//sets the line graph color
        //series2.setDrawDataPoints(true);

        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>(databaseAccess.getDatawmb50p());//used to draw line graph for 50 percent growth rate and gets the datapoints(who standard values weight per age) from the database
        graph.addSeries((series3));;//adds the datapoints series to the line graph
        series3.setColor(Color.GREEN);//sets the line graph color
        series3.setTitle(" 50%");//sets the line graph title
        //series3.setDrawDataPoints(true);
        series3.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getApplicationContext(),dataPoint.toString(),Toast.LENGTH_LONG).show();
            }
        });

        LineGraphSeries<DataPoint> series85 = new LineGraphSeries<>(databaseAccess.getDatawmb85p());//used to draw line graph for 3 percent growth rate and gets the datapoints(who standard values weight per age) from the database
        graph.addSeries(series85);//adds the datapoints series to the line graph
        series85.setTitle("85%");//sets the line graph title
        series85.setColor(Color.MAGENTA);//sets the line graph color
        //series85.setDrawDataPoints(true);

        LineGraphSeries<DataPoint> series5 = new LineGraphSeries<DataPoint>(databaseAccess.getDatawmb97p());//used to draw line graph for 97 percent growth rate and gets the datapoints(who standard values weight per age) from the database
        graph.addSeries((series5));//adds the datapoints series to the line graph
        series5.setColor(Color.BLUE);//sets the line graph color
        series5.setTitle(" 97%");//sets the line graph title
        //series5.setDrawDataPoints(true);

        graph.getLegendRenderer().setVisible(true);//displays the line graph chart's legend
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);//sets the legend's position to the bottom
        graph.getViewport().setMinX(0);//sets minimum x-axis to 0
        graph.getViewport().setMaxX(15);//sets maximum x axis to 14
        graph.getViewport().setMinY(0);//sets mimimum y axis to 0
        graph.getViewport().setMaxY(15);//sets maximum y axis
        graph.setTitle("WHO weight_for_age percentiles for Boys ");//sets graph title
        graph.getViewport().setYAxisBoundsManual(true);//allows to set manual y bounds
        graph.getViewport().setXAxisBoundsManual(true);//allows to set manual x bounds
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();//is responsible for generating vertical and horizontal lables and the grid lines
        gridLabel.setHorizontalAxisTitle("Age(months)"); //sets x axis title
        gridLabel.setVerticalAxisTitle("Weight(kg)"); // sets y axis title
    }
    public void graphGirls() //a method to draw a WHO  growth chart for girls(displaying 3%,50% and 97% growth rate standards)
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());//gets database instances
        databaseAccess.open();//opens the database
        GraphView graph = (GraphView) findViewById(R.id.graph);// gets the id of the graph in a view(from the xml layout_result using this method
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(databaseAccess.getDatawmg3p());//used to draw line graph for 3 percent growth rate  and gets the datapoints(who standard weight per age values)) from the database
        graph.addSeries(series);//adds the datapoints series to the line graph
        series.setTitle("3%");//sets the line graph title
        series.setColor(Color.RED);//set the line graph color to red
        //series.setDrawDataPoints(true);

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(databaseAccess.getDatawmg15p());//used to draw line graph for 3 percent growth rate and gets the datapoints(who standard values weight per age) from the database
        graph.addSeries(series2);//adds the datapoints series to the line graph
        series2.setTitle("15%");//sets the line graph title
        series2.setColor(Color.MAGENTA);//sets the line graph color
        //series2.setDrawDataPoints(true);

        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>(databaseAccess.getDatawmg50p());//used to draw line graph and gets the datapoints from the database
        graph.addSeries((series3));//adds series data points to the graph
        series3.setColor(Color.GREEN);//sets the line graph color
        series3.setTitle(" 50%");//sets line graph title
        //series3.setDrawDataPoints(true);

        LineGraphSeries<DataPoint> series85 = new LineGraphSeries<>(databaseAccess.getDatawmg85p());//used to draw line graph for 3 percent growth rate and gets the datapoints(who standard values weight per age) from the database
        graph.addSeries(series85);//adds the datapoints series to the line graph
        series85.setTitle("85%");//sets the line graph title
        series85.setColor(Color.LTGRAY);//sets the line graph color
        //series85.setDrawDataPoints(true);


        LineGraphSeries<DataPoint> series5 = new LineGraphSeries<DataPoint>(databaseAccess.getDatawmg97p());//is used to draw line graph
        graph.addSeries((series5));//add series points to the graph
        series5.setColor(Color.BLUE);//sets the line graph color
        series5.setTitle(" 97%");//sets the line graph title
        //series5.setDrawDataPoints(true);

        graph.getLegendRenderer().setVisible(true);//sets legend visibility
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM); //to show the legend at the bottom
        graph.getViewport().setMinX(0);//sets the x axis minimum value 0
        graph.getViewport().setMaxX(15);//sets x axix maximum value 14
        graph.getViewport().setMinY(0);//sets y axix minumum value 0
        graph.getViewport().setMaxY(15);//sets y axix maximum value to 12
        graph.setTitle("WHO weight_for_age percentiles for Girls ");//sets graph title
        graph.getViewport().setYAxisBoundsManual(true);// allows the a axis bound to be set manually
        graph.getViewport().setXAxisBoundsManual(true);//allows the x axis bound to be set manually
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();//allows grid lable
        gridLabel.setHorizontalAxisTitle("Age(months)");//sets x axis  title
        gridLabel.setVerticalAxisTitle("Weight(kg)");//sets y axis title
    }
}





















