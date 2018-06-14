package ch.hevs.aislab.demo.ui.client;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.database.async.client.DeleteClient;
import ch.hevs.aislab.demo.database.async.client.UpdateClient;
import ch.hevs.aislab.demo.database.entity.ClientEntity;
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.util.OnAsyncEventListener;
import ch.hevs.aislab.demo.viewmodel.client.ClientViewModel;

public class ClientActivity extends BaseActivity {

    private static final String TAG = "ClientActivity";

    private static final int EDIT_CLIENT = 1;
    private static final int DELETE_CLIENT = 2;

    private Toast mToast;

    private boolean mEditable;

    private EditText mEtFirstName;
    private EditText mEtLastName;
    private EditText mEtEmail;
    private EditText mEtPwd1;
    private EditText mEtPwd2;

    private ClientViewModel mViewModel;

    private ClientEntity mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_client);

        getLayoutInflater().inflate(R.layout.activity_client, frameLayout);

        initiateView();

        SharedPreferences settings = getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        String user = settings.getString(PREFS_USER, null);

        ClientViewModel.Factory factory = new ClientViewModel.Factory(getApplication(), user);
        mViewModel = ViewModelProviders.of(this, factory).get(ClientViewModel.class);
        mViewModel.getClient().observe(this, accountEntity -> {
            if (accountEntity != null) {
                mClient = accountEntity;
                updateContent();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == BaseActivity.position) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }
        /*
        The activity has to be finished manually in order to guarantee the navigation hierarchy working.
        */
        finish();
        return super.onNavigationItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, EDIT_CLIENT, Menu.NONE, getString(R.string.action_edit))
                .setIcon(R.drawable.ic_edit_white_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, DELETE_CLIENT, Menu.NONE, getString(R.string.action_delete))
                .setIcon(R.drawable.ic_delete_white_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == EDIT_CLIENT) {
            if (mEditable) {
                item.setIcon(R.drawable.ic_edit_white_24dp);
                switchEditableMode();
            } else {
                item.setIcon(R.drawable.ic_done_white_24dp);
                switchEditableMode();
            }
        }
        if (item.getItemId() == DELETE_CLIENT) {
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getString(R.string.action_delete));
            alertDialog.setCancelable(false);
            alertDialog.setMessage(getString(R.string.delete_msg));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.action_delete), (dialog, which) -> {
                new DeleteClient(getApplication(), new OnAsyncEventListener() {
                    @Override
                    public void onSuccess(Object object) {
                        logout();
                        Log.d(TAG, "deleteUser: success");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "deleteUser: failure", e);
                    }
                }).execute(mClient);
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.action_cancel), (dialog, which) -> alertDialog.dismiss());
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initiateView() {
        mEditable = false;
        mEtFirstName = findViewById(R.id.firstName);
        mEtLastName = findViewById(R.id.lastName);
        mEtEmail = findViewById(R.id.email);
        mEtPwd1 = findViewById(R.id.password);
        mEtPwd2 = findViewById(R.id.passwordRep);
    }

    private void switchEditableMode() {
        if (!mEditable) {
            LinearLayout linearLayout = findViewById(R.id.clientPasswordLayout);
            linearLayout.setVisibility(View.VISIBLE);
            mEtFirstName.setFocusable(true);
            mEtFirstName.setEnabled(true);
            mEtLastName.setFocusable(true);
            mEtLastName.setEnabled(true);
            mEtEmail.setFocusable(true);
            mEtEmail.setEnabled(true);
            mEtEmail.setFocusableInTouchMode(true);
            mEtEmail.requestFocus();
        } else {
            saveChanges(
                    mEtFirstName.getText().toString(),
                    mEtLastName.getText().toString(),
                    mEtEmail.getText().toString(),
                    mEtPwd1.getText().toString(),
                    mEtPwd2.getText().toString()
            );
            LinearLayout linearLayout = findViewById(R.id.clientPasswordLayout);
            linearLayout.setVisibility(View.GONE);
            mEtFirstName.setFocusable(false);
            mEtFirstName.setEnabled(false);
            mEtLastName.setFocusable(false);
            mEtLastName.setEnabled(false);
            mEtEmail.setFocusable(false);
            mEtEmail.setEnabled(false);
        }
        mEditable = !mEditable;
    }

    private void updateContent() {
        if (mClient != null) {
            mEtFirstName.setText(mClient.getFirstName());
            mEtLastName.setText(mClient.getLastName());
            mEtEmail.setText(mClient.getEmail());
        }
    }

    private void saveChanges(String firstName, String lastName, String email, String pwd, String pwd2) {
        if (!pwd.equals(pwd2) || pwd.length() < 5) {
            mToast = Toast.makeText(this, getString(R.string.error_edit_invalid_password), Toast.LENGTH_LONG);
            mToast.show();
            mEtPwd1.setText("");
            mEtPwd2.setText("");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEtEmail.setError(getString(R.string.error_invalid_email));
            mEtEmail.requestFocus();
            return;
        }
        mClient.setEmail(email);
        mClient.setFirstName(firstName);
        mClient.setLastName(lastName);
        mClient.setPassword(pwd);

        new UpdateClient(getApplication(), new OnAsyncEventListener() {
            @Override
            public void onSuccess(Object object) {
                Log.d(TAG, "updateClient: success");
                setResponse(true);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "updateClient: failure", e);
                setResponse(false);
            }
        }).execute(mClient);
    }

    private void setResponse(Boolean response) {
        if (response) {
            updateContent();
            mToast = Toast.makeText(this, getString(R.string.client_edited), Toast.LENGTH_LONG);
            mToast.show();
        } else {
            mEtEmail.setError(getString(R.string.error_used_email));
            mEtEmail.requestFocus();
        }
    }
}
