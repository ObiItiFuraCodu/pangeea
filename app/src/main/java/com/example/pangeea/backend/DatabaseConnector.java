package com.example.pangeea.backend;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.pangeea.ai.AI_core;
import com.example.pangeea.ai.AI_generator;
import com.example.pangeea.catalogue.Materie_info;
import com.example.pangeea.catalogue.Pupil_info;
import com.example.pangeea.content.Lesson_list;
import com.example.pangeea.hour.Hour_info_elev;
import com.example.pangeea.hour.Hour_info_profesor;
import com.example.pangeea.main.Class_info;
import com.example.pangeea.other.Basic_tools;
import com.example.pangeea.other.FileDownloader;
import com.example.pangeea.other.FileViewer;
import com.example.pangeea.other.NFC_detection;
import com.example.pangeea.task.Task_info;
import com.example.pangeea.task.Task_info_proffesor;
import com.example.pangeea.test.Question_viewer;
import com.example.pangeea.test.Test_info_prof;
import com.example.pangeea.test.Test_viewer_elev;
import com.example.pangeea.test.Test_viewer_proffesor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseConnector {
    Context context;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore store = FirebaseFirestore.getInstance();
    final long ONE_HOUR_IN_MILIS = 3600000;
    final long ONE_DAY_IN_MILIS = 86400000;
    Basic_tools tool = new Basic_tools();


    public DatabaseConnector(Context context) {
        this.context = context;
    }

    public boolean createuser(String username,String password,String email){
        final boolean[] created = {true};



        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                            user.updateProfile(profileUpdates);


                        }else{
                           // Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(context,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            created[0] = false;


                        }
                    }
                });

     return created[0];

    }
    public void upload_highschool_class_and_category(String user_highschool,String user_class,String category,String class_subject){
        HashMap<String,String>user_data = new HashMap<>();
        HashMap<String,String>filler_data = new HashMap<>();

        FirebaseUser user = auth.getCurrentUser();
        user_data.put("Username",user.getDisplayName());
        user_data.put("user_highschool",user_highschool.replaceAll("[^A-Za-z0-9]", ""));
        user_data.put("user_class",user_class.replaceAll("[^A-Za-z0-9]", ""));
        user_data.put("user_category",category.replaceAll("[^A-Za-z0-9]", ""));
        user_data.put("user_subject",class_subject.replaceAll("[^A-Za-z0-9]", ""));
        filler_data.put("data",user_highschool.replaceAll("[^A-Za-z0-9]", ""));


        store.collection("users").document(user.getDisplayName())
                .set(user_data);
        store.collection("highschools").document(user_highschool.replaceAll("[^A-Za-z0-9]", ""))
                .set(filler_data);
        if(category.replaceAll("[^A-Za-z0-9]", "").equals("1")){
            store.collection("highschools").document(user_highschool.replaceAll("[^A-Za-z0-9]", "")).collection("teachers").document(user.getDisplayName())
                    .set(user_data);
        }else{
            store.collection("highschools").document(user_highschool.replaceAll("[^A-Za-z0-9]", "")).collection("classes").document(user_class.replaceAll("[^A-Za-z0-9]", "")).collection("pupils").document(user.getDisplayName())
                    .set(user_data);
            store.collection("highschools").document(user_highschool.replaceAll("[^A-Za-z0-9]", "")).collection("classes").document(user_class.replaceAll("[^A-Za-z0-9]", ""))
                    .set(filler_data);


        }
        Toast.makeText(context,"User created successfully",Toast.LENGTH_SHORT).show();
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
  /*  public String[] getuserdata(){
        String[] userdata = new String[4];
        FirebaseUser user = auth.getCurrentUser();
        userdata[0] = user.getDisplayName();

        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userdata[1] = (String)documentSnapshot.get("user_highschool");
                        userdata[2] = (String)documentSnapshot.get("user_class");
                        userdata[3] = (String)documentSnapshot.get("user_category");
                        userdata[4] = (String)documentSnapshot.get("user_subject");

                    }
                });

    return userdata;
    }*/
    //DIS SHIT DONT WORK






   /* public void retrieve_task_data(String hour_milis,TextView class_status){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DatabaseReference database_reference;

                        if(documentSnapshot.get("category",String.class).equals("1")){
                            database_reference = database.getReference("tasks").child((String)documentSnapshot.get("user_highschool")).child("teachers").child((String)documentSnapshot.get("Username")).child(hour_milis);
                        }else{
                            database_reference = database.getReference("tasks").child((String)documentSnapshot.get("user_highschool")).child("classes").child((String)documentSnapshot.get("user_class")).child(hour_milis);

                        }
                        database_reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int time_in_millis = snapshot.getValue(Integer.class);
                                if(time_in_millis < System.currentTimeMillis())class_status.setText("status : active");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                });

    }*/





    public void make_presence(String class_presence,String lesson_class,Long hour_ms,String teacher){
        if(!class_presence.equals(lesson_class.replaceAll("[^A-Za-z0-9]", ""))){
            Toast.makeText(context,"Wrong class",Toast.LENGTH_SHORT).show();
            Log.i("ABCDEFG",lesson_class.replaceAll("[^A-Za-z0-9]", ""));
            Log.i("HIJKLMNOPQRSTCUV",Long.toString(hour_ms));

        }else{
            FirebaseUser user = auth.getCurrentUser();
            List<String> filenames  = new ArrayList<>();
            FirebaseDatabase dbb = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
            StorageReference storage_ref = FirebaseStorage.getInstance().getReference();

            DatabaseReference ref = dbb.getReference("hourss");
            store.collection("users").document(user.getDisplayName())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            ref.child((String)documentSnapshot.get("user_highschool")).child("teachers").child(teacher).child(Long.toString(hour_ms)).child("present_list").child(documentSnapshot.getString("Username"))
                                    .setValue("present");
                        }
                    });

        }
        Intent i = new Intent(context,Hour_info_elev.class);
        i.putExtra("hour_milis",Long.toString(hour_ms));
        i.putExtra("presence",false);
        context.startActivity(i);

    }

    public void ask_question(String hour_ms,String teacher){

            FirebaseUser user = auth.getCurrentUser();
            List<String> filenames  = new ArrayList<>();
            FirebaseDatabase dbb = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
            StorageReference storage_ref = FirebaseStorage.getInstance().getReference();

            DatabaseReference ref = dbb.getReference("hourss");
            store.collection("users").document(user.getDisplayName())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            ref.child((String)documentSnapshot.get("user_highschool")).child("teachers").child(teacher).child(hour_ms).child("questions").child(documentSnapshot.getString("Username"))
                                    .setValue("question");
                        }
                    });


    }
    public void submit_work(Uri work,Long hour_ms,String teacher){


            FirebaseUser user = auth.getCurrentUser();
           // List<String> filenames  = new ArrayList<>();
            FirebaseDatabase dbb = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
            StorageReference storage_ref = FirebaseStorage.getInstance().getReference();

            DatabaseReference ref = dbb.getReference("tasks");
            store.collection("users").document(user.getDisplayName())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            ref.child((String)documentSnapshot.get("user_highschool")).child("teachers").child(teacher).child(Long.toString(hour_ms)).child("submissions").child(documentSnapshot.getString("Username"))
                                    .setValue(work.getLastPathSegment());
                            ref.child((String)documentSnapshot.get("user_highschool")).child("classes").child(documentSnapshot.getString("user_class")).child(Long.toString(hour_ms)).child("submissions").child(documentSnapshot.getString("Username"))
                                    .setValue(work.getLastPathSegment());
                            storage_ref.child("lessons/" + work.getLastPathSegment())
                                    .putFile(work);
                        }
                    });


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void mark_absent(String class_marked, List<String> pupils_present){
        FirebaseUser user = auth.getCurrentUser();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for(int i = 0;i< queryDocumentSnapshots.size();i++){
                                            String user_child_name = queryDocumentSnapshots.getDocuments().get(i).get("Username",String.class);
                                            if(!pupils_present.contains(user_child_name)){
                                                store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(user_child_name).collection("absences")
                                                        .document(documentSnapshot.get("user_subject",String.class)).collection("absences").document(now.toString())

                                                        .set("absence");

                                            }
                                        }
                                    }
                                });

                    }
                });
    }
    public void save_file(List<String> file_paths,String class_sent,String course_title){
        FirebaseUser user = auth.getCurrentUser();
        Map<String,Object> map = new HashMap<>();
        map.put("title",course_title);
        map.put("files",file_paths);

        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        store.collection("courses").document(class_sent.replaceAll("[^0-9.]", "")).collection(class_sent.replaceAll("[^0-9.]", "")).document(course_title)
                                .set(map);


                    }
                });
    }




}
