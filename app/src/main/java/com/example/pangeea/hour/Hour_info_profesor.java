package com.example.pangeea.hour;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pangeea.backend.DatabaseConnector;
import com.example.pangeea.R;

public class Hour_info_profesor extends AppCompatActivity {
    DatabaseConnector connector = new DatabaseConnector(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hour_info_profesor);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle e = getIntent().getExtras();
        connector.retrieve_hour_data_prof(e.getString("hour_milis"),findViewById(R.id.linear_1),findViewById(R.id.linear_2),findViewById(R.id.linear_3),findViewById(R.id.close_presence));
    }
}