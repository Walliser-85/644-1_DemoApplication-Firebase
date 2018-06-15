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

import ch.hevs.aislab.demo.database.entity.ClientEntity;

public class ClientLiveData extends LiveData<ClientEntity> {
    private static final String TAG = "ClientLiveData";

    private final Query mQuery;
    private final ClientLiveData.MyValueEventListener mListener = new ClientLiveData.MyValueEventListener();

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

    public ClientLiveData(Query query, Class test) {
        this.mQuery = query;
    }

    public ClientLiveData(DatabaseReference ref) {
        this.mQuery = ref;
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
            ClientEntity entity = dataSnapshot.getValue(ClientEntity.class);
            entity.setId(dataSnapshot.getKey());
            setValue(entity);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + mQuery, databaseError.toException());
        }
    }
}
