package com.example.acmesupermarket.ui.shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.acmesupermarket.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import models.Item;
import models.Voucher;

// Instances of this class are fragments representing a single
// object in our collection.
public class CartFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    private static final int MAX_CART_ITEMS = 10;

    List<Item> items = new ArrayList<>();
    ArrayAdapter<Item> cartAdapter;
    DecimalFormat df = new DecimalFormat("#.##");
    private double totalPrice = 0;
    private View v;
    private boolean applyDiscount = false;
    private double discount = 2;
    private int voucherDiscount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cart_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        v = view;
        ListView list = v.findViewById(R.id.cart_list);
        Context context = Objects.requireNonNull(getActivity()).getApplicationContext();

        if (savedInstanceState != null) {
            ArrayList<Item> values = savedInstanceState.getParcelableArrayList("cart_items");
            if (values != null) {
                items = values;
//                ((ShopActivity) getActivity()).setTab(0);
            }
            totalPrice = savedInstanceState.getDouble("total_price");
            applyDiscount = savedInstanceState.getBoolean("applyDiscount");
            discount = savedInstanceState.getDouble("discount");
        }

        cartAdapter = new CartItemAdapter(context);
        list.setAdapter(cartAdapter);

        setSelectedVoucher();

        TextView discount_value = v.findViewById(R.id.discount_value);
        discount_value.setText(String.format("%s", df.format(discount)));

        CheckBox apply_discount_checkbox = v.findViewById(R.id.apply_discount);
        apply_discount_checkbox.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if (apply_discount_checkbox.isChecked() && !applyDiscount) {
                updateTotalPrice(-discount);
                applyDiscount = true;
            } else if (!apply_discount_checkbox.isChecked() && applyDiscount) {
                updateTotalPrice(discount);
                applyDiscount = false;
            }
        });

        updateTotalPrice(0);
    }

    public void onSaveInstanceState(Bundle savedState) {

        super.onSaveInstanceState(savedState);
        savedState.putParcelableArrayList("cart_items", (ArrayList<? extends Parcelable>) items);
        savedState.putDouble("total_price", totalPrice);
        savedState.putBoolean("applyDiscount", applyDiscount);
        savedState.putDouble("discount", discount);
    }

    private void setSelectedVoucher() {
        TextView voucher_id_text_view = v.findViewById(R.id.voucher_id);
//        Voucher voucher = ((ShopActivity) getActivity()).getSelectedVoucher();
        Voucher voucher = new Voucher("oi", 1);
        String voucher_id = "None";
        voucherDiscount = 0;
        if (voucher != null) {
            voucher_id = voucher.getName();
            voucherDiscount = voucher.getDiscount();
        }
        voucher_id_text_view.setText(voucher_id);
    }

    public void addItem(Item item) {
        if (items.contains(item)) {
            int index = items.indexOf(item);
            items.get(index).increaseQuantity();
            cartAdapter.notifyDataSetChanged();
        } else {
            cartAdapter.add(item);
        }
        updateTotalPrice(item.getPrice());
    }

    public void removeItem(Item item) {
        double itemsPrice = item.getTotalPrice();
        updateTotalPrice(-itemsPrice);
        items.remove(item);
    }

    public boolean isFull() {
        int total = 0;
        for (Item i : items) {
            total += i.getQuantity();
        }
        return total == MAX_CART_ITEMS;
    }

    private void updateTotalPrice(double price) {
        totalPrice += price;
        TextView total_price_value = v.findViewById(R.id.total_price_value);

        double voucherPercent = (double) voucherDiscount / 100;

        double totalAfterVoucher = (1.0 - voucherPercent) * totalPrice;
        total_price_value.setText(String.format("%s", df.format(totalAfterVoucher)));
    }


    class CartItemAdapter extends ArrayAdapter<Item> {
        CartItemAdapter(Context context) {
            super(context, R.layout.cart_row, items);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public @NonNull
        View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View row = convertView;
            if (row == null)
                row = getLayoutInflater().inflate(R.layout.cart_row, parent, false);
            Item item = items.get(position);
            ((TextView) row.findViewById(R.id.itemTitle)).setText(item.getTitle());
            ((TextView) row.findViewById(R.id.priceUnit)).setText(item.getPrice() + "");
            ((TextView) row.findViewById(R.id.quantityItem)).setText(item.getQuantity() + "");

            row.findViewById(R.id.increase_btn).setOnClickListener(view -> {
                if (isFull()) {
                    Toast.makeText(getContext(), "Your cart is already full!", Toast.LENGTH_LONG).show();
                } else {
                    addItem(item);
                }
            });

            row.findViewById(R.id.decrease_btn).setOnClickListener(view -> {
                if (item.getQuantity() == 1) {
                    Toast.makeText(getContext(), "You only have one item of that type left!", Toast.LENGTH_LONG).show();
                } else {
                    item.decreaseQuantity();
                    cartAdapter.notifyDataSetChanged();
                    updateTotalPrice(-item.getPrice());
                }
            });

            row.findViewById(R.id.remove_btn).setOnClickListener(view -> {
                removeItem(item);
            });

            return (row);
        }
    }
}