package com.example.pangeea.backend;

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
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.pangeea.CustomElements.CustomButtonLesson;

import com.example.pangeea.R;
import com.example.pangeea.ai.AI_generator;
import com.example.pangeea.ai.AI_lesson;
import com.example.pangeea.content.Lesson_list;
import com.example.pangeea.content.Lesson_viewer_nongenerated;
import com.example.pangeea.other.Basic_tools;
import com.example.pangeea.other.FileDownloader;
import com.example.pangeea.other.FileViewer;
import com.example.pangeea.task.Task_info;
import com.example.pangeea.task.Task_info_proffesor;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskBackend extends DatabaseConnector{
    Context context;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore store = FirebaseFirestore.getInstance();
    final long ONE_HOUR_IN_MILIS = 3600000;
    final long ONE_DAY_IN_MILIS = 86400000;
    Basic_tools tool = new Basic_tools();
    String database_string = "https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app";
    public TaskBackend(Context context) {
        super(context);
        this.context = context;

    }


    public void add_task(long test_ms, String class_name, String details, List<Uri> files, String title,String content,boolean is_public){
        FirebaseUser user = auth.getCurrentUser();
        FirebaseDatabase dbb = FirebaseDatabase.getInstance(database_string);
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
                        save_file(filenames,class_name,title,content,is_public);
                        map.put("details",details);
                        map.put("user_subject",(String)documentSnapshot.get("user_subject"));
                        map.put("files",filenames);
                        map.put("title",title);
                        map.put("teacher",(String)documentSnapshot.get("Username"));

                        Map<String,Object> map2 = new HashMap<>();
                        map2.put("details",details);
                        map2.put("class_name",class_name);
                        map2.put("files",filenames);
                        map2.put("title",title);
                        map2.put("teacher",(String)documentSnapshot.get("Username"));


                        ref.child((String)documentSnapshot.get("user_highschool")).child("classes").child(class_name).child(Long.toString(test_ms)).setValue(map);
                        ref.child((String)documentSnapshot.get("user_highschool")).child("teachers").child(user.getDisplayName()).child(Long.toString(test_ms )).setValue(map2);
                    }
                });

    }
    public void import_tasks(LinearLayout layout, ScrollView task_view){
        FirebaseUser user = auth.getCurrentUser();
        FirebaseDatabase dbb = FirebaseDatabase.getInstance(database_string);

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
                                                //  task_view.setBackground(context.getResources().getDrawable(R.drawable.rounded4));
                                              }else{
                                                  ref = dbb.getReference("tasks").child(user_highschool.replaceAll("[^A-Za-z0-9]", "")).child("classes").child(user_class.replaceAll("[^A-Za-z0-9]", ""));

                                              }
                                              ref.addValueEventListener(new ValueEventListener() {
                                                  @Override
                                                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                   layout.removeAllViews();




                                                      Map<String,Map<String,String>> map =  (Map<String,Map<String,String>>)snapshot.getValue();
                                                      if(map != null){

                                                          for(Map.Entry<String,Map<String,String>> set :
                                                                  map.entrySet()){
                                                              CustomButtonLesson custom_butt = new CustomButtonLesson(context);
                                                              LinearLayout v = (LinearLayout) custom_butt.getChildAt(0);
                                                              Map<String,String> value = (Map<java.lang.String, java.lang.String>)set.getValue();



                                                              Long hour_milisecs =  Long.parseLong(set.getKey().toString());
                                                              Log.i("SYSTEMAMSA",Integer.toString((int)System.currentTimeMillis()));
                                                              //  Log.i("HOURMILIS",Integer.toString(hour_milisecs));
                                                              if(user_category.equals("1")){
                                                                  Button button = (Button) v.getChildAt(0);
                                                                  TextView view = (TextView) v.getChildAt(1);
                                                                  button.setText("task" + value.get("class_name") + " " + value.get("title"));
                                                                  v.setBackgroundColor(context.getResources().getColor(R.color.dark_red));
                                                                  view.setText("deadline in : " +  Long.toString ((hour_milisecs - System.currentTimeMillis()) / 3600000) + context.getResources().getString(R.string.hours));
                                                              }else{
                                                                  Button button = (Button) v.getChildAt(0);
                                                                  TextView view = (TextView) v.getChildAt(1);
                                                                  button.setText("task" + value.get("user_subject") + " " + value.get("title"));
                                                                  view.setText("deadline in : " +  Long.toString ((hour_milisecs - System.currentTimeMillis()) / 3600000) + context.getResources().getString(R.string.hours));
                                                              }
                                                              v.setOnClickListener(new View.OnClickListener() {
                                                                  @Override
                                                                  public void onClick(View c) {
                                                                      if(!user_category.equals("1")){

                                                                          Intent i = new Intent(c.getContext(), Task_info.class);
                                                                          i.putExtra("classname",value.get("class_name"));
                                                                          i.putExtra("hour_milis",set.getKey().toString());
                                                                          context.startActivity(i);

                                                                      }else{

                                                                          Intent i = new Intent(c.getContext(), Task_info_proffesor.class);
                                                                          i.putExtra("classname",value.get("user_subject"));
                                                                          i.putExtra("hour_milis",set.getKey().toString());
                                                                          context.startActivity(i);
                                                                      }




                                                                  }
                                                              });
                                                              if(hour_milisecs > System.currentTimeMillis()){
                                                                  custom_butt.setElevation(10f);
                                                                  layout.addView(custom_butt);

                                                              }else{
                                                                  Log.i("TF","WTFFFFFFFFFFFFFFFFFF");
                                                                  snapshot.getRef().child(Long.toString(hour_milisecs)).removeValue();
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
    public void retrieve_task_data_proffesor(String deadline, ListView submissions, ListView lessons_sent,TextView class_tasked){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance(database_string);
        FileDownloader downloader = new FileDownloader();
        submissions.setBackgroundColor(context.getResources().getColor(R.color.very_light_red));
        lessons_sent.setBackgroundColor(context.getResources().getColor(R.color.very_light_red));
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
                                    class_tasked.setText(map.get("class_name").toString());

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
                                                    Intent i = new Intent(context, FileViewer.class);
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
    public void retrieve_task_data_elev(String hour_ms, ListView lessons, ListView submissions, TextView teacher, TextView ai, TextView lesson_network,TextView task_title){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FileDownloader downloader = new FileDownloader();
        FirebaseDatabase database = FirebaseDatabase.getInstance(database_string);
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
                                        task_title.setText(map.get("title").toString());
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
                                        ai.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i = new Intent(v.getContext(), AI_lesson.class);
                                                i.putExtra("title",map.get("title").toString());
                                                context.startActivity(i);
                                            }
                                        });
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,lessons_list);
                                        lessons.setAdapter(adapter);
                                        lessons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent i = new Intent(context, Lesson_viewer_nongenerated.class);
                                                i.putExtra("grade",(String)((String) documentSnapshot.get("user_class")).replaceAll("[^0-9.]", ""));
                                                i.putExtra("title",map.get("title").toString());
                                                context.startActivity(i);

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







                                }





                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                });
    }



}
