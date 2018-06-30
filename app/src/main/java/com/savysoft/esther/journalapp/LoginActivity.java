package com.savysoft.esther.journalapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText mUserEmail;
    private EditText mUserPassword;
    private TextView mRegisterLink;
    private TextView mResetLink;
    private Button mLogin;
    private FirebaseAuth mFirebaseAuth;
    private String mEmail;
    private String mPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //views
        initViews();
        //Get firebase instance
        mFirebaseAuth = FirebaseAuth.getInstance();
        //handles click events on views
        clickOnViews();
    }

    private void initViews(){
        mUserEmail = findViewById(R.id.user_email);
        mUserPassword = findViewById(R.id.user_password);
        mRegisterLink = findViewById(R.id.user_signup_link);
        mLogin = findViewById(R.id.login_btn);
        mResetLink = findViewById(R.id.user_reset_password);
    }

    private void clickOnViews(){
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = mUserEmail.getText().toString().trim();
                mPassword = mUserPassword.getText().toString().trim();
                //handle Login
                processLogin();
            }
        });

        mRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle sign intent
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //password reset
        mResetLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordReset();
            }
        });
    }

    private boolean isInputTrue(){
        boolean isCorrect = true;
        if (TextUtils.isEmpty(mEmail) || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
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
        return isCorrect;
    }

    private void processLogin(){
        Log.d(TAG, "Processing Login");
        if(isInputTrue()){
            mLogin.setEnabled(false);
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Processing Login...");
            progressDialog.show();
            // Login user here
            mFirebaseAuth.signInWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    Toast.makeText(LoginActivity.this, "User Login is" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    if(task.isSuccessful()){
                        Log.d(TAG, "Login successfull");
                        //Launch the Login Activity
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        setResult(RESULT_OK, null);
                        finish();
                    }else if (task.isCanceled()){
                        Log.d(TAG, "Action Cancelled");
                        Toast.makeText(LoginActivity.this, "Login Cancelled" + task.getException(),
                                Toast.LENGTH_SHORT).show();
                        mLogin.setEnabled(true);
                    }else if(!task.isSuccessful()){
                        Log.d(TAG, "Auth Failed");
                        Toast.makeText(LoginActivity.this, "Authentication failed." + task.getException(),
                                Toast.LENGTH_SHORT).show();
                        mLogin.setEnabled(true);
                    }
                }
            });

        }else{
            Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
            mLogin.setEnabled(true);
        }
    }

    private void passwordReset() {
        //Alert Dialog for Diasplaying password reset form
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogView = inflater.inflate(R.layout.activity_reset_password, null);
        dialogBuilder.setView(dialogView);

        //init views
        EditText resetPasswordView = dialogView.findViewById(R.id.user_reset_email);
        Button resetBtn = dialogView.findViewById(R.id.btn_reset_password);
        Button btnBack = dialogView.findViewById(R.id.btn_back);
        final String resetEmail = resetPasswordView.getText().toString().trim();
        final AlertDialog dialog = dialogBuilder.create();
       resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle Password Reset
                mFirebaseAuth.sendPasswordResetEmail(resetEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }
                       dialog.cancel();
                    }
                });
            }
        });

       btnBack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
           }
       });
        dialog.show();
    }
}
