package com.example.pangeea;


import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseConnector {
    Context context;
    String user_highschool,user_class;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore store = FirebaseFirestore.getInstance();


    public DatabaseConnector(Context context) {
        this.context = context;
    }

    public void createuser(String username,String password,String email,String highschool,String selected_class){
        HashMap<String,String>user_data = new HashMap<>();
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

                            user_data.put("Username",username);
                            user_data.put("user_highschool",highschool);
                            user_data.put("user_class",selected_class);
                            store.collection("users").document(username)
                                    .set(user_data);
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
    public void import_highschools_and_classes(Spinner highschool_list,String[] highschool_text,Spinner class_list){
         ArrayList<String> highschool = new ArrayList<String>();

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
                                ArrayList<String> classlist =(ArrayList<String>)document.getData().get("Classes");
                                highschool.add(text);
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,highschool);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                highschool_list.setAdapter(adapter);
                                highschool_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        TextView viewe = (TextView)parent.getItemAtPosition(position);
                                        highschool_text[0] = viewe.getText().toString();
                                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,classlist);
                                        class_list.setAdapter(adapter2);
                                        class_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                TextView view2 = (TextView)parent.getItemAtPosition(position);
                                                highschool_text[1] = view2.getText().toString();
                                            }
                                        });
                                    }
                                });







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
                        userdata[1] = (String)documentSnapshot.get("user_highschool");
                        userdata[2] = (String)documentSnapshot.get("user_class");

                    }
                });
    return userdata;
    }

    public void import_hours(LinearLayout layout){
        FirebaseUser user = auth.getCurrentUser();

        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user_highschool = documentSnapshot.get("Hs",String.class);
                        user_class = documentSnapshot.get("clas",String.class);
                    }
                });

        FirebaseDatabase dbb = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference ref = dbb.getReference("hourss").child(user_highschool).child(user_class);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Button v = new Button(context);
                v.setText(snapshot.getValue(String.class));
                v.setWidth(100);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View c) {
                        Intent i = new Intent(c.getContext(),Class_info.class);
                        i.putExtra("classname",v.getText().toString());
                        context.startActivity(i);
                    }
                });
                layout.addView(layout);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int hour = snapshot.getValue(Integer.class);
                if(hour < System.currentTimeMillis()){snapshot.getRef().removeValue();}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    public void add_hour(String materie,String dbref,String classs,String liceu,int orams){

        FirebaseDatabase dbb = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference ref = dbb.getReference(dbref);
        ref.child(liceu).child(classs).child(materie).setValue(orams);


    }
    public void retrieve_hour_data(String class_subject,TextView class_status){
        String[] userdata = getuserdata();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference database_reference = database.getReference("hourss").child(userdata[1]).child(userdata[2]).child(class_subject);
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
}
