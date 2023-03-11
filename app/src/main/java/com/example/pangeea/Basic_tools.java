package com.example.pangeea;

public class Basic_tools {
    final long ONE_HOUR_IN_MILIS = 3600000;

    public boolean hour_is_active(Long milisecs){
        if((milisecs - ONE_HOUR_IN_MILIS) < System.currentTimeMillis() && milisecs > System.currentTimeMillis()){
            return true;
        }else{
            return false;
        }
    }

}
