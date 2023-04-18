package com.example.pangeea.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pangeea.R;
import com.example.pangeea.backend.TestBackend;

public class Test_info_prof extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_info_prof);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle e = getIntent().getExtras();
        TestBackend backend = new TestBackend(this);
        backend.retrieve_test_data_proffesor(findViewById(R.id.support_lesson_list),e.getString("hour_milis"),findViewById(R.id.submission_list));
    }
}