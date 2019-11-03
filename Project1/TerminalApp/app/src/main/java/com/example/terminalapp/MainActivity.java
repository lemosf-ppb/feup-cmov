package com.example.terminalapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    TerminalViewModel mViewModel;
    private DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewModel = ViewModelProviders.of(this).get(TerminalViewModel.class);

        Button button = findViewById(R.id.scan_transaction);
        button.setOnClickListener(v -> {
            scanQRCode();
        });

        mViewModel.response.observe(this, response -> setColor(response));
    }

    private static AlertDialog showDialog(final Activity act) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle("No Scanner Found");
        downloadDialog.setMessage("Download a scanner code activity?");
        downloadDialog.setPositiveButton("Yes", (d, i) -> {
            Uri uri = Uri.parse(Constants.QR_READER_PLAYSTORE);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            act.startActivity(intent);
        });
        downloadDialog.setNegativeButton("No", null);
        return downloadDialog.show();
    }

    private void scanQRCode() {
        try {
            Intent intent = new Intent(Constants.ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException e) {
            showDialog(this).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                if (contents != null)
                    decodeAndShow(contents.getBytes(StandardCharsets.ISO_8859_1));
            }
        }
    }

    private void decodeAndShow(byte[] encTag) {
        String transactionPayload = new String(encTag);

        try {
            JSONObject transactionJSON = new JSONObject(transactionPayload);
            double totalPrice = transactionJSON.getDouble("totalPrice");
            String total_price_string = getResources().getString(R.string.total_price, String.format("%s", df.format(totalPrice)));
            TextView total_price = findViewById(R.id.total_price);
            total_price.setText(total_price_string);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("app", transactionPayload);
        mViewModel.postTransaction(transactionPayload);
    }

    public void setColor(RestCall.Response response){
        RelativeLayout screen = findViewById(R.id.screen);

        if(response.getCode() == 200){
            screen.setBackgroundColor(getResources().getColor(R.color.green));
            Toast.makeText(getApplicationContext(), "Transaction successfully complete!", Toast.LENGTH_SHORT).show();
        }
        else{
            screen.setBackgroundColor(getResources().getColor(R.color.red));
            Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Handler handler = new Handler();
        handler.postDelayed(() -> screen.setBackgroundColor(getResources().getColor(R.color.white)), 5000); // 5000ms delay
    }

}
