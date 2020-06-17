package com.temprecord.temprecordapp.comms;



import com.temprecord.temprecordapp.Types.DataString;
import com.temprecord.temprecordapp.Types.baseType;
import com.temprecord.temprecordapp.Types.sVal16_2;
import com.temprecord.temprecordapp.Types.stringAscii;

import java.util.ArrayList;

/**
 * Created by Yasiru on 26/02/2018.
 */

public class MT2_CHData extends baseType.baseClass {

    /// <summary>The bytes taken up to store this object on the device </summary>
    public static final int ByteSize = 12;
    public DataString sensorName;
    public sVal16_2 sensorMin;
    public sVal16_2 sensorMax;

    public double SensorMax;
    public double SensorMin;
    public stringAscii SensorName;
    // <summary>
    /// Creates a new empty class initializing all properties to their default state
    /// </summary>
    public MT2_CHData(){
        super(ByteSize);
        sensorName = new DataString();
        sensorMin = new sVal16_2();
        sensorMax = new sVal16_2();
    }

    /// <summary>
    /// Creates a new class by reading in a list of bytes and initializing all properties from the read bytes
    /// </summary>
    /// <param name="data">The list of bytes to extract the properties from</param>
    public MT2_CHData(ArrayList<Byte> data){
        super(ByteSize);

        HexData hexData = new HexData();
        //Log.d("******1", " " + data.get(0));
        hexData.BytetoHex2(data);
        sensorName = new DataString(data);
        sensorMin = new sVal16_2(data);
        sensorMax = new sVal16_2(data);

        if(SensorMax < SensorMin)
        {
            sVal16_2 temp = sensorMax;
            sensorMax = sensorMin;
            sensorMin = temp;
        }
    }

    /// <summary>
    /// The method that converts this class into a list of bytes.
    /// </summary>
    /// <param name="data">The list of bytes to append this classes bytes to.</param>
    @Override
    public void ToByte(ArrayList<Byte> data)
    {
        sensorName.ToByte(data);
        sensorMin.ToByte(data);
        sensorMax.ToByte(data);
    }

    /// <summary>
    /// A size check for the class, this time calculating the size based
    /// on the sum of all the types in the class rather than a value passed.
    /// </summary>
    /// <returns>The total size of bytes on the device that this class takes up</returns>
    @Override
    public int CalculateSize()
    {
        int i = 0;
        i += sensorName.TypeSize;
        i += sensorMin.TypeSize;
        i += sensorMax.TypeSize;
        return i;
    }

    public double getSensorMax(){
        return sVal16_2.returnDouble(sensorMax);
    }

    public double getSensorMin(){
        return sVal16_2.returnDouble(sensorMin);
    }

    public String getSensorName(){
        return DataString.getString(sensorName);
    }




}
