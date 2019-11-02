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
import utils.Utils;

public class AcmeRepository {

    private static String getUserIdAndSignature(String userId, PrivateKey userPrivateKey) throws JSONException {
        JSONObject payload = new JSONObject();
        payload.put("userId", userId);
        payload.put("signature", CryptoBuilder.signMessageHex(userPrivateKey, userId.getBytes()));
        return payload.toString();
    }

    private static boolean validateSignature(Client client, byte[] message, String signature) {
        return CryptoBuilder.validateMessage(client.getAcmePublicKey(), message, Utils.hexStringToByteArray(signature));
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

                String signature = (String) responseObject.remove("signature");
                if (!validateSignature(client, responseObject.toString().getBytes(), signature)) {
                    Log.e(Constants.TAG, "Signature Invalid!");
                    return;
                }

                ArrayList<Transaction> transactions = new ArrayList<>();
                for (int i = 0; i < transactionsArray.length(); i++) {
                    transactions.add(new Transaction(transactionsArray.getJSONObject(i)));
                }
                transactionsViewModel.transactions.postValue(transactions);
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
                JSONObject responseObject = new JSONObject(response.getMessage());
                JSONArray vouchers = responseObject.getJSONArray("vouchers");

                String signature = (String) responseObject.remove("signature");
                if (!validateSignature(client, responseObject.toString().getBytes(), signature)) {
                    Log.e(Constants.TAG, "Signature Invalid!");
                    return;
                }

                ArrayList<Voucher> unusedVouchers = new ArrayList<>();
                for (int i = 0; i < vouchers.length(); i++) {
                    Voucher voucher = new Voucher(vouchers.getJSONObject(i));
                    unusedVouchers.add(voucher);
                }
                shopViewModel.vouchers.postValue(unusedVouchers);
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
                Client client = loginViewModel.getClient();
                JSONObject user = responseObject.getJSONObject("user");
                String signature = (String) responseObject.remove("signature");
                if (!validateSignature(client, user.toString().replace("\\/", "/").getBytes(), signature)) {
                    Log.e(Constants.TAG, "Signature Invalid!");
                    return;
                }

                client.updateDatabaseData(user);
                loginViewModel.client.postValue(loginViewModel.getClient());
            }
        }
    }

    public static class LogIn extends AbstractRestCall {
        private LoginViewModel loginViewModel;

        public LogIn(LoginViewModel loginViewModel) {
            this.requestURL = Constants.ACME_REPOSITORY_URL + Constants.LOGIN;
            this.requestType = Constants.POST;
            this.loginViewModel = loginViewModel;
        }

        @Override
        public String createPayload() throws JSONException {
            return loginViewModel.getClient().getAsJSON().toString();
        }

        @Override
        public void handleResponse(final Response response) throws JSONException {
            Log.e("Login", response.getCode() + response.getMessage());
            if (response.getCode() == 200) {
                JSONObject responseObject = new JSONObject(response.getMessage());
                Client client = loginViewModel.getClient();
                client.setAcmePublicKey(responseObject.getString("supermarketPublicKey"));
                client.setUserId(responseObject.getString("userId"));
            }
        }
    }
}
