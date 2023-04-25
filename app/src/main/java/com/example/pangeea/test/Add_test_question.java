package com.example.pangeea.test;

import static java.sql.Types.NULL;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.pangeea.R;
import com.example.pangeea.hour.Add_hour;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Add_test_question extends AppCompatActivity {
    Boolean a_val = false;
    Boolean b_val = false;
    Boolean c_val = false;
    List<String> filenames = new ArrayList<>();
    List<Uri> files = new ArrayList<>();

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
        Button add_q_f = findViewById(R.id.add_q_file);

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
        add_q_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });
        Spinner type = findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Add_test_question.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,types);
        type.setAdapter(adapter);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(types.get(position).equals("A/B/C")){
                    a_variant.setVisibility(View.VISIBLE);
                    b_variant.setVisibility(View.VISIBLE);
                    c_variant.setVisibility(View.VISIBLE);
                    a_valid.setVisibility(View.VISIBLE);
                    b_valid.setVisibility(View.VISIBLE);
                    c_valid.setVisibility(View.VISIBLE);
                    enter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String,Object> question = new HashMap<>();
                            question.put("prompt",prompt.getText().toString());
                            question.put("type",types.get(position));
                            question.put("filenames",filenames);

                            HashMap<String,String> variants = new HashMap<>();
                            Bundle e = getIntent().getExtras();
                            if(types.get(position).equals("A/B/C")){
                                if(a_val == true){
                                    variants.put("A",a_variant.getText().toString());
                                    variants.put("A_valid","valid");
                                }else{
                                    variants.put("A",a_variant.getText().toString());
                                    variants.put("A_valid","invalid");
                                }
                                if(b_val == true){
                                    variants.put("B",a_variant.getText().toString());
                                    variants.put("B_valid","valid");
                                }else{
                                    variants.put("B",a_variant.getText().toString());
                                    variants.put("B_valid","invalid");

                                }
                                if(c_val == true){
                                    variants.put("C",a_variant.getText().toString());
                                    variants.put("C_valid","valid");
                                }else{
                                    variants.put("C",a_variant.getText().toString());
                                    variants.put("C_valid","invalid");

                                }
                                question.put("variants",variants);
                            }
                            List<HashMap<String,Object>> questions;
                            List<String> question_names;
                            if(e.get("questions") != null){
                                 questions = (List<HashMap<String, Object>>) e.get("questions");
                                 question_names = (List<String>) e.get("questionnames");

                            }else{
                                 questions = new ArrayList<>();
                                 question_names = new ArrayList<>();

                            }
                            questions.add(question);
                            question_names.add(prompt.getText().toString());
                            Intent i = new Intent(Add_test_question.this,Add_test.class);
                            i.putExtra("questions", (Serializable) questions);
                            i.putExtra("questionnames", (Serializable) question_names);
                            i.putExtra("files", (Serializable) e.get("files"));
                            i.putExtra("filenames", (Serializable) e.get("filenames"));
                            i.putExtra("class_selected",e.getString("class_selected"));
                            if(e.get("hour_ms") != null){
                                i.putExtra("hour_ms",e.getLong("hour_ms"));
                            }
                            startActivity(i);

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
                            Bundle e = getIntent().getExtras();
                            HashMap<String,Object> question = new HashMap<>();
                            question.put("prompt",prompt.getText().toString());
                            question.put("type",types.get(position));
                            question.put("filenames",filenames);

                            List<HashMap<String,Object>> questions;
                            List<String> question_names;
                            if(e.get("questions") != null){
                                questions = (List<HashMap<String, Object>>) e.get("questions");
                                question_names = (List<String>) e.get("questionnames");

                            }else{
                                questions = new ArrayList<>();
                                question_names = new ArrayList<>();

                            }
                            questions.add(question);
                            question_names.add(prompt.getText().toString());
                            Intent i = new Intent(Add_test_question.this,Add_test.class);
                            i.putExtra("questions", (Serializable) questions);
                            i.putExtra("questionnames", (Serializable) question_names);
                            i.putExtra("files", (Serializable) e.get("files"));
                            i.putExtra("filenames", (Serializable) e.get("filenames"));
                            i.putExtra("class_selected",e.getString("class_selected"));
                            if(e.get("hour_ms") != null){
                                i.putExtra("hour_ms",e.getLong("hour_ms"));
                            }
                            startActivity(i);

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        files.add(data.getData());
                        filenames.add(data.getData().getLastPathSegment());
                        Spinner spinner = findViewById(R.id.question_files);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Add_test_question.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,filenames);
                        spinner.setAdapter(adapter);


                    }
                }
            });
    public void openFile(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent = Intent.createChooser(intent,"Choose NOW");
        activityResultLauncher.launch(intent);
    }

}