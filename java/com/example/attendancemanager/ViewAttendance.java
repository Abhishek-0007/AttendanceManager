package com.example.attendancemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;

public class ViewAttendance extends AppCompatActivity {
    private final OkHttpClient client = new OkHttpClient();
    private RequestQueue requestQueue;
    private TextView list, rollno;
    Button get;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);
        requestQueue = Volley.newRequestQueue(this);
        //ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_view_attendance,list);
        get = (Button) findViewById(R.id.getAttendance);
        list = (TextView) findViewById(R.id.list);
        rollno = (TextView) findViewById(R.id.roll);
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonParse();
            }
        });
    }
        private void jsonParse() {
        System.out.println("hey");
        String url = "https://attendance-gbuapi.herokuapp.com/api/show/portal?coursecode=CS-II-B&class=CS-201&date=2020-09-26";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("attendance");
                            for (int i=0; i < jsonArray.length(); i++) {
                                JSONObject students = jsonArray.getJSONObject(i);
                                String name = students.getString("name");
                                String roll = students.getString("rollno");
                                //list.setText(roll);
                                System.out.println(response);
                                list.append(""+name + "\n\n");
                                rollno.append("" + roll + "\n\n");


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }
}