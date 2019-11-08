package models;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.UUID;

import services.crypto.CryptoKeysManagement;
import utils.Utils;

public class Client implements Serializable {
    static final long serialVersionUID = 1L;

    private UUID userId;
    private String name, username, password;
    private CreditCard creditCard;
    private PrivateKey clientPrivateKey;
    private PublicKey clientPublicKey;
    private PublicKey acmePublicKey;
    private Double totalValueSpent, discountValueAvailable;
    private Date createdAt;

    public Client(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.totalValueSpent = 0.0;
        this.discountValueAvailable = 0.0;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public String getUserId() {
        return userId.toString();
    }

    public void setUserId(String userId) {
        this.userId = UUID.fromString(userId);
    }

    public UUID getUserIdAsUUID() {
        return userId;
    }

    public PublicKey getAcmePublicKey() {
        return acmePublicKey;
    }

    public void setAcmePublicKey(String acmePublicKey) {
        this.acmePublicKey = CryptoKeysManagement.getPublicKeyFromString(acmePublicKey);
    }

    public PublicKey getClientPublicKey() {
        return clientPublicKey;
    }

    public PrivateKey getClientPrivateKey() {
        return clientPrivateKey;
    }

    public Double getTotalValueSpent() {
        return totalValueSpent;
    }

    public Double getDiscountValueAvailable() {
        return discountValueAvailable;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public JSONObject getAsJSON() throws JSONException {
        JSONObject clientObject = new JSONObject();
        clientObject.put("username", username);
        clientObject.put("name", name);
        clientObject.put("creditCard", creditCard.getAsJSON());
        clientObject.put("password", password);
        clientObject.put("publicKey", CryptoKeysManagement.convertToPublicKeyPEMFormat(clientPublicKey));
        return clientObject;
    }

    public void setClientKeys() {
        this.clientPublicKey = CryptoKeysManagement.getPublicKey();
        this.clientPrivateKey = CryptoKeysManagement.getPrivateKey();
    }

    public void updateDatabaseData(JSONObject clientObject) throws JSONException {
        totalValueSpent = clientObject.getDouble("totalValueSpent");
        discountValueAvailable = clientObject.getDouble("discountValueAvailable");
        createdAt = Utils.parseDate(clientObject.getString("createdAt"));
    }
}