package services.repository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import models.Client;
import services.crypto.Cryptography;

public class AcmeRepository {

    public String getUserIdAndSignature(UUID userId) throws JSONException {
        JSONObject payload = new JSONObject();
        payload.put("userId", userId.toString());
        payload.put("signature", Cryptography.buildMessage(userId.toString().getBytes())); //TODO: Check Signature
        return payload.toString();
    }

    public class SignUp extends AbstractRestCall {
        private Client client;
        private String password;
        private String publicKey;

        public SignUp(Client client, String password, String publicKey) {
            this.client = client;
            this.password = password;
            this.publicKey = publicKey;
        }

        @Override
        public String createPayload() throws JSONException {
            JSONObject payload = client.getAsJSON();
            payload.put("password", password);
            payload.put("publicKey", publicKey);
            return payload.toString();
        }

        @Override
        public void handleResponse(String response) {

        }
    }

    public class LogIn extends AbstractRestCall {
        private Client client;
        private String password;

        public LogIn(Client client, String password) {
            this.client = client;
            this.password = password;
        }

        @Override
        public String createPayload() throws JSONException {
            JSONObject payload = new JSONObject();
            payload.put("username", client.getUsername());
            payload.put("password", password);
            return payload.toString();
        }

        @Override
        public void handleResponse(String response) {

        }
    }

    public class getTransactions extends AbstractRestCall {
        private UUID userId;

        public getTransactions(UUID userId) {
            this.userId = userId;
        }

        @Override
        public String createPayload() throws JSONException {
            return getUserIdAndSignature(userId);
        }

        @Override
        public void handleResponse(String response) {

        }
    }

    public class getUnusedVouchers extends AbstractRestCall {
        private UUID userId;

        public getUnusedVouchers(UUID userId) {
            this.userId = userId;
        }

        @Override
        public String createPayload() throws JSONException {
            return getUserIdAndSignature(userId);
        }

        @Override
        public void handleResponse(String response) {

        }
    }

    public class getUserInfo extends AbstractRestCall {
        private UUID userId;

        public getUserInfo(UUID userId) {
            this.userId = userId;
        }

        @Override
        public String createPayload() throws JSONException {
            return getUserIdAndSignature(userId);
        }

        @Override
        public void handleResponse(String response) {

        }
    }
}
