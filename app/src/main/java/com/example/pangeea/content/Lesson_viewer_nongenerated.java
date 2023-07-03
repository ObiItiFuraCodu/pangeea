package com.example.pangeea.content;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pangeea.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Lesson_viewer_nongenerated extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_viewer_nongenerated);
        FirebaseFirestore store = FirebaseFirestore.getInstance();
        TextView lesson_content = findViewById(R.id.lesson_content);
        LinearLayout lesson_files = findViewById(R.id.lesson_files);
        StorageReference storage_ref = FirebaseStorage.getInstance().getReference();
        Bundle e = getIntent().getExtras();
        store.collection("courses").document(e.getString("grade")).collection(e.getString("grade")).document(e.getString("title"))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.contains("content")){
                            lesson_content.setText(documentSnapshot.getString("content"));

                        }
                        List<String> filenames = documentSnapshot.get("files", ArrayList.class);
                        for(String file : filenames){
                            Button file_button = new Button(Lesson_viewer_nongenerated.this);
                            file_button.setText(file);
                            file_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    storage_ref.child("lessons/" + file).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            });
                        }
                    }
                });



    }
}