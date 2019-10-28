package services.repository;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class AbstractRestCall implements Runnable {

    private String requestURL;
    private String requestType;
    HttpURLConnection urlConnection;

    @Override
    public void run() {
        try {
            setupURLConnection();
            sendPayload(createPayload());
            getResponse();
        } catch (IOException e) {
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
    }

    private void getResponse() throws IOException {
        int responseCode = urlConnection.getResponseCode();
        if (responseCode == 200) {
            String response = readStream(urlConnection.getInputStream());
            handleResponse(response);
        } else {
            Log.e("REQUEST ERROR", String.valueOf(responseCode));
        }
    }

    public abstract String createPayload();

    private void sendPayload(String payload) throws IOException {
        DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
        outputStream.writeBytes(payload);
        outputStream.flush();
        outputStream.close();
    }

    public abstract void handleResponse(final String response);

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
