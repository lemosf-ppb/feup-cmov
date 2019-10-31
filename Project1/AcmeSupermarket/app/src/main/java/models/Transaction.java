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
    private String voucherId;
    private boolean useDiscounts;
    private Double totalPrice;

    public Transaction(String id, UUID userId, ArrayList<TransactionItem> transactionItems, String voucherId, boolean useDiscounts, Double totalPrice) {
        this.id = id;
        this.userId = userId;
        this.transactionItems = transactionItems;
        this.voucherId = voucherId;
        this.useDiscounts = useDiscounts;
        this.totalPrice = totalPrice;
    }

    public Transaction(JSONObject transactionObject) throws JSONException {
        this.id = transactionObject.getString("id");
        this.userId = UUID.fromString(transactionObject.getString("UserId"));
        this.useDiscounts = transactionObject.getBoolean("useDiscounts");
        this.totalPrice = transactionObject.getDouble("totalPrice");

        ArrayList<TransactionItem> transactionItems = new ArrayList<>();
        JSONArray transactionItemsArray = transactionObject.getJSONArray("TransactionItems");
        for (int i = 0; i < transactionItemsArray.length(); i++) {
            transactionItems.add(new TransactionItem(transactionItemsArray.getJSONObject(i)));
        }

        this.voucherId = null; //TODO: Update this later

        this.transactionItems = transactionItems;
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

    public String getVoucherId() {
        return voucherId;
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
        transactionObject.put("voucherId", voucherId);

        Log.e("transaction", transactionObject.toString());
        return transactionObject;
    }

}
