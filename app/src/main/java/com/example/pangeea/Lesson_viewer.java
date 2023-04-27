package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.pangeea.ai.AI_core;

import java.util.List;

public class Lesson_viewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_viewer);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LinearLayout layout = findViewById(R.id.linearl_lesson_t);
        Bundle e = getIntent().getExtras();
        AI_core core = new AI_core(Lesson_viewer.this);
        String title = e.getString("title");
        List<String> answer_list = (List<String>) e.get("answer_list");
        core.generate_lessons(layout,answer_list,title);
    }
}