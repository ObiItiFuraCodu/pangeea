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
                iswrong.setText(getResources().getString(R.string.a_was_true_));
                TextView question_was = (TextView) answer.getChildAt(1);
                question_was.setText(getResources().getString(R.string.a_was_true)+ (String)map.get("prompt"));
                TextView button_was = (TextView) answer.getChildAt(2);
                button_was.setText(getResources().getString(R.string.a_was) + actual_answer.get("A"));
                TextView explanation = (TextView) answer.getChildAt(3);
                core.AI_Text(getResources().getString(R.string.why_is_true) + (String)actual_answer.get("A") + getResources().getString(R.string.for_q) + (String)map.get("prompt") + "?",explanation);
            }else{
                iswrong.setText(R.string.a_was_wrong);
                TextView question_was = (TextView) answer.getChildAt(1);
                question_was.setText(getResources().getString(R.string.q_was)+ (String)map.get("prompt"));
                TextView button_was = (TextView) answer.getChildAt(2);
                button_was.setText(getResources().getString(R.string.a_was) + actual_answer.get("A"));
                TextView explanation = (TextView) answer.getChildAt(3);
                core.AI_Text(getResources().getString(R.string.why_is_wrong) + (String)actual_answer.get("A") + getResources().getString(R.string.for_q) + (String)map.get("prompt") + "?",explanation);
            }
            layout.addView(custom_butt);
        }
        if(!map.get("B_valid").equals(actual_answer.get("B_valid"))){
            CustomButtonAnswer custom_butt = new CustomButtonAnswer(Test_Q_explanation.this);
            LinearLayout answer = (LinearLayout) custom_butt.getChildAt(0);
            Button iswrong = (Button) answer.getChildAt(0);
            if(actual_answer.get("B_valid").equals("valid")){
                iswrong.setText(getResources().getString(R.string.b_was_true));
                TextView question_was = (TextView) answer.getChildAt(1);
                question_was.setText(getResources().getString(R.string.q_was)+ (String)map.get("prompt"));
                TextView button_was = (TextView) answer.getChildAt(2);
                button_was.setText(getResources().getString(R.string.b_was) + actual_answer.get("B"));
                TextView explanation = (TextView) answer.getChildAt(3);
                core.AI_Text(getResources().getString(R.string.why_is_true) + (String)actual_answer.get("B") + getResources().getString(R.string.for_q) + (String)map.get("prompt") + "?",explanation);
            }else{
                iswrong.setText(getResources().getString(R.string.b_was_wrong));
                TextView question_was = (TextView) answer.getChildAt(1);
                question_was.setText(getResources().getString(R.string.q_was)+ (String)map.get("prompt"));
                TextView button_was = (TextView) answer.getChildAt(2);
                button_was.setText(getResources().getString(R.string.b_was) + actual_answer.get("B"));
                TextView explanation = (TextView) answer.getChildAt(3);
                core.AI_Text(getResources().getString(R.string.why_is_wrong) + (String)actual_answer.get("B") + getResources().getString(R.string.for_q) + (String)map.get("prompt") + "?",explanation);
            }
            layout.addView(custom_butt);


        }
        if(!map.get("C_valid").equals(actual_answer.get("C_valid"))){
            CustomButtonAnswer custom_butt = new CustomButtonAnswer(Test_Q_explanation.this);
            LinearLayout answer = (LinearLayout) custom_butt.getChildAt(0);
            Button iswrong = (Button) answer.getChildAt(0);
            if(actual_answer.get("C_valid").equals("valid")){
                iswrong.setText(R.string.c_was_true);
                TextView question_was = (TextView) answer.getChildAt(1);
                question_was.setText(getResources().getString(R.string.q_was)+ (String)map.get("prompt"));
                TextView button_was = (TextView) answer.getChildAt(2);
                button_was.setText(getString(R.string.c_was) + actual_answer.get("C"));
                TextView explanation = (TextView) answer.getChildAt(3);
                core.AI_Text(getResources().getString(R.string.why_is_true) + (String)actual_answer.get("C") + getResources().getString(R.string.for_q) + (String)map.get("prompt") + "?",explanation);
            }else{
                iswrong.setText(R.string.c_was_wrong);
                TextView question_was = (TextView) answer.getChildAt(1);
                question_was.setText(getResources().getString(R.string.q_was)+ (String)map.get("prompt"));
                TextView button_was = (TextView) answer.getChildAt(2);
                button_was.setText(getString(R.string.c_was) + actual_answer.get("C"));
                TextView explanation = (TextView) answer.getChildAt(3);
                core.AI_Text(getResources().getString(R.string.why_is_wrong) + (String)actual_answer.get("C") + getResources().getString(R.string.for_q) + (String)map.get("prompt") + "?",explanation);
            }
            layout.addView(custom_butt);


        }
    }
}