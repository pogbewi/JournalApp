package com.savysoft.esther.journalapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private EditText mUserFullname;
    private EditText mUserEmail;
    private EditText mUserPassword;
    private EditText mUserPasswordConfirm;
    private TextView mLoginLink;
    private Button mRegister;
    private FirebaseAuth mFirebaseAuth;
    private String mFullname;
    private String mEmail;
    private String mPassword;
    private String mConfirmPassword;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //views
        initViews();
        //Get firebase instance
        mFirebaseAuth = FirebaseAuth.getInstance();
        //handles click events on views
        clickOnViews();

    }

    private void initViews(){
        mUserFullname = findViewById(R.id.user_fullname);
        mUserEmail = findViewById(R.id.user_email);
        mUserPassword = findViewById(R.id.user_password);
        mUserPasswordConfirm = findViewById(R.id.user_confirm_password);
        mLoginLink = findViewById(R.id.link_login);
        mRegister = findViewById(R.id.btn_signup);

    }

    private void clickOnViews(){
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFullname = mUserFullname.getText().toString().trim();
                mEmail = mUserEmail.getText().toString().trim();
                mPassword = mUserPassword.getText().toString().trim();
                mConfirmPassword = mUserPasswordConfirm.getText().toString().trim();
                //handle signup
                processSignUp();
            }
        });

        mLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle sign intent
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean isInputTrue(){
        boolean isCorrect = true;
        if (TextUtils.isEmpty(mFullname) || mFullname.length() < 3) {
            mUserFullname.setError("at least 3 characters");
            isCorrect = false;
        }else{
            mUserFullname.setError(null);
        }

        if (TextUtils.isEmpty(mEmail)|| !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            mUserEmail.setError("enter a valid email address");
            isCorrect = false;
        }else{
            mUserEmail.setError(null);
        }

        if (TextUtils.isEmpty(mPassword) || mPassword.length() < 6) {
            mUserPassword.setError("Password must not be less than 6 alphanumeric characters");
            isCorrect = false;
        }else{
            mUserPassword.setError(null);
        }

        if (TextUtils.isEmpty(mConfirmPassword) || mConfirmPassword.length() < 4 || mConfirmPassword.length() > 10 || !(mConfirmPassword.equals(mPassword))) {
            mUserPasswordConfirm.setError("Password must not be less than 4 alphanumeric characters");
            isCorrect = false;
        }else{
            mUserPasswordConfirm.setError(null);
        }
        return isCorrect;
    }

    private void processSignUp(){
        Log.d(TAG, "Processing Sign Up");
        if(isInputTrue()){
            mRegister.setEnabled(false);
            final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();
            // create user here
            mFirebaseAuth.createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // If sign up fails, display a message to the user. If sign up succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed up user can be handled in the listener.
                    Toast.makeText(RegisterActivity.this, "User registration is" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    if(task.isSuccessful()){
                        Log.d(TAG, "Reg. successfull");
                        //Launch the Login Activity
                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i);
                        setResult(RESULT_OK, null);
                        finish();
                    }else if (task.isCanceled()){
                        Log.d(TAG, "Action Cancelled");
                        Toast.makeText(RegisterActivity.this, "Registration Cancelled" + task.getException(),
                                Toast.LENGTH_SHORT).show();
                    }else if(!task.isSuccessful()){
                        Log.d(TAG, "Auth Failed");
                        Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
            mRegister.setEnabled(true);
        }
    }
}
