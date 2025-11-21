package com.example.androidassignments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class MessageFragment extends Fragment {

    public static final String ARG_ID = "id";
    public static final String ARG_TEXT = "text";

    private ChatWindow chatActivity;
    private long messageId;
    private String messageText;

    public MessageFragment() {
        chatActivity = null;
    }

    public MessageFragment(ChatWindow activity) {
        chatActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message_detail, container, false);

        Bundle args = getArguments();
        if (args != null) {
            messageId = args.getLong(ARG_ID);
            messageText = args.getString(ARG_TEXT);
        }

        TextView idView = v.findViewById(R.id.textMessageID);
        TextView msgView = v.findViewById(R.id.textMessageBody);
        Button deleteButton = v.findViewById(R.id.buttonDelete);

        idView.setText("ID: " + messageId);
        msgView.setText(messageText);

        deleteButton.setOnClickListener(view -> {
            if (chatActivity != null) {
                chatActivity.deleteMessageFromFragment(messageId);
                chatActivity.removeFragment();
            } else if (getActivity() != null) {
                Intent data = new Intent();
                data.putExtra("deleteId", messageId);
                getActivity().setResult(1, data);
                getActivity().finish();
            }
        });

        return v;
    }
}
