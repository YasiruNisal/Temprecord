package com.example.yasiruw.temprecord.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
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
import com.example.yasiruw.temprecord.comms.QueryStrings;
import com.example.yasiruw.temprecord.services.StoreKeyService;
import com.example.yasiruw.temprecord.utils.CommsChar;
import com.example.yasiruw.temprecord.utils.HexData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;
import static android.content.Context.BIND_AUTO_CREATE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;


public class BLEQueryFragment extends Fragment {

    public static final String EXTRAS_MESSAGE = "0";

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

    private Handler handler1 =new Handler();
    Thread t = new Thread();
    private int state = 1;

    HexData hexData = new HexData();
    BaseCMD baseCMD =  new BaseCMD();
    CommsSerial commsSerial = new CommsSerial();
    MT2Msg_Read mt2Msg_read;
    QueryStrings QS = new QueryStrings();
    CommsChar commsChar = new CommsChar();

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
            getActivity().getActionBar().setTitle("Query Logger");
        }else if(message.equals("2")){
            getActivity().getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_startc));
            getActivity().getActionBar().setTitle("Start Logger");
        }else if(message.equals("3")){
            getActivity().getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_stopc));
            getActivity().getActionBar().setTitle("Stop Logger");
        }else if(message.equals("4")){
            getActivity().getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_reusec));
            getActivity().getActionBar().setTitle("Re-Use Logger");
        }else if(message.equals("5")){
            getActivity().getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_tagc));
            getActivity().getActionBar().setTitle("Tag Logger");
        }else if(message.equals("8")){
            getActivity().getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_searchc));
            getActivity().getActionBar().setTitle("Search Logger");
        }


        linearLayout = (LinearLayout) view.findViewById(R.id.attop);
        mWrapperFL = (FrameLayout) view.findViewById(R.id.flWrapper);
        queryScroll = (ScrollView) view.findViewById(R.id.queryscroll);
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

        progressDialoge();
        //ScrollListener();
        queryScroll.getViewTreeObserver().addOnScrollChangedListener(new ScrollPositionObserver());

        Log.d("TAG", "am i coming to this place 11  " + bleFragmentI);
        bleFragmentI.onBLEWrite(HexData.BLE_ACK);
        bleFragmentI.onBLERead();


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
                bleFragmentI.onBLEWrite(HexData.BLE_ACK);
                bleFragmentI.onBLERead();
                message = "1";
                state = 1;
                progressDialoge();
                return true;
            case R.id.action_start:
                bleFragmentI.onBLEWrite(HexData.BLE_ACK);
                bleFragmentI.onBLERead();
                message = "2";
                state = 1;
                progressDialoge();
                return true;
            case R.id.action_stop:
                bleFragmentI.onBLEWrite(HexData.BLE_ACK);
                bleFragmentI.onBLERead();
                message = "3";
                state = 1;
                progressDialoge();
                return true;
            case R.id.action_reuse:
                bleFragmentI.onBLEWrite(HexData.BLE_ACK);
                bleFragmentI.onBLERead();
                message = "4";
                state = 1;
                progressDialoge();
                return true;
            case R.id.action_tag:
                bleFragmentI.onBLEWrite(HexData.BLE_ACK);
                bleFragmentI.onBLERead();
                message = "5";
                state = 1;
                progressDialoge();
                return true;
            case R.id.menu_about:
                //sendEmail();
                return true;
            case R.id.action_find:
                bleFragmentI.onBLEWrite(HexData.BLE_ACK);
                bleFragmentI.onBLERead();
                message = "8";
                state = 1;
                return true;
                default:
                    return false;
        }

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
                            bleFragmentI.onBLEWrite(HexData.STOP_L);
                            bleFragmentI.onBLERead();
                            state = 3;
                        } else if (message.equals("4")) {
                            bleFragmentI.onBLEWrite(HexData.REUSE_L);
                            bleFragmentI.onBLERead();
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

                        state = 0;
                        bleFragmentI.onBLEWrite(HexData.QUARY);
                        bleFragmentI.onBLERead();
                        //firsttime = 0;
                        break;
                    case 2:
                        Log.i("YAS", "CommsI in message start logger");
                        commsSerial.BytetoHex(in);
                        if (in[0] == 0x00) {
                            BuildDialogue("Start", "The Logger has started successfully.", 0);
                            makesound(getActivity(), R.raw.definite);
                        } else {
                            BuildDialogue("Start", "Start not successful.\n\nThe logger is either running or is in stopped state", 0);
                            makesound(getActivity(), R.raw.unsure);
                        }
                        SystemClock.sleep(1000);
                        bleFragmentI.onBLEWrite(HexData.QUARY);
                        bleFragmentI.onBLERead();

                        state = 8;
                        break;
                    case 3:
                        Log.i("YAS", "CommsI in message stop logger");
                        commsSerial.BytetoHex(in);
                        if (in[0] == 0x00) {
                            BuildDialogue("Stop", "The Logger has stopped successfully.", 0);
                            makesound(getActivity(), R.raw.definite);
                        } else {
                            BuildDialogue("Stop", "Stop not successful.\n\nThe logger is either in ready or already has stopped", 0);
                            makesound(getActivity(), R.raw.unsure);
                        }
                        SystemClock.sleep(1000);
                        bleFragmentI.onBLEWrite(HexData.QUARY);
                        bleFragmentI.onBLERead();

                        state = 8;
                        break;
                    case 4:
                        Log.i("YAS", "CommsI in message reuse logger");
                        commsSerial.BytetoHex(in);
                        if (in[0] == 0x00) {
                            BuildDialogue("Re-Use", "The Logger has been successfully re-used.", 0);
                            makesound(getActivity(), R.raw.definite);
                        } else {
                            BuildDialogue("Re-Use", "Re-use unsuccessful.\n\nTry stopping the logger first", 0);
                            makesound(getActivity(), R.raw.unsure);
                        }
                        SystemClock.sleep(1000);
                        bleFragmentI.onBLEWrite(HexData.QUARY);
                        bleFragmentI.onBLERead();

                        state = 8;
                        break;
                    case 5:
                        Log.i("YAS", "CommsI in message find logger or tag logger");
                        commsSerial.BytetoHex(in);
                        if (message.equals("8")) {
                            if (in[0] == 0x00) {
                                BuildDialogue("Find Logger", "Green LED should be blinking on the logger now.", 0);
                                makesound(getActivity(), R.raw.definite);
                            } else {
                                BuildDialogue("Find Logger", "An error occurred", 0);
                                makesound(getActivity(), R.raw.unsure);
                            }
                        }else{
                            if (in[0] == 0x00) {
                                BuildDialogue("Tag", "The Logger has been successfully tagged.", 0);
                                makesound(getActivity(), R.raw.definite);
                            } else {
                                BuildDialogue("Tag", "Tagging unsuccessful.\n\nTry starting the logger first", 0);
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
                            state = 24;
                            System.arraycopy(mt2Msg_read.memoryData, 0, ExtraRead, 236, 48);
                            SetUI();
                            hexData.BytetoHex(ExtraRead);
                            progresspercentage = 100;
                            progress.cancel();
                        }
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                        bleFragmentI.onBLERead();
                        break;
                    case 24:
                        bleFragmentI.onBLEWrite(HexData.BLE_ACK);
                        bleFragmentI.onBLERead();
                        state = 25;
                        break;
                    case 25:
                        hexData.BytetoHex(in);
                        //FifteenSecTimeout();
                        break;

                }
            }
        };handler1.postDelayed(runnableCode,1);
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
        energysave.setText(QS.YesorNo(baseCMD.energysave));

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

    private void FifteenSecTimeout(){
         t = new Thread() {
            @Override
            public void run() {

                try {
                    TimeUnit.SECONDS.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    TimeUnit.SECONDS.sleep(timeoutdelay/10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                bleFragmentI.onBLEWrite(HexData.GO_TO_SLEEP);
                try {
                    TimeUnit.MILLISECONDS.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("TAG", "15 sec time out");
                if (getFragmentManager() == null) {
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
        t.start();

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

    public void progressDialoge(){

        progress=new ProgressDialog(getActivity());
        progress.setMessage("Querying Logger");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(false);
        progress.setProgress(0);
        progress.setCancelable(false);
        progress.setButton(DialogInterface.BUTTON_NEGATIVE, "Abort", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                sendData(hexData.BLE_ACK);
//                state = 7;

                if(family.getText().toString() == "")
                    BuildDialogue("Query Aborted", "Entries might be empty!\nGo back to menu and reconnect", 1);
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


}
