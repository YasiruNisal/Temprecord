package com.example.yasiruw.temprecord.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import android.widget.Toast;

import com.example.yasiruw.temprecord.App;
import com.example.yasiruw.temprecord.R;
import com.example.yasiruw.temprecord.activities.GraphAcivity;
import com.example.yasiruw.temprecord.activities.MainActivity;
import com.example.yasiruw.temprecord.comms.BaseCMD;
import com.example.yasiruw.temprecord.comms.CommsSerial;
import com.example.yasiruw.temprecord.comms.MT2Msg_Read;
import com.example.yasiruw.temprecord.comms.MT2Values;
import com.example.yasiruw.temprecord.CustomLibraries.Yasiru_Temp_Library;
import com.example.yasiruw.temprecord.comms.USBFragmentI;
import com.example.yasiruw.temprecord.services.Json_Data;
import com.example.yasiruw.temprecord.services.PDF;
import com.example.yasiruw.temprecord.services.StoreKeyService;
import com.example.yasiruw.temprecord.services.TXT_FILE;
import com.example.yasiruw.temprecord.comms.CommsChar;
import com.example.yasiruw.temprecord.comms.HexData;
import com.example.yasiruw.temprecord.services.Screenshot;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class USBReadFragment extends Fragment {

    private static final int PROGRESSBAR_MAX = 700;

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

    private TextView limitstatus;
    private ImageView limiticon;

    private LinearLayout Humidity;
    private LinearLayout Humidity2;
    private ScrollView scrollView;
    private ScrollView screen;

    private LinearLayout querycurrenttemp;
    private WebView Graph1;
    private WebView Graph2;

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
    public boolean imperial;
    private int state = 1;
    private ArrayList<String> Q_data = new ArrayList<String>();
    private ArrayList<String> U_data = new ArrayList<String>();
    private ArrayList<String> F_data = new ArrayList<String>();
    private ArrayList<String> R_data = new ArrayList<String>();

    private String BLE_Address = "";

    ProgressTask task;

    HexData hexData = new HexData();
    BaseCMD baseCMD =  new BaseCMD();
    CommsSerial commsSerial = new CommsSerial();
    MT2Values.MT2Mem_values mt2Mem_values = new MT2Values.MT2Mem_values();
    MT2Msg_Read mt2Msg_read;
    Yasiru_Temp_Library QS = new Yasiru_Temp_Library();
    CommsChar commsChar = new CommsChar();
    Json_Data json_data;
    TXT_FILE txt_file = new TXT_FILE();
    Screenshot screenshot;
    static final String FILENAME = "json_file.txt";

    private List<BluetoothDevice> mDevices = new ArrayList<>();
    private int currentDevice = 0;

    //==========================LoopOverWrite READ======
    int address;
    int bytesToRead;
    int totalToRead;
    int bytePointer;
    int pageOffset;



    private Handler handler1 =new Handler();
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";


    USBFragmentI usbFragmentI;

    public USBReadFragment GET_INSTANCE(Bundle data)
    {
        USBReadFragment fragment = new USBReadFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            usbFragmentI = (USBFragmentI) activity;
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_bleread, container, false);
        getActivity().closeContextMenu();
        getActivity().getActionBar().show();
        getActivity().getActionBar().setIcon(R.drawable.ic_readc);
        getActivity().getActionBar().setTitle(R.string.ReadLogger);
        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
        txt_file.Verify_Storage_Permissions(getActivity());
        AssetManager am = getActivity().getAssets();


        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
        Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Medium.ttf");

        limitstatus = view.findViewById(R.id.limitstatus);
        limiticon = view.findViewById(R.id.limiticon);
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


        Humidity2 = (LinearLayout) view.findViewById(R.id.humidity2);
        scrollView = (ScrollView) view.findViewById(R.id.scroll);
        screen = (ScrollView) view.findViewById(R.id.screen);


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

        zoomin = view.findViewById(R.id.zoominButton);
        zoomin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GraphAcivity.class);
                intent.putExtra("VALUE", new Json_Data(mt2Mem_values, baseCMD, 0,getActivity()).CreateObject());
                startActivity(intent);
            }
        });

