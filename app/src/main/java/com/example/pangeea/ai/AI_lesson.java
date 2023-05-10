package com.example.pangeea.ai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pangeea.CustomElements.CustomButtonLesson;
import com.example.pangeea.R;

import org.checkerframework.checker.units.qual.A;

public class AI_lesson extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_lesson);

    }

    @Override
    protected void onStart() {
        super.onStart();
        AI_core core = new AI_core(AI_lesson.this);
        LinearLayout linearl = findViewById(R.id.linearl_prep);
        Bundle e = getIntent().getExtras();
        EditText question_editt = findViewById(R.id.got_questions);
        Button enter = findViewById(R.id.enter_q_button);
        question_editt.setVisibility(View.INVISIBLE);
        enter.setVisibility(View.INVISIBLE);
        CustomButtonLesson lesson = new CustomButtonLesson(AI_lesson.this);
        Button button = (Button) lesson.getChildAt(0);
        button.setText(e.getString("title"));
        TextView view = (TextView) lesson.getChildAt(1);
        core.AI_Lesson(e.getString("title"),view,enter,question_editt);
        linearl.addView(lesson);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomButtonLesson question = new CustomButtonLesson(AI_lesson.this);
                Button buttonq = (Button) question.getChildAt(0);
                TextView viewq = (TextView) question.getChildAt(1);
                buttonq.setText(question_editt.getText().toString());
                core.AI_Text("Raspunsul la intrebarea "+question_editt.getText().toString()+" este:",viewq);
                question_editt.setText("");
                linearl.addView(question);


            }
        });

    }
}