package com.frolaan.messenger;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ReauthViewModelFactory implements ViewModelProvider.Factory {

    private String email;
    private String password;

    public ReauthViewModelFactory(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ReauthViewModel(email, password);
    }
}
