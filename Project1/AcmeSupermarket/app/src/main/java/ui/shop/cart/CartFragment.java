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
import ui.shop.ShopViewModel;

public class CartFragment extends Fragment {
    DecimalFormat df = new DecimalFormat("#.##");
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

        //        checkSavedInstanceState(savedInstanceState);

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
        TextView discount_value = view.findViewById(R.id.discount_value);
        discount_value.setText(String.format("%s", df.format(mViewModel.discountAvailable.getValue())));

        TextView total_price_value = view.findViewById(R.id.total_price_value);
        total_price_value.setText(String.format("%s", df.format(mViewModel.totalPrice.getValue())));
    }

    private void setObservers(View view) {
        mViewModel.transactionItems.observe(this, transactionItemsList -> cartItemsAdapter.setTransactionItems(transactionItemsList));
        mViewModel.currentVoucher.observe(this, currentVoucher -> {
            TextView voucher_id_text_view = view.findViewById(R.id.voucher_id);
            String voucher_id = "None";

            if (currentVoucher != null) {
                voucher_id = currentVoucher.getName();
            }
            voucher_id_text_view.setText(voucher_id);
        });
        mViewModel.discountAvailable.observe(this, discountAvailable -> {
            TextView discount_value = view.findViewById(R.id.discount_value);
            discount_value.setText(String.format("%s", df.format(discountAvailable)));
        });
        mViewModel.applyDiscount.observe(this, applyDiscount -> {

        });
        mViewModel.totalPrice.observe(this, totalPrice -> {
            TextView total_price_value = view.findViewById(R.id.total_price_value);

            double voucherPercent = 0;
            if (mViewModel.currentVoucher.getValue() != null)
                voucherPercent = mViewModel.currentVoucher.getValue().getDiscount() / 100.0;

            double totalAfterVoucher = (1.0 - voucherPercent) * totalPrice;
            total_price_value.setText(String.format("%s", df.format(totalAfterVoucher)));
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


//    public void onSaveInstanceState(Bundle savedState) {
//        super.onSaveInstanceState(savedState);
//        savedState.putParcelableArrayList("cart_items", (ArrayList<? extends Parcelable>) transactionItems);
//        savedState.putDouble("total_price", totalPrice);
//        savedState.putBoolean("applyDiscount", applyDiscount);
//        savedState.putDouble("discount", discount);
//    }
//
//    private void checkSavedInstanceState(Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            ArrayList<TransactionItem> values = savedInstanceState.getParcelableArrayList("cart_items");
//            if (values != null) {
//                transactionItems = values;
//                ((ShopActivity) getActivity()).setTab(0);
//            }
//            totalPrice = savedInstanceState.getDouble("total_price");
//            applyDiscount = savedInstanceState.getBoolean("applyDiscount");
//            discount = savedInstanceState.getDouble("discount");
//        }
//    }
}