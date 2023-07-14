package com.example.pangeea.backend;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.example.pangeea.CustomElements.CustomButtonAnswer;
import com.example.pangeea.CustomElements.CustomButtonLesson;

import com.example.pangeea.R;
import com.example.pangeea.ai.AI_generator;
import com.example.pangeea.ai.AI_lesson;
import com.example.pangeea.content.Lesson_list;
import com.example.pangeea.content.Lesson_viewer_nongenerated;
import com.example.pangeea.hour.Hour_info_elev;
import com.example.pangeea.hour.Hour_info_profesor;
import com.example.pangeea.other.Basic_tools;
import com.example.pangeea.other.FileDownloader;
import com.example.pangeea.other.FileViewer;
import com.example.pangeea.other.NFC_detection;
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
import java.util.Locale;
import java.util.Map;

public class HourBackend extends DatabaseConnector {
    Context context;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore store = FirebaseFirestore.getInstance();
    final long ONE_HOUR_IN_MILIS = 3600000;
    final long ONE_DAY_IN_MILIS = 86400000;
    Basic_tools tool = new Basic_tools();
    String database_string = "https://pangeea-835fb-default-rtdb.europe-west1.firebasedatabase.app";
    public HourBackend(Context context) {
        super(context);
        this.context = context;

    }
    public void import_hours(LinearLayout layout, TextView welcome_back, ScrollView hour_view){
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
                                              welcome_back.setText(context.getResources().getString(R.string.welcome_back) + " " + auth.getCurrentUser().getDisplayName().toUpperCase(Locale.ROOT));
                                              if(user_category.equals("1")){
                                                  ref = dbb.getReference("hourss").child(user_highschool.replaceAll("[^A-Za-z0-9]", "")).child("teachers").child(user.getDisplayName());
                                               //   hour_view.setBackground(context.getResources().getDrawable(R.drawable.rounded4));
                                                 // welcome_back.setTextColor(context.getResources().getColor(R.color.dark_red));


                                              }else{
                                                  ref = dbb.getReference("hourss").child(user_highschool.replaceAll("[^A-Za-z0-9]", "")).child("classes").child(user_class.replaceAll("[^A-Za-z0-9]", ""));

                                              }
                                              ref.addValueEventListener(new ValueEventListener() {
                                                  private Object String;

                                                  @Override
                                                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                      layout.removeAllViews();

                                                      Map<String, Map<String,String>> map =  (Map<String,Map<String,String>>)snapshot.getValue();
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
                                                                  if(System.currentTimeMillis() < (hour_milisecs + ONE_HOUR_IN_MILIS) && System.currentTimeMillis() > hour_milisecs){
                                                                      Button button = (Button) v.getChildAt(0);
                                                                      TextView view = (TextView) v.getChildAt(1);
                                                                      v.setBackgroundColor(context.getResources().getColor(R.color.dark_red));
                                                                      button.setText("hour " + value.get("class_name"));

                                                                      view.setText("active now");
                                                                  }else{
                                                                      Button button = (Button) v.getChildAt(0);
                                                                      TextView view = (TextView) v.getChildAt(1);
                                                                      button.setText("hour "+ value.get("class_name"));
                                                                      view.setText(" starts in " + Long.toString ((hour_milisecs - System.currentTimeMillis()) / 3600000) + " hours");
                                                                      v.setBackgroundColor(context.getResources().getColor(R.color.dark_red));
                                                                  }

                                                              }else{
                                                                  if(System.currentTimeMillis() < (hour_milisecs + ONE_HOUR_IN_MILIS) && System.currentTimeMillis() > hour_milisecs){
                                                                      Button button = (Button) v.getChildAt(0);
                                                                      TextView view = (TextView) v.getChildAt(1);
                                                                      button.setText("hour" + value.get("user_subject"));
                                                                      view.setText("active now");
                                                                  }else{
                                                                      Button button = (Button) v.getChildAt(0);
                                                                      TextView view = (TextView) v.getChildAt(1);
                                                                      button.setText("hour "+ value.get("user_subject"));
                                                                      view.setText(" starts in " + Long.toString ((hour_milisecs - System.currentTimeMillis()) / 3600000) + " hours");
                                                                  }
                                                              }
                                                              v.setOnClickListener(new View.OnClickListener() {
                                                                  @Override
                                                                  public void onClick(View c) {
                                                                      if(user_category.equals("1")){
                                                                          Intent i = new Intent(c.getContext(), Hour_info_profesor.class);
                                                                          i.putExtra("classname",value.get("class_name"));
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
                                                              if(hour_milisecs > System.currentTimeMillis() - ONE_HOUR_IN_MILIS){
                                                                  custom_butt.setElevation(10f);
                                                                  layout.addView(custom_butt);

                                                              }else{
                                                                 // snapshot.getRef().child(snapshot.getKey()).removeValue();
                                                                  Log.i("BLBLBLBLBLBLB",snapshot.getKey());
                                                                  snapshot.getRef().child(Long.toString(hour_milisecs)).removeValue();
                                                                  ///Log.i("TAFDASRFWSREFAS",Long.toString(System.currentTimeMillis()));
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
    public void add_hour(long hour_ms, String class_name, String details, List<Uri> files, String title,String support_lesson_content,boolean is_public){
        FirebaseUser user = auth.getCurrentUser();
        List<String> filenames  = new ArrayList<>();
        FirebaseDatabase dbb = FirebaseDatabase.getInstance(database_string);
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
                        save_file(filenames,class_name,title,support_lesson_content,is_public);
                        Map<String,Object> map = new HashMap<>();
                        map.put("details",details);
                        map.put("user_subject",(String)documentSnapshot.get("user_subject"));
                        map.put("files",filenames);
                        map.put("title",title);
                        map.put("content",support_lesson_content);
                        map.put("teacher",(String)documentSnapshot.get("Username"));
                        map.put("public",is_public);
                        Map<String,Object> map2 = new HashMap<>();
                        map2.put("details",details);
                        map2.put("class_name",class_name);
                        map2.put("files",filenames);
                        map2.put("title",title);
                        map2.put("teacher",(String)documentSnapshot.get("Username"));
                        ref.child((String)documentSnapshot.get("user_highschool")).child("classes").child(class_name).child(Long.toString(hour_ms)).setValue(map);
                        ref.child((String)documentSnapshot.get("user_highschool")).child("teachers").child(user.getDisplayName()).child(Long.toString(hour_ms)).setValue(map2);
                        store.collection("courses").document(class_name.replaceAll("[^0-9.]", "")).collection(class_name.replaceAll("[^0-9.]", "")).document(title)
                                .set(map);
                    }
                });




    }
    public void add_automated_hour(long hour_ms, String class_name, String details, String title,String teacher,String highschool){
        FirebaseUser user = auth.getCurrentUser();
        List<String> filenames  = new ArrayList<>();
        FirebaseDatabase dbb = FirebaseDatabase.getInstance(database_string);
        StorageReference storage_ref = FirebaseStorage.getInstance().getReference();

        DatabaseReference ref = dbb.getReference("hourss");
       // save_file(filenames,class_name,title);
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
    public void retrieve_hour_data_prof(String hour_milis, ListView pupil_present, ListView lessons_sent, ListView question, Button close_presence,TextView class_name){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance(database_string);
        pupil_present.setBackgroundColor(context.getResources().getColor(R.color.very_light_red));
        lessons_sent.setBackgroundColor(context.getResources().getColor(R.color.very_light_red));
        question.setBackgroundColor(context.getResources().getColor(R.color.very_light_red));
        close_presence.setBackgroundColor(context.getResources().getColor(R.color.dark_red));



        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DatabaseReference database_reference;
                        List<String> presence_list = new ArrayList<>();

                        database_reference = database.getReference("hourss").child((String)documentSnapshot.get("user_highschool")).child("teachers").child((String)documentSnapshot.get("Username")).child(hour_milis);

                        database_reference.addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Map<String,Object> map =  (Map<String,Object>)snapshot.getValue();
                                if(map != null){
                                    class_name.setText(map.get("class_name").toString());

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
                                        question.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                new AlertDialog.Builder(context)
                                                        .setTitle(questions.get(position) + " has a question")
                                                        .setMessage("How will you answer?")
                                                        .setPositiveButton(context.getResources().getString(R.string.i_will_answer), new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                answer_question(hour_milis,questions.get(position),true);
                                                                dialog.dismiss();
                                                            }
                                                        })
                                                        .setNegativeButton(context.getResources().getString(R.string.ai_will_answer), new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                answer_question(hour_milis,questions.get(position),false);
                                                                dialog.dismiss();
                                                            }
                                                        })
                                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                                        .show();
                                            }
                                        });
                                    }
                                    close_presence.setOnClickListener(new View.OnClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                        @Override
                                        public void onClick(View v) {
                                            mark_absent((String) map.get("class_name"),presence_list);
                                        }
                                    });
                                    if(Long.parseLong(hour_milis) + ONE_HOUR_IN_MILIS  -  System.currentTimeMillis() < 1000){
                                        mark_absent((String) map.get("class_name"),presence_list);
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
    public void retrieve_hour_data_elev(String hour_ms, ListView lv, Button questions, boolean presence, TextView ai, TextView lesson_network,TextView hour_name){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FileDownloader downloader = new FileDownloader();
        Basic_tools tools = new Basic_tools();
        FirebaseDatabase database = FirebaseDatabase.getInstance(database_string);
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
                                    hour_name.setText(map.get("title").toString());
                                    if(!presence && tools.hour_is_active(Long.parseLong(hour_ms))){
                                        Intent i = new Intent(context, NFC_detection.class);
                                        i.putExtra("teacher",map.get("teacher").toString());
                                        i.putExtra("hour_ms",hour_ms);
                                        i.putExtra("user_class",documentSnapshot.get("user_class").toString());
                                        i.putExtra("presence",presence);
                                        context.startActivity(i);
                                    }else{
                                        if(!tools.hour_is_active(Long.parseLong(hour_ms))){
                                            questions.setVisibility(View.INVISIBLE);

                                        }else{
                                            if(tools.hour_is_active(Long.parseLong(hour_ms))){
                                                lesson_network.setVisibility(View.INVISIBLE);
                                                ai.setVisibility(View.INVISIBLE);
                                            }

                                        }

                                    }


                                    questions.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ask_question(hour_ms,(String)map.get("teacher"),questions,presence);
                                        }
                                    });
                                    if((List<String>)map.get("files") != null){
                                        List<String> lessons_list = (List<String>)map.get("files");
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,lessons_list);
                                        lv.setAdapter(adapter);
                                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent i = new Intent(context, Lesson_viewer_nongenerated.class);
                                                i.putExtra("grade",(String)((String) documentSnapshot.get("user_class")).replaceAll("[^0-9.]", ""));
                                                i.putExtra("title",map.get("title").toString());
                                                context.startActivity(i);
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
                                ai.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(v.getContext(), AI_lesson.class);
                                        i.putExtra("title",map.get("title").toString());
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

}
