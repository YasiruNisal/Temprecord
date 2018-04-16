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
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ublox.BLE.R;
import com.ublox.BLE.services.BluetoothLeService;
import com.ublox.BLE.utils.GattAttributes;
import com.ublox.BLE.utils.HexData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class DeviceControlActivity extends Activity {
    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    public static final String EXTRAS_MESSAGE = "0";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";



    public static final String EXTRAS_DEVICES = "Device";

    private List<BluetoothDevice> mDevices = new ArrayList<>();
    private int currentDevice = 0;


    private TextView mConnectionState;
    private TextView resultview;
    private String mDeviceName;
    private String mDeviceAddress;
    private EditText sendData;
    private Button send;

  //  private ExpandableListView mGattServicesList;
    private BluetoothLeService mBluetoothLeService;
     private boolean mConnected = false;
    private BluetoothGattCharacteristic characteristicTX;
    private BluetoothGattCharacteristic characteristicRX;
    private static byte[] test;

    public final static UUID UUID_CHARACTERISTIC_FIFO =
            UUID.fromString(GattAttributes.UUID_CHARACTERISTIC_FIFO);

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
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                //mBluetoothLeService.setCharacteristicNotification(characteristicRX,true);
                //mBluetoothLeService.setCharacteristicNotification(characteristicTX,true);
                SystemClock.sleep(15);
                test = new byte[] {0x00, 0x55, 0x22, 0x02,(byte) 0xC9, 0x5D, 0x0D};
                sendData(HexData.STAY_UP);
                SystemClock.sleep(15);
                //sendData(HexData.GO_TO_SLEEP);

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                HexData hexData = new HexData();
                hexData.BytetoHex(intent.getByteArrayExtra(mBluetoothLeService.EXTRA_DATA));
                //displayData2(intent.getByteExtra(mBluetoothLeService.EXTRA_BYTE, (byte) 0 ));
                // this is where we get the data so it needs to be checked and set the start
                // measure button enabled and send data button disabled

            }
        }
    };

    private void clearUI() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);
        getActionBar().setBackgroundDrawable(new ColorDrawable(0xff202456));
        final Intent intent = getIntent();
        mDevices = intent.getParcelableArrayListExtra(EXTRAS_DEVICES);



        // Sets up UI references.
        //((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
         mConnectionState = (TextView) findViewById(R.id.connection_state);

        send = (Button) findViewById(R.id.send);
        test = new byte[] {0x00, 0x55, 0x22, 0x02,(byte) 0xC9, 0x5D, 0x0D};
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData(HexData.GO_TO_SLEEP);
            }
        });

        resultview = (TextView) findViewById(R.id.result);
     
        getActionBar().setTitle(mDeviceName);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDevices.get(currentDevice).getAddress());
            Log.d(TAG, "Connect request result=" + result);
        }
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mBluetoothLeService.disconnect();
            mBluetoothLeService.close();
            mConnected = false;
        } catch (Exception ignore) {}
        unregisterReceiver(mGattUpdateReceiver);
        invalidateOptionsMenu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_connected, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDevices.get(currentDevice).getAddress());
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });
    }

    private void displayData(byte[] data) {
        //Log.d(TAG, String.format("%s+++++++", data));

        if (data != null) {



                resultview.setText(data[1]);

        }else{
            Log.d(TAG, "Nothing there GGGGG");
        }
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
    

    // on change of bars write char 


    private void sendData(byte[] test){
        //final byte[] tx = str.getBytes();


        //for(int i = 0; i < test.length; i++) {
            if (mConnected) {
                //characteristicTX.setValue(new byte[] {test[i]});
                mBluetoothLeService.writeCharacteristic(characteristicTX, test);
                mBluetoothLeService.setCharacteristicNotification(characteristicRX, true);
            }
            SystemClock.sleep(2);
        //}

    }
   
}