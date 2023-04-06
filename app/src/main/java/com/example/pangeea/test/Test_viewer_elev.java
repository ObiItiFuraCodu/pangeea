package com.example.pangeea.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pangeea.R;
import com.example.pangeea.backend.TestBackend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Test_viewer_elev extends AppCompatActivity {
    private List<HashMap<String,String>> answer_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TestBackend backend = new TestBackend(this);
        Bundle e = getIntent().getExtras();

        setContentView(R.layout.activity_test_viewer_elev);
        Button button = findViewById(R.id.finish_butt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backend.correct_test_and_upload(e.getString("hour_ms"),answer_list,e.getString("teacher"));

            }
        });



    }
    public void add_question(HashMap<String,String> answer,int index){
        answer_list.set(index,answer);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle e = getIntent().getExtras();
        TestBackend backend = new TestBackend(this);
        backend.retrieve_test_questions_elev(findViewById(R.id.q_lv),e.getString("hour_ms"));

    }

}