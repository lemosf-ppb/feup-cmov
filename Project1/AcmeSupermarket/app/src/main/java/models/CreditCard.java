package models;

public class CreditCard {
    private String holder, number, expiry, cvv;

    public CreditCard(String holder, String number, String expiry, String cvv) {
        this.holder = holder;
        this.number = number;
        this.expiry = expiry;
        this.cvv = cvv;
    }

    public String getHolder() {
        return holder;
    }

    public String getNumber() {
        return number;
    }

    public String getExpiry() {
        return expiry;
    }

    public String getCvv() {
        return cvv;
    }
}
