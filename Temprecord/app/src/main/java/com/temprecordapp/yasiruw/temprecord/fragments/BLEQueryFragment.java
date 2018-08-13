package com.temprecordapp.yasiruw.temprecord.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.temprecordapp.yasiruw.temprecord.App;
import com.temprecordapp.yasiruw.temprecord.R;
import com.temprecordapp.yasiruw.temprecord.activities.GraphAcivity;
import com.temprecordapp.yasiruw.temprecord.activities.MainActivity;
import com.temprecordapp.yasiruw.temprecord.comms.BLEFragmentI;
import com.temprecordapp.yasiruw.temprecord.comms.BaseCMD;
import com.temprecordapp.yasiruw.temprecord.comms.CommsSerial;
import com.temprecordapp.yasiruw.temprecord.comms.MT2Msg_Read;
import com.temprecordapp.yasiruw.temprecord.CustomLibraries.Yasiru_Temp_Library;
import com.temprecordapp.yasiruw.temprecord.comms.MT2Values;
import com.temprecordapp.yasiruw.temprecord.services.Json_Data;
import com.temprecordapp.yasiruw.temprecord.services.StoreKeyService;
import com.temprecordapp.yasiruw.temprecord.comms.CommsChar;
import com.temprecordapp.yasiruw.temprecord.comms.HexData;
import com.temprecordapp.yasiruw.temprecord.services.Screenshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;


public class BLEQueryFragment extends Fragment {

    public static final String EXTRAS_MESSAGE = "0";

    private static final int PROGRESSBAR_MAX = 100;

    private ScrollView queryScroll;
    private FrameLayout mWrapperFL;
    private LinearLayout linearLayout;
    private LinearLayout st_at;
    private LinearLayout st_by;
    private LinearLayout sp_at;
    private LinearLayout sp_by;
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
    private TextView energysave;

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

    private TextView limitstatus;
    private ImageView limiticon;
    private ImageButton zoomin;

    private LinearLayout querygraphlayout;

    //==========================LoopOverWrite READ======
    int address;
    int bytesToRead;
    int totalToRead;
    int bytePointer;
    int pageOffset;
    byte[] ReadValues1;
    byte[] ReadValues2;
    Json_Data json_data;
    private WebView Graph1;
    MT2Values.MT2Mem_values mt2Mem_values = new MT2Values.MT2Mem_values();

    private byte[] TWFlash = new byte[144];
    private byte[] RamRead = new byte[100];
    private byte[] UserRead = new byte[512];
    private byte[] ExtraRead = new byte[284];
    private ArrayList<String> Q_data = new ArrayList<String>();
    private ArrayList<String> U_data = new ArrayList<String>();
    private ArrayList<String> F_data = new ArrayList<String>();
    private ArrayList<String> R_data = new ArrayList<String>();

    private String mDeviceName;
    private String mDeviceAddress;
    private String message = "0";

    private ProgressDialog progress;
    private int progresspercentage;
    private int timeoutdelay;
    private int firsttime = 0;
    private boolean soundon = true;
    StoreKeyService storeKeyService;

    ProgressTask task = new ProgressTask();

    private Handler handler1 =new Handler();
    Thread t = new Thread();
    private int state = 1;
    public boolean imperial;
    HexData hexData = new HexData();
    BaseCMD baseCMD =  new BaseCMD();
    CommsSerial commsSerial = new CommsSerial();
    MT2Msg_Read mt2Msg_read;
    Yasiru_Temp_Library QS = new Yasiru_Temp_Library();
    CommsChar commsChar = new CommsChar();
    public static final Handler mainThreadHandler = new Handler();
    Runnable delayedTask;

    BLEFragmentI bleFragmentI;

