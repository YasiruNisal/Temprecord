package com.ublox.BLE.utils;

import android.bluetooth.BluetoothDevice;

import com.ublox.BLE.services.BluetoothLeService;

/**
 * Created by Yasiru on 13/03/2018.
 */

public class RecentBLEDevice {

    public BluetoothDevice ble;

    public RecentBLEDevice (BluetoothDevice ble){
        this.ble = ble;
    }

}
