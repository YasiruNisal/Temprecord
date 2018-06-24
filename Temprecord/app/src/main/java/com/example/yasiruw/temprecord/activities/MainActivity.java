package com.example.yasiruw.temprecord.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yasiruw.temprecord.R;
import com.example.yasiruw.temprecord.comms.BLEFragmentI;
import com.example.yasiruw.temprecord.comms.BaseCMD;
import com.example.yasiruw.temprecord.comms.CommsSerial;
import com.example.yasiruw.temprecord.comms.QueryStrings;
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
import com.example.yasiruw.temprecord.utils.HexData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.example.yasiruw.temprecord.fragments.BLEQueryFragment.EXTRAS_MESSAGE;

public class MainActivity extends Activity implements
        AdapterView.OnItemClickListener,
        BLEFragmentI, USBFragmentI {
    //=================Bluetooth service variables==================================================

    private List < BluetoothDevice > mDevices = new ArrayList < >();
    private BluetoothLeService mBluetoothLeService;
    private static boolean mConnected = false;
    private static boolean mUSBConnected = false;
    private BluetoothGattCharacteristic characteristicTX;
    private BluetoothGattCharacteristic characteristicRX;
    public final static UUID UUID_CHARACTERISTIC_FIFO = UUID.fromString(GattAttributes.UUID_CHARACTERISTIC_FIFO);
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    //============Bluetooth device activity variables===============================================
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;

    private static final int REQUEST_ENABLE_BT = 1;
    private HexData hexData = new HexData();
    private byte[] extraData = new byte[100];
    private BaseCMD baseCMD = new BaseCMD();
    private QueryStrings QS = new QueryStrings();
    private CommsSerial commsSerial = new CommsSerial();
    private byte[] returndata;
    private ArrayList < String > Q_data = new ArrayList < String > ();
    private ArrayList < String > U_data = new ArrayList < String > ();
    private ArrayList < String > F_data = new ArrayList < String > ();
    private ArrayList < String > R_data = new ArrayList < String > ();
    private int main_state = 1;
    private int ones = 0;
    private Menu mainmenu;
    ProgressDialog progressDialog;

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
    ImageView connecttype;

    LinearLayout starttext;
    LinearLayout parametertext;
    LinearLayout reusetext;
    LinearLayout tagtext;
    LinearLayout readtext;
    LinearLayout stoptext;

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

        r1 = (LinearLayout) findViewById(R.id.r1);
        r2 = (LinearLayout) findViewById(R.id.r2);
        r3 = (LinearLayout) findViewById(R.id.r3);
        r4 = (LinearLayout) findViewById(R.id.r4);
        list = (LinearLayout) findViewById(R.id.List);
        con_info = (LinearLayout) findViewById(R.id.con_info);
        menubuttons = (LinearLayout) findViewById(R.id.Menubuttons);
        fragment_container = (LinearLayout) findViewById(R.id.Fragment_Container);
        state = (TextView) findViewById(R.id.state);
        type = (TextView) findViewById(R.id.type);
        serial = (TextView) findViewById(R.id.serial);
        battery = (TextView) findViewById(R.id.battery);
        connecttype = (ImageView) findViewById(R.id.connecttype);
        starttext = (LinearLayout) findViewById(R.id.starttext);
        parametertext = (LinearLayout) findViewById(R.id.parametertext);
        reusetext = (LinearLayout) findViewById(R.id.reusetext);
        tagtext = (LinearLayout) findViewById(R.id.tagtext);
        readtext = (LinearLayout) findViewById(R.id.readtext);
        stoptext = (LinearLayout) findViewById(R.id.stoptext);

        read = (ImageButton) findViewById(R.id.read);
        query = (ImageButton) findViewById(R.id.query);
        parameters = (ImageButton) findViewById(R.id.parameters);
        tag = (ImageButton) findViewById(R.id.tag);
        start = (ImageButton) findViewById(R.id.start);
        stop = (ImageButton) findViewById(R.id.stop);
        devicelist = (ImageButton) findViewById(R.id.devicelist);
        reuse = (ImageButton) findViewById(R.id.reuse);
        files = (ImageButton) findViewById(R.id.files);
        help = (ImageButton) findViewById(R.id.help);
        findlogger = (ImageButton) findViewById(R.id.find);
        settings = (ImageButton) findViewById(R.id.settings);

        Update_UI_state(1);

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //filter with required action
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);

        registerReceiver(mGattUpdateReceiver, intentFilter);

        //Register Receiver to be notified for All internal intent USB
        //==========================================================//
        IntentFilter m_internal_intent_filter = new IntentFilter("panel_internal_intent");
        LocalBroadcastManager.getInstance(this).registerReceiver(INTERNAL_BROADCAST_RECEIVER,
                m_internal_intent_filter);
        //==========================================================//

        final Bundle m_bundle_data = new Bundle();

        //==============Menu Button click events ===================================================

        devicelist.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {
            if(mUSBConnected){
                Toast.makeText(MainActivity.this, "Unavailable while USB is connected", Toast.LENGTH_SHORT).show();
            }else
                appStartState();
        }
        });

        read.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {
            if (mUSBConnected) {
                Update_UI_state(3);
                m_bundle_data.putString(EXTRAS_MESSAGE, "6"); //put string, int, etc in bundle with a key value
                usbReadFragment = new USBReadFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                FragmentNumber = 5;
                getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, usbReadFragment).commit();
            } else {
                Update_UI_state(3);
                m_bundle_data.putString(EXTRAS_MESSAGE, "6"); //put string, int, etc in bundle with a key value
                bleReadFragment = new BLEReadFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                FragmentNumber = 2;
                getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, bleReadFragment).commit();
            }
            Toast.makeText(MainActivity.this, "Read Selected", Toast.LENGTH_SHORT).show();
        }
        });

        parameters.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {
            if (mUSBConnected) {
                Update_UI_state(3);
                m_bundle_data.putString(EXTRAS_MESSAGE, "7"); //put string, int, etc in bundle with a key value
                usbParameterFragment = new USBParameterFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                FragmentNumber = 6;
                getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, usbParameterFragment).commit();
            } else {
                Update_UI_state(3);
                m_bundle_data.putString(EXTRAS_MESSAGE, "7"); //put string, int, etc in bundle with a key value
                bleParameterFragment = new BLEParameterFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                FragmentNumber = 3;
                getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, bleParameterFragment).commit();
            }Toast.makeText(MainActivity.this, "Parameters Selected", Toast.LENGTH_SHORT).show();
        }
        });

        query.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {
            if(mUSBConnected){
                Update_UI_state(3);
                m_bundle_data.putString(EXTRAS_MESSAGE, "1"); //put string, int, etc in bundle with a key value
                usbQueryFragment = new USBQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                FragmentNumber = 4;
                getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, usbQueryFragment).commit();
            }else {
                Update_UI_state(3);
                m_bundle_data.putString(EXTRAS_MESSAGE, "1"); //put string, int, etc in bundle with a key value
                bleQueryFragment = new BLEQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                FragmentNumber = 1;
                getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, bleQueryFragment).commit();
            }
            Toast.makeText(MainActivity.this, "Query Selected", Toast.LENGTH_SHORT).show();
        }
        });

        tag.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {
            if (mUSBConnected) {
                Update_UI_state(3);
                m_bundle_data.putString(EXTRAS_MESSAGE, "5"); //put string, int, etc in bundle with a key value
                usbQueryFragment = new USBQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                FragmentNumber = 4;
                getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, usbQueryFragment).commit();
            } else {
                Update_UI_state(3);
                m_bundle_data.putString(EXTRAS_MESSAGE, "5"); //put string, int, etc in bundle with a key value
                bleQueryFragment = new BLEQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                FragmentNumber = 1;
                getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, bleQueryFragment).commit();
                Toast.makeText(MainActivity.this, "Tag Selected", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(MainActivity.this, "Must be in Running state to Tag", Toast.LENGTH_SHORT).show();

        }
        });

        start.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {
            if (mUSBConnected) {
                Update_UI_state(3);
                m_bundle_data.putString(EXTRAS_MESSAGE, "2"); //put string, int, etc in bundle with a key value
                usbQueryFragment = new USBQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                FragmentNumber = 4;
                getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, usbQueryFragment).commit();
            } else {
                Update_UI_state(3);
                m_bundle_data.putString(EXTRAS_MESSAGE, "2"); //put string, int, etc in bundle with a key value
                bleQueryFragment = new BLEQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                FragmentNumber = 1;
                getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, bleQueryFragment).commit();
                Toast.makeText(MainActivity.this, "Start Selected", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(MainActivity.this, "Must be in Ready state to Start", Toast.LENGTH_SHORT).show();
        }
        });

        stop.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {
            if (mUSBConnected) {
                Update_UI_state(3);
                m_bundle_data.putString(EXTRAS_MESSAGE, "3"); //put string, int, etc in bundle with a key value
                usbQueryFragment = new USBQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                FragmentNumber = 4;
                getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, usbQueryFragment).commit();
            } else {
                Update_UI_state(3);
                m_bundle_data.putString(EXTRAS_MESSAGE, "3"); //put string, int, etc in bundle with a key value
                bleQueryFragment = new BLEQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                FragmentNumber = 1;
                getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, bleQueryFragment).commit();
                Toast.makeText(MainActivity.this, "Stop Selected", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(MainActivity.this, "Must be in Running state to Stop", Toast.LENGTH_SHORT).show();

        }
        });

        reuse.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {
            if (mUSBConnected) {
                Update_UI_state(3);
                m_bundle_data.putString(EXTRAS_MESSAGE, "4"); //put string, int, etc in bundle with a key value
                usbQueryFragment = new USBQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                FragmentNumber = 4;
                getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, usbQueryFragment).commit();
            } else {
                Update_UI_state(3);
                m_bundle_data.putString(EXTRAS_MESSAGE, "4"); //put string, int, etc in bundle with a key value
                bleQueryFragment = new BLEQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                FragmentNumber = 1;
                getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, bleQueryFragment).commit();
                Toast.makeText(MainActivity.this, "Re-Use Selected", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(MainActivity.this, "Must be in Stop state to Re-Use", Toast.LENGTH_SHORT).show();

        }
        });

        findlogger.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {

              if (mUSBConnected) {
//                  Update_UI_state(3);
//                  m_bundle_data.putString(EXTRAS_MESSAGE, "8"); //put string, int, etc in bundle with a key value
//                  usbQueryFragment = new USBQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
//                  FragmentNumber = 4;
//                  getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, usbQueryFragment).commit();
                  Toast.makeText(MainActivity.this, "Invalid for this device", Toast.LENGTH_SHORT).show();
              } else {
                  Update_UI_state(3);
                  m_bundle_data.putString(EXTRAS_MESSAGE, "8"); //put string, int, etc in bundle with a key value
                  bleQueryFragment = new BLEQueryFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
                  FragmentNumber = 1;
                  getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, bleQueryFragment).commit();
                  Toast.makeText(MainActivity.this, "Find Logger Selected", Toast.LENGTH_SHORT).show();
              }
        }
        });

        settings.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
            //myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"6");
            MainActivity.this.startActivity(myIntent);
            Toast.makeText(MainActivity.this,"Settings Selected", Toast.LENGTH_SHORT).show();
        }
        });

        help.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this, AboutActivity.class);
            //myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"6");
            MainActivity.this.startActivity(myIntent);
            Toast.makeText(MainActivity.this, "Help Selected", Toast.LENGTH_SHORT).show();
        }
        });



    }

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
                        for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); ++i) {
                            getFragmentManager().popBackStack();
                        }
                        if (mConnected) {
                            mBluetoothLeService.writeCharacteristic(characteristicTX, hexData.QUARY);
                            mBluetoothLeService.readCharacteristic(characteristicRX);
                            Update_UI_state(2);
                        } else {
                            Intent intent = getIntent();
                            unregisterReceiver(mGattUpdateReceiver);
                            unbindService(mServiceConnection);
                            finish();
                            startActivity(intent);
                        }
                        break;
                    case 2:
                        //coming back from the read page
                        for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); ++i) {
                            getFragmentManager().popBackStack();
                        }
                        if (mConnected) {
                            mBluetoothLeService.writeCharacteristic(characteristicTX, hexData.QUARY);
                            mBluetoothLeService.readCharacteristic(characteristicRX);
                            Update_UI_state(2);
                        } else {
                            Intent intent = getIntent();
                            unregisterReceiver(mGattUpdateReceiver);
                            unbindService(mServiceConnection);
                            finish();
                            startActivity(intent);
                        }
                        break;
                    case 3:
                        for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); ++i) {
                            getFragmentManager().popBackStack();
                        }
                        if (mConnected) {
                            mBluetoothLeService.writeCharacteristic(characteristicTX, hexData.QUARY);
                            mBluetoothLeService.readCharacteristic(characteristicRX);
                            Update_UI_state(2);
                        } else {
                            Intent intent = getIntent();
                            unregisterReceiver(mGattUpdateReceiver);
                            unbindService(mServiceConnection);
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
                            Intent intent = getIntent();
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
                            Intent intent = getIntent();
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
                            Intent intent = getIntent();
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
                ones = 0;
                break;
            case 2:
                //after connection to a logger over BLE
                Log.i("YAS", "UI UPDATE STATE " + baseCMD.state);
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
                ones = 0;
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
        if(!mUSBConnected)
            Q_data = baseCMD.CMDQuery(commsSerial.ReadByte(data));
        else
            Q_data = baseCMD.CMDQuery(commsSerial.ReadUSBByte(data));
        if (Q_data.size() >= 7) {
            state.setText(QS.GetState(Integer.parseInt(Q_data.get(5))));
            state.setTypeface(font);
            serial.setText(Q_data.get(0));
            serial.setTypeface(font);
            battery.setText(Q_data.get(6)+"%");
            battery.setTypeface(font);
            if (mConnected) {
                type.setText("BLE Active");
                connecttype.setImageDrawable(getResources().getDrawable(R.drawable.ic_bluetooth_logo));

            } else if(mUSBConnected){
                type.setText("USB Active");
                connecttype.setImageDrawable(getResources().getDrawable(R.drawable.ic_usb_logo));
            }
            else type.setText("Inactive");
            type.setTypeface(font);
        } else {
            //Toast.makeText(MainActivity.this, "Invalid Data. Please go back." + Q_data.size(), Toast.LENGTH_SHORT).show();
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
                Update_UI_state(2);
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
        if ((mConnected || mUSBConnected) && FragmentNumber > 0) {//Fix fot the weired menu behaviour
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_stop).setVisible(false);
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
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        setListAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);

        //===============//
        if (m_usb == null)
        {
            m_usb = new USB(this);
        }
//===============//
        m_usb.Connection_Initialisation();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i("YO", "On Destroy !");
        m_usb.unregister_receiver(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(INTERNAL_BROADCAST_RECEIVER);
       // if (mGattUpdateReceiver != null && mServiceConnection != null) unregisterReceiver(mGattUpdateReceiver);
        //if (mServiceConnection != null) unbindService(mServiceConnection);

    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
    }

    private void setListAdapter(BaseAdapter baseAdapter) {
        ListView lvDevices = (ListView) findViewById(R.id.lvDevices);
        lvDevices.setAdapter(baseAdapter);
        lvDevices.setOnItemClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    List < Integer > selectedPositions = new ArrayList < >();

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mScanning = true;
            selectedPositions = new ArrayList < >();
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    @Override
    public void onItemClick(AdapterView < ?>parent, View view, int position, long id) {

        selectedPositions.add(new Integer(position));

        StringBuilder sb = new StringBuilder();
        for (Integer integer: selectedPositions) {
            sb.append(integer);
            sb.append(", ");
        }

        connectToDevices();
        spinnerProgressDialog();
    }

    //=============================================================================================//
    //trying to connect to a device when a device from the device list is pressed
    //=============================================================================================//
    private void connectToDevices() {
        final ArrayList < BluetoothDevice > devices = new ArrayList < >();
        for (Integer integer: selectedPositions) {
            BluetoothDevice device = mLeDeviceListAdapter.getDevice(integer);
            devices.add(device);
        }
        //stop scanning
        if (mScanning) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            scanLeDevice(false);
            mScanning = false;
        }
        mDevices = devices;

        selectedPositions.clear();
        getActionBar().setTitle(mDevices.get(0).getName());

        Intent gattServiceIntent = new Intent(MainActivity.this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

    }

    //=============================================================================================//
    // Code to manage Service lifecycle.
    //=============================================================================================//

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDevices.get(0).getAddress());
            Update_UI_state(2);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    //==============================================================================================
    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    //==============================================================================================
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                mainmenu.findItem(R.id.menu_scan).setVisible(false);
                invalidateOptionsMenu();
                //BuildDialogue("test", "set to false");
                send_to_active_Fragment(R.string.connected, 1);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                invalidateOptionsMenu();
                mConnected = false;
                send_to_active_Fragment(R.string.disconnected, 0);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                displayGattServices(mBluetoothLeService.getSupportedGattServices());

                mBluetoothLeService.setCharacteristicNotification(characteristicTX, true);
                mBluetoothLeService.setCharacteristicNotification(characteristicRX, true);
                mBluetoothLeService.writeCharacteristic(characteristicTX, hexData.STAY_UP);
                //SystemClock.sleep(1000);
                mBluetoothLeService.writeCharacteristic(characteristicTX, hexData.QUARY);
                mBluetoothLeService.readCharacteristic(characteristicRX);
                Log.i("BLE", "Coming to send query in the braocast receiver");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                String extraUuid = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
                int extraType = intent.getIntExtra(BluetoothLeService.EXTRA_TYPE, -1);
                extraData = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                Log.d("TAG", "extra data size " + extraData.length);
                commsSerial.BytetoHex(extraData);
                send_to_active_Fragment(extraData);
            }
        }
    };

    //=============================================================================================//
    // MAIN LOCAL BROADCAST RECEIVER USB
    //=============================================================================================//
    public BroadcastReceiver INTERNAL_BROADCAST_RECEIVER = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if (intent.getPackage() != null) {
                switch (intent.getPackage()) {
                    //-------------------//
                    case "DETACHED":
                        mUSBConnected = false;
                        Intent i = getIntent();
                        m_usb.unregister_receiver(getApplicationContext());
                        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(INTERNAL_BROADCAST_RECEIVER);
                        finish();
                        startActivity(i);

                        //-------------------//
                        //DO SOMETHING WHEN DETACHED
                        break;
                    //-------------------//
                    case "UI_update":
                        //-------------------//
                        if (extras != null) {
                            String message = extras.getString("message");


                            switch (message){
                                case "U06 <- USB CONNECTED !":
                                    spinnerProgressDialog();
                                    scanLeDevice(false);
                                    mScanning = false;
                                    mUSBConnected = true;
                                   // Update_UI_state(2);
                                    m_usb.Send_Command(HexData.QUARY_USB);
                                    invalidateOptionsMenu();
                                    break;
                            }
                        }
                        break;
                    //-------------------//
                    //-------------------//
                    case "HID_USB_Message_Received":
                        //-------------------//
                        if (extras != null) {
                            byte[] i_message = extras.getByteArray("b_data");
                            //commsSerial.BytetoHex(i_message);
                            if (i_message != null) {
                                send_to_active_Fragment(i_message);
                                //DO SOMETHING WITH DATA RECEIVED FROM USB
                            }
                        }
                        break;
                    //-------------------//
                }
            }
        }
    };//==========================================================//
