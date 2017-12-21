package youten.redo.yasiru.util;

/**
 * Created by Yasiru on 14-Dec-17.
 */

public class QueryStrings {

    public String GetGeneration(int str){
        String Generation = "";
        if(str == 5){
            Generation =  "G5";
        }
        return Generation;
    }

    public String GetType(int str){
        String Type = "";
        if(str == 1){
            Type = "MonT-2 BLE";
        }

        return Type;
    }

    public String GetState(int str){
        String state = "";
        if(str == 0){
            state = "Sleeping";
        }else if(str == 1){
            state = "No Config";
        }else if(str == 2){
            state = "Ready";
        }else if(str == 3){
            state = "Delay";
        }else if(str == 4){
            state = "Running";
        }else if(str == 5){
            state = "Stopped";
        }else if(str == 6){
            state = "Reuse";
        }else if(str == 7){
            state = "Error";
        }else if(str == 8){
            state = "Unknown";
        }

        return state;
    }


}
