package com.example.androidassignments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class ChatWindow extends AppCompatActivity {

    private ListView listViewMessages;
    private EditText editTextMessage;
    private Button buttonSend;
    private ArrayList<String> chatMessages;
    private ChatAdapter messageAdapter;
    private SQLiteDatabase db;
    private ChatDatabaseHelper dbHelper;
    private static final String ACTIVITY_NAME = "ChatWindow";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        Log.i(ACTIVITY_NAME, "In onCreate()");

        listViewMessages = findViewById(R.id.listViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        chatMessages = new ArrayList<>();

        // === Setup database ===
        dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase(); // creates Messages.db if missing

        // === Load any saved messages ===
        Cursor cursor = db.query(
                ChatDatabaseHelper.TABLE_NAME,
                new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},
                null, null, null, null, null
        );

        Log.i(ACTIVITY_NAME, "Cursor’s column count = " + cursor.getColumnCount());
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, "Column " + i + " name: " + cursor.getColumnName(i));
        }

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String msg = cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_MESSAGE));
                chatMessages.add(msg);
                Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + msg);
                cursor.moveToNext();
            }
        }
        cursor.close();

        // === Setup adapter ===
        messageAdapter = new ChatAdapter(this, chatMessages);
        listViewMessages.setAdapter(messageAdapter);

        // === Send button logic ===
        buttonSend.setOnClickListener(v -> {
            String message = editTextMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                chatMessages.add(message);
                messageAdapter.notifyDataSetChanged();
                editTextMessage.setText("");
                listViewMessages.smoothScrollToPosition(chatMessages.size() - 1);

                // Insert message into DB
                ContentValues values = new ContentValues();
                values.put(ChatDatabaseHelper.KEY_MESSAGE, message);
                db.insert(ChatDatabaseHelper.TABLE_NAME, null, values);
            }
        });

        findViewById(R.id.buttonPrevious).setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        Log.i(ACTIVITY_NAME, "Database closed in onDestroy()");
    }



    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context context, ArrayList<String> messages) {
            super(context, 0, messages);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View result;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, parent, false);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, parent, false);
            }
            TextView messageText = result.findViewById(R.id.message_text);
            messageText.setText(getItem(position));
            return result;
        }
    }
}
