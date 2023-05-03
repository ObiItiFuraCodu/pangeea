package com.example.pangeea.other;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pangeea.backend.DatabaseConnector;
import com.example.pangeea.R;

import java.util.ArrayList;
import java.util.List;

public class CSList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cslist);
        ListView list = findViewById(R.id.list_view);
        DatabaseConnector connector = new DatabaseConnector(this);

        List<String> materii = new ArrayList<>();
        materii.add("Matematica");
        materii.add("Fizica");
        materii.add("Biolgie");
        materii.add("Informatica");
        Bundle e = getIntent().getExtras();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,materii);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                connector.upload_highschool_class_and_category((String) e.get("user_highschool"),"","1",materii.get(position));
                startActivity(new Intent(CSList.this, ControlRoom.class));
                finish();

            }
        });


    }
}