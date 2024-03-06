package com.frolaan.messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ReauthActivity extends AppCompatActivity {

    private EditText editTextEmailForReauth;
    private EditText editTextPasswordForReauth;
    private Button buttonConfirm;

    private String email;
    private String password;

    private ReauthViewModel viewModel;
    private ReauthViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reauth);
        initViews();
        viewModelFactory = new ReauthViewModelFactory(email, password);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ReauthViewModel.class);
        observeViewModel();
        setupClickListener();
    }

    private void setupClickListener() {
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmailForReauth.getText().toString().trim();
                password = editTextPasswordForReauth.getText().toString().trim();
                viewModel.deleteUser();
                viewModel.logout();
                Intent intent = LoginActivity.newIntent(ReauthActivity.this);
                startActivity(intent);

            }
        });
    }
    
    private void observeViewModel() {
        viewModel.getErrorDb().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                Toast.makeText(ReauthActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initViews() {
        editTextEmailForReauth = findViewById(R.id.editTextEmailForReauth);
        editTextPasswordForReauth = findViewById(R.id.editTextPasswordForReauth);
        buttonConfirm = findViewById(R.id.buttonConfirm);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, ReauthActivity.class);
    }
}