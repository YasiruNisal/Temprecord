package com.example.yasiruw.temprecord.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.yasiruw.temprecord.R;
import com.example.yasiruw.temprecord.activities.MainActivity;
import com.example.yasiruw.temprecord.comms.BLEFragmentI;
import com.example.yasiruw.temprecord.comms.BaseCMD;
import com.example.yasiruw.temprecord.comms.CommsSerial;
import com.example.yasiruw.temprecord.comms.MT2Msg_Read;
import com.example.yasiruw.temprecord.comms.MT2Values;
import com.example.yasiruw.temprecord.comms.QueryStrings;
import com.example.yasiruw.temprecord.services.BluetoothLeService;
import com.example.yasiruw.temprecord.services.StoreKeyService;
import com.example.yasiruw.temprecord.utils.CommsChar;
import com.example.yasiruw.temprecord.utils.HexData;
import com.github.mikephil.charting.charts.LineChart;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;


public class BLEReadFragment extends Fragment {

    private static final int PROGRESSBAR_MAX = 300;

//==============================UI ELEMENTS=========================================================
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

    private WebView Graph1;
    private WebView Graph2;


    private LinearLayout Humidity;
    private LinearLayout Humidity2;
    private ScrollView scrollView;

    private ProgressDialog progress;
    private int progresspercentage;
    private int progressincrement;
    StoreKeyService storeKeyService;

    //==============================================================================================
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

    private Handler handler1 =new Handler();
    public static final Handler mainThreadHandler = new Handler();
    Runnable delayedTask;
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";


    BLEFragmentI bleFragmentI;

    public BLEReadFragment GET_INSTANCE(Bundle data)
    {
        BLEReadFragment fragment = new BLEReadFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            bleFragmentI = (BLEFragmentI) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IServiceFragmentInteraction");
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }
    //==========================================================//

    //==========================================================//
    @Override
    public void onResume()
    {
        super.onResume();
    }
    //==========================================================//

    //==========================================================//
    @Override
    public void onPause()
    {
        super.onPause();
    }
    //==========================================================//

    //==========================================================//
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_bleread, container, false);
        getActivity().closeContextMenu();
        getActivity().getActionBar().show();
        getActivity().getActionBar().setTitle("Read Logger");
        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));

        AssetManager am = getActivity().getAssets();


        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
        Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Medium.ttf");


