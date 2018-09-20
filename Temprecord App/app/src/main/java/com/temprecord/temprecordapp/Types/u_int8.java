package com.temprecord.temprecordapp.Types;



import java.util.ArrayList;

/**
 * Created by Yasiru on 14-Dec-17.
 */

public class u_int8 {

    public static class uVal8 extends baseType //1
    {
        /// <summary>The actual value stored</summary>
        public Byte value;
        /// <summary>The bytes taken up to store this type on the device.</summary>
        public static final int ByteSize = 1;

        /// <summary>
        /// Creates a new class with the default value of 0
        /// </summary>
        public uVal8(){
            super(ByteSize);
            value = 0;
        }

        protected uVal8(Byte val){
            super(ByteSize);
        value = val;
    }

        /// <summary>
        /// Creates a new instance of this class by reading bytes from a list.
        /// </summary>
        /// <param name="data">The byte list to read bytes from to initialize this class</param>
        public uVal8(ArrayList<Byte> data){
            super(ByteSize);
            value = data.get(0);
            data.remove(0);
        }

        /// <summary>
        /// The method that converts this class into a list of bytes.
        /// </summary>
        /// <param name="data">The list of bytes to append this classes bytes to.</param>
        @Override
        public  void ToByte(ArrayList<Byte> data)
        {
            data.add(value);
        }

        /// <summary>
        /// Implicit operator which will automatically convert from one type to the other
        /// </summary>
//            public static implicit operator Byte(uVal8 val)
//            {
//                return (val.value);
//            }
//
//            /// <summary>
//            /// Implicit operator which will automatically convert from one type to the other
//            /// </summary>
//            public static implicit operator uVal8(Byte val)
//            {
//                return new uVal8(val);
//            }
    }

    public static class uVal8_2 extends uVal8 //1
    {
        /// <summary>
        /// Creates a new class with the default value of 0.00
        /// </summary>
        public uVal8_2(){
            super();
        }

        /// <summary>
        /// Creates a new class with a specified default value
        /// </summary>
        /// <param name="val">The default value to initialize the class to</param>
        public uVal8_2(Byte val){
            super(val);
        }

        /// <summary>
        /// Creates a new instance of this class by reading bytes from a list.
        /// </summary>
        /// <param name="data">The byte list to read bytes from to initialize this class</param>
        public uVal8_2(ArrayList<Byte> data){
            super(data);
        }

        /// <summary>
        /// Implicit operator which will automatically convert from one type to the other
        /// </summary>
//        public static implicit operator Double(uVal8_2 val)
//    {
//        return (val.value / 100.0);
//    }
//
//        /// <summary>
//        /// Implicit operator which will automatically convert from one type to the other
//        /// </summary>
//        public static implicit operator uVal8_2(Double val)
//    {
//        return new uVal8_2(Convert.ToByte(val * 100.0));
//    }

        public String ToString(){

            return String.valueOf(value.doubleValue());
        }
    }

}
