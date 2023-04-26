package com.example.pangeea.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        int i = 0;

        for(HashMap<String,Object> map : question_list){
            String result = (String) map.get("answer");
            if(result.equals("wrong")){
                Button button = new Button(Test_result.this);
                button.setText((String) map.get("prompt"));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(map.get("type").equals("A/B/C")){
                            Intent i = new Intent(Test_result.this,Test_Q_explanation.class);
                            i.putExtra("question_content",map);
                            startActivity(i);

                        }
                    }
                });
                mistakes.addView(button);
            }
        }

        String final_mark_string = Integer.toString(e.getInt("final_mark"));
        mark.setText(final_mark_string);

    }
}