//</editor-fold>

    //=============================================================================================//
    //function of the BLEFragmentI interface
    //=============================================================================================//
    @Override
    public void onBLERead() {
        try {
            mBluetoothLeService.readCharacteristic(characteristicRX);
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
            mBluetoothLeService.writeCharacteristic(characteristicTX, value);
        } catch(Exception ignore) {
            ignore.printStackTrace();
        }
    }

    //=============================================================================================//
    //function of the BLEFragmentI interface
    //=============================================================================================//
    @Override
    public boolean onConnectionchange() {
        return mConnected;
    }

    //=============================================================================================//
    //function of the BLEFragmentI interface
    //=============================================================================================//
    @Override
    public void BLEDisconnect() {
        //unregisterReceiver(mGattUpdateReceiver);
        //        unbindService(mServiceConnection);
        mBluetoothLeService.disconnect();
    }

    //=============================================================================================//
    //function of the USBFragmentI interface
    //=============================================================================================//
    @Override
    public void onUSBWrite(byte[] value) {
        m_usb.Send_Command(value);
    }

    //=============================================================================================//
    //function of the USBFragmentI interface
    //=============================================================================================//
    @Override
    public boolean USBDisconnect() {
        return mUSBConnected;
    }

    //==============================================================================================
    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    //==============================================================================================
    private void displayGattServices(List < BluetoothGattService > gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        ArrayList < HashMap < String,
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
            characteristicTX = gattService.getCharacteristic(UUID_CHARACTERISTIC_FIFO);
            characteristicRX = gattService.getCharacteristic(UUID_CHARACTERISTIC_FIFO);
        }

    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {@Override
            public void run() {

                mLeDeviceListAdapter.addDevice(device, rssi);
                if (mLeDeviceListAdapter.getCount() == 0) getActionBar().setTitle("Searching Devices..");
                else if (mLeDeviceListAdapter.getCount() > 0) {
                    getActionBar().setTitle("Available Devices");
                }

            }
            });
        }
    };



    static class ViewHolder {
        TextView deviceName;
        TextView deviceRssi;
        ImageView image;
        TextView deviceAddress;
    }

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList < BluetoothDevice > mLeDevices;
        private LayoutInflater mInflator;

        private HashMap < BluetoothDevice,
                Integer > mDevicesRssi = new HashMap < >();

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList < BluetoothDevice > ();
            mInflator = MainActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device, int rssi) {
            if (mDevicesRssi.containsKey(device)) {
                int oldRssi = mDevicesRssi.get(device);
                if (Math.abs(oldRssi - rssi) > 10) {
                    mDevicesRssi.put(device, rssi);
                    notifyDataSetChanged();
                }
            } else {
                mDevicesRssi.put(device, rssi);
                notifyDataSetChanged();
            }
            if (!mLeDevices.contains(device)) {
                if (device.getName() != null && device.getName().length() > 0) {
                    mLeDevices.add(device);
                    notifyDataSetChanged();
                }

            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            BluetoothDevice device = mLeDevices.get(i);
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                //viewHolder.deviceRssi = (TextView) view.findViewById(R.id.device_rssi);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                viewHolder.image = (ImageView) view.findViewById(R.id.strength);
                //                viewHolder.image.getLayoutParams().height = 100;
                //                viewHolder.image.getLayoutParams().width = 100;
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            final View finalView = view;
            view.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) {
                onItemClick(null, finalView, i, i);
            }
            });

            final String deviceName = device.getName();
            final String deviceAddress = device.getAddress();
            if (deviceName != null && deviceName.length() > 0) viewHolder.deviceName.setText(deviceName);
            else viewHolder.deviceName.setText(R.string.unknown_device);

            viewHolder.deviceAddress.setText(deviceAddress);
            if (mDevicesRssi.get(device) < -90) {
                viewHolder.image.setBackground(getDrawable(R.drawable.level1));

            } else if (mDevicesRssi.get(device) < -85) {
                viewHolder.image.setBackground(getDrawable(R.drawable.level2));
            } else if (mDevicesRssi.get(device) < -55) {
                viewHolder.image.setBackground(getDrawable(R.drawable.level3));
            } else if (mDevicesRssi.get(device) < 0) {
                viewHolder.image.setBackground(getDrawable(R.drawable.level4));
            }

            return view;
        }
    }

    //=============================================================================================//
    //Spinner dialog thats show when trying to connect to a device
    //=============================================================================================//
    private void spinnerProgressDialog() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting.....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Retry connecting", new DialogInterface.OnClickListener() {@Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            appStartState();
        }
        });
        progressDialog.show();
    }

    //=============================================================================================//
    //Going back to the initial state of the app
    //=============================================================================================//
    private void appStartState(){
        Intent intent = getIntent();
        Log.d("TAG", mConnected + " m connected value");
        if ((mConnected == true)) {
            mBluetoothLeService.writeCharacteristic(characteristicTX, hexData.GO_TO_SLEEP);
            Log.d("TAG", "Sending to sleep");
            SystemClock.sleep(1000);
        }
        if (mGattUpdateReceiver != null) unregisterReceiver(mGattUpdateReceiver);

        if (mServiceConnection != null) unbindService(mServiceConnection);
        finish();
        startActivity(intent);
    }

}