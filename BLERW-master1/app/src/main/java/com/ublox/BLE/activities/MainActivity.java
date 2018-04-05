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

        final CircleMenu circleMenu = findViewById(R.id.circle_menu);
        circleMenu.setOnItemClickListener(new CircleMenu.OnItemClickListener() {
            @Override
            public void onItemClick(CircleMenuButton menuButton) {

            }
        });

        circleMenu.redrawView();
        circleMenu.setEventListener(new CircleMenu.EventListener() {
            @Override
            public void onMenuOpenAnimationStart() {
                Log.d("CircleMenuStatus", "onMenuOpenAnimationStart");
            }

            @Override
            public void onMenuOpenAnimationEnd() {
                Log.d("CircleMenuStatus", "onMenuOpenAnimationEnd");
            }

            @Override
            public void onMenuCloseAnimationStart() {
                Log.d("CircleMenuStatus", "onMenuCloseAnimationStart");
            }

            @Override
            public void onMenuCloseAnimationEnd() {
                Log.d("CircleMenuStatus", "onMenuCloseAnimationEnd");
            }

            @Override
            public void onButtonClickAnimationStart( CircleMenuButton menuButton) {
                Log.d("CircleMenuStatus", "onButtonClickAnimationStart");
            }


            @Override
            public void onButtonClickAnimationEnd( CircleMenuButton menuButton) {
                Log.d("CircleMenuStatus", "onButtonClickAnimationEnd");
                if(menuButton.getHintText().equals("Device List")){
                    Intent myIntent = new Intent(MainActivity.this, DevicesActivity.class);
                    myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"0");
                    MainActivity.this.startActivity(myIntent);
                    Toast.makeText(MainActivity.this,"Device List Selected", Toast.LENGTH_SHORT).show();
                }else if(menuButton.getHintText().equals("Alert")){
                }else if(menuButton.getHintText().equals("Parameters")){
                    Intent myIntent = new Intent(MainActivity.this, DevicesActivity.class);
                    myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"7");
                    MainActivity.this.startActivity(myIntent);
                    Toast.makeText(MainActivity.this,"Parameter Selected", Toast.LENGTH_SHORT).show();
                }else if(menuButton.getHintText().equals("Query")){
                    Intent myIntent = new Intent(MainActivity.this, DevicesActivity.class);
                    myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"1");
                    MainActivity.this.startActivity(myIntent);
                    Toast.makeText(MainActivity.this,"Query Selected", Toast.LENGTH_SHORT).show();
                }else if(menuButton.getHintText().equals("Start")){
                    Intent myIntent = new Intent(MainActivity.this, DevicesActivity.class);
                    myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"2");
                    MainActivity.this.startActivity(myIntent);
                    Toast.makeText(MainActivity.this,"Start Selected", Toast.LENGTH_SHORT).show();
                }else if(menuButton.getHintText().equals("Stop")){
                    Intent myIntent = new Intent(MainActivity.this, DevicesActivity.class);
                    myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"3");
                    MainActivity.this.startActivity(myIntent);
                    Toast.makeText(MainActivity.this,"Stop Selected", Toast.LENGTH_SHORT).show();
                }else if(menuButton.getHintText().equals("Tag")){
                    Intent myIntent = new Intent(MainActivity.this, DevicesActivity.class);
                    myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"5");
                    MainActivity.this.startActivity(myIntent);
                    Toast.makeText(MainActivity.this,"Tag Selected", Toast.LENGTH_SHORT).show();
                }else if(menuButton.getHintText().equals("Reuse")){
                    Intent myIntent = new Intent(MainActivity.this, DevicesActivity.class);
                    myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"4");
                    MainActivity.this.startActivity(myIntent);
                    Toast.makeText(MainActivity.this,"Reuse Selected", Toast.LENGTH_SHORT).show();
                }else if(menuButton.getHintText().equals("Read")){
                    Intent myIntent = new Intent(MainActivity.this, DevicesActivity.class);
                    myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"6");
                    MainActivity.this.startActivity(myIntent);
                    Toast.makeText(MainActivity.this,"Read Selected", Toast.LENGTH_SHORT).show();
                }else if(menuButton.getHintText().equals("Help")){
                    Intent myIntent = new Intent(MainActivity.this, AboutActivity.class);
                    //myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"6");
                    MainActivity.this.startActivity(myIntent);
                    Toast.makeText(MainActivity.this,"Help Selected", Toast.LENGTH_SHORT).show();
                }else if(menuButton.getHintText().equals("Settings")){
                    Intent myIntent = new Intent(MainActivity.this, Activity_Settings.class);
                    //myIntent.putExtra(DevicesActivity.EXTRAS_MESSAGE,"6");
                    MainActivity.this.startActivity(myIntent);
                    Toast.makeText(MainActivity.this,"Settings Selected", Toast.LENGTH_SHORT).show();
                }
            }


        });

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
