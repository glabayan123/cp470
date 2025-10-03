package com.example.androidassignments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ListItemsActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private Switch mySwitch;
    private CheckBox myCheckBox;

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Log.i("ListItemsActivity", "Photo taken and returned");
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button buttonPrevious = findViewById(R.id.buttonPrevious);
        Button buttonNext = findViewById(R.id.buttonNext);
        Button buttonHome = findViewById(R.id.buttonHome);

        buttonPrevious.setOnClickListener(v -> NavUtils.navigateUpFromSameTask(ListItemsActivity.this));
        buttonNext.setOnClickListener(v -> startActivity(new Intent(ListItemsActivity.this, LoginActivity.class)));
        buttonHome.setOnClickListener(v -> startActivity(new Intent(ListItemsActivity.this, LoginActivity.class)));

        imageButton = findViewById(R.id.imageButton);
        mySwitch = findViewById(R.id.mySwitch);
        myCheckBox = findViewById(R.id.myCheckBox);

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                cameraLauncher.launch(intent);
            }
        });

        mySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String text = isChecked ? getString(R.string.switch_on) : getString(R.string.switch_off);
            int duration = isChecked ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
            Toast.makeText(ListItemsActivity.this, text, duration).show();
        });

        myCheckBox.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
            builder.setMessage(R.string.checkbox_message)
                    .setTitle("Confirm Exit")
                    .setPositiveButton("Yes", (dialog, id) -> {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("Response", "Here is my response");
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.dismiss())
                    .show();
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
