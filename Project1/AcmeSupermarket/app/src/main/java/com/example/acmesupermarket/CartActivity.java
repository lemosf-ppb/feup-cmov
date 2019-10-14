package com.example.acmesupermarket;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    Toolbar toolbar;
    List<Item> items = new ArrayList<>();
    ArrayAdapter<Item> cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView list = findViewById(R.id.cartList);
        cartAdapter = new CartItemAdapter();
        list.setAdapter(cartAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.loggedmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.photo) {
            String title = "Batata";
            String description = "Viscosa mas saborosa";
            double price = 10.6;
            int quantity = 2;
            Item item = new Item(title, description, price, quantity);
            //cartAdapter.add(item);
            Toast.makeText(getApplicationContext(), "Created Batata", Toast.LENGTH_LONG).show();
        }

        return true;
    }

    class CartItemAdapter extends ArrayAdapter<Item> {
        CartItemAdapter() {
            super(CartActivity.this, R.layout.cart_row, items);
        }

        @Override
        public @NonNull
        View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View row = convertView;
            if (row == null)
                row = getLayoutInflater().inflate(R.layout.cart_row, parent, false);
            Item item = items.get(position);
            ((TextView) row.findViewById(R.id.itemTitle)).setText(item.getTitle());
            ((TextView) row.findViewById(R.id.priceUnit)).setText((int) item.getPrice());
            ((TextView) row.findViewById(R.id.quantityItem)).setText(item.getQuantity());

            return (row);
        }
    }
}
