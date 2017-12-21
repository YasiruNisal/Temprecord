/*
 * Copyright (C) 2013 youten
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package youten.redo.yasiru.readwrite;

import java.util.List;

import youten.redo.yasiru.util.ScannedDevice;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * スキャンされたBLEデバイスリストのAdapter
 */
public class DeviceAdapter extends ArrayAdapter<ScannedDevice> {
    private static final String PREFIX_RSSI = "RSSI:";
    private List<ScannedDevice> mList;
    private LayoutInflater mInflater;
    private int mResId;

    public DeviceAdapter(Context context, int resId, List<ScannedDevice> objects) {
        super(context, resId, objects);
        mResId = resId;
        mList = objects;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScannedDevice item = (ScannedDevice) getItem(position);

        if (convertView == null) {
            convertView = mInflater.inflate(mResId, null);
        }
        TextView name = (TextView) convertView.findViewById(R.id.device_name);
        //Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(),  "fonts/Quicksand-Medium.ttf");

        //name.setTypeface(custom_font);
        name.setText(item.getDisplayName());
        //TextView address = (TextView) convertView.findViewById(R.id.device_address);
        //address.setText(item.getDevice().getAddress());
        //TextView rssi = (TextView) convertView.findViewById(R.id.device_rssi);
        //rssi.setText(PREFIX_RSSI + Integer.toString(item.getRssi()));
        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.strength);
        progressBar.setMax(150);
        progressBar.setProgress(150+item.getRssi());
        progressBar.getProgressDrawable().setColorFilter(
                Color.rgb(0,51,157), android.graphics.PorterDuff.Mode.SRC_IN);
        //Log.d("INFO", "How many time do we come here");

        return convertView;
    }

    /** add or update BluetoothDevice */
    public void update(BluetoothDevice newDevice, int rssi, byte[] scanRecord) {
        if ((newDevice == null) || (newDevice.getAddress() == null)) {
            return;
        }

        boolean contains = false;
        for (ScannedDevice device : mList) {
            if (newDevice.getAddress().equals(device.getDevice().getAddress())) {
                contains = true;
                device.setRssi(rssi); // update
                break;
            }
        }
        if (!contains) {
            // add new BluetoothDevice
            mList.add(new ScannedDevice(newDevice, rssi));
        }
        notifyDataSetChanged();
    }
}
