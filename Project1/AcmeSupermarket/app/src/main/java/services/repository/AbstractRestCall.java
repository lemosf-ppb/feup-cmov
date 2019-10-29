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

    public class Response {
        int code;
        String message;

        public Response(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getErrorMessage() {
            return "Error:" + code + " - " + message;
        }

        public String getSuccessMessage() {
            try {
                return new JSONObject(message).getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    protected String requestURL;
    protected String requestType;
    HttpURLConnection urlConnection;
    protected View view; // Only necessary to show error/success messages

    @Override
    public void run() {
        try {
            setupURLConnection();
            sendPayload(createPayload());
            getResponse();
        } catch (Exception e) {
            e.printStackTrace();
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
        Log.e("DEBUG", url.getPath());
    }

    private void getResponse() throws IOException, JSONException {
        int responseCode = urlConnection.getResponseCode();
        Log.e("DEBUG", String.valueOf(responseCode));
        String response;
        if(responseCode == 200) {
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
            return e.getMessage();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    return e.getMessage();
                }
            }
        }
        return response.toString();
    }
}
