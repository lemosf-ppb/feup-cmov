package ui.shop;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.UUID;

import models.Client;
import models.TransactionItem;
import models.Voucher;
import services.repository.AcmeRepository;

public class ShopViewModel extends ViewModel {
    private static final int MAX_CART_ITEMS = 10;

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
            updateTotalPrice(discountAvailable);
            applyDiscount.setValue(false);
        }
    }

    public MutableLiveData<ArrayList<TransactionItem>> getTransactionItems() {
        if (transactionItems.getValue() == null) {
            loadTransactionItems();
        }
        return transactionItems;
    }

    public MutableLiveData<ArrayList<Voucher>> getVouchers() {
        if (vouchers.getValue() == null) {
            loadVouchers();
        }
        return vouchers;
    }

    public void syncDatabase(Client client) {
        AcmeRepository.getUnusedVouchers getUnusedVouchers = new AcmeRepository.getUnusedVouchers(client, this);
        new Thread(getUnusedVouchers).start();
    }

    private void loadVouchers() {
        ArrayList<Voucher> vouchersList = new ArrayList<>();
        vouchersList.add(new Voucher(UUID.randomUUID(), 5));
        vouchersList.add(new Voucher(UUID.randomUUID(), 15));
        vouchers.setValue(vouchersList);
    }

    private void loadTransactionItems() {
        transactionItems.setValue(new ArrayList<>());
        addTransactionItem(new TransactionItem(UUID.randomUUID(), "Batata", 10.6, 1));
        addTransactionItem(new TransactionItem(UUID.randomUUID(), "Tomate", 8.6, 1));
    }
}