//        ExpandableRelativeLayout expandablereadloggerdetails = view.findViewById(R.id.expandablereadloggerdetails);
//        expandablereadloggerdetails.toggle();
//
//        ExpandableRelativeLayout expandablereadgraph = view.findViewById(R.id.expandablereadgraph);
//        expandablereadgraph.toggle();
//
//        ExpandableRelativeLayout expandablechannel1stats = view.findViewById(R.id.expandablechannel1stats);
//        expandablechannel1stats.toggle();
//
//        ExpandableRelativeLayout expandablechannel2stats = view.findViewById(R.id.expandablechannel2stats);
//        expandablechannel2stats.toggle();

        mConnectionState.setText(getString(R.string.USB_Connected));
        task = new ProgressTask();
        showProgress();
        usbFragmentI.onUSBWrite(HexData.QUARY_USB);


        Graph1 = (WebView) view.findViewById(R.id.graphone);
        querycurrenttemp = view.findViewById(R.id.querycurrenttemp);
        querycurrenttemp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("TOUCH", "Touch event" + imperial);
                imperial = !imperial;
                StoreKeyService.setDefaults("UNITS", String.valueOf(imperial?1:0), App.getContext());
                SetUI();
                plotGraph1(1);
                return false;
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_simple, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_about:
                //sendEmail();
                new Screenshot(scrollView,baseCMD,getActivity()).print();
                return true;
            case R.id.menu_pdf:
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.pdf_dialog);
                dialog.setTitle(getString(R.string.pdf_options));
                ImageButton simplepdf = (ImageButton) dialog.findViewById(R.id.simplepdf);
                ImageButton pdfv1button = (ImageButton) dialog.findViewById(R.id.pdfv1);
                ImageButton pdfv2button = (ImageButton) dialog.findViewById(R.id.pdfv2);
                TextView simplepdf_text = dialog.findViewById(R.id.simplepdf_text);
                TextView fullpdf_v1_text = dialog.findViewById(R.id.fullpdf_v1_text);
                TextView fullpdf_v2_text = dialog.findViewById(R.id.fullpdf_v2_text);
                simplepdf_text.setText(getString(R.string.simplePDF) + PDF_pages(0));
                fullpdf_v1_text.setText(getString(R.string.fullPDFV1) + PDF_pages(1));
                fullpdf_v2_text.setText(getString(R.string.fullPDFV2) + PDF_pages(2));
                // if button is clicked, close the custom dialog
                simplepdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(),getString(R.string.PDF_gen), Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                        PDF pdf = new PDF();
                        pdf.getData(mt2Mem_values, baseCMD, 0, Q_data, U_data, F_data, R_data);
                        pdf.Create_Report(App.getContext());
                        pdf.Open_PDF_in_Chrome(App.getContext());

                    }
                });
                pdfv1button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(),getString(R.string.PDF_gen), Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                        PDF pdf1 = new PDF();
                        pdf1.getData(mt2Mem_values, baseCMD, 1, Q_data, U_data, F_data, R_data);
                        pdf1.Create_Report(App.getContext());
                        pdf1.Open_PDF_in_Chrome(App.getContext());

                    }
                });
                pdfv2button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(),getString(R.string.PDF_gen), Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                        PDF pdf3 = new PDF();
                        pdf3.getData(mt2Mem_values, baseCMD, 2, Q_data, U_data, F_data, R_data);
                        pdf3.Create_Report(App.getContext());
                        pdf3.Open_PDF_in_Chrome(App.getContext());

                    }
                });

                dialog.show();
                return true;
            default:
                return false;
        }
    }


    public void CommsI(final byte[] in){

//        if (storeKeyService.getDefaults("SOUND", getActivity().getApplication()) != null && storeKeyService.getDefaults("SOUND", getActivity().getApplication()).equals("1"))
//            soundon = true;
//        else
//            soundon = false;
//        Runnable runnableCode = new Runnable() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void run() {
                byte[] query;



                progresspercentage++;

                switch (state) {
                    case 0:

//                        hexData.BytetoHex(in);
//                        query = baseCMD.ReadByte(in);
//                        Q_data = baseCMD.CMDQuery(query);
//                        SetUI(Q_data);
                        if(message.equals("1")){
                            usbFragmentI.onUSBWrite(HexData.GO_TO_SLEEP);
                            state = 6;
                            //mBluetoothLeService.disconnect();
                        }
                        break;
                    case 1:
                        if(frommenu){

                            progresspercentage = 0;
                        }
                        //enters here first
                        usbFragmentI.onUSBWrite(HexData.QUARY);
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
                        usbFragmentI.onUSBWrite(HexData.GO_TO_SLEEP);
                        break;
                    case 8:
                        hexData.BytetoHex(in);
                        query = commsSerial.ReadUSBByte(in);
                        Q_data = baseCMD.CMDQuery(query);


                        //sets up reading the first 120 bytes from flash, then the message is constructed with 0x55 and stuff
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_TRW, 0, 120, 120, 3);
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(true)));
                        //sendData(mt2Msg_read.Read_into_writeByte(false));
                        state = 9;
                        break;
                    case 9:// reads the 120 bytes till write_into_readbyte returns true. then the state is changed to read the next 24 bytes
                        if(mt2Msg_read.write_into_readByte(commsSerial.ReadUSBByte(in))){
                            state = 10;
                            System.arraycopy(mt2Msg_read.memoryData, 0, TWFlash, 0, 120);
                            //TWFlash = mt2Msg_read.memoryData;

                        }
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;
                    case 10://sets up to read the next 24 bytes
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_TRW, 120, 144, 24, 3);
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(true)));
                        //sendData(mt2Msg_read.Read_into_writeByte(false));
                        state = 11;
                        break;
                    case 11://next 24 bytes are read and appened to the TWFlassh byte array. Then moving on to reading user flash
                        if(mt2Msg_read.write_into_readByte(commsSerial.ReadUSBByte(in))){
                            state = 12;

                            System.arraycopy(mt2Msg_read.memoryData, 0, TWFlash, 120, 24);
                            hexData.BytetoHex(TWFlash);
                            F_data = baseCMD.CMDFlash(TWFlash);

                            // call the decoding functon here to decode all we need and send the data back in an string array so that they can be desplayed in appropriate textviews
                        }
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;
                    case 12:
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_USER, 0, 250, 64, 3);
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(true)));
                        state = 13;
                        break;
                    case 13:
                        if(mt2Msg_read.write_into_readByte(commsSerial.ReadUSBByte(in))) {
                            state = 14;
                            System.arraycopy(mt2Msg_read.memoryData, 0, UserRead, 0, 250);

                            //hexData.BytetoHex(TWFlash);
                        }
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;
                    case 14:
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_RAM, 0, 100, 64, 3);
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(true)));
                        state = 15;
                        break;
                    case 15:
                        if(mt2Msg_read.write_into_readByte(commsSerial.ReadUSBByte(in))) {

                            System.arraycopy(mt2Msg_read.memoryData, 0, RamRead, 0, 100);
                            hexData.BytetoHex(RamRead);
                            R_data = baseCMD.CMDRamRead(RamRead);
                            state = 16;


                        }
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;
                    case 16:
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_USER, 250, 500, 64, 3);
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(true)));
                        state = 17;
                        break;
                    case 17:
                        if(mt2Msg_read.write_into_readByte(commsSerial.ReadUSBByte(in))) {
                            state = 18;

                            System.arraycopy(mt2Msg_read.memoryData, 0, UserRead, 250, 250);

                            //hexData.BytetoHex(TWFlash);
                        }
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;

                    case 18:
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_USER, 500, 512, 64, 3);
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(true)));
                        state = 19;
                        break;
                    case 19:
                        if(mt2Msg_read.write_into_readByte(commsSerial.ReadUSBByte(in))) {
                            state = 24;
                            System.arraycopy(mt2Msg_read.memoryData, 0, UserRead, 500, 12);
                            hexData.BytetoHex(UserRead);
                            U_data = baseCMD.CMDUserRead(UserRead);

                            /// SetUI();
                        }
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;
                    case 20:
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_EXTRA, 114, 350, 64, 3);
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(true)));
                        state = 21;
                        break;
                    case 21:
                        if(mt2Msg_read.write_into_readByte(commsSerial.ReadUSBByte(in))) {
                            state = 22;
                            System.arraycopy(mt2Msg_read.memoryData, 0, ExtraRead, 0, 236);
                            //hexData.BytetoHex(TWFlash);
                        }
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;
                    case 22:
                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_EXTRA, 350, 398, 300, 3);
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(true)));
                        state = 23;
                        break;
                    case 23:
                        if(mt2Msg_read.write_into_readByte(commsSerial.ReadUSBByte(in))) {
                            state = 24;
                            System.arraycopy(mt2Msg_read.memoryData, 0, ExtraRead, 236, 48);
                            firsttime++;
                            hexData.BytetoHex(ExtraRead);

                        }
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;
                    case 24:// the set up needs to change if loop over right is active.

                        if(baseCMD.numberofsamples == 0) {
                            if(!(baseCMD.state == 4 || baseCMD.state == 5)){
                                //BuildDialogue("No Data","Logger is not in running or stop state to display data", 1);
                            }else
                                SetUI();
                            progresspercentage = 100;

                            usbFragmentI.onUSBWrite(HexData.BLE_ACK);
                            state = 29;
                        }else {

                            if(baseCMD.LoopOverwrite && !baseCMD.isLoopOverwriteOverFlow){//if loop overwrite is enabled
                                state = 26;
                                totalToRead = baseCMD.MemorySizeMax*2;
                                bytePointer = baseCMD.SamplePointer *2;
                                pageOffset = bytePointer % baseCMD.PAGESIZE;
                                address = (int)((bytePointer / baseCMD.PAGESIZE) + 1) * baseCMD.PAGESIZE;
                                address += pageOffset; //Fix up loop overwrite
                                bytesToRead = totalToRead - address;
                                mt2Msg_read = new MT2Msg_Read(commsChar.MEM_VAL, address, baseCMD.MemorySizeMax*2, 64, 3);
                            }else{
                                state = 25;
                                mt2Msg_read = new MT2Msg_Read(commsChar.MEM_VAL, 0, baseCMD.SamplePointer *2, 64, 3);
                            }
                            usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(true)));
                        }
                        break;
                    case 25://normal read
                        if (mt2Msg_read.write_into_readByte(commsSerial.ReadUSBByte(in)))
                        {
                            state = 29;
                            byte[] ValueRead = new byte[mt2Msg_read.memoryData.length];
                            System.arraycopy(mt2Msg_read.memoryData, 0, ValueRead, 0, mt2Msg_read.memoryData.length);
                            MT2ValueIn(ValueRead);
                            plotGraph1(1);//plots the graph
                            if(!(baseCMD.state == 4 || baseCMD.state == 5))
                            {
                                //BuildDialogue("No Data","Logger is not in running or stop state to display data", 1);
                            }
                            else
                            {
                                SetUI();//fill ui elements
                                progresspercentage = 100;
                                stopProgress();
                            }
                            state = 29;
                            //Log.i("SPEED", "Stop READ");

                        }
                       // Log.i("SPEED", "in state 25");
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(false)));
                        //Update_UI(App.getContext(), null, "HID_USB_Message_Received", ValueRead, null);
                        break;
                    case 26:
                        if (mt2Msg_read.write_into_readByte(commsSerial.ReadUSBByte(in))) {
                            state = 27;
                            ReadValues1 = new byte[mt2Msg_read.memoryData.length];
                            System.arraycopy(mt2Msg_read.memoryData, 0, ReadValues1, 0, mt2Msg_read.memoryData.length);

                        }
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(false)));
                        firstRead = mt2Msg_read.memoryData.length;
                        break;
                    case 27:

                        mt2Msg_read = new MT2Msg_Read(commsChar.MEM_VAL, 0, bytePointer, 64, 3);
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(true)));
                        state = 28;
                        break;
                    case 28:
                        if (mt2Msg_read.write_into_readByte(commsSerial.ReadUSBByte(in))) {

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
                            }else
                                SetUI();
                            progresspercentage = 100;
                            stopProgress();
                        }

                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;
                    case 29:

                        break;


                }
            }
        //};handler1.postDelayed(runnableCode,1);
    //}


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

    public void logLargeString(String str) {
        if(str.length() > 3000) {
            //Log.i(TAG, str.substring(0, 3000));
            logLargeString(str.substring(3000));
        } else {
            //Log.i(TAG, str); // continuation
        }
    }


    /** This passes our data out to the JS */
    @JavascriptInterface
    public String getData() {
        String jobj = json_data.CreateObject();
        //Log.i("GRAPH", jobj);
        return jobj;
       // return json_data.CreateObject();
    }





    public void SetUI(){
        serialno.setText(Q_data.get(0));
        //firmware.setText(Q_data.get(1));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss aa");
        time.setText(QS.UTCtoLocal(new Date().getTime()));

        if(Double.parseDouble(Q_data.get(6)) > 66){
            bat.setBackgroundResource(R.drawable.bfull);
        }else if(Double.parseDouble(Q_data.get(6)) > 33){
            bat.setBackgroundResource(R.drawable.bhalf);
        }else if(Double.parseDouble(Q_data.get(6)) > 0){
            bat.setBackgroundResource(R.drawable.blow);

        }

        if(mt2Mem_values.ch0Stats.TotalLimit == 0 && mt2Mem_values.ch1Stats.TotalLimit == 0){//drawing the tick if within limits
            limitstatus.setText(App.getContext().getString(R.string.within_limits));
            limiticon.setBackgroundResource(R.drawable.greentick);
        }else{//drawing the warning sign if out of limits
            limitstatus.setText(App.getContext().getString(R.string.outof_limits));
            limiticon.setBackgroundResource(R.drawable.redwarning);
        }

        //model.setText(QS.GetGeneration(Integer.parseInt(Q_data.get(2))));
        //family.setText(QS.GetType(Integer.parseInt(Q_data.get(3))));

        Lstate.setText( QS.GetState(Integer.parseInt(Q_data.get(5))));
        battery.setText(  R_data.get(17)+"%");
        if (storeKeyService.getDefaults("UNITS", getActivity().getApplication()) != null && storeKeyService.getDefaults("UNITS", getActivity().getApplication()).equals("1")) {
            currentTemp.setText(R_data.get(9) + " °C");
        }else {
            currentTemp.setText(QS.returnF(R_data.get(9)) + " °F");
        }
        currenthumidity.setText(R_data.get(13) + " %");

        memory.setText(F_data.get(1));
        if(baseCMD.numberofsamples != 0) {
            //TripSamples.setText(R_data.get(7));
            TotalLoggedTime.setText(QS.Period((int) mt2Mem_values.LoggedTripDuration * 1000));
            LoggedSamples.setText(mt2Mem_values.Data.size() + "");
            LoggedTags.setText(mt2Mem_values.TagCount() + "");
            FirstSample.setText(QS.UTCtoLocal(mt2Mem_values.Data.get(0).valTime.getTime()));
            FirstLoggedSample.setText(QS.UTCtoLocal(mt2Mem_values.Data.get(0).valTime.getTime()));
            LastSample.setText(QS.UTCtoLocal(mt2Mem_values.Data.get(mt2Mem_values.Data.size() - 1).valTime.getTime()));
            LastLoggedSample.setText(QS.UTCtoLocal(mt2Mem_values.Data.get(mt2Mem_values.Data.size() - 1).valTime.getTime()));
        }


        if(baseCMD.ch1Enable && baseCMD.numberofsamples != 0) {
            channel1.setText(getString(R.string.Temp));
            if (storeKeyService.getDefaults("UNITS", getActivity().getApplication()) != null && storeKeyService.getDefaults("UNITS", getActivity().getApplication()).equals("1")) {
                upperLimit1.setText(baseCMD.ch1Hi / 10.0 + " °C");
                lowerLimit1.setText(baseCMD.ch1Lo / 10.0 + " °C");
                mkt.setText(String.format("%.1f", mt2Mem_values.ch0Stats.MKTValue) + " °C");
                mean1.setText(String.format("%.1f", mt2Mem_values.ch0Stats.Mean) + " °C");
                String max1time = QS.UTCtoLocal(mt2Mem_values.Data.get(mt2Mem_values.ch0Stats.Max.Number - 1).valTime.getTime());
                max1.setText(mt2Mem_values.ch0Stats.Max.Value / 10.0 + " °C\n" + max1time);
                String min1time = QS.UTCtoLocal(mt2Mem_values.Data.get(mt2Mem_values.ch0Stats.Min.Number - 1).valTime.getTime());
                min1.setText(mt2Mem_values.ch0Stats.Min.Value / 10.0 + " °C\n" + min1time);
            }else{
                upperLimit1.setText(QS.returnFD(baseCMD.ch1Hi / 10.0) + " °F");
                lowerLimit1.setText(QS.returnFD(baseCMD.ch1Lo / 10.0) + " °F");
                mkt.setText(QS.returnF(String.format("%.1f", mt2Mem_values.ch0Stats.MKTValue)) + " °F");
                mean1.setText(QS.returnF(String.format("%.1f", mt2Mem_values.ch0Stats.Mean)) + " °F");
                String max1time = QS.UTCtoLocal(mt2Mem_values.Data.get(mt2Mem_values.ch0Stats.Max.Number - 1).valTime.getTime());
                max1.setText(QS.returnFD(mt2Mem_values.ch0Stats.Max.Value / 10.0) + " °F\n" + max1time);
                String min1time = QS.UTCtoLocal(mt2Mem_values.Data.get(mt2Mem_values.ch0Stats.Min.Number - 1).valTime.getTime());
                min1.setText(QS.returnFD(mt2Mem_values.ch0Stats.Min.Value / 10.0) + " °F\n" + min1time);
            }

            totalSampleswithinLimits1.setText(mt2Mem_values.ch0Stats.TotalLimitWithin + "");
            totalTimewithinLimits1.setText(mt2Mem_values.ch0Stats.TotalTimeWithin + "");
            totalPercentagewitinLimits1.setText(String.format("%.2f",mt2Mem_values.ch0Stats.TotalPercentWithin) + " %");

            totalSaamplesoutofLimits1.setText(mt2Mem_values.ch0Stats.TotalLimit + "");
            totalTimeoutofLImits1.setText(mt2Mem_values.ch0Stats.TotalTime);
            totalPercentageoutofLimits1.setText(String.format("%.2f",mt2Mem_values.ch0Stats.TotalPercent) + " %");

            samplesaboveUpperLimit1.setText(mt2Mem_values.ch0Stats.AboveLimit + "");
            timeaboveUpperLimit1.setText(mt2Mem_values.ch0Stats.AboveTime + "");
            percentageaboveUpperLimit1.setText(String.format("%.2f",mt2Mem_values.ch0Stats.AbovePercent) + " %");

            samplesbelowLowerLimit1.setText(mt2Mem_values.ch0Stats.BelowLimit + "");
            timebelowLowerSamples1.setText(mt2Mem_values.ch0Stats.BelowTime);
            percentagebelowLowerSample1.setText(String.format("%.2f",mt2Mem_values.ch0Stats.BelowPercent) + " %");
        }

        if(baseCMD.ch2Enable && baseCMD.numberofsamples != 0) {
            channel2.setText(getString(R.string.Hum));
            upperLimit2.setText(baseCMD.ch2Hi / 10.0 + "");
            lowerLimit2.setText(baseCMD.ch2Lo / 10.0 + "");

            mean2.setText(String.format("%.1f", mt2Mem_values.ch1Stats.Mean) + "");
            String max2time = QS.UTCtoLocal(mt2Mem_values.Data.get(mt2Mem_values.ch1Stats.Max.Number-1).valTime.getTime());
            max2.setText(mt2Mem_values.ch1Stats.Max.Value / 10.0 + "\n" + max2time);
            String min2time = QS.UTCtoLocal(mt2Mem_values.Data.get(mt2Mem_values.ch1Stats.Min.Number-1).valTime.getTime());
            min2.setText(mt2Mem_values.ch1Stats.Min.Value / 10.0 + "\n" + min2time);

            totalSampleswithinLimits2.setText(mt2Mem_values.ch1Stats.TotalLimitWithin + "");
            totalTimewithinLimits2.setText(mt2Mem_values.ch1Stats.TotalTimeWithin + "");
            totalPercentagewitinLimits2.setText(String.format("%.2f",mt2Mem_values.ch1Stats.TotalPercentWithin) + " %");

            totalSaamplesoutofLimits2.setText(mt2Mem_values.ch1Stats.TotalLimit + "");
            totalTimeoutofLImits2.setText(mt2Mem_values.ch1Stats.TotalTime);
            totalPercentageoutofLimits2.setText(String.format("%.2f",mt2Mem_values.ch1Stats.TotalPercent) +" %");

            samplesaboveUpperLimit2.setText(mt2Mem_values.ch1Stats.AboveLimit + "");
            timeaboveUpperLimit2.setText(mt2Mem_values.ch1Stats.AboveTime + "");
            percentageaboveUpperLimit2.setText(String.format("%.2f",mt2Mem_values.ch1Stats.AbovePercent) + " %");

            samplesbelowLowerLimit2.setText(mt2Mem_values.ch1Stats.BelowLimit + "");
            timebelowLowerSamples2.setText(mt2Mem_values.ch1Stats.BelowTime);
            percentagebelowLowerSample2.setText(String.format("%.2f",mt2Mem_values.ch1Stats.BelowPercent) + " %");
        }

        if((R_data.get(0)).equals("Yes")){
            //set the starttimedate here;
        }



        if(!baseCMD.ch2Enable){
          //  Humidity.setVisibility(LinearLayout.GONE);
            Humidity2.setVisibility(LinearLayout.GONE);
        }

        if(!baseCMD.ch2Enable){
            currenthumidity.setText("--");
        }


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
                            ((MainActivity)getActivity()).Update_UI_state(2);
                        }
                        // continue with delete
                    }
                })
                .setIcon(R.drawable.ic_message)
                .show();
    }




    private class ProgressTask extends AsyncTask<Integer,Integer,Void> {

        protected void onPreExecute() {
            super.onPreExecute(); ///////???????

            progress=new ProgressDialog(getActivity());
            progress.setMessage(getString(R.string.ReadLogger));
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(false);
            progress.setProgress(0);
            progress.setCancelable(false);
            progress.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.Abort), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    usbFragmentI.onUSBWrite(HexData.BLE_ACK);
                    state =29;
                    BuildDialogue(getString(R.string.ReadAbort), getString(R.string.Go_back_and_reconnect),1);
                    //dialog.dismiss();
                    stopProgress();
                }
            });
            progress.setProgressNumberFormat("");
            progress.setMax(PROGRESSBAR_MAX);
            progress.show();
        }
        protected void onCancelled() {

            progress.dismiss();
            //stopProgress();

        }
        protected Void doInBackground(Integer... params) {

            while(progresspercentage < PROGRESSBAR_MAX) {
                try {
                    Thread.sleep(90);
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

    private int PDF_pages(int option){
        if(option == 0){
            return 1;
        }else if(option == 1){
            return (int)((baseCMD.ch2Enable)? Math.ceil((mt2Mem_values.Data.size()*2/57)/11)+2 : Math.ceil((mt2Mem_values.Data.size()/57)/11)+2);
        }else if (option == 2){
            return (int)Math.ceil((mt2Mem_values.Data.size()/57)/2)+2;
        }
        return  0;

    }





}
