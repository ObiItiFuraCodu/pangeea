package com.example.pangeea;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Add_hour extends AppCompatActivity {
    int time;
    Calendar date;
    //int dateinmillis;
    DatabaseConnector connector = new DatabaseConnector(this);
    List<Uri> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hour);
        Bundle e = getIntent().getExtras();

        final View dialogView = View.inflate(this, R.layout.activity_add_hour, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        Button select_date = findViewById(R.id.select_date_time);
        Button upload_lessons = findViewById(R.id.upload_button);
        Button add_ai = findViewById(R.id.generate_ai);
        Button add = findViewById(R.id.add_button);
        EditText details = findViewById(R.id.details);



        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();

            }
        });
        upload_lessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });
        add_ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e.getString("hour/task").equals("hour")){
                    connector.add_hour((int) date.getTimeInMillis(), (String) e.get("class_selected"),details.getText().toString(),list);

                }else{
                    connector.add_task((int) date.getTimeInMillis(), (String) e.get("class_selected"),details.getText().toString(),list);
                }
                startActivity(new Intent(Add_hour.this,MainActivity.class));

            }
        });






    }
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        list.add(data.getData());

                    }
                }
            });
    public void openFile(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent = Intent.createChooser(intent,"Choose NOW");
        activityResultLauncher.launch(intent);
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();


        new DatePickerDialog(Add_hour.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(Add_hour.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);


                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

}

