package ch.hevs.aislab.demo.database.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import ch.hevs.aislab.demo.database.entity.ClientEntity;

public class ClientRepository {

    private static ClientRepository sInstance;

    public static ClientRepository getInstance() {
        if (sInstance == null) {
            synchronized (AccountRepository.class) {
                if (sInstance == null) {
                    sInstance = new ClientRepository();
                }
            }
        }
        return sInstance;
    }

    public LiveData<ClientEntity> getClient(final String clientId) {
        //TODO: Implement this using Firebase.
        return null;
    }

    /*    TODO: Rework this for firebase
    public LiveData<List<ClientWithAccounts>> getOtherClientsWithAccounts(final String owner) {
        //TODO: Implement this using Firebase.
        return null;
    }*/

    public void insert(final ClientEntity client) {
        //TODO: Implement this using Firebase.
    }

    public void update(final ClientEntity client) {
        //TODO: Implement this using Firebase.
    }

    public void delete(final ClientEntity client) {
        //TODO: Implement this using Firebase.
    }
}
