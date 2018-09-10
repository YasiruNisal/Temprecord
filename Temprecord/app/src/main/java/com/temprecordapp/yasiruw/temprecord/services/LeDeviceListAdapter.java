package com.temprecordapp.yasiruw.temprecord.services;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.temprecordapp.yasiruw.temprecord.R;

import java.util.ArrayList;
import java.util.HashMap;

// Adapter for holding devices found through scanning.
public class LeDeviceListAdapter extends BaseAdapter {
    private ArrayList< BluetoothDevice > mLeDevices;
    private LayoutInflater mInflator;

    private Context context;
    private Activity activity;
    private HashMap< BluetoothDevice,
            Integer > mDevicesRssi = new HashMap < >();

    public LeDeviceListAdapter(Context context, Activity activity) {
        super();
        this.context = context;
        this.activity = activity;
        mLeDevices = new ArrayList < BluetoothDevice > ();
        mInflator = activity.getLayoutInflater();
    }

    public void addDevice(BluetoothDevice device, int rssi) {
        if (mDevicesRssi.containsKey(device)) {
            int oldRssi = mDevicesRssi.get(device);
            if (Math.abs(oldRssi - rssi) > 10) {
                mDevicesRssi.put(device, rssi);
                notifyDataSetChanged();
            }
        } else {
            mDevicesRssi.put(device, rssi);
            notifyDataSetChanged();
        }
        if (!mLeDevices.contains(device)) {
            if (device.getName() != null && device.getName().length() > 0) {
                mLeDevices.add(device);
                notifyDataSetChanged();
            }

        }
    }

    public BluetoothDevice getDevice(int position) {
        return mLeDevices.get(position);
    }

    public void clear() {
        mLeDevices.clear();
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        BluetoothDevice device = mLeDevices.get(i);
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.listitem_device, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress =  view.findViewById(R.id.device_address);
            //viewHolder.deviceRssi = (TextView) view.findViewById(R.id.device_rssi);
            viewHolder.deviceName =  view.findViewById(R.id.device_name);
            viewHolder.image =  view.findViewById(R.id.strength);
            //                viewHolder.image.getLayoutParams().height = 100;
            //                viewHolder.image.getLayoutParams().width = 100;
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

//        final View finalView = view;
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("TAG", "Coming in to  button click from the device list 1111111111111111111111111111 "  );
//                //onItemClick(null, finalView, i, i);
//            }
//        });

        final String deviceName = device.getName();
        final String deviceAddress = device.getAddress();
        if (deviceName != null && deviceName.length() > 0) viewHolder.deviceName.setText(deviceName);
        else viewHolder.deviceName.setText(R.string.unknown_device);

        viewHolder.deviceAddress.setText(deviceAddress);
//        if (mDevicesRssi.get(device) < -90) {
//            viewHolder.image.setBackground(context.getResources().getDrawable(R.drawable.level1));
//
//        } else if (mDevicesRssi.get(device) < -85) {
//            viewHolder.image.setBackground(context.getResources().getDrawable(R.drawable.level2));
//        } else if (mDevicesRssi.get(device) < -55) {
//            viewHolder.image.setBackground(context.getResources().getDrawable(R.drawable.level3));
//        } else if (mDevicesRssi.get(device) < 0) {
//            viewHolder.image.setBackground(context.getResources().getDrawable(R.drawable.level4));
//        }

        return view;
    }

    public void clicked(){

    }


}

class ViewHolder {
    TextView deviceName;
    TextView deviceRssi;
    ImageView image;
    TextView deviceAddress;
}
