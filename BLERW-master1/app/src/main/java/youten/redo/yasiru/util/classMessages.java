package youten.redo.yasiru.util;

import java.util.ArrayList;

/**
 * Created by Yasiru on 13-Dec-17.
 */

public class classMessages {
    public static class Msg_Query extends baseType.baseClass {

        public loggerType.LoggerType type;
        public loggerType.LoggerFirmware firmware;
        public loggerType.LoggerSerial_12 serial_12;

        public static final int ByteSize = 14;

        public Msg_Query(){
            super(ByteSize);
            type = new loggerType.LoggerType();
            firmware = new loggerType.LoggerFirmware();
            serial_12 = new loggerType.LoggerSerial_12();
        }

        public Msg_Query(ArrayList<Byte> data){
            super(ByteSize);
            type = new loggerType.LoggerType(data);
            firmware = new loggerType.LoggerFirmware(data);
            serial_12 = new loggerType.LoggerSerial_12(data);
            //Log.d("INFO","coming in here t the constructor");
        }

        @Override
        public void ToByte(ArrayList<Byte> data) {
            type.ToByte(data);
            firmware.ToByte(data);
            serial_12.ToByte(data);
        }

        @Override
        public int CalculateSize() {
            int i = 0;
            i+= type.TypeSize;
            i+= firmware.TypeSize;
            i += serial_12.TypeSize;
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

        public static final int ByteSize = 2;


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

}
