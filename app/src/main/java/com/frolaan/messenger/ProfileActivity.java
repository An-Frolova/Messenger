package com.frolaan.messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ProfileActivity extends AppCompatActivity {

    private static final String EXTRA_CURRENT_USER_ID = "current_id";
    private static final String TAG = "ProfileActivity";

    private TextView textViewUserName;
    private TextView textViewUserLastName;
    private TextView textViewUserAge;
    private TextView textViewDeleteProfile;
    private Button buttonEditUserInfo;

    private String currentUserId;
    private ProfileViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews();
        currentUserId = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        observeViewModel();
        setupClickListener();
    }

    private void setupClickListener() {
        buttonEditUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = EditUserInfoActivity.newIntent(ProfileActivity.this, currentUserId);
                startActivity(intent);
            }
        });
        textViewDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteUser();
            }
        });
    }

    private void observeViewModel() {
        viewModel.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                textViewUserName.setText(user.getFirstName());
                textViewUserLastName.setText(user.getLastName());
                String userAge = String.format("%s", user.getAge());
                textViewUserAge.setText(userAge);
            }
        });
        viewModel.getErrorDb().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                Log.d("ProfileActivity", error);
            }
        });
        viewModel.isSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success) {
                    Intent intent = ProfileDeletedActivity.newIntent(ProfileActivity.this, currentUserId);
                    startActivity(intent);
                }
            }
        });
    }

    private void initViews() {
        textViewDeleteProfile = findViewById(R.id.textViewDeleteProfile);
        buttonEditUserInfo = findViewById(R.id.buttonEditUserInfo);
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewUserLastName = findViewById(R.id.textViewUserLastName);
        textViewUserAge = findViewById(R.id.textViewUserAge);
    }

    public static Intent newIntent(Context context, String currentUserId) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        return intent;
    }
}