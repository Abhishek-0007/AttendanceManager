package com.example.attendancemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.Date;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.Year;
import java.util.Calendar;

import okhttp3.OkHttpClient;

public class FacultyViewAttendance extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    Spinner courseSpinner, classSpinner;
    private final OkHttpClient client = new OkHttpClient();
    private RequestQueue requestQueue;
    private String codeClass, course;
    private String date;
    DatePicker picker;
    TextView viewDate, name, roll;
    Button showDate, attend;
    LinearLayout linearLayout;
    ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_view_attendance);
        requestQueue = Volley.newRequestQueue(this);
        courseSpinner = (Spinner) findViewById(R.id.spinner1);
        classSpinner = (Spinner) findViewById(R.id.spinner2);
        viewDate = (TextView) findViewById(R.id.dateView);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        name = (TextView) findViewById(R.id.name);
        roll = (TextView) findViewById(R.id.roll);
        showDate = (Button) findViewById(R.id.dateBtn);
        scrollView = (ScrollView) findViewById(R.id.scroll);
        attend = (Button) findViewById(R.id.attend);
        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonParse();
                linearLayout.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.VISIBLE);
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
                Toast.makeText(FacultyViewAttendance.this, "Please select one of the Course Code", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(FacultyViewAttendance.this, "Please select your class", Toast.LENGTH_SHORT).show();

            }
        });
        //date
        showDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateDialog();
            }
        });
    }
    //date dialog box
    public void dateDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH)+1,
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if (month < 10){
            date = year + "-0" + month + "-" + day;
        } else {
            date = year + "-" + month + "-" + day;

        }
        viewDate.setText(date);
    }
    private void jsonParse() {
        System.out.println("hey");
        String url = ("https://attendance-gbuapi.herokuapp.com/api/show/portal?coursecode="+course+"&class="+codeClass+"&date="+date);
        System.out.println(url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("attendance");
                            for (int i=0; i < jsonArray.length(); i++) {
                                JSONObject students = jsonArray.getJSONObject(i);
                                String Name = students.getString("name");
                                String rollno = students.getString("rollno");
                                System.out.println(response);
                                name.append(""+Name + "\n\n");
                                roll.append("" + rollno + "\n\n");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println(error);
            }
        });
        requestQueue.add(request);
    }

}