package com.example.pangeea.other;

import android.content.Context;

import com.example.pangeea.R;

import java.util.HashMap;

public class Basic_tools {
    private static Long ONE_HOUR_IN_MILIS = Long.valueOf(3600000);
    private int rank1_points = 0;
    private int rank2_points = 100;
    private int rank3_points = 1000;
    private int rank4_points = 2000;
    private int rank5_points = 5000;

    public static boolean hour_is_active(Long milisecs){
        if((System.currentTimeMillis() > milisecs) && System.currentTimeMillis() < (milisecs + ONE_HOUR_IN_MILIS)){
            return true;
        }else{
            return false;
        }
    }
    public HashMap<String,Object> ranking_system(long RP, Context context){
        HashMap<String,Object> ranking_data = new HashMap<>();
        if(RP < 100){
            ranking_data.put("rank",1);
            ranking_data.put("icon",context.getResources().getDrawable(R.drawable.ic__rank_1));
            ranking_data.put("next_rank_rp",100);
        }else if(RP < 1000){
            ranking_data.put("rank",2);
            ranking_data.put("icon",context.getResources().getDrawable(R.drawable.ic__rank_2));
            ranking_data.put("next_rank_rp",1000);
        }else if(RP < 2000){
            ranking_data.put("rank",3);
            ranking_data.put("icon",context.getResources().getDrawable(R.drawable.ic__rank_3));
            ranking_data.put("next_rank_rp",2000);
        }else if(RP < 5000){
            ranking_data.put("rank",4);
            ranking_data.put("icon",context.getResources().getDrawable(R.drawable.ic__rank_4));
            ranking_data.put("next_rank_rp",5000);
        }else{
            ranking_data.put("rank",5);
            ranking_data.put("icon",context.getResources().getDrawable(R.drawable.ic__rank_4));
            ranking_data.put("next_rank_rp",5000);
        }
        return ranking_data;
    }

}
