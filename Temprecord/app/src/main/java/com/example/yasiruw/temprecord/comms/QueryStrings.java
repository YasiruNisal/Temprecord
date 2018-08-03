package com.example.yasiruw.temprecord.comms;


import android.content.res.Resources;
import android.text.format.Time;
import android.util.Log;

import com.example.yasiruw.temprecord.App;
import com.example.yasiruw.temprecord.R;
import com.example.yasiruw.temprecord.activities.MainActivity;
import com.example.yasiruw.temprecord.utils.CommsChar;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

/**
 * Created by Yasiru on 14-Dec-17.
 */

public class QueryStrings {


    CommsChar commsChar = new CommsChar();
    /// <summary>The FLASH page size on the actual PIC18 device</summary>
    public static final int PAGESIZE = 512;
    /// <summary>The percentage at which we display a warning to the user that the battery is low</summary>
    public static final double BATTERYWARNINGLEVEL = 10.0;
    /// <summary>Factory default offset in MEM_EXTRA used for storing additional variables on top of default user parameter set.</summary>
    public static final int FACTUSEROFFSET = PAGESIZE - 398;//mt2 user bytesize
    /// <summary>Maximum ADC value + 1</summary>
    public static final int ADCMAX = 4096;
    /// <summary>The degrees C to Kelvin constant</summary>
    public static final double KELVIN = 273.15;
    /// <summary>Mon-T2 product string to add in front of model string</summary>
    public static final String MT2STRING = "MonT-2";
    /// <summary>Factory/Unallocated serial number prefix for Mon-T2</summary>
    public static final String LABPrefix = "XX";
    /// <summary>Factory/Unallocated/Unconfigured variant of the Mon-T2</summary>
    public static final byte LABVariant = 0;

    /// <summary> Mon-T2 Unknown/Unprogrammed device</summary>
    static final int  MT2_UNKNOWN = 0,
    /// <summary> Mon-T2 Plain</summary>
    MT2_95MPSYK = 1,
    /// <summary> Mon-T2 USB</summary>
    MT2_95MUSYK = 2,
    /// <summary> Mon-T2 USB RH</summary>
    MT2_95MRSYK = 3,
    /// <summary> Mon-T2 LCD</summary>
    MT2_95MPDYK = 4,
    /// <summary> Mon-T2 LCD USB</summary>
    MT2_95MUDYK = 5,
    /// <summary> Mon-T2 LCD USB RH</summary>
    MT2_95MRDYK = 6,
    /// <summary> Mon-T2 PDF</summary>
    MT2_95MUPYK = 7,
    /// <summary> Mon-T2 PDF RH</summary>
    MT2_95MRPYK = 8,
    /// <summary> Mon-T2 LCD PDF</summary>
    MT2_95MUUYK = 9,
    /// <summary> Mon-T2 LCD PDF RH</summary>
    MT2_95MRUYK = 10;

    TimeZone timeZone;
    static TimeZone entrytimeZone;

    public String GetGeneration(int str){
        String Generation = "";
        if(str == 5){
            Generation =  "MonT-2";
        }
        return Generation;
    }

    public String GetType(int model){
        String Type = "";
        switch (model)
        {
            case MT2_95MPSYK:
                return MT2STRING + " Plain";
            case MT2_95MUSYK:
                return MT2STRING + " Plain USB";
            case MT2_95MRSYK:
                return MT2STRING + " Plain RH USB";
            case MT2_95MPDYK:
                return MT2STRING + " LCD";
            case MT2_95MUDYK:
                return MT2STRING + " LCD USB";
            case MT2_95MRDYK:
                return MT2STRING + " LCD RH USB";
            case MT2_95MUPYK:
                return MT2STRING + " PDF";
            case MT2_95MRPYK:
                return MT2STRING + " RH PDF";
            case MT2_95MUUYK:
                return MT2STRING + " LCD PDF";
            case MT2_95MRUYK:
                return MT2STRING + " LCD RH PDF";
            case MT2_UNKNOWN:
            default:
                return "Unknown";
        }
    }

    public String GetState(int str){
        String state = "";
        if(str == 0){
            state = App.getContext().getString(R.string.Sleeping);
        }else if(str == 1){
            state = App.getContext().getString(R.string.NoConfig);
        }else if(str == 2){
            state = App.getContext().getString(R.string.Ready);
        }else if(str == 3){
            state = App.getContext().getString(R.string.Start_Delay);
        }else if(str == 4){
            state = App.getContext().getString(R.string.Running);
        }else if(str == 5){
            state = App.getContext().getString(R.string.Stopped);
        }else if(str == 6){
            state = App.getContext().getString(R.string.Reuse_);
        }else if(str == 7){
            state = App.getContext().getString(R.string.Error);
        }else if(str == 8){
            state = App.getContext().getString(R.string.Unknown);
        }

        return state;
    }

    public String YesorNo(boolean in){
        String yesorno = "";
        if(in){
            yesorno =  App.getContext().getString(R.string.Yes);
        }else if(!in){
            yesorno = App.getContext().getString(R.string.No);
        }
        return yesorno;
    }


    public String TrueorFalse(boolean in){
        String trueorfalse = "";
        if(in){
            trueorfalse =  App.getContext().getString(R.string.true_);
        }else if(!in){
            trueorfalse = App.getContext().getString(R.string.false_);
        }
        return trueorfalse;
    }

