package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Materie_info extends AppCompatActivity {
    DatabaseConnector connector = new DatabaseConnector(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materie_info);

    }
    @Override
    protected void onStart() {
        super.onStart();
        connector.retrieve_pupil_info_elev(findViewById(R.id.mark_list),findViewById(R.id.absence_list),findViewById(R.id.avg_mark),findViewById(R.id.absences),getIntent().getExtras().getString("materie_info"));

    }
}