package com.example.attendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.sax.Element;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.sanojpunchihewa.glowbutton.GlowButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;

import okhttp3.OkHttpClient;
import ticker.views.com.ticker.widgets.circular.timer.callbacks.CircularViewCallback;
import ticker.views.com.ticker.widgets.circular.timer.view.CircularView;

import android.os.Bundle;

public class AttendanceStatusView extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private int mMenuId;
    private static final int STORAGE_CODE = 1000;
    private String course;
    private String codeClass;
    private int count=1;
    TextView list, status, rollNum;
    LinearLayout viewOtp, viewAttendance;
    TextView textView1,textView2,textView3,textView4,textView5,textView6, stopText;
    private TextView merge;
    private String number;
    CircularView circularView;
    GlowButton online, cross;
    ProgressBar bar;
    TextView attendees;
    Button close, refresh, view;
    private RequestQueue requestQueue;
    private final OkHttpClient client = new OkHttpClient();
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_status_view);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        online = (GlowButton) findViewById(R.id.online);
        bar = (ProgressBar) findViewById(R.id.bar);
        cross = (GlowButton) findViewById(R.id.cross);
//        stopImg = (ImageView) findViewById(R.id.stopimg);
//        stopText = (TextView) findViewById(R.id.stoptext);
        requestQueue = Volley.newRequestQueue(this);
        refresh = (Button) findViewById(R.id.refresh);
        attendees = (TextView) findViewById(R.id.attendanceNum);
        circularView = (CircularView) findViewById(R.id.circular_view);
        circularView.startTimer();
        merge = (TextView) findViewById(R.id.merge);
        bar = (ProgressBar) findViewById(R.id.bar);
        textView1 = (TextView) findViewById(R.id.textview1);
        textView2 = (TextView) findViewById(R.id.textview2);
        textView3 = (TextView) findViewById(R.id.textview3);
        textView4 = (TextView) findViewById(R.id.textview4);
        textView5 = (TextView) findViewById(R.id.textview5);
        textView6 = (TextView) findViewById(R.id.textview6);
        status = (TextView) findViewById(R.id.status);
        viewOtp = (LinearLayout) findViewById(R.id.viewOtp);
        viewAttendance = (LinearLayout) findViewById(R.id.viewAttendance);
        list = (TextView) findViewById(R.id.attedanceName);
        rollNum = (TextView) findViewById(R.id.attedanceRoll);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mMenuId = item.getItemId();

                for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
                    MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
                    boolean isChecked = menuItem.getItemId() == item.getItemId();
                    menuItem.setChecked(isChecked);
                }
                switch (item.getItemId()) {
                    case R.id.nav_download_attendance:{
                        getPortalStatus();
                        if(count == 1){
                            Toast.makeText(AttendanceStatusView.this, "Stop the portal", Toast.LENGTH_SHORT).show();
                        } else if (count == 0){
                            cross.setVisibility(View.GONE);
                            circularView.setVisibility(View.GONE);
                            viewOtp.setVisibility(View.GONE);
                            attendees.setVisibility(View.GONE);
                            status.setVisibility(View.GONE);
                            viewOtp.setVisibility(View.GONE);
                            viewAttendance.setVisibility(View.VISIBLE);
                            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                                    String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                    requestPermissions(permission, STORAGE_CODE);
                                } else {
                                    savePDF();
                                }
                            } else {
                                savePDF();
                            }

                        }

                    }
                    break;
                    case R.id.nav_view_list: {
                        getPortalStatus();
                        if(count == 1){
                            Toast.makeText(AttendanceStatusView.this, "Stop the portal", Toast.LENGTH_SHORT).show();
                        } else if (count == 0){
                            getAttendance();
                            cross.setVisibility(View.GONE);
                            circularView.setVisibility(View.GONE);
                            viewOtp.setVisibility(View.GONE);
                            attendees.setVisibility(View.GONE);
                            status.setVisibility(View.GONE);
                            viewOtp.setVisibility(View.GONE);
                            viewAttendance.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(AttendanceStatusView.this, "view attendance", Toast.LENGTH_SHORT).show();
                        handler2.post(timedTask2);
                    }
                    break;
                    case R.id.nav_stop_portal: {
                         closePortal();
                         attendees.setVisibility(View.GONE);
                        viewOtp.setVisibility(View.GONE);
                        handler1.post(timedTask1);
                        circularView.setVisibility(View.GONE);
                        Toast.makeText(AttendanceStatusView.this, "Portal Closing", Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                return true;
            }
        });

        final String code = getIntent().getStringExtra("OTP_VIEW");
        course = getIntent().getStringExtra("COURSE");
        codeClass = getIntent().getStringExtra("CLASS");
        char[] ch = new char[code.length()];
        for (int i = 0; i < code.length(); i++) {
            ch[i] = code.charAt(i);
        }
        for (int i = 0; i < ch.length; i++) {

            if (i == 0){
                textView1.setText("" + ch[i]);

            } else if (i == 1) {
                 textView2.setText("" + ch[i]);

            } else if (i == 2) {
                 textView3.setText("" + ch[i]);

            } else if (i == 3) {
                 textView4.setText("" + ch[i]);

            } else if (i == 4) {
                 textView5.setText("" + ch[i]);

            } else if (i == 5) {
                System.out.println(ch[i]);
                 textView6.setText("" + ch[i]);
            }
        }
        CircularView.OptionsBuilder builderWithTimer =
                new CircularView.OptionsBuilder()
                        .shouldDisplayText(true)
                        .setCounterInSeconds(300)
                        .setCircularViewCallback(new CircularViewCallback() {
                            @Override
                            public void onTimerFinish() {

                                // Will be called if times up of countdown timer
                                closePortal();
                                handler1.post(timedTask1);
                                Toast.makeText(AttendanceStatusView.this, "Portal has been closed!! ", Toast.LENGTH_SHORT).show();
                                circularView.setVisibility(View.GONE);
                                viewOtp.setVisibility(View.GONE);
                                Toast.makeText(AttendanceStatusView.this, "You can view attendance now.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onTimerCancelled() {

                                // Will be called if stopTimer is called
                                //Toast.makeText(AttendanceStatusView.this, "CircularCallback: Timer Cancelled ", Toast.LENGTH_SHORT).show();
                            }
                        });

        circularView.setOptions(builderWithTimer);
        final String statusPortal = getIntent().getStringExtra("status");
        final String idPortal = getIntent().getStringExtra("_id");
        id = idPortal;
        System.out.println(statusPortal);
        //fuction calling
        doSomething();
        getNumberOfAttendance();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler1.post(timedTask1);
                handler2.post(timedTask2);
                getNumberOfAttendance();
                getPortalStatus();
            }
        });
    }



    private void getNumberOfAttendance() {
        String url = "https://attendance-gbuapi.herokuapp.com/api/show/portal/" + id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("attendance");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject students = jsonArray.getJSONObject(i);
                                number = String.valueOf(jsonArray.length());
                                attendees.setText("Number of attendence marked: " + number);
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

    Handler handler2 = new Handler();
    Runnable timedTask2 = new Runnable() {
        @Override
        public void run() {
            getNumberOfAttendance();
            handler2.postDelayed(timedTask2, 100);
        }
    };
    Handler handler1 = new Handler();
    Runnable timedTask1 = new Runnable() {
        @Override
        public void run() {
            getPortalStatus();
            handler1.postDelayed(timedTask1, 100);
        }
    };


    private void getPortalStatus() {
        String url = "https://attendance-gbuapi.herokuapp.com/api/show/portal/" + id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("true")) {
                                online.setVisibility(View.VISIBLE);
                                cross.setVisibility(View.INVISIBLE);
                                count = 1;
                            } else {
                                online.setVisibility(View.INVISIBLE);
                                cross.setVisibility(View.VISIBLE);
                                count = 0;
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

    private void closePortal() {
        String URL_PUT = "https://attendance-gbuapi.herokuapp.com/api/close/attendance/portal/" + id;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest putRequest = new StringRequest(Request.Method.PUT, URL_PUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                //or try with this:
                //headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        queue.add(putRequest);
    }

    public void doSomething() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getAttendance();
            }
        }, 100);
    }
    private void getAttendance() {
        String url = "https://attendance-gbuapi.herokuapp.com/api/show/portal/" + id;
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
                                list.append(""+name +"\n\n");
                                rollNum.append("" + roll + "\n\n");
                                merge.append("Name: "+name + " , " + "Roll: "+String.valueOf(roll) + "\n\n");
                                if (list == null){
                                    bar.setVisibility(View.VISIBLE);
                                } else {
                                    bar.setVisibility(View.INVISIBLE);
                                }
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
    private void savePDF() {
        Document mDoc = new Document();
        String mFileName = new SimpleDateFormat("yyyyMMdd",
                Locale.getDefault()).format(System.currentTimeMillis());
        String mFilePath = Environment.getExternalStorageDirectory() + "/" + mFileName + ".pdf";
        try {
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
            mDoc.open();
            String mText = merge.getText().toString();
            mDoc.addTitle("Attendance");
            mDoc.add(new Paragraph(mText));
            mDoc.close();
            Toast.makeText(this, "File has been saved in" + mFilePath, Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_CODE: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    savePDF();
                } else {
                    //permission denied
                    Toast.makeText(this, "Permission Denied..!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}