package com.example.pangeea.ai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pangeea.CustomElements.CustomButtonLesson;
import com.example.pangeea.R;

public class AI_Preparation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_preparation);

    }

    @Override
    protected void onStart() {
        super.onStart();
        LinearLayout preparation_lr = findViewById(R.id.linearl_preparation);
        AI_core core = new AI_core(this);
        Bundle e = getIntent().getExtras();
        String title = e.getString("title");
        String materie = e.getString("materie");
        CustomButtonLesson lesson = new CustomButtonLesson(this);
        Button titleb = (Button) lesson.getChildAt(0);
        TextView content = (TextView) lesson.getChildAt(1);
        titleb.setText("title");
        core.AI_Text("Scrie o lectie legata de " + title + "legata de materia " + materie,content);
        preparation_lr.addView(lesson);


    }
}