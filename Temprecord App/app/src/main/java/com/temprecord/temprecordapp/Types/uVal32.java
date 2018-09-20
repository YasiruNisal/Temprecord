package com.temprecord.temprecordapp.Types;

import java.util.ArrayList;

/**
 * Created by Yasiru on 27/02/2018.
 */

public class uVal32 extends baseType {

    public int value;

    public static final int ByteSize = 4;

    /// <summary>
    /// Creates a new class with the default value of 0
    /// </summary>
    public uVal32(){
        super(ByteSize);
        value = 0;
    }

    /// <summary>
    /// Creates a new class with a specified default value
    /// </summary>
    /// <param name="val">The default value to initialize the class to</param>
    protected uVal32(int val){
        super(ByteSize);
        value = val;
    }

    /// <summary>
    /// Creates a new instance of this class by reading bytes from a list.
    /// </summary>
    /// <param name="data">The byte list to read bytes from to initialize this class</param>
    public uVal32(ArrayList<Byte> data){
        super(ByteSize);
        int val;


        int s = ((data.get(0) & 0xff) | ((data.get(1)& 0xff) << 8) | ((data.get(2)& 0xffff) << 16) | ((data.get(3)& 0xffffff) << 24));
        val = (int) s;
        value = val;
//        int byte1 = 0xFF & data.get(0);
//        int byte2 = (0xFF & data.get(1)) << 8;
//        int byte3 = (0xFF & data.get(2)) << 16;
//        int byte4 = (0xFF & data.get(3)) << 24;
//        value = byte1 | byte2 | byte3 | byte4;
//        Log.d("$$$$$$", byte1 + " " + byte2 + " " + byte3 + " " + byte4 + " " + value);
        //val = (( ((byte) data.get(3)) << 24) |  ( ((byte) data.get(2)) << 16) | ( ((byte) data.get(1)) << 8) | ( data.get(0)));
//        val = ((int)data.get(0)); //| (int)(data.get(1) << 8) | (int)(data.get(2) << 16) | (int)(data.get(3) << 24));
//        val += (int)((0xFF & data.get(1)) << 8);
//        val += (int)((0xFFFF & data.get(2)) << 16);
//        val += (int)((0xFFFFFF & data.get(3)) << 24);
        //value = (int)val;
        data.remove(0);
        data.remove(0);
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
        int val = (int)value;
        data.add((byte)(val));
        val = val >> 8;
        data.add((byte)(val));
        val = val >> 8;
        data.add((byte)(val));
        val = val >> 8;
        data.add((byte)(val));
    }

    /// <summary>
    /// Implicit operator which will automatically convert from one type to the other
    /// </summary>
    public int getValue()
    {
        return (value);
    }


}
