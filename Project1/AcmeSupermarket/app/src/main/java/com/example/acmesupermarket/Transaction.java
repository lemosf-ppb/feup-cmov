package com.example.acmesupermarket;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Transaction implements Parcelable {

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
    private String id;
    private ArrayList<Voucher> vouchersUser;
    private String price;

    public Transaction(String id, ArrayList<Voucher> vouchersUser, String price) {
        this.id = id;
        this.vouchersUser = vouchersUser;
        this.price = price;
    }

    protected Transaction(Parcel in) {
        id = in.readString();
        vouchersUser = in.createTypedArrayList(Voucher.CREATOR);
        price = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeTypedList(vouchersUser);
        dest.writeString(price);
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }
}
