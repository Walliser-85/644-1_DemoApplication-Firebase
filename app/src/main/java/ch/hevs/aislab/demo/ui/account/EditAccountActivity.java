package ch.hevs.aislab.demo.ui.account;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.database.entity.AccountEntity;
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.viewmodel.account.AccountViewModel;

public class EditAccountActivity extends BaseActivity {

    private final String TAG = "EditAccountActivity";

    private AccountEntity mAccount;
    private String mOwner;
    private boolean mEditMode;
    private Toast mToast;
    private EditText mEtAccountName;

    private AccountViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_edit_account, frameLayout);

        mOwner = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mEtAccountName = findViewById(R.id.accountName);
        mEtAccountName.requestFocus();
        Button saveBtn = findViewById(R.id.createAccountButton);
        saveBtn.setOnClickListener(view -> {
            saveChanges(mEtAccountName.getText().toString());
            onBackPressed();
            mToast.show();
        });

        Long accountId = getIntent().getLongExtra("accountId", 0L);
        if (accountId == 0L) {
            setTitle(getString(R.string.title_activity_create_account));
            mToast = Toast.makeText(this, getString(R.string.account_created), Toast.LENGTH_LONG);
            mEditMode = false;
        } else {
            setTitle(getString(R.string.title_activity_edit_account));
            saveBtn.setText(R.string.action_update);
            mToast = Toast.makeText(this, getString(R.string.account_edited), Toast.LENGTH_LONG);
            mEditMode = true;
        }

        AccountViewModel.Factory factory = new AccountViewModel.Factory(
                getApplication(), accountId);
        mViewModel = ViewModelProviders.of(this, factory).get(AccountViewModel.class);
        if (mEditMode) {
            mViewModel.getAccount().observe(this, accountEntity -> {
                if (accountEntity != null) {
                    mAccount = accountEntity;
                    mEtAccountName.setText(mAccount.getName());
                }
            });
        }
    }

    private void saveChanges(String accountName) {
        if (mEditMode) {
            if(!"".equals(accountName)) {
                mAccount.setName(accountName);
                mViewModel.updateAccount(mAccount);
            }
        } else {
            AccountEntity newAccount = new AccountEntity();
            newAccount.setOwner(mOwner);
            newAccount.setBalance(0.0d);
            newAccount.setName(accountName);
            mViewModel.createAccount(newAccount);
        }
    }
}
