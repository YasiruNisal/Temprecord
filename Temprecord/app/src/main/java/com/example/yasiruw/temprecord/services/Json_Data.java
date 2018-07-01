package com.example.yasiruw.temprecord.services;

import com.example.yasiruw.temprecord.comms.BaseCMD;
import com.example.yasiruw.temprecord.comms.MT2Values;
import com.example.yasiruw.temprecord.comms.QueryStrings;
import com.example.yasiruw.temprecord.utils.Graph_Info;
import com.google.gson.JsonSerializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Json_Data {

    static final String VERSION = "1.0";
    static final String THEME = "Yasiru";
    static final String TEMP_TITLE = "Temperature";
    static final String HU_TITLE = "Humidity";
    static final String LOCKED = "false";

    private MT2Values.MT2Mem_values mt2Mem_values;
    private BaseCMD baseCMD;
    private QueryStrings QS = new QueryStrings();
    private Graph_Info graph_info = new Graph_Info();


    public Json_Data(MT2Values.MT2Mem_values mt2Mem_values, BaseCMD baseCMD){
        this.mt2Mem_values = mt2Mem_values;
        this.baseCMD = baseCMD;
    }

    public String CreateObject(){



/**/    JSONObject DataPacket = new JSONObject();
/**/        JSONObject H = new JSONObject();


            JSONArray logger = new JSONArray();
/**/        JSONObject L = new JSONObject();
/**/            JSONObject data = new JSONObject();

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
                JSONObject D = new JSONObject();
                    JSONObject tempg = new JSONObject();
                    JSONObject graph = new JSONObject();
                        JSONObject limit = new JSONObject();
                    JSONObject humg = new JSONObject();

        try {

//================================================================================================//
            //Header
//================================================================================================//


            H.put("version", VERSION);
            H.accumulate("theme", THEME);
            H.accumulate("title", TEMP_TITLE);
            H.accumulate("ampm", "true");
            H.accumulate("celcius", QS.TrueorFalse(baseCMD.ImperialUnit));
            H.accumulate("showlimit", QS.TrueorFalse((baseCMD.ch1limitEnabled|| baseCMD.ch2limitEnabled)));
            H.accumulate("locked", LOCKED);


//================================================================================================//
            //Logger
//================================================================================================//

            //============================data====================================================//
            for(int i = 0; i < mt2Mem_values.Data.size(); i++){
                timea.put(i,  String.valueOf((mt2Mem_values.Data.get(i).valTime.getTime())/1000));
            }
            data.put("datetime", timea);


            for(int i = 0; i < mt2Mem_values.Data.size(); i++){
                tempa.put(i,  String.valueOf(mt2Mem_values.Data.get(i).valueCh0()));
            }
            data.accumulate("temp",tempa);


            if(baseCMD.ch2Enable) {
                for (int i = 0; i < mt2Mem_values.Data.size(); i++) {
                    huma.put(i, String.valueOf(mt2Mem_values.Data.get(i).valueCh1()));
                }
                data.accumulate("hum", huma);
            }else{
                data.accumulate("hum", "null");
            }

            for(int i = 0; i < mt2Mem_values.Data.size(); i++){
                taga.put(i,  String.valueOf(mt2Mem_values.Data.get(i).ValTag));
            }
            data.accumulate("tag",taga);
            L.put("data", data);

            //============================limits==================================================//

                //======================temperature===============================================//

                    //===================================upper limits=============================//


                uhight.put(0, "null");
                uhight.put(1, "null");
                uhight.put(2, "null");

            luppert.put("high", uhight);

                umidt.put(0, String.valueOf(baseCMD.ch1Hi / 10.0));
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

                lmidt.put(0, String.valueOf(baseCMD.ch1Lo/10.0));
                lmidt.put(1, "#FF0000");
                lmidt.put(2, "dash");

            llowert.put("mid", lmidt);

                llowt.put(0, "null");
                llowt.put(1, "null");
                llowt.put(2, "null");

            llowert.put("low", llowt);


                ltemp.put("lower", llowert);

                limit.put("temp", ltemp);

//            //=============================humidity===============================================//
            if(baseCMD.ch2Enable ) {
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

                limit.accumulate("hum", lhum);
            }

                L.put("limits", limit);

//================================================================================================//
            //Details
//================================================================================================//

            D.put("name", baseCMD.usercomment);
            D.put("serial", baseCMD.serialno);

            L.put("details", D);


//================================================================================================//
            //Graph
//================================================================================================//

            //===================================temp=============================================//


            tempg.put("linecolor", graph_info.TEMP_LINECOLOR);
            tempg.put("linethickness", graph_info.TEMP_LINETHICKNESS);
            tempg.put("linetype", graph_info.TEMP_LINETYPE);
            tempg.put("limitlinecolor", graph_info.TEMP_LIMITLINECOLOR);
            tempg.put("limitlinethickness", graph_info.TEMP_LINETHICKNESS);
            tempg.put("limitlinetype", graph_info.TEMP_LIMITLINETYPE);
            tempg.put("markercolor", graph_info.TEMP_MARKERCOLOR);
            tempg.put("markersize", graph_info.TEMP_MARKERSIZE);
            tempg.put("markertype", graph_info.TEMP_MARKERTYPE);

            graph.put("temp", tempg);

            if(baseCMD.ch2Enable) {

                humg.put("linecolor", graph_info.HUM_LINECOLOR);
                humg.put("linethickness", graph_info.HUM_LINETHICKNESS);
                humg.put("linetype", graph_info.HUM_LINETYPE);
                humg.put("limitlinecolor", graph_info.HUM_LIMITLINECOLOR);
                humg.put("limitlinethickness", graph_info.HUM_LINETHICKNESS);
                humg.put("limitlinetype", graph_info.HUM_LIMITLINETYPE);
                humg.put("markercolor", graph_info.HUM_MARKERCOLOR);
                humg.put("markersize", graph_info.HUM_MARKERSIZE);
                humg.put("markertype", graph_info.HUM_MARKERTYPE);

                graph.put("hum", humg);

            }
            L.put("graph", graph);
            //====================================================================================//
            logger.put(0,L);

            DataPacket.put("header", H);
            DataPacket.put("logger", logger);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return DataPacket.toString();
    }
}
