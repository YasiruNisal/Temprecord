package com.example.yasiruw.temprecord.Types;

import java.util.ArrayList;

/**
 * Created by Yasiru on 28/02/2018.
 */

public class sVal16_1 extends sVal16 {

    /// <summary>
    /// Creates a new class with the default value of 0.0
    /// </summary>
    public sVal16_1(){
        super();
    }

    /// <summary>
    /// Creates a new class with a specified default value
    /// </summary>
    /// <param name="val">The default value to initialize the class to</param>
    protected sVal16_1(int val){
        super(val);
    }

    /// <summary>
    /// Creates a new instance of this class by reading bytes from a list.
    /// </summary>
    /// <param name="data">The byte list to read bytes from to initialize this class</param>
    public sVal16_1(ArrayList<Byte> data){
        super(data);
    }

    public static double returnDouble(sVal16_2 val){
        return val.value/10.0;
    }


}
