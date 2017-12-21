package youten.redo.yasiru.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

/**
 * Created by Yasiru on 13-Dec-17.
 */

public class BaseCMD {

    private CommsChar commsChar = new CommsChar();


    public byte[] ReadByte(byte[] rawBytes){
        byte[] b = new byte[0];
        int rawLength;
        List<Byte> msgData = new ArrayList<Byte>();

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

        //crc check sum is not done at this stage cause when the data comes it is 100% of the time correct (so far) because BLE has its own checksums
//		for(int  i = 0; i < msgData.size() - 2; i++){
//			crc[i] = msgData.get(i);
//
//		}

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
        Log.d("HEX", sb.toString());
    }


    public int CMDStart(byte[] start){
        if(start[0] == commsChar.CMD_ACK) {
            //create a popup window and sound
            return 1;
        }else{
            return 0;
        }

    }

    public int CMDStop(byte[] stop){
        return CheckLoginMessage(stop);
    }

    public int CMDTag(byte[] tag){
        if(tag[0] == commsChar.CMD_ACK){
            return 1;
        }else if((tag.length >= 3)  && (tag[2] == commsChar.NAK_USER)){
            return 2;
        }
        return 0;
    }

    public int CMDReuse(byte[] reuse){

        return CheckLoginMessage(reuse);
    }

    private int CheckLoginMessage(byte[] msg){
        if (msg[0] == (byte)commsChar.CMD_ACK) {
            return 1;
        }
        else if ((msg.length >= 2) && (msg[1] == (byte)commsChar.NAK_LOGIN)) {
            return 2;
        }
        return 0;
    }

    public ArrayList<String> CMDQuery(byte[] query){
        classMessages.Msg_Query msg = null;
        ArrayList<String> returndata = new ArrayList<String>();
        if(query[0] == commsChar.CMD_ACK){
            ArrayList<Byte> data = new ArrayList<Byte>();
            for(int  i = 0; i < query.length; i++) data.add(query[i]);

            data.remove(0);
            data.remove(0);

            if(data.size() == classMessages.Msg_Query.ByteSize){
                msg = new classMessages.Msg_Query(data);


                returndata.add(msg.serial_12.ToString());
                returndata.add(msg.firmware.ToString());
                returndata.add(msg.type.getGenString());
                returndata.add(msg.type.getTyString());
//                Log.d("INFO1", msg.serial_12.ToString()+"++++++++++");
//                Log.d("INFO2", msg.firmware.ToString()+"++++++++++");
//                Log.d("INFO3", msg.type.getGenString()+"++++++++++");
//                Log.d("INFO3", msg.type.getTyString()+"++++++++++");
            }



        }
        return returndata;
    }

    public ArrayList<String> CMDState(byte[] state){

        classMessages.Msg_Bat_state msg = null;
        ArrayList<String> returndata = new ArrayList<String>();
        if(state[0] == commsChar.CMD_ACK){
            ArrayList<Byte> data = new ArrayList<Byte>();
            for(int  i = 0; i < state.length; i++) data.add(state[i]);

            data.remove(0);
            data.remove(0);
            data.remove(0);
            data.remove(0);
            data.remove(0);
            data.remove(0);

            if(data.size() == classMessages.Msg_Bat_state.ByteSize){

                msg = new classMessages.Msg_Bat_state(data);
                returndata.add(msg.state.ToString());
                returndata.add(msg.battery.ToString());
//                Log.d("INFO1", msg.state.ToString()+"++++++++++");
//                Log.d("INFO2", msg.battery.ToString()+"++++++++++");

            }
        }
        return returndata;
    }
}
