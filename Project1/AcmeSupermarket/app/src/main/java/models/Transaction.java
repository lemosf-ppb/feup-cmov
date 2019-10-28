package models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public Transaction(String uuidUser, ArrayList<TransactionItem> transactionItems, Voucher currentVoucher, boolean usedDiscounts) {
        this.id = uuidUser;
        this.voucher = currentVoucher;
        this.price = "4";
        this.usedDiscounts = usedDiscounts;
        this.transactionItems = transactionItems;
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

    public JSONObject getAsJSON() throws JSONException {
        JSONObject transactionObject = new JSONObject();
        transactionObject.put("userId", "96b471c1-0372-41cc-a121-9a8e3dc74662");
        JSONArray productsList = new JSONArray();
        for(int i=0; i< transactionItems.size(); i++) {
            TransactionItem transactionItem = transactionItems.get(i);
            JSONObject transactionItemObject = new JSONObject();
            transactionItemObject.put("uuid", "b932a053-5163-45da-a174-8ee6fc44c61f");
            transactionItemObject.put("price", transactionItem.getPrice());
            transactionItemObject.put("quantity", transactionItem.getQuantity());
            productsList.put(transactionItemObject);
        }
        transactionObject.put("productsList", productsList);
        transactionObject.put("useDiscounts", usedDiscounts);

        Log.e("transaction", transactionObject.toString());
        return transactionObject;
    }
}
