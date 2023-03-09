package com.example.pangeea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button = findViewById(R.id.button2);
        EditText email = findViewById(R.id.email2);
        EditText password = findViewById(R.id.password2);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        mAuth = FirebaseAuth.getInstance();
        DatabaseConnector conn = new DatabaseConnector(this);
        TextView create_account = findViewById(R.id.createaccount);
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,NewAccount.class));
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                       .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                           @Override
                           public void onSuccess(AuthResult authResult) {
                               FirebaseFirestore store = FirebaseFirestore.getInstance();
                               store.collection("users").document(mAuth.getCurrentUser().getDisplayName())
                                               .get()
                                                       .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                           @Override
                                                           public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                               editor.putBoolean("logged_in",true);
                                                               editor.putString("user_class",documentSnapshot.get("user_class").toString());
                                                               editor.commit();
                                                           }
                                                       });

                               startActivity(new Intent(v.getContext(),MainActivity.class));

                           }
                       });

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i = new Intent(Login.this,ControlRoom.class);
            startActivity(i);
        }


    }
}