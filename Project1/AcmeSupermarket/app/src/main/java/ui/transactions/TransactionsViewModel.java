package ui.transactions;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import models.Client;
import models.Transaction;
import services.repository.AcmeRepository;
import utils.Utils;

public class TransactionsViewModel extends ViewModel {
    private static final String TRANSACTIONS_FILENAME = "transactionsData";

    public final MutableLiveData<ArrayList<Transaction>> transactions = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Transaction>> getTransactions(Client client, Context context) {
        if (transactions.getValue() == null) {
            ArrayList<Transaction> transactionsLoaded = loadTransactions(client, context);
            this.transactions.setValue(transactionsLoaded == null ? new ArrayList<>() : transactionsLoaded);
        }
        return transactions;
    }

    public void syncDatabase(Client client) {
        AcmeRepository.getTransactions getTransactions = new AcmeRepository.getTransactions(client, this);
        new Thread(getTransactions).start();
    }

    public void saveTransactions(Client client, ArrayList<Transaction> transactions, Context context) {
        Utils.saveObject(client.getUsername() + "_" + TRANSACTIONS_FILENAME, transactions, context);
    }

    private ArrayList<Transaction> loadTransactions(Client client, Context context) {
        return (ArrayList<Transaction>) Utils.loadObject(client.getUsername() + "_" + TRANSACTIONS_FILENAME, context);
    }

}
