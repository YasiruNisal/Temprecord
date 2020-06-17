package com.temprecordapp.yasiruw.temprecord.services;

import android.content.Context;

import com.temprecordapp.yasiruw.temprecord.comms.BaseCMD;
import com.temprecordapp.yasiruw.temprecord.comms.MT2Values;
import com.temprecordapp.yasiruw.temprecord.CustomLibraries.Yasiru_Temp_Library;
import com.temprecordapp.yasiruw.temprecord.utils.App_Info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Json_Data {

    static final String VERSION = "1.0";
    static final String LANGUAGE = "english";
    static final String THEME = "Yasiru";
    static final String TEMP_TITLE = "Temperature";
    static final String HU_TITLE = "Humidity";
    static final String LOCKED = "true";
    static final String TYPE = "mobile";

    private MT2Values.MT2Mem_values mt2Mem_values;
    private BaseCMD baseCMD;
    private Yasiru_Temp_Library QS = new Yasiru_Temp_Library();
    private App_Info app_info = new App_Info();
    private int viewtype;
    private int numchannel = 0;
    StoreKeyService storeKeyService;
    Context context;



    public Json_Data(MT2Values.MT2Mem_values mt2Mem_values, BaseCMD baseCMD, int viewtype, Context context){
        this.mt2Mem_values = mt2Mem_values;
        this.baseCMD = baseCMD;
        this.viewtype = viewtype;
        this.context = context;
    }

    public String CreateObject(){

        JSONArray Devicea = new JSONArray();
        JSONObject Device = new JSONObject();
/**/        JSONObject DataPacket = new JSONObject();
/**/

            JSONObject H = new JSONObject();
            JSONArray channel = new JSONArray();
/**/
/**/            JSONObject data = new JSONObject();
                JSONObject data1 = new JSONObject();
                JSONObject data2 = new JSONObject();

                        JSONArray timea = new JSONArray();
                        JSONArray tempa = new JSONArray();
                        JSONArray huma = new JSONArray();
                        JSONArray taga = new JSONArray();

                    JSONObject ltemp = new JSONObject();
                        JSONObject luppert = new JSONObject();
                            JSONArray uhight = new JSONArray();
                            JSONArray umidt = new JSONArray();
                            JSONArray ulowt = new JSONArray();
                        JSONObject llowert = new JSONObject();
                            JSONArray lhight = new JSONArray();
                            JSONArray lmidt = new JSONArray();
                            JSONArray llowt = new JSONArray();
                    JSONObject lhum = new JSONObject();
                        JSONObject lupperh = new JSONObject();
                            JSONArray uhighh = new JSONArray();
                            JSONArray umidh = new JSONArray();
                            JSONArray ulowh = new JSONArray();
                        JSONObject llowerh = new JSONObject();
                            JSONArray lhighh = new JSONArray();
                            JSONArray lmidh = new JSONArray();
                            JSONArray llowh = new JSONArray();
                            JSONObject limit = new JSONObject();
                    JSONObject D = new JSONObject();

                    JSONObject graph = new JSONObject();
                        JSONObject tempg = new JSONObject();
                        JSONObject humg = new JSONObject();
                        JSONObject tagg = new JSONObject();

        try {

//================================================================================================//
            //Header
//================================================================================================//


            H.put("version", VERSION);
            H.accumulate("language", LANGUAGE);
            H.accumulate("theme", THEME);
            H.accumulate("title", TEMP_TITLE);
            H.accumulate("serial", baseCMD.serialno);
            for (int i = 0; i < mt2Mem_values.Data.size(); i++) {
                timea.put(i, String.valueOf((mt2Mem_values.Data.get(i).valTime.getTime()) / 1000));
                //Log.i(TAG, "date " + mt2Mem_values.Data.get(i).valTime + " +++++++++++++++++++");
            }
            H.accumulate("datetime", timea);
            H.accumulate("mainyaxis", QS.imperial(baseCMD.ImperialUnit));
            H.accumulate("comment", baseCMD.usercomment);
            H.accumulate("firmwareversion", baseCMD.firmware);
            H.accumulate("type", TYPE);
            H.accumulate("ampm", "true");
            H.accumulate("showlimit", QS.TrueorFalse((baseCMD.ch1limitEnabled || baseCMD.ch2limitEnabled)));
            if(viewtype == 1)
                H.accumulate("locked", "true");
            else if(viewtype == 2)
                H.accumulate("locked", "false");

//================================================================================================//
// Channel 1 temperature
//================================================================================================//
            if(baseCMD.ch1Enable){
                for (int i = 0; i < mt2Mem_values.Data.size(); i++) {
                    if (storeKeyService.getDefaults("UNITS", context) != null && storeKeyService.getDefaults("UNITS", context).equals("1")) {
                        tempa.put(i, String.valueOf(mt2Mem_values.Data.get(i).valueCh0()));
                    }else{
                        tempa.put(i, String.format( "%.2f",QS.returnFD(mt2Mem_values.Data.get(i).valueCh0())));
                    }
                }
                data.put("value", tempa);
                data.accumulate("units", QS.imperial(baseCMD.ImperialUnit));

                //date time will be left out for now

                //============================limits==================================================//

                //======================temperature===============================================//

                //===================================upper limits=============================//


                uhight.put(0, "null");
                uhight.put(1, "null");
                uhight.put(2, "null");

                luppert.put("high", uhight);

                if (storeKeyService.getDefaults("UNITS", context) != null && storeKeyService.getDefaults("UNITS", context).equals("1")) {
                    umidt.put(0, String.valueOf(baseCMD.ch1Hi / 10.0));
                }else{
                    umidt.put(0, String.format( "%.2f",QS.returnFD((baseCMD.ch1Hi / 10.0))));
                }
                umidt.put(1, "#FF0000");
                umidt.put(2, "dash");

                luppert.put("mid", umidt);

                ulowt.put(0, "null");
                ulowt.put(1, "null");
                ulowt.put(2, "null");

                luppert.put("low", ulowt);

                ltemp.put("upper", luppert);

                //==============================lower limits==================================//

                lhight.put(0, "null");
                lhight.put(1, "null");
                lhight.put(2, "null");

                llowert.put("high", lhight);
                if (storeKeyService.getDefaults("UNITS", context) != null && storeKeyService.getDefaults("UNITS", context).equals("1")) {
                    lmidt.put(0, String.valueOf(baseCMD.ch1Lo / 10.0));
                }else{
                    lmidt.put(0, String.format( "%.2f",QS.returnFD((baseCMD.ch1Lo / 10.0))));
                }

                lmidt.put(1, "#FF0000");
                lmidt.put(2, "dash");

                llowert.put("mid", lmidt);

                llowt.put(0, "null");
                llowt.put(1, "null");
                llowt.put(2, "null");

                llowert.put("low", llowt);


                ltemp.put("lower", llowert);

                data.put("limits", ltemp);

                //================================================================================//
                //Graph
                //================================================================================//


                tempg.put("linecolor", app_info.TEMP_LINECOLOR);
                tempg.put("linethickness", app_info.TEMP_LINETHICKNESS);
                tempg.put("linetype", app_info.TEMP_LINETYPE);
                tempg.put("upperlimitlinecolor", app_info.TEMP_LIMITLINECOLOR);
                tempg.put("upperlimitlinethickness", app_info.TEMP_LIMITLINETHICKNESS);
                tempg.put("upperlimitlinetype", app_info.TEMP_LIMITLINETYPE);
                tempg.put("lowerlimitlinecolor", app_info.TEMP_LIMITLINECOLOR);
                tempg.put("lowerlimitlinethickness", app_info.TEMP_LIMITLINETHICKNESS);
                tempg.put("lowerlimitlinetype", app_info.TEMP_LIMITLINETYPE);
                tempg.put("markercolor", app_info.TEMP_MARKERCOLOR);
                tempg.put("markersize", app_info.TEMP_MARKERSIZE);
                tempg.put("markertype", app_info.TEMP_MARKERTYPE);

                data.accumulate("graph", tempg);
                channel.put(numchannel,data);
                numchannel++;
            }



//================================================================================================//
//Channel 2 Humidity
//================================================================================================//

            if(baseCMD.ch2Enable){

                for (int i = 0; i < mt2Mem_values.Data.size(); i++) {
                    huma.put(i, String.valueOf(mt2Mem_values.Data.get(i).valueCh1()));
                }
                data1.put("value", huma);
                data1.accumulate("units", " %");
                //date time is not used for now

                //============================limits==================================================//

                //=============================humidity===============================================//

                    //===================================upper limits=============================//
                uhighh.put(0, "null");
                uhighh.put(1, "null");
                uhighh.put(2, "null");

                lupperh.put("high", uhighh);

                umidh.put(0, String.valueOf(baseCMD.ch2Hi / 10.0));
                umidh.put(1, "#FF0000");
                umidh.put(2, "dash");

                lupperh.put("mid", umidh);

                ulowh.put(0, "null");
                ulowh.put(1, "null");
                ulowh.put(2, "null");

                lupperh.put("low", ulowh);


                lhum.put("upper", lupperh);

                //===================================lower limits=============================//

                lhighh.put(0, "null");
                lhighh.put(1, "null");
                lhighh.put(2, "null");

                llowerh.put("high", lhighh);

                lmidh.put(0, String.valueOf(baseCMD.ch2Lo / 10.0));
                lmidh.put(1, "#FF0000");
                lmidh.put(2, "dash");

                llowerh.put("mid", lmidh);

                llowh.put(0, "null");
                llowh.put(1, "null");
                llowh.put(2, "null");

                llowerh.put("low", llowh);

                lhum.put("lower", llowerh);

                data1.accumulate("limits", lhum);

                //================================================================================//
                                //Graph
                //================================================================================//
                humg.put("linecolor", app_info.HUM_LINECOLOR);
                humg.put("linethickness", app_info.HUM_LINETHICKNESS);
                humg.put("linetype", app_info.HUM_LINETYPE);
                humg.put("upperlimitlinecolor", app_info.HUM_LIMITLINECOLOR);
                humg.put("upperlimitlinethickness", app_info.HUM_LIMITLINETHICKNESS);
                humg.put("upperlimitlinetype", app_info.HUM_LIMITLINETYPE);
                humg.put("lowerlimitlinecolor", app_info.HUM_LIMITLINECOLOR);
                humg.put("lowerlimitlinethickness", app_info.HUM_LIMITLINETHICKNESS);
                humg.put("lowerlimitlinetype", app_info.HUM_LIMITLINETYPE);
                humg.put("markercolor", app_info.HUM_MARKERCOLOR);
                humg.put("markersize", app_info.HUM_MARKERSIZE);
                humg.put("markertype", app_info.HUM_MARKERTYPE);

                data1.accumulate("graph", humg);
                channel.put(numchannel,data1);
                numchannel++;

            }

//================================================================================================//
// Tags
//================================================================================================//

            if(mt2Mem_values.TagCount()>0){

                for (int i = 0; i < mt2Mem_values.Data.size(); i++) {
                    taga.put(i, String.valueOf(mt2Mem_values.Data.get(i).ValTag));
                }
                data2.put("value", taga);
                data2.accumulate("units", "");
                //date time is empty
                data2.accumulate("limits","");

                //================================================================================//
                //Graph
                //================================================================================//

                tagg.put("tagcolor", app_info.TAG_COLOR);
                tagg.put("tagsize", app_info.TAG_SIZE);
                tagg.put("tagtype", app_info.TAG_TYPE);

                data2.accumulate("graph", tagg);
                channel.put(numchannel,data2);
                numchannel++;
            }




            DataPacket.put("header", H);
            DataPacket.put("channel", channel);

            Devicea.put(0, DataPacket);

            Device.put("device",Devicea);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Device.toString();
    }
}