    public String Startby(MT2ClassFlags.MT2_LoggerFlags data){
        String r = "";
        if(data.startDateTime.getValue()){
            r = App.getContext().getString(R.string.DateTime);
        }else if(data.buttonStart.getValue()){
            r = App.getContext().getString(R.string.Button);
        }else if(data.softwareStart.getValue()){
            r = App.getContext().getString(R.string.Software);
        }

        return r;
    }

    public String Stopby(MT2ClassFlags.MT2_LoggerFlags data){
        String r = "";
        if(data.buttonStop.getValue()){
            r = App.getContext().getString(R.string.Button);
        }else if(data.softwareStop.getValue()){
            r = App.getContext().getString(R.string.Software);
        }else if(data.sampleStop.getValue()){
            r = App.getContext().getString(R.string.SampleCount);
        }else if(data.fullStop.getValue()){
            r = App.getContext().getString(R.string.MemoryFull);
        }

        return r;
    }

    public String Period(int mseconds){
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(tz);
        String time = df.format(new Date(mseconds));
        return time;
    }

    public String PMorAM(int num){
        String M = "";
        if(num == 1){
            M = "PM";
        }else if(num == 0){
            M = "AM";
        }

        return M;
    }

    public String imperial(boolean imperial){
        String M = "";
        if(!imperial){
            M = " °C";
        }else if(imperial){
            M = " °F";
        }

        return M;
    }

    public String returnF(String celsius){
        double fahrenheit;

        fahrenheit = 32 + (Double.parseDouble(celsius) * 9 / 5);

        return String.valueOf(fahrenheit);
    }

    public double returnC(double fahrenheit){
        return  ((fahrenheit - 32)*5)/9;
    }

    public Double returnFD(double celsius){
        double fahrenheit;

        fahrenheit = 32 + (celsius * 9 / 5);

        return fahrenheit;
    }

    public void set_default_timeZone(TimeZone timeZone){
        this.timeZone = timeZone;
    }

    public String UTCtoLocal(long now){
//        long now = 1486624863;
        //UTCtoLocalConvert();
        Date date = new java.util.Date(now);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
        sdf.setTimeZone(App.getInit_TimeZone());
        String formattedDate = sdf.format(date);


        return formattedDate;
    }

    public String UTCtoLocalParameters(long now){
//        long now = 1486624863;
        //UTCtoLocalConvert();
        Date date = new java.util.Date(now);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        sdf.setTimeZone(App.getInit_TimeZone());
        String formattedDate = sdf.format(date);


        return formattedDate;
    }

    public void UTCtoLocalConvert(){
        TimeZone.setDefault(App.getInit_TimeZone());
    }

    public void LocaltoUTC(){
        TimeZone entrytimeZone = TimeZone.getDefault();

        Calendar cal = Calendar.getInstance();
        TimeZone utcZone = TimeZone.getTimeZone("UTC");
        TimeZone.setDefault(utcZone);


       // Log.i("TIME", cal.getTime()+"");


//        utcZone = entrytimeZone;
//        TimeZone.setDefault(utcZone);
//
//        cal = Calendar.getInstance();
//
//        Log.i("TIME", cal.getTime()+"");
    }



    public String[] StringDatetoInt(String string){
        String[] parts = string.split(":|\\/|\\ ");
        //Log.i(TAG, parts.length + " " + parts[0] + "=============================");
        return parts;
    }

    public static Calendar toCalendar(Date date){
//        TimeZone utcZone = TimeZone.getTimeZone("UTC");
////        TimeZone.setDefault(utcZone);
        Calendar cal = Calendar.getInstance();
        TimeZone utcZone = TimeZone.getTimeZone("UTC");
        TimeZone.setDefault(utcZone);

        cal.setTime(date);

        //

        return cal;
    }

    public Date getDatefromString(String date){//used for start and stop in date time button in parameters

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        sdf.setTimeZone(App.getInit_TimeZone());



        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
//
//    public Calendar getCalenderfromString(String date, S){
//
//    }

    public String calendertoString(Calendar cal){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        String formatted = sdf.format(cal.getTime());

        return formatted;
    }

    public String datetoString(Date today){
        // Create an instance of SimpleDateFormat used for formatting
        // the string representation of date (month/day/year)
        DateFormat df = new SimpleDateFormat("Mdyyyyhms");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Get the date today using Calendar object.
        //today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        return df.format(today);
    }

    public boolean compareByte(byte[] one, byte[] two){
        if (Arrays.equals(one, two))
        {
            return true;
        }else{
            return false;
        }

    }

    public byte[] md5(String s) {
        byte[] reply = {0X00, 0X00,0X00,0X00,0X00,0X00,0X00,0X00};
        if(s != null) {
            try {
                // Create MD5 Hash
                MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                byte[] byteOfTextToHash = s.getBytes(StandardCharsets.UTF_16LE);
                byte messageDigest[] = digest.digest(byteOfTextToHash);
                // Create Hex String
                for(int i = 0 ; i < 8; i++){
                    reply[i] = messageDigest[i+4];
                }
                return reply;
                //BytetoHex(messageDigest);
                //System.out.print(hexString.toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }else{

            return reply;
        }
        return reply;
    }




}
