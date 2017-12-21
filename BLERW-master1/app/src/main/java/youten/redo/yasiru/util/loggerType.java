package youten.redo.yasiru.util;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Yasiru on 13-Dec-17.
 */

public  class loggerType {
    private static CommsChar commsChar = new CommsChar();


    public static class LoggerFirmware extends baseType //2
    {
        /// <summary>The bytes taken up to store this type on the device.</summary>
        public static final int ByteSize = 2;

        /// <summary>
        /// Major firmware number 0-255
        /// </summary>
        public byte Major;
        public byte GetMajor(){return Major;}


        /// <summary>
        /// Minor firmware number 0-255
        /// </summary>
        public byte Minor;
        public byte GetMinor(){return Minor;}


        /// <summary>
        /// Default constructor initializes firmware to 0.0
        /// </summary>
        public LoggerFirmware(){
            super(ByteSize);
            Minor = 0;
            Major = 0;
        }

        /// <summary>
        /// Constructs firmware and initializes to Major.Minor
        /// </summary>
        public LoggerFirmware(byte major, byte minor){
            super(ByteSize);
            Major = major;
            Minor = minor;
        }

        /// <summary>
        /// Default constructor that initializes the firmware version from
        /// a lsit of bytes.
        /// </summary>
        /// <param name="data">The list of bytes to extract the firmware version from</param>
        public LoggerFirmware(ArrayList<Byte> data){
            super(ByteSize);
            Minor = data.get(0);
            Major = data.get(1);
            Log.d("INFO4", "__________________________" + data.size() + " " +data.get(0) + " " +data.get(1) );
            data.remove(0);
            data.remove(0);
            Log.d("INFO4", "__________________________" + data.size());
        }

        /// <summary>
        /// The method that converts this class into a list of bytes.
        /// </summary>
        /// <param name="data">The list of bytes to append this classes bytes to.</param>
        @Override
        public void ToByte(ArrayList<Byte> data)
        {
            data.add(Minor);
            data.add(Major);
        }

        /// <summary>
        /// Compares if this Firmware version is equal to the firmware version passed in.
        /// </summary>
        /// <param name="obj">The firmware version to compare to.</param>
        /// <returns>Returns true if the firmware version is the same</returns>
        public boolean IsEqual(LoggerFirmware obj)
        {
            if ((this.Minor == obj.Minor) && (this.Major == obj.Major))
            {
                return true;
            }
            return false;
        }

        /// <summary>
        /// Implicit operator which will automatically convert from one type to the other
        /// </summary>
        public String String(LoggerFirmware val)
        {
            String retVal = null;
            retVal += val.Major;
            retVal += ".";
            retVal += val.Minor;
            return retVal;
        }

        /// <summary>
        /// Overrides the ToString() function to output a custom formatted string
        /// </summary>

        public  String ToString()
        {
            String retVal = "";
            retVal += Major;
            retVal += ".";
            retVal += Minor;
            return retVal;
            //Uses the implicit conversion above to convert to string

        }

        /// <summary>
        /// Firmware comparison
        /// Returns 1 if firmware newer, returns -1 if older and 0 if the same
        /// </summary>
        /// <param name="val">The firmware to compare the current firmware to</param>
        public int CompareFirmware(LoggerFirmware val)
        {
            if(val.Major > this.Major) { return 1; }
            else if(val.Major < this.Major) { return -1; }
            else
            {
                if (val.Minor > this.Minor) { return 1; }
                else if (val.Minor < this.Minor) { return -1; }
            }
            return 0;
        }

    }


    public static class LoggerType extends baseType //3
    {
        /// <summary>The bytes taken up to store this type on the device.</summary>
        public static final int ByteSize = 2;

        /// <summary>
        /// The technological generation of this device
        /// </summary>
        public byte Generation;
        private  String Gen;
        public byte getGeneration(){return Generation;}


        /// <summary>
        /// The type of logger that this is
        /// </summary>
        public byte Type;
        private String Ty;
        public byte getType(){return Type;}


        /// <summary>
        /// The variant of the logger that this is based on features enabled etc.
        /// </summary>
        public byte Variant;
        public byte getVariant(){return Variant;}

        /// <summary>
        /// Initializes a blank LoggerType
        /// </summary>
        public LoggerType() {
            super(ByteSize);
            Generation = 0;
            Type = 0;
            Variant = 0;
        }

