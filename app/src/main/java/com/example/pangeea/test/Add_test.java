package com.example.pangeea.test;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pangeea.backend.TestBackend;
import com.example.pangeea.hour.Add_hour;
import com.example.pangeea.main.MainActivity;
import com.example.pangeea.R;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class Add_test extends AppCompatActivity {

    int time;
    Calendar date;
    Long dateinmillis;
    TestBackend connector = new TestBackend(this);
    Boolean is_public = false;
    Spinner test_questions;
    List<Uri> list = new ArrayList<>();
    List<String> stringlist = new ArrayList<>();
    List<HashMap<String,Object>> questions_list = new ArrayList<>();
    List<String> question_stringlist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test);
        Bundle e = getIntent().getExtras();

        final View dialogView = View.inflate(this, R.layout.activity_add_hour, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        EditText title = findViewById(R.id.test_title);

        test_questions = findViewById(R.id.test_questions);

        Button select_date = findViewById(R.id.select_test_date);
        ImageButton upload_lessons = findViewById(R.id.add_test_support_lesson);
        TextView ai_generator = findViewById(R.id.ai_generator);

        Spinner support_lessons = findViewById(R.id.test_support_lessons);
        Spinner questions = findViewById(R.id.test_questions);

        ImageButton add_test_question = findViewById(R.id.add_test_question);

        Button add = findViewById(R.id.add_test);
        EditText details = findViewById(R.id.support_less_content);



        ai_generator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),AI_test_generator.class);

                if(date == null && e.get("hour_ms") == null){
                    Toast.makeText(Add_test.this,"you didnt pick a date",Toast.LENGTH_SHORT).show();

                }else{
                    if(e.get("hour_ms") != null){
                        i.putExtra("title",title.getText().toString());
                        i.putExtra("class_name",e.getString("class_selected"));
                        i.putExtra("hour_ms",e.getLong("hour_ms"));
                        i.putExtra("details",details.getText().toString());
                    }else{
                        i.putExtra("title",title.getText().toString());
                        i.putExtra("class_name",e.getString("class_selected"));
                        i.putExtra("hour_ms",date.getTimeInMillis());
                        i.putExtra("details",details.getText().toString());

                    }
                    startActivity(i);
                }


            }
        });
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
                if(date == null && e.get("hour_ms") == null){
                    Toast.makeText(Add_test.this,getResources().getString(R.string.no_time),Toast.LENGTH_SHORT).show();
                }else if(questions_list.isEmpty()){
                    Toast.makeText(Add_test.this, getResources().getString(R.string.no_q),Toast.LENGTH_LONG).show();
                }else{
                    new AlertDialog.Builder(v.getContext())
                            .setTitle(R.string.make_public)
                            .setMessage(R.string.want_to_be_public)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    is_public = true;
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    if(e.get("hour_ms") != null){
                        connector.add_test(e.getLong("hour_ms"), e.getString("class_selected"),details.getText().toString(),list,title.getText().toString(),questions_list,details.getText().toString(),is_public);

                    }else{
                        connector.add_test(date.getTimeInMillis(), e.getString("class_selected"),details.getText().toString(),list,title.getText().toString(),questions_list,details.getText().toString(),is_public);
                    }
                    startActivity(new Intent(Add_test.this, MainActivity.class));

                }


            }
        });

      add_test_question.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
             Intent i = new Intent(Add_test.this,Add_test_question.class);
             if(e.get("questions") != null){
                 i.putExtra("questions", (Serializable) e.get("questions"));
                 i.putExtra("questionnames", (Serializable) e.get("questionnames"));


             }
              i.putExtra("files", (Serializable) list);
              i.putExtra("filenames", (Serializable) stringlist);
              i.putExtra("class_selected",e.getString("class_selected"));
              if(dateinmillis != null){
                  i.putExtra("hour_ms",date.getTimeInMillis());
              }
              startActivity(i);
              finish();

          }
      });
    if(e.get("questions") != null){

        list = (List<Uri>) e.get("files");
        questions_list = (List<HashMap<String, Object>>) e.get("questions");
        question_stringlist = (List<String>) e.get("questionnames");
        stringlist = (List<String>) e.get("filenames");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Add_test.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,question_stringlist);
        questions.setAdapter(adapter);
        ArrayAdapter<String> adapter_files = new ArrayAdapter<String>(Add_test.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,stringlist);
        support_lessons.setAdapter(adapter_files);
     }


    }
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("Range")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Uri uri = data.getData();

                        String fileName = null;
                        if (uri.getScheme().equals("content")) {
                            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                            try {
                                if (cursor != null && cursor.moveToFirst()) {
                                    fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                }
                            } finally {
                                cursor.close();
                            }
                        }
                        if (fileName == null){
                            fileName = uri.getPath();
                            int mark = fileName.lastIndexOf("/");
                            if (mark != -1){
                                fileName = fileName.substring(mark + 1);
                            }
                        }
                        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

                        if((extension.equals("pdf") || extension.equals("jpg") || extension.equals("png") || extension.equals("jpeg"))){
                            list.add(data.getData());
                            stringlist.add(data.getData().getLastPathSegment());
                            Spinner spinner = findViewById(R.id.support_lessons);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Add_test.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,stringlist);
                            spinner.setAdapter(adapter);
                        }else{
                            Toast.makeText(Add_test.this,getResources().getString(R.string.invalid_file),Toast.LENGTH_LONG).show();
                            Log.e("Extension",extension);
                        }



                    }
                }
            });
    public void openFile(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent = Intent.createChooser(intent,"Choose NOW");
        activityResultLauncher.launch(intent);
    }
    public static String getExtension(String fullName) {
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
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
                        dateinmillis =  date.getTimeInMillis();



                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
}