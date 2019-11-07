package ui.transactions;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import models.Client;
import models.Transaction;
import models.TransactionItem;
import services.repository.AcmeRepository;
import utils.Utils;

public class TransactionsViewModel extends ViewModel {
    private static final String TRANSACTIONS_FILENAME = "transactionsData";

    public final MutableLiveData<ArrayList<Transaction>> transactions = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Transaction>> getTransactions(Context context) {
        if (transactions.getValue() == null) {
            ArrayList<Transaction> transactionsLoaded = loadTransactions(context);
            this.transactions.setValue(transactionsLoaded == null ? new ArrayList<>() : transactionsLoaded);
        }
        return transactions;
    }

    private void loadTransactions() {
        ArrayList<TransactionItem> transactionItems = new ArrayList<>();
        transactionItems.add(new TransactionItem(UUID.randomUUID(), "Batata", 10.6, 1));
        transactionItems.add(new TransactionItem(UUID.randomUUID(), "Tomate", 8.6, 1));

        Transaction transaction = new Transaction(String.valueOf(Math.random()), UUID.randomUUID(), transactionItems, null, false, 0.0, new Date());
        ArrayList<Transaction> transactionsList = new ArrayList<>();
        transactionsList.add(transaction);
        transactions.postValue(transactionsList);
    }

    public void syncDatabase(Client client) {
        AcmeRepository.getTransactions getTransactions = new AcmeRepository.getTransactions(client, this);
        new Thread(getTransactions).start();
    }

    public void saveTransactions(ArrayList<Transaction> transactions, Context context) {
        Utils.saveObject(TRANSACTIONS_FILENAME, transactions, context);
    }

    private ArrayList<Transaction> loadTransactions(Context context) {
        return (ArrayList<Transaction>) Utils.loadObject(TRANSACTIONS_FILENAME, context);
    }

}
