package com.example.pangeea.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.pangeea.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class See_corrected_tests extends AppCompatActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore store = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_corrected_tests);


    }

    @Override
    protected void onStart() {
        super.onStart();
        LinearLayout linearl = findViewById(R.id.linearl);
        store.collection("users").document(auth.getCurrentUser().getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        store.collection("highschools").document(documentSnapshot.getString("user_highschool")).collection("classes").document(documentSnapshot.getString("user_class")).collection("pupils").document(documentSnapshot.getString("Username")).collection("tests")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){
                                            Button button = new Button(See_corrected_tests.this);
                                            button.setText(document.getId());
                                            button.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    List<HashMap<String,Object>> question_list = (List<HashMap<String, Object>>) document.get("answers");
                                                    HashMap<String,Object> final_mark = (HashMap<String, Object>) document.get("mark");
                                                    long final_mark_long = (long)final_mark.get("mark");
                                                    int final_mark_int = (int)final_mark_long;
                                                    Intent i = new Intent(v.getContext(),Test_result.class);
                                                    i.putExtra("question_list", (Serializable) question_list);
                                                    i.putExtra("final_mark",final_mark_int);
                                                    startActivity(i);
                                                    finish();


                                                }
                                            });
                                            linearl.addView(button);
                                        }
                                    }
                                });
                    }
                });
    }
}