package com.ublox.BLE.comms;

import android.util.Log;

import com.ublox.BLE.Types.bitBool;
import com.ublox.BLE.utils.CommsChar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
    public static final String MT2STRING = "MonT2 ";
    /// <summary>Factory/Unallocated serial number prefix for Mon-T2</summary>
    public static final String LABPrefix = "XX";
    /// <summary>Factory/Unallocated/Unconfigured variant of the Mon-T2</summary>
    public static final byte LABVariant = 0;

    public String GetGeneration(int str){
        String Generation = "";
        if(str == 5){
            Generation =  "G5";
        }
        return Generation;
    }

    public String GetType(int str){
        String Type = "";
        if(str == 1){
            Type = "MonT-2 BLE";
        }

        return Type;
    }

    public String GetState(int str){
        String state = "";
        if(str == 0){
            state = "Sleeping";
        }else if(str == 1){
            state = "No Config";
        }else if(str == 2){
            state = "Ready";
        }else if(str == 3){
            state = "Start Delay";
        }else if(str == 4){
            state = "Running";
        }else if(str == 5){
            state = "Stopped";
        }else if(str == 6){
            state = "Reuse";
        }else if(str == 7){
            state = "Error";
        }else if(str == 8){
            state = "Unknown";
        }

        return state;
    }

    public String YesorNo(boolean in){
        String yesorno = "";
        if(in == true){
            yesorno =  "Yes";
        }else if(in == false){
            yesorno = "No";
        }
        return yesorno;
    }

    public String Startby(MT2ClassFlags.MT2_LoggerFlags data){
        String r = "";
        if(data.startDateTime.getValue()){
            r = "Date Time";
        }else if(data.buttonStart.getValue()){
            r = "Button";
        }else if(data.softwareStart.getValue()){
            r = "Software";
        }

        return r;
    }

    public String Stopby(MT2ClassFlags.MT2_LoggerFlags data){
        String r = "";
        if(data.buttonStop.getValue()){
            r = "Button";
        }else if(data.softwareStop.getValue()){
            r = "Software";
        }else if(data.sampleStop.getValue()){
            r = "Sample Count";
        }else if(data.fullStop.getValue()){
            r = "Memory Full";
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

    public String[] StringDatetoInt(String string){
        String[] parts = string.split(":|\\/|\\ ");
        return parts;
    }

    public Calendar getDatefromString(String date){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));


        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(d);

        return c;
    }

    public String calendertoString(Calendar cal){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        String formatted = sdf.format(cal.getTime());

        return formatted;
    }





}
