package nl.hu.frontenddevelopment.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nl.hu.frontenddevelopment.Model.Person;
import nl.hu.frontenddevelopment.R;

public class EmailPasswordActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mNameField;
    private EditText mPhoneField;
    private EditText mSidenoteField;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password);

        // Views
        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);
        mNameField = (EditText) findViewById(R.id.field_name);
        mPhoneField = (EditText) findViewById(R.id.field_phone);
        mSidenoteField = (EditText) findViewById(R.id.field_sidenote);

        // Buttons
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                hideProgressDialog();
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createAccount(Person person, String password) {
        if (!validateForm()) return;

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(person.getEmail(), password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                    } else {
                        signIn(person, password);
                    }
                }
            });
    }

    private void signIn(Person person, String password) {
        if (!validateForm()) return;

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(person.getEmail(), password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                    } else {
                        hideProgressDialog();

                        addToPersonDatabase(person);

                        Toast.makeText(EmailPasswordActivity.this, R.string.signed_in, Toast.LENGTH_SHORT).show();
                        goToHomeActivity();
                    }
                }
            });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required min. 6 chars.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }
        return valid;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_create_account_button) {
            Person person = new Person();
            person.setName(mNameField.getText().toString());
            person.setEmail(mEmailField.getText().toString());
            person.setPhonenumber(mPasswordField.getText().toString());
            person.setSidenote(mSidenoteField.getText().toString());
            person.setProfilePhoto(getString(R.string.defaultProfilePhoto));

            String password = mPasswordField.getText().toString();
            createAccount(person, password);
        } else if (i == R.id.email_sign_in_button) {
            Person person = new Person();
            person.setName(mNameField.getText().toString());
            person.setEmail(mEmailField.getText().toString());
            person.setPhonenumber(mPhoneField.getText().toString());
            person.setSidenote(mSidenoteField.getText().toString());
            person.setProfilePhoto(getString(R.string.defaultProfilePhoto));

            String password = mPasswordField.getText().toString();
            signIn(person, password);
        }
    }

    private void addToPersonDatabase(Person person) {
        // TODO: replace to BaseActivity
        // TODO: Check for double data!?
        mDatabase.child("persons").push().setValue(person);
    }
}