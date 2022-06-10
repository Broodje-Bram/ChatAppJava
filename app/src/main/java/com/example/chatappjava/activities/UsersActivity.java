package com.example.chatappjava.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.chatappjava.adapters.UsersAdapter;
import com.example.chatappjava.databinding.ActivityUsersBinding;
import com.example.chatappjava.listeners.UserListener;
import com.example.chatappjava.models.User;
import com.example.chatappjava.utilities.PreferenceManager;
import com.example.chatappjava.utilities.constants;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity implements UserListener{

    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        getUsers();
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(view -> onBackPressed());
    }

    private void getUsers() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(constants.KEY_COLLECTION_USERS).get().addOnCompleteListener(task -> {
            loading(false);
            String currentUserId =  preferenceManager.getString(constants.KEY_USER_ID);
            if (task.isSuccessful() && task.getResult() != null) {
                List<User> users = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                        continue;
                    }
                    User user = new User();
                    user.name = queryDocumentSnapshot.getString(constants.KEY_NAME);
                    user.email = queryDocumentSnapshot.getString(constants.KEY_EMAIL);
                    user.image = queryDocumentSnapshot.getString(constants.KEY_IMAGE);
                    user.token = queryDocumentSnapshot.getString(constants.KEY_FCM_TOKEN);
                    users.add(user);
                }
                if (users.size() > 0) {
                    UsersAdapter usersAdapter = new UsersAdapter(users, this);
                    binding.userRecyclerView.setAdapter(usersAdapter);
                    binding.userRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    showErrorMessage();
                }
            } else {
                showErrorMessage();
            }
        });
    }

    private void showErrorMessage() {
        binding.textErrorMessage.setText(String.format("%s", "No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(boolean isLoading) {
        if (isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}