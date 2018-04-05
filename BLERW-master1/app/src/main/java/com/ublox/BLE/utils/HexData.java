package com.ublox.BLE.utils;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Yasiru on 15-Jan-18.
 */

public class HexData {

    public static final byte[] STAY_UP = {0x00, 0x55, 0x22, 0x02, (byte) 0xC9, 0x5D, 0x0D};
    public static final byte[] GO_TO_SLEEP = {0x00, 0x55, 0x23, 0x02, (byte) 0xF8, 0x6E, 0x0D};
    public static final byte[] START_L =  {0x00,0x55,0x04,0x02,(byte) 0x89,(byte) 0xF1,0x0D};
    public static final byte[] STOP_L = {0x00,0x55,0x06,0x02,(byte) 0xEB,(byte) 0x97,0x0D};
    public static final byte[] REUSE_L = {0x00,0x55,0x07,0x02,(byte) 0xDA,(byte) 0xA4,0x0D};
    public static final byte[] TAG_L = {0x00,0x55,0x05,0x02,(byte) 0xB8,(byte) 0xC2,0x0D};
    public static final byte[] QUARY = {0x00,0x55,0x01,0x02,(byte) 0x7C,(byte) 0x0E,0x0D};
    public static final byte[] RTC = {0x00,0x55,0x0B,0x03,(byte) 0x00,(byte) 0x3E, 0x69 ,0x0D};
    public static final byte[] BLE_ACK = {0x00, 0x55, 0x24, 0x02, (byte) 0x6F,(byte) 0xF7, 0x0D};


    public void BytetoHex(byte[] b){
        StringBuilder sb = new StringBuilder();
        for (byte b1 : b){
            sb.append(String.format("%02X ", b1));
        }
        Log.d("HEX", sb.toString());
    }

    public void BytetoHex2(ArrayList<Byte> b){
        StringBuilder sb = new StringBuilder();
        for (byte b1 : b){
            sb.append(String.format("%02X ", b1));
        }
        Log.d("HEX11", sb.toString());
    }
}