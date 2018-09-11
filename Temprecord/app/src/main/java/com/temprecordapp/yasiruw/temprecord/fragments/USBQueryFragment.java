package com.temprecordapp.yasiruw.temprecord.fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.ikovac.timepickerwithseconds.TimePicker;
import com.temprecordapp.yasiruw.temprecord.App;
import com.temprecordapp.yasiruw.temprecord.R;
import com.temprecordapp.yasiruw.temprecord.activities.GraphAcivity;
import com.temprecordapp.yasiruw.temprecord.activities.MainActivity;
import com.temprecordapp.yasiruw.temprecord.comms.BaseCMD;
import com.temprecordapp.yasiruw.temprecord.comms.CHUserData;
import com.temprecordapp.yasiruw.temprecord.comms.CommsSerial;
import com.temprecordapp.yasiruw.temprecord.comms.MT2Msg_Read;
import com.temprecordapp.yasiruw.temprecord.CustomLibraries.Yasiru_Temp_Library;
import com.temprecordapp.yasiruw.temprecord.comms.MT2Msg_Write;
import com.temprecordapp.yasiruw.temprecord.comms.MT2Values;
import com.temprecordapp.yasiruw.temprecord.comms.USBFragmentI;
import com.temprecordapp.yasiruw.temprecord.services.InputFilterMinMax;
import com.temprecordapp.yasiruw.temprecord.services.Json_Data;
import com.temprecordapp.yasiruw.temprecord.services.PDF;
import com.temprecordapp.yasiruw.temprecord.services.StoreKeyService;
import com.temprecordapp.yasiruw.temprecord.comms.CommsChar;
import com.temprecordapp.yasiruw.temprecord.comms.HexData;
import com.temprecordapp.yasiruw.temprecord.services.USB;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class USBQueryFragment extends Fragment implements com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener{

    public static final String EXTRAS_MESSAGE = "0";

    private static final int PROGRESSBAR_MAX = 500;

    private ScrollView queryScroll;
    private FrameLayout mWrapperFL;
    private LinearLayout linearLayout;
    private LinearLayout bleenegy;
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

    private TextView LoggedTags;
    private TextView FirstSample;
    private TextView FirstLoggedSample;
    private TextView LastSample;
    private TextView LastLoggedSample;


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
    private ImageButton zoomin;

    private LinearLayout querycurrenttemp;
    private LinearLayout usercomment;

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

    private LinearLayout Humidity2;
    private LinearLayout Temp2;
    private LinearLayout limitlayout;
    private LinearLayout Menubuttons;

    public boolean imperial;
    private TextView limitstatus;
    private ImageView limiticon;

    private ImageButton start_stop;
    private TextView start_stop_text;
    private ImageButton read;
    private TextView read_text;
    private ImageButton pdf;
    private Button Certificate;
    private Button Certificate2;
    private Button Website;

    private LinearLayout parameterlayout;
    private LinearLayout countdownlayout;
    private TextView countdowntext;
    private CountDownTimer countDownTimer;


    private TextView appversion;
    private TextView androidversion;
    TextView more;
    LinearLayout queryloggerdetails;
    LinearLayout querytripinfomation;
    LinearLayout querychannelinfo;
    LinearLayout querychannel1stats;
    LinearLayout querychannel2stats;
    LinearLayout querygraphlayout;
    LinearLayout helplayout;


    LinearLayout parametersparent;

    ExpandableRelativeLayout expandablemore;
    ExpandableRelativeLayout expandablehelp;

    ExpandableRelativeLayout expandablechannel1stats;
    ExpandableRelativeLayout expandablechannelinfomation;

    ExpandableRelativeLayout expandablechannel2stats;
    ExpandableRelativeLayout expandablereadgraph;
    ExpandableRelativeLayout expandabletripinfomation;
    ExpandableRelativeLayout expandableloggerdetails;


    //Parameter UI Stuff
    private TextView passwordtxt;
    private TextView passwordconfirmtxt;
    private TextView passwordtxtheading;
    private TextView passwordconfirmtxtheading;
    private TextView usercommenttxt;

    private RadioButton celsius;
    private RadioButton fahrenheit;
    private RadioButton startwithdelay;
    private RadioButton stopbyuser;
    private RadioButton startondatetimeR;//
    private RadioButton stopwhenfullR;
    private RadioButton stoponsampleR;
    private RadioButton stopondatetimeR;//

    private RadioGroup imperialunit;
    private RadioGroup startoptions;
    private RadioGroup stopoptions;

    private Button startwithdelaybutton;
    private Button startondatetimebutton;
    private Button sampleperiodbutton;
    private Button stopondatebutton;

    private EditText stoponsamplebutton;
    private EditText ch1upperlimitnb;
    private EditText ch1lowerlimitnb;
    private EditText ch1alarmdelaynb;
    private EditText BLE_Name;
    private EditText ch2upperlimitnb;
    private EditText ch2lowerlimitnb;
    private EditText ch2alarmdelaynb;

    private TextView BLE_heading;

    private Switch BLEenergysave;
    private Switch loopovewritecb;
    private Switch startwithbuttoncb;
    private Switch stopwithbuttoncb;
    private Switch reusewithbuttoncb;
    private Switch allowplacingtagcb;
    private Switch enablelcdmenucb;
    private Switch extendedlcdmenucb;
    private Switch passwordenabledcb;
    private Switch ch1enabledcb;
    private Switch ch1limitenabledcb;
    private Switch ch2enabledcb;
    private Switch ch2limitenabledcb;

    private boolean celsiusfahrenheit = false;
    private TextView tempheading;
    private int whichbutton;
    private boolean Complete = true;
    private AlertDialog.Builder builder1 = null;
    Boolean wantToCloseDialog = false;
    AlertDialog dialogcancel = null;

    private byte[] TWFlash = new byte[144];
    private byte[] RamRead = new byte[100];
    private byte[] UserRead = new byte[512];
    private byte[] ExtraRead = new byte[284];
    private byte[] UserReadtemp = new byte[398];
    private byte[] UserReadtemp_2 = new byte[398];
    private byte[] UserReadtemp_3 = new byte[398];
    private ArrayList<String> Q_data = new ArrayList<String>();
    private ArrayList<String> U_data = new ArrayList<String>();
    private ArrayList<String> F_data = new ArrayList<String>();
    private ArrayList<String> R_data = new ArrayList<String>();
    private boolean done_Reading = false;
    private String mDeviceName;
    private String mDeviceAddress;
    private String message = "0";
    private String parameterString = "";

    private ProgressDialog progress;
    private int progresspercentage;
    private int progressperc = 0;
    private int timeoutdelay;
    private int firsttime = 0;
    private boolean soundon = true;
    private boolean programselect = false;
    StoreKeyService storeKeyService;
    MT2Values.MT2Mem_values mt2Mem_values = new MT2Values.MT2Mem_values();
    MT2Msg_Write mt2Msg_write;
    ProgressTask task;
    private final   Handler         m_handler               = new Handler();
    private Handler handler1 =new Handler();
    Thread t = new Thread();
    private int state = 1;
    private boolean connected = true;
    private ArrayList<Byte> state29 = new ArrayList<Byte>();

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

    //========================temp and humidity update====
    private int part = 0;
    private boolean fromtimer  = false;
    public  static  int     m_next_memory_address   = 0;
    byte[] write = new byte[64];
    public boolean frompart2 = false;
//==================================================

    HexData hexData = new HexData();
    BaseCMD baseCMD =  new BaseCMD();
    CommsSerial commsSerial = new CommsSerial();
    Yasiru_Temp_Library QS = new Yasiru_Temp_Library();
    CommsChar commsChar = new CommsChar();
    MT2Msg_Read mt2Msg_read;
    USBFragmentI usbFragmentI;

    public USBQueryFragment GET_INSTANCE(Bundle data)
    {
        USBQueryFragment fragment = new USBQueryFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            usbFragmentI = (USBFragmentI) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IServiceFragmentInteraction");
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        Stop_Timer();
    }

    @Override
    public  void onStop(){
        Stop_Timer();
        stopProgress();
        super.onStop();
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
    {   stopProgress();
        super.onPause();
        Stop_Timer();
    }
    //==========================================================//

    //==========================================================//
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Stop_Timer();
        stopProgress();
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


        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");



        Bundle bundle = this.getArguments();
        if (bundle != null) {
            message = bundle.getString(EXTRAS_MESSAGE);
        }
        Menubuttons = view.findViewById(R.id.Menubuttons);
        limitlayout = view.findViewById(R.id.limitlayout);
        countdownlayout = view.findViewById(R.id.countdownlayout);
        countdowntext = view.findViewById(R.id.countdowntext);
        countdowntext.setTypeface(font);
        start_stop = view.findViewById(R.id.start_stop);
        start_stop.setOnClickListener(QueryButtonClick);
        start_stop_text = view.findViewById(R.id.start_stop_text);
        read = view.findViewById(R.id.read);
        read.setOnClickListener(QueryButtonClick);
        pdf = view.findViewById(R.id.pdf);
        pdf.setOnClickListener(QueryButtonClick);
        read_text = view.findViewById(R.id.read_text);

        querycurrenttemp = view.findViewById(R.id.querycurrenttemp);
        linearLayout =  view.findViewById(R.id.attop);
        parameterlayout = view.findViewById(R.id.parameterlayout);
        st_at = view.findViewById(R.id.st_at);
        st_by = view.findViewById(R.id.st_by);
        sp_at = view.findViewById(R.id.sp_at);
        sp_by = view.findViewById(R.id.sp_by);
        bleenegy = view.findViewById(R.id.bleenegy);
        bleenegy.setVisibility(view.GONE);
        queryScroll = (ScrollView) view.findViewById(R.id.queryscroll);

        // Sets up UI references.
        bat = (ImageView) view.findViewById(R.id.imageView1);
        temp = (ImageView) view.findViewById(R.id.imageView2);
        hu = (ImageView) view.findViewById(R.id.imageView3);

        limitstatus = view.findViewById(R.id.limitstatus);
        limiticon = view.findViewById(R.id.limiticon);
        usercomment = view.findViewById(R.id.usercomment);
        currentTemp = (TextView) view.findViewById(R.id.temperature);
        //currentTemp.setTypeface(font);
        currenthumidity = (TextView) view.findViewById(R.id.humiditytop);
        //currenthumidity.setTypeface(font);
//        time = (TextView) view.findViewById(R.id.time);
//        time.setTypeface(font);
        //mConnectionState = (TextView) view.findViewById(R.id.connection_state);
        Lstate = (TextView) view.findViewById(R.id.state);
        //Lstate.setTypeface(font);
        battery = (TextView) view.findViewById(R.id.battery);
        //battery.setTypeface(font);

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

        LoggedTags = (TextView) view.findViewById(R.id.loggedtags);
        FirstSample = (TextView) view.findViewById(R.id.firstsample);
        FirstLoggedSample = (TextView) view.findViewById(R.id.firstloggedsample);
        LastSample = (TextView) view.findViewById(R.id.lastsample);
        LastLoggedSample = (TextView) view.findViewById(R.id.lastloggedsample);

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

        channel1 = (TextView) view.findViewById(R.id.channel1query);
        upperLimit1 = (TextView) view.findViewById(R.id.upperlimit1query);
        lowerLimit1 = (TextView) view.findViewById(R.id.lowerlimit1query);
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


        channel2 = (TextView) view.findViewById(R.id.channel2query);
        upperLimit2 = (TextView) view.findViewById(R.id.upperlimit2query);
        lowerLimit2 = (TextView) view.findViewById(R.id.lowerlimit2query);
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
        Temp2 = view.findViewById(R.id.temp2);
        Humidity2 = (LinearLayout) view.findViewById(R.id.humidity2);
        Temp2.setVisibility(View.GONE);
        Humidity2.setVisibility(View.GONE);
        Graph1 = (WebView) view.findViewById(R.id.graphone);
        Graph1.setFocusableInTouchMode(false);
        Graph1.setFocusable(false);
//        mConnectionState.setText(getString(R.string.USB_Connected));


        //task = new ProgressTask();
        showProgress();
        appversion = (TextView) view.findViewById(R.id.appversion);
        androidversion = (TextView) view.findViewById(R.id.androidversion);

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        int version = Build.VERSION.SDK_INT;
        String versionRelease = Build.VERSION.RELEASE;

        appversion.setText(getString(R.string.appversion) + getString(R.string.App_version));
        androidversion.setText(getString(R.string.manufacturer) + manufacturer
                + " \n" +getString(R.string.devicemode)+ model
                + " \n" +getString(R.string.version)+ version
                + " \n" +getString(R.string.versionrelease)+ versionRelease+"\n\nsupport@temprecord.com");
        usbFragmentI.onUSBWrite(HexData.QUARY_USB);
//        bleFragmentI.onBLERead();
        view.findViewById(R.id.Visitweb).setOnClickListener(QueryButtonClick);
        view.findViewById(R.id.bVisit).setOnClickListener(QueryButtonClick);
        view.findViewById(R.id.bVisit1).setOnClickListener(QueryButtonClick);
        view.findViewById(R.id.querycurrenttemp).setOnClickListener(QueryButtonClick);
        view.findViewById(R.id.zoominButton).setOnClickListener(QueryButtonClick);
        limitlayout.setOnClickListener(QueryButtonClick);
        querygraphlayout = view.findViewById(R.id.querygraphlayout);

        queryloggerdetails = view.findViewById(R.id.queryloggerdetails);
        querytripinfomation = view.findViewById(R.id.querytripinfomation);
        querychannelinfo = view.findViewById(R.id.querychannelinfo);
        querychannel1stats = view.findViewById(R.id.querychannel1stats);
        querychannel2stats = view.findViewById(R.id.querychannel2stats);
        querygraphlayout = view.findViewById(R.id.querygraphlayout);
        helplayout = view.findViewById(R.id.helplayout);
        parametersparent = view.findViewById(R.id.parametersparent);
        more = view.findViewById(R.id.more);

        queryloggerdetails.setOnLongClickListener(longpress);
        querytripinfomation.setOnLongClickListener(longpress);
        querychannelinfo.setOnLongClickListener(longpress);
        querychannel1stats.setOnLongClickListener(longpress);
        querychannel2stats.setOnLongClickListener(longpress);
        querygraphlayout.setOnLongClickListener(longpress);
        helplayout.setOnLongClickListener(longpress);
        parametersparent.setOnLongClickListener(longpress);

        queryloggerdetails.setOnClickListener(clicked);
        querytripinfomation.setOnClickListener(clicked);
        querychannelinfo.setOnClickListener(clicked);
        querychannel1stats.setOnClickListener(clicked);
        querychannel2stats.setOnClickListener(clicked);
        querygraphlayout.setOnClickListener(clicked);
        helplayout.setOnClickListener(clicked);

        more.setOnClickListener(clicked);
        parametersparent.setOnClickListener(clicked);

        expandablemore = (ExpandableRelativeLayout) view.findViewById(R.id.expandablemore);
        expandablehelp = (ExpandableRelativeLayout) view.findViewById(R.id.expandablehelp);
        expandablechannel1stats = (ExpandableRelativeLayout) view.findViewById(R.id.expandablechannel1stats);
        expandablechannelinfomation = (ExpandableRelativeLayout) view.findViewById(R.id.expandablechannelinformation);
        expandablechannel2stats = (ExpandableRelativeLayout) view.findViewById(R.id.expandablechannel2stats);
        expandablereadgraph = (ExpandableRelativeLayout) view.findViewById(R.id.expandablequerygraph);
        expandabletripinfomation = (ExpandableRelativeLayout) view.findViewById(R.id.expandabletripinformation);
        expandableloggerdetails = (ExpandableRelativeLayout) view.findViewById(R.id.expandableloggerdetails);


        collapsAll();
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_query, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }


    /*********************************************************************************************
     * Expandable layouts in the Fragment
     * performs Expand and collapse actions depending on its current state
     * ********************************************************************************************/
    private View.OnClickListener clicked = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.queryloggerdetails:
                    expandableloggerdetails.toggle(); // toggle expand and collapse
                    if(expandableloggerdetails.isExpanded())
                        queryloggerdetails.setBackground(getResources().getDrawable(R.drawable.layout_txtbg));
                    else
                        queryloggerdetails.setBackgroundColor(getResources().getColor(R.color.gray_bg_color));

                    break;
                case R.id.querytripinfomation:
                    expandabletripinfomation.toggle(); // toggle expand and collapse
                    expandablemore.collapse();
                    if(expandabletripinfomation.isExpanded())
                        querytripinfomation.setBackground(getResources().getDrawable(R.drawable.layout_txtbg));
                    else
                        querytripinfomation.setBackgroundColor(getResources().getColor(R.color.gray_bg_color));

                    break;
                case R.id.querychannelinfo:
                    expandablechannelinfomation.toggle(); // toggle expand and collapse
                    if(expandablechannelinfomation.isExpanded())
                        querychannelinfo.setBackground(getResources().getDrawable(R.drawable.layout_txtbg));
                    else
                        querychannelinfo.setBackgroundColor(getResources().getColor(R.color.gray_bg_color));

                    break;
                case R.id.querychannel1stats:
                    expandablechannel1stats.toggle(); // toggle expand and collapse
                    if(expandablechannel1stats.isExpanded())
                        querychannel1stats.setBackground(getResources().getDrawable(R.drawable.layout_txtbg));
                    else
                        querychannel1stats.setBackgroundColor(getResources().getColor(R.color.gray_bg_color));

                    break;
                case R.id.querychannel2stats:
                    expandablechannel2stats.toggle(); // toggle expand and collapse
                    if(expandablechannel2stats.isExpanded())
                        querychannel2stats.setBackground(getResources().getDrawable(R.drawable.layout_txtbg));
                    else
                        querychannel2stats.setBackgroundColor(getResources().getColor(R.color.gray_bg_color));


                    break;
                case R.id.querygraphlayout:
                    expandablereadgraph.toggle(); // toggle expand and collapse
                    if(expandablereadgraph.isExpanded())
                        querygraphlayout.setBackground(getResources().getDrawable(R.drawable.layout_txtbg));
                    else
                        querygraphlayout.setBackgroundColor(getResources().getColor(R.color.gray_bg_color));

                    break;
                case R.id.helplayout:
                    expandablehelp.toggle(); // toggle expand and collapse
                    if(expandablehelp.isExpanded())
                        helplayout.setBackground(getResources().getDrawable(R.drawable.layout_txtbg));
                    else
                        helplayout.setBackgroundColor(getResources().getColor(R.color.gray_bg_color));

                    break;
                case R.id.more:
                    expandablemore.toggle(); // toggle expand and collapse
                    if(expandablemore.isExpanded())
                        more.setText(getString(R.string.moreinfo));
                    else
                        more.setText(getString(R.string.lessinfo));
                    break;
                case R.id.parametersparent:
//                    expandableparameters.toggle(); // toggle expand and collapse
//                    if(expandableparameters.isExpanded())
//                        parametersparent.setBackground(getResources().getDrawable(R.drawable.layout_txtbg));
//                    else
//                        parametersparent.setBackgroundColor(getResources().getColor(R.color.gray_bg_color));
//                    collapsAllParameters();
                    parameterDialogue();
                    break;

            }
        }
    };

    /**
     * Long press on the headings will collapse all open expandable views
     * */
    private View.OnLongClickListener longpress = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.queryloggerdetails:
                    collapsAll();
                    break;
                case R.id.querytripinfomation:
                    collapsAll();
                    break;
                case R.id.querychannelinfo:
                    collapsAll();
                    break;
                case R.id.querychannel1stats:
                    collapsAll();
                    break;
                case R.id.querychannel2stats:
                    collapsAll();
                    break;
                case R.id.querygraphlayout:
                    collapsAll();
                    break;
                case R.id.helplayout:
                    collapsAll();
                    break;
                case R.id.more:
                    collapsAll();
                    break;
                case R.id.parametersparent:
                    collapsAll();
                    break;
            }
            return false;
        }

    };

    /**
    * Handle button action on the Fragment
    * Include the circle button
    * full screen graph
    * limit breach layout
    * */
    private View.OnClickListener QueryButtonClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.zoominButton:
                    Intent intent = new Intent(getActivity(), GraphAcivity.class);
                    intent.putExtra("VALUE", new Json_Data(mt2Mem_values, baseCMD, 0,getActivity()).CreateObject());
                    startActivity(intent);
                    break;
                case R.id.pdf:
                    if(done_Reading) {
                        Stop_Timer();
                        if(baseCMD.state == 5) {
                            Show_PDF_Dialogue();
                        }else{
                            if(connected)
                                BuildDialogue(getString(R.string.Moreavailable),getString(R.string.more),3,true,getString(R.string.Yes));
                            else
                                Show_PDF_Dialogue();
                        }
                    }else if(!done_Reading && connected)
                        Toast.makeText(getActivity(), getString(R.string.wait), Toast.LENGTH_SHORT).show();
                    else if (!done_Reading && !connected)
                        Toast.makeText(getActivity(), getString(R.string.removed), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.read:
                    if(done_Reading) {
                        Stop_Timer();
                        done_Reading = false;
                        getActivity().getActionBar().setTitle(R.string.QueryLogger);
                        usbFragmentI.onUSBWrite(HexData.BLE_ACK);
                        message = "1";
                        state = 1;
                        //task = new ProgressTask();
                        showProgress();
                    }else
                        Toast.makeText(getActivity(), getString(R.string.wait), Toast.LENGTH_LONG).show();
                    break;
                case R.id.start_stop:
                    if (done_Reading) {
                        Stop_Timer();
                        FragmentUIupdate();
                        done_Reading = false;
                        switch (baseCMD.state) {
                            case 2:
                                getActivity().getActionBar().setTitle(R.string.StartLogger);
                                usbFragmentI.onUSBWrite(HexData.BLE_ACK);
                                message = "2";
                                state = 1;
                                //task = new ProgressTask();
                                showProgress();
                                break;
                            case 4:
                                getActivity().getActionBar().setTitle(R.string.StopLogger);
                                usbFragmentI.onUSBWrite(HexData.BLE_ACK);
                                message = "3";
                                state = 1;
                                //task = new ProgressTask();
                                showProgress();
                                break;
                            case 5:
                                BuildDialogue(getString(R.string.warning), getString(R.string.startwarning), 2, true,getString(R.string.Yes));
                                break;
                        }
                    }else
                        Toast.makeText(getActivity(), getString(R.string.wait), Toast.LENGTH_LONG).show();
                    break;
                case R.id.querycurrenttemp:
                    imperial = !imperial;
                    StoreKeyService.setDefaults("UNITS", String.valueOf(imperial?1:0), App.getContext());
                    SetUI();
                    plotGraph1(1);
                    if(done_Reading)SetUI_stage2();

                    break;
                case R.id.bVisit:
                    visitAccuracyCertificate();
                    break;
                case R.id.bVisit1:
                    visitAccuracyCertificate();
                    break;
                case R.id.Visitweb:
                    visitweb();
                    break;
                case R.id.limitlayout:
                    if(done_Reading){
                        String note = "";
                        boolean breach = false;
                        if((baseCMD.querych1hi > baseCMD.ch1Hi)){
                            breach = true;
                            note += getString(R.string.tempupperB);
                        }
                        if(baseCMD.querych1lo < baseCMD.ch1Lo){
                            breach = true;
                            note += "\n"+getString(R.string.templowerB);
                        }
                        if(baseCMD.ch2Enable) {
                            if (baseCMD.querych2hi > baseCMD.ch2Hi) {
                                breach = true;
                                note += "\n"+getString(R.string.humupperB);
                            }
                            if (baseCMD.querych2lo < baseCMD.ch2Lo) {
                                breach = true;
                                note += "\n"+getString(R.string.humlowerB);
                            }
                        }
                        if(breach) {
                            breach = false;
                            BuildDialogue(getString(R.string.limitbreached), note, 6, false, getString(R.string.Ok));
                        }
                    }
                    break;


            }
        }
    };


