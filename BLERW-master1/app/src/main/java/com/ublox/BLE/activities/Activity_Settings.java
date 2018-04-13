package com.ublox.BLE.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.renderscript.Sampler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.ublox.BLE.R;
import com.ublox.BLE.services.StoreKeyService;

public class Activity_Settings extends Activity {

    StoreKeyService storeKeyService;
    Button emailto;
    Button emailcc;
    Button name;
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


        emailto = (Button) findViewById(R.id.emailto);
        emailcc = (Button) findViewById(R.id.emailcc);
        name = (Button) findViewById(R.id.name);
        sound = (Switch) findViewById(R.id.sound);

        Log.d("WHAT", "********************************" + storeKeyService.getDefaults("NAME", getApplication())+"88");

        if(storeKeyService.getDefaults("EMAIL_TO", getApplication()).equals("")) {
            emailto.setText("Enter e-mail to send logger data");
            emailto.setTextColor(getResources().getColor(R.color.list_title_color));
        }else{
            emailto.setText(storeKeyService.getDefaults("EMAIL_TO", getApplication()));
        }

        if(storeKeyService.getDefaults("EMAIL_CC", getApplication()).equals("")) {
            emailcc.setText("Enter CC e-mail to send logger data");
            emailcc.setTextColor(getResources().getColor(R.color.list_title_color));
        }else{
            emailcc.setText(storeKeyService.getDefaults("EMAIL_CC", getApplication()));
        }

        if(storeKeyService.getDefaults("NAME", getApplication()).equals("")) {
            name.setText("Enter your name");
            name.setTextColor(getResources().getColor(R.color.list_title_color));
        }else{
            name.setText(storeKeyService.getDefaults("NAME", getApplication()));
        }
//        emailcc.setText(storeKeyService.getDefaults("EMAIL_CC", getApplication()));
//        name.setText(storeKeyService.getDefaults("NAME", getApplication()));
        if(storeKeyService.getDefaults("SOUND", getApplication()) != null && storeKeyService.getDefaults("SOUND", getApplication()).equals("1"))
            sound.setChecked(true);
        else
            sound.setChecked(false);

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

                Toast.makeText(Activity_Settings.this, "Settings Applied", Toast.LENGTH_SHORT).show();
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
