package com.frolaan.messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class ProfileDeletedActivity extends AppCompatActivity {

    private static final String EXTRA_CURRENT_USER_ID = "current_id";
    private String currentUserId;
    private Button buttonOk;
    private ProfileDeletedViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_deleted);
        currentUserId = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
        viewModel = new ViewModelProvider(this).get(ProfileDeletedViewModel.class);
        setupClickListener();
    }

    private void setupClickListener() {
        buttonOk = findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.newIntent(ProfileDeletedActivity.this);
                startActivity(intent);
                viewModel.deleteUserFromDb(currentUserId);
            }
        });
    }

    public static Intent newIntent(Context context, String currentUserId) {
        Intent intent = new Intent(context, ProfileDeletedActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        return intent;
    }
}