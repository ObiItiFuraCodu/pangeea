package com.example.pangeea.backend;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pangeea.R;
import com.example.pangeea.catalogue.Materie_info;
import com.example.pangeea.catalogue.Pupil_info;
import com.example.pangeea.main.Class_info;
import com.example.pangeea.other.Basic_tools;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogueBackend extends DatabaseConnector{
    Context context;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore store = FirebaseFirestore.getInstance();
    final long ONE_HOUR_IN_MILIS = 3600000;
    final long ONE_DAY_IN_MILIS = 86400000;
    Basic_tools tool = new Basic_tools();


    public CatalogueBackend(Context context) {
        super(context);
        this.context = context;
    }
    public void retrieve_class_info(String class_selected, LinearLayout pupil_list,TextView num_of_pupils){
        FirebaseUser user = auth.getCurrentUser();
        pupil_list.removeAllViews();

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
                                            button.setBackgroundColor(context.getResources().getColor(R.color.binaryblue));
                                            button.setElevation(10f);
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
                                            num_of_pupils.setText("Number of pupils : " + i);
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
    public void retrieve_pupil_info(String pupil_name, String pupil_class, ListView mark_list, ListView absence_list){
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
                                                mark_contour++;
                                                mark_total +=  Integer.parseInt(list.get(i).getString("mark"));


                                            }
                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,datelist);
                                            mark_list.setAdapter(adapter);

                                        }

                                    }
                                });
                    }
                });

    }
    public void retrieve_pupil_info_elev(ListView mark_list,ListView absence_list,String materie){
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
                                                ///absencecontour++;
                                                //absences.setText("Absences : " + i);
                                            }
                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,datelist);
                                            absence_list.setAdapter(adapter);


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
                                                mark_contour++;
                                                mark_total +=  Integer.parseInt(list.get(i).getString("mark"));
                                            }
                                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,datelist);
                                            mark_list.setAdapter(adapter);


                                        }

                                    }
                                });
                    }
                });

    }
}
