package com.example.pangeea;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Task_info extends AppCompatActivity {

    DatabaseConnector connector = new DatabaseConnector(this);
    Bundle e = getIntent().getExtras();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

    }
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        ///data.getData()
                        TextView helper = findViewById(R.id.helper_text);
                        connector.submit_work(data.getData(),Long.parseLong(e.getString("hour_ms")) ,helper.getText().toString());
                        startActivity(new Intent(Task_info.this,MainActivity.class));


                    }
                }
            });
    public void openFile(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent = Intent.createChooser(intent,"Choose NOW");
        activityResultLauncher.launch(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        connector.retrieve_task_data_elev(e.getString("hour_ms"),findViewById(R.id.lessons_list),findViewById(R.id.submissions_list),findViewById(R.id.helper_text));
        Button submit = findViewById(R.id.submit_work);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 openFile();
            }
        });
    }
}