package com.temprecordapp.yasiruw.temprecord.comms;



import com.temprecordapp.yasiruw.temprecord.Types.baseType;
import com.temprecordapp.yasiruw.temprecord.Types.dataType;
import com.temprecordapp.yasiruw.temprecord.Types.loggerType;
import com.temprecordapp.yasiruw.temprecord.Types.uVal16;
import com.temprecordapp.yasiruw.temprecord.Types.uVal32;

import java.util.ArrayList;

/**
 * Created by Yasiru on 1/03/2018.
 */

public class MT2_USERData extends baseType.baseClass  {

    public static final int ByteSize = 510;

    public dataType.dateRtc timestartdadtetime;
    public MT2_UserFlags mt2_userFlags;
    public MT2_CHUser mt2_chUser;
    public MT2_CHUser mt2_chUser2;
    public uVal16 samplePeriod;
    public uVal16 startDelay;
    public uVal32 startdatetime;
    public uVal32 stoponsamples;
    public loggerType.UserPassword password;
    public uVal16 textsize;
    public loggerType.UserString userString;
    public loggerType.BLEnameString blenameString;


    public MT2_USERData(){
        super(ByteSize);
        timestartdadtetime = new dataType.dateRtc();
        mt2_userFlags = new MT2_UserFlags();
        mt2_chUser = new MT2_CHUser();
        mt2_chUser2 = new MT2_CHUser();
        samplePeriod = new uVal16();
        startDelay = new uVal16();
        startdatetime = new uVal32();
        stoponsamples = new uVal32();
        password = new loggerType.UserPassword();
        textsize = new uVal16();
        userString = new loggerType.UserString();
        blenameString = new loggerType.BLEnameString();
    }

    public MT2_USERData(ArrayList<Byte> data){
        super(ByteSize);
        timestartdadtetime = new dataType.dateRtc(data);
        mt2_userFlags = new MT2_UserFlags(data);
        mt2_chUser = new MT2_CHUser(data);
        mt2_chUser2 = new MT2_CHUser(data);
        samplePeriod = new uVal16(data);
        startDelay = new uVal16(data);
        startdatetime = new uVal32(data);
        stoponsamples = new uVal32(data);
        password = new loggerType.UserPassword(data);
        for (int i = 0; i < 32; i++) data.remove(0);
        textsize = new uVal16(data);
        userString = new loggerType.UserString(data);
        blenameString = new loggerType.BLEnameString(data);



    }

    public void ToByte(ArrayList<Byte> data){
        timestartdadtetime.ToByte(data);
        mt2_userFlags.ToByte(data);
        mt2_chUser.ToByte(data);
        mt2_chUser2.ToByte(data);
        samplePeriod.ToByte(data);
        startDelay.ToByte(data);
        stoponsamples.ToByte(data);
        password.ToByte(data);
        for (int i = 0; i < 32; i++) data.add((byte)0x00);
        textsize.ToByte(data);
        userString.ToByte(data);
        blenameString.ToByte(data);

    }

    public int CalculateSize(){
        int i = 0;
        i += timestartdadtetime.TypeSize;
        i += mt2_userFlags.TypeSize;
        i += mt2_chUser.TypeSize;
        i += mt2_chUser2.TypeSize;
        i += samplePeriod.TypeSize;
        i += startDelay.TypeSize;
        i += stoponsamples.TypeSize;
        i += password.TypeSize;
        i += 32;
        i += textsize.TypeSize;
        i += userString.TypeSize;
        i += blenameString.TypeSize;

        return i;
    }




}
