package com.example.pangeea.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pangeea.R;
import com.example.pangeea.ai.Test_AI;
import com.example.pangeea.backend.TestBackend;
import com.example.pangeea.CustomElements.CustomButtonView;
import com.example.pangeea.main.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AI_test_generator extends AppCompatActivity {

    ////int redColor = ContextCompat.getColor(this, R.color.red);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_test_generator);
        LinearLayout question_list = findViewById(R.id.question_list);

        List<HashMap<String,Object>> question_array = new ArrayList<>();

        Bundle e = getIntent().getExtras();
        Test_AI ai = new Test_AI(AI_test_generator.this,question_list,e.getString("title"),10);
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
                for(int i = 0;i< 10;i++){
                    HashMap<String,Object> question = new HashMap<>();
                    CustomButtonView custom_button = (CustomButtonView) question_list.getChildAt(i);
                    LinearLayout button = (LinearLayout) custom_button.getChildAt(0);
                    Button prompt = (Button)button.getChildAt(0);
                    TextView a = (TextView) button.getChildAt(1);
                    TextView b = (TextView) button.getChildAt(2);
                    TextView c = (TextView) button.getChildAt(3);
                    question.put("prompt",prompt.getText().toString());
                    question.put("type","A/B/C");
                    HashMap<String,String> variants = new HashMap<>();
                    variants.put("A",a.getText().toString());
                    if (a.getBackground() instanceof ColorDrawable) {
                        int backgroundColor = ((ColorDrawable) a.getBackground()).getColor();
                        if (backgroundColor == getResources().getColor(R.color.green)) {
                            variants.put("A_valid", "valid");
                        } else {
                            variants.put("A_valid", "invalid");
                        }
                    } else {
                        // Handle the case where the background is not a solid color
                        variants.put("A_valid", "invalid");
                    }
                    variants.put("B",b.getText().toString());

                    if (b.getBackground() instanceof ColorDrawable) {
                        int backgroundColor = ((ColorDrawable) b.getBackground()).getColor();
                        if (backgroundColor == getResources().getColor(R.color.green)) {
                            variants.put("B_valid", "valid");
                        } else {
                            variants.put("B_valid", "invalid");
                        }
                    } else {
                        // Handle the case where the background is not a solid color
                        variants.put("B_valid", "invalid");
                    }
                    variants.put("C",c.getText().toString());
                    if (c.getBackground() instanceof ColorDrawable) {
                        int backgroundColor = ((ColorDrawable) c.getBackground()).getColor();
                        if (backgroundColor == getResources().getColor(R.color.green)) {
                            variants.put("C_valid", "valid");
                        } else {
                            variants.put("C_valid", "invalid");
                        }
                    } else {
                        // Handle the case where the background is not a solid color
                        variants.put("C_valid", "invalid");
                    }
                    question.put("variants",variants);
                    question_array.add(question);


                }
                TestBackend backend = new TestBackend(AI_test_generator.this);
                backend.add_test(e.getLong("hour_ms"),e.getString("class_name"),e.getString("details"),files,e.getString("title"),question_array,null,false);
                startActivity(new Intent(AI_test_generator.this, MainActivity.class));
                finish();

            }


        });
        bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question_list.removeAllViews();
                Test_AI ai = new Test_AI(AI_test_generator.this,question_list,e.getString("title"),7);
                ai.generate_test();




            }
        });
    }
}