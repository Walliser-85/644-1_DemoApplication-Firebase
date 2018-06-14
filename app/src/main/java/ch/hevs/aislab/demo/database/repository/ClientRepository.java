package ch.hevs.aislab.demo.database.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class ClientRepository {

    private static final String TAG = "ClientRepository";

    private static ClientRepository sInstance;

    private ClientRepository() {
    }

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

    public void signIn(final String email, final String password, final OnCompleteListener<AuthResult> listener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
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

    public void register(final ClientEntity client, final OnAsyncEventListener callback) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(client.getEmail(), client.getPassword()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                client.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                insert(client, callback);
            } else {
                callback.onFailure(task.getException());
            }
        });
    }

    private void insert(final ClientEntity client, final OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("clients")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(client, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        Log.d(TAG, "Firebase DB Insert failure!", databaseError.toException());
                        callback.onFailure(databaseError.toException());
                        FirebaseAuth.getInstance().getCurrentUser().delete()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Rollback successful: User account deleted");
                                    } else {
                                        Log.d(TAG, "Rollback failed: signInWithEmail:failure", task.getException());
                                    }
                                });
                    } else {
                        Log.d(TAG, "Firebase DB Insert successful!");
                        callback.onSuccess(null);
                    }
                });
    }

    public void update(final ClientEntity client) {
        //TODO: Implement this using Firebase.
    }

    public void delete(final ClientEntity client) {
        //TODO: Implement this using Firebase.
    }
}
