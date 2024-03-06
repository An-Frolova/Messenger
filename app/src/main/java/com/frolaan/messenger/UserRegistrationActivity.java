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

import com.google.firebase.auth.FirebaseUser;

public class UserRegistrationActivity extends AppCompatActivity {

    private static final String TAG = "UserRegistrationActivity";

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextAge;
    private Button buttonRegistration;
    private UserRegistrationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        initViews();
        viewModel = new ViewModelProvider(this).get(UserRegistrationViewModel.class);
        observeViewModel();
        setupClickListener();
    }

    private void setupClickListener() {
        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = getTrimmedValue(editTextEmail);
                String password = getTrimmedValue(editTextPassword);
                String firstName = getTrimmedValue(editTextFirstName);
                String lastName = getTrimmedValue(editTextLastName);
                String ageStr = (String) editTextAge.getText().toString().trim();
                int age=0;

                if(ageStr.equals("")) {
                    Toast.makeText(
                            UserRegistrationActivity.this,
                            "Заполните все поля",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    age = Integer.parseInt(getTrimmedValue(editTextAge));
                }

                if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() ||
                lastName.isEmpty()) {
                    Toast.makeText(
                            UserRegistrationActivity.this,
                            "Заполните все поля",
                            Toast.LENGTH_SHORT
                    ).show();
                } else if (age > 100 || age < 5) {
                    Toast.makeText(
                            UserRegistrationActivity.this,
                            "Некорректные данные в поле возраст",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    viewModel.registerNewUser(email, password, firstName, lastName, age);
                }
            }
        });
    }

    private void observeViewModel() {
        viewModel.getError().observe(UserRegistrationActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Toast.makeText(
                            UserRegistrationActivity.this,
                            error,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
        viewModel.getUser().observe(UserRegistrationActivity.this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Intent intent = HomePageActivity.newIntent(UserRegistrationActivity.this, firebaseUser.getUid());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextAge = findViewById(R.id.editTextAge);
        buttonRegistration = findViewById(R.id.buttonRegistration);
    }

    private String getTrimmedValue(EditText editText) {
        return editText.getText().toString().trim();
    }


    public static Intent newIntent(Context context) {
        return new Intent(context, UserRegistrationActivity.class);
    }
}

