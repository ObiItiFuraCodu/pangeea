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
        //EXPLICATIA TESTULUI
        HashMap<String,Object> map = (HashMap<String, Object>) e.get("question_content");
        HashMap<String,Object> actual_answer1 = (HashMap<String, Object>) map.get("actual_answer");
        HashMap<String,Object> actual_answer = (HashMap<String, Object>) actual_answer1.get("variants");
        if(!map.get("A_valid").equals(actual_answer.get("A_valid"))){
            CustomButtonAnswer custom_butt = new CustomButtonAnswer(Test_Q_explanation.this);
            LinearLayout answer = (LinearLayout) custom_butt.getChildAt(0);
            Button iswrong = (Button) answer.getChildAt(0);
            if(actual_answer.get("A_valid").equals("valid")){
                iswrong.setText("A era adevarat");
                TextView question_was = (TextView) answer.getChildAt(1);
                question_was.setText("Intrebarea era : "+ (String)map.get("prompt"));
                TextView button_was = (TextView) answer.getChildAt(2);
                button_was.setText("A era : " + actual_answer.get("A"));
                TextView explanation = (TextView) answer.getChildAt(3);
                core.AI_Text("De ce este adevarat raspunsul " + (String)actual_answer.get("A") + "pentru intrebarea" + (String)map.get("prompt") + "?",explanation);
            }else{
                iswrong.setText("A era gresit");
                TextView question_was = (TextView) answer.getChildAt(1);
                question_was.setText("Intrebarea era : "+ (String)map.get("prompt"));
                TextView button_was = (TextView) answer.getChildAt(2);
                button_was.setText("A era : " + actual_answer.get("A"));
                TextView explanation = (TextView) answer.getChildAt(3);
                core.AI_Text("De ce este gresit raspunsul " + (String)actual_answer.get("A") + "pentru intrebarea" + (String)map.get("prompt") + "?",explanation);
            }
            layout.addView(custom_butt);
        }
        if(!map.get("B_valid").equals(actual_answer.get("B_valid"))){
            CustomButtonAnswer custom_butt = new CustomButtonAnswer(Test_Q_explanation.this);
            LinearLayout answer = (LinearLayout) custom_butt.getChildAt(0);
            Button iswrong = (Button) answer.getChildAt(0);
            if(actual_answer.get("B_valid").equals("valid")){
                iswrong.setText("B era adevarat");
                TextView question_was = (TextView) answer.getChildAt(1);
                question_was.setText("Intrebarea era : "+ (String)map.get("prompt"));
                TextView button_was = (TextView) answer.getChildAt(2);
                button_was.setText("B era : " + actual_answer.get("B"));
                TextView explanation = (TextView) answer.getChildAt(3);
                core.AI_Text("De ce este adevarat raspunsul " + (String)actual_answer.get("B") + "pentru intrebarea" + (String)map.get("prompt") + "?",explanation);
            }else{
                iswrong.setText("B era gresit");
                TextView question_was = (TextView) answer.getChildAt(1);
                question_was.setText("Intrebarea era : "+ (String)map.get("prompt"));
                TextView button_was = (TextView) answer.getChildAt(2);
                button_was.setText("B era : " + actual_answer.get("B"));
                TextView explanation = (TextView) answer.getChildAt(3);
                core.AI_Text("De ce este gresit raspunsul " + (String)actual_answer.get("B") + "pentru intrebarea" + (String)map.get("prompt") + "?",explanation);
            }
            layout.addView(custom_butt);


        }
        if(!map.get("C_valid").equals(actual_answer.get("C_valid"))){
            CustomButtonAnswer custom_butt = new CustomButtonAnswer(Test_Q_explanation.this);
            LinearLayout answer = (LinearLayout) custom_butt.getChildAt(0);
            Button iswrong = (Button) answer.getChildAt(0);
            if(actual_answer.get("C_valid").equals("valid")){
                iswrong.setText("C era adevarat");
                TextView question_was = (TextView) answer.getChildAt(1);
                question_was.setText("Intrebarea era : "+ (String)map.get("prompt"));
                TextView button_was = (TextView) answer.getChildAt(2);
                button_was.setText("C era : " + actual_answer.get("C"));
                TextView explanation = (TextView) answer.getChildAt(3);
                core.AI_Text("De ce este adevarat raspunsul " + (String)actual_answer.get("C") + "pentru intrebarea" + (String)map.get("prompt") + "?",explanation);
            }else{
                iswrong.setText("C era gresit");
                TextView question_was = (TextView) answer.getChildAt(1);
                question_was.setText("Intrebarea era : "+ (String)map.get("prompt"));
                TextView button_was = (TextView) answer.getChildAt(2);
                button_was.setText("C era : " + actual_answer.get("C"));
                TextView explanation = (TextView) answer.getChildAt(3);
                core.AI_Text("De ce este gresit raspunsul " + (String)actual_answer.get("C") + "pentru intrebarea" + (String)map.get("prompt") + "?",explanation);
            }
            layout.addView(custom_butt);


        }
    }
}