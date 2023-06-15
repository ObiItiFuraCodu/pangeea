package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Improvement_list extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_improvement_list);
        LinearLayout improvement_layout = findViewById(R.id.linearl_improve_test);
        Bundle e = getIntent().getExtras();
        List<HashMap<String,Object>> improvement_list = (List<HashMap<String,Object>>) e.get("improvement_list");
        for(HashMap<String,Object> improvement_test : improvement_list){
            String title = (String) improvement_test.get("title");
            Button title_button = new Button(this);
            title_button.setText(title);
            title_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(),Improvement_test_viewer.class);
                    i.putExtra("question_list",(ArrayList)improvement_test.get("question_list"));
                    startActivity(i);
                    finish();

                }
            });
            improvement_layout.addView(title_button);

        }
    }
}