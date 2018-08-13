package com.temprecordapp.yasiruw.temprecord.activities;

import android.app.Activity;
import android.os.Bundle;

import com.temprecordapp.yasiruw.temprecord.R;
import com.temprecordapp.yasiruw.temprecord.services.PDF;

public class SavedFileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_files);

        PDF pdf = new PDF();
        pdf.Create_Report(getApplication());
        pdf.Open_PDF_in_Chrome(getApplication());
    }


}
