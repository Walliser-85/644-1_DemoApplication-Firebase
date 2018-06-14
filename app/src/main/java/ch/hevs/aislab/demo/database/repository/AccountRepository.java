package ch.hevs.aislab.demo.database.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.firebase.AccountListLiveData;

public class AccountRepository {
    private static final String TAG = "AccountRepository";

    private static AccountRepository sInstance;

    public static AccountRepository getInstance() {
        if (sInstance == null) {
            synchronized (AccountRepository.class) {
                if (sInstance == null) {
                    sInstance = new AccountRepository();
                }
            }
        }
        return sInstance;
    }

    public LiveData<AccountEntity> getAccount(final Long accountId) {
        //TODO: Implement this using Firebase.
        return null;
    }

    public LiveData<List<AccountEntity>> getAccounts() {
        //TODO: Implement this using Firebase.
        return null;
    }

    public LiveData<List<AccountEntity>> getByOwner(final String owner) {
        //TODO: Implement this using Firebase.
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(owner)
                .child("accounts");
        return new AccountListLiveData(databaseReference);
    }

    public void insert(final AccountEntity account) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(account.getOwner())
                .child("accounts");
        String key = reference.push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(account.getOwner())
                .child("accounts")
                .child(key)
                .setValue(account, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        Log.d(TAG, "Insert failure!", databaseError.toException());
                    } else {
                        Log.i(TAG, "Insert successful!");
                    }
                });
    }

    public void update(final AccountEntity account) {
        //TODO: Implement this using Firebase.
    }

    public void delete(final AccountEntity account) {
        //TODO: Implement this using Firebase.
    }

    public void transaction(final AccountEntity sender, final AccountEntity recipient) {
        //TODO: Implement this using Firebase.
    }
}
