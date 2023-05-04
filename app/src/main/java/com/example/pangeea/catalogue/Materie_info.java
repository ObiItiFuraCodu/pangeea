package com.example.pangeea.catalogue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.pangeea.content.Improve_performance;
import com.example.pangeea.backend.CatalogueBackend;
import com.example.pangeea.backend.ContentBackend;
import com.example.pangeea.R;

public class Materie_info extends AppCompatActivity {
    ContentBackend connector = new ContentBackend(this);
    CatalogueBackend backend = new CatalogueBackend(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materie_info);

    }
    @Override
    protected void onStart() {
        super.onStart();
        TextView absences = findViewById(R.id.absences2);
        TextView avg_mark = findViewById(R.id.avg_mark2);
        backend.retrieve_pupil_info_elev(findViewById(R.id.mark_list),findViewById(R.id.absence_list),findViewById(R.id.avg_mark2),findViewById(R.id.absences2),getIntent().getExtras().getString("materie_name"));
        TextView do_better = findViewById(R.id.improve_performance);
        do_better.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Materie_info.this, Improve_performance.class));
                finish();
            }
        });
    }
}