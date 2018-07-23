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

    private final DatabaseReference mReference;
    private final String mOwner;
    private final AccountLiveData.MyValueEventListener mListener = new AccountLiveData.MyValueEventListener();

    public AccountLiveData(DatabaseReference ref) {
        mReference = ref;
        mOwner = ref.getParent().getParent().getKey();
    }

    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        mReference.addValueEventListener(mListener);
    }

    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive");
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
            Log.e(TAG, "Can't listen to query " + mReference, databaseError.toException());
        }
    }
}