//        m.setText(message);
        // Sets up UI references.
        //((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
        bat = (ImageView) view.findViewById(R.id.imageView1);
        temp = (ImageView) view.findViewById(R.id.imageView2);
        hu = (ImageView) view.findViewById(R.id.imageView3);

        currentTemp = (TextView) view.findViewById(R.id.temperature);
        currentTemp.setTypeface(font);
        currenthumidity = (TextView) view.findViewById(R.id.humiditytop);
        currenthumidity.setTypeface(font);
        time = (TextView) view.findViewById(R.id.time);
        time.setTypeface(font);
        mConnectionState = (TextView) view.findViewById(R.id.connection_state);
        Lstate = (TextView) view.findViewById(R.id.state);
        Lstate.setTypeface(font);
        battery = (TextView) view.findViewById(R.id.battery);
        battery.setTypeface(font);
        family = (TextView) view.findViewById(R.id.family);
        model = (TextView) view.findViewById(R.id.model);
        firmware = (TextView) view.findViewById(R.id.firmware);
        serialno = (TextView) view.findViewById(R.id.serialno);
        memory = (TextView) view.findViewById(R.id.memory);

        TripSamples = (TextView) view.findViewById(R.id.tripsamples);
        TotalLoggedTime = (TextView) view.findViewById(R.id.totalloggedtime);
        LoggedSamples = (TextView)view.findViewById(R.id.loggedsamples);
        LoggedTags = (TextView) view.findViewById(R.id.loggedtags);
        FirstSample = (TextView) view.findViewById(R.id.firstsample);
        FirstLoggedSample = (TextView) view.findViewById(R.id.firstloggedsample);
        LastSample = (TextView) view.findViewById(R.id.lastsample);
        LastLoggedSample = (TextView) view.findViewById(R.id.lastloggedsample);

        Humidity = (LinearLayout) view.findViewById(R.id.humidity);
        Humidity2 = (LinearLayout) view.findViewById(R.id.humidity2);
        scrollView = (ScrollView) view.findViewById(R.id.scroll);


        channel1 = (TextView) view.findViewById(R.id.channel1);
        upperLimit1 = (TextView) view.findViewById(R.id.upperlimit1);
        lowerLimit1 = (TextView) view.findViewById(R.id.lowerlimit1);
        mean1 = (TextView) view.findViewById(R.id.mean1);
        max1 = (TextView) view.findViewById(R.id.max1);
        min1 = (TextView) view.findViewById(R.id.min1);
        mkt = (TextView) view.findViewById(R.id.mkt1);

        totalSampleswithinLimits1 = (TextView) view.findViewById(R.id.totalwithinlimits);
        totalTimewithinLimits1 = (TextView) view.findViewById(R.id.totaltimewithinlimits);
        totalPercentagewitinLimits1 = (TextView) view.findViewById(R.id.totslpercentagewithinlimits);
        totalSaamplesoutofLimits1 = (TextView) view.findViewById(R.id.totalsamplesoutoflimits);
        totalTimeoutofLImits1 = (TextView) view.findViewById(R.id.totaltimeoutoflimits);
        totalPercentageoutofLimits1 = (TextView) view.findViewById(R.id.totalpercentageoutoflimits);
        samplesaboveUpperLimit1 = (TextView) view.findViewById(R.id.samplesaboveupperlimit);
        timeaboveUpperLimit1 = (TextView) view.findViewById(R.id.timeaboveupperlimit);
        percentageaboveUpperLimit1 = (TextView) view.findViewById(R.id.percentageaboveupperlimit);
        samplesbelowLowerLimit1 = (TextView) view.findViewById(R.id.samplesbelowlowerlimit);
        timebelowLowerSamples1 = (TextView) view.findViewById(R.id.timebelowlowerlimit);
        percentagebelowLowerSample1 = (TextView) view.findViewById(R.id.percentagebelowlowerlimit);


        channel2 = (TextView) view.findViewById(R.id.channel2);
        upperLimit2 = (TextView) view.findViewById(R.id.upperlimit2);
        lowerLimit2 = (TextView) view.findViewById(R.id.lowerlimit2);
        mean2 = (TextView) view.findViewById(R.id.mean2);
        max2 = (TextView) view.findViewById(R.id.max2);
        min2 = (TextView) view.findViewById(R.id.min2);


        totalSampleswithinLimits2 = (TextView) view.findViewById(R.id.totalwithinlimits2);
        totalTimewithinLimits2 = (TextView) view.findViewById(R.id.totaltimewithinlimits2);
        totalPercentagewitinLimits2 = (TextView) view.findViewById(R.id.totslpercentagewithinlimits2);
        totalSaamplesoutofLimits2 = (TextView) view.findViewById(R.id.totalsamplesoutoflimits2);
        totalTimeoutofLImits2 = (TextView) view.findViewById(R.id.totaltimeoutoflimits2);
        totalPercentageoutofLimits2 = (TextView) view.findViewById(R.id.totalpercentageoutoflimits2);
        samplesaboveUpperLimit2 = (TextView) view.findViewById(R.id.samplesaboveupperlimit2);
        timeaboveUpperLimit2 = (TextView) view.findViewById(R.id.timeaboveupperlimit2);
        percentageaboveUpperLimit2 = (TextView) view.findViewById(R.id.percentageaboveupperlimit2);
        samplesbelowLowerLimit2 = (TextView) view.findViewById(R.id.samplesbelowlowerlimit2);
        timebelowLowerSamples2 = (TextView) view.findViewById(R.id.timebelowlowerlimit2);
        percentagebelowLowerSample2 = (TextView) view.findViewById(R.id.percentagebelowlowerlimit2);

        zoomin = (ImageButton) view.findViewById(R.id.zoominButton);
        zoomin1 = (ImageButton) view.findViewById(R.id.zoominButton1);

        Graph1 = (WebView) view.findViewById(R.id.graphone);

        Graph2 = (WebView) view.findViewById(R.id.graphtwo);

        progressDialoge();

        Log.d("TAG", "am i coming to this place 11  " + bleFragmentI);
        bleFragmentI.onBLEWrite(HexData.BLE_ACK);
        bleFragmentI.onBLERead();

        return view;
    }

    double[] data = new double[] {42.6, 24, 17, 15.4};

    /** This passes our data out to the JS */
    @JavascriptInterface
    public String getData() {
        Log.d(TAG, "getData() called");
        return a1dToJson(data).toString();
    }

    private String a1dToJson(double[] data) {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (int i = 0; i < data.length; i++) {
            double d = data[i];
            if (i > 0)
                sb.append(",");
            sb.append(d);
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_simple, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_about:
                //sendEmail();
                return true;
                default:
                    return false;
        }


    }

    public void updateConnectionState(final int resourceId, final int num) {
        getActivity().runOnUiThread(new Runnable() {
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


    public void CommsI(final byte[] in){


        Runnable runnableCode = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                byte[] query;

                if (storeKeyService.getDefaults("SOUND", getActivity().getApplication()) != null && storeKeyService.getDefaults("SOUND", getActivity().getApplication()).equals("1"))
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
                            bleFragmentI.onBLEWrite(HexData.GO_TO_SLEEP);
                            bleFragmentI.onBLERead();
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
                        bleFragmentI.onBLEWrite(HexData.QUARY);
                        bleFragmentI.onBLERead();
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
                        bleFragmentI.onBLEWrite(HexData.GO_TO_SLEEP);
                        bleFragmentI.onBLERead();
                        break;
                    case 8:
                        hexData.BytetoHex(in);
                        query = commsSerial.ReadByte(in);
                        Q_data = baseCMD.CMDQuery(query);


                        //sets up reading the first 120 bytes from flash, then the message is constructed with 0x55 and stuff
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_TRW, 0, 120, 120, 3);
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));
                        bleFragmentI.onBLERead();
                        //sendData(mt2Msg_read.Read_into_writeByte(false));
                        state = 9;
                        break;
                    case 9:// reads the 120 bytes till write_into_readbyte returns true. then the state is changed to read the next 24 bytes
                        if(mt2Msg_read.write_into_readByte(commsSerial.ReadByte(in))){
                            state = 10;
                            System.arraycopy(mt2Msg_read.memoryData, 0, TWFlash, 0, 120);
                            //TWFlash = mt2Msg_read.memoryData;

                        }
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        bleFragmentI.onBLERead();
                        break;
                    case 10://sets up to read the next 24 bytes
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_TRW, 120, 144, 24, 3);
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));
                        bleFragmentI.onBLERead();
                        //sendData(mt2Msg_read.Read_into_writeByte(false));
                        state = 11;
                        break;
                    case 11://next 24 bytes are read and appened to the TWFlassh byte array. Then moving on to reading user flash
                        if(mt2Msg_read.write_into_readByte(commsSerial.ReadByte(in))){
                            state = 12;
                            System.arraycopy(mt2Msg_read.memoryData, 0, TWFlash, 120, 24);
                            hexData.BytetoHex(TWFlash);
                            F_data = baseCMD.CMDFlash(TWFlash);

                            // call the decoding functon here to decode all we need and send the data back in an string array so that they can be desplayed in appropriate textviews
                        }
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        bleFragmentI.onBLERead();
                        break;
                    case 12:
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_USER, 0, 250, 250, 3);
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));
                        bleFragmentI.onBLERead();
                        state = 13;
                        break;
                    case 13:
                        if(mt2Msg_read.write_into_readByte(commsSerial.ReadByte(in))) {
                            state = 14;
                            System.arraycopy(mt2Msg_read.memoryData, 0, UserRead, 0, 250);
                            //hexData.BytetoHex(TWFlash);
                        }
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        bleFragmentI.onBLERead();
                        break;
                    case 14:
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_RAM, 0, 100, 100, 3);
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));
                        bleFragmentI.onBLERead();
                        state = 15;
                        break;
                    case 15:
                        if(mt2Msg_read.write_into_readByte(commsSerial.ReadByte(in))) {

                            System.arraycopy(mt2Msg_read.memoryData, 0, RamRead, 0, 100);
                            hexData.BytetoHex(RamRead);
                            R_data = baseCMD.CMDRamRead(RamRead);
                            state = 16;
                        }
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        bleFragmentI.onBLERead();
                        break;
                    case 16:
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_USER, 250, 500, 250, 3);
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));
                        bleFragmentI.onBLERead();
                        state = 17;
                        break;
                    case 17:
                        if(mt2Msg_read.write_into_readByte(commsSerial.ReadByte(in))) {
                            state = 18;
                            System.arraycopy(mt2Msg_read.memoryData, 0, UserRead, 250, 250);
                            //hexData.BytetoHex(TWFlash);
                        }
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        bleFragmentI.onBLERead();
                        break;

                    case 18:
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_USER, 500, 512, 250, 3);
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));
                        bleFragmentI.onBLERead();
                        state = 19;
                        break;
                    case 19:
                        if(mt2Msg_read.write_into_readByte(commsSerial.ReadByte(in))) {
                            state = 24;
                            System.arraycopy(mt2Msg_read.memoryData, 0, UserRead, 500, 12);
                            hexData.BytetoHex(UserRead);
                            U_data = baseCMD.CMDUserRead(UserRead);
                            /// SetUI();
                        }
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        bleFragmentI.onBLERead();
                        break;
                    case 20:
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_EXTRA, 114, 350, 300, 3);
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));
                        bleFragmentI.onBLERead();
                        state = 21;
                        break;
                    case 21:
                        if(mt2Msg_read.write_into_readByte(commsSerial.ReadByte(in))) {
                            state = 22;
                            System.arraycopy(mt2Msg_read.memoryData, 0, ExtraRead, 0, 236);
                            //hexData.BytetoHex(TWFlash);
                        }
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        bleFragmentI.onBLERead();
                        break;
                    case 22:
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_EXTRA, 350, 398, 300, 3);
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));
                        bleFragmentI.onBLERead();
                        state = 23;
                        break;
                    case 23:
                        if(mt2Msg_read.write_into_readByte(commsSerial.ReadByte(in))) {
                            state = 24;
                            System.arraycopy(mt2Msg_read.memoryData, 0, ExtraRead, 236, 48);
                            firsttime++;
                            hexData.BytetoHex(ExtraRead);

                        }
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        bleFragmentI.onBLERead();
                        break;
                    case 24:// the set up needs to change if loop over right is active.

                        if(baseCMD.numberofsamples == 0) {
                            if(!(baseCMD.state == 4 || baseCMD.state == 5)){
                                BuildDialogue("No Data","Logger is not in running or stop state to display data", 1);
                            }else
                                SetUI();
                            progresspercentage = 100;
                            progress.cancel();
                            bleFragmentI.onBLEWrite(HexData.BLE_ACK);
                            bleFragmentI.onBLERead();
                            state = 29;
                        }else {
                            mt2Msg_read = new MT2Msg_Read(commsChar.MEM_VAL, 0, baseCMD.SamplePointer *2, 120, 3);
                            bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));
                            bleFragmentI.onBLERead();
                            if(baseCMD.LoopOverwrite && !baseCMD.isLoopOverwriteOverFlow){state = 26;}else{state = 25;}
                        }
                        break;
                    case 25://
                        if (mt2Msg_read.write_into_readByte(commsSerial.ReadByte(in))) {
                            state = 29;
                            byte[] ValueRead = new byte[mt2Msg_read.memoryData.length];
                            System.arraycopy(mt2Msg_read.memoryData, 0, ValueRead, 0, mt2Msg_read.memoryData.length);
                            //hexData.BytetoHex(ValueRead);
                            MT2ValueIn(ValueRead);
                            //addDatatoGraph();
                            if(!(baseCMD.state == 4 || baseCMD.state == 5)){
                                BuildDialogue("No Data","Logger is not in running or stop state to display data", 1);
                            }else
                                SetUI();
                            progresspercentage = 100;
                            progress.cancel();
                        }
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        bleFragmentI.onBLERead();
                        break;
                    case 26:
                        if (mt2Msg_read.write_into_readByte(commsSerial.ReadByte(in))) {
                            state = 27;
                            ReadValues1 = new byte[mt2Msg_read.memoryData.length];
                            System.arraycopy(mt2Msg_read.memoryData, 0, ReadValues1, 0, mt2Msg_read.memoryData.length);
                            //hexData.BytetoHex(ValueRead);
                        }
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        bleFragmentI.onBLERead();
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
                        bytesToRead = totalToRead - baseCMD.SamplePointer *2;
                        Log.d(TAG, "OOOOOOOOOOOOOOOOOOOOOO " + address + " " + bytesToRead + " "+ totalToRead + " " + pageOffset +" " + baseCMD.SamplePointer *2);

                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_VAL, baseCMD.SamplePointer *2, bytesToRead, 120, 3);
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));
                        bleFragmentI.onBLERead();
                        state = 28;
                        break;
                    case 28:
                        if (mt2Msg_read.write_into_readByte(commsSerial.ReadByte(in))) {
                            state = 29;
                            ReadValues2 = new byte[mt2Msg_read.memoryData.length];
                            byte[] combo = new byte[ReadValues2.length  + ReadValues1.length];
                            System.arraycopy(mt2Msg_read.memoryData, 0, ReadValues2, 0, mt2Msg_read.memoryData.length);
                            System.arraycopy(ReadValues2,0, combo,0,ReadValues2.length-1);
                            System.arraycopy(ReadValues1,0,combo,ReadValues2.length,ReadValues1.length);
                            //hexData.BytetoHex(ReadValues);
                            MT2ValueIn(combo);
                            //addDatatoGraph();
                            if(!(baseCMD.state == 4 || baseCMD.state == 5)){
                                BuildDialogue("No Data","Logger is not in running or stop state to display data", 1);
                            }else
                                SetUI();
                            progresspercentage = 100;
                            progress.cancel();
                        }
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        bleFragmentI.onBLERead();
                        break;
                    case 29:
                        FifteenSecTimeout();
                        break;


                }
            }
        };handler1.postDelayed(runnableCode,1);
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



        if(!baseCMD.ch2Enable){
            Humidity.setVisibility(LinearLayout.GONE);
            Humidity2.setVisibility(LinearLayout.GONE);
        }

        if(!baseCMD.ch2Enable){
            currenthumidity.setText("--");
        }


    }

    public void plotGraph1(){
        Graph1.getSettings().setJavaScriptEnabled(true);
        Graph1.addJavascriptInterface(this,"android");
        Graph1.requestFocusFromTouch();
        Graph1.setWebViewClient(new WebViewClient());
        Graph1.setWebChromeClient(new WebChromeClient());
        Graph1.loadUrl("file:///android_asset/canvasJS.html");
    }

    public void plotGraph2(){
        Graph2.getSettings().setJavaScriptEnabled(true);
        Graph2.addJavascriptInterface(this,"android");
        Graph2.requestFocusFromTouch();
        Graph2.setWebViewClient(new WebViewClient());
        Graph2.setWebChromeClient(new WebChromeClient());
        Graph2.loadUrl("file:///android_asset/canvasJS.html");

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
    private void FifteenSecTimeout(){
        delayedTask = new Runnable() {
            @Override
            public void run() {

                bleFragmentI.onBLEWrite(HexData.GO_TO_SLEEP);
                try {
                    TimeUnit.MILLISECONDS.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("TAG", "15 sec time out");
                if(getFragmentManager() == null){
                    bleFragmentI.BLEDisconnect();
//                    unbindService(mServiceConnection);

                }
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        mainThreadHandler.postDelayed(delayedTask, 20000);

    }

    private void BuildDialogue(String str1, String str2, final int press){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle(str1)
                .setMessage(str2)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(press == 1){
                            Intent intent = new Intent(getActivity(), MainActivity.class);
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

        progress=new ProgressDialog(getActivity());
        progress.setMessage("Reading Logger");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(false);
        progress.setProgress(0);
        progress.setCancelable(false);
        progress.setButton(DialogInterface.BUTTON_NEGATIVE, "Abort", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bleFragmentI.onBLEWrite(hexData.BLE_ACK);
                bleFragmentI.onBLERead();
                state =29;
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




}