/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////
 * UI updates
 * ////////////////////////////////////////////////////////////////////////////////////////////////
 * */

    /**********************************************************************************************
    * Collapse all the expandable layouts
    * Used in long press of the heading which collapse all teh opened layouts
    * ********************************************************************************************/
    public void collapsAll(){
        queryloggerdetails.setBackgroundColor(getResources().getColor(R.color.contentback));
        querytripinfomation.setBackgroundColor(getResources().getColor(R.color.contentback));
        querychannelinfo.setBackgroundColor(getResources().getColor(R.color.contentback));
        querychannel1stats.setBackgroundColor(getResources().getColor(R.color.contentback));
        querychannel2stats.setBackgroundColor(getResources().getColor(R.color.contentback));
        querygraphlayout.setBackgroundColor(getResources().getColor(R.color.contentback));
        helplayout.setBackgroundColor(getResources().getColor(R.color.contentback));
        parametersparent .setBackgroundColor(getResources().getColor(R.color.contentback));
        expandablemore.collapse();
        expandablehelp.collapse();
        expandablechannel1stats.collapse();
        expandablechannelinfomation.collapse();
        expandablechannel2stats.collapse();
        expandablereadgraph.collapse();
        expandabletripinfomation.collapse();
        expandableloggerdetails.collapse();
    }

    /**********************************************************************************************
     * Plots the graph after background reading is complete
     * @param viewtype pass 1 or 0 which controlles the complexity of the graph shown
     * *******************************************************************************************/
    public void plotGraph1(int viewtype){
        querygraphlayout.setVisibility(View.VISIBLE);
        Graph1.getSettings().setJavaScriptEnabled(true);
        Graph1.addJavascriptInterface(this,"android");
        //Graph1.requestFocusFromTouch();
        Graph1.setWebViewClient(new WebViewClient());
        Graph1.setWebChromeClient(new WebChromeClient());
        json_data = new Json_Data(mt2Mem_values, baseCMD, viewtype,getActivity());
        Graph1.loadUrl("file:///android_asset/highcharts.html");



    }

    /*********************************************************************************************
     * Sets the UI after finishing a query
     * only update the summary sections eg. logger details, trip info
     * *******************************************************************************************/
    @SuppressLint("ClickableViewAccessibility")
    public void SetUI(){
        FragmentUIupdate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss aa");
        //Log.i("TIME", " " + TimeZone.getDefault().getDisplayName() + "____" + sdf.getTimeZone().getDisplayName());
        serialno.setText(Q_data.get(0));
        firmware.setText(Q_data.get(1));


        model.setText(QS.GetType(Integer.parseInt(Q_data.get(4))));


        if((baseCMD.querych1hi < baseCMD.ch1Hi) && (baseCMD.querych1lo > baseCMD.ch1Lo) && (baseCMD.querych2hi < baseCMD.ch2Hi) && (baseCMD.querych2lo > baseCMD.ch2Lo)){//drawing the tick if within limits
            limitstatus.setText(App.getContext().getString(R.string.within_limits));
            limiticon.setBackgroundResource(R.drawable.greentick);
        }else{//drawing the warning sign if out of limits
            limitstatus.setText(App.getContext().getString(R.string.outof_limits));
            limiticon.setBackgroundResource(R.drawable.redwarning);
        }


        Lstate.setText( QS.GetState(Integer.parseInt(Q_data.get(5))).toUpperCase());
        battery.setText(  R_data.get(17)+"%");
        if (storeKeyService.getDefaults("UNITS", getActivity().getApplication()) != null && storeKeyService.getDefaults("UNITS", getActivity().getApplication()).equals("1")) {
            currentTemp.setText(R_data.get(9) + " °C");
            ch1ul.setText(U_data.get(15) + " °C");
            ch1ll.setText(U_data.get(16) + " °C");
        }else {
            currentTemp.setText(String.format("%1$,.2f", QS.returnFD(Double.parseDouble(R_data.get(9)))) + " °F");
            ch1ul.setText(String.format("%1$,.2f", QS.returnFD(Double.parseDouble(R_data.get(15)))) + " °F");
            ch1ll.setText(String.format("%1$,.2f", QS.returnFD(Double.parseDouble(R_data.get(16)))) + " °F");
        }
        currenthumidity.setText(R_data.get(13) + " %");
        if(!(baseCMD.state == 4 || baseCMD.state == 5)){
            currentTemp.setText("__._ °C");
            currenthumidity.setText("__._ %");
        }




        manudate.setText(QS.UTCtoLocal(baseCMD.dmanu.getTime()));
        memory.setText(F_data.get(1));
        startondatetime.setText(R_data.get(0));
        startwithbutton.setText(R_data.get(1));
        stopwithbutton.setText(R_data.get(3));
        stopwhenfull.setText(R_data.get(5));
        stoponsample.setText(R_data.get(6));




        loggedsamples.setText(R_data.get(7));
        tripsamples.setText(R_data.get(7));
        //lastsample.setText(R_data.get(8));


        sampleno.setText(U_data.get(5));
        trips.setText(U_data.get(3));
        //energysave.setText(QS.YesorNo(baseCMD.energysave));
        //Log.i("TEST", "TIME IN +++++++++++++" + baseCMD.dstart.getTime());
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
            if(baseCMD.StoponDateTime){
                stoppedby.setText(getString(R.string.DateTime));
            }else{
                stoppedby.setText(R_data.get(4));
            }

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
        if(baseCMD.ch2Enable)
            units.setText(QS.imperial(baseCMD.ImperialUnit) + "  and  %");
        else
            units.setText(QS.imperial(baseCMD.ImperialUnit));



        //time.setText(QS.UTCtoLocal(new Date().getTime()));

        ch1alarmdelay.setText( R_data.get(12)+" "+getString(R.string.Samples));
        ch2alarmdelay.setText( R_data.get(16)+" "+getString(R.string.Samples));


        if(Double.parseDouble(Q_data.get(6)) > 66){
            bat.setImageResource(R.drawable.bt_full);
//            bat.getLayoutParams().width = 150;
//            bat.getLayoutParams().height = 150;
        }else if(Double.parseDouble(Q_data.get(6)) > 33){
            bat.setImageResource(R.drawable.bt_half);
//            bat.getLayoutParams().width = 150;
//            bat.getLayoutParams().height = 150;
        }else if(Double.parseDouble(Q_data.get(6)) > 0){
            bat.setImageResource(R.drawable.bt_half2);
//            bat.getLayoutParams().width = 150;
//            bat.getLayoutParams().height = 150;
        }else if(Double.parseDouble(Q_data.get(6)) == 0){
            bat.setImageResource(R.drawable.bt_low);
//            bat.getLayoutParams().width = 150;
//            bat.getLayoutParams().height = 150;
        }

        if(U_data.get(27).trim().isEmpty()){
            usercomment.setVisibility(View.GONE);
        }usercomments.setText( getString(R.string.comment)+"  "+U_data.get(27));
        if(!baseCMD.ch2Enable){
            currenthumidity.setText("__._ %");
        }

        if(!(baseCMD.state == 4 || baseCMD.state == 5))
            querygraphlayout.setVisibility(View.GONE);

        if(baseCMD.state == 3){
            Countdowntimer(baseCMD.getStartDelaycounter*1000);
        }


    }

    /*********************************************************************************************
     * Updates the UI after a full read in the background
     * update Temperature stats, Humidity stats
     * ********************************************************************************************/
    public void SetUI_stage2(){
        FragmentUIupdate();
        if(baseCMD.numberofsamples != 0) {

            loggedsamples.setText(String.valueOf(mt2Mem_values.Data.size()));
            tripsamples.setText(String.valueOf(mt2Mem_values.Data.size()));
            LoggedTags.setText(mt2Mem_values.TagCount() + "");
            FirstSample.setText(QS.UTCtoLocal(mt2Mem_values.Data.get(0).valTime.getTime()));
            FirstLoggedSample.setText(QS.UTCtoLocal(mt2Mem_values.Data.get(0).valTime.getTime()));
            LastSample.setText(QS.UTCtoLocal(mt2Mem_values.Data.get(mt2Mem_values.Data.size() - 1).valTime.getTime()));
            LastLoggedSample.setText(QS.UTCtoLocal(mt2Mem_values.Data.get(mt2Mem_values.Data.size() - 1).valTime.getTime()));
        }

        Temp2.setVisibility(View.GONE);
        if(baseCMD.ch1Enable && baseCMD.numberofsamples != 0) {
            Temp2.setVisibility(View.VISIBLE);
            channel1.setText("Temperature");
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
                max1.setText(String.format("%.1f",QS.returnFD(mt2Mem_values.ch0Stats.Max.Value / 10.0)) + " °F\n" + max1time);
                String min1time = QS.UTCtoLocal(mt2Mem_values.Data.get(mt2Mem_values.ch0Stats.Min.Number - 1).valTime.getTime());
                min1.setText(String.format("%.1f",QS.returnFD(mt2Mem_values.ch0Stats.Min.Value / 10.0)) + " °F\n" + min1time);
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
        Humidity2.setVisibility(View.GONE);
        if(baseCMD.ch2Enable && baseCMD.numberofsamples != 0) {
            Humidity2.setVisibility(View.VISIBLE);
            channel2.setText("Humidity");
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
            totalPercentageoutofLimits2.setText(String.format("%.2f",mt2Mem_values.ch1Stats.TotalPercent) + " %");

            samplesaboveUpperLimit2.setText(mt2Mem_values.ch1Stats.AboveLimit + "");
            timeaboveUpperLimit2.setText(mt2Mem_values.ch1Stats.AboveTime + "");
            percentageaboveUpperLimit2.setText(String.format("%.2f",mt2Mem_values.ch1Stats.AbovePercent) + " %");

            samplesbelowLowerLimit2.setText(mt2Mem_values.ch1Stats.BelowLimit + "");
            timebelowLowerSamples2.setText(mt2Mem_values.ch1Stats.BelowTime);
            percentagebelowLowerSample2.setText(String.format("%.2f",mt2Mem_values.ch1Stats.BelowPercent) + " %");
        }
//        expandablechannel1stats.initLayout();
//        expandablechannel2stats.initLayout();
        //collapsAll();
    }

    /*********************************************************************************************
     * Updates the UI depending on what state the logger is in
     * update the circle buttons and expandable layouts
     * ********************************************************************************************/
    public void FragmentUIupdate(){
        switch (baseCMD.state){
            case 2://ready
                countdownlayout.setVisibility(View.GONE);
                limitlayout.setVisibility(View.GONE);
                start_stop.setClickable(true);
                start_stop.setBackground(getResources().getDrawable(R.drawable.startc));
                //start_stop.setImageResource(R.drawable.ic_start);
                start_stop_text.setText(getString(R.string.Start));
                read.setClickable(false);
                read.setBackground(getResources().getDrawable(R.drawable.readg));
                //read.setImageResource(R.drawable.ic_read);
                read_text.setText(getString(R.string.ReadLogger));
                pdf.setClickable(false);
                pdf.setBackground(getResources().getDrawable(R.drawable.pdfg));
                parameterlayout.setVisibility(View.VISIBLE);
                Menubuttons.setVisibility(View.VISIBLE);
                querytripinfomation.setVisibility(View.GONE);
                break;
            case 3://start delay
                countdownlayout.setVisibility(View.VISIBLE);
                limitlayout.setVisibility(View.GONE);
                start_stop.setClickable(false);
                start_stop.setBackground(getResources().getDrawable(R.drawable.stopg));
                read.setClickable(true);
                read.setBackground(getResources().getDrawable(R.drawable.readc));
                //read.setImageResource(R.drawable.ic_info);
                read_text.setText(getString(R.string.QueryLogger));
                pdf.setClickable(false);
                pdf.setBackground(getResources().getDrawable(R.drawable.graybutton));
                parameterlayout.setVisibility(View.GONE);
                Menubuttons.setVisibility(View.GONE);
                querytripinfomation.setVisibility(View.VISIBLE);
                querycurrenttemp.setClickable(false);
                break;
            case 4://running
                countdownlayout.setVisibility(View.GONE);
                limitlayout.setVisibility(View.VISIBLE);
                start_stop.setClickable(true);
                start_stop.setBackground(getResources().getDrawable(R.drawable.stopc));
                //start_stop.setImageResource(R.drawable.ic_stop);
                start_stop_text.setText(getString(R.string.Stop));
                read.setClickable(true);
                read.setBackground(getResources().getDrawable(R.drawable.readc));
                read_text.setText(getString(R.string.ReadLogger));
                //read.setImageResource(R.drawable.ic_read);
                pdf.setClickable(true);
                pdf.setBackground(getResources().getDrawable(R.drawable.pdf1c));
                parameterlayout.setVisibility(View.GONE);
                Menubuttons.setVisibility(View.VISIBLE);
                querytripinfomation.setVisibility(View.VISIBLE);
                querycurrenttemp.setClickable(true);
                break;
            case 5://stopped
                countdownlayout.setVisibility(View.GONE);
                limitlayout.setVisibility(View.VISIBLE);
                start_stop.setClickable(true);
                start_stop.setBackground(getResources().getDrawable(R.drawable.startc));
                //start_stop.setImageResource(R.drawable.ic_start);
                start_stop_text.setText(getString(R.string.Start));
                read.setClickable(true);
                read.setBackground(getResources().getDrawable(R.drawable.readc));
                read_text.setText(getString(R.string.ReadLogger));
                //read.setImageResource(R.drawable.ic_read);
                pdf.setClickable(true);
                pdf.setBackground(getResources().getDrawable(R.drawable.pdf1c));
                parameterlayout.setVisibility(View.VISIBLE);
                Menubuttons.setVisibility(View.VISIBLE);
                querytripinfomation.setVisibility(View.VISIBLE);
                Stop_Timer();
                break;
            default://
                start_stop.setClickable(false);
                read.setClickable(false);
                pdf.setClickable(false);
                break;

        }
    }


    /*********************************************************************************************
     * Used when the logger is in the start delay state
     * This will do a countdown and will re read the logger after  the start delay
     *
     * @param time time left for the logger  to start
     * ********************************************************************************************/
    private void Countdowntimer(int time){
        countDownTimer = new CountDownTimer(time+1000, 1000) {

            public void onTick(long millisUntilFinished) {
                countdowntext.setText(QS.msToString(millisUntilFinished));
            }

            public void onFinish() {
                done_Reading = false;
                getActivity().getActionBar().setTitle(R.string.QueryLogger);
                usbFragmentI.onUSBWrite(HexData.BLE_ACK);
                message = "1";
                state = 1;
                //task = new ProgressTask();
                showProgress();
            }
        }.start();
    }


/**
 * /////////////////////////////////////////////////////////////////////////////////////////////////
 * Commincation between the MainActivty and Fragment
 * /////////////////////////////////////////////////////////////////////////////////////////////////
 *
 * */
    /**********************************************************************************************
     * Handles all the communications between the main activity and the fragment
     *state machine changes as new data arrives in the mainactiity
     *
     *@param in byte array of data sent from the MainActivity INTERNAL BROADCAST RECEIVER
     * *******************************************************************************************/
    public void CommsI(final byte[] in){
//        LogThings("in " + state);
        //Log.i("TEST", "******************************************** Going to CONNECTED======== " + state);
//        Runnable runnableCode = new Runnable() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void run() {
        byte[] query;


        if (storeKeyService.getDefaults("SOUND", App.getContext()) != null && storeKeyService.getDefaults("SOUND",  App.getContext()).equals("1"))
            soundon = true;
        else
            soundon = false;

        //progresspercentage = progresspercentage + 5;
        switch (state) {
            case 0:
                connected = true;
//                        hexData.BytetoHex(in);
                query = commsSerial.ReadUSBByte(in);
                Q_data = baseCMD.CMDQuery(query);
//                        SetUI(Q_data);

                if (message.equals("2")) {
                    usbFragmentI.onUSBWrite(HexData.START_USB);
                    state = 2;
                } else if (message.equals("3")) {
                    usbFragmentI.onUSBWrite(HexData.STOP_USB);
                    state = 3;
                } else if (message.equals("4")) {
                    state = 30;
                    programbutton();
                    if(Complete)
                        usbFragmentI.onUSBWrite(HexData.REUSE_USB);
                } else if (message.equals("5")) {
                    usbFragmentI.onUSBWrite(HexData.TAG_USB);
                    state = 5;
                }else if(message.equals("1")){
                    usbFragmentI.onUSBWrite(HexData.QUARY_USB);
                    state = 8;
                    //mBluetoothLeService.disconnect();
                }else if (message.equals("8")) {
                    usbFragmentI.onUSBWrite(HexData.FIND_USB);
                    state = 5;
                }
                break;
            case 1:

                //Log.i("TEST", "start reading in fragmant ");
                if(baseCMD.passwordEnabled) {
                    if (message.equals("3")) {
                        promtPassword(3);
                    } else if (message.equals("4")) {
                        promtPassword(4);
                    } else{
                        usbFragmentI.onUSBWrite(HexData.QUARY_USB);
                    }
                }else
                    usbFragmentI.onUSBWrite(HexData.QUARY_USB);
                state = 0;

                //firsttime = 0;
                break;
            case 2:
                //commsSerial.BytetoHex(in);
                if (in[0] == 0x00) {

                    makesound(getActivity(), R.raw.definite);
                } else {
                    BuildDialogue(getString(R.string.Start), getString(R.string.StartNotSuccessfull), 0, false,getString(R.string.Ok));
                    makesound(getActivity(), R.raw.unsure);
                }
                SystemClock.sleep(1000);
                usbFragmentI.onUSBWrite(HexData.QUARY_USB);

                state = 8;
                break;
            case 3:
                commsSerial.BytetoHex(in);
                if (in[0] == 0x00) {
                    makesound(getActivity(), R.raw.definite);
                } else {
                    BuildDialogue(getString(R.string.Stop), getString(R.string.StopNotSuccessfull), 0, false,getString(R.string.Ok));
                    makesound(getActivity(), R.raw.unsure);
                }
                SystemClock.sleep(1000);
                usbFragmentI.onUSBWrite(HexData.QUARY_USB);

                state = 8;
                break;
            case 4:
                commsSerial.BytetoHex(in);
                if (in[0] == 0x00) {
                    makesound(getActivity(), R.raw.definite);
                } else {
                    BuildDialogue(getString(R.string.Reuse), getString(R.string.ReuseNotSuccessfull), 0, false,getString(R.string.Ok));
                    makesound(getActivity(), R.raw.unsure);
                }
                SystemClock.sleep(1000);
                usbFragmentI.onUSBWrite(HexData.QUARY_USB);

                state = 8;
                break;
            case 5:
                commsSerial.BytetoHex(in);
                if (message.equals("8")) {
                    if (in[0] == 0x00) {
                        makesound(getActivity(), R.raw.definite);
                    } else {
                        BuildDialogue(getString(R.string.FindLogger), getString(R.string.FindNotSuccessfull), 0, false,getString(R.string.Ok));
                        makesound(getActivity(), R.raw.unsure);
                    }
                }else{
                    if (in[0] == 0x00) {
                        makesound(getActivity(), R.raw.definite);
                    } else {
                        BuildDialogue(getString(R.string.Tag), getString(R.string.TagNotSuccessfull), 0, false,getString(R.string.Ok));
                        makesound(getActivity(), R.raw.unsure);
                    }
                }
                SystemClock.sleep(1000);
                usbFragmentI.onUSBWrite(HexData.QUARY_USB);

                state = 8;
                break;
            case 6:
                break;
            case 7:
//                        hexData.BytetoHex(in);
//                        query = baseCMD.ReadByte(in);
//                        Q_data = baseCMD.CMDQuery(query);
                //usbFragmentI.onUSBWrite(HexData.GO_TO_SLEEP);
                state = 6;
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



                break;
            case 8:
                //hexData.BytetoHex(in);

                query = commsSerial.ReadUSBByte(in);
                Q_data = baseCMD.CMDQuery(query);


                //sets up reading the first 120 bytes from flash, then the message is constructed with 0x55 and stuff
                mt2Msg_read = new MT2Msg_Read(commsChar.MEM_TRW, 0, 120, 100, 3);
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
                    // hexData.BytetoHex(TWFlash);
                    F_data = baseCMD.CMDFlash(TWFlash);

                    // call the decoding functon here to decode all we need and send the data back in an string array so that they can be desplayed in appropriate textviews
                }
                usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(false)));
                break;
            case 12:
                mt2Msg_read = new MT2Msg_Read(commsChar.MEM_USER, 0, 250, 250, 3);
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
                mt2Msg_read = new MT2Msg_Read(commsChar.MEM_RAM, 0, 100, 100, 3);
                usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(true)));
                state = 15;
                break;
            case 15:
                if(mt2Msg_read.write_into_readByte(commsSerial.ReadUSBByte(in))) {
                    System.arraycopy(mt2Msg_read.memoryData, 0, RamRead, 0, 100);
                    //hexData.BytetoHex(RamRead);
                    R_data = baseCMD.CMDRamRead(RamRead);
                    if(fromtimer == true){
                        state = 29;
                        fromtimer = false;
                    }else
                        state = 16;
                }
                usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(false)));

                break;
            case 16:
                mt2Msg_read = new MT2Msg_Read(commsChar.MEM_USER, 250, 500, 250, 3);
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
                mt2Msg_read = new MT2Msg_Read(commsChar.MEM_USER, 500, 512, 250, 3);
                usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(true)));
                state = 19;
                break;
            case 19:
                if(mt2Msg_read.write_into_readByte(commsSerial.ReadUSBByte(in))) {
                    state = 20;
                    System.arraycopy(mt2Msg_read.memoryData, 0, UserRead, 500, 12);
                    //  hexData.BytetoHex(UserRead);
                    U_data = baseCMD.CMDUserRead(UserRead);

                }
                usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(false)));
                break;
            case 20:
                mt2Msg_read = new MT2Msg_Read(commsChar.MEM_EXTRA, 114, 350, 300, 3);
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
                    SetUI();

                    //   hexData.BytetoHex(ExtraRead);
                    //progresspercentage = 100;
                    stopProgress();
                }
                usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(false)));
                break;
            case 24:// the set up needs to change if loop over right is active.

                if(baseCMD.numberofsamples == 0 || done_Reading) {
                    done_Reading=true;
                    if(!(baseCMD.state == 4 || baseCMD.state == 5)){
                        //BuildDialogue("No Data","Logger is not in running or stop state to display data", 1);
                    }else
//                                SetUI();
                        //progresspercentage = 100;

                    usbFragmentI.onUSBWrite(HexData.BLE_ACK);
                    state = 29;
                }
                else {
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
                    //Toast.makeText(getActivity(), "Background Reading DONE", Toast.LENGTH_LONG).show();
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
                        Start_Timer();
                        done_Reading = true;
                        SetUI_stage2();
                        //SetUI();//fill ui elements
//                                progresspercentage = 100;
//                                stopProgress();
                    }
