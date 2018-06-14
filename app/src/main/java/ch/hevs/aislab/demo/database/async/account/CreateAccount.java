package ch.hevs.aislab.demo.database.async.account;

import android.app.Application;
import android.os.AsyncTask;

import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;

public class CreateAccount extends AsyncTask<AccountEntity, Void, Void> {

    private static final String TAG = "CreateAccount";

    private Application mApplication;
    private OnAsyncEventListener<Boolean> mCallBack;
    private Exception mException;

    public CreateAccount(Application application, OnAsyncEventListener callback) {
        mApplication = application;
        mCallBack = callback;
    }

    @Override
    protected Void doInBackground(AccountEntity... params) {
        try {
            for (AccountEntity account : params)
                ((BaseApp) mApplication).getAccountRepository()
                        .insert(account);
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