package com.example.chatappjava.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import com.example.chatappjava.R;
import com.example.chatappjava.adapters.ChatAdapter;
import com.example.chatappjava.databinding.ActivityChatBinding;
import com.example.chatappjava.models.ChatMessage;
import com.example.chatappjava.models.User;
import com.example.chatappjava.utilities.PreferenceManager;
import com.example.chatappjava.utilities.constants;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private User receiveUser;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverDetails();
    }

    private Bitmap getBitmapFromEncodedString(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void loadReceiverDetails() {
        receiveUser = (User) getIntent().getSerializableExtra(constants.KEY_USER);
        binding.textName.setText(receiveUser.name);
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }
}