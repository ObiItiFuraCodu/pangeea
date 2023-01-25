package com.example.pangeea;


import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseConnector {
    Context context;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore store = FirebaseFirestore.getInstance();


    public DatabaseConnector(Context context) {
        this.context = context;
    }

    public void createuser(String username,String password,String email,String highschool){
        HashMap<String,String>userdat = new HashMap<>();
        final boolean[] logged = {false};


        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();

                            userdat.put("Username",username);
                            userdat.put("Hs",highschool);
                            store.collection("users").document(username)
                                    .set(userdat);
                            Toast.makeText(context,"User created successfully",Toast.LENGTH_SHORT).show();

                            System.out.println(logged[0]);



                        }else{
                           // Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(context,task.getException().getMessage(),Toast.LENGTH_SHORT).show();


                        }
                    }
                });



    }
    public boolean login(String email,String password){
        final boolean[] log = new boolean[1];
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            log[0] = true;

                        }else{
                            log[0] = false;

                        }
                    }
                });
        return log[0];
    }
    public void imporths(LinearLayout linear,final String[] hstext){
         ArrayList<String> hs = new ArrayList<String>();

        store.collection("highschools")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                               // TextView view2 = new TextView(NewAccount.this);
                                String text = document.getData().get("Name").toString();

                               // Log.d(TAG,s);
                                TextView view = new TextView(context);
                                view.setText(text);
                                Log.d(TAG,text);
                                view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        hstext[0] = text;
                                    }
                                });
                                try {
                                    linear.addView(view);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public String[] getuserdata(){
        String[] userdata = new String[3];
        FirebaseUser user = auth.getCurrentUser();
        userdata[0] = user.getDisplayName();

        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userdata[1] = (String)documentSnapshot.get("Hs");

                    }
                });
    return userdata;
    }
}
