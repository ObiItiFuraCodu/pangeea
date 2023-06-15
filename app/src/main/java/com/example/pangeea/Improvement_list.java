package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

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

                }
            });
            improvement_layout.addView(title_button);

        }
    }
}