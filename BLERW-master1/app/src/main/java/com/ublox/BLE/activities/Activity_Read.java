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
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.ublox.BLE.R;
import com.ublox.BLE.comms.BaseCMD;
import com.ublox.BLE.comms.CommsSerial;
import com.ublox.BLE.comms.MT2Msg_Read;
import com.ublox.BLE.comms.MT2Values;
import com.ublox.BLE.comms.QueryStrings;
import com.ublox.BLE.services.BluetoothLeService;
import com.ublox.BLE.services.StoreKeyService;
import com.ublox.BLE.utils.CommsChar;
import com.ublox.BLE.utils.GattAttributes;
import com.ublox.BLE.utils.HexData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
public class Activity_Read extends Activity {
    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    public static final String EXTRAS_MESSAGE = "0";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String EXTRAS_DEVICES = "Device";

    private static final int PROGRESSBAR_MAX = 300;


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

    private TextView TripSamples;
    private TextView TotalLoggedTime;
    private TextView LoggedSamples;
    private TextView LoggedTags;
    private TextView FirstSample;
    private TextView FirstLoggedSample;
    private TextView LastSample;
    private TextView LastLoggedSample;

    private TextView channel1;
    private TextView upperLimit1;
    private TextView lowerLimit1;
    private TextView mean1;
    private TextView mkt;
    private TextView max1;
    private TextView min1;

    private TextView totalSampleswithinLimits1;
    private TextView totalTimewithinLimits1;
    private TextView totalPercentagewitinLimits1;
    private TextView totalSaamplesoutofLimits1;
    private TextView totalTimeoutofLImits1;
    private TextView totalPercentageoutofLimits1;
    private TextView samplesaboveUpperLimit1;
    private TextView timeaboveUpperLimit1;
    private TextView percentageaboveUpperLimit1;
    private TextView samplesbelowLowerLimit1;
    private TextView timebelowLowerSamples1;
    private TextView percentagebelowLowerSample1;

    private TextView channel2;
    private TextView upperLimit2;
    private TextView lowerLimit2;
    private TextView mean2;
    private TextView max2;
    private TextView min2;

    private TextView totalSampleswithinLimits2;
    private TextView totalTimewithinLimits2;
    private TextView totalPercentagewitinLimits2;
    private TextView totalSaamplesoutofLimits2;
    private TextView totalTimeoutofLImits2;
    private TextView totalPercentageoutofLimits2;
    private TextView samplesaboveUpperLimit2;
    private TextView timeaboveUpperLimit2;
    private TextView percentageaboveUpperLimit2;
    private TextView samplesbelowLowerLimit2;
    private TextView timebelowLowerSamples2;
    private TextView percentagebelowLowerSample2;

    private ImageButton zoomin;
    private ImageButton zoomout;
    private ImageButton zoomin1;
    private ImageButton zoomout1;

    private LinearLayout Humidity;
    private LinearLayout Humidity2;

    private ProgressDialog progress;
    private int progresspercentage;
    private int progressincrement;
    StoreKeyService storeKeyService;

    private byte[] TWFlash = new byte[144];
    private byte[] RamRead = new byte[100];
    private byte[] UserRead = new byte[512];
    private byte[] ExtraRead = new byte[284];
    byte[] ReadValues1;
    byte[] ReadValues2;
    private int firstRead = 0;
    private String mDeviceName;
    private String mDeviceAddress;
    private String message;
    private int firsttime = 0;
    private boolean backpress = false;
    private boolean frommenu = false;

    private boolean soundon = true;

    private int state = 1;
    private ArrayList<String> Q_data = new ArrayList<String>();
    private ArrayList<String> U_data = new ArrayList<String>();
    private ArrayList<String> F_data = new ArrayList<String>();
    private ArrayList<String> R_data = new ArrayList<String>();

    private String BLE_Address = "";



