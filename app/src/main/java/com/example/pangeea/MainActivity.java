package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    DatabaseConnector connector = new DatabaseConnector(this);
    FirebaseFirestore store = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView username = findViewById(R.id.Username);
        TextView hs = findViewById(R.id.hs);
        String highschool;
        LinearLayout linear = findViewById(R.id.liner);
        connector.import_hours(linear);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}