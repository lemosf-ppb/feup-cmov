package ui.shop.cart;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.acmesupermarket.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import models.TransactionItem;
import models.Voucher;
import ui.shop.ShopViewModel;

public class CartFragment extends Fragment {
    private DecimalFormat df = new DecimalFormat("#.##");
    private ShopViewModel mViewModel;
    private CartItemAdapter cartItemsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cart_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(requireActivity()).get(ShopViewModel.class);

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
}