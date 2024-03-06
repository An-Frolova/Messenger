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

public class RestorePasswordActivity extends AppCompatActivity {

    private static final String EXTRA_MAIL = "email";

    private EditText editTextEmail;
    private Button buttonRestorePassword;
    private RestorePasswordViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_password);
        initViews();
        String email = getIntent().getStringExtra(EXTRA_MAIL);
        editTextEmail.setText(email);
        viewModel = new ViewModelProvider(this).get(RestorePasswordViewModel.class);
        observeViewModel();
        setupClickListener();
    }

    private void setupClickListener() {
        buttonRestorePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                if (!email.equals("")) {
                    viewModel.resertPassword(email);
                } else {
                    Toast.makeText(
                            RestorePasswordActivity.this,
                            R.string.toast_enter_email,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }

    private void observeViewModel() {
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                Toast.makeText(RestorePasswordActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.isSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success) {
                    Toast.makeText(
                            RestorePasswordActivity.this,
                            R.string.toast_letter_sent,
                            Toast.LENGTH_LONG
                    ).show();
                Intent intent = LoginActivity.newIntent(RestorePasswordActivity.this);
                startActivity(intent);
                }
            }
        });
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonRestorePassword = findViewById(R.id.buttonRestorePassword);
    }

    public static Intent newIntent(Context context, String email) {
        Intent intent = new Intent(context, RestorePasswordActivity.class);
        intent.putExtra(EXTRA_MAIL, email);
        return intent;
    }
}