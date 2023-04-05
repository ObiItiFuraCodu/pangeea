package com.example.pangeea.ai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pangeea.R;
import com.example.pangeea.ai.AI_core;

public class AI_generator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_generator);


    }

    @Override
    protected void onStart() {
        super.onStart();
        AI_core ai = new AI_core(this);
        Button button = findViewById(R.id.regenerate);
        EditText prompt = findViewById(R.id.prompt);
        TextView result = findViewById(R.id.text_result);
        Bundle e = getIntent().getExtras();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ai.AI_text_3(prompt.getText().toString(),result);
            }
        });
    }
}