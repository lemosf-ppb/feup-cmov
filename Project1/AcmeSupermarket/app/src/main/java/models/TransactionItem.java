package models;

import android.os.Parcel;
import android.os.Parcelable;

public class TransactionItem implements Parcelable {

    public static final Creator<TransactionItem> CREATOR = new Creator<TransactionItem>() {
        @Override
        public TransactionItem createFromParcel(Parcel in) {
            return new TransactionItem(in);
        }

        @Override
        public TransactionItem[] newArray(int size) {
            return new TransactionItem[size];
        }
    };
    private String title;
    private double price;
    private int quantity;

    public TransactionItem(String title, double price, int quantity) {
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    protected TransactionItem(Parcel in) {
        title = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeDouble(price);
        dest.writeInt(quantity);
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

    public boolean equals(Object object) {
        TransactionItem other = (TransactionItem) object;
        return this.title == other.title && this.price == other.price;
    }
}
