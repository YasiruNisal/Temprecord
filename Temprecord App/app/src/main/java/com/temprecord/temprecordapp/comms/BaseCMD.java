package com.temprecord.temprecordapp.comms;

import android.content.Context;


import com.temprecord.temprecordapp.CustomLibraries.Yasiru_Temp_Library;
import com.temprecord.temprecordapp.Types.dataType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Yasiru on 13-Dec-17.
 */

public class BaseCMD {

    private CommsChar commsChar = new CommsChar();
    private Yasiru_Temp_Library yasiruTempLibrary = new Yasiru_Temp_Library();

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
    public boolean energysave;
    public boolean isLoopOverwriteOverFlow;
    public String Password;
    public Date startDateTime;
    //public Calendar stoponDateTime;
    public int startDelay;
    public int samplePeriod;
    private int startdatetime;
    public byte[] password;
    public int ch1Hi;
    public int numberstopon;
    public int ch1Lo;
    public int ch2Hi;
    public int ch2Lo;
    public int numberofsamples;
    public int MemorySizeMax;
    public int SamplePointer;
    public boolean ch1Enable;
    public boolean ch2Enable;
    private int commentTextsize;
    public Date timestartstopdatetime;
    public int state;
    public String usercomment;
    public String serialno;
    public String firmware;
    public String model;
    public String battery;
    public String State;
    public String memory;
    private Context context;
    public int getStartDelaycounter;

    public Date dstart;
    public Date dstop;
    public Date dmanu;
    public Date dman;
    public int PAGESIZE = 512;
    public int TOTALMEMORY = 16384;

    public double querych1hi = 0;
    public double querych1lo = 0;
    public double querych2hi = 0;
    public double querych2lo = 0;

    public boolean inSaferange = true;



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

