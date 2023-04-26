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


        Button A = findViewById(R.id.a_button);
        Button B = findViewById(R.id.b_button);
        Button C = findViewById(R.id.c_button);
        answer_map.put("A_valid","invalid");
        answer_map.put("B_valid","invalid");
        answer_map.put("C_valid","invalid");

        HashMap<String,Object> variants = (HashMap<String, Object>) question.get("variants");
        A.setText((String) variants.get("A"));
        answer_map.put("A",variants.get("A"));
        B.setText((String) variants.get("B"));
        answer_map.put("B",variants.get("B"));
        C.setText((String) variants.get("C"));
        answer_map.put("C",variants.get("C"));


        A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a_pressed == false){
                    a_pressed = true;
                    A.setBackgroundColor(getResources().getColor(R.color.teal_700));
                    answer_map.put("A_valid","valid");
                    if(answer_map.equals(variants)){
                        answer_map.put("answer","correct");
                        variants.put("answer","correct");
                    }else{
                        answer_map.put("answer","wrong");
                        variants.put("answer","wrong");
                    }

                }else{
                    a_pressed = false;
                    A.setBackgroundColor(getResources().getColor(R.color.purple_500));
                    answer_map.put("A_valid","invalid");
                    if(answer_map.equals(variants)){
                        answer_map.put("answer","correct");
                        variants.put("answer","correct");
                    }else{
                        answer_map.put("answer","wrong");
                        variants.put("answer","wrong");
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
                    answer_map.put("B_valid","valid");
                    if(answer_map.equals(variants)){
                        answer_map.put("answer","correct");
                        variants.put("answer","correct");
                    }else{
                        answer_map.put("answer","wrong");
                        variants.put("answer","wrong");
                    }



                }else{
                    b_pressed = false;
                    B.setBackgroundColor(getResources().getColor(R.color.purple_500));
                    answer_map.put("B_valid","invalid");
                    if(answer_map.equals(variants)){
                        answer_map.put("answer","correct");
                        variants.put("answer","correct");
                    }else{
                        answer_map.put("answer","wrong");
                        variants.put("answer","wrong");
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
                    answer_map.put("C_valid","valid");

                    if(answer_map.equals(variants)){
                        answer_map.put("answer","correct");
                        variants.put("answer","correct");
                    }else{
                        answer_map.put("answer","wrong");
                        variants.put("answer","wrong");
                    }

                }else{
                    c_pressed = false;
                    C.setBackgroundColor(getResources().getColor(R.color.teal_700));
                    answer_map.put("C_valid","invalid");
                    if(answer_map.equals(variants)){
                        answer_map.put("answer","correct");
                        variants.put("answer","correct");
                    }else{
                        answer_map.put("answer","wrong");
                        variants.put("answer","wrong");
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
                answer_map.put("type","A/B/C");
                answer_map.put("prompt",(String)question.get("prompt"));
                answer_map.put("actual_answer",question);
                Bundle e = getIntent().getExtras();
                List<HashMap<String,Object>> answer_list = (List<HashMap<String, Object>>) e.get("answer_list");
                answer_list.set(e.getInt("index"), answer_map);
                Intent i = new Intent(Question_viewer_ABC.this,Test_viewer_elev.class);
                i.putExtra("answer_list", (Serializable) answer_list);
                i.putExtra("hour_ms",e.getString("hour_ms"));
                i.putExtra("teacher",e.getString("teacher"));
                startActivity(i);
                finish();
            }
        });

    }
}