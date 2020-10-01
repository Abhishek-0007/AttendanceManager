package com.example.attendancemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.OkHttpClient;

public class StudentViewAttendance extends AppCompatActivity {
    Spinner courseSpinner, classSpinner;
    private final OkHttpClient client = new OkHttpClient();
    private RequestQueue queue;
    private String codeClass;
    private String course;
    private String rollKey;
    private Integer count ;
    LinearLayout attendance, courseID, classID;
    TextView get_attendance, get_roll;
    EditText rollNum;
    Button showDate, attend;
    LinearLayout linearLayout;
    ScrollView scrollView;
    ProgressBar prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_attendance);
        attendance = (LinearLayout) findViewById(R.id.attendanceList);
        courseID = (LinearLayout) findViewById(R.id.a);
        classID = (LinearLayout) findViewById(R.id.s);
        rollNum = (EditText) findViewById(R.id.rollNum);
        queue = Volley.newRequestQueue(this);
        courseSpinner = (Spinner) findViewById(R.id.spinner1);
        classSpinner = (Spinner) findViewById(R.id.spinner2);
        get_attendance = (TextView) findViewById(R.id.attenNum);
        get_roll = (TextView) findViewById(R.id.attenDate);
        prog = (ProgressBar) findViewById(R.id.prog);
        attend = (Button) findViewById(R.id.attend);
        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //prog.setVisibility(View.VISIBLE);
                getAttendance();
                attendance.setVisibility(View.VISIBLE);
                rollNum.setVisibility(View.GONE);
                courseID.setVisibility(View.GONE);
                classID.setVisibility(View.GONE);
            }
        });
        //Spinners
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
                Toast.makeText(StudentViewAttendance.this, "Please select one of the Course Code", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(StudentViewAttendance.this, "Please select your class", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void getAttendance(){
        String url = ("https://attendance-gbuapi.herokuapp.com/api/show/portal?coursecode="+course+"&class="+codeClass);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    JSONArray jSONArray = new JSONArray(response); count = 0;
                    for (int j = 0; j < jSONArray.length(); j++){

                        JSONArray attendanceArray = ((JSONObject) jSONArray.get(j)).getJSONArray("attendance");
                        for (int i = 0; i < attendanceArray.length(); i++){
                            JSONObject attendance = (JSONObject) attendanceArray.get(i);
                            String roll = attendance.getString("rollno");
                            String date = attendance.getString("createdAt");
                            String cut = date.substring(0, 10);
                            String input = rollNum.getText().toString();
                            System.out.println(input);
                            System.out.println(roll);
                            if(input.equals(roll)){
//                            get_attendance.append(roll);
//                            get_roll.append(cut);
                                count++;
                                prog.setVisibility(View.INVISIBLE);
                            } else {
                                Toast.makeText(StudentViewAttendance.this, "Enter the correct roll number!!", Toast.LENGTH_SHORT).show();
                                prog.setVisibility(View.INVISIBLE);
                            }
                        }

                    }

                    get_roll.setText(count.toString());

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(request);

    }
}