package ui.transactions;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import models.Item;
import models.Transaction;

public class TransactionsViewModel extends ViewModel {

    public final MutableLiveData<ArrayList<Transaction>> transactions = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Transaction>> getTransactions() {
        if (transactions.getValue() == null) {
            loadTransactions();
        }
        return transactions;
    }

    private void loadTransactions() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Batata", 10.6, 1));
        items.add(new Item("Tomate", 8.6, 1));

        Transaction transaction = new Transaction("1", null, "20.0", true, items);
        ArrayList<Transaction> transactionsList = new ArrayList<>();
        transactionsList.add(transaction);
        transactions.setValue(transactionsList);
    }
}
