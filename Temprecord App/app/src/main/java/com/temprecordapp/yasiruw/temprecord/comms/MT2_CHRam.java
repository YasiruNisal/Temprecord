package com.temprecordapp.yasiruw.temprecord.comms;

import com.temprecordapp.yasiruw.temprecord.Types.baseType;
import com.temprecordapp.yasiruw.temprecord.Types.sVal16_1;
import com.temprecordapp.yasiruw.temprecord.Types.u_int8;

import java.util.ArrayList;

/**
 * Created by Yasiru on 28/02/2018.
 */

public class MT2_CHRam extends baseType.baseClass {

    public static final int ByteSize = 21;
    public sVal16_1 chValue;
    public sVal16_1 chValuecomms;
    public sVal16_1 valHi;
    public sVal16_1 valLo;
    public u_int8.uVal8 delayHi;

    public MT2_CHRam(){
        super(ByteSize);

        chValue = new sVal16_1();
        chValuecomms = new sVal16_1();
        valHi = new sVal16_1();
        valLo = new sVal16_1();
        delayHi = new u_int8.uVal8();
    }

    public MT2_CHRam(ArrayList<Byte> data){
        super(ByteSize);

        chValue = new sVal16_1(data);
        chValuecomms = new sVal16_1(data);
        for (int i = 0; i < 6; i++) data.remove(0);
        valHi = new sVal16_1(data);
        valLo = new sVal16_1(data);
        delayHi = new u_int8.uVal8(data);
        for (int i = 0; i < 6; i++) data.remove(0);
    }

    @Override
    public void ToByte(ArrayList<Byte> data){
        chValue.ToByte(data);
        chValuecomms.ToByte(data);
        valHi.ToByte(data);
        valLo.ToByte(data);
        delayHi.ToByte(data);
    }

    @Override
    public int CalculateSize(){
        int i = 0;
        i += chValue.TypeSize;
        i += chValuecomms.TypeSize;
        i += 2;
        i += valHi.TypeSize;
        i += valLo.TypeSize;
        i += delayHi.TypeSize;
        i += 6;

        return i;
    }


}