        /// <summary>
        /// Initializes a LoggerType from a list of bytes.
        /// </summary>
        /// <param name="data">The list of bytes to extract the LoggerType from</param>
        public LoggerType(ArrayList<Byte> data){
            super(ByteSize);
            Log.d("INFO99", "000000000 "+ data.get(0));


//
            Generation = data.get(0);
            Type = data.get(1);
            //Variant = data.get(2);
            data.remove(0);
            data.remove(0);

            //data.remove(2);
        }

        /// <summary>
        /// Clones a LoggerType from another LoggerType
        /// </summary>
        /// <param name="data">The data to clone from</param>
        public LoggerType(LoggerType data){
            super(ByteSize);
            Generation = data.Generation;
            Type = data.Type;
            //Variant = data.Variant;
        }

        /// <summary>
        /// Clones a LoggerType from what is passed in
        /// </summary>
        /// <param name="gen">Generation of logger</param>
        /// <param name="type">Type of logger</param>
        /// <param name="variant">Product variant of logger</param>
        public LoggerType(Byte gen, Byte type, Byte variant){
            super(ByteSize);
            Generation = gen;
            Type = type;
            //Variant = variant;
        }


        /// <summary>
        /// The method that converts this class into a list of bytes.
        /// </summary>
        /// <param name="data">The list of bytes to append this classes bytes to.</param>
        @Override
        public void ToByte(ArrayList<Byte> data)
        {

            data.add(Generation);
            data.add(Type);
            data.add(Variant);
        }

        /// <summary>
        /// Checks if this LoggerType matches the passed logger type.
        /// </summary>
        /// <param name="obj">The LoggerType to compare to this one</param>
        /// <returns>True if the logger types match</returns>
        public boolean IsEqual(LoggerType obj)
        {
            if ((this.Generation == obj.Generation) && (this.Type == obj.Type) && (this.Variant == obj.Variant))
            {
                return true;
            }
            return false;
        }

        /// <summary>
        /// Implicit operator which will automatically convert from one type to the other
        /// </summary>
        public String String(LoggerType val)
        {
            String retVal = null;
            retVal += val.Generation;
            retVal += ".";
            retVal += val.Type;
            retVal += ".";
            retVal += val.Variant;
            return retVal;
        }

        /// <summary>
        /// Overrides the ToString() function to output a custom formatted string
        /// </summary>
        public  String ToString()
        {

            //Uses the implicit conversion above to convert to string
            String retVal = "";
            retVal += Generation;
            retVal += ".";
            retVal += Type;
            retVal += ".";
            retVal += Variant;
            return retVal;
        }

        public String getGenString(){

            String retVal = "";
            retVal += Generation;
            return retVal;
        }

        public String getTyString(){

            String retVal = "";
            retVal += Type;
            return retVal;
        }




    }

    public static class LoggerSerial_12 extends baseType //10
    {
        /// <summary>
        /// The serial number (correctly formated string)
        /// </summary>
        private String value;

        /// <summary>
        /// The total number of bytes taken up by the serial number on the device
        /// </summary>
        public static final int ByteSize = 10;

        /// <summary>
        /// The number of bytes that we use up for characters in the serial number
        /// </summary>
        private static final int CharLength = 2;
        /// <summary>
        /// The number of bytes that we use up for the numbers in the serial number
        /// </summary>
        private static final int NumberLength = 8;
        /// <summary>
        /// The total length of the string when converted to the correct format
        /// </summary>
        private static final int StringLength = 10;
        /// <summary>
        /// The first character in the ASCII table to base calculations from.
        /// </summary>
        private static final int ASCIIStart = (int) '0';

        /// <summary>
        /// Creates an empty serial number
        /// </summary>
        public LoggerSerial_12() {
            super(ByteSize);
            value = "XX00000000";
        }

        /// <summary>
        /// Creates a serial number passed in from the string.
        /// Can cause an exception to be thrown if the string is not in the correct format!
        /// </summary>
        /// <param name="val">The string to use to create a serial number</param>
        private LoggerSerial_12(String val) {
            super(ByteSize);

            //if(char.IsLetterOrDigit(val[0]) && char.IsLetterOrDigit(val[1]))
            //{
            for (int i = 2; i < StringLength; i++) {
                if (Character.isDigit(val.charAt(i)) == false) {
                    throw new IllegalArgumentException();
                }
            }
            value = val.substring(0, StringLength);
            return;
            //}
            //throw new IllegalArgumentException();
        }

