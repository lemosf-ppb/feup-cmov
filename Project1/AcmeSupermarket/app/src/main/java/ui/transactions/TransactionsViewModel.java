package ui.transactions;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.UUID;

import models.Client;
import models.Transaction;
import models.TransactionItem;
import services.repository.AcmeRepository;

public class TransactionsViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Transaction>> transactions = new MutableLiveData<>(new ArrayList<>());

    public MutableLiveData<ArrayList<Transaction>> getTransactions() {
        if (transactions.getValue() == null) {
            loadTransactions();
        }
        return transactions;
    }

    private void loadTransactions() {
        ArrayList<TransactionItem> transactionItems = new ArrayList<>();
        transactionItems.add(new TransactionItem(UUID.randomUUID(), "Batata", 10.6, 1));
        transactionItems.add(new TransactionItem(UUID.randomUUID(), "Tomate", 8.6, 1));

        Transaction transaction = new Transaction(String.valueOf(Math.random()), UUID.randomUUID(), transactionItems, null, false, 0.0);
        ArrayList<Transaction> transactionsList = new ArrayList<>();
        transactionsList.add(transaction);
        transactions.postValue(transactionsList);
    }

    public void syncDatabase(Client client) {
        AcmeRepository.getTransactions getTransactions = new AcmeRepository.getTransactions(client, this);
        new Thread(getTransactions).start();
    }
}
