package com.ublox.BLE.Types;

import com.ublox.BLE.Types.baseType;

import java.util.ArrayList;

/**
 * Created by Yasiru on 15/02/2018.
 */

public class bitBool extends baseType {
    /// <summary>
    /// The byte size of this class is technically 0 as it doesn't use up a whole byte
    /// </summary>
    public static final int ByteSize = 0;

    /// <summary>
    /// The actual bool value we wish to store
    /// </summary>
    private boolean value;

    /// <summary>
    /// The index of the bit in a byte
    /// </summary>
    private int i;


    public void setValue(boolean value){
        this.value = value;
    }


    /// <summary>
    /// Constructor for the boolean bitBool type which stores boolean values
    /// as bits on the device.
    /// </summary>
    /// <param name="val">The boolean value we wish to initialize the boolean to.</param>
    /// <param name="index">The bit index of a byte we will use to store the boolean.</param>
    public bitBool(boolean val, int index){
        super(ByteSize);
        value = val;
        i = index;
    }

    /// <summary>
    /// The basic constructor which initializes the boolean bitBool type which stores
    /// boolean values as bits on the device.
    /// </summary>
    /// <param name="index">The bit index of a byte we will use to store the boolean.</param>
    public bitBool(int index){
        super(ByteSize);
        value = false;
        i = index;
    }

    /// <summary>
    /// Constructor for the boolean bitBool type which stores voolean values as bits on the device.
    /// This particular constructor takes a byte read from the file/device to initialize the boolean value.
    /// </summary>
    /// <param name="b">The byte to read the bit boolean value from</param>
    /// <param name="index">The index of the bit to use for reading the boolean value</param>
    public bitBool(byte b, int index){
        super(ByteSize);
        value = ((b & (1 << index)) != 0);
        i = index;
    }

    /// <summary>
    /// Used to convert the bitBool to a byte. Uses the last byte's position in the list
    /// and modifies it to store the boolean bit value at the index location in the byte.
    /// </summary>
    /// <param name="data">The last byte of this list will have the bit boolean value applied to it</param>
    @Override
    public  void ToByte(ArrayList<Byte> data)
    {
        int index = data.size() - 1;
        if (value)
        {
            data.set(index,(byte)(data.get(index) | (1 << i)));
        }
        else
        {
            data.set(index,(byte)(byte)(data.get(index) & ~(1 << i)));
        }
    }

    public boolean getValue(){
        return value;
    }

}
