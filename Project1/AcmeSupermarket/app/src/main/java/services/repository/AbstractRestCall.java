package services.repository;

import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class AbstractRestCall implements Runnable {

    protected String requestURL;
    protected String requestType;
    protected View view; // Only necessary to show error/success messages
    private HttpURLConnection urlConnection;

    @Override
    public void run() {
        try {
            setupURLConnection();
            sendPayload(createPayload());
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

    public abstract String createPayload() throws JSONException;

    private void sendPayload(String payload) throws IOException {
        DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
        outputStream.writeBytes(payload);
        outputStream.flush();
        outputStream.close();
    }

    public abstract void handleResponse(final Response response) throws JSONException;

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
