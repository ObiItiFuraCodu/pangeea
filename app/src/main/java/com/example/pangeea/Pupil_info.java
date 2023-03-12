package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Pupil_info extends AppCompatActivity {
    DatabaseConnector connector = new DatabaseConnector(this);
    Bundle e = getIntent().getExtras();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pupil_info);

    }

    @Override
    protected void onStart() {
        super.onStart();
        connector.retrieve_pupil_info(e.getString("pupil_name"),e.getString("pupil_class"),findViewById(R.id.mark_list),findViewById(R.id.absence_list),findViewById(R.id.avg_mark),findViewById(R.id.absences));

    }
}