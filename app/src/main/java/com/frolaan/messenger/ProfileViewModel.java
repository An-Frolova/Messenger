package com.frolaan.messenger;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileViewModel extends ViewModel {


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference referenceUsers;
    private FirebaseUser user;

    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    private MutableLiveData<String> errorDb = new MutableLiveData<>();
    private MutableLiveData<Boolean> success = new MutableLiveData<>();


    public ProfileViewModel() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        referenceUsers = firebaseDatabase.getReference("Users");
        referenceUsers.child(user.getUid()).child("online").setValue(true);

        referenceUsers.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                currentUser.setValue(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorDb.setValue(error.getMessage());
            }
        });
    }

    public void deleteUser() {

        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    success.setValue(true);
                }
            }
        });
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public LiveData<String> getErrorDb() {
        return errorDb;
    }

    public MutableLiveData<Boolean> isSuccess() {
        return success;
    }
}