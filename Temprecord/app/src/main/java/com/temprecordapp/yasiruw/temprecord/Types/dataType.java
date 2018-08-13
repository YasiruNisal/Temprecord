package com.temprecordapp.yasiruw.temprecord.Types;

import android.util.Log;

import com.temprecordapp.yasiruw.temprecord.App;
import com.temprecordapp.yasiruw.temprecord.CustomLibraries.Yasiru_Temp_Library;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Yasiru on 23/02/2018.
 */

public class dataType {

    public static Yasiru_Temp_Library QS = new Yasiru_Temp_Library();


    public static class dateRtc extends baseType {

        public Calendar value = QS.toCalendar(new Date());

        /// <summary>
        /// The RTC DateTime fits within 6 bytes on the device
        /// </summary>
        public static final int ByteSize = 6;

        /// <summary>
        /// The device's RTC can only store values above year 2000.
        /// </summary>
        public static final int YEAREPOCH = 2000;

        public dateRtc(){
            super(ByteSize);
            value.set(YEAREPOCH,1,1);
        }

        public dateRtc(Calendar val){
            super(ByteSize);
            value = val;
        }

        public void BytetoHex(byte[] b){
            StringBuilder sb = new StringBuilder();
            for (byte b1 : b){
                sb.append(String.format("%02X ", b1));

            }
            //Log.d("HEX", sb.toString());
        }

        public void BytetoHex2(ArrayList<Byte> b){
            StringBuilder sb = new StringBuilder();
            for (byte b1 : b){
                sb.append(String.format("%02X ", b1));
            }
            Log.d("HEX11", sb.toString());
        }

        public dateRtc(ArrayList<Byte> data){
            super(ByteSize);


            if(data.get(0) == (byte)0xFF && data.get(1) == (byte)0xFF && data.get(2) == (byte)0xFF && data.get(3) == (byte)0xFF && data.get(4) == (byte)0xFF && data.get(5) == (byte)0xFF){
//                Date d = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
//                value = QS.toCalendar(d);
                //value.setTimeZone(TimeZone.getTimeZone("UTC"));
                //value.get(Calendar.DATE);
                //Log.d("******2", " " + value);
            }else{
//                int rtcStruct = data.get(0) & 0x01;
//                if (rtcStruct == 1)
//                {
                    //RTC Structure
                    int year = (data.get(0) >> 1 ) + YEAREPOCH;
                    int weekday = (data.get(1) & 0x0F); //Not really used but good for debug
                    int month = (data.get(1) >> 4)-1;
                    //value.setTimeZone(TimeZone.getDefault());
//                    TimeZone utcZone = TimeZone.getTimeZone("GMT");
//                    value.setTimeZone(utcZone);
                    //TimeZone.setDefault(TimeZone.getDefault());
                    value.set(year, month, data.get(2), data.get(3), data.get(4), data.get(5));
//                    Log.d("HEX11", year +" === " + data.get(1) + " " + value.getTime());
//                    BytetoHex2(data);

                    //QS.UTCtoLocal(value.getTime().getTime()/1000L);
                    //QS.LocaltoUTC();
                    //QS.UTCtoLocal(value.getTime().getTime());


//                }
//                else
//                {
//                    //Ticks
//                    //dataOut.RemoveRange(0, 2);
//                    //4 bytes remainng are Tick count
//
//                    value = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//                    Log.d("******4", " " + value);
//                }
            }
            for(int i = 0; i < 6; i++)
            data.remove(0);
        }

        @Override
        public void ToByte(ArrayList<Byte> data) {
            byte dataByte;


            if(value.get(Calendar.YEAR) > YEAREPOCH){
                dataByte = (byte) (value.get(Calendar.YEAR) - YEAREPOCH);
            }else{
                dataByte = (byte) (value.get(Calendar.YEAR) % 100);
            }
            dataByte = (byte)((dataByte << 1) + 1);
            //dataByte = (byte)((dataByte));
            data.add(dataByte);

            dataByte = (byte)((value.get(Calendar.MONTH)+1 )<< 4 );
            dataByte += (byte)(value.get(Calendar.DAY_OF_WEEK));
            data.add(dataByte);

//            data.add((byte)(value.get(Calendar.DAY_OF_WEEK)));
//            data.add((byte)(value.get((Calendar.MONTH))+1));
            data.add((byte)(value.get(Calendar.DATE)));
            data.add((byte)(value.get(Calendar.HOUR)));
            data.add((byte)(value.get(Calendar.MINUTE)));
            data.add((byte)(value.get(Calendar.SECOND)));
            //BytetoHex2(data);
//            Log.d("******3", " " + (data.get(2)));
//                    Log.d("******3", " " + data.get(3));
//                    Log.d("******3", " " + data.get(4));
//                    Log.d("******3", " " + data.get(5));
//                    Log.d("******3", " " + data.get(6));
//                    Log.d("******3", " " + data.get(7));
        }

        public Date getValue(){
            return value.getTime();
        }



    }

}
