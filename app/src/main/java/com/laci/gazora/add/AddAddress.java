package com.laci.gazora.add;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.laci.gazora.model.AddressItem;
import com.laci.gazora.MainActivity;
import com.laci.gazora.ProfileActivity;
import com.laci.gazora.R;
import com.laci.gazora.ShowAddressesActivity;

public class AddAddress extends AppCompatActivity {

    private static AddressItem addressItem;
    EditText editTextNewAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address);

        editTextNewAddress = findViewById(R.id.editTextNewAddress);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.gazora_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        menuItem.setVisible(false);

        MenuItem menuItemAdd = menu.findItem(R.id.add);
        menuItemAdd.setVisible(false);
        return true;
    }

    public void addNewAddress(View view) {
        String newAddress = editTextNewAddress.getText().toString();

        if (newAddress.equals("")) {
            Toast.makeText(AddAddress.this, "Írj be valamit he",Toast.LENGTH_LONG).show();
        }
        else {
            addressItem = new AddressItem(newAddress);
            Intent intent = new Intent(this, ShowAddressesActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public static AddressItem getAddressItem() {
        return addressItem;
    }

    public static void setAddressItem(AddressItem addressItem) {
        AddAddress.addressItem = addressItem;
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
                Intent intent1 = new Intent(this, ProfileActivity.class);
                startActivity(intent1);
                finish();
                return true;
            case R.id.show_addresses:
                Intent intent = new Intent(this, ShowAddressesActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.add:
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public void backToPrevious(View view) {
        Intent intent = new Intent(this, ShowAddressesActivity.class);
        startActivity(intent);
        finish();
    }
}