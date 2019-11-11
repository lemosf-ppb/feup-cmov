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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.acmesupermarket.R;

import java.util.ArrayList;

import models.Transaction;
import ui.login.LoginViewModel;

import static ui.Utils.disableBackButtonNavigation;

public class TransactionsFragment extends Fragment {

    private TransactionsViewModel transactionsViewModel;
    private TransactionAdapter transactionsAdapter;

    private LoginViewModel loginViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.transactions_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProvider provider = ViewModelProviders.of(requireActivity());
        loginViewModel = provider.get(LoginViewModel.class);
        transactionsViewModel = provider.get(TransactionsViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewModelProvider provider = ViewModelProviders.of(requireActivity());
        loginViewModel = provider.get(LoginViewModel.class);
        transactionsViewModel = provider.get(TransactionsViewModel.class);

        setTransactionsAdapter(view);

        setTransactionsObserver();

        disableBackButtonNavigation(requireActivity(), this);
    }


    private void setTransactionsAdapter(View view) {
        Context context = requireActivity().getApplicationContext();
        ArrayList<Transaction> transactions = transactionsViewModel.getTransactions(loginViewModel.getClient(), context).getValue();

        ExpandableListView transactionExpandableListView = view.findViewById(R.id.simpleExpandableListView);
        transactionsAdapter = new TransactionAdapter(context, transactions, getResources());
        transactionExpandableListView.setAdapter(transactionsAdapter);
    }

    private void setTransactionsObserver() {
        transactionsViewModel.transactions.observe(this, transactions -> transactionsAdapter.setTransactions(transactions));
    }
}
