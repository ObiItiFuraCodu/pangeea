package com.example.pangeea.other;

public class Basic_tools {
    private static Long ONE_HOUR_IN_MILIS = Long.valueOf(3600000);


    public static boolean hour_is_active(Long milisecs){
        if((milisecs - ONE_HOUR_IN_MILIS) < System.currentTimeMillis() && milisecs > System.currentTimeMillis()){
            return true;
        }else{
            return false;
        }
    }

}
