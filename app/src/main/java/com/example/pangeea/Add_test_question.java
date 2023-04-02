package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

public class Add_test_question extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test_question);
        EditText prompt = findViewById(R.id.q_prompt);
        EditText a_variant = findViewById(R.id.a_prompt);
        EditText b_variant = findViewById(R.id.b_prompt);
        EditText c_variant = findViewById(R.id.c_prompt);

        Spinner type = findViewById(R.id.spinner2);


    }
}