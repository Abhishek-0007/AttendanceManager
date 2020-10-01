package com.example.attendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.android.material.navigation.NavigationView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainDashboard extends AppCompatActivity {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    TextView userName, userEmail;
    SwipeButton mark, view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        NavigationView navigation = (NavigationView) findViewById(R.id.nav_view);
        mark = (SwipeButton) findViewById(R.id.swipe_mark_btn);
        view = (SwipeButton) findViewById(R.id.swipe_view_btn);
        mark.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("avatar");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println(dataSnapshot);
                        if(dataSnapshot.getValue(String.class).equals("Teacher"))
                        {
                            Intent intent = new Intent(MainDashboard.this, FacultyForAttendance.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Intent intent = new Intent(MainDashboard.this, StudentMarkAttendance.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        view.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
                FirebaseAuth firebaseAuth1 = FirebaseAuth.getInstance();
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth1.getUid()).child("avatar");
                databaseReference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println(dataSnapshot);
                        if(dataSnapshot.getValue(String.class).equals("Teacher"))
                        {
                            Intent intent = new Intent(MainDashboard.this, FacultyViewAttendance.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Intent intent = new Intent(MainDashboard.this, StudentViewAttendance.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        View header = navigation.getHeaderView(0);
        userName = (TextView) header.findViewById(R.id.firebase_name);
        userEmail = (TextView) header.findViewById(R.id.firebase_email);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            userEmail.setText(""+email);
        }

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("avatar");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);
                if(dataSnapshot.getValue(String.class).equals("Teacher"))
                {
                    userName.setText("TEACHER");
                }
                else
                {
                    userName.setText("STUDENT");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_mark_attendance:
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("avatar");
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                System.out.println(dataSnapshot);
                                if(dataSnapshot.getValue(String.class).equals("Teacher"))
                                {
                                    Intent intent = new Intent(MainDashboard.this, FacultyForAttendance.class);
                                    startActivity(intent);

                                }
                                else
                                {
                                    Intent intent = new Intent(MainDashboard.this, StudentMarkAttendance.class);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        break;
                    case R.id.nav_view_attendance:
                        FirebaseAuth firebaseAuth1 = FirebaseAuth.getInstance();
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth1.getUid()).child("avatar");
                        databaseReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                System.out.println(dataSnapshot);
                                if(dataSnapshot.getValue(String.class).equals("Teacher"))
                                {
                                    Intent intent = new Intent(MainDashboard.this, FacultyViewAttendance.class);
                                    startActivity(intent);

                                }
                                else
                                {
                                    Intent intent = new Intent(MainDashboard.this, StudentViewAttendance.class);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        break;
                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainDashboard.this, login.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        drawer.closeDrawer(GravityCompat.START);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    long lastPress;
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastPress > 5000){
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_LONG).show();
            lastPress = currentTime;
        }else{
            super.onBackPressed();
        }
    }
}