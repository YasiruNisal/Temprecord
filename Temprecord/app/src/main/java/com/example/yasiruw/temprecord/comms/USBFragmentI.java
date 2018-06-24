package com.example.yasiruw.temprecord.comms;

/**
 * Created by yasiruw on 19/06/2018.
 */

public interface USBFragmentI {

   // public void onUSBRead();
    public void onUSBWrite( byte[] value);
    public boolean USBDisconnect();
}
