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
import com.jjoe64.graphview.series.Series;

import java.text.DecimalFormat;

public class HightIncreaseActivity extends AppCompatActivity {
    Button bCalc_h;//button for calculate
    EditText etC_h;//current hight input text editer
    EditText etB_h;//birth hight input text editer
    EditText etAge_h;//age input text editer
    RadioGroup rgGender_h;//radio group for gender
    RadioButton rbMale_h;//radio button for gender male
    RadioButton rbFemale_h;//radio button for gender female
    RadioGroup rgAge_h;//radio group for age
    RadioButton rbweeks_h;//radio button for weeks
    RadioButton rbmonths_h;//radio button for months
    String Age_week,Age_month;//Age_week,Age_month;
    Double BH = 0.0;//birth weight
    Double CH = 0.0;//current weight
    Double AG = 0.0;//average gain
    Double AGPW = 0.0;//average gain per week
    Double AGPM = 0.0;//average gain per month
    Double GPM = 0.0;//gain per month
    Double GPW = 0.0;//gain per week
    Double PercentH = 1.0 ;//weight gain percentile
    Double p1=0.0,p3=0.0,p15=0.0,p50=0.0,p85=0.0,p97=0.0;//1st percent,3rd percent,15th percent,50th percent,85th percent,97th percent
    int Age_w = 0;//age given in weeks
    int Age_m = 0;//age given in months

    DatabaseAccess databaseAccess;//declaring the databaseAccess
    DecimalFormat rate = new DecimalFormat(".#%");//formats the decimal point of the result.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hight_increase);
        rgGender_h =  findViewById(R.id.radiogroup_Hightgender); //gets the id of the textView_R2 (from the layout_result  xml)
        etB_h = findViewById(R.id.et_BirthHight);
        etC_h = findViewById(R.id.et_currentHight);
        etAge_h = findViewById(R.id.et_Hage);
        rbMale_h =  findViewById(R.id.rb_Hmale);
        rbFemale_h = findViewById(R.id.rb_Hfemale);
        bCalc_h = findViewById(R.id.button_hight_calculate);
        rgAge_h = findViewById(R.id.Rg_Age);
        rbweeks_h = findViewById(R.id.RBH_weeks);
        rbmonths_h =  findViewById(R.id.RBH_months);
        bCalc_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseAccess = DatabaseAccess.getInstance(getApplicationContext());//to get instances from the database
                databaseAccess.open();//opens the database connection
                // the below if statement is to check if none of the radiobuttons for gender is not checked or any of the text input fields is empty or none of the age radiobuttons are not checked
                // or if there is an empty field which a user must put values in  and should not be left unfilled
                if((!rbMale_h.isChecked()&&!rbFemale_h.isChecked())||(etB_h.getText().toString().equals(""))||
                        (etC_h.getText().toString().equals(""))||(!rbweeks_h.isChecked()&&!rbmonths_h.isChecked())||
                        (etAge_h.getText().toString().equals(""))){


                    Toast.makeText(getApplicationContext(),"ENTER ALL FIELDS !!!",Toast.LENGTH_LONG).show();//makes a pop up message
                }
                else if ((rbMale_h.isChecked()&& rbweeks_h.isChecked()))//if statement checks whether gender male radiobutton is checked and age radiobutton weeks is cheked
                {
                    maleHightWeeks();//this method is called which does the calculation for boys weight per age in weeks  growth rate and displays the result on another layout
                }
                else if((rbMale_h.isChecked()&& rbmonths_h.isChecked())){//if statement checks whether gender male radiobutton is checked and age radiobutton months is checked
                    maleHightMonths();//this method is called which does the calculation for boys weight per age in months  growth rate and displays the result on another layout
                }
                else if(rbFemale_h.isChecked()&& rbmonths_h.isChecked()){//if statement checks whether gender Female radiobutton is checked and age radiobutton months is checked
                    femaleHightMonths();//this method is called which does the calculation for girls weight per age in months growth rate  and displays the result on another layout
                }

                else if (rbFemale_h.isChecked()&&rbweeks_h.isChecked()){//if statement checks whether gender Female radiobutton is checked and age radiobutton weeks is checked
                    femaleHightWeeks();//this method is called which does the calculation for girls weight per age in weeks  growth rate and displays the result on another layout
                }

