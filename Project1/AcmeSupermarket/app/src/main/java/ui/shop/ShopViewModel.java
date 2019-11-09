package ui.shop;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import models.Client;
import models.TransactionItem;
import models.Voucher;
import services.repository.AcmeRepository;
import utils.Utils;

public class ShopViewModel extends ViewModel {
    private static final int MAX_CART_ITEMS = 10;
    private static final String VOUCHERS_FILENAME = "vouchersData";

    public final MutableLiveData<ArrayList<TransactionItem>> transactionItems = new MutableLiveData<>();
    public final MutableLiveData<Double> totalPrice = new MutableLiveData<>((double) 0);
    public final MutableLiveData<Voucher> currentVoucher = new MutableLiveData<>();
    public final MutableLiveData<Boolean> applyDiscount = new MutableLiveData<>(false);
    public final MutableLiveData<ArrayList<Voucher>> vouchers = new MutableLiveData<>();
    public Double discountAvailable;

    public void addTransactionItem(TransactionItem transactionItem) {
        ArrayList<TransactionItem> transactionItemsList = transactionItems.getValue();
        if (transactionItemsList.contains(transactionItem)) {
            int index = transactionItemsList.indexOf(transactionItem);
            transactionItemsList.get(index).increaseQuantity();
        } else {
            transactionItemsList.add(transactionItem);
        }

        updateTotalPrice(transactionItem.getPrice());

        transactionItems.setValue(transactionItemsList);
    }

    public void decreaseTransactionItem(TransactionItem transactionItem) {
        ArrayList<TransactionItem> transactionItemsList = transactionItems.getValue();

        int index = transactionItemsList.indexOf(transactionItem);
        transactionItemsList.get(index).decreaseQuantity();

        updateTotalPrice(-transactionItem.getPrice());

        transactionItems.setValue(transactionItemsList);
    }

    public void removeTransactionItem(TransactionItem transactionItem) {
        ArrayList<TransactionItem> transactionItemsList = transactionItems.getValue();

        double itemsPrice = transactionItem.getTotalPrice();
        updateTotalPrice(-itemsPrice);

        transactionItemsList.remove(transactionItem);
        transactionItems.setValue(transactionItemsList);
    }

    public boolean isCartFull() {
        ArrayList<TransactionItem> transactionItemsList = transactionItems.getValue();

        int total = 0;
        for (TransactionItem i : transactionItemsList) {
            total += i.getQuantity();
        }

        return total == MAX_CART_ITEMS;
    }

    private void updateTotalPrice(double price) {
        double newPrice = totalPrice.getValue() + price;
        if (newPrice <= 0) {
            newPrice = 0;
        }
        totalPrice.setValue(newPrice);
    }

    public void setSelectedVoucher(Voucher voucher) {
        currentVoucher.setValue(voucher);
    }

    public void applyDiscount(boolean applyDiscountIsChecked) {
        if (applyDiscountIsChecked && !applyDiscount.getValue()) {
            updateTotalPrice(-discountAvailable);
            applyDiscount.setValue(true);
        } else if (!applyDiscountIsChecked && applyDiscount.getValue()) {
            ArrayList<TransactionItem> transactionItemsArray = transactionItems.getValue();
            double totalValue = 0;
            for (int i = 0; i < transactionItemsArray.size(); i++) {
                totalValue += transactionItemsArray.get(i).getTotalPrice();
            }
            totalPrice.setValue(totalValue);
            applyDiscount.setValue(false);
        }
    }

    public MutableLiveData<ArrayList<TransactionItem>> getTransactionItems() {
        if (transactionItems.getValue() == null) {
            transactionItems.setValue(new ArrayList<>());
        }
        return transactionItems;
    }

    public MutableLiveData<ArrayList<Voucher>> getVouchers(Client client, Context context) {
        if (vouchers.getValue() == null) {
            ArrayList<Voucher> vouchersLoaded = loadVouchers(client, context);
            this.vouchers.setValue(vouchersLoaded == null ? new ArrayList<>() : vouchersLoaded);
        }
        return vouchers;
    }

    public void syncDatabase(Client client) {
        AcmeRepository.getUnusedVouchers getUnusedVouchers = new AcmeRepository.getUnusedVouchers(client, this);
        new Thread(getUnusedVouchers).start();
    }

    public void saveVouchers(Client client, ArrayList<Voucher> vouchers, Context context) {
        Utils.saveObject(client.getUsername() + "_" + VOUCHERS_FILENAME, vouchers, context);
    }

    private ArrayList<Voucher> loadVouchers(Client client, Context context) {
        return (ArrayList<Voucher>) Utils.loadObject(client.getUsername() + "_" + VOUCHERS_FILENAME, context);
    }

    public void resetTransactionItems() {
        ArrayList<TransactionItem> clearCart = transactionItems.getValue();
        clearCart.clear();
        transactionItems.setValue(clearCart);
    }
}
