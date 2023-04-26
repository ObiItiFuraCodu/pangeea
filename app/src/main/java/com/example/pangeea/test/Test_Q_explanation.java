package com.example.pangeea.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pangeea.CustomElements.CustomButtonAnswer;
import com.example.pangeea.R;
import com.example.pangeea.ai.AI_core;

import java.util.HashMap;

public class Test_Q_explanation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_qexplanation);

    }

    @Override
    protected void onStart() {
        super.onStart();
        AI_core core = new AI_core(Test_Q_explanation.this);

        LinearLayout layout = findViewById(R.id.linearl_answer_explanation);
        Bundle e = getIntent().getExtras();
        HashMap<String,Object> map = (HashMap<String, Object>) e.get("question_content");
        HashMap<String,Object> actual_answer1 = (HashMap<String, Object>) map.get("actual_answer");
        HashMap<String,Object> actual_answer = (HashMap<String, Object>) actual_answer1.get("variants");
        if(!map.get("A_valid").equals(actual_answer.get("A_valid"))){
            CustomButtonAnswer answer = new CustomButtonAnswer(Test_Q_explanation.this);
            Button iswrong = (Button) answer.getChildAt(0);
            if(actual_answer.get("A_valid").equals("valid")){
                iswrong.setText("A era adevarat");
                TextView explanation = (TextView) answer.getChildAt(1);
                core.AI_Text("De ce este adevarat raspunsul " + (String)actual_answer.get("A") + "pentru intrebarea" + (String)map.get("prompt") + "?",explanation);
            }else{
                iswrong.setText("A era gresit");
                TextView explanation = (TextView) answer.getChildAt(1);
                core.AI_Text("De ce este gresit raspunsul " + (String)actual_answer.get("A") + "pentru intrebarea" + (String)map.get("prompt") + "?",explanation);
            }
            layout.addView(answer);
        }
        if(!map.get("B_valid").equals(actual_answer.get("B_valid"))){
            CustomButtonAnswer answer = new CustomButtonAnswer(Test_Q_explanation.this);
            Button iswrong = (Button) answer.getChildAt(0);
            if(actual_answer.get("B_valid").equals("valid")){
                iswrong.setText("B era adevarat");
                TextView explanation = (TextView) answer.getChildAt(1);
                core.AI_Text("De ce este adevarat raspunsul " + (String)actual_answer.get("A") + "pentru intrebarea" + (String)map.get("prompt") + "?",explanation);
            }else{
                iswrong.setText("B era gresit");
                TextView explanation = (TextView) answer.getChildAt(1);
                core.AI_Text("De ce este gresit raspunsul " + (String)actual_answer.get("A") + "pentru intrebarea" + (String)map.get("prompt") + "?",explanation);
            }
            layout.addView(answer);


        }
        if(!map.get("C_valid").equals(actual_answer.get("C_valid"))){
            CustomButtonAnswer answer = new CustomButtonAnswer(Test_Q_explanation.this);
            Button iswrong = (Button) answer.getChildAt(0);
            if(actual_answer.get("C_valid").equals("valid")){
                iswrong.setText("C era adevarat");
                TextView explanation = (TextView) answer.getChildAt(1);
                core.AI_Text("De ce este adevarat raspunsul " + (String)actual_answer.get("A") + "pentru intrebarea" + (String)map.get("prompt") + "?",explanation);
            }else{
                iswrong.setText("C era gresit");
                TextView explanation = (TextView) answer.getChildAt(1);
                core.AI_Text("De ce este gresit raspunsul " + (String)actual_answer.get("A") + "pentru intrebarea" + (String)map.get("prompt") + "?",explanation);
            }
            layout.addView(answer);


        }
    }
}