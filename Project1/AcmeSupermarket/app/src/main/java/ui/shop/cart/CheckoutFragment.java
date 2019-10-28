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
import androidx.lifecycle.ViewModelProviders;

import com.example.acmesupermarket.R;
import com.google.zxing.WriterException;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import models.Transaction;
import models.TransactionItem;
import models.Voucher;
import services.crypto.Cryptography;
import ui.shop.ShopViewModel;
import utils.Utils;

public class CheckoutFragment extends Fragment {
    private ShopViewModel mViewModel;

    private ImageView qrCodeImageview;
    private String qr_content = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(ShopViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(requireActivity()).get(ShopViewModel.class);

        //TODO: Replace this to Registry/Login
        Cryptography.generateAndStoreKeys(requireActivity().getApplicationContext());

        drawQRCode(view);
    }

    private void drawQRCode(View view) {
        TextView titleTv;
        qrCodeImageview = view.findViewById(R.id.img_qr_code_image);
        titleTv = view.findViewById(R.id.title);

        ArrayList<TransactionItem> transactionItems = mViewModel.transactionItems.getValue();
        Voucher currentVoucher = mViewModel.currentVoucher.getValue();
        boolean usedDiscounts = mViewModel.applyDiscount.getValue();

        Transaction transaction = new Transaction("uuidUser", transactionItems, currentVoucher, usedDiscounts);
        byte[] transactionBytes = new byte[0];
        try {
            transactionBytes = transaction.getAsJSON().toString().getBytes();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        byte[] messageSigned = Cryptography.buildMessage(transactionBytes);

        try {
            qr_content = new String(messageSigned, Utils.ISO_SET);
            boolean verified = Cryptography.validate(messageSigned);
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
                requireActivity().runOnUiThread(() -> qrCodeImageview.setImageBitmap(bitmap));
            } catch (WriterException e) {
                Log.d("Debug", e.getMessage());
            }
        });
        t.start();
    }

}
