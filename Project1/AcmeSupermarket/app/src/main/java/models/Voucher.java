package models;

import java.util.UUID;

public class Voucher {
    private UUID id;
    private int discount;

    public Voucher(UUID id, int discount) {
        this.id = id;
        this.discount = discount;
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
