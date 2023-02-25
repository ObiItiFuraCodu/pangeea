package com.example.pangeea;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Add_hour extends AppCompatActivity {
    int time;
    Calendar date;
    //int dateinmillis;
    DatabaseConnector connector = new DatabaseConnector(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hour);
        Bundle e = getIntent().getExtras();
        final View dialogView = View.inflate(this, R.layout.activity_add_hour, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        Button button = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();

            }
        });



    }
    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        Bundle e = getIntent().getExtras();
        EditText details = findViewById(R.id.details);


        new DatePickerDialog(Add_hour.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(Add_hour.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        Log.v(TAG, "The choosen one " + date.getTime());
                        if(e.getString("hour/task").equals("hour")){
                            connector.add_hour((int) date.getTimeInMillis(), (String) e.get("class_selected"),details.getText().toString());

                        }else{
                            connector.add_task((int) date.getTimeInMillis(), (String) e.get("class_selected"),details.getText().toString());
                        }
                        startActivity(new Intent(Add_hour.this,MainActivity.class));

                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

}

