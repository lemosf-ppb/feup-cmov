package models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class Voucher {
    private UUID id;
    private int discount;

    public Voucher(UUID id, int discount) {
        this.id = id;
        this.discount = discount;
    }

    public Voucher(JSONObject voucherObject) throws JSONException {
        this.id = UUID.fromString(voucherObject.getString("id"));
        this.discount = voucherObject.getInt("discount");
    }

    public UUID getId() {
        return id;
    }

    public int getDiscount() {
        return discount;
    }

    public boolean equals(Object object) {
        Voucher other = (Voucher) object;
        return this.id == other.id;
    }
}
