package com.ublox.BLE.comms;

import android.util.Log;

import com.ublox.BLE.Types.dataType;
import com.ublox.BLE.utils.CHUserData;
import com.ublox.BLE.utils.CommsChar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.zip.DataFormatException;

/**
 * Created by Yasiru on 13-Dec-17.
 */

public class BaseCMD {

    private CommsChar commsChar = new CommsChar();
    private QueryStrings queryStrings = new QueryStrings();

    public boolean ImperialUnit;
    public boolean LoopOverwrite;
    public boolean StartwithButton;
    public boolean StopwithButton;
    public boolean ReUsewithButton;
    public boolean AllowplacingTags;
    public boolean EnableLCDMenu;
    public boolean ExtendedLCDMenu;
    public boolean passwordEnabled;
    public boolean StartonDateTime;
    public boolean StartwithDelay;
    public boolean StopwhenFull;
    public boolean StoponSample;
    public boolean StoponDateTime;
    public boolean ch1limitEnabled;
    public boolean ch2limitEnabled;
    public String Password;
    public Calendar startDateTime;
    public Calendar stoponDateTime;
    public int startDelay;
    public int samplePeriod;
    public int startdatetime;
    public String password;
    public int ch1Hi;
    public int numberstopon;
    public int ch1Lo;
    public int ch2Hi;
    public int ch2Lo;
    public int numberofsamples;
    public boolean ch1Enable;
    public boolean ch2Enable;
    public int commentTextsize;
    public Calendar timestartstopdatetime;
    public int state;
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

