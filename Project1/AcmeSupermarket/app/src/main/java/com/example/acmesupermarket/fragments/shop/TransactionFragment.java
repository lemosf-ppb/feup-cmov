package com.example.acmesupermarket.fragments.shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
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
import com.example.acmesupermarket.ShopActivity;
import com.example.acmesupermarket.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionFragment extends Fragment {

    private static final String TAG = "TransactionFragment";

    List<Transaction> transactions = new ArrayList<>();
    ArrayAdapter<Transaction> transactionAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_transactions, container, false);

        ListView list = v.findViewById(R.id.transactions_list);
        Context context = Objects.requireNonNull(getActivity()).getApplicationContext();

        if (savedInstanceState != null) {
            ArrayList<Transaction> values = savedInstanceState.getParcelableArrayList("transactions");
            if (values != null) {
                transactions = values;
                ((ShopActivity) getActivity()).setTab(1);
            }
        }
        else{
            loadTransactions();
        }

        transactionAdapter = new TransactionAdapter(context);
        list.setAdapter(transactionAdapter);

        return v;
    }

    public void onSaveInstanceState(Bundle savedState) {

        super.onSaveInstanceState(savedState);
        savedState.putParcelableArrayList("transactions", (ArrayList<? extends Parcelable>) transactions);
    }

    private void loadTransactions(){
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Batata", 10.6, 1));
        items.add(new Item("Tomate", 8.6, 1));

        Transaction transaction = new Transaction("1",null, "20.0", true, items);
        transactions.add(transaction);
    }

    class TransactionAdapter extends ArrayAdapter<Transaction> {
        Resources res;

        TransactionAdapter(Context context) {
            super(context, R.layout.transaction_row, transactions);
            res = getResources();
        }

        @SuppressLint("SetTextI18n")
        @Override
        public @NonNull
        View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View row = convertView;
            if (row == null)
                row = getLayoutInflater().inflate(R.layout.transaction_row, parent, false);
            Transaction transaction = transactions.get(position);
            ((TextView) row.findViewById(R.id.transaction_id)).setText(transaction.getId());

            String usedDiscounts = res.getString(R.string.used_discounts, transaction.hasUsedDiscounts());
            ((TextView) row.findViewById(R.id.used_discounts)).setText(usedDiscounts);

            String totalPrice = res.getString(R.string.total_payed_price, transaction.getPrice());
            ((TextView) row.findViewById(R.id.transaction_price)).setText(totalPrice);

            return (row);
        }
    }

}
