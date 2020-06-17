package com.temprecord.temprecordapp.Types;

import java.util.ArrayList;

/**
 * Created by Yasiru on 26/02/2018.
 */

public class sVal16 extends baseType {

    public int value;

    public static final int ByteSize = 2;


    public void setValue(int value){
        this.value = value;
    }
    /// <summary>
    /// Creates a new class with the default value of 0
    /// </summary>
    public sVal16(){
        super(ByteSize);
        value = 0;
    }

    /// <summary>
    /// Creates a new class with a specified default value
    /// </summary>
    /// <param name="val">The default value to initialize the class to</param>
    public sVal16(int val){
        super(ByteSize);
        value = val;
    }

    /// <summary>
    /// Creates a new instance of this class by reading bytes from a list.
    /// </summary>
    /// <param name="data">The byte list to read bytes from to initialize this class</param>
    public sVal16(ArrayList<Byte> data){
        super(ByteSize);
        int val;
        short s = (short) ((data.get(0) & 0xff) | (data.get(1) << 8));
        val = (int) s;
//        val = (short) (data.get(0) & 0xFF);
//        val +=  ((data.get(1) & 0xFF) << 8);
        value = val;
        data.remove(0);
        data.remove(0);
    }

    /// <summary>
    /// The method that converts this class into a list of bytes.
    /// </summary>
    /// <param name="data">The list of bytes to append this classes bytes to.</param>
    @Override
    public void ToByte(ArrayList<Byte> data)
    {
        short val = (short)value;
        data.add((byte)(val));
        val = (short) (val >> 8);
        data.add((byte)(val));
    }

    public int getValue(){
        return value;
    }

    public double getDoubleValue(){
        return value;
    }
}



