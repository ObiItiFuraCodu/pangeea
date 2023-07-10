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
import android.widget.ListView;
import android.widget.TextView;

import com.example.pangeea.ai.AI_core;
import com.example.pangeea.hour.Add_hour;
import com.example.pangeea.main.MainActivity;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class Generate_ai_lesson_teacher extends AppCompatActivity {
    AI_core core = new AI_core(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<String> keyword_list = new ArrayList<>();
        setContentView(R.layout.activity_generate_ai_lesson_teacher);
        ListView keywords = findViewById(R.id.list_view_keywords);
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
                keyword_writer.setText("");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,keyword_list);
                keywords.setAdapter(adapter);
                keywords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Delete keyword?")
                                .setMessage("Vrei sa stergi keywordu?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        keyword_list.remove(position);
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,keyword_list);
                                        keywords.setAdapter(adapter);
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
            }
        });
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                core.AI_lesson_teacher(e.getString("title"),result,keyword_list);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Generate_ai_lesson_teacher.this,Add_hour.class);
                i.putExtra("hour/task",e.getString("hour/task"));
                i.putExtra("class_selected",e.getString("class_selected"));
                i.putExtra("lesson_content",result.getText().toString());
                startActivity(i);

            }
        });



    }
}