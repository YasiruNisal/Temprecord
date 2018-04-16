package com.ublox.BLE.comms;


import com.ublox.BLE.utils.CRC16;
import com.ublox.BLE.utils.CommsChar;

import java.util.ArrayList;

/**
 * Created by Yasiru on 19-Dec-17.
 */

public class CommsSerial {

    private CRC16 crc = new CRC16();
    private CommsChar commsChar = new CommsChar();
    private BaseCMD baseCMD = new BaseCMD();

    public byte[] WriteByte(byte[] b){
        ArrayList<Byte> msgData   = new ArrayList<Byte>();
        for (int i = 0; i < b.length; i ++){
            msgData.add(b[i]);
        }

        Byte c = 0x00;
        for (int i = 0; i < b.length+1; i ++){
            c++;
        }
        msgData.add(1,c);

        byte[] temp = new byte[msgData.size()];
        int i = 0;
        for (Byte current : msgData) {
            temp[i] = current;
            i++;
        }

        crc.crc16_2(temp);
        msgData.add(crc.getCrcLo());
        msgData.add(crc.getCrcHi());
        crc.crc_reset();

        if(msgData.size() > 120){
            throw new ArrayIndexOutOfBoundsException();
        }

        int j = 0;
        while(j < msgData.size())
        {
            switch (msgData.get(j))
            {
                //Escape character
                case 0x1B:
                    msgData.remove(j);
                    msgData.add(j,commsChar.CHAR_ESC);
                    j++;
                    msgData.add(j, commsChar.CESC_ESC);
                    j++;
                    break;
                //Return character
                case 0x0D:
                    msgData.remove(j);
                    msgData.add(j,commsChar.CHAR_ESC);
                    j++;
                    msgData.add(j, commsChar.CESC_RET);
                    j++;
                    break;
                //Clear character
                case 0x55:
                    msgData.remove(j);
                    msgData.add(j,commsChar.CHAR_ESC);
                    j++;
                    msgData.add(j, commsChar.CESC_CLR);
                    j++;
                    break;
                //All other characters
                default:
                    j++;
                    break;
            }
        }

        msgData.add(commsChar.CHAR_RET);

        msgData.add(0, (byte)0x55);
        msgData.add(0, (byte)0x00);

        byte[] temp2 = new byte[msgData.size()];
        int k = 0;
        for (Byte current : msgData) {
            temp2[k] = current;
            k++;
        }

        baseCMD.BytetoHex(temp2);
        return temp2;
    }


}