    /*==============================================================================================
    * pass in the received bytes for the query and it will return an array of strings containing
    * the data
    * =============================================================================================*/

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
                serialno = msg.serial_12.ToString();
                returndata.add(msg.firmware.ToString());
                firmware = msg.firmware.ToString();
                returndata.add(msg.type.getGenString());
                returndata.add(msg.type.getTyString());
                model = msg.type.getVarientString();
                returndata.add(msg.type.getVarientString());
                returndata.add(msg.state.ToString());
                returndata.add(msg.battery.ToString());
                battery = msg.battery.ToString();
                state = msg.state.value;
                State = msg.state.ToString();
            }
        }
        return returndata;
    }

    public ArrayList<String> CMDFlash(byte[] flash){
        //dont need to check if the first byte is ACk here since its already done before putting data in here
        ArrayList<Byte> data = new ArrayList<>();
        ArrayList<String> returndata = new ArrayList<>();
        classMessages.TWFlash msg = null;
        for(int  i = 0; i < flash.length; i++) data.add(flash[i]);
        //removing the first 46 bytes since its used for usbproductstring. we can always add it later on
        for (int i = 0; i < 46; i++) data.remove(0);

        //Log.d("******0", " " + data.size());
        if(data.size() == classMessages.TWFlash.ByteSize){
            msg = new classMessages.TWFlash(data);
            msg.dateRtc.getValue();
            dmanu = msg.dateRtc.getValue();
            returndata.add("");
            returndata.add(""+msg.uval16.getValue()+"");
            memory = String.valueOf(msg.uval16.getValue());
            MemorySizeMax = msg.MemorySizeMax.getValue();
            //Log.d("******0", " " + msg.uval16.getValue());

        }
        return returndata;
    }

    public ArrayList<String> CMDRamRead(byte[] ram){
        //dont need to check if the first byte is ACk here since its already done before putting data in here
        ArrayList<Byte> data = new ArrayList<>();
        ArrayList<String> returndata = new ArrayList<>();
        classMessages.RamRead msg = null;
        for(int  i = 0; i < ram.length; i++) data.add(ram[i]);

        //removing the first two bytes since we dont need to dispaly the infomation these contain


        if(data.size() == classMessages.RamRead.ByteSize)
            msg = new classMessages.RamRead(data);

            isLoopOverwriteOverFlow = msg.mt2_flashFlags.NotOverFlowed();

            returndata.add(yasiruTempLibrary.YesorNo(msg.mt2_loggerFlags.startDateTime.getValue()));
            returndata.add(yasiruTempLibrary.YesorNo(msg.mt2_loggerFlags.buttonStart.getValue()));
            returndata.add(yasiruTempLibrary.Startby(msg.mt2_loggerFlags));
            returndata.add(yasiruTempLibrary.YesorNo(msg.mt2_loggerFlags.buttonStop.getValue()));
            returndata.add(yasiruTempLibrary.Stopby(msg.mt2_loggerFlags));
            returndata.add(yasiruTempLibrary.YesorNo(msg.mt2_loggerFlags.fullStop.getValue()));
            returndata.add(yasiruTempLibrary.YesorNo(msg.mt2_loggerFlags.sampleStop.getValue()));
            returndata.add(msg.uval32.getValue()+"");//8
            numberofsamples = msg.uval32.getValue();

            msg.dateRtc.getValue();
            dman = msg.dateRtc.getValue();
            returndata.add("");
            getStartDelaycounter = msg.startPeriod.value;
            //Log.i("TEST", "TETING DELAY ++++++++++++++" + getStartDelaycounter);
            if((msg.mt2_chRam.chValue.getDoubleValue()/10) > 3000){
                returndata.add("0");
                returndata.add("0");
                returndata.add("0");
            }else {
                returndata.add("" + (msg.mt2_chRam.chValuecomms.getDoubleValue() / 100));
                returndata.add("" + (msg.mt2_chRam.valHi.getDoubleValue() / 10));
                returndata.add("" + (msg.mt2_chRam.valLo.getDoubleValue() / 10));//12
            }
            returndata.add("" + msg.mt2_chRam.delayHi.value);//12 th element is the return arraylist

            if((msg.mt2_chRam1.chValue.getDoubleValue()) > 3000) {
                returndata.add("0");
                returndata.add("0");
                returndata.add("0");
            }else{
                returndata.add("" + (msg.mt2_chRam1.chValuecomms.getDoubleValue()/ 100));
                returndata.add("" + (msg.mt2_chRam1.valHi.getDoubleValue()/ 10));
                returndata.add("" + (msg.mt2_chRam1.valLo.getDoubleValue()/ 10));
            }
            returndata.add("" + msg.mt2_chRam1.delayHi.value);
            returndata.add(""+msg.batLife.value);//17
            returndata.add(""+msg.samplePointer.getValue());
            SamplePointer = msg.samplePointer.getValue();
            //Log.d("%%%%%0", " " +(msg.mt2_chRam1.chValue.getDoubleValue()) + " " + (msg.mt2_chRam1.valHi.getDoubleValue()) + " " + (msg.mt2_chRam1.valLo.getDoubleValue()) + " " + msg.mt2_chRam1.delayHi.value);//);
            querych1hi = msg.mt2_chRam.valHi.getDoubleValue();
        querych1lo = msg.mt2_chRam.valLo.getDoubleValue();
        querych2hi = msg.mt2_chRam1.valHi.getDoubleValue();
        querych2lo = msg.mt2_chRam1.valLo.getDoubleValue();

        return returndata;
    }

    public ArrayList<String> CMDUserRead(byte[] user){
        ArrayList<Byte> data = new ArrayList<>();
        ArrayList<String> returndata = new ArrayList<>();
        classMessages.MT2_USERFlash msg = null;
        for(int  i = 0; i < user.length; i++) data.add(user[i]);

        //removing the first two bytes since we dont need to dispaly the infomation these contain
        for (int i = 0; i < 2; i++) data.remove(0);

        if(data.size() == classMessages.MT2_USERFlash.ByteSize){
            msg = new classMessages.MT2_USERFlash(data);
            returndata.add(msg.tripSamples.getValue()+"");
            //Log.d("******0", msg.tripSamples.getValue()+"");

            startDateTime = msg.dateStarted.getValue();//**********************************************
            dstart = msg.dateStarted.getValue();
            returndata.add("");
            dstop = msg.dateStopped.getValue();
            returndata.add("");

            returndata.add(msg.totalTrips.getValue()+"");
            returndata.add(msg.totalSamples.getValue()+"");
            returndata.add(msg.totalLoggedSamples.getValue()+"");//5

            returndata.add(yasiruTempLibrary.YesorNo(msg.mt2_userData.mt2_userFlags.LoopOverWrite()));
            returndata.add(yasiruTempLibrary.YesorNo(msg.mt2_userData.mt2_userFlags.LCDMenu()));
            returndata.add(yasiruTempLibrary.YesorNo(msg.mt2_userData.mt2_userFlags.AllowTags()));
            returndata.add(yasiruTempLibrary.YesorNo(msg.mt2_userData.mt2_userFlags.ButtonReuse()));
            returndata.add(yasiruTempLibrary.YesorNo(msg.mt2_userData.mt2_userFlags.PasswordEnabled()));
            returndata.add(yasiruTempLibrary.YesorNo(msg.mt2_userData.mt2_userFlags.StoponDate()));
            returndata.add(yasiruTempLibrary.YesorNo(msg.mt2_userData.mt2_userFlags.ExtendedMenu()));//12

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
            energysave = msg.mt2_userData.mt2_userFlags.EnergySave();
            returndata.add(yasiruTempLibrary.YesorNo(msg.mt2_userData.mt2_chUser.CHEnable()));
            ch1Enable = msg.mt2_userData.mt2_chUser.CHEnable();
            returndata.add(yasiruTempLibrary.YesorNo(msg.mt2_userData.mt2_chUser.CHLimits()));
            ch1limitEnabled = msg.mt2_userData.mt2_chUser.CHLimits();
            returndata.add((double)(msg.mt2_userData.mt2_chUser.alarm_Hi.getValue())/10+"");
            ch1Hi = msg.mt2_userData.mt2_chUser.alarm_Hi.getValue();
            returndata.add((double)(msg.mt2_userData.mt2_chUser.alarm_Lo.getValue())/10+"");
            ch1Lo = msg.mt2_userData.mt2_chUser.alarm_Lo.getValue();
            returndata.add(msg.mt2_userData.mt2_chUser.delay.value+"");//17

            returndata.add(yasiruTempLibrary.YesorNo(msg.mt2_userData.mt2_chUser2.CHEnable()));
            ch2Enable = msg.mt2_userData.mt2_chUser2.CHEnable();
            returndata.add(yasiruTempLibrary.YesorNo(msg.mt2_userData.mt2_chUser2.CHLimits()));
            ch2limitEnabled = msg.mt2_userData.mt2_chUser2.CHLimits();
            returndata.add((double)(msg.mt2_userData.mt2_chUser2.alarm_Hi.getValue())/10+"");
            ch2Hi = msg.mt2_userData.mt2_chUser2.alarm_Hi.getValue();
            returndata.add((double)(msg.mt2_userData.mt2_chUser2.alarm_Lo.getValue())/10+"");
            ch2Lo = msg.mt2_userData.mt2_chUser2.alarm_Lo.getValue();
            returndata.add(msg.mt2_userData.mt2_chUser2.delay.value+"");//22

            returndata.add(yasiruTempLibrary.Period(msg.mt2_userData.samplePeriod.getValue()*1000));
            samplePeriod = msg.mt2_userData.samplePeriod.getValue();
            returndata.add(yasiruTempLibrary.Period(msg.mt2_userData.startDelay.getValue()*1000));
            startDelay = msg.mt2_userData.startDelay.getValue();
            returndata.add(yasiruTempLibrary.Period(msg.mt2_userData.startdatetime.getValue()*1000));//25
            returndata.add(msg.mt2_userData.stoponsamples.getValue()+"");
            numberstopon = msg.mt2_userData.stoponsamples.getValue();
            startdatetime = msg.mt2_userData.startdatetime.getValue();
            commentTextsize = msg.mt2_userData.textsize.getValue();
            returndata.add(msg.mt2_userData.userString._value);
            usercomment = msg.mt2_userData.userString._value;
            returndata.add(msg.mt2_userData.blenameString._value);
            timestartstopdatetime = msg.mt2_userData.timestartdadtetime.getValue();
            password = msg.mt2_userData.password.hash;

        }

        return returndata;

    }

    public ArrayList<String> CMDState(byte[] state){

        classMessages.Msg_Bat_state msg = null;
        ArrayList<String> returndata = new ArrayList<>();
        if(state[0] == commsChar.CMD_ACK){
            ArrayList<Byte> data = new ArrayList<>();
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
            //Log.d("INFO2",data.get(i) +"++++++++++");
        }
    }


    public byte[] Write_USERFlags(boolean[] flags) {
        ArrayList<Byte> data = new ArrayList<>();
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
        msg.mt2_userData.mt2_userFlags.energysave.setValue(flags[9]);
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
        ArrayList<Byte> data = new ArrayList<>();
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
        ArrayList<Byte> data = new ArrayList<>();
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
        ArrayList<Byte> data = new ArrayList<>();
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
        ArrayList<Byte> data = new ArrayList<>();
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
        ArrayList<Byte> data = new ArrayList<>();
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
        ArrayList<Byte> data = new ArrayList<>();
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
        ArrayList<Byte> data = new ArrayList<>();
        classMessages.MT2_USERFlash msg = new classMessages.MT2_USERFlash();


        msg.mt2_userData.userString._value = "";
        msg.mt2_userData.userString._value = comment;
        msg.mt2_userData.userString.ToByte(data);

        int i = 0;
        byte[] rd = new byte[300];
        for (Byte current : data) {
            rd[i] = current;
            i++;
        }

        return rd;
    }

    public byte[] Write_BLEnameString(String comment) {
        ArrayList<Byte> data = new ArrayList<>();
        classMessages.MT2_USERFlash msg = new classMessages.MT2_USERFlash();


        msg.mt2_userData.blenameString._value = "";
        msg.mt2_userData.blenameString._value = comment;
        msg.mt2_userData.blenameString.ToByte(data);

        int i = 0;
        byte[] rd = new byte[40];
        for (Byte current : data) {
            rd[i] = current;
            i++;
        }

        return rd;
    }

    public byte[] WriteRTCStartDateTime(Date date){

        //Log.i(TAG,"DateRTC+++++++ " + date);
        ArrayList<Byte> data = new ArrayList<>();
        classMessages.MT2_USERFlash msg = new classMessages.MT2_USERFlash();


        msg.mt2_userData.timestartdadtetime.value = yasiruTempLibrary.toCalendar(date);
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

        ArrayList<Byte> date = new ArrayList<>();

        date.add(commsChar.CMD_RTC);
        date.add(commsChar.RTC_SET);

        Calendar cal = yasiruTempLibrary.toCalendar(new Date());

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
        ArrayList<Byte> date = new ArrayList<>();

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




    public byte[] WritePassword(String password){
        ArrayList<Byte> date = new ArrayList<>();
        byte pass[] =  yasiruTempLibrary.md5(password);
        //pass = new byte[]{(byte)0xEE, (byte)0xDC,(byte) 0xFB, 0x31, (byte)0xC4, (byte)0x9B, 0x19, (byte)0xF9};

        date.add((byte)0x08);
        date.add((byte)0x01);
//        date.add((byte)0xEE);
//        date.add((byte)0xDC);
//        date.add((byte)0xFB);
//        date.add((byte)0x31);
//        date.add((byte)0xC4);
//        date.add((byte)0x9B);
//        date.add((byte)0x19);
//        date.add((byte)0xF9);//10

        date.add(pass[0]);
        date.add(pass[1]);
        date.add(pass[2]);
        date.add(pass[3]);
        date.add(pass[4]);
        date.add(pass[5]);
        date.add(pass[6]);
        date.add(pass[7]);



        int i = 0;
        byte[] rd = new byte[date.size()];
        for (Byte current : date) {
            rd[i] = current;
            i++;
        }

        return rd;
    }
}
