package com.ublox.BLE.Types;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Yasiru on 23/02/2018.
 */

public class dataType {


    public static class dateRtc extends baseType {

        public Calendar value = Calendar.getInstance();

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
                value = Calendar.getInstance();
                //value.get(Calendar.DATE);
                //Log.d("******2", " " + value);
            }else{
//                int rtcStruct = data.get(0) & 0x01;
//                if (rtcStruct == 1)
//                {
                    //RTC Structure
                    int year = (data.get(0) ) + YEAREPOCH;
                    int weekday = (data.get(1) & 0x0F); //Not really used but good for debug
                    int month = (data.get(1) >> 4);
                    //value = Calendar.getInstance().set(year, month, data.get(2), data.get(3), data.get(4), data.get(5));
                    Log.d("******3", " " + (data.get(0) >> 1));
                    Log.d("******3", " " + year);
                    Log.d("******3", " " + month);
                    Log.d("******3", " " + data.get(2));
                    Log.d("******3", " " + data.get(3));
                    Log.d("******3", " " + data.get(4));

                    value.set(year, month, data.get(2), data.get(3), data.get(4), data.get(5));

                    Log.d("******3", " " + value);
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
            //dataByte = (byte)((dataByte));
            data.add(dataByte);

            dataByte = (byte)((value.get((Calendar.MONTH)) << 4) +1);
            dataByte += (byte)(value.get(Calendar.DAY_OF_WEEK));
            data.add(dataByte);
            data.add((byte)(value.get(Calendar.DAY_OF_MONTH)));
            data.add((byte)(value.get(Calendar.HOUR_OF_DAY)));
            data.add((byte)(value.get(Calendar.MINUTE)));
            data.add((byte)(value.get(Calendar.SECOND)));
        }

        public Calendar getValue(){
            return value;
        }



    }

}
