package com.example.pangeea.test;

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
import com.example.pangeea.main.MainActivity;
import com.example.pangeea.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class Add_test extends AppCompatActivity {

    int time;
    Calendar date;
    int dateinmillis;
    DatabaseConnector connector = new DatabaseConnector(this);
    List<Uri> list = new ArrayList<>();
    List<String> stringlist = new ArrayList<>();
    List<HashMap<String,String>> questions_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hour);
        Bundle e = getIntent().getExtras();

        final View dialogView = View.inflate(this, R.layout.activity_add_hour, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        EditText title = findViewById(R.id.test_title);


        Button select_date = findViewById(R.id.select_test_date);
        Button upload_lessons = findViewById(R.id.add_test_support_lesson);
        //Spinner support_lessons = findViewById(R.id.test_support_lessons);

        Spinner test_questions = findViewById(R.id.test_questions);
        Button add_test_question = findViewById(R.id.add_test_question);

        Button add = findViewById(R.id.add_test);
        EditText details = findViewById(R.id.test_details);



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
                connector.add_test(date.getTimeInMillis(), (String) e.get("class_selected"),details.getText().toString(),list,title.getText().toString(),questions_list);

                startActivity(new Intent(Add_test.this, MainActivity.class));

            }
        });

      add_test_question.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
             startActivity(new Intent(v.getContext(), Add_test_question.class));
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Add_test.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,stringlist);
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
    public void add_question(HashMap<String,String> question){
     questions_list.add(question);
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));


        new DatePickerDialog(Add_test.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(Add_test.this, new TimePickerDialog.OnTimeSetListener() {
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