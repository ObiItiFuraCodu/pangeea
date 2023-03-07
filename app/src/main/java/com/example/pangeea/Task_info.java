package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Task_info extends AppCompatActivity {
    DatabaseConnector connector = new DatabaseConnector(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle e = getIntent().getExtras();
        connector.retrieve_task_data_elev(e.getString("hour_ms"),findViewById(R.id.lessons_list),findViewById(R.id.submissions_list));
    }
}