        /// <summary>
        /// Creates a LoggerSerial from a list of data bytes passed in.
        /// </summary>
        /// <param name="data">The list of data bytes to extract a LoggerSerial from</param>
        public LoggerSerial_12(ArrayList<Byte> data) {
            super(ByteSize);
            byte[] temp = new byte[data.size()];
            int i = 0;
            for (Byte current : data) {
                temp[i] = current;
                i++;
            }

            value = new String(temp);
            //Log.d("INFO22", "__________________________" + value);
//            int charData;
//            value = null;
//            charData = ((data.get(0) - ASCIIStart) * 10) + (data.get(0) - ASCIIStart) + ASCIIStart;
//            Log.d("INFO22", "__________________________" + (data.get(0) + " " + (data.get(1))));
//            value += Character.toChars(charData);
//            charData = ((data.get(1) - ASCIIStart) * 10) + (data.get(1) - ASCIIStart) + ASCIIStart;
//            Log.d("INFO22", "__________________________" + charData);
//            Log.d("INFO22", "__________________________" + (data.get(2) + " " + (data.get(3))));
//            value += Character.toChars(charData);
//            for(int i = 0; i < CharLength; i++){
//                data.remove(0);
//            }
//
//            value += data.subList(0, NumberLength).toString();
//            data.remove(7);
//            data.remove(6);
//            data.remove(5);
//            data.remove(4);
//            data.remove(3);
//            data.remove(2);
//            data.remove(1);
//            data.remove(0);


        }

        /// <summary>
        /// Creates a LoggerSerial from a Int64 passed in
        /// </summary>
        /// <param name="data">The list of data bytes to extract a LoggerSerial from</param>
        public LoggerSerial_12(long intVal) {
            super(ByteSize);
            String charVal = "";
            charVal += (char) ((intVal / 260000000 * 10) % 26 + 65);
            charVal += (char) ((intVal / 100000000) % 26 + 65);
            charVal += (char) ((intVal / 10000000) % 10 + 48);
            charVal += (char) ((intVal / 1000000) % 10 + 48);
            charVal += (char) ((intVal / 100000) % 10 + 48);
            charVal += (char) ((intVal / 10000) % 10 + 48);
            charVal += (char) ((intVal / 1000) % 10 + 48);
            charVal += (char) ((intVal / 100) % 10 + 48);
            charVal += (char) ((intVal / 10) % 10 + 48);
            charVal += (char) ((intVal % 10) + 48);

            value = charVal;
        }

        /// <summary>
        /// The method that converts this class into a list of bytes.
        /// </summary>
        /// <param name="data">The list of bytes to append this classes bytes to.</param>
        @Override
        public void ToByte(ArrayList<Byte> data) {
            int charData;

            charData = value.charAt(0) - ASCIIStart;
            data.add((byte) ((charData / 10) + ASCIIStart));
            data.add((byte) ((charData % 10) + ASCIIStart));
            charData = value.charAt(1) - ASCIIStart;
            data.add((byte) ((charData / 10) + ASCIIStart));
            data.add((byte) ((charData % 10) + ASCIIStart));


            byte[] val = new byte[0];
            byte[] temp = value.getBytes();
            for (int i = 2; i < NumberLength; i++) {
                val[i - 2] = temp[i];
                data.add(val[i - 2]);
            }


        }

        /// <summary>
        /// Converts a USBSerial string to a LoggerSerial string
        /// </summary>
        /// <param name="usbSerial">The USBSerial string to pass in to be converted</param>
        /// <returns>A new LoggeerSerial class created from the USBSerial</returns>
//        public static LoggerSerial_12 ConvertFromUSBSerial(String usbSerial)
//        {
//            List<byte> data = ASCIIEncoding.ASCII.GetBytes(usbSerial).ToList();
//            return (new LoggerSerial_12(data));
//        }
//
//        /// <summary>
//        /// Converts to USB string
//        /// </summary>
//        /// <param name="serialNo">The serialNo string to pass in to be converted</param>
//        /// <returns>A new USB class created from the USBSerial</returns>
//        public static UsbSerialString ConvertToUSBSerial(string serialNo)
//        {
//            LoggerSerial_12 sn = new LoggerSerial_12(serialNo);
//
//            int charData;
//            string textNumber = "";
//
//            charData = sn.value[0] - ASCIIStart;
//            textNumber += ((char)((charData / 10) + ASCIIStart));
//            textNumber += ((char)((charData % 10) + ASCIIStart));
//            charData = sn.value[1] - ASCIIStart;
//            textNumber += ((char)((charData / 10) + ASCIIStart));
//            textNumber += ((char)((charData % 10) + ASCIIStart));
//            textNumber += sn.value.Substring(2, sn.value.Length-2);
//
//            return (UsbSerialString)textNumber;
//        }

