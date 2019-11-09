package models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Voucher implements Serializable {
    static final long serialVersionUID = 1L;

    private UUID id;
    private int discount;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voucher voucher = (Voucher) o;
        return Objects.equals(id, voucher.id);
    }
}
