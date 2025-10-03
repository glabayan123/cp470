package com.example.androidassignments;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LIST = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button buttonPrevious = findViewById(R.id.buttonPrevious);
        Button buttonNext = findViewById(R.id.buttonNext);
        Button buttonHome = findViewById(R.id.buttonHome);

        buttonPrevious.setOnClickListener(v -> NavUtils.navigateUpFromSameTask(MainActivity.this));
        buttonNext.setOnClickListener(v -> startActivityForResult(new Intent(MainActivity.this, ListItemsActivity.class), REQUEST_CODE_LIST));
        buttonHome.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LIST) {
            Log.i("MainActivity", "Returned to MainActivity.onActivityResult");
            if (resultCode == RESULT_OK && data != null) {
                String messagePassed = data.getStringExtra("Response");
                Log.i("MainActivity", "Data returned: " + messagePassed);
            }
        }
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
