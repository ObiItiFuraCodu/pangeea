package com.example.pangeea.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pangeea.R;
import com.example.pangeea.backend.TestBackend;

public class Test_info_elev extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_info_elev);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle e = getIntent().getExtras();
        TestBackend backend = new TestBackend(this);
        backend.retrieve_test_data_elev(e.getString("hour_milis"),findViewById(R.id.lessons_lt),findViewById(R.id.teacher_tv),findViewById(R.id.ai_tv),findViewById(R.id.lesson_network_tv));

    }
}