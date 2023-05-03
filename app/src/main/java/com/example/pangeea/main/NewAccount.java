package com.example.pangeea.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.pangeea.backend.DatabaseConnector;
import com.example.pangeea.other.NFC_detection;
import com.example.pangeea.R;
import com.example.pangeea.other.ControlRoom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewAccount extends AppCompatActivity {
    private FirebaseAuth mAuth;
    DatabaseConnector conn = new DatabaseConnector(this);


    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_account);
        // Initialize Firebase Auth
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Button button = findViewById(R.id.button);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        EditText name = findViewById(R.id.fullname);






        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(conn.createuser(name.getText().toString(),password.getText().toString(),email.getText().toString())){
                   Intent i = new Intent(v.getContext(), NFC_detection.class);
                   i.putExtra("login","yes");
                   startActivity(i);
                   finish();
               }



            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();

      FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
           Intent i = new Intent(NewAccount.this, ControlRoom.class);
          startActivity(i);
          finish();
        }


    }
}