package ch.hevs.aislab.demo.database.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.List;

import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.firebase.AccountListLiveData;
import ch.hevs.aislab.demo.database.firebase.AccountLiveData;

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

    public LiveData<AccountEntity> getAccount(final String accountId) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("accounts")
                .child(accountId);
        return new AccountLiveData(reference);
    }

    public LiveData<List<AccountEntity>> getByOwner(final String owner) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(owner)
                .child("accounts");
        return new AccountListLiveData(reference, owner);
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
        FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(account.getOwner())
                .child("accounts")
                .child(account.getId())
                .updateChildren(account.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        Log.d(TAG, "Update failure!", databaseError.toException());
                    } else {
                        Log.d(TAG, "Update successful!");
                    }
                });
    }

    public void delete(final AccountEntity account) {
        FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(account.getOwner())
                .child("accounts")
                .child(account.getId())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        Log.d(TAG, "Delete failure!", databaseError.toException());
                    } else {
                        Log.d(TAG, "Delete successful!");
                    }
                });
    }

    public void transaction(final AccountEntity sender, final AccountEntity recipient) {
        final DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
        rootReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                rootReference
                        .child("clients")
                        .child(sender.getOwner())
                        .child("accounts")
                        .child(sender.getId())
                        .updateChildren(sender.toMap());

                rootReference
                        .child("clients")
                        .child(recipient.getOwner())
                        .child("accounts")
                        .child(recipient.getId())
                        .updateChildren(recipient.toMap());

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    Log.d(TAG, "Transaction failure!", databaseError.toException());
                } else {
                    Log.d(TAG, "Transaction successful!");
                }
            }
        });
    }
}
