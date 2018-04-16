package com.ublox.BLE.comms;

import com.ublox.BLE.Types.baseType;
import com.ublox.BLE.Types.bitBool;

import java.util.ArrayList;

/**
 * Created by Yasiru on 27/02/2018.
 */

public class MT2ClassFlags {

    public static class MT2_LoggerFlags extends baseType{

        bitBool safeRange;
        bitBool batteryLow;
        bitBool batteryExpired;
        bitBool startupError;
        bitBool rtcRunning;
        bitBool comRequest;
        bitBool comComplete;
        bitBool startDateTime;

        bitBool buttonStart;
        bitBool softwareStart;
        bitBool buttonStop;
        bitBool softwareStop;
        bitBool fullStop;
        bitBool sampleStop;
        bitBool pdfStop;
        bitBool spare;

        public MT2_LoggerFlags(){
            super(ByteSize);

            safeRange = new bitBool(0);
            batteryLow = new bitBool(1);
            batteryExpired = new bitBool(2);
            startupError  = new bitBool(3);
            rtcRunning = new bitBool(4);
            comRequest = new bitBool(5);
            comComplete = new bitBool(6);
            startDateTime = new bitBool(7);

            buttonStart = new bitBool(0);
            softwareStart = new bitBool(1);
            buttonStop = new bitBool(2);
            softwareStart = new bitBool(3);
            fullStop = new bitBool(4);
            sampleStop = new bitBool(5);
            pdfStop = new bitBool(6);

            spare = new bitBool(7);
        }

        public MT2_LoggerFlags(ArrayList<Byte> data){
            super(ByteSize);

            safeRange = new bitBool(data.get(0), 0);
            batteryLow = new bitBool(data.get(0), 1);
            batteryExpired = new bitBool(data.get(0), 2);
            startupError = new bitBool(data.get(0), 3);
            rtcRunning = new bitBool(data.get(0), 4);
            comRequest = new bitBool(data.get(0), 5);
            comComplete = new bitBool(data.get(0), 6);
            startDateTime = new bitBool(data.get(0), 7);
            data.remove(0);

            buttonStart = new bitBool(data.get(0), 0);
            softwareStart = new bitBool(data.get(0), 1);
            buttonStop = new bitBool(data.get(0), 2);
            softwareStop = new bitBool(data.get(0), 3);
            fullStop = new bitBool(data.get(0), 4);
            sampleStop = new bitBool(data.get(0), 5);
            pdfStop = new bitBool(data.get(0), 6);

            spare = new bitBool(data.get(0), 7);

            data.remove(0);
        }


        @Override
        public void ToByte(ArrayList<Byte> data)
        {
            data.add((byte)0x00);
            safeRange.ToByte(data);
            batteryLow.ToByte(data);
            batteryExpired.ToByte(data);
            startupError.ToByte(data);
            rtcRunning.ToByte(data);
            comRequest.ToByte(data);
            comComplete.ToByte(data);
            startDateTime.ToByte(data);

            data.add((byte)0x00);
            buttonStart.ToByte(data);
            softwareStart.ToByte(data);
            buttonStop.ToByte(data);
            softwareStop.ToByte(data);
            fullStop.ToByte(data);
            sampleStop.ToByte(data);
            pdfStop.ToByte(data);

            spare.ToByte(data);
        }

        public boolean StartDateTime(){return startDateTime.getValue();}
        public boolean ButtonStart(){return buttonStart.getValue();}
        public boolean SoftwareStart(){return softwareStart.getValue();}
        public boolean ButtonStop(){return buttonStop.getValue();}
        public boolean SoftwareStop(){return softwareStart.getValue();}
        public boolean FullStop(){return fullStop.getValue();}
        public boolean SampleStop(){return sampleStop.getValue();}

    }



}