        /// <summary>
        /// Returst the first byte present in the serial
        /// </summary>
        /// <returns>The very first byte of the serial number</returns>
        public byte GetFirstByte() {
            int charData = value.charAt(0) - ASCIIStart;
            return (byte) ((charData / 10) + ASCIIStart);
        }

        /// <summary>
        /// Returns the last byte present in the serial.
        /// </summary>
        /// <returns>The last byte of the serial number</returns>
        public byte GetLastByte() {
            byte[] val = new byte[0];
            byte[] temp = value.getBytes();
            for (int i = 2; i < NumberLength; i++) {
                val[i - 2] = temp[i];
            }
            // byte[] val = Encoding.ASCII.GetBytes(value.ToCharArray(), 2, NumberLength);
            return val[val.length - 1];
        }


        /// <summary>
        /// Compares if two LoggerSerial objects are the same
        /// </summary>
        /// <param name="obj">The LoggerSerial object to compare to this one</param>
        /// <returns>True if the LoggerSerial numbers match</returns>
//        public bool IsEqual(LoggerSerial_12 obj)
//        {
//            return (this.value == obj.value);
//        }
//
//        /// <summary>
//        /// Implicit operator which will automatically convert from one type to the other
//        /// </summary>
//        public static implicit operator String(LoggerSerial_12 val)
//    {
//        return (val.value);
//    }
//
//        /// <summary>
//        /// Implicit operator which will automatically convert from one type to the other
//        /// </summary>
//        public static implicit operator LoggerSerial_12(String val)
//    {
//        return (new LoggerSerial_12(val));
//    }

        /// <summary>
        /// Overrides the ToString() function to output a custom formatted string
        /// </summary>
        public String ToString() {
            return value;
        }
    }


        public static class LoggerState extends baseType //1
        {
            /// <summary>
            /// The state of the logger
            /// </summary>
            private byte value;
            /// <summary>The bytes taken up to store this type on the device.</summary>
            public static final int ByteSize = 1;

            /// <summary>
            /// Creates a new LoggerState and initializes it to an UNKNOWN state
            /// </summary>
            public LoggerState(){
                super(ByteSize);
                value = commsChar.MODE_UNKNOWN;
            }

            /// <summary>
            /// Creates a new LoggerState initialized to the ENUM value provided.
            /// </summary>
            /// <param name="val">The state to initialize this class to</param>
            private LoggerState(byte val){
                super(ByteSize);
                value = val;
            }

            /// <summary>
            /// Creates a new LoggerState by reading bytes from a list.
            /// </summary>
            /// <param name="data">The byte list to read bytes from to initialize this class</param>
            public LoggerState(ArrayList<Byte> data){
                super(ByteSize);
                value = (byte)data.get(0);
                data.remove(0);

            }

            /// <summary>
            /// The method that converts this class into a list of bytes.
            /// </summary>
            /// <param name="data">The list of bytes to append this classes bytes to.</param>
            @Override
            public void ToByte(ArrayList<Byte> data)
            {
                data.add((byte)value);
            }

            /// <summary>
            /// Compares if this LoggerState is equal to another
            /// </summary>
            /// <param name="obj">The logger state to compare to</param>
            /// <returns>Returns true if the state is equal</returns>
//            public bool IsEqual(LoggerState obj)
//            {
//                return (this.value == obj.value);
//            }
//
//            /// <summary>
//            /// Implicit operator which will automatically convert from one type to the other
//            /// </summary>
//            public static implicit operator LogState(LoggerState val)
//            {
//                return (val.value);
//            }
//
//            /// <summary>
//            /// Implicit operator which will automatically convert from one type to the other
//            /// </summary>
//            public static implicit operator LoggerState(LogState val)
//            {
//                return (new LoggerState(val));
//            }

            /// <summary>
            /// Overrides the ToString() function to output a custom formatted string
            /// </summary>
            public String ToString()
            {

                String retVal = "";
                retVal += value;
                return retVal;


            }
    }




}