//                            state = 29;
//                            Toast.makeText(getActivity(), "Background Reading DONE", Toast.LENGTH_LONG).show();
                    //Log.i("SPEED", "Stop READ");

                }
                // Log.i("SPEED", "in state 25");
                usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(false)));
                //Update_UI(App.getContext(), null, "HID_USB_Message_Received", ValueRead, null);
                break;
            case 26:
//                LogThings(baseCMD.MemorySizeMax+" "+ baseCMD.SamplePointer);
//                hexData.BytetoHex(in);
                if (mt2Msg_read.write_into_readByte(commsSerial.ReadUSBByte(in))) {
                    state = 27;
                    ReadValues1 = new byte[mt2Msg_read.memoryData.length];
                    System.arraycopy(mt2Msg_read.memoryData, 0, ReadValues1, 0, mt2Msg_read.memoryData.length);

                }
                usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(false)));

                break;
            case 27:

                mt2Msg_read = new MT2Msg_Read(commsChar.MEM_VAL, 0, bytePointer, 64, 3);
                usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(true)));
                state = 28;
                break;
            case 28:
                if (mt2Msg_read.write_into_readByte(commsSerial.ReadUSBByte(in))) {

                    state = 29;
                    //Toast.makeText(getActivity(), "Background Reading DONE", Toast.LENGTH_LONG).show();
                    ReadValues2 = new byte[mt2Msg_read.memoryData.length];
                    byte[] combo = new byte[ReadValues2.length  + ReadValues1.length];
                    System.arraycopy(mt2Msg_read.memoryData, 0, ReadValues2, 0, mt2Msg_read.memoryData.length);
                    System.arraycopy(ReadValues1,0,combo,0,ReadValues1.length-1);
                    System.arraycopy(ReadValues2,0,combo,ReadValues1.length,ReadValues2.length);
                    MT2ValueIn(combo);
                    plotGraph1(1);
                    if(!(baseCMD.state == 4 || baseCMD.state == 5)){
                        //BuildDialogue("No Data","Logger is not in running or stop state to display data", 1);
                    }else {
                        done_Reading = true;
                        Start_Timer();
                    }
                    SetUI_stage2();
//                                SetUI();
//                            progresspercentage = 100;
//                            stopProgress();
                }

                usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(false)));
                break;
            case 29:

                break;
            case 30:
                usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(baseCMD.WriteRTC()));
                state  = 31;
                break;
            case 31:
                usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(baseCMD.ReadRTC()));
                if(baseCMD.passwordEnabled){
                    promtPassword(1);
                }
                state  = 32;
                break;
            case 32:
                mt2Msg_write = new MT2Msg_Write();
                mt2Msg_write.MT2Msg_WriteUSB(UserReadtemp);

                usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_write.writeSetup()));

                state = 33;
                break;
            case 33:
                if(in[0] == commsChar.CMD_ACK){
                    hexData.BytetoHex(UserReadtemp);
                    usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_write.writeSetup()));
                    state = 34;
                }
                break;
            case 34:
                //progresspercentage = progresspercentage + 10;
                hexData.BytetoHex(in);
                if(mt2Msg_write.writeDone()){
                    state = 35;
                    //progresspercentage = 100;
                    stopProgress();
                }
                usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_write.writeFill()));
                break;
            case 35:
                usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_write.writeFlash()));
                Toast.makeText(getActivity(),getString(R.string.ProgrammedSuccessfully), Toast.LENGTH_SHORT).show();
                state = 36;
                break;
            case 36:
                done_Reading = false;
                getActivity().getActionBar().setTitle(R.string.QueryLogger);
                usbFragmentI.onUSBWrite(HexData.BLE_ACK);
                message = "1";
                state = 1;
                //task = new ProgressTask();
                showProgress();
                break;
            case 37:
                query = commsSerial.ReadUSBByte(in);
                Q_data = baseCMD.CMDQuery(query);
                //Log.i("TEST", "SIZE OF Q " + Q_data.size());
                SetUI();
                break;


        }
