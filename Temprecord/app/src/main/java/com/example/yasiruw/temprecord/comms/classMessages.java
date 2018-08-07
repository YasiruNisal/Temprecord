package com.example.yasiruw.temprecord.comms;



import com.example.yasiruw.temprecord.Types.baseType;
import com.example.yasiruw.temprecord.Types.dataType;
import com.example.yasiruw.temprecord.Types.loggerType;
import com.example.yasiruw.temprecord.Types.uVal16;
import com.example.yasiruw.temprecord.Types.uVal32;
import com.example.yasiruw.temprecord.Types.u_int8;

import java.util.ArrayList;

/**
 * Created by Yasiru on 13-Dec-17.
 */

public class classMessages {

    HexData hexData = new HexData();
    public static class Msg_Query extends baseType.baseClass {

        public loggerType.LoggerType type;
        public loggerType.LoggerFirmware firmware;
        public loggerType.LoggerSerial_12 serial_12;
        public loggerType.uVal8 uVal8;
        public loggerType.MT2_LabFlags mt2_labFlags;
        loggerType.LoggerState state;
        u_int8.uVal8_2 battery;


        public static final int ByteSize = 26;

        public Msg_Query(){
            super(ByteSize);
            type = new loggerType.LoggerType();
            firmware = new loggerType.LoggerFirmware();
            serial_12 = new loggerType.LoggerSerial_12();
            uVal8 = new loggerType.uVal8();
            mt2_labFlags = new loggerType.MT2_LabFlags();
            state = new loggerType.LoggerState();
            battery = new u_int8.uVal8_2();
        }

        public Msg_Query(ArrayList<Byte> data){
            super(ByteSize);
            type = new loggerType.LoggerType(data);
            firmware = new loggerType.LoggerFirmware(data);
            serial_12 = new loggerType.LoggerSerial_12(data);
            uVal8 = new loggerType.uVal8(data);
            mt2_labFlags = new  loggerType.MT2_LabFlags(data);
            state = new loggerType.LoggerState(data);
            battery = new u_int8.uVal8_2(data);
            //Log.d("INFO","coming in here t the constructor");
        }

        @Override
        public void ToByte(ArrayList<Byte> data) {
            type.ToByte(data);
            firmware.ToByte(data);
            serial_12.ToByte(data);
            uVal8.ToByte(data);
            mt2_labFlags.ToByte(data);
            state.ToByte(data);
            battery.ToByte(data);
        }

        @Override
        public int CalculateSize() {
            int i = 0;
            i+= type.TypeSize;
            i+= firmware.TypeSize;
            i += serial_12.TypeSize;
            i+= uVal8.TypeSize;
            i+= mt2_labFlags.TypeSize;
            i+= state.TypeSize;
            i+= battery.TypeSize;
            return i;
        }

        public loggerType.LoggerType getType(){
            return type;
        }

        public loggerType.LoggerFirmware getFirmware(){
            return firmware;
        }

        public loggerType.LoggerSerial_12 getSerial_12(){
            return serial_12;
        }
    }

    public static class Msg_Bat_state extends baseType.baseClass {

        loggerType.LoggerState state;
        u_int8.uVal8_2 battery;

        public static final int ByteSize = 6;


        public Msg_Bat_state(){
            super(ByteSize);
            state = new loggerType.LoggerState();
            battery = new u_int8.uVal8_2();

        }

        public Msg_Bat_state(ArrayList<Byte> data){
            super(ByteSize);
            state = new loggerType.LoggerState(data);
            battery = new u_int8.uVal8_2(data);

        }

        @Override
        public void ToByte(ArrayList<Byte> data) {
            state.ToByte(data);
            battery.ToByte(data);

        }

        @Override
        public int CalculateSize() {
            int i = 0;
            i+= state.TypeSize;
            i+= battery.TypeSize;
            return i;
        }


    }

    public static class TWFlash extends baseType.baseClass{

        public dataType.dateRtc dateRtc;
        public uVal16 uval16;
        public uVal16 MemorySizeMax;
        public MT2_CHData mt2_chData;

        //46 bytes for the first set of data and 6 for the second set
        public static final int ByteSize = 98;

        public TWFlash(){
            super(ByteSize);
            dateRtc = new dataType.dateRtc();
            uval16 = new uVal16();
            MemorySizeMax = new uVal16();
            mt2_chData = new MT2_CHData();
        }

        public TWFlash(ArrayList<Byte> data) {
            super(ByteSize);
            dateRtc = new dataType.dateRtc(data);
            for (int i = 0; i < 34; i++) data.remove(0);
            uval16 = new uVal16(data);
            MemorySizeMax = new uVal16(data);
            for (int i = 0; i < 30; i++) data.remove(0);
            mt2_chData = new MT2_CHData(data);//KT103 means temperature sensor and SHT2X means humidity sensor.
//            Log.d("&&&&&&&4", " " + manudate);
//            Log.d("&&&&&&&4", " " + mt2_chData.getSensorMax());
//            Log.d("&&&&&&&4", " " + mt2_chData.getSensorMin());
        }

        @Override
        public void ToByte(ArrayList<Byte> data) {
            dateRtc.ToByte(data);
            uval16.ToByte(data);
            MemorySizeMax.ToByte(data);
            mt2_chData.ToByte(data);
        }


