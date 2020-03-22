package com.yemte.infantgrowthcalculator;



import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jjoe64.graphview.series.DataPoint;

import java.nio.DoubleBuffer;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c = null;
    private DatabaseAccess(Context context){

        this.openHelper=new DatabaseOpenHelper(context);
    }

    // to return a single instance of a database
    public static DatabaseAccess getInstance(Context context){
        if(instance==null){
            instance = new DatabaseAccess(context);
        }

        return instance;

    }
    //to open the database
    public  void open(){
        this.db = openHelper.getWritableDatabase();
    }
    //closing the database connection

    public void close(){
        if(db!=null){
            this.db.close();
        }

    }
    //this is   a method to get values of one percent weekly growth rate for a specific age and return the result
    //we will query one percent from the database WHO standard table of boys weight gain per week   by passing age
    //and we will use the result to calculate weekly growth rate for boys.

    public String getBww1p(String age){

        c=db.rawQuery("select P1st from Boysweightweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String p = c.getString(0);
            buffer.append(""+p);
        }
        return buffer.toString();

    }
    //this is   a method to get values of three percent weekly growth rate for a specific age and return the result
    //we will query 3 percent from the database WHO standard table of boys weight gain per week   by passing age
    //and we will use the result to calculate weekly growth rate for boys.
    public String getBww3p(String age){

        c=db.rawQuery("select P3rd from Boysweightweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String Wwb3p = c.getString(0);
            buffer.append(""+Wwb3p);
        }
        return buffer.toString();

    }
    //this is   a method to get values of 15 percent weekly growth rate for a specific age and return the result
    //we will query 15 percent from the database WHO standard table of boys weight gain per week   by passing age
    //and we will use the result to calculate weekly growth rate for boys.
    public String getBww15P(String age){

        c=db.rawQuery("select P15th from Boysweightweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String Wwb15P = c.getString(0);
            buffer.append(""+Wwb15P);
        }
        return buffer.toString();

    }
    //this is   a method to get values of 50 percent weekly growth rate for a specific age and return the result
    //we will query 50 percent from the database WHO standard table of boys weight gain per week   by passing age
    //and we will use the result to calculate weekly growth rate for boys.
    public String getBww50p(String age){

        c=db.rawQuery("select P50th from Boysweightweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String Wwb50p= c.getString(0);
            buffer.append(""+Wwb50p);
        }
        return buffer.toString();

    }
    //this is   a method to get values of 85 percent weekly growth rate for a specific age and return the result
    //we will query 85 percent from the database WHO standard table of boys weight gain per week   by passing age
    //and we will use the result to calculate weekly growth rate for boys.
    public String getBww85p(String age){

        c=db.rawQuery("select P85th from Boysweightweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String Wwb85p = c.getString(0);
            buffer.append(""+Wwb85p);
        }
        return buffer.toString();

    }
    //this is   a method to get values of 97 percent weekly growth rate for a specific age and return the result


    public String getBww97p(String age){
//this is query 97 percent from the database WHO standard table of boys weight gain per week  by passing age
        c=db.rawQuery("select P97th from Boysweightweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String Wwb97p = c.getString(0);
            buffer.append(""+Wwb97p
            );
        }
        return buffer.toString();//  the result will be used to calculate weekly growth rate for boys.

    }
    //this is   a method to get values of 1 percent monthly growth rate for a specific age and return the result

    public String getBwm1p(String age){
//this is query 1 percent from the database WHO standard table of boys weight gain per month  by passing age
        c=db.rawQuery("select P1st from Boysweightmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmbonep = c.getString(0);
            buffer.append(""+wmbonep);
        }
        return buffer.toString();//  the result will be used  to calculate weekly growth rate for boys.

    }
    //this is   a method to get values of 3 percent monthly growth rate for a specific age and return the resul
    public String getBwm3p(String age){
//this is query for 1 percent from the database WHO standard table of boys weight gain per month  by passing age
        c=db.rawQuery("select P3rd from Boysweightmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmb3p = c.getString(0);
            buffer.append(""+wmb3p);
        }
        return buffer.toString();//  the result will be used  to calculate weekly growth rate for boys.

    }
    //this is   a method to get values of 15 percent monthly growth rate for a specific age and return the result
    //we will query 15 percent from  WHO standard table of boys weight gain per month  in the database by passing age
    //and we will use the result to calculate monthly growth rate for boys.
    public String getBwm15p(String age){

        c=db.rawQuery("select P15th from Boysweightmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmb15p = c.getString(0);
            buffer.append(""+wmb15p);
        }
        return buffer.toString();

    }//this is   a method to get values of 50 percent monthly growth rate for a specific age and return the result
    //we will query 50 percent from  WHO standard table of boys weight gain per month  in the database by passing age
    //and we will use the result to calculate monthly growth rate for boys.
    public String getBwm50p(String age){

        c=db.rawQuery("select P50th from Boysweightmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String Wmb50p = c.getString(0);
            buffer.append(""+Wmb50p);
        }
        return buffer.toString();

    }
    //this is   a method to get values of 85 percent monthly growth rate for a specific age and return the result
    //we will query 85 percent from  WHO standard table of boys weight gain per month  in the database by passing age
    //and we will use the result to calculate monthly growth rate for boys.
    public String getBwm85p(String age){

        c=db.rawQuery("select P85th from Boysweightmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmb85p = c.getString(0);
            buffer.append(""+wmb85p);
        }
        return buffer.toString();

    }
    //this is   a method to get values of 97 percent monthly growth rate for a specific age and return the result
    //we will query 97 percent from  WHO standard table of boys weight gain per month  in the database by passing age
    //and we will use the result to calculate monthly growth rate for boys.
    public String getBwm97p(String age){

        c=db.rawQuery("select P97th from Boysweightmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmb97p = c.getString(0);
            buffer.append(""+wmb97p);
        }
        return buffer.toString();

    }
    public String getGwm1p(String age){

        c=db.rawQuery("select P1st from Girlsweightmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmgonep = c.getString(0);
            buffer.append(""+wmgonep);
        }
        return buffer.toString();

    }
    public String getGwm3p(String age){

        c=db.rawQuery("select P3rd from Girlsweightmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmg3p = c.getString(0);
            buffer.append(""+wmg3p);
        }
        return buffer.toString();

    }
    public String getGwm15p(String age){

        c=db.rawQuery("select P15th from Girlsweightmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmg15p = c.getString(0);
            buffer.append(""+wmg15p);
        }
        return buffer.toString();

    }
    public String getGwm50p(String age){

        c=db.rawQuery("select P50th from Girlsweightmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmg50p = c.getString(0);
            buffer.append(""+wmg50p);
        }
        return buffer.toString();

    }
    public String getGwm85p(String age){

        c=db.rawQuery("select P85th from Girlsweightmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmg85p = c.getString(0);
            buffer.append(""+wmg85p);
        }
        return buffer.toString();

    }
    public String getGwm97p(String age){

        c=db.rawQuery("select P97th from Girlsweightmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmg97p = c.getString(0);
            buffer.append(""+wmg97p);
        }
        return buffer.toString();

    }
    public String getGww1p(String age){

        c=db.rawQuery("select P1st from Girlsweightweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wwgonep = c.getString(0);
            buffer.append(""+wwgonep);
        }
        return buffer.toString();

    }
    public String getGww3p(String age){

        c=db.rawQuery("select P3rd from Girlsweightweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wwg3p = c.getString(0);
            buffer.append(""+wwg3p);
        }
        return buffer.toString();

    }
    public String getGww15p(String age){

        c=db.rawQuery("select P15th from Girlsweightweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wwg15p = c.getString(0);
            buffer.append(""+wwg15p);
        }
        return buffer.toString();

    }
    public String getGww50p(String age){

        c=db.rawQuery("select P50th from Girlsweightweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wwgfiftyp = c.getString(0);
            buffer.append(""+wwgfiftyp);
        }
        return buffer.toString();

    }
    public String getGww85p(String age){

        c=db.rawQuery("select P85th from Girlsweightweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wwg85p = c.getString(0);
            buffer.append(""+wwg85p);
        }
        return buffer.toString();

    }
    public String getGww97p(String age){

        c=db.rawQuery("select P97th from Girlsweightweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wwg97p = c.getString(0);
            buffer.append(""+wwg97p);
        }
        return buffer.toString();

    }
    //this is   a method to get values of 1 percent weekly growth rate for a specific age and return the result
    //we will query 1 percent from  WHO standard table of boys Hight gain per week  in the database by passing age
    //and we will use the result to calculate monthly growth rate for boys.

    public String getHwb1p(String age){

        c=db.rawQuery("select P1st from Hightweeksboys where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String p = c.getString(0);
            buffer.append(""+p);
        }
        return buffer.toString();

    }
    //this is   a method to get values of 3 percent weekly growth rate for a specific age and return the result
    //we will query 3 percent from  WHO standard table of boys Hight gain per week  in the database by passing age
    //and we will use the result to calculate monthly growth rate for boys.
    public String getHwb3p(String age){

        c=db.rawQuery("select P3rd from Hightweeksboys where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String Hwb3p = c.getString(0);
            buffer.append(""+Hwb3p);
        }
        return buffer.toString();

    }
    //this is   a method to get values of 15 percent weekly growth rate for a specific age and return the result
    //we will query 15 percent from  WHO standard table of boys Hight gain per week  in the database by passing age
    //and we will use the result to calculate monthly growth rate for boys.
    public String getHwb15P(String age){

        c=db.rawQuery("select P15th from Hightweeksboys where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String Hwb15P = c.getString(0);
            buffer.append(""+Hwb15P);
        }
        return buffer.toString();

    }
    public String getHwb50p(String age){

        c=db.rawQuery("select P50th from Hightweeksboys where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String Whb50p= c.getString(0);
            buffer.append(""+Whb50p);
        }
        return buffer.toString();

    }
    public String getHwb85p(String age){

        c=db.rawQuery("select P85th from Hightweeksboys where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String Whb85p = c.getString(0);
            buffer.append(""+Whb85p);
        }
        return buffer.toString();

    }
    public String getHwb97p(String age){

        c=db.rawQuery("select P97th from Hightweeksboys where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String Whb97p = c.getString(0);
            buffer.append(""+Whb97p
            );
        }
        return buffer.toString();

    }
    //this is   a method to get values of 1 percent monthly growth rate for a specific age and return the result
    //we will query 1 percent from  WHO standard table of boys Hight gain per month  in the database by passing age
    //and we will use the result to calculate monthly growth rate for boys.
    public String getBhm1p(String age){

        c=db.rawQuery("select P1st from Hightmonthsboys where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmbonep = c.getString(0);
            buffer.append(""+wmbonep);
        }
        return buffer.toString();

    }
    public String getBhm3p(String age){

        c=db.rawQuery("select P3rd from Hightmonthsboys where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmb3p = c.getString(0);
            buffer.append(""+wmb3p);
        }
        return buffer.toString();

    }
    public String getBhm15p(String age){

        c=db.rawQuery("select P15th from Hightmonthsboys where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmb15p = c.getString(0);
            buffer.append(""+wmb15p);
        }
        return buffer.toString();

        //this is   a method to get values of 50 percent monthly growth rate for a specific age and return the result
        //we will query 50 percent from  WHO standard table of boys Hight gain per month  in the database by passing age
        //and we will use the result to calculate monthly growth rate for boys.

    }
    public String getBhm50p(String age){

        c=db.rawQuery("select P50th from Hightmonthsboys where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String Wmb50p = c.getString(0);
            buffer.append(""+Wmb50p);
        }
        return buffer.toString();

    }
    public String getBhm85p(String age){

        c=db.rawQuery("select P85th from Hightmonthsboys where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmb85p = c.getString(0);
            buffer.append(""+wmb85p);
        }
        return buffer.toString();

    }
    public String getBhm97p(String age){

        c=db.rawQuery("select P97th from Hightmonthsboys where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmb97p = c.getString(0);
            buffer.append(""+wmb97p);
        }
        return buffer.toString();

    }
    //this is   a method to get values of 1 percent monthly growth rate for a specific age and return the result
    //we will query 1 percent from  WHO standard table of girls Hight gain per month  in the database by passing age
    //and we will use the result to calculate monthly growth rate for boys.
    public String getGhm1p(String age){

        c=db.rawQuery("select P1st from Hightmonthsgirls where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmgonep = c.getString(0);
            buffer.append(""+wmgonep);
        }
        return buffer.toString();

    }
    public String getGhm3p(String age){

        c=db.rawQuery("select P3rd from Hightmonthsgirls where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmg3p = c.getString(0);
            buffer.append(""+wmg3p);
        }
        return buffer.toString();

    }
    public String getGhm15p(String age){

        c=db.rawQuery("select P15th from Hightmonthsgirls where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmg15p = c.getString(0);
            buffer.append(""+wmg15p);
        }
        return buffer.toString();

    }
    public String getGhm50p(String age){

        c=db.rawQuery("select P50th from Hightmonthsgirls where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmg50p = c.getString(0);
            buffer.append(""+wmg50p);
        }
        return buffer.toString();

    }
    public String getGhm85p(String age){

        c=db.rawQuery("select P85th from Hightmonthsgirls where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmg85p = c.getString(0);
            buffer.append(""+wmg85p);
        }
        return buffer.toString();

    }
    public String getGhm97p(String age){

        c=db.rawQuery("select P97th from Hightmonthsgirls where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmg97p = c.getString(0);
            buffer.append(""+wmg97p);
        }
        return buffer.toString();

    }
    //this is   a method to get values of 1 percent weekly growth rate for a specific age and return the result
    //we will query 1 percent from  WHO standard table of girls Hight gain per month  in the database by passing age
    //and we will use the result to calculate monthly growth rate for boys.
    public String getGhw1p(String age){

        c=db.rawQuery("select P1st from Hightweeksgirls where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wwgonep = c.getString(0);
            buffer.append(""+wwgonep);
        }
        return buffer.toString();

    }
    public String getGhw3p(String age){

        c=db.rawQuery("select P3rd from Hightweeksgirls where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wwg3p = c.getString(0);
            buffer.append(""+wwg3p);
        }
        return buffer.toString();

    }
    public String getGhw15p(String age){

        c=db.rawQuery("select P15th from Hightweeksgirls where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wwg15p = c.getString(0);
            buffer.append(""+wwg15p);
        }
        return buffer.toString();

    }
    public String getGhw50p(String age){

        c=db.rawQuery("select P50th from Hightweeksgirls where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wwgfiftyp = c.getString(0);
            buffer.append(""+wwgfiftyp);
        }
        return buffer.toString();

    }
    public String getGhw85p(String age){

        c=db.rawQuery("select P85th from Hightweeksgirls where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wwg85p = c.getString(0);
            buffer.append(""+wwg85p);
        }
        return buffer.toString();

    }
    public String getGhw97p(String age){

        c=db.rawQuery("select P97th from Hightweeksgirls where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wwg97p = c.getString(0);
            buffer.append(""+wwg97p);
        }
        return buffer.toString();

    }
    //this is   a method to get values of 1 percent weekly growth rate for a specific age and return the result
    //we will query 1 percent from  WHO standard table of boys  Head Circumference increase per month  in the database by passing age
    //and we will use the result to calculate weekly increase rate for boys.

    public String getBhcw1p(String age){

        c=db.rawQuery("select P1st from Headboysweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String p = c.getString(0);
            buffer.append(""+p);
        }
        return buffer.toString();

    }
    public String getBhcw3p(String age){

        c=db.rawQuery("select P3rd from Headboysweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String Hwb3p = c.getString(0);
            buffer.append(""+Hwb3p);
        }
        return buffer.toString();

    }
    public String getBhcw15P(String age){

        c=db.rawQuery("select P15th from Headboysweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String Hwb15P = c.getString(0);
            buffer.append(""+Hwb15P);
        }
        return buffer.toString();

    }
    public String getBhcw50p(String age){

        c=db.rawQuery("select P50th from Headboysweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String Whb50p= c.getString(0);
            buffer.append(""+Whb50p);
        }
        return buffer.toString();

    }
    public String getBhcw85p(String age){

        c=db.rawQuery("select P85th from Headboysweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String Whb85p = c.getString(0);
            buffer.append(""+Whb85p);
        }
        return buffer.toString();

    }
    public String getBhcw97p(String age){

        c=db.rawQuery("select P97th from Headboysweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String Whb97p = c.getString(0);
            buffer.append(""+Whb97p
            );
        }
        return buffer.toString();

    }
    public String getBhcm1p(String age){

        c=db.rawQuery("select P1st from Headboysmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmbonep = c.getString(0);
            buffer.append(""+wmbonep);
        }
        return buffer.toString();

    }
    public String getBhcm3p(String age){

        c=db.rawQuery("select P3rd from Headboysmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmb3p = c.getString(0);
            buffer.append(""+wmb3p);
        }
        return buffer.toString();

    }
    public String getBhcm15p(String age){

        c=db.rawQuery("select P15th from Headboysmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmb15p = c.getString(0);
            buffer.append(""+wmb15p);
        }
        return buffer.toString();

    }
    public String getBhcm50p(String age){

        c=db.rawQuery("select P50th from Headboysmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String Wmb50p = c.getString(0);
            buffer.append(""+Wmb50p);
        }
        return buffer.toString();

    }
    public String getBhcm85p(String age){

        c=db.rawQuery("select P85th from Headboysmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmb85p = c.getString(0);
            buffer.append(""+wmb85p);
        }
        return buffer.toString();

    }
    public String getBhcm97p(String age){

        c=db.rawQuery("select P97th from Headboysmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmb97p = c.getString(0);
            buffer.append(""+wmb97p);
        }
        return buffer.toString();

    }
    public String getGhcw1p(String age){

        c=db.rawQuery("select P1st from Headgirlsweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmgonep = c.getString(0);
            buffer.append(""+wmgonep);
        }
        return buffer.toString();

    }
    public String getGhcw3p(String age){

        c=db.rawQuery("select P3rd from Headgirlsweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmg3p = c.getString(0);
            buffer.append(""+wmg3p);
        }
        return buffer.toString();

    }
    public String getGhcw15p(String age){

        c=db.rawQuery("select P15th from Headgirlsweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmg15p = c.getString(0);
            buffer.append(""+wmg15p);
        }
        return buffer.toString();

    }
    public String getGhcw50p(String age){

        c=db.rawQuery("select P50th from Headgirlsweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmg50p = c.getString(0);
            buffer.append(""+wmg50p);
        }
        return buffer.toString();

    }
    public String getGhcw85p(String age){

        c=db.rawQuery("select P85th from Headgirlsweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmg85p = c.getString(0);
            buffer.append(""+wmg85p);
        }
        return buffer.toString();

    }
    public String getGhcw97p(String age){

        c=db.rawQuery("select P97th from Headgirlsweeks where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wmg97p = c.getString(0);
            buffer.append(""+wmg97p);
        }
        return buffer.toString();

    }
    public String getGhcm1p(String age){

        c=db.rawQuery("select P1st from Headgirlsmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wwgonep = c.getString(0);
            buffer.append(""+wwgonep);
        }
        return buffer.toString();

    }
    public String getGhcm3p(String age){

        c=db.rawQuery("select P3rd from Headgirlsmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wwg3p = c.getString(0);
            buffer.append(""+wwg3p);
        }
        return buffer.toString();

    }
    public String getGhcm15p(String age){

        c=db.rawQuery("select P15th from Headgirlsmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wwg15p = c.getString(0);
            buffer.append(""+wwg15p);
        }
        return buffer.toString();

    }
    public String getGhcm50p(String age){

        c=db.rawQuery("select P50th from Headgirlsmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wwgfiftyp = c.getString(0);
            buffer.append(""+wwgfiftyp);
        }
        return buffer.toString();

    }
    public String getGhcm85p(String age){

        c=db.rawQuery("select P85th from Headgirlsmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wwg85p = c.getString(0);
            buffer.append(""+wwg85p);
        }
        return buffer.toString();

    }
    public String getGhcm97p(String age){

        c=db.rawQuery("select P97th from Headgirlsmonths where Age = '"+age+"'",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()){
            String wwg97p = c.getString(0);
            buffer.append(""+wwg97p);
        }
        return buffer.toString();

    }

    protected   DataPoint[] getDatawmb3p(){

        String[] columns = {"Age", "P3rd"};
        c = db.query("Boysweightmonths", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    protected   DataPoint[] getDatawmb15p(){

        String[] columns = {"Age", "P15th"};
        c = db.query("Boysweightmonths", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    protected   DataPoint[] getDatawmb50p(){

        String[] columns = {"Age", "P50th"};
        c = db.query("Boysweightmonths", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    protected   DataPoint[] getDatawmb85p(){

        String[] columns = {"Age", "P85th"};
        c = db.query("Boysweightmonths", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    protected   DataPoint[] getDatawmb97p(){

        String[] columns = {"Age", "P97th"};
        c = db.query("Boysweightmonths", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    protected   DataPoint[] getDatawmg3p(){

        String[] columns = {"Age", "P3rd"};
        c = db.query("Girlsweightmonths", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    protected   DataPoint[] getDatawmg15p(){

        String[] columns = {"Age", "P15th"};
        c = db.query("Girlsweightmonths", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    protected   DataPoint[] getDatawmg50p(){

        String[] columns = {"Age", "P50th"};
        c = db.query("Girlsweightmonths", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    //this method is used to get the datapoints for 85% girls weight gain per month  to draw a WHO chart showing 85% on the graph
    protected   DataPoint[] getDatawmg85p(){

        String[] columns = {"Age", "P85th"};//columns Age and p85th are used to get the datapoints of 'x' and 'y'axis
        c = db.query("Girlsweightmonths", columns, null, null, null,null,null,null);//query to get the values from Age and p85th columns of table "Girlsweightmonths"
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    protected   DataPoint[] getDatawmg97p(){

        String[] columns = {"Age", "P97th"};
        c = db.query("Girlsweightmonths", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    protected   DataPoint[] getDatahmb3p(){

        String[] columns = {"Age", "P3rd"};
        c = db.query("Hightmonthsboys" , columns , null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    protected   DataPoint[] getDatahmb15p(){

        String[] columns = {"Age", "P15th"};
        c = db.query("Hightmonthsboys" , columns , null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    protected   DataPoint[] getDatahmb50p(){

        String[] columns = {"Age", "P50th"};
        c = db.query("Hightmonthsboys" , columns , null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    protected   DataPoint[] getDatahmb85p(){

        String[] columns = {"Age", "P85th"};
        c = db.query("Hightmonthsboys" , columns , null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    protected   DataPoint[] getDatahmb97p(){

        String[] columns = {"Age", "P97th"};
        c = db.query("Hightmonthsboys" , columns , null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    protected   DataPoint[] getDatahmg3p(){

        String[] columns = {"Age", "P3rd"};
        c = db.query("Hightmonthsgirls" , columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    protected   DataPoint[] getDatahmg15p(){

        String[] columns = {"Age", "P15th"};
        c = db.query("Hightmonthsgirls", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    protected   DataPoint[] getDatahmg50p(){

        String[] columns = {"Age", "P50th"};
        c = db.query("Hightmonthsgirls", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    protected   DataPoint[] getDatahmg85p(){

        String[] columns = {"Age", "P85th"};
        c = db.query("Hightmonthsgirls", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    protected   DataPoint[] getDatahmg97p(){

        String[] columns = {"Age", "P97th"};
        c = db.query("Hightmonthsgirls", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    }
    protected   DataPoint[] getDatahcmb3p(){

        String[] columns = {"Age", "P3rd"};
        c = db.query("Headboysmonths", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    }
    protected   DataPoint[] getDatahcmb15p(){

        String[] columns = {"Age", "P15th"};
        c = db.query("Headboysmonths", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    }
    protected   DataPoint[] getDatahcmb50p(){

        String[] columns = {"Age", "P50th"};
        c = db.query("Headboysmonths", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    }
    protected   DataPoint[] getDatahcmb85p(){

        String[] columns = {"Age", "P85th"};
        c = db.query("Headboysmonths", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    }
    protected   DataPoint[] getDatahcmb97p(){

        String[] columns = {"Age", "P97th"};
        c = db.query("Headboysmonths", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    }
    protected   DataPoint[] getDatahcmg3p(){

        String[] columns = {"Age", "P3rd"};
        c = db.query("Headgirlsmonths", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    }
    protected   DataPoint[] getDatahcmg15p(){

        String[] columns = {"Age", "P15th"};
        c = db.query("Headgirlsmonths", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    }
    protected   DataPoint[] getDatahcmg50p(){

        String[] columns = {"Age", "P50th"};
        c = db.query("Headgirlsmonths", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    protected   DataPoint[] getDatahcmg85p(){

        String[] columns = {"Age", "P85th"};
        c = db.query("Headgirlsmonths", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };
    protected   DataPoint[] getDatahcmg97p(){

        String[] columns = {"Age", "P97th"};
        c = db.query("Headgirlsmonths", columns, null, null, null,null,null,null);
        DataPoint[] dp = new DataPoint[c.getCount()];
        for(int i = 0;i<c.getCount(); i++){
            c.moveToNext();
            dp[i]=new DataPoint(c.getInt(0), c.getDouble(1));

        }

        return dp;
    };



}

