package com.example.acmesupermarket.ui.transactions;

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
import androidx.lifecycle.ViewModelProviders;

import com.example.acmesupermarket.R;

import java.util.ArrayList;
import java.util.Objects;

import models.Item;
import models.Transaction;

public class TransactionsFragment extends Fragment {

    ArrayList<Transaction> transactions = new ArrayList<>();
    private TransactionsViewModel mViewModel;
    private TransactionAdapter listAdapter;
    private ExpandableListView simpleExpandableListView;

    public static TransactionsFragment newInstance() {
        return new TransactionsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.transactions_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TransactionsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Context context = Objects.requireNonNull(getActivity()).getApplicationContext();

        if (savedInstanceState != null) {
            ArrayList<Transaction> values = savedInstanceState.getParcelableArrayList("transactions");
            if (values != null) {
                transactions = values;
//                ((ShopActivity) getActivity()).setTab(1);
            }
        } else {
            loadTransactions();
        }

        simpleExpandableListView = view.findViewById(R.id.simpleExpandableListView);
        listAdapter = new TransactionAdapter(context, transactions, getResources());
        simpleExpandableListView.setAdapter(listAdapter);
    }

    public void onSaveInstanceState(Bundle savedState) {

        super.onSaveInstanceState(savedState);
        savedState.putParcelableArrayList("transactions", transactions);
    }

    private void loadTransactions() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Batata", 10.6, 1));
        items.add(new Item("Tomate", 8.6, 1));

        Transaction transaction = new Transaction("1", null, "20.0", true, items);
        transactions.add(transaction);
    }
}
