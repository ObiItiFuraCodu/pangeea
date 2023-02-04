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
    String hs,clas;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore store = FirebaseFirestore.getInstance();


    public DatabaseConnector(Context context) {
        this.context = context;
    }

    public void createuser(String username,String password,String email,String highschool,String classs){
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
                            userdat.put("clas",classs);
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
    public void imporths(Spinner spinner,String[] hstext,Spinner spinner2){
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
                                ArrayList<String> classlist =(ArrayList<String>)document.getData().get("Classes");
                                hs.add(text);
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,hs);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(adapter);
                                spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        TextView viewe = (TextView)parent.getItemAtPosition(position);
                                        hstext[0] = viewe.getText().toString();
                                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,classlist);
                                        spinner2.setAdapter(adapter2);
                                        spinner2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                TextView view2 = (TextView)parent.getItemAtPosition(position);
                                                hstext[1] = view2.getText().toString();
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
    public String[] getuserdata(String hourss, LinearLayout linear){
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

    public void import_hours(LinearLayout layout){
        FirebaseUser user = auth.getCurrentUser();

        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        hs = documentSnapshot.get("Hs",String.class);
                        clas = documentSnapshot.get("clas",String.class);
                    }
                });

        FirebaseDatabase dbb = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference ref = dbb.getReference("hourss").child(hs).child(clas);
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
        ref.child(liceu).child(materie).child(classs).setValue(orams);


    }
}
