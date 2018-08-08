package com.example.yasiruw.temprecord.activities;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yasiruw.temprecord.services.Temprecord_BLE;
import com.example.yasiruw.temprecord.R;
import com.example.yasiruw.temprecord.comms.BLEFragmentI;
import com.example.yasiruw.temprecord.comms.BaseCMD;
import com.example.yasiruw.temprecord.comms.CommsSerial;
import com.example.yasiruw.temprecord.CustomLibraries.Yasiru_Temp_Library;
import com.example.yasiruw.temprecord.comms.USBFragmentI;
import com.example.yasiruw.temprecord.fragments.BLEParameterFragment;
import com.example.yasiruw.temprecord.fragments.BLEQueryFragment;
import com.example.yasiruw.temprecord.fragments.BLEReadFragment;
import com.example.yasiruw.temprecord.fragments.USBParameterFragment;
import com.example.yasiruw.temprecord.fragments.USBQueryFragment;
import com.example.yasiruw.temprecord.fragments.USBReadFragment;
import com.example.yasiruw.temprecord.services.BluetoothLeService;
import com.example.yasiruw.temprecord.services.USB;
import com.example.yasiruw.temprecord.utils.GattAttributes;
import com.example.yasiruw.temprecord.comms.HexData;
import com.example.yasiruw.temprecord.services.LeDeviceListAdapter;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;


import java.util.ArrayList;
import java.util.TimeZone;
import java.util.UUID;

import static com.example.yasiruw.temprecord.fragments.BLEQueryFragment.EXTRAS_MESSAGE;


