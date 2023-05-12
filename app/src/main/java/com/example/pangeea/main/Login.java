package com.example.pangeea.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pangeea.Awaiter;
import com.example.pangeea.backend.DatabaseConnector;
import com.example.pangeea.R;
import com.example.pangeea.other.ControlRoom;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button = findViewById(R.id.button2);
        EditText email = findViewById(R.id.email2);
        EditText password = findViewById(R.id.password2);

        mAuth = FirebaseAuth.getInstance();
        DatabaseConnector conn = new DatabaseConnector(this);
        TextView create_account = findViewById(R.id.createaccount);
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, NewAccount.class));
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                       .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                           @Override
                           public void onSuccess(AuthResult authResult) {


                               startActivity(new Intent(v.getContext(), MainActivity.class));
                               finish();

                           }
                       });

            }
        });
        TextView await = findViewById(R.id.awaiter_text);
        await.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Awaiter.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i = new Intent(Login.this, ControlRoom.class);
            startActivity(i);
            finish();
        }


    }
}