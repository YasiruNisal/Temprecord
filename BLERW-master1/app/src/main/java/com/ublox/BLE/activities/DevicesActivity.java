package com.ublox.BLE.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ublox.BLE.R;
import com.ublox.BLE.utils.HexData;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class DevicesActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final String TAG = DevicesActivity.class.getSimpleName();
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private String EXTRA_STRING;


    public static final String EXTRAS_MESSAGE = "0";

    private static final int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle("Searching Devices..");
        getActionBar().setLogo(R.drawable.applogo);
        getActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_devices);



        final Intent intent = getIntent();
        EXTRA_STRING = intent.getStringExtra(EXTRAS_MESSAGE);

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();


        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        findViewById(R.id.bConnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectToDevices();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_devices, menu);
        if (!mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_indeterminate_progress);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                mLeDeviceListAdapter.clear();
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
//            case R.id.menu_about:
//                scanLeDevice(false);
//                Intent intent = new Intent(this, AboutActivity.class);
//                startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        setListAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);
    }

    private void setListAdapter(BaseAdapter baseAdapter) {
        ListView lvDevices = (ListView) findViewById(R.id.lvDevices);
        lvDevices.setAdapter(baseAdapter);
        lvDevices.setOnItemClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
    }

    List<Integer> selectedPositions = new ArrayList<>();

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mScanning = true;
            selectedPositions = new ArrayList<>();
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onListItemClick");
        boolean isChecked = ((CheckBox) view.findViewById(R.id.cbSelected)).isChecked();
        if (!isChecked) {
            ((CheckBox) view.findViewById(R.id.cbSelected)).setChecked(true);
            selectedPositions.add(new Integer(position));
        } else {
            ((CheckBox) view.findViewById(R.id.cbSelected)).setChecked(false);
            selectedPositions.remove(new Integer(position));
        }

        StringBuilder sb = new StringBuilder();
        for (Integer integer : selectedPositions) {
            sb.append(integer);
            sb.append(", ");
        }
    }

    private void connectToDevices() {
        ArrayList<BluetoothDevice> devices = new ArrayList<>();
        for (Integer integer : selectedPositions) {
            BluetoothDevice device = mLeDeviceListAdapter.getDevice(integer);
            devices.add(device);
        }
        if (mScanning) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mScanning = false;
        }
        if (devices.size() > 0) {
//            Intent intent = new Intent(this, DeviceControlActivity.class);
//            intent.putParcelableArrayListExtra(DeviceControlActivity.EXTRAS_DEVICES, devices);
//            startActivity(intent);



            if(EXTRA_STRING.equals("6")) {
                final Intent intent = new Intent(this, Activity_Read.class);

                intent.putParcelableArrayListExtra(Activity_Read.EXTRAS_DEVICES, devices);
                intent.putExtra(Activity_Read.EXTRAS_MESSAGE, EXTRA_STRING);
                if (mScanning) {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mScanning = false;
                }
                startActivity(intent);
            }else if(EXTRA_STRING.equals("7")) {
                final Intent intent = new Intent(this, Activity_Parameter.class);

                intent.putParcelableArrayListExtra(Activity_Parameter.EXTRAS_DEVICES, devices);
                intent.putExtra(Activity_Parameter.EXTRAS_MESSAGE, EXTRA_STRING);
                if (mScanning) {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mScanning = false;
                }
                startActivity(intent);
            }else if(EXTRA_STRING.equals("0")){

            }else {
                final Intent intent = new Intent(this, Activity_Query.class);

                intent.putParcelableArrayListExtra(Activity_Query.EXTRAS_DEVICES, devices);
                intent.putExtra(Activity_Query.EXTRAS_MESSAGE, EXTRA_STRING);
                if (mScanning) {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mScanning = false;
                }
                startActivity(intent);
            }
        } else {
            Toast.makeText(this, "You must select at least one device", Toast.LENGTH_LONG).show();
        }
    }

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        private HashMap<BluetoothDevice, Integer> mDevicesRssi = new HashMap<>();

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = DevicesActivity.this.getLayoutInflater();
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
                if(device.getName() != null && device.getName().length() > 0){
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
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                //viewHolder.deviceRssi = (TextView) view.findViewById(R.id.device_rssi);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                viewHolder.image = (ImageView) view.findViewById(R.id.strength);
//                viewHolder.image.getLayoutParams().height = 100;
//                viewHolder.image.getLayoutParams().width = 100;
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            final View finalView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(null, finalView, i, i);
                }
            });


            final String deviceName = device.getName();
            final String deviceAddress = device.getAddress();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);


            viewHolder.deviceAddress.setText(deviceAddress);
            if(mDevicesRssi.get(device) < -90){
                viewHolder.image.setBackground(getDrawable(R.drawable.level1));

            }else if(mDevicesRssi.get(device) < -85){
                viewHolder.image.setBackground(getDrawable(R.drawable.level2));
            }else if(mDevicesRssi.get(device) < -55){
                viewHolder.image.setBackground(getDrawable(R.drawable.level3));
            }else if(mDevicesRssi.get(device) < 0){
                viewHolder.image.setBackground(getDrawable(R.drawable.level4));
            }

            return view;
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mLeDeviceListAdapter.addDevice(device, rssi);
                            if(mLeDeviceListAdapter.getCount() == 0)
                                getActionBar().setTitle("Searching Devices..");
                            else if(mLeDeviceListAdapter.getCount() > 0){
                                getActionBar().setTitle("Available Devices");
                            }

                        }
                    });
                }
            };

    static class ViewHolder {
        TextView deviceName;
        TextView deviceRssi;
        ImageView image;
        TextView deviceAddress;
    }
}
