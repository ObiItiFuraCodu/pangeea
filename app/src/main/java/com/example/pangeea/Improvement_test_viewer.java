package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.List;

public class Improvement_test_viewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_improvement_test_viewer);
        Bundle e = getIntent().getExtras();
        LinearLayout layout = findViewById(R.id.linearl_improvement_viewer);
        List<HashMap<String,Object>> question_list = (List<HashMap<String,Object>>) e.get("question_list");
        for(HashMap<String,Object> question : question_list){
               Button button = new Button(this);
               button.setText((String) question.get("prompt"));
               button.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       //TODO : DE FACUT ACTIUNEA DE TRIMITERE LA QUESTION VIEWER
                   }
               });
               layout.addView(button);
        }

    }
}