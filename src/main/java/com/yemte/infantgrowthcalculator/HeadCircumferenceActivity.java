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

public class HeadCircumferenceActivity extends AppCompatActivity {
    Button bCalc_hc;//button for calculate
    EditText etC_hc;//current hight input text editer
    EditText etB_hc;//birth hight input text editer
    EditText etAge_hc;//age input text editer
    RadioGroup rgGender_hc;//radio group for gender
    RadioButton rbMale_hc;//radio button for gender male
    RadioButton rbFemale_hc;//radio button for gender female
    RadioGroup rgAge_hc;//radio group for age
    RadioButton rbweeks_hc;//radio button for weeks
    RadioButton rbmonths_hc;//radio button for months
    String Age_week,Age_month;//Age_week,Age_month;
    Double BHC = 0.0;//birth Head circumference
    Double CHC = 0.0;//current Head circumference
    Double AG = 0.0;//average gain
    Double AGPW = 0.0;//average gain per week/month
    Double GPW = 0.0;//gain per week/month
    Double PercentHC = 1.0 ;//Head circumference increase percentile
    Double p1=0.0,p3=0.0,p15=0.0,p50=0.0,p85=0.0,p97=0.0;//1st percent,3rd percent,15th percent,50th percent,85th percent,97th percent
    int Age_w = 0;//age given in weeks
    int Age_m = 0;//age given in months

    DatabaseAccess databaseAccess;//declaring the databaseAccess
    DecimalFormat rate = new DecimalFormat(".#%");//formats the decimal point of the result.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_circumference);
        rgGender_hc =  findViewById(R.id.radiogroup_HCgender); //gets the id of the textView_R2 (from the layout_result  xml)
        etB_hc = findViewById(R.id.et_BirthHC);
        etC_hc = findViewById(R.id.et_currentHC);
        etAge_hc = findViewById(R.id.et_HCage);
        rbMale_hc =  findViewById(R.id.rb_HCmale);
        rbFemale_hc = findViewById(R.id.rb_HCfemale);
        bCalc_hc = findViewById(R.id.button_HC_calculate);
        rgAge_hc = findViewById(R.id.Rg_Age_HC);
        rbweeks_hc = findViewById(R.id.RBHC_weeks);
        rbmonths_hc =  findViewById(R.id.RBHC_months);
        bCalc_hc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseAccess = DatabaseAccess.getInstance(getApplicationContext());//to get instances from the database
                databaseAccess.open();//opens the database connection
                // the below if statement is to check if none of the radiobuttons for gender is not checked or any of the text input fields is empty or none of the age radiobuttons are not checked
                // or if there is an empty field which a user must put values in  and should not be left unfilled
                if((!rbMale_hc.isChecked()&&!rbFemale_hc.isChecked())||(etB_hc.getText().toString().equals(""))||
                        (etC_hc.getText().toString().equals(""))||(!rbweeks_hc.isChecked()&&!rbmonths_hc.isChecked())||
                        (etAge_hc.getText().toString().equals(""))){


                    Toast.makeText(getApplicationContext(),"ENTER ALL FIELDS !!!",Toast.LENGTH_LONG).show();//makes a pop up message
                }
                else if ((rbMale_hc.isChecked()&& rbweeks_hc.isChecked()))//if statement checks whether gender male radiobutton is checked and age radiobutton weeks is cheked
                {
                    maleHCWeeks();//this method is called which does the calculation for boys Head circumference per age in weeks  growth rate and displays the result on another layout
                }
                else if((rbMale_hc.isChecked()&& rbmonths_hc.isChecked())){//if statement checks whether gender male radiobutton is checked and age radiobutton months is checked
                    maleHCMonths();//this method is called which does the calculation for boys Head circumference per age in months  growth rate and displays the result on another layout
                }
                else if(rbFemale_hc.isChecked()&& rbmonths_hc.isChecked()){//if statement checks whether gender Female radiobutton is checked and age radiobutton months is checked
                    femaleHCMonths();//this method is called which does the calculation for girls head circumference per age in months growth rate  and displays the result on another layout
                }

                else if (rbFemale_hc.isChecked()&&rbweeks_hc.isChecked()){//if statement checks whether gender Female radiobutton is checked and age radiobutton weeks is checked
                    femaleHCWeeks();//this method is called which does the calculation for girls Head circumference per age in weeks  growth rate and displays the result on another layout
                }

