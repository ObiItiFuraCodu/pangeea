package com.example.pangeea.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pangeea.backend.CatalogueBackend;
import com.example.pangeea.backend.DatabaseConnector;
import com.example.pangeea.R;
import com.example.pangeea.hour.Add_hour;
import com.example.pangeea.test.Add_test;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class Class_info extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    Calendar date;
    int dateinmillis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);
        String classname = getIntent().getStringExtra("class_selected");
        TextView hour = findViewById(R.id.classname);
        hour.setText("Class name : " + classname);
        if(getIntent().getStringExtra("pupil") != null){
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view2);
            navigationView.setNavigationItemSelectedListener(this);

            drawerLayout = findViewById(R.id.drawer_layout_class_info);
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }




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

            case R.id.nav_add_task: {
                Intent i = new Intent(Class_info.this, Add_hour.class);
                i.putExtra("class_selected",getIntent().getExtras().getString("class_selected"));
                i.putExtra("hour/task","task");
                startActivity(i);
                finish();
                break;
            }
            case R.id.nav_add_test: {
                Intent i = new Intent(Class_info.this, Add_test.class);
                i.putExtra("class_selected",getIntent().getExtras().getString("class_selected"));
                startActivity(i);
                finish();
                break;
            }
            case R.id.nav_add_hour: {
                Intent i = new Intent(Class_info.this,Add_hour.class);
                i.putExtra("class_selected",getIntent().getExtras().getString("class_selected"));
                i.putExtra("hour/task","hour");
                startActivity(i);
                finish();
                break;


            }
        }

        return true;
    }



    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = getIntent().getExtras();
        TextView status = findViewById(R.id.number_of_pupils);

        CatalogueBackend connector = new CatalogueBackend(Class_info.this);
        LinearLayout pupil_list = findViewById(R.id.linear_layout);
        connector.retrieve_class_info(bundle.getString("class_selected"),pupil_list,status);
    }
}