//            }
//        };handler1.postDelayed(runnableCode,1);
    }


    /*********************************************************************************************
     * Update the UI and tells the Fragment that the USB device is disconnected
     * which turns the blue banner to gray and some buttons to gray to indicate they are not
     * functional
     *
     * @param disconnected informs the fragment if the USB device is disconnected
     * ********************************************************************************************/
    public void updateConnectionState(boolean disconnected) {
        if(disconnected == true){
            stopProgress();
            connected = false;
            linearLayout.setBackgroundColor(getResources().getColor(R.color.list_title_color));
            start_stop.setClickable(false);
            if(querycurrenttemp!=null)
                querycurrenttemp.setClickable(false);
            if(baseCMD.state == 5)
                start_stop.setBackground(getResources().getDrawable(R.drawable.startg));
            else if(baseCMD.state == 4)
                start_stop.setBackground(getResources().getDrawable(R.drawable.stopg));
            else
                start_stop.setBackground(getResources().getDrawable(R.drawable.startg));
            read.setClickable(false);
            read.setBackground(getResources().getDrawable(R.drawable.readg));
            parameterlayout.setVisibility(View.GONE);
            if(dialogcancel != null)
                dialogcancel.dismiss();
            countdownlayout.setVisibility(View.GONE);
            if(countDownTimer != null)
                countDownTimer.cancel();
            Stop_Timer();

        }
    }


