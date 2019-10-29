package models;

import org.json.JSONException;
import org.json.JSONObject;

public class Client {
    //TODO: Save password and keys here?
    private String userId;
    private String name, username, password;
    private CreditCard creditCard;
    private String clientPrivateKey; //TODO: Change to PRivateKey and PublicKey
    private String clientPublicKey = "a";
    private String acmePublicKey;

    public Client(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

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

    public String getUserId() {
        return userId;
    }

    public String getClientPublicKey() {
        return clientPublicKey;
    }

    public String getAcmePublicKey() {
        return acmePublicKey;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAcmePublicKey(String acmePublicKey) {
        this.acmePublicKey = acmePublicKey;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public JSONObject getAsJSON() throws JSONException {
        JSONObject clientObject = new JSONObject();
        clientObject.put("username", username);
        clientObject.put("name", name);
        clientObject.put("creditCard", creditCard.getAsJSON());
        clientObject.put("password", password);
        clientObject.put("publicKey", clientPublicKey); //TODO: Update
        return clientObject;
    }
}

