package models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class Transaction {
    private String id;
    private UUID userId;
    private ArrayList<TransactionItem> transactionItems;
    private Voucher voucher;
    private boolean useDiscounts;
    private Double totalPrice;

    public Transaction(String id, UUID userId, ArrayList<TransactionItem> transactionItems, Voucher voucher, boolean useDiscounts, Double totalPrice) {
        this.id = id;
        this.userId = userId;
        this.transactionItems = transactionItems;
        this.voucher = voucher;
        this.useDiscounts = useDiscounts;
        this.totalPrice = totalPrice;
    }

    public String getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public ArrayList<TransactionItem> getTransactionItems() {
        return transactionItems;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public boolean isUseDiscounts() {
        return useDiscounts;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public JSONObject getAsJSON() throws JSONException {
        JSONObject transactionObject = new JSONObject();
        transactionObject.put("userId", userId);
        JSONArray productsList = new JSONArray();
        for (int i = 0; i < transactionItems.size(); i++) {
            TransactionItem transactionItem = transactionItems.get(i);
            JSONObject transactionItemObject = new JSONObject();
            transactionItemObject.put("uuid", transactionItem.getId());
            transactionItemObject.put("price", transactionItem.getPrice());
            transactionItemObject.put("quantity", transactionItem.getQuantity());
            productsList.put(transactionItemObject);
        }
        transactionObject.put("productsList", productsList);
        transactionObject.put("useDiscounts", useDiscounts);
        transactionObject.put("voucherId", (voucher == null ? null : voucher.getId()));

        Log.e("transaction", transactionObject.toString());
        return transactionObject;
    }

}
