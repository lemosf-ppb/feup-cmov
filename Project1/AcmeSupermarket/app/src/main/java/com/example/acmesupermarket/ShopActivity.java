package com.example.acmesupermarket;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.acmesupermarket.fragments.SectionsStatePagerAdapter;
import com.example.acmesupermarket.fragments.shop.CartFragment;
import com.example.acmesupermarket.fragments.shop.TransactionFragment;
import com.example.acmesupermarket.fragments.shop.VoucherFragment;
import com.google.android.material.tabs.TabLayout;

import java.nio.charset.StandardCharsets;

public class ShopActivity extends AppCompatActivity {

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    Toolbar toolbar;
    private CartFragment cartFragment;
    private Voucher selectedVoucher = null;
    private ViewPager mViewPager;
    private TabLayout tabs;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            selectedVoucher = savedInstanceState.getParcelable("active_voucher");
        }
        setContentView(R.layout.activity_shop);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = findViewById(R.id.shop_view_pager);
        setupViewPager(mViewPager);

        tabs = findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        setViewPager(0);
                        break;
                    case 1:
                        setViewPager(1);
                        break;
                    case 2:
                        setViewPager(2);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void onSaveInstanceState(Bundle savedState) {

        super.onSaveInstanceState(savedState);
        savedState.putParcelable("active_voucher", selectedVoucher);
    }

    private void setupViewPager(ViewPager mViewPager) {
        cartFragment = new CartFragment();

        SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(cartFragment, "CartFragment");
        adapter.addFragment(new TransactionFragment(), "TransactionFragment");
        adapter.addFragment(new VoucherFragment(), "VoucherFragment");
        mViewPager.setAdapter(adapter);
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

            if(cartFragment.isFull()){
                Toast.makeText(getApplicationContext(), "Your cart is already full!", Toast.LENGTH_LONG).show();
            }
            else {
                scan(true);
                String title = "Batata";
                double price = 10.6;
                int quantity = 1;
                Item item = new Item(title, price, quantity);

                cartFragment.addItem(item);
            }
        }

        return true;
    }

    public void scan(boolean qrcode) {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", qrcode ? "QR_CODE_MODE" : "PRODUCT_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
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
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    return;
                }
                System.out.println("Format: " + format + "\nMessage: " + contents + "\n\nHex: " + byteArrayToHex(baMess));
            }
        }
    }

    String byteArrayToHex(byte[] ba) {
        StringBuilder sb = new StringBuilder(ba.length * 2);
        for (byte b : ba)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    public void setViewPager(int fragmentNumber) {
        mViewPager.setCurrentItem(fragmentNumber);
    }


    //TODO: Descobrir porque raio isto faz crashar
    public void setTab(int index) {
        //tabs.getTabAt(index).select();
    }

    public void setSelectedVoucher(Voucher voucher)
    {
        selectedVoucher = voucher;
    }

    public Voucher getSelectedVoucher(){
        return selectedVoucher;
    }
}
