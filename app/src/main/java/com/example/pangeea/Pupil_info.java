package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Pupil_info extends AppCompatActivity {
    DatabaseConnector connector = new DatabaseConnector(this);
    Bundle e = getIntent().getExtras();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pupil_info);
        Button button = findViewById(R.id.add_mark_1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Pupil_info.this,Add_mark.class);
                i.putExtra("class",e.getString("pupil_class"));
                i.putExtra("name",e.getString("pupil_name"));
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        connector.retrieve_pupil_info(e.getString("pupil_name"),e.getString("pupil_class"),findViewById(R.id.mark_list),findViewById(R.id.absence_list),findViewById(R.id.avg_mark),findViewById(R.id.absences));

    }
}