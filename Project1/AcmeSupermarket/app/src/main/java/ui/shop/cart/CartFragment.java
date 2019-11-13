package ui.shop.cart;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.acmesupermarket.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;

import javax.crypto.Cipher;

import models.TransactionItem;
import models.Voucher;
import ui.login.LoginViewModel;
import ui.shop.ShopViewModel;
import utils.Constants;
import utils.Utils;

import static android.app.Activity.RESULT_OK;
import static services.crypto.Constants.ENC_ALGO;

public class CartFragment extends Fragment {
    private DecimalFormat df = new DecimalFormat("#.##");
    private LoginViewModel loginViewModel;
    private ShopViewModel shopViewModel;
    private CartItemAdapter cartItemsAdapter;
    private Resources res;
    private View view;

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

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cart_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        ViewModelProvider provider = ViewModelProviders.of(requireActivity());
        loginViewModel = provider.get(LoginViewModel.class);
        shopViewModel = provider.get(ShopViewModel.class);
        res = requireActivity().getApplicationContext().getResources();

        setCartItemAdapter(view);

        inflateView(view);

        setObservers(view);
    }

    private void setCartItemAdapter(View view) {
        ListView list = view.findViewById(R.id.cart_list);
        Context context = requireActivity().getApplicationContext();
        ArrayList<TransactionItem> transactionItems = shopViewModel.getTransactionItems().getValue();

        cartItemsAdapter = new CartItemAdapter(context, transactionItems, shopViewModel);
        list.setAdapter(cartItemsAdapter);
    }

    private void inflateView(View view) {
        updateCurrentVoucherUI(view, shopViewModel.currentVoucher.getValue());

        updateDiscountAvailableUI(view, loginViewModel.getClient().getDiscountValueAvailable());

        updateUseDiscountUI(view, shopViewModel.applyDiscount.getValue());

        updateTotalPriceUI(view, shopViewModel.totalPrice.getValue());
    }

    private void setObservers(View view) {
        shopViewModel.transactionItems.observe(this, transactionItemsList -> cartItemsAdapter.setTransactionItems(transactionItemsList));
        shopViewModel.currentVoucher.observe(this, currentVoucher -> updateCurrentVoucherUI(view, currentVoucher));
        loginViewModel.client.observe(this, client -> updateDiscountAvailableUI(view, client.getDiscountValueAvailable()));
        shopViewModel.totalPrice.observe(this, totalPrice -> updateTotalPriceUI(view, totalPrice));

        Switch apply_discount_checkbox = view.findViewById(R.id.apply_discount);
        apply_discount_checkbox.setOnCheckedChangeListener((buttonView, isChecked) ->
                shopViewModel.applyDiscount(isChecked));

        Button checkout_button = view.findViewById(R.id.checkout_btn);
        checkout_button.setOnClickListener(v -> {
            if (shopViewModel.isCartEmpty()) {
                Snackbar.make(view,
                        "Your cart is empty! Please add at least one item!",
                        Snackbar.LENGTH_SHORT
                ).show();
                return;
            }
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.checkoutFragment);
        });

        Button clearCart_btn = view.findViewById(R.id.clear_btn);
        clearCart_btn.setOnClickListener(v -> {
            if (shopViewModel.isCartEmpty()) {
                Snackbar.make(view,
                        "Your cart is already empty!",
                        Snackbar.LENGTH_SHORT
                ).show();
                return;
            }
            shopViewModel.resetTransactionItems();
        });

        FloatingActionButton add_item_btn = view.findViewById(R.id.add_item);
        add_item_btn.setOnClickListener(v -> {
            if (shopViewModel.isCartFull()) {
                Snackbar.make(view,
                        "Your cart is already full!",
                        Snackbar.LENGTH_SHORT
                ).show();
                return;
            }
            scanQRCode();
        });
    }

    private void updateCurrentVoucherUI(View view, Voucher currentVoucher) {
        TextView voucher_id_text_view = view.findViewById(R.id.voucher_label);
        String voucher_id = "None";

        if (currentVoucher != null) {
            voucher_id = (currentVoucher.getId() + "").substring(0, 10);
        }

        String selected_voucher_string = res.getString(R.string.use_voucher, voucher_id);
        voucher_id_text_view.setText(selected_voucher_string);
    }

    private void updateDiscountAvailableUI(View view, Double discountAvailable) {
        shopViewModel.discountAvailable = (discountAvailable != null ? discountAvailable : 0);
        TextView discount_value = view.findViewById(R.id.discount_label);

        String discount_value_string = res.getString(R.string.discount_label, String.format("%s", df.format(shopViewModel.discountAvailable)));
        discount_value.setText(discount_value_string);
    }

    private void updateUseDiscountUI(View view, Boolean useDiscount) {
        Switch apply_discount_checkbox = view.findViewById(R.id.apply_discount);
        apply_discount_checkbox.setChecked(useDiscount);
    }

    private void updateTotalPriceUI(View view, Double totalPrice) {
        TextView total_price_value = view.findViewById(R.id.total_price_text);

        String total_price_string = res.getString(R.string.total_payed_price, String.format("%s", df.format(totalPrice)));
        total_price_value.setText(total_price_string);
    }

    private void scanQRCode() {
        try {
            Intent intent = new Intent(Constants.ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException e) {
            showDialog(getActivity()).show();
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
        byte[] clearTag;

        try {
            Cipher cipher = Cipher.getInstance(ENC_ALGO);
            cipher.init(Cipher.DECRYPT_MODE, loginViewModel.getClient().getAcmePublicKey());
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

        String text = "Read Tag (" + clearTag.length + "):\n" + Utils.byteArrayToHex(clearTag) + "\n\n" +
                ((tId == Constants.tagId) ? "correct" : "wrong") + "\n" +
                "ID: " + id.toString() + "\n" +
                "Name: " + name + "\n" +
                "Price: €" + euros + "." + cents;

        Snackbar.make(view,
                text,
                Snackbar.LENGTH_SHORT
        ).show();

        double price = euros * 1.0 + cents / 100.0;

        TransactionItem transactionItem = new TransactionItem(id, name, price, 1);
        shopViewModel.addTransactionItem(transactionItem);
    }
}