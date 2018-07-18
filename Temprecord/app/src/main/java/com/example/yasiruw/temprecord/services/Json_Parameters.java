package com.example.yasiruw.temprecord.services;

import com.example.yasiruw.temprecord.comms.BaseCMD;
import com.example.yasiruw.temprecord.comms.MT2Values;

public class Json_Parameters {

    MT2Values.MT2Mem_values mt2Mem_values;
    BaseCMD baseCMD;

    public Json_Parameters(MT2Values.MT2Mem_values mt2Mem_values, BaseCMD baseCMD){
        this.baseCMD = baseCMD;
        this.mt2Mem_values = mt2Mem_values;
    }




}
