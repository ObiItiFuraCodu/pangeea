package com.example.pangeea.task;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pangeea.backend.DatabaseConnector;
import com.example.pangeea.R;
import com.example.pangeea.backend.TaskBackend;

public class Task_info_proffesor extends AppCompatActivity {
    TaskBackend connector = new TaskBackend(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info_proffesor);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle e = getIntent().getExtras();
        connector.retrieve_task_data_proffesor(e.getString("hour_milis"),findViewById(R.id.submissions_list),findViewById(R.id.lessons_list),findViewById(R.id.textView27));
    }
}