        @Override
        public int CalculateSize() {
            int i = 0;
            i+= dateRtc.TypeSize;
            i+= uVal16.ByteSize;
            i+= uVal16.ByteSize;
            i+= MT2_CHData.ByteSize;
            return i;
        }
    }


    public static class RamRead extends baseType.baseClass{

        public MT2ClassFlags.MT2_FlashFlags mt2_flashFlags;
        public MT2ClassFlags.MT2_LoggerFlags mt2_loggerFlags;
        public uVal32 uval32;
        public dataType.dateRtc dateRtc;
        public MT2_CHRam mt2_chRam;
        public MT2_CHRam mt2_chRam1;
        public u_int8.uVal8_2 batLife;
        public uVal16 samplePointer;


        public static final int ByteSize = 100;

        public RamRead(){
            super(ByteSize);
            mt2_flashFlags = new MT2ClassFlags.MT2_FlashFlags();
            mt2_loggerFlags = new MT2ClassFlags.MT2_LoggerFlags();
            uval32 = new uVal32();
            dateRtc = new dataType.dateRtc();
            mt2_chRam = new MT2_CHRam();
            mt2_chRam1 = new MT2_CHRam();
            batLife = new u_int8.uVal8_2();
            samplePointer = new uVal16();
        }

        public RamRead(ArrayList<Byte> data){
            super(ByteSize);
            mt2_flashFlags = new MT2ClassFlags.MT2_FlashFlags(data);
            mt2_loggerFlags = new MT2ClassFlags.MT2_LoggerFlags(data);
            for (int i = 0; i < 1; i++) data.remove(0);
            batLife = new u_int8.uVal8_2(data);
            samplePointer = new uVal16(data);
            uval32 = new uVal32(data);
            dateRtc = new dataType.dateRtc(data);
            for (int i = 0; i < 20; i++) data.remove(0);
            //remove three more as as first 3 RAM channel data are not used
            for (int i = 0; i < 3; i++) data.remove(0);
            mt2_chRam = new MT2_CHRam(data);
            //remove three more as as first 3 RAM channel data are not used
            for (int i = 0; i < 3; i++) data.remove(0);
            mt2_chRam1 = new MT2_CHRam(data);
        }

        @Override
        public void ToByte(ArrayList<Byte> data){
            mt2_flashFlags.ToByte(data);
            mt2_loggerFlags.ToByte(data);
            batLife.ToByte(data);
            samplePointer.ToByte(data);
            uval32.ToByte(data);
            dateRtc.ToByte(data);
            mt2_chRam.ToByte(data);
            mt2_chRam1.ToByte(data);
        }

        @Override
        public int CalculateSize(){
            int i = 0;
            i += mt2_flashFlags.TypeSize;
            i += mt2_loggerFlags.TypeSize;
            i += uval32.TypeSize;
            i += batLife.TypeSize;
            i += samplePointer.TypeSize;
            i += dateRtc.TypeSize;
            i += mt2_chRam.TypeSize;
            i += mt2_chRam1.TypeSize;
            return i;
        }
    }


    public static class MT2_USERFlash extends baseType.baseClass  {

        public static final int ByteSize = 510;

        public uVal32 tripSamples;
        public dataType.dateRtc dateStarted;
        public dataType.dateRtc dateStopped;
        public uVal16 totalTrips;
        public uVal32 totalSamples;
        public uVal32 totalLoggedSamples;
        public MT2_USERData mt2_userData;


        public MT2_USERFlash(){
            super(ByteSize);
            tripSamples = new uVal32();
            dateStarted = new dataType.dateRtc();
            dateStopped = new dataType.dateRtc();
            totalTrips = new uVal16();
            totalSamples = new uVal32();
            totalLoggedSamples = new uVal32();
            mt2_userData = new MT2_USERData();

        }

        public MT2_USERFlash(ArrayList<Byte> data){
            super(ByteSize);
            tripSamples = new uVal32(data);
            for (int i = 0; i < 6; i++) data.remove(0);
            dateStarted = new dataType.dateRtc(data);
            dateStopped = new dataType.dateRtc(data);
            for (int i = 0; i < 12; i++) data.remove(0);
            totalTrips = new uVal16(data);
            totalSamples = new uVal32(data);
            totalLoggedSamples = new uVal32(data);
            for (int i = 0; i < 68; i++) data.remove(0);
            HexData hexData = new HexData();
            hexData.BytetoHex2(data);
            mt2_userData = new MT2_USERData(data);



        }

        public void ToByte(ArrayList<Byte> data){
            tripSamples.ToByte(data);
            dateStarted.ToByte(data);
            dateStopped.ToByte(data);
            totalTrips.ToByte(data);
            totalSamples.ToByte(data);
            totalLoggedSamples.ToByte(data);
            mt2_userData.ToByte(data);
        }

        public int CalculateSize(){
            int i = 0;
            i += uVal32.ByteSize;
            i += dataType.dateRtc.ByteSize;
            i += dataType.dateRtc.ByteSize;
            i += uVal16.ByteSize;
            i += uVal32.ByteSize;
            i += uVal32.ByteSize;
            i += MT2_USERData.ByteSize;
            return i;
        }
    }




}
