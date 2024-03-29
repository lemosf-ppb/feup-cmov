package models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class TransactionItem implements Serializable {
    static final long serialVersionUID = 1L;

    private UUID id;
    private String name;
    private double price;
    private int quantity;

    public TransactionItem(UUID id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public TransactionItem(JSONObject transactionItemObject) throws JSONException {
        this.id = UUID.fromString(transactionItemObject.getString("uuid"));
        this.price = transactionItemObject.getDouble("price");
        this.quantity = transactionItemObject.getInt("quantity");
    }

    public void increaseQuantity() {
        quantity++;
    }

    public void decreaseQuantity() {
        quantity--;
    }

    public double getTotalPrice() {
        return quantity * price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionItem that = (TransactionItem) o;
        return Objects.equals(id, that.id);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