/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////
 * Commincation Fragment and GRAPH
 * ////////////////////////////////////////////////////////////////////////////////////////////////
 *
 * */

    /**********************************************************************************************
     * This passes our data out to the JS
     * *******************************************************************************************/
    @JavascriptInterface
    public String getData() {
        String jobj = json_data.CreateObject();
        //Log.i("GRAPH", jobj);
        return jobj;
        // return json_data.CreateObject();
    }

    /*********************************************************************************************
     * converts all the value bytes read from the logger to proper temperature and humidity values
     * These will be sent to the graph Json_Data.java class and PDF.java class
     *
     * @param value byte values read from the logger (include temperature and humidity)
     * *******************************************************************************************/
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



/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////
 * All the Dialog pop-ups
 * ////////////////////////////////////////////////////////////////////////////////////////////////
 *
 * */

    /*********************************************************************************************
     * Used to build dialogues in the application
     * @param str1 pop-up heading
     * @param str2 pop-up message
     * @param press from where the popup is called (used in positive and negative button clicks)
     * @param neg should the negative button be visible
     * @param positive whats the string thats should be displayed in the positive button (eg. OK or Yes)
     * *******************************************************************************************/
    private void BuildDialogue(String str1, String str2, final int press, boolean neg, String positive){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle(str1)
                .setCancelable(false)
                .setMessage(str2)
                .setIcon(R.drawable.ic_message)
                .setPositiveButton(positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(press == 1){
                            done_Reading = true;
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else if(press == 2){
                            getActivity().getActionBar().setTitle(R.string.StartLogger);
                            usbFragmentI.onUSBWrite(HexData.REUSE_USB);
                            message = "2";
                            state = 1;
                            //task = new ProgressTask();
                            showProgress();
                        }else if(press == 3){
                            //Log.i("Q", done_Reading +"");
                            done_Reading = false;
                            usbFragmentI.onUSBWrite(HexData.QUARY_USB);
                            message = "1";
                            state = 1;
                            Show_PDF_Dialogue();
                            //task = new ProgressTask();
                            showProgress();
                        }else if(press == 4){
                            if(Complete) {
                                done_Reading = false;
                                usbFragmentI.onUSBWrite(HexData.BLE_ACK);
                                message = "4";
                                state = 1;
                                dialogcancel.dismiss();
                            }
                        }
                        else if(press == 5){
                            dialogcancel.dismiss();
                        }
                        // continue with delete
                    }
                });
                if(neg){
                    builder.setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(press == 3){
                                done_Reading = true;
                                Show_PDF_Dialogue();
                            }else if(press == 2){
                                done_Reading = true;
                            }else if(press == 5){
                                //wantToCloseDialog = true;
                            }

                            // continue with delete
                        }
                    });
                }
                builder.show();
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String date = String.format("%02d",dayOfMonth)+"/"+String.format("%02d",(monthOfYear+1))+"/"+String.format("%02d",year);
        switch (whichbutton) {
            case 0:
                startondatetimebutton.setText(date);
                break;
            case 1:
                stopondatebutton.setText(date);
                break;
        }
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String time = " "+String.format("%02d",hourOfDay)+":"+String.format("%02d",minute)+"";
        switch (whichbutton) {
            case 0:
                if(QS.StringDatetoInt(startondatetimebutton.getText().toString()).length < 5)
                    startondatetimebutton.append(time);
                break;
            case 1:
                if(QS.StringDatetoInt(stopondatebutton.getText().toString()).length < 5)
                    stopondatebutton.append(time);
                break;
        }
    }


    /*********************************************************************************************
     * ask the user for the password if password protection is enabled
     * @param command NOT USED ANYMORE
     * ********************************************************************************************/
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
                                //
                                //BytetoHex(baseCMD.password);
                                //BytetoHex(QS.md5(userInput.getText().toString()));
                                //Log.i("PASS", QS.compareByte(baseCMD.password,QS.md5(userInput.getText().toString())) + "============================================== " + userInput.getText().toString()+ " " + state + " " + command);
                                //if(QS.compareByte(baseCMD.password,QS.md5(userInput.getText().toString()))) {
                                    usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(baseCMD.WritePassword(userInput.getText().toString())));
//                                    if (command == 3) {
//                                        usbFragmentI.onUSBWrite(HexData.STOP_USB);
//                                    } else if (command == 4) {
//                                        usbFragmentI.onUSBWrite(HexData.REUSE_USB);
//                                    }
                                //}

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


    /*********************************************************************************************
     * AsyncTask for the prograsbar at the start. Meant to help with context switching between the threads
     * ********************************************************************************************/
    private class ProgressTask extends AsyncTask<Integer,Integer,Void> {

        protected void onPreExecute() {
            super.onPreExecute(); ///////???????

            progress=new ProgressDialog(getActivity());
            progress.setMessage(getString(R.string.pleasewait));
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(false);
            progress.setProgress(0);
            progress.setCancelable(false);
            progress.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.Abort), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //usbFragmentI.onUSBWrite(HexData.BLE_ACK);
                    state =29;
                    BuildDialogue(getString(R.string.ReadAbort), getString(R.string.Go_back_and_reconnect),1, false,getString(R.string.Yes));
                    stopProgress();
                    //dialog.dismiss();
                }
            });
            progress.setProgressNumberFormat("");
            progress.setMax(PROGRESSBAR_MAX);
            progress.show();
            //progresspercentage = 0;

        }
        protected void onCancelled() {

            progress.dismiss();

        }
        protected Void doInBackground(Integer... params) {
            //LogThings("Doinf stuff in the background " + progresspercentage);

            for(int i = 0; i < 500; i+=5){
                if(!isCancelled()) {
                    try {
                        Thread.sleep(400);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    publishProgress(i);
                }

            }
            return null;
        }
        protected void onProgressUpdate(Integer... values) {
            progress.setProgress(values[0]);

        }
        protected void onPostExecute(Void result) {

            //progresspercentage = 0;

            progress.dismiss();
            // async task finished

        }

    }

    /*********************************************************************************************
     * start the progress
     ******************************************************************************************** */
    public void showProgress() {
        ////////////////////task = new ProgressTask();
        // start progress bar with initial progress 10
        ///////////////////task.execute(10,5,null);
        //progresspercentage = 0;
        //LogThings("TASK " + task);
        task = new ProgressTask();
        if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }

    }

    /*********************************************************************************************
     * stops the progrss and dismiss the dialog
     * ********************************************************************************************/
    public void stopProgress() {
        progress.dismiss();
        task.cancel(true);
    }

    /*********************************************************************************************
     * calculates the length of the pdf which will be generated on the three pdf options
     * @param   option  which option to calculates number of pages for
     * ********************************************************************************************/


/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////
 * Helper functions
 * ////////////////////////////////////////////////////////////////////////////////////////////////
 *
 * */

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

    /*********************************************************************************************
     * Three button option PDF dialogue shows up when the pdf circile button is pressed
     * ********************************************************************************************/
    private void Show_PDF_Dialogue(){
        final Dialog dialog = new Dialog(getActivity(),R.style.AppCompatAlertDialogStyle2);
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
                Toast.makeText(getActivity(), getString(R.string.PDF_gen), Toast.LENGTH_LONG).show();
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
                Toast.makeText(getActivity(), getString(R.string.PDF_gen), Toast.LENGTH_LONG).show();
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
                Toast.makeText(getActivity(), getString(R.string.PDF_gen), Toast.LENGTH_LONG).show();
                dialog.dismiss();

                PDF pdf3 = new PDF();
                pdf3.getData(mt2Mem_values, baseCMD, 2, Q_data, U_data, F_data, R_data);
                pdf3.Create_Report(App.getContext());
                pdf3.Open_PDF_in_Chrome(App.getContext());

            }
        });

        dialog.show();
    }

    /*********************************************************************************************
     * Makes a sound depending on a positive or negative outcome eg. start successful stop unsuccessful)
     * @param context context of the application
     * @param resid which sound file is used in mediaplayback
     * ********************************************************************************************/
    private void makesound(Context context, int resid){
        if(soundon == true) {
            final MediaPlayer mp = MediaPlayer.create(context, resid);
            mp.start();
        }
    }

    /*********************************************************************************************
     * Opens the certificate of accuracy
     * ********************************************************************************************/
    private void visitAccuracyCertificate() {
        if(connected) {
            String url = "http://certificate-of-accuracy.temprecord.com/index.php?link=" + baseCMD.serialno;
            Intent i = new Intent(Intent.ACTION_VIEW);
            Uri u = Uri.parse(url);
            i.setData(u);
            startActivity(i);
        }
    }

    /*********************************************************************************************
     *Takes the user to Temprecord web page
     * *******************************************************************************************/
    private void visitweb() {//Open the temprecord web page when the button is pressed
        String url = "http://temprecord.com/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        Uri u = Uri.parse(url);
        i.setData(u);
        startActivity(i);
    }

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////
 * All the parameter things from UI to controls and programming
 * ////////////////////////////////////////////////////////////////////////////////////////////////
 *
 * */
    /*********************************************************************************************
     * Parameter UI setup rules
     * Called when the parameter button is pressed
     * ********************************************************************************************/
    private void uiSetupRules(){

        enablelcdmenucb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    extendedlcdmenucb.setVisibility(View.VISIBLE);
                }else{
                    extendedlcdmenucb.setVisibility(View.GONE);
                }
            }
        });

        passwordenabledcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    passwordtxt.setVisibility(View.VISIBLE);
                    passwordconfirmtxt.setVisibility(View.VISIBLE);
                    passwordtxtheading.setVisibility(View.VISIBLE);
                    passwordconfirmtxtheading.setVisibility(View.VISIBLE);
                }else{
                    passwordtxt.setVisibility(View.GONE);
                    passwordconfirmtxt.setVisibility(View.GONE);
                    passwordtxtheading.setVisibility(View.GONE);
                    passwordconfirmtxtheading.setVisibility(View.GONE);
                }
            }
        });

        loopovewritecb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    stoponsample.setEnabled(false);
                }else{
                    stoponsample.setEnabled(true);
                }
            }
        });


        startoptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){

                    case R.id.startwithdelay:
                        startondatetimebutton.setEnabled(false);
                        startwithdelaybutton.setEnabled(true);
                        stopondatebutton.setEnabled(false);
                        stopondatetimeR.setEnabled(false);
                        break;
                    case R.id.startondateand:
                        startwithdelaybutton.setEnabled(false);
                        startondatetimebutton.setEnabled(true);
                        stopondatebutton.setEnabled(true);
                        stopondatetimeR.setEnabled(true);
                        break;
                }
            }
        });

        imperialunit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){


                    case R.id.celsius:
                        ch1upperlimitnb.setFilters(new InputFilter[]{ new InputFilterMinMax("-30.0", "60.0")});
                        ch1lowerlimitnb.setFilters(new InputFilter[]{ new InputFilterMinMax("-30.0", "60.0")});
                        celsiusfahrenheit  = false;
                        tempheading.setText(getString(R.string.Channel1Temperature) + " °C");
                        ch1upperlimitnb.setText(baseCMD.ch1Hi / 10.0 + "");
                        ch1lowerlimitnb.setText(baseCMD.ch1Lo / 10.0 + "");

                        break;
                    case R.id.fahrenheit:
                        ch1upperlimitnb.setFilters(new InputFilter[]{ new InputFilterMinMax("-22.0", "140.0")});
                        ch1lowerlimitnb.setFilters(new InputFilter[]{ new InputFilterMinMax("-22.0", "140.0")});
                        celsiusfahrenheit = true;
                        tempheading.setText(getString(R.string.Channel1Temperature) + " °F");
                        ch1upperlimitnb.setText(String.format("%.1f",QS.returnFD(baseCMD.ch1Hi / 10.0)) + "");
                        ch1lowerlimitnb.setText(String.format("%.1f",QS.returnFD(baseCMD.ch1Lo / 10.0)) + "");

                        break;
                }
            }
        });



        stopoptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){

                    case R.id.stopbyuser:
                        stoponsamplebutton.setEnabled(false);
                        stopondatebutton.setEnabled(false);
                        if(startondatetimeR.isChecked()){
                            stopondatetimeR.setEnabled(true);
                            stopondatebutton.setEnabled(true);
                        }else {
                            stopondatetimeR.setEnabled(false);
                            stopondatebutton.setEnabled(false);
                        }
                        break;
                    case R.id.stopwhenfullp:
                        stoponsamplebutton.setEnabled(false);
                        if(startondatetimeR.isChecked()){
                            stopondatetimeR.setEnabled(true);
                            stopondatebutton.setEnabled(true);
                        }else {
                            stopondatetimeR.setEnabled(false);
                            stopondatebutton.setEnabled(false);
                        }
                        stoponsamplebutton.setEnabled(false);
                        break;
                    case R.id.stoponsamplep:

                        stoponsamplebutton.setEnabled(true);
                        if(startondatetimeR.isChecked()){
                            stopondatetimeR.setEnabled(true);
                            stopondatebutton.setEnabled(true);
                        }else {
                            stopondatetimeR.setEnabled(false);
                            stopondatebutton.setEnabled(false);
                        }
                        break;
                    case R.id.stopondatatime:
                        stoponsamplebutton.setEnabled(false);
                        stopondatebutton.setEnabled(true);
                        break;
                }
            }
        });

