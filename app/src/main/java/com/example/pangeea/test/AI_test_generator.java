package com.example.pangeea.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pangeea.R;
import com.example.pangeea.ai.Test_AI;

import java.util.HashMap;
import java.util.List;

public class AI_test_generator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_test_generator);
        LinearLayout question_list = findViewById(R.id.question_list);

        Bundle e = getIntent().getExtras();
        Test_AI ai = new Test_AI(AI_test_generator.this,question_list,e.getString("title"),7);


        Button good = findViewById(R.id.good);
        Button bad = findViewById(R.id.bad);
        //core.AI_Text(e.getString("title"),result);
        ai.generate_test();
        ///HashMap<String,String> question = (HashMap<String, String>) test.get(5);
        //Log.i("PROMPT",question.get("prompt"));
        //Log.i("ANSWER1",question.get("1"));
        //Log.i("ANSWER2",question.get("2"));
        //Log.i("ANSWER3",question.get("3"));
        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:set test here
            }
        });
        bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // core.AI_Text(e.getString("title"),result);

            }
        });
    }
}