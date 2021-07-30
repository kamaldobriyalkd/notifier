package com.rla.withu_thesarthi;

import com.ufobeaconsdk.main.UFODevice;

public class CurrentLocation {
    public static String strLocation;
    public static String curLoc(UFODevice ufodevice){
        if(ufodevice.getMajor()==1 && ufodevice.getMinor()==1){
            strLocation="Entrance";
        }else if(ufodevice.getMajor()==1 && ufodevice.getMinor()==2){
            strLocation="Stage";
        }
        return strLocation;
    }
}
