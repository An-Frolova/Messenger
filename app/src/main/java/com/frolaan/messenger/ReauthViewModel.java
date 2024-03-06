package com.frolaan.messenger;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ReauthViewModel extends ViewModel {

    private String email;
    private String password;

    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;

    private MutableLiveData<String> errorDb = new MutableLiveData<>();
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();

    public ReauthViewModel(String email, String password) {
        this.email = email;
        this.password = password;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user.setValue(firebaseUser);
        auth = FirebaseAuth.getInstance();
    }

    public void deleteUser() {
        AuthCredential authCredential = EmailAuthProvider.getCredential(email, password);
        firebaseUser.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                firebaseUser.delete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                errorDb.setValue(e.getMessage());
            }
        });
    }

    public void logout() {
        auth.signOut();
    }

    public LiveData<String> getErrorDb() {
        return errorDb;
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }
}
