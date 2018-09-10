package com.temprecordapp.yasiruw.temprecord.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.temprecordapp.yasiruw.temprecord.App;
import com.temprecordapp.yasiruw.temprecord.R;
import com.temprecordapp.yasiruw.temprecord.activities.MainActivity;
import com.temprecordapp.yasiruw.temprecord.comms.BaseCMD;
import com.temprecordapp.yasiruw.temprecord.comms.CommsSerial;
import com.temprecordapp.yasiruw.temprecord.comms.MT2Msg_Read;
import com.temprecordapp.yasiruw.temprecord.comms.MT2Msg_Write;
import com.temprecordapp.yasiruw.temprecord.CustomLibraries.Yasiru_Temp_Library;
import com.temprecordapp.yasiruw.temprecord.comms.USBFragmentI;
import com.temprecordapp.yasiruw.temprecord.services.StoreKeyService;
import com.temprecordapp.yasiruw.temprecord.comms.CHUserData;
import com.temprecordapp.yasiruw.temprecord.comms.CommsChar;
import com.temprecordapp.yasiruw.temprecord.comms.HexData;
import com.temprecordapp.yasiruw.temprecord.services.Screenshot;
import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.ikovac.timepickerwithseconds.TimePicker;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

public class USBParameterFragment extends Fragment implements com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

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

    private TextView BLE_heading;

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

    private LinearLayout querycurrenttemp;
    private Button Programparam;
    //============OTHER GLOBAL VARIABLES============================================================
    private boolean backpress = false;

    private int whichbutton;
    private int timeoutdelay;
    private boolean Complete = true;

    private boolean humidityenabled = false;

    StoreKeyService storeKeyService;
    private ProgressDialog progress;
    private int progresspercentage;
    private int progressincrement;


    private byte[] TWFlash = new byte[144];
    private byte[] RamRead = new byte[100];
    private byte[] UserRead = new byte[512];
    private byte[] UserReadtemp = new byte[398];
    private byte[] ExtraRead = new byte[284];
    private int firsttime = 0;
    public boolean imperial;
    private boolean soundon = true;
    private boolean pands = false;
    private boolean prgrammingback = false;
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
    Yasiru_Temp_Library QS = new Yasiru_Temp_Library();
    CommsChar commsChar = new CommsChar();
    ProgressTask task = new ProgressTask();
    private Handler handler1 =new Handler();


    USBFragmentI usbFragmentI;

    public USBParameterFragment GET_INSTANCE(Bundle data)
    {
        USBParameterFragment fragment = new USBParameterFragment();
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
        View view = inflater.inflate(R.layout.fragment_bleparameter, container, false);
        getActivity().getActionBar().show();
        getActivity().getActionBar().setTitle(R.string.LoggerParameters);
        getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
        getActivity().getActionBar().setIcon(getResources().getDrawable(R.drawable.ic_parametersc));

        AssetManager am = getActivity().getAssets();


        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
        Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Medium.ttf");

        parameterscroll = (ScrollView) view.findViewById(R.id.parametersscroll);
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

        passwordtxt = (TextView) view.findViewById(R.id.password);
        passwordconfirmtxt = (TextView) view.findViewById(R.id.confirm);
        usercommenttxt = (TextView) view.findViewById(R.id.editusercomment);

        imperialunit = (RadioGroup) view.findViewById(R.id.imperialunitrg);
        startoptions = (RadioGroup) view.findViewById(R.id.startoptionsrg);
        stopoptions = (RadioGroup) view.findViewById(R.id.stopsettingrg);
        celsius = (RadioButton) view.findViewById(R.id.celsius);
        fahrenheit = (RadioButton) view.findViewById(R.id.fahrenheit);
        startwithdelay = (RadioButton) view.findViewById(R.id.startwithdelay);
        startondatetime = (RadioButton) view.findViewById(R.id.startondateand);
        stopbyuser = (RadioButton) view.findViewById(R.id.stopbyuser);
        stopwhenfull = (RadioButton) view.findViewById(R.id.stopwhenfull);
        stoponsample = (RadioButton) view.findViewById(R.id.stoponsample);
        stopondatetime = (RadioButton) view.findViewById(R.id.stopondatetime);

        startwithdelaybutton = (Button) view.findViewById(R.id.timePickerstartdelay);
        startondatetimebutton = (Button) view.findViewById(R.id.timepickerstartdatetime);
        sampleperiodbutton = (Button) view.findViewById(R.id.timePickersampleperiod);
        stopondatebutton = (Button) view.findViewById(R.id.timePickerstopondatetime);

        stoponsamplebutton = (EditText) view.findViewById(R.id.samplenumber);
        ch1upperlimitnb = (EditText) view.findViewById(R.id.ch1upperlimit);
        ch1lowerlimitnb = (EditText) view.findViewById(R.id.ch1lowerlimit);
        ch1alarmdelaynb = (EditText) view.findViewById(R.id.ch1alarmdelay);

        ch2upperlimitnb = (EditText) view.findViewById(R.id.ch2upperlimit);
        ch2lowerlimitnb = (EditText) view.findViewById(R.id.ch2lowerlimit);
        ch2alarmdelaynb = (EditText) view.findViewById(R.id.ch2alarmdelay);
        BLE_Name = (EditText) view.findViewById(R.id.editbluetoothname);
        BLE_heading = (TextView) view.findViewById(R.id.heading50);

        BLEenergysave = (CheckBox) view.findViewById(R.id.bleenergysave);
        loopovewritecb = (CheckBox) view.findViewById(R.id.loopoverwrite);
        startwithbuttoncb = (CheckBox) view.findViewById(R.id.startwithbutton);
        stopwithbuttoncb = (CheckBox) view.findViewById(R.id.stopwithbutton);
        reusewithbuttoncb = (CheckBox) view.findViewById(R.id.reusewithbutton);
        allowplacingtagcb = (CheckBox) view.findViewById(R.id.allowplacingtags);
        enablelcdmenucb = (CheckBox) view.findViewById(R.id.enablelcdmenu);
        extendedlcdmenucb = (CheckBox) view.findViewById(R.id.extendedlcdmenu);
        passwordenabledcb = (CheckBox) view.findViewById(R.id.securewithpassword);

        ch1enabledcb = (CheckBox) view.findViewById(R.id.ch1enable);
        ch1limitenabledcb = (CheckBox) view.findViewById(R.id.ch1limitsenabled);

        ch2enabledcb = (CheckBox) view.findViewById(R.id.ch2enable);
        ch2limitenabledcb = (CheckBox) view.findViewById(R.id.ch2limitenabled);

        ch2limitenabledcb.setEnabled(false);
        ch2upperlimitnb.setEnabled(false);
        ch2lowerlimitnb.setEnabled(false);
        ch2alarmdelaynb.setEnabled(false);
        tempheading = view.findViewById(R.id.heading39);
        BLE_heading.setVisibility(View.GONE);
        BLE_Name.setVisibility(View.GONE);
        BLEenergysave.setVisibility(View.GONE);

        Programparam = (Button) view.findViewById(R.id.done);
        mConnectionState.setText(getString(R.string.USB_Connected));
        showProgress();
        uiSetupRules();
        buttonAction();
        Passwordenter();

        usbFragmentI.onUSBWrite(HexData.BLE_ACK);

        querycurrenttemp = view.findViewById(R.id.querycurrenttemp);
        querycurrenttemp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("TOUCH", "Touch event" + imperial);
                imperial = !imperial;
                StoreKeyService.setDefaults("UNITS", String.valueOf(imperial?1:0), App.getContext());
                SetUI();
                return false;
            }
        });

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
                }else
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
                if(baseCMD.state == 2) {
                    programbutton();
                }else
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

    //state machine used to send and receive data from the logger
    //always start on state one
    public void CommsI(final byte[] in){



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
                        usbFragmentI.onUSBWrite(HexData.QUARY);
                        state = 8;
                        //firsttime = 0;
                        break;

                    case 2:

                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(baseCMD.WriteRTC()));
                        state = 8;
                        break;
                    case 3:
                        usbFragmentI.onUSBWrite(HexData.START_USB);
                        Toast.makeText(getActivity(),getString(R.string.StartedSuccessfully), Toast.LENGTH_SHORT).show();
                        state = 4;
                        break;
                    case 4:
                        usbFragmentI.onUSBWrite(HexData.QUARY);
                        state = 7;
                        break;
                    case 7:
                        if(pands){
                            query = commsSerial.ReadUSBByte(in);
                            Q_data = baseCMD.CMDQuery(query);
                            SetUI();
                        }
                        break;
                    case 8:
                        hexData.BytetoHex(in);
                        query = commsSerial.ReadUSBByte(in);
                        Q_data = baseCMD.CMDQuery(query);
                        hexData.BytetoHex(in);
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
                            hexData.BytetoHex(RamRead);
                            R_data = baseCMD.CMDRamRead(RamRead);
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
                            System.arraycopy(UserRead, 114  , UserReadtemp, 0, 398);
                            hexData.BytetoHex(UserRead);
                            U_data = baseCMD.CMDUserRead(UserRead);
                            SetUI();
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
                            usbFragmentI.onUSBWrite(HexData.BLE_ACK);
                            state = 25;
                            System.arraycopy(mt2Msg_read.memoryData, 0, ExtraRead, 236, 48);
                            firsttime++;
                            hexData.BytetoHex(ExtraRead);
                            progresspercentage = 100;
                            stopProgress();
                        }
                        if(state == 25)
                            usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_read.Read_into_writeByte(false)));
                        break;
                    case 24:
                        hexData.BytetoHex(in);
                        usbFragmentI.onUSBWrite(HexData.BLE_ACK);
                        state = 25;
                        break;
                    case 25:

                        hexData.BytetoHex(in);

                        break;
                    case 26:
                        hexData.BytetoHex(in);
                        if(in[0] == commsChar.CMD_ACK){
                            hexData.BytetoHex(UserReadtemp);
                            usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_write.writeSetup()));
                            state = 27;
                        }
                        break;
                    case 27:
                        progresspercentage = progresspercentage + 10;
                        hexData.BytetoHex(in);
                        if(mt2Msg_write.writeDone()){
                            state = 28;
                            progresspercentage = 100;
                            stopProgress();
                        }
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_write.writeFill()));
                        break;
                    case 28:
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_write.writeFlash()));
                        Toast.makeText(getActivity(),getString(R.string.ProgrammedSuccessfully), Toast.LENGTH_SHORT).show();
                        if(pands){state = 3;}else{state = 7;}
                        break;
                    case 29:

                        hexData.BytetoHex(in);
                        mt2Msg_write = new MT2Msg_Write();
                        mt2Msg_write.MT2Msg_WriteUSB(UserReadtemp);

                            usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_write.writeSetup()));

                        state = 26;
                        break;
                    case 30://sync times with logger
                        hexData.BytetoHex(in);
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(baseCMD.WriteRTC()));
                        state  = 31;
                        break;
                    case 31:
                        hexData.BytetoHex(in);
                        usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(baseCMD.ReadRTC()));
                        if(baseCMD.passwordEnabled){
                            promtPassword(1);
                        }
                        state  = 29;
                        break;
                }


            }
