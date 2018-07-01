package com.example.yasiruw.temprecord.CustomLibraries;

//======================================//

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
//======================================//


//TODO VERSION RETURN


//======================================//

/**USB Mass Storage
 * @author      Yo LAHOLA <yo.lahola@ live.com>
 * @version     1.0
 * @since       1.0
 * */
//==========================================================//
public class Yo_Library
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
    //==========================================================//
    //</editor-fold>


/*  //<editor-fold default state="collapsed" desc="FUNCTION()">
    //==========================================================//
    /**<b>Create PDF Document based on recorded Temperature in a .Txt file</b><br>
     * @param m_context GUI Context argument.*/
    //==========================================================//


    //==========================================================//
    //</editor-fold>











    //====================================================================//
    static String Yo_Append_to_String(String Original, String To_Append)
    {
        StringBuilder strBuilder = new StringBuilder(Original);
        strBuilder.append(To_Append);
        return(strBuilder.toString());
    }
    //====================================================================//

    //====================================================================//
    static Long Yo_Date_to_Unix_Conversion(Date m_date)
    {
        return(m_date.getTime() / 1000L);
    }
    //====================================================================//

    //====================================================================//
    static Date Yo_String_to_Date_Conversion(String Date_Time)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
        Date m_date = null;

        try
        {
            m_date =formatter.parse(Date_Time);
        }
        catch (ParseException e)
        {
            Log.i("YO", "CONVERSION DATE ERROR !");
        }
        return(m_date);
    }
    //====================================================================//








    //====================================================================//
    static long Get_Current_Time_in_MilliSecond()
    {
        return(System.currentTimeMillis());
    }
    //====================================================================//

    //====================================================================//
    public static void Error_Log(String[] m_error_message)
    {
        Log.i("YO","#"+m_error_message[0]+m_error_message[1]+" ERROR : " + m_error_message[2] + " T: " + Get_Current_Time_in_MilliSecond());
    }
    //====================================================================//

    //====================================================================//
    public static void Comment_Log(String[] m_comment_message)
    {

        Log.i("YO","#"+m_comment_message[0]+" : " + m_comment_message[1] + " T: " + Get_Current_Time_in_MilliSecond());
    }
    //====================================================================//

    //====================================================================//
    static int String_To_Integer(String S)
    {
         return (Integer.parseInt(S));
    }
    //====================================================================//

    //====================================================================//
    static String Integer_To_String(int i)
    {
        return(String.valueOf(i));
    }
    //====================================================================//

    //====================================================================//
    public static String byte_array_To_Hex_String(byte[] byte_array,int Length)
    {
        if ((byte_array != null) && (Length > 0))
        {
            Formatter formatter = new Formatter();
            for (int b = 0; b < Length; b++)
            {
                formatter.format("%02x-", byte_array[b]);
            }
            return(formatter.toString());
        }
        return ("");
    }
    //====================================================================//

    //====================================================================//
    static String Int_Array_To_Hex_String(int[] int_array,int Length)
    {
        if ((int_array != null) && (Length > 0))
        {
            Formatter formatter = new Formatter();
            for (int b = 0; b < Length; b++)
            {
                formatter.format("%02x-", int_array[b]);
            }
            return(formatter.toString());
        }
        return ("");
    }
    //====================================================================//

    //====================================================================//
    public static void Update_UI(Context context,String mString,String Package,byte[] b_data,int[] i_data)
    {
        Bundle bundle = new Bundle();
        bundle.putInt("flag",0);
        bundle.putByteArray("b_data", b_data);
        bundle.putIntArray("i_data",i_data);


        bundle.putString("message",mString);
        New_Send_Update_internal_intent(context,Package,bundle);
    }
    //====================================================================//

    //==========================================================//
    private static void New_Send_Update_internal_intent(Context context, String Package, Bundle parameters)
    {
        Intent panel_internal_intent = new Intent("panel_internal_intent");
        panel_internal_intent.setPackage(Package);
        panel_internal_intent.putExtras(parameters);
        LocalBroadcastManager.getInstance(context).sendBroadcast(panel_internal_intent);
    }
    //==========================================================//

    //==========================================================//
    static int GetBit(int Value, int bit)
    {
        return (Value >> bit) & 1;
    }
    //==========================================================//

    //==========================================================//
    static int SetBit(int Value, int bit)
    {
        return 	(Value |= (1 << bit));
    }
    //==========================================================//

    //==========================================================//
    static int ClearBit(int Value, int bit)
    {
        return (Value &= ~(1 << bit));
    }
    //==========================================================//

    //==========================================================//
    static void TEMPO(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch (InterruptedException e)
        {
        }
    }
    //==========================================================//

    //==========================================================//
    static String Convert_UNIX_Date_Time_To_String(long date_time)
    {
        Date d = new Date(date_time*1000L);
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return(f.format(d))  ;
    }
    //==========================================================//

    //==========================================================//
    public static String Convert_UNIX_To_Date(long date_time)
    {
        Date date = new Date(date_time * 1000L);
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }
    //==========================================================//

    //==========================================================//
    public static String Convert_UNIX_To_Time(long date_time)
    {


        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
        Date date = new Date(date_time * 1000L);
        sdf.format(date);
        return(sdf.format(date));




        //Date date = new Date(date_time * 1000L);
        //return new SimpleDateFormat("HH:mm:ss z").format(date);
    }
    //==========================================================//


    //==========================================================//
    static byte[] Convert_Byte_Buffer_to_byte_array(ByteBuffer buffer)
    {
        if (buffer.remaining() > 0)
        {
            byte[] byte_array = new byte[buffer.remaining()];
            buffer.get(byte_array, 0, byte_array.length);
            return (byte_array);
        }
        return (null);
    }
    //==========================================================//


}
