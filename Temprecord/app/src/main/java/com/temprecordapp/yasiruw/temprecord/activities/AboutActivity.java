package com.temprecordapp.yasiruw.temprecord.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.temprecordapp.yasiruw.temprecord.R;

import static android.os.Build.*;


public class AboutActivity extends Activity {

    private TextView appversion;
    private TextView androidversion;
    private TextView devicemodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle(R.string.AboutHelp);
        getActionBar().setLogo(R.drawable.ic_helpc);
        getActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_about);

        findViewById(R.id.bVisit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visit();
            }
        });

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        int version = Build.VERSION.SDK_INT;
        String versionRelease = Build.VERSION.RELEASE;

        appversion = (TextView) findViewById(R.id.appversion);
        androidversion = (TextView) findViewById(R.id.androidversion);
        devicemodel = (TextView) findViewById(R.id.devicemodel);


        appversion.setText("support@temprecord.com\n\n"+getString(R.string.appversion) + getString(R.string.App_version));
        androidversion.setText(getString(R.string.manufacturer) + manufacturer
                + " \n" +getString(R.string.devicemode)+ model
                + " \n" +getString(R.string.version)+ version
                + " \n" +getString(R.string.versionrelease)+ versionRelease);
    }

    private void visit() {//Open the temprecord web page when the button is pressed
        String url = "http://temprecord.com/about/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        Uri u = Uri.parse(url);
        i.setData(u);
        startActivity(i);
    }
}