    HexData hexData = new HexData();
    BaseCMD baseCMD =  new BaseCMD();
    CommsSerial commsSerial = new CommsSerial();
    MT2Values.MT2Mem_values mt2Mem_values = new MT2Values.MT2Mem_values();
    MT2Msg_Read mt2Msg_read;
    QueryStrings QS = new QueryStrings();
    CommsChar commsChar = new CommsChar();
    private List<BluetoothDevice> mDevices = new ArrayList<>();
    private int currentDevice = 0;

    LineChart chart;
    LineChart chart1;



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
            // Automatically connects to the device upon successful start-up initialization.

            //for(int i = 0; i < 5; i++) {
            mBluetoothLeService.connect(mDevices.get(currentDevice).getAddress());
            BLE_Address = mDevices.get(currentDevice).getAddress();
            Log.d("______________", "mBluetoothLeService.connectttttt");
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

                SystemClock.sleep(10);
                sendData(HexData.STAY_UP);

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //mBluetoothLeService.readCharacteristic(characteristicRX);

                returndata = null;
                returndata = intent.getByteArrayExtra(mBluetoothLeService.EXTRA_DATA);
                hexData.BytetoHex(returndata);
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
        if(mBluetoothLeService.isRestricted())
            unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_simple, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_menu:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.menu_about:
                //sendEmail();
                return true;
            case R.id.menu_refresh:
                frommenu = true;

                Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
                bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

                registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
                if (mBluetoothLeService != null) {
                    final boolean result = mBluetoothLeService.connect(BLE_Address);
                    Log.d(TAG, "Connect request result===" + result);
                }

                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                state = 1;
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
                if (mConnected && test != null  && characteristicTX != null && characteristicRX != null && mBluetoothLeService != null) {
                    //characteristicTX.setValue(new byte[] {test[i]});
                    mBluetoothLeService.writeCharacteristic(characteristicTX, test);
                    mBluetoothLeService.setCharacteristicNotification(characteristicRX, true);
                }
            }
        };

        thread.start();

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

                progresspercentage++;
                switch (state) {
                    case 0:

//                        hexData.BytetoHex(in);
//                        query = baseCMD.ReadByte(in);
//                        Q_data = baseCMD.CMDQuery(query);
//                        SetUI(Q_data);
                        if(message.equals("1")){
                            sendData(HexData.GO_TO_SLEEP);
                            state = 6;
                            //mBluetoothLeService.disconnect();
                        }
                        break;
                    case 1:
                        if(frommenu){
                            progressDialoge();
                            progresspercentage = 0;
                        }
                        //enters here first
                        sendData(HexData.QUARY);
                        state = 8;
                        firsttime = 0;
                        firstRead = 0;
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
                            TimeUnit.MILLISECONDS.sleep(400);
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
                            state = 24;
                            System.arraycopy(mt2Msg_read.memoryData, 0, UserRead, 500, 12);
                            hexData.BytetoHex(UserRead);
                            U_data = baseCMD.CMDUserRead(UserRead);
                           /// SetUI();
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
                            state = 24;
                            System.arraycopy(mt2Msg_read.memoryData, 0, ExtraRead, 236, 48);
                            firsttime++;
                            hexData.BytetoHex(ExtraRead);

                        }
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;
                    case 24:// the set up needs to change if loop over right is active.

                        if(baseCMD.numberofsamples == 0) {
                            SetUI();
                            progresspercentage = 100;
                            progress.cancel();
                            sendData(HexData.BLE_ACK);
                            state = 7;
                        }else {
                            mt2Msg_read = new MT2Msg_Read(commsChar.MEM_VAL, 0, baseCMD.SamplePointer *2, 120, 3);
                            sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));
                            if(baseCMD.LoopOverwrite && !baseCMD.isLoopOverwriteOverFlow){state = 26;}else{state = 25;}
                        }
                        break;
                    case 25://
                        if (mt2Msg_read.write_into_readByte(baseCMD.ReadByte(in))) {
                            state = 7;
                            byte[] ValueRead = new byte[mt2Msg_read.memoryData.length];
                            System.arraycopy(mt2Msg_read.memoryData, 0, ValueRead, 0, mt2Msg_read.memoryData.length);
                            //hexData.BytetoHex(ValueRead);
                            MT2ValueIn(ValueRead);
                            addDatatoGraph();
                            SetUI();
                            progresspercentage = 100;
                            progress.cancel();
                        }
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;
                    case 26:
                        if (mt2Msg_read.write_into_readByte(baseCMD.ReadByte(in))) {
                            state = 27;
                            ReadValues1 = new byte[mt2Msg_read.memoryData.length];
                            System.arraycopy(mt2Msg_read.memoryData, 0, ReadValues1, 0, mt2Msg_read.memoryData.length);
                            //hexData.BytetoHex(ValueRead);
                        }
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        firstRead = mt2Msg_read.memoryData.length;
                        break;
                    case 27:
                        int bytesToRead;
                        int address;
                        int totalToRead = baseCMD.MemorySizeMax*2;
                        int bytePointer = baseCMD.SamplePointer *2;
                        int pageOffset = bytePointer % QS.PAGESIZE;
                        address = (int)((bytePointer / QS.PAGESIZE) + 1) * QS.PAGESIZE;
                        address += pageOffset;
                        bytesToRead = totalToRead - address;
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_VAL, address, bytesToRead, 120, 3);
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));
                        state = 28;
                        break;
                    case 28:
                        if (mt2Msg_read.write_into_readByte(baseCMD.ReadByte(in))) {
                            state = 7;
                            ReadValues2 = new byte[mt2Msg_read.memoryData.length];
                            byte[] combo = new byte[ReadValues2.length  + ReadValues1.length];
                            System.arraycopy(mt2Msg_read.memoryData, 0, ReadValues2, 0, mt2Msg_read.memoryData.length);
                            System.arraycopy(ReadValues2,0, combo,0,ReadValues2.length-1);
                            System.arraycopy(ReadValues1,0,combo,ReadValues2.length,ReadValues1.length);
                            //hexData.BytetoHex(ReadValues);
                            MT2ValueIn(combo);
                            addDatatoGraph();
                            SetUI();
                            progresspercentage = 100;
                            progress.cancel();
                        }
                        sendData(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;
                    case 29:
                        break;


                }
            }
        };handler1.postDelayed(runnableCode,1);
    }


    private void addDatatoGraph(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<Entry> entries = new ArrayList<Entry>(mt2Mem_values.Data.size());
                Calendar c = new GregorianCalendar(2000, 1, 1, 12, 0, 0);


                for (int i = 0; i < mt2Mem_values.Data.size(); i++) {

                    float x = (mt2Mem_values.Data.get(i).valTime.getTime()) ;
                    entries.add(i, new Entry(x, (mt2Mem_values.Data.get(i).valCh0 / 10f)));
                    //Log.d("___________", mt2Mem_values.Data.get(i).valTime.getTime() + "  " + (mt2Mem_values.Data.get(i).valCh0 / 10f) + " " + x);
                }

                LineDataSet dataSet = new LineDataSet(entries, "Temperature °C"); // add entries to dataset
                dataSet.setCircleColor(Color.argb(150, 10, 109, 255));
                dataSet.setDrawValues(false);//disable y axis value appearing near the line
                dataSet.setLineWidth(3f);
                dataSet.setColor(Color.argb(150, 10, 12, 247));
                dataSet.setCircleRadius(2f);
                dataSet.setHighLightColor(Color.BLACK);
                LineData lineData = new LineData(dataSet);
                chart.setData(lineData);
                chart.setDragEnabled(true);
                chart.setScaleEnabled(true);
                //chart.setVisibleXRangeMaximum(4000);
                YAxis yAxis = chart.getAxisLeft();
                yAxis.setLabelCount(10, true);


                LimitLine upperLimit = new LimitLine(baseCMD.ch1Hi / 10, "Upper Limit");
                upperLimit.setLineWidth(2f);
                upperLimit.enableDashedLine(10f, 10f, 0f);
                upperLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);

                LimitLine lowerLimit = new LimitLine(baseCMD.ch1Lo / 10, "Lower Limit");
                lowerLimit.setLineWidth(2f);
                lowerLimit.enableDashedLine(10f, 10f, 0f);
                lowerLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
                XAxis xAxis = chart.getXAxis();
                xAxis.setValueFormatter(new IAxisValueFormatter() {

                    private SimpleDateFormat mFormat = new SimpleDateFormat("MMM dd HH:mm:ss");

                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {

                        long millis = TimeUnit.MILLISECONDS.toMillis((long) value);
                        return mFormat.format(new Date(millis));
                    }
                });
                xAxis.setLabelCount(3);
                YAxis leftAxis = chart.getAxisLeft();
                leftAxis.removeAllLimitLines();
                leftAxis.addLimitLine(upperLimit);
                leftAxis.addLimitLine(lowerLimit);
                leftAxis.setAxisMaximum(120f);
                leftAxis.setAxisMinimum(-90f);
                leftAxis.enableGridDashedLine(10f, 10f, 0);
                leftAxis.setDrawLimitLinesBehindData(true);
                chart.getAxisRight().setEnabled(false);
                chart.invalidate();
                chart1.setVisibility(View.INVISIBLE);

                chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e, Highlight h) {
                        Toast.makeText(Activity_Read.this,"Date/Time\t\t:" + chart.getXAxis().getValueFormatter().getFormattedValue(e.getX(), chart.getXAxis()) + "\nTemperature :\t" + Integer.parseInt(chart.getAxisLeft().getValueFormatter().getFormattedValue(e.getY()*10, chart.getAxisLeft()))/10.0 + " °C", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected() {

                    }
                });

                zoomin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chart.zoomIn();
                    }
                });
                zoomout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chart.fitScreen();
