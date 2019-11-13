package models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CreditCard implements Serializable {
    static final long serialVersionUID = 1L;
    private String holder, number, expiry, cvv;

    public CreditCard(String holder, String number, String expiry, String cvv) {
        this.holder = holder;
        this.number = number;
        this.expiry = expiry;
        this.cvv = cvv;
    }

    public CreditCard(JSONObject creditCard) throws JSONException {
        this.holder = creditCard.getString("holder");
        this.number = creditCard.getString("number");
        this.expiry = creditCard.getString("validity");
        this.cvv = creditCard.getString("cvv");
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

    public JSONObject getAsJSON() throws JSONException {
        JSONObject creditCardObject = new JSONObject();
        creditCardObject.put("number", number);
        creditCardObject.put("validity", expiry);
        creditCardObject.put("cvv", cvv);
        creditCardObject.put("holder", holder);
        return creditCardObject;
    }
}
