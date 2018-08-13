package com.temprecordapp.yasiruw.temprecord.comms;




import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

/**
 * Created by Yasiru on 19-Dec-17.
 */

public class CommsSerial {

    private CRC16 crc = new CRC16();
    private CommsChar commsChar = new CommsChar();


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
                    msgData.add(j, CommsChar.CESC_ESC);
                    j++;
                    break;
                //Return character
                case 0x0D:
                    msgData.remove(j);
                    msgData.add(j,commsChar.CHAR_ESC);
                    j++;
                    msgData.add(j, CommsChar.CESC_RET);
                    j++;
                    break;
                //Clear character
                case 0x55:
                    msgData.remove(j);
                    msgData.add(j,commsChar.CHAR_ESC);
                    j++;
                    msgData.add(j, CommsChar.CESC_CLR);
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

        BytetoHex(temp2);
        return temp2;
    }

    public byte[] ReadByte(byte[] rawBytes){
        byte[] b = new byte[0];
        int rawLength;
        List<Byte> msgData = new ArrayList<>();

        rawLength = rawBytes.length;
        byte[] crc = new byte[rawLength-2];


        //check valid data length
        if(rawBytes.length == 0){
            b = new byte[]{(byte)0xFF};
            return b;
        }
        //check string ends with newline charactor
        if(rawBytes[rawBytes.length-1] == commsChar.CHAR_RET){
            rawLength--;
        }

        for (int i = 0; i < (rawLength); i++) {
            //Check the byte
            if (rawBytes[i] == commsChar.CHAR_CLR) {
                //CLEAR byte is invalid if sent by logger!
                throw new IllegalArgumentException();
            } else if (rawBytes[i] == commsChar.CHAR_RET) {
                //RETURN byte should not be present as it's filtered out already
                throw new IllegalArgumentException();
            } else if (rawBytes[i] == commsChar.CHAR_ESC) {
                //We have an ESCAPE character, handle it
                i++;
                switch (rawBytes[i]) {

                    case 0x00://need to add the proper constants from the commsChar class
                        msgData.add(commsChar.CHAR_ESC);
                        break;
                    case 0x01:
                        msgData.add(commsChar.CHAR_RET);
                        break;
                    case 0x02:
                        msgData.add(commsChar.CHAR_CLR);
                        break;
                    default:
                        break;
                }
            } else {
                msgData.add(rawBytes[i]);
            }
        }


        //remove CRC data
        msgData.remove(msgData.size()-1);
        msgData.remove(msgData.size()-1);


        if(msgData.size() != msgData.get(1)){
            try {
                throw new DataFormatException();
            } catch (DataFormatException e) {
                e.printStackTrace();
            }
        }else{
            msgData.remove(1);
        }
        b = new byte[msgData.size()];
        for(int  i = 0; i < msgData.size() ; i++) {
            b[i] = msgData.get(i).byteValue();
        }

        //Log.d("INFO", msgData.get(1) +" "+b[1]+ "------------ " + msgData.size() +" "+ b.length);

        //BytetoHex(b);

        return b;

    }


    public void BytetoHex(byte[] b){
        StringBuilder sb = new StringBuilder();
        for (byte b1 : b){
            sb.append(String.format("%02X ", b1));
        }
        //Log.d("HEX", sb.toString());
    }

    public byte[] int2byte(int[]src) {
        int srcLength = src.length;
        byte[]dst = new byte[srcLength << 2];

        for (int i=0; i<srcLength; i++) {
            int x = src[i];
            int j = i << 2;
            dst[j++] = (byte) ((x) & 0xff);
            dst[j++] = (byte) ((x >>> 8) & 0xff);
            dst[j++] = (byte) ((x >>> 16) & 0xff);
            dst[j++] = (byte) ((x >>> 24) & 0xff);
        }
        return dst;
    }

    public byte[] convertIntegersToBytes (int[] integers) {
        if (integers != null) {
            //Log.i("INT", Arrays.toString(integers));
            byte[] outputBytes = new byte[integers.length * 4];
            byte[] outputarray = new byte[integers.length];

            for(int i = 0, k = 0; i < integers.length; i++) {
                int integerTemp = integers[i];
                for(int j = 0; j < 4; j++, k++) {
                    outputBytes[k] = (byte)((integerTemp >> (8 * j)) & 0xFF);
                }
            }

            for(int l = 0, m = 0; l < outputBytes.length; l = l + 4, m++){
                outputarray[m] = outputBytes[l];
            }
            return outputarray;
        } else {
            return null;
        }
    }

    public byte[] ReadUSBByte(byte[] rawBytes){
        //Log.i("USB","IN READ USB++++++++");
        //BytetoHex(rawBytes);
        byte[] b = new byte[0];
        int rawLength;
        List<Byte> msgData = new ArrayList<>();

        rawLength = rawBytes.length;
        byte[] crc = new byte[rawLength-2];


        //check valid data length
        if(rawBytes.length == 0){
            b = new byte[]{(byte)0xFF};
            return b;
        }
        //check string ends with newline charactor
        if(rawBytes[rawBytes.length-1] == commsChar.CHAR_RET){
            rawLength--;
        }

        for (int i = 0; i < (rawLength); i++) {
            //Check the byte

                msgData.add(rawBytes[i]);

        }


//        //remove CRC data
//        msgData.remove(msgData.size()-1);
//        msgData.remove(msgData.size()-1);

        //add 1 cause we removed the 0D above
        if(msgData.size() != msgData.get(1)){
            try {
                throw new DataFormatException();
            } catch (DataFormatException e) {
                e.printStackTrace();
            }
        }else{
            msgData.remove(1);
        }
        b = new byte[msgData.size()];
        for(int  i = 0; i < msgData.size() ; i++) {
            b[i] = msgData.get(i).byteValue();
        }

        //Log.d("INFO", msgData.get(1) +" "+b[1]+ "------------ " + msgData.size() +" "+ b.length);
        //Log.i("USB", "IN USB READ DATA");
       // BytetoHex(b);

        return b;

    }

    public byte[] WriteUSBByte(byte[] b){
        ArrayList<Byte> msgData   = new ArrayList<>();
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

//        crc.crc16_2(temp);
//        msgData.add(crc.getCrcLo());
//        msgData.add(crc.getCrcHi());
//        crc.crc_reset();

        if(msgData.size() > 64){
            throw new ArrayIndexOutOfBoundsException();
        }

        int j = 0;
        while(j < msgData.size())
        {
            switch (msgData.get(j))
            {
//                //Escape character
//                case 0x1B:
//                    msgData.remove(j);
//                    msgData.add(j,commsChar.CHAR_ESC);
//                    j++;
//                    msgData.add(j, commsChar.CESC_ESC);
//                    j++;
//                    break;
//                //Return character
//                case 0x0D:
//                    msgData.remove(j);
//                    msgData.add(j,commsChar.CHAR_ESC);
//                    j++;
//                    msgData.add(j, commsChar.CESC_RET);
//                    j++;
//                    break;
//                //Clear character
//                case 0x55:
//                    msgData.remove(j);
//                    msgData.add(j,commsChar.CHAR_ESC);
//                    j++;
//                    msgData.add(j, commsChar.CESC_CLR);
//                    j++;
//                    break;
                //All other characters
                default:
                    j++;
                    break;
            }
        }

        msgData.add(commsChar.CHAR_RET);

//        msgData.add(0, (byte)0x55);
//        msgData.add(0, (byte)0x00);

        byte[] temp2 = new byte[msgData.size()];
        int k = 0;
        for (Byte current : msgData) {
            temp2[k] = current;
            k++;
        }

       // BytetoHex(temp2);
        return temp2;
    }


}
