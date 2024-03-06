package com.frolaan.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    private static final String EXTRA_CURRENT_USER_ID = "current_id";

    private static final String TAG = "HomePageActivity";
    private HomePageViewModel viewModel;
    private RecyclerView recyclerViewUsers;
    private UsersAdapter usersAdapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference("Users");

    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initViews();
        currentUserId = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);
        viewModel = new ViewModelProvider(this).get(HomePageViewModel.class);
        observeViewModel();
        usersAdapter.setOnUserClickListener(new UsersAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(User user) {
                Intent intent = ChatActivity.newIntent(HomePageActivity.this, currentUserId, user.getId());
                startActivity(intent);
            }
        });
    }

    private void observeViewModel() {
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser == null) {
                    Intent intent = LoginActivity.newIntent(HomePageActivity.this);
                    startActivity(intent);
                    finish();
                    Log.d(TAG, "Выход из аккаунта");
                }
            }
        });
        viewModel.getUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                usersAdapter.setUsers(users);
            }
        });
    }


//  Отображаем меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemSignOut) {
            viewModel.logout();
        } else if (item.getItemId() == R.id.itemProfile) {
            Intent intent = ProfileActivity.newIntent(this, currentUserId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        viewModel.setOnlineStatus(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.setOnlineStatus(false);
    }

    public static Intent newIntent(Context context, String currentUserId) {
        Intent intent = new Intent(context, HomePageActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        return intent;
    }

    private void initViews() {
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        usersAdapter = new UsersAdapter();
        recyclerViewUsers.setAdapter(usersAdapter);
    }

}