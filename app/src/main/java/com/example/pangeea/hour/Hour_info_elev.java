package com.example.pangeea.hour;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.pangeea.backend.DatabaseConnector;
import com.example.pangeea.R;
import com.example.pangeea.backend.HourBackend;
import com.example.pangeea.other.Basic_tools;

public class Hour_info_elev extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hour_info);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle e = getIntent().getExtras();
        Basic_tools tools = new Basic_tools();

        HourBackend connector = new HourBackend(Hour_info_elev.this);
        connector.retrieve_hour_data_elev(e.getString("hour_milis"),findViewById(R.id.lv),findViewById(R.id.raise_hand),e.getBoolean("presence"),findViewById(R.id.ai_button),findViewById(R.id.lesson_net_button),findViewById(R.id.textView8));
        Button active_inactive = findViewById(R.id.active_inactive);

        if(!tools.hour_is_active(Long.parseLong(e.getString("hour_milis")))){

            active_inactive.setText("inactive");
            active_inactive.setBackgroundColor(getResources().getColor(R.color.red));
        }else{
            active_inactive.setText("active");
            active_inactive.setBackgroundColor(getResources().getColor(R.color.green));
        }
    }
}