package com.example.pangeea.hour;

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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.pangeea.backend.DatabaseConnector;
import com.example.pangeea.backend.HourBackend;
import com.example.pangeea.backend.TaskBackend;
import com.example.pangeea.main.MainActivity;
import com.example.pangeea.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class Add_hour extends AppCompatActivity {
    int time;
    Calendar date;
    int dateinmillis;
    DatabaseConnector connector = new DatabaseConnector(this);
    List<Uri> list = new ArrayList<>();
    List<String> stringlist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hour);
        Bundle e = getIntent().getExtras();
        HourBackend backend1 = new HourBackend(this);
        TaskBackend backend2 = new TaskBackend(this);

        final View dialogView = View.inflate(this, R.layout.activity_add_hour, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        EditText title = findViewById(R.id.lesson_title);


        Button select_date = findViewById(R.id.select_date_time);
        Button upload_lessons = findViewById(R.id.add_lesson);
        Spinner support_lessons = findViewById(R.id.support_lessons);
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
       /* add_ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e.getString("hour/task").equals("hour")){

                    backend1.add_hour(date.getTimeInMillis(), (String) e.get("class_selected"),details.getText().toString(),list,title.getText().toString());

                }else{
                    backend2.add_task(date.getTimeInMillis(), (String) e.get("class_selected"),details.getText().toString(),list,title.getText().toString());
                }
                startActivity(new Intent(Add_hour.this, MainActivity.class));

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
                        stringlist.add(data.getData().getLastPathSegment());
                        Spinner spinner = findViewById(R.id.support_lessons);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Add_hour.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,stringlist);
                        spinner.setAdapter(adapter);


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
        final Calendar currentDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));


        new DatePickerDialog(Add_hour.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(Add_hour.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        dateinmillis = (int) date.getTimeInMillis();



                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

}