public class MainActivity extends Activity implements
        BLEFragmentI, USBFragmentI {
    //=================Bluetooth service variables==================================================

    private static boolean mConnected = false;
    private static boolean mUSBConnected = false;
    public BluetoothGattCharacteristic characteristicTX;
    public BluetoothGattCharacteristic characteristicRX;
    public final static UUID UUID_CHARACTERISTIC_FIFO = UUID.fromString(GattAttributes.UUID_CHARACTERISTIC_FIFO);


    //============Bluetooth device activity variables===============================================
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    public boolean mScanning;


    private HexData hexData = new HexData();
    private byte[] extraData = new byte[100];
    private BaseCMD baseCMD = new BaseCMD();
    private Yasiru_Temp_Library QS = new Yasiru_Temp_Library();
    private CommsSerial commsSerial = new CommsSerial();
    private Temprecord_BLE temprecord_ble;
    private ArrayList < String > Q_data = new ArrayList < String > ();
    private int main_state = 1;
    private Menu mainmenu;
    ProgressDialog progressDialog;
    private static final Handler mainThreadHandler = new Handler();
    Runnable delayedTask;
    public TimeZone default_timeZone;


    //====================layout variables==============================================================
    LinearLayout r1;
    LinearLayout r2;
    LinearLayout r3;
    LinearLayout r4;
    LinearLayout list;
    LinearLayout con_info;
    LinearLayout fragment_container;
    LinearLayout menubuttons;
    TextView state;
    TextView type;
    TextView serial;
    TextView battery;
    TextView BLEname;
    ImageView connecttype;

    LinearLayout starttext;
    LinearLayout parametertext;
    LinearLayout reusetext;
    LinearLayout tagtext;
    LinearLayout readtext;
    LinearLayout stoptext;
    public ScrollView scroll;

    ImageButton read;
    ImageButton query;
    ImageButton parameters;
    ImageButton tag;
    ImageButton start;
    ImageButton stop;
    ImageButton devicelist;
    ImageButton reuse;
    ImageButton files;
    ImageButton help;
    ImageButton findlogger;
    ImageButton settings;

    ListView lvDevices;

    private Bundle m_bundle_data = new Bundle();
    //========================Fragment Initialisation===============================================
    private int FragmentNumber = 0;
    private BLEQueryFragment bleQueryFragment = new BLEQueryFragment();
    private BLEReadFragment bleReadFragment = new BLEReadFragment();
    private BLEParameterFragment bleParameterFragment = new BLEParameterFragment();
    private USBQueryFragment usbQueryFragment = new USBQueryFragment();
    private USBReadFragment usbReadFragment = new USBReadFragment();
    private USBParameterFragment usbParameterFragment = new USBParameterFragment();
    //==============================================================================================

    //Classes
    //==========================================================//
    USB m_usb;
    public  static  int     m_next_memory_address   = 0;
    //==========================================================//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        r1 =  findViewById(R.id.r1);
        r2 =  findViewById(R.id.r2);
        r3 =  findViewById(R.id.r3);
        r4 = findViewById(R.id.r4);
        list =  findViewById(R.id.List);
        con_info =  findViewById(R.id.con_info);
        menubuttons =  findViewById(R.id.Menubuttons);
        fragment_container =  findViewById(R.id.Fragment_Container);
        state =  findViewById(R.id.state);
        type =  findViewById(R.id.type);
        serial =  findViewById(R.id.serial);
        battery =  findViewById(R.id.battery);
        BLEname =  findViewById(R.id.blename);
        connecttype = findViewById(R.id.connecttype);
        starttext =  findViewById(R.id.starttext);
        parametertext =  findViewById(R.id.parametertext);
        reusetext =  findViewById(R.id.reusetext);
        tagtext =  findViewById(R.id.tagtext);
        readtext =  findViewById(R.id.readtext);
        stoptext =  findViewById(R.id.stoptext);

        read =  findViewById(R.id.read);
        query =  findViewById(R.id.query);
        parameters =  findViewById(R.id.parameters);
        tag =  findViewById(R.id.tag);
        start =  findViewById(R.id.start);
        stop =  findViewById(R.id.stop);
        devicelist =  findViewById(R.id.devicelist);
        reuse =  findViewById(R.id.reuse);
        files =  findViewById(R.id.files);
        help =  findViewById(R.id.help);
        findlogger =  findViewById(R.id.find);
        settings =  findViewById(R.id.settings);

        lvDevices =  findViewById(R.id.lvDevices);
        scroll = findViewById(R.id.screen);

        getActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
        Update_UI_state(1);

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        if(!mUSBConnected) {
            final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
        if(!mUSBConnected) {
            temprecord_ble = new Temprecord_BLE(mBluetoothAdapter, MainActivity.this, lvDevices);
            //temprecord_ble.isBluetoothSupported();// checks if Bluetooth is supported
            //temprecord_ble.isBLESupported();// checks if BLE is supported
            // Use this check to determine whether BLE is supported on the device.  Then you can
            // selectively disable BLE-related features.

            if (mBluetoothAdapter == null) {
                Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
                finish();
            }
        }


        //Register Receiver to be notified for All internal intent USB and BLE
        //==========================================================//
        IntentFilter m_internal_intent_filter = new IntentFilter("temprecord_action_receiver");
        m_internal_intent_filter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        m_internal_intent_filter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        m_internal_intent_filter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        m_internal_intent_filter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        m_internal_intent_filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        LocalBroadcastManager.getInstance(this).registerReceiver(INTERNAL_BROADCAST_RECEIVER,
                m_internal_intent_filter);
        //==========================================================//

        //==============Menu Button click events ===================================================
        read.setOnClickListener(OnMenuButtonClick);
        query.setOnClickListener(OnMenuButtonClick);
        parameters.setOnClickListener(OnMenuButtonClick);
        tag.setOnClickListener(OnMenuButtonClick);
        start.setOnClickListener(OnMenuButtonClick);
        stop.setOnClickListener(OnMenuButtonClick);
        devicelist.setOnClickListener(OnMenuButtonClick);
        reuse.setOnClickListener(OnMenuButtonClick);
        files.setOnClickListener(OnMenuButtonClick);
        help.setOnClickListener(OnMenuButtonClick);
        findlogger.setOnClickListener(OnMenuButtonClick);
        settings.setOnClickListener(OnMenuButtonClick);
        //QS.LocaltoUTC();


    }

    //==================================================================================================================================================================================//
    //==================================================================Button Click events=============================================================================================//
    //==================================================================================================================================================================================//
    //=============================================================================================//
    // Menu button click event Listener
    //=============================================================================================//
    private View.OnClickListener OnMenuButtonClick = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.read:
                    //Log.i("SPEED", "start stufff++++++++++++++++");
                    if (mUSBConnected) {
                        Update_UI_state(3);
                        m_bundle_data.putString(EXTRAS_MESSAGE, "6"); //put string, int, etc in bundle with a key value
                        usbReadFragment = new USBReadFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                        FragmentNumber = 5;
                        getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, usbReadFragment).commit();
                    } else if(mConnected) {
                        Update_UI_state(3);
                        m_bundle_data.putString(EXTRAS_MESSAGE, "6"); //put string, int, etc in bundle with a key value
                        bleReadFragment = new BLEReadFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                        FragmentNumber = 2;
                        getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, bleReadFragment).commit();
                    }
                    Toast.makeText(MainActivity.this, getString(R.string.Read_S), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.query:
                    if(mUSBConnected){
                        Update_UI_state(3);
                        m_bundle_data.putString(EXTRAS_MESSAGE, "1"); //put string, int, etc in bundle with a key value
                        usbQueryFragment = new USBQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                        FragmentNumber = 4;
                        getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, usbQueryFragment).commit();
                    }else if(mConnected) {
                        Update_UI_state(3);
                        m_bundle_data.putString(EXTRAS_MESSAGE, "1"); //put string, int, etc in bundle with a key value
                        bleQueryFragment = new BLEQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                        FragmentNumber = 1;
                        getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, bleQueryFragment).commit();
                    }
                    Toast.makeText(MainActivity.this, getString(R.string.Query_S), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.parameters:
                    if (mUSBConnected) {
                        Update_UI_state(3);
                        m_bundle_data.putString(EXTRAS_MESSAGE, "7"); //put string, int, etc in bundle with a key value
                        usbParameterFragment = new USBParameterFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                        FragmentNumber = 6;
                        getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, usbParameterFragment).commit();
                    } else if(mConnected) {
                        Update_UI_state(3);
                        m_bundle_data.putString(EXTRAS_MESSAGE, "7"); //put string, int, etc in bundle with a key value
                        bleParameterFragment = new BLEParameterFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                        FragmentNumber = 3;
                        getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, bleParameterFragment).commit();
                    }
                    Toast.makeText(MainActivity.this, getString(R.string.Parameters_S), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tag:
                    if (mUSBConnected) {
                        Update_UI_state(3);
                        m_bundle_data.putString(EXTRAS_MESSAGE, "5"); //put string, int, etc in bundle with a key value
                        usbQueryFragment = new USBQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                        FragmentNumber = 4;
                        getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, usbQueryFragment).commit();
                    } else if(mConnected) {
                        Update_UI_state(3);
                        m_bundle_data.putString(EXTRAS_MESSAGE, "5"); //put string, int, etc in bundle with a key value
                        bleQueryFragment = new BLEQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                        FragmentNumber = 1;
                        getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, bleQueryFragment).commit();
                    }
                    Toast.makeText(MainActivity.this, getString(R.string.Tag_S), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.start:
                    if (mUSBConnected) {
                        Update_UI_state(3);
                        m_bundle_data.putString(EXTRAS_MESSAGE, "2"); //put string, int, etc in bundle with a key value
                        usbQueryFragment = new USBQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                        FragmentNumber = 4;
                        getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, usbQueryFragment).commit();
                    } else if(mConnected) {
                        Update_UI_state(3);
                        m_bundle_data.putString(EXTRAS_MESSAGE, "2"); //put string, int, etc in bundle with a key value
                        bleQueryFragment = new BLEQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                        FragmentNumber = 1;
                        getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, bleQueryFragment).commit();
                        //Toast.makeText(MainActivity.this, "Start Selected", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(MainActivity.this, getString(R.string.Start_S), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.stop:
                    if (mUSBConnected) {
                        Update_UI_state(3);
                        m_bundle_data.putString(EXTRAS_MESSAGE, "3"); //put string, int, etc in bundle with a key value
                        usbQueryFragment = new USBQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                        FragmentNumber = 4;
                        getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, usbQueryFragment).commit();
                    } else if(mConnected) {
                        Update_UI_state(3);
                        m_bundle_data.putString(EXTRAS_MESSAGE, "3"); //put string, int, etc in bundle with a key value
                        bleQueryFragment = new BLEQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                        FragmentNumber = 1;
                        getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, bleQueryFragment).commit();
                        //Toast.makeText(MainActivity.this, "Stop Selected", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(MainActivity.this, getString(R.string.Stop_S), Toast.LENGTH_SHORT).show();

                    break;
                case R.id.devicelist:
                    if(mUSBConnected){
                        Toast.makeText(MainActivity.this, getString(R.string.Not_available_with_USB), Toast.LENGTH_SHORT).show();
                    }else {
                        mainThreadHandler.removeCallbacksAndMessages(null);
                        appStartState();
                    }
                    break;
                case R.id.reuse:
                    if (mUSBConnected) {
                        Update_UI_state(3);
                        m_bundle_data.putString(EXTRAS_MESSAGE, "4"); //put string, int, etc in bundle with a key value
                        usbQueryFragment = new USBQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                        FragmentNumber = 4;
                        getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, usbQueryFragment).commit();
                    } else if(mConnected) {
                        Update_UI_state(3);
                        m_bundle_data.putString(EXTRAS_MESSAGE, "4"); //put string, int, etc in bundle with a key value
                        bleQueryFragment = new BLEQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                        FragmentNumber = 1;
                        getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, bleQueryFragment).commit();
                        //Toast.makeText(MainActivity.this, "Re-Use Selected", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(MainActivity.this, getString(R.string.Reuse_S), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.files:
                    Toast.makeText(MainActivity.this, getString(R.string.nextversion), Toast.LENGTH_LONG).show();
                    // custom dialog
