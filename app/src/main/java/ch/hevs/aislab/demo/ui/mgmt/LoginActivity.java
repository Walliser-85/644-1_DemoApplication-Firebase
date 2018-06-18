package ch.hevs.aislab.demo.ui.mgmt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


import ch.hevs.aislab.demo.BaseApp;
import ch.hevs.aislab.demo.R;
import ch.hevs.aislab.demo.database.repository.ClientRepository;
import ch.hevs.aislab.demo.ui.BaseActivity;
import ch.hevs.aislab.demo.ui.MainActivity;
import ch.hevs.aislab.demo.util.LocaleManager;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private ProgressBar mProgressBar;

    private ClientRepository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_login);

        String lang = getSharedPreferences(BaseActivity.PREFS_NAME, 0).getString(BaseActivity.PREFS_LNG, "en");
        LocaleManager.updateLanguage(this, lang);

        setContentView(R.layout.activity_login);

        mRepository = ((BaseApp) getApplication()).getClientRepository();
        mProgressBar = findViewById(R.id.progress);

        // Set up the login form.
        mEmailView = findViewById(R.id.email);

        mPasswordView = findViewById(R.id.password);

        Button emailSignInButton = findViewById(R.id.email_sign_in_button);
        emailSignInButton.setOnClickListener(view -> attemptLogin());

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    /**
     * Attempts to sign in or register the client specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            mPasswordView.setText("");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            mRepository.signIn(email, password, task -> {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    mEmailView.setText("");
                    mPasswordView.setText("");
                } else {
                    mEmailView.setError(getString(R.string.error_invalid_email));
                    mEmailView.requestFocus();
                    mPasswordView.setText("");
                }
                mProgressBar.setVisibility(View.GONE);
            });
        }
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}

