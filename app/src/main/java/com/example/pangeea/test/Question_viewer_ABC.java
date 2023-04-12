package com.example.pangeea.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pangeea.R;

import java.util.HashMap;

public class Question_viewer_ABC extends AppCompatActivity {
    String answer = "Non existent";
    HashMap<String, Object> answer_map = new HashMap<String, Object>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle e = getIntent().getExtras();
        HashMap <String,Object>question = (HashMap<String,Object>)e.get("question");

        setContentView(R.layout.activity_question_viewer_abc);
        TextView q_prompt = findViewById(R.id.question_prompt_abc);
        TextView q_files = findViewById(R.id.question_files_abc);
        Button upload_question = findViewById(R.id.upload_question_abc);
        answer_map.put("type","A/B/C");

        Button A = findViewById(R.id.a_button);
        Button B = findViewById(R.id.b_button);
        Button C = findViewById(R.id.c_button);

        A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(question.get("a_answer").equals("invalid")){
                    answer = "wrong";
                    answer_map.put("answer",answer);
                }else if(answer.equals("Non existent")){
                    answer = "right";
                    answer_map.put("answer",answer);
                }

            }
        });
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(question.get("b_answer").equals("invalid")){
                    answer = "wrong";
                    answer_map.put("answer",answer);
                }else if(answer.equals("Non existent")){
                    answer = "right";
                    answer_map.put("answer",answer);
                }
            }
        });
        C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(question.get("c_answer").equals("invalid")){
                    answer = "wrong";
                    answer_map.put("answer",answer);
                }else if(answer.equals("Non existent")){
                    answer = "right";
                    answer_map.put("answer",answer);
                }
            }
        });





        q_prompt.setText((String)question.get("prompt"));
        q_files.setText("see files");
        q_files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        upload_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Test_viewer_elev info = new Test_viewer_elev();
                info.add_question(answer_map ,(int)question.get("index"));
                startActivity(new Intent(v.getContext(),Test_viewer_elev.class));
            }
        });

    }
}