package com.example.androidassignments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class ChatWindow extends AppCompatActivity {

    private ListView listViewMessages;
    private EditText editTextMessage;
    private Button buttonSend;

    private ArrayList<String> chatMessages;
    private ChatAdapter messageAdapter;

    private ChatDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;

    private boolean isTablet;
    private static final int REQUEST_MESSAGE_DETAILS = 5000;
    private static final int RESULT_DELETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        isTablet = findViewById(R.id.rightPane) != null;

        listViewMessages = findViewById(R.id.listViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        chatMessages = new ArrayList<>();

        dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        loadCursor();
        fillMessagesFromCursor();

        messageAdapter = new ChatAdapter(this, chatMessages);
        listViewMessages.setAdapter(messageAdapter);

        listViewMessages.setOnItemClickListener((parent, view, position, id) -> {
            cursor.moveToPosition(position);
            long msgId = id;
            String msgText = cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_MESSAGE));

            Bundle args = new Bundle();
            args.putLong(MessageFragment.ARG_ID, msgId);
            args.putString(MessageFragment.ARG_TEXT, msgText);

            if (isTablet) {
                MessageFragment fragment = new MessageFragment(this);
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.rightPane, fragment)
                        .commit();
            } else {
                Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                intent.putExtra("id", msgId);
                intent.putExtra("text", msgText);
                startActivityForResult(intent, REQUEST_MESSAGE_DETAILS);
            }
        });

        buttonSend.setOnClickListener(v -> {
            String message = editTextMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                ContentValues cv = new ContentValues();
                cv.put(ChatDatabaseHelper.KEY_MESSAGE, message);
                db.insert(ChatDatabaseHelper.TABLE_NAME, null, cv);
                reloadMessages();
                editTextMessage.setText("");
            }
        });

        findViewById(R.id.buttonPrevious).setOnClickListener(v -> finish());
    }

    private void loadCursor() {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor = db.query(
                ChatDatabaseHelper.TABLE_NAME,
                new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},
                null, null, null, null, null
        );
    }

    private void fillMessagesFromCursor() {
        chatMessages.clear();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                chatMessages.add(cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_MESSAGE)));
                cursor.moveToNext();
            }
        }
    }

    private void reloadMessages() {
        loadCursor();
        fillMessagesFromCursor();
        messageAdapter.notifyDataSetChanged();
        listViewMessages.invalidateViews();
    }

    public void deleteMessageFromFragment(long id) {
        db.delete(ChatDatabaseHelper.TABLE_NAME,
                ChatDatabaseHelper.KEY_ID + "=?",
                new String[]{String.valueOf(id)});
        reloadMessages();
    }

    public void removeFragment() {
        if (isTablet) {
            if (getSupportFragmentManager().findFragmentById(R.id.rightPane) != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(getSupportFragmentManager().findFragmentById(R.id.rightPane))
                        .commit();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MESSAGE_DETAILS && resultCode == RESULT_DELETE && data != null) {
            long id = data.getLongExtra("deleteId", -1);
            if (id != -1) {
                deleteMessageFromFragment(id);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();
    }

    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx, ArrayList<String> messages) {
            super(ctx, 0, messages);
        }

        @Override
        public long getItemId(int position) {
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_ID));
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater infl = LayoutInflater.from(getContext());
            View v;
            if (position % 2 == 0) {
                v = infl.inflate(R.layout.chat_row_incoming, parent, false);
            } else {
                v = infl.inflate(R.layout.chat_row_outgoing, parent, false);
            }
            TextView messageText = v.findViewById(R.id.message_text);
            messageText.setText(getItem(position));
            return v;
        }
    }
}
