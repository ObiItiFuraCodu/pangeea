package com.example.pangeea;


import android.content.Context;
import android.content.Intent;
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
        FirebaseUser user = auth.getCurrentUser();
        user_data.put("Username",user.getDisplayName());
        user_data.put("user_highschool",user_highschool.replaceAll("[^A-Za-z0-9]", ""));
        user_data.put("user_class",user_class.replaceAll("[^A-Za-z0-9]", ""));
        user_data.put("user_category",category.replaceAll("[^A-Za-z0-9]", ""));
        user_data.put("user_subject",class_subject.replaceAll("[^A-Za-z0-9]", ""));

        store.collection("users").document(user.getDisplayName())
                .set(user_data);
        if(category.replaceAll("[^A-Za-z0-9]", "").equals("1")){
            store.collection("highschools").document(user_highschool.replaceAll("[^A-Za-z0-9]", "")).collection("teachers").document(user.getDisplayName())
                    .set(user_data);
        }else{
            store.collection("highschools").document(user_highschool.replaceAll("[^A-Za-z0-9]", "")).collection("classes").document(user_class.replaceAll("[^A-Za-z0-9]", "")).collection("pupils").document(user.getDisplayName())
                    .set(user_data);

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

    public void import_hours(LinearLayout layout){
        FirebaseUser user = auth.getCurrentUser();
        FirebaseDatabase dbb = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");

        Log.i("ATENTIE FRAIERE",user.getDisplayName());

        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //e.printStackTrace();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.i("ATENTIE FRAIERE O MERS",user.getDisplayName());
                        DatabaseReference ref;

                       String user_highschool = documentSnapshot.get("user_highschool",String.class);
                       String  user_class = documentSnapshot.get("user_class",String.class);
                       String user_category = documentSnapshot.get("user_category",String.class);
                       // Log.i("ATENTIE FRAIERE : ",user_highschool.replaceAll("[^A-Za-z0-9]", ""));
                        // Log.i("ATENTIE FRAIERE : ",user_class.replaceAll("[^A-Za-z0-9]", ""));
                        if(user_category.equals("1")){
                            ref = dbb.getReference("hourss").child(user_highschool.replaceAll("[^A-Za-z0-9]", "")).child("teachers").child(user.getDisplayName());

                        }else{
                            ref = dbb.getReference("hourss").child(user_highschool.replaceAll("[^A-Za-z0-9]", "")).child("classes").child(user_class.replaceAll("[^A-Za-z0-9]", ""));

                        }
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Button v = new Button(context);
                                v.setText(snapshot.getValue(String.class));
                                int hour_milisecs = 10;
                                v.setWidth(100);
                                v.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View c) {
                                        Intent i = new Intent(c.getContext(),Class_info.class);
                                        if(user_category.equals("1"))
                                            i.putExtra("classname",v.getText().toString());


                                        context.startActivity(i);
                                    }
                                });
                                if(hour_milisecs < System.currentTimeMillis()){
                                    layout.addView(v);

                                }



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                      /*  ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int hour = snapshot.getValue(Integer.class);
                                if(hour < System.currentTimeMillis()){snapshot.getRef().removeValue();}
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });*/


                    }
                }
                );
    }
    public void add_hour(int hour_ms,String class_name){
        FirebaseUser user = auth.getCurrentUser();
        FirebaseDatabase dbb = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference ref = dbb.getReference("hourss");
        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        ref.child("hourss").child((String)documentSnapshot.get("user_highschool")).child("classes").child(class_name).child((String)documentSnapshot.get("user_subject")).child(Integer.toString(hour_ms)).setValue(hour_ms);
                        ref.child("hourss").child( (String)documentSnapshot.get("user_highschool")).child("teachers").child(user.getDisplayName()).child(class_name).child(Integer.toString(hour_ms)).setValue(hour_ms);
                    }
                });




    }
    public void retrieve_hour_data(String class_subject,TextView class_status){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {



                        DatabaseReference database_reference = database.getReference("hourss").child((String)documentSnapshot.get("user_highschool")).child((String)documentSnapshot.get("user_class")).child(class_subject);
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

    }
    public void retrieve_class_info(String class_selected,LinearLayout pupil_list){
        FirebaseUser user = auth.getCurrentUser();

        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_selected).collection("pupils")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for(int i = 0;i< queryDocumentSnapshots.size();i++){
                                            Button button = new Button(context);
                                            button.setText(queryDocumentSnapshots.getDocuments().get(i).get("Username",String.class));
                                            pupil_list.addView(button);
                                        }
                                    }
                                });

                    }
                });


    }
    public void retrieveclasses(Spinner spinner){
        List<String> list = new ArrayList<String>();
        FirebaseUser user = auth.getCurrentUser();
        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes")
                                        .get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        for(int i = 0;i < queryDocumentSnapshots.size();i++){
                                                            list.add(queryDocumentSnapshots.getDocuments().get(i).getId());
                                                        }
                                                    }
                                                });

                        spinner.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,list));
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Intent i = new Intent(context,Class_info.class);
                                i.putExtra("class_selected",list.get(position));
                                context.startActivity(i);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                    }
                });


    }
}