    public BLEQueryFragment GET_INSTANCE(Bundle data)
    {
        BLEQueryFragment fragment = new BLEQueryFragment();
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
    {   super.onPause();
        mainThreadHandler.removeCallbacksAndMessages(null);

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_blequery,container,false);

        getActivity().getActionBar().show();
        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));


        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
        Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Medium.ttf");


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            message = bundle.getString(EXTRAS_MESSAGE);
        }


        if(message.equals("1")){
            getActivity().getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_infoc));
            getActivity().getActionBar().setTitle(R.string.QueryLogger);
        }else if(message.equals("2")){
            getActivity().getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_startc));
            getActivity().getActionBar().setTitle(R.string.StartLogger);
        }else if(message.equals("3")){
            getActivity().getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_stopc));
            getActivity().getActionBar().setTitle(R.string.StopLogger);
        }else if(message.equals("4")){
            getActivity().getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_reusec));
            getActivity().getActionBar().setTitle(R.string.ReuseLogger);
        }else if(message.equals("5")){
            getActivity().getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_tagc));
            getActivity().getActionBar().setTitle(R.string.TagLogger);
        }else if(message.equals("8")){
            getActivity().getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_searchc));
            getActivity().getActionBar().setTitle(R.string.SearchLogger);
        }


        linearLayout = (LinearLayout) view.findViewById(R.id.attop);

        queryScroll = (ScrollView) view.findViewById(R.id.queryscroll);
//        m.setText(message);
        // Sets up UI references.
        //((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
        bat = (ImageView) view.findViewById(R.id.imageView1);
        temp = (ImageView) view.findViewById(R.id.imageView2);
        hu = (ImageView) view.findViewById(R.id.imageView3);

        limitstatus = view.findViewById(R.id.limitstatus);
        limiticon = view.findViewById(R.id.limiticon);

        st_at = view.findViewById(R.id.st_at);
        st_by = view.findViewById(R.id.st_by);
        sp_at = view.findViewById(R.id.sp_at);
        sp_by = view.findViewById(R.id.sp_by);
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
        manudate = (TextView) view.findViewById(R.id.manudate);
        sampleno = (TextView) view.findViewById(R.id.sampleno);
        trips = (TextView) view.findViewById(R.id.trips);
        energysave = (TextView) view.findViewById(R.id.energysave);

        logging = (TextView) view.findViewById(R.id.logging);
        units = (TextView) view.findViewById(R.id.units);
        startdelay = (TextView) view.findViewById(R.id.startdelay);
        sampleperiod = (TextView) view.findViewById(R.id.sampleperiod);
        tripsamples = (TextView) view.findViewById(R.id.tripsamples);
        loggedsamples = (TextView) view.findViewById(R.id.loggedsamples);
        startedat = (TextView) view.findViewById(R.id.startedat);
        startedby = (TextView) view.findViewById(R.id.startedby);
        stoppedat = (TextView) view.findViewById(R.id.stoppedat);
        stoppedby = (TextView) view.findViewById(R.id.stoppedby);
        firstsample = (TextView) view.findViewById(R.id.firstsample);
        lastsample = (TextView) view.findViewById(R.id.lastsample);

        startwithbutton = (TextView) view.findViewById(R.id.startwithbutton);
        startondatetime = (TextView) view.findViewById(R.id.startondatetime);
        loopoverwrite = (TextView) view.findViewById(R.id.loopoverwrite);
        allowplacingtags = (TextView) view.findViewById(R.id.allowplacingtags);
        reusewithbutton = (TextView) view.findViewById(R.id.reusewithbutton);
        passwordprotected = (TextView) view.findViewById(R.id.passwordprotected);
        stopwithbutton = (TextView) view.findViewById(R.id.stopwithbutton);
        stopondatetime = (TextView) view.findViewById(R.id.stopondatatime);
        stoponsample = (TextView) view.findViewById(R.id.stoponsample);
        stopwhenfull = (TextView) view.findViewById(R.id.stopwhenfull);
        enablelcdmenu = (TextView) view.findViewById(R.id.enablelcdmenu);
        extentedlcdmenu = (TextView) view.findViewById(R.id.extendedlcdmenu);
        usercomments = (TextView) view.findViewById(R.id.usercomments);

        channel1enable = (TextView) view.findViewById(R.id.c1enabled);
        ch1enablelimits = (TextView) view.findViewById(R.id.c1enablelimits);
        ch1alarmdelay = (TextView) view.findViewById(R.id.alarmdelay1);
        ch1ul = (TextView) view.findViewById(R.id.upperlimit1);
        ch1ll = (TextView) view.findViewById(R.id.lowerlimit1);

        channel2enable = (TextView) view.findViewById(R.id.c2enabled);
        ch2enablelimits = (TextView) view.findViewById(R.id.c2enablelimits);
        ch2alarmdelay = (TextView) view.findViewById(R.id.alarmdelay2);
        ch2ul = (TextView) view.findViewById(R.id.upperlimit2);
        ch2ll = (TextView) view.findViewById(R.id.lowerlimit2);

        showProgress();
