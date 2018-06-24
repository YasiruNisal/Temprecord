package com.example.yasiruw.temprecord.services;

//======================================//

import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.util.Log;

import static com.example.yasiruw.temprecord.CustomLibraries.Yo_Library.Update_UI;
import static com.example.yasiruw.temprecord.CustomLibraries.Yo_Library.byte_array_To_Hex_String;


//======================================//
//==========================================================//


//======================================//

/**USB Mass Storage
 * @author      Yo LAHOLA <yo.lahola@ live.com>
 * @version     1.0
 * @since       1.0
 * */
//==========================================================//
public class USB_HID_RECEIVE_Thread extends Thread
//==========================================================//
{
    //<editor-fold default state="collapsed" desc="Classes">
    //==========================================================//
    //CLASS
    //==========================================================//
    //==========================================================//
    //</editor-fold>

    //<editor-fold default state="collapsed" desc="Constant">
    //==========================================================//
    //Constant
    //==========================================================//
    private static  final   int                 mTimeout               = 100;   //100ms
    //==========================================================//
    //</editor-fold>

    //<editor-fold default state="collapsed" desc="Static">
    //==========================================================//
    //Static
    //==========================================================//
    //==========================================================//
    //</editor-fold>

    //<editor-fold default state="collapsed" desc="Private">
    //==========================================================//
    //Private
    //==========================================================//
    private                 byte[]              m_message_received;
    private                 UsbDeviceConnection m_usb_connection;
    private                 UsbEndpoint         m_usb_endpoint;
    private                 Context             m_context;
    //==========================================================//
    //</editor-fold>


    //<editor-fold default state="collapsed" desc="CLASS CONSTRUCTOR()">
    //==========================================================//
    /**<b>USB_HID_RECEIVE_Thread Class Constructor</b><br>
     */
    //====================================================================//
    USB_HID_RECEIVE_Thread(Context context, UsbDeviceConnection usb_connection, UsbEndpoint usb_endpoint)
    {
        m_context           = context;
        m_usb_connection    = usb_connection;
        m_usb_endpoint      = usb_endpoint;
        m_message_received  = new byte[m_usb_endpoint.getMaxPacketSize()];
    }
    //====================================================================//
    //</editor-fold>

    //<editor-fold default state="collapsed" desc="Run()">
    //==========================================================//
    /**<b>Main Running Thread /b><br>
    */
    //==========================================================//
    @Override
    public void run()
    {
        while (m_usb_connection != null)
        {
            int     P_offset = 0;
            while   (P_offset != -1)
            {
                int receivedLength = m_usb_connection.bulkTransfer(m_usb_endpoint,m_message_received,P_offset,m_message_received.length - P_offset, mTimeout);

                if (receivedLength > 0)
                {
                    P_offset += receivedLength;                                                     //Keep Reading

                    //Log.i("YO","ADDRESS HID = " + m_usb_endpoint.getAddress());
                    if (P_offset >= m_usb_endpoint.getMaxPacketSize())
                    {
                        int E_O_P = m_message_received[1];                                          //End of Packet (Based on Length)
                        if (m_message_received[E_O_P] == 0x0d)
                        {   //Valid Message
                            //------------
                            //Log.i("USB", "we found 0x0d at position EOP " + E_O_P);
                            byte[] m_message = new byte[E_O_P+1];

                            for (int i = 0; i <= E_O_P ; i++)
                            {
                                m_message[i] =(byte) (m_message_received[i] & 0xFF);
                            }



                            Update_UI(m_context, null, "HID_USB_Message_Received", m_message, null);
                        }
                        else
                        {   //ERROR Message!
                            Update_UI(m_context, "-> ERROR !!! HID RECEIVE DATA : [" + receivedLength + "/" + P_offset + "]" + byte_array_To_Hex_String(m_message_received, receivedLength), "UI_update", null, null);
                        }
                    }
                }
                P_offset = -1;
            }
        }
    }
    //====================================================================//
    //</editor-fold>

}

