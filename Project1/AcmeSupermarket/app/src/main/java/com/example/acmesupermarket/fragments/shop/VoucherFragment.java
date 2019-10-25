package com.example.acmesupermarket.fragments.shop;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.acmesupermarket.R;
import com.example.acmesupermarket.ShopActivity;
import com.example.acmesupermarket.Voucher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VoucherFragment extends Fragment {

    private static final String TAG = "VoucherFragment";

    List<Voucher> vouchers = new ArrayList<>();
    private Voucher active_voucher = null;
    private View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_voucher, container, false);

        Context context = Objects.requireNonNull(getActivity()).getApplicationContext();

        if (savedInstanceState != null) {
            ArrayList<Voucher> values = savedInstanceState.getParcelableArrayList("vouchers");
            if (values != null) {
                vouchers = values;
                ((ShopActivity) getActivity()).setTab(2);
            }

            active_voucher = savedInstanceState.getParcelable("active_voucher");
        }
        else{
            //TODO: Fill with correct vouchers list
            vouchers.add(new Voucher("Voucher 1",5));
            vouchers.add(new Voucher("Voucher 2",15));
        }

        addVouchersToRadioGroup(context);
        setActiveRadioBtn();
        ((ShopActivity) getActivity()).setSelectedVoucher(active_voucher);

        return v;
    }

    private void addVouchersToRadioGroup(Context context){
        RadioGroup radioGroup = v.findViewById(R.id.voucher_radio_group);
        Resources res = getResources();

        for(Voucher voucher : vouchers)
        {
            RadioButton voucher_btn = new RadioButton(context);
            String voucher_label = res.getString(R.string.voucher_list_item, voucher.getName(), voucher.getDiscount()) + "%";
            voucher_btn.setText(voucher_label);
            radioGroup.addView(voucher_btn);
        }

        radioGroup.setOnCheckedChangeListener((rGroup, checkedId) -> {

            int radioBtnID = rGroup.getCheckedRadioButtonId();
            RadioButton radioB = rGroup.findViewById(radioBtnID);

            int index = rGroup.indexOfChild(radioB);
            if(index == 0){
                active_voucher = null;
            }
            else{
                active_voucher = vouchers.get(index-1);
            }

        });
    }

    private void setActiveRadioBtn(){
        RadioGroup radioGroup = v.findViewById(R.id.voucher_radio_group);
        int index = 0;
        if(active_voucher != null)
        {
            index = vouchers.indexOf(active_voucher) + 1;
        }

        RadioButton radioButton = (RadioButton) radioGroup.getChildAt(index);
        radioButton.setChecked(true);
    }

    public void onSaveInstanceState(Bundle savedState) {

        super.onSaveInstanceState(savedState);
        savedState.putParcelableArrayList("vouchers", (ArrayList<? extends Parcelable>) vouchers);
        savedState.putParcelable("active_voucher", active_voucher);
    }
}
