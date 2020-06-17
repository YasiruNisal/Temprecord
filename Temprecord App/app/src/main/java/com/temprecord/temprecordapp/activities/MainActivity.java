package com.temprecord.temprecordapp.activities;


import android.Manifest;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.temprecord.temprecordapp.services.StoreKeyService;
import com.temprecord.temprecordapp.services.Temprecord_BLE;
import com.temprecord.temprecordapp.R;
import com.temprecord.temprecordapp.comms.BLEFragmentI;
import com.temprecord.temprecordapp.comms.BaseCMD;
import com.temprecord.temprecordapp.comms.CommsSerial;
import com.temprecord.temprecordapp.CustomLibraries.Yasiru_Temp_Library;
import com.temprecord.temprecordapp.comms.USBFragmentI;
import com.temprecord.temprecordapp.fragments.USBQueryFragment;
import com.temprecord.temprecordapp.services.BluetoothLeService;
import com.temprecord.temprecordapp.services.USB;
import com.temprecord.temprecordapp.utils.GattAttributes;
import com.temprecord.temprecordapp.comms.HexData;



import java.util.ArrayList;
import java.util.TimeZone;
import java.util.UUID;

import static com.temprecord.temprecordapp.fragments.USBQueryFragment.EXTRAS_MESSAGE;


