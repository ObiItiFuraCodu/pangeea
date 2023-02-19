package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.widget.LinearLayout;
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

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = getIntent().getExtras();
        DatabaseConnector connector = new DatabaseConnector(Class_info.this);
        LinearLayout pupil_list = findViewById(R.id.linear_layout);
        connector.retrieve_class_info(bundle.getString("class_selected"),pupil_list);
    }
}