//        };handler1.postDelayed(runnableCode,1);
//    }


    //after reading the logger the UI fields get filled with the logger information

    public void SetUI(){


        if(pands){
            Lstate.setText(QS.GetState(baseCMD.state));
        }else {
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
            String currentDateandTime = sdf1.format(new Date());
            time.setText(currentDateandTime);
            Lstate.setText(QS.GetState(Integer.parseInt(Q_data.get(5))));
            battery.setText(R_data.get(17) + "%");
            if (storeKeyService.getDefaults("UNITS", getActivity().getApplication()) != null && storeKeyService.getDefaults("UNITS", getActivity().getApplication()).equals("1")) {
                currentTemp.setText("---" + " °C");
            }else {
                currentTemp.setText("---" + " °F");
            }

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
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            startondatetimebutton.setText(QS.UTCtoLocalParameters(baseCMD.startDateTime.getTime()));
            if (stopondatetime.isChecked()) {

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
                            if(Complete)
                                programbutton();
                            else
                                Toast.makeText(getActivity(),getString(R.string.incomplete), Toast.LENGTH_SHORT).show();
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


        data = baseCMD.Write_USERStartdatetimeDelay((QS.getDatefromString(startondatetimebutton.getText().toString()).getTime()/1000 - Calendar.getInstance().getTimeInMillis()/1000));
        UserReadtemp[28] = data[0]; UserReadtemp[29] = data[1]; UserReadtemp[30] = data[2]; UserReadtemp[31] = data[3];
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
        if(Complete) {
            usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(baseCMD.ReadRTC()));
            prgrammingback = true;
            task = new ProgressTask();
            showProgress();
        }else
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

                                usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(baseCMD.WritePassword(userInput.getText().toString())));
//                                if(command == 1)
//                                    usbFragmentI.onUSBWrite(commsSerial.WriteUSBByte(mt2Msg_write.writeSetup()));
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


    private class ProgressTask extends AsyncTask<Integer,Integer,Void> {

        protected void onPreExecute() {
            super.onPreExecute(); ///////???????

            progress=new ProgressDialog(getActivity());
            if(prgrammingback)
                progress.setMessage(getString(R.string.ProgramingParameters));
            else
                progress.setMessage(getString(R.string.LoadingParameters));
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
                    stopProgress();

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
            prgrammingback = false;
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

}