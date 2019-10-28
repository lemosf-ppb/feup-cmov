package models;

public class Client {
    private String name, username, password, publicKey;
    private CreditCard creditCard;

    public Client(String name, String username, CreditCard creditCard) {
        this.name = name;
        this.username = username;
        this.password = "";
        this.publicKey = "";
        this.creditCard = creditCard;
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

    public String getPublicKey() {
        return publicKey;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }
}
