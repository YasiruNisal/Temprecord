/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ublox.BLE.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.ublox.BLE.R;
import com.ublox.BLE.comms.BaseCMD;
import com.ublox.BLE.comms.CommsSerial;
import com.ublox.BLE.comms.MT2Msg_Read;
import com.ublox.BLE.comms.QueryStrings;
import com.ublox.BLE.services.BluetoothLeService;
import com.ublox.BLE.services.StoreKeyService;
import com.ublox.BLE.utils.CommsChar;
import com.ublox.BLE.utils.GattAttributes;
import com.ublox.BLE.utils.HexData;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static com.ublox.BLE.activities.DeviceControlActivity.UUID_CHARACTERISTIC_FIFO;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class Activity_Query extends Activity {
    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    public static final String EXTRAS_MESSAGE = "0";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String EXTRAS_DEVICES = "Device";

    private static final int PROGRESSBAR_MAX = 100;

    private ScrollView queryScroll;
    private FrameLayout mWrapperFL;
    private LinearLayout linearLayout;
    private TextView currentTemp;
    private TextView currenthumidity;
    private ImageView bat;
    private ImageView temp;
    private ImageView hu;
    private TextView bluetoothL;
    private TextView  rssi;
    private TextView time;
    private TextView mConnectionState;
    private TextView Lstate;
    private TextView battery;
    private TextView family;
    private TextView model;
    private TextView firmware;
    private TextView serialno;
    private TextView memory;
    private TextView manudate;
    private TextView sampleno;
    private TextView trips;

    private TextView logging;
    private TextView units;
    private TextView startdelay;
    private TextView sampleperiod;
    private TextView tripsamples;
    private TextView loggedsamples;
    private TextView startedat;
    private TextView startedby;
    private TextView stoppedat;
    private TextView stoppedby;
    private TextView firstsample;
    private TextView lastsample;

    private TextView startwithbutton;
    private TextView startondatetime;
    private TextView loopoverwrite;
    private TextView allowplacingtags;
    private TextView reusewithbutton;
    private TextView passwordprotected;
    private TextView stopwithbutton;
    private TextView stopondatetime;
    private TextView stoponsample;
    private TextView stopwhenfull;
    private TextView enablelcdmenu;
    private TextView extentedlcdmenu;
    private TextView usercomments;

    private TextView channel1enable;
    private TextView ch1enablelimits;
    private TextView ch1alarmdelay;
    private TextView ch1ul;
    private TextView ch1ll;

    private TextView channel2enable;
    private TextView ch2enablelimits;
    private TextView ch2alarmdelay;
    private TextView ch2ul;
    private TextView ch2ll;

    private boolean humidityenabled = false;
    private boolean frommenu = false;

    StoreKeyService storeKeyService;
    private ProgressDialog progress;
    private int progresspercentage;
    private int progressincrement;


    private byte[] TWFlash = new byte[144];
    private byte[] RamRead = new byte[100];
    private byte[] UserRead = new byte[512];
    private byte[] ExtraRead = new byte[284];
    private String mDeviceName;
    private String mDeviceAddress;
    private String message;
    private int firsttime = 0;
    private int timeoutdelay;

    private boolean soundon = true;
    private boolean backpress = false;

    private int state = 1;
    private ArrayList<String> Q_data = new ArrayList<String>();
    private ArrayList<String> U_data = new ArrayList<String>();
    private ArrayList<String> F_data = new ArrayList<String>();
    private ArrayList<String> R_data = new ArrayList<String>();


    HexData hexData = new HexData();
    BaseCMD baseCMD =  new BaseCMD();
    CommsSerial commsSerial = new CommsSerial();
    MT2Msg_Read mt2Msg_read;
    QueryStrings QS = new QueryStrings();
    CommsChar commsChar = new CommsChar();
    private List<BluetoothDevice> mDevices = new ArrayList<>();
    private int currentDevice = 0;

    private IBinder iBinder;


    //  private ExpandableListView mGattServicesList;
    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;
    private BluetoothGattCharacteristic characteristicTX;
    private BluetoothGattCharacteristic characteristicRX;
    private byte[] test = {0x00, 0x00};
    private byte[] returndata;

    private Handler handler =new Handler();
    private Handler handler1 =new Handler();
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    private String BLE_Address = "";
    private boolean beenHere = false;

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                finish();
            }
            iBinder = service;
            // Automatically connects to the device upon successful start-up initialization.

            //for(int i = 0; i < 5; i++) {
            mBluetoothLeService.connect(mDevices.get(currentDevice).getAddress());
            BLE_Address = mDevices.get(currentDevice).getAddress();

            //}
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected, 1);
                beenHere = true;
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected, 0);
                invalidateOptionsMenu();
                clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
