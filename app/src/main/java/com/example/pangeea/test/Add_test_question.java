package com.example.pangeea.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.pangeea.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Add_test_question extends AppCompatActivity {
    Boolean a_val = false;
    Boolean b_val = false;
    Boolean c_val = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test_question);
        List<String> types = new ArrayList<>();
        types.add("File");
        types.add("Text");
        types.add("A/B/C");

        EditText prompt = findViewById(R.id.q_prompt);
        EditText a_variant = findViewById(R.id.a_prompt);
        EditText b_variant = findViewById(R.id.b_prompt);
        EditText c_variant = findViewById(R.id.c_prompt);
        Button a_valid = findViewById(R.id.a_valid);
        Button b_valid = findViewById(R.id.b_valid);
        Button c_valid = findViewById(R.id.c_valid);

        Button enter = findViewById(R.id.add_q);
        a_valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a_val == false){
                    a_val = true;
                    a_valid.setBackgroundColor(getResources().getColor(R.color.teal_700));

                }else{
                    a_val = false;
                    a_valid.setBackgroundColor(getResources().getColor(R.color.purple_200));
                }
            }
        });
        b_valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b_val == false){
                    b_val = true;
                    b_valid.setBackgroundColor(getResources().getColor(R.color.teal_700));

                }else{
                    b_val = false;
                    b_valid.setBackgroundColor(getResources().getColor(R.color.purple_200));
                }
            }
        });
        c_valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c_val == false){
                    c_val = true;
                    c_valid.setBackgroundColor(getResources().getColor(R.color.teal_700));

                }else{
                    c_val = false;
                    c_valid.setBackgroundColor(getResources().getColor(R.color.purple_200));
                }
            }
        });
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
                            question.put("type",types.get(position));

                            if(a_val == true){
                                question.put("valid",a_variant.getText().toString());
                            }else{
                                question.put("invalid",a_variant.getText().toString());

                            }
                            if(b_val == true){
                                question.put("valid",b_variant.getText().toString());
                            }else{
                                question.put("invalid",b_variant.getText().toString());

                            }
                            if(c_val == true){
                                question.put("valid",c_variant.getText().toString());
                            }else{
                                question.put("invalid",c_variant.getText().toString());

                            }
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
                            question.put("type",types.get(position));

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