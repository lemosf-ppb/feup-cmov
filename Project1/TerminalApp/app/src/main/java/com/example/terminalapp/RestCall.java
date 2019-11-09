package com.example.terminalapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestCall implements Runnable {

    private String requestURL;
    private String requestType;
    private HttpURLConnection urlConnection;
    private String payload;
    private TerminalViewModel terminalViewModel;

    RestCall(TerminalViewModel terminalViewModel, String transactionPayload) {
        this.requestURL = Constants.ACME_REPOSITORY_URL + Constants.TRANSACTIONS;
        this.requestType = Constants.POST;
        this.payload = transactionPayload;
        this.terminalViewModel = terminalViewModel;
    }

    @Override
    public void run() {
        try {
            setupURLConnection();
            sendPayload();
            getResponse();
        } catch (Exception e) {
            Log.e(Constants.TAG, e.getMessage());
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }

    private void setupURLConnection() throws IOException {
        URL url = new URL(requestURL);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setRequestMethod(requestType);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setUseCaches(false);
    }

    private void getResponse() throws IOException, JSONException {
        int responseCode = urlConnection.getResponseCode();
        String response;
        if (responseCode == 200) {
            response = readStream(urlConnection.getInputStream());
        } else {
            response = readStream(urlConnection.getErrorStream());
        }
        handleResponse(new Response(responseCode, response));
    }

    private void sendPayload() throws IOException {
        DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
        outputStream.writeBytes(payload);
        outputStream.flush();
        outputStream.close();
    }

    private void handleResponse(final Response response) throws JSONException {
        Log.i("transactions", response.getCode() + response.getMessage());
        terminalViewModel.response.postValue(response);
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            Log.e(Constants.TAG, e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(Constants.TAG, e.getMessage());
                }
            }
        }
        return response.toString();
    }

    public class Response {
        private int code;
        private String message;

        Response(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getErrorMessage() {
            return code + " - " + message;
        }

        public String getSuccessMessage() {
            try {
                return new JSONObject(message).getString("message");
            } catch (JSONException e) {
                Log.e(Constants.TAG, e.getMessage());
            }
            return "";
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
