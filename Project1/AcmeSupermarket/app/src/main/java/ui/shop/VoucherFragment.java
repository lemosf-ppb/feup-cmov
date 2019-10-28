package ui.shop;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.acmesupermarket.R;

import java.util.ArrayList;

import models.Voucher;

public class VoucherFragment extends Fragment {

    private ShopViewModel mViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.voucher_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(requireActivity()).get(ShopViewModel.class);
        Context context = requireActivity().getApplicationContext();

//        checkSavedInstanceState(savedInstanceState);

        addVouchersToRadioGroup(context, view);

        setActiveRadioBtn(view);
    }

//    private void checkSavedInstanceState(Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            ArrayList<Voucher> values = savedInstanceState.getParcelableArrayList("vouchers");
//            if (values != null) {
//                vouchers = values;
//                ((ShopActivity) getActivity()).setTab(2);
//            }
//
//            active_voucher = savedInstanceState.getParcelable("active_voucher");
//        }
//    }

    private void addVouchersToRadioGroup(Context context, View view) {
        RadioGroup radioGroup = view.findViewById(R.id.voucher_radio_group);
        Resources res = getResources();

        ArrayList<Voucher> vouchersList = mViewModel.getVouchers().getValue();
        for (Voucher voucher : vouchersList) {
            RadioButton voucher_btn = new RadioButton(context);
            String voucher_label = res.getString(R.string.voucher_list_item, voucher.getId(), voucher.getDiscount()) + "%";
            voucher_btn.setText(voucher_label);
            radioGroup.addView(voucher_btn);
        }

        radioGroup.setOnCheckedChangeListener((rGroup, checkedId) -> {

            int radioBtnID = rGroup.getCheckedRadioButtonId();
            RadioButton radioB = rGroup.findViewById(radioBtnID);

            int index = rGroup.indexOfChild(radioB);
            if (index == 0) {
                mViewModel.setSelectedVoucher(null);
            } else {
                mViewModel.setSelectedVoucher(vouchersList.get(index - 1));
            }

        });
    }

    private void setActiveRadioBtn(View view) {
        RadioGroup radioGroup = view.findViewById(R.id.voucher_radio_group);
        ArrayList<Voucher> vouchersList = mViewModel.getVouchers().getValue();

        int index = 0;
        Voucher currentVoucher = mViewModel.currentVoucher.getValue();
        if (currentVoucher != null) {
            index = vouchersList.indexOf(currentVoucher) + 1;
        }

        RadioButton radioButton = (RadioButton) radioGroup.getChildAt(index);
        radioButton.setChecked(true);
    }

//    public void onSaveInstanceState(Bundle savedState) {
//        super.onSaveInstanceState(savedState);
//        savedState.putParcelableArrayList("vouchers", (ArrayList<? extends Parcelable>) vouchers);
//        savedState.putParcelable("active_voucher", active_voucher);
//    }
}
