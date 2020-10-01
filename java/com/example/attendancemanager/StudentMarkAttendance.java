package com.example.attendancemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class StudentMarkAttendance extends AppCompatActivity {
    Spinner courseSpinner, classSpinner;
    private String codeClass;
    private String url = "https://attendance-gbuapi.herokuapp.com/api/mark/attendance";
    private int key;
    private String course;
    private FirebaseUser name;
    private EditText get_otp, get_roll, get_name;
    Button submit, exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_mark_attendance);
        //spinner_begin
        courseSpinner = (Spinner) findViewById(R.id.spinner1);
        classSpinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.classCode, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(adapter);
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                course = courseSpinner.getSelectedItem().toString();
                System.out.println(course);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(StudentMarkAttendance.this, "Please select one of the Course Code", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.courses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter1);
        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                codeClass = classSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(StudentMarkAttendance.this, "Please select your class", Toast.LENGTH_SHORT).show();

            }
        });
        //spinner_ends
        get_otp = (EditText) findViewById(R.id.get_otp);
        get_roll = (EditText) findViewById(R.id.get_roll);
        get_name = (EditText) findViewById(R.id.get_name);
        submit = (Button) findViewById(R.id.submit);
        exit = (Button) findViewById(R.id.exit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InsertSV();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(StudentMarkAttendance.this, "Please wait", Toast.LENGTH_SHORT).show();
                submit.setVisibility(View.GONE);
                exit.setVisibility(View.VISIBLE);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StudentMarkAttendance.this, "Back to Dashboard!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StudentMarkAttendance.this, MainDashboard.class);
                startActivity(intent);
            }
        });

    }
    private void InsertSV() throws JSONException {
        String url = "https://attendance-gbuapi.herokuapp.com/api/mark/attendance";
        JSONObject jsonBody = new JSONObject();
        RequestQueue requestQueue = Volley.newRequestQueue(StudentMarkAttendance.this);
        String name = get_name.getText().toString();
        String roll = get_roll.getText().toString();
        String otp = get_otp.getText().toString();
        jsonBody.put("name", name);
        jsonBody.put("courseCode", course);
        jsonBody.put("class", codeClass);
        jsonBody.put("otp", otp);
        jsonBody.put("rollno", roll);
        System.out.println(jsonBody);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url
                , jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        if (response != null){
                            openDialog();
                        } else {
                            Toast.makeText(StudentMarkAttendance.this, "Please re-check the OTP", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.networkResponse);
            }
        }) {
        };
        requestQueue.add(jsonObjectRequest);
    }
    public void openDialog() {
        AttendanceDialogBox exampleDialog = new AttendanceDialogBox();
        exampleDialog.show(getSupportFragmentManager(), "Attendance dialog box");
    }

}
