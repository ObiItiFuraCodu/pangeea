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
                                layout.removeAllViews();

                               Map<String,Map<String,String>> map =  (Map<String,Map<String,String>>)snapshot.getValue();
                               if(map != null){

                                   for(Map.Entry<String,Map<String,String>> set :
                                           map.entrySet()){
                                       Button v = new Button(context);
                                      Map<String,String> value = (Map<java.lang.String, java.lang.String>)set.getValue();

                                       v.setWidth(100);
                                       Long hour_milisecs =  Long.parseLong(set.getKey().toString());
                                       Log.i("SYSTEMAMSA",Integer.toString((int)System.currentTimeMillis()));
                                     //  Log.i("HOURMILIS",Integer.toString(hour_milisecs));
                                       if(user_category.equals("1")){
                                           if(hour_milisecs < (System.currentTimeMillis() + ONE_HOUR_IN_MILIS)){
                                               v.setText(value.get("class_name") + " active now ");
                                           }else{
                                               v.setText(value.get("class_name") + " starts in " + Long.toString ((hour_milisecs - System.currentTimeMillis() - ONE_HOUR_IN_MILIS ) / 3600000) + " hours");

                                           }

                                       }else{
                                           if(hour_milisecs < (System.currentTimeMillis() + ONE_HOUR_IN_MILIS)){
                                               v.setText(value.get("user_subject") + " active now ");
                                           }else{
                                               v.setText(value.get("user_subject") + " starts in " + Long.toString ((hour_milisecs - System.currentTimeMillis() - ONE_HOUR_IN_MILIS ) / 3600000) + " hours");

                                           }
                                       }
                                       v.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View c) {
                                               if(user_category.equals("1")){
                                                   Intent i = new Intent(c.getContext(), Hour_info_profesor.class);
                                                   i.putExtra("classname",v.getText().toString());
                                                   i.putExtra("hour_milis",set.getKey().toString());
                                                   context.startActivity(i);
                                               }else{
                                                   Intent i = new Intent(c.getContext(), Hour_info_elev.class);
                                                   i.putExtra("hour_milis",set.getKey().toString());
                                                   if(tool.hour_is_active(hour_milisecs)){
                                                       i.putExtra("presence",false);

                                                   }else{
                                                       i.putExtra("presence",true);
                                                   }
                                                   context.startActivity(i);

                                               }

                                           }
                                       });
                                      if(hour_milisecs > System.currentTimeMillis() - ONE_DAY_IN_MILIS){
                                           layout.addView(v);

                                      }else{
                                           snapshot.getRef().child(snapshot.getKey()).removeValue();
                                          Log.i("TAFDASRFWSREFAS",Long.toString(System.currentTimeMillis()));
                                       }
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
    public void add_hour(long hour_ms, String class_name, String details, List<Uri> files,String title){
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
                        for(int i = 0;i< files.size();i++){
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
                        save_file(filenames,class_name,title);
                        Map<String,Object> map = new HashMap<>();
                        map.put("details",details);
                        map.put("user_subject",(String)documentSnapshot.get("user_subject"));
                        map.put("files",filenames);
                        map.put("title",title);
                        map.put("teacher",(String)documentSnapshot.get("Username"));
                        Map<String,Object> map2 = new HashMap<>();
                        map2.put("details",details);
                        map2.put("class_name",class_name);
                        map2.put("files",filenames);
                        map.put("title",title);
                        map2.put("teacher",(String)documentSnapshot.get("Username"));
                        ref.child((String)documentSnapshot.get("user_highschool")).child("classes").child(class_name).child(Long.toString(hour_ms + ONE_HOUR_IN_MILIS)).setValue(map);
                        ref.child((String)documentSnapshot.get("user_highschool")).child("teachers").child(user.getDisplayName()).child(Long.toString(hour_ms + ONE_HOUR_IN_MILIS)).setValue(map2);
                        store.collection("courses").document(class_name.replaceAll("[^0-9.]", "")).collection(class_name.replaceAll("[^0-9.]", "")).document(title)
                                .set(map);
                    }
                });




    }
    public void add_automated_hour(long hour_ms, String class_name, String details, String title,String teacher,String highschool){
        FirebaseUser user = auth.getCurrentUser();
        List<String> filenames  = new ArrayList<>();
        FirebaseDatabase dbb = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        StorageReference storage_ref = FirebaseStorage.getInstance().getReference();

        DatabaseReference ref = dbb.getReference("hourss");
                        save_file(filenames,class_name,title);
                        Map<String,Object> map = new HashMap<>();
                        map.put("details",details);
                        map.put("user_subject",title);
                        map.put("files",filenames);
                        map.put("title",title);
                        map.put("teacher",teacher);
                        Map<String,Object> map2 = new HashMap<>();
                        map2.put("details",details);
                        map2.put("class_name",class_name);
                        map2.put("files",filenames);
                        map.put("title",title);
                        map2.put("teacher",teacher);
                        ref.child(highschool).child("classes").child(class_name).child(Long.toString(hour_ms + ONE_HOUR_IN_MILIS)).setValue(map);
                        ref.child(highschool).child("teachers").child(user.getDisplayName()).child(Long.toString(hour_ms + ONE_HOUR_IN_MILIS)).setValue(map2);
    }
    public void add_task(long test_ms,String class_name,String details, List<Uri> files,String title){
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
                        for(int i = 0;i< files.size();i++){
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
                        save_file(filenames,class_name,title);
                        map.put("details",details);
                        map.put("user_subject",(String)documentSnapshot.get("user_subject"));
                        map.put("files",filenames);
                        map.put("title",title);
                        map.put("teacher",(String)documentSnapshot.get("Username"));

                        Map<String,Object> map2 = new HashMap<>();
                        map2.put("details",details);
                        map2.put("class_name",class_name);
                        map2.put("files",filenames);
                        map.put("title",title);
                        map2.put("teacher",(String)documentSnapshot.get("Username"));


                        ref.child((String)documentSnapshot.get("user_highschool")).child("classes").child(class_name).child(Long.toString(test_ms)).setValue(map);
                        ref.child((String)documentSnapshot.get("user_highschool")).child("teachers").child(user.getDisplayName()).child(Long.toString(test_ms )).setValue(map2);
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


                                                              v.setWidth(100);
                                                              Long hour_milisecs =  Long.parseLong(set.getKey().toString());
                                                              Log.i("SYSTEMAMSA",Integer.toString((int)System.currentTimeMillis()));
                                                            //  Log.i("HOURMILIS",Integer.toString(hour_milisecs));
                                                              if(user_category.equals("1")){
                                                                      v.setText(value.get("class_name") + "deadline in : " +  Long.toString ((hour_milisecs - System.currentTimeMillis()) / 3600000) + " hours");
                                                              }else{
                                                                      v.setText(value.get("user_subject") + "deadline in : " +  Long.toString ((hour_milisecs - System.currentTimeMillis()) / 3600000) + " hours");
                                                              }
                                                              v.setOnClickListener(new View.OnClickListener() {
                                                                  @Override
                                                                  public void onClick(View c) {
                                                                      if(!user_category.equals("1")){

                                                                          Intent i = new Intent(c.getContext(), Task_info.class);
                                                                          i.putExtra("classname",v.getText().toString());
                                                                          i.putExtra("hour_milis",set.getKey().toString());
                                                                          context.startActivity(i);

                                                                      }else{

                                                                          Intent i = new Intent(c.getContext(), Task_info_proffesor.class);
                                                                          i.putExtra("classname",v.getText().toString());
                                                                          i.putExtra("hour_milis",set.getKey().toString());
                                                                          context.startActivity(i);
                                                                      }




                                                                  }
                                                              });
                                                              if(true){
                                                              layout.addView(v);

                                                               }else{
                                                                  Log.i("TF","WTFFFFFFFFFFFFFFFFFF");
                                                                  snapshot.getRef().child(snapshot.getKey()).removeValue();
                                                               }
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
    public void add_test(long test_ms,String class_name,String details, List<Uri> files,String title,List<HashMap<String,String>> questions){
        FirebaseUser user = auth.getCurrentUser();
        FirebaseDatabase dbb = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference ref = dbb.getReference("tests");
        List<String> filenames  = new ArrayList<>();
        StorageReference storage_ref = FirebaseStorage.getInstance().getReference();

        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String,Object> map = new HashMap<>();
                        for(int i = 0;i< files.size();i++){
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
                        save_file(filenames,class_name,title);
                        map.put("details",details);
                        map.put("user_subject",(String)documentSnapshot.get("user_subject"));
                        map.put("files",filenames);
                        map.put("title",title);
                        map.put("teacher",(String)documentSnapshot.get("Username"));
                        map.put("questions",questions);

                        Map<String,Object> map2 = new HashMap<>();
                        map2.put("details",details);
                        map2.put("class_name",class_name);
                        map2.put("files",filenames);
                        map.put("title",title);
                        map2.put("teacher",(String)documentSnapshot.get("Username"));
                        map.put("questions",questions);



                        ref.child((String)documentSnapshot.get("user_highschool")).child("classes").child(class_name).child(Long.toString(test_ms)).setValue(map);
                        ref.child((String)documentSnapshot.get("user_highschool")).child("teachers").child(user.getDisplayName()).child(Long.toString(test_ms )).setValue(map2);
                    }
                });

    }
    public void import_tests(LinearLayout layout){
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
                                                  ref = dbb.getReference("tests").child(user_highschool.replaceAll("[^A-Za-z0-9]", "")).child("teachers").child(user.getDisplayName());

                                              }else{
                                                  ref = dbb.getReference("tests").child(user_highschool.replaceAll("[^A-Za-z0-9]", "")).child("classes").child(user_class.replaceAll("[^A-Za-z0-9]", ""));

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


                                                              v.setWidth(100);
                                                              Long hour_milisecs =  Long.parseLong(set.getKey().toString());
                                                              Log.i("SYSTEMAMSA",Integer.toString((int)System.currentTimeMillis()));
                                                              //  Log.i("HOURMILIS",Integer.toString(hour_milisecs));
                                                              if(user_category.equals("1")){
                                                                  v.setText(value.get("class_name") + "begins in : " +  Long.toString ((hour_milisecs - System.currentTimeMillis()) / 3600000) + " hours");
                                                              }else{
                                                                  v.setText(value.get("user_subject") + "begins in : " +  Long.toString ((hour_milisecs - System.currentTimeMillis()) / 3600000) + " hours");
                                                              }
                                                              v.setOnClickListener(new View.OnClickListener() {
                                                                  @Override
                                                                  public void onClick(View c) {
                                                                      if(!user_category.equals("1")){

                                                                          Intent i = new Intent(c.getContext(), Test_info_prof.class);
                                                                          i.putExtra("classname",v.getText().toString());
                                                                          i.putExtra("hour_milis",set.getKey().toString());
                                                                          context.startActivity(i);

                                                                      }else{

                                                                          Intent i = new Intent(c.getContext(), Test_viewer_elev.class);
                                                                          i.putExtra("classname",v.getText().toString());
                                                                          i.putExtra("hour_milis",set.getKey().toString());
                                                                          context.startActivity(i);
                                                                      }




                                                                  }
                                                              });
                                                              if(true){
                                                                  layout.addView(v);

                                                              }else{
                                                                  Log.i("TF","WTFFFFFFFFFFFFFFFFFF");
                                                                  snapshot.getRef().child(snapshot.getKey()).removeValue();
                                                              }
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
    public void retrieve_hour_data_prof(String hour_milis,ListView pupil_present,ListView lessons_sent,ListView question,Button close_presence){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DatabaseReference database_reference;
                        List<String> presence_list = new ArrayList<>();

                        database_reference = database.getReference("hourss").child((String)documentSnapshot.get("user_highschool")).child("teachers").child((String)documentSnapshot.get("Username")).child(hour_milis);

                        database_reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Map<String,Object> map =  (Map<String,Object>)snapshot.getValue();
                                if(map != null){

                                    if((List<String>)map.get("files") != null){
                                        List<String> lessons_list = (List<String>)map.get("files");
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,lessons_list);
                                        lessons_sent.setAdapter(adapter);
                                    }

                                    if(map.get("present_list") != null){
                                        Map<String,String> presence_map = new HashMap<>();
                                        presence_map = (Map<String,String>)map.get("present_list");
                                        presence_list.addAll(presence_map.keySet());
                                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,presence_list);
                                        pupil_present.setAdapter(adapter2);


                                    }
                                    if(map.get("questions") != null){
                                        Map<String,String> keys = (Map<String, String>) map.get("questions");
                                        List<String> questions = new ArrayList<>();
                                               questions.addAll(keys.keySet());
                                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,questions);
                                        question.setAdapter(adapter2);
                                    }
                                    close_presence.setOnClickListener(new View.OnClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                        @Override
                                        public void onClick(View v) {
                                            mark_absent((String) map.get("class_name"),presence_list);
                                        }
                                    });

                                }





                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                });

    }
    public void retrieve_hour_data_elev(String hour_ms, ListView lv, Button questions, boolean presence, TextView ai,TextView lesson_network){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FileDownloader downloader = new FileDownloader();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DatabaseReference database_reference;

                        database_reference = database.getReference("hourss").child((String)documentSnapshot.get("user_highschool")).child("classes").child((String)documentSnapshot.get("user_class")).child(hour_ms);


                        database_reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Map<String,Object> map =  (Map<String,Object>)snapshot.getValue();

                                if(map != null){
                                    if(!presence){
                                        Intent i = new Intent(context, NFC_detection.class);
                                        i.putExtra("teacher",map.get("teacher").toString());
                                        i.putExtra("hour_ms",hour_ms);
                                        i.putExtra("user_class",documentSnapshot.get("user_class").toString());
                                        i.putExtra("presence",presence);
                                        context.startActivity(i);
                                    }


                                    questions.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ask_question(hour_ms,(String)map.get("teacher"));
                                        }
                                    });
                                    if((List<String>)map.get("files") != null){
                                        List<String> lessons_list = (List<String>)map.get("files");
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,lessons_list);
                                        lv.setAdapter(adapter);
                                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                try{
                                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                                    StorageReference storageRef = storage.getReference();
                                                    storageRef.child("lessons/" + lessons_list.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                            context.startActivity(intent);
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception exception) {
                                                            // Handle any errors
                                                        }
                                                    });
                                                }catch(Exception e){
                                                    Intent i = new Intent(context, FileViewer.class);
                                                    i.putExtra("lesson_name",lessons_list.get(position));
                                                    context.startActivity(i);
                                                }
                                            }
                                        });

                                    }



                                }



                                lesson_network.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(context, Lesson_list.class);
                                        i.putExtra("grade",(String)((String) documentSnapshot.get("user_class")).replaceAll("[^0-9.]", ""));
                                        i.putExtra("main_course",map.get("title").toString());
                                        context.startActivity(i);

                                    }
                                });


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        ai.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                context.startActivity(new Intent(context, AI_generator.class));
                            }
                        });

                    }
                });
    }
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
    public void retrieve_task_data_proffesor(String deadline,ListView submissions,ListView lessons_sent){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        FileDownloader downloader = new FileDownloader();
        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DatabaseReference database_reference;

                        database_reference = database.getReference("tasks").child((String)documentSnapshot.get("user_highschool")).child("teachers").child((String)documentSnapshot.get("Username")).child(deadline);

                        database_reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Map<String,Object> map =  (Map<String,Object>)snapshot.getValue();
                                if(map != null){
                                    if((List<String>)map.get("files") != null){
                                        List<String> lessons_list = (List<String>)map.get("files");
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,lessons_list);
                                        lessons_sent.setAdapter(adapter);
                                    }
                                    if(map.get("submissions") != null){
                                        Map<String,String> submissions_map = (Map<String, String>) map.get("submissions");
                                        List<String> presence_list = new ArrayList<>();
                                        presence_list.addAll(submissions_map.keySet());
                                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,presence_list);
                                        submissions.setAdapter(adapter2);
                                        submissions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                try{
                                                    downloader.saveFile(context,presence_list.get(position),"files/lessons","lesosns");
                                                }catch(Exception e){
                                                    Intent i = new Intent(context,FileViewer.class);
                                                    i.putExtra("lesson_name",presence_list.get(position));
                                                    context.startActivity(i);

                                                }
                                            }
                                        });


                                    }


                                }





                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                });

    }
    public void retrieve_task_data_elev(String hour_ms,ListView lessons,ListView submissions,TextView teacher,TextView ai,TextView lesson_network){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FileDownloader downloader = new FileDownloader();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DatabaseReference database_reference;
                        if(hour_ms == null){
                            Log.i("YUYUYUYUYTUYUYUY","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

                        }

                        database_reference = database.getReference("tasks").child((String)documentSnapshot.get("user_highschool")).child("classes").child((String)documentSnapshot.get("user_class")).child(hour_ms);

                        database_reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Map<String,Object> map =  (Map<String,Object>)snapshot.getValue();
                                if(map != null){
                                    if((List<String>)map.get("files") != null){
                                        List<String> lessons_list = (List<String>)map.get("files");
                                        lesson_network.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i = new Intent(context,Lesson_list.class);
                                                i.putExtra("grade",(String)((String) documentSnapshot.get("user_class")).replaceAll("[^0-9.]", ""));
                                                i.putExtra("main_course",map.get("title").toString());
                                                context.startActivity(i);

                                            }
                                        });
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,lessons_list);
                                        lessons.setAdapter(adapter);
                                        lessons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                try{
                                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                                    StorageReference storageRef = storage.getReference();
                                                    storageRef.child("lessons/" + lessons_list.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                            context.startActivity(intent);
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception exception) {
                                                            // Handle any errors
                                                        }
                                                    });
                                                }catch(Exception e){
                                                    Intent i = new Intent(context,FileViewer.class);
                                                    i.putExtra("lesson_name",lessons_list.get(position));
                                                    context.startActivity(i);
                                                }

                                            }
                                        });
                                    }

                                    if(map.get("submissions") != null){
                                        Map<String,String> submissions_map = (Map<String, String>) map.get("submissions");
                                        List<String> submission_list = new ArrayList<>();
                                        submission_list.addAll(submissions_map.values());
                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,submission_list);
                                        submissions.setAdapter(arrayAdapter);
                                    }
                                  teacher.setText(map.get("teacher").toString());

                                    ai.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            context.startActivity(new Intent(context,AI_generator.class));
                                        }
                                    });





                                }





                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                });
    }

    public void retrieve_test_data_proffesor(ListView questions,String deadline){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        FileDownloader downloader = new FileDownloader();
        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DatabaseReference database_reference;

                        database_reference = database.getReference("tests").child((String)documentSnapshot.get("user_highschool")).child("teachers").child((String)documentSnapshot.get("Username")).child(deadline);

                        database_reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Map<String,Object> map =  (Map<String,Object>)snapshot.getValue();
                                if(map != null){

                                    if(map.get("questions") != null){
                                        Map<String,String> questions_map = (Map<String, String>) map.get("submissions");
                                        List<String> questions_list = new ArrayList<>();
                                        questions_list.addAll(questions_map.keySet());
                                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,questions_list);
                                        questions.setAdapter(adapter2);
                                        questions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent i = new Intent(context, Test_viewer_proffesor.class);
                                                context.startActivity(i);

                                            }
                                        });


                                    }


                                }





                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                });

    }
    public void retrieve_test_data_elev(String hour_ms,ListView lessons,ListView submissions,TextView teacher,TextView ai,TextView lesson_network){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FileDownloader downloader = new FileDownloader();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DatabaseReference database_reference;
                        if(hour_ms == null){
                            Log.i("YUYUYUYUYTUYUYUY","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

                        }

                        database_reference = database.getReference("tasks").child((String)documentSnapshot.get("user_highschool")).child("classes").child((String)documentSnapshot.get("user_class")).child(hour_ms);
                        if(Basic_tools.hour_is_active(Long.parseLong(hour_ms))){
                            Intent i = new Intent(context,NFC_detection.class);
                            i.putExtra("Test","ye");
                            i.putExtra("hour_ms",hour_ms);

                            context.startActivity(i);
                        }

                        database_reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Map<String,Object> map =  (Map<String,Object>)snapshot.getValue();
                                if(map != null){
                                    if((List<String>)map.get("files") != null){
                                        List<String> lessons_list = (List<String>)map.get("files");
                                        lesson_network.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i = new Intent(context,Lesson_list.class);
                                                i.putExtra("grade",(String)((String) documentSnapshot.get("user_class")).replaceAll("[^0-9.]", ""));
                                                i.putExtra("main_course",map.get("title").toString());
                                                context.startActivity(i);

                                            }
                                        });
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,lessons_list);
                                        lessons.setAdapter(adapter);
                                        lessons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                try{
                                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                                    StorageReference storageRef = storage.getReference();
                                                    storageRef.child("lessons/" + lessons_list.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                            context.startActivity(intent);
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception exception) {
                                                            // Handle any errors
                                                        }
                                                    });
                                                }catch(Exception e){
                                                    Intent i = new Intent(context,FileViewer.class);
                                                    i.putExtra("lesson_name",lessons_list.get(position));
                                                    context.startActivity(i);
                                                }

                                            }
                                        });
                                    }


                                    teacher.setText(map.get("teacher").toString());

                                    ai.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            context.startActivity(new Intent(context,AI_generator.class));
                                        }
                                    });






                                }





                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                });
    }
    public void retrieve_test_questions_elev(ListView question,String hour_ms){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        database.getReference("tasks").child((String)documentSnapshot.get("user_highschool")).child("classes").child((String)documentSnapshot.get("user_class")).child(hour_ms)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Map<String,Object> map =  (Map<String,Object>)snapshot.getValue();
                                        List<HashMap<String,String>> q_list = (List<HashMap<String,String>>) map.get("questions");
                                        List<String> q_names = new ArrayList<>();
                                        for(HashMap<String,String> question : q_list){
                                            q_names.add(question.get("prompt"));

                                        }
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,q_names);
                                        question.setAdapter(adapter);
                                        question.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent i = new Intent(context, Question_viewer.class);
                                                i.putExtra("question",q_list.get(position));
                                                context.startActivity(i);


                                            }
                                        });


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
                                            button.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent i = new Intent(context, Pupil_info.class);
                                                    i.putExtra("pupil_name",button.getText().toString());
                                                    i.putExtra("pupil_class",class_selected);
                                                    context.startActivity(i);
                                                }
                                            });
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
                                                    Intent i = new Intent(context, Class_info.class);
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
    public void retrieve_materies(Spinner spinner){
        FirebaseUser user = auth.getCurrentUser();
        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(documentSnapshot.getString("user_class")).collection("pupils").document(documentSnapshot.getString("Username")).collection("marks")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        List<String> keys = new ArrayList<>();
                                        keys.add("");
                                        for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){
                                            keys.add(document.getId());
                                        }
                                        ArrayAdapter adapter = new ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item,keys);
                                        spinner.setAdapter(adapter);
                                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                if(!keys.get(position).equals("")){
                                                    Intent i = new Intent(context, Materie_info.class);
                                                    i.putExtra("materie_name",keys.get(position));
                                                    context.startActivity(i);
                                                }

                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {

                                            }
                                        });

                                    }
                                });
                    }
                });

    }
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
        i.putExtra("presence",true);
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
    public void upload_mark(String class_marked, String pupil, String mark,String date){
        FirebaseUser user = auth.getCurrentUser();

        Map<String,String> map = new HashMap<>();
        map.put("mark",mark);
        Map<String,String> test = new HashMap<>();
        test.put("didldo","blblblb");
        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(pupil).collection("marks")
                                .document(documentSnapshot.get("user_subject",String.class)).collection("marks").document(date)
                                .set(map);
                        store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(pupil).collection("marks")
                                .document(documentSnapshot.get("user_subject",String.class))
                                .set(test);

                    }
                });
    }
    public void retrieve_pupil_info(String pupil_name,String pupil_class,ListView mark_list,ListView absence_list,TextView avg_mark,TextView absences){
        FirebaseUser user = auth.getCurrentUser();

        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(pupil_class).collection("pupils").document(pupil_name).collection("absences")
                                .document(documentSnapshot.get("user_subject",String.class)).collection("absences")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                        int absencecontour = 0;
                                        if(list != null){
                                            List<String> datelist = new ArrayList<>();
                                            for(int i = 0;i< list.size();i++){
                                                datelist.add(list.get(i).getId());
                                                absencecontour++;
                                            }
                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,datelist);
                                            absence_list.setAdapter(adapter);
                                            absences.setText("Absences : " + absencecontour);

                                        }

                                    }
                                });

                        store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(pupil_class).collection("pupils").document(pupil_name).collection("marks")
                                .document(documentSnapshot.get("user_subject",String.class)).collection("marks")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                        int mark_contour = 0;
                                        int mark_total = 0;
                                        if(list != null){
                                            List<String> datelist = new ArrayList<>();
                                            for(int i = 0;i< list.size();i++){

                                                datelist.add( list.get(i).getString("mark"));
                                               // mark_contour++;
                                               // mark_total +=  Integer.parseInt(list.get(i).getString("mark"));
                                            }
                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,datelist);
                                            mark_list.setAdapter(adapter);
                                           // absences.setText("Avg mark : " + mark_total / mark_contour);

                                        }

                                    }
                                });
                    }
                });

    }
    public void retrieve_pupil_info_elev(ListView mark_list,ListView absence_list,TextView avg_mark,TextView absences,String materie){
        FirebaseUser user = auth.getCurrentUser();

        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(documentSnapshot.getString("user_class")).collection("pupils").document(user.getDisplayName()).collection("absences")
                                .document(materie).collection("absences")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                        int absencecontour = 0;
                                        if(list != null){
                                            List<String> datelist = new ArrayList<>();
                                            for(int i = 0;i< list.size();i++){
                                                datelist.add(list.get(i).getId());
                                                absencecontour++;
                                            }
                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,datelist);
                                            absence_list.setAdapter(adapter);
                                           // absences.setText("Absences : " + absencecontour);

                                        }

                                    }
                                });

                        store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(documentSnapshot.getString("user_class")).collection("pupils").document(user.getDisplayName()).collection("marks")
                                .document(materie).collection("marks")

                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                        int mark_contour = 0;
                                        int mark_total = 0;
                                        if(list != null){
                                            List<String> datelist = new ArrayList<>();
                                            for(int i = 0;i< list.size();i++){

                                                datelist.add(list.get(i).getString("mark"));
                                               // mark_contour++;
                                               /// mark_total +=  Integer.parseInt(list.get(i).getString("mark"));
                                            }
                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,datelist);
                                            mark_list.setAdapter(adapter);
                                           // absences.setText("Avg mark : " + mark_total / mark_contour);

                                        }

                                    }
                                });
                    }
                });

    }
    public void retrieve_lessons(String grade,LinearLayout linearl,String main_course){
        AI_core core = new AI_core(context);
        store.collection("courses").document(grade).collection(grade)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> list =  queryDocumentSnapshots.getDocuments();
                        if(!(list == null || list.isEmpty())){
                            list = core.recommender_system(list,main_course);

                            for (DocumentSnapshot document : list) {
                                Button lesson_button = new Button(context);
                                lesson_button.setText(document.getString("title"));
                                lesson_button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        List<String> lesson_names = document.get("files",ArrayList.class);
                                        for(String name : lesson_names){
                                            FirebaseStorage storage = FirebaseStorage.getInstance();
                                            StorageReference storageRef = storage.getReference();
                                            storageRef.child("lessons/" + name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {


                                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    // Handle any errors
                                                }
                                            });

                                        }
                                    }
                                });
                            }
                        }

                    }
                });
    }
}
