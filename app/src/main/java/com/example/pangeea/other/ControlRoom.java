package com.example.pangeea.other;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pangeea.main.MainActivity;
import com.example.pangeea.R;
import com.google.firebase.auth.FirebaseAuth;

public class ControlRoom extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_room);
        Button butt = findViewById(R.id.controlbutt);
        TextView view = findViewById(R.id.textView3);
        view.setText(auth.getCurrentUser().getDisplayName());
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), MainActivity.class));
            }
        });
    }
}