//        stopondatetimeR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    stoponsamplebutton.setEnabled(false);
//                    stopondatebutton.setEnabled(true);
//                } else {
//                    stoponsamplebutton.setEnabled(true);
//                    stopondatebutton.setEnabled(false);
//                }
//            }
//        });
//
//
//        stoponsampleR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    stoponsamplebutton.setEnabled(true);
//                    stopondatebutton.setEnabled(false);
//                } else {
//                    stoponsamplebutton.setEnabled(false);
//                    stopondatebutton.setEnabled(true);
//                }
//            }
//        });

        ch1limitenabledcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked){
                    ch1upperlimitnb.setEnabled(true);
                    ch1lowerlimitnb.setEnabled(true);
                    ch1alarmdelaynb.setEnabled(true);
                }else{
                    ch1upperlimitnb.setEnabled(false);
                    ch1lowerlimitnb.setEnabled(false);
                    ch1alarmdelaynb.setEnabled(false);
                }

            }
        });


        ch2limitenabledcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked){
                    ch2upperlimitnb.setEnabled(true);
                    ch2lowerlimitnb.setEnabled(true);
                    ch2alarmdelaynb.setEnabled(true);
                }else{
                    ch2upperlimitnb.setEnabled(false);
                    ch2lowerlimitnb.setEnabled(false);
                    ch2alarmdelaynb.setEnabled(false);
                }

            }
        });


        ch1enabledcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked){
                    ch1limitenabledcb.setEnabled(true);
                    ch1upperlimitnb.setEnabled(false);
                    ch1lowerlimitnb.setEnabled(false);
                    ch1alarmdelaynb.setEnabled(false);
                }else{
                    ch1limitenabledcb.setChecked(false);
                    ch1upperlimitnb.setEnabled(false);
                    ch1lowerlimitnb.setEnabled(false);
                    ch1alarmdelaynb.setEnabled(false);
                }

            }
        });

        ch2enabledcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked){
                    ch2limitenabledcb.setEnabled(true);
                    ch2upperlimitnb.setEnabled(false);
                    ch2lowerlimitnb.setEnabled(false);
                    ch2alarmdelaynb.setEnabled(false);
                }else{
                    ch2limitenabledcb.setChecked(false);
                    ch2upperlimitnb.setEnabled(false);
                    ch2lowerlimitnb.setEnabled(false);
                    ch2alarmdelaynb.setEnabled(false);
                }

            }
        });




        ch2upperlimitnb.setFilters(new InputFilter[]{ new InputFilterMinMax("0.0", "100.0")});
        ch2lowerlimitnb.setFilters(new InputFilter[]{ new InputFilterMinMax("0.0", "100.0")});


//        passwordenabledcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    passwordtxt.setEnabled(true);
//                    passwordconfirmtxt.setEnabled(true);
//                } else {
//                    passwordtxt.setEnabled(false);
//                    passwordconfirmtxt.setEnabled(false);
//                }
//            }
//        });




    }


    /*********************************************************************************************
     * Parameter button actions.
     * opens up pop-ups for different paramaters
     * ********************************************************************************************/
    private void buttonAction(){

//        Programparam.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(baseCMD.state == 2) {
//                    programbutton();
//                }else
//                    Toast.makeText(getActivity(),getString(R.string.incomplete), Toast.LENGTH_SHORT).show();
//
//            }
//        });

        startwithdelaybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), getString(R.string.StartwithdelayF), Toast.LENGTH_LONG).show();
                showPicker(v, startwithdelaybutton);
            }
        });

        sampleperiodbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), getString(R.string.SampleperiodF), Toast.LENGTH_LONG).show();
                showPicker(v, sampleperiodbutton);
            }
        });

        startondatetimebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichbutton = 0;
                startondatepopup();

            }
        });

        stopondatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichbutton = 1;
                stopondatepopup();
            }
        });


    }

    /*********************************************************************************************
     * show hour:minute:sec pop-up
     * This is used in parameters start delay select and sample period
     *
     * @param   v   parent view of the app
     * @param   b   which button selected the pop-up
     * ********************************************************************************************/
    public void showPicker(View v, final Button b){
        MyTimePickerDialog mTimePicker = new MyTimePickerDialog(getActivity(), new MyTimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {
                // TODO Auto-generated method stub
                b.setText(String.format("%02d", hourOfDay)+
                        ":" + String.format("%02d", minute) +
                        ":" + String.format("%02d", seconds));
            }
        }, Integer.parseInt(QS.StringDatetoInt(b.getText().toString())[0]), Integer.parseInt(QS.StringDatetoInt(b.getText().toString())[1]), Integer.parseInt(QS.StringDatetoInt(b.getText().toString())[2]), true);
        mTimePicker.show();
    }


    /*********************************************************************************************
     * Parameters start on date time pop-up
     * ********************************************************************************************/
    private void startondatepopup(){
        timeoutdelay = timeoutdelay + 15;
        Date date = new Date();
        Calendar now = QS.toCalendar(date);
        int hour = 0; int min = 0; int day = 0; int month = 0; int year = 0;
        if((QS.StringDatetoInt(startondatetimebutton.getText().toString()).length) == 5){
            hour = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[3]);
            min = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[4]);
            year = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[2]);
            month = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[1])-1;
            day = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[0]);
        }else if((QS.StringDatetoInt(startondatetimebutton.getText().toString()).length) == 0){//if date and time is not picked properly
            hour = now.getMaximum(Calendar.HOUR_OF_DAY);
            min = now.getMaximum(Calendar.MINUTE);
            year = now.getMaximum(Calendar.YEAR);
            month = now.getMaximum(Calendar.MONTH);
            day = now.getMaximum(Calendar.DATE);
        }else if((QS.StringDatetoInt(startondatetimebutton.getText().toString()).length) == 1){
            hour = now.getMaximum(Calendar.HOUR_OF_DAY);
            min = now.getMaximum(Calendar.MINUTE);
            year = now.getMaximum(Calendar.YEAR);
            month = now.getMaximum(Calendar.MONTH);
            day = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[0]);
        }else if((QS.StringDatetoInt(startondatetimebutton.getText().toString()).length) == 2){
            hour = now.getMaximum(Calendar.HOUR_OF_DAY);
            min = now.getMaximum(Calendar.MINUTE);
            year = now.getMaximum(Calendar.YEAR);
            month = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[1])-1;
            day = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[0]);
        }else if((QS.StringDatetoInt(startondatetimebutton.getText().toString()).length) == 3){
            hour = now.getMaximum(Calendar.HOUR_OF_DAY);
            min = now.getMaximum(Calendar.MINUTE);
            year = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[2]);
            month = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[1])-1;
            day = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[0]);
        }else if((QS.StringDatetoInt(startondatetimebutton.getText().toString()).length) == 4){
            hour = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[3]);
            min = now.getMaximum(Calendar.MINUTE);
            year = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[2]);
            month = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[1])-1;
            day = Integer.parseInt(QS.StringDatetoInt(startondatetimebutton.getText().toString())[0]);
        }



        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                hour,
                min,
                true
        );
        tpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
        tpd.setVersion(TimePickerDialog.Version.VERSION_1);
        tpd.setAccentColor(getResources().getColor(R.color.temp_blue));

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                year,
                month,
                day
        );
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
        dpd.setVersion(DatePickerDialog.Version.VERSION_1);
        dpd.setAccentColor(getResources().getColor(R.color.temp_blue));


    }
    /*********************************************************************************************
     * Parameter stop on date time pop-up
     * ********************************************************************************************/
    private void stopondatepopup(){
        timeoutdelay = timeoutdelay + 15;
        Date date = new Date();
        Calendar now = QS.toCalendar(date);
        int hour = 0; int min = 0; int day = 0; int month = 0; int year = 0;
        if((QS.StringDatetoInt(stopondatebutton.getText().toString()).length) == 5){
            hour = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[3]);
            min = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[4]);
            year = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[2]);
            month = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[1])-1;
            day = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[0]);
        }else if((QS.StringDatetoInt(stopondatebutton.getText().toString()).length) == 0){
            hour = now.getMaximum(Calendar.HOUR_OF_DAY);
            min = now.getMaximum(Calendar.MINUTE);
            year = now.getMaximum(Calendar.YEAR);
            month = now.getMaximum(Calendar.MONTH);
            day = now.getMaximum(Calendar.DATE);
        }else if((QS.StringDatetoInt(stopondatebutton.getText().toString()).length) == 1){
            hour = now.getMaximum(Calendar.HOUR_OF_DAY);
            min = now.getMaximum(Calendar.MINUTE);
            year = now.getMaximum(Calendar.YEAR);
            month = now.getMaximum(Calendar.MONTH);
            day = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[0]);
        }else if((QS.StringDatetoInt(stopondatebutton.getText().toString()).length) == 2){
            hour = now.getMaximum(Calendar.HOUR_OF_DAY);
            min = now.getMaximum(Calendar.MINUTE);
            year = now.getMaximum(Calendar.YEAR);
            month = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[1])-1;
            day = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[0]);
        }else if((QS.StringDatetoInt(stopondatebutton.getText().toString()).length) == 3){
            hour = now.getMaximum(Calendar.HOUR_OF_DAY);
            min = now.getMaximum(Calendar.MINUTE);
            year = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[2]);
            month = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[1])-1;
            day = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[0]);
        }else if((QS.StringDatetoInt(stopondatebutton.getText().toString()).length) == 4){
            hour = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[3]);
            min = now.getMaximum(Calendar.MINUTE);
            year = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[2]);
            month = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[1])-1;
            day = Integer.parseInt(QS.StringDatetoInt(stopondatebutton.getText().toString())[0]);
        }



        final TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                hour,
                min,
                true
        );
        tpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
        tpd.setVersion(TimePickerDialog.Version.VERSION_1);
        tpd.setAccentColor(getResources().getColor(R.color.temp_blue));