//                sendData(test);
//                SystemClock.sleep(500);
                mBluetoothLeService.setCharacteristicNotification(characteristicTX,true);
                //mBluetoothLeService.setCharacteristicNotification(characteristicRX,true);

                SystemClock.sleep(5);
                sendData(HexData.STAY_UP);
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //mBluetoothLeService.readCharacteristic(characteristicRX);
                returndata = null;
                returndata = intent.getByteArrayExtra(mBluetoothLeService.EXTRA_DATA);
                //hexData.BytetoHex(returndata);
                Function(returndata);

            }
        }
    };

    private void clearUI() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_query, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        progresspercentage = 0;
        switch(item.getItemId()) {
            case R.id.action_requery:
                sendData(HexData.QUARY);
                message = "1";
                state = 1;
                progressDialoge();
                return true;
            case R.id.action_menu:
                backpress = true;
                sendData(HexData.GO_TO_SLEEP);
                try {
                    TimeUnit.MILLISECONDS.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mBluetoothLeService.disconnect();
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.action_start:
                sendData(HexData.QUARY);
                message = "2";
                state = 1;
                progressDialoge();
                return true;
            case R.id.action_stop:
                sendData(HexData.QUARY);
                message = "3";
                state = 1;
                progressDialoge();
                return true;
            case R.id.action_reuse:
                sendData(HexData.QUARY);
                message = "4";
                state = 1;
                progressDialoge();
                return true;
            case R.id.action_tag:
                sendData(HexData.QUARY);
                message = "5";
                state = 1;
                progressDialoge();
                return true;
            case R.id.menu_about:
                //sendEmail();
                return true;
            case R.id.action_find:
                sendData(HexData.FIND_L);
                message = "8";
                state = 1;
                return true;
            case R.id.menu_refresh:
                frommenu = true;
                reconnect();

                //displayGattServices(mBluetoothLeService.getSupportedGattServices());

        }
        return super.onOptionsItemSelected(item);
    }

    private void reconnect(){
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(BLE_Address);
            Log.d(TAG, "Connect request result===" + result);
        }
    }


    private void updateConnectionState(final int resourceId, final int num) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
                if(num == 0)
                    mConnectionState.setTextColor(RED);
                else if(num == 1)
                    mConnectionState.setTextColor(GREEN);
            }
        });
    }


    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();


        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, GattAttributes.lookup(uuid, unknownServiceString));

            // If the service exists for HM 10 Serial, say so.
            if(GattAttributes.lookup(uuid, unknownServiceString) == "HM 10 Serial") {
                //isSerial.setText("Serial Connection Available");
            } else {
                //isSerial.setText("No, serial :-(");
            }
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            // get characteristic when UUID matches RX/TX UUID
            characteristicTX = gattService.getCharacteristic(UUID_CHARACTERISTIC_FIFO);
            characteristicRX = gattService.getCharacteristic(UUID_CHARACTERISTIC_FIFO);
        }

    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }



    private void sendData(final byte[] test){


        Thread thread = new Thread() {
            @Override
            public void run() {
                if (mConnected && test != null && characteristicTX != null && characteristicRX != null && mBluetoothLeService != null) {
                    //characteristicTX.setValue(new byte[] {test[i]});
                    mBluetoothLeService.writeCharacteristic(characteristicTX, test);
                    mBluetoothLeService.setCharacteristicNotification(characteristicRX, true);
                }
            }
        };

        thread.start();

        //for(int i = 0; i < test.length; i++) {

        //SystemClock.sleep(2);
        //}

    }

    @Override
    public void onBackPressed() {
        backpress = true;
        sendData(HexData.GO_TO_SLEEP);
        try {
            TimeUnit.MILLISECONDS.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mBluetoothLeService.disconnect();
        try {
            TimeUnit.MILLISECONDS.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //super.onBackPressed();
    }

    private void Function(final byte[] in){


        Runnable runnableCode = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                byte[] query;

                if (storeKeyService.getDefaults("SOUND", getApplication()) != null && storeKeyService.getDefaults("SOUND", getApplication()).equals("1"))
                    soundon = true;
                else
                    soundon = false;

                progresspercentage = progresspercentage + 5;
                switch (state) {
                    case 0:

//                        hexData.BytetoHex(in);
//                        query = baseCMD.ReadByte(in);
//                        Q_data = baseCMD.CMDQuery(query);
//                        SetUI(Q_data);

                        if (message.equals("2")) {
                            sendData(HexData.START_L);
                            state = 2;
                        } else if (message.equals("3")) {
                            sendData(HexData.STOP_L);
                            state = 3;
                        } else if (message.equals("4")) {
                            sendData(HexData.REUSE_L);
                            state = 4;
                        } else if (message.equals("5")) {
                            sendData(HexData.TAG_L);
                            state = 5;
                        }else if(message.equals("1")){
                            sendData(HexData.QUARY);
                            state = 8;
                            //mBluetoothLeService.disconnect();
                        }else if (message.equals("8")) {
                            sendData(HexData.FIND_L);
                            Log.d(TAG, "&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&11");
                            state = 5;
                        }
                        break;
                    case 1:

                        //Log.d("++++++", "am i coming to this place");
                        sendData(HexData.QUARY);
                        state = 0;
                        //firsttime = 0;
                        break;
                    case 2:

                        if (in[0] == 0x00) {
                            BuildDialogue("Start", "The Logger has started successfully.", 0);
                            makesound(getApplicationContext(), R.raw.definite);
                        } else {
                            BuildDialogue("Start", "Start not successful.\n\nThe logger is either running or is in stopped state", 0);
                            makesound(getApplicationContext(), R.raw.unsure);
                        }
                        SystemClock.sleep(1000);
                        sendData(HexData.QUARY);

                        state = 8;
                        break;
                    case 3:
                        if (in[0] == 0x00) {
                            BuildDialogue("Stop", "The Logger has stopped successfully.", 0);
                            makesound(getApplicationContext(), R.raw.definite);
                        } else {
                            BuildDialogue("Stop", "Stop not successful.\n\nThe logger is either in ready or already has stopped", 0);
                            makesound(getApplicationContext(), R.raw.unsure);
                        }
                        SystemClock.sleep(1000);
                        sendData(HexData.QUARY);

                        state = 8;
                        break;
                    case 4:
                        if (in[0] == 0x00) {
                            BuildDialogue("Re-Use", "The Logger has been successfully re-used.", 0);
                            makesound(getApplicationContext(), R.raw.definite);
                        } else {
                            BuildDialogue("Re-Use", "Re-use unsuccessful.\n\nTry stopping the logger first", 0);
                            makesound(getApplicationContext(), R.raw.unsure);
                        }
                        SystemClock.sleep(1000);
                        sendData(HexData.QUARY);

                        state = 8;
                        break;
                    case 5:

                        if (message.equals("8")) {
                            if (in[0] == 0x00) {
                                BuildDialogue("Find Logger", "Green LED should be blinking on the logger now.", 0);
                                makesound(getApplicationContext(), R.raw.definite);
                            } else {
                                BuildDialogue("Find Logger", "An error occurred", 0);
                                makesound(getApplicationContext(), R.raw.unsure);
                            }
                        }else{
                            if (in[0] == 0x00) {
                                BuildDialogue("Tag", "The Logger has been successfully tagged.", 0);
                                makesound(getApplicationContext(), R.raw.definite);
                            } else {
                                BuildDialogue("Tag", "Tagging unsuccessful.\n\nTry starting the logger first", 0);
                                makesound(getApplicationContext(), R.raw.unsure);
                            }
                        }
                        SystemClock.sleep(1000);
                        sendData(HexData.QUARY);

                        state = 8;
                        break;
                    case 6:
                        break;
                    case 7:
//                        hexData.BytetoHex(in);
//                        query = baseCMD.ReadByte(in);
//                        Q_data = baseCMD.CMDQuery(query);
                        sendData(HexData.GO_TO_SLEEP);
                        state = 6;
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        mBluetoothLeService.disconnect();
                        unbindService(mServiceConnection);

                        break;
                    case 8:
                        hexData.BytetoHex(in);
                        query = baseCMD.ReadByte(in);
                        Q_data = baseCMD.CMDQuery(query);


                        //sets up reading the first 120 bytes from flash, then the message is constructed with 0x55 and stuff
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_TRW, 0, 120, 120, 3);
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));
                        //sendData(mt2Msg_read.Read_into_writeByte(false));
                        state = 9;
                        break;
                    case 9:// reads the 120 bytes till write_into_readbyte returns true. then the state is changed to read the next 24 bytes
                        if(mt2Msg_read.write_into_readByte(baseCMD.ReadByte(in))){
                            state = 10;
                            System.arraycopy(mt2Msg_read.memoryData, 0, TWFlash, 0, 120);
                            //TWFlash = mt2Msg_read.memoryData;

                        }
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;
                    case 10://sets up to read the next 24 bytes
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_TRW, 120, 144, 24, 3);
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));
                        //sendData(mt2Msg_read.Read_into_writeByte(false));
                        state = 11;
                        break;
                    case 11://next 24 bytes are read and appened to the TWFlassh byte array. Then moving on to reading user flash
                        if(mt2Msg_read.write_into_readByte(baseCMD.ReadByte(in))){
                            state = 12;
                            System.arraycopy(mt2Msg_read.memoryData, 0, TWFlash, 120, 24);
                            hexData.BytetoHex(TWFlash);
                            F_data = baseCMD.CMDFlash(TWFlash);

                            // call the decoding functon here to decode all we need and send the data back in an string array so that they can be desplayed in appropriate textviews
                        }
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;
                    case 12:
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_USER, 0, 250, 250, 3);
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));

                        state = 13;
                        break;
                    case 13:
                        if(mt2Msg_read.write_into_readByte(baseCMD.ReadByte(in))) {
                            state = 14;
                            System.arraycopy(mt2Msg_read.memoryData, 0, UserRead, 0, 250);
                            //hexData.BytetoHex(TWFlash);
                        }
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;
                    case 14:
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_RAM, 0, 100, 100, 3);
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));
                        state = 15;
                        break;
                    case 15:
                        if(mt2Msg_read.write_into_readByte(baseCMD.ReadByte(in))) {

                            System.arraycopy(mt2Msg_read.memoryData, 0, RamRead, 0, 100);
                            hexData.BytetoHex(RamRead);
                            R_data = baseCMD.CMDRamRead(RamRead);
                            state = 16;
                        }
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;
                    case 16:
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_USER, 250, 500, 250, 3);
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));
                        state = 17;
                        break;
                    case 17:
                        if(mt2Msg_read.write_into_readByte(baseCMD.ReadByte(in))) {
                            state = 18;
                            System.arraycopy(mt2Msg_read.memoryData, 0, UserRead, 250, 250);
                            //hexData.BytetoHex(TWFlash);
                        }
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;

                    case 18:
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_USER, 500, 512, 250, 3);
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));
                        state = 19;
                        break;
                    case 19:
                        if(mt2Msg_read.write_into_readByte(baseCMD.ReadByte(in))) {
                            state = 20;
                            System.arraycopy(mt2Msg_read.memoryData, 0, UserRead, 500, 12);
                            hexData.BytetoHex(UserRead);
                            U_data = baseCMD.CMDUserRead(UserRead);
                            SetUI();
                        }
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;
                    case 20:
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_EXTRA, 114, 350, 300, 3);
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));
                        state = 21;
                        break;
                    case 21:
                        if(mt2Msg_read.write_into_readByte(baseCMD.ReadByte(in))) {
                            state = 22;
                            System.arraycopy(mt2Msg_read.memoryData, 0, ExtraRead, 0, 236);
                            //hexData.BytetoHex(TWFlash);
                        }
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;
                    case 22:
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_EXTRA, 350, 398, 300, 3);
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));
                        state = 23;
                        break;
                    case 23:
                        if(mt2Msg_read.write_into_readByte(baseCMD.ReadByte(in))) {
                            if(firsttime>0){state = 7;}else{state = 24;}
                            System.arraycopy(mt2Msg_read.memoryData, 0, ExtraRead, 236, 48);
                            firsttime++;
                            hexData.BytetoHex(ExtraRead);
                            progresspercentage = 100;
                            progress.cancel();
                        }
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;
                    case 24:
                        sendData(HexData.BLE_ACK);
                        state = 25;
                        break;
                    case 25:
                        hexData.BytetoHex(in);
                        FifteenSecTimeout();
                        break;

                }
            }
        };handler1.postDelayed(runnableCode,1);
    }


    private void BuildDialogue(String str1, String str2, final int press){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(Activity_Query.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(Activity_Query.this);
        }
        builder.setTitle(str1)
                .setMessage(str2)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(press == 1){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        // continue with delete
                    }
                })
                .setIcon(R.drawable.ic_message)
                .show();
    }

    public void progressDialoge(){

        progress=new ProgressDialog(this);
        progress.setMessage("Querying Logger");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(false);
        progress.setProgress(0);
        progress.setCancelable(false);
        progress.setButton(DialogInterface.BUTTON_NEGATIVE, "Abort", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mBluetoothLeService.disconnect();
                dialog.dismiss();
                backpress = true;
                if(family.getText().toString() == "")
                BuildDialogue("Query Aborted", "Entries might be empty!\nGo back to menu and reconnect", 1);
            }
        });
        progress.setProgressNumberFormat("");
        progress.setMax(PROGRESSBAR_MAX);
        progress.show();


        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;

                while(progresspercentage < PROGRESSBAR_MAX) {
                    progress.setProgress(progresspercentage);
                }
            }
        };
        t.start();
    }

    private void FifteenSecTimeout(){
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    TimeUnit.SECONDS.sleep(timeoutdelay/10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendData(HexData.GO_TO_SLEEP);
                try {
                    TimeUnit.MILLISECONDS.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               //BuildDialogue("BlueTooth Timeout", "BlueTooth has timed out. Will require a reconnect\n Start from the menu page!", 2);
                if(!backpress && !frommenu) {

                    mBluetoothLeService.disconnect();
//                    unbindService(mServiceConnection);

                }
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void SetUI(){
        serialno.setText(Q_data.get(0));
        firmware.setText(Q_data.get(1));
        model.setText(QS.GetGeneration(Integer.parseInt(Q_data.get(2))));
        family.setText(QS.GetType(Integer.parseInt(Q_data.get(3))));

        Lstate.setText( QS.GetState(Integer.parseInt(Q_data.get(5))));
        battery.setText(  R_data.get(17)+"%");
        currentTemp.setText(R_data.get(9) + " °C");
        currenthumidity.setText(R_data.get(11) + " %");

        manudate.setText(F_data.get(0));
        memory.setText(F_data.get(1));
        startondatetime.setText(R_data.get(0));
        startwithbutton.setText(R_data.get(1));
        stopwithbutton.setText(R_data.get(3));
        stopwhenfull.setText(R_data.get(5));
        stoponsample.setText(R_data.get(6));

        startedby.setText(R_data.get(2));
        stoppedby.setText(R_data.get(4));

        loggedsamples.setText(R_data.get(7));
        tripsamples.setText(R_data.get(7));
        lastsample.setText(R_data.get(8));

        sampleno.setText(U_data.get(5));
        trips.setText(U_data.get(3));

        startedat.setText(U_data.get(1));
        stoppedat.setText(U_data.get(2));

        loopoverwrite.setText(U_data.get(6));
        enablelcdmenu.setText(U_data.get(7));
        allowplacingtags.setText(U_data.get(8));
        reusewithbutton.setText(U_data.get(9));
        passwordprotected.setText(U_data.get(10));
        stopondatetime.setText(U_data.get(11));
        extentedlcdmenu.setText(U_data.get(12));


        channel1enable.setText(U_data.get(13));
        ch1enablelimits.setText(U_data.get(14));
        ch1ul.setText(U_data.get(15)+ " °C");
        ch1ll.setText(U_data.get(16)+ " °C");

        channel2enable.setText(U_data.get(18));
        ch2enablelimits.setText(U_data.get(19));
        ch2ul.setText(U_data.get(20)+ " %");
        ch2ll.setText(U_data.get(21)+ " %");

        sampleperiod.setText(U_data.get(23));
        startdelay.setText(U_data.get(24));

        if((R_data.get(0)).equals("Yes")){
            //set the starttimedate here;
        }


        if((U_data.get(18)).equals("Yes")){
            logging.setText("Temperature + Humidity");
        }else{
            logging.setText("Temperature");
        }

        units.setText("°C");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        time.setText(currentDateandTime);

        ch1alarmdelay.setText( R_data.get(12)+ " Samples");
        ch2alarmdelay.setText( R_data.get(16)+ " Samples");


        if(Double.parseDouble(Q_data.get(6)) > 66){
            bat.setBackgroundResource(R.drawable.bfull);
        }else if(Double.parseDouble(Q_data.get(6)) > 33){
            bat.setBackgroundResource(R.drawable.bhalf);
        }else if(Double.parseDouble(Q_data.get(6)) > 0){
            bat.setBackgroundResource(R.drawable.blow);

        }
        usercomments.setText( U_data.get(27));

        if(!baseCMD.ch2Enable){
            currenthumidity.setText("--");
        }


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__query);
        getActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
        AssetManager am = this.getAssets();


        Typeface font = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf");



        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        message = intent.getStringExtra(EXTRAS_MESSAGE);
        mDevices = intent.getParcelableArrayListExtra(EXTRAS_DEVICES);

        linearLayout = (LinearLayout) findViewById(R.id.attop);
        mWrapperFL = (FrameLayout) findViewById(R.id.flWrapper);
        queryScroll = (ScrollView) findViewById(R.id.queryscroll);
//        m.setText(message);
        // Sets up UI references.
        //((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
        bat = (ImageView) findViewById(R.id.imageView1);
        temp = (ImageView) findViewById(R.id.imageView2);
        hu = (ImageView) findViewById(R.id.imageView3);

        currentTemp = (TextView) findViewById(R.id.temperature);
        currentTemp.setTypeface(font);
        currenthumidity = (TextView) findViewById(R.id.humiditytop);
        currenthumidity.setTypeface(font);
        time = (TextView) findViewById(R.id.time);
        time.setTypeface(font);
        mConnectionState = (TextView) findViewById(R.id.connection_state);
        Lstate = (TextView) findViewById(R.id.state);
        Lstate.setTypeface(font);
        battery = (TextView) findViewById(R.id.battery);
        battery.setTypeface(font);
        family = (TextView) findViewById(R.id.family);
        model = (TextView) findViewById(R.id.model);
        firmware = (TextView) findViewById(R.id.firmware);
        serialno = (TextView) findViewById(R.id.serialno);
        memory = (TextView) findViewById(R.id.memory);
        manudate = (TextView) findViewById(R.id.manudate);
        sampleno = (TextView) findViewById(R.id.sampleno);
        trips = (TextView) findViewById(R.id.trips);

        logging = (TextView) findViewById(R.id.logging);
        units = (TextView) findViewById(R.id.units);
        startdelay = (TextView) findViewById(R.id.startdelay);
        sampleperiod = (TextView) findViewById(R.id.sampleperiod);
        tripsamples = (TextView) findViewById(R.id.tripsamples);
        loggedsamples = (TextView) findViewById(R.id.loggedsamples);
        startedat = (TextView) findViewById(R.id.startedat);
        startedby = (TextView) findViewById(R.id.startedby);
        stoppedat = (TextView) findViewById(R.id.stoppedat);
        stoppedby = (TextView) findViewById(R.id.stoppedby);
        firstsample = (TextView) findViewById(R.id.firstsample);
        lastsample = (TextView) findViewById(R.id.lastsample);

        startwithbutton = (TextView) findViewById(R.id.startwithbutton);
        startondatetime = (TextView) findViewById(R.id.startondatetime);
        loopoverwrite = (TextView) findViewById(R.id.loopoverwrite);
        allowplacingtags = (TextView) findViewById(R.id.allowplacingtags);
        reusewithbutton = (TextView) findViewById(R.id.reusewithbutton);
        passwordprotected = (TextView) findViewById(R.id.passwordprotected);
        stopwithbutton = (TextView) findViewById(R.id.stopwithbutton);
        stopondatetime = (TextView) findViewById(R.id.stopondatatime);
        stoponsample = (TextView) findViewById(R.id.stoponsample);
        stopwhenfull = (TextView) findViewById(R.id.stopwhenfull);
        enablelcdmenu = (TextView) findViewById(R.id.enablelcdmenu);
        extentedlcdmenu = (TextView) findViewById(R.id.extendedlcdmenu);
        usercomments = (TextView) findViewById(R.id.usercomments);

        channel1enable = (TextView) findViewById(R.id.c1enabled);
        ch1enablelimits = (TextView) findViewById(R.id.c1enablelimits);
        ch1alarmdelay = (TextView) findViewById(R.id.alarmdelay1);
        ch1ul = (TextView) findViewById(R.id.upperlimit1);
        ch1ll = (TextView) findViewById(R.id.lowerlimit1);


        channel2enable = (TextView) findViewById(R.id.c2enabled);
        ch2enablelimits = (TextView) findViewById(R.id.c2enablelimits);
        ch2alarmdelay = (TextView) findViewById(R.id.alarmdelay2);
        ch2ul = (TextView) findViewById(R.id.upperlimit2);
        ch2ll = (TextView) findViewById(R.id.lowerlimit2);

        getActionBar().setTitle(mDevices.get(currentDevice).getName());
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        tryAgain();
        progressDialoge();
        //ScrollListener();
        queryScroll.getViewTreeObserver().addOnScrollChangedListener(new ScrollPositionObserver());

    }

    private void tryAgain(){
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(6);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!beenHere)
                    reconnect();
            }
        };
        t.start();

    }

    private class ScrollPositionObserver implements ViewTreeObserver.OnScrollChangedListener {

        private int mImageViewHeight;

        public ScrollPositionObserver() {
            mImageViewHeight = 100;//getResources().getDimensionPixelSize(100);
        }

        @Override
        public void onScrollChanged() {
            int scrollY = Math.min(Math.max(queryScroll.getScrollY()/3, 0), mImageViewHeight);
            timeoutdelay++;
            // changing position of ImageView
            linearLayout.setTranslationY(scrollY *2);

            // alpha you could set to ActionBar background
            float alpha = scrollY / (float) mImageViewHeight;
        }
    }

