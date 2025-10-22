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

public class ChatWindow extends AppCompatActivity {

    private ListView listViewMessages;
    private EditText editTextMessage;
    private Button buttonSend;
    private ArrayList<String> chatMessages;
    private ChatAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        listViewMessages = findViewById(R.id.listViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        chatMessages = new ArrayList<>();

        messageAdapter = new ChatAdapter(this, chatMessages);
        listViewMessages.setAdapter(messageAdapter);

        buttonSend.setOnClickListener(v -> {
            String message = editTextMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                chatMessages.add(message);
                messageAdapter.notifyDataSetChanged();
                editTextMessage.setText("");
                listViewMessages.smoothScrollToPosition(chatMessages.size() - 1);
            }
        });
        findViewById(R.id.buttonPrevious).setOnClickListener(v -> finish());

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
