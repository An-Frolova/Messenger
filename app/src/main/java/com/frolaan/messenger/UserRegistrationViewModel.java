package com.frolaan.messenger;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRegistrationViewModel extends ViewModel {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference usersReference;
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();

    public LiveData<String> getError() {
        return error;
    }
    public LiveData<FirebaseUser> getUser() {
        return user;
    }


//    Конструктор. Проверка состояния пользователя
    public UserRegistrationViewModel() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference("Users");

        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    user.setValue(firebaseAuth.getCurrentUser());
                }
            }
        });
    }

//    Регистрация нового пользователя
    public void registerNewUser(
            String email,
            String password,
            String firstName,
            String lastName,
            int age
    ) {
//        Добавление пользователя в Базу данных
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser firebaseUser = authResult.getUser();
                        if (firebaseUser == null) {
                            return;
                        }
                        User user = new User(
                                firebaseUser.getUid(),
                                firstName,
                                lastName,
                                age,
                                false
                                );
                        usersReference.child(user.getId()).setValue(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        error.setValue(e.getMessage());
                    }
                });

    }
}
