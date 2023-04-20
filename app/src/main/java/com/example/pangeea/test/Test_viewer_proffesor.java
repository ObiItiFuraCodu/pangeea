package com.example.pangeea.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pangeea.R;
import com.example.pangeea.backend.TestBackend;

public class Test_viewer_proffesor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_viewer_proffesor);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Bundle e = getIntent().getExtras();
        TestBackend backend = new TestBackend(this);
        backend.retrieve_questions_to_be_corrected(findViewById(R.id.q_lv_2),e.getString("hour_ms"),e.getString("pupil"),e.getString("pupil_class"));

    }
}