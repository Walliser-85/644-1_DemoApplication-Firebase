package ch.hevs.aislab.demo.database.async.account;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Pair;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class Transaction extends AsyncTask<Pair<AccountEntity, AccountEntity>, Void, Void> {

    private static final String TAG = "Transaction";

    private Application mApplication;
    private OnAsyncEventListener<Boolean> mCallBack;
    private Exception mException;

    public Transaction(Application application, OnAsyncEventListener callback) {
        mApplication = application;
        mCallBack = callback;
    }

    @Override
    protected Void doInBackground(Pair<AccountEntity, AccountEntity>... params) {
        try {
            for (Pair<AccountEntity, AccountEntity> accountPair : params)
                ((BaseApp) mApplication).getAccountRepository()
                        .transaction(accountPair.first, accountPair.second);
        } catch (Exception e) {
            mException = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(null);
            } else {
                mCallBack.onFailure(mException);
            }
        }
    }
}