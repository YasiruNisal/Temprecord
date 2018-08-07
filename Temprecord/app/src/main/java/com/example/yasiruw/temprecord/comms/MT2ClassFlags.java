package com.example.yasiruw.temprecord.comms;


import com.example.yasiruw.temprecord.Types.baseType;
import com.example.yasiruw.temprecord.Types.bitBool;

import java.util.ArrayList;

/**
 * Created by Yasiru on 27/02/2018.
 */

public class MT2ClassFlags {

    public static class MT2_LoggerFlags extends baseType {

        public bitBool safeRange;
        public bitBool batteryLow;
        public bitBool batteryExpired;
        public bitBool startupError;
        public bitBool rtcRunning;
        public bitBool comRequest;
        public bitBool comComplete;
        public bitBool startDateTime;

        public bitBool buttonStart;
        public bitBool softwareStart;
        public bitBool buttonStop;
        public bitBool softwareStop;
        public bitBool fullStop;
        public bitBool sampleStop;
        public bitBool pdfStop;
        public bitBool spare;

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

        public boolean SafeRange(){return safeRange.getValue();}
        public boolean StartDateTime(){return startDateTime.getValue();}
        public boolean ButtonStart(){return buttonStart.getValue();}
        public boolean SoftwareStart(){return softwareStart.getValue();}
        public boolean ButtonStop(){return buttonStop.getValue();}
        public boolean SoftwareStop(){return softwareStart.getValue();}
        public boolean FullStop(){return fullStop.getValue();}
        public boolean SampleStop(){return sampleStop.getValue();}

    }

    public static class MT2_FlashFlags extends baseType{

/// <summary>The bytes taken up to store this object on the device.</summary>
        public static final int ByteSize = 2;

        bitBool notErased;
        bitBool notStarted;
        bitBool notOverflowed;
        bitBool notStopped;
        bitBool noAlarms;
        bitBool notRead;
        bitBool notReadTRW;
        bitBool notReadPdf;

        bitBool noRtcSycnc;
        bitBool noBatLow;
        bitBool notDefault;
        bitBool noError;
        bitBool noErrBatExpired;
        bitBool noErrNotErased;
        bitBool noErrStarted;
        bitBool noErrSpare;

        /// <summary>
        /// Creates a default flag object initalizing all flags to default values.
        /// </summary>
        public MT2_FlashFlags(){
            super(ByteSize);
            notErased = new bitBool(true, 0);
            notStarted = new bitBool(true, 1);
            notOverflowed = new bitBool(true, 2);
            notStopped = new bitBool(true, 3);
            noAlarms = new bitBool(true, 4);
            notRead = new bitBool(true, 5);
            notReadTRW = new bitBool(true, 6);
            notReadPdf = new bitBool(true, 7);

            noRtcSycnc = new bitBool(true, 0);
            noBatLow = new bitBool(true, 1);
            notDefault = new bitBool(true,2);
            noError = new bitBool(true, 3);
            noErrBatExpired = new bitBool(true, 4);
            noErrNotErased = new bitBool(true, 5);
            noErrStarted = new bitBool(true, 6);
            noErrSpare = new bitBool(true, 7);
        }

        /// <summary>
        /// Creates a flags object by reading in bytes from a list
        /// </summary>
        /// <param name="data">The list of bytes to extract the flag data from</param>
        public MT2_FlashFlags(ArrayList<Byte> data){
            super(ByteSize);
            notErased = new bitBool(data.get(0), 0);
            notStarted = new bitBool(data.get(0), 1);
            notOverflowed = new bitBool(data.get(0), 2);
            notStopped = new bitBool(data.get(0), 3);
            noAlarms = new bitBool(data.get(0), 4);
            notRead = new bitBool(data.get(0), 5);
            notReadTRW = new bitBool(data.get(0), 6);
            notReadPdf = new bitBool(data.get(0), 7);
            data.remove(0);

            noRtcSycnc = new bitBool(data.get(0), 0);
            noBatLow = new bitBool(data.get(0), 1);
            notDefault = new bitBool(data.get(0), 2);
            noError = new bitBool(data.get(0), 3);
            noErrBatExpired = new bitBool(data.get(0), 4);
            noErrNotErased = new bitBool(data.get(0), 5);
            noErrStarted = new bitBool(data.get(0), 6);
            noErrSpare = new bitBool(data.get(0), 7);
            data.remove(0);
        }

        /// <summary>
        /// The method that converts this class into a list of bytes.
        /// </summary>
        /// <param name="data">The list of bytes to append this classes bytes to.</param>
        @Override
        public void ToByte(ArrayList<Byte> data)
        {
            data.add((byte)0x00);
            notErased.ToByte(data);
            notStarted.ToByte(data);
            notOverflowed.ToByte(data);
            notStopped.ToByte(data);
            noAlarms.ToByte(data);
            notRead.ToByte(data);
            notReadTRW.ToByte(data);
            notReadPdf.ToByte(data);

            data.add((byte)0x00);
            noRtcSycnc.ToByte(data);
            noBatLow.ToByte(data);
            notDefault.ToByte(data);
            noError.ToByte(data);
            noErrBatExpired.ToByte(data);
            noErrNotErased.ToByte(data);
            noErrStarted.ToByte(data);
            noErrSpare.ToByte(data);
        }

        public boolean NotOverFlowed(){return notOverflowed.getValue();}

    }



}
