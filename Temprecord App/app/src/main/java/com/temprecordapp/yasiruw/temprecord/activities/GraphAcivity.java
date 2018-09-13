package com.temprecordapp.yasiruw.temprecord.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.temprecordapp.yasiruw.temprecord.R;
import com.temprecordapp.yasiruw.temprecord.services.Json_Data;

public class GraphAcivity extends Activity {

    WebView Graphbig;
    Intent intent;
    private String json_structure;
    private Json_Data json_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        getActionBar().hide();

        intent = getIntent();
        json_structure = intent.getStringExtra("VALUE");
        //Log.i("GRAPHACTIVITY", json_structure);
        Graphbig = findViewById(R.id.biggraph);
        plotGraphbig(2);

    }


    public void plotGraphbig(int viewtype){
        Graphbig.getSettings().setJavaScriptEnabled(true);
        Graphbig.addJavascriptInterface(this,"android");
        //Graph1.requestFocusFromTouch();
        Graphbig.setWebViewClient(new WebViewClient());
        Graphbig.setWebChromeClient(new WebChromeClient());
        //json_data = new Json_Data(mt2Mem_values, baseCMD, viewtype);
        Graphbig.loadUrl("file:///android_asset/highcharts.html");



    }



    /** This passes our data out to the JS */
    @JavascriptInterface
    public String getData() {
        return json_structure;
    }
}
