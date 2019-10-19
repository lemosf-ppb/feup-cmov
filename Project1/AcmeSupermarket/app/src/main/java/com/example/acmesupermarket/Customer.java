package com.example.acmesupermarket;

import android.os.Parcel;
import android.os.Parcelable;

public class Customer implements Parcelable {

    private String name, username, card_holder, number, expiry, cvv;

    public Customer(String name, String username, String card_holder, String number, String expiry, String cvv){
        this.name = name;
        this.username = username;
        this.card_holder = card_holder;
        this.number = number;
        this.expiry = expiry;
        this.cvv = cvv;
    }

    protected Customer(Parcel in) {
        name = in.readString();
        username = in.readString();
        card_holder = in.readString();
        number = in.readString();
        expiry = in.readString();
        cvv = in.readString();
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(card_holder);
        dest.writeString(number);
        dest.writeString(expiry);
        dest.writeString(cvv);
    }
}
