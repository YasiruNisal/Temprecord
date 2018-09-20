package com.temprecord.temprecordapp.comms;



import com.temprecord.temprecordapp.Types.dataType;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Yasiru on 16/03/2018.
 */

public class MT2Msg_Write {

    private CommsChar commsChar = new CommsChar();
    private BaseCMD basecmd = new BaseCMD();
    private byte[] data;
    private ArrayList<Byte> writedata = new ArrayList<Byte>();
    private boolean done = false;
    int msgNo = 0;
    //Size in bytes to fill each time
    int size = 112;

    public  MT2Msg_Write(){

    }

    public void MT2Msg_WriteBLE(byte[] data){
        //Message number starts with 0 and increments every fill
        msgNo = 0;
        //Size in bytes to fill each time
        size = 112;

        for(int j = 0; j < data.length; j++){
            writedata.add(data[j]);
        }
    }

    public void MT2Msg_WriteUSB(byte[] data){
        //Message number starts with 0 and increments every fill
        msgNo = 0;
        //Size in bytes to fill each time
        size = 48;

        for(int j = 0; j < data.length; j++){
            writedata.add(data[j]);
        }
    }



    public byte[] writeSetup(){
        byte[] msg = new byte[]
                {
                        (byte)commsChar.CMD_WRITE,
                        (byte)commsChar.MEM_CLEAR,
                        (byte)commsChar.MEM_USER,
                };

        return msg;
    }

    public byte[] writeFill(){
        ArrayList<Byte> listmsg = new ArrayList<Byte>();
        listmsg.add(commsChar.CMD_WRITE);
        listmsg.add(commsChar.MEM_FILL);
        listmsg.add((byte) msgNo);
        listmsg.add((byte) size);
        for (int i = 0; i < size; i++) {
            listmsg.add(listmsg.size(), writedata.get(i));
        }

        for (int i = 0; i < size; i++) {
            writedata.remove(0);
        }

        msgNo++;
        if (size > writedata.size()) {
            size = writedata.size();
        }
        int i = 0;
        byte[] msg = new byte[listmsg.size()];
        for (Byte current : listmsg) {
            msg[i] = current;
            i++;
        }

        //basecmd.BytetoHex(msg);
        return msg;
    }

    public boolean writeDone(){
        if(writedata.size() == 0){
            return true;
        }
        return false;
    }

    public byte[] writeFlash(){
        //Confirm write to flash message
        byte[] msg = new byte[]
                {
                        (byte)commsChar.CMD_WRITE,
                        (byte)commsChar.MEM_WRITE,
                        (byte)commsChar.MEM_USER,
                };

        return msg;
    }


    public byte[] WriteRTC(){

       ArrayList<Byte> date = new ArrayList<Byte>();

       date.add(commsChar.CMD_RTC);
       date.add(commsChar.RTC_SET);
       new dataType.dateRtc(Calendar.getInstance()).ToByte(date);

        int i = 0;
        byte[] rd = new byte[data.length];
        for (Byte current : data) {
            rd[i] = current;
            i++;
        }

        return rd;
    }

}

