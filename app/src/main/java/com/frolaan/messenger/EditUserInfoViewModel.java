package com.frolaan.messenger;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditUserInfoViewModel extends ViewModel {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference referenceUsers;

    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    private MutableLiveData<String> errorDb = new MutableLiveData<>();

    public EditUserInfoViewModel() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        referenceUsers = firebaseDatabase.getReference("Users");
        referenceUsers.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
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

//    Обновляем данные
    public void editInfo(String name, String lastName, int age) {
        referenceUsers.child(firebaseUser.getUid()).child("firstName").setValue(name);
        referenceUsers.child(firebaseUser.getUid()).child("lastName").setValue(lastName);
        referenceUsers.child(firebaseUser.getUid()).child("age").setValue(age);
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public LiveData<String> getErrorDb() {
        return errorDb;
    }
}
