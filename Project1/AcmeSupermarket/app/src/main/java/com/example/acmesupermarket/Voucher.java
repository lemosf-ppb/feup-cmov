package com.example.acmesupermarket;

import android.os.Parcel;
import android.os.Parcelable;

public class Voucher implements Parcelable {

    public static final Creator<Voucher> CREATOR = new Creator<Voucher>() {
        @Override
        public Voucher createFromParcel(Parcel in) {
            return new Voucher(in);
        }

        @Override
        public Voucher[] newArray(int size) {
            return new Voucher[size];
        }
    };
    private String name;
    private String discount;

    public Voucher(String name, String discount) {
        this.name = name;
        this.discount = discount;
    }

    protected Voucher(Parcel in) {
        name = in.readString();
        discount = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(discount);
    }

    public String getName() {
        return name;
    }

    public String getDiscount() {
        return discount;
    }
}
