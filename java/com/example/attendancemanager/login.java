package com.example.attendancemanager;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    EditText Email, Password;
    Button LogInButton, RegisterButton;
    FirebaseAuth mAuth;
    String Avatar1, Avatar2;
    RadioButton teacher,student;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;
    String email, password;
    ProgressDialog dialog;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    public static final String userEmail="";

    public static final String TAG="LOGIN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        teacher = (RadioButton)findViewById(R.id.teacher);
        student = (RadioButton)findViewById(R.id.student);
        LogInButton = (Button) findViewById(R.id.buttonLogin);

        RegisterButton = (Button) findViewById(R.id.buttonRegister);

        Email = (EditText) findViewById(R.id.editEmai);
        Password = (EditText) findViewById(R.id.editPassword);
        dialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                if (mUser != null) {
                    Intent intent = new Intent(login.this, StudentViewAttendance.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else
                {
                    Log.d(TAG,"AuthStateChanged:Logout");
                }

            }
        };
        // LogInButton.setOnClickListener((View.OnClickListener) this);
        //RegisterButton.setOnClickListener((View.OnClickListener) this);
        //Adding click listener to log in button.
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling EditText is empty or no method.
                userSign();


            }
        });

        // Adding click listener to register button.
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Opening new user registration activity using intent on button click.
                Intent intent = new Intent(login.this, Register.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //removeAuthSateListner is used  in onStart function just for checking purposes,it helps in logging you out.
        mAuth.removeAuthStateListener(mAuthListner);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListner != null) {
            mAuth.removeAuthStateListener(mAuthListner);
        }

    }



    private void userSign() {
        email = Email.getText().toString().trim();
        password = Password.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(login.this, "Enter the correct Email", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(login.this, "Enter the correct password", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.setMessage("Loging in please wait...");
        dialog.setIndeterminate(true);
        dialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(login.this, "Login not successfull", Toast.LENGTH_SHORT).show();

                } else {
                    dialog.dismiss();

                    checkIfEmailVerified();

                }
            }
        });

    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            login.super.finish();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Hit again to exit!",    Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }
    //This function helps in verifying whether the email is verified or not.
    private void checkIfEmailVerified() {
        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
        boolean emailVerified = users.isEmailVerified();
        System.out.println("hello");
        if (!emailVerified) {
            Toast.makeText(this, "Verify the Email Id", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            finish();
        } else {
            Email.getText().clear();
            Password.getText().clear();
            System.out.println("hello1");
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getUid()).child("avatar");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println(dataSnapshot);
                    if(dataSnapshot.getValue(String.class).equals("Teacher"))
                    {
                        Intent intent = new Intent(login.this, MainDashboard.class);
                        startActivity(intent);
                        Toast.makeText(login.this, "Please Wait", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Intent intent = new Intent(login.this, MainDashboard.class);
                        Toast.makeText(login.this, "Please Wait!", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
}
