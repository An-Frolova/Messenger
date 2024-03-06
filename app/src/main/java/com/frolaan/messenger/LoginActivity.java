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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private TextView textViewRegistration;
    private TextView textViewRestorePassword;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignIn;
    private LoginViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        observeViewModel();
        setupClickListener();
    }

    private void setupClickListener() {
//      Вход
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                            LoginActivity.this,
                            R.string.fill_in_all_fields,
                            Toast.LENGTH_SHORT
                    ).show();
                    Log.d(TAG, "Попытка входа с незаполненными данными");
                } else {
                    viewModel.login(email, password);
                }
            }
        });
//      Переход к регистрации
        textViewRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = UserRegistrationActivity.newIntent(LoginActivity.this);
                startActivity(intent);
            }
        });
//      Переход к восстановлению пароля
        textViewRestorePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                Intent intent = RestorePasswordActivity.newIntent(
                        LoginActivity.this,
                        email
                );
                startActivity(intent);
            }
        });
    }

    private void observeViewModel() {
        viewModel.getError().observe(LoginActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Toast.makeText(
                            LoginActivity.this,
                            "Неверный e-mail или пароль",
                            Toast.LENGTH_SHORT
                    ).show();
                    Log.d(TAG, "Попытка входа с неверными данными");
                }
            }
        });
        viewModel.getUser().observe(LoginActivity.this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Intent intent = HomePageActivity.newIntent(LoginActivity.this, firebaseUser.getUid());
                    startActivity(intent);
                    finish();
                    Log.d(TAG, "Вход выполнен");
                }
            }
        });
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewRegistration = findViewById(R.id.textViewRegistration);
        textViewRestorePassword = findViewById(R.id.textViewRestorePassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}