package com.example.pangeea.content;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.pangeea.R;
import com.example.pangeea.ai.AI_core;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Improve_performance extends AppCompatActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore store = FirebaseFirestore.getInstance();
    private FirebaseDatabase db = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_improve_performance);


    }

    @Override
    protected void onStart() {
        super.onStart();
        AI_core core = new AI_core(Improve_performance.this);
        LinearLayout generated = findViewById(R.id.lessons_generated);
        LinearLayout sorted = findViewById(R.id.lessons_sorted);
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
                                       for(DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                                           HashMap<String,Object> mark = (HashMap<String, Object>) snapshot.get("mark");
                                           long mark_long = (long)mark.get("mark");
                                           int mark_int = (int)mark_long;
                                           if(mark_int < 5){
                                               //SORTED BEGIN
                                               store.collection("courses").document(documentSnapshot.getString("user_class").replaceAll("[^\\d.]", "")).collection(documentSnapshot.getString("user_class").replaceAll("[^\\d.]", ""))
                                                       .get()
                                                       .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                           @Override
                                                           public void onSuccess(QuerySnapshot queryDocumentSnapshots1) {
                                                               Log.i("SSADASDSA",snapshot.getId());
                                                               String title = (String) snapshot.get("title");
                                                               List<DocumentSnapshot> list = core.recommender_system(queryDocumentSnapshots1.getDocuments(),title);
                                                               for(DocumentSnapshot snapshot2 : list){
                                                                   Button button = new Button(Improve_performance.this);
                                                                   button.setText(snapshot2.getString("title"));
                                                                   sorted.addView(button);

                                                               }

                                                           }
                                                       });
                                               //SORTED END
                                               //GENERATED BEGIN
                                               String title = (String) snapshot.get("title");
                                               List<String> wrong_answers = new ArrayList<>();
                                               List<HashMap<String,Object>> answers = (List<HashMap<String, Object>>) snapshot.get("answers");
                                               for(HashMap<String,Object> answer : answers){
                                                   if(answer.get("answer").equals("wrong")){
                                                       String prompt = (String) answer.get("prompt");
                                                       wrong_answers.add(prompt);

                                                   }
                                               }
                                               Button button = new Button(Improve_performance.this);
                                               button.setText(title);
                                               button.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       Intent i = new Intent(Improve_performance.this, Lesson_viewer.class);
                                                       i.putExtra("title",title);
                                                       i.putExtra("answer_list", (Serializable) wrong_answers);
                                                       startActivity(i);
                                                       finish();
                                                   }
                                               });
                                               generated.addView(button);



                                           }

                                       }
                                    }
                                });
                    }
                });

    }
}