package com.example.yasiruw.temprecord.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.yasiruw.temprecord.R;
import com.example.yasiruw.temprecord.services.StoreKeyService;


public class SettingsActivity extends Activity {

    StoreKeyService storeKeyService;
    Button emailto;
    Button emailcc;
    Button name;
    Switch sound;
    Switch units;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__settings);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
        bar.setTitle(getString(R.string.settings));
        bar.setIcon(getResources().getDrawable(R.drawable.ic_settingsc));
        bar.setElevation(15);


        emailto =  findViewById(R.id.emailto);
        emailcc =  findViewById(R.id.emailcc);
        name =  findViewById(R.id.name);
        sound =  findViewById(R.id.sound);
        units = findViewById(R.id.imperial);
        sound.setChecked(true);

        //Log.d("WHAT", "********************************" + StoreKeyService.getDefaults("NAME", getApplication())+"88");
        if(storeKeyService != null) {
            if (StoreKeyService.getDefaults("EMAIL_TO", getApplication()).equals("")) {
                emailto.setText("Enter e-mail to send logger data");
                emailto.setTextColor(getResources().getColor(R.color.list_title_color));
            } else {
                emailto.setText(StoreKeyService.getDefaults("EMAIL_TO", getApplicationContext()));
            }

            if (StoreKeyService.getDefaults("EMAIL_CC", getApplicationContext()).equals("")) {
                emailcc.setText("Enter CC e-mail to send logger data");
                emailcc.setTextColor(getResources().getColor(R.color.list_title_color));
            } else {
                emailcc.setText(StoreKeyService.getDefaults("EMAIL_CC", getApplicationContext()));
            }

            if (StoreKeyService.getDefaults("NAME", getApplicationContext()).equals("")) {
                name.setText("Enter your name");
                name.setTextColor(getResources().getColor(R.color.list_title_color));
            } else {
                name.setText(StoreKeyService.getDefaults("NAME", getApplicationContext()));
            }
        }
//        emailcc.setText(storeKeyService.getDefaults("EMAIL_CC", getApplication()));
//        name.setText(storeKeyService.getDefaults("NAME", getApplication()));
        if(StoreKeyService.getDefaults("SOUND", getApplicationContext()) != null && StoreKeyService.getDefaults("SOUND", getApplicationContext()).equals("1"))
            sound.setChecked(true);
        else
            sound.setChecked(false);

        if(StoreKeyService.getDefaults("UNITS", getApplicationContext()) != null && StoreKeyService.getDefaults("UNITS", getApplicationContext()).equals("1"))
            units.setChecked(true);
        else
            units.setChecked(false);

        buttonClick();

    }

    private void buttonClick(){
        emailto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBulider(1, "E-mail TO", emailto.getText().toString());
            }
        });
        emailcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBulider(2, "E-mail CC", emailcc.getText().toString());
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBulider(3, "Name", name.getText().toString());
            }
        });
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
                String unit;
                if(sound.isChecked())
                    sond = "1";
                else
                    sond = "0";

                if(units.isChecked())
                    unit = "1";
                else
                    unit = "0";


                StoreKeyService.setDefaults("EMAIL_TO", emailto.getText().toString(), getApplication());
                StoreKeyService.setDefaults("EMAIL_CC", emailcc.getText().toString(), getApplication());
                StoreKeyService.setDefaults("NAME", name.getText().toString(), getApplication());
                StoreKeyService.setDefaults("SOUND", sond, getApplicationContext());
                StoreKeyService.setDefaults("UNITS", unit, getApplicationContext());
                //emailcc.setText(storeKeyService.getDefaults("EMAIL_TO", getApplication()));
                //apply settings

                Toast.makeText(SettingsActivity.this, "Settings Applied", Toast.LENGTH_SHORT).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void DialogBulider(final int button, String heading, String str){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(heading);

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(str);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               if(button == 1){
                    emailto.setText(input.getText().toString());
               }else if(button == 2){
                   emailcc.setText(input.getText().toString());
               }else if(button == 3){
                   name.setText(input.getText().toString());
               }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
