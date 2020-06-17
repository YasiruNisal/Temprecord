package com.temprecord.temprecordapp.comms;

/**
 * Created by yasiruw on 12/06/2018.
 */

public interface BLEFragmentI {
     void onBLERead();
     void onBLEWrite( byte[] value);
     void BLEDisconnect();
}
