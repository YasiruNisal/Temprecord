package com.temprecordapp.yasiruw.temprecord.services;

//======================================//

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.temprecordapp.yasiruw.temprecord.App;
import com.temprecordapp.yasiruw.temprecord.R;
import com.temprecordapp.yasiruw.temprecord.comms.BaseCMD;
import com.temprecordapp.yasiruw.temprecord.comms.MT2Values;
import com.temprecordapp.yasiruw.temprecord.CustomLibraries.Yasiru_Temp_Library;
import com.temprecordapp.yasiruw.temprecord.utils.App_Info;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.temprecordapp.yasiruw.temprecord.CustomLibraries.Yo_Library.Comment_Log;
import static com.temprecordapp.yasiruw.temprecord.CustomLibraries.Yo_Library.Convert_UNIX_To_Date;
import static com.temprecordapp.yasiruw.temprecord.CustomLibraries.Yo_Library.Convert_UNIX_To_Time;
import static com.temprecordapp.yasiruw.temprecord.CustomLibraries.Yo_Library.Error_Log;

//======================================//
//======================================//

//======================================//

/**Create and Open a PDF Document of recorded temperature
 * @author      Yo LAHOLA <yo.lahola@ live.com>
 * @version     1.0
 * @since       1.0
 * */
//==========================================================//
public class PDF
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
    private                 String                  K_PDF_FILENAME          = "yasiru_file.pdf";
    private static  final   String                  K_PDF_FOLDER            = "/Temprecord";
    static final    String FILENAME = "yo_file.txt";

    private static  final   int                     K_column_date           =  10;
    private static  final   int                     K_column_time           =  20; //space from the left of time column
    private static  final   int                     K_column_temp           =  100;
    private static  final   int                     K_v2_column_index       =  45;
    private static  final   int                     K_v2_column_date        =  70;
    private static  final   int                     K_v2_column_time        =  90;
    private static  final   int                     K_v2_column             =  60;
    private static  final   int                     K_v2_column_middle      =  340;
    private static  final   int                     K_v2_column_two         =  350;
    private static          int                     K_column_space          =  55;
    private static  final   int                     K_column_max            = 660;
    private static  final   int                     K_line_start            =  100;
    private static  final   int                     K_line_max              = 940;
    private static  final   int                     K_cr_line               =  15;
    //</editor-fold>

    //<editor-fold default state="collapsed" desc="Static">
    //==========================================================//
    //Static
    //==========================================================//
    private static          String                  m_pdf_folder;
    //</editor-fold>

    //<editor-fold default state="collapsed" desc="Private">
    //==========================================================//
    //Private
    //==========================================================//
    private                 boolean                 m_page_open             = false;
    private                 int                     m_current_column        = K_column_temp;
    private                 int                     m_current_column_v2     = K_column_date+20;
    private                 int                     m_current_line          = K_line_start + K_line_max + K_cr_line;
    private                 int                     m_page_number           = 1;

    private                 PdfDocument.Page        m_page;
    private                 Canvas                  m_canvas;
    private                 PdfDocument             m_pdf_document;
    private                 FileOutputStream        m_file_output_stream;

    private                 MT2Values.MT2Mem_values mt2Mem_values;
    private                 BaseCMD                 baseCMD;
    private Yasiru_Temp_Library QS = new Yasiru_Temp_Library();
    private                 App_Info                app_info = new App_Info();
    private                 boolean                 alternate = false;
    private                 int                     inc_val = 0;
    private                 int                     num_col = 0;
    private                 int                     current_col = 0;
    private                 ArrayList<Double>       ch1 = new ArrayList<Double>();
    private                 ArrayList<Double>       ch2 = new ArrayList<Double>();
    private                 ArrayList<Long>         time = new ArrayList<Long>();

                            StoreKeyService         storeKeyService;
    private                 int                 simplePDF = 0;

    private                 Paint                   line_paint = new Paint();
    private                 Paint                   RH_paint = new Paint();
    private                 Paint                   TEMP_paint = new Paint();
    private                 Paint                   circle_paint = new Paint();
    private                 Paint                   triangle_paint = new Paint();
    private                 Paint                   m_paint;
    private                 Paint                   m_paint_dark = new Paint();
    private                 Paint                   fade_out = new Paint();
    private                 Paint                   green_tick = new Paint();
    private                 Paint                   mark = new Paint();
    private                 Paint                   limit_temp = new Paint();
    private                 Paint                   limit_RH = new Paint();


    private                 int                     page_num = 1;

    private                 int                     line_startX = 10;
    private                 int                     line_startY = 80;
    private                 int                     line_endX = 690;
    private                 int                     line_endY = 80;

    private                 int                     report_startX = 10;
    private                 int                     report_startY = 70;

    private                 int                     serial_startX = 275;
    private                 int                     serial_startY = 70;

    private                 int                     temprecord_startX = 300;
    private                 int                     temprecord_startY = 30;

    private                 int                     RH_startX = 650;
    private                 int                     RH_startY = 70;

    private                 int                     temp_startX = 650;
    private                 int                     temp_startY = 50;

    private                 int                     page_startX = 600;
    private                 int                     page_startY = 980;

    private                 int                     blue_text_size = 18;
    private                 int                     page_text_size = 20;
    private ArrayList<String> Q_data = new ArrayList<String>();
    private ArrayList<String> U_data = new ArrayList<String>();
    private ArrayList<String> F_data = new ArrayList<String>();
    private ArrayList<String> R_data = new ArrayList<String>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss aa");
    private String Temp_unit = "";
    private boolean my_unit = true;
    private String temp_upper_breached = "";
    private String temp_lower_breached = "";
    private String RH_upper_breached = "";
    private String RH_lower_breached = "";
    //==========================================================//
    //</editor-fold>


    //<editor-fold default state="collapsed" desc="Create_Report()">
    //==========================================================//
    /**<b>Create PDF Document based on recorded Temperature in a .Txt file</b><br>
     * @param m_context GUI Context argument.*/
    //==========================================================//
    public void Create_Report(Context m_context)
    {
        m_pdf_folder = Environment.getExternalStorageDirectory().toString()+K_PDF_FOLDER;

        try {
            //==========================================================//
            //  Prepare File Handler for PDF File
            //==========================================================//
            Date c = Calendar.getInstance().getTime();
            String formattedDate = QS.UTCtoLocal(c.getTime());
            K_PDF_FILENAME = "("+baseCMD.serialno+")" + formattedDate+".pdf";
            final File file = new File(m_pdf_folder, K_PDF_FILENAME);
            m_file_output_stream = new FileOutputStream(file);
            //==========================================================//

            //==========================================================//
            //OPEN REPORT TXT FILE
            //==========================================================//
            //storing values in arraylists so they can be used through out the program
            for(int i = 0; i < mt2Mem_values.Data.size(); i++){
                ch1.add(mt2Mem_values.Data.get(i).valueCh0());
                time.add((mt2Mem_values.Data.get(i).valTime.getTime()) / 1000);
            }
            if(baseCMD.ch2Enable){
                for(int i = 0; i < mt2Mem_values.Data.size(); i++){
                    ch2.add(mt2Mem_values.Data.get(i).valueCh1());
                }
            }

            //determining what temperature unit needs displaying
            if (storeKeyService.getDefaults("UNITS", App.getContext()) != null && storeKeyService.getDefaults("UNITS", App.getContext()).equals("1")) {
                Temp_unit = QS.imperial(false);
                my_unit = true;
            }else{
                Temp_unit = QS.imperial(true);
                my_unit = false;
            }

            //single or multi column on values pages
            if(StoreKeyService.getDefaults("VALUE", App.getContext()) != null && StoreKeyService.getDefaults("VALUE", App.getContext()).equals("1")){
                K_column_space = 600;
            }else{
                K_column_space = 55;
            }



            //================//
            Create_a_PDF_Document();
            //================//

            line_paint.setColor(Color.BLUE);
            m_page_open = false;
            m_current_column = K_column_temp;
            m_current_line = K_line_start + K_line_max + K_cr_line;
            String m_ref_date = "--------";

            //============================================//
           // String m_Data = Read_from_Txt_File();
            num_col = (int)Math.ceil((K_column_max-K_column_temp)/K_column_space)+1;
            Inc_Line_and_Check_if_need_new_page();
            //=============================Logger Report=========================================
            if(mt2Mem_values.ch0Stats.TotalLimit == 0 && mt2Mem_values.ch1Stats.TotalLimit == 0){//drawing the tick if within limits
                Drawable d = App.getContext().getResources().getDrawable(R.drawable.greentick);
                d.setBounds(app_info.sign_left, app_info.sign_top, app_info.sign_right, app_info.sign_bottom);
                d.draw(m_canvas);
                m_canvas.drawText(App.getContext().getString(R.string.within_limits), app_info.limitinfo_startX, app_info.limitinfo_startY, m_paint_dark);
            }else{//drawing the warning sign if out of limits
                m_canvas.drawText(App.getContext().getString(R.string.outof_limits), app_info.limitinfo_startX, app_info.limitinfo_startY, m_paint_dark);
                Drawable d = App.getContext().getResources().getDrawable(R.drawable.redwarning);
                d.setBounds(app_info.sign_left, app_info.sign_top, app_info.sign_right, app_info.sign_bottom);
                d.draw(m_canvas);
            }


            //drawing the box around the warning and tick sign
            m_canvas.drawLine(app_info.box1_X1, app_info.box1_Y1, app_info.box1_X2, app_info.box1_Y1,m_paint_dark);
            m_canvas.drawLine(app_info.box1_X1, app_info.box1_Y2, app_info.box1_X2, app_info.box1_Y2,m_paint_dark);
            m_canvas.drawLine(app_info.box1_X1, app_info.box1_Y1, app_info.box1_X1, app_info.box1_Y2,m_paint_dark);
            m_canvas.drawLine(app_info.box1_X2, app_info.box1_Y1, app_info.box1_X2, app_info.box1_Y2,m_paint_dark);

            //drawing the box for the omments at the bottom of first page
            m_canvas.drawLine(app_info.box2_X1, app_info.box2_Y1, app_info.box2_X2, app_info.box2_Y1,m_paint);
            m_canvas.drawLine(app_info.box2_X1, app_info.box2_Y2, app_info.box2_X2, app_info.box2_Y2,m_paint);
            m_canvas.drawLine(app_info.box2_X1, app_info.box2_Y1, app_info.box2_X1, app_info.box2_Y2,m_paint);
            m_canvas.drawLine(app_info.box2_X2, app_info.box2_Y1, app_info.box2_X2, app_info.box2_Y2,m_paint);

            //drawing the box for the signature at the bottom of the first page
            m_canvas.drawLine(app_info.box3_X1, app_info.box3_Y1, app_info.box3_X2, app_info.box3_Y1,m_paint);
            m_canvas.drawLine(app_info.box3_X1, app_info.box3_Y2, app_info.box3_X2, app_info.box3_Y2,m_paint);
            m_canvas.drawLine(app_info.box3_X1, app_info.box3_Y1, app_info.box3_X1, app_info.box3_Y2,m_paint);
            m_canvas.drawLine(app_info.box3_X2, app_info.box3_Y1, app_info.box3_X2, app_info.box3_Y2,m_paint);
            //=====================================================Logger INFO==============================================================================================
            m_canvas.drawText(App.getContext().getString(R.string.LoggerModel), app_info.first_column, app_info.line_counter, m_paint_dark);
            m_canvas.drawText(QS.GetType(Integer.parseInt(baseCMD.model)), app_info.second_column, app_info.line_counter, m_paint);
            app_info.line_counter += app_info.line_inc;
            m_canvas.drawText(App.getContext().getString(R.string.LoggerState), app_info.first_column, app_info.line_counter, m_paint_dark);
            m_canvas.drawText(QS.GetState(Integer.parseInt(Q_data.get(5))), app_info.second_column, app_info.line_counter, m_paint);
            app_info.line_counter += app_info.line_inc;
            m_canvas.drawText(App.getContext().getString(R.string.Estimated_Battery), app_info.first_column, app_info.line_counter, m_paint_dark);
            m_canvas.drawText(baseCMD.battery+"%", app_info.second_column, app_info.line_counter, m_paint);
            app_info.line_counter += app_info.line_inc;
            m_canvas.drawText(App.getContext().getString(R.string.Sample_Period_), app_info.first_column, app_info.line_counter, m_paint_dark);
            m_canvas.drawText(U_data.get(23)+" (hh:mm:ss)", app_info.second_column, app_info.line_counter, m_paint);
            app_info.line_counter += app_info.line_inc;
            m_canvas.drawText(App.getContext().getString(R.string.Start_Delay_), app_info.first_column, app_info.line_counter, m_paint_dark);
            m_canvas.drawText(U_data.get(24)+" (hh:mm:ss)", app_info.second_column, app_info.line_counter, m_paint);
            app_info.line_counter += app_info.line_inc;
            m_canvas.drawText(App.getContext().getString(R.string.FirstSample), app_info.first_column, app_info.line_counter, m_paint_dark);
            m_canvas.drawText(QS.UTCtoLocal(mt2Mem_values.Data.get(0).valTime.getTime()), app_info.second_column, app_info.line_counter, m_paint);
            app_info.line_counter += app_info.line_inc;
            m_canvas.drawText(App.getContext().getString(R.string.LastSample), app_info.first_column, app_info.line_counter, m_paint_dark);
            m_canvas.drawText(QS.UTCtoLocal(mt2Mem_values.Data.get(mt2Mem_values.Data.size() - 1).valTime.getTime()), app_info.second_column, app_info.line_counter, m_paint);
            app_info.line_counter += app_info.line_inc;
            m_canvas.drawText(App.getContext().getString(R.string.LoggedSample), app_info.first_column, app_info.line_counter, m_paint_dark);
            m_canvas.drawText(R_data.get(7), app_info.second_column, app_info.line_counter, m_paint);
            app_info.line_counter += app_info.section_inc;

            //check if limits are breached and print that in the PDF
            if(((my_unit) ? baseCMD.ch1Hi / 10.0 : QS.returnFD(baseCMD.ch1Hi / 10.0)) < ((my_unit) ? mt2Mem_values.ch0Stats.Max.Value / 10.0 : QS.returnFD(mt2Mem_values.ch0Stats.Max.Value / 10.0))){
                temp_upper_breached  = App.getContext().getString(R.string.breached);
            }else temp_upper_breached = "";

            if(((my_unit) ? baseCMD.ch1Lo / 10.0 :QS.returnFD(baseCMD.ch1Lo / 10.0)) > ((my_unit) ? mt2Mem_values.ch0Stats.Min.Value / 10.0 : QS.returnFD(mt2Mem_values.ch0Stats.Min.Value / 10.0))){
                temp_lower_breached  = App.getContext().getString(R.string.breached);
            }else temp_lower_breached = "";

            if(baseCMD.ch2Enable){
                if(baseCMD.ch2Hi / 10.0 < mt2Mem_values.ch1Stats.Max.Value / 10.0){
                    RH_upper_breached = App.getContext().getString(R.string.breached);
                }else RH_upper_breached ="";

                if(baseCMD.ch2Lo / 10.0 > mt2Mem_values.ch1Stats.Min.Value / 10.0){
                    RH_lower_breached =  App.getContext().getString(R.string.breached);
                }else RH_lower_breached = "";
            }

            //===================================================================================================================================================================
            m_canvas.drawLine(app_info.line2_startX, app_info.line2_startY, app_info.line2_endX, app_info.line2_endY, m_paint);
            //==============================================================Value INFO==========================================================================================
            m_canvas.drawText(App.getContext().getString(R.string.Temp_channel_1), app_info.second_column-25, app_info.line_counter, m_paint_dark);
            //m_canvas.drawText(App.getContext().getString(R.string.Temp_channel_1), app_info.second_column-50, app_info.line_counter, m_paint_dark);
            if(baseCMD.ch2Enable) m_canvas.drawText(App.getContext().getString(R.string.RH_channel_1), app_info.third_column-25, app_info.line_counter, m_paint_dark);
            app_info.line_counter += app_info.line_inc;

            m_canvas.drawText(App.getContext().getString(R.string.UpperLimit), app_info.first_column, app_info.line_counter, m_paint_dark);
            m_canvas.drawText(String.format("%.1f",((my_unit) ? baseCMD.ch1Hi / 10.0 : QS.returnFD(baseCMD.ch1Hi / 10.0))) + Temp_unit + temp_upper_breached, app_info.second_column, app_info.line_counter, m_paint);
            if(baseCMD.ch2Enable) m_canvas.drawText(baseCMD.ch2Hi / 10.0 + App.getContext().getString(R.string.RH) + RH_upper_breached, app_info.third_column, app_info.line_counter, m_paint);

            app_info.line_counter += app_info.line_inc;
            m_canvas.drawText(App.getContext().getString(R.string.LowerLimit), app_info.first_column, app_info.line_counter, m_paint_dark);
            m_canvas.drawText(String.format("%.1f",((my_unit) ? baseCMD.ch1Lo / 10.0 :QS.returnFD(baseCMD.ch1Lo / 10.0)))+ Temp_unit + temp_lower_breached, app_info.second_column, app_info.line_counter, m_paint);
            if(baseCMD.ch2Enable) m_canvas.drawText(baseCMD.ch2Lo / 10.0 + App.getContext().getString(R.string.RH) + RH_lower_breached, app_info.third_column, app_info.line_counter, m_paint);

            app_info.line_counter += app_info.line_inc;
            m_canvas.drawText(App.getContext().getString(R.string.Mean), app_info.first_column, app_info.line_counter, m_paint_dark);
            m_canvas.drawText(String.format("%.1f",((my_unit) ?  mt2Mem_values.ch0Stats.Mean: QS.returnFD( mt2Mem_values.ch0Stats.Mean))) + Temp_unit, app_info.second_column, app_info.line_counter, m_paint);
            if(baseCMD.ch2Enable) m_canvas.drawText(String.format("%.1f", mt2Mem_values.ch1Stats.Mean) + App.getContext().getString(R.string.RH), app_info.third_column, app_info.line_counter, m_paint);

            app_info.line_counter += app_info.line_inc;
            m_canvas.drawText(App.getContext().getString(R.string.MKT), app_info.first_column, app_info.line_counter, m_paint_dark);
            m_canvas.drawText(String.format("%.1f",((my_unit) ?  mt2Mem_values.ch0Stats.MKTValue: QS.returnFD( mt2Mem_values.ch0Stats.MKTValue))) + Temp_unit, app_info.second_column, app_info.line_counter, m_paint);

            app_info.line_counter += app_info.line_inc;
            m_canvas.drawText(App.getContext().getString(R.string.MAX), app_info.first_column, app_info.line_counter, m_paint_dark);
            m_canvas.drawText(String.format("%.1f",((my_unit) ? mt2Mem_values.ch0Stats.Max.Value / 10.0 : QS.returnFD(mt2Mem_values.ch0Stats.Max.Value / 10.0)))+ Temp_unit, app_info.second_column, app_info.line_counter, m_paint);
            if(baseCMD.ch2Enable) m_canvas.drawText(mt2Mem_values.ch1Stats.Max.Value / 10.0 + App.getContext().getString(R.string.RH), app_info.third_column, app_info.line_counter, m_paint);


            app_info.line_counter += app_info.line_inc;
            m_canvas.drawText(App.getContext().getString(R.string.Min), app_info.first_column, app_info.line_counter, m_paint_dark);
            m_canvas.drawText(String.format("%.1f",((my_unit) ? mt2Mem_values.ch0Stats.Min.Value / 10.0 : QS.returnFD(mt2Mem_values.ch0Stats.Min.Value / 10.0)))+ Temp_unit, app_info.second_column, app_info.line_counter, m_paint);
            if(baseCMD.ch2Enable) m_canvas.drawText(mt2Mem_values.ch1Stats.Min.Value / 10.0 + App.getContext().getString(R.string.RH), app_info.third_column, app_info.line_counter, m_paint);
            //===================================================================================================================================================================

            if(mt2Mem_values.ch0Stats.TotalLimit == 0 && mt2Mem_values.ch1Stats.TotalLimit == 0){
                fade_out = RH_paint;
            }else{
                fade_out = m_paint_dark;
            }
            //==============================================================Limit INFO==========================================================================================
            app_info.line_counter += app_info.section_inc;
            m_canvas.drawText(App.getContext().getString(R.string.TotalSamplewithinLimits), app_info.first_column, app_info.line_counter, m_paint_dark);
            m_canvas.drawText(mt2Mem_values.ch0Stats.TotalLimitWithin+"", app_info.second_column, app_info.line_counter, m_paint);
            if(baseCMD.ch2Enable) m_canvas.drawText(mt2Mem_values.ch1Stats.TotalLimitWithin+"", app_info.third_column, app_info.line_counter, m_paint);

            app_info.line_counter += app_info.line_inc;
            m_canvas.drawText(App.getContext().getString(R.string.TotalTimewithinLimits), app_info.first_column, app_info.line_counter, m_paint_dark);
            m_canvas.drawText(mt2Mem_values.ch0Stats.TotalTimeWithin, app_info.second_column, app_info.line_counter, m_paint);
            if(baseCMD.ch2Enable) m_canvas.drawText(mt2Mem_values.ch1Stats.TotalTimeWithin, app_info.third_column, app_info.line_counter, m_paint);


            app_info.line_counter += app_info.section_inc;
            m_canvas.drawText(App.getContext().getString(R.string.TotalSamplesoutofLimits), app_info.first_column, app_info.line_counter, fade_out);
            m_canvas.drawText(mt2Mem_values.ch0Stats.TotalLimit+"", app_info.second_column, app_info.line_counter, m_paint);
            if(baseCMD.ch2Enable) m_canvas.drawText(mt2Mem_values.ch1Stats.TotalLimit+"", app_info.third_column, app_info.line_counter, m_paint);

            app_info.line_counter += app_info.line_inc;
            m_canvas.drawText(App.getContext().getString(R.string.TotalTimeoutofLimits), app_info.first_column, app_info.line_counter, fade_out);
            m_canvas.drawText(mt2Mem_values.ch0Stats.TotalTime, app_info.second_column, app_info.line_counter, m_paint);
            if(baseCMD.ch2Enable) m_canvas.drawText(mt2Mem_values.ch1Stats.TotalTime, app_info.third_column, app_info.line_counter, m_paint);

            app_info.line_counter += app_info.section_inc;
            m_canvas.drawText(App.getContext().getString(R.string.SamplesaboveUpperLimits), app_info.first_column, app_info.line_counter, fade_out);
            m_canvas.drawText(mt2Mem_values.ch0Stats.AboveLimit+"", app_info.second_column, app_info.line_counter, m_paint);
            if(baseCMD.ch2Enable) m_canvas.drawText(mt2Mem_values.ch1Stats.AboveLimit+"", app_info.third_column, app_info.line_counter, m_paint);

            app_info.line_counter += app_info.line_inc;
            m_canvas.drawText(App.getContext().getString(R.string.TimeaboveUpperLimit), app_info.first_column, app_info.line_counter, fade_out);
            m_canvas.drawText(mt2Mem_values.ch0Stats.AboveTime, app_info.second_column, app_info.line_counter, m_paint);
            if(baseCMD.ch2Enable) m_canvas.drawText(mt2Mem_values.ch1Stats.AboveTime, app_info.third_column, app_info.line_counter, m_paint);

            app_info.line_counter += app_info.section_inc;
            m_canvas.drawText(App.getContext().getString(R.string.SamplesbelowLowerLimits), app_info.first_column, app_info.line_counter, fade_out);
            m_canvas.drawText(mt2Mem_values.ch0Stats.BelowLimit+"", app_info.second_column, app_info.line_counter, m_paint);
            if(baseCMD.ch2Enable) m_canvas.drawText(mt2Mem_values.ch1Stats.BelowLimit+"", app_info.third_column, app_info.line_counter, m_paint);

            app_info.line_counter += app_info.line_inc;
            m_canvas.drawText(App.getContext().getString(R.string.TimebelowLowerLimit), app_info.first_column, app_info.line_counter, fade_out);
            m_canvas.drawText(mt2Mem_values.ch0Stats.BelowTime, app_info.second_column, app_info.line_counter, m_paint);
            if(baseCMD.ch2Enable) m_canvas.drawText(mt2Mem_values.ch1Stats.BelowTime, app_info.third_column, app_info.line_counter, m_paint);

            app_info.line_counter += app_info.section_inc;
            m_canvas.drawText(App.getContext().getString(R.string.UserComments_), app_info.first_column, app_info.line_counter, m_paint_dark);
            app_info.line_counter += app_info.line_inc;
            if(baseCMD.ch2Enable) m_canvas.drawText( U_data.get(27), app_info.first_column, app_info.line_counter, m_paint);

            m_canvas.drawText(App.getContext().getString(R.string.Comment), app_info.commentX, app_info.commentY, m_paint_dark);
            m_canvas.drawText(App.getContext().getString(R.string.Signature), app_info.sigX, app_info.sigY, m_paint_dark);
            //========================================================================================
            m_canvas.drawLine(app_info.line3_startX, app_info.line3_startY, app_info.line3_endX, app_info.line3_endY, m_paint);

            //=============================plot graph =========================================
            //calculating the highest and lowest values from the temperature and humidity values
            app_info.ch1_upper_L = ((my_unit) ? baseCMD.ch1Hi / 10.0 : QS.returnFD(baseCMD.ch1Hi / 10.0));
            app_info.ch1_lower_L = ((my_unit) ? baseCMD.ch1Lo / 10.0 : QS.returnFD(baseCMD.ch1Lo / 10.0));
            app_info.ch1_max = ((my_unit) ? mt2Mem_values.ch0Stats.Max.Value / 10.0 : QS.returnFD(mt2Mem_values.ch0Stats.Max.Value / 10.0));
            app_info.ch1_min = ((my_unit) ? mt2Mem_values.ch0Stats.Min.Value / 10.0 : QS.returnFD(mt2Mem_values.ch0Stats.Min.Value / 10.0));

            if(app_info.ch1_upper_L > app_info.ch1_max){
                app_info.ch1_highest = app_info.ch1_upper_L;
            }else{
                app_info.ch1_highest = app_info.ch1_max;
            }

            if(app_info.ch1_lower_L < app_info.ch1_min){
                app_info.ch1_lowest = app_info.ch1_lower_L;
            }else{
                app_info.ch1_lowest = app_info.ch1_min;
            }

            if(baseCMD.ch2Enable) {
                app_info.ch2_upper_L = ((my_unit) ? baseCMD.ch2Hi / 10.0 : QS.returnFD(baseCMD.ch2Hi / 10.0));
                app_info.ch2_lower_L = ((my_unit) ? baseCMD.ch2Lo / 10.0 : QS.returnFD(baseCMD.ch2Lo / 10.0));
                app_info.ch2_max = ((my_unit) ? mt2Mem_values.ch1Stats.Max.Value / 10.0 : QS.returnFD(mt2Mem_values.ch1Stats.Max.Value / 10.0));
                app_info.ch2_min = ((my_unit) ? mt2Mem_values.ch1Stats.Min.Value / 10.0 : QS.returnFD(mt2Mem_values.ch1Stats.Min.Value / 10.0));

                app_info.ch2_upper_L = baseCMD.ch2Hi / 10.0;
                app_info.ch2_lower_L = baseCMD.ch2Lo / 10.0;
                app_info.ch2_max = mt2Mem_values.ch1Stats.Max.Value / 10.0;
                app_info.ch2_min = mt2Mem_values.ch1Stats.Min.Value / 10.0;

                if (app_info.ch2_upper_L > app_info.ch2_max) {
                    app_info.ch2_highest = app_info.ch2_upper_L;
                } else {
                    app_info.ch2_highest = app_info.ch2_max;
                }

                if (app_info.ch2_lower_L < app_info.ch2_min) {
                    app_info.ch2_lowest = app_info.ch2_lower_L;
                } else {
                    app_info.ch2_lowest = app_info.ch2_min;
                }


                if(app_info.ch1_highest > app_info.ch2_highest){
                    app_info.ch_highest = app_info.ch1_highest;
                }else{
                    app_info.ch_highest = app_info.ch2_highest;
                }

                if(app_info.ch1_lowest < app_info.ch2_lowest){
                    app_info.ch_lowest = app_info.ch1_lowest;
                }else{
                    app_info.ch_lowest = app_info.ch2_lowest;
                }
            }else{
                app_info.ch_highest = app_info.ch1_highest;
                app_info.ch_lowest = app_info.ch1_lowest;
            }
            //start drawing the graph
            m_canvas.drawRect(app_info.graph_topX, app_info.graph_topY, app_info.graph_topX+app_info.graph_W, app_info.graph_topY+app_info.graph_H,app_info.graph_paint);
            m_canvas.drawLine(app_info.G_axis_startX,app_info.G_axis_startY,app_info.G_axis_meetX,app_info.G_axis_meetY,m_paint);//y axis
            m_canvas.drawLine(app_info.G_axis_meetX,app_info.G_axis_meetY,app_info.G_axis_endX,app_info.G_axis_endY,m_paint);//x axis
            app_info.graph_y_scale = (float)((app_info.graph_H-20)/(app_info.ch_highest-app_info.ch_lowest));
            app_info.graph_x_scale = (float)app_info.graph_W/ mt2Mem_values.Data.size();

            if(baseCMD.ch2Enable){//plotting humidity chaneel 2 limit line and labeling them and labeling the axis
                app_info.rh_next_y = (float) (app_info.graph_H - ((ch2.get(0) - (app_info.ch_lowest)) * app_info.graph_y_scale)) + app_info.graph_topY;//initial y location on the graph
                app_info.ch2_upper_Y = (app_info.graph_H-((app_info.ch2_upper_L-app_info.ch_lowest)*app_info.graph_y_scale))+app_info.graph_topY;//ch2 upper limit
                app_info.ch2_lower_Y = (app_info.graph_H-((app_info.ch2_lower_L-app_info.ch_lowest)*app_info.graph_y_scale))+app_info.graph_topY;//ch2 lower limit
                m_canvas.drawLine(app_info.graph_l_lineX_start, (float)app_info.ch2_upper_Y,app_info.graph_l_lineX_end,(float)app_info.ch2_upper_Y,app_info.ch2_u_L);
                m_canvas.drawText(App.getContext().getString(R.string.RH) + App.getContext().getString(R.string.G_Upper_LImit),app_info.third_column,(float)app_info.ch2_upper_Y-app_info.graph_limit_label,app_info.ch2_u_L);
                m_canvas.drawText(String.valueOf(baseCMD.ch2Hi / 10.0),app_info.first_column,(float)app_info.ch2_upper_Y,m_paint);//printing the values of limits at the y axis
                m_canvas.drawLine(app_info.graph_l_lineX_start, (float)app_info.ch2_lower_Y,app_info.graph_l_lineX_end,(float)app_info.ch2_lower_Y,app_info.ch2_l_L);
                m_canvas.drawText(App.getContext().getString(R.string.RH) + App.getContext().getString(R.string.G_Lower_LImit),app_info.third_column,(float)app_info.ch2_lower_Y+app_info.graph_limit_label+5,app_info.ch2_l_L);
                m_canvas.drawText(String.valueOf(baseCMD.ch2Lo / 10.0),app_info.first_column,(float)app_info.ch2_lower_Y,m_paint);//printing the values of limits at the y axis
                m_canvas.drawText(String.format("%.1f", mt2Mem_values.ch1Stats.Mean),app_info.first_column,(float)(app_info.graph_H-(((mt2Mem_values.ch1Stats.Mean-app_info.ch_lowest)*app_info.graph_y_scale)))+app_info.graph_topY,m_paint);//mean value of the humidity line
                //max min dash line for channel 2
                float ch2_max = (float)(app_info.graph_H-(((mt2Mem_values.ch1Stats.Max.Value / 10.0)-app_info.ch_lowest)*app_info.graph_y_scale))+app_info.graph_topY;
                m_canvas.drawLine(app_info.graph_l_lineX_start,ch2_max,app_info.graph_l_lineX_end,ch2_max,app_info.ch2_max_min);
                //m_canvas.drawText(String.valueOf(mt2Mem_values.ch1Stats.Max.Value / 10.0),app_info.graph_l_lineX_end,ch2_max,app_info.max_min);
                float ch2_min = (float)(app_info.graph_H-(((mt2Mem_values.ch1Stats.Min.Value / 10.0)-app_info.ch_lowest)*app_info.graph_y_scale))+app_info.graph_topY;
                m_canvas.drawLine(app_info.graph_l_lineX_start,ch2_min,app_info.graph_l_lineX_end,ch2_min,app_info.ch2_max_min);
                //m_canvas.drawText(String.valueOf(mt2Mem_values.ch1Stats.Min.Value / 10.0),app_info.graph_l_lineX_end,ch2_min,app_info.max_min);
                m_canvas.drawText("__ "+App.getContext().getString(R.string.Hum) + " " +App.getContext().getString(R.string.RH),app_info.second_column+120,app_info.graph_topY-5,app_info.rh_line);
            }

            //plotting temperature channel 1 limit line and labeling them and labeling the axis
            app_info.ch1_upper_Y = (app_info.graph_H-((app_info.ch1_upper_L-app_info.ch_lowest)*app_info.graph_y_scale))+app_info.graph_topY;
            app_info.ch1_lower_Y = (app_info.graph_H-((app_info.ch1_lower_L-app_info.ch_lowest)*app_info.graph_y_scale))+app_info.graph_topY;
            app_info.temp_next_y = (float)  (app_info.graph_H-((((my_unit) ? ch1.get(0) : QS.returnFD(ch1.get(0)))-(app_info.ch_lowest))*app_info.graph_y_scale))+app_info.graph_topY;
            m_canvas.drawLine(app_info.graph_l_lineX_start, (float)app_info.ch1_upper_Y,app_info.graph_l_lineX_end,(float)app_info.ch1_upper_Y,app_info.ch1_u_L);
            m_canvas.drawText(Temp_unit + App.getContext().getString(R.string.G_Upper_LImit),app_info.third_column,(float)app_info.ch1_upper_Y-app_info.graph_limit_label,app_info.ch1_u_L);
            m_canvas.drawText(String.format("%.1f",((my_unit) ? baseCMD.ch1Hi / 10.0 : QS.returnFD(baseCMD.ch1Hi / 10.0))),app_info.first_column,(float)app_info.ch1_upper_Y,m_paint);
            m_canvas.drawLine(app_info.graph_l_lineX_start, (float)app_info.ch1_lower_Y,app_info.graph_l_lineX_end,(float)app_info.ch1_lower_Y,app_info.ch1_l_L);
            m_canvas.drawText(Temp_unit + App.getContext().getString(R.string.G_Lower_LImit),app_info.third_column,(float)app_info.ch1_lower_Y+app_info.graph_limit_label+5,app_info.ch1_l_L);
            m_canvas.drawText(String.format("%.1f",((my_unit) ? baseCMD.ch1Lo / 10.0 : QS.returnFD(baseCMD.ch1Lo / 10.0))),app_info.first_column,(float)app_info.ch1_lower_Y,m_paint);

            m_canvas.drawText(String.format("%.1f",((my_unit) ?  mt2Mem_values.ch0Stats.Mean: QS.returnFD( mt2Mem_values.ch0Stats.Mean))),app_info.first_column,(float)(app_info.graph_H-((((my_unit) ?  mt2Mem_values.ch0Stats.Mean: QS.returnFD( mt2Mem_values.ch0Stats.Mean))-app_info.ch_lowest)*app_info.graph_y_scale))+app_info.graph_topY,m_paint);
            float ch1_max = (float)(app_info.graph_H-((((my_unit) ? mt2Mem_values.ch0Stats.Max.Value / 10.0 : QS.returnFD(mt2Mem_values.ch0Stats.Max.Value / 10.0))-app_info.ch_lowest)*app_info.graph_y_scale))+app_info.graph_topY;
            m_canvas.drawLine(app_info.graph_l_lineX_start,ch1_max,app_info.graph_l_lineX_end,ch1_max,app_info.ch1_max_min);
           // m_canvas.drawText(String.format("%.1f",((my_unit) ? mt2Mem_values.ch0Stats.Max.Value / 10.0 : QS.returnFD(mt2Mem_values.ch0Stats.Max.Value / 10.0))),app_info.graph_l_lineX_end,ch1_max,app_info.max_min);
            float ch1_min = (float)(app_info.graph_H-((((my_unit) ? mt2Mem_values.ch0Stats.Min.Value / 10.0 : QS.returnFD(mt2Mem_values.ch0Stats.Min.Value / 10.0))-app_info.ch_lowest)*app_info.graph_y_scale))+app_info.graph_topY;
            m_canvas.drawLine(app_info.graph_l_lineX_start,ch1_min,app_info.graph_l_lineX_end,ch1_min,app_info.ch1_max_min);
           // m_canvas.drawText(String.format("%.1f",((my_unit) ? mt2Mem_values.ch0Stats.Min.Value / 10.0 : QS.returnFD(mt2Mem_values.ch0Stats.Min.Value / 10.0))),app_info.graph_l_lineX_end,ch1_min,app_info.max_min);
            m_canvas.drawText("__ "+App.getContext().getString(R.string.Temp)+ Temp_unit,app_info.second_column,app_info.graph_topY-5,app_info.temp_line);
            //labeling the mean value of the graph lines
            m_canvas.drawText(String.valueOf(baseCMD.ch2Lo / 10.0),app_info.first_column,(float)app_info.ch2_lower_Y,m_paint);
            int k = 0;


            app_info.x_point_loc = app_info.graph_topX;
            //loop which actually draws the temperature and humidity lines
            while (k < mt2Mem_values.Data.size()){
                //m_canvas.drawCircle(app_info.x_point_loc, (float)(app_info.graph_H-((((my_unit) ? ch1.get(k) : QS.returnFD(ch1.get(k)))-(app_info.ch1_lowest))*app_info.graph_y_scale))+app_info.graph_topY,5,green_tick);
                m_canvas.drawLine(app_info.x_point_loc, app_info.temp_next_y, app_info.x_point_loc+app_info.graph_x_scale,(float)(app_info.graph_H-((((my_unit) ? ch1.get(k) : QS.returnFD(ch1.get(k)))-(app_info.ch_lowest))*app_info.graph_y_scale))+app_info.graph_topY, app_info.temp_line );
                app_info.temp_next_y = (float)  (app_info.graph_H-((((my_unit) ? ch1.get(k) : QS.returnFD(ch1.get(k)))-(app_info.ch_lowest))*app_info.graph_y_scale))+app_info.graph_topY;
                if(baseCMD.ch2Enable) {
                   // m_canvas.drawCircle(app_info.x_point_loc, (float) (app_info.graph_H - ((ch2.get(k) - (app_info.ch_lowest)) * app_info.graph_y_scale)) + app_info.graph_topY, 5, green_tick);
                    m_canvas.drawLine(app_info.x_point_loc, app_info.rh_next_y,app_info.x_point_loc+app_info.graph_x_scale,(float) (app_info.graph_H - ((ch2.get(k) - (app_info.ch_lowest)) * app_info.graph_y_scale)) + app_info.graph_topY,app_info.rh_line );
                    app_info.rh_next_y = (float) (app_info.graph_H - ((ch2.get(k) - (app_info.ch_lowest)) * app_info.graph_y_scale)) + app_info.graph_topY;
                }
                app_info.x_point_loc+=app_info.graph_x_scale;
                k++;
            }

            //drawing the date and time on the x axis
            int gap =  mt2Mem_values.Data.size()/5;
            int i = gap;
            if(gap <= 0)gap = 1;
            while(i <  mt2Mem_values.Data.size()){
                app_info.graph_date_x  = (app_info.graph_x_scale*i)+app_info.graph_topX;
                m_canvas.drawCircle((app_info.graph_x_scale*i)+app_info.graph_topX, app_info.G_axis_meetY,2,m_paint);
                m_canvas.drawText(Convert_UNIX_To_Date(time.get(i)),app_info.graph_date_x-40,app_info.G_axis_meetY+15,m_paint);
                m_canvas.drawText(Convert_UNIX_To_Time(time.get(i)),app_info.graph_date_x-30,app_info.G_axis_meetY+28,m_paint);
                i+=gap;
            }
//


//            m_canvas.drawPoint(app_info.graph_topX+20, (float)(app_info.graph_H-((40+(Math.abs(app_info.ch1_lowest)))*app_info.graph_y_scale))+app_info.graph_topY,green_tick);
//            m_canvas.drawPoint(app_info.graph_topX+40, (float)(app_info.graph_H-((50+(Math.abs(app_info.ch1_lowest)))*app_info.graph_y_scale))+app_info.graph_topY,green_tick);

//            Log.i("TAG", "))))))))))))))))))))))))" + app_info.graph_y_scale+ " " + (app_info.ch1_upper_L+(Math.abs(app_info.ch1_lowest))) + " " + ((app_info.ch1_upper_L+(Math.abs(app_info.ch1_lowest)))*app_info.graph_y_scale));
//            Log.i("TAG", "))))))))))))))))))))))))" +(app_info.graph_H-((app_info.ch1_upper_L+(Math.abs(app_info.ch1_lowest)))*app_info.graph_y_scale))+ " " + app_info.ch1_upper_Y + " " + app_info.ch1_lower_Y+ " " + app_info.graph_x_scale + " " + mt2Mem_values.Data.size());
            //========================================================================================
            //============================================//
            if(simplePDF == 1) {//printing the values if user selects full report -- starts from the second psge
                m_current_line = 1000;
                Inc_Line_and_Check_if_need_new_page();
                while (inc_val < mt2Mem_values.Data.size()) {

                    String m_date = Convert_UNIX_To_Date(time.get(inc_val));
                    String m_time = Convert_UNIX_To_Time(time.get(inc_val));

                    if (m_date.equals(m_ref_date)) {
                        m_canvas.drawText(m_time, K_column_time, m_current_line, m_paint);
                        while (current_col < num_col) {
                            if (inc_val >= mt2Mem_values.Data.size())
                                break;
                            if(((my_unit) ? ch1.get(inc_val) : QS.returnFD(ch1.get(inc_val))) > ((my_unit) ? baseCMD.ch1Hi / 10.0 : QS.returnFD(baseCMD.ch1Hi / 10.0))){
                                limit_temp.setColor(Color.rgb(255,92,51));
                                limit_temp.setFakeBoldText(true);
                                m_canvas.drawText(String.format("%.1f",(my_unit) ? ch1.get(inc_val) : QS.returnFD(ch1.get(inc_val)))+Temp_unit, m_current_column, m_current_line, limit_temp);

                            }else if(((my_unit) ? ch1.get(inc_val) : QS.returnFD(ch1.get(inc_val))) < ((my_unit) ? baseCMD.ch1Lo / 10.0 : QS.returnFD(baseCMD.ch1Lo / 10.0))){
                                limit_temp.setColor(Color.rgb(0,82,204));
                                limit_temp.setFakeBoldText(true);
                                m_canvas.drawText(String.format("%.1f",(my_unit) ? ch1.get(inc_val) : QS.returnFD(ch1.get(inc_val)))+Temp_unit, m_current_column, m_current_line, limit_temp);

                            }else{
                                m_canvas.drawText(String.format("%.1f",(my_unit) ? ch1.get(inc_val) : QS.returnFD(ch1.get(inc_val)))+Temp_unit, m_current_column, m_current_line, m_paint);

                            }


                            if (baseCMD.ch2Enable) {
                                if(ch2.get(inc_val) > baseCMD.ch2Hi / 10.0){
                                    limit_RH.setColor(Color.rgb(255,92,51));
                                    limit_RH.setFakeBoldText(true);
                                    m_canvas.drawText(String.valueOf(ch2.get(inc_val))+" %", m_current_column, m_current_line + K_cr_line, limit_RH);
                                }else  if(ch2.get(inc_val) < baseCMD.ch2Lo / 10.0){
                                    limit_RH.setColor(Color.rgb(0,102,255));
                                    limit_RH.setFakeBoldText(true);
                                    m_canvas.drawText(String.valueOf(ch2.get(inc_val))+" %", m_current_column, m_current_line + K_cr_line, limit_RH);
                                }else{
                                    m_canvas.drawText(String.valueOf(ch2.get(inc_val))+" %", m_current_column, m_current_line + K_cr_line, RH_paint);
                                }


                            }
                            current_col++;
                            m_current_column += K_column_space;
                            inc_val++;
                        }
                        current_col = 0;
                        m_current_column = K_column_temp;
                        if (baseCMD.ch2Enable)
                            m_current_line += K_cr_line;

                        Inc_Line_and_Check_if_need_new_page();
                    } else {
                        Inc_Line_and_Check_if_need_new_page();
                        m_canvas.drawText(m_date, K_column_date, m_current_line, m_paint);
                        Inc_Line_and_Check_if_need_new_page();
                        m_current_column = K_column_temp;
                        m_ref_date = m_date;
                    }

                    //============================================//


                    //============================================//
                }
            }else if(simplePDF == 2){
//                private static  final   int                     K_v2_column_index       =  40;
//                private static  final   int                     K_v2_column_date        =  70;
//                private static  final   int                     K_v2_column_time        =  80;
//                private static  final   int                     K_v2_column             =  60;

                m_current_line = 1000;
                Inc_Line_and_Check_if_need_new_page();
                int index = 0;
                inc_val = 0;
                String m_date = Convert_UNIX_To_Date(time.get(inc_val));
                String m_time = Convert_UNIX_To_Time(time.get(inc_val));
                while (inc_val < mt2Mem_values.Data.size()) {
                    m_date = Convert_UNIX_To_Date(time.get(inc_val));
                    m_time = Convert_UNIX_To_Time(time.get(inc_val));
                    m_paint.setColor(Color.BLACK);

                    m_canvas.drawText(String.valueOf(inc_val+1),m_current_column_v2,m_current_line,m_paint);
                    m_canvas.drawText(m_date,m_current_column_v2+=K_v2_column_index,m_current_line,m_paint);
                    m_canvas.drawText(m_time,m_current_column_v2+=K_v2_column_date,m_current_line,m_paint);

                    if(((my_unit) ? ch1.get(inc_val) : QS.returnFD(ch1.get(inc_val))) > ((my_unit) ? baseCMD.ch1Hi / 10.0 : QS.returnFD(baseCMD.ch1Hi / 10.0))){
                        limit_temp.setColor(Color.rgb(255,92,51));
                        limit_temp.setFakeBoldText(true);
                        m_canvas.drawText(String.format("%.1f",(my_unit) ? ch1.get(inc_val) : QS.returnFD(ch1.get(inc_val)))+Temp_unit,m_current_column_v2+=K_v2_column_time,m_current_line,limit_temp);
                    }else if(((my_unit) ? ch1.get(inc_val) : QS.returnFD(ch1.get(inc_val))) < ((my_unit) ? baseCMD.ch1Lo / 10.0 : QS.returnFD(baseCMD.ch1Lo / 10.0))){
                        limit_temp.setColor(Color.rgb(0,82,204));
                        limit_temp.setFakeBoldText(true);
                        m_canvas.drawText(String.format("%.1f",(my_unit) ? ch1.get(inc_val) : QS.returnFD(ch1.get(inc_val)))+Temp_unit,m_current_column_v2+=K_v2_column_time,m_current_line,limit_temp);
                    }else{
                        m_canvas.drawText(String.format("%.1f",(my_unit) ? ch1.get(inc_val) : QS.returnFD(ch1.get(inc_val)))+Temp_unit,m_current_column_v2+=K_v2_column_time,m_current_line,m_paint);

                    }


                    if (baseCMD.ch2Enable) {
                        if(ch2.get(inc_val) > baseCMD.ch2Hi / 10.0){
                            limit_RH.setColor(Color.rgb(255,92,51));
                            limit_RH.setFakeBoldText(true);
                            m_canvas.drawText(String.valueOf(ch2.get(inc_val))+" %",m_current_column_v2+=K_v2_column, m_current_line, limit_RH);
                        }else  if(ch2.get(inc_val) < baseCMD.ch2Lo / 10.0){
                            limit_RH.setColor(Color.rgb(0,102,255));
                            limit_RH.setFakeBoldText(true);
                            m_canvas.drawText(String.valueOf(ch2.get(inc_val))+" %",m_current_column_v2+=K_v2_column, m_current_line, limit_RH);
                        }else{
                            m_canvas.drawText(String.valueOf(ch2.get(inc_val))+" %",m_current_column_v2+=K_v2_column, m_current_line, RH_paint);
                        }
                    }

                    m_canvas.drawText("|",K_v2_column_middle,m_current_line,m_paint);
                    inc_val++;

                    m_date = Convert_UNIX_To_Date(time.get(inc_val));
                    m_time = Convert_UNIX_To_Time(time.get(inc_val));
                    m_current_column_v2 = K_v2_column_two;
                    m_canvas.drawText(String.valueOf(inc_val+1),m_current_column_v2,m_current_line,m_paint);
                    m_canvas.drawText(m_date,m_current_column_v2+=K_v2_column_index,m_current_line,m_paint);
                    m_canvas.drawText(m_time,m_current_column_v2+=K_v2_column_date,m_current_line,m_paint);

                    if(((my_unit) ? ch1.get(inc_val) : QS.returnFD(ch1.get(inc_val))) > ((my_unit) ? baseCMD.ch1Hi / 10.0 : QS.returnFD(baseCMD.ch1Hi / 10.0))){
                        limit_temp.setColor(Color.rgb(255,92,51));
                        limit_temp.setFakeBoldText(true);
                        m_canvas.drawText(String.format("%.1f",(my_unit) ? ch1.get(inc_val) : QS.returnFD(ch1.get(inc_val)))+Temp_unit,m_current_column_v2+=K_v2_column_time,m_current_line,limit_temp);
                    }else if(((my_unit) ? ch1.get(inc_val) : QS.returnFD(ch1.get(inc_val))) < ((my_unit) ? baseCMD.ch1Lo / 10.0 : QS.returnFD(baseCMD.ch1Lo / 10.0))){
                        limit_temp.setColor(Color.rgb(0,82,204));
                        limit_temp.setFakeBoldText(true);
                        m_canvas.drawText(String.format("%.1f",(my_unit) ? ch1.get(inc_val) : QS.returnFD(ch1.get(inc_val)))+Temp_unit,m_current_column_v2+=K_v2_column_time,m_current_line,limit_temp);
                    }else{
                        m_canvas.drawText(String.format("%.1f",(my_unit) ? ch1.get(inc_val) : QS.returnFD(ch1.get(inc_val)))+Temp_unit,m_current_column_v2+=K_v2_column_time,m_current_line,m_paint);

                    }


                    if (baseCMD.ch2Enable) {
                        if(ch2.get(inc_val) > baseCMD.ch2Hi / 10.0){
                            limit_RH.setColor(Color.rgb(255,92,51));
                            limit_RH.setFakeBoldText(true);
                            m_canvas.drawText(String.valueOf(ch2.get(inc_val))+" %",m_current_column_v2+=K_v2_column, m_current_line, limit_RH);
                        }else  if(ch2.get(inc_val) < baseCMD.ch2Lo / 10.0){
                            limit_RH.setColor(Color.rgb(0,102,255));
                            limit_RH.setFakeBoldText(true);
                            m_canvas.drawText(String.valueOf(ch2.get(inc_val))+" %",m_current_column_v2+=K_v2_column, m_current_line, limit_RH);
                        }else{
                            m_canvas.drawText(String.valueOf(ch2.get(inc_val))+" %",m_current_column_v2+=K_v2_column, m_current_line, RH_paint);
                        }
                    }

                    inc_val++;
                    Inc_Line_and_Check_if_need_new_page();

                }

            }
            //==============//
            Finish_a_Page();
            //==============//

            //==============//
            Write_and_Close_PDF_Document();
            //==============//

        }
        catch (IOException e)
        {
            Error_Log(new String[]{"PDF","000",e.toString()});
        }
    }



    //==========================================================//
    //</editor-fold>

    //<editor-fold default state="collapsed" desc="Inc_Line_and_Check_if_need_new_page()">
    //==========================================================//
    /**<b>Increment the current PDF/Document/Page line pointer (m_current_line)</b><br>
     * then check if a new page is needed<br>
     * <br><i> - m_page_number is incremented after page creation</i>
     * <br><i> - m_current_line : updated</i>
     * <br><i> - m_current_column : updated</i>
     * <br><i> - m_paint : updated</i>*/
    //==========================================================//
    private void Inc_Line_and_Check_if_need_new_page()
    {

        m_current_line += K_cr_line;
        m_current_column = K_column_temp;
        m_current_column_v2 = K_column_date+20;


        if (m_current_line > K_line_max) {
            Finish_a_Page();

            //============================================//
            Create_a_Page(700, 1000, Color.WHITE);
            //m_canvas.drawLine(50,50,50, 450);

            //creating a new page so the colours are set for thst page
            DashPathEffect dashPathEffect = new DashPathEffect(new float[]{5, 5,}, 0);
            DashPathEffect dashPathEffect2 = new DashPathEffect(new float[]{2, 5,}, 0);
            m_paint.setColor(Color.BLACK);

            m_paint_dark.setColor(Color.BLACK);
            m_paint_dark.setTypeface(Typeface.createFromAsset(App.getContext().getAssets(), "Roboto-Medium.ttf"));
            RH_paint.setColor(Color.GRAY);
            line_paint.setColor(Color.BLUE);
            circle_paint.setColor(Color.GREEN);
            triangle_paint.setColor(Color.RED);
            triangle_paint.setStrokeWidth(12);
            line_paint.setStrokeWidth(3);
            line_paint.setTextSize(blue_text_size);
            green_tick.setColor(Color.GREEN);
            green_tick.setStrokeWidth(13);
            mark.setColor(Color.BLACK);
            mark.setTextSize(52);
            limit_temp.setColor(Color.CYAN);
            app_info.graph_paint.setColor(Color.WHITE);
            app_info.ch1_u_L.setColor(Color.rgb(204,0,0));
            app_info.ch1_u_L.setStrokeWidth(app_info.graph_brush_thickness);
            app_info.ch1_u_L.setPathEffect(dashPathEffect);

            app_info.ch1_l_L.setColor(Color.rgb(0,102,204));
            app_info.ch1_l_L.setStrokeWidth(app_info.graph_brush_thickness);
            app_info.ch1_l_L.setPathEffect(dashPathEffect);

            app_info.ch2_u_L.setColor(Color.rgb(255,51,51));
            app_info.ch2_u_L.setStrokeWidth(app_info.graph_brush_thickness);
            app_info.ch2_u_L.setPathEffect(dashPathEffect);

            app_info.ch2_l_L.setColor(Color.rgb(51,153,255));
            app_info.ch2_l_L.setStrokeWidth(app_info.graph_brush_thickness);
            app_info.ch2_l_L.setPathEffect(dashPathEffect);

            app_info.temp_line.setColor(Color.rgb(27,0,255));
            app_info.temp_line.setStrokeWidth(app_info.graph_brush_thickness);

            app_info.rh_line.setColor(Color.rgb(102,204,0));
            app_info.rh_line.setStrokeWidth(app_info.graph_brush_thickness);

            app_info.ch1_max_min.setColor(Color.rgb(27,0,255));
            app_info.ch1_max_min.setPathEffect(dashPathEffect2);

            app_info.ch2_max_min.setColor(Color.rgb(102,204,0));
            app_info.ch2_max_min.setPathEffect(dashPathEffect2);

            //printing the header and footer on each page. -- some of them are different for the first page
            m_canvas.drawLine(line_startX, line_startY, line_endX, line_endY, line_paint);
            //m_canvas.drawText(App.getContext().getString(R.string.Temprecord), temprecord_startX, temprecord_startY, line_paint);
            m_canvas.drawText(App.getContext().getString(R.string.Page) + " " + page_num, page_startX, page_startY, m_paint);
            m_canvas.drawText("www.temprecord.com", app_info.siteX, app_info.siteY, m_paint);
            m_canvas.drawText(QS.UTCtoLocal(new Date().getTime()), app_info.dateX, app_info.dateY, m_paint);
            m_canvas.drawText(App.getContext().getString(R.string.App_version), app_info.versionX, app_info.versionY, m_paint);
            Drawable d = App.getContext().getResources().getDrawable(R.drawable.temprecord_logo);
            d.setBounds(250, 15, 450, 50);
            d.draw(m_canvas);
            if (page_num == 1) {
                m_canvas.drawText(App.getContext().getString(R.string.Logger_Report), report_startX, report_startY, line_paint);
                m_canvas.drawText("S/N: "+baseCMD.serialno, 550, serial_startY, line_paint);
            }else{
                m_canvas.drawText("S/N: "+baseCMD.serialno, serial_startX, serial_startY, line_paint);
                m_canvas.drawText(App.getContext().getString(R.string.Values_Report), report_startX, report_startY, line_paint);
                if(simplePDF != 2) {
                    if (baseCMD.ch2Enable)
                        m_canvas.drawText(App.getContext().getString(R.string.RH), RH_startX, RH_startY, line_paint);
                    if (storeKeyService.getDefaults("UNITS", App.getContext()) != null && storeKeyService.getDefaults("UNITS", App.getContext()).equals("1")) {
                        m_canvas.drawText(QS.imperial(false), temp_startX, temp_startY, line_paint);
                    } else {
                        m_canvas.drawText(QS.imperial(true), temp_startX, temp_startY, line_paint);
                    }
                }
            }

            page_num++;
            //============================================//
            m_current_line      = K_line_start;
            m_current_column    = K_column_temp;
        }
    }
    //==========================================================//
    //</editor-fold>
    //getting data in to the class before creating the PDF
    public void getData(MT2Values.MT2Mem_values mt2Mem_values, BaseCMD baseCMD, int simplePDF, ArrayList<String> Q_data, ArrayList<String> U_data, ArrayList<String> F_data, ArrayList<String> R_data){
        this.mt2Mem_values = mt2Mem_values;
        this.baseCMD = baseCMD;
        this.simplePDF = simplePDF;
        this.Q_data = Q_data;
        this.U_data = U_data;
        this.F_data = F_data;
        this.R_data = R_data;
    }

    //<editor-fold default state="collapsed" desc="Create_a_Page()">
    //==========================================================//
    /**<br></><b>Create a new page in the existing PDF document</b><br>
     * <br><i> - m_page_number is incremented after page creation</i>
     * <br><i> - m_page : updated</i>
     * <br><i> - m_canvas : updated</i>
     * <br><i> - m_paint : updated</i>
     * <br><i> - m_page_open : Set to True</i>
     * @param width  new page width
     * @param height new page height
     * @param background_color new page color*/
    //==========================================================//
    private void Create_a_Page(int width,int height,int background_color)
    {
        m_page      = m_pdf_document.startPage(new PdfDocument.PageInfo.Builder(width, height, m_page_number++).create());
        m_canvas    = m_page.getCanvas();
        m_paint     = new Paint();

        m_canvas.drawColor(background_color);
        m_page_open   = true;
    }
    //==========================================================//
    //</editor-fold>

    //<editor-fold default state="collapsed" desc="Finish_a_Page()">
    //==========================================================//
    /**<b>Finish the created page of the PDF Document</b><br>
     * <br><i> - m_page_open : Set to False</i>*/
    //==========================================================//
    private void Finish_a_Page()
    //==========================================================//
    {
        if (m_page_open)
        {
            m_pdf_document.finishPage(m_page);
            m_page_open = false;
        }
    }
    //==========================================================//
    //</editor-fold>

    //<editor-fold default state="collapsed" desc="Create_a_PDF_Document()">
    //==========================================================//
    /**<b>Create a new PDF Document</b><br>*/
    //==========================================================//
    private void Create_a_PDF_Document()
    //==========================================================//
    {   //Create a new PDF document
        m_pdf_document  = new PdfDocument();
    }
    //==========================================================//
    //</editor-fold>

    //<editor-fold default state="collapsed" desc="Write_and_Close_PDF_Document()">
    //==========================================================//
    /**<b>Write and Close the generated PDF report</b><br>*/
    //==========================================================//
    private void Write_and_Close_PDF_Document()
    //==========================================================//
    {
        try
        {   //Write the PDF document content
            m_pdf_document.writeTo(m_file_output_stream);
            //Close PDF document
            m_pdf_document.close();
        }
        catch (IOException e)
        {
            Error_Log(new String[]{"PDF","001",e.toString()});
        }
    }
    //==========================================================//
    //</editor-fold>

    //<editor-fold default state="collapsed" desc="Open_PDF_in_Chrome()">
    //==========================================================//
    /**<b>Open the generated PDF report in Chrome</b><br>
     * <br><i> - Check if Chrome is installed</i>
     * <br><i> - Folder : {@value K_PDF_FOLDER}</i>

     * <br><i> - Package Intent : "com.android.chrome"
     * @param m_context the GUI Context argument.*/
    //==========================================================//
    public void Open_PDF_in_Chrome(Context m_context)
    //==========================================================//
    {
        String  urlString   =   m_pdf_folder + "/" + K_PDF_FILENAME;
        Comment_Log(new String[]{"PDF","Open in Chrome : " + urlString});
        File file = new File(urlString);
        Intent  intent      =   new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        Uri apkURI = FileProvider.getUriForFile(
                m_context,
                m_context.getApplicationContext()
                        .getPackageName() + ".provider", file);
        intent.setDataAndType(apkURI, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try
        {
            m_context.startActivity(intent);
        }
        catch (ActivityNotFoundException ex)
        {
            // Chrome browser presumably not installed so allow user to choose instead
            intent.setPackage(null);
            m_context.startActivity(intent);
        }
    }
    //==========================================================//
    //</editor-fold>
}
//==========================================================//

