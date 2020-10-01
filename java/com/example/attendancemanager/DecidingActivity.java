package com.example.attendancemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DecidingActivity extends AppCompatActivity {

    Button teacher, student, view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deciding);
    teacher = (Button) findViewById(R.id.teachers);
    student = (Button) findViewById(R.id.students);
    view = (Button) findViewById(R.id.view);
    teacher.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(DecidingActivity.this, FacultyForAttendance.class);
            //openDialog();
            startActivity(intent);
        }
    });
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DecidingActivity.this, StudentMarkAttendance.class);
                startActivity(intent);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DecidingActivity.this, StudentViewAttendance.class);
                startActivity(intent);
            }
        });

    }
    public void openDialog() {
        AttendanceDialogBox exampleDialog = new AttendanceDialogBox();
        exampleDialog.show(getSupportFragmentManager(), "Attendance dialog box");
    }
}