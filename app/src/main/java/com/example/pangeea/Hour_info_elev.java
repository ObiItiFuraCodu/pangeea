package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class Hour_info_elev extends AppCompatActivity {
    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
    SharedPreferences.Editor editor = pref.edit();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hour_info);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle e = getIntent().getExtras();

        DatabaseConnector connector = new DatabaseConnector(Hour_info_elev.this);
        if(true){
            connector.retrieve_hour_data_elev(e.getString("hour_milis"),findViewById(R.id.lv),findViewById(R.id.raise_hand));

        }else{
            editor.putBoolean(e.getString("hour_milis"),false);
            editor.commit();
        }
    }
}