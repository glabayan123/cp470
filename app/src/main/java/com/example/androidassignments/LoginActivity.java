package com.example.androidassignments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String EMAIL_KEY = "DefaultEmail";

    private Button buttonPrevious, buttonNext, buttonHome, buttonLogin;
    private EditText emailField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonPrevious = findViewById(R.id.buttonPrevious);
        buttonNext = findViewById(R.id.buttonNext);
        buttonHome = findViewById(R.id.buttonHome);
        emailField = findViewById(R.id.editTextTextEmailAddress);
        passwordField = findViewById(R.id.editPassword);
        buttonLogin = findViewById(R.id.buttonLogin);


        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedEmail = prefs.getString(EMAIL_KEY, "email@domain.com");
        emailField.setText(savedEmail);

        buttonPrevious.setOnClickListener(v -> NavUtils.navigateUpFromSameTask(LoginActivity.this));
        buttonNext.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, MainActivity.class)));
        buttonHome.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, LoginActivity.class)));

        buttonLogin.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (!email.contains("@")) {
                Log.i("LoginActivity", "Invalid email format");
                return;
            }
            if (password.isEmpty()) {
                Log.i("LoginActivity", "Password is empty");
                return;
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(EMAIL_KEY, email);
            editor.apply();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("ActivityName", "onResume() called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("ActivityName", "onStart() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ActivityName", "onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("ActivityName", "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("ActivityName", "onDestroy() called");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("ActivityName", "onSaveInstanceState() called");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("ActivityName", "onRestoreInstanceState() called");
    }
}
