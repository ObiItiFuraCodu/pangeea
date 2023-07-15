package com.example.pangeea.backend;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.pangeea.R;
import com.example.pangeea.ai.AI_core;
import com.example.pangeea.content.Lesson_viewer_nongenerated;
import com.example.pangeea.other.Basic_tools;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentBackend extends DatabaseConnector{
    Context context;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore store = FirebaseFirestore.getInstance();
    final long ONE_HOUR_IN_MILIS = 3600000;
    final long ONE_DAY_IN_MILIS = 86400000;
    Basic_tools tool = new Basic_tools();

    public ContentBackend(Context context) {
        super(context);
        this.context = context;

    }
    public void retrieve_lessons(String grade, LinearLayout linearl, String main_course){
        AI_core core = new AI_core(context);
        store.collection("courses").document(grade).collection(grade)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> list =  queryDocumentSnapshots.getDocuments();
                        if(!(list == null || list.isEmpty())){
                            Log.i("WWQEBEWA","WBWBWBW");
                            list = core.recommender_system(list,main_course);

                            for (DocumentSnapshot document : list) {
                                Button lesson_button = new Button(context);
                                lesson_button.setBackgroundColor(context.getResources().getColor(R.color.binaryblue));
                                lesson_button.setElevation(10f);
                                lesson_button.setText(document.getString("title"));
                                lesson_button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(context, Lesson_viewer_nongenerated.class);
                                        i.putExtra("grade",grade);
                                        i.putExtra("title",document.getString("title"));
                                        context.startActivity(i);
                                    }
                                });
                                lesson_button.setElevation(10f);
                                linearl.addView(lesson_button);
                            }
                        }

                    }
                });
    }
}
