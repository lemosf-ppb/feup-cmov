package ui.shop.cart;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.acmesupermarket.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;

import javax.crypto.Cipher;

import models.TransactionItem;
import models.Voucher;
import ui.shop.ShopViewModel;
import utils.Constants;

import static android.app.Activity.RESULT_OK;

public class CartFragment extends Fragment {
    private DecimalFormat df = new DecimalFormat("#.##");
    private ShopViewModel mViewModel;
    private CartItemAdapter cartItemsAdapter;
    private PublicKey pub;

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, (d, i) -> {
            Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            act.startActivity(intent);
        });
        downloadDialog.setNegativeButton(buttonNo, null);
        return downloadDialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cart_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(requireActivity()).get(ShopViewModel.class);

        //        checkSavedInstanceState(savedInstanceState);


        try {
            pub = readPublicKey(Constants.key);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        setCartItemAdapter(view);

        inflateView(view);

        setObservers(view);
    }

    private void setCartItemAdapter(View view) {
        ListView list = view.findViewById(R.id.cart_list);
        Context context = requireActivity().getApplicationContext();
        ArrayList<TransactionItem> transactionItems = mViewModel.getTransactionItems().getValue();

        cartItemsAdapter = new CartItemAdapter(context, transactionItems, mViewModel);
        list.setAdapter(cartItemsAdapter);
    }

    private void inflateView(View view) {
        updateCurrentVoucherUI(view, mViewModel.currentVoucher.getValue());

        updateDiscountAvailableUI(view, mViewModel.discountAvailable.getValue());

        updateUseDiscountUI(view, mViewModel.applyDiscount.getValue());

        updateTotalPriceUI(view, mViewModel.totalPrice.getValue());
    }

    private void setObservers(View view) {
        mViewModel.transactionItems.observe(this, transactionItemsList -> cartItemsAdapter.setTransactionItems(transactionItemsList));
        mViewModel.currentVoucher.observe(this, currentVoucher -> {
            updateCurrentVoucherUI(view, currentVoucher);
        });
        mViewModel.discountAvailable.observe(this, discountAvailable -> {
            updateDiscountAvailableUI(view, discountAvailable);
        });
        mViewModel.totalPrice.observe(this, totalPrice -> {
            updateTotalPriceUI(view, totalPrice);
        });

        CheckBox apply_discount_checkbox = view.findViewById(R.id.apply_discount);
        apply_discount_checkbox.setOnCheckedChangeListener((buttonView, isChecked) ->
                mViewModel.applyDiscount(isChecked));

        Button checkout_button = view.findViewById(R.id.checkout_btn);
        checkout_button.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.checkoutFragment);
        });

        FloatingActionButton add_item_btn = view.findViewById(R.id.add_item);
        add_item_btn.setOnClickListener(v -> scanQRCode());
    }

    private PublicKey readPublicKey(String pubKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String cleanKey = pubKey.replace("\n", "");
        cleanKey = cleanKey.replace(Constants.BEGIN_PUBLIC_KEY, "");
        cleanKey = cleanKey.replace(Constants.END_PUBLIC_KEY, "");
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(android.util.Base64.decode(cleanKey, android.util.Base64.DEFAULT));
        KeyFactory keyFactory = KeyFactory.getInstance(Constants.KEY_ALGO);
        return keyFactory.generatePublic(publicSpec);
    }

    private void scanQRCode() {
        try {
            Intent intent = new Intent(Constants.ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            showDialog(getActivity(), "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
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

    private void updateCurrentVoucherUI(View view, Voucher currentVoucher) {
        TextView voucher_id_text_view = view.findViewById(R.id.voucher_id);
        String voucher_id = "None";

        if (currentVoucher != null) {
            voucher_id = (currentVoucher.getId() + "").substring(0, 10);
        }
        voucher_id_text_view.setText(voucher_id);
    }

    private void updateDiscountAvailableUI(View view, Double discountAvailable) {
        TextView discount_value = view.findViewById(R.id.discount_value);
        discount_value.setText(String.format("%s", df.format(discountAvailable)));
    }

    private void updateUseDiscountUI(View view, Boolean useDiscount) {
        CheckBox apply_discount_checkbox = view.findViewById(R.id.apply_discount);
        apply_discount_checkbox.setChecked(useDiscount);
    }

    private void updateTotalPriceUI(View view, Double totalPrice) {
        TextView total_price_value = view.findViewById(R.id.total_price_value);

        double voucherPercent = 0;
        if (mViewModel.currentVoucher.getValue() != null)
            voucherPercent = mViewModel.currentVoucher.getValue().getDiscount() / 100.0;

        double totalAfterVoucher = (1.0 - voucherPercent) * totalPrice;
        total_price_value.setText(String.format("%s", df.format(totalAfterVoucher)));
    }

    //TODO: SAVE UUID on the transaction item model
    private void decodeAndShow(byte[] encTag) {
        byte[] clearTag;

        try {
            Cipher cipher = Cipher.getInstance(Constants.ENC_ALGO);
            cipher.init(Cipher.DECRYPT_MODE, pub);
            clearTag = cipher.doFinal(encTag);
        } catch (Exception e) {
            return;
        }
        ByteBuffer tag = ByteBuffer.wrap(clearTag);
        int tId = tag.getInt();
        long most = tag.getLong();
        long less = tag.getLong();
        UUID id = new UUID(most, less);
        int euros = tag.getInt();
        int cents = tag.getInt();
        byte l = tag.get();
        byte[] bName = new byte[l];
        tag.get(bName);
        String name = new String(bName, StandardCharsets.ISO_8859_1);

        String text = "Read Tag (" + clearTag.length + "):\n" + byteArrayToHex(clearTag) + "\n\n" +
                ((tId == Constants.tagId) ? "correct" : "wrong") + "\n" +
                "ID: " + id.toString() + "\n" +
                "Name: " + name + "\n" +
                "Price: €" + euros + "." + cents;

        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();

        double price = euros * 1.0 + cents / 100.0;

        TransactionItem transactionItem = new TransactionItem(id, name, price, 1);
        mViewModel.addTransactionItem(transactionItem);
    }

    private String byteArrayToHex(byte[] ba) {                              // converter
        StringBuilder sb = new StringBuilder(ba.length * 2);
        for (byte b : ba)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

}