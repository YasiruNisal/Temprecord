package com.example.yasiruw.temprecord.CustomLibraries;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yasiruw.temprecord.R;
import com.example.yasiruw.temprecord.activities.MainActivity;
import com.example.yasiruw.temprecord.services.BluetoothLeService;
import com.example.yasiruw.temprecord.utils.GattAttributes;
import com.example.yasiruw.temprecord.utils.HexData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.example.yasiruw.temprecord.activities.MainActivity.UUID_CHARACTERISTIC_FIFO;

public class Temprecord_BLE {

    public static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private Activity activity;
    private ListView listView;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private boolean mScanning;
    private BluetoothDevice mDevice;
    public BluetoothLeService mBluetoothLeService;
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    private int pos = 0;


    public static final Handler mainThreadHandler = new Handler();
    Runnable delayedTask;

    public Temprecord_BLE(BluetoothAdapter mBluetoothAdapter, Activity activity, ListView listView){
        this.mBluetoothAdapter = mBluetoothAdapter;
        this.activity = activity;
        this.listView = listView;
    }

    public void isBluetoothSupported(){
        if (mBluetoothAdapter == null) {
            Toast.makeText(activity, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            activity.finish();
            return;
        }
    }

    public void isBLESupported(){
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(activity, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            activity.finish();
        }
    }

    public void BLE_turnon_dialog(){
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    public void setListAdapter(LeDeviceListAdapter mLeDeviceListAdapter, boolean mScanning) {
        this.mLeDeviceListAdapter = mLeDeviceListAdapter;
        this.mScanning = mScanning;
        listView.setAdapter(mLeDeviceListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                pos = position;
                connectToDevices(position);
                ((MainActivity)activity).spinnerProgressDialog();
            }
        });

    }


    //=============================================================================================//
    //trying to connect to a device when a device from the device list is pressed
    //=============================================================================================//
    private void connectToDevices(int position) {
        final ArrayList< BluetoothDevice > devices = new ArrayList < >();
//        for (Integer integer: selectedPositions) {
//
//            devices.add(device);
//        }
        BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
        //stop scanning
        if (mScanning) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            scanLeDevice(false);
            mScanning = false;
        }
        //mDevices = devices;
        this.mDevice = device;
        //selectedPositions.clear();
        activity.getActionBar().setTitle(mDevice.getName());
        Intent gattServiceIntent = new Intent(activity, BluetoothLeService.class);
        activity.bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        Log.i("TEST", "IN after binding to a service ----------------------------------");

    }


    public void scanLeDevice(final boolean enable) {

        if (enable) {
            ((MainActivity)activity).mScanning = true;

            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            ((MainActivity)activity).mScanning = false;
            if(mLeScanCallback != null)
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        activity.invalidateOptionsMenu();
    }

    // Device scan callback.
    public BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {


           activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    mLeDeviceListAdapter.addDevice(device, rssi);
                    if (mLeDeviceListAdapter.getCount() == 0 ) activity.getActionBar().setTitle(R.string.Searching_Device);
                    else if (mLeDeviceListAdapter.getCount() > 0 ) {
                        activity.getActionBar().setTitle(R.string.Available_Devices);
                    }

                }
            });
        }
    };


    public BluetoothDevice getDevice(){
        return mDevice;
    }

    public ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, final IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                activity.finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDevice.getAddress());
            ((MainActivity)activity).Update_UI_state(2);

            delayedTask = new Runnable() {
                @Override
                public void run() {
                    mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
                    if (!mBluetoothLeService.initialize()) {
                        activity.finish();
                    }
                    mBluetoothLeService.connect(mDevice.getAddress());
                    mainThreadHandler.postDelayed(delayedTask, 8000);
                    Log.i("TEST", "IN on service connected runable after 8 secs----------------------------------");
                }
            };

            mainThreadHandler.postDelayed(delayedTask, 9000);
            Log.i("TEST", "IN on service connected ----------------------------------");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    public void stopRunable(){
        Log.i("TEST", "IN on service connected runable STOP ----------------------------------");
        mainThreadHandler.removeCallbacksAndMessages(null);
    }



    //==============================================================================================
    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    //==============================================================================================
    public void displayGattServices(List< BluetoothGattService > gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = activity.getResources().getString(R.string.unknown_service);
        ArrayList <HashMap< String,
                String >> gattServiceData = new ArrayList < HashMap < String,
                String >> ();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService: gattServices) {
            HashMap < String,
                    String > currentServiceData = new HashMap < String,
                    String > ();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, GattAttributes.lookup(uuid, unknownServiceString));

            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            // get characteristic when UUID matches RX/TX UUID
            ((MainActivity)activity).characteristicTX = gattService.getCharacteristic(UUID_CHARACTERISTIC_FIFO);
            ((MainActivity)activity).characteristicRX = gattService.getCharacteristic(UUID_CHARACTERISTIC_FIFO);
        }

    }





}
