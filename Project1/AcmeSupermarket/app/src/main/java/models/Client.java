package models;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import services.crypto.Cryptography;

import static services.crypto.Cryptography.getPublicKeyFromString;

public class Client {
    //TODO: Save password and keys here?
    private String userId;
    private String name, username, password;
    private CreditCard creditCard;
    private PrivateKey clientPrivateKey;
    private PublicKey clientPublicKey;
    private PublicKey acmePublicKey;

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

    public PublicKey getClientPublicKey() {
        return clientPublicKey;
    }

    public PublicKey getAcmePublicKey() {
        return acmePublicKey;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAcmePublicKey(String acmePublicKey) {
        try {
            this.acmePublicKey = getPublicKeyFromString(acmePublicKey);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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
        clientObject.put("publicKey", Cryptography.convertToPublicKeyPEMFormat(clientPublicKey));
        return clientObject;
    }

    public void setClientKeys() {
        this.clientPublicKey = Cryptography.getPublicKey();
        this.clientPrivateKey = Cryptography.getPrivateKey();
    }
}

