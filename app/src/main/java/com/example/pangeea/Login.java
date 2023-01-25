package com.example.pangeea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(conn.login(email.getText().toString(),password.getText().toString())){
                    startActivity(new Intent(v.getContext(),MainActivity.class));
                }else{
                    Toast.makeText(v.getContext(),"Invalid username or password",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}