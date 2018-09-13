package com.temprecordapp.yasiruw.temprecord.Types;

import java.util.ArrayList;

/**
 * Created by Yasiru on 13-Dec-17.
 */

public abstract class baseType {

    public static final int  ByteSize =  0;

    public int TypeSize;

    protected baseType(int size){
        TypeSize =size;
    }

    public int getTypeSize(){
        return TypeSize;
    }

    public abstract void ToByte(ArrayList<Byte> data);

    public byte[] GetByteArray() {
        ArrayList<Byte> data = new ArrayList<Byte>();
        byte[] b = new byte[0];
        this.ToByte(data);
        for (int i = 0; i < data.size(); i++) {
            b[i] = data.get(i).byteValue();
        }
        return b;
    }



    public abstract static class baseClass extends baseType{

        protected baseClass(int size) {
            super(size);
        }

        public abstract int CalculateSize();
    }
}


