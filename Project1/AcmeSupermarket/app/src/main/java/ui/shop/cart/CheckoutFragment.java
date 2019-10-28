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
import java.nio.ByteBuffer;
import java.util.ArrayList;

import models.Transaction;
import models.TransactionItem;
import models.Voucher;
import services.Crypto.Constants;
import services.Crypto.Cryptography;
import ui.shop.ShopViewModel;
import ui.transactions.TransactionAdapter;
import utils.Utils;

public class CheckoutFragment extends Fragment {
    private ShopViewModel mViewModel;

    ImageView qrCodeImageview;
    String qr_content = null;

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
        byte[] content = new byte[0];
        try {
            content = transaction.getAsJSON().toString().getBytes();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int sign_size = Constants.KEY_SIZE / 8;
        int mess_size = content.length - sign_size;

        try {
            qr_content = new String(content, Utils.ISO_SET);
            ByteBuffer bb = ByteBuffer.wrap(content);
            byte[] mess = new byte[mess_size];
            byte[] sign = new byte[sign_size];
            bb.get(mess, 0, mess_size);
            bb.get(sign, 0, sign_size);
            boolean verified = Cryptography.validate(mess, sign);
            String text = "Message: \"" + Utils.byteArrayToHex(mess) + "\"\nVerified: " + verified + "\nTotal bytes: " + content.length;
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
