package ch.hevs.aislab.demo.viewmodel.account;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import java.util.List;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.database.async.account.DeleteAccount;
import ch.hevs.aislab.demo.database.async.account.Transaction;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.database.repository.AccountRepository;
import ch.hevs.aislab.demo.database.repository.ClientRepository;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class AccountListViewModel extends AndroidViewModel {

    private static final String TAG = "AccountListViewModel";

    private AccountRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    //TODO: Implement this using Firebase.
    //private final MediatorLiveData<List<ClientAccounts>> mObservableClientAccounts;
    private final MediatorLiveData<List<AccountEntity>> mObservableOwnAccounts;

    public AccountListViewModel(@NonNull Application application,
                                final String ownerId, ClientRepository clientRepository, AccountRepository accountRepository) {
        super(application);

        mRepository = accountRepository;

        //TODO: Implement this using Firebase.
        //mObservableClientAccounts = new MediatorLiveData<>();
        mObservableOwnAccounts = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        //TODO: Implement this using Firebase.
        //mObservableClientAccounts.setValue(null);
        mObservableOwnAccounts.setValue(null);

        //TODO: Implement this using Firebase.
        //LiveData<List<ClientAccounts>> clientAccounts = clientRepository.getOtherClientsWithAccounts(ownerId);
        LiveData<List<AccountEntity>> ownAccounts = mRepository.getByOwner(ownerId);

        // observe the changes of the entities from the database and forward them
        //TODO: Implement this using Firebase.
        //mObservableClientAccounts.addSource(clientAccounts, mObservableClientAccounts::setValue);
        mObservableOwnAccounts.addSource(ownAccounts, mObservableOwnAccounts::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final String mOwnerId;

        private final ClientRepository mClientRepository;

        private final AccountRepository mAccountRepository;

        public Factory(@NonNull Application application, String ownerId) {
            mApplication = application;
            mOwnerId = ownerId;
            mClientRepository = ((BaseApp) application).getClientRepository();
            mAccountRepository = ((BaseApp) application).getAccountRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new AccountListViewModel(mApplication, mOwnerId, mClientRepository, mAccountRepository);
        }
    }

    /**
     * Expose the LiveData ClientAccounts query so the UI can observe it.
     */
    //TODO: Implement this using Firebase.
    /*public LiveData<List<ClientAccounts>> getClientAccounts() {
        return mObservableClientAccounts;
    }*/

    /**
     * Expose the LiveData AccountEntities query so the UI can observe it.
     */
    public LiveData<List<AccountEntity>> getOwnAccounts() {
        return mObservableOwnAccounts;
    }

    public void deleteAccount(AccountEntity account) {
        new DeleteAccount(getApplication(), new OnAsyncEventListener() {
            @Override
            public void onSuccess(Object object) {
                Log.d(TAG, "deleteAccount: success");
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "deleteAccount: failure", e);
            }
        }).execute(account);
    }

    public void executeTransaction(final AccountEntity sender, final AccountEntity recipient) {
        //noinspection unchecked
        new Transaction(getApplication(), new OnAsyncEventListener() {
            @Override
            public void onSuccess(Object object) {
                Log.d(TAG, "transaction: success");
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "transaction: failure", e);
            }
        }).execute(Pair.create(sender, recipient));
    }
}
