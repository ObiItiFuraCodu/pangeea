package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

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

        DatabaseConnector connector = new DatabaseConnector(Hour_info_elev.this);
        connector.retrieve_hour_data_elev(e.getString("hour_ms"),findViewById(R.id.lv));
    }
}