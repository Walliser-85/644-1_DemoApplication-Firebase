package ch.hevs.aislab.demo.database.firebase;

import android.arch.lifecycle.LiveData;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.database.pojo.ClientWithAccounts;

public class ClientAccountsListLiveData extends LiveData<List<ClientWithAccounts>> {

    private static final String TAG = "ClientAccountsLiveData";

    private final Query mQuery;
    private final String mOwner;
    private final ClientAccountsListLiveData.MyValueEventListener mListener = new ClientAccountsListLiveData.MyValueEventListener();

    /**
     * Following changes are made in order to remove unnecessary queries to Firebase due to
     * state changes in Activities (device orientation change)
     * Source: https://firebase.googleblog.com/2017/12/using-android-architecture-components_22.html
     */
    private boolean listenerRemovePending = false;
    private final Handler handler = new Handler();
    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "onInactive");
            mQuery.removeEventListener(mListener);
            listenerRemovePending = false;
        }
    };

    public ClientAccountsListLiveData(DatabaseReference ref, String owner) {
        mQuery = ref;
        mOwner = owner;
    }

    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener);
        }
        else {
            mQuery.addValueEventListener(mListener);
        }
        listenerRemovePending = false;
    }

    @Override
    protected void onInactive() {
        handler.postDelayed(removeListener, 2000);
        listenerRemovePending = true;
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toClientWithAccountsList(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + mQuery, databaseError.toException());
        }
    }

    private List<ClientWithAccounts> toClientWithAccountsList(DataSnapshot snapshot) {
        List<ClientWithAccounts> clientWithAccountsList = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            if (!childSnapshot.getKey().equals(mOwner)) {
                ClientWithAccounts clientWithAccounts = new ClientWithAccounts();
                clientWithAccounts.client = childSnapshot.getValue(ClientEntity.class);
                clientWithAccounts.client.setId(childSnapshot.getKey());
                clientWithAccounts.accounts = toAccounts(childSnapshot.child("accounts"), childSnapshot.getKey());
                clientWithAccountsList.add(clientWithAccounts);
            }
        }
        return clientWithAccountsList;
    }

    private List<AccountEntity> toAccounts(DataSnapshot snapshot, String ownerId) {
        List<AccountEntity> accounts = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            AccountEntity entity = childSnapshot.getValue(AccountEntity.class);
            entity.setId(childSnapshot.getKey());
            entity.setOwner(ownerId);
            accounts.add(entity);
        }
        return accounts;
    }
}
