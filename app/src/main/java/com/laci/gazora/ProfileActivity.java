package com.laci.gazora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.laci.gazora.model.UserModel;

public class ProfileActivity extends AppCompatActivity {
    private static final String LOG_TAG = ProfileActivity.class.getName();

    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private FirebaseFirestore mFirestore;
    private CollectionReference mUsers;
    private FirebaseUser currentUser;

    private UserModel userData;

    TextView name;
    TextView userName;
    TextView email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        mAuth = FirebaseAuth.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user");
            currentUser = mAuth.getCurrentUser();
        }
        else {
            Log.d(LOG_TAG, "Unauthenticated user");
            finish();
        }

        mUsers = FirebaseFirestore.getInstance().collection("Users/");

        name = findViewById(R.id.nameText);
        userName = findViewById(R.id.userNameText);
        email = findViewById(R.id.emailText);

        queryData();

    }

    public void queryData() {
        DocumentReference ref = mUsers.document(currentUser.getUid());
        ref.get().addOnSuccessListener(queryDocumentSnapshot -> {
            userData = queryDocumentSnapshot.toObject(UserModel.class);
            name.setText(userData.getName());
            userName.setText(userData.getUserName());
            email.setText(userData.getEmail());
        });




    }

    public void deleteProfile(View view) {
        DocumentReference ref = mUsers.document(currentUser.getUid());
        ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ProfileActivity.this, "Profil sikeresen törölve!",Toast.LENGTH_LONG).show();
                finish();
            }
        });
        currentUser.delete();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.gazora_menu, menu);
        MenuItem menuItemSearch = menu.findItem(R.id.search_bar);
        menuItemSearch.setVisible(false);

        MenuItem menuItemAdd = menu.findItem(R.id.add);
        menuItemAdd.setVisible(false);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out_button:
                FirebaseAuth.getInstance().signOut();
                Intent intent0 = new Intent(this, MainActivity.class);
                startActivity(intent0);
                finish();
                return true;
            case R.id.profil:
                return true;
            case R.id.show_addresses:
                Intent intent = new Intent(this, ShowAddressesActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}