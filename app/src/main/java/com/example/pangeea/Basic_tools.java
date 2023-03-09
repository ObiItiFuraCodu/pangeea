package com.example.pangeea;

public class Basic_tools {
    public boolean hour_is_active(Long hour_ms){
        if(System.currentTimeMillis() > hour_ms && System.currentTimeMillis() < (hour_ms + 3600000)){
            return true;
        }else{
            return false;
        }
    }

}