//                        chart.moveViewToX(mt2Mem_values.Data.get(0).valCh0 / 10f);
                    }
                });
            }
        });

    }

    private void addDatatoGraph2(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<Entry> entries = new ArrayList<Entry>(mt2Mem_values.Data.size());
                Calendar c = new GregorianCalendar(2000, 1, 1, 12, 0, 0);


                for (int i = 0; i < mt2Mem_values.Data.size(); i++) {

                    float x = (mt2Mem_values.Data.get(i).valTime.getTime()) - c.getTime().getTime();
                    entries.add(i, new Entry(x, (mt2Mem_values.Data.get(i).valCh1 / 10f)));
                    //Log.d("___________", mt2Mem_values.Data.get(i).valTime.getTime() + "  " + (mt2Mem_values.Data.get(i).valCh0 / 10f) + " " + x);
                }

                LineDataSet dataSet = new LineDataSet(entries, "Humidity %RH"); // add entries to dataset
                dataSet.setCircleColor(Color.argb(150, 10, 109, 255));
                dataSet.setLineWidth(3f);
                dataSet.setDrawValues(false);//disable y axis value appearing near the line
                dataSet.setColor(Color.argb(150, 10, 12, 247));
                dataSet.setCircleRadius(2f);
                dataSet.setHighLightColor(Color.BLACK);
                LineData lineData = new LineData(dataSet);
                chart1.setData(lineData);
                chart1.setDragEnabled(true);
                chart1.setScaleEnabled(true);


                LimitLine upperLimit = new LimitLine(baseCMD.ch2Hi / 10, "Upper Limit");
                upperLimit.setLineWidth(2f);
                upperLimit.enableDashedLine(10f, 10f, 0f);
                upperLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);

                LimitLine lowerLimit = new LimitLine(baseCMD.ch2Lo / 10, "Lower Limit");
                lowerLimit.setLineWidth(2f);
                lowerLimit.enableDashedLine(10f, 10f, 0f);
                lowerLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
                XAxis xAxis = chart1.getXAxis();
                xAxis.setValueFormatter(new IAxisValueFormatter() {

                    private SimpleDateFormat mFormat = new SimpleDateFormat("MMM dd HH:mm:ss");

                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {

                        long millis = TimeUnit.MILLISECONDS.toMillis((long) value);
                        return mFormat.format(new Date(millis));
                    }
                });
                xAxis.setLabelCount(3);
                YAxis leftAxis = chart1.getAxisLeft();
                leftAxis.removeAllLimitLines();
                leftAxis.addLimitLine(upperLimit);
                leftAxis.addLimitLine(lowerLimit);
                leftAxis.setAxisMaximum(120f);
                leftAxis.setAxisMinimum(-90f);
                leftAxis.enableGridDashedLine(10f, 10f, 0);
                leftAxis.setDrawLimitLinesBehindData(true);
                chart1.getAxisRight().setEnabled(false);

                chart1.invalidate();

                chart1.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e, Highlight h) {
                        //Log.d("____", "VALUE IS SELECTED " +  + " " + );
                        //Toast.makeText(Activity_Read.this, +"Temperature :\t" + Integer.parseInt(chart.getAxisLeft().getValueFormatter().getFormattedValue(e.getY()*10, chart.getAxisLeft()))/10.0)) + " " ,Toast.LENGTH_SHORT)).show();
                        Toast.makeText(Activity_Read.this,"Date/Time :\t\t" + chart1.getXAxis().getValueFormatter().getFormattedValue(e.getX(), chart1.getXAxis()) + "\nHumidity :\t" + Integer.parseInt(chart1.getAxisLeft().getValueFormatter().getFormattedValue(e.getY()*10, chart1.getAxisLeft()))/10.0 + " %RH", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected() {

                    }
                });

                zoomin1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chart1.zoomIn();
                    }
                });
                zoomout1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chart1.fitScreen();
                    }
                });
                //chart1.setVisibility(View.INVISIBLE);
            }
        });

    }


    private void MT2ValueIn(byte[] value){
        ArrayList<Byte> data = new ArrayList<Byte>();
        for(int  i = 0; i < value.length; i++) data.add(value[i]);
        //calculating first loged sample
        Calendar calendar = baseCMD.startDateTime;
        calendar.add(Calendar.SECOND, baseCMD.startDelay);
        try {
            mt2Mem_values = new MT2Values.MT2Mem_values(data, calendar, baseCMD.ch1Hi/10, baseCMD.ch1Lo/10, baseCMD.ch2Hi/10, baseCMD.ch2Lo/10, baseCMD.samplePeriod, baseCMD.ch1Enable, baseCMD.ch2Enable );
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private int BytestoRead(){
        int bytestoRead = (int) Integer.parseInt(R_data.get(18))*2;

        int maxSize = (int) (Integer.parseInt(F_data.get(1)))*2;
        if (bytestoRead >= maxSize) {
            bytestoRead = maxSize;
        }

        return bytestoRead;
    }

    private void BuildDialogue(String str1, String str2, final int press){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(Activity_Read.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(Activity_Read.this);
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
        progress.setMessage("Reading Logger");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(false);
        progress.setProgress(0);
        progress.setCancelable(false);
        progress.setButton(DialogInterface.BUTTON_NEGATIVE, "Abort", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendData(hexData.BLE_ACK);
                state = 7;
                BuildDialogue("Read Aborted", "Entries might be empty!\nGo back to menu and reconnect",1);

                dialog.dismiss();
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void SetUI(){
        serialno.setText(Q_data.get(0));
        //firmware.setText(Q_data.get(1));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        time.setText(currentDateandTime);

        if(Double.parseDouble(Q_data.get(6)) > 66){
            bat.setBackgroundResource(R.drawable.bfull);
        }else if(Double.parseDouble(Q_data.get(6)) > 33){
            bat.setBackgroundResource(R.drawable.bhalf);
        }else if(Double.parseDouble(Q_data.get(6)) > 0){
            bat.setBackgroundResource(R.drawable.blow);

        }

        //model.setText(QS.GetGeneration(Integer.parseInt(Q_data.get(2))));
        //family.setText(QS.GetType(Integer.parseInt(Q_data.get(3))));

        Lstate.setText( QS.GetState(Integer.parseInt(Q_data.get(5))));
        battery.setText(  R_data.get(17)+"%");
        currentTemp.setText(R_data.get(9) + " °C");
        currenthumidity.setText(R_data.get(11) + " %");

        memory.setText(F_data.get(1));
        if(baseCMD.numberofsamples != 0) {
            //TripSamples.setText(R_data.get(7));
            TotalLoggedTime.setText(QS.Period((int) mt2Mem_values.LoggedTripDuration * 1000));
            LoggedSamples.setText(mt2Mem_values.Data.size() + "");
            LoggedTags.setText(mt2Mem_values.TagCount() + "");
            FirstSample.setText(sdf.format(mt2Mem_values.Data.get(0).valTime));
            FirstLoggedSample.setText(sdf.format(mt2Mem_values.Data.get(0).valTime));
            LastSample.setText(sdf.format(mt2Mem_values.Data.get(mt2Mem_values.Data.size() - 1).valTime));
            LastLoggedSample.setText(sdf.format(mt2Mem_values.Data.get(mt2Mem_values.Data.size() - 1).valTime));
        }


        if(baseCMD.ch1Enable && baseCMD.numberofsamples != 0) {
            channel1.setText("Temperature");
            upperLimit1.setText(baseCMD.ch1Hi / 10.0 + " °C");
            lowerLimit1.setText(baseCMD.ch1Lo / 10.0 + " °C");
            mkt.setText(String.format("%.1f", mt2Mem_values.ch0Stats.MKTValue) + " °C");
            mean1.setText(String.format("%.1f", mt2Mem_values.ch0Stats.Mean) + " °C");
            String max1time = sdf.format(mt2Mem_values.Data.get(mt2Mem_values.ch0Stats.Max.Number).valTime);
            max1.setText(mt2Mem_values.ch0Stats.Max.Value / 10.0 + " °C\n" + max1time);
            String min1time = sdf.format(mt2Mem_values.Data.get(mt2Mem_values.ch0Stats.Min.Number).valTime);
            min1.setText(mt2Mem_values.ch0Stats.Min.Value / 10.0 + " °C\n" + min1time);

            totalSampleswithinLimits1.setText(mt2Mem_values.ch0Stats.TotalLimitWithin + "");
            totalTimewithinLimits1.setText(mt2Mem_values.ch0Stats.TotalTimeWithin + "");
            totalPercentagewitinLimits1.setText(mt2Mem_values.ch0Stats.TotalPercentWithin + "");

            totalSaamplesoutofLimits1.setText(mt2Mem_values.ch0Stats.TotalLimit + "");
            totalTimeoutofLImits1.setText(mt2Mem_values.ch0Stats.TotalTime);
            totalPercentageoutofLimits1.setText(mt2Mem_values.ch0Stats.TotalPercent + "");

            samplesaboveUpperLimit1.setText(mt2Mem_values.ch0Stats.AboveLimit + "");
            timeaboveUpperLimit1.setText(mt2Mem_values.ch0Stats.AboveTime + "");
            percentageaboveUpperLimit1.setText(mt2Mem_values.ch0Stats.AbovePercent + "");

            samplesbelowLowerLimit1.setText(mt2Mem_values.ch0Stats.BelowLimit + "");
            timebelowLowerSamples1.setText(mt2Mem_values.ch0Stats.BelowTime);
            percentagebelowLowerSample1.setText(mt2Mem_values.ch0Stats.BelowPercent + "");
        }

        if(baseCMD.ch2Enable && baseCMD.numberofsamples != 0) {
            channel2.setText("Humidity");
            upperLimit2.setText(baseCMD.ch2Hi / 10.0 + "");
            lowerLimit2.setText(baseCMD.ch2Lo / 10.0 + "");

            mean2.setText(String.format("%.1f", mt2Mem_values.ch1Stats.Mean) + "");
            String max2time = sdf.format(mt2Mem_values.Data.get(mt2Mem_values.ch1Stats.Max.Number).valTime);
            max2.setText(mt2Mem_values.ch1Stats.Max.Value / 10.0 + "\n" + max2time);
            String min2time = sdf.format(mt2Mem_values.Data.get(mt2Mem_values.ch1Stats.Min.Number).valTime);
            min2.setText(mt2Mem_values.ch1Stats.Min.Value / 10.0 + "\n" + min2time);

            totalSampleswithinLimits2.setText(mt2Mem_values.ch1Stats.TotalLimitWithin + "");
            totalTimewithinLimits2.setText(mt2Mem_values.ch1Stats.TotalTimeWithin + "");
            totalPercentagewitinLimits2.setText(mt2Mem_values.ch1Stats.TotalPercentWithin + "");

            totalSaamplesoutofLimits2.setText(mt2Mem_values.ch1Stats.TotalLimit + "");
            totalTimeoutofLImits2.setText(mt2Mem_values.ch1Stats.TotalTime);
            totalPercentageoutofLimits2.setText(mt2Mem_values.ch1Stats.TotalPercent + "");

            samplesaboveUpperLimit2.setText(mt2Mem_values.ch1Stats.AboveLimit + "");
            timeaboveUpperLimit2.setText(mt2Mem_values.ch1Stats.AboveTime + "");
            percentageaboveUpperLimit2.setText(mt2Mem_values.ch1Stats.AbovePercent + "");

            samplesbelowLowerLimit2.setText(mt2Mem_values.ch1Stats.BelowLimit + "");
            timebelowLowerSamples2.setText(mt2Mem_values.ch1Stats.BelowTime);
            percentagebelowLowerSample2.setText(mt2Mem_values.ch1Stats.BelowPercent + "");
        }

        if((R_data.get(0)).equals("Yes")){
            //set the starttimedate here;
        }

        channel1 = (TextView) findViewById(R.id.channel1);


        if(!baseCMD.ch2Enable){
            Humidity.setVisibility(LinearLayout.GONE);
            Humidity2.setVisibility(LinearLayout.GONE);
        }

        if(!baseCMD.ch2Enable){
            currenthumidity.setText("--");
        }


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__read);
        getActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
        getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_readc));
        AssetManager am = this.getAssets();


        Typeface font = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        Typeface font1 = Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf");


        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        message = intent.getStringExtra(EXTRAS_MESSAGE);
        mDevices = intent.getParcelableArrayListExtra(EXTRAS_DEVICES);


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

        TripSamples = (TextView) findViewById(R.id.tripsamples);
        TotalLoggedTime = (TextView) findViewById(R.id.totalloggedtime);
        LoggedSamples = (TextView) findViewById(R.id.loggedsamples);
        LoggedTags = (TextView) findViewById(R.id.loggedtags);
        FirstSample = (TextView) findViewById(R.id.firstsample);
        FirstLoggedSample = (TextView) findViewById(R.id.firstloggedsample);
        LastSample = (TextView) findViewById(R.id.lastsample);
        LastLoggedSample = (TextView) findViewById(R.id.lastloggedsample);

        Humidity = (LinearLayout) findViewById(R.id.humidity);
        Humidity2 = (LinearLayout) findViewById(R.id.humidity2);


        channel1 = (TextView) findViewById(R.id.channel1);
        upperLimit1 = (TextView) findViewById(R.id.upperlimit1);
        lowerLimit1 = (TextView) findViewById(R.id.lowerlimit1);
        mean1 = (TextView) findViewById(R.id.mean1);
        max1 = (TextView) findViewById(R.id.max1);
        min1 = (TextView) findViewById(R.id.min1);
        mkt = (TextView) findViewById(R.id.mkt1);

        totalSampleswithinLimits1 = (TextView) findViewById(R.id.totalwithinlimits);
        totalTimewithinLimits1 = (TextView) findViewById(R.id.totaltimewithinlimits);
        totalPercentagewitinLimits1 = (TextView) findViewById(R.id.totslpercentagewithinlimits);
        totalSaamplesoutofLimits1 = (TextView) findViewById(R.id.totalsamplesoutoflimits);
        totalTimeoutofLImits1 = (TextView) findViewById(R.id.totaltimeoutoflimits);
        totalPercentageoutofLimits1 = (TextView) findViewById(R.id.totalpercentageoutoflimits);
        samplesaboveUpperLimit1 = (TextView) findViewById(R.id.samplesaboveupperlimit);
        timeaboveUpperLimit1 = (TextView) findViewById(R.id.timeaboveupperlimit);
        percentageaboveUpperLimit1 = (TextView) findViewById(R.id.percentageaboveupperlimit);
        samplesbelowLowerLimit1 = (TextView) findViewById(R.id.samplesbelowlowerlimit);
        timebelowLowerSamples1 = (TextView) findViewById(R.id.timebelowlowerlimit);
        percentagebelowLowerSample1 = (TextView) findViewById(R.id.percentagebelowlowerlimit);


        channel2 = (TextView) findViewById(R.id.channel2);
        upperLimit2 = (TextView) findViewById(R.id.upperlimit2);
        lowerLimit2 = (TextView) findViewById(R.id.lowerlimit2);
        mean2 = (TextView) findViewById(R.id.mean2);
        max2 = (TextView) findViewById(R.id.max2);
        min2 = (TextView) findViewById(R.id.min2);


        totalSampleswithinLimits2 = (TextView) findViewById(R.id.totalwithinlimits2);
        totalTimewithinLimits2 = (TextView) findViewById(R.id.totaltimewithinlimits2);
        totalPercentagewitinLimits2 = (TextView) findViewById(R.id.totslpercentagewithinlimits2);
        totalSaamplesoutofLimits2 = (TextView) findViewById(R.id.totalsamplesoutoflimits2);
        totalTimeoutofLImits2 = (TextView) findViewById(R.id.totaltimeoutoflimits2);
        totalPercentageoutofLimits2 = (TextView) findViewById(R.id.totalpercentageoutoflimits2);
        samplesaboveUpperLimit2 = (TextView) findViewById(R.id.samplesaboveupperlimit2);
        timeaboveUpperLimit2 = (TextView) findViewById(R.id.timeaboveupperlimit2);
        percentageaboveUpperLimit2 = (TextView) findViewById(R.id.percentageaboveupperlimit2);
        samplesbelowLowerLimit2 = (TextView) findViewById(R.id.samplesbelowlowerlimit2);
        timebelowLowerSamples2 = (TextView) findViewById(R.id.timebelowlowerlimit2);
        percentagebelowLowerSample2 = (TextView) findViewById(R.id.percentagebelowlowerlimit2);

        zoomin = (ImageButton) findViewById(R.id.zoominButton);
        zoomout = (ImageButton) findViewById(R.id.zoomoutButton);
        zoomin1 = (ImageButton) findViewById(R.id.zoominButton1);
        zoomout1 = (ImageButton) findViewById(R.id.zoomoutButton1);

        getActionBar().setTitle(mDevices.get(currentDevice).getName());
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        chart = (LineChart) findViewById(R.id.chart);
        chart1 = (LineChart) findViewById(R.id.chart1);

        progressDialoge();

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
            Toast.makeText(Activity_Read.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
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
