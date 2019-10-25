package com.example.acmesupermarket.fragments.shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.acmesupermarket.Item;
import com.example.acmesupermarket.R;
import com.example.acmesupermarket.ShopActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartFragment extends Fragment {

    private static final String TAG = "CartFragment";
    private static final int MAX_CART_ITEMS = 10;

    List<Item> items = new ArrayList<>();
    ArrayAdapter<Item> cartAdapter;
    private double totalPrice = 0;
    private View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_cart, container, false);

        ListView list = v.findViewById(R.id.cart_list);
        Context context = Objects.requireNonNull(getActivity()).getApplicationContext();

        if (savedInstanceState != null) {
            ArrayList<Item> values = savedInstanceState.getParcelableArrayList("cart_items");
            if (values != null) {
                items = values;
                ((ShopActivity) getActivity()).setTab(0);
            }
            this.totalPrice = savedInstanceState.getDouble("total_price");
        }

        cartAdapter = new CartItemAdapter(context);
        list.setAdapter(cartAdapter);

        return v;
    }

    public void onSaveInstanceState(Bundle savedState) {

        super.onSaveInstanceState(savedState);
        savedState.putParcelableArrayList("cart_items", (ArrayList<? extends Parcelable>) items);
        savedState.putDouble("total_price", totalPrice);
    }

    public void addItem(Item item) {
        if(items.contains(item)){
            int index = items.indexOf(item);
            items.get(index).increaseQuantity();
            cartAdapter.notifyDataSetChanged();
        }
        else {
            cartAdapter.add(item);
        }
        updateTotalPrice(item.getPrice());
    }

    public void removeItem(Item item) {
        double itemsPrice = item.getTotalPrice();
        updateTotalPrice(-itemsPrice);
        items.remove(item);
    }

    public boolean isFull(){
        int total = 0;
        for(Item i : items){
            total += i.getQuantity();
        }
        return total == MAX_CART_ITEMS;
    }

    private void updateTotalPrice(double price){
        totalPrice += price;
        TextView total_price_value = v.findViewById(R.id.total_price_value);
        total_price_value.setText(String.format("%s", totalPrice));
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
                if(isFull()){
                    Toast.makeText(getContext(), "Your cart is already full!", Toast.LENGTH_LONG).show();
                }
                else {
                    addItem(item);
                }
            });

            row.findViewById(R.id.decrease_btn).setOnClickListener(view -> {
                if(item.getQuantity() == 1){
                    Toast.makeText(getContext(), "You only have one item of that type left!", Toast.LENGTH_LONG).show();
                }
                else {
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
