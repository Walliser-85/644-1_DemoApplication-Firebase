package ch.hevs.aislab.demo.database.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import ch.hevs.aislab.demo.database.AppDatabase;
import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.database.pojo.ClientAccounts;

public class ClientRepository {

    private static ClientRepository sInstance;

    private final AppDatabase mDatabase;

    private ClientRepository(final AppDatabase database) {
        mDatabase = database;
    }

    public static ClientRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (AccountRepository.class) {
                if (sInstance == null) {
                    sInstance = new ClientRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<ClientEntity> getClient(final String clientId) {
        return mDatabase.clientDao().getById(clientId);
    }

    public LiveData<List<ClientAccounts>> getOtherClientsWithAccounts(final String owner) {
        return mDatabase.clientDao().getOtherClientsWithAccounts(owner);
    }

    public void insert(final ClientEntity client) {
        mDatabase.clientDao().insert(client);
    }

    public void update(final ClientEntity client) {
        mDatabase.clientDao().update(client);
    }

    public void delete(final ClientEntity client) {
        mDatabase.clientDao().delete(client);
    }
}
