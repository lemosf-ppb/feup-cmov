package services.repository;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import models.Client;
import services.crypto.Cryptography;
import ui.registration.RegistrationViewModel;

public class AcmeRepository {

    private String getUserIdAndSignature(UUID userId) throws JSONException {
        JSONObject payload = new JSONObject();
        payload.put("userId", userId.toString());
        payload.put("signature", Cryptography.buildMessage(userId.toString().getBytes())); //TODO: Check Signature
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

    public class getTransactions extends AbstractRestCall {
        private UUID userId;

        public getTransactions(UUID userId) {
            this.requestURL = Constants.ACME_REPOSITORY_URL + Constants.TRANSACTIONS;
            this.requestType = Constants.GET;
            this.userId = userId;
        }

        @Override
        public String createPayload() throws JSONException {
            return getUserIdAndSignature(userId);
        }

        @Override
        public void handleResponse(Response response) throws JSONException {
            Log.e("transactions", response.code + response.message);

        }
    }

    public class getUnusedVouchers extends AbstractRestCall {
        private UUID userId;

        public getUnusedVouchers(UUID userId) {
            this.requestURL = Constants.ACME_REPOSITORY_URL + Constants.VOUCHERS_UNUSED;
            this.requestType = Constants.GET;
            this.userId = userId;
        }

        @Override
        public String createPayload() throws JSONException {
            return getUserIdAndSignature(userId);
        }

        @Override
        public void handleResponse(Response response) throws JSONException {
            Log.e("vouchers", response.code + response.message);

        }
    }

    public class getUserInfo extends AbstractRestCall {
        private UUID userId;

        public getUserInfo(UUID userId) {
            this.requestURL = Constants.ACME_REPOSITORY_URL + Constants.USERS;
            this.requestType = Constants.GET;
            this.userId = userId;
        }

        @Override
        public String createPayload() throws JSONException {
            return getUserIdAndSignature(userId);
        }

        @Override
        public void handleResponse(Response response) throws JSONException {
            Log.e("userInfo", response.code + response.message);

        }
    }
}
