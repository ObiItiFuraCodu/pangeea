package com.example.pangeea.backend;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.pangeea.R;
import com.example.pangeea.ai.AI_generator;
import com.example.pangeea.content.Lesson_list;
import com.example.pangeea.other.Basic_tools;
import com.example.pangeea.other.FileDownloader;
import com.example.pangeea.other.FileViewer;
import com.example.pangeea.other.NFC_detection;
import com.example.pangeea.test.Question_viewer;
import com.example.pangeea.test.Question_viewer_ABC;
import com.example.pangeea.test.Test_info_prof;
import com.example.pangeea.test.Test_viewer_elev;
import com.example.pangeea.test.Test_viewer_proffesor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

public class TestBackend extends DatabaseConnector {
    Context context;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore store = FirebaseFirestore.getInstance();
    final long ONE_HOUR_IN_MILIS = 3600000;
    final long ONE_DAY_IN_MILIS = 86400000;
    Basic_tools tool = new Basic_tools();


    public TestBackend(Context context) {
        super(context);
    }
    public void add_test(long test_ms, String class_name, String details, List<Uri> files, String title, List<HashMap<String,String>> questions){
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
    public void retrieve_test_data_proffesor(ListView support_lessons, String deadline_ms,ListView submissions){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        FileDownloader downloader = new FileDownloader();
        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DatabaseReference database_reference;

                        database_reference = database.getReference("tests").child((String)documentSnapshot.get("user_highschool")).child("teachers").child((String)documentSnapshot.get("Username")).child(deadline_ms);

                        database_reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Map<String,Object> map =  (Map<String,Object>)snapshot.getValue();
                                if(map != null){

                                    if(map.get("support_lessons") != null){
                                        Map<String,String> questions_map = (Map<String, String>) map.get("support_lessons");
                                        List<String> questions_list = new ArrayList<>();
                                        questions_list.addAll(questions_map.keySet());
                                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,questions_list);
                                        support_lessons.setAdapter(adapter2);
                                        support_lessons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent i = new Intent(context, Test_viewer_proffesor.class);
                                                context.startActivity(i);

                                            }
                                        });


                                    }
                                    if(map.get("submissions") != null){
                                        Map<String,String> questions_map = (Map<String, String>) map.get("submissions");
                                        List<String> questions_list = new ArrayList<>();
                                        questions_list.addAll(questions_map.keySet());
                                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,questions_list);
                                        submissions.setAdapter(adapter2);
                                        submissions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent i = new Intent(context, Test_viewer_proffesor.class);
                                                i.putExtra("pupil",questions_list.get(position).toString());
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
    public void retrieve_test_data_elev(String hour_ms, ListView lessons, TextView teacher, TextView ai, TextView lesson_network){
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
                            Intent i = new Intent(context, NFC_detection.class);
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
                                                Intent i = new Intent(context, Lesson_list.class);
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
                                                    Intent i = new Intent(context, FileViewer.class);
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
                                            context.startActivity(new Intent(context, AI_generator.class));
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
                        database.getReference("tests").child((String)documentSnapshot.get("user_highschool")).child("classes").child((String)documentSnapshot.get("user_class")).child(hour_ms)
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
                                                if(q_list.get(position).get("type").equals("A/B/C")){
                                                    Intent i = new Intent(context, Question_viewer_ABC.class);
                                                    i.putExtra("question",q_list.get(position));
                                                    context.startActivity(i);
                                                }else{
                                                    Intent i = new Intent(context, Question_viewer.class);
                                                    i.putExtra("question",q_list.get(position));
                                                    context.startActivity(i);
                                                }




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
    public void correct_test_and_upload(String hour_ms,List<HashMap<String,Object>> answers,String teacher){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference storage_ref = FirebaseStorage.getInstance().getReference();
        for(HashMap<String,Object> answer : answers){
            List<Uri> answer_files = (List<Uri>) answer.get("files");
            for(Uri file : answer_files){
                storage_ref.child("tests/" + user.getDisplayName() + "/submissions")
                        .putFile(file);
            }
            answer.remove(answer.get("files"));

        }



        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        database.getReference("tests").child((String)documentSnapshot.get("user_highschool")).child("teachers").child(teacher).child(hour_ms).child("submissions").child(documentSnapshot.getString("Username"))
                                .setValue(answers);
                        database.getReference("tests").child((String)documentSnapshot.get("user_highschool")).child("classes").child(documentSnapshot.getString("user_class")).child(hour_ms).child("submissions").child(documentSnapshot.getString("Username"))
                                .setValue(answers);
                    }
                });

    }
    public void retrieve_questions_to_be_corrected(ListView to_correct,String test_ms,String pupil){
        FirebaseUser user = auth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app");
        List<String> question_names = new ArrayList<>();
        StorageReference storage_ref = FirebaseStorage.getInstance().getReference();
        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        database.getReference("tests").child((String)documentSnapshot.get("user_highschool")).child("teachers").child(documentSnapshot.getString("teacher")).child(test_ms).child("submissions").child(pupil)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                    @Override
                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                        List<HashMap<String,Object>> question_list = new ArrayList<>();
                                        for(int i = 0;i<  question_list.size();i++){

                                            HashMap<String,Object> question = question_list.get(i);
                                            String type = (String)question.get("type");
                                            if(type.equals("to_be_corrected")){
                                                question_names.add(Integer.toString(i));
                                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,question_names);
                                                to_correct.setAdapter(adapter);
                                                to_correct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        List<String> filenames = (List<String>) question.get("filenames");
                                                        for(String file : filenames){
                                                            storage_ref.child("tests/" + pupil + "/submissions/" + file).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                                    context.startActivity(intent);
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
                });
    }



}