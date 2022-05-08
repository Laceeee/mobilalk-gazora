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
import com.laci.gazora.adapter.GasAdapter;
import com.laci.gazora.add.AddGas;
import com.laci.gazora.model.GasItem;

import java.util.ArrayList;

public class ShowGasActivity extends AppCompatActivity {
    private static final String LOG_TAG = ShowGasActivity.class.getName();

    private RecyclerView mRecyclerView;
    private ArrayList<GasItem> mGasList;
    private GasAdapter mGasAdapter;

    private int gridNumber = 1;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private CollectionReference mGases;
    private FirebaseUser currentUser;
    private String secret_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gas_page);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        currentUser = mAuth.getCurrentUser();

        secret_key = getIntent().getStringExtra("SECRET_KEY");

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mGasList = new ArrayList<>();

        mGasAdapter = new GasAdapter(this, mGasList);

        mRecyclerView.setAdapter(mGasAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mGases = mFirestore.collection("Users/"+currentUser.getUid()+"/Addresses/"+secret_key+"/Gas_states");

        queryData();
    }

    public void queryData() {
        mGasList.clear();

        if (AddGas.getGasItem() != null) {
            mGases.add(AddGas.getGasItem());
            Toast.makeText(ShowGasActivity.this, "Gázóra érték sikeresen hozzáadva!",Toast.LENGTH_LONG).show();
        }

        mGases.orderBy("date", Query.Direction.DESCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                GasItem item = document.toObject(GasItem.class);
                item.setId(document.getId());
                mGasList.add(item);
            }

            mGasAdapter.notifyDataSetChanged();
        });


        AddGas.setGasItem(null);
    }

    public void deleteGas(GasItem item) {
        DocumentReference ref = mGases.document(item._getId());
        ref.delete().addOnSuccessListener(success ->{
            Log.d(LOG_TAG, "Itten van egy DELETE művelet" + item._getId());
            Toast.makeText(ShowGasActivity.this, "Gázóra érték sikeresen törölve!",Toast.LENGTH_LONG).show();

        })
                .addOnFailureListener(fail -> {
                    Toast.makeText(this, "szir szar", Toast.LENGTH_LONG).show();
                });

        queryData();
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
                mGasAdapter.getFilter().filter(s);
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
                Intent intent1 = new Intent(this, ShowAddressesActivity.class);
                startActivity(intent1);
                finish();
                return true;
            case R.id.add:
                Intent intent2 = new Intent(this, AddGas.class);
                intent2.putExtra("SECRET_KEY", secret_key);
                startActivity(intent2);
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