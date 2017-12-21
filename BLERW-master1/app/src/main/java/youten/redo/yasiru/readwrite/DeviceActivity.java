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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;


import youten.redo.yasiru.util.BaseCMD;
import youten.redo.yasiru.util.BleUtil;
import youten.redo.yasiru.util.BleUuid;
import youten.redo.yasiru.util.CommsChar;
import youten.redo.yasiru.util.QueryStrings;
import youten.redo.yasiru.util.classMessages;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class DeviceActivity extends Activity implements View.OnClickListener {
	private static final String TAG = "BLEDevice";

	public static final String EXTRA_BLUETOOTH_DEVICE = "BT_DEVICE";
	private BluetoothAdapter mBTAdapter;
	private BluetoothDevice mDevice;
	private BluetoothGatt mConnGatt;
	private CommsChar commsChar = new CommsChar();
	private BaseCMD basecmd = new BaseCMD();
	private QueryStrings QS = new QueryStrings();

	private int mStatus;
	private ArrayList<String> Q_data = new ArrayList<String>();
	private ArrayList<String> S_data = new ArrayList<String>();
	private String Generations;
	private String Types;
	private String Firmwares;
	private String Serials;
	private String States;
	private String Batterys;

	private byte[] bytearray;
	private byte[] bytearrayaddon;

	private byte[] query;
	private byte[] rtc;
	private byte[] battery;
	private byte[] start;
	private byte[] stop;
	private byte[] tag;
	private byte[] reuse;
	private byte[] usercomment;
	private byte[] serial;
	private byte[] state;

	private ImageButton queryButton;
	private ImageButton programButton;
	private ImageButton startButton;
	private ImageButton tagButton;
	private ImageButton stopButton;
	private ImageButton readButton;
	private ImageButton reuseButton;
	private short crc16;
	private int value = 0;


	private TextView Family;
	private TextView Model;
	private TextView Firmware;
	private TextView Serial;
	private TextView Battery;
	private TextView State;

	private BluetoothGattCharacteristic mWriteCharacteristic;
	private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	private int buttonState = 0;//1 = QueryLogger, 2 = Program parameters, 3 = Start Logger, 4 = Tag Logger,
	// 5 = Stop logger, 6 = Reread logger, 7 = Read logger

	private Button getData;
	private TextView textView;
	private TextView textView2;

	private byte mWriteValue = 48;
	private static byte[] test;// = new byte[] {0x00,0x55,0x01,0x02,0x0D};
	private String testStr = "5556575859";
	private int i = 0;
	private Handler handler=new Handler();
	private final BluetoothGattCallback mGattcallback = new BluetoothGattCallback() {


		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
											int newState) {
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				mStatus = newState;
				mConnGatt.discoverServices();
			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				mStatus = newState;
				runOnUiThread(new Runnable() {
					public void run() {
						getData.setEnabled(false);

					};
				});
			}
		};

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			for (BluetoothGattService service : gatt.getServices()) {
				if ((service == null) || (service.getUuid() == null)) {
					continue;
				}
				// Find the characteristic that we want to write to
				mWriteCharacteristic = service.getCharacteristic(UUID.fromString(BleUuid.CHAR_MANUFACTURER_NAME_STRING));
				if(BleUuid.SERVICE_DEVICE_INFORMATION.equalsIgnoreCase(service.getUuid().toString())){
					getData.setTag(service.getCharacteristic(UUID.fromString(BleUuid.CHAR_MANUFACTURER_NAME_STRING)));
					queryButton.setTag(service.getCharacteristic(UUID.fromString(BleUuid.CHAR_MANUFACTURER_NAME_STRING)));
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							getData.setText("Connect");
							getData.setEnabled(true);
							//queryButton.setEnabled(true);
						}
					});
				}

			}



		};

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
											BluetoothGattCharacteristic characteristic) {
			gatt.readCharacteristic(characteristic);
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
										 BluetoothGattCharacteristic characteristic, int status) {


			int inc = 0;
			if (status == BluetoothGatt.GATT_SUCCESS) {
				if (BleUuid.CHAR_MANUFACTURER_NAME_STRING
						.equalsIgnoreCase(characteristic.getUuid().toString())) {
					//incoming = characteristic.getStringValue(0);
					//addingOn += incoming;
					//rawBytes = incoming.toCharArray().;

					//bytearray = characteristic.getValue();
					//outputStream.write(characteristic.getValue(),0,1);
					try {

						switch (buttonState){
							case 1://CMD BLE QUERY(0x20)
								outputStream.write(characteristic.getValue());
								query = outputStream.toByteArray();
								//Log.d("INFO",query[0] + "  " + query[1] + "  " + query[2] + "  " + query[query.length-1] + "  " +query.length + "  "  +"__________________________________");
								basecmd.BytetoHex(query);
								query = basecmd.ReadByte(query);
								Q_data = basecmd.CMDQuery(query);
								SystemClock.sleep(400);
								buttonState = 8;
								write();
								break;
							case 2://CMD_WRITE (0x03)		crc-low,crc-high

								break;
							case 3://CMD_START (0x04)
								outputStream.write(characteristic.getValue());
								start = outputStream.toByteArray();
								basecmd.BytetoHex(start);
								start = basecmd.ReadByte(start);
								if(basecmd.CMDStart(start) == 1){
                                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                    v.vibrate(300);
                                }
								break;
							case 4://CMD_TAG (0x05)
								outputStream.write(characteristic.getValue());
								tag = outputStream.toByteArray();
								basecmd.BytetoHex(tag);
								tag = basecmd.ReadByte(tag);
								if(basecmd.CMDTag(tag) == 1){
									Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
									v.vibrate(300);
								}

								break;
							case 5://CMD_STOP (0x06)
								outputStream.write(characteristic.getValue());
								stop = outputStream.toByteArray();
								basecmd.BytetoHex(stop);
								stop = basecmd.ReadByte(stop);
								if(basecmd.CMDStop(stop) == 1){
                                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                    v.vibrate(300);
                                }
								break;
							case 6:// CMD_READ (0x02)00-55-02-07-02-02-82-5E-01-2C-98-0D

								break;
							case 7://CMD_REUSE (0x07)
								outputStream.write(characteristic.getValue());
								reuse = outputStream.toByteArray();
								basecmd.BytetoHex(reuse);
								reuse = basecmd.ReadByte(reuse);
								if(basecmd.CMDReuse(reuse) == 1){
                                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                    v.vibrate(300);
                                }
								break;
							case 8:
								outputStream.write(characteristic.getValue());
								rtc = outputStream.toByteArray();
								basecmd.BytetoHex(rtc);
								rtc = basecmd.ReadByte(rtc);
								SystemClock.sleep(300);
								buttonState = 11;
								write();
								break;
							case 9:
								outputStream.write(characteristic.getValue());
								battery = outputStream.toByteArray();
								basecmd.BytetoHex(battery);
								battery = basecmd.ReadByte(battery);
								SystemClock.sleep(300);
								buttonState = 10;
								write();
								break;
							case 10:
								outputStream.write(characteristic.getValue());
								serial = outputStream.toByteArray();
								basecmd.BytetoHex(serial);
								serial = basecmd.ReadByte(serial);
							case 11:
								outputStream.write(characteristic.getValue());
								state = outputStream.toByteArray();
								basecmd.BytetoHex(state);
								state = basecmd.ReadByte(state);
								S_data = basecmd.CMDState(state);
						}

						outputStream.reset();

						//outputStream.write(characteristic.getValue());
					} catch (IOException e) {
						e.printStackTrace();
					}


					//bytearrayaddon = outputStream.toByteArray();
					//BytetoHex(bytearrayaddon);
					//Log.d("GET","Received data+++++++++++++++++++++++ "+ bytearray[0] + " " + bytearray[1] + " " + bytearray[2] + " " + bytearray[bytearray.length-1]+ " " + bytearray.length);
					//Log.d("GET","Received data+++++++++++++++++++++++ "+ cha);
					runOnUiThread(new Runnable() {
						public void run() {
							getData.setText("Connected");
							queryButton.setEnabled(true);
							startButton.setEnabled(true);
							stopButton.setEnabled(true);
							reuseButton.setEnabled(true);
							tagButton.setEnabled(true);
							programButton.setEnabled(true);
							readButton.setEnabled(true);
							//textView2.setText(incoming);
							if(Q_data.size() != 0 ) {

								Serial.setText("Logger Serial No. : " + Q_data.get(0));
								Log.d("PRINT", Q_data.get(0));
								Firmware.setText("Logger Firmware : " + Q_data.get(1));
								Log.d("PRINT", Q_data.get(1));
								Model.setText("Logger Model : " + QS.GetGeneration(Integer.parseInt(Q_data.get(2))));
								Log.d("PRINT", QS.GetGeneration(Integer.parseInt(Q_data.get(2))));
								Family.setText("Logger Family : " + QS.GetType(Integer.parseInt(Q_data.get(3))));
								Log.d("PRINT", QS.GetType(Integer.parseInt(Q_data.get(3))));
							}

							if(S_data.size() != 0){

								State.setText("Logger State : " + QS.GetState(Integer.parseInt(S_data.get(0))));
								Log.d("PRINT", QS.GetState(Integer.parseInt(S_data.get(0))));
								Battery.setText("Estimated Battery : " +  S_data.get(1));
								Log.d("PRINT", S_data.get(1));
							}

							setProgressBarIndeterminateVisibility(false);
						};
					});
				}

			}
			characteristic = gatt.getService(UUID.fromString(BleUuid.SERVICE_DEVICE_INFORMATION)).getCharacteristic(UUID.fromString(BleUuid.CHAR_MANUFACTURER_NAME_STRING));

			//enable notifications
			gatt.setCharacteristicNotification(characteristic, true);
			BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
					UUID.fromString(BleUuid.BLE_CONFIG));
			descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			mConnGatt.writeDescriptor(descriptor);
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
										  BluetoothGattCharacteristic characteristic, int status) {
			outputStream.reset();
			if (status != BluetoothGatt.GATT_SUCCESS) {
				Log.w(TAG, "Unable to write to characteristic " + characteristic.getUuid());
				//mConnGatt.abortReliableWrite(mDevice);
			} else {
				//mConnGatt.executeReliableWrite();
				// Update interface
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						//queryButton.setEnabled(true);
						mWriteValue = (byte) ((mWriteValue + 1));

						//queryButton.setText(mWriteValue);

						// create a for loop here with  i = 0; i < length and send the bytes one at a time

					}
				});
			}

		}
	};
