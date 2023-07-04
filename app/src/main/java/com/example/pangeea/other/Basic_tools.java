package com.example.pangeea.other;

import android.content.Context;

import com.example.pangeea.R;

import java.util.HashMap;

public class Basic_tools {
    private static Long ONE_HOUR_IN_MILIS = Long.valueOf(3600000);


    public static boolean hour_is_active(Long milisecs){
        if((System.currentTimeMillis() > milisecs) && System.currentTimeMillis() < (milisecs + ONE_HOUR_IN_MILIS)){
            return true;
        }else{
            return false;
        }
    }
    public HashMap<String,Object> ranking_system(int RP, Context context){
        HashMap<String,Object> ranking_data = new HashMap<>();
        if(RP < 100){
            ranking_data.put("rank",1);
            ranking_data.put("icon",context.getResources().getDrawable(R.drawable.ranklow));
        }else{
            ranking_data.put("rank",1);
            ranking_data.put("icon",context.getResources().getDrawable(R.drawable.rankhigh));
        }
        return ranking_data;
    }

}
