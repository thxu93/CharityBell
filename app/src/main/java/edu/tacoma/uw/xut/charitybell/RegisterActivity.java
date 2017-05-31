/**
 * Charity Bell
 * Adam Waldron and Thomas Xu
 * TCSS450
 *
 * RegisterActivity
 * This activity class represents the register activity. In this activity the user can create a new
 * account with their name, email, and password. If the account is successfully created, the user is
 * returned to the login page to log into the app.
 */
package edu.tacoma.uw.xut.charitybell;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button mRegisterButton;
    private EditText mEmail, mPassword, mDisplayName;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRegisterButton = (Button) findViewById(R.id.registerButton);
        mEmail = (EditText) findViewById(R.id.emailRegister);
        mPassword = (EditText) findViewById(R.id.passwordRegister);
        mDisplayName = (EditText) findViewById(R.id.nameRegister);


        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String name = mDisplayName.getText().toString().trim();

                // Form validation.
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Enter a name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Creates a user with the user data provided and stores this data on the Firebase DB
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(mDisplayName.getText().toString())
                                            .build();
                                    mAuth.getCurrentUser().updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Creates user keys and values into Firebase DB
                                                mDatabase.child("users").child(mAuth.getCurrentUser()
                                                        .getUid()).child("name")
                                                        .setValue(mAuth.getCurrentUser()
                                                                .getDisplayName());
                                                mDatabase.child("users").child(mAuth.getCurrentUser()
                                                        .getUid()).child("email")
                                                        .setValue(mAuth.getCurrentUser()
                                                                .getEmail());
                                                mDatabase.child("users").child(mAuth.getCurrentUser()
                                                        .getUid()).child("alarms");

                                            }
                                        }
                                    });

                                    Toast.makeText(RegisterActivity.this, "Account Created!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Could not create account: " + task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }

                            }
                        });

            }
        });

    }

}
