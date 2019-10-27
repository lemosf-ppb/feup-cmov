package models;

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
    private String id, price;
    private Voucher voucher;
    private ArrayList<TransactionItem> transactionItems;
    private boolean usedDiscounts;


    public Transaction(String id, Voucher voucher, String price, boolean usedDiscounts, ArrayList<TransactionItem> transactionItems) {
        this.id = id;
        this.voucher = voucher;
        this.price = price;
        this.usedDiscounts = usedDiscounts;
        this.transactionItems = transactionItems;
    }

    protected Transaction(Parcel in) {
        id = in.readString();
        price = in.readString();
        voucher = in.readParcelable(Voucher.class.getClassLoader());
        transactionItems = in.createTypedArrayList(TransactionItem.CREATOR);
        usedDiscounts = in.readByte() != 0;
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public boolean hasUsedDiscounts() {
        return usedDiscounts;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public ArrayList<TransactionItem> getTransactionItems() {
        return transactionItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(price);
        dest.writeParcelable(voucher, flags);
        dest.writeTypedList(transactionItems);
        dest.writeByte((byte) (usedDiscounts ? 1 : 0));
    }
}
