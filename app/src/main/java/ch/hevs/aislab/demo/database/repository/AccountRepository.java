package ch.hevs.aislab.demo.database.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import ch.hevs.aislab.demo.database.AppDatabase;
import ch.hevs.aislab.demo.database.entity.AccountEntity;

public class AccountRepository {
    private static AccountRepository sInstance;

    private final AppDatabase mDatabase;

    private AccountRepository(final AppDatabase database) {
        mDatabase = database;
    }

    public static AccountRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (AccountRepository.class) {
                if (sInstance == null) {
                    sInstance = new AccountRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<AccountEntity> getAccount(final Long accountId) {
        return mDatabase.accountDao().getById(accountId);
    }

    public LiveData<List<AccountEntity>> getAccounts() {
        return mDatabase.accountDao().getAll();
    }

    public LiveData<List<AccountEntity>> getByOwner(final String owner) {
        return mDatabase.accountDao().getOwned(owner);
    }

    public void insert(final AccountEntity account) {
        mDatabase.accountDao().insert(account);
    }

    public void update(final AccountEntity account) {
        mDatabase.accountDao().update(account);
    }

    public void delete(final AccountEntity account) {
        mDatabase.accountDao().delete(account);
    }

    public void transaction(final AccountEntity sender, final AccountEntity recipient) {
        mDatabase.accountDao().transaction(sender, recipient);
    }
}
