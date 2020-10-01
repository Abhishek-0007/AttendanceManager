package com.example.attendancemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;

public class FacultyForAttendance extends AppCompatActivity {
    private TextView list;
    private String code, statusPortal, idPortal;
    private String teachedID;
    private Button submit, portalBTN, refresh;
    ProgressBar progressBar;
    Spinner courseSpinner, classSpinner;
    private String codeClass, course;
    String url = "https://attendance-gbuapi.herokuapp.com/api/generate/attendance/portal";

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_for_attendance);
        submit = (Button) findViewById(R.id.submit);
        portalBTN = (Button) findViewById(R.id.portal);
        String uid = FirebaseAuth.getInstance().getUid();
        teachedID = uid;
        progressBar = (ProgressBar)findViewById(R.id.progress);
        courseSpinner = (Spinner) findViewById(R.id.spinner1);
        classSpinner = (Spinner) findViewById(R.id.spinner2);

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
                Toast.makeText(FacultyForAttendance.this, "Please select one of the Course Code", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(FacultyForAttendance.this, "Please select your class", Toast.LENGTH_SHORT).show();

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);
                try {
                    InsertSV();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(FacultyForAttendance.this, "Please Wait, OTP is generating!", Toast.LENGTH_SHORT).show();
            }
        });
        portalBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FacultyForAttendance.this, AttendanceStatusView.class);
                intent.putExtra("COURSE", course);
                intent.putExtra("CLASS", codeClass);
                intent.putExtra("OTP_VIEW", code);
                intent.putExtra("status", statusPortal);
                intent.putExtra("_id", idPortal);
                startActivity(intent);
            }
        });
    }
    private void InsertSV() throws JSONException {
        JSONObject jsonBody = new JSONObject();
        RequestQueue requestQueue = Volley.newRequestQueue(FacultyForAttendance.this);
        jsonBody.put("teacherId", teachedID);
        jsonBody.put("courseCode", course);
        jsonBody.put("class", codeClass);
        jsonBody.put("secret", new Date()+"secret hai");
//        System.out.println(new Date());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url
                , jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            JSONObject porTal = response.getJSONObject("portal");
                            String status = porTal.getString("status");
                            String portalID = porTal.getString("_id");
                            idPortal = portalID;
                            statusPortal = status;
                            code = response.getString("otp");
                            for (int i = 0; i < 500 ; i++){
                                if(code == null){
                                    progressBar.setProgress(25);
                                } else {
                                    progressBar.setProgress(100);
                                    submit.setVisibility(View.INVISIBLE);
                                    portalBTN.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }


                            //System.out.println(otp);
                            //list.setText(otp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }) {
        };
        requestQueue.add(jsonObjectRequest);
    }
}
