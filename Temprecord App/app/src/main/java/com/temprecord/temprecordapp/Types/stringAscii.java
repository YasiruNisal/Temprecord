package com.temprecord.temprecordapp.Types;

import java.util.ArrayList;

/**
 * Created by Yasiru on 26/02/2018.
 */

public class stringAscii extends baseType {

    /// <summary>The actual stored string value</summary>
    private String _value;

    protected String Value;

    /// <summary>
    /// Creates a default stringAscii object with an empty string (all spaces)
    /// </summary>
    /// <param name="len">The maximum lenghth of ASCII characters permited in this string</param>
    protected stringAscii(int len){
        super(len);
        Value = "";
    }

    /// <summary>
    /// Intializes this class with the ASCII string provided and uses the length to set up the max length parameter for this object
    /// </summary>
    /// <param name="str">The ASCII string to use to intialize this class</param>
    /// <param name="len">The maximum lenghth of ASCII characters permited in this string</param>
    protected stringAscii(String str, int len){
        super(len);
        Value = str;
    }

    /// <summary>
    /// Creates a new ASCII string class by reading bytes from a list with a specified character lenght len
    /// </summary>
    /// <param name="data">The byte list to read bytes from to initialize this class</param>
    /// <param name="len">The maximum lenghth of ASCII characters permited in this string</param>
    protected stringAscii(ArrayList<Byte> data, int len){
        super(len);
        byte[] temp = new byte[TypeSize];
        int i = 0;
        for (Byte current : data) {
            if(i < TypeSize) {
                temp[i] = current;
                i++;
            }
        }

        Value = new String(temp);
        for (int j = 0; j < TypeSize; j++) data.remove(0);
        data.remove(0);
    }

    /// <summary>
    /// The method that converts this class into a list of bytes.
    /// </summary>
    /// <param name="data">The list of bytes to append this classes bytes to.</param>
    @Override
    public void ToByte(ArrayList<Byte> data)
    {


        byte[] val = new byte[0];
        byte[] temp = Value.getBytes();
        for (int i = 0; i < TypeSize; i++) {
            val[i] = temp[i];
            data.add(val[i]);
        }
    }

    public String getValue(){
        return Value;
    }

}

