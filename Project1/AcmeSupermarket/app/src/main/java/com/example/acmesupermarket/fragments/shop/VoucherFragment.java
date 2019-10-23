package com.example.acmesupermarket.fragments.shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.acmesupermarket.ShopActivity;
import com.example.acmesupermarket.R;
import com.example.acmesupermarket.Voucher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VoucherFragment extends Fragment {

    private static final String TAG = "VoucherFragment";

    List<Voucher> vouchers = new ArrayList<>();
    ArrayAdapter<Voucher> voucherAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_voucher, container, false);

        ListView list = v.findViewById(R.id.voucher_list);
        Context context = Objects.requireNonNull(getActivity()).getApplicationContext();

        if (savedInstanceState != null) {
            ArrayList<Voucher> values = savedInstanceState.getParcelableArrayList("vouchers");
            if (values != null) {
                vouchers = values;
                ((ShopActivity)getActivity()).setTab(2);
            }
        }

        voucherAdapter = new VoucherAdapter(context);
        list.setAdapter(voucherAdapter);

        return v;
    }

    public void onSaveInstanceState(Bundle savedState) {

        super.onSaveInstanceState(savedState);
        savedState.putParcelableArrayList("vouchers", (ArrayList<? extends Parcelable>) vouchers);
    }

    class VoucherAdapter extends ArrayAdapter<Voucher> {
        VoucherAdapter(Context context) {
            super(context, R.layout.voucher_row, vouchers);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public @NonNull
        View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View row = convertView;
            if (row == null)
                row = getLayoutInflater().inflate(R.layout.voucher_row, parent, false);
            Voucher voucher = vouchers.get(position);
            ((TextView) row.findViewById(R.id.voucher_title)).setText(voucher.getName());
            ((TextView) row.findViewById(R.id.voucher_discount)).setText(voucher.getDiscount()+"");

            return (row);
        }
    }

}
