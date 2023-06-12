package com.example.pangeea.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pangeea.backend.CatalogueBackend;
import com.example.pangeea.R;
import com.example.pangeea.backend.HourBackend;
import com.example.pangeea.backend.TaskBackend;
import com.example.pangeea.backend.TestBackend;
import com.example.pangeea.other.NFC_detection;
import com.example.pangeea.test.See_corrected_tests;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    CatalogueBackend connector = new CatalogueBackend(this);
    HourBackend backend1 = new HourBackend(this);
    TaskBackend backend2 = new TaskBackend(this);
    TestBackend backend3 = new TestBackend(this);
    FirebaseFirestore store = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);









        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;



            return super.onOptionsItemSelected(item);


    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_logout: {
                connector.log_out();
                finish();
                break;
            }
            case R.id.nav_add_hour: {


            }
            case R.id.see_corrected_tests:{
                startActivity(new Intent(MainActivity.this, See_corrected_tests.class));
                finish();
            }
            case R.id.pair_device:{
                Intent i = new Intent(MainActivity.this, NFC_detection.class);
                i.putExtra("Pair","ye");
                startActivity(i);
            }
        }

        return true;
    }
    @Override
    protected void onStart() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        super.onStart();

        store.collection("users").document(auth.getCurrentUser().getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        if(documentSnapshot.getString("user_category").equals("0")){
                            //navigationView.getMenu().findItem(R.id.nav_add_hour).setVisible(false);
                            connector.retrieve_materies((Spinner) navigationView.getMenu().findItem(R.id.nav_add_hour).getActionView());
                            navigationView.getMenu().findItem(R.id.nav_add_hour).setTitle("Materies");
                            navigationView.getMenu().findItem(R.id.pair_device).setVisible(false);
                            navigationView.getMenu().findItem(R.id.pair_device).setEnabled(false);

                        }else{
                            navigationView.getMenu().findItem(R.id.see_corrected_tests).setVisible(false);
                            connector.retrieveclasses((Spinner) navigationView.getMenu().findItem(R.id.nav_add_hour).getActionView());

                        }
                    }
                });


        backend1.import_hours(findViewById(R.id.liner3),findViewById(R.id.welcome_back));
        backend2.import_tasks(findViewById(R.id.liner2));
        backend3.import_tests(findViewById(R.id.liner1));

    }

    @Override
    protected void onPause() {
        super.onPause();
        LinearLayout layout = findViewById(R.id.liner1);
        layout.removeAllViews();
    }
}