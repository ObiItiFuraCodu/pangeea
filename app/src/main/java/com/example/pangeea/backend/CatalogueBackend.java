package com.example.pangeea.backend;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.pangeea.CustomElements.CustomCardElement;
import com.example.pangeea.CustomElements.CustomPupilButton;
import com.example.pangeea.CustomElements.CustomPupilCard;
import com.example.pangeea.R;
import com.example.pangeea.catalogue.Materie_info;
import com.example.pangeea.catalogue.Pupil_info;
import com.example.pangeea.main.Class_info;
import com.example.pangeea.other.Basic_tools;
import com.example.pangeea.other.CSList;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogueBackend {
    Context context;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore store = FirebaseFirestore.getInstance();
    final long ONE_HOUR_IN_MILIS = 3600000;
    final long ONE_DAY_IN_MILIS = 86400000;
    Basic_tools tool = new Basic_tools();


    public CatalogueBackend(Context context) {

        this.context = context;
    }
    public void retrieve_class_info(String class_selected, LinearLayout pupil_list,TextView num_of_pupils,boolean pupil){
        FirebaseUser user = auth.getCurrentUser();
        pupil_list.removeAllViews();
        Basic_tools tool = new Basic_tools();

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
                                            CustomPupilButton button = new CustomPupilButton(context);
                                            LinearLayout rootlayout = (LinearLayout) button.getChildAt(0);
                                            TextView view = (TextView) rootlayout.getChildAt(1);
                                            ImageView rank = (ImageView) rootlayout.getChildAt(0);
                                            if(!pupil){
                                                button.setBackground(context.getResources().getDrawable(R.drawable.rounded4));
                                            }


                                            button.setElevation(10f);
                                            view.setText(queryDocumentSnapshots.getDocuments().get(i).get("Username",String.class));
                                            store.collection("users").document(queryDocumentSnapshots.getDocuments().get(i).get("Username",String.class))
                                                            .get()
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                            HashMap<String,Object> ranking_data = new HashMap<>();
                                                                            if(documentSnapshot.contains("RP")){
                                                                                long rp = Long.parseLong((String) documentSnapshot.get("RP"));
                                                                                ranking_data =  tool.ranking_system(rp,context);

                                                                            }else{
                                                                                ranking_data =  tool.ranking_system(0,context);
                                                                            }
                                                                            rank.setImageDrawable((Drawable) ranking_data.get("icon"));
                                                                        }
                                                                    });
                                            button.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent i = new Intent(context, Pupil_info.class);
                                                    i.putExtra("pupil_name",view.getText().toString());
                                                    i.putExtra("pupil_class",class_selected);
                                                    context.startActivity(i);
                                                }
                                            });
                                            num_of_pupils.setText("");
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
    public void increase_pupil_score(String name,int score){
    store.collection("users").document(name)
            .get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    HashMap<String,Object> user_info = (HashMap<String, Object>) documentSnapshot.getData();
                  String RP = documentSnapshot.getString("RP");
                    long rank;
                  if(documentSnapshot.get("rank") == null){
                      rank = 1;
                  }else{
                      rank = (Long) documentSnapshot.get("rank");
                  }
                  int rp = Integer.parseInt(RP);
                  rp = rp + score;
                  RP = Integer.toString(rp);
                  HashMap<String,Object> data = tool.ranking_system(rp,context);
                  int data_rank = (int) data.get("rank");
                  if(data_rank > rank){
                      rank = data_rank;
                      user_info.put("to_be_prized","mark");
                  }
                  user_info.put("RP",RP);
                  user_info.put("rank",rank);
                  store.collection("users").document(name)
                          .set(user_info);
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
                                        List<String> materii = new ArrayList<>();
                                        materii.add("Matematica");
                                        materii.add("Fizica");
                                        materii.add("Biolgie");
                                        materii.add("Informatica");
                                        for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){
                                            if(materii.contains(document.getId())){
                                                keys.add(document.getId());
                                            }

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
    public void upload_mark(String class_marked, String pupil, String mark,String date,String test_name,boolean improvement_test,String subject){
        FirebaseUser user = auth.getCurrentUser();

        Map<String,String> map = new HashMap<>();
        map.put("mark",mark);
        Map<String,String> test = new HashMap<>();
        test.put("BLBLBLBL","blblblb");
        int mark_int = Integer.parseInt(mark);
        increase_pupil_score(pupil,mark_int*2);
        HashMap<String,String> details = new HashMap<>();
        details.put("name",test_name);
        details.put("mark",mark);
        details.put("points",Integer.toString(mark_int*2));
        if(test_name != null){
            if(improvement_test){
                store.collection("users").document(pupil).collection("ranking_history").document(test_name)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Map<String, Object> test_received_details = documentSnapshot.getData();
                                HashMap<String,Object> improvement = new HashMap<>();
                                improvement.put("improvement_mark",mark);
                                improvement.put("improvement_points",Integer.toString(mark_int*2));
                                test_received_details.put("improvement",improvement);

                                increase_pupil_score(pupil,mark_int*2);
                                store.collection("users").document(pupil).collection("ranking_history").document(test_name)
                                        .set(test_received_details);
                            }
                        });
            }else{
                store.collection("users").document(pupil).collection("ranking_history").document(test_name)
                        .set(details);
            }

        }
        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.get("user_category").equals("1")){
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(pupil).collection("marks").document(documentSnapshot.get("user_subject",String.class)).collection("marks").document(date)
                                    .set(map);
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(pupil).collection("marks").document(documentSnapshot.get("user_subject",String.class))
                                    .set(test);
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(pupil).collection("marks").document("overall").collection("marks").document(date)
                                    .set(map);
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(pupil).collection("marks").document("overall")
                                    .set(test);
                        }else if(subject == null){
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(user.getDisplayName()).collection("marks").document(test_name).collection("marks").document(date)
                                    .set(map);
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(user.getDisplayName()).collection("marks").document(test_name)
                                    .set(test);
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(user.getDisplayName()).collection("marks").document("overall").collection("marks").document(date)
                                    .set(map);
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(user.getDisplayName()).collection("marks").document("overall")
                                    .set(test);
                        }else{

                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(user.getDisplayName()).collection("marks").document(test_name).collection("marks").document(date)
                                    .set(map);
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(user.getDisplayName()).collection("marks").document(test_name)
                                    .set(test);
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(user.getDisplayName()).collection("marks").document(subject).collection("marks").document(date)
                                    .set(map);
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(user.getDisplayName()).collection("marks").document(subject)
                                    .set(test);

                        }


                    }
                });
    }
    public void retrieve_pupil_info(String pupil_name, String pupil_class, ListView mark_list, ListView absence_list, LinearLayout rank_history, TextView rank, TextView rp, boolean from_pupil, ProgressBar progressBar,ImageView rank_image){
        FirebaseUser user = auth.getCurrentUser();

        DateFormat formatter = new SimpleDateFormat(
                "dd MMM yyyy");

        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        store.collection("users").document(pupil_name)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String ranking_points = documentSnapshot.get("RP",String.class);
                                       int  ranking_points_long = (int) Long.parseLong(ranking_points);

                                        HashMap<String,Object> data = tool.ranking_system(ranking_points_long,context);
                                        int next_rank = (int) data.get("next_rank_rp");
                                        progressBar.setMax(next_rank);
                                        progressBar.setProgress(ranking_points_long);

                                        long rank_int;
                                        if(documentSnapshot.get("rank") == null){
                                            rank_int = 1;
                                        }else{
                                            rank_int = (long) documentSnapshot.get("rank");

                                        }
                                        String rank_string = Long.toString(rank_int);

                                        if(ranking_points != null){
                                            rp.setText(" * " + ranking_points + "/" + Integer.toString(next_rank));
                                            Drawable icon = (Drawable) data.get("icon");
                                            PorterDuffColorFilter blue_color = new PorterDuffColorFilter(Color.BLUE,
                                                    PorterDuff.Mode.SRC_ATOP);
                                            icon.setColorFilter(blue_color);

                                            rank_image.setImageDrawable(icon);


                                        }
                                        rp.setTextColor(context.getResources().getColor(R.color.dark_red));
                                        HashMap<String,Object> ranking_history = documentSnapshot.get("ranking_history",HashMap.class);
                                        store.collection("users").document(pupil_name).collection("ranking_history")
                                                        .get()
                                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                        for(DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                                                                            HashMap<String,Object> test_details = (HashMap<String, Object>) snapshot.getData();
                                                                            CustomPupilCard card = new CustomPupilCard(context);
                                                                            LinearLayout base_layout = (LinearLayout) card.getChildAt(0);
                                                                            TextView test_name_view = (TextView) base_layout.getChildAt(0);
                                                                            CustomCardElement first_test = (CustomCardElement) base_layout.getChildAt(1);
                                                                            CustomCardElement improvement_test = (CustomCardElement) base_layout.getChildAt(2);
                                                                            test_name_view.setText((String) test_details.get("name"));
                                                                            Boolean number;

                                                                            try{
                                                                                Long date_long = Long.parseLong((String)test_details.get("name"));
                                                                                Date date = new Date(date_long);
                                                                                test_name_view.setText(formatter.format(date));
                                                                                number = true;
                                                                            }catch(Exception e){
                                                                                number = false;
                                                                            }
                                                                            LinearLayout first_test_base_layout = (LinearLayout) first_test.getChildAt(0);
                                                                            LinearLayout improvement_test_base_layout = (LinearLayout) improvement_test.getChildAt(0);
                                                                            TextView first_test_view_name = (TextView) first_test_base_layout.getChildAt(0);
                                                                            TextView first_test_view_points = (TextView) first_test_base_layout.getChildAt(1);
                                                                            if (number) {
                                                                                first_test_view_name.setText(context.getResources().getString(R.string.mark) + test_details.get("mark"));
                                                                                first_test_view_points.setText("+" + test_details.get("points"));
                                                                            }else{
                                                                                first_test_view_name.setText(context.getResources().getString(R.string.first_test_mark) + test_details.get("mark"));
                                                                                first_test_view_points.setText("+" + test_details.get("points"));
                                                                            }

                                                                            if(test_details.get("improvement") != null){
                                                                                HashMap<String,Object> improvement = (HashMap<String, Object>) test_details.get("improvement");
                                                                                String improvement_mark = (String) improvement.get("improvement_mark");
                                                                                String improvement_points = (String)improvement.get("improvement_points");

                                                                                TextView improvement_test_view_name = (TextView) improvement_test_base_layout.getChildAt(0);
                                                                                TextView improvement_test_view_points = (TextView) improvement_test_base_layout.getChildAt(1);
                                                                                improvement_test_view_name.setText(context.getResources().getString(R.string.improvement_test_mark) + improvement_mark);
                                                                                improvement_test_view_points.setText("+" + improvement_points);


                                                                            }else{
                                                                                TextView improvement_test_view_name = (TextView) improvement_test_base_layout.getChildAt(0);
                                                                                TextView improvement_test_view_points = (TextView) improvement_test_base_layout.getChildAt(1);
                                                                                if(number){
                                                                                    improvement_test_view_name.setText("");
                                                                                    improvement_test_view_points.setText("");
                                                                                }else{
                                                                                    improvement_test_view_name.setText(R.string.not_yet_held);
                                                                                    improvement_test_view_points.setText("");
                                                                                }

                                                                            }
                                                                            rank_history.addView(card);
                                                                        }
                                                                    }
                                                                });

                                        rank.setText("Rank :" + rank_string);
                                    }
                                });
                        String user_subject;
                        if(documentSnapshot.getString("user_category").equals("1")){
                            user_subject = (String) documentSnapshot.get("user_subject");
                        }else{
                            user_subject = "overall";
                            if(documentSnapshot.contains("to_be_prized")){
                                HashMap<String,Object> user_data = (HashMap<String, Object>) documentSnapshot.getData();
                                user_data.remove("to_be_prized");
                                store.collection("users").document(user.getDisplayName())
                                        .set(user_data);
                                new AlertDialog.Builder(context)
                                        .setTitle(R.string.congrats)
                                        .setMessage(R.string.received_mark)
                                        .setPositiveButton(R.string.ye, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(context, CSList.class);
                                                i.putExtra("class_marked",pupil_class);
                                                i.putExtra("pupil_name",user.getDisplayName());
                                                i.putExtra("prize","yes");

                                                context.startActivity(i);
                                                dialog.dismiss();
                                            }
                                        })
                                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();

                            };
                        }




                        store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(pupil_class).collection("pupils").document(pupil_name).collection("absences")
                                .document(user_subject).collection("absences")
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
                                .document(user_subject).collection("marks")
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
