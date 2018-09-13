package com.temprecordapp.yasiruw.temprecord.comms;



import com.temprecordapp.yasiruw.temprecord.Types.baseType;
import com.temprecordapp.yasiruw.temprecord.Types.bitBool;
import com.temprecordapp.yasiruw.temprecord.Types.loggerType;
import com.temprecordapp.yasiruw.temprecord.Types.sVal16_1;

import java.util.ArrayList;

/**
 * Created by Yasiru on 1/03/2018.
 */

public class MT2_CHUser extends baseType.baseClass {

    public static final int ByteSize = 8;

    bitBool enable;
    bitBool checkLimits;
    bitBool safeRange;

    sVal16_1 alarm_Hi;
    sVal16_1 alarm_Lo;
    loggerType.uVal8 delay;
    loggerType.uVal8 safeEntry;

    byte SpareFlags0 = 0x00;
    byte SpareFlags1 = 0x00;

    public MT2_CHUser(){
        super(ByteSize);

        SpareFlags0 = 0x00;
        enable = new bitBool(SpareFlags0, 0);
        checkLimits  = new bitBool(SpareFlags0, 1);
        safeRange  = new bitBool(SpareFlags0, 2);

        SpareFlags1 = 0x00;
        alarm_Hi = new sVal16_1();
        alarm_Lo = new sVal16_1();
        delay = new loggerType.uVal8();
        safeEntry = new loggerType.uVal8();

    }

    public MT2_CHUser(ArrayList<Byte> data){
        super(ByteSize);

        SpareFlags0 = data.get(0);
        enable = new bitBool(SpareFlags0, 0);
        checkLimits  = new bitBool(SpareFlags0, 1);
        safeRange  = new bitBool(SpareFlags0, 2);
        data.remove(0);

        SpareFlags1 = data.get(0);
        data.remove(0);
        alarm_Hi = new sVal16_1(data);
        alarm_Lo = new sVal16_1(data);
        delay = new loggerType.uVal8(data);
        safeEntry = new loggerType.uVal8(data);

    }

    @Override
    public void ToByte(ArrayList<Byte> data)
    {
        data.add(SpareFlags0);
        enable.ToByte(data);
        checkLimits.ToByte(data);
        safeRange.ToByte(data);

        data.add(SpareFlags1);

        alarm_Hi.ToByte(data);
        alarm_Lo.ToByte(data);
        delay.ToByte(data);
        safeEntry.ToByte(data);
    }

    @Override
    public  int CalculateSize()
    {
        int i = 0;
        i += 1; //spareFlags0
        i += 1; //spareFlags1
        i += alarm_Hi.TypeSize;
        i += alarm_Lo.TypeSize;
        i += delay.TypeSize;
        i += safeEntry.TypeSize;
        return i;
    }

    public boolean CHEnable(){return enable.getValue();}
    public boolean CHLimits(){return checkLimits.getValue();}
}
