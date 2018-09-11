package com.temprecordapp.yasiruw.temprecord.Types;

import java.util.ArrayList;

public class DataString extends stringAscii {
    public static final int ByteSize = 8;

    /// <summary>
    /// Creates a new DataString object with a blank string
    /// </summary>
    public DataString(){
        super(ByteSize);
    }

    /// <summary>
    /// Initializes the class with the string provided
    /// </summary>
    /// <param name="val">The ASCII string to use to intialize this class</param>
    public DataString(String val){
        super(val, ByteSize);
    }

    /// <summary>
    /// Creates a new instance of this class by reading bytes from a list.
    /// </summary>
    /// <param name="data">The byte list to read bytes from to initialize this class</param>
    public DataString(ArrayList<Byte> data){
        super(data, ByteSize);
    }

    public static String getString(stringAscii val){
        return val.getValue();
    }


}
