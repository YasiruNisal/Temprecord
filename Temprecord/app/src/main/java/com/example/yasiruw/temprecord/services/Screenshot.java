package com.example.yasiruw.temprecord.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.yasiruw.temprecord.comms.BaseCMD;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Screenshot {
    private View view;
    private BaseCMD baseCMD;
    private Context context;

    public Screenshot(View view, BaseCMD baseCMD, Context context){
        this.view = view;
        this.baseCMD = baseCMD;
        this.context = context;
    }

    public void print(){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Saving...");
        dialog.show();

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
        String formattedDate = df.format(c);

        Bitmap bitmap = getBitmapFromView(view,view.getHeight(),view.getWidth());
        try {
            File defaultFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
            if (!defaultFile.exists())
                defaultFile.mkdirs();

            String filename = "Logger ID "+baseCMD.serialno+ "("+formattedDate+").jpg";
            File file = new File(defaultFile,filename);
            if (file.exists()) {
                file.delete();
                file = new File(defaultFile,filename);
            }

            FileOutputStream output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();

            dialog.dismiss();

            Toast.makeText(context, "Screenshot Saved", Toast.LENGTH_SHORT).show();

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
//                            Log.i("ExternalStorage", "Scanned " + path + ":");
//                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();
            Toast.makeText(context, "Screenshot Saved Failed", Toast.LENGTH_SHORT).show();
        }



    }

    //create bitmap from the view
    private Bitmap getBitmapFromView(View view,int height,int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }
}
