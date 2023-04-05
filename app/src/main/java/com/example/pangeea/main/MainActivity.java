package com.example.pangeea.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pangeea.backend.DatabaseConnector;
import com.example.pangeea.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseConnector connector = new DatabaseConnector(this);
    FirebaseFirestore store = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView username = findViewById(R.id.Username);
        TextView hs = findViewById(R.id.hs);
        String highschool;

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
                auth.signOut();

                startActivity(new Intent(MainActivity.this, Login.class));
                break;
            }
            case R.id.nav_add_hour: {


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


                        }else{

                            connector.retrieveclasses((Spinner) navigationView.getMenu().findItem(R.id.nav_add_hour).getActionView());

                        }
                    }
                });

        LinearLayout linear = findViewById(R.id.liner);
        connector.import_hours(linear);
        connector.import_tasks(linear);
        connector.import_tests(linear);

    }
}