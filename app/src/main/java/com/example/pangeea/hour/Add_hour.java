package com.example.pangeea.hour;



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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pangeea.Generate_ai_lesson_teacher;
import com.example.pangeea.backend.DatabaseConnector;
import com.example.pangeea.backend.HourBackend;
import com.example.pangeea.backend.TaskBackend;
import com.example.pangeea.main.MainActivity;
import com.example.pangeea.R;

import java.io.File;
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
    Boolean is_public = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hour);
        Bundle e = getIntent().getExtras();
        HourBackend backend1 = new HourBackend(this);
        TaskBackend backend2 = new TaskBackend(this);

        TextView addhour = findViewById(R.id.textView1);

        TextView generate_ai = findViewById(R.id.generate_ai_lesson);

        EditText title = findViewById(R.id.lesson_title);


        TextView select_date = findViewById(R.id.select_date_time);
        TextView upload_lessons = findViewById(R.id.add_lesson);
        EditText support_lesson_content = findViewById(R.id.support_lesson_content);
        Spinner support_lessons = findViewById(R.id.support_lessons);
        TextView add = findViewById(R.id.add_button);
        EditText details = findViewById(R.id.details);
        if(e.getString("lesson_content") != null){
            support_lesson_content.setText(e.getString("lesson_content"));
            title.setText(e.getString("title"));
        }



        if(e.getString("hour/task").equals("hour")){
            addhour.setText("ADD AN HOUR");
        }else{
            addhour.setText("ADD A TASK");
        }
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
        generate_ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Add_hour.this, Generate_ai_lesson_teacher.class);
                i.putExtra("hour/task",e.getString("hour/task"));
                i.putExtra("class_selected",e.getString("class_selected"));
                if(title.getText().toString().equals("")){
                    Toast.makeText(v.getContext(),"you didn't enter a title",Toast.LENGTH_LONG).show();

                }else{
                    i.putExtra("title",title.getText().toString());
                    startActivity(i);
                    finish();
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(date == null){
                    Toast.makeText(Add_hour.this,"you didn't pick a time",Toast.LENGTH_LONG).show();
                }else{
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Make public")
                            .setMessage("Do you want the lesson to be public?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    is_public = true;
                                    if(e.getString("hour/task").equals("hour")){
                                        backend1.add_hour(date.getTimeInMillis(), (String) e.get("class_selected"),details.getText().toString(),list,title.getText().toString(),support_lesson_content.getText().toString(),is_public);
                                    }else{
                                        backend2.add_task(date.getTimeInMillis(), (String) e.get("class_selected"),details.getText().toString(),list,title.getText().toString(),support_lesson_content.getText().toString(),is_public);
                                    }
                                    startActivity(new Intent(Add_hour.this, MainActivity.class));
                                    finish();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(e.getString("hour/task").equals("hour")){

                                        backend1.add_hour(date.getTimeInMillis(), (String) e.get("class_selected"),details.getText().toString(),list,title.getText().toString(),support_lesson_content.getText().toString(),is_public);

                                    }else{
                                        backend2.add_task(date.getTimeInMillis(), (String) e.get("class_selected"),details.getText().toString(),list,title.getText().toString(),support_lesson_content.getText().toString(),is_public);
                                    }
                                    startActivity(new Intent(Add_hour.this, MainActivity.class));
                                    finish();
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }



            }
        });





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
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Add_hour.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,stringlist);
                            spinner.setAdapter(adapter);
                        }else{
                            Toast.makeText(Add_hour.this,"Invalid file",Toast.LENGTH_LONG).show();
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

