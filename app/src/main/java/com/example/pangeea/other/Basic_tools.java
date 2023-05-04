package com.example.pangeea.other;

public class Basic_tools {
    private static Long ONE_HOUR_IN_MILIS = Long.valueOf(3600000);


    public static boolean hour_is_active(Long milisecs){
        if((System.currentTimeMillis() > milisecs) && System.currentTimeMillis() < (milisecs + ONE_HOUR_IN_MILIS)){
            return true;
        }else{
            return false;
        }
    }

}