//https://www.programcreek.com/java-api-examples/index.php?source_dir=android-bluetooth-demo-master/app/src/main/java/com/pixplicity/bluetoothdemo/MainActivity.java
	//this link will help find what to put into the write funciton and what to do in the  oncharactoristic write
	/**
	 * Writes a value (alternating 0 and 1) to the characteristics
	 */
	private void write() {
		if (mWriteCharacteristic == null) {
			Log.e(TAG, "There's no characteristic to write to");
		}
		//the bytes in the test array should be changed for some commands depending on the requirements
		switch (buttonState){
			case 1://CMD BLE QUERY(0x20)
				//test = new byte[] {0x00,0x55,0x01,0x02,0x7C,0x0E,0x0D}; //COmplete
				test = new byte[] {0x00,0x55,0x20,0x02,(byte)0xAB,0x3B,0x0D};


				break;
			case 2://CMD_WRITE (0x03)		crc-low,crc-high
				test = new byte[] {0x00,0x55,0x03,0x02,0x0D};
				break;
			case 3://CMD_START (0x04)
				test = new byte[] {0x00,0x55,0x04,0x02,(byte) 0x89,(byte) 0xF1,0x0D}; //COmplete
				break;
			case 4://CMD_TAG (0x05)
				test = new byte[] {0x00,0x55,0x05,0x02,(byte) 0xB8,(byte) 0xC2,0x0D}; //COmplete
				break;
			case 5://CMD_STOP (0x06)
				test = new byte[] {0x00,0x55,0x06,0x02,(byte) 0xEB,(byte) 0x97,0x0D}; //COmplete
				break;
			case 6:// CMD_READ (0x02)00-55-02-07-02-02-82-5E-01-2C-98-0D
				test = new byte[] {0x00,0x55,0x02,0x07,0x02,0x02,(byte)0x82,0x5E,0x01,0x2C,(byte)0x98,0x0D};
				break;
			case 7://CMD_REUSE (0x07)
				test = new byte[] {0x00,0x55,0x07,0x02,(byte) 0xDA,(byte) 0xA4,0x0D}; //COmplete
				break;
			case 8:
				test = new byte[] {0x00,0x55,0x0B,0x03,0x00,0x3E,0x69,0x0D};
				break;
			case 9:
				test = new byte[] {0x00,0x55,0x10,0x03,0x00,(byte)0xAC,(byte)0xDA,0x0D};
				break;
			case 10:
				test = new byte[] {0x00, 0x55, 0x50, 0x02,(byte) 0xF2, 0x33, 0x0D};
			case 11:
				test = new byte[] {0x00, 0x55, 0x21, 0x02,(byte) 0x9A, 0x08, 0x0D};
		}




		// Disable until write has finished to prevent sending faster than the connection can handle
		//queryButton.setEnabled(false);


		Runnable runnableCode = new Runnable() {
			@Override
			public void run() {
				if ((mWriteCharacteristic.getProperties() | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
					//Log.i(TAG, "Writing data to bluetooth device...");
					//mConnGatt.beginReliableWrite();
					mWriteCharacteristic.setValue(new byte[] {test[i]});
    					//Log.d("SEND", test[i] + "This is what I need to send____________________________");
				} else {
					Log.w(TAG, "Characteristic  not writable");
				}
				i++;
				if(i < test.length){
					handler.postDelayed(this,3);
				}
			}
		};
		i = 0;
		handler.postDelayed(runnableCode,3);



	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_device);

		getActionBar().setBackgroundDrawable(new ColorDrawable(0xFF28A6DC));

		// state
		mStatus = BluetoothProfile.STATE_DISCONNECTED;
		getData = (Button) findViewById(R.id.read_manufacturer_name_button);
		textView = (TextView) findViewById(R.id.textView);
		textView2 = (TextView) findViewById(R.id.textView2);

		Family = (TextView) findViewById(R.id.family);
		Model = (TextView) findViewById(R.id.model);
		Firmware = (TextView) findViewById(R.id.firmware);
		Serial = (TextView) findViewById(R.id.serialno);
		Battery = (TextView) findViewById(R.id.battery);
		State = (TextView) findViewById(R.id.state);

		getData.setOnClickListener(this);
		queryButton = (ImageButton) findViewById(R.id.querybtn);
		programButton = (ImageButton) findViewById(R.id.programbtn);
		startButton = (ImageButton) findViewById(R.id.startbtn);
		tagButton  = (ImageButton) findViewById(R.id.tagbtn);
		stopButton = (ImageButton) findViewById(R.id.stopbtn);
		readButton = (ImageButton) findViewById(R.id.readbtn);
		reuseButton = (ImageButton) findViewById(R.id.reusebtn);


		queryButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Log.i(TAG, "Press quary button checking....");
				buttonState = 1;
				write();
				//SystemClock.sleep(150);
				//buttonState = 8;
				//write();
			}
		});

		programButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				buttonState = 2;
				write();
			}
		});

		startButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Log.i(TAG, "Press start button checking....");
				buttonState = 3;
				write();
			}

		});

		tagButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				buttonState = 4;
				write();
			}
		});


		stopButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				buttonState = 5;
				write();
			}
		});

		readButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				buttonState = 6;
				write();
			}
		});

		reuseButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				buttonState = 7;
				write();
			}
		});
	}





	@Override
	protected void onResume() {
		super.onResume();

		init();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mConnGatt != null) {
			if ((mStatus != BluetoothProfile.STATE_DISCONNECTING)
					&& (mStatus != BluetoothProfile.STATE_DISCONNECTED)) {
				mConnGatt.disconnect();
			}
			mConnGatt.close();
			mConnGatt = null;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.read_manufacturer_name_button) {
			if ((v.getTag() != null)
					&& (v.getTag() instanceof BluetoothGattCharacteristic)) {
				BluetoothGattCharacteristic ch = (BluetoothGattCharacteristic) v
						.getTag();
				if (mConnGatt.readCharacteristic(ch)) {
					setProgressBarIndeterminateVisibility(true);
				}
			}
		}
	}

	private void init() {
		// BLE check
		if (!BleUtil.isBLESupported(this)) {
			Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT)
					.show();
			finish();
			return;
		}

		// BT check
		BluetoothManager manager = BleUtil.getManager(this);
		if (manager != null) {
			mBTAdapter = manager.getAdapter();
		}
		if (mBTAdapter == null) {
			Toast.makeText(this, R.string.bt_unavailable, Toast.LENGTH_SHORT)
					.show();
			finish();
			return;
		}

		// check BluetoothDevice
		if (mDevice == null) {
			mDevice = getBTDeviceExtra();
			if (mDevice == null) {
				finish();
				return;
			}
		}

		// button disable
		getData.setText("Initialising");
		getData.setEnabled(false);
		queryButton.setEnabled(false);
		startButton.setEnabled(false);
		stopButton.setEnabled(false);
		reuseButton.setEnabled(false);
		tagButton.setEnabled(false);
		programButton.setEnabled(false);
		readButton.setEnabled(false);

		// connect to Gatt
		if ((mConnGatt == null)
				&& (mStatus == BluetoothProfile.STATE_DISCONNECTED)) {
			// try to connect
			mConnGatt = mDevice.connectGatt(this, false, mGattcallback);
			mStatus = BluetoothProfile.STATE_CONNECTING;
		} else {
			if (mConnGatt != null) {
				// re-connect and re-discover Services
				mConnGatt.connect();
				mConnGatt.discoverServices();
			} else {
				Log.e(TAG, "state error");
				finish();
				return;
			}
		}
		setProgressBarIndeterminateVisibility(true);
	}

	private BluetoothDevice getBTDeviceExtra() {
		Intent intent = getIntent();
		if (intent == null) {
			return null;
		}

		Bundle extras = intent.getExtras();
		if (extras == null) {
			return null;
		}

		return extras.getParcelable(EXTRA_BLUETOOTH_DEVICE);
	}





}

