package com.example.pangeea;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class HourSetter extends Service {
    FirebaseFirestore store = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseConnector connector = new DatabaseConnector(this);
    public HourSetter() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        store.collection("highschools").document(documentSnapshot.getString("user_highschool")).collection("timetables").document("classes").collection("classes")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if(queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()){
                                            for(DocumentSnapshot document : queryDocumentSnapshots){
                                                String class_taught = document.getId();

                                                    if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) < 5){
                                                        List<HashMap<String,String>> list_of_hours = document.get(Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)), ArrayList.class);
                                                        for(HashMap<String,String> hour : list_of_hours){
                                                            int index = Integer.parseInt(hour.get("index"));
                                                            String name = hour.get("name");
                                                            String teacher = hour.get("teacher");
                                                            Calendar tomorrow = Calendar.getInstance();
                                                            tomorrow.set(Calendar.HOUR_OF_DAY,index + 8);
                                                            tomorrow.set(Calendar.MINUTE,0);
                                                            tomorrow.set(Calendar.SECOND,0);
                                                            tomorrow.set(Calendar.MILLISECOND,0);
                                                            tomorrow.set(Calendar.DAY_OF_WEEK,Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 1);
                                                            connector.add_automated_hour(tomorrow.getTimeInMillis(),class_taught,"automated_hour",name,teacher,documentSnapshot.getString("user_highschool"));
                                                        }
                                                    }

                                            }
                                        }
                                    }
                                });

                    }
                });
        return super.onStartCommand(intent, flags, startId);
    }
}