//                    final Dialog dialog = new Dialog(MainActivity.this);
//                    dialog.setContentView(R.layout.filesbutton_dialog);
//                    dialog.setTitle(getString(R.string.files));
//                    ImageButton ssbutton = (ImageButton) dialog.findViewById(R.id.screenshot);
//                    ImageButton pdfbutton = (ImageButton) dialog.findViewById(R.id.pdf);
//                    // if button is clicked, close the custom dialog
//                    ssbutton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            dialog.dismiss();
//                        }
//                    });
//                    pdfbutton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(MainActivity.this, getString(R.string.nextversion), Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                        }
//                    });
//
//                    dialog.show();
                    break;
                case R.id.help:
                    Intent myIntent = new Intent(MainActivity.this, AboutActivity.class);
                    //myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"6");
                    MainActivity.this.startActivity(myIntent);
                    Toast.makeText(MainActivity.this, getString(R.string.Help_S), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.find:
                    if (mUSBConnected) {
//                  Update_UI_state(3);
//                  m_bundle_data.putString(EXTRAS_MESSAGE, "8"); //put string, int, etc in bundle with a key value
//                  usbQueryFragment = new USBQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
//                  FragmentNumber = 4;
//                  getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, usbQueryFragment).commit();
                        Toast.makeText(MainActivity.this, getString(R.string.Invalid_for_device), Toast.LENGTH_SHORT).show();
                    } else if(mConnected) {
                        Update_UI_state(3);
                        m_bundle_data.putString(EXTRAS_MESSAGE, "8"); //put string, int, etc in bundle with a key value
                        bleQueryFragment = new BLEQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                        FragmentNumber = 1;
                        getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, bleQueryFragment).commit();

                    }
                    Toast.makeText(MainActivity.this, getString(R.string.Find_S), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.settings:
                    Intent myIntent1 = new Intent(MainActivity.this, SettingsActivity.class);
                    //myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"6");
                    MainActivity.this.startActivity(myIntent1);
                    Toast.makeText(MainActivity.this,getString(R.string.Settings_S), Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    //=============================================================================================//
    //Settings and Back Key Interception
    //=============================================================================================//
    @Override
    public boolean onKeyUp(int keycode, KeyEvent e) {
        switch (keycode) {

            case KeyEvent.KEYCODE_BACK:

                switch (FragmentNumber) {
                    case 1:
                        //coming back from the query page
                        BLEQueryFragment.mainThreadHandler.removeCallbacksAndMessages(null);
                        for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); ++i) {
                            getFragmentManager().popBackStack();
                        }
                        if (mConnected) {
                            temprecord_ble.mBluetoothLeService.writeCharacteristic(characteristicTX, HexData.QUARY);
                            temprecord_ble.mBluetoothLeService.readCharacteristic(characteristicRX);
                            Update_UI_state(2);
                        } else {
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            LocalBroadcastManager.getInstance(this).unregisterReceiver(INTERNAL_BROADCAST_RECEIVER);
                            unbindService(temprecord_ble.mServiceConnection);
                            finish();
                            startActivity(intent);
                        }
                        break;
                    case 2:
                        //coming back from the read page
                        BLEReadFragment.mainThreadHandler.removeCallbacksAndMessages(null);
                        for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); ++i) {
                            getFragmentManager().popBackStack();
                        }
                        if (mConnected) {
                            temprecord_ble.mBluetoothLeService.writeCharacteristic(characteristicTX, HexData.QUARY);
                            temprecord_ble.mBluetoothLeService.readCharacteristic(characteristicRX);
                            Update_UI_state(2);
                        } else {
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            LocalBroadcastManager.getInstance(this).unregisterReceiver(INTERNAL_BROADCAST_RECEIVER);
                            unbindService(temprecord_ble.mServiceConnection);
                            finish();
                            startActivity(intent);
                        }
                        break;
                    case 3://comming back from the parameter page
                        BLEParameterFragment.mainThreadHandler.removeCallbacksAndMessages(null);
                        for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); ++i) {
                            getFragmentManager().popBackStack();
                        }
                        if (mConnected) {
                            temprecord_ble.mBluetoothLeService.writeCharacteristic(characteristicTX, HexData.QUARY);
                            temprecord_ble.mBluetoothLeService.readCharacteristic(characteristicRX);
                            Update_UI_state(2);
                        } else {
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            LocalBroadcastManager.getInstance(this).unregisterReceiver(INTERNAL_BROADCAST_RECEIVER);
                            unbindService(temprecord_ble.mServiceConnection);
                            finish();
                            startActivity(intent);
                        }
                        break;
                    case 4:
                        for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); ++i) {
                            getFragmentManager().popBackStack();
                        }
                        if (mUSBConnected) {
                            m_usb.Send_Command(HexData.QUARY_USB);
                            Update_UI_state(2);
                        } else {
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            m_usb.unregister_receiver(this);
                            LocalBroadcastManager.getInstance(this).unregisterReceiver(INTERNAL_BROADCAST_RECEIVER);
                            finish();
                            startActivity(intent);
                        }
                        break;
                    case 5:
                        for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); ++i) {
                            getFragmentManager().popBackStack();
                        }
                        if (mUSBConnected) {
                            m_usb.Send_Command(HexData.QUARY_USB);
                            Update_UI_state(2);
                        } else {
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            m_usb.unregister_receiver(this);
                            LocalBroadcastManager.getInstance(this).unregisterReceiver(INTERNAL_BROADCAST_RECEIVER);
                            finish();
                            startActivity(intent);
                        }
                        break;
                    case 6:
                        for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); ++i) {
                            getFragmentManager().popBackStack();
                        }
                        if (mUSBConnected) {
                            m_usb.Send_Command(HexData.QUARY_USB);
                            Update_UI_state(2);
                        } else {
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            m_usb.unregister_receiver(this);
                            LocalBroadcastManager.getInstance(this).unregisterReceiver(INTERNAL_BROADCAST_RECEIVER);
                            finish();
                            startActivity(intent);
                        }
                        break;

                }

        }
        return super.onKeyUp(keycode, e);
    }
    //==================================================================================================================================================================================//
    //==================================================================UI updates and fragment comms===================================================================================//
    //==================================================================================================================================================================================//
    //=============================================================================================//
    //Setting different states in the Main Activity
    //=============================================================================================//
    public void Update_UI_state(int UIstate) {
        switch (UIstate) {

            case 1:
                //start of the app with the device list
                menubuttons.setVisibility(View.VISIBLE);
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.GONE);
                r3.setVisibility(View.GONE);
                r4.setVisibility(View.VISIBLE);
                list.setVisibility(View.VISIBLE);
                con_info.setVisibility(View.GONE);
                fragment_container.setVisibility(View.GONE);
                main_state = 1;

                break;
            case 2:

                //after connection to a logger over BLE
               // Log.i("YAS", "UI UPDATE STATE " + baseCMD.state);
                FragmentNumber = 0;
                buttonVisibility();
                r3.setVisibility(View.VISIBLE);
                r4.setVisibility(View.VISIBLE);
                menubuttons.setVisibility(View.VISIBLE);
                list.setVisibility(View.GONE);
                con_info.setVisibility(View.VISIBLE);
                fragment_container.setVisibility(View.GONE);
                getActionBar().hide();
                main_state = 2;
                break;
            case 3:
                mainThreadHandler.removeCallbacksAndMessages(null);
                //when query or read fragment is selected
                menubuttons.setVisibility(View.GONE);
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.GONE);
                r3.setVisibility(View.GONE);
                r4.setVisibility(View.GONE);
                list.setVisibility(View.GONE);
                con_info.setVisibility(View.GONE);
                fragment_container.setVisibility(View.VISIBLE);
                main_state = 3;

                break;
        }
        invalidateOptionsMenu();
    }

    //=============================================================================================//
    //Settings which buttons are visible at different states
    //=============================================================================================//

    private void buttonVisibility() {
        if (baseCMD.state == 2) { //ready state
            r2.setVisibility(View.GONE);
            parametertext.setVisibility(View.VISIBLE);
            starttext.setVisibility(View.VISIBLE);
            reusetext.setVisibility(View.GONE);
            r1.setVisibility(View.VISIBLE);

        } else if (baseCMD.state == 3) { //start delay state
            r2.setVisibility(View.GONE);
            r1.setVisibility(View.GONE);

        } else if (baseCMD.state == 4) { //running
            tagtext.setVisibility(View.VISIBLE);
            readtext.setVisibility(View.VISIBLE);
            stoptext.setVisibility(View.VISIBLE);
            r2.setVisibility(View.VISIBLE);
            r1.setVisibility(View.GONE);

        } else if (baseCMD.state == 5) { //stopped
            parametertext.setVisibility(View.GONE);
            tagtext.setVisibility(View.GONE);
            starttext.setVisibility(View.GONE);
            stoptext.setVisibility(View.GONE);
            readtext.setVisibility(View.VISIBLE);
            reusetext.setVisibility(View.VISIBLE);
            r2.setVisibility(View.VISIBLE);
            r1.setVisibility(View.VISIBLE);

        }else if(baseCMD.state == 7){//error state
            r2.setVisibility(View.GONE);
            r1.setVisibility(View.GONE);
        }else {
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.VISIBLE);

        }
    }

    //=============================================================================================//
    //Filling in the data in the Blue banner
    //logger state, serial number and  connection status
    //=============================================================================================//

    private void fillBlueBanner(byte[] data) {
        //commsSerial.BytetoHex(commsSerial.ReadUSBByte(data));
        Typeface font = Typeface.createFromAsset(this.getAssets(), "Roboto-Light.ttf");
        if(data[0] == 0xFF){
            temprecord_ble.mBluetoothLeService.writeCharacteristic(characteristicTX, HexData.QUARY);
            temprecord_ble.mBluetoothLeService.readCharacteristic(characteristicRX);
        }
        if(!mUSBConnected)
            Q_data = baseCMD.CMDQuery(commsSerial.ReadByte(data));
        else
            Q_data = baseCMD.CMDQuery(commsSerial.ReadUSBByte(data));
        if (Q_data.size() >= 7) {
            state.setText(QS.GetState(Integer.parseInt(Q_data.get(5))));
            state.setTypeface(font);
            serial.setText(Q_data.get(0));
            serial.setTypeface(font);
            battery.setText(Q_data.get(6)+getString(R.string.Percentage));
            battery.setTypeface(font);
            if (mConnected && !mUSBConnected) {
                type.setText(getString(R.string.BLE_Active));
                connecttype.setImageDrawable(getResources().getDrawable(R.drawable.ic_bluetooth_logo));
                BLEname.setText(temprecord_ble.getDevice().getName());
                BLEname.setTypeface(font);
            } else if(mUSBConnected){
                type.setText(getString(R.string.USB_Active));
                connecttype.setImageDrawable(getResources().getDrawable(R.drawable.ic_usb_logo));
            }//we can add NFC or WI-FI here as well
            else type.setText(getString(R.string.Inactive));
            type.setTypeface(font);
        }
    }

    //=============================================================================================//
    //Sending the required data to the active fragment by calling the CommsI function in each
    //fragment
    //=============================================================================================//
    public void send_to_active_Fragment(byte[] data) {

        switch (FragmentNumber) {
            case 0:
                //still in the main activity so access the UI elements directly=

                fillBlueBanner(data);
                if(mConnected || mUSBConnected)
                Update_UI_state(2);// updates the buttons concidering what state the logger is in
                if(mConnected) {
                    BLEmainTimeout();

                    temprecord_ble.stopRunable();
                }
                if(progressDialog!=null)
                progressDialog.cancel();
                break;
            case 1:
                //query page(BLE)
                bleQueryFragment.CommsI(data);
                break;
            case 2:
                //read page(BLE)
                bleReadFragment.CommsI(data);
                break;
            case 3:
                //parameter page(BLE)
                bleParameterFragment.CommsI(data);
                break;
            case 4:
                //USB query page
                usbQueryFragment.CommsI(data);
                break;
            case 5:
                //USB read page
                usbReadFragment.CommsI(data);
                break;
            case 6:
                usbParameterFragment.CommsI(data);
                break;
        }
    }


    //=============================================================================================//
    //Used to update the connection status of the fragment if a timeout occurs while in that
    //fragment
    //=============================================================================================//
    public void send_to_active_Fragment(final int resourceId, final int num) {
        switch (FragmentNumber) {
            case 0:
                //still in the main activity so access the UI elements directly
                break;
            case 1:
                //query page
                bleQueryFragment.updateConnectionState(resourceId, num);
                break;
            case 2:
                //read page
                bleReadFragment.updateConnectionState(resourceId, num);
                break;
            case 3:
                bleParameterFragment.updateConnectionState(resourceId, num);
        }
    }


    //==================================================================================================================================================================================//
    //==================================================================Main activity overriding functions==============================================================================//
    //==================================================================================================================================================================================//
    //=============================================================================================//
    //Closing the app when back press is pressed from the device list state of the main activity
    //=============================================================================================//
    @Override
    public void onBackPressed() {
        if (main_state == 1) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mainmenu = menu;
        getMenuInflater().inflate(R.menu.menu_devices, menu);
        if ( FragmentNumber > 0     )                {//Fix fot the weired menu behaviour
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_stop).setVisible(false);
            temprecord_ble.scanLeDevice(false);//so the name doesnt change in the query page
        } else {

            if (!mScanning) {

                menu.findItem(R.id.menu_stop).setVisible(false);
                menu.findItem(R.id.menu_scan).setVisible(true);
                menu.findItem(R.id.menu_refresh).setActionView(null);
            } else {
                menu.findItem(R.id.menu_stop).setVisible(true);
                menu.findItem(R.id.menu_scan).setVisible(false);
                menu.findItem(R.id.menu_refresh).setActionView(
                        R.layout.actionbar_indeterminate_progress);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_scan:
                mLeDeviceListAdapter.clear();
                if(!mUSBConnected)temprecord_ble.scanLeDevice(true);
                break;
            case R.id.menu_stop:
                if(!mUSBConnected)temprecord_ble.scanLeDevice(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();


        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
       if(!mUSBConnected) temprecord_ble.BLE_turnon_dialog();

        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter(getApplicationContext(), MainActivity.this);
        temprecord_ble.setListAdapter(mLeDeviceListAdapter, mScanning);
        if(!mUSBConnected)temprecord_ble.scanLeDevice(true);

        if (m_usb == null)
        {
            m_usb = new USB(this);
        }

        m_usb.Connection_Initialisation();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //Log.i("YO", "On Destroy !");
        if(m_usb != null)
        m_usb.unregister_receiver(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(INTERNAL_BROADCAST_RECEIVER);
        // if (mGattUpdateReceiver != null && mServiceConnection != null) unregisterReceiver(mGattUpdateReceiver);
        //if (mServiceConnection != null) unbindService(mServiceConnection);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!mUSBConnected)temprecord_ble.scanLeDevice(false);
        mLeDeviceListAdapter.clear();
    }

    //=============================================================================================//
    //function of the BLEFragmentI interface
    //=============================================================================================//
    @Override
    public void onBLERead() {
        try {
            temprecord_ble.mBluetoothLeService.readCharacteristic(characteristicRX);
        } catch(Exception ignore) {
            ignore.printStackTrace();
        }
    }

    //=============================================================================================//
    //function of the BLEFragmentI interface
    //=============================================================================================//
    @Override
    public void onBLEWrite(byte[] value) {
        try {
            temprecord_ble.mBluetoothLeService.writeCharacteristic(characteristicTX, value);
        } catch(Exception ignore) {
            ignore.printStackTrace();
        }
    }
    //=============================================================================================//
    //function of the BLEFragmentI interface
    //=============================================================================================//
    @Override
    public void BLEDisconnect() {
        //unregisterReceiver(mGattUpdateReceiver);
        //        unbindService(mServiceConnection);
        temprecord_ble.mBluetoothLeService.disconnect();
    }

    //=============================================================================================//
    //function of the USBFragmentI interface
    //=============================================================================================//
    @Override
    public void onUSBWrite(byte[] value)
    {

        m_usb.Send_Command(value);
    }

    //==================================================================================================================================================================================//
    //==================================================================Receives action from USB and BLE================================================================================//
    //==================================================================================================================================================================================//
    //=============================================================================================//
    // MAIN LOCAL BROADCAST RECEIVER USB
    //=============================================================================================//
    public BroadcastReceiver INTERNAL_BROADCAST_RECEIVER = new BroadcastReceiver()
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            final String action = intent.getAction();


            //Log.d("TAG", "Coming in to  INTERNAL_BROADCAST_RECEIVER " + action );
            if (intent.getPackage() != null) {
                switch (intent.getPackage()) {

                    case "DETACHED":
                        mUSBConnected = false;
                        //Update_UI_state(1);
                        //getActionBar().show();
                        m_usb.unregister_receiver(getApplicationContext());
                        //finish();
                        //LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(INTERNAL_BROADCAST_RECEIVER);

                        break;

                    case "UI_update":
                        if (extras != null) {
                            String message = extras.getString("message");
                            //Log.i("Device", message+"  ===========================");

                            switch (message){
                                case "U06 <- USB CONNECTED !":
                                    if(mConnected && temprecord_ble.mBluetoothLeService != null){//if BLE is in connected state it disconnects before USB comms happen
                                        temprecord_ble.mBluetoothLeService.writeCharacteristic(characteristicTX, HexData.GO_TO_SLEEP);
                                        mConnected = false;
                                    }
                                    //Log.i("Device", "coming in to USB connected part===========================");
                                    //spinnerProgressDialog();
                                    if(!mUSBConnected)temprecord_ble.scanLeDevice(false);
                                    mScanning = false;
                                    mUSBConnected = true;
                                    if(getFragmentManager().getBackStackEntryCount() == 0) {
                                        Update_UI_state(3);
                                        m_bundle_data.putString(EXTRAS_MESSAGE, "1"); //put string, int, etc in bundle with a key value
                                        usbQueryFragment = new USBQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                                        FragmentNumber = 4;

                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                        transaction.replace(R.id.Fragment_Container, usbQueryFragment).addToBackStack(null).commit();
//
                                    }
                                    //m_usb.Send_Command(HexData.QUARY_USB);
                                    invalidateOptionsMenu();

                                    break;
                            }
                        }
                        break;

                    case "HID_USB_Message_Received":
                        //Log.i("SPEED", "receive stufff=============");
                        //-------------------//
                        if (extras != null) {
                            byte[] i_message = extras.getByteArray("b_data");
                            //commsSerial.BytetoHex(i_message);
                            if (i_message != null) {

                                send_to_active_Fragment(i_message);
                               // usbReadFragment.CommsI(i_message);

                                //DO SOMETHING WITH DATA RECEIVED FROM USB
                            }
                        }
                        break;

                }
            }

            //==============================================================================================
            // Handles various events fired by the bluetooth Service.
            // ACTION_GATT_CONNECTED: connected to a GATT server.
            // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
            // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
            // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
            //                        or notification operations.
            //==============================================================================================
            if(intent.getAction() != null){
                switch (action){
                    case BluetoothLeService.ACTION_GATT_CONNECTED:
                        mConnected = true;
                        mainmenu.findItem(R.id.menu_scan).setVisible(false);
                        invalidateOptionsMenu();
                        send_to_active_Fragment(R.string.connected, 1);
                        break;

                    case BluetoothLeService.ACTION_GATT_DISCONNECTED:
                        invalidateOptionsMenu();
                        mConnected = false;
                        send_to_active_Fragment(R.string.disconnected, 0);
                        break;

                    case BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED:
                        //Log.i("TEST", "IN GATT SERVICES DISCOVERED----------------------------------");
                        SystemClock.sleep(10);
                        temprecord_ble.displayGattServices(temprecord_ble.mBluetoothLeService.getSupportedGattServices());
                        temprecord_ble.mBluetoothLeService.setCharacteristicNotification(characteristicTX, true);
                        temprecord_ble.mBluetoothLeService.setCharacteristicNotification(characteristicRX, true);
                        temprecord_ble.mBluetoothLeService.writeCharacteristic(characteristicTX, HexData.STAY_UP);
                        temprecord_ble.mBluetoothLeService.writeCharacteristic(characteristicTX, HexData.QUARY);
                        temprecord_ble.mBluetoothLeService.readCharacteristic(characteristicRX);
                        break;

                    case BluetoothLeService.ACTION_DATA_AVAILABLE:
                        //String extraUuid = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
                        //int extraType = intent.getIntExtra(BluetoothLeService.EXTRA_TYPE, -1);
                        extraData = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                       // Log.d("TAG", "extra data size " + extraData.length);
                        commsSerial.BytetoHex(extraData);
                        send_to_active_Fragment(extraData);
                        break;


                }
            }
        }
    };//==========================================================//
//</editor-fold>

    //=============================================================================================//
    //Spinner dialog thats show when trying to connect to a device
    //=============================================================================================//
    public void spinnerProgressDialog() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.Connecting));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.Retry_connecting), new DialogInterface.OnClickListener() {@Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            temprecord_ble.stopRunable();
            appStartState();
        }
        });
        progressDialog.show();
    }

    //=============================================================================================//
    //Going back to the initial state of the app
    //=============================================================================================//
    public void appStartState(){
        Intent intent =  getIntent();
//        Update_UI_state(1);
//        getActionBar().show();
//        temprecord_ble.scanLeDevice(true);
        if ((mConnected)) {
            temprecord_ble.mBluetoothLeService.writeCharacteristic(characteristicTX, HexData.GO_TO_SLEEP);

            if (INTERNAL_BROADCAST_RECEIVER != null)
                LocalBroadcastManager.getInstance(this).unregisterReceiver(INTERNAL_BROADCAST_RECEIVER);

            if (temprecord_ble.mServiceConnection != null) unbindService(temprecord_ble.mServiceConnection);
        }
        finish();
        startActivity(intent);

    }

    public void BLEmainTimeout(){
        delayedTask = new Runnable() {
            @Override
            public void run() {
                if ((mConnected) && temprecord_ble.mBluetoothLeService != null) {
                    type.setText(getString(R.string.Timedout));
                    type.setTextColor(getResources().getColor(R.color.colorRED));
                    temprecord_ble.mBluetoothLeService.writeCharacteristic(characteristicTX, HexData.GO_TO_SLEEP);
                    //Log.d("TAG", "Sending to sleep");
                    SystemClock.sleep(1000);
                    temprecord_ble.mBluetoothLeService.disconnect();
                }


                //Log.d("TAG","IN the delayed task");
            }
        };
        mainThreadHandler.postDelayed(delayedTask, 20000);
    }

    public void Read_Action(){
        if (mUSBConnected) {
            Update_UI_state(3);
            m_bundle_data.putString(EXTRAS_MESSAGE, "6"); //put string, int, etc in bundle with a key value
            usbReadFragment = new USBReadFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
            FragmentNumber = 5;
            getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, usbReadFragment).commit();
        } else if(mConnected) {
            Update_UI_state(3);
            m_bundle_data.putString(EXTRAS_MESSAGE, "6"); //put string, int, etc in bundle with a key value
            bleReadFragment = new BLEReadFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
            FragmentNumber = 2;
            getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, bleReadFragment).commit();
        }
    }

    public void Expandableloggerdetails(View view){
        ExpandableRelativeLayout expandableloggerdetails = (ExpandableRelativeLayout) findViewById(R.id.expandableloggerdetails);
        expandableloggerdetails.toggle(); // toggle expand and collapse
    }

    public void Expandabletripinfomation(View view){
        ExpandableRelativeLayout expandabletripinfomation = (ExpandableRelativeLayout) findViewById(R.id.expandabletripinformation);
        expandabletripinfomation.toggle(); // toggle expand and collapse
    }

    public void Expandablechannelinfomation(View view){
        ExpandableRelativeLayout expandablechannelinfomation = (ExpandableRelativeLayout) findViewById(R.id.expandablechannelinformation);
        expandablechannelinfomation.toggle(); // toggle expand and collapse
    }

    public void Expandableusercomments(View view){
        ExpandableRelativeLayout expandableusercomments = (ExpandableRelativeLayout) findViewById(R.id.expandableusercomments);
        expandableusercomments.toggle(); // toggle expand and collapse
    }

    public  void Expandablequerygraph(View view){
        ExpandableRelativeLayout expandablereadgraph = (ExpandableRelativeLayout) findViewById(R.id.expandablequerygraph);
        expandablereadgraph.toggle(); // toggle expand and collapse
    }

    public void ExpandableReadloggerdetails(View view){
        ExpandableRelativeLayout expandablereadloggerdetails = (ExpandableRelativeLayout) findViewById(R.id.expandablereadloggerdetails);
        expandablereadloggerdetails.toggle(); // toggle expand and collapse
    }

    public  void Expandablereadgraph(View view){
        ExpandableRelativeLayout expandablereadgraph = (ExpandableRelativeLayout) findViewById(R.id.expandablereadgraph);
        expandablereadgraph.toggle(); // toggle expand and collapse
    }

    public  void Expandablechannel1stats(View view){
        ExpandableRelativeLayout expandablechannel1stats = (ExpandableRelativeLayout) findViewById(R.id.expandablechannel1stats);
        expandablechannel1stats.toggle(); // toggle expand and collapse
    }

    public  void Expandablechannel2stats(View view){
        ExpandableRelativeLayout expandablechannel2stats = (ExpandableRelativeLayout) findViewById(R.id.expandablechannel2stats);
        expandablechannel2stats.toggle(); // toggle expand and collapse
    }

}