package models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import utils.Utils;

public class Transaction implements Serializable {
    static final long serialVersionUID = 1L;

    private String id;
    private UUID userId;
    private ArrayList<TransactionItem> transactionItems;
    private Voucher voucher;
    private boolean useDiscounts;
    private Double totalPrice;
    private Date createdAt;

    public Transaction(String id, UUID userId, ArrayList<TransactionItem> transactionItems, Voucher voucher, boolean useDiscounts, Double totalPrice) {
        this.id = id;
        this.userId = userId;
        this.transactionItems = transactionItems;
        this.voucher = voucher;
        this.useDiscounts = useDiscounts;
        this.totalPrice = totalPrice;
    }

    public Transaction(UUID userId, ArrayList<TransactionItem> transactionItems, Voucher voucher, boolean useDiscounts) {
        this.userId = userId;
        this.transactionItems = transactionItems;
        this.voucher = voucher;
        this.useDiscounts = useDiscounts;
    }

    public Transaction(JSONObject transactionObject) throws JSONException {
        this.id = transactionObject.getString("id");
        this.userId = UUID.fromString(transactionObject.getString("UserId"));
        this.useDiscounts = transactionObject.getBoolean("useDiscounts");
        this.totalPrice = transactionObject.getDouble("totalPrice");
        this.createdAt = Utils.parseDate(transactionObject.getString("createdAt"));

        ArrayList<TransactionItem> transactionItems = new ArrayList<>();
        JSONArray transactionItemsArray = transactionObject.getJSONArray("TransactionItems");
        for (int i = 0; i < transactionItemsArray.length(); i++) {
            transactionItems.add(new TransactionItem(transactionItemsArray.getJSONObject(i)));
        }

        if (!transactionObject.isNull("Voucher"))
            this.voucher = new Voucher(transactionObject.getJSONObject("Voucher"));

        this.transactionItems = transactionItems;
    }

    public String getId() {
        return id;
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

    public Date getCreatedAt() {
        return createdAt;
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
        transactionObject.put("voucherId", (voucher == null ? null : voucher.getId().toString()));

        return transactionObject;
    }

}
