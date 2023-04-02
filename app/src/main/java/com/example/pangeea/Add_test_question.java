package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Add_test_question extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test_question);
        List<String> types = new ArrayList<>();
        types.add("File");
        types.add("Text");
        types.add("A/B/C");
        Boolean a_val = false;
        Boolean b_val = false;
        Boolean c_val = false;
        EditText prompt = findViewById(R.id.q_prompt);
        EditText a_variant = findViewById(R.id.a_prompt);
        EditText b_variant = findViewById(R.id.b_prompt);
        EditText c_variant = findViewById(R.id.c_prompt);
        Button a_valid = findViewById(R.id.a_valid);
        Button b_valid = findViewById(R.id.b_valid);
        Button c_valid = findViewById(R.id.c_valid);

        Button enter = findViewById(R.id.add_q);


        Spinner type = findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Add_test_question.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,types);
        type.setAdapter(adapter);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(types.get(position).equals("A/B/C")){
                    enter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String,String> question = new HashMap<>();
                            question.put("prompt",prompt.getText().toString());

                            Add_test test = new Add_test();
                            test.add_question(question);

                        }
                    });
                }else{
                    a_variant.setVisibility(View.INVISIBLE);
                    b_variant.setVisibility(View.INVISIBLE);
                    c_variant.setVisibility(View.INVISIBLE);
                    a_valid.setVisibility(View.INVISIBLE);
                    b_valid.setVisibility(View.INVISIBLE);
                    c_valid.setVisibility(View.INVISIBLE);

                    enter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String,String> question = new HashMap<>();
                            question.put("prompt",prompt.getText().toString());

                            Add_test test = new Add_test();
                            test.add_question(question);

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}