                else {

                    Toast.makeText(getApplicationContext()," ERORR TRY AGAIN!!!!!!!!!!!",Toast.LENGTH_LONG).show();// makes pop up message
                }

            }
        });
    }
   public void graphGirlsH()//a method to draw a WHO  growth chart for boys (displaying 3%,50% and 97% growth rate standards)
   {
       DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());//gets database instances
       databaseAccess.open();//opens the database
       GraphView graph2g = (GraphView) findViewById(R.id.graph);// gets the id of the graph in a view(from the xml layout_result using this method
       LineGraphSeries<DataPoint> series = new LineGraphSeries<>(databaseAccess.getDatahmg3p());//used to draw line graph for 3 percent growth rate and gets the data points from the method getDatahmg3p() and gets the datapoints(who standard values hight per age month) from the database
       graph2g.addSeries(series);//adds the datapoints series to the line graph
       series.setTitle("3%");//sets the line graph title
       series.setColor(Color.RED);//sets the line graph color
       //series.setDrawDataPoints(true);

       LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(databaseAccess.getDatahmg15p());//used to draw line graph for 3 percent growth rate and gets the datapoints(who standard values hight  per age in month) from the database through the method getDatahmg15p
       graph2g.addSeries(series2);//adds the datapoints series to the line graph
       series2.setTitle("15%");//sets the line graph title
       series2.setColor(Color.LTGRAY);//sets the line graph color
       //series2.setDrawDataPoints(true);

       LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>(databaseAccess.getDatahmg50p());//used to draw line graph for 50 percent growth rate and gets the datapoints(who standard values hight per age) from the database through the method getDatahmg50p
       graph2g.addSeries((series3));;//adds the datapoints series to the line graph
       series3.setColor(Color.GREEN);//sets the line graph color
       series3.setTitle(" 50%");//sets the line graph title
       //series3.setDrawDataPoints(true);
       series3.setOnDataPointTapListener(new OnDataPointTapListener() {
           @Override
           public void onTap(Series series, DataPointInterface dataPoint) {
               Toast.makeText(getApplicationContext(),dataPoint.toString(),Toast.LENGTH_LONG).show();
           }
       });

       LineGraphSeries<DataPoint> series85 = new LineGraphSeries<>(databaseAccess.getDatahmg85p());//used to draw line graph for 85 percent growth rate and gets the datapoints(who standard values hight  per age in months) from the database through the method getDatahmg85p()
       graph2g.addSeries(series85);//adds the datapoints series to the line graph
       series85.setTitle("85%");//sets the line graph title
       series85.setColor(Color.MAGENTA);//sets the line graph color
       //series85.setDrawDataPoints(true);

       LineGraphSeries<DataPoint> series5 = new LineGraphSeries<DataPoint>(databaseAccess.getDatahmg97p());//used to draw line graph for 97 percent growth rate and gets the datapoints(who standard values hight per age in months) from the database through the method getDatahmg97p()
       graph2g.addSeries((series5));//adds the datapoints series to the line graph
       series5.setColor(Color.BLUE);//sets the line graph color
       series5.setTitle(" 97%");//sets the line graph title
       //series5.setDrawDataPoints(true);

       graph2g.getLegendRenderer().setVisible(true);//displays the line graph chart's legend
       graph2g.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);//sets the legend's position to the bottom
       graph2g.getViewport().setMinX(0);//sets minimum x-axis to 0
       graph2g.getViewport().setMaxX(15);//sets maximum x axis to 14
       graph2g.getViewport().setMinY(40);//sets mimimum y axis to 0
       graph2g.getViewport().setMaxY(80);//sets maximum y axis
       graph2g.setTitle("WHO hight_for_age percentiles for Girls ");//sets graph title
       graph2g.getViewport().setYAxisBoundsManual(true);//allows to set manual y bounds
       graph2g.getViewport().setXAxisBoundsManual(true);//allows to set manual x bounds
       GridLabelRenderer gridLabel = graph2g.getGridLabelRenderer();//is responsible for generating vertical and horizontal lables and the grid lines
       gridLabel.setHorizontalAxisTitle("Age(months)"); //sets x axis title
       gridLabel.setVerticalAxisTitle(" Hight(Cm)"); // sets y axis title
   }
    public void graphH()
        //a method to draw a WHO  growth chart for boys  Hight vs age in months (displaying 3%,50% and 97% growth rate standards)
        {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());//gets database instances
            databaseAccess.open();//opens the database
            GraphView graph = (GraphView) findViewById(R.id.graph);// gets the id of the graph in a view(from the xml layout_result using this method
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(databaseAccess.getDatahmb3p());//used to draw line graph for 3 percent growth rate and gets the data points from the method getDatahmb3p() and gets the datapoints(who standard values hight per age month) from the database
            graph.addSeries(series);//adds the datapoints series to the line graph
            series.setTitle("3%");//sets the line graph title
            series.setColor(Color.RED);//sets the line graph color
            //series.setDrawDataPoints(true);

            LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(databaseAccess.getDatahmb15p());//used to draw line graph for 3 percent growth rate and gets the datapoints(who standard values hight  per age in month) from the database through the method getDatahmb15p
            graph.addSeries(series2);//adds the datapoints series to the line graph
            series2.setTitle("15%");//sets the line graph title
            series2.setColor(Color.LTGRAY);//sets the line graph color
            //series2.setDrawDataPoints(true);

            LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>(databaseAccess.getDatahmb50p());  //used to draw line graph for 50 percent growth rate and gets the datapoints(who standard values hight per age) from the database through the method getDatahmb50p
            graph.addSeries(series3);    ; //adds the datapoints series to the line graph
            series3.setColor(Color.GREEN);//sets the line graph color
            series3.setTitle(" 50%");//sets the line graph title
            //series3.setDrawDataPoints(true);
            series3.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(getApplicationContext(),dataPoint.toString(),Toast.LENGTH_LONG).show();
                }
            });

            LineGraphSeries<DataPoint> series85 = new LineGraphSeries<>(databaseAccess.getDatahmb85p());//used to draw line graph for 85 percent growth rate and gets the datapoints(who standard values hight  per age in months) from the database through the method getDatahmb85p
            graph.addSeries(series85);//adds the datapoints series to the line graph
            series85.setTitle("85%");//sets the line graph title
            series85.setColor(Color.MAGENTA);//sets the line graph color
            //series85.setDrawDataPoints(true);

            LineGraphSeries<DataPoint> series5 = new LineGraphSeries<DataPoint>(databaseAccess.getDatahmb97p());//used to draw line graph for 97 percent growth rate and gets the datapoints(who standard values hight per age in months) from the database through the method getDatahmb97p
            graph.addSeries((series5));//adds the datapoints series to the line graph
            series5.setColor(Color.BLUE);//sets the line graph color
            series5.setTitle(" 97%");//sets the line graph title
            //series5.setDrawDataPoints(true);

            graph.getLegendRenderer().setVisible(true);//displays the line graph chart's legend
            graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);//sets the legend's position to the bottom
            graph.getViewport().setMinX(0);//sets minimum x-axis to 0
            graph.getViewport().setMaxX(15);//sets maximum x axis to 14
            graph.getViewport().setMinY(40);//sets mimimum y axis to 0
            graph.getViewport().setMaxY(85);//sets maximum y axis
            graph.setTitle("WHO Hight_for_age percentiles for Boys ");//sets graph title
            graph.getViewport().setYAxisBoundsManual(true);//allows to set manual y bounds
            graph.getViewport().setXAxisBoundsManual(true);//allows to set manual x bounds
            GridLabelRenderer gridLabel = graph.getGridLabelRenderer();//is responsible for generating vertical and horizontal lables and the grid lines
            gridLabel.setHorizontalAxisTitle("Age(months)"); //sets x axis title
            gridLabel.setVerticalAxisTitle("Hight(Cm)"); // sets y axis title
    }
    public void  maleHightWeeks()//this method does the calculation for boys weight per age in weeks  growth rate and displays the result on another layout
    {
            {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());////gets database instances
                databaseAccess.open();// opens the database connection
                etAge_h =  findViewById(R.id.et_Hage );;// gets the id of the editText_wAge(from the xml layout using this method
                BH = Double.parseDouble(etB_h.getText().toString());//BW is birth_weight changes the string value of user input  to double
                CH = Double.parseDouble(etC_h.getText().toString());//CW is current_weight changes the string value of user input to double

                Age_week = etAge_h.getText().toString();//get text from user input in age text editer
                Age_w = Integer.valueOf(Age_week);//convert the string  value of age_week in to integer
                if(Age_w<=13)//if age given in the input is less than or equal to 13 weeks
                {

                    String p1st = databaseAccess.getHwb1p(Age_week);//we used the getBhw1p method to get 1%
                    String p3rd = databaseAccess.getHwb3p(Age_week);//we used thegetgetBhw3p method to get 3%
                    String p15th = databaseAccess.getHwb15P(Age_week);//we used the getBhw15P method to get 15%
                    String p50th = databaseAccess.getHwb50p(Age_week);//we used the getBhw50p method to get 50%
                    String p85th = databaseAccess.getHwb85p(Age_week);//we used the getBhw85p method to get 85%
                    String p97th = databaseAccess.getHwb97p(Age_week);//we used the getBhw97p method to get 97%
                    p1 = Double.parseDouble(p1st);//changes the string value of 1st % weight in kg to double type
                    p3 = Double.parseDouble(p3rd);//changes the string value of 3% weight in kg to double type
                    p15 = Double.parseDouble(p15th);//changes the string value of 15% weight in kg to double type
                    p50 = Double.parseDouble(p50th);//changes the string value of 50% weight in kg to double type
                    p85 = Double.parseDouble(p85th);//changes the string value of 85% weight in kg to double type
                    p97 = Double.parseDouble(p97th);//changes the string value of 97% weight in kg to double type
                    switch (Age_week) {
                        case "0": {//case 0 is when age is given 0 week
                            if (BH <= p1)//if BH Hight input is less than 1%
                            {
                                setContentView(R.layout.activity_result);//sets the content view to another layout the results will be displayed on activity_result layout.
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2
                                tv.setText(" growth rate is : 0 %" );//sets text to the textviw tv
                                GraphView gh = findViewById(R.id.graph);//gets the id for graph
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {//creates line graph with datapoint series
                                        new DataPoint(Age_w, BH)});//gets the datapoint x,y  values from user input age and birth hight
                                gh.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//sets title for the result
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setDataPointsRadius(10);//sets dataPoint radius to 10
                                series4.setThickness(8);//sets dataPoint thickness to 8
                                graphH();//WHO Boys Hight to age growth  chart
                            }
                            else if (BH >p1 &&BH <= p3)//if BH birth hight input is greater  than 1% and less than or equal to 3 %
                            {
                                AG = (BH - p1);// AG Average gain is equal to BH mines the value of 1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the value of  3% minus the value fo 1%
                                GPW = (AG) / AGPW;// GPW gain per week rate is equal to the ratio of  calculated Average gain per  standard Average gain per week
                                PercentH = (GPW * 0.02)+0.01; // Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text to textview tv

                                GraphView gh = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {//creates line graph with datapoint series
                                        new DataPoint(Age_w, BH)});//gets the datapoint x,y  values from user input age and birth hight
                                gh.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//sets title for the result
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO boys weight to age growth  chart
                            }

                            else if (BH >  p3 && BH <= p15)//if BH(birth hight)  input is greater  than 3% and less than or equal to 15 %
                            {
                                AG = (BH - p3);// AG Average gain is equal to BH minus the value of 3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG)/AGPW; // AGPW;GPW gain per week rate is equal to the ratio of  calculated Average gain per  standard Average gain per week
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                GraphView gh = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_w, BH)});//gets the datapoint x,y  values from user input age and birth hight
                                gh.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO boys Hight to age growth  chart
                            }
                            else if (BH > p15 && BH <= p50)//if BH(birth hight)  input is greater  than 15% and less than or equal to 50 %
                            {
                                AG = (BH - p15);// AG Average gain is equal to BH minus WHO standard value of  value of 15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain per  standard Average gain per week
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                GraphView gh = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_w, BH)});//gets the datapoint x,y  values from user input age and birth hight
                                gh.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO boys Hight to age growth  chart

                            }
                            else if (BH > p50 && BH <=p85)//if BH(birth hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (BH - p50);// AG Average gain is equal to BH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                GraphView gh = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_w, BH)});//gets the datapoint x,y  values from user input age and birth_hight
                                gh.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO boys hight to age growth  chart
                            }
                            else if (BH > p85 && BH <=p97)//if BH(birth hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (BH - p85);// AG Average gain is equal to BH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                GraphView gh = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_w, BH)});//gets the datapoint x,y  values from user input age and birth_hight
                                gh.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO boys hight to age growth  chart
                            }
                            else {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//seta text to textview tv
                                GraphView gh = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_w, BH)});//gets the datapoint x,y  values from user input age and birth_hight
                                gh.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("100%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO boys hight to age growth  chart
                            }
                        }
                        break;
                        case "1": //case 1 is when the given age is 1 week
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO boys hight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO boys hight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO boys hight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO boys hight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO boys hight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO boys hight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(0, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO boys hight to age growth  chart
                            }
                        }

                        break;
                        case "2":  //case 2 is when the given age is 2 weekS
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO boys hight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO boys hight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO boys hight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO girls hight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO boys hight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO boys hight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(0, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO boys hight to age growth  chart
                            }
                        }
                        break;
                        case "3":  //case 3 is when the given age is 3 weekS
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO boys hight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO boys hight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO boys hight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO boys hight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO boys hight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO boys hight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(0, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO boys hight to age growth  chart
                            }
                        }

                        break;
                        case "4":  //case 4 is when the given age is 4 weekS
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {

                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(0, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                        }
                        break;
                        case "5":  //case 1 is when the given age is 1 week
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO  BOYS hight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(1, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                        }
                        break;
                        case "6":  //case 6 is when the given age is 6 weeks
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(1, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                        }
                        break;
                        case "7": //case 7 is when the given age is 7 weekS
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {                                   new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(1, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                        }
                        break;
                        case "8": //case 8 is when the given age is 8 weekS
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1  because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %");//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(1, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                        }
                        break;
                        case "9":  //case 9 is when the given age is 9 weekS
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(2, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                        }
                        break;
                        case "10": //case 10 is when the given age is 10 weekS
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(2, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                        }
                        break;
                        case "11": //case 11 is when the given age is 11 weekS
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(2, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                        }
                        break;
                        case "12": //case 12 is when the given age is 12 weekS
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO BOYS  weight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(2, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                        }
                        break;
                        case "13": //case 13 is when the given age is 13 weekS
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(3, CH)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(3, CH)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(3, CH)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(3, CH)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(3, CH)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(3, CH)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(3, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
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
        }

    public void  maleHightMonths()//this method does the calculation for boys hight  per age in months  growth rate and displays the result on another layout
    {
            {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());////gets database instances
                databaseAccess.open();// opens the database connection
                etAge_h =  findViewById(R.id.et_Hage);;// gets the id of the editText_wAge(from the xml layout using this method
                BH = Double.parseDouble(etB_h.getText().toString());//BW is birth_weight changes the string value of user input  to double
                CH = Double.parseDouble(etC_h.getText().toString());//CW is current_weight changes the string value of user input to double

                Age_month = etAge_h.getText().toString();//get text from user input in age text editer
                Age_m = Integer.valueOf(Age_month);//convert the string  value of age_month in to integer
                if(Age_m<=13)//if age given in the input is less than or equal to 13 weeks
                {

                    String p1st = databaseAccess.getBhm1p(Age_month);//we used the getBhm1p method to get 1%
                    String p3rd = databaseAccess.getBhm3p(Age_month);//we used thegetgetBhm3p method to get 3%
                    String p15th = databaseAccess.getBhm15p(Age_month);//we used the getBhm15P method to get 15%
                    String p50th = databaseAccess.getBhm50p(Age_month);//we used the getBhm50p method to get 50%
                    String p85th = databaseAccess.getBhm85p(Age_month);//we used the getBhm85p method to get 85%
                    String p97th = databaseAccess.getBhm97p(Age_month);//we used the getBhm97p method to get 97%
                    p1 = Double.parseDouble(p1st);//changes the string value of 1st % weight in kg to double type
                    p3 = Double.parseDouble(p3rd);//changes the string value of 3% weight in kg to double type
                    p15 = Double.parseDouble(p15th);//changes the string value of 15% weight in kg to double type
                    p50 = Double.parseDouble(p50th);//changes the string value of 50% weight in kg to double type
                    p85 = Double.parseDouble(p85th);//changes the string value of 85% weight in kg to double type
                    p97 = Double.parseDouble(p97th);//changes the string value of 97% weight in kg to double type
                    switch (Age_month) {
                        case "0": {//case 0 is when age is given 0
                            if (BH <= p1)//if BH Hight input is less than 1%
                            {
                                setContentView(R.layout.activity_result);//sets the content view to another layout the results will be displayed on activity_result layout.
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2
                                tv.setText(" growth rate is : 0 %" );//sets text to the textviw tv
                                GraphView g = findViewById(R.id.graph);//gets the id for graph
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {//creates line graph with datapoint series
                                        new DataPoint(Age_m, BH)});//gets the datapoint x,y  values from user input age and birth hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//sets title for the result
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setDataPointsRadius(10);//sets dataPoint radius to 10
                                series4.setThickness(8);//sets dataPoint thickness to 8
                                graphH();//WHO Boys Hight to age growth  chart
                            }
                            else if (BH >p1 &&BH <= p3)//if BH birth hight input is greater  than 1% and less than or equal to 3 %
                            {
                                AG = (BH - p1);// AG Average gain is equal to BH mines the value of 1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the value of  3% minus the value fo 1%
                                GPW = (AG) / AGPW;// GPW gain per month rate is equal to the ratio of  calculated Average gain per  standard Average gain per month
                                PercentH = (GPW * 0.02)+0.01; // Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {//creates line graph with datapoint series
                                        new DataPoint(Age_m, BH)});//gets the datapoint x,y  values from user input age and birth hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//sets title for the result
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO boys weight to age growth  chart
                            }

                            else if (BH >  p3 && BH <= p15)//if BH(birth hight)  input is greater  than 3% and less than or equal to 15 %
                            {
                                AG = (BH - p3);// AG Average gain is equal to BH minus the value of 3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG)/AGPW; // AGPW;GPW gain per month rate is equal to the ratio of  calculated Average gain per  standard Average gain per month
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, BH)});//gets the datapoint x,y  values from user input age and birth hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO boys Hight to age growth  chart
                            }
                            else if (BH > p15 && BH <= p50)//if BH(birth hight)  input is greater  than 15% and less than or equal to 50 %
                            {
                                AG = (BH - p15);// AG Average gain is equal to BH minus WHO standard value of  value of 15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain per  standard Average gain per month
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, BH)});//gets the datapoint x,y  values from user input age and birth hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO boys Hight to age growth  chart

                            }
                            else if (BH > p50 && BH <=p85)//if BH(birth hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (BH - p50);// AG Average gain is equal to BH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, BH)});//gets the datapoint x,y  values from user input age and birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO boys hight to age growth  chart
                            }
                            else if (BH > p85 && BH <=p97)//if BH(birth hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (BH - p85);// AG Average gain is equal to BH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, BH)});//gets the datapoint x,y  values from user input age and birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO boys hight to age growth  chart
                            }
                            else {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, BH)});//gets the datapoint x,y  values from user input age and birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("100%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO boys hight to age growth  chart
                            }
                        }
                        break;
                        case "1": //case 1 is when the given age is 1 month
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value is given user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO boys hight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value is user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO boys weight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value is set to user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO boys weight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value is set to user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO boys weight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value is set to user input age,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO boys weight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO boys weight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO boys weight to age growth  chart
                            }
                        }

                        break;
                        case "2":  //case 2 is when the given age is 2 months
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO boys weight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO boys weight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO boys hight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO boys hight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO boys hight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO boys weight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO boys hight to age growth  chart
                            }
                        }
                        break;
                        case "3":  //case 3 is when the given age is 3 months
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO girls weight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO boys hight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user imput age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO boys weight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO boys weight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO boys weight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO boys weight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO boys hight to age growth  chart
                            }
                        }

                        break;
                        case "4":  //case 4 is when the given age is 4 MONTHS
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {

                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per month  equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per month  rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                        }
                        break;
                        case "5":  //case 1 is when the given age is 1 week
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per month  equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per month  rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO  BOYS hight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value form user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight  to age growth  chart
                            }
                        }
                        break;
                        case "6":  //case 6 is when the given age is 6 months
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                        }
                        break;
                        case "7": //case 7 is when the given age is 7 months
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {                                   new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hightto age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS weight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                        }
                        break;
                        case "8": //case 8 is when the given age is 8 months
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                        }
                        break;
                        case "9":  //case 9 is when the given age is 9 months
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value is from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value is from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value is from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                        }
                        break;
                        case "10": //case 10 is when the given age is 10 weekS
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per month  equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight  to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per month  equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value is from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value is from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value is from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input  age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value is from user input  age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                        }
                        break;
                        case "11": //case 11 is when the given age is 11 months
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value is from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value is from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value fromu user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                        }
                        break;
                        case "12": //case 12 is when the given age is 12 month
                        {
                            if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                            {
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setTitle("0%");//seta text to textview tv
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                graphH();//WHO BOYS  hight to age growth  chart
                            }
                            else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                            {
                                AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                                AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }

                            else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                            {
                                AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                                AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                                GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                            {
                                AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                                AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                                graphH();//WHO BOYS hight to age growth  chart

                            }
                            else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                            {
                                AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                                AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value fromuser input age  ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                            {
                                AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                                AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                                GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                                PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                        {
                                                new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                            else {
                                //when the given CH is greater than WHO standard value of 97%
                                setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                                tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                                GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                                LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                        new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                                //gets the datapoint x value is from user input age ,y  values from user input  birth_hight
                                g.addSeries(series4);//add the data point series in the graph
                                series4.setColor(Color.BLACK);//sets the  line graph color to black
                                series4.setDrawDataPoints(true);//shows datapoints on the graph
                                series4.setTitle("100%");//set a text using string value of the result to textview tv
                                graphH();//WHO BOYS hight to age growth  chart
                            }
                        }
                        break;

                    }
                }

                else{

                    Toast.makeText(getApplicationContext(),"The app is valid for Age up to 12 months ",Toast.LENGTH_LONG).show();//pop up message
                }
                databaseAccess.close();//database connection closed
            }
        }

    public void femaleHightMonths(){
        //this method does the calculation for girls hight  per age in months  growth rate and displays the result on another layout
        {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());////gets database instances
            databaseAccess.open();// opens the database connection
            etAge_h =  findViewById(R.id.et_Hage);;// gets the id of the editText_wAge(from the xml layout using this method
            BH = Double.parseDouble(etB_h.getText().toString());//BW is birth_weight changes the string value of user input  to double
            CH = Double.parseDouble(etC_h.getText().toString());//CW is current_weight changes the string value of user input to double

            Age_month = etAge_h.getText().toString();//get text from user input in age text editer
            Age_m = Integer.valueOf(Age_month);//convert the string  value of age_week in to integer
            if(Age_m<=13)//if age given in the input is less than or equal to 13 weeks
            {

                String p1st = databaseAccess.getGhm1p(Age_month);//we used the getGhm1p method to get 1%
                String p3rd = databaseAccess.getGhm3p(Age_month);//we used the getGhm3p method to get 3%
                String p15th = databaseAccess.getGhm15p(Age_month);//we used the getGhm15p method to get 15%
                String p50th = databaseAccess.getGhm50p(Age_month);//we used the getGhm50p method to get 50%
                String p85th = databaseAccess.getGhm85p(Age_month);//we used the getGhm85p method to get 85%
                String p97th = databaseAccess.getGhm97p(Age_month);//we used the getGhm97p method to get 97%
                p1 = Double.parseDouble(p1st);//changes the string value of 1st % weight in kg to double type
                p3 = Double.parseDouble(p3rd);//changes the string value of 3% weight in kg to double type
                p15 = Double.parseDouble(p15th);//changes the string value of 15% weight in kg to double type
                p50 = Double.parseDouble(p50th);//changes the string value of 50% weight in kg to double type
                p85 = Double.parseDouble(p85th);//changes the string value of 85% weight in kg to double type
                p97 = Double.parseDouble(p97th);//changes the string value of 97% weight in kg to double type
                switch (Age_month) {
                    case "0": {//case 0 is when age is given 0
                        if (BH <= p1)//if BH Hight input is less than 1%
                        {
                            setContentView(R.layout.activity_result);//sets the content view to another layout the results will be displayed on activity_result layout.
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2
                            tv.setText(" growth rate is : 0 %" );//sets text to the textviw tv
                            GraphView g = findViewById(R.id.graph);//gets the id for graph
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {//creates line graph with datapoint series
                                    new DataPoint(Age_m, BH)});//gets the datapoint x,y  values from user input age and birth hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//sets title for the result
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setDataPointsRadius(10);//sets dataPoint radius to 10
                            series4.setThickness(8);//sets dataPoint thickness to 8
                            graphGirlsH();//WHO girls Hight to age growth  chart
                        }
                        else if (BH >p1 &&BH <= p3)//if BH birth hight input is greater  than 1% and less than or equal to 3 %
                        {
                            AG = (BH - p1);// AG Average gain is equal to BH mines the value of 1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the value of  3% minus the value fo 1%
                            GPW = (AG) / AGPW;// GPW gain per month rate is equal to the ratio of  calculated Average gain per  standard Average gain per month
                            PercentH = (GPW * 0.02)+0.01; // Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {//creates line graph with datapoint series
                                    new DataPoint(Age_m, BH)});//gets the datapoint x,y  values from user input age and birth hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//sets title for the result
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsH();//WHO girls weight to age growth  chart
                        }

                        else if (BH >  p3 && BH <= p15)//if BH(birth hight)  input is greater  than 3% and less than or equal to 15 %
                        {
                            AG = (BH - p3);// AG Average gain is equal to BH minus the value of 3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG)/AGPW; // AGPW;GPW gain per month rate is equal to the ratio of  calculated Average gain per  standard Average gain per month
                            PercentH = (GPW * 0.12)+0.03;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, BH)});//gets the datapoint x,y  values from user input age and birth hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsH();//WHO girls Hight to age growth  chart
                        }
                        else if (BH > p15 && BH <= p50)//if BH(birth hight)  input is greater  than 15% and less than or equal to 50 %
                        {
                            AG = (BH - p15);// AG Average gain is equal to BH minus WHO standard value of  value of 15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain per  standard Average gain per month
                            PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, BH)});//gets the datapoint x,y  values from user input age and birth hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsH();//WHO girls Hight to age growth  chart

                        }
                        else if (BH > p50 && BH <=p85)//if BH(birth hight)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (BH - p50);// AG Average gain is equal to BH minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, BH)});//gets the datapoint x,y  values from user input age and birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (BH > p85 && BH <=p97)//if BH(birth hight)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (BH - p85);// AG Average gain is equal to BH minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, BH)});//gets the datapoint x,y  values from user input age and birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, BH)});//gets the datapoint x,y  values from user input age and birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("100%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                    }
                    break;
                    case "1": //case 1 is when the given age is 1 month
                    {
                        if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value is given user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value is user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }

                        else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value is set to user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value is set to user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart

                        }
                        else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value is set to user input age,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight  to age growth  chart
                        }
                        else {
                            //when the given CH is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight  to age growth  chart
                        }
                    }

                    break;
                    case "2":  //case 2 is when the given age is 2 months
                    {
                        if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsH();//WHO girls hight  to age growth  chart
                        }
                        else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }

                        else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart

                        }
                        else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO boys hight to age growth  chart
                        }
                        else {
                            //when the given CH is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                    }
                    break;
                    case "3":  //case 3 is when the given age is 3 months
                    {
                        if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }

                        else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user imput age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart

                        }
                        else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else {
                            //when the given CH is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                    }

                    break;
                    case "4":  //case 4 is when the given age is 4 MONTHS
                    {
                        if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {

                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls weight to age growth  chart
                        }

                        else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month  equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month  rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart

                        }
                        else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else {
                            //when the given CH is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                    }
                    break;
                    case "5":  //case 5 is when the given age is 5 months
                    {
                        if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsH();//WHO gight hight to age growth  chart
                        }
                        else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }

                        else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month  equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month  rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls weight to age growth  chart
                        }
                        else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart

                        }
                        else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO  girls hight to age growth  chart
                        }
                        else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else {
                            //when the given CH is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                            //gets the datapoint x value form user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight  to age growth  chart
                        }
                    }
                    break;
                    case "6":  //case 6 is when the given age is 6 months
                    {
                        if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls weight to age growth  chart
                        }

                        else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart

                        }
                        else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else {
                            //when the given CH is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                    }
                    break;
                    case "7": //case 7 is when the given age is 7 months
                    {
                        if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }

                        else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls weight to age growth  chart
                        }
                        else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {                                   new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hightto age growth  chart

                        }
                        else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls weight to age growth  chart
                        }
                        else {
                            //when the given CH is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                    }
                    break;
                    case "8": //case 8 is when the given age is 8 months
                    {
                        if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }

                        else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO gight hight to age growth  chart

                        }
                        else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else {
                            //when the given CH is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                    }
                    break;
                    case "9":  //case 9 is when the given age is 9 months
                    {
                        if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }

                        else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value is from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart

                        }
                        else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value is from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else {
                            //when the given CH is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                    }
                    break;
                    case "10": //case 10 is when the given age is 10 months
                    {
                        if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month  equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight  to age growth  chart
                        }

                        else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month  equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value is from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value is from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart

                        }
                        else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value is from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input  age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else {
                            //when the given CH is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is from user input  age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                    }
                    break;
                    case "11": //case 11 is when the given age is 11 months
                    {
                        if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value is from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }

                        else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value is from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart

                        }
                        else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else {
                            //when the given CH is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                            //gets the datapoint x value fromu user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                    }
                    break;
                    case "12": //case 12 is when the given age is 12 month
                    {
                        if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsH();//WHO girls  hight to age growth  chart
                        }
                        else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }

                        else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart

                        }
                        else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value fromuser input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CH)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                        else {
                            //when the given CH is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CH)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsH();//WHO girls hight to age growth  chart
                        }
                    }
                    break;

                }
            }

            else{

                Toast.makeText(getApplicationContext(),"The app is valid for Age up to 12 months ",Toast.LENGTH_LONG).show();//pop up message
            }
            databaseAccess.close();//database connection closed
        }
    }
    public void femaleHightWeeks(){
        //this method does the calculation for girls hoght per age percentile in weeks  growth rate and displays the result with WHO growth rate chart on another layout
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());////gets database instances
            databaseAccess.open();// opens the database connection
            etAge_h =  findViewById(R.id.et_Hage);// gets the id of the editText_wAge(from the xml layout using this method
            BH = Double.parseDouble(etB_h.getText().toString());//BW is birth_weight changes the string value of user input  to double
            CH = Double.parseDouble(etC_h.getText().toString());//CW is current_weight changes the string value of user input to double

            Age_week = etAge_h.getText().toString();//get text from user input in age text editer
            Age_w = Integer.valueOf(Age_week);//convert the string  value of age_week in to integer

        if(Age_w<=13)//if age given in the input is less than or equal to 13 weeks
        {

            String p1st = databaseAccess.getGhw1p(Age_week);//we used the getGhw1p method to get 1%
            String p3rd = databaseAccess.getGhw3p(Age_week);//we used thegetgetGhw3p method to get 3%
            String p15th = databaseAccess.getGhw15p(Age_week);//we used the getGhw15P method to get 15%
            String p50th = databaseAccess.getGhw50p(Age_week);//we used the getGhw50p method to get 50%
            String p85th = databaseAccess.getGhw85p(Age_week);//we used the getGhw85p method to get 85%
            String p97th = databaseAccess.getGhw97p(Age_week);//we used the getGhw97p method to get 97%
            p1 = Double.parseDouble(p1st);//changes the string value of 1st % weight in kg to double type
            p3 = Double.parseDouble(p3rd);//changes the string value of 3% weight in kg to double type
            p15 = Double.parseDouble(p15th);//changes the string value of 15% weight in kg to double type
            p50 = Double.parseDouble(p50th);//changes the string value of 50% weight in kg to double type
            p85 = Double.parseDouble(p85th);//changes the string value of 85% weight in kg to double type
            p97 = Double.parseDouble(p97th);//changes the string value of 97% weight in kg to double type
            switch (Age_week) {
                case "0": {//case 0 is when age is given 0 week
                    if (BH <= p1)//if BH Hight input is less than 1%
                    {
                        setContentView(R.layout.activity_result);//sets the content view to another layout the results will be displayed on activity_result layout.
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2
                        tv.setText(" growth rate is : 0 %" );//sets text to the textviw tv
                        GraphView g = findViewById(R.id.graph);//gets the id for graph
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {//creates line graph with datapoint series
                                new DataPoint(Age_w, BH)});//gets the datapoint x,y  values from user input age and birth hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//sets title for the result
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setDataPointsRadius(10);//sets dataPoint radius to 10
                        series4.setThickness(8);//sets dataPoint thickness to 8
                        graphGirlsH();//WHO girls Hight to age growth  chart
                    }
                    else if (BH >p1 &&BH <= p3)//if BH birth hight input is greater  than 1% and less than or equal to 3 %
                    {
                        AG = (BH - p1);// AG Average gain is equal to BH mines the value of 1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the value of  3% minus the value fo 1%
                        GPW = (AG) / AGPW;// GPW gain per week rate is equal to the ratio of  calculated Average gain per  standard Average gain per week
                        PercentH = (GPW * 0.02)+0.01; // Percentile Hight result
                        setContentView(R.layout.activity_result); //sets content view to activity_result so that the result will be displayed on this other layout
                                TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {//creates line graph with datapoint series
                                new DataPoint(Age_w, BH)});//gets the datapoint x,y  values from user input age and birth hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//sets title for the result
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsH();//WHO girls hight to age growth  chart
                    }

                    else if (BH >  p3 && BH <= p15)//if BH(birth hight)  input is greater  than 3% and less than or equal to 15 %
                    {
                        AG = (BH - p3);// AG Average gain is equal to BH minus the value of 3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW; // AGPW;GPW gain per week rate is equal to the ratio of  calculated Average gain per  standard Average gain per week
                                PercentH = (GPW * 0.12)+0.03;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(Age_w, BH)});//gets the datapoint x,y  values from user input age and birth hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsH();//WHO girls Hight to age growth  chart
                    }
                    else if (BH > p15 && BH <= p50)//if BH(birth hight)  input is greater  than 15% and less than or equal to 50 %
                    {
                        AG = (BH - p15);// AG Average gain is equal to BH minus WHO standard value of  value of 15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain per  standard Average gain per week
                        PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(Age_w, BH)});//gets the datapoint x,y  values from user input age and birth hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsH();//WHO girls Hight to age growth  chart

                    }
                    else if (BH > p50 && BH <=p85)//if BH(birth hight)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (BH - p50);// AG Average gain is equal to BH minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(Age_w, BH)});//gets the datapoint x,y  values from user input age and birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsH();//WHO girls Hight to age growth  chart
                    }
                    else if (BH > p85 && BH <=p97)//if BH(birth hight)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (BH - p85);// AG Average gain is equal to BH minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(Age_w, BH)});//gets the datapoint x,y  values from user input age and birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsH();//WHO girls Hight to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(Age_w, BH)});//gets the datapoint x,y  values from user input age and birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsH();//WHO girls Hight to age growth  chart
                    }
                }
                break;
                case "1": //case 1 is when the given age is 1 week
                {
                    if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsH();//WHO girls Hight to age growth  chart
                    }
                    else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }

                    else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart

                    }
                    else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else {
                        //when the given CH is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CH)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                }

                break;
                case "2":  //case 2 is when the given age is 2 weekS
                {
                    if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }

                    else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart

                    }
                    else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else {
                        //when the given CH is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CH)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "3":  //case 3 is when the given age is 3 weekS
                {
                    if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }

                    else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart

                    }
                    else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else {
                        //when the given CH is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CH)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                }

                break;
                case "4":  //case 4 is when the given age is 4 weekS
                {
                    if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }

                    else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart

                    }
                    else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(0, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else {
                        //when the given CH is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CH)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "5":  //case 1 is when the given age is 1 week
                {
                    if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }

                    else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart

                    }
                    else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else {
                        //when the given CH is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CH)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "6":  //case 6 is when the given age is 6 weeks
                {
                    if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle("0%");//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }

                    else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart

                    }
                    else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else {
                        //when the given CH is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CH)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle("100%");//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "7": //case 7 is when the given age is 7 weekS
                {
                    if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle("0%");//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }

                    else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {                                   new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart

                    }
                    else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else {
                        //when the given CH is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CH)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle("100%");//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "8": //case 8 is when the given age is 8 weekS
                {
                    if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle("0%");//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }

                    else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1  because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart

                    }
                    else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(1, CH)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else {
                        //when the given CH is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CH)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle("100%");//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "9":  //case 9 is when the given age is 9 weekS
                {
                    if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle("0%");//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }

                    else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart

                    }
                    else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else {
                        //when the given CH is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CH)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle("100%");//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "10": //case 10 is when the given age is 10 weekS
                {
                    if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle("0%");//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }

                    else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart

                    }
                    else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else {
                        //when the given CH is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CH)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle("100%");//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "11": //case 11 is when the given age is 11 weekS
                {
                    if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle("0%");//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }

                    else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart

                    }
                    else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else {
                        //when the given CH is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CH)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle("100%");//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "12": //case 12 is when the given age is 12 weekS
                {
                    if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle("0%");//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }

                    else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart

                    }
                    else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(2, CH)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else {
                        //when the given CH is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %");//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CH)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle("100%");//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "13": //case 13 is when the given age is 13 weekS
                {
                    if (CH <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(3, CH)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle("0%");//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH >p1 &&CH <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CH - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.02)+0.01;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(3, CH)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }

                    else if (CH >  p3 && CH <= p15)//if CH(current_hight)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CH - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.03;// Percentile Hight result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(3, CH)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p15 && CH <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CH - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.15;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(3, CH)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//seta text to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart

                    }
                    else if (CH > p50 && CH <=p85)//if CH(current_hight)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CH - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.35)+0.5;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(3, CH)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else if (CH > p85 && CH <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CH - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentH = (GPW * 0.12)+0.85;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(3, CH)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentH)));//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
                    }
                    else {
                        //when the given CH is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(3, CH)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle("100%");//set a text using string value of the result to textview tv
                        graphGirlsH();//WHO girls weight to age growth  chart
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


}
