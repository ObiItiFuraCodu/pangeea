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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.pangeea.ai.AI_generator;
import com.example.pangeea.content.Lesson_list;
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
import java.util.Map;

public class HourBackend extends DatabaseConnector {
    Context context;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore store = FirebaseFirestore.getInstance();
    final long ONE_HOUR_IN_MILIS = 3600000;
    final long ONE_DAY_IN_MILIS = 86400000;
    Basic_tools tool = new Basic_tools();

    public HourBackend(Context context) {
        super(context);
    }
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

                                                      Map<String, Map<String,String>> map =  (Map<String,Map<String,String>>)snapshot.getValue();
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
    public void add_hour(long hour_ms, String class_name, String details, List<Uri> files, String title){
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
    public void retrieve_hour_data_prof(String hour_milis, ListView pupil_present, ListView lessons_sent, ListView question, Button close_presence){
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
    public void retrieve_hour_data_elev(String hour_ms, ListView lv, Button questions, boolean presence, TextView ai, TextView lesson_network){
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

}
