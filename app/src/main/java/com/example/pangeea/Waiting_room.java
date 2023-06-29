package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pangeea.ai.AI_core;

import java.util.ArrayList;

public class Waiting_room extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

    }

    @Override
    protected void onStart() {
        AI_core core = new AI_core(this);

        Bundle e = getIntent().getExtras();
        ArrayList<Object> question_list = (ArrayList<Object>) e.get("question_list");
        core.AI_complete_test(e.getString("title"),question_list,question_list.size(),1,true,false,null);
        finish();

        super.onStart();
    }
}