//    private void ScrollListener(){
//        queryScroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                timeoutdelay++;
//            }
//        });
//    }

    //send email
    protected void sendEmail() {


        Log.i("Send email", "");
        String[] TO = {storeKeyService.getDefaults("EMAIL_TO", getApplication())};
        String[] CC = {storeKeyService.getDefaults("EMAIL_CC", getApplication())};//storeKeyService.getDefaults("EMAIL_CC", getApplication());
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        //Log.i("++++++++", storeKeyService.getDefaults("EMAIL_TO", getApplication()));
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Logger ("+Q_data.get(0)+") Query Details");
        emailIntent.putExtra(Intent.EXTRA_TEXT,   EmailText()+"\n\n\n" + storeKeyService.getDefaults("NAME", getApplication()));

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email.", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Activity_Query.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    public String EmailText(){
        String email = "Logger Family: " + QS.GetType(Integer.parseInt(Q_data.get(3)))+"\n"+
                "Logger Serial Number: " + Q_data.get(0)+"\n\n"+
                "Current Logger Temperature: " + R_data.get(9) + " °C\n\n"+
                "Logger State: " + QS.GetState(Integer.parseInt(Q_data.get(5)))+"\n"+
                "Trip Samples: " + R_data.get(7)+"\n"+
                "Start Dealy: " + U_data.get(24)+"\n"+
                "Sample Period: " + U_data.get(23)+"\n"+
                "Channel 1 Enabled: " + U_data.get(13)+"\n"+
                "Channel 2 Enabled: " + U_data.get(18);

        return email;
    }

    private void makesound(Context context, int resid){
        if(soundon == true) {
            final MediaPlayer mp = MediaPlayer.create(context, resid);
            mp.start();
        }
    }




}
