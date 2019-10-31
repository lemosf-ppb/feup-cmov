package ui.transactions;

import android.content.Context;
import android.os.Bundle;
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

import models.Transaction;

public class TransactionsFragment extends Fragment {

    private TransactionsViewModel mViewModel;
    private TransactionAdapter transactionsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.transactions_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(TransactionsViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(requireActivity()).get(TransactionsViewModel.class);

        setTransactionsAdapter(view);

        setTransactionsObserver();
    }


    private void setTransactionsAdapter(View view) {
        Context context = requireActivity().getApplicationContext();
        ArrayList<Transaction> transactions = mViewModel.getTransactions().getValue();

        ExpandableListView transactionExpandableListView = view.findViewById(R.id.simpleExpandableListView);
        transactionsAdapter = new TransactionAdapter(context, transactions, getResources());
        transactionExpandableListView.setAdapter(transactionsAdapter);
    }

    private void setTransactionsObserver() {
        mViewModel.getTransactions().observe(this, transactions -> transactionsAdapter.setTransactions(transactions));
    }
}
