package com.laci.gazora;

import androidx.annotation.NonNull;
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

public class LoginActivity extends AppCompatActivity {
    private static final String PREF_KEY = LoginActivity.class.getPackage().toString();
    private static final String LOG_TAG = SignUpActivity.class.getName();

    private static final int SECRET_KEY = 80085;


    EditText editTextUserEmail;
    EditText editTextUserPassword;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 80085) {
            finish();
        }

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        editTextUserEmail = findViewById(R.id.editTextUserEmail);
        editTextUserPassword = findViewById(R.id.editTextUserPassword);

        mAuth = FirebaseAuth.getInstance();
    }


    public void backToMain(View view) {
        finish();
    }

    public void login(View view) {
        String email = editTextUserEmail.getText().toString();
        String password = editTextUserPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "Sikeres belépés!");
                    Toast.makeText(LoginActivity.this, "Sikeres belépés!",Toast.LENGTH_LONG).show();
                    finishLogin();
                }
                else {
                    Log.d(LOG_TAG, "Sikertelen belépés!");
                    Toast.makeText(LoginActivity.this, "Sikertelen belépés:" + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void finishLogin() {
        Intent intent = new Intent(this, ShowAddressesActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", editTextUserEmail.getText().toString());
        editor.apply();
    }
}