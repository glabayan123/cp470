package com.example.androidassignments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.androidassignments.databinding.ActivityTestToolbarBinding;
import com.google.android.material.snackbar.Snackbar;

public class TestToolbar extends AppCompatActivity {

    private ActivityTestToolbarBinding binding;
    private String currentMessage; // current message for menu item 1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestToolbarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set Toolbar as ActionBar
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        // Initialize currentMessage from strings.xml
        currentMessage = getString(R.string.default_snackbar_message);

        // FAB click listener
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.fab_message), Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null)
                        .show();
            }
        });
    }

    // Inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    // Handle menu clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem mi) {
        int id = mi.getItemId();

        if (id == R.id.instagram) { // Menu item 1
            Snackbar.make(findViewById(R.id.toolbar), currentMessage, Snackbar.LENGTH_LONG).show();
            return true;

        } else if (id == R.id.steam) { // Menu item 2
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_back_title)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
            return true;

        } else if (id == R.id.twitter) { // Menu item 3
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_custom_message, null);
            builder.setView(dialogView);

            final EditText editText = dialogView.findViewById(R.id.editTextMessage);
            editText.setHint(R.string.hint_new_message);
            ImageView imageView = dialogView.findViewById(R.id.imageViewIcon);
            imageView.setImageResource(R.drawable.alien);

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String input = editText.getText().toString().trim();
                    if (!input.isEmpty()) {
                        currentMessage = input;
                    }
                }
            });

            builder.setNegativeButton(R.string.cancel, null);
            builder.show();
            return true;

        } else if (id == R.id.about) { // About item
            Toast.makeText(this, R.string.about_message, Toast.LENGTH_LONG).show();
            return true;

        } else {
            return super.onOptionsItemSelected(mi);
        }
    }

}
