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
    private int discount;

    public Voucher(String name, int discount) {
        this.name = name;
        this.discount = discount;
    }

    protected Voucher(Parcel in) {
        name = in.readString();
        discount = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(discount);
    }

    public String getName() {
        return name;
    }

    public int getDiscount() {
        return discount;
    }

    public boolean equals(Object object){
        Voucher other = (Voucher) object;
        return this.name == other.name && this.discount == other.discount;
    }
}
