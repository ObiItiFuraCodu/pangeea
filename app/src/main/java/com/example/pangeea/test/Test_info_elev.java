package com.example.pangeea.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.pangeea.R;
import com.example.pangeea.backend.TestBackend;
import com.example.pangeea.other.Basic_tools;

public class Test_info_elev extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_info_elev);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle e = getIntent().getExtras();
        TestBackend backend = new TestBackend(this);

        backend.retrieve_test_data_elev(e.getString("hour_milis"),findViewById(R.id.lessons_lt),findViewById(R.id.ai_tv),findViewById(R.id.lesson_network_tv),findViewById(R.id.textView29));
        Basic_tools tools = new Basic_tools();
        Button active_inactive = findViewById(R.id.button3);
        if(tools.hour_is_active(Long.parseLong(e.getString("hour_milis")))){
            active_inactive.setText("active");
            active_inactive.setBackgroundColor(getResources().getColor(R.color.green));
        }else{
            active_inactive.setText("inactive");
            active_inactive.setBackgroundColor(getResources().getColor(R.color.red));
        }

    }
}