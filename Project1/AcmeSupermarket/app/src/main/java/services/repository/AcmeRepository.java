package services.repository;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import models.Client;
import models.Transaction;
import models.Voucher;
import services.crypto.Cryptography;
import ui.login.LoginViewModel;
import ui.registration.RegistrationViewModel;
import ui.shop.ShopViewModel;
import ui.transactions.TransactionsViewModel;

public class AcmeRepository {

    private static String getUserIdAndSignature(String userId) throws JSONException {
        JSONObject payload = new JSONObject();
        payload.put("userId", userId);
        payload.put("signature", Cryptography.signMessageHex(userId.getBytes()));
        return payload.toString();
    }

    public static class SignUp extends AbstractRestCall {
        private RegistrationViewModel mViewModel;

        public SignUp(RegistrationViewModel mViewModel) {
            this.requestURL = Constants.ACME_REPOSITORY_URL + Constants.SIGNUP;
            this.requestType = Constants.POST;
            this.mViewModel = mViewModel;
        }

        @Override
        public String createPayload() throws JSONException {
            return mViewModel.getClient().getAsJSON().toString();
        }

        @Override
        public void handleResponse(final Response response) throws JSONException {
            Log.e("Signup", response.code + response.message);
            mViewModel.getSignUpResponse().postValue(response);
            if (response.code == 200) {
                JSONObject responseObject = new JSONObject(response.message);
                Client client = mViewModel.getClient();
                client.setAcmePublicKey(responseObject.getString("supermarketPublicKey"));
                client.setUserId(responseObject.getString("userId"));
                mViewModel.getRegistrationState().postValue(RegistrationViewModel.RegistrationState.REGISTRATION_COMPLETED);
            } else {
                mViewModel.getRegistrationState().postValue(RegistrationViewModel.RegistrationState.REGISTRATION_FAILED);
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
            Log.e("login", response.code + response.message);

        }
    }

    public static class getTransactions extends AbstractRestCall {
        private String userId;
        private TransactionsViewModel transactionsViewModel;

        public getTransactions(String userId, TransactionsViewModel transactionsViewModel) {
            this.requestURL = Constants.ACME_REPOSITORY_URL + Constants.TRANSACTIONS + "/" + Constants.USER;
            this.requestType = Constants.POST;
            this.userId = userId;
            this.transactionsViewModel = transactionsViewModel;
        }

        @Override
        public String createPayload() throws JSONException {
            return getUserIdAndSignature(userId);
        }

        @Override
        public void handleResponse(Response response) throws JSONException {
            Log.e("transactions", response.code + response.message);
            if (response.code == 200) {
                JSONObject responseObject = new JSONObject(response.message);
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
        private String userId;
        private ShopViewModel shopViewModel;

        public getUnusedVouchers(String userId, ShopViewModel shopViewModel) {
            this.requestURL = Constants.ACME_REPOSITORY_URL + Constants.VOUCHERS_UNUSED + "/" + Constants.USER;
            this.requestType = Constants.POST;
            this.userId = userId;
            this.shopViewModel = shopViewModel;
        }

        @Override
        public String createPayload() throws JSONException {
            return getUserIdAndSignature(userId);
        }

        @Override
        public void handleResponse(Response response) throws JSONException {
            Log.e("vouchers", response.code + response.message);
            if (response.code == 200) {
                JSONArray responseObject = new JSONArray(response.message);
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
        private ShopViewModel shopViewModel;

        public getUserInfo(LoginViewModel loginViewModel, ShopViewModel shopViewModel) {
            this.requestURL = Constants.ACME_REPOSITORY_URL + Constants.USERS + "/" + Constants.USER;
            this.requestType = Constants.POST;
            this.loginViewModel = loginViewModel;
            this.shopViewModel = shopViewModel;
        }

        @Override
        public String createPayload() throws JSONException {
            return getUserIdAndSignature(loginViewModel.getClient().getUserId());
        }

        @Override
        public void handleResponse(Response response) throws JSONException {
            Log.e("userInfo", response.code + response.message);
            if (response.code == 200) {
                JSONObject responseObject = new JSONObject(response.message);
                try {
                    loginViewModel.getClient().updateDatabaseData(responseObject);
                    shopViewModel.discountAvailable.postValue(loginViewModel.getClient().getDiscountValueAvailable());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
