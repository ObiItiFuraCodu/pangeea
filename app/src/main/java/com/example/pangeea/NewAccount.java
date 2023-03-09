package com.example.pangeea;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.service.controls.Control;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                   Intent i = new Intent(v.getContext(),NFC_detection.class);
                   i.putExtra("login","yes");
                   startActivity(i);
               }



            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();

      FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
           Intent i = new Intent(NewAccount.this,ControlRoom.class);
          startActivity(i);
        }


    }
}