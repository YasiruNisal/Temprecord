package com.temprecordapp.yasiruw.temprecord.services;

//======================================//

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.temprecordapp.yasiruw.temprecord.CustomLibraries.Yo_Library.Comment_Log;
import static com.temprecordapp.yasiruw.temprecord.CustomLibraries.Yo_Library.Convert_UNIX_To_Date;
import static com.temprecordapp.yasiruw.temprecord.CustomLibraries.Yo_Library.Convert_UNIX_To_Time;
import static com.temprecordapp.yasiruw.temprecord.CustomLibraries.Yo_Library.Error_Log;

//======================================//
//======================================//

//======================================//

/**USB Mass Storage
 * @author      Yo LAHOLA <yo.lahola@ live.com>
 * @version     1.0
 * @since       1.0
 * */
//==========================================================//
public class TXT_FILE
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
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    //==========================================================//
    //</editor-fold>

    //<editor-fold default state="collapsed" desc="Static">
    //==========================================================//
    //Static
    //==========================================================//
    private static  String[]            PERMISSIONS_STORAGE     = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static  InputStreamReader   m_input_stream_reader;
    private static  BufferedReader      m_buffer_reader;
   //==========================================================//
    //</editor-fold>

    //<editor-fold default state="collapsed" desc="Private">
    //==========================================================//
    //Private
    //==========================================================//
    //==========================================================//
    //</editor-fold>


    //==========================================================//
    public static void Verify_Storage_Permissions(Activity activity)
    {
        //Check if we have write permission
//        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (permission != PackageManager.PERMISSION_GRANTED)

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=  PackageManager.PERMISSION_GRANTED)
        {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }
    //==========================================================//

    //==========================================================//
    // TXT READ
    //==========================================================//
    //==========================================================//
    public static boolean Open_Txt_File_for_Read(Context m_context, String m_local_app_folder, String m_filename)
    //==========================================================//
    {
        if (m_local_app_folder == null)
        {
            m_local_app_folder  = m_context.getFilesDir().getAbsolutePath();
        }

        File m_file_to_read     = new File(m_local_app_folder, m_filename);
        Comment_Log(new String[]{"TXT","Opening for Read : " + m_file_to_read.toString()});

        if(m_file_to_read.exists())
        {   //Ok File exist
            try
            {
                FileInputStream m_input_stream  = m_context.openFileInput(m_filename);
                m_input_stream_reader           = new InputStreamReader(m_input_stream);
                m_buffer_reader                 = new BufferedReader(m_input_stream_reader);
            }
            catch (IOException e)
            {
                Error_Log(new String[]{"TXT","000",e.toString()});
            }
        }
        else
        {
            Comment_Log(new String[]{"TXT","File for Read does not exist ! : " + m_file_to_read.toString()});
            return (false);
        }
        return (true);
    }
    //==========================================================//

    //==========================================================//
    public static String Read_from_Txt_File()
    //==========================================================//
    {
        String  m_read_line = null;
        try
        {
            m_read_line = m_buffer_reader.readLine();

            if (m_read_line == null)
            {
                Close_Read_Txt_File();
            }
        }
        catch (IOException e)
        {
            Error_Log(new String[]{"TXT","001",e.toString()});
        }
        return(m_read_line);
    }
    //==========================================================//

    //==========================================================//
    private static void Close_Read_Txt_File()
    //==========================================================//
    {
        try
        {
            m_input_stream_reader.close();
        }
        catch (IOException e)
        {
            Error_Log(new String[]{"TXT","002",e.toString()});
        }
    }
    //==========================================================//




    //==========================================================//
    // TXT WRITE
    //==========================================================//
    //==========================================================//
    public boolean Open_Write_and_Close_Txt_File(Context m_context, String m_local_app_folder, String m_filename,String m_data,Boolean m_new_file_flag)
    //==========================================================//
    {
        if (m_local_app_folder == null)
        {
            m_local_app_folder  = m_context.getFilesDir().getAbsolutePath();
        }

        File m_file_to_write    = new File(m_local_app_folder, m_filename);
        //Comment_Log(new String[]{"TXT","Opening for Write : " + m_file_to_write.toString()});

        if(m_file_to_write.exists() & !m_new_file_flag)
        {   //File already exist, so append it
            //Comment_Log(new String[]{"TXT","Append to : " + m_file_to_write.toString()});
            Append_Txt_File(m_file_to_write,m_data);
        }
        else
        {   //File does not exist yet, so create it and write new data
            //Comment_Log(new String[]{"TXT","Create Write File : " + m_file_to_write.toString()});
            try
            {
                FileOutputStream m_output_stream = new FileOutputStream(m_file_to_write, false);
                m_output_stream.write(m_data.getBytes());
                m_output_stream.close();
            }
            catch (FileNotFoundException e)
            {
                Error_Log(new String[]{"TXT","003",e.toString()});
            }
            catch (IOException e)
            {
                Error_Log(new String[]{"TXT","004",e.toString()});
            }
            return (false);
        }

        return (true);
    }
    //==========================================================//

    //==========================================================//
    private static void Append_Txt_File(File file,String Data)
    //==========================================================//
    {
        try
        {
            FileOutputStream m_output_stream = new FileOutputStream(file, true);
            m_output_stream.write(Data.getBytes());
            m_output_stream.close();
        }
        catch (FileNotFoundException e)
        {
            Error_Log(new String[]{"TXT","005",e.toString()});
        }
        catch (IOException e)
        {
            Error_Log(new String[]{"TXT","006",e.toString()});
        }
    }
    //==========================================================//



//    static boolean Open_Write_and_Close_Txt_File(Context m_context, String m_local_app_folder, String m_filename,String m_data,Boolean m_new_file_flag)
//    //==========================================================//
//    {





    public String Read_and_Display_Txt_File(Context m_context, String m_filename)
    {

        String REPORT ="";
        String          m_Data = "";
        //==========================================================//
        //OPEN REPORT TXT FILE
        //==========================================================//
        if (Open_Txt_File_for_Read(m_context,null,m_filename))
        {
            //============================================//
                      m_Data      = Read_from_Txt_File();
            //============================================//
//int i =0;
//            while (m_Data != null)
//            {
//                String[]    m_separated = m_Data.split(",");
////                String      m_date      = Convert_UNIX_To_Date(Long.parseLong(m_separated[0]));
////                String      m_time      = Convert_UNIX_To_Time(Long.parseLong(m_separated[0]));
//
//REPORT += "\t\t\t\t" + m_separated[1]+"\r\n";
//
//                //============================================//
//                m_Data      = Read_from_Txt_File();
//                //============================================//
//            }
//return REPORT;
        }
//        Log.i("parameters", m_Data);
        return m_Data;
    }
}