/*
m_paint.setColor(Color.DKGRAY);
m_paint.setFontVariationSettings(true);
m_paint.setFontVariationSettings("'slnt' 20, 'ital' 1");
m_paint.setLetterSpacing(0.75f);
m_paint.setShadowLayer(9.5f,5.2f,5.8f,Color.DKGRAY);
m_paint.setStrikeThruText(true);
m_paint.setStrokeMiter(1.75f);
m_paint.setStrokeWidth(2.2f);
m_paint.setStyle();
m_paint.setTextAlign(Paint.Align.RIGHT);
m_paint.setTextScaleX(8.2f);
m_paint.setTextSize(15.0f);
m_paint.setTextSkewX(-0.45f);
m_paint.setUnderlineText(true);
*/

/*
m_canvas.drawArc(0f,0f,50f,50f,25,1,true,m_paint);
m_canvas.drawBitmap(...);
m_canvas.drawCircle(50, 50, 30, m_paint);
m_canvas.drawLine(0,120,200,200,m_paint);
m_canvas.drawOval(50,150,100,300,m_paint);
        Drawable myImage = res.getDrawable(R.drawable.read);
        m_canvas.drawPicture(mPicture);
m_canvas.drawRect(20,20,180,80,m_paint);
m_canvas.drawRoundRect(20,40,180,200,10,10,m_paint);
m_canvas.drawPoint(20,50,m_paint);
float[] ff={20,80,22,75,24,70,26,65,28,60,30,65,32,70,34,75};
m_canvas.drawPoints(ff,0,16,m_paint);
m_canvas.drawText("Lio Test Text",100,220,m_paint);
m_canvas.drawLine(20,120,180,120,m_paint);
float[] ll=  {20,140, 60,140,        //1er Segment
            60,140,100, 80,        //2eme Segment
            100,80,180,180         //3eme....
};
m_canvas.drawLines(ll,m_paint);
m_canvas.drawText("YO TEST TITLE", 50, 20, m_paint);
m_canvas.drawCircle(200, 200, 100, m_paint);
m_canvas.drawText("set a label here ", 50, 20, m_paint);
*/