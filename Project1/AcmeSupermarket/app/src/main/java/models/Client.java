package models;

import org.json.JSONException;
import org.json.JSONObject;

public class Client {
    private String name, username;
    private CreditCard creditCard;

    public Client(String name, String username, CreditCard creditCard) {
        this.name = name;
        this.username = username;
        this.creditCard = creditCard;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public JSONObject getAsJSON() throws JSONException {
        JSONObject clientObject = new JSONObject();
        clientObject.put("username", username);
        clientObject.put("name", name);
        clientObject.put("creditCard", creditCard.getAsJSON());

        return clientObject;
    }
}
