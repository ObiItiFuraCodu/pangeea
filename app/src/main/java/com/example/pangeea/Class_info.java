package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.widget.TextView;

public class Class_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);
        String classname = getIntent().getStringExtra("classname");
        TextView hour = findViewById(R.id.classname);
        TextView status = findViewById(R.id.status);
        TextView lesson = findViewById(R.id.currentlesson);


    }
}