//        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                tpd.se
//            }
//        });

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                year,
                month,
                day
        );
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
        dpd.setVersion(DatePickerDialog.Version.VERSION_1);
        dpd.setAccentColor(getResources().getColor(R.color.temp_blue));


    }
    /*********************************************************************************************
    * used to compare the two password fields
    * if the passwords do not match the second one turns red
    * this comparison is done dynamically
     * ********************************************************************************************/
    private void Passwordenter(){
        passwordtxt.addTextChangedListener(new TextWatcher() {
                                               @Override
                                               public void beforeTextChanged (CharSequence s,int start, int count,
                                                                              int after){
                                               }
                                               @Override
                                               public void onTextChanged ( final CharSequence s, int start, int before,
                                                                           int count){
                                                   if(!passwordtxt.getText().toString().equals(passwordconfirmtxt.getText().toString())){
                                                       passwordconfirmtxt.setBackgroundColor(Color.RED);
                                                       Complete = false;
                                                   }else {
                                                       passwordconfirmtxt.setBackgroundColor(Color.WHITE);
                                                       Complete = true;
                                                   }

                                                   timeoutdelay = timeoutdelay + 10;

                                               }
                                               @Override
                                               public void afterTextChanged ( final Editable s){

                                               }
                                           }

        );

        passwordconfirmtxt.addTextChangedListener(new TextWatcher() {
                                                      @Override
                                                      public void beforeTextChanged (CharSequence s,int start, int count,
                                                                                     int after){
                                                      }
                                                      @Override
                                                      public void onTextChanged ( final CharSequence s, int start, int before,
                                                                                  int count){
                                                          if(passwordtxt.getText().toString().equals(passwordconfirmtxt.getText().toString())){
                                                              passwordconfirmtxt.setBackgroundColor(Color.WHITE);
                                                              Complete = true;
                                                          }else{
                                                              passwordconfirmtxt.setBackgroundColor(Color.RED);
                                                              Complete = false;
                                                          }
                                                          timeoutdelay = timeoutdelay + 10;

                                                      }
                                                      @Override
                                                      public void afterTextChanged ( final Editable s){
                                                          if(passwordtxt.getText().toString().equals(passwordconfirmtxt.getText().toString())){
                                                              passwordconfirmtxt.setBackgroundColor(Color.WHITE);
                                                              Complete = true;
                                                          }else{
                                                              passwordconfirmtxt.setBackgroundColor(Color.RED);
                                                              Complete = false;
                                                          }

                                                      }
                                                  }

        );
    }

    /*********************************************************************************************
     * set all the UI on the parameter pop-up
     * It follows uiSetupRules function
     * ********************************************************************************************/
    public void SetUI_Parameters(){
        if (baseCMD.energysave) {
            BLEenergysave.setChecked(true);
        } else {
            BLEenergysave.setChecked(false);
        }
        if (baseCMD.ImperialUnit) {
            fahrenheit.setChecked(true);
        } else {
            celsius.setChecked(true);
        }
        if (baseCMD.LoopOverwrite) {
            loopovewritecb.setChecked(true);
        } else {
            loopovewritecb.setChecked(false);
        }
        if (baseCMD.StartwithButton) {
            startwithbuttoncb.setChecked(true);
        } else {
            startwithbuttoncb.setChecked(false);
        }
        if (baseCMD.StopwithButton) {
            stopwithbuttoncb.setChecked(true);
        } else {
            stopwithbuttoncb.setChecked(false);
        }
        if (baseCMD.ReUsewithButton) {
            reusewithbuttoncb.setChecked(true);
        } else {
            reusewithbuttoncb.setChecked(false);
        }
        if (baseCMD.AllowplacingTags) {
            allowplacingtagcb.setChecked(true);
        } else {
            allowplacingtagcb.setChecked(false);
        }
        if (baseCMD.EnableLCDMenu) {
            enablelcdmenucb.setChecked(true);
        } else {
            enablelcdmenucb.setChecked(false);
        }
        if (baseCMD.ExtendedLCDMenu) {
            extendedlcdmenucb.setChecked(true);
        } else {
            extendedlcdmenucb.setChecked(false);
        }
        if (baseCMD.passwordEnabled) {
            passwordenabledcb.setChecked(true);
        } else {
            passwordenabledcb.setChecked(false);
        }

        //if(baseCMD.StartwithDelay){startwithdelay.setChecked(true);}else {startwithdelay.setChecked(false);}
        if (baseCMD.StartonDateTime) {
            startondatetimeR.setChecked(true);
        } else {
            startwithdelay.setChecked(true);
        }
        if (baseCMD.StopwhenFull) {
            stopwhenfullR.setChecked(true);
        } else {
            stopwhenfullR.setChecked(false);
        }
        if (baseCMD.StoponSample) {
            stoponsampleR.setChecked(true);
        } else {
            stoponsampleR.setChecked(false);
        }
        if (baseCMD.StoponDateTime) {
            stopondatetimeR.setChecked(true);
        } else {
            stopondatetimeR.setChecked(false);
        }
        if (!baseCMD.StopwhenFull & !baseCMD.StoponSample & !baseCMD.StoponDateTime) {
            stopbyuser.setChecked(true);
        } else {
            stopbyuser.setChecked(false);
        }


        if (baseCMD.ch1Enable) {
            ch1enabledcb.setChecked(true);
        } else {
            ch1enabledcb.setChecked(false);
        }
        if (baseCMD.ch1limitEnabled) {
            ch1limitenabledcb.setChecked(true);
        } else {
            ch1limitenabledcb.setChecked(false);
        }
        if (baseCMD.ch2Enable) {
            ch2enabledcb.setChecked(true);
        } else {
            ch2enabledcb.setChecked(false);
        }
        if (baseCMD.ch2limitEnabled) {
            ch2limitenabledcb.setChecked(true);
        } else {
            ch2limitenabledcb.setChecked(false);
        }

        passwordtxt.setVisibility(View.GONE);
        passwordconfirmtxt.setVisibility(View.GONE);
        passwordtxtheading.setVisibility(View.GONE);
        passwordconfirmtxtheading.setVisibility(View.GONE);

        startwithdelaybutton.setText(QS.Period(baseCMD.startDelay * 1000));
        sampleperiodbutton.setText(QS.Period(baseCMD.samplePeriod * 1000));
        stoponsamplebutton.setText(U_data.get(26));
        //startondatetimebutton.setText(baseCMD.startdatetime+"");

        if (baseCMD.ImperialUnit) {
            ch1upperlimitnb.setText(String.format("%.1f",QS.returnFD(baseCMD.ch1Hi / 10.0)) + "");
            ch1lowerlimitnb.setText(String.format("%.1f",QS.returnFD(baseCMD.ch1Lo / 10.0)) + "");
        }else{
            ch1upperlimitnb.setText(baseCMD.ch1Hi / 10.0 + "");
            ch1lowerlimitnb.setText(baseCMD.ch1Lo / 10.0 + "");
        }

        ch2upperlimitnb.setText(baseCMD.ch2Hi / 10.0 + "");
        ch2lowerlimitnb.setText(baseCMD.ch2Lo / 10.0 + "");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        startondatetimebutton.setText(QS.UTCtoLocalParameters(baseCMD.startDateTime.getTime()));
        if (stopondatetimeR.isChecked()) {

            Date date = baseCMD.startDateTime;
            Calendar calendar = QS.toCalendar(date);
            calendar.add(Calendar.SECOND, baseCMD.samplePeriod * baseCMD.numberstopon);
            date = calendar.getTime();
            stopondatebutton.setText(QS.UTCtoLocalParameters(date.getTime()));
        } else {
            stopondatebutton.setText(QS.UTCtoLocalParameters(baseCMD.startDateTime.getTime()));
        }

        usercommenttxt.setText(U_data.get(27));
        BLE_Name.setText(U_data.get(28));


    }


    /*********************************************************************************************
     * fills the program parameter array with the data we want to program
     * this function is called when the program parameter button is pressed
     * ********************************************************************************************/
    private void programbutton(){
        String stat =  fillEmpty();
        byte[] data;
        boolean[] flags = {fahrenheit.isChecked(),loopovewritecb.isChecked(),enablelcdmenucb.isChecked(),allowplacingtagcb.isChecked(),startwithbuttoncb.isChecked(),stopwithbuttoncb.isChecked(),
                reusewithbuttoncb.isChecked(),false,false,BLEenergysave.isChecked(),startondatetimeR.isChecked(), passwordenabledcb.isChecked(),stopwhenfullR.isChecked(),stoponsampleR.isChecked(),stopondatetimeR.isChecked(),extendedlcdmenucb.isChecked()};

        if(startondatetimeR.isChecked()) {
            data = baseCMD.WriteRTCStartDateTime(QS.getDatefromString(startondatetimebutton.getText().toString()));
            for (int i = 0; i < 6; i++) {
                UserReadtemp[i] = data[i];
            }
        }else{
            for (int i = 0; i < 6; i++) {
                UserReadtemp[i] = (byte)0xFF;
            }
        }

        data = baseCMD.Write_USERFlags(flags);
        UserReadtemp[6] = data[0];
        UserReadtemp[7] = data[1];//two flag bytes

        double ch1upper = 0, ch1lower = 0;

        if(celsiusfahrenheit){//if fahrenheit is entered convert it to celsius
            ch1upper = QS.returnC(Double.parseDouble(ch1upperlimitnb.getText().toString()))*10;
            ch1lower = QS.returnC(Double.parseDouble(ch1lowerlimitnb.getText().toString()))*10;
        }else{
            ch1upper = Double.parseDouble(ch1upperlimitnb.getText().toString())*10;
            ch1lower = Double.parseDouble(ch1lowerlimitnb.getText().toString())*10;
        }

        CHUserData chUserData = new CHUserData(ch1enabledcb.isChecked(), ch1limitenabledcb.isChecked(), ch1upper, ch1lower,Integer.parseInt(ch1alarmdelaynb.getText().toString()),
                ch2enabledcb.isChecked(), ch2limitenabledcb.isChecked(), Double.parseDouble(ch2upperlimitnb.getText().toString())*10, Double.parseDouble(ch2lowerlimitnb.getText().toString())*10,Integer.parseInt(ch2alarmdelaynb.getText().toString()));



        data = baseCMD.Write_USERCH1(chUserData);
        UserReadtemp[8] = data[0]; UserReadtemp[9] = data[1]; UserReadtemp[10] = data[2]; UserReadtemp[11] = data[3];//channel 1 info
        UserReadtemp[12] = data[4]; UserReadtemp[13] = data[5]; UserReadtemp[14] = data[6]; UserReadtemp[15] = data[7];
        UserReadtemp[16] = data[8]; UserReadtemp[17] = data[9]; UserReadtemp[18] = data[10]; UserReadtemp[19] = data[11];//channel 2 info
        UserReadtemp[20] = data[12]; UserReadtemp[21] = data[13]; UserReadtemp[22] = data[14]; UserReadtemp[23] = data[15];

        data = baseCMD.Write_USERSamplePeriod(Integer.parseInt(QS.StringDatetoInt(sampleperiodbutton.getText().toString())[0])*60*60+ Integer.parseInt(QS.StringDatetoInt(sampleperiodbutton.getText().toString())[1])*60 + Integer.parseInt(QS.StringDatetoInt(sampleperiodbutton.getText().toString())[2]));
        UserReadtemp[24] = data[0]; UserReadtemp[25] = data[1];//sample period

        data = baseCMD.Write_USERStartDelay(Integer.parseInt(QS.StringDatetoInt(startwithdelaybutton.getText().toString())[0])*60*60+ Integer.parseInt(QS.StringDatetoInt(startwithdelaybutton.getText().toString())[1])*60 + Integer.parseInt(QS.StringDatetoInt(startwithdelaybutton.getText().toString())[2]));
        UserReadtemp[26] = data[0]; UserReadtemp[27] = data[1];//start delay


        data = baseCMD.Write_USERStartdatetimeDelay((QS.getDatefromString(startondatetimebutton.getText().toString()).getTime()/1000 - Calendar.getInstance().getTimeInMillis()/1000));
        UserReadtemp[28] = data[0]; UserReadtemp[29] = data[1]; UserReadtemp[30] = data[2]; UserReadtemp[31] = data[3];
        if(stoponsampleR.isChecked()) {
            int val = 0;
            if(Integer.parseInt(stoponsamplebutton.getText().toString()) > 65536)val = 65536;else if(Integer.parseInt(stoponsamplebutton.getText().toString())<10)val = 10; else val = Integer.parseInt(stoponsamplebutton.getText().toString());
            data = baseCMD.Write_USERStoponsample(val);
            UserReadtemp[32] = data[0];
            UserReadtemp[33] = data[1];
            UserReadtemp[34] = data[2];
            UserReadtemp[35] = data[3];
        }else if(stopondatetimeR.isChecked()){
            if((QS.getDatefromString(startondatetimebutton.getText().toString()).compareTo(QS.getDatefromString(stopondatebutton.getText().toString()))) < 0){
                long timediff  = ((QS.getDatefromString(stopondatebutton.getText().toString()).getTime()/1000)-(QS.getDatefromString(startondatetimebutton.getText().toString()).getTime())/1000);
                int s =  Integer.parseInt(QS.StringDatetoInt(sampleperiodbutton.getText().toString())[0])*60*60+ Integer.parseInt(QS.StringDatetoInt(sampleperiodbutton.getText().toString())[1])*60 + Integer.parseInt(QS.StringDatetoInt(sampleperiodbutton.getText().toString())[2]);
                int length = (int)timediff/s;
                data = baseCMD.Write_USERStoponsample((int)length);
                UserReadtemp[32] = data[0];
                UserReadtemp[33] = data[1];
                UserReadtemp[34] = data[2];
                UserReadtemp[35] = data[3];
            }else{
                Complete = false;
            }
        }else{
            data = baseCMD.Write_USERStoponsample(Integer.parseInt(stoponsamplebutton.getText().toString()));
            UserReadtemp[32] = data[0];
            UserReadtemp[33] = data[1];
            UserReadtemp[34] = data[2];
            UserReadtemp[35] = data[3];
        }

        if(passwordenabledcb.isChecked()) {
            byte[] reply = QS.md5(passwordtxt.getText().toString());
            UserReadtemp[36] = reply[0];
            UserReadtemp[37] = reply[1];
            UserReadtemp[38] = reply[2];
            UserReadtemp[39] = reply[3];
            UserReadtemp[40] = reply[4];
            UserReadtemp[41] = reply[5];
            UserReadtemp[42] = reply[6];
            UserReadtemp[43] = reply[7];
        }


        data = baseCMD.Write_USERString(usercommenttxt.getText().toString());
        //32 bytes are ignored in the middle;
        for(int k = 78; k < 378; k++){
            UserReadtemp[k] = data[k-78];
        }
        String name = BLE_Name.getText().toString();
        name = name.replaceAll("\\s+", "_");
//        }
        data = baseCMD.Write_BLEnameString(name);

        for(int k = 378; k < 398; k++){
            UserReadtemp[k] = data[k-378];
        }

        if(stat.matches("")){
            Complete = true;
        }else{
            //Toast.makeText(getActivity(),stat, Toast.LENGTH_SHORT).show();
            Complete = false;
        }
//        if(Complete) {
//            //usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(baseCMD.ReadRTC()));
//            //prgrammingback = true;
//        }else
//            Toast.makeText(getActivity(),getString(R.string.incomplete), Toast.LENGTH_SHORT).show();

    }


    /*********************************************************************************************
     * opens the parameter dialogue with all the data written in the logger
     * after editing the user is able to cancel or program the parameters to the logger
     * Initializes all the layout variables
     * ********************************************************************************************/
    private void parameterDialogue(){
        builder1 = new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle);

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View popupView = inflater.inflate(R.layout.parameter_dialog, null);
        passwordtxt = (TextView) popupView.findViewById(R.id.password);
        passwordconfirmtxt = (TextView) popupView.findViewById(R.id.confirm);
        usercommenttxt = (TextView) popupView.findViewById(R.id.editusercomment);

        imperialunit = (RadioGroup) popupView.findViewById(R.id.imperialunitrg);
        startoptions = (RadioGroup) popupView.findViewById(R.id.startoptionsrg);
        stopoptions = (RadioGroup) popupView.findViewById(R.id.stopsettingrg);
        celsius = (RadioButton) popupView.findViewById(R.id.celsius);
        fahrenheit = (RadioButton) popupView.findViewById(R.id.fahrenheit);
        startwithdelay = (RadioButton) popupView.findViewById(R.id.startwithdelay);
        startondatetimeR = (RadioButton) popupView.findViewById(R.id.startondateand);
        stopbyuser = (RadioButton) popupView.findViewById(R.id.stopbyuser);
        stopwhenfullR = (RadioButton) popupView.findViewById(R.id.stopwhenfullp);
        stoponsampleR = (RadioButton) popupView.findViewById(R.id.stoponsamplep);
        stopondatetimeR = (RadioButton) popupView.findViewById(R.id.stopondatetime);

        startwithdelaybutton = (Button) popupView.findViewById(R.id.timePickerstartdelay);
        startondatetimebutton = (Button) popupView.findViewById(R.id.timepickerstartdatetime);
        sampleperiodbutton = (Button) popupView.findViewById(R.id.timePickersampleperiod);
        stopondatebutton = (Button) popupView.findViewById(R.id.timePickerstopondatetime);

        stoponsamplebutton = (EditText) popupView.findViewById(R.id.samplenumber);
        ch1upperlimitnb = (EditText) popupView.findViewById(R.id.ch1upperlimit);
        ch1lowerlimitnb = (EditText) popupView.findViewById(R.id.ch1lowerlimit);
        ch1alarmdelaynb = (EditText) popupView.findViewById(R.id.ch1alarmdelay);

        ch2upperlimitnb = (EditText) popupView.findViewById(R.id.ch2upperlimit);
        ch2lowerlimitnb = (EditText) popupView.findViewById(R.id.ch2lowerlimit);
        ch2alarmdelaynb = (EditText) popupView.findViewById(R.id.ch2alarmdelay);
        BLE_Name = (EditText) popupView.findViewById(R.id.editbluetoothname);
        BLE_heading = (TextView) popupView.findViewById(R.id.heading50);

        BLEenergysave = (Switch) popupView.findViewById(R.id.bleenergysave);
        loopovewritecb = (Switch) popupView.findViewById(R.id.loopoverwritep);
        startwithbuttoncb = (Switch) popupView.findViewById(R.id.startwithbuttonp);
        stopwithbuttoncb = (Switch) popupView.findViewById(R.id.stopwithbuttonp);
        reusewithbuttoncb = (Switch) popupView.findViewById(R.id.reusewithbuttonp);
        allowplacingtagcb = (Switch) popupView.findViewById(R.id.allowplacingtagsp);
        enablelcdmenucb = (Switch) popupView.findViewById(R.id.enablelcdmenup);
        extendedlcdmenucb = (Switch) popupView.findViewById(R.id.extendedlcdmenup);
        passwordenabledcb = (Switch) popupView.findViewById(R.id.securewithpasswordp);

        ch1enabledcb = (Switch) popupView.findViewById(R.id.ch1enable);
        ch1limitenabledcb = (Switch) popupView.findViewById(R.id.ch1limitsenabled);

        ch2enabledcb = (Switch) popupView.findViewById(R.id.ch2enable);
        ch2limitenabledcb = (Switch) popupView.findViewById(R.id.ch2limitenabled);

        passwordtxtheading = popupView.findViewById(R.id.heading33);
        passwordconfirmtxtheading = popupView.findViewById(R.id.heading34);

        ch2limitenabledcb.setEnabled(false);
        ch2upperlimitnb.setEnabled(false);
        ch2lowerlimitnb.setEnabled(false);
        ch2alarmdelaynb.setEnabled(false);
        tempheading = popupView.findViewById(R.id.heading39);
        BLE_heading.setVisibility(View.GONE);
        BLE_Name.setVisibility(View.GONE);
        BLEenergysave.setVisibility(View.GONE);
        builder1.setView(popupView)
                .setTitle(getString(R.string.Parametermenu));
        builder1.setPositiveButton(R.string.ProgramParameters,null);

        builder1.setNegativeButton(R.string.cancel, null);
        builder1.setCancelable(false);

        dialogcancel = builder1.create();
        dialogcancel.show();
