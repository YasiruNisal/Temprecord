package com.example.yasiruw.temprecord.comms;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by yasiruw on 12/06/2018.
 */

public interface BLEFragmentI {
    public void onBLERead();
    public void onBLEWrite( byte[] value);
    public boolean onConnectionchange();
    public void BLEDisconnect();
}
