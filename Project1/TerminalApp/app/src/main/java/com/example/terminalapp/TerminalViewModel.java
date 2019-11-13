package com.example.terminalapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TerminalViewModel extends ViewModel {

    public final MutableLiveData<RestCall.Response> response = new MutableLiveData<>();

    public void postTransaction(String transactionPayload) {
        RestCall postTransactions = new RestCall(this, transactionPayload);
        new Thread(postTransactions).start();
    }
}
