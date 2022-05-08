package com.laci.gazora;

import
        androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.laci.gazora.model.UserModel;

public class SignUpActivity extends AppCompatActivity {
    private static final String LOG_TAG = SignUpActivity.class.getName();
    private static final String PREF_KEY = LoginActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 80085;



    EditText editTextUserName;
    EditText editTextPassword;
    EditText editTextPasswordAgain;
    EditText editTextEmail;
    EditText editTextName;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    private FirebaseFirestore mFirestore;
    private CollectionReference mUsers;
    private FirebaseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 80085) {
            finish();
        }

        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordAgain = findViewById(R.id.editTextPasswordAgain);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextName = findViewById(R.id.editTextName);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String email = preferences.getString("email", "");

        editTextEmail.setText(email);

        mFirestore = FirebaseFirestore.getInstance();
        mUsers = mFirestore.collection("Users");

        mAuth = FirebaseAuth.getInstance();

    }

    public void backToMain(View view) {
        finish();
    }

    public void signUp(View view) {
        String userName = editTextUserName.getText().toString();
        String password = editTextPassword.getText().toString();
        String passwordAgain = editTextPasswordAgain.getText().toString();
        String email = editTextEmail.getText().toString();
        String name = editTextName.getText().toString();

        if (!password.equals(passwordAgain)) {
            Log.e(LOG_TAG, "A két jelszó nem egyezik meg!");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "Sikeres regisztráció!");
                    Toast.makeText(SignUpActivity.this, "Sikeres regisztráció!",Toast.LENGTH_LONG).show();
                    currentUser = mAuth.getCurrentUser();
                    String uid = currentUser.getUid();
                    UserModel user = new UserModel(userName, email, name);
                    mUsers.document(uid).set(user);
                    finishSignUp();
                }
                else {
                    Log.d(LOG_TAG, "Sikertelen regisztráció!");
                    Toast.makeText(SignUpActivity.this, "Sikertelen regisztráció:" + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void finishSignUp() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("SECRET_KEY", 80085);
        startActivity(intent);
    }

}