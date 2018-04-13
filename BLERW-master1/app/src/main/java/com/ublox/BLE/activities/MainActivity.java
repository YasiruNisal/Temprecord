package com.ublox.BLE.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imangazaliev.circlemenu.CircleMenu;
import com.imangazaliev.circlemenu.CircleMenuButton;
import com.ublox.BLE.R;


public class MainActivity extends Activity {

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private Button Devicelist;
    RelativeLayout topLevelLayout;
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

    TextView menu;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topLevelLayout = findViewById(R.id.top_layout);
        ActionBar bar = getActionBar();
        bar.hide();
//        bar.setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
//        bar.setTitle("                             Menu");
//        bar.setElevation(20);
        Typeface font = Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf");
        final Intent intent = getIntent();
        menu = (TextView) findViewById(R.id.menu);
        menu.setTypeface(font);
        if (isFirstTime()) {
            topLevelLayout.setVisibility(View.INVISIBLE);
        }

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

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, DevicesActivity.class);
                myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"6");
                MainActivity.this.startActivity(myIntent);
                Toast.makeText(MainActivity.this,"Read Selected", Toast.LENGTH_SHORT).show();
            }
        });
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, DevicesActivity.class);
                myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"1");
                MainActivity.this.startActivity(myIntent);
                Toast.makeText(MainActivity.this,"Query Selected", Toast.LENGTH_SHORT).show();
            }
        });
        parameters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, DevicesActivity.class);
                myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"7");
                MainActivity.this.startActivity(myIntent);
                Toast.makeText(MainActivity.this,"Parameter Selected", Toast.LENGTH_SHORT).show();
            }
        });
        tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, DevicesActivity.class);
                myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"5");
                MainActivity.this.startActivity(myIntent);
                Toast.makeText(MainActivity.this,"Tag Selected", Toast.LENGTH_SHORT).show();
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, DevicesActivity.class);
                myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"2");
                MainActivity.this.startActivity(myIntent);
                Toast.makeText(MainActivity.this,"Start Selected", Toast.LENGTH_SHORT).show();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, DevicesActivity.class);
                myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"3");
                MainActivity.this.startActivity(myIntent);
                Toast.makeText(MainActivity.this,"Stop Selected", Toast.LENGTH_SHORT).show();
            }
        });
        devicelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, DevicesActivity.class);
                myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"0");
                MainActivity.this.startActivity(myIntent);
                Toast.makeText(MainActivity.this,"Device List Selected", Toast.LENGTH_SHORT).show();
            }
        });
        reuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, DevicesActivity.class);
                myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"4");
                MainActivity.this.startActivity(myIntent);
                Toast.makeText(MainActivity.this,"Reuse Selected", Toast.LENGTH_SHORT).show();
            }
        });
        files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Currently unavailable", Toast.LENGTH_SHORT).show();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, AboutActivity.class);
                //myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"6");
                MainActivity.this.startActivity(myIntent);
                Toast.makeText(MainActivity.this,"Help Selected", Toast.LENGTH_SHORT).show();
            }
        });
        findlogger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, DevicesActivity.class);
                myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE, "8");
                MainActivity.this.startActivity(myIntent);
                Toast.makeText(MainActivity.this, "Find Selected", Toast.LENGTH_SHORT).show();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Activity_Settings.class);
                //myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"6");
                MainActivity.this.startActivity(myIntent);
                Toast.makeText(MainActivity.this,"Settings Selected", Toast.LENGTH_SHORT).show();
            }
        });

//
    }


    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
            topLevelLayout.setVisibility(View.VISIBLE);
            topLevelLayout.setOnTouchListener(new View.OnTouchListener(){

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    topLevelLayout.setVisibility(View.INVISIBLE);
                    return false;
                }

            });


        }
        return ranBefore;

    }






}
