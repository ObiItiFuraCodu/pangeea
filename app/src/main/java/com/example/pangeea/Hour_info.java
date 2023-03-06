package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Hour_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hour_info);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle e = getIntent().getExtras();
        TextView hour_info = findViewById(R.id.hour_details);
        DatabaseConnector conn = new DatabaseConnector(Hour_info.this);
        conn.retrieve_hour_data(e.getString("hour_milis"),hour_info);
    }
}