public class MainActivity extends FragmentActivity implements
        BLEFragmentI, USBFragmentI {
    //=================Bluetooth service variables==================================================
    private static final String ACTION_USB_ATTACHED  = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    private static boolean mConnected = false;
    private static boolean mUSBConnected = false;
    public BluetoothGattCharacteristic characteristicTX;
    public BluetoothGattCharacteristic characteristicRX;
    public final static UUID UUID_CHARACTERISTIC_FIFO = UUID.fromString(GattAttributes.UUID_CHARACTERISTIC_FIFO);


    //============Bluetooth device activity variables===============================================

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
    public int mode = 1;// BLE no  and USB yes  0 = BLE yes  and USB yes
    private android.support.v4.app.FragmentManager fragmentManager;


    //====================layout variables==============================================================
    LinearLayout r1;
    LinearLayout r2;
    LinearLayout r3;
    LinearLayout r4;
    LinearLayout info;
    LinearLayout list;
    LinearLayout con_info;
    FrameLayout fragment_container;
    LinearLayout menubuttons;
    LinearLayout history;
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
    WebView web_gif;
    ListView lvDevices;
    FragmentTransaction transaction;

    TextView more;
    LinearLayout queryloggerdetails;




    private Bundle m_bundle_data = new Bundle();
    //========================Fragment Initialisation===============================================
    private int FragmentNumber = 0;
    private USBQueryFragment usbQueryFragment = new USBQueryFragment();


    //==============================================================================================

    //Classes
    //==========================================================//
    USB m_usb;
    public  static  int     m_next_memory_address   = 0;
    //==========================================================//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.i("TEST", "|||||||||||||||||||||||||||||||||||ONCREATE ||||||||||||||||||||||||| ");
        setContentView(R.layout.activity_main);
        r1 =  findViewById(R.id.r1);
        info = findViewById(R.id.info);
        menubuttons =  findViewById(R.id.Menubuttons);
        fragment_container =  findViewById(R.id.Fragment_Container);
        state =  findViewById(R.id.state);
        battery =  findViewById(R.id.battery);
        parametertext =  findViewById(R.id.parametertext);
        readtext =  findViewById(R.id.readtext);
        scroll = findViewById(R.id.screen);
        web_gif = findViewById(R.id.web_gif);
        getActionBar().hide();
        Update_UI_state(1);
        web_gif.loadUrl("file:///android_asset/gif.html");
        web_gif.getSettings().setLoadWithOverviewMode(true);
//        web_gif.getSettings().setUseWideViewPort(true);


        more = findViewById(R.id.more);

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

        //QS.LocaltoUTC();
        fragmentManager = getSupportFragmentManager();
        //Log.i("TEST", "++++++++++++++++++++++++++++++++++++++++++++++ " + usbQueryFragment);

        //Log.i("TEST", "++++++++++++++++++++++++++++++++++++++++++++++ " + usbQueryFragment);
        Update_UI_state(1);
        StoreKeyService.setDefaults("UNITS", "1", getApplicationContext());
        int requestcode = 0;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},requestcode);
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
                    case 4:
                        if(mUSBConnected) {
                            for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                                fragmentManager.popBackStack();
                            }
                            this.finish();
                        }else
                            Update_UI_state(1);
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
                info.setVisibility(View.VISIBLE);
                fragment_container.setVisibility(View.GONE);
                main_state = 1;
                break;
            case 2:
                info.setVisibility(View.GONE);
                fragment_container.setVisibility(View.VISIBLE);
                main_state = 2;
                break;
        }
        invalidateOptionsMenu();
    }

    //=============================================================================================//
    //Settings which buttons are visible at different states
    //=============================================================================================//



    //=============================================================================================//
    //Filling in the data in the Blue banner
    //logger state, serial number and  connection status
    //=============================================================================================//

    //=============================================================================================//
    //Sending the required data to the active fragment by calling the CommsI function in each
    //fragment
    //=============================================================================================//
    public void send_to_active_Fragment(byte[] data) {

        switch (FragmentNumber) {
            case 0:
                //still in the main activity so access the UI elements directly=

                //fillBlueBanner(data);
                if(mConnected || mUSBConnected)
                    Update_UI_state(2);// updates the buttons concidering what state the logger is in
                if(mConnected) {
                    //BLEmainTimeout();

                    temprecord_ble.stopRunable();
                }
                if(progressDialog!=null)
                    progressDialog.cancel();
                break;
            case 1:
                //query page(BLE)
                //bleQueryFragment.CommsI(data);
                break;
            case 2:
                //read page(BLE)
                //bleReadFragment.CommsI(data);
                break;
            case 3:
                //parameter page(BLE)
                //bleParameterFragment.CommsI(data);
                break;
            case 4:
                //USB query page
                usbQueryFragment.CommsI(data);
                break;
            case 5:
                //USB read page
                //usbReadFragment.CommsI(data);
                break;
            case 6:
                //usbParameterFragment.CommsI(data);
                break;
        }
    }


    //=============================================================================================//
    //Used to update the connection status of the fragment if a timeout occurs while in that
    //fragment
    //=============================================================================================//
    public void send_to_active_Fragment(boolean disconnected) {
        switch (FragmentNumber) {
            case 4:
                usbQueryFragment.updateConnectionState(disconnected);
                //still in the main activity so access the UI elements directly
                break;
//            case 1:
//                //query page
//                bleQueryFragment.updateConnectionState(num);
//                break;
//            case 2:
//                //read page
//                bleReadFragment.updateConnectionState(num);
//                break;
//            case 3:
//                bleParameterFragment.updateConnectionState(num);
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
        if (main_state == 1)
            super.onBackPressed();
        //}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.i("TEST", "******************************************** Going to OnResume " + m_usb);
        if (m_usb == null) {
            m_usb = new USB(this);
            //Log.i("TEST", "******************************************** new USB object " + m_usb);
        }
        m_usb.Connection_Initialisation();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //Log.i("TEST", "******************************************** Going to OnDestroy |||||||||||||||||||||||||" + m_usb);
        if(m_usb != null)
            m_usb.unregister_receiver(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(INTERNAL_BROADCAST_RECEIVER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Log.i("TEST", "******************************************** Going to OnPause " + m_usb);
    }

    @Override
    protected  void onStop(){
        super.onStop();
        //Log.i("TEST", "******************************************** Going to OnStop " + m_usb);

    }


    @Override
    protected void onStart(){
        super.onStart();
        //Log.i("TEST", "******************************************** Going to OnStart " + m_usb);

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
        if(m_usb != null)
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


            //Log.d("TEST", "Coming in to  INTERNAL_BROADCAST_RECEIVER " + action );
            if (intent.getPackage() != null) {
                switch (intent.getPackage()) {

                    case "DETACHED":
                        send_to_active_Fragment(true);
                        //Log.i("TEST", "******************************************** Going to DETACHED======== " + m_usb);
//                        if(m_usb != null)
//                            m_usb.unregister_receiver(getApplication());
//                        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(INTERNAL_BROADCAST_RECEIVER);
//                        m_usb = null;
                        mUSBConnected = false;

                        break;

                    case "UI_update":
                        if (extras != null) {
                            String message = extras.getString("message");

                            switch (message){
                                case "U06 <- USB CONNECTED !":
                                    mUSBConnected = true;
                                    //Log.i("TEST", "******************************************** Going to CONNECTED======== " + m_usb);

                                    for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                                        fragmentManager.popBackStack();
                                    }
                                    Update_UI_state(2);
                                    m_bundle_data.putString(EXTRAS_MESSAGE, "1");
                                    usbQueryFragment = new USBQueryFragment().GET_INSTANCE(m_bundle_data);
                                    FragmentNumber = 4;
                                    fragmentManager.beginTransaction().add(R.id.Fragment_Container, usbQueryFragment, "Page1").addToBackStack(null).commit();
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

                            if (i_message != null) {

                                send_to_active_Fragment(i_message);

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
                        //send_to_active_Fragment(R.string.connected, 1);
                        break;

                    case BluetoothLeService.ACTION_GATT_DISCONNECTED:
                        invalidateOptionsMenu();
                        mConnected = false;
                        //send_to_active_Fragment(R.string.disconnected, 0);
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
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.Retry_connecting), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                temprecord_ble.stopRunable();
                //appStartState();
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
//        mainThreadHandler.postDelayed(delayedTask, 20000);
    }

    public void Read_Action(){
        if (mUSBConnected) {
//            Update_UI_state(3);
//            m_bundle_data.putString(EXTRAS_MESSAGE, "6"); //put string, int, etc in bundle with a key value
//            usbReadFragment = new USBReadFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
//            FragmentNumber = 5;
//            getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, usbReadFragment).commit();
        } else if(mConnected) {
            //Update_UI_state(3);
            //m_bundle_data.putString(EXTRAS_MESSAGE, "6"); //put string, int, etc in bundle with a key value
            //bleReadFragment = new BLEReadFragment().GET_INSTANCE(m_bundle_data); //Use bundle to pass data
            //FragmentNumber = 2;
            //getFragmentManager().beginTransaction().replace(R.id.Fragment_Container, bleReadFragment).commit();
        }
    }


   public  void HistoryButton(View view){
       // Log.i("TEST", "IN button" + getFragmentManager().getBackStackEntryCount() + " " + getFragmentManager().findFragmentByTag("USBQuery"));
        Update_UI_state(3);
        FragmentNumber = 4;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        getFragmentManager().beginTransaction().attach(getFragmentManager().findFragmentByTag("USBQuery")).commit();

    }

    public void setFragmentNumber(int fragmentNumber){
        this.FragmentNumber = fragmentNumber;
    }

}