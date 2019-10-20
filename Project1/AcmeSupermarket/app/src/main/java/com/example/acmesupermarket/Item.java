package com.example.acmesupermarket;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {

    private String title;
    private double price;
    private int quantity;

    public Item(String title, double price, int quantity) {
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    protected Item(Parcel in) {
        title = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

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
}
