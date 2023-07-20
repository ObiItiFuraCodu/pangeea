package com.example.pangeea;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pangeea.ai.AI_core;
import com.example.pangeea.hour.Add_hour;

import java.util.ArrayList;
import java.util.List;

public class Generate_ai_lesson_teacher extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AI_core core = new AI_core(Generate_ai_lesson_teacher.this);
        List<String> keyword_list = new ArrayList<>();
        setContentView(R.layout.activity_generate_ai_lesson_teacher);
        LinearLayout keywords = findViewById(R.id.linear_view_keywords);
        TextView result = findViewById(R.id.text_view_result);
        Button enter_keyword = findViewById(R.id.enter_keyword);
        Button generate = findViewById(R.id.generate_lesson);
        Button finish = findViewById(R.id.lesson_ok);
        EditText keyword_writer = findViewById(R.id.keyword_text);
        Bundle e = getIntent().getExtras();

        enter_keyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword_list.add(keyword_writer.getText().toString());
                Button keyword_button = new Button(v.getContext());
                keyword_button.setText(keyword_writer.getText().toString());
                keyword_writer.setText("");
                keyword_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Delete keyword?")
                                .setMessage(R.string.want_to_delete)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        keyword_list.remove(keyword_button.getText().toString());
                                        keyword_button.setVisibility(View.GONE);
                                        keyword_button.setEnabled(false);
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
                keywords.addView(keyword_button);

            }
        });
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("thinking...");
                core.AI_lesson_teacher(e.getString("title"),result,keyword_list);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Generate_ai_lesson_teacher.this,Add_hour.class);
                i.putExtra("title",e.getString("title"));
                i.putExtra("hour/task",e.getString("hour/task"));
                i.putExtra("class_selected",e.getString("class_selected"));
                i.putExtra("lesson_content",result.getText().toString());
                startActivity(i);
                finish();

            }
        });



    }
}