package com.example.pangeea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class Class_info extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);
        String classname = getIntent().getStringExtra("classname");
        TextView hour = findViewById(R.id.classname);
        TextView status = findViewById(R.id.number_of_pupils);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.drawer_layout_class_info);
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

            case R.id.nav_add_test: {

                break;
            }
            case R.id.nav_add_hour: {
                Intent i = new Intent(Class_info.this,Add_hour.class);
                i.putExtra("class_selected",getIntent().getExtras().getString("class_selected"));
                startActivity(i);


            }
        }

        return true;
    }




    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = getIntent().getExtras();
        DatabaseConnector connector = new DatabaseConnector(Class_info.this);
        LinearLayout pupil_list = findViewById(R.id.linear_layout);
        connector.retrieve_class_info(bundle.getString("class_selected"),pupil_list);
    }
}