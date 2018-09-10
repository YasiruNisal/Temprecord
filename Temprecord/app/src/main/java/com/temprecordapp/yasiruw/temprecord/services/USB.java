package com.temprecordapp.yasiruw.temprecord.services;

//======================================//

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.temprecordapp.yasiruw.temprecord.comms.CommsSerial;

import java.util.HashMap;

import static android.content.Context.USB_SERVICE;
import static com.temprecordapp.yasiruw.temprecord.activities.MainActivity.m_next_memory_address;
import static com.temprecordapp.yasiruw.temprecord.CustomLibraries.Yo_Library.Update_UI;

//======================================//
//======================================//



//====================================================================//
/*
Mon-T2 with PDF
    m_usb_interface[0]
    UsbInterface[mId=0,mAlternateSetting=0,mName=null,mClass=3,mSubclass=0,mProtocol=0,
    mEndpoints=
    [
        UsbEndpoint[mAddress=129,mAttributes=3,mMaxPacketSize=64,mInterval=1]
        UsbEndpoint[mAddress=1,mAttributes=3,mMaxPacketSize=64,mInterval=1]
    ]

    m_usb_interface[1]
    UsbInterface[mId=1,mAlternateSetting=0,mName=null,mClass=8,mSubclass=6,mProtocol=80,
    mEndpoints=
    [
        UsbEndpoint[mAddress=130,mAttributes=2,mMaxPacketSize=64,mInterval=1]
        UsbEndpoint[mAddress=2,mAttributes=2,mMaxPacketSize=64,mInterval=1]
    ]

    https://www.keil.com/pack/doc/mw/USB/html/_u_s_b__device__descriptor.html

    http://www.usb.org/developers/defined_class
    mClass=3 HID
    mClass=8 Mass Storage

    https://www.keil.com/pack/doc/mw/USB/html/_u_s_b__endpoint__descriptor.html

https://docs.microsoft.com/en-us/windows-hardware/drivers/storage/device-object-example-for-a-usb-mass-storage-device
https://developer.apple.com/library/content/documentation/DeviceDrivers/Conceptual/MassStorage/01_Introduction/Introduction.html
*/
//====================================================================//

//======================================//

//======================================//
/**USB
 * @author Yo LAHOLA <yo.lahola@ live.com>
 * @version 1.0
 * @since 1.0
 * */
//==========================================================//
public class USB
//==========================================================//
{
    private CommsSerial commsSerial = new CommsSerial();
    //<editor-fold default state="collapsed" desc="Constant">
//==========================================================//
//Constant
//==========================================================//
//Mon-T2
    private static final int MonT_2_vendor_id = 5840;
    private static final int MonT_2_product_id = 2913;
    private static final int MonT_2_max_interface_number = 2;
    private static final int MonT_2_max_endpoint_number = 2;
    private static final int mTimeout = 100; //100ms
    private static final String ACTION_USB_PERMISSION = "com.example.yo.usb.USB_PERMISSION";
    //<editor-fold default state="collapsed" desc="Private">
//==========================================================//
//Private
//==========================================================//
    private Context m_context;
    private BroadcastReceiver m_usb_state_receiver;
    private UsbDevice m_usb_device;
    private UsbManager m_usb_manager;
    private Thread m_usb_HID_rx_thread;
    private Thread m_usb_MTP_rx_thread;
    private UsbDeviceConnection m_usb_connection;
    private UsbInterface[] m_usb_interface = new UsbInterface[MonT_2_max_interface_number];
    private UsbEndpoint[][] m_usb_endpoint = new UsbEndpoint[MonT_2_max_interface_number][MonT_2_max_endpoint_number];
//==========================================================//
//</editor-fold>
//<editor-fold default state="collapsed" desc="CLASS CONSTRUCTOR()">
//==========================================================//
    /**<b>USB Class Constructor</b><br>
     */
//====================================================================//
    public USB(Context context)
    {
        m_context = context;
    }
    //====================================================================//
//</editor-fold>
//====================================================================//
    public void unregister_receiver(Context context)
    {


        if (m_usb_HID_rx_thread != null)
        {
            //Log.i("TEST", "UNREGISTRING IN USB CALSS if1111");
            m_usb_HID_rx_thread.interrupt();
            m_usb_HID_rx_thread = null;
        }
        if (m_usb_state_receiver != null)
        {
            //Log.i("TEST", "UNREGISTRING IN USB CALSS if222222222222");
            context.unregisterReceiver(m_usb_state_receiver);
            m_usb_state_receiver = null;
        }
    }
    //====================================================================//
//====================================================================//
    public void Connection_Initialisation()
    {
        m_usb_manager   = (UsbManager) m_context.getSystemService(USB_SERVICE);

        Check_Device();
        if (m_usb_device != null)
        {
            //===================================================================//
            // BROADCAST RECEIVER
            //===================================================================//
            if (m_usb_state_receiver == null)
            {
            //======================================================//
            IntentFilter filter = new IntentFilter();
            filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
            filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
            //======================================================//

            //=========================//
            USB_open_connection();
            //=========================//

            //======================================================//
            m_usb_state_receiver = new BroadcastReceiver()
            {
                public void onReceive(Context context, Intent intent)
                {
                    String action = intent.getAction();
                    //Log.i("TEST1", "ACTION ************  " + action);
                    if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action))
                    {

                        Update_UI(m_context,"U02 <- onReceive ACTION_USB_DEVICE_ATTACHED","UI_update",null,null);
                    }
                    if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action))
                    {
                        Update_UI(m_context,"U03 <- onReceive DETACHED","UI_update",null,null);
                        //=========================//
                        USB_close_connection();
                        //=========================//
                    }
                }
            };
            //===================================================//

            //===================================================//
            //Log.i("TEST", "======== !!!!!RECEIVER REGISTERED HERE!!!!!====" + m_context + " " +m_usb_state_receiver );
            m_context.registerReceiver(m_usb_state_receiver, filter);
            //===================================================//
        }
            //===================================================================//
        }
    }
    //====================================================================//
