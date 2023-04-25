package com.example.pangeea.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pangeea.R;

import java.util.HashMap;
import java.util.List;

public class Test_result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle e = getIntent().getExtras();
        List<HashMap<String,Object>> question_list = (List<HashMap<String, Object>>) e.get("question_list");
        TextView mark = findViewById(R.id.test_mark);
        LinearLayout mistakes = findViewById(R.id.test_mistakes);
        HashMap<String, Object> final_mark = question_list.get(question_list.size()-1);
        question_list.remove(question_list.size()-1);
        String final_mark_string = (String) final_mark.get("mark");
        mark.setText(final_mark_string);
        for(HashMap<String,Object> map : question_list){
            String result = (String) map.get("answer");
            if(result.equals("wrong")){
                Button button = new Button(Test_result.this);
                button.setText((String) map.get("prompt"));
            }
        }

    }
}