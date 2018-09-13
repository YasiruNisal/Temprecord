package com.temprecordapp.yasiruw.temprecord.comms;



import com.temprecordapp.yasiruw.temprecord.Types.baseType;
import com.temprecordapp.yasiruw.temprecord.Types.bitBool;

import java.util.ArrayList;

/**
 * Created by Yasiru on 1/03/2018.
 */

public class MT2_UserFlags extends baseType {

    public static final int ByteSize = 2;

    bitBool imperialUnits;
    bitBool loopOverwrite;
    bitBool lcdMenu;
    bitBool allowTags;
    bitBool buttonStart1;
    bitBool buttonStop1;
    bitBool buttonReuse;
    bitBool fullCheck;

    bitBool safeStats;
    bitBool energysave;
    bitBool startDateTime;
    bitBool passwordEnabled;
    bitBool stoponfull;
    bitBool stopOnSamples;
    bitBool stoponDate;
    bitBool extendedMenu;

    public MT2_UserFlags(){
        super(ByteSize);

        imperialUnits = new bitBool(0);
        loopOverwrite = new bitBool(1);
        lcdMenu = new bitBool(2);
        allowTags  = new bitBool(3);
        buttonStart1 = new bitBool(4);
        buttonStop1 = new bitBool(5);
        buttonReuse = new bitBool(6);
        fullCheck = new bitBool(7);

        safeStats = new bitBool(0);
        energysave = new bitBool(1);
        startDateTime = new bitBool(2);
        passwordEnabled = new bitBool(3);
        stoponfull = new bitBool(4);
        stopOnSamples = new bitBool(5);
        stoponDate = new bitBool(6);
        extendedMenu = new bitBool(7);
    }

    public MT2_UserFlags(ArrayList<Byte> data){
        super(ByteSize);

        imperialUnits = new bitBool(data.get(0),0);
        loopOverwrite = new bitBool(data.get(0),1);
        lcdMenu = new bitBool(data.get(0),2);
        allowTags  = new bitBool(data.get(0),3);
        buttonStart1 = new bitBool(data.get(0),4);
        buttonStop1 = new bitBool(data.get(0),5);
        buttonReuse = new bitBool(data.get(0),6);
        fullCheck = new bitBool(data.get(0),7);
        data.remove(0);
        safeStats = new bitBool(data.get(0),0);
        energysave = new bitBool(data.get(0),1);
        startDateTime = new bitBool(data.get(0),2);
        passwordEnabled = new bitBool(data.get(0),3);
        stoponfull = new bitBool(data.get(0),4);
        stopOnSamples = new bitBool(data.get(0),5);
        stoponDate = new bitBool(data.get(0),6);
        extendedMenu = new bitBool(data.get(0),7);
        data.remove(0);
    }

    public void ToByte(ArrayList<Byte> data){

        data.add((byte)0x00);
        imperialUnits.ToByte(data);
        loopOverwrite.ToByte(data);
        lcdMenu.ToByte(data);
        allowTags.ToByte(data);
        buttonStart1.ToByte(data);
        buttonStop1.ToByte(data);
        buttonReuse.ToByte(data);
        fullCheck.ToByte(data);


        data.add((byte)0x00);
        safeStats.ToByte(data);
        energysave.ToByte(data);
        startDateTime.ToByte(data);
        passwordEnabled.ToByte(data);
        stoponfull.ToByte(data);
        stopOnSamples.ToByte(data);
        stoponDate.ToByte(data);
        extendedMenu.ToByte(data);
    }


    public boolean ImperialUnit(){return imperialUnits.getValue();}
    public boolean LoopOverWrite(){return loopOverwrite.getValue();}
    public boolean LCDMenu(){return lcdMenu.getValue();}
    public boolean AllowTags(){return allowTags.getValue();}
    public boolean ButtonStart1(){return buttonStart1.getValue();}
    public boolean ButtonStop(){return buttonStop1.getValue();}
    public boolean ButtonReuse(){return buttonReuse.getValue();}
    public boolean FullCheck(){return fullCheck.getValue();}
    public boolean EnergySave(){return energysave.getValue();}
    public boolean StartDateTime(){return startDateTime.getValue();}
    public boolean PasswordEnabled(){return passwordEnabled.getValue();}
    public boolean StoponFull(){return stoponfull.getValue();}
    public boolean StoponSample(){return stopOnSamples.getValue();}
    public boolean StoponDate(){return stoponDate.getValue();}
    public boolean ExtendedMenu(){return extendedMenu.getValue();}

}