        BytetoHex(b);

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
            //Log.d("INFO1", data.size()+"++++++++++");
            if(data.size() == classMessages.Msg_Query.ByteSize){
                msg = new classMessages.Msg_Query(data);


                returndata.add(msg.serial_12.ToString());
                returndata.add(msg.firmware.ToString());
                returndata.add(msg.type.getGenString());
                returndata.add(msg.type.getTyString());
                returndata.add(msg.type.getVarientString());
                returndata.add(msg.state.ToString());
                returndata.add(msg.battery.ToString());
                state = msg.state.value;
//                Log.d("INFO1", msg.serial_12.ToString()+"++++++++++");
//                Log.d("INFO2", msg.firmware.ToString()+"++++++++++");
//                Log.d("INFO3", msg.type.getGenString()+"++++++++++");
//                Log.d("INFO3", msg.type.getTyString()+"++++++++++");
//                Log.d("INFO3", msg.type.getVarientString()+"++++++++++");
//                Log.d("INFO1", msg.state.ToString()+"++++++++++");
//                Log.d("INFO2", msg.battery.ToString()+"++++++++++");
            }



        }
        return returndata;
    }

    public ArrayList<String> CMDFlash(byte[] flash){
        //dont need to check if the first byte is ACk here since its already done before putting data in here
        ArrayList<Byte> data = new ArrayList<Byte>();
        ArrayList<String> returndata = new ArrayList<String>();
        classMessages.TWFlash msg = null;
        for(int  i = 0; i < flash.length; i++) data.add(flash[i]);
        //removing the first 46 bytes since its used for usbproductstring. we can always add it later on
        for (int i = 0; i < 46; i++) data.remove(0);

        //Log.d("******0", " " + data.size());
        if(data.size() == classMessages.TWFlash.ByteSize){
            msg = new classMessages.TWFlash(data);
            msg.dateRtc.getValue();
            String manudate = "" + msg.dateRtc.getValue().get(Calendar.YEAR)+"/"+(msg.dateRtc.getValue().get(Calendar.MONTH)+1)+"/"+msg.dateRtc.getValue().get(Calendar.DAY_OF_MONTH
            )+" "+ msg.dateRtc.getValue().get(Calendar.HOUR)+":"+msg.dateRtc.getValue().get(Calendar.MINUTE)+":"+msg.dateRtc.getValue().get(Calendar.SECOND)+" "+ queryStrings.PMorAM(msg.dateRtc.getValue().get(Calendar.AM_PM));


            returndata.add(manudate);
            returndata.add(""+msg.uval16.getValue()+"");
            //Log.d("******0", " " + msg.uval16.getValue());

        }
        return returndata;
    }

    public ArrayList<String> CMDRamRead(byte[] ram){
        //dont need to check if the first byte is ACk here since its already done before putting data in here
        ArrayList<Byte> data = new ArrayList<Byte>();
        ArrayList<String> returndata = new ArrayList<String>();
        classMessages.RamRead msg = null;
        for(int  i = 0; i < ram.length; i++) data.add(ram[i]);

        //removing the first two bytes since we dont need to dispaly the infomation these contain
        for (int i = 0; i < 2; i++) data.remove(0);

        if(data.size() == classMessages.RamRead.ByteSize)
            msg = new classMessages.RamRead(data);

            returndata.add(queryStrings.YesorNo(msg.mt2_loggerFlags.startDateTime.getValue()));
            returndata.add(queryStrings.YesorNo(msg.mt2_loggerFlags.buttonStart.getValue()));
            returndata.add(queryStrings.Startby(msg.mt2_loggerFlags));
            returndata.add(queryStrings.YesorNo(msg.mt2_loggerFlags.buttonStop.getValue()));
            returndata.add(queryStrings.Stopby(msg.mt2_loggerFlags));
            returndata.add(queryStrings.YesorNo(msg.mt2_loggerFlags.fullStop.getValue()));
            returndata.add(queryStrings.YesorNo(msg.mt2_loggerFlags.sampleStop.getValue()));
            returndata.add(msg.uval32.getValue()+"");//8
            numberofsamples = msg.uval32.getValue();
            msg.dateRtc.getValue();
            String manudate = "" + msg.dateRtc.getValue().get(Calendar.YEAR)+"/"+(msg.dateRtc.getValue().get(Calendar.MONTH)+1)+"/"+msg.dateRtc.getValue().get(Calendar.DAY_OF_MONTH
            )+" "+ msg.dateRtc.getValue().get(Calendar.HOUR)+":"+msg.dateRtc.getValue().get(Calendar.MINUTE)+":"+msg.dateRtc.getValue().get(Calendar.SECOND)+" "+ queryStrings.PMorAM(msg.dateRtc.getValue().get(Calendar.AM_PM));

            returndata.add(manudate);
            if((msg.mt2_chRam.chValue.getDoubleValue()/10) > 3000){
                returndata.add("0");
                returndata.add("0");
                returndata.add("0");
            }else {
                returndata.add("" + (msg.mt2_chRam.chValue.getDoubleValue() / 10));
                returndata.add("" + (msg.mt2_chRam.valHi.getDoubleValue() / 10));
                returndata.add("" + (msg.mt2_chRam.valLo.getDoubleValue() / 10));//12
            }
            returndata.add("" + msg.mt2_chRam.delayHi.value);//12 th element is the return arraylist

            if((msg.mt2_chRam1.chValue.getDoubleValue()) > 3000) {
                returndata.add("0");
                returndata.add("0");
                returndata.add("0");
            }else{
                returndata.add("" + (msg.mt2_chRam1.chValue.getDoubleValue()));
                returndata.add("" + (msg.mt2_chRam1.valHi.getDoubleValue()));
                returndata.add("" + (msg.mt2_chRam1.valLo.getDoubleValue()));
            }
            returndata.add("" + msg.mt2_chRam1.delayHi.value);
            returndata.add(""+msg.batLife.value);//17
            returndata.add(""+msg.samplePointer.getValue());
            //Log.d("%%%%%0", " " +(msg.mt2_chRam1.chValue.getDoubleValue()) + " " + (msg.mt2_chRam1.valHi.getDoubleValue()) + " " + (msg.mt2_chRam1.valLo.getDoubleValue()) + " " + msg.mt2_chRam1.delayHi.value);//);


        return returndata;
    }

    public ArrayList<String> CMDUserRead(byte[] user){
        ArrayList<Byte> data = new ArrayList<Byte>();
        ArrayList<String> returndata = new ArrayList<String>();
        classMessages.MT2_USERFlash msg = null;
        for(int  i = 0; i < user.length; i++) data.add(user[i]);

        //removing the first two bytes since we dont need to dispaly the infomation these contain
        for (int i = 0; i < 2; i++) data.remove(0);

        if(data.size() == classMessages.MT2_USERFlash.ByteSize){
            msg = new classMessages.MT2_USERFlash(data);
            returndata.add(msg.tripSamples.getValue()+"");
            //Log.d("******0", msg.tripSamples.getValue()+"");

            startDateTime = msg.dateStarted.getValue();//**********************************************
            String dstart = "" + msg.dateStarted.getValue().get(Calendar.YEAR)+"/"+(msg.dateStarted.getValue().get(Calendar.MONTH)+1)+"/"+msg.dateStarted.getValue().get(Calendar.DAY_OF_MONTH
            )+" "+ msg.dateStarted.getValue().get(Calendar.HOUR)+":"+msg.dateStarted.getValue().get(Calendar.MINUTE)+":"+msg.dateStarted.getValue().get(Calendar.SECOND)+" "+ queryStrings.PMorAM(msg.dateStarted.getValue().get(Calendar.AM_PM));
            returndata.add(dstart);
            String dstop = "" + msg.dateStopped.getValue().get(Calendar.YEAR)+"/"+(msg.dateStopped.getValue().get(Calendar.MONTH)+1)+"/"+msg.dateStopped.getValue().get(Calendar.DAY_OF_MONTH
            )+" "+ msg.dateStopped.getValue().get(Calendar.HOUR)+":"+msg.dateStopped.getValue().get(Calendar.MINUTE)+":"+msg.dateStopped.getValue().get(Calendar.SECOND)+" "+ queryStrings.PMorAM(msg.dateStopped.getValue().get(Calendar.AM_PM));
            returndata.add(dstop);

            returndata.add(msg.totalTrips.getValue()+"");
            returndata.add(msg.totalSamples.getValue()+"");
            returndata.add(msg.totalLoggedSamples.getValue()+"");//5

            returndata.add(queryStrings.YesorNo(msg.mt2_userData.mt2_userFlags.LoopOverWrite()));
            returndata.add(queryStrings.YesorNo(msg.mt2_userData.mt2_userFlags.LCDMenu()));
            returndata.add(queryStrings.YesorNo(msg.mt2_userData.mt2_userFlags.AllowTags()));
            returndata.add(queryStrings.YesorNo(msg.mt2_userData.mt2_userFlags.ButtonReuse()));
            returndata.add(queryStrings.YesorNo(msg.mt2_userData.mt2_userFlags.PasswordEnabled()));
            returndata.add(queryStrings.YesorNo(msg.mt2_userData.mt2_userFlags.StoponDate()));
            returndata.add(queryStrings.YesorNo(msg.mt2_userData.mt2_userFlags.ExtendedMenu()));//12

            ImperialUnit = msg.mt2_userData.mt2_userFlags.ImperialUnit();
            LoopOverwrite = msg.mt2_userData.mt2_userFlags.LoopOverWrite();
            StartwithButton = msg.mt2_userData.mt2_userFlags.ButtonStart1();
            StopwithButton = msg.mt2_userData.mt2_userFlags.ButtonStop();
            ReUsewithButton = msg.mt2_userData.mt2_userFlags.ButtonReuse();
            AllowplacingTags = msg.mt2_userData.mt2_userFlags.AllowTags();
            EnableLCDMenu = msg.mt2_userData.mt2_userFlags.LCDMenu();
            ExtendedLCDMenu = msg.mt2_userData.mt2_userFlags.ExtendedMenu();
            passwordEnabled = msg.mt2_userData.mt2_userFlags.PasswordEnabled();
            StartonDateTime = msg.mt2_userData.mt2_userFlags.StartDateTime();
            StopwhenFull = msg.mt2_userData.mt2_userFlags.StoponFull();
            StoponDateTime = msg.mt2_userData.mt2_userFlags.StoponDate();
            StoponSample = msg.mt2_userData.mt2_userFlags.StoponSample();

            returndata.add(queryStrings.YesorNo(msg.mt2_userData.mt2_chUser.CHEnable()));
            ch1Enable = msg.mt2_userData.mt2_chUser.CHEnable();
            returndata.add(queryStrings.YesorNo(msg.mt2_userData.mt2_chUser.CHLimits()));
            ch1limitEnabled = msg.mt2_userData.mt2_chUser.CHLimits();
            returndata.add((double)(msg.mt2_userData.mt2_chUser.alarm_Hi.getValue())/10+"");
            ch1Hi = msg.mt2_userData.mt2_chUser.alarm_Hi.getValue();
            returndata.add((double)(msg.mt2_userData.mt2_chUser.alarm_Lo.getValue())/10+"");
            ch1Lo = msg.mt2_userData.mt2_chUser.alarm_Lo.getValue();
            returndata.add(msg.mt2_userData.mt2_chUser.delay.value+"");//17

            returndata.add(queryStrings.YesorNo(msg.mt2_userData.mt2_chUser2.CHEnable()));
            ch2Enable = msg.mt2_userData.mt2_chUser2.CHEnable();
            returndata.add(queryStrings.YesorNo(msg.mt2_userData.mt2_chUser2.CHLimits()));
            ch2limitEnabled = msg.mt2_userData.mt2_chUser2.CHLimits();
            returndata.add((double)(msg.mt2_userData.mt2_chUser2.alarm_Hi.getValue())/10+"");
            ch2Hi = msg.mt2_userData.mt2_chUser2.alarm_Hi.getValue();
            returndata.add((double)(msg.mt2_userData.mt2_chUser2.alarm_Lo.getValue())/10+"");
            ch2Lo = msg.mt2_userData.mt2_chUser2.alarm_Lo.getValue();
            returndata.add(msg.mt2_userData.mt2_chUser2.delay.value+"");//22

            returndata.add(queryStrings.Period(msg.mt2_userData.samplePeriod.getValue()*1000));
            samplePeriod = msg.mt2_userData.samplePeriod.getValue();
            returndata.add(queryStrings.Period(msg.mt2_userData.startDelay.getValue()*1000));
            startDelay = msg.mt2_userData.startDelay.getValue();
            returndata.add(queryStrings.Period(msg.mt2_userData.startdatetime.getValue()*1000));//25
            returndata.add(msg.mt2_userData.stoponsamples.getValue()+"");
            numberstopon = msg.mt2_userData.stoponsamples.getValue();
            startdatetime = msg.mt2_userData.startdatetime.getValue();
            commentTextsize = msg.mt2_userData.textsize.getValue();
            returndata.add(msg.mt2_userData.userString._value.toString());
            timestartstopdatetime = msg.mt2_userData.timestartdadtetime.getValue();

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

    void printarray(ArrayList<Byte> data){
        for(int i = 0; i < data.size(); i++) {
            Log.d("INFO2",data.get(i) +"++++++++++");
        }
    }


    public byte[] Write_USERFlags(boolean[] flags) {
        ArrayList<Byte> data = new ArrayList<Byte>();
        classMessages.MT2_USERFlash msg = new classMessages.MT2_USERFlash();

        msg.mt2_userData.mt2_userFlags.imperialUnits.setValue(flags[0]);
        msg.mt2_userData.mt2_userFlags.loopOverwrite.setValue(flags[1]);
        msg.mt2_userData.mt2_userFlags.lcdMenu.setValue(flags[2]);
        msg.mt2_userData.mt2_userFlags.allowTags.setValue(flags[3]);
        msg.mt2_userData.mt2_userFlags.buttonStart1.setValue(flags[4]);
        msg.mt2_userData.mt2_userFlags.buttonStop1.setValue(flags[5]);
        msg.mt2_userData.mt2_userFlags.buttonReuse.setValue(flags[6]);
        msg.mt2_userData.mt2_userFlags.fullCheck.setValue(flags[7]);

        msg.mt2_userData.mt2_userFlags.safeStats.setValue(flags[8]);
        msg.mt2_userData.mt2_userFlags.FullStats.setValue(flags[9]);
        msg.mt2_userData.mt2_userFlags.startDateTime.setValue(flags[10]);
        msg.mt2_userData.mt2_userFlags.passwordEnabled.setValue(flags[11]);
        msg.mt2_userData.mt2_userFlags.stoponfull.setValue(flags[12]);
        msg.mt2_userData.mt2_userFlags.stopOnSamples.setValue(flags[13]);
        msg.mt2_userData.mt2_userFlags.stoponDate.setValue(flags[14]);
        msg.mt2_userData.mt2_userFlags.extendedMenu.setValue(flags[15]);

        msg.mt2_userData.mt2_userFlags.ToByte(data);

        int i = 0;
        byte[] rd = new byte[2];
        for (Byte current : data) {
            rd[i] = current;
            i++;
        }

        return rd;
    }


    public byte[] Write_USERCH1(CHUserData chUserData) {
        ArrayList<Byte> data = new ArrayList<Byte>();
        classMessages.MT2_USERFlash msg = new classMessages.MT2_USERFlash();

        msg.mt2_userData.mt2_chUser.enable.setValue(chUserData.getCh1enable());
        msg.mt2_userData.mt2_chUser.checkLimits.setValue(chUserData.getCh1limitenable());
        msg.mt2_userData.mt2_chUser.alarm_Hi.setValue((int)chUserData.getCh1upperlimit());
        msg.mt2_userData.mt2_chUser.alarm_Lo.setValue((int)chUserData.getCh1lowerlimit());
        msg.mt2_userData.mt2_chUser.delay.value = (byte)chUserData.getCh1alarmdelay();

        msg.mt2_userData.mt2_chUser2.enable.setValue(chUserData.getCh2enable());
        msg.mt2_userData.mt2_chUser2.checkLimits.setValue(chUserData.getCh2limitenable());
        msg.mt2_userData.mt2_chUser2.alarm_Hi.setValue((int)chUserData.getCh2upperlimit());
        msg.mt2_userData.mt2_chUser2.alarm_Lo.setValue((int)chUserData.getCh2lowerlimit());
        msg.mt2_userData.mt2_chUser2.delay.value = (byte)chUserData.getCh2alarmdelay();

        msg.mt2_userData.mt2_chUser.ToByte(data);
        msg.mt2_userData.mt2_chUser2.ToByte(data);


        int i = 0;
        byte[] rd = new byte[16];
        for (Byte current : data) {
            rd[i] = current;
            i++;
        }

        return rd;
    }


    public byte[] Write_USERSamplePeriod(long seconds) {
        ArrayList<Byte> data = new ArrayList<Byte>();
        classMessages.MT2_USERFlash msg = new classMessages.MT2_USERFlash();

        msg.mt2_userData.samplePeriod.value = (int)seconds;
        msg.mt2_userData.samplePeriod.ToByte(data);

        int i = 0;
        byte[] rd = new byte[16];
        for (Byte current : data) {
            rd[i] = current;
            i++;
        }

        return rd;
    }


    public byte[] Write_USERStartDelay(long seconds) {
        ArrayList<Byte> data = new ArrayList<Byte>();
        classMessages.MT2_USERFlash msg = new classMessages.MT2_USERFlash();

        msg.mt2_userData.startDelay.value = (int)seconds;
        msg.mt2_userData.startDelay.ToByte(data);

        int i = 0;
        byte[] rd = new byte[16];
        for (Byte current : data) {
            rd[i] = current;
            i++;
        }

        return rd;
    }

    public byte[] Write_USERStartdatetimeDelay(long seconds) {
        ArrayList<Byte> data = new ArrayList<Byte>();
        classMessages.MT2_USERFlash msg = new classMessages.MT2_USERFlash();

        msg.mt2_userData.startdatetime.value = (int)seconds;
        msg.mt2_userData.startdatetime.ToByte(data);

        int i = 0;
        byte[] rd = new byte[4];
        for (Byte current : data) {
            rd[i] = current;
            i++;
        }

        return rd;
    }


    public byte[] Write_USERStoponsample(int samples) {
        ArrayList<Byte> data = new ArrayList<Byte>();
        classMessages.MT2_USERFlash msg = new classMessages.MT2_USERFlash();

        msg.mt2_userData.stoponsamples.value = samples;
        msg.mt2_userData.stoponsamples.ToByte(data);

        int i = 0;
        byte[] rd = new byte[4];
        for (Byte current : data) {
            rd[i] = current;
            i++;
        }

        return rd;
    }

    public byte[] Write_USERpassword(String pass) {
        ArrayList<Byte> data = new ArrayList<Byte>();
        classMessages.MT2_USERFlash msg = new classMessages.MT2_USERFlash();

        msg.mt2_userData.password.setHasPassword(pass);
        msg.mt2_userData.password.ToByte(data);

        int i = 0;
        byte[] rd = new byte[4];
        for (Byte current : data) {
            rd[i] = current;
            i++;
        }

        return rd;
    }

    public byte[] Write_USERString(String comment) {
        ArrayList<Byte> data = new ArrayList<Byte>();
        classMessages.MT2_USERFlash msg = new classMessages.MT2_USERFlash();


        msg.mt2_userData.userString._value = "";
        msg.mt2_userData.userString._value = comment;
        msg.mt2_userData.userString.ToByte(data);

        int i = 0;
        byte[] rd = new byte[640];
        for (Byte current : data) {
            rd[i] = current;
            i++;
        }

        return rd;
    }

    public byte[] WriteRTCStartDateTime(Calendar date){


        ArrayList<Byte> data = new ArrayList<Byte>();
        classMessages.MT2_USERFlash msg = new classMessages.MT2_USERFlash();

        msg.mt2_userData.timestartdadtetime.value = date;
        msg.mt2_userData.timestartdadtetime.ToByte(data);

        int i = 0;
        byte[] rd = new byte[640];
        for (Byte current : data) {
            rd[i] = current;
            i++;
        }



        return rd;
    }

    public byte[] WriteRTC(){

        ArrayList<Byte> date = new ArrayList<Byte>();

        date.add(commsChar.CMD_RTC);
        date.add(commsChar.RTC_SET);

        Calendar cal = Calendar.getInstance();
        Log.d("======================", " "+ cal.get(Calendar.YEAR) + " " + cal.get(Calendar.DAY_OF_MONTH) + " " + cal.get(Calendar.HOUR) + " " + cal.get(Calendar.HOUR_OF_DAY));
        new dataType.dateRtc(cal).ToByte(date);

        int i = 0;
        byte[] rd = new byte[date.size()];
        for (Byte current : date) {
            rd[i] = current;
            i++;
        }



        return rd;
    }

    public byte[] ReadRTC(){
        ArrayList<Byte> date = new ArrayList<Byte>();

        date.add(commsChar.CMD_RTC);
        date.add(commsChar.RTC_GET);


        int i = 0;
        byte[] rd = new byte[date.size()];
        for (Byte current : date) {
            rd[i] = current;
            i++;
        }



        return rd;
    }



    public byte[] WritePassword(){
        ArrayList<Byte> date = new ArrayList<Byte>();


        date.add((byte)0x08);
        date.add((byte)0x01);
        date.add((byte)0xEE);
        date.add((byte)0xDC);
        date.add((byte)0xFB);
        date.add((byte)0x31);
        date.add((byte)0xC4);
        date.add((byte)0x9B);
        date.add((byte)0x19);
        date.add((byte)0xF9);


        int i = 0;
        byte[] rd = new byte[date.size()];
        for (Byte current : date) {
            rd[i] = current;
            i++;
        }

        return rd;
    }
}