//        progressDialoge();
        //ScrollListener();
        queryScroll.getViewTreeObserver().addOnScrollChangedListener(new ScrollPositionObserver());

        bleFragmentI.onBLEWrite(HexData.BLE_ACK);
        bleFragmentI.onBLERead();

        currentTemp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                imperial = !imperial;
                StoreKeyService.setDefaults("UNITS", String.valueOf(imperial?1:0), App.getContext());
                SetUI();
                return false;
            }
        });
        Graph1 = (WebView) view.findViewById(R.id.graphone);

        querygraphlayout = view.findViewById(R.id.querygraphlayout);
        querygraphlayout.setVisibility(View.GONE);
        zoomin = view.findViewById(R.id.zoominButton);
        zoomin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GraphAcivity.class);
                intent.putExtra("VALUE", new Json_Data(mt2Mem_values, baseCMD, 0,getActivity()).CreateObject());
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_query, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        progresspercentage = 0;
        timeoutdelay = timeoutdelay + 10;

        switch(item.getItemId()) {
            case R.id.action_requery:
                mainThreadHandler.removeCallbacksAndMessages(null);
                getActivity().getActionBar().setTitle(R.string.QueryLogger);
                bleFragmentI.onBLEWrite(HexData.BLE_ACK);
                bleFragmentI.onBLERead();
                message = "1";
                state = 1;
                task = new ProgressTask();
                showProgress();
                return true;
            case R.id.action_start:
                mainThreadHandler.removeCallbacksAndMessages(null);
                if(baseCMD.state == 2) {

                    getActivity().getActionBar().setTitle(R.string.StartLogger);
                    bleFragmentI.onBLEWrite(HexData.BLE_ACK);
                    bleFragmentI.onBLERead();
                    message = "2";
                    state = 1;
                    task = new ProgressTask();
                    showProgress();
                }else{
                    Toast.makeText(getActivity(), getString(R.string.Not_Available_in_Current_State), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_stop:
                mainThreadHandler.removeCallbacksAndMessages(null);
                if(baseCMD.state == 4) {

                    getActivity().getActionBar().setTitle(R.string.StopLogger);
                    bleFragmentI.onBLEWrite(HexData.BLE_ACK);
                    bleFragmentI.onBLERead();
                    message = "3";
                    state = 1;
                    task = new ProgressTask();
                    showProgress();
                }else{
                    Toast.makeText(getActivity(), getString(R.string.Not_Available_in_Current_State), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_reuse:
                mainThreadHandler.removeCallbacksAndMessages(null);
                if(baseCMD.state == 5) {
                    getActivity().getActionBar().setTitle(R.string.ReuseLogger);
                    bleFragmentI.onBLEWrite(HexData.BLE_ACK);
                    bleFragmentI.onBLERead();
                    message = "4";
                    state = 1;
                    task = new ProgressTask();
                    showProgress();
                }else{
                    Toast.makeText(getActivity(), getString(R.string.Not_Available_in_Current_State), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_tag:
                mainThreadHandler.removeCallbacksAndMessages(null);
                if(baseCMD.state == 4) {
                    getActivity().getActionBar().setTitle(R.string.TagLogger);
                    bleFragmentI.onBLEWrite(HexData.BLE_ACK);
                    bleFragmentI.onBLERead();
                    message = "5";
                    state = 1;
                    task = new ProgressTask();
                    showProgress();
                }else{
                    Toast.makeText(getActivity(), getString(R.string.Not_Available_in_Current_State), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.menu_about:
                //sendEmail();
                new Screenshot(queryScroll,baseCMD,getActivity()).print();
                return true;
            case R.id.action_find:
                mainThreadHandler.removeCallbacksAndMessages(null);
                getActivity().getActionBar().setTitle(R.string.FindLogger);
                bleFragmentI.onBLEWrite(HexData.BLE_ACK);
                bleFragmentI.onBLERead();
                message = "8";
                state = 1;
                return true;
            case R.id.action_read:
                if(baseCMD.state == 5 || baseCMD.state == 4)
                ((MainActivity)getActivity()).Read_Action();
                return true;
                default:
                    return false;
        }

    }

    public void CommsI(final byte[] in){


//        Runnable runnableCode = new Runnable() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void run() {
                byte[] query;


                if (storeKeyService.getDefaults("SOUND", getActivity().getApplication()) != null && storeKeyService.getDefaults("SOUND", getActivity().getApplication()).equals("1"))
                    soundon = true;
                else
                    soundon = false;

                progresspercentage = progresspercentage + 5;
                switch (state) {
                    case 0:

//                        hexData.BytetoHex(in);
                        query = commsSerial.ReadByte(in);
                        Q_data = baseCMD.CMDQuery(query);
//                        SetUI(Q_data);

                        if (message.equals("2")) {
                            bleFragmentI.onBLEWrite(HexData.START_L);
                            bleFragmentI.onBLERead();
                            state = 2;
                        } else if (message.equals("3")) {
                            if (baseCMD.passwordEnabled) {
                                promtPassword(3);
                            }else {
                                bleFragmentI.onBLEWrite(HexData.STOP_L);
                                bleFragmentI.onBLERead();
                            }
                            state = 3;
                        } else if (message.equals("4")) {
                            if (baseCMD.passwordEnabled) {
                                promtPassword(4);
                            }else {
                                bleFragmentI.onBLEWrite(HexData.REUSE_L);
                                bleFragmentI.onBLERead();
                            }
                            state = 4;
                        } else if (message.equals("5")) {
                            bleFragmentI.onBLEWrite(HexData.TAG_L);
                            bleFragmentI.onBLERead();
                            state = 5;
                        }else if(message.equals("1")){
                            bleFragmentI.onBLEWrite(HexData.QUARY);
                            bleFragmentI.onBLERead();
                            state = 8;
                            //mBluetoothLeService.disconnect();
                        }else if (message.equals("8")) {
                            bleFragmentI.onBLEWrite(HexData.FIND_L);
                            bleFragmentI.onBLERead();
                            state = 5;
                        }
                        break;
                    case 1:


                        bleFragmentI.onBLEWrite(HexData.QUARY);
                        bleFragmentI.onBLERead();
                        //firsttime = 0;

                        //bleFragmentI.onBLERead();
                        state = 0;
                        break;
                    case 2:

                        commsSerial.BytetoHex(in);
                        if (in[0] == 0x00) {
                            BuildDialogue(getString(R.string.Start), getString(R.string.StartSuccessfull), 0);
                            makesound(getActivity(), R.raw.definite);
                        } else {
                            BuildDialogue(getString(R.string.Start), getString(R.string.StartNotSuccessfull), 0);
                            makesound(getActivity(), R.raw.unsure);
                        }
                        SystemClock.sleep(1000);
                        bleFragmentI.onBLEWrite(HexData.QUARY);
                        bleFragmentI.onBLERead();

                        state = 8;
                        break;
                    case 3:
                        hexData.BytetoHex(in);
                        commsSerial.BytetoHex(in);
                        if (in[0] == 0x00) {
                            BuildDialogue(getString(R.string.Stop), getString(R.string.StopSuccessfull), 0);
                            makesound(getActivity(), R.raw.definite);
                        } else {
                            BuildDialogue(getString(R.string.Stop), getString(R.string.StopNotSuccessfull), 0);
                            makesound(getActivity(), R.raw.unsure);
                        }
                        SystemClock.sleep(1000);
                        bleFragmentI.onBLEWrite(HexData.QUARY);
                        bleFragmentI.onBLERead();

                        state = 8;
                        break;
                    case 4:
                        commsSerial.BytetoHex(in);
                        if (in[0] == 0x00) {
                            BuildDialogue(getString(R.string.Reuse), getString(R.string.ReuseSuccessfull), 0);
                            makesound(getActivity(), R.raw.definite);
                        } else {
                            BuildDialogue(getString(R.string.Reuse), getString(R.string.ReuseNotSuccessfull), 0);
                            makesound(getActivity(), R.raw.unsure);
                        }
                        SystemClock.sleep(1000);
                        bleFragmentI.onBLEWrite(HexData.QUARY);
                        bleFragmentI.onBLERead();

                        state = 8;
                        break;
                    case 5:
                        commsSerial.BytetoHex(in);
                        if (message.equals("8")) {
                            if (in[0] == 0x00) {
                                BuildDialogue(getString(R.string.FindLogger), getString(R.string.FindSuccessfull), 0);
                                makesound(getActivity(), R.raw.definite);
                            } else {
                                BuildDialogue(getString(R.string.FindLogger), getString(R.string.FindNotSuccessfull), 0);
                                makesound(getActivity(), R.raw.unsure);
                            }
                        }else{
                            if (in[0] == 0x00) {
                                BuildDialogue(getString(R.string.Tag), getString(R.string.TagSuccessfull), 0);
                                makesound(getActivity(), R.raw.definite);
                            } else {
                                BuildDialogue(getString(R.string.Tag), getString(R.string.TagNotSuccessfull), 0);
                                makesound(getActivity(), R.raw.unsure);
                            }
                        }
                        SystemClock.sleep(1000);
                        bleFragmentI.onBLEWrite(HexData.QUARY);
                        bleFragmentI.onBLERead();

                        state = 8;
                        break;
                    case 6:
                        break;
                    case 7:
//                        hexData.BytetoHex(in);
//                        query = baseCMD.ReadByte(in);
//                        Q_data = baseCMD.CMDQuery(query);
                        bleFragmentI.onBLEWrite(HexData.GO_TO_SLEEP);
                        bleFragmentI.onBLERead();
                        state = 6;
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }



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
                            state = 20;
                            System.arraycopy(mt2Msg_read.memoryData, 0, UserRead, 500, 12);
                            hexData.BytetoHex(UserRead);
                            U_data = baseCMD.CMDUserRead(UserRead);

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
                            state = 29;
                            System.arraycopy(mt2Msg_read.memoryData, 0, ExtraRead, 236, 48);
                            SetUI();
                            hexData.BytetoHex(ExtraRead);
                            progresspercentage = 100;
//                            progress.cancel();
                            stopProgress();
                        }
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        bleFragmentI.onBLERead();
                        break;
                    case 24:// the set up needs to change if loop over right is active.

                        if(baseCMD.numberofsamples == 0) {
                            if(!(baseCMD.state == 4 || baseCMD.state == 5)){
                                //BuildDialogue("No Data","Logger is not in running or stop state to display data", 1);
                            }
//                                SetUI();
//                            progresspercentage = 100;
//                            stopProgress();
                            bleFragmentI.onBLEWrite(HexData.BLE_ACK);
                            bleFragmentI.onBLERead();
                            state = 29;
                        }else {
                            if(baseCMD.LoopOverwrite && !baseCMD.isLoopOverwriteOverFlow){
                                state = 26;
                                totalToRead = baseCMD.MemorySizeMax*2;
                                bytePointer = baseCMD.SamplePointer *2;
                                pageOffset = bytePointer % baseCMD.PAGESIZE;
                                address = (int)((bytePointer / baseCMD.PAGESIZE) + 1) * baseCMD.PAGESIZE;
                                address += pageOffset; //Fix up loop overwrite
                                bytesToRead = totalToRead - address;
                                mt2Msg_read = new MT2Msg_Read(commsChar.MEM_VAL, address, baseCMD.MemorySizeMax *2, 120, 3);
                            }else{
                                state = 25;
                                mt2Msg_read = new MT2Msg_Read(commsChar.MEM_VAL, 0, baseCMD.SamplePointer *2, 120, 3);
                            }
                            bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(true)));
                            bleFragmentI.onBLERead();
                        }
                        break;
                    case 25://
                        if (mt2Msg_read.write_into_readByte(commsSerial.ReadByte(in))) {
                            state = 29;
                            byte[] ValueRead = new byte[mt2Msg_read.memoryData.length];
                            System.arraycopy(mt2Msg_read.memoryData, 0, ValueRead, 0, mt2Msg_read.memoryData.length);
                            MT2ValueIn(ValueRead);
                            plotGraph1(1);
                            if(!(baseCMD.state == 4 || baseCMD.state == 5)){
                                //BuildDialogue("No Data","Logger is not in running or stop state to display data", 1);
                            }
//                                SetUI();
//                            progresspercentage = 100;
//                            stopProgress();
                        }
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        bleFragmentI.onBLERead();
                        break;
                    case 26:
                        if (mt2Msg_read.write_into_readByte(commsSerial.ReadByte(in))) {
                            state = 27;
                            ReadValues1 = new byte[mt2Msg_read.memoryData.length];
                            System.arraycopy(mt2Msg_read.memoryData, 0, ReadValues1, 0, mt2Msg_read.memoryData.length);
                        }
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        bleFragmentI.onBLERead();
                        break;
                    case 27:
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_VAL, 0, bytePointer, 120, 3);
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
                            System.arraycopy(ReadValues1,0,combo,0,ReadValues1.length-1);
                            System.arraycopy(ReadValues2,0,combo,ReadValues1.length,ReadValues2.length);
                            MT2ValueIn(combo);
                            plotGraph1(1);
                            if(!(baseCMD.state == 4 || baseCMD.state == 5)){
                                //BuildDialogue("No Data","Logger is not in running or stop state to display data", 1);
                            }
//                                SetUI();
//                            progresspercentage = 100;
//                            stopProgress();
                        }
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        bleFragmentI.onBLERead();
                        break;
                    case 29:
                        FifteenSecTimeout();
                        break;


                }
//            }
//        };handler1.postDelayed(runnableCode,1);
    }
    /** This passes our data out to the JS */
    @JavascriptInterface
    public String getData() {
        String jobj = json_data.CreateObject();
        //Log.i("GRAPH", jobj);
        return jobj;
        // return json_data.CreateObject();
    }

    public void plotGraph1(int viewtype){
        Graph1.getSettings().setJavaScriptEnabled(true);
        Graph1.addJavascriptInterface(this,"android");
        //Graph1.requestFocusFromTouch();
        Graph1.setWebViewClient(new WebViewClient());
        Graph1.setWebChromeClient(new WebChromeClient());
        json_data = new Json_Data(mt2Mem_values, baseCMD, viewtype,getActivity());
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Graph1.loadUrl("file:///android_asset/highcharts.html");
            }
        }, 0);


    }

    private void MT2ValueIn(byte[] value){
        ArrayList<Byte> data = new ArrayList<Byte>();
        for(int  i = 0; i < value.length; i++) data.add(value[i]);
        //calculating first loged sample
        Date date = baseCMD.startDateTime;
        Calendar calendar = QS.toCalendar(date);
        calendar.add(Calendar.SECOND, baseCMD.startDelay);
        date = calendar.getTime();
        try {
            mt2Mem_values = new MT2Values.MT2Mem_values(data, date, baseCMD.ch1Hi/10, baseCMD.ch1Lo/10, baseCMD.ch2Hi/10, baseCMD.ch2Lo/10, baseCMD.samplePeriod, baseCMD.ch1Enable, baseCMD.ch2Enable );
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void SetUI(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss aa");
        serialno.setText(Q_data.get(0));
        firmware.setText(Q_data.get(1));
        model.setText(QS.GetGeneration(Integer.parseInt(Q_data.get(3))));
        family.setText(QS.GetType(Integer.parseInt(Q_data.get(4))));

        if((baseCMD.querych1hi < baseCMD.ch1Hi) && (baseCMD.querych1lo > baseCMD.ch1Lo) && (baseCMD.querych2hi < baseCMD.ch2Hi) && (baseCMD.querych2lo > baseCMD.ch2Lo)){//drawing the tick if within limits
            limitstatus.setText(App.getContext().getString(R.string.within_limits));
            limiticon.setBackgroundResource(R.drawable.greentick);
        }else{//drawing the warning sign if out of limits
            limitstatus.setText(App.getContext().getString(R.string.outof_limits));
            limiticon.setBackgroundResource(R.drawable.redwarning);
        }

        Lstate.setText( QS.GetState(Integer.parseInt(Q_data.get(5))));
        battery.setText(  R_data.get(17)+"%");

        if (storeKeyService.getDefaults("UNITS", getActivity().getApplication()) != null && storeKeyService.getDefaults("UNITS", getActivity().getApplication()).equals("1")) {
            currentTemp.setText(R_data.get(9) + " °C");
            ch1ul.setText(U_data.get(15) + " °C");
            ch1ll.setText(U_data.get(16) + " °C");
        }else {
            currentTemp.setText(QS.returnF(R_data.get(9)) + " °F");
            ch1ul.setText(QS.returnF(U_data.get(15)) + " °F");
            ch1ll.setText(QS.returnF(U_data.get(16)) + " °F");
        }

        currenthumidity.setText(R_data.get(13) + " %");

        manudate.setText(QS.UTCtoLocal(baseCMD.dmanu.getTime()));
        memory.setText(F_data.get(1));
        startondatetime.setText(R_data.get(0));
        startwithbutton.setText(R_data.get(1));
        stopwithbutton.setText(R_data.get(3));
        stopwhenfull.setText(R_data.get(5));
        stoponsample.setText(R_data.get(6));

        loggedsamples.setText(R_data.get(7));
        tripsamples.setText(R_data.get(7));


        sampleno.setText(U_data.get(5));
        trips.setText(U_data.get(3));
        energysave.setText(QS.YesorNo(baseCMD.energysave));
        if(baseCMD.state == 4){
            st_at.setVisibility(View.VISIBLE);
            st_by.setVisibility(View.VISIBLE);
            sp_at.setVisibility(View.GONE);
            sp_by.setVisibility(View.GONE);
            startedat.setText(QS.UTCtoLocal(baseCMD.dstart.getTime()));
            startedby.setText(R_data.get(2));
        }else if(baseCMD.state == 5){
            st_at.setVisibility(View.VISIBLE);
            st_by.setVisibility(View.VISIBLE);
            sp_at.setVisibility(View.VISIBLE);
            sp_by.setVisibility(View.VISIBLE);
            startedat.setText(QS.UTCtoLocal(baseCMD.dstart.getTime()));
            startedby.setText(R_data.get(2));
            stoppedat.setText(QS.UTCtoLocal(baseCMD.dstop.getTime()));
            stoppedby.setText(R_data.get(4));
        }else{
            st_at.setVisibility(View.GONE);
            st_by.setVisibility(View.GONE);
            sp_at.setVisibility(View.GONE);
            sp_by.setVisibility(View.GONE);
        }


        loopoverwrite.setText(U_data.get(6));
        enablelcdmenu.setText(U_data.get(7));
        allowplacingtags.setText(U_data.get(8));
        reusewithbutton.setText(U_data.get(9));
        passwordprotected.setText(U_data.get(10));
        stopondatetime.setText(U_data.get(11));
        extentedlcdmenu.setText(U_data.get(12));


        channel1enable.setText(U_data.get(13));
        ch1enablelimits.setText(U_data.get(14));


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
            logging.setText(getString(R.string.TempHum));
        }else{
            logging.setText(getString(R.string.Temp));
        }

        units.setText(QS.imperial(baseCMD.ImperialUnit));


        time.setText(QS.UTCtoLocal(new Date().getTime()));

        ch1alarmdelay.setText( R_data.get(12)+ getString(R.string.Samples));
        ch2alarmdelay.setText( R_data.get(16)+ getString(R.string.Samples));


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

        if(!(baseCMD.state == 4 || baseCMD.state == 5))
            querygraphlayout.setVisibility(View.GONE);



    }

    private void FifteenSecTimeout(){
        delayedTask = new Runnable() {
            @Override
            public void run() {


//                try {
//                    TimeUnit.SECONDS.sleep(timeoutdelay/10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                if (getFragmentManager() != null) {
                    bleFragmentI.onBLEWrite(HexData.GO_TO_SLEEP);
                    try {
                        TimeUnit.MILLISECONDS.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    bleFragmentI.BLEDisconnect();
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


    private class ProgressTask extends AsyncTask<Integer,Integer,Void> {

        protected void onPreExecute() {
            super.onPreExecute(); ///////???????

            progress=new ProgressDialog(getActivity());
            progress.setMessage(getString(R.string.QueryLogger));
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(false);
            progress.setProgress(0);
            progress.setCancelable(false);
            progress.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.Abort), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    bleFragmentI.onBLEWrite(HexData.BLE_ACK);
                    bleFragmentI.onBLERead();
                    state =29;
                    BuildDialogue(getString(R.string.ReadAbort), getString(R.string.Go_back_and_reconnect),1);
                    stopProgress();
                    //dialog.dismiss();
                }
            });
            progress.setProgressNumberFormat("");
            progress.setMax(PROGRESSBAR_MAX);
            progress.show();
        }
        protected void onCancelled() {
            stopProgress();
            progress.dismiss();

        }
        protected Void doInBackground(Integer... params) {

            while(progresspercentage < PROGRESSBAR_MAX) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progress.setProgress(progresspercentage);
            }
            return null;
        }
        protected void onProgressUpdate(Integer... values) {


        }
        protected void onPostExecute(Void result) {
            progresspercentage = 0;
            progress.dismiss();
            // async task finished

        }

    }

    public void showProgress() {
        ////////////////////task = new ProgressTask();
        // start progress bar with initial progress 10
        ///////////////////task.execute(10,5,null);
        progresspercentage = 0;
        if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }

    }

    public void stopProgress() {
        progress.dismiss();
        task.cancel(true);
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

    private void makesound(Context context, int resid){
        if(soundon == true) {
            final MediaPlayer mp = MediaPlayer.create(context, resid);
            mp.start();
        }
    }

    //if the logger is password protected this will popup at the start to login
    private void promtPassword(final int command){
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.Ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                //this is where the command is sent
                                if(QS.compareByte(baseCMD.password,QS.md5(userInput.getText().toString()))) {
                                    if (command == 3) {
                                        bleFragmentI.onBLEWrite(HexData.STOP_L);
                                        bleFragmentI.onBLERead();
                                    } else if (command == 4) {
                                        bleFragmentI.onBLEWrite(HexData.REUSE_L);
                                        bleFragmentI.onBLERead();
                                    }
                                }
                                //bleFragmentI.onBLERead();
                                dialog.cancel();
                            }
                        })
                .setNegativeButton(getString(R.string.Cancel),
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


}
