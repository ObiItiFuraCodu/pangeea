package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.pangeea.ai.AI_core;
import com.example.pangeea.ai.Test_AI;
import com.example.pangeea.backend.CatalogueBackend;
import com.example.pangeea.test.Question_viewer_ABC;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Improvement_test_viewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_improvement_test_viewer);
        Bundle e = getIntent().getExtras();

        LinearLayout layout = findViewById(R.id.linearl_improvement_viewer);
        List<HashMap<String,Object>> question_list = (List<HashMap<String,Object>>) e.get("question_list");
        FirebaseFirestore store = FirebaseFirestore.getInstance();
        CatalogueBackend backend = new CatalogueBackend(this);

        int question_counter = 0;
        for(HashMap<String,Object> question : question_list){
               Button button = new Button(this);
               button.setText((String) question.get("prompt"));

            int finalQuestion_counter = question_counter;
            button.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent i = new Intent(v.getContext(), Improvement_question_viewer.class);
                       i.putExtra("question",question);
                       i.putExtra("index", finalQuestion_counter);
                       i.putExtra("prompt",(String) question.get("prompt"));
                       i.putExtra("title",e.getString("title"));
                       i.putExtra("question_list", (Serializable) e.get("question_list"));
                       if(e.get("answer_list") == null){
                           ArrayList<Object> answer_list = new ArrayList<>();
                           for (int counter = 0; counter < 12; counter++) {
                               answer_list.add(new HashMap<String,Object>());

                           }
                           i.putExtra("answer_list",answer_list);
                       }else{
                           i.putExtra("answer_list", (Serializable) e.get("answer_list"));
                       }


                       startActivity(i);
                   }

               });
            button.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int mark = 0;
                    List<HashMap<String,Object>> answer_list;
                    if(e.get("answer_list") == null){
                      answer_list = new ArrayList<>();
                    }else{
                       answer_list = (List<HashMap<String, Object>>) e.get("answer_list");
                    }
                    for(HashMap<String,Object> answer : answer_list){
                        String result = (String) answer.get("answer");
                        if(result.equals("correct")){
                            mark++;
                        }

                    }
                    HashMap<String,Object> improvement_map = new HashMap<>();
                    improvement_map.put("mark",mark);
                    improvement_map.put("answer_list",answer_list);

                    int finalMark = mark;
                    store.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                                    .get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    String class_marked = documentSnapshot.getString("user_class");
                                                    backend.upload_mark(class_marked,FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),Integer.toString(finalMark),"nu",e.getString("title"),true);
                                                }
                                            });


                    store.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).collection("ranking_history").document(e.getString("title")).collection("improvement").document("improvement")
                            .set(improvement_map);



                    return false;
                }
            });
               layout.addView(button);
            question_counter++;
        }



    }
}