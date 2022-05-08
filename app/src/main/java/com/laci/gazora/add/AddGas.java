package com.laci.gazora.add;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.laci.gazora.model.GasItem;
import com.laci.gazora.MainActivity;
import com.laci.gazora.ProfileActivity;
import com.laci.gazora.R;
import com.laci.gazora.ShowAddressesActivity;
import com.laci.gazora.ShowGasActivity;

public class AddGas extends AppCompatActivity {

    private static GasItem gasItem;
    EditText editTextNewGas;
    EditText editTextNewDate;

    private String secret_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_gas);

        editTextNewGas = findViewById(R.id.editTextNewGas);
        editTextNewDate = findViewById(R.id.editTextNewDate);

        secret_key = getIntent().getStringExtra("SECRET_KEY");
    }

    public void addNewGas(View view) {
        String newGas = editTextNewGas.getText().toString();
        String newDate = editTextNewDate.getText().toString();

        if (newGas.equals("") || newDate.equals("")) {
            Toast.makeText(AddGas.this, "Írj be valamit he",Toast.LENGTH_LONG).show();
        }
        else {
            gasItem = new GasItem(newGas,newDate);
            Intent intent = new Intent(this, ShowGasActivity.class);
            intent.putExtra("SECRET_KEY", secret_key);
            startActivity(intent);
            finish();
        }

    }

    public static GasItem getGasItem() {
        return gasItem;
    }

    public static void setGasItem(GasItem gasItem) {
        AddGas.gasItem = gasItem;
    }

    public void backToPrevious(View view) {
        Intent intent = new Intent(this, ShowGasActivity.class);
        intent.putExtra("SECRET_KEY", secret_key);
        startActivity(intent);
        finish();
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
}