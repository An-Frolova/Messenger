package com.frolaan.messenger;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {

    private FirebaseAuth auth;
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();


//    Проверка авторизированного пользователя в конструкторе
    public LoginViewModel() {
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    user.setValue(firebaseAuth.getCurrentUser());
                }
            }
        });
    }

//    Вход в аккаунт
    public void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        error.setValue(e.getMessage());
                    }
                });
    }

    public MutableLiveData<String> getError() {
        return error;
    }
    public LiveData<FirebaseUser> getUser() {
        return user;
    }
}
