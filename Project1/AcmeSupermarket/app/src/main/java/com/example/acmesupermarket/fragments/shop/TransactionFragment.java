package com.example.acmesupermarket.fragments.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.acmesupermarket.R;
import com.example.acmesupermarket.Voucher;

import java.util.ArrayList;
import java.util.List;

public class TransactionFragment extends Fragment {

    private static final String TAG = "VoucherFragment";

    List<Voucher> transactions = new ArrayList<>();
    ArrayAdapter<Voucher> voucherAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_voucher, container, false);

        return v;
    }

}
