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

public class EditUserInfoActivity extends AppCompatActivity {

    private static final String EXTRA_CURRENT_USER_ID = "current_id";

    private EditText editTextNewName;
    private EditText editTextNewLastName;
    private EditText editTextNewAge;
    private Button buttonSaveData;

    private String currentUserId;

    private EditUserInfoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        initViews();
        currentUserId = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
        viewModel = new ViewModelProvider(this).get(EditUserInfoViewModel.class);
        observeViewModel();
        setupClickListener();
    }

    private void setupClickListener() {
        buttonSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextNewName.getText().toString().trim();
                String lastName = editTextNewLastName.getText().toString().trim();
                String ageStr = editTextNewAge.getText().toString().trim();

                if (ageStr.equals("") ) {
                    Toast.makeText(
                            EditUserInfoActivity.this,
                            "Заполните все поля",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                int age = Integer.parseInt(ageStr);

                if (name.isEmpty() || lastName.isEmpty()) {
                    Toast.makeText(
                            EditUserInfoActivity.this,
                            "Заполните все поля",
                            Toast.LENGTH_SHORT
                    ).show();
                } else if (age > 100 || age < 5) {
                    Toast.makeText(
                            EditUserInfoActivity.this,
                            "Некорректные данные в поле возраст",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    viewModel.editInfo(name, lastName, age);
                    Intent intent = HomePageActivity.newIntent(EditUserInfoActivity.this, currentUserId);
                    startActivity(intent);
                    Toast.makeText(
                            EditUserInfoActivity.this,
                            "Изменения сохранены",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }

    private void observeViewModel() {
        viewModel.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                editTextNewName.setText(user.getFirstName());
                editTextNewLastName.setText(user.getLastName());
                String age = String.format("%s", user.getAge());
                editTextNewAge.setText(age);
            }
        });
    }

    private void initViews() {
        editTextNewName = findViewById(R.id.editTextNewName);
        editTextNewLastName = findViewById(R.id.editTextNewLastName);
        editTextNewAge = findViewById(R.id.editTextNewAge);
        buttonSaveData = findViewById(R.id.buttonSaveData);
    }
     public static Intent newIntent(Context context, String currentUserId) {
        Intent intent = new Intent(context, EditUserInfoActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        return intent;
     }
}