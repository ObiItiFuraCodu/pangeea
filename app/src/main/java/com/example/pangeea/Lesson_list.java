package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Lesson_list extends AppCompatActivity {
    DatabaseConnector connector = new DatabaseConnector(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_list);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle e = getIntent().getExtras();

        connector.retrieve_lessons(e.getString("grade"),findViewById(R.id.linearl_lessons),e.getString("main_course"));
    }
}