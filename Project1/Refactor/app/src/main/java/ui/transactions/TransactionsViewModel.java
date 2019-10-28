package ui.transactions;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import models.Transaction;
import models.TransactionItem;

public class TransactionsViewModel extends ViewModel {

    public final MutableLiveData<ArrayList<Transaction>> transactions = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Transaction>> getTransactions() {
        if (transactions.getValue() == null) {
            loadTransactions();
        }
        return transactions;
    }

    private void loadTransactions() {
        ArrayList<TransactionItem> transactionItems = new ArrayList<>();
        transactionItems.add(new TransactionItem("Batata", 10.6, 1));
        transactionItems.add(new TransactionItem("Tomate", 8.6, 1));

        Transaction transaction = new Transaction("1", null, "20.0", true, transactionItems);
        ArrayList<Transaction> transactionsList = new ArrayList<>();
        transactionsList.add(transaction);
        transactions.setValue(transactionsList);
    }
}
