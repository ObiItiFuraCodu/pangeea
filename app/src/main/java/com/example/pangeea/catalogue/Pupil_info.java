package com.example.pangeea.catalogue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.pangeea.backend.CatalogueBackend;
import com.example.pangeea.backend.DatabaseConnector;
import com.example.pangeea.R;
import com.google.android.material.navigation.NavigationView;

public class Pupil_info extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    CatalogueBackend connector = new CatalogueBackend(this);
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pupil_info);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle e = getIntent().getExtras();

        connector.retrieve_pupil_info(e.getString("pupil_name"),e.getString("pupil_class"),findViewById(R.id.mark_list),findViewById(R.id.absence_list));
        TextView pupil_name = findViewById(R.id.textView20);
        pupil_name.setText(e.getString("pupil_name"));


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

            case R.id.nav_add_mark: {
                Bundle e = getIntent().getExtras();

                Intent i = new Intent(Pupil_info.this, Add_mark.class);
                i.putExtra("class",e.getString("pupil_class"));
                i.putExtra("name",e.getString("pupil_name"));
                startActivity(i);
                finish();
            }

        }

        return true;
    }
}