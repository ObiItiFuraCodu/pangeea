package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class Dashboard extends AppCompatActivity {
    FirebaseFirestore store = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setAlarm();


    }
    public void setAlarm(){
        AlarmManager mAlarmManger = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        //Create pending intent & register it to your alarm notifier class
        Intent intent = new Intent(this, HourSetter.class);
       // intent.putExtra("uur", "1e"); // if you want
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        //set timer you want alarm to work (here I have set it to 9.00)
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        //set that timer as a RTC Wakeup to alarm manager object
        mAlarmManger .set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LinearLayout layout_prof = findViewById(R.id.linearl_prof);
        LinearLayout layout_class = findViewById(R.id.linearl_class);

        store.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
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
                                           for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){
                                               Button button = new Button(Dashboard.this);
                                               button.setText(document.getId());
                                               button.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       Intent i = new Intent(v.getContext(),ModifyTimetable.class);
                                                       i.putExtra("class",document.getId());
                                                       startActivity(i);
                                                   }
                                               });
                                               layout_class.addView(button);

                                           }
                                       }

                                   }
                               });
                    }
                });
        store.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        store.collection("highschools").document(documentSnapshot.getString("user_highschool")).collection("timetables").document("teachers").collection("teachers")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if(queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()){
                                            for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){
                                                Button button = new Button(Dashboard.this);
                                                button.setText(document.getId());
                                                button.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent i = new Intent(v.getContext(),ModifyTimetable.class);
                                                        i.putExtra("teacher",document.getId());
                                                        startActivity(i);
                                                    }
                                                });
                                                layout_prof.addView(button);

                                            }
                                        }

                                    }
                                });
                    }
                });

    }

}