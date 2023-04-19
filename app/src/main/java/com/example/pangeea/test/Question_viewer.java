package com.example.pangeea.test;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pangeea.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Question_viewer extends AppCompatActivity {
    HashMap<String,Object> question = new HashMap<>();
    HashMap<String,Object> answer_map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_viewer);
        FirebaseFirestore store = FirebaseFirestore.getInstance();
        answer_map.put("type","to_be_corrected");
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        TextView q_prompt = findViewById(R.id.question_prompt);
        TextView q_files = findViewById(R.id.q_files);
        Button upload_q_file = findViewById(R.id.upload_q_file);
        Button upload_question = findViewById(R.id.upload_question);

        Bundle e = getIntent().getExtras();
        question = (HashMap<String,Object>)e.get("question");
        q_prompt.setText((String)question.get("prompt"));
        q_files.setText("see files");
        q_files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        upload_q_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });
        upload_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle e = getIntent().getExtras();
                List<HashMap<String,Object>> answer_list = (List<HashMap<String, Object>>) e.get("answer_list");
                answer_list.add(answer_map);
                Intent i = new Intent(Question_viewer.this,Test_viewer_elev.class);
                i.putExtra("answer_list", (Serializable) answer_list);
                i.putExtra("hour_ms",e.getString("hour_ms"));
                i.putExtra("teacher",e.getString("teacher"));
                startActivity(i);

            }
        });









    }
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(question.get("files") == null){
                            List<Uri> files = new ArrayList<>();
                            List<String> filenames = new ArrayList<>();
                            files.add(data.getData());
                            filenames.add(data.getData().getLastPathSegment());
                            answer_map.put("filenames",filenames);
                            answer_map.put("files",files);


                        }else{
                            List<Uri> files = (List<Uri>) question.get("files");
                            List<String> filenames = (List<String>) question.get("filenames");
                            files.add(data.getData());
                            filenames.add(data.getData().getLastPathSegment());
                            answer_map.put("filenames",filenames);
                            answer_map.put("files",files);

                        }



                    }
                }
            });
    public void openFile(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent = Intent.createChooser(intent,"Choose NOW");
        activityResultLauncher.launch(intent);
    }
}