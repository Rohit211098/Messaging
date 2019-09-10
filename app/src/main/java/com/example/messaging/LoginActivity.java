package com.example.messaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "tag";
    EditText email,password;
    Button login,register;
    String mEmail,mPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener stateListener ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.login_editText_email);
        password = findViewById(R.id.login_editText_password);
        mAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.button_login);
        register = findViewById(R.id.login_button_register);

        stateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = mAuth.getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }

            }
        };

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = email.getText().toString().trim();
                mPassword = password.getText().toString().trim();

                signIn();



            }
        });



    }

    private void signIn(){
        Log.e(TAG," " + mEmail + mPassword);
        mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(stateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.addAuthStateListener(stateListener);
    }
}
