package com.example.yasiruw.temprecord.Types;

import android.util.Log;

import com.example.yasiruw.temprecord.comms.QueryStrings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

/**
 * Created by Yasiru on 23/02/2018.
 */

public class dataType {

    static QueryStrings QS = new QueryStrings();


    public static class dateRtc extends baseType {

        public Calendar value = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

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

        public dateRtc(ArrayList<Byte> data){
            super(ByteSize);


            if(data.get(0) == (byte)0xFF && data.get(1) == (byte)0xFF && data.get(2) == (byte)0xFF && data.get(3) == (byte)0xFF && data.get(4) == (byte)0xFF && data.get(5) == (byte)0xFF){
                Date d = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
                value = QS.toCalendar(d);
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
                   // value = Calendar.getInstance().set(year, month, data.get(2), data.get(3), data.get(4), data.get(5));
//                    Log.d("******6", " " + value.getTimeZone());
//                    Log.d("******6", " " + year);
//                    Log.d("******6", " " + month);
//                    Log.d("******6", " " + data.get(2));
//                    Log.d("******6", " " + data.get(3));
//                    Log.d("******6", " " + data.get(4));

                    value.set(year, month, data.get(2), data.get(3), data.get(4), data.get(5));
                    value.setTimeZone(TimeZone.getTimeZone("UTC"));

                    Log.d("******3", " " + value.getTime().getTime());
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
            Log.i(TAG,"Date value " + value);

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