//        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//
//            @Override
//            public void onShow(DialogInterface dialog) {
//
//                Button b = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                b.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View view) {
//                        // TODO Do something
//
//                    }
//                });
//            }
//        });

        dialogcancel.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // User clicked OK button
                if(baseCMD.state == 5){
                    fillEmpty();
                    if(Complete)
                        BuildDialogue(getString(R.string.warning), getString(R.string.programmingwarning), 4, true,getString(R.string.Yes));
                    else
                        BuildDialogue(getString(R.string.check), parameterString, 4, false,getString(R.string.Ok));

                }else if(baseCMD.state == 2) {
                    programbutton();
                    if (Complete) {
                        state = 30;
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(baseCMD.ReadRTC()));
                        //task = new ProgressTask();
                        showProgress();
                        dialogcancel.dismiss();
                    }else{
                        BuildDialogue(getString(R.string.warning), parameterString, 4, false,getString(R.string.Ok));
                    }

                }
            }

        });
        dialogcancel.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                BuildDialogue(getString(R.string.warning), getString(R.string.programmingwarning2), 5, true,getString(R.string.Yes));
                //Do stuff, possibly set wantToCloseDialog to true then...
//                if(wantToCloseDialog) {
//                    wantToCloseDialog = false;
//                    dialog.dismiss();
//                }
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });

        uiSetupRules();
        buttonAction();
        Passwordenter();
        SetUI_Parameters();
    }


    private String fillEmpty(){
        parameterString = "";
        if(ch1upperlimitnb.getText().toString().matches("")){
            Complete = false;
            parameterString = getString(R.string.channel1uppernotset);
        }
        if(ch1lowerlimitnb.getText().toString().matches("")){
            Complete = false;
            parameterString = getString(R.string.channel1lowernotset);
        }
        if(ch2upperlimitnb.getText().toString().matches("")){
            Complete = false;
            parameterString = getString(R.string.channel2uppernotset);
        }
        if(ch2lowerlimitnb.getText().toString().matches("")){
            Complete = false;
            parameterString = getString(R.string.channel2uppernotset);
        }

        if(ch1alarmdelaynb.getText().toString().matches("")){
            Complete = false;
            parameterString = getString(R.string.channel1alarmnotset);
        }
        if(ch2alarmdelaynb.getText().toString().matches("")){
            Complete = false;
            parameterString = getString(R.string.channel2alarmnotset);
        }

        if(Double.parseDouble(ch2upperlimitnb.getText().toString())*10 <= Double.parseDouble(ch2lowerlimitnb.getText().toString())*10)
            parameterString = getString(R.string.channel2upperlower);

        double ch1upper = 0, ch1lower = 0;

        if(celsiusfahrenheit){//if fahrenheit is entered convert it to celsius
            ch1upper = QS.returnC(Double.parseDouble(ch1upperlimitnb.getText().toString()))*10;
            ch1lower = QS.returnC(Double.parseDouble(ch1lowerlimitnb.getText().toString()))*10;
        }else{
            ch1upper = Double.parseDouble(ch1upperlimitnb.getText().toString())*10;
            ch1lower = Double.parseDouble(ch1lowerlimitnb.getText().toString())*10;
        }

        if(ch1upper <= ch1lower)parameterString = getString(R.string.channel1upperlower);

        String [] start = QS.StringDatetoInt(startondatetimebutton.getText().toString());
        if(start.length < 5){
            parameterString = getString(R.string.startdateincomplete);
        }

        String [] stop = QS.StringDatetoInt(stopondatebutton.getText().toString());
        if(stop.length < 5){
            parameterString = getString(R.string.stopdateincomplete);
        }


        if(startondatetimeR.isChecked() && start.length == 5) {
            if(Calendar.getInstance().compareTo(QS.toCalendar(QS.getDatefromString(startondatetimebutton.getText().toString()))) < 0){

            }else{
                parameterString = getString(R.string.startdatelowerthannow);
            }
        }

        if(stopondatetimeR.isChecked()  && stop.length == 5) {
            if ((QS.getDatefromString(startondatetimebutton.getText().toString()).compareTo(QS.getDatefromString(stopondatebutton.getText().toString()))) < 0) {

            } else if (Calendar.getInstance().compareTo(QS.toCalendar(QS.getDatefromString(stopondatebutton.getText().toString()))) < 0) {
                parameterString = getString(R.string.stopdatelowerthannow);
            }else{
                parameterString = getString(R.string.stopdatelowerthanstart);
            }
        }

        if(!ch1enabledcb.isChecked()){
            parameterString = getString(R.string.channel1deisabled);
        }


        if(parameterString.matches("")){
            Complete = true;
        }else{
            //Toast.makeText(getActivity(),stat, Toast.LENGTH_SHORT).show();
            Complete = false;
        }

        return parameterString;
    }

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////
 * Timer for updating temperature and Humidity
 * ////////////////////////////////////////////////////////////////////////////////////////////////
 *
 * */
    /*********************************************************************************************
     * Runnable which update the temperature and humidity in the blue banner. Shows the Current
     * values read by the sensor in the logger
     * values get updated every two seconds
     * First second it reads the RAM data
     * Second second updates the UI
     * ********************************************************************************************/
//    private final Runnable m_timer = new Runnable()
//    {
//        public void run()
//        {
//            LogThings("in runable update temp and hum");
//            if(part == 0) {
//                mt2Msg_read = new MT2Msg_Read(commsChar.MEM_RAM, 0, 100, 100, 3);
//                usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(true)));
//                state = 15;
//                fromtimer = true;
//                part = 1;
//
//                if (storeKeyService.getDefaults("UNITS", getActivity().getApplication()) != null && storeKeyService.getDefaults("UNITS", getActivity().getApplication()).equals("1")) {
//                    currentTemp.setText(R_data.get(9) + " °C");
//                }else {
//                    currentTemp.setText(String.format("%1$,.2f", QS.returnFD(Double.parseDouble(R_data.get(9)))) + " °F");
//                }
//                if(baseCMD.ch2Enable)
//                    currenthumidity.setText(R_data.get(13) + " %");
//
//            //BytetoHex(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(false)));
//
//            }else if(part == 1 ){
//                usbFragmentI.onUSBWrite(HexData.QUARY_USB);
//                frompart2 = true;
//                part = 0;
//
//            }
//
//            //m_handler.postDelayed(m_timer, 1 * 1000);
//        }
//    };

    /*********************************************************************************************
     * starts the timer for the Tempearture and humidity update
     * ********************************************************************************************/
    private void Start_Timer()
    {
//        if (baseCMD.numberofsamples > 0)
//        {
//                m_handler.post(m_timer);
//
//        }
    }
     /*********************************************************************************************
      * Stops the timer which updated the Tempearture and humidity in blue banner
      * ********************************************************************************************/
    private void Stop_Timer()
    {
//        fromtimer = false;
//            m_handler.removeCallbacks(m_timer);

    }

    private void LogThings(String value){
        //Log.i("TEST", value);
    }





}
