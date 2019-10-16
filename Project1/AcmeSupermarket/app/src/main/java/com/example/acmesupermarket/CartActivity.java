package com.example.acmesupermarket;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    Toolbar toolbar;
    List<Item> items = new ArrayList<>();
    ArrayAdapter<Item> cartAdapter;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

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
            scan(true);
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

    public void scan(boolean qrcode) {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", qrcode ? "QR_CODE_MODE" : "PRODUCT_MODE");
            startActivityForResult(intent, 0);
        }
        catch (ActivityNotFoundException anfe) {
            showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    private static AlertDialog showDialog(final AppCompatActivity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, (dialogInterface, i) -> {
            Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            act.startActivity(intent);
        });
        downloadDialog.setNegativeButton(buttonNo, null);
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        byte[] baMess;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                try {
                    baMess = contents.getBytes(StandardCharsets.ISO_8859_1);
                }
                catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    return;
                }
                System.out.println("Format: " + format + "\nMessage: " + contents + "\n\nHex: " + byteArrayToHex(baMess));
            }
        }
    }

    String byteArrayToHex(byte[] ba) {
        StringBuilder sb = new StringBuilder(ba.length * 2);
        for(byte b: ba)
            sb.append(String.format("%02x", b));
        return sb.toString();
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
