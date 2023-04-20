package com.example.pangeea.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pangeea.R;
import com.example.pangeea.ai.AI_core;

public class AI_test_generator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_test_generator);
        AI_core core = new AI_core(this);
        Bundle e = getIntent().getExtras();
        TextView result = findViewById(R.id.text_result_from);
        Button good = findViewById(R.id.good);
        Button bad = findViewById(R.id.bad);
        core.AI_text_3("Genereaza un test bazat pe lectia" + e.getString("title") + "cu 3 intrebari fiecare avand 3 posibile raspunsuri",result);
        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:set test here
            }
        });
        bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                core.AI_text_3("Genereaza un test bazat pe lectia" + e.getString("title") + "cu 3 intrebari fiecare avand 3 posibile raspunsuri",result);

            }
        });
    }
}