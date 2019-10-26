package com.example.acmesupermarket.fragments.shop;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.acmesupermarket.TransactionAdapter;
import com.example.acmesupermarket.Item;
import com.example.acmesupermarket.R;
import com.example.acmesupermarket.ShopActivity;
import com.example.acmesupermarket.Transaction;

import java.util.ArrayList;
import java.util.Objects;

public class TransactionFragment extends Fragment {

    private static final String TAG = "TransactionFragment";

    ArrayList<Transaction> transactions = new ArrayList<>();
    private TransactionAdapter listAdapter;
    private ExpandableListView simpleExpandableListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_transactions, container, false);

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

        simpleExpandableListView = v.findViewById(R.id.simpleExpandableListView);
        listAdapter = new TransactionAdapter(context, transactions, getResources());
        simpleExpandableListView.setAdapter(listAdapter);

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
}
