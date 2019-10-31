package ui.shop.cart;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.acmesupermarket.R;
import com.google.zxing.WriterException;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import models.Client;
import models.Transaction;
import models.TransactionItem;
import models.Voucher;
import services.crypto.CryptoBuilder;
import ui.login.LoginViewModel;
import ui.shop.ShopViewModel;
import utils.Utils;

public class CheckoutFragment extends Fragment {
    private LoginViewModel loginViewModel;
    private ShopViewModel shopViewModel;

    private ImageView qrCodeImageView;
    private String qr_content = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProvider provider = ViewModelProviders.of(requireActivity());
        loginViewModel = provider.get(LoginViewModel.class);
        shopViewModel = provider.get(ShopViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewModelProvider provider = ViewModelProviders.of(requireActivity());
        loginViewModel = provider.get(LoginViewModel.class);
        shopViewModel = provider.get(ShopViewModel.class);

        drawQRCode(view);
    }

    private void drawQRCode(View view) {
        TextView titleTv;
        qrCodeImageView = view.findViewById(R.id.img_qr_code_image);
        titleTv = view.findViewById(R.id.title);

        ArrayList<TransactionItem> transactionItems = shopViewModel.transactionItems.getValue();
        Voucher currentVoucher = shopViewModel.currentVoucher.getValue();
        boolean usedDiscounts = shopViewModel.applyDiscount.getValue();
        Client client = loginViewModel.getClient();

        Transaction transaction = new Transaction(client.getUserIdAsUUID(), transactionItems, currentVoucher, usedDiscounts);
        byte[] transactionBytes = new byte[0];
        try {
            transactionBytes = transaction.getAsJSON().toString().getBytes();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        byte[] messageSigned = CryptoBuilder.buildMessageWithSignature(client.getClientPrivateKey(), transactionBytes);

        try {
            qr_content = new String(messageSigned, Utils.ISO_SET);
            boolean verified = CryptoBuilder.validateMessageWithSignature(client.getClientPublicKey(), messageSigned);
            String text = "Transaction: \"" + new String(transactionBytes) + "\"\nVerified: " + verified + "\nTotal bytes: " + messageSigned.length;
            titleTv.setText(text);
        } catch (UnsupportedEncodingException e) {
            Log.d("Debug", e.getMessage());
        }

        // do the creation in a new thread to avoid ANR Exception
        Thread t = new Thread(() -> {
            final Bitmap bitmap;
            try {
                bitmap = Utils.encodeAsBitmap(qr_content);
                // runOnUiThread method used to do UI task in main thread.
                requireActivity().runOnUiThread(() -> qrCodeImageView.setImageBitmap(bitmap));
            } catch (WriterException e) {
                Log.d("Debug", e.getMessage());
            }
        });
        t.start();
    }

}
