package com.ublox.BLE.utils;

/**
 * Created by Yasiru on 19/03/2018.
 */

public class CHUserData {

    boolean ch1enable;
    boolean ch1limitenable;
    double ch1upperlimit;
    double ch1lowerlimit;
    int ch1alarmdelay;

    boolean ch2enable;
    boolean ch2limitenable;
    double ch2upperlimit;
    double ch2lowerlimit;
    int ch2alarmdelay;


    public CHUserData(boolean ch1enable, boolean ch1limitenable, double ch1upperlimit, double ch1lowerlimit, int ch1alarmdelay, boolean ch2enable, boolean ch2limitenable, double ch2upperlimit, double ch2lowerlimit, int ch2alarmdelay){
        this.ch1enable = ch1enable;
        this.ch1limitenable = ch1limitenable;
        this.ch1upperlimit = ch1upperlimit;
        this.ch1lowerlimit = ch1lowerlimit;
        this.ch1alarmdelay = ch1alarmdelay;

        this.ch2enable = ch2enable;
        this.ch2limitenable = ch2limitenable;
        this.ch2upperlimit = ch2upperlimit;
        this.ch2lowerlimit = ch2lowerlimit;
        this.ch2alarmdelay = ch2alarmdelay;
    }

    public void setCh1enable(boolean ch1enable){
        this.ch1enable = ch1enable;
    }

    public boolean getCh1enable(){
        return ch1enable;
    }

    public boolean getCh1limitenable(){
        return ch1limitenable;
    }

    public double getCh1upperlimit(){
        return ch1upperlimit;
    }

    public double getCh1lowerlimit(){
        return ch1lowerlimit;
    }

    public int getCh1alarmdelay(){
        return ch1alarmdelay;
    }

    public boolean getCh2enable(){
        return ch2enable;
    }

    public boolean getCh2limitenable(){
        return ch2limitenable;
    }

    public double getCh2upperlimit(){
        return ch2upperlimit;
    }

    public double getCh2lowerlimit(){
        return ch2lowerlimit;
    }

    public int getCh2alarmdelay(){
        return ch2alarmdelay;
    }
}
