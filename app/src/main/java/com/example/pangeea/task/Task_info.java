package com.example.pangeea.task;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pangeea.backend.DatabaseConnector;
import com.example.pangeea.R;
import com.example.pangeea.backend.TaskBackend;
import com.example.pangeea.main.MainActivity;

public class Task_info extends AppCompatActivity {

    TaskBackend connector = new TaskBackend(this);

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
                        Bundle e = getIntent().getExtras();

                        connector.submit_work(data.getData(),Long.parseLong(e.getString("hour_milis")) ,helper.getText().toString());
                        startActivity(new Intent(Task_info.this, MainActivity.class));


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
        Bundle e = getIntent().getExtras();

        connector.retrieve_task_data_elev(e.getString("hour_milis"),findViewById(R.id.lessons_list),findViewById(R.id.submissions_list),findViewById(R.id.helper_text),findViewById(R.id.ai_button_2),findViewById(R.id.lesson_net_button_2));
        Button submit = findViewById(R.id.submit_work);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 openFile();
            }
        });
    }
}