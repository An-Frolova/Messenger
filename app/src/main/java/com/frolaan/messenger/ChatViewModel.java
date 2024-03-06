package com.frolaan.messenger;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {

    private MutableLiveData<List<Message>> messages = new MutableLiveData<List<Message>>();
    private MutableLiveData<User> otherUser = new MutableLiveData<>();
    private MutableLiveData<Boolean> messageSent = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference referenceUsers;
    private DatabaseReference referenceMessages;

    private String currentUserId;
    private String otherUserId;


    public ChatViewModel(String currentUserId, String otherUserId) {
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;

        firebaseDatabase = FirebaseDatabase.getInstance();
        referenceUsers = firebaseDatabase.getReference("Users");
        referenceMessages = firebaseDatabase.getReference("Messages");

//        Получаем данные о собеседнике
        referenceUsers.child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                otherUser.setValue(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        Получаем все сообщения из Базы данных
        referenceMessages.child(currentUserId).child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Message> messagesFromDb = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    messagesFromDb.add(message);
                }
                messages.setValue(messagesFromDb);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError e) {
                error.setValue(e.getMessage());
            }
        });
    }


    public void setOnlineStatus(boolean isOnline) {
        referenceUsers.child(currentUserId).child("online").setValue(isOnline);
    }

    public  void sendMessage(Message message) {
//        Сохраняем сообщение сначала для одного пользователя
        referenceMessages
                .child(message.getSenderId())
                .child(message.getReceiverId())
                .push()
                .setValue(message)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
//                        а потом для его собеседника
                        referenceMessages
                                .child(message.getReceiverId())
                                .child(message.getSenderId())
                                .push()
                                .setValue(message)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        messageSent.setValue(true);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        error.setValue(e.getMessage());
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        error.setValue(e.getMessage());
                    }
                });
    }


    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public LiveData<User> getOtherUser() {
        return otherUser;
    }

    public LiveData<Boolean> getMessageSent() {
        return messageSent;
    }

    public LiveData<String> getError() {
        return error;
    }

    public MutableLiveData<FirebaseUser> getUser() {
        return user;
    }
}
