package com.example.pangeea.test;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pangeea.R;
import com.example.pangeea.ai.Test_AI;
import com.example.pangeea.backend.TestBackend;
import com.example.pangeea.other.CustomButtonView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AI_test_generator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_test_generator);
        LinearLayout question_list = findViewById(R.id.question_list);

        List<HashMap<String,Object>> question_array = new ArrayList<>();

        Bundle e = getIntent().getExtras();
        Test_AI ai = new Test_AI(AI_test_generator.this,question_list,e.getString("title"),7);
        List<Uri> files = new ArrayList<>();


        Button good = findViewById(R.id.good);
        Button bad = findViewById(R.id.bad);
        //core.AI_Text(e.getString("title"),result);
        ai.generate_test();
        ///HashMap<String,String> question = (HashMap<String, String>) test.get(5);
        //Log.i("PROMPT",question.get("prompt"));
        //Log.i("ANSWER1",question.get("1"));
        //Log.i("ANSWER2",question.get("2"));
        //Log.i("ANSWER3",question.get("3"));
        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              for(int i = 0;i< 7;i++){
                  HashMap<String,Object> question = new HashMap<>();
                  CustomButtonView button = (CustomButtonView) question_list.getChildAt(i);
                  Button prompt = (Button)button.getChildAt(0);
                  TextView a = (TextView) button.getChildAt(1);
                  TextView b = (TextView) button.getChildAt(2);
                  TextView c = (TextView) button.getChildAt(3);
                  question.put("prompt",prompt.getText().toString());
                  question.put("type","A/B/C");
                  HashMap<String,String> variants = new HashMap<>();
                  variants.put(a.getText().toString(),"valid");
                  variants.put(b.getText().toString(),"valid");
                  variants.put(c.getText().toString(),"invalid");
                  question.put("variants",variants);
                  question_array.add(question);


              }
                TestBackend backend = new TestBackend(AI_test_generator.this);
                backend.add_test(e.getLong("test_ms"),e.getString("class_name"),e.getString("details"),files,e.getString("title"),question_array);
                finish();

            }


        });
        bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question_list.removeAllViews();
                Test_AI ai = new Test_AI(AI_test_generator.this,question_list,e.getString("title"),7);


            }
        });
    }
}