package com.ublox.BLE.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.renderscript.Sampler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;

import com.ublox.BLE.R;
import com.ublox.BLE.services.StoreKeyService;

public class Activity_Settings extends Activity {

    StoreKeyService storeKeyService;
    EditText emailto;
    EditText emailcc;
    EditText name;
    Switch sound;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__settings);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
        bar.setTitle("Settings");
        bar.setElevation(15);


        emailto = (EditText) findViewById(R.id.emailto);
        emailcc = (EditText) findViewById(R.id.emailcc);
        name = (EditText) findViewById(R.id.name);
        sound = (Switch) findViewById(R.id.sound);

        emailto.setText(storeKeyService.getDefaults("EMAIL_TO", getApplication()));
        emailcc.setText(storeKeyService.getDefaults("EMAIL_CC", getApplication()));
        name.setText(storeKeyService.getDefaults("NAME", getApplication()));
        if(storeKeyService.getDefaults("SOUND", getApplication()) != null && storeKeyService.getDefaults("SOUND", getApplication()).equals("1"))
            sound.setChecked(true);
        else
            sound.setChecked(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.apply:
                String sond;
                if(sound.isChecked() == true)
                    sond = "1";
                else
                    sond = "0";


                storeKeyService.setDefaults("EMAIL_TO", emailto.getText().toString(), getApplication());
                storeKeyService.setDefaults("EMAIL_CC", emailcc.getText().toString(), getApplication());
                storeKeyService.setDefaults("NAME", name.getText().toString(), getApplication());
                storeKeyService.setDefaults("SOUND", sond, getApplicationContext());
                //emailcc.setText(storeKeyService.getDefaults("EMAIL_TO", getApplication()));
                //apply settings
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
