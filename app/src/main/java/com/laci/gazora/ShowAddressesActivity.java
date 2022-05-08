package com.laci.gazora;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.laci.gazora.adapter.AddressAdapter;
import com.laci.gazora.add.AddAddress;
import com.laci.gazora.model.AddressItem;

import java.util.ArrayList;

public class ShowAddressesActivity extends AppCompatActivity {
    private static final String LOG_TAG = ShowAddressesActivity.class.getName();

    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerView;
    private ArrayList<AddressItem> mAddressList;
    private AddressAdapter mAddressAdapter;

    private int gridNumber = 1;

    private FirebaseFirestore mFirestore;
    private CollectionReference mAddresses;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_addresses);
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

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mAddressList = new ArrayList<>();

        mAddressAdapter = new AddressAdapter(this, mAddressList);

        mRecyclerView.setAdapter(mAddressAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mAddresses = mFirestore.collection("Users/"+currentUser.getUid()+"/Addresses");

        queryData();
    }

    public void queryData() {
        mAddressList.clear();

        if (AddAddress.getAddressItem() != null) {
            mAddresses.add(AddAddress.getAddressItem());
            Toast.makeText(ShowAddressesActivity.this, "Cím sikeresen hozzáadva!",Toast.LENGTH_LONG).show();
        }

        mAddresses.orderBy("name", Query.Direction.ASCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                AddressItem item = document.toObject(AddressItem.class);
                item.setId(document.getId());
                mAddressList.add(item);
            }

            mAddressAdapter.notifyDataSetChanged();
        });


        AddAddress.setAddressItem(null);
    }

    public void deleteAddress(AddressItem item) {
        DocumentReference ref = mAddresses.document(item._getId());
        ref.delete().addOnSuccessListener(success ->{
            Log.d(LOG_TAG, "Itten van egy DELETE művelet" + item._getId());
            Toast.makeText(ShowAddressesActivity.this, "Cím sikeresen törölve!",Toast.LENGTH_LONG).show();
        })
            .addOnFailureListener(fail -> {
                Toast.makeText(this, "szir szar", Toast.LENGTH_LONG).show();
            });

        queryData();
    }


    public void openAddress(AddressItem item) {
        DocumentReference ref = mAddresses.document(item._getId());
        String id = ref.getId();
        Intent intent = new Intent(this, ShowGasActivity.class);
        intent.putExtra("SECRET_KEY", id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.gazora_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAddressAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out_button:
                FirebaseAuth.getInstance().signOut();
                Intent intent0 = new Intent(this, MainActivity.class);
                startActivity(intent0);
                finish();
                return true;
            case R.id.profil:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.show_addresses:
                return true;
            case R.id.add:
                Intent intent1 = new Intent(this, AddAddress.class);
                startActivity(intent1);
                finish();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

}