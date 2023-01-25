package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseDatabase dbb = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView username = findViewById(R.id.Username);
        TextView hs = findViewById(R.id.hs);
        LinearLayout linear = findViewById(R.id.liner);
        DatabaseReference ref = dbb.getReference("hourss");
        ref.setValue("yey");



        Button butt = new Button(MainActivity.this);
        butt.setWidth(100);
        linear.addView(butt);


    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}