//====================================================================//
    private void Check_Device()
    {
        m_usb_device = null;
        m_usb_manager = (UsbManager) m_context.getSystemService(USB_SERVICE);
        if (m_usb_manager != null)
        {
            HashMap<String, UsbDevice> deviceList = m_usb_manager.getDeviceList();
            for (UsbDevice device:deviceList.values())
            {
                if(device.getVendorId()==MonT_2_vendor_id)
                {
                    if(device.getProductId()==MonT_2_product_id)
                    {
                        m_usb_device = device;
                    }
                }
            }
        }
    }
    //====================================================================//
//====================================================================//
    private void USB_open_connection()
    {
//======================//
// FOR USB AUTHORIZATION
//======================//
        if (!m_usb_manager.hasPermission(m_usb_device))
        {
            Update_UI(m_context,"U04 <-USB_open_connection No Permission","UI_update",null,null);
            m_usb_manager.requestPermission(m_usb_device,PendingIntent.getBroadcast(m_context, 0, new Intent(ACTION_USB_PERMISSION), 0));
        }
        else
        {
            Update_UI(m_context,"U05 <-USB_open_connectionPermission OK","UI_update",null,null);
            m_usb_connection = m_usb_manager.openDevice(m_usb_device);
            if (m_usb_connection != null)
            {
                for (int nb_interface = 0; nb_interface < m_usb_device.getInterfaceCount();
                     nb_interface++)
                {
                    m_usb_interface[nb_interface] = m_usb_device.getInterface(nb_interface);
                    //Log.i("YO","=====> m_usb_interface["+nb_interface+"] = " +
                            //m_usb_interface[nb_interface]+"\r\n");
                    if (m_usb_connection.claimInterface(m_usb_interface[nb_interface], true))
                    {
                        switch (m_usb_interface[nb_interface].getInterfaceProtocol())
                        {
//-------------------------//
                            case 0: //HID
//-------------------------//
                                for (int m_nb_end_point = 0;m_nb_end_point <
                                        m_usb_interface[nb_interface].getEndpointCount(); m_nb_end_point++)
                                {
                                    m_usb_endpoint[nb_interface][m_nb_end_point] =
                                            m_usb_interface[nb_interface].getEndpoint(m_nb_end_point);
                                    if (m_usb_endpoint[nb_interface][m_nb_end_point].getDirection()
                                            == 128)
                                    {
                                        if (m_usb_HID_rx_thread == null)
                                        {
                                            m_usb_HID_rx_thread = new
                                                    USB_HID_RECEIVE_Thread(m_context, m_usb_connection, m_usb_endpoint[nb_interface][m_nb_end_point]);
                                            m_usb_HID_rx_thread.start();
                                        }
                                    }
                                }
                                break;
//-------------------------//
                        }
                    }
                }
//========================================//
                Update_UI(m_context,"U06 <- USB CONNECTED !","UI_update",null,null);

//========================================//
            }
            else
            {
                m_usb_connection = null;
            }
        }
    }
    //====================================================================//
//====================================================================//
    private void USB_close_connection()
    {
        //Log.i("YO","=========== USB_close !!!!!!!!!!");
        unregister_receiver(m_context);
        if (m_usb_connection != null)
        {
            Update_UI(m_context,"U99 <-USB closing...)","UI_update",null,null);
//When you are done communicating with a device or if the device was detached,
//close the UsbInterface and UsbDeviceConnection by calling releaseInterface() and close().
            for (int nb_interface = 0; nb_interface < m_usb_device.getInterfaceCount();
                 nb_interface++)
            {
                m_usb_connection.releaseInterface(m_usb_interface[nb_interface]);
                m_usb_interface[nb_interface] = null;
            }
//!!! ANDROID BUG HERE !!!
//m_usb_connection.close();
//!!! ANDROID BUG HERE !!!
            m_usb_connection = null;
        }
        m_usb_device = null;
        Update_UI(m_context,null,"DETACHED",null,null);
    }
    //====================================================================//
    //====================================================================//
    public void Send_Command(byte[] write)
    {
        //Log.i("USB", "Sendcommand====");
        //commsSerial.BytetoHex((write));
        if (m_usb_connection!= null)
        {
            m_usb_connection.bulkTransfer(m_usb_endpoint[0][1], write, write.length, 100);
        }
        else
        {
            Update_UI(m_context,"U21 -> SEND ERROR...","UI_update",null,null);
        }
    }
    //====================================================================//
}