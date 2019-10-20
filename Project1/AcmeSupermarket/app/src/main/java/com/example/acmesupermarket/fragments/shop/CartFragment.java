package com.example.acmesupermarket.fragments.shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.acmesupermarket.Item;
import com.example.acmesupermarket.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartFragment extends Fragment {

    private static final String TAG = "CartFragment";

    List<Item> items = new ArrayList<>();
    ArrayAdapter<Item> cartAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        ListView list = v.findViewById(R.id.cart_list);
        Context context = Objects.requireNonNull(getActivity()).getApplicationContext();

        if (savedInstanceState != null) {
            ArrayList<Item> values = savedInstanceState.getParcelableArrayList("cart_items");
            if (values != null) {
                items = values;
            }
        }

        cartAdapter = new CartItemAdapter(context);
        list.setAdapter(cartAdapter);

        return v;
    }

    public void onSaveInstanceState(Bundle savedState) {

        super.onSaveInstanceState(savedState);
        savedState.putParcelableArrayList("cart_items", (ArrayList<? extends Parcelable>) items);
    }

    public void addItem(Item item){
        cartAdapter.add(item);
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
            ((TextView) row.findViewById(R.id.priceUnit)).setText(item.getPrice()+"");
            ((TextView) row.findViewById(R.id.quantityItem)).setText(item.getQuantity()+"");

            return (row);
        }
    }
}
