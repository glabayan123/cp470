package com.example.androidassignments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MessageDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);

        long id = getIntent().getLongExtra("id", -1);
        String text = getIntent().getStringExtra("text");

        Bundle args = new Bundle();
        args.putLong(MessageFragment.ARG_ID, id);
        args.putString(MessageFragment.ARG_TEXT, text);

        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.messageDetailContainer, fragment)
                .commit();
    }
}
