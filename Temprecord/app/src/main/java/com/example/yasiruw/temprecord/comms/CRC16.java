package com.example.yasiruw.temprecord.comms;


public class CRC16 {

    public byte crcHi;

    public byte crcLo;

    private int crc = 0xFFFF;

    public  void crc16_2(byte[] data) {
        // initial value
        int polynomial = 0x1021;   // 0001 0000 0010 0001  (0, 5, 12)

        // byte[] testBytes = "123456789".getBytes("ASCII");

        byte[] bytes = data;

        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b   >> (7-i) & 1) == 1);
                boolean c15 = ((crc >> 15    & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
            }
        }

        this.crc &= 0xffff;
        //Log.i("CRC16","CRC16-CCITT = " + Integer.toHexString(crc));
    }

    public void crc_reset(){
        crc = 0xFFFF;
    }

    public byte getCrcHi(){
        return (byte) ((crc & 0x0000ff00) >>> 8);
    }

    public byte getCrcLo(){
        return (byte) ((crc & 0x000000ff));
    }
}
