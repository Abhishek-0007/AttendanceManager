package com.example.attendancemanager;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import android.os.Bundle;

public class FacultyLoginScreen extends AppCompatActivity {
    Button mark, view;
    TextView get_mark, get_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_login_screen);
        mark = (Button) findViewById(R.id.mark);
        view = (Button) findViewById(R.id.view);
        get_mark = (TextView) findViewById(R.id.h);
        get_view = (TextView) findViewById(R.id.g);

        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FacultyLoginScreen.this, FacultyForAttendance.class);
                startActivity(intent);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FacultyLoginScreen.this, FacultyLoginScreen.class);
                startActivity(intent);
            }
        });
        get_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FacultyLoginScreen.this, FacultyLoginScreen.class);
                startActivity(intent);
            }
        });
        get_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FacultyLoginScreen.this, FacultyForAttendance.class);
                startActivity(intent);
            }
        });

    }
}