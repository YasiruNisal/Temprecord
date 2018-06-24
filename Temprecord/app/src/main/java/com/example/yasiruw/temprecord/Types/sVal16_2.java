package com.example.yasiruw.temprecord.Types;

import java.util.ArrayList;

public class sVal16_2 extends sVal16 //2
{
    /// <summary>
    /// Creates a new class with the default value of 0.00
    /// </summary>
    public sVal16_2(){
        super();
    }

    /// <summary>
    /// Creates a new class with a specified default value
    /// </summary>
    /// <param name="val">The default value to initialize the class to</param>
    protected sVal16_2(int val){
        super(val);
    }

    /// <summary>
    /// Creates a new instance of this class by reading bytes from a list.
    /// </summary>
    /// <param name="data">The byte list to read bytes from to initialize this class</param>
    public sVal16_2(ArrayList<Byte> data){
        super(data);
    }

    public static double returnDouble(sVal16_2 val){
        return val.value/100.0;
    }
}
