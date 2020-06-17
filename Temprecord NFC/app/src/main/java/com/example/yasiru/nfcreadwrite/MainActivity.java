package com.example.yasiru.nfcreadwrite;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;

import static android.nfc.NdefRecord.TNF_EXTERNAL_TYPE;

public class MainActivity extends Activity {

    public static final String ERROR_DETECTED = "No NFC tag detected!";
    public static final String WRITE_SUCCESS = "Text written to the NFC tag successfully!";
    public static final String WRITE_ERROR = "Error during writing, is the NFC tag close enough to your device?";
    public static final byte[] RTD_ANDROID_APP = "android.com:pkg".getBytes();
    public String state_string = "";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag myTag;
    Context context;

    TextView tvNFCContent;
    TextView message;
    //Button btnWrite;
    TextView temp;
    TextView state;
    TextView info;
    TextView serialnumber;
    ImageButton start_stop;
    TextView start_stop_text;
    byte[] array = new byte[500];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        context = this;
        Typeface font = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");

        tvNFCContent = (TextView) findViewById(R.id.nfc_contents);
        //message = (TextView) findViewById(R.id.edit_message);
        //btnWrite = (Button) findViewById(R.id.button);
        serialnumber = (TextView) findViewById(R.id.serial_number);
        serialnumber.setTypeface(font);
        temp = (TextView) findViewById(R.id.temp);
        temp.setTypeface(font);
        state = (TextView) findViewById(R.id.state);
        state.setTypeface(font);
        info = (TextView)findViewById(R.id.info);
        start_stop = (ImageButton) findViewById(R.id.start_stop);
        start_stop_text = (TextView) findViewById(R.id.start_stop_text);
        start_stop.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (myTag == null) {
                   // Toast.makeText(context, ERROR_DETECTED, Toast.LENGTH_LONG).show();
                } else {
                    writeTag(myTag);
                    readFromIntent(getIntent());
//                    try {
//                        write(message.getText().toString(), myTag);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (FormatException e) {
//                        e.printStackTrace();
//                    }
                  //  Toast.makeText(context, WRITE_SUCCESS, Toast.LENGTH_LONG ).show();
                }

            }
        });

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
        }
        readFromIntent(getIntent());

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[] { tagDetected };
    }


    @SuppressLint("NewApi")
    private boolean writeTag(Tag tag) {
//        String packageName = "com.example.yasiru.nfcreadwrite";
        String packageName = "com.temprecord.temprecordapp";
        NdefRecord appRecord = new NdefRecord(TNF_EXTERNAL_TYPE ,RTD_ANDROID_APP, new byte[]{},packageName.getBytes(Charset.forName("UTF-8")));
//                "com.example.pend.nfcreadwrite".getBytes(Charset.forName("US-ASCII")),
//                new byte[0], new byte[0]);
        NdefRecord appRecord1 = null;
        NdefRecord appRecord2 = null;
        NdefRecord appRecord3 = null;
        try {
            if(state_string.equals("Running")) {
                start_stop_text.setText("Stop");
                appRecord1 = createRecord("Stopped");
                start_stop.setImageResource(R.drawable.ic_stop);
                start_stop.setBackground(getResources().getDrawable(R.drawable.stopbutton));
            }else if(state_string.equals("Stopped")) {
                start_stop_text.setText("Start");
                start_stop.setImageResource(R.drawable.ic_start);
                appRecord1 = createRecord("Running");
                start_stop.setBackground(getResources().getDrawable(R.drawable.startbutton));
            }else{
                appRecord1 = createRecord("Running");
            }

            for(int i = 0; i < 500;i++){
                array[i] = (byte)i;
            }

            appRecord2 = createRecord("Serial Number :          NFC0000001");
            appRecord3 = createExternal("temprecord.com","BYTE",array);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        NdefMessage message = new NdefMessage(new NdefRecord[] { appRecord, appRecord1,appRecord2, appRecord3 });

        try {
            // see if tag is already NDEF formatted
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();

                if (!ndef.isWritable()) {
                    info.setText("Read-only tag.");
                    Toast.makeText(context, "Read-only tag.", Toast.LENGTH_LONG ).show();
                    return false;
                }

                // work out how much space we need for the data
                int size = message.toByteArray().length;
                if (ndef.getMaxSize() < size) {
                    info.setText("Tag doesn't have enough free space.");
                    Toast.makeText(context, "Tag doesn't have enough free space.", Toast.LENGTH_LONG ).show();
                    return false;
                }

                ndef.writeNdefMessage(message);
                info.setText("Tag written successfully.");
                Toast.makeText(context, "Tag written successfully.", Toast.LENGTH_LONG ).show();
                return true;
            } else {
                // attempt to format tag
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        info.setText("Tag written successfully!\nClose this app and scan tag.");
                        Toast.makeText(context, "Tag written successfully!\nClose this app and scan tag.", Toast.LENGTH_LONG ).show();
                        return true;
                    } catch (IOException e) {
                        info.setText("Unable to format tag to NDEF.");
                        Toast.makeText(context, "Unable to format tag to NDEF.", Toast.LENGTH_LONG ).show();
                        return false;
                    }
                } else {
                    info.setText("Tag doesn't appear to support NDEF format.");
                    Toast.makeText(context, "Tag doesn't appear to support NDEF format.", Toast.LENGTH_LONG ).show();
                    return false;
                }
            }
        } catch (Exception e) {
            info.setText("Failed to write tag");
            Toast.makeText(context, "Failed to write tag", Toast.LENGTH_LONG ).show();
        }

        return false;
    }

    /******************************************************************************
     **********************************Read From NFC Tag***************************
     ******************************************************************************/
    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }
    @SuppressLint("NewApi")
    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;

        String text = "";
        try {
//        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[1].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        BytetoHex(payload);
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        byte[] payload1 = {}, payload2 = {};
        if(msgs[0].getRecords().length > 2) {
            payload1 = msgs[0].getRecords()[2].getPayload();
            textEncoding = ((payload1[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
            languageCodeLength = payload1[0] & 0063; // Get the Language Code, e.g. "en
            BytetoHex(payload1);
            serialnumber.setText(new String(payload1, languageCodeLength + 1, payload1.length - languageCodeLength - 1, textEncoding));

//            payload2 = msgs[0].getRecords()[3].getPayload();
//            BytetoHex(payload2);

        }

            // Get the Text


            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);

        Random r = new Random();
        double randomValue = 20.0 + (30.0 - 20.0) * r.nextDouble();

        //serialnumber.setBackgroundResource(R.drawable.textback);
        temp.setText(String.format("%.1f",randomValue)+"°");
        state_string = text;
        tvNFCContent.setText("NFC Content: " + text +" " + new String(payload) );
        state.setText(state_string);
        if(state_string.equals("Running")) {
            start_stop_text.setText("Stop");
            start_stop.setImageResource(R.drawable.ic_stop);
            start_stop.setBackground(getResources().getDrawable(R.drawable.stopbutton));
        }else if(state_string.equals("Stopped")) {
            start_stop_text.setText("Start");
            start_stop.setImageResource(R.drawable.ic_start);
            start_stop.setBackground(getResources().getDrawable(R.drawable.startbutton));
        }
    } catch (UnsupportedEncodingException e) {
        Log.e("UnsupportedEncoding", e.toString());
    }
    }

    public void BytetoHex(byte[] b){
        StringBuilder sb = new StringBuilder();
        for (byte b1 : b){
            sb.append(String.format("%02X ", b1));

        }
        Log.d("HEX", sb.toString());
    }
    /******************************************************************************
     **********************************Write to NFC Tag****************************
     ******************************************************************************/
    private void write(String text, Tag tag) throws IOException, FormatException {
        NdefRecord[] records = { createRecord(text) };
        NdefMessage message = new NdefMessage(records);
        // Get an instance of Ndef for the tag.
        Ndef ndef =  Ndef.get(tag);

        if (ndef != null) {
            NdefMessage ndefMesg = ndef.getCachedNdefMessage();
            if (ndefMesg != null) {
                Log.i("NFC", "Ndef is not null");
                // Enable I/O
                ndef.connect();
                // Write the message
                ndef.writeNdefMessage(message);
            }
        } else {
            Log.i("NFC", "ndef is null");
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);
            if (ndefFormatable != null) {

                // initialize tag with new NDEF message
                try {
                    ndefFormatable.connect();
                    ndefFormatable.format(message);
                } finally {
                    try {
                        ndefFormatable.close();
                    } catch (Exception e) {}
                }
            }
        }

        // Close the connection
        ndef.close();
    }

    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang       = "en";
        byte[] textBytes  = text.getBytes();
        byte[] langBytes  = lang.getBytes("US-ASCII");
        int    langLength = langBytes.length;
        int    textLength = textBytes.length;
        byte[] payload    = new byte[1 + langLength + textLength];

        // set status byte (see NDEF spec for actual bits)
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1,              langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

            NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);


        return recordNFC;
    }

    public static android.nfc.NdefRecord createExternal(String domain, String type, byte[] data) {
        if (domain == null) throw new NullPointerException("domain is null");
        if (type == null) throw new NullPointerException("type is null");

        domain = domain.trim().toLowerCase(Locale.US);
        type = type.trim().toLowerCase(Locale.US);

        if (domain.length() == 0) throw new IllegalArgumentException("domain is empty");
        if (type.length() == 0) throw new IllegalArgumentException("type is empty");

        byte[] byteDomain = domain.getBytes(Charset.forName("UTF-8"));
        byte[] byteType = type.getBytes(Charset.forName("UTF-8"));
        byte[] b = new byte[byteDomain.length + 1 + byteType.length];
        System.arraycopy(byteDomain, 0, b, 0, byteDomain.length);
        b[byteDomain.length] = ':';
        System.arraycopy(byteType, 0, b, byteDomain.length + 1, byteType.length);

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            return new android.nfc.NdefRecord(TNF_EXTERNAL_TYPE, b, null, data);
        } else {
            return new android.nfc.NdefRecord(TNF_EXTERNAL_TYPE, b, new byte[]{}, data);
        }
    }



    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        readFromIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){

            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        WriteModeOff();
    }

    @Override
    public void onResume(){
        super.onResume();
        WriteModeOn();
    }



    /******************************************************************************
     **********************************Enable Write********************************
     ******************************************************************************/
    private void WriteModeOn(){
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }
    /******************************************************************************
     **********************************Disable Write*******************************
     ******************************************************************************/
    private void WriteModeOff(){
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }
}