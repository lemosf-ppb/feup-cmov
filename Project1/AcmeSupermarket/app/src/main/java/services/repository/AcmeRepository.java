package services.repository;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.PrivateKey;
import java.util.ArrayList;

import models.Client;
import models.Transaction;
import models.Voucher;
import services.crypto.CryptoBuilder;
import ui.login.LoginViewModel;
import ui.registration.RegistrationViewModel;
import ui.shop.ShopViewModel;
import ui.transactions.TransactionsViewModel;

public class AcmeRepository {

    private static String getUserIdAndSignature(String userId, PrivateKey userPrivateKey) throws JSONException {
        JSONObject payload = new JSONObject();
        payload.put("userId", userId);
        payload.put("signature", CryptoBuilder.signMessageHex(userPrivateKey, userId.getBytes()));
        return payload.toString();
    }

    public static class SignUp extends AbstractRestCall {
        private RegistrationViewModel registrationViewModel;

        public SignUp(RegistrationViewModel registrationViewModel) {
            this.requestURL = Constants.ACME_REPOSITORY_URL + Constants.SIGNUP;
            this.requestType = Constants.POST;
            this.registrationViewModel = registrationViewModel;
        }

        @Override
        public String createPayload() throws JSONException {
            return registrationViewModel.getClient().getAsJSON().toString();
        }

        @Override
        public void handleResponse(final Response response) throws JSONException {
            Log.e("Signup", response.getCode() + response.getMessage());
            registrationViewModel.getSignUpResponse().postValue(response);

            if (response.getCode() == 200) {
                JSONObject responseObject = new JSONObject(response.getMessage());
                Client client = registrationViewModel.getClient();
                client.setAcmePublicKey(responseObject.getString("supermarketPublicKey"));
                client.setUserId(responseObject.getString("userId"));
                registrationViewModel.getRegistrationState().postValue(RegistrationViewModel.RegistrationState.REGISTRATION_COMPLETED);
            } else {
                registrationViewModel.getRegistrationState().postValue(RegistrationViewModel.RegistrationState.REGISTRATION_FAILED);
            }
        }
    }

    public static class getTransactions extends AbstractRestCall {
        private Client client;
        private TransactionsViewModel transactionsViewModel;

        public getTransactions(Client client, TransactionsViewModel transactionsViewModel) {
            this.requestURL = Constants.ACME_REPOSITORY_URL + Constants.TRANSACTIONS + "/" + Constants.USER;
            this.requestType = Constants.POST;
            this.client = client;
            this.transactionsViewModel = transactionsViewModel;
        }

        @Override
        public String createPayload() throws JSONException {
            return getUserIdAndSignature(client.getUserId(), client.getClientPrivateKey());
        }

        @Override
        public void handleResponse(Response response) throws JSONException {
            Log.e("transactions", response.getCode() + response.getMessage());
            if (response.getCode() == 200) {
                JSONObject responseObject = new JSONObject(response.getMessage());
                JSONArray transactionsArray = responseObject.getJSONArray("transactions");
                ArrayList<Transaction> transactions = new ArrayList<>();
                for (int i = 0; i < transactionsArray.length(); i++) {
                    transactions.add(new Transaction(transactionsArray.getJSONObject(i)));
                }
                transactionsViewModel.getTransactions().postValue(transactions);
            }
        }
    }

    public static class getUnusedVouchers extends AbstractRestCall {
        private Client client;
        private ShopViewModel shopViewModel;

        public getUnusedVouchers(Client client, ShopViewModel shopViewModel) {
            this.requestURL = Constants.ACME_REPOSITORY_URL + Constants.VOUCHERS_UNUSED + "/" + Constants.USER;
            this.requestType = Constants.POST;
            this.client = client;
            this.shopViewModel = shopViewModel;
        }

        @Override
        public String createPayload() throws JSONException {
            return getUserIdAndSignature(client.getUserId(), client.getClientPrivateKey());
        }

        @Override
        public void handleResponse(Response response) throws JSONException {
            Log.e("vouchers", response.getCode() + response.getMessage());
            if (response.getCode() == 200) {
                JSONArray responseObject = new JSONArray(response.getMessage());
                ArrayList<Voucher> unusedVouchers = new ArrayList<>();
                for (int i = 0; i < responseObject.length(); i++) {
                    Voucher voucher = new Voucher(responseObject.getJSONObject(i));
                    unusedVouchers.add(voucher);
                }
                shopViewModel.getVouchers().postValue(unusedVouchers);
            }
        }
    }

    public static class getUserInfo extends AbstractRestCall {
        private LoginViewModel loginViewModel;

        public getUserInfo(LoginViewModel loginViewModel) {
            this.requestURL = Constants.ACME_REPOSITORY_URL + Constants.USERS + "/" + Constants.USER;
            this.requestType = Constants.POST;
            this.loginViewModel = loginViewModel;
        }

        @Override
        public String createPayload() throws JSONException {
            Client client = loginViewModel.getClient();
            return getUserIdAndSignature(client.getUserId(), client.getClientPrivateKey());
        }

        @Override
        public void handleResponse(Response response) throws JSONException {
            Log.e("userInfo", response.getCode() + response.getMessage());
            if (response.getCode() == 200) {
                JSONObject responseObject = new JSONObject(response.getMessage());
                loginViewModel.getClient().updateDatabaseData(responseObject);
            }
        }
    }

    public class LogIn extends AbstractRestCall {
        private Client client;
        private String password;

        public LogIn(Client client, String password) {
            this.requestURL = Constants.ACME_REPOSITORY_URL + Constants.LOGIN;
            this.requestType = Constants.POST;
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
        public void handleResponse(Response response) throws JSONException {
            Log.e("login", response.getCode() + response.getMessage());

        }
    }
}
