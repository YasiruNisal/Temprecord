package com.temprecord.temprecordapp.Types;

import java.util.ArrayList;

/**
 * Created by Yasiru on 26/02/2018.
 */

public class uVal16 extends baseType {

    /// <summary>The actual value stored</summary>
    public int value;
    /// <summary>The bytes taken up to store this type on the device.</summary>
    public static final int ByteSize = 2;

    /// <summary>
    /// Creates a new class with the default value of 0
    /// </summary>
    public uVal16(){
        super(ByteSize);
        value = 0;
    }

    public uVal16(int val){
        super(ByteSize);
        value = val;
    }

    public uVal16(ArrayList<Byte> data){
        super(ByteSize);

        int val;
        val = (0xFF & data.get(0));
        val += ((0xFF & data.get(1)) << 8);
        value = (short)val;
        data.remove(0);
        data.remove(0);
    }

    @Override
    public void ToByte(ArrayList<Byte> data)
    {
        int val = (int)value;
        data.add((byte)(val));
        val = val >> 8;
        data.add((byte)(val));
    }

    public int getValue(){
        return value;
    }
}
