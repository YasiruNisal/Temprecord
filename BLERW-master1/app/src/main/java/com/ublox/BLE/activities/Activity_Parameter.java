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
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.ikovac.timepickerwithseconds.TimePicker;
import com.ublox.BLE.R;
import com.ublox.BLE.Types.dataType;
import com.ublox.BLE.comms.BaseCMD;
import com.ublox.BLE.comms.CRC16;
import com.ublox.BLE.comms.CommsSerial;
import com.ublox.BLE.comms.MT2Msg_Read;
import com.ublox.BLE.comms.MT2Msg_Write;
import com.ublox.BLE.comms.QueryStrings;
import com.ublox.BLE.services.BluetoothLeService;
import com.ublox.BLE.services.StoreKeyService;
import com.ublox.BLE.utils.CHUserData;
import com.ublox.BLE.utils.CommsChar;
import com.ublox.BLE.utils.GattAttributes;
import com.ublox.BLE.utils.HexData;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
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
public class Activity_Parameter extends Activity  implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    public static final String EXTRAS_MESSAGE = "0";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String EXTRAS_DEVICES = "Device";

    private static final int PROGRESSBAR_MAX = 100;


    private boolean celsiusfahrenheit = false;

    private ScrollView parameterscroll;
    private TextView currentTemp;
    private ImageView bat;
    private ImageView temp;
    private ImageView hu;
    private TextView time;
    private TextView mConnectionState;
    private TextView Lstate;
    private TextView battery;
    private TextView currenthumidity;

    private TextView passwordtxt;
    private TextView passwordconfirmtxt;
    private TextView usercommenttxt;

    private RadioButton celsius;
    private RadioButton fahrenheit;
    private RadioButton startwithdelay;
    private RadioButton stopbyuser;
    private RadioButton startondatetime;
    private RadioButton stopwhenfull;
    private RadioButton stoponsample;
    private RadioButton stopondatetime;

    private RadioGroup imperialunit;
    private RadioGroup startoptions;
    private RadioGroup stopoptions;

    private Button startwithdelaybutton;
    private Button startondatetimebutton;
    private Button sampleperiodbutton;
    private EditText stoponsamplebutton;
    private Button stopondatebutton;

    private EditText ch1upperlimitnb;
    private EditText ch1lowerlimitnb;
    private EditText ch1alarmdelaynb;

    private EditText ch2upperlimitnb;
    private EditText ch2lowerlimitnb;
    private EditText ch2alarmdelaynb;

    private CheckBox BLEenergysave;
    private CheckBox loopovewritecb;
    private CheckBox startwithbuttoncb;
    private CheckBox stopwithbuttoncb;
    private CheckBox reusewithbuttoncb;
    private CheckBox allowplacingtagcb;
    private CheckBox enablelcdmenucb;
    private CheckBox extendedlcdmenucb;
    private CheckBox passwordenabledcb;

    private CheckBox ch1enabledcb;
    private CheckBox ch1limitenabledcb;

    private CheckBox ch2enabledcb;
    private CheckBox ch2limitenabledcb;

    private Button Programparam;

    private boolean backpress = false;

    private int STOPONSAMPLE;

    private double CH1UPPERLIMIT;
    private double CH1LOWERLIMIT;
    private int CH1ALARMDELAY;

    private double CH2UPPERLIMIT;
    private double CH2LOWERLIMIT;
    private int CH2ALARMDELAY;

    private int whichbutton;
    private int timeoutdelay;


    private boolean humidityenabled = false;

    StoreKeyService storeKeyService;
    private ProgressDialog progress;
    private int progresspercentage;
    private int progressincrement;


    private byte[] TWFlash = new byte[144];
    private byte[] RamRead = new byte[100];
    private byte[] UserRead = new byte[512];
    private byte[] UserReadtemp = new byte[398];
    private byte[] ExtraRead = new byte[284];
    private String mDeviceName;
    private String mDeviceAddress;
    private String message;
    private String BLE_Address;
    private int firsttime = 0;

    private boolean soundon = true;

    private int state = 1;
    private ArrayList<String> Q_data = new ArrayList<String>();
    private ArrayList<String> U_data = new ArrayList<String>();
    private ArrayList<String> F_data = new ArrayList<String>();
    private ArrayList<String> R_data = new ArrayList<String>();


    HexData hexData = new HexData();
    BaseCMD baseCMD =  new BaseCMD();
    CommsSerial commsSerial = new CommsSerial();
    MT2Msg_Read mt2Msg_read;
    MT2Msg_Write mt2Msg_write;
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
//        sendData(HexData.GO_TO_SLEEP);
//        SystemClock.sleep(300);
        //unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_parameters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        progresspercentage = 0;
        switch(item.getItemId()) {
            case R.id.action_program:
                if(baseCMD.state == 2) {
                    programbutton();
                }else{
                    BuildDialogue("Can't Program parameters", "Parameters can't be programed when the logger is in "+QS.GetState(baseCMD.state)+" state.\nPut the logger in to ready state!",2);
                }
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
            case R.id.menu_about:
                //sendEmail();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        //final byte[] tx = str.getBytes();


        Thread thread = new Thread() {
            @Override
            public void run() {
                if (mConnected && !backpress && test != null && characteristicTX != null && characteristicRX != null && mBluetoothLeService != null) {
                    //characteristicTX.setValue(new byte[] {test[i]});
                    mBluetoothLeService.writeCharacteristic(characteristicTX, test);
                    mBluetoothLeService.setCharacteristicNotification(characteristicRX,true);
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
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                        break;
                    case 1:
                        //Log.d("++++++", "am i coming to this place");
                        sendData(HexData.QUARY);
                        state = 8;
                        //firsttime = 0;
                        break;

                    case 2:

                        sendData(commsSerial.WriteByte(baseCMD.WriteRTC()));
                        Log.d(")))))))", "Sending RTCccccccccc");
                        state = 8;
                        break;
                    case 3:
                        break;
                    case 7:
                        hexData.BytetoHex(in);
//                        query = baseCMD.ReadByte(in);
//                        Q_data = baseCMD.CMDQuery(query);
                        sendData(HexData.GO_TO_SLEEP);
                        state = 6;
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(mBluetoothLeService != null){
                            mBluetoothLeService.disconnect();
                            unbindService(mServiceConnection);
                        }

                        break;
                    case 8:
                        hexData.BytetoHex(in);
                        query = baseCMD.ReadByte(in);
                        Q_data = baseCMD.CMDQuery(query);
                        hexData.BytetoHex(in);
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
                            System.arraycopy(UserRead, 114  , UserReadtemp, 0, 398);
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
                            state = 25;
                            System.arraycopy(mt2Msg_read.memoryData, 0, ExtraRead, 236, 48);
                            firsttime++;
                            hexData.BytetoHex(ExtraRead);
                            progresspercentage = 100;
                            progress.cancel();
                        }
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;
                    case 24:
                        hexData.BytetoHex(in);
                        sendData(HexData.BLE_ACK);
                        state = 25;
                        break;
                    case 25:

                        hexData.BytetoHex(in);
                        ThirtySecTimeout();
                        break;
                    case 26:
                        hexData.BytetoHex(in);
                        if(in[0] == commsChar.CMD_ACK){
                            hexData.BytetoHex(UserReadtemp);
                            sendData(commsSerial.WriteByte(mt2Msg_write.writeSetup()));
                            state = 27;
                        }
                        break;
                    case 27:
                        progresspercentage = progresspercentage + 10;
                        hexData.BytetoHex(in);
                        if(mt2Msg_write.writeDone()){
                            state = 28;
                            progress.cancel();
                        }
                        sendData(commsSerial.WriteByte(mt2Msg_write.writeFill()));
                        break;
                    case 28:
                        sendData(commsSerial.WriteByte(mt2Msg_write.writeFlash()));
                        Toast.makeText(Activity_Parameter.this,"Programmed Successfully", Toast.LENGTH_SHORT).show();
                        state = 7;
                        break;
                    case 29:
                        hexData.BytetoHex(in);
                        mt2Msg_write = new MT2Msg_Write(UserReadtemp);
                        sendData(commsSerial.WriteByte(mt2Msg_write.writeSetup()));
                        state = 26;
                        break;
                    case 30:
                        hexData.BytetoHex(in);

                        sendData(commsSerial.WriteByte(baseCMD.WriteRTC()));

                        state  = 31;
                        break;
                    case 31:
                        hexData.BytetoHex(in);
                        sendData(commsSerial.WriteByte(baseCMD.ReadRTC()));
                        state  = 29;
                        break;

                }
            }
        };handler1.postDelayed(runnableCode,1);
    }


    private void BuildDialogue(String str1, String str2, final int press){
        backpress = true;
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(Activity_Parameter.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(Activity_Parameter.this);
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
        progress.setMessage("Loading Parameters");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(false);
        progress.setProgress(0);
        progress.setCancelable(false);
        progress.setButton(DialogInterface.BUTTON_NEGATIVE, "Abort", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mBluetoothLeService.disconnect();

                dialog.dismiss();
                BuildDialogue("Parameter Read Aborted", "Entries might be empty!\nGo back to menu and reconnect", 1);
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

    public void progressDialoge2(){

        progress=new ProgressDialog(this);
        progress.setMessage("Programming Parameters");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(false);
        progress.setProgress(0);
        progress.setCancelable(false);
        progress.setButton(DialogInterface.BUTTON_NEGATIVE, "Abort", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mBluetoothLeService.disconnect();
                dialog.dismiss();
            }
        });
        progress.setProgressNumberFormat("");
        progress.setMax(40);
        progress.show();
        progresspercentage = 0;

        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;

                while(progresspercentage < 40) {
                    progress.setProgress(progresspercentage);
                }
            }
        };
        t.start();
    }

    private void ThirtySecTimeout(){
        final Thread t = new Thread() {
            @Override
            public void run() {
                timeoutdelay = 0;
                try {
                    TimeUnit.SECONDS.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    TimeUnit.SECONDS.sleep(timeoutdelay/8);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendData(HexData.GO_TO_SLEEP);
                try {
                    TimeUnit.MILLISECONDS.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(!backpress){
                    Log.d(TAG, "coming here after a timeout");
                    mBluetoothLeService.disconnect();

                }
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                if(mBluetoothLeService != null)
//                unbindService(mServiceConnection);
            }
        };
        t.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void SetUI(){


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        time.setText(currentDateandTime);
        Lstate.setText( QS.GetState(Integer.parseInt(Q_data.get(5))));
        battery.setText(  R_data.get(17)+"%");
        currentTemp.setText(R_data.get(9) + " °C");
        currenthumidity.setText(R_data.get(11) + " %");

        if((R_data.get(0)).equals("Yes")){
            //set the starttimedate here;
        }

        if(Double.parseDouble(Q_data.get(6)) > 66){
            bat.setBackgroundResource(R.drawable.bfull);
        }else if(Double.parseDouble(Q_data.get(6)) > 33){
            bat.setBackgroundResource(R.drawable.bhalf);
        }else if(Double.parseDouble(Q_data.get(6)) > 0){
            bat.setBackgroundResource(R.drawable.blow);

        }

        if(baseCMD.energysave){BLEenergysave.setChecked(true);}else{BLEenergysave.setChecked(false);}
        if(baseCMD.ImperialUnit){fahrenheit.setChecked(true);}else {celsius.setChecked(true);}
        if(baseCMD.LoopOverwrite){loopovewritecb.setChecked(true);}else{loopovewritecb.setChecked(false);}
        if(baseCMD.StartwithButton){startwithbuttoncb.setChecked(true);}else{startwithbuttoncb.setChecked(false);}
        if(baseCMD.StopwithButton){stopwithbuttoncb.setChecked(true);}else {stopwithbuttoncb.setChecked(false);}
        if(baseCMD.ReUsewithButton){reusewithbuttoncb.setChecked(true);}else {reusewithbuttoncb.setChecked(false);}
        if(baseCMD.AllowplacingTags){allowplacingtagcb.setChecked(true);}else {allowplacingtagcb.setChecked(false);}
        if(baseCMD.EnableLCDMenu){enablelcdmenucb.setChecked(true);}else {enablelcdmenucb.setChecked(false);}
        if(baseCMD.ExtendedLCDMenu){extendedlcdmenucb.setChecked(true);}else {extendedlcdmenucb.setChecked(false);}
        if(baseCMD.passwordEnabled){passwordenabledcb.setChecked(true);}else {passwordenabledcb.setChecked(false);}

        //if(baseCMD.StartwithDelay){startwithdelay.setChecked(true);}else {startwithdelay.setChecked(false);}
        if(baseCMD.StartonDateTime){startondatetime.setChecked(true);}else {startwithdelay.setChecked(true);}
        if(baseCMD.StopwhenFull){stopwhenfull.setChecked(true);}else {stopwhenfull.setChecked(false);}
        if(baseCMD.StoponSample){stoponsample.setChecked(true);}else {stoponsample.setChecked(false);}
        if(baseCMD.StoponDateTime){stopondatetime.setChecked(true);}else {stopondatetime.setChecked(false);}
        if(!baseCMD.StopwhenFull & !baseCMD.StoponSample & !baseCMD.StoponDateTime){stopbyuser.setChecked(true);}else {stopbyuser.setChecked(false);}


        if(baseCMD.ch1Enable){ch1enabledcb.setChecked(true);}else {ch1enabledcb.setChecked(false);}
        if(baseCMD.ch1limitEnabled){ch1limitenabledcb.setChecked(true);}else{ch1limitenabledcb.setChecked(false);}
        if(baseCMD.ch2Enable){ch2enabledcb.setChecked(true);}else{ch2enabledcb.setChecked(false);}
        if(baseCMD.ch2limitEnabled){ch2limitenabledcb.setChecked(true);}else {ch2limitenabledcb.setChecked(false);}

        startwithdelaybutton.setText(QS.Period(baseCMD.startDelay*1000));
        sampleperiodbutton.setText(QS.Period(baseCMD.samplePeriod*1000));
        stoponsamplebutton.setText(U_data.get(26));
        //startondatetimebutton.setText(baseCMD.startdatetime+"");

        ch1upperlimitnb.setText(baseCMD.ch1Hi/10.0+"");
        ch1lowerlimitnb.setText(baseCMD.ch1Lo/10.0+"");

        ch2upperlimitnb.setText(baseCMD.ch2Hi/10.0+"");
        ch2lowerlimitnb.setText(baseCMD.ch2Lo/10.0+"");

        startondatetimebutton.setText(QS.calendertoString(baseCMD.timestartstopdatetime));
        if(stopondatetime.isChecked()) {
            Calendar c = Calendar.getInstance();
            c = baseCMD.timestartstopdatetime;
            c.add(Calendar.SECOND, baseCMD.samplePeriod*baseCMD.numberstopon);
            stopondatebutton.setText(QS.calendertoString(c));
        }else{
            stopondatebutton.setText(QS.calendertoString(baseCMD.timestartstopdatetime));
        }

        if(baseCMD.passwordEnabled){
            promtPassword();
        }

        usercommenttxt.setText( U_data.get(27));

        if(!baseCMD.ch2Enable){
            currenthumidity.setText("--");
        }

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);
        getActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));

        AssetManager am = this.getAssets();


        Typeface font = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf");


        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        message = intent.getStringExtra(EXTRAS_MESSAGE);
        mDevices = intent.getParcelableArrayListExtra(EXTRAS_DEVICES);

        parameterscroll = (ScrollView) findViewById(R.id.parametersscroll);
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

        getActionBar().setTitle(mDevices.get(currentDevice).getName());
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);


        passwordtxt = (TextView) findViewById(R.id.password);
        passwordconfirmtxt = (TextView) findViewById(R.id.confirm);
        usercommenttxt = (TextView) findViewById(R.id.editusercomment);

        imperialunit = (RadioGroup) findViewById(R.id.imperialunitrg);
        startoptions = (RadioGroup) findViewById(R.id.startoptionsrg);
        stopoptions = (RadioGroup) findViewById(R.id.stopsettingrg);
        celsius = (RadioButton) findViewById(R.id.celsius);
        fahrenheit = (RadioButton) findViewById(R.id.fahrenheit);
        startwithdelay = (RadioButton) findViewById(R.id.startwithdelay);
        startondatetime = (RadioButton) findViewById(R.id.startondateand);
        stopbyuser = (RadioButton) findViewById(R.id.stopbyuser);
        stopwhenfull = (RadioButton) findViewById(R.id.stopwhenfull);
        stoponsample = (RadioButton) findViewById(R.id.stoponsample);
        stopondatetime = (RadioButton) findViewById(R.id.stopondatetime);

        startwithdelaybutton = (Button) findViewById(R.id.timePickerstartdelay);
        startondatetimebutton = (Button) findViewById(R.id.timepickerstartdatetime);
        sampleperiodbutton = (Button) findViewById(R.id.timePickersampleperiod);
        stopondatebutton = (Button) findViewById(R.id.timePickerstopondatetime);

        stoponsamplebutton = (EditText) findViewById(R.id.samplenumber);
        ch1upperlimitnb = (EditText) findViewById(R.id.ch1upperlimit);
        ch1lowerlimitnb = (EditText) findViewById(R.id.ch1lowerlimit);
        ch1alarmdelaynb = (EditText) findViewById(R.id.ch1alarmdelay);

        ch2upperlimitnb = (EditText) findViewById(R.id.ch2upperlimit);
        ch2lowerlimitnb = (EditText) findViewById(R.id.ch2lowerlimit);
        ch2alarmdelaynb = (EditText) findViewById(R.id.ch2alarmdelay);

        BLEenergysave = (CheckBox) findViewById(R.id.bleenergysave);
        loopovewritecb = (CheckBox) findViewById(R.id.loopoverwrite);
        startwithbuttoncb = (CheckBox) findViewById(R.id.startwithbutton);
        stopwithbuttoncb = (CheckBox) findViewById(R.id.stopwithbutton);
        reusewithbuttoncb = (CheckBox) findViewById(R.id.reusewithbutton);
        allowplacingtagcb = (CheckBox) findViewById(R.id.allowplacingtags);
        enablelcdmenucb = (CheckBox) findViewById(R.id.enablelcdmenu);
        extendedlcdmenucb = (CheckBox) findViewById(R.id.extendedlcdmenu);
        passwordenabledcb = (CheckBox) findViewById(R.id.securewithpassword);

        ch1enabledcb = (CheckBox) findViewById(R.id.ch1enable);
        ch1limitenabledcb = (CheckBox) findViewById(R.id.ch1limitsenabled);

        ch2enabledcb = (CheckBox) findViewById(R.id.ch2enable);
        ch2limitenabledcb = (CheckBox) findViewById(R.id.ch2limitenabled);

        ch2limitenabledcb.setEnabled(false);
        ch2upperlimitnb.setEnabled(false);
        ch2lowerlimitnb.setEnabled(false);
        ch2alarmdelaynb.setEnabled(false);

        Programparam = (Button) findViewById(R.id.done);

        progressDialoge();
        uiSetupRules();
        buttonAction();
        Passwordenter();
        ScrollListener();

    }


    private void ScrollListener(){
        parameterscroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                timeoutdelay++;
            }
        });
    }

    private void Passwordenter(){
        passwordtxt.addTextChangedListener(new TextWatcher() {
               @Override
               public void beforeTextChanged (CharSequence s,int start, int count,
                                              int after){
               }
               @Override
               public void onTextChanged ( final CharSequence s, int start, int before,
                                           int count){
                   if(!passwordtxt.getText().toString().equals(passwordconfirmtxt.getText().toString())){
                       passwordconfirmtxt.setBackgroundColor(Color.RED);
                   }else
                       passwordconfirmtxt.setBackgroundColor(Color.WHITE);



               }
               @Override
               public void afterTextChanged ( final Editable s){
                   Log.d(")))))))", "AFTERRRR text change");
               }
           }

        );

        passwordconfirmtxt.addTextChangedListener(new TextWatcher() {
              @Override
              public void beforeTextChanged (CharSequence s,int start, int count,
                                             int after){
              }
              @Override
              public void onTextChanged ( final CharSequence s, int start, int before,
                                          int count){
                  if(passwordtxt.getText().toString().equals(passwordconfirmtxt.getText().toString())){
                      passwordconfirmtxt.setBackgroundColor(Color.WHITE);
                  }else{
                      passwordconfirmtxt.setBackgroundColor(Color.RED);
                  }
                  Log.d(")))))))", "ON text change");

              }
              @Override
              public void afterTextChanged ( final Editable s){
                  if(passwordtxt.getText().toString().equals(passwordconfirmtxt.getText().toString())){
                      passwordconfirmtxt.setBackgroundColor(Color.WHITE);
                  }else{
                      passwordconfirmtxt.setBackgroundColor(Color.RED);
                  }

                  Log.d(")))))))", "AFTER text change");
              }
          }

        );
    }

    private void buttonAction(){

        Programparam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(baseCMD.state == 2) {
                    programbutton();
                }else{
                    BuildDialogue("Can't Program parameters", "Parameters can't be programed when the logger is in "+QS.GetState(baseCMD.state)+" state.\nPut the logger in to ready state!",2);
                }
            }
        });

        startwithdelaybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker(v, startwithdelaybutton);
            }
        });

        sampleperiodbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker(v, sampleperiodbutton);
            }
        });

        startondatetimebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichbutton = 0;
                startondatepopup();

            }
        });

        stopondatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichbutton = 1;
                stopondatepopup();
            }
        });


    }


    public void showPicker(View v, final Button b){
        MyTimePickerDialog mTimePicker = new MyTimePickerDialog(this, new MyTimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {
                // TODO Auto-generated method stub
                b.setText(String.format("%02d", hourOfDay)+
                        ":" + String.format("%02d", minute) +
                        ":" + String.format("%02d", seconds));
            }
        }, Integer.parseInt(QS.StringDatetoInt(b.getText().toString())[0]), Integer.parseInt(QS.StringDatetoInt(b.getText().toString())[1]), Integer.parseInt(QS.StringDatetoInt(b.getText().toString())[2]), true);
        mTimePicker.show();
    }

    private void uiSetupRules(){


        startoptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){

                    case R.id.startwithdelay:
                        startondatetimebutton.setEnabled(false);
                        startwithdelaybutton.setEnabled(true);
                        stopondatebutton.setEnabled(false);
                        stopondatetime.setEnabled(false);
                        break;
                    case R.id.startondateand:
                        startwithdelaybutton.setEnabled(false);
                        startondatetimebutton.setEnabled(true);
                        stopondatebutton.setEnabled(true);
                        stopondatetime.setEnabled(true);
                        break;
                }
            }
        });

        imperialunit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){

                    case R.id.celsius:
                        celsiusfahrenheit  = false;
                        break;
                    case R.id.fahrenheit:
                        celsiusfahrenheit = true;
                        break;
                }
            }
        });



        stopoptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){

                    case R.id.stopbyuser:
                        stopondatebutton.setEnabled(false);
                        stopondatetime.setEnabled(false);
                        stoponsamplebutton.setEnabled(false);
                        break;
                    case R.id.stopwhenfull:
                        stopondatebutton.setEnabled(false);
                        stopondatetime.setEnabled(false);
                        stoponsamplebutton.setEnabled(false);
                        break;
                    case R.id.stoponsample:
                        stoponsamplebutton.setEnabled(true);
                        break;
                    case R.id.stopondatatime:
                        stoponsamplebutton.setEnabled(false);
                        break;
                }
            }
        });

        ch1limitenabledcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked){
                    ch1upperlimitnb.setEnabled(true);
                    ch1lowerlimitnb.setEnabled(true);
                    ch1alarmdelaynb.setEnabled(true);
                }else{
                    ch1upperlimitnb.setEnabled(false);
                    ch1lowerlimitnb.setEnabled(false);
                    ch1alarmdelaynb.setEnabled(false);
                }

            }
        });


        ch1limitenabledcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked){
                    ch1upperlimitnb.setEnabled(true);
                    ch1lowerlimitnb.setEnabled(true);
                    ch1alarmdelaynb.setEnabled(true);
                }else{
                    ch1upperlimitnb.setEnabled(false);
                    ch1lowerlimitnb.setEnabled(false);
                    ch1alarmdelaynb.setEnabled(false);
                }

            }
        });

        ch2limitenabledcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked){
                    ch2upperlimitnb.setEnabled(true);
                    ch2lowerlimitnb.setEnabled(true);
                    ch2alarmdelaynb.setEnabled(true);
                }else{
                    ch2upperlimitnb.setEnabled(false);
                    ch2lowerlimitnb.setEnabled(false);
                    ch2alarmdelaynb.setEnabled(false);
                }

            }
        });


        ch1enabledcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked){
                    ch1limitenabledcb.setEnabled(true);
                    ch1upperlimitnb.setEnabled(false);
                    ch1lowerlimitnb.setEnabled(false);
                    ch1alarmdelaynb.setEnabled(false);
                }else{
                    ch1limitenabledcb.setEnabled(false);
                    ch1upperlimitnb.setEnabled(false);
                    ch1lowerlimitnb.setEnabled(false);
                    ch1alarmdelaynb.setEnabled(false);
                }

            }
        });

        ch2enabledcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked){
                    ch2limitenabledcb.setEnabled(true);
                    ch2upperlimitnb.setEnabled(false);
                    ch2lowerlimitnb.setEnabled(false);
                    ch2alarmdelaynb.setEnabled(false);
                }else{
                    ch2limitenabledcb.setEnabled(false);
                    ch2upperlimitnb.setEnabled(false);
                    ch2lowerlimitnb.setEnabled(false);
                    ch2alarmdelaynb.setEnabled(false);
                }

            }
        });

        passwordenabledcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passwordtxt.setEnabled(true);
                    passwordconfirmtxt.setEnabled(true);
                } else {
                    passwordtxt.setEnabled(false);
                    passwordconfirmtxt.setEnabled(false);
                }
            }
        });

        stopondatetime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    stoponsamplebutton.setEnabled(false);
                } else {
                    stoponsamplebutton.setEnabled(true);
                }
            }
        });


    }

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
            Toast.makeText(Activity_Parameter.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String time = " "+hourOfDay+":"+minute+"";
        switch (whichbutton) {
            case 0:
                startondatetimebutton.append(time);
                break;
            case 1:
                stopondatebutton.append(time);
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        switch (whichbutton) {
            case 0:
                startondatetimebutton.setText(date);
                break;
            case 1:
                stopondatebutton.setText(date);
                break;
        }
    }

    private void promtPassword(){
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(Activity_Parameter.this);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Activity_Parameter.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                //this is where the command is sent
                                sendData(commsSerial.WriteByte(baseCMD.WritePassword()));
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void reconnect(){

        if(!mConnected) {
            Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
            if (mBluetoothLeService != null) {
                final boolean result = mBluetoothLeService.connect(BLE_Address);
                Log.d(TAG, "Connect request result===" + result);
            }

            displayGattServices(mBluetoothLeService.getSupportedGattServices());
        }
    }


    private void programbutton(){
        byte[] data;
        reconnect();
        progressDialoge2();
        boolean[] flags = {fahrenheit.isChecked(),loopovewritecb.isChecked(),enablelcdmenucb.isChecked(),allowplacingtagcb.isChecked(),startwithbuttoncb.isChecked(),stopwithbuttoncb.isChecked(),
                reusewithbuttoncb.isChecked(),false,false,BLEenergysave.isChecked(),startondatetime.isChecked(), passwordenabledcb.isChecked(),stopwhenfull.isChecked(),stoponsample.isChecked(),stopondatetime.isChecked(),extendedlcdmenucb.isChecked()};

        if(startondatetime.isChecked()) {
            data = baseCMD.WriteRTCStartDateTime(QS.getDatefromString(startondatetimebutton.getText().toString()));
            for (int i = 0; i < 6; i++) {
                UserReadtemp[i] = data[i];
            }
        }else{
            for (int i = 0; i < 6; i++) {
                UserReadtemp[i] = (byte)0xFF;
            }
        }

        data = baseCMD.Write_USERFlags(flags);
        UserReadtemp[6] = data[0];
        UserReadtemp[7] = data[1];//two flag bytes

        CHUserData chUserData = new CHUserData(ch1enabledcb.isChecked(), ch1limitenabledcb.isChecked(), Double.parseDouble(ch1upperlimitnb.getText().toString())*10, Double.parseDouble(ch1lowerlimitnb.getText().toString())*10,Integer.parseInt(ch1alarmdelaynb.getText().toString()),
                ch2enabledcb.isChecked(), ch2limitenabledcb.isChecked(), Double.parseDouble(ch2upperlimitnb.getText().toString())*10, Double.parseDouble(ch2lowerlimitnb.getText().toString())*10,Integer.parseInt(ch2alarmdelaynb.getText().toString()));

        data = baseCMD.Write_USERCH1(chUserData);
        UserReadtemp[8] = data[0]; UserReadtemp[9] = data[1]; UserReadtemp[10] = data[2]; UserReadtemp[11] = data[3];//channel 1 info
        UserReadtemp[12] = data[4]; UserReadtemp[13] = data[5]; UserReadtemp[14] = data[6]; UserReadtemp[15] = data[7];
        UserReadtemp[16] = data[8]; UserReadtemp[17] = data[9]; UserReadtemp[18] = data[10]; UserReadtemp[19] = data[11];//channel 2 info
        UserReadtemp[20] = data[12]; UserReadtemp[21] = data[13]; UserReadtemp[22] = data[14]; UserReadtemp[23] = data[15];

        data = baseCMD.Write_USERSamplePeriod(Integer.parseInt(QS.StringDatetoInt(sampleperiodbutton.getText().toString())[0])*60*60+ Integer.parseInt(QS.StringDatetoInt(sampleperiodbutton.getText().toString())[1])*60 + Integer.parseInt(QS.StringDatetoInt(sampleperiodbutton.getText().toString())[2]));
        UserReadtemp[24] = data[0]; UserReadtemp[25] = data[1];//sample period

        data = baseCMD.Write_USERStartDelay(Integer.parseInt(QS.StringDatetoInt(startwithdelaybutton.getText().toString())[0])*60*60+ Integer.parseInt(QS.StringDatetoInt(startwithdelaybutton.getText().toString())[1])*60 + Integer.parseInt(QS.StringDatetoInt(startwithdelaybutton.getText().toString())[2]));
        UserReadtemp[26] = data[0]; UserReadtemp[27] = data[1];//start delay

        Log.d("testing flags^^++^^", "COming in hereeeeeeeeeeeee22222  " +QS.getDatefromString(startondatetimebutton.getText().toString()).getTimeInMillis() + " current time " + (QS.getDatefromString(startondatetimebutton.getText().toString()).getTimeInMillis() - Calendar.getInstance().getTimeInMillis())  + " curret time / 1000 " + Calendar.getInstance().getTime().getTime()/1000 + " utc " + Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() + " calender instance " + Calendar.getInstance().getTimeInMillis());
        data = baseCMD.Write_USERStartdatetimeDelay((QS.getDatefromString(startondatetimebutton.getText().toString()).getTimeInMillis()/1000 - Calendar.getInstance().getTimeInMillis()/1000));
        UserReadtemp[28] = data[0]; UserReadtemp[29] = data[1]; UserReadtemp[30] = data[2]; UserReadtemp[31] = data[3];

        if(stoponsample.isChecked()) {
            data = baseCMD.Write_USERStoponsample(Integer.parseInt(stoponsamplebutton.getText().toString()));
            UserReadtemp[32] = data[0];
            UserReadtemp[33] = data[1];
            UserReadtemp[34] = data[2];
            UserReadtemp[35] = data[3];
        }else if(stopondatetime.isChecked()){
            if((QS.getDatefromString(startondatetimebutton.getText().toString()).compareTo(QS.getDatefromString(stopondatebutton.getText().toString()))) < 0){
                long timediff  = ((QS.getDatefromString(stopondatebutton.getText().toString()).getTimeInMillis()/1000)-(QS.getDatefromString(startondatetimebutton.getText().toString()).getTimeInMillis())/1000);
                Log.d("testing flags^^++^^", "COming in hereeeee22222 " + timediff);
                int s =  Integer.parseInt(QS.StringDatetoInt(sampleperiodbutton.getText().toString())[0])*60*60+ Integer.parseInt(QS.StringDatetoInt(sampleperiodbutton.getText().toString())[1])*60 + Integer.parseInt(QS.StringDatetoInt(sampleperiodbutton.getText().toString())[2]);
                int length = (int)timediff/s;
                Log.d("testing flags^^++^^", "COming in hereeeee22222 " + length);
                data = baseCMD.Write_USERStoponsample((int)length);
                UserReadtemp[32] = data[0];
                UserReadtemp[33] = data[1];
                UserReadtemp[34] = data[2];
                UserReadtemp[35] = data[3];
            }
        }else{
            data = baseCMD.Write_USERStoponsample(Integer.parseInt(stoponsamplebutton.getText().toString()));
            UserReadtemp[32] = data[0];
            UserReadtemp[33] = data[1];
            UserReadtemp[34] = data[2];
            UserReadtemp[35] = data[3];
        }

        if(passwordenabledcb.isChecked()) {
            Log.d("testing flags^^++^^", "COming in hereeeeeeeeeeeee22222");
            UserReadtemp[36] = (byte) 0xEE;
            UserReadtemp[37] = (byte) 0xDC;
            UserReadtemp[38] = (byte) 0xFB;
            UserReadtemp[39] = (byte) 0x31;
            UserReadtemp[40] = (byte) 0xC4;
            UserReadtemp[41] = (byte) 0x9B;
            UserReadtemp[42] = (byte) 0x19;
            UserReadtemp[43] = (byte) 0xF9;
        }

        data = baseCMD.Write_USERString(usercommenttxt.getText().toString());
        //32 bytes are ignored in the middle;
        for(int k = 78; k < 398; k++){
            UserReadtemp[k] = data[k-78];
        }
        Log.d("testing flags^******^", "Reading RTC for the first time");
        sendData(commsSerial.WriteByte(baseCMD.ReadRTC()));
        // Log.d(")))))))", "Sending RTCccccccccc");

        state = 30;
    }


    private void startondatepopup(){
        timeoutdelay = timeoutdelay + 15;
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        int hour = 0; int min = 0; int day = 0; int month = 0; int year = 0;
        if((QS.StringDatetoInt(startondatetimebutton.getText().toString()).length) == 5){
            hour = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[3]);
            min = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[4]);
            year = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[2]);
            month = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[1])-1;
            day = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[0]);
        }else if((QS.StringDatetoInt(startondatetimebutton.getText().toString()).length) == 0){
            hour = now.getMaximum(Calendar.HOUR);
            min = now.getMaximum(Calendar.MINUTE);
            year = now.getMaximum(Calendar.YEAR);
            month = now.getMaximum(Calendar.MONTH);
            day = now.getMaximum(Calendar.DATE);
        }else if((QS.StringDatetoInt(startondatetimebutton.getText().toString()).length) == 1){
            hour = now.getMaximum(Calendar.HOUR);
            min = now.getMaximum(Calendar.MINUTE);
            year = now.getMaximum(Calendar.YEAR);
            month = now.getMaximum(Calendar.MONTH);
            day = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[0]);
        }else if((QS.StringDatetoInt(startondatetimebutton.getText().toString()).length) == 2){
            hour = now.getMaximum(Calendar.HOUR);
            min = now.getMaximum(Calendar.MINUTE);
            year = now.getMaximum(Calendar.YEAR);
            month = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[1])-1;
            day = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[0]);
        }else if((QS.StringDatetoInt(startondatetimebutton.getText().toString()).length) == 3){
            hour = now.getMaximum(Calendar.HOUR);
            min = now.getMaximum(Calendar.MINUTE);
            year = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[2]);
            month = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[1])-1;
            day = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[0]);
        }else if((QS.StringDatetoInt(startondatetimebutton.getText().toString()).length) == 4){
            hour = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[3]);
            min = now.getMaximum(Calendar.MINUTE);
            year = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[2]);
            month = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[1])-1;
            day = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[0]);
        }



        TimePickerDialog tpd = TimePickerDialog.newInstance(
                Activity_Parameter.this,
                hour,
                min,
                true
        );
        tpd.show(getFragmentManager(), "Datepickerdialog");
        tpd.setVersion(TimePickerDialog.Version.VERSION_2);
        tpd.setAccentColor(getResources().getColor(R.color.ublox_blue_color));

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                Activity_Parameter.this,
                year,
                month,
                day
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.setAccentColor(getResources().getColor(R.color.ublox_blue_color));


    }

    private void stopondatepopup(){
        timeoutdelay = timeoutdelay + 15;
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        int hour = 0; int min = 0; int day = 0; int month = 0; int year = 0;
        if((QS.StringDatetoInt(stopondatebutton.getText().toString()).length) == 5){
            hour = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[3]);
            min = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[4]);
            year = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[2]);
            month = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[1])-1;
            day = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[0]);
        }else if((QS.StringDatetoInt(stopondatebutton.getText().toString()).length) == 0){
            hour = now.getMaximum(Calendar.HOUR);
            min = now.getMaximum(Calendar.MINUTE);
            year = now.getMaximum(Calendar.YEAR);
            month = now.getMaximum(Calendar.MONTH);
            day = now.getMaximum(Calendar.DATE);
        }else if((QS.StringDatetoInt(stopondatebutton.getText().toString()).length) == 1){
            hour = now.getMaximum(Calendar.HOUR);
            min = now.getMaximum(Calendar.MINUTE);
            year = now.getMaximum(Calendar.YEAR);
            month = now.getMaximum(Calendar.MONTH);
            day = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[0]);
        }else if((QS.StringDatetoInt(stopondatebutton.getText().toString()).length) == 2){
            hour = now.getMaximum(Calendar.HOUR);
            min = now.getMaximum(Calendar.MINUTE);
            year = now.getMaximum(Calendar.YEAR);
            month = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[1])-1;
            day = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[0]);
        }else if((QS.StringDatetoInt(stopondatebutton.getText().toString()).length) == 3){
            hour = now.getMaximum(Calendar.HOUR);
            min = now.getMaximum(Calendar.MINUTE);
            year = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[2]);
            month = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[1])-1;
            day = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[0]);
        }else if((QS.StringDatetoInt(stopondatebutton.getText().toString()).length) == 4){
            hour = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[3]);
            min = now.getMaximum(Calendar.MINUTE);
            year = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[2]);
            month = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[1])-1;
            day = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[0]);
        }



        TimePickerDialog tpd = TimePickerDialog.newInstance(
                Activity_Parameter.this,
                hour,
                min,
                true
        );
        tpd.show(getFragmentManager(), "Datepickerdialog");
        tpd.setVersion(TimePickerDialog.Version.VERSION_2);
        tpd.setAccentColor(getResources().getColor(R.color.ublox_blue_color));

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                Activity_Parameter.this,
                year,
                month,
                day
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.setAccentColor(getResources().getColor(R.color.ublox_blue_color));


    }

}
