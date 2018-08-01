package com.example.yasiruw.temprecord.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yasiruw.temprecord.R;
import com.example.yasiruw.temprecord.activities.MainActivity;
import com.example.yasiruw.temprecord.comms.BLEFragmentI;
import com.example.yasiruw.temprecord.comms.BaseCMD;
import com.example.yasiruw.temprecord.comms.CommsSerial;
import com.example.yasiruw.temprecord.comms.MT2Msg_Read;
import com.example.yasiruw.temprecord.comms.MT2Msg_Write;
import com.example.yasiruw.temprecord.comms.QueryStrings;
import com.example.yasiruw.temprecord.services.StoreKeyService;
import com.example.yasiruw.temprecord.services.TXT_FILE;
import com.example.yasiruw.temprecord.utils.CHUserData;
import com.example.yasiruw.temprecord.utils.CommsChar;
import com.example.yasiruw.temprecord.utils.HexData;
import com.example.yasiruw.temprecord.utils.Screenshot;
import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.ikovac.timepickerwithseconds.TimePicker;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;


public class BLEParameterFragment extends Fragment implements com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    private static final int PROGRESSBAR_MAX = 100;
    //=======================UI ELEMENT VARIABLES===================================================
    private ScrollView parameterscroll;
    private TextView currentTemp;
    private ImageView bat;
    private ImageView temp;
    private ImageView hu;
    private TextView time;
    private TextView mConnectionState;
    private TextView Lstate;
    private TextView battery;
    private TextView currenthumidity;
    private TextView tempheading;

    private TextView passwordtxt;
    private TextView passwordconfirmtxt;
    private TextView usercommenttxt;

    private RadioButton celsius;
    private RadioButton fahrenheit;
    private RadioButton startwithdelay;
    private RadioButton stopbyuser;
    private RadioButton startondatetime;
    private RadioButton stopwhenfull;
    private RadioButton stoponsample;
    private RadioButton stopondatetime;

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

    private CheckBox BLEenergysave;
    private CheckBox loopovewritecb;
    private CheckBox startwithbuttoncb;
    private CheckBox stopwithbuttoncb;
    private CheckBox reusewithbuttoncb;
    private CheckBox allowplacingtagcb;
    private CheckBox enablelcdmenucb;
    private CheckBox extendedlcdmenucb;
    private CheckBox passwordenabledcb;
    private CheckBox ch1enabledcb;
    private CheckBox ch1limitenabledcb;
    private CheckBox ch2enabledcb;
    private CheckBox ch2limitenabledcb;

    private Button Programparam;
    //============OTHER GLOBAL VARIABLES============================================================
    private boolean backpress = false;

    private int whichbutton;
    private int timeoutdelay;
    private boolean Complete = true;

    StoreKeyService storeKeyService;
    private ProgressDialog progress;
    private int progresspercentage;



    private byte[] TWFlash = new byte[144];
    private byte[] RamRead = new byte[100];
    private byte[] UserRead = new byte[512];
    private byte[] UserReadtemp = new byte[398];
    private byte[] ExtraRead = new byte[284];


    private boolean soundon = true;
    private boolean pands = false;
    private boolean celsiusfahrenheit = false;
    private int state = 1;
    private ArrayList<String> Q_data = new ArrayList<String>();
    private ArrayList<String> U_data = new ArrayList<String>();
    private ArrayList<String> F_data = new ArrayList<String>();
    private ArrayList<String> R_data = new ArrayList<String>();


    HexData hexData = new HexData();
    BaseCMD baseCMD =  new BaseCMD();
    CommsSerial commsSerial = new CommsSerial();
    MT2Msg_Read mt2Msg_read;
    MT2Msg_Write mt2Msg_write;
    QueryStrings QS = new QueryStrings();
    CommsChar commsChar = new CommsChar();
    TXT_FILE txt_file = new TXT_FILE();
    private List<BluetoothDevice> mDevices = new ArrayList<>();
    private int currentDevice = 0;

    private IBinder iBinder;

    private byte[] returndata;

    private Handler handler1 =new Handler();
    public static final Handler mainThreadHandler = new Handler();
    Runnable delayedTask;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    BLEFragmentI bleFragmentI;

    //============================================================================================//

    public BLEParameterFragment GET_INSTANCE(Bundle data)
    {
        BLEParameterFragment fragment = new BLEParameterFragment();
        fragment.setArguments(data);
        return fragment;
    }

    //===================================Override functions=======================================//

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
        View view = inflater.inflate(R.layout.fragment_bleparameter, container, false);
        getActivity().getActionBar().show();
        getActivity().getActionBar().setTitle(R.string.LoggerParameters);
        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
        getActivity().getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_parametersc));

        QS = new QueryStrings();
        AssetManager am = getActivity().getAssets();


        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
        Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Medium.ttf");

        parameterscroll =  view.findViewById(R.id.parametersscroll);
        bat =  view.findViewById(R.id.imageView1);
        temp =  view.findViewById(R.id.imageView2);
        hu =  view.findViewById(R.id.imageView3);

        currentTemp =  view.findViewById(R.id.temperature);
        currentTemp.setTypeface(font);
        currenthumidity =  view.findViewById(R.id.humiditytop);
        currenthumidity.setTypeface(font);
        time =  view.findViewById(R.id.time);
        time.setTypeface(font);
        mConnectionState =  view.findViewById(R.id.connection_state);
        Lstate =  view.findViewById(R.id.state);
        Lstate.setTypeface(font);
        battery =  view.findViewById(R.id.battery);
        battery.setTypeface(font);

        passwordtxt =  view.findViewById(R.id.password);
        passwordconfirmtxt =  view.findViewById(R.id.confirm);
        usercommenttxt =  view.findViewById(R.id.editusercomment);

        imperialunit =  view.findViewById(R.id.imperialunitrg);
        startoptions =  view.findViewById(R.id.startoptionsrg);
        stopoptions =  view.findViewById(R.id.stopsettingrg);
        celsius = view.findViewById(R.id.celsius);
        fahrenheit =  view.findViewById(R.id.fahrenheit);
        startwithdelay =  view.findViewById(R.id.startwithdelay);
        startondatetime =  view.findViewById(R.id.startondateand);
        stopbyuser =  view.findViewById(R.id.stopbyuser);
        stopwhenfull =  view.findViewById(R.id.stopwhenfull);
        stoponsample =  view.findViewById(R.id.stoponsample);
        stopondatetime = view.findViewById(R.id.stopondatetime);

        startwithdelaybutton =  view.findViewById(R.id.timePickerstartdelay);
        startondatetimebutton =  view.findViewById(R.id.timepickerstartdatetime);
        sampleperiodbutton =  view.findViewById(R.id.timePickersampleperiod);
        stopondatebutton =  view.findViewById(R.id.timePickerstopondatetime);

        stoponsamplebutton =  view.findViewById(R.id.samplenumber);
        ch1upperlimitnb =  view.findViewById(R.id.ch1upperlimit);
        ch1lowerlimitnb =  view.findViewById(R.id.ch1lowerlimit);
        ch1alarmdelaynb =  view.findViewById(R.id.ch1alarmdelay);

        ch2upperlimitnb =  view.findViewById(R.id.ch2upperlimit);
        ch2lowerlimitnb =  view.findViewById(R.id.ch2lowerlimit);
        ch2alarmdelaynb =  view.findViewById(R.id.ch2alarmdelay);
        BLE_Name = view.findViewById(R.id.editbluetoothname);

        BLEenergysave =  view.findViewById(R.id.bleenergysave);
        loopovewritecb =  view.findViewById(R.id.loopoverwrite);
        startwithbuttoncb =  view.findViewById(R.id.startwithbutton);
        stopwithbuttoncb =  view.findViewById(R.id.stopwithbutton);
        reusewithbuttoncb =  view.findViewById(R.id.reusewithbutton);
        allowplacingtagcb = view.findViewById(R.id.allowplacingtags);
        enablelcdmenucb =  view.findViewById(R.id.enablelcdmenu);
        extendedlcdmenucb =  view.findViewById(R.id.extendedlcdmenu);
        passwordenabledcb =  view.findViewById(R.id.securewithpassword);

        ch1enabledcb =  view.findViewById(R.id.ch1enable);
        ch1limitenabledcb =  view.findViewById(R.id.ch1limitsenabled);

        ch2enabledcb =  view.findViewById(R.id.ch2enable);
        ch2limitenabledcb =  view.findViewById(R.id.ch2limitenabled);

        ch2limitenabledcb.setEnabled(false);
        ch2upperlimitnb.setEnabled(false);
        ch2lowerlimitnb.setEnabled(false);
        ch2alarmdelaynb.setEnabled(false);
        tempheading = view.findViewById(R.id.heading39);

        Programparam =  view.findViewById(R.id.done);

        progressDialoge();
        uiSetupRules();
        buttonAction();
        Passwordenter();
        ScrollListener();

        bleFragmentI.onBLEWrite(HexData.BLE_ACK);
        bleFragmentI.onBLERead();

        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_parameters, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        progresspercentage = 0;
        switch(item.getItemId()) {
            case R.id.action_program://code that run when the program button is pressed
                if(baseCMD.state == 2) {
                    programbutton();
                    showHeadsUpNotification();
                    //BuildDialogue(getString(R.string.Parametersprogrammed), getString(R.string.NeedstartLogger),3);
                } else
                    Toast.makeText(getActivity(),getString(R.string.incomplete), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_about://email the data
                //sendEmail();
                new Screenshot(parameterscroll,baseCMD,getActivity()).print();
                return true;
            case R.id.action_p_and_s:
                BuildDialogue("", getString(R.string.WantstartLogger),4);
                return true;
            case R.id.action_load_previous:
                SetUI();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    public void showHeadsUpNotification() {
        int notificationId = new Random().nextInt();//use this to get different notification popups
        // NotificationCompat Builder takes care of backwards compatibility and
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity())
                .setSmallIcon(R.drawable.ic_start)
                .setContentTitle("Parameters")
                .setContentText("Remember to start Logger " + baseCMD.serialno)
                .setPriority(NotificationCompat.PRIORITY_MAX).setVibrate(new long[0])
                .setAutoCancel(true);


        // Obtain NotificationManager system service in order to show the notification
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId, mBuilder.build());
    }
    //dynamically increase the time the BLE stay connected
    private void ScrollListener(){
        parameterscroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                timeoutdelay++;
            }
        });
    }

    //used to compare the two password fields //if the passwords do not match the second one turns red
    //this comparison is done dynamically
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

    //button actions
    private void buttonAction(){

        Programparam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(baseCMD.state == 2 ) {
                    programbutton();
                } else
                    Toast.makeText(getActivity(),getString(R.string.incomplete), Toast.LENGTH_SHORT).show();
            }
        });

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

    //what happens when specific buttons are pressed on the UI
    //for example stop on datetime can't be selected if startr on datetime is not selected
    private void uiSetupRules(){

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
                        stopondatetime.setEnabled(false);
                        break;
                    case R.id.startondateand:
                        startwithdelaybutton.setEnabled(false);
                        startondatetimebutton.setEnabled(true);
                        stopondatebutton.setEnabled(true);
                        stopondatetime.setEnabled(true);
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
                        celsiusfahrenheit  = false;
                        tempheading.setText(getString(R.string.Channel1Temperature) + " °C");
                        ch1upperlimitnb.setText(baseCMD.ch1Hi / 10.0 + "");
                        ch1lowerlimitnb.setText(baseCMD.ch1Lo / 10.0 + "");
                        break;
                    case R.id.fahrenheit:
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
                        stopondatebutton.setEnabled(false);
                        stopondatetime.setEnabled(false);
                        stoponsamplebutton.setEnabled(false);
                        break;
                    case R.id.stopwhenfull:
                        stopondatebutton.setEnabled(false);
                        stopondatetime.setEnabled(false);
                        stoponsamplebutton.setEnabled(false);
                        break;
                    case R.id.stoponsample:
                        stoponsamplebutton.setEnabled(true);
                        break;
                    case R.id.stopondatatime:
                        stoponsamplebutton.setEnabled(false);
                        break;
                }
            }
        });

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
                    ch1limitenabledcb.setEnabled(false);
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
                    ch2limitenabledcb.setEnabled(false);
                    ch2upperlimitnb.setEnabled(false);
                    ch2lowerlimitnb.setEnabled(false);
                    ch2alarmdelaynb.setEnabled(false);
                }

            }
        });

        passwordenabledcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passwordtxt.setEnabled(true);
                    passwordconfirmtxt.setEnabled(true);
                } else {
                    passwordtxt.setEnabled(false);
                    passwordconfirmtxt.setEnabled(false);
                }
            }
        });

        stopondatetime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    stoponsamplebutton.setEnabled(false);
                } else {
                    stoponsamplebutton.setEnabled(true);
                }
            }
        });


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

    //state machine used to send and receive data from the logger
    //always start on state one
    public void CommsI(final byte[] in){

        Log.i("STATE" , "state "  + state);
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
//                        query = baseCMD.ReadByte(in);
//                        Q_data = baseCMD.CMDQuery(query);
//                        SetUI(Q_data);
                        break;
                    case 1://always comes here first
                        pands = false;
                        bleFragmentI.onBLEWrite(HexData.QUARY);
                        bleFragmentI.onBLERead();
                        state = 8;
                        //firsttime = 0;
                        break;

                    case 2:

                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(baseCMD.WriteRTC()));
                        bleFragmentI.onBLERead();
                        state = 8;
                        break;
                    case 3:
                        bleFragmentI.onBLEWrite(HexData.START_L);
                        bleFragmentI.onBLERead();
                        Toast.makeText(getActivity(),getString(R.string.StartedSuccessfully), Toast.LENGTH_SHORT).show();
                        state = 4;
                        break;
                    case 4:
                        bleFragmentI.onBLEWrite(HexData.QUARY);
                        bleFragmentI.onBLERead();
                        state = 7;
                        break;
                    case 7:

                        break;
                    case 8:
                        hexData.BytetoHex(in);
                        query = commsSerial.ReadByte(in);
                        Q_data = baseCMD.CMDQuery(query);
                        hexData.BytetoHex(in);
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
                            System.arraycopy(UserRead, 114  , UserReadtemp, 0, 398);
                            hexData.BytetoHex(UserRead);
                            U_data = baseCMD.CMDUserRead(UserRead);
                            SetUI();
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
                            bleFragmentI.onBLEWrite(HexData.BLE_ACK);
                            bleFragmentI.onBLERead();
                            state = 25;
                            System.arraycopy(mt2Msg_read.memoryData, 0, ExtraRead, 236, 48);
                            hexData.BytetoHex(ExtraRead);
                            progresspercentage = 100;
                            progress.cancel();
                        }
                        if(state == 25)
                            bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_read.Read_into_writeByte(false)));
                            bleFragmentI.onBLERead();
                        break;
                    case 24:
                        hexData.BytetoHex(in);
                        bleFragmentI.onBLEWrite(HexData.BLE_ACK);
                        bleFragmentI.onBLERead();
                        state = 25;
                        break;
                    case 25:

                        hexData.BytetoHex(in);
                        ThirtySecTimeout();
                        break;
                    case 26:
                        hexData.BytetoHex(in);
                        if(in[0] == commsChar.CMD_ACK){
                            hexData.BytetoHex(UserReadtemp);
                            bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_write.writeSetup()));
                            bleFragmentI.onBLERead();
                            state = 27;
                        }
                        break;
                    case 27:
                        progresspercentage = progresspercentage + 10;
                        hexData.BytetoHex(in);
                        if(mt2Msg_write.writeDone()){
                            state = 28;
                            progress.cancel();
                        }
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_write.writeFill()));
                        bleFragmentI.onBLERead();
                        break;
                    case 28:
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_write.writeFlash()));
                        bleFragmentI.onBLERead();
                        Toast.makeText(getActivity(),getString(R.string.ProgrammedSuccessfully), Toast.LENGTH_SHORT).show();
                        if(pands){state = 3;}else{state = 7;}
                        break;
                    case 29:
                        hexData.BytetoHex(in);
                        mt2Msg_write = new MT2Msg_Write();
                        mt2Msg_write.MT2Msg_WriteBLE(UserReadtemp);
                        if(baseCMD.passwordEnabled){
                            promtPassword(1);
                        }else {
                            bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_write.writeSetup()));
                            bleFragmentI.onBLERead();
                        }
                        state = 26;
                        break;
                    case 30://sync times with logger
                        hexData.BytetoHex(in);

                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(baseCMD.WriteRTC()));
                        bleFragmentI.onBLERead();
                        state  = 31;
                        break;
                    case 31:
                        hexData.BytetoHex(in);
                        bleFragmentI.onBLEWrite(commsSerial.WriteByte(baseCMD.ReadRTC()));
                        bleFragmentI.onBLERead();
                        state  = 29;
                        break;

                }
            }
        };handler1.postDelayed(runnableCode,1);
    }

    private void ThirtySecTimeout(){
        delayedTask = new Runnable() {
            @Override
            public void run() {


//                try {
//                    TimeUnit.SECONDS.sleep(timeoutdelay/10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                if(getFragmentManager() != null){
                    bleFragmentI.onBLEWrite(HexData.GO_TO_SLEEP);
                    try {
                        TimeUnit.MILLISECONDS.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

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
        mainThreadHandler.postDelayed(delayedTask, 40000);
    }

    //after reading the logger the UI fields get filled with the logger information
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void SetUI(){


        if(pands){
            Lstate.setText( QS.GetState(Integer.parseInt(Q_data.get(5))));
        }else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());
            time.setText(currentDateandTime);
            Lstate.setText(QS.GetState(Integer.parseInt(Q_data.get(5))));
            battery.setText(R_data.get(17) + "%");
            currentTemp.setText("--");
            currenthumidity.setText(R_data.get(13) + " %");

            if ((R_data.get(0)).equals("Yes")) {
                //set the starttimedate here;
            }

            if (Double.parseDouble(Q_data.get(6)) > 66) {
                bat.setBackgroundResource(R.drawable.bfull);
            } else if (Double.parseDouble(Q_data.get(6)) > 33) {
                bat.setBackgroundResource(R.drawable.bhalf);
            } else if (Double.parseDouble(Q_data.get(6)) > 0) {
                bat.setBackgroundResource(R.drawable.blow);

            }

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
                startondatetime.setChecked(true);
            } else {
                startwithdelay.setChecked(true);
            }
            if (baseCMD.StopwhenFull) {
                stopwhenfull.setChecked(true);
            } else {
                stopwhenfull.setChecked(false);
            }
            if (baseCMD.StoponSample) {
                stoponsample.setChecked(true);
            } else {
                stoponsample.setChecked(false);
            }
            if (baseCMD.StoponDateTime) {
                stopondatetime.setChecked(true);
            } else {
                stopondatetime.setChecked(false);
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

            startondatetimebutton.setText(sdf.format(baseCMD.timestartstopdatetime));
            if (stopondatetime.isChecked()) {

                Date date = baseCMD.timestartstopdatetime;
                Calendar calendar = QS.toCalendar(date);
                calendar.add(Calendar.SECOND, baseCMD.samplePeriod * baseCMD.numberstopon);
                date = calendar.getTime();
                stopondatebutton.setText(sdf.format(date));
            } else {
                stopondatebutton.setText(sdf.format(baseCMD.timestartstopdatetime));
            }

            usercommenttxt.setText(U_data.get(27));
            BLE_Name.setText(U_data.get(28));

            if (!baseCMD.ch2Enable) {
                currenthumidity.setText("--");
            }
        }

    }



    private void BuildDialogue(String str1, String str2, final int press){
        backpress = true;
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        if(press == 4){
            builder.setTitle(str1)
                    .setMessage(str2)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            pands = true;
                            programbutton();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            pands = false;
                        }
                    })
                    .setIcon(R.drawable.ic_message)
                    .show();
        }else {
            builder.setTitle(str1)
                    .setMessage(str2)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (press == 1) {
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
    }

    //progress dialog thats used when the parameters are getting read
    public void progressDialoge(){

        progress=new ProgressDialog(getActivity());
        progress.setMessage(getString(R.string.LoadingParameters));
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(false);
        progress.setProgress(0);
        progress.setCancelable(false);
        progress.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.Abort), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                dialog.dismiss();
                BuildDialogue(getString(R.string.ParameterReadAborted), getString(R.string.Go_back_and_reconnect), 1);
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

    //progress dialog thats used when the parameters are getting programmed
    public void progressDialoge2(){

        progress=new ProgressDialog(getActivity());
        progress.setMessage(getString(R.string.ProgramingParameters));
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(false);
        progress.setProgress(0);
        progress.setCancelable(false);
        progress.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.Abort), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        progress.setProgressNumberFormat("");
        progress.setMax(40);
        progress.show();
        progresspercentage = 0;

        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;

                while(progresspercentage < 40) {
                    progress.setProgress(progresspercentage);
                }
            }
        };
        t.start();
    }


    //fills the program parameter array with the data we want to program
    // this function is called when the program parameter button is pressed
    private void programbutton(){

        byte[] data;

        boolean[] flags = {fahrenheit.isChecked(),loopovewritecb.isChecked(),enablelcdmenucb.isChecked(),allowplacingtagcb.isChecked(),startwithbuttoncb.isChecked(),stopwithbuttoncb.isChecked(),
                reusewithbuttoncb.isChecked(),false,false,BLEenergysave.isChecked(),startondatetime.isChecked(), passwordenabledcb.isChecked(),stopwhenfull.isChecked(),stoponsample.isChecked(),stopondatetime.isChecked(),extendedlcdmenucb.isChecked()};

        if(startondatetime.isChecked()) {
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

        if(ch1upper <= ch1lower)Complete = false;else Complete = true;
        if(Double.parseDouble(ch2upperlimitnb.getText().toString())*10 <= Double.parseDouble(ch2lowerlimitnb.getText().toString())*10)Complete = false;else Complete = true;

        data = baseCMD.Write_USERCH1(chUserData);
        UserReadtemp[8] = data[0]; UserReadtemp[9] = data[1]; UserReadtemp[10] = data[2]; UserReadtemp[11] = data[3];//channel 1 info
        UserReadtemp[12] = data[4]; UserReadtemp[13] = data[5]; UserReadtemp[14] = data[6]; UserReadtemp[15] = data[7];
        UserReadtemp[16] = data[8]; UserReadtemp[17] = data[9]; UserReadtemp[18] = data[10]; UserReadtemp[19] = data[11];//channel 2 info
        UserReadtemp[20] = data[12]; UserReadtemp[21] = data[13]; UserReadtemp[22] = data[14]; UserReadtemp[23] = data[15];

        data = baseCMD.Write_USERSamplePeriod(Integer.parseInt(QS.StringDatetoInt(sampleperiodbutton.getText().toString())[0])*60*60+ Integer.parseInt(QS.StringDatetoInt(sampleperiodbutton.getText().toString())[1])*60 + Integer.parseInt(QS.StringDatetoInt(sampleperiodbutton.getText().toString())[2]));
        UserReadtemp[24] = data[0]; UserReadtemp[25] = data[1];//sample period

        data = baseCMD.Write_USERStartDelay(Integer.parseInt(QS.StringDatetoInt(startwithdelaybutton.getText().toString())[0])*60*60+ Integer.parseInt(QS.StringDatetoInt(startwithdelaybutton.getText().toString())[1])*60 + Integer.parseInt(QS.StringDatetoInt(startwithdelaybutton.getText().toString())[2]));
        UserReadtemp[26] = data[0]; UserReadtemp[27] = data[1];//start delay


        data = baseCMD.Write_USERStartdatetimeDelay((QS.getDatefromString(startondatetimebutton.getText().toString()).getTime() / 1000 - Calendar.getInstance().getTimeInMillis() / 1000));
        UserReadtemp[28] = data[0];
        UserReadtemp[29] = data[1];
        UserReadtemp[30] = data[2];
        UserReadtemp[31] = data[3];
        if(startondatetime.isChecked()) {
            if(Calendar.getInstance().compareTo(QS.toCalendar(QS.getDatefromString(startondatetimebutton.getText().toString()))) < 0){
                Complete = true;
            }else{
                Complete = false;
            }
        }
        if(stoponsample.isChecked()) {
            int val = 0;
            if(Integer.parseInt(stoponsamplebutton.getText().toString()) > 65536)val = 65536;else if(Integer.parseInt(stoponsamplebutton.getText().toString())<10)val = 10; else val = Integer.parseInt(stoponsamplebutton.getText().toString());
            data = baseCMD.Write_USERStoponsample(val);
            UserReadtemp[32] = data[0];
            UserReadtemp[33] = data[1];
            UserReadtemp[34] = data[2];
            UserReadtemp[35] = data[3];
        }else if(stopondatetime.isChecked()){
            if((QS.getDatefromString(startondatetimebutton.getText().toString()).compareTo(QS.getDatefromString(stopondatebutton.getText().toString()))) < 0){
                long timediff  = ((QS.getDatefromString(stopondatebutton.getText().toString()).getTime()/1000)-(QS.getDatefromString(startondatetimebutton.getText().toString()).getTime())/1000);
                int s =  Integer.parseInt(QS.StringDatetoInt(sampleperiodbutton.getText().toString())[0])*60*60+ Integer.parseInt(QS.StringDatetoInt(sampleperiodbutton.getText().toString())[1])*60 + Integer.parseInt(QS.StringDatetoInt(sampleperiodbutton.getText().toString())[2]);
                int length = (int)timediff/s;
                data = baseCMD.Write_USERStoponsample((int)length);
                UserReadtemp[32] = data[0];
                UserReadtemp[33] = data[1];
                UserReadtemp[34] = data[2];
                UserReadtemp[35] = data[3];
            }else {
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
            UserReadtemp[36] = (byte) 0xEE;
            UserReadtemp[37] = (byte) 0xDC;
            UserReadtemp[38] = (byte) 0xFB;
            UserReadtemp[39] = (byte) 0x31;
            UserReadtemp[40] = (byte) 0xC4;
            UserReadtemp[41] = (byte) 0x9B;
            UserReadtemp[42] = (byte) 0x19;
            UserReadtemp[43] = (byte) 0xF9;
        }


        data = baseCMD.Write_USERString(usercommenttxt.getText().toString());
        //32 bytes are ignored in the middle;
        for(int k = 78; k < 378; k++){
            UserReadtemp[k] = data[k-78];
        }
        String name = BLE_Name.getText().toString();
//        if(name.equals("")){
//            name = "default_name";
//        }else {
        name = name.replaceAll("\\s+", "_");
//        }
        data = baseCMD.Write_BLEnameString(name);

        for(int k = 378; k < 398; k++){
            UserReadtemp[k] = data[k-378];
        }

        if(Complete) {
            bleFragmentI.onBLEWrite(commsSerial.WriteByte(baseCMD.ReadRTC()));
            bleFragmentI.onBLERead();
            progressDialoge2();
        }
        else
            Toast.makeText(getActivity(),getString(R.string.incomplete), Toast.LENGTH_SHORT).show();

        state = 30;
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
                                bleFragmentI.onBLEWrite(commsSerial.WriteByte(baseCMD.WritePassword()));
                                if(command == 1){
                                    bleFragmentI.onBLEWrite(commsSerial.WriteByte(mt2Msg_write.writeSetup()));
                                    bleFragmentI.onBLERead();
                                }
                                //bleFragmentI.onBLERead();
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


    //pop-up the date picking for start on date time
    private void startondatepopup(){
        timeoutdelay = timeoutdelay + 15;
        Calendar now = Calendar.getInstance();
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
        tpd.show(getFragmentManager(), "Datepickerdialog");
        tpd.setVersion(TimePickerDialog.Version.VERSION_2);
        tpd.setAccentColor(getResources().getColor(R.color.ublox_blue_color));

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                year,
                month,
                day
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.setAccentColor(getResources().getColor(R.color.ublox_blue_color));


    }
    //pop-up the date picking for stop on date time
    private void stopondatepopup(){
        timeoutdelay = timeoutdelay + 15;
        Calendar now = Calendar.getInstance();
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



        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                hour,
                min,
                true
        );
        tpd.show(getFragmentManager(), "Datepickerdialog");
        tpd.setVersion(TimePickerDialog.Version.VERSION_2);
        tpd.setAccentColor(getResources().getColor(R.color.ublox_blue_color));

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                year,
                month,
                day
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.setAccentColor(getResources().getColor(R.color.ublox_blue_color));


    }


    //settext on the start and stop button on the UI for hour of the day, minute and seconds
    @Override
    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
        String time = " "+hourOfDay+":"+minute+"";
        switch (whichbutton) {
            case 0:
                startondatetimebutton.append(time);
                break;
            case 1:
                stopondatebutton.append(time);
                break;
        }
    }

    //settext on the start and stop button on the UI for year month and day of the month
    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        switch (whichbutton) {
            case 0:
                startondatetimebutton.setText(date);
                break;
            case 1:
                stopondatebutton.setText(date);
                break;
        }
    }
}
