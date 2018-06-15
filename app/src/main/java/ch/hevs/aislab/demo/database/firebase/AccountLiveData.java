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

import ch.hevs.aislab.demo.database.entity.AccountEntity;

public class AccountLiveData extends LiveData<AccountEntity> {
    private static final String TAG = "AccountLiveData";

    private final Query mQuery;
    private final String mOwner;
    private final AccountLiveData.MyValueEventListener mListener = new AccountLiveData.MyValueEventListener();

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

    public AccountLiveData(Query query, String owner) {
        mQuery = query;
        mOwner = owner;
    }

    public AccountLiveData(DatabaseReference ref, String owner) {
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
            AccountEntity entity = dataSnapshot.getValue(AccountEntity.class);
            entity.setId(dataSnapshot.getKey());
            entity.setOwner(mOwner);
            setValue(entity);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + mQuery, databaseError.toException());
        }
    }
}
