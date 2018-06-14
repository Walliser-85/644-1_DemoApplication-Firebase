package ch.hevs.aislab.demo.database.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import ch.hevs.aislab.demo.database.entity.AccountEntity;

public class AccountRepository {
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
        return null;
    }

    public void insert(final AccountEntity account) {
        //TODO: Implement this using Firebase.
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