                else {

                    Toast.makeText(getApplicationContext()," ERORR TRY AGAIN!!!!!!!!!!!",Toast.LENGTH_LONG).show();// makes pop up message
                }

            }
        });
    }
    public void graphGirlsHC()//a method to draw a WHO  growth chart head_circumference_for_age percentile for boys (displaying 3%, 15%,50%,85% and 97% growth rate standards)
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());//gets database instances
        databaseAccess.open();//opens the database
        GraphView graph2g = (GraphView) findViewById(R.id.graph);// gets the id of the graph in a view(from the xml layout_result using this method
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(databaseAccess.getDatahcmg3p());//used to draw line graph for 3 percent growth rate and gets the data points from the method getDatahmg3p() and gets the datapoints(who standard values hight per age month) from the database
        graph2g.addSeries(series);//adds the datapoints series to the line graph
        series.setTitle("3%");//sets the line graph title
        series.setColor(Color.RED);//sets the line graph color
        //series.setDrawDataPoints(true);

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(databaseAccess.getDatahcmg15p());//used to draw line graph for 3 percent growth rate and gets the datapoints(who standard values hight  per age in month) from the database through the method getDatahmg15p
        graph2g.addSeries(series2);//adds the datapoints series to the line graph
        series2.setTitle("15%");//sets the line graph title
        series2.setColor(Color.LTGRAY);//sets the line graph color
        //series2.setDrawDataPoints(true);

        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>(databaseAccess.getDatahcmg50p());//used to draw line graph for 50 percent growth rate and gets the datapoints(who standard values hight per age) from the database through the method getDatahmg50p
        graph2g.addSeries((series3));//adds the datapoints series to the line graph
        series3.setColor(Color.GREEN);//sets the line graph color
        series3.setTitle(" 50%");//sets the line graph title
        //series3.setDrawDataPoints(true);
        series3.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getApplicationContext(),dataPoint.toString(),Toast.LENGTH_LONG).show();
            }
        });

        LineGraphSeries<DataPoint> series85 = new LineGraphSeries<>(databaseAccess.getDatahcmg85p());//used to draw line graph for 85 percent growth rate and gets the datapoints(who standard values hight  per age in months) from the database through the method getDatahmg85p()
        graph2g.addSeries(series85);//adds the datapoints series to the line graph
        series85.setTitle("85%");//sets the line graph title
        series85.setColor(Color.MAGENTA);//sets the line graph color
        //series85.setDrawDataPoints(true);

        LineGraphSeries<DataPoint> series5 = new LineGraphSeries<DataPoint>(databaseAccess.getDatahcmg97p());//used to draw line graph for 97 percent growth rate and gets the datapoints(who standard values hight per age in months) from the database through the method getDatahmg97p()
        graph2g.addSeries((series5));//adds the datapoints series to the line graph
        series5.setColor(Color.BLUE);//sets the line graph color
        series5.setTitle(" 97%");//sets the line graph title
        //series5.setDrawDataPoints(true);

        graph2g.getLegendRenderer().setVisible(true);//displays the line graph chart's legend
        graph2g.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);//sets the legend's position to the bottom
        graph2g.getViewport().setMinX(0);//sets minimum x-axis to 0
        graph2g.getViewport().setMaxX(14);//sets maximum x axis to 14
        graph2g.getViewport().setMinY(30);//sets mimimum y axis to 0
        graph2g.getViewport().setMaxY(52);//sets maximum y axis
        graph2g.setTitle("WHO HeadCircumference_per_age percentile for Girls");//sets graph title
        graph2g.getViewport().setYAxisBoundsManual(true);//allows to set manual y bounds
        graph2g.getViewport().setXAxisBoundsManual(true);//allows to set manual x bounds
        GridLabelRenderer gridLabel = graph2g.getGridLabelRenderer();//is responsible for generating vertical and horizontal lables and the grid lines
        gridLabel.setHorizontalAxisTitle("Age(months)"); //sets x axis title
        gridLabel.setVerticalAxisTitle(" Head_Circumference(Cm)"); // sets y axis title
    }
    public void graphHC()
    //a method to draw a WHO  growth chart for boys  Hight vs age in months (displaying 3%,50% and 97% growth rate standards)
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());//gets database instances
        databaseAccess.open();//opens the database
        GraphView graph = (GraphView) findViewById(R.id.graph);// gets the id of the graph in a view(from the xml layout_result using this method
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(databaseAccess.getDatahcmb3p());//used to draw line graph for 3 percent growth rate and gets the data points from the method getDatahmb3p() and gets the datapoints(who standard values hight per age month) from the database
        graph.addSeries(series);//adds the datapoints series to the line graph
        series.setTitle("3%");//sets the line graph title
        series.setColor(Color.RED);//sets the line graph color
        //series.setDrawDataPoints(true);

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(databaseAccess.getDatahcmb15p());//used to draw line graph for 3 percent growth rate and gets the datapoints(who standard values hight  per age in month) from the database through the method getDatahmb15p
        graph.addSeries(series2);//adds the datapoints series to the line graph
        series2.setTitle("15%");//sets the line graph title
        series2.setColor(Color.LTGRAY);//sets the line graph color
        //series2.setDrawDataPoints(true);

        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>(databaseAccess.getDatahcmb50p());  //used to draw line graph for 50 percent growth rate and gets the datapoints(who standard values hight per age) from the database through the method getDatahmb50p
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

        LineGraphSeries<DataPoint> series85 = new LineGraphSeries<>(databaseAccess.getDatahcmb85p());//used to draw line graph for 85 percent growth rate and gets the datapoints(who standard values hight  per age in months) from the database through the method getDatahmb85p
        graph.addSeries(series85);//adds the datapoints series to the line graph
        series85.setTitle("85%");//sets the line graph title
        series85.setColor(Color.MAGENTA);//sets the line graph color
        //series85.setDrawDataPoints(true);

        LineGraphSeries<DataPoint> series5 = new LineGraphSeries<DataPoint>(databaseAccess.getDatahcmb97p());//used to draw line graph for 97 percent growth rate and gets the datapoints(who standard values hight per age in months) from the database through the method getDatahmb97p
        graph.addSeries((series5));//adds the datapoints series to the line graph
        series5.setColor(Color.BLUE);//sets the line graph color
        series5.setTitle(" 97%");//sets the line graph title
        //series5.setDrawDataPoints(true);

        graph.getLegendRenderer().setVisible(true);//displays the line graph chart's legend
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);//sets the legend's position to the bottom
        graph.getViewport().setMinX(0);//sets minimum x-axis to 0
        graph.getViewport().setMaxX(14);//sets maximum x axis to 14
        graph.getViewport().setMinY(30);//sets mimimum y axis to 0
        graph.getViewport().setMaxY(52);//sets maximum y axis
        graph.setTitle("WHO HeadCircumference_per_age percentile for Boys");//sets graph title
        graph.getViewport().setYAxisBoundsManual(true);//allows to set manual y bounds
        graph.getViewport().setXAxisBoundsManual(true);//allows to set manual x bounds
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();//is responsible for generating vertical and horizontal lables and the grid lines
        gridLabel.setHorizontalAxisTitle("Age(months)"); //sets x axis title
        gridLabel.setVerticalAxisTitle("Head_Circumference(Cm)"); // sets y axis title
    }
    public void  maleHCWeeks()//this method does the calculation for boys head_circumference per age in weeks  growth rate and displays the result on another layout
    {
        {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());////gets database instances
            databaseAccess.open();// opens the database connection
            etAge_hc =  findViewById(R.id.et_HCage );;// gets the id of the editText_HCage(from the xml layout using this method
            BHC = Double.parseDouble(etB_hc.getText().toString());//BHC is birth_head_circumference changes the string value of user input  to double
            CHC = Double.parseDouble(etC_hc.getText().toString());//CHC is current_head_circumfrrenece changes the string value of user input to double

            Age_week = etAge_hc.getText().toString();//get text from user input in age text editer
            Age_w = Integer.valueOf(Age_week);//convert the string  value of age_week in to integer
            if(Age_w<=13)//if age given in the input is less than or equal to 13 weeks
            {

                String p1st = databaseAccess.getBhcw1p(Age_week);//we used the getBhcw1p method to get 1%
                String p3rd = databaseAccess.getBhcw3p(Age_week);//we used the getBhcw3p method to get 3%
                String p15th = databaseAccess.getBhcw15P(Age_week);//we used the getBhcw15p method to get 15%
                String p50th = databaseAccess.getBhcw50p(Age_week);//we used the getBhcw50p method to get 50%
                String p85th = databaseAccess.getBhcw85p(Age_week);//we used the getBhcw85p method to get 85%
                String p97th = databaseAccess.getBhcw97p(Age_week);//we used the getBhw97p method to get 97%
                p1 = Double.parseDouble(p1st);//changes the string value of 1st % weight in kg to double type
                p3 = Double.parseDouble(p3rd);//changes the string value of 3% weight in kg to double type
                p15 = Double.parseDouble(p15th);//changes the string value of 15% weight in kg to double type
                p50 = Double.parseDouble(p50th);//changes the string value of 50% weight in kg to double type
                p85 = Double.parseDouble(p85th);//changes the string value of 85% weight in kg to double type
                p97 = Double.parseDouble(p97th);//changes the string value of 97% weight in kg to double type
                switch (Age_week) {
                    case "0": {//case 0 is when age is given 0 week
                        if (BHC <= p1)//if BHC head_circumference input is less than 1%
                        {
                            setContentView(R.layout.activity_result);//sets the content view to another layout the results will be displayed on activity_result layout.
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2
                            tv.setText(" growth rate is : 0 %" );//sets text to the textviw tv
                            GraphView ghc = findViewById(R.id.graph);//gets the id for graph
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {//creates line graph with datapoint series
                                    new DataPoint(Age_w, BHC)});//gets the datapoint x,y  values from user input age and birth hight
                            ghc.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//sets title for the result
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setDataPointsRadius(10);//sets dataPoint radius to 10
                            series4.setThickness(8);//sets dataPoint thickness to 8
                            graphHC();//WHO Boys Hight to age growth  chart
                        }
                        else if (BHC >p1 &&BHC <= p3)//if BHC birth head_circumference input is greater  than 1% and less than or equal to 3 %
                        {
                            AG = (BHC - p1);// AG Average gain is equal to BH minus the value of 1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the value of  3% minus the value fo 1%
                            GPW = (AG) / AGPW;// GPW gain per week rate is equal to the ratio of  calculated Average gain per  standard Average gain per week
                            PercentHC = (GPW * 0.02)+0.01; // Percentile Head_Circumference result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text to textview tv

                            GraphView gh = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {//creates line graph with datapoint series
                                    new DataPoint(Age_w, BHC)});//gets the datapoint x,y  values from user input age and birth HC
                            gh.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//sets title for the result
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO boys HC to age growth  chart
                        }

                        else if (BHC >  p3 && BHC <= p15)//if BHC(birth head_circumference)  input is greater  than 3% and less than or equal to 15 %
                        {
                            AG = (BHC - p3);// AG Average gain is equal to BHC minus the value of 3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG)/AGPW; // AGPW;GPW gain per week rate is equal to the ratio of  calculated Average gain per  standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.03;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            GraphView gh = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_w, BHC)});//gets the datapoint x,y  values from user input age and birth HC
                            gh.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else if (BHC > p15 && BHC <= p50)//if BHC(birth head_circumference)  input is greater  than 15% and less than or equal to 50 %
                        {
                            AG = (BHC - p15);// AG Average gain is equal to BHC minus WHO standard value of  value of 15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain per  standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            GraphView gh = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_w, BHC)});//gets the datapoint x,y  values from user input age and birth hight
                            gh.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO boys HC to age growth  chart

                        }
                        else if (BHC > p50 && BHC <=p85)//if BH(birth HC)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (BHC - p50);// AG Average gain is equal to BHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            GraphView gh = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_w, BHC)});//gets the datapoint x,y  values from user input age and birth_hight
                            gh.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else if (BHC > p85 && BHC <=p97)//if BH(birth HC)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (BHC - p85);// AG Average gain is equal to BHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            GraphView gh = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_w, BHC)});//gets the datapoint x,y  values from user input age and birth_hight
                            gh.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//seta text to textview tv
                            GraphView gh = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_w, BHC)});//gets the datapoint x,y  values from user input age and birth_hight
                            gh.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("100%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO boys HC to age growth  chart
                        }
                    }
                    break;
                    case "1": //case 1 is when the given age is 1 week
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CH(current_HC)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CH(current_HC)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CH(current_HC)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO boys HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CH(current_HC)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.5;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CH(current_HC)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.85;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                    }

                    break;
                    case "2":  //case 2 is when the given age is 2 weekS
                    {
                        if (CHC <= p1)//If current_HC input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_HC)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_HC)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CH(current_HC)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO girls HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CH(current_HC)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.5;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CH(current_HC)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.85;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                    }
                    break;
                    case "3":  //case 3 is when the given age is 3 weekS
                    {
                        if (CHC <= p1)//If current_HC input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CH(current_HC)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.02)+0.01;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CH(current_HC)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CH(current_HC)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO boys HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CH(current_HC)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CH(current_HC)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                    }

                    break;
                    case "4":  //case 4 is when the given age is 4 weekS
                    {
                        if (CHC <= p1)//If current_HC input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CH(current_HC)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {

                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CH(current_HC)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CH(current_HC)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_HC)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CH(current_HC)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                    }
                    break;
                    case "5":  //case 5 is when the given age is 5 week
                    {
                        if (CHC <= p1)//If current_HC input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_HC)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.02)+0.01;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_HC)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_HC)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_HC)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO  BOYS HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_HC)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                    }
                    break;
                    case "6":  //case 6 is when the given age is 6 weeks
                    {
                        if (CHC <= p1)//If current_HC input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CH(current_HC)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_HC)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_HC)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_HC)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.5;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_HC)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                    }
                    break;
                    case "7": //case 7 is when the given age is 7 weekS
                    {
                        if (CHC <= p1)//If current_HC input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_HC)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.02)+0.01;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_HC)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_HC)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {                                   new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_HC)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_HC)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                    }
                    break;
                    case "8": //case 8 is when the given age is 8 weekS
                    {
                        if (CHC <= p1)//If current_HC input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_HC)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.02)+0.01;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_HC)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1  because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_HC)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_HC)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.5;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_HC)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %");//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(1, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                    }
                    break;
                    case "9":  //case 9 is when the given age is 9 weekS
                    {
                        if (CHC <= p1)//If current_HC input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_HC)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_HC)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_HC)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_HC)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_HC)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.85;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else {
                            //when the given CH is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                    }
                    break;
                    case "10": //case 10 is when the given age is 10 weekS
                    {
                        if (CHC <= p1)//If current_HC input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_HC)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_HC)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_HC)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_HC)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_CH)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                    }
                    break;
                    case "11": //case 11 is when the given age is 11 weekS
                    {
                        if (CHC <= p1)//If current_HC input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_HC)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_HC)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_HC)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_HC)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.5;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_HC)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                    }
                    break;
                    case "12": //case 12 is when the given age is 12 weekS
                    {
                        if (CHC <= p1)//If current_HC input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO BOYS  HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_HC)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_HC)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_HC)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_HC)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_HC)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.85;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(2, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                    }
                    break;
                    case "13": //case 13 is when the given age is 13 weekS
                    {
                        if (CHC <= p1)//If current_HC input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(3, CHC)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_HC)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(3, CHC)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_HC)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(3, CHC)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(3, CHC)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_HC)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(3, CHC)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_HC)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(3, CHC)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(3, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
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

    public void  maleHCMonths()//this method does the calculation for boys head_circumference  per age in months  growth rate and displays the result on another layout
    {
        {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());////gets database instances
            databaseAccess.open();// opens the database connection
            etAge_hc =  findViewById(R.id.et_HCage);;// gets the id of the editText_wAge(from the xml layout using this method
            BHC = Double.parseDouble(etB_hc.getText().toString());//BC is birth_head_circumference changes the string value of user input  to double
            CHC = Double.parseDouble(etC_hc.getText().toString());//CH is current_head_circumfrrenece changes the string value of user input to double

            Age_month = etAge_hc.getText().toString();//get text from user input in age text editer
            Age_m = Integer.valueOf(Age_month);//convert the string  value of age_month in to integer
            if(Age_m<=13)//if age given in the input is less than or equal to 13 weeks
            {

                String p1st = databaseAccess.getBhcm1p(Age_month);//we used the getBhcm1p method to get 1%
                String p3rd = databaseAccess.getBhcm3p(Age_month);//we used thegetgetBhcm3p method to get 3%
                String p15th = databaseAccess.getBhcm15p(Age_month);//we used the getBhcm15P method to get 15%
                String p50th = databaseAccess.getBhcm50p(Age_month);//we used the getBhcm50p method to get 50%
                String p85th = databaseAccess.getBhcm85p(Age_month);//we used the getBhcm85p method to get 85%
                String p97th = databaseAccess.getBhcm97p(Age_month);//we used the getBhcm97p method to get 97%
                p1 = Double.parseDouble(p1st);//changes the string value of 1st % HC in CM to double type
                p3 = Double.parseDouble(p3rd);//changes the string value of 3% HC in CM to double type
                p15 = Double.parseDouble(p15th);//changes the string value of 15% HC in CM to double type
                p50 = Double.parseDouble(p50th);//changes the string value of 50% HC in CM to double type
                p85 = Double.parseDouble(p85th);//changes the string value of 85% HC in CM to double type
                p97 = Double.parseDouble(p97th);//changes the string value of 97% HC in CM to double type
                switch (Age_month) {
                    case "0": {//case 0 is when age is given 0
                        if (BHC <= p1)//if BHC (Birth_head_circumference) input is less than 1%
                        {
                            setContentView(R.layout.activity_result);//sets the content view to another layout the results will be displayed on activity_result layout.
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2
                            tv.setText(" growth rate is : 0 %" );//sets text to the textviw tv
                            GraphView g = findViewById(R.id.graph);//gets the id for graph
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {//creates line graph with datapoint series
                                    new DataPoint(Age_m, BHC)});//gets the datapoint x,y  values from user input age and birth hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//sets title for the result
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setDataPointsRadius(10);//sets dataPoint radius to 10
                            series4.setThickness(8);//sets dataPoint thickness to 8
                            graphHC();//WHO Boys HC to age growth  chart
                        }
                        else if (BHC >p1 &&BHC <= p3)//if BHC birth HC input is greater  than 1% and less than or equal to 3 %
                        {
                            AG = (BHC - p1);// AG Average gain is equal to BHC mines the value of 1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the value of  3% minus the value fo 1%
                            GPW = (AG) / AGPW;// GPW gain per month rate is equal to the ratio of  calculated Average gain per  standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01; // Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {//creates line graph with datapoint series
                                    new DataPoint(Age_m, BHC)});//gets the datapoint x,y  values from user input age and birth hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//sets title for the result
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO boys HC to age growth  chart
                        }

                        else if (BHC >  p3 && BHC <= p15)//if BHC(birth head_circumference)  input is greater  than 3% and less than or equal to 15 %
                        {
                            AG = (BHC - p3);// AG Average gain is equal to BHC minus the value of 3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG)/AGPW; // AGPW;GPW gain per month rate is equal to the ratio of  calculated Average gain per  standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, BHC)});//gets the datapoint x,y  values from user input age and birth hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else if (BHC > p15 && BHC <= p50)//if BHC(birth head_circumference)  input is greater  than 15% and less than or equal to 50 %
                        {
                            AG = (BHC - p15);// AG Average gain is equal to BHC minus WHO standard value of  value of 15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain per  standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, BHC)});//gets the datapoint x,y  values from user input age and birth hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO boys HC to age growth  chart

                        }
                        else if (BHC > p50 && BHC <=p85)//if BHC(birth head_circumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (BHC - p50);// AG Average gain is equal to BHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, BHC)});//gets the datapoint x,y  values from user input age and birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else if (BHC > p85 && BHC <=p97)//if BHC(birth head_circumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (BHC - p85);// AG Average gain is equal to BHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, BHC)});//gets the datapoint x,y  values from user input age and birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, BHC)});//gets the datapoint x,y  values from user input age and birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("100%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO boys HC to age growth  chart
                        }
                    }
                    break;
                    case "1": //case 1 is when the given age is 1 month
                    {
                        if (CHC <= p1)//If current_head_circumfrrenece input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(
                                    " growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is given user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_head_circumfrrenece)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_head_circumfrrenece)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is set to user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_HC)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is set to user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO boys HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_head_circumfrrenece)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is set to user input age,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_head_circumfrrenece)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                    }

                    break;
                    case "2":  //case 2 is when the given age is 2 months
                    {
                        if (CHC <= p1)//If current_head_circumfrrenece input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_head_circumfrrenece)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_head_circumfrrenece)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_head_circumfrrenece)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO boys HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_head_circumfrrenece)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_head_circumfrrenece)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.85;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                    }
                    break;
                    case "3":  //case 3 is when the given age is 3 months
                    {
                        if (CHC <= p1)//If current_head_circumfrrenece input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_HC)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user imput age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO boys HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO boys HC to age growth  chart
                        }
                    }

                    break;
                    case "4":  //case 4 is when the given age is 4 MONTHS
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {

                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month  equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month  rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                    }
                    break;
                    case "5":  //case 5 is when the given age is 5 MONTHS
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month  equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month  rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO  BOYS HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value form user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC  to age growth  chart
                        }
                    }
                    break;
                    case "6":  //case 6 is when the given age is 6 months
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CH(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                    }
                    break;
                    case "7": //case 7 is when the given age is 7 months
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {                                   new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                    }
                    break;
                    case "8": //case 8 is when the given age is 8 months
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                    }
                    break;
                    case "9":  //case 9 is when the given age is 9 months
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC
                                    - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                    }
                    break;
                    case "10": //case 10 is when the given age is 10 MONTHS
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month  equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC  to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month  equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CH(current_hight)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input  age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is from user input  age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                    }
                    break;
                    case "11": //case 11 is when the given age is 11 months
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value fromu user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                    }
                    break;
                    case "12": //case 12 is when the given age is 12 month
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphHC();//WHO BOYS  HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CH minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value fromuser input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphHC();//WHO BOYS HC to age growth  chart
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

    public void femaleHCMonths(){
        //this method does the calculation for girls Head_Circumference  per age in months  growth rate and displays the result on another layout
        {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());////gets database instances
            databaseAccess.open();// opens the database connection
            etAge_hc =  findViewById(R.id.et_HCage);;// gets the id of the editText_wAge(from the xml layout using this method
            BHC = Double.parseDouble(etB_hc.getText().toString());//BHC is birth_Head circumference changes the string value of user input  to double
            CHC = Double.parseDouble(etC_hc.getText().toString());//CHC is current_head_circumfrrenece changes the string value of user input to double

            Age_month = etAge_hc.getText().toString();//get text from user input in age text editer
            Age_m = Integer.valueOf(Age_month);//convert the string  value of age_week in to integer
            if(Age_m<=13)//if age given in the input is less than or equal to 13 weeks
            {

                String p1st = databaseAccess.getGhcm1p(Age_month);//we used the getGhcm1p method to get 1%
                String p3rd = databaseAccess.getGhcm3p(Age_month);//we used the getGhcm3p method to get 3%
                String p15th = databaseAccess.getGhcm15p(Age_month);//we used the getGhcm15p method to get 15%
                String p50th = databaseAccess.getGhcm50p(Age_month);//we used the getGhcm50p method to get 50%
                String p85th = databaseAccess.getGhcm85p(Age_month);//we used the getGhcm85p method to get 85%
                String p97th = databaseAccess.getGhcm97p(Age_month);//we used the getGhcm97p method to get 97%
                p1 = Double.parseDouble(p1st);//changes the string value of 1st % weight in kg to double type
                p3 = Double.parseDouble(p3rd);//changes the string value of 3% weight in kg to double type
                p15 = Double.parseDouble(p15th);//changes the string value of 15% weight in kg to double type
                p50 = Double.parseDouble(p50th);//changes the string value of 50% weight in kg to double type
                p85 = Double.parseDouble(p85th);//changes the string value of 85% weight in kg to double type
                p97 = Double.parseDouble(p97th);//changes the string value of 97% weight in kg to double type
                switch (Age_month) {
                    case "0": {//case 0 is when age is given 0
                        if (BHC <= p1)//if BHC Birth_head_circumference input is less than 1%
                        {
                            setContentView(R.layout.activity_result);//sets the content view to another layout the results will be displayed on activity_result layout.
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2
                            tv.setText(" growth rate is : 0 %" );//sets text to the textviw tv
                            GraphView g = findViewById(R.id.graph);//gets the id for graph
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {//creates line graph with datapoint series
                                    new DataPoint(Age_m, BHC)});//gets the datapoint x,y  values from user input age and birth hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//sets title for the result
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setDataPointsRadius(10);//sets dataPoint radius to 10
                            series4.setThickness(8);//sets dataPoint thickness to 8
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (BHC >p1 &&BHC <= p3)//if BHC birth_head_circumference input is greater  than 1% and less than or equal to 3 %
                        {
                            AG = (BHC - p1);// AG Average gain is equal to BHC minus the value of 1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the value of  3% minus the value fo 1%
                            GPW = (AG) / AGPW;// GPW gain per month rate is equal to the ratio of  calculated Average gain per  standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01; // Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {//creates line graph with datapoint series
                                    new DataPoint(Age_m, BHC)});//gets the datapoint x,y  values from user input age and birth hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//sets title for the result
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }

                        else if (BHC >  p3 && BHC <= p15)//if BHC(birth_head_circumference)  input is greater  than 3% and less than or equal to 15 %
                        {
                            AG = (BHC - p3);// AG Average gain is equal to BHC minus the value of 3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG)/AGPW; // AGPW;GPW gain per month rate is equal to the ratio of  calculated Average gain per  standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, BHC)});//gets the datapoint x,y  values from user input age and birth hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (BHC > p15 && BHC <= p50)//if BHC(birth_head_circumference)  input is greater  than 15% and less than or equal to 50 %
                        {
                            AG = (BHC - p15);// AG Average gain is equal to BH minus WHO standard value of  value of 15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain per  standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile Hight result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, BHC)});//gets the datapoint x,y  values from user input age and birth hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsHC();//WHO girls HC to age growth  chart

                        }
                        else if (BHC > p50 && BHC <=p85)//if BHC(birth_head_circumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (BHC - p50);// AG Average gain is equal to BHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, BHC)});//gets the datapoint x,y  values from user input age and birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (BHC > p85 && BHC <=p97)//if BHC(birth_head_circumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (BHC - p85);// AG Average gain is equal to BHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, BHC)});//gets the datapoint x,y  values from user input age and birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, BHC)});//gets the datapoint x,y  values from user input age and birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("100%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                    }
                    break;
                    case "1": //case 1 is when the given age is 1 month
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is given user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is set to user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is set to user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is set to user input age,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC  to age growth  chart
                        }
                    }

                    break;
                    case "2":  //case 2 is when the given age is 2 months
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsHC();//WHO girls HC  to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CH(current_hight)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CH(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CH(current_hight)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO boys HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                    }
                    break;
                    case "3":  //case 3 is when the given age is 3 months
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user imput age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                    }

                    break;
                    case "4":  //case 4 is when the given age is 4 MONTHS
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {

                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month  equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month  rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                    }
                    break;
                    case "5":  //case 5 is when the given age is 5 months
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsHC();//WHO gight HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month  equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month  rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO  girls HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value form user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC  to age growth  chart
                        }
                    }
                    break;
                    case "6":  //case 6 is when the given age is 6 months
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                    }
                    break;
                    case "7": //case 7 is when the given age is 7 months
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_HC)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {                                   new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC tO age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                    }
                    break;
                    case "8": //case 8 is when the given age is 8 months
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO gight HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                    }
                    break;
                    case "9":  //case 9 is when the given age is 9 months
                    {
                        if (CHC <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                    }
                    break;
                    case "10": //case 10 is when the given age is 10 months
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month  equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC  to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month  equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input  age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is from user input  age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                    }
                    break;
                    case "11": //case 11 is when the given age is 11 months
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value fromu user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                    }
                    break;
                    case "12": //case 12 is when the given age is 12 month
                    {
                        if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                        {
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setTitle("0%");//seta text to textview tv
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            graphGirlsHC();//WHO girls  HC to age growth  chart
                        }
                        else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                        {
                            AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                            AGPW = p3-p1;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  3% minus 1%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }

                        else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                        {
                            AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                            AGPW = p15-p3;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  15% minus 3%
                            GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                        {
                            AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                            AGPW = p50-p15;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  50% minus 15%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart

                        }
                        else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                        {
                            AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                            AGPW = p85-p50;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  85% minus 50%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value fromuser input age  ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                        {
                            AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                            AGPW = p97-p85;//AGPW is Who standard  average gain per month equal to the WHO Standard  value of  97% minus 85%
                            GPW = (AG) / AGPW;//GPW gain per month rate is equal to the ratio of  calculated Average gain and standard Average gain per month
                            PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                    {
                                            new DataPoint(Age_m, CHC)});//gets the datapoint x value from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                        else {
                            //when the given CHC is greater than WHO standard value of 97%
                            setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                            TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                            tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                            GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                            LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(Age_m, CHC)}); //creates line graph with datapoint series array
                            //gets the datapoint x value is from user input age ,y  values from user input  birth_hight
                            g.addSeries(series4);//add the data point series in the graph
                            series4.setColor(Color.BLACK);//sets the  line graph color to black
                            series4.setDrawDataPoints(true);//shows datapoints on the graph
                            series4.setTitle("100%");//set a text using string value of the result to textview tv
                            graphGirlsHC();//WHO girls HC to age growth  chart
                        }
                    }
                    break;

                }
            }

            else{

                Toast.makeText(getApplicationContext(),"The App is valid for Age up to 12 months ",Toast.LENGTH_LONG).show();//pop up message
            }
            databaseAccess.close();//database connection closed
        }
    }
    public void femaleHCWeeks(){
        //this method does the calculation for girls Head_Circumference per age percentile in weeks  growth rate and displays the result with WHO growth rate chart on another layout
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());////gets database instances
        databaseAccess.open();// opens the database connection
        etAge_hc =  findViewById(R.id.et_HCage);// gets the id of the editText_wAge(from the xml layout using this method
        BHC = Double.parseDouble(etB_hc.getText().toString());//BHC is birth_head_circumference changes the string value of user input  to double
        CHC = Double.parseDouble(etC_hc.getText().toString());//CHC is current_head_circumfrrenece changes the string value of user input to double

        Age_week = etAge_hc.getText().toString();//get text from user input in age text editer
        Age_w = Integer.valueOf(Age_week);//convert the string  value of age_week in to integer

        if(Age_w<=13)//if age given in the input is less than or equal to 13 weeks
        {

            String p1st = databaseAccess.getGhcw1p(Age_week);//we used the getGhcw1p method to get 1%
            String p3rd = databaseAccess.getGhcw3p(Age_week);//we used thegetgetGhcw3p method to get 3%
            String p15th = databaseAccess.getGhcw15p(Age_week);//we used the getGhcw15P method to get 15%
            String p50th = databaseAccess.getGhcw50p(Age_week);//we used the getGhcw50p method to get 50%
            String p85th = databaseAccess.getGhcw85p(Age_week);//we used the getGhcw85p method to get 85%
            String p97th = databaseAccess.getGhcw97p(Age_week);//we used the getGhcw97p method to get 97%
            p1 = Double.parseDouble(p1st);//changes the string value of 1st % weight in kg to double type
            p3 = Double.parseDouble(p3rd);//changes the string value of 3% weight in kg to double type
            p15 = Double.parseDouble(p15th);//changes the string value of 15% weight in kg to double type
            p50 = Double.parseDouble(p50th);//changes the string value of 50% weight in kg to double type
            p85 = Double.parseDouble(p85th);//changes the string value of 85% weight in kg to double type
            p97 = Double.parseDouble(p97th);//changes the string value of 97% weight in kg to double type
            switch (Age_week) {
                case "0": {//case 0 is when age is given 0 week
                    if (BHC <= p1)//if BHC birth_head_circumference input is less than 1%
                    {
                        setContentView(R.layout.activity_result);//sets the content view to another layout the results will be displayed on activity_result layout.
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2
                        tv.setText(" growth rate is : 0 %" );//sets text to the textviw tv
                        GraphView g = findViewById(R.id.graph);//gets the id for graph
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {//creates line graph with datapoint series
                                new DataPoint(Age_w, BHC)});//gets the datapoint x,y  values from user input age and birth hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//sets title for the result
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setDataPointsRadius(10);//sets dataPoint radius to 10
                        series4.setThickness(8);//sets dataPoint thickness to 8
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (BHC >p1 &&BHC <= p3)//if BHC birth_head_circumference input is greater  than 1% and less than or equal to 3 %
                    {
                        AG = (BHC - p1);// AG Average gain is equal to BHC mines the value of 1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the value of  3% minus the value fo 1%
                        GPW = (AG) / AGPW;// GPW gain per week rate is equal to the ratio of  calculated Average gain per  standard Average gain per week
                        PercentHC = (GPW * 0.02)+0.01; // Percentile HC result
                        setContentView(R.layout.activity_result); //sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {//creates line graph with datapoint series
                                new DataPoint(Age_w, BHC)});//gets the datapoint x,y  values from user input age and birth hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//sets title for the result
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }

                    else if (BHC >  p3 && BHC <= p15)//if BHC(birth_head_circumference)  input is greater  than 3% and less than or equal to 15 %
                    {
                        AG = (BHC - p3);// AG Average gain is equal to BHC minus the value of 3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW; // AGPW;GPW gain per week rate is equal to the ratio of  calculated Average gain per  standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.03;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(Age_w, BHC)});//gets the datapoint x,y  values from user input age and birth hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (BHC > p15 && BHC <= p50)//if BHC(birth_head_circumference)  input is greater  than 15% and less than or equal to 50 %
                    {
                        AG = (BHC - p15);// AG Average gain is equal to BHC minus WHO standard value of  value of 15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain per  standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(Age_w, BHC)});//gets the datapoint x,y  values from user input age and birth hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsHC();//WHO girls HC to age growth  chart

                    }
                    else if (BHC > p50 && BHC <=p85)//if BHC(birth_head_circumference)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (BHC - p50);// AG Average gain is equal to BHC minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(Age_w, BHC)});//gets the datapoint x,y  values from user input age and birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (BHC > p85 && BHC <=p97)//if BHC(birth_head_circumference)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (BHC - p85);// AG Average gain is equal to BHC minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(Age_w, BHC)});//gets the datapoint x,y  values from user input age and birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(Age_w, BHC)});//gets the datapoint x,y  values from user input age and birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsHC();//WHO girls Hight to age growth  chart
                    }
                }
                break;
                case "1": //case 1 is when the given age is 1 week
                {
                    if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }

                    else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart

                    }
                    else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else {
                        //when the given CHC is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CHC)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                }

                break;
                case "2":  //case 2 is when the given age is 2 weekS
                {
                    if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }

                    else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart

                    }
                    else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else {
                        //when the given CH is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CHC)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                }
                break;
                case "3":  //case 3 is when the given age is 3 weekS
                {
                    if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }

                    else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart

                    }
                    else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p85 && CHC <=p97)//if CH(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else {
                        //when the given CHC is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CHC)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                }

                break;
                case "4":  //case 4 is when the given age is 4 weekS
                {
                    if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsHC();//WHO girls weight to age growth  chart
                    }
                    else if (CHC >p1 &&CHC <= p3)//if CH(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }

                    else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CHC- p15);// AG Average gain is equal to CH minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart

                    }
                    else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(0, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else {
                        //when the given CHC is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, CHC)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                }
                break;
                case "5":  //case 5 is when the given age is 5 week
                {
                    if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }

                    else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart

                    }
                    else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 0 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else {
                        //when the given CHC is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CHC)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                }
                break;
                case "6":  //case 6 is when the given age is 6 weeks
                {
                    if (CHC <= p1)//If current_hight input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle("0%");//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }

                    else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CHC - p3);// AG Average gain is equal to CH minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p15 && CHC <= p50)//if CH(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart

                    }
                    else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else {
                        //when the given CHC is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CHC)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle("100%");//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                }
                break;
                case "7": //case 7 is when the given age is 7 weekS
                {
                    if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle("0%");//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }

                    else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {                                   new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart

                    }
                    else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else {
                        //when the given CHC is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CHC)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle("100%");//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls weight to age growth  chart
                    }
                }
                break;
                case "8": //case 8 is when the given age is 8 weekS
                {
                    if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle("0%");//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }

                    else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1  because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart

                    }
                    else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(1, CHC)});//gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else {
                        //when the given CHC is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, CHC)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 1 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle("100%");//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                }
                break;
                case "9":  //case 9 is when the given age is 9 weekS
                {
                    if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle("0%");//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }

                    else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart

                    }
                    else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else {
                        //when the given CHC is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CHC)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle("100%");//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                }
                break;
                case "10": //case 10 is when the given age is 10 weekS
                {
                    if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle("0%");//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.02)+0.01;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }

                    else if (CHC >  p3 && CHC <= p15)//if CH(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart

                    }
                    else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else {
                        //when the given CHC is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CHC)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle("100%");//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                }
                break;
                case "11": //case 11 is when the given age is 11 weekS
                {
                    if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle("0%");//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }

                    else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart

                    }
                    else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CHC - p50);// AG Average gain is equal to CH minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.5;// Percentile Hight result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else {
                        //when the given CHC is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CHC)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle("100%");//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                }
                break;
                case "12": //case 12 is when the given age is 12 weekS
                {
                    if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle("0%");//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }

                    else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart

                    }
                    else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CHC - p85);// AG Average gain is equal to CH minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(2, CHC)});//gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else {
                        //when the given CHC is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %");//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(2, CHC)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 2 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle("100%");//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                }
                break;
                case "13": //case 13 is when the given age is 13 weekS
                {
                    if (CHC <= p1)//If current_Headcircumference input is less than or equal to WHO standard value of 1%
                    {
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 0 %" );//seta text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(3, CHC)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setTitle("0%");//seta text to textview tv
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC >p1 &&CHC <= p3)//if CHC(current_Headcircumference)  input is greater  than 1% and less than or equal to 3%
                    {
                        AG = (CHC - p1);// AG Average gain is equal to CHC minus WHO standard value of  1%
                        AGPW = p3-p1;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  3% minus 1%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.02)+0.01;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series
                                {
                                        new DataPoint(3, CHC)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }

                    else if (CHC >  p3 && CHC <= p15)//if CHC(current_Headcircumference)  input is greater  than 3% and less than or equal to 15%
                    {
                        AG = (CHC - p3);// AG Average gain is equal to CHC minus WHO standard value of  3%
                        AGPW = p15-p3;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  15% minus 3%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.03;// Percentile HC result

                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//seta text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(3, CHC)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p15 && CHC <= p50)//if CHC(current_Headcircumference)  input is greater  than 15% and less than or equal to 50%
                    {
                        AG = (CHC - p15);// AG Average gain is equal to CHC minus WHO standard value of  15%
                        AGPW = p50-p15;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  50% minus 15%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.15;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series
                                {
                                        new DataPoint(3, CHC)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//seta text to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart

                    }
                    else if (CHC > p50 && CHC <=p85)//if CHC(current_Headcircumference)  input is greater  than 50% and less than or equal to 85%
                    {
                        AG = (CHC - p50);// AG Average gain is equal to CHC minus WHO standard value of  50%
                        AGPW = p85-p50;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  85% minus 50%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.35)+0.5;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv

                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[]//creates line graph with datapoint series arry
                                {
                                        new DataPoint(3, CHC)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else if (CHC > p85 && CHC <=p97)//if CHC(current_Headcircumference)  input is greater  than 85% and less than or equal to 97%
                    {
                        AG = (CHC - p85);// AG Average gain is equal to CHC minus WHO standard value of  85%
                        AGPW = p97-p85;//AGPW is Who standard  average gain per week equal to the WHO Standard  value of  97% minus 85%
                        GPW = (AG) / AGPW;//GPW gain per week rate is equal to the ratio of  calculated Average gain and standard Average gain per week
                        PercentHC = (GPW * 0.12)+0.85;// Percentile HC result
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : " + String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        GraphView g = findViewById(R.id.graph); //gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] //creates line graph with datapoint series araay
                                {
                                        new DataPoint(3, CHC)});//gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle(String.valueOf(rate.format(PercentHC)));//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
                    }
                    else {
                        //when the given CHC is greater than WHO standard value of 97%
                        setContentView(R.layout.activity_result);//sets content view to activity_result so that the result will be displayed on this other layout
                        TextView tv = findViewById(R.id.result_h);//gets the id for textView_R2 from activity_result layout
                        tv.setText(" growth rate is : 100 %" );//set a text to textview tv
                        GraphView g = findViewById(R.id.graph);//gets the id for the graph from activity_result layout
                        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(3, CHC)}); //creates line graph with datapoint series array
                        //gets the datapoint x value is set to 3 because WHO Standard growth chart uses age in months ,y  values from user input  birth_hight
                        g.addSeries(series4);//add the data point series in the graph
                        series4.setColor(Color.BLACK);//sets the  line graph color to black
                        series4.setDrawDataPoints(true);//shows datapoints on the graph
                        series4.setTitle("100%");//set a text using string value of the result to textview tv
                        graphGirlsHC();//WHO girls HC to age growth  chart
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
