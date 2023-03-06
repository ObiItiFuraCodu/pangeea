package com.example.pangeea;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
                            private Object String;

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                               Map<String,Map<String,String>> map =  (Map<String,Map<String,String>>)snapshot.getValue();
                               if(map != null){

                                   for(Map.Entry<String,Map<String,String>> set :
                                           map.entrySet()){
                                       Button v = new Button(context);
                                      Map<String,String> value = (Map<java.lang.String, java.lang.String>)set.getValue();

                                       v.setText(value.get("class_name"));
                                       v.setWidth(100);
                                       int hour_milisecs =  Integer.parseInt(set.getKey().toString());
                                       Log.i("SYSTEMAMSA",Integer.toString((int)System.currentTimeMillis()));
                                       Log.i("HOURMILIS",Integer.toString(hour_milisecs));
                                       v.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View c) {
                                               Intent i = new Intent(c.getContext(),Class_info.class);
                                               if(user_category.equals("1"))
                                                   i.putExtra("classname",v.getText().toString());
                                                   i.putExtra("hour_milis",set.getKey().toString());


                                               context.startActivity(i);
                                           }
                                       });
                                      // if(hour_milisecs > System.currentTimeMillis()){
                                           layout.addView(v);

                                      // }else{
                                          // snapshot.getRef().removeValue();
                                      // }
                                   }


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
    public void add_hour(int hour_ms, String class_name, String details, List<Uri> files){
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
                        for(int i = 1;i<=files.size();i++){
                            storage_ref.child("lessons/" + files.get(i).getLastPathSegment())
                                    .putFile(files.get(i))
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i("TAG","n-a mers fra");
                                        }
                                    });
                            filenames.add(files.get(i).getLastPathSegment());


                        }
                        Map<String,Object> map = new HashMap<>();
                        map.put("details",details);
                        map.put("user_subject",(String)documentSnapshot.get("user_subject"));
                        map.put("files",files);
                        Map<String,Object> map2 = new HashMap<>();
                        map2.put("details",details);
                        map2.put("class_name",class_name);



                        map2.put("files",filenames);
                        ref.child((String)documentSnapshot.get("user_highschool")).child("classes").child(class_name).child(Integer.toString(hour_ms)).setValue(map);
                        ref.child((String)documentSnapshot.get("user_highschool")).child("teachers").child(user.getDisplayName()).child(Integer.toString(hour_ms)).setValue(map2);
                    }
                });




    }
    public void add_task(int test_ms,String class_name,String details, List<Uri> files){
        FirebaseUser user = auth.getCurrentUser();
        FirebaseDatabase dbb = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference ref = dbb.getReference("tasks");
        List<String> filenames  = new ArrayList<>();
        StorageReference storage_ref = FirebaseStorage.getInstance().getReference();

        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String,Object> map = new HashMap<>();
                        for(int i = 1;i<=files.size();i++){
                            storage_ref.child("lessons/" + files.get(i).getLastPathSegment())
                                    .putFile(files.get(i))
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i("TAG","n-a mers fra");
                                        }
                                    });
                            filenames.add(files.get(i).getLastPathSegment());


                        }
                        map.put("details",details);
                        map.put("user_subject",(String)documentSnapshot.get("user_subject"));
                        map.put("files",files);
                        Map<String,Object> map2 = new HashMap<>();
                        map2.put("details",details);
                        map2.put("class_name",class_name);
                        map2.put("files",files);

                        ref.child((String)documentSnapshot.get("user_highschool")).child("classes").child(class_name).child(Integer.toString(test_ms)).setValue(map);
                        ref.child((String)documentSnapshot.get("user_highschool")).child("teachers").child(user.getDisplayName()).child(Integer.toString(test_ms)).setValue(map2);
                    }
                });




    }
    public void import_tasks(LinearLayout layout){
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
                                                  ref = dbb.getReference("tasks").child(user_highschool.replaceAll("[^A-Za-z0-9]", "")).child("teachers").child(user.getDisplayName());

                                              }else{
                                                  ref = dbb.getReference("tasks").child(user_highschool.replaceAll("[^A-Za-z0-9]", "")).child("classes").child(user_class.replaceAll("[^A-Za-z0-9]", ""));

                                              }
                                              ref.addValueEventListener(new ValueEventListener() {
                                                  @Override
                                                  public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                      Map<String,Map<String,String>> map =  (Map<String,Map<String,String>>)snapshot.getValue();
                                                      if(map != null){

                                                          for(Map.Entry<String,Map<String,String>> set :
                                                                  map.entrySet()){
                                                              Button v = new Button(context);
                                                              Map<String,String> value = (Map<java.lang.String, java.lang.String>)set.getValue();

                                                              v.setText(value.get("class_name"));
                                                              v.setWidth(100);
                                                              int hour_milisecs =  Integer.parseInt(set.getKey().toString());
                                                              Log.i("SYSTEMAMSA",Integer.toString((int)System.currentTimeMillis()));
                                                              Log.i("HOURMILIS",Integer.toString(hour_milisecs));
                                                              v.setOnClickListener(new View.OnClickListener() {
                                                                  @Override
                                                                  public void onClick(View c) {
                                                                      Intent i = new Intent(c.getContext(),Task_info.class);
                                                                      if(user_category.equals("1"))
                                                                          i.putExtra("classname",v.getText().toString());
                                                                          i.putExtra("hour_milis",set.getKey().toString());


                                                                      context.startActivity(i);
                                                                  }
                                                              });
                                                              // if(hour_milisecs > System.currentTimeMillis()){
                                                              layout.addView(v);

                                                              // }else{
                                                              // snapshot.getRef().removeValue();
                                                              // }
                                                          }


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

    public void retrieve_hour_data(String hour_milis,TextView class_info){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DatabaseReference database_reference;

                            if(documentSnapshot.get("category",String.class).equals("1")){
                                database_reference = database.getReference("hourss").child((String)documentSnapshot.get("user_highschool")).child("teachers").child((String)documentSnapshot.get("Username")).child(hour_milis);
                            }else{
                                database_reference = database.getReference("hourss").child((String)documentSnapshot.get("user_highschool")).child("classes").child((String)documentSnapshot.get("user_class")).child(hour_milis);

                            }
                        database_reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Map<String,String> map =  (Map<String,String>)snapshot.getValue();
                                if(map != null){



                                        Log.i("SYSTEMAMSA",Integer.toString((int)System.currentTimeMillis()));


                                        // if(hour_milisecs > System.currentTimeMillis()){


                                        // }else{
                                        // snapshot.getRef().removeValue();
                                        // }
                                    class_info.setText(map.get("details"));
                                }





                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                });

    }
    public void retrieve_task_data(String hour_milis,TextView class_status){
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
        list.add("");
        FirebaseUser user = auth.getCurrentUser();
        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.i("USER_HIGHSCHOOL",documentSnapshot.get("user_highschool",String.class));
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes")
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            Log.i("QUERY SIZE",Integer.toString(queryDocumentSnapshots.size()));

                                            for(int i = 0;i < queryDocumentSnapshots.size();i++){

                                                list.add(queryDocumentSnapshots.getDocuments().get(i).getId());
                                                Log.i("CLASE",queryDocumentSnapshots.getDocuments().get(i).getId());
                                            }
                                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,list);

                                            spinner.setAdapter(dataAdapter);
                                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    Intent i = new Intent(context,Class_info.class);
                                                    i.putExtra("class_selected",parent.getItemAtPosition(position).toString());
                                                    if(!parent.getItemAtPosition(position).toString().equals("")){
                                                        context.startActivity(i);

                                                    }

                                                }
                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {

                                                }
                                            });

                                        }
                                    });


                           // spinner.setVisibility(View.INVISIBLE);
                           // spinner.setEnabled(false);




                    }
                });


    }

}
