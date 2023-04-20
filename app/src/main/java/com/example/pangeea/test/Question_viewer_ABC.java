package com.example.pangeea.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pangeea.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Question_viewer_ABC extends AppCompatActivity {
    String answer = "Non existent";

    boolean a_pressed = false;
    boolean b_pressed = false;
    boolean c_pressed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle e = getIntent().getExtras();
        HashMap <String,Object>question = (HashMap<String,Object>)e.get("question");
        HashMap<String, Object> answer_map = new HashMap<String, Object>();

        setContentView(R.layout.activity_question_viewer_abc);
        TextView q_prompt = findViewById(R.id.question_prompt_abc);
        TextView q_files = findViewById(R.id.question_files_abc);
        Button upload_question = findViewById(R.id.upload_question_abc);
        answer_map.put("type","A/B/C");

        Button A = findViewById(R.id.a_button);
        Button B = findViewById(R.id.b_button);
        Button C = findViewById(R.id.c_button);
        answer_map.put("A","invalid");
        answer_map.put("B","invalid");
        answer_map.put("C","invalid");
        answer_map.put("prompt",question.get("prompt"));

        A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a_pressed == false){
                    a_pressed = true;
                    A.setBackgroundColor(getResources().getColor(R.color.teal_700));
                    answer_map.put("A","valid");
                    if(answer_map == question){
                        answer_map.put("answer","correct");
                        question.put("answer","correct");
                    }else{
                        answer_map.put("answer","wrong");
                        question.put("answer","wrong");
                    }

                }else{
                    a_pressed = false;
                    A.setBackgroundColor(getResources().getColor(R.color.purple_500));
                    answer_map.put("A","invalid");
                    if(answer_map == question){
                        answer_map.put("answer","correct");
                        question.put("answer","correct");
                    }else{
                        answer_map.put("answer","wrong");
                        question.put("answer","wrong");
                    }

                }


            }
        });
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b_pressed == false){
                    b_pressed = true;
                    B.setBackgroundColor(getResources().getColor(R.color.teal_700));
                    answer_map.put("B","valid");
                    if(answer_map == question);
                    if(answer_map == question){
                        answer_map.put("answer","correct");
                        question.put("answer","correct");
                    }else{
                        answer_map.put("answer","wrong");
                        question.put("answer","wrong");
                    }



                }else{
                    b_pressed = false;
                    B.setBackgroundColor(getResources().getColor(R.color.purple_500));
                    answer_map.put("B","invalid");
                    if(answer_map == question){
                        answer_map.put("answer","correct");
                        question.put("answer","correct");
                    }else{
                        answer_map.put("answer","wrong");
                        question.put("answer","wrong");
                    }
                }

            }
        });
        C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c_pressed == false){
                    c_pressed = true;
                    C.setBackgroundColor(getResources().getColor(R.color.teal_700));
                    answer_map.put("C","valid");

                    if(answer_map == question){
                        answer_map.put("answer","correct");
                        question.put("answer","correct");
                    }else{
                        answer_map.put("answer","wrong");
                        question.put("answer","wrong");
                    }

                }else{
                    c_pressed = false;
                    C.setBackgroundColor(getResources().getColor(R.color.teal_700));
                    answer_map.put("C","invalid");
                    if(answer_map == question){
                        answer_map.put("answer","correct");
                        question.put("answer","correct");
                    }else{
                        answer_map.put("answer","wrong");
                        question.put("answer","wrong");
                    }
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

                Bundle e = getIntent().getExtras();
                List<HashMap<String,Object>> answer_list = (List<HashMap<String, Object>>) e.get("answer_list");
                answer_list.set(e.getInt("index"), answer_map);
                Intent i = new Intent(Question_viewer_ABC.this,Test_viewer_elev.class);
                i.putExtra("answer_list", (Serializable) answer_list);
                i.putExtra("hour_ms",e.getString("hour_ms"));
                i.putExtra("teacher",e.getString("teacher"));
                startActivity(i);
            }
        });

    }
}