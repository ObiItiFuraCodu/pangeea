package com.example.pangeea.catalogue;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pangeea.backend.CatalogueBackend;
import com.example.pangeea.backend.DatabaseConnector;
import com.example.pangeea.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Add_mark extends AppCompatActivity {
    String datestring;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mark);
        Bundle e = getIntent().getExtras();
        Button add_date = findViewById(R.id.add_date);
        Button add = findViewById(R.id.add_mark);
        EditText mark = findViewById(R.id.mark);
        CatalogueBackend connector = new CatalogueBackend(this);
        add_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mark.getText().toString().contains("[a-zA-Z]") == false){
                    connector.upload_mark(e.getString("class"),e.getString("name"),mark.getText().toString(),datestring);

                }else{
                    Toast.makeText(Add_mark.this,"Invalid mark",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));


        new DatePickerDialog(Add_mark.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(Add_mark.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        Date date1 = date.getTime();
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

                        datestring = dateFormat.format(date1);



                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

}