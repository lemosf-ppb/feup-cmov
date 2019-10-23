package com.example.acmesupermarket.fragments.registry;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cooltechworks.creditcarddesign.CreditCardView;
import com.example.acmesupermarket.R;
import com.example.acmesupermarket.RegisterActivity;

public class RegistryFragment2 extends Fragment {

    private static final String TAG = "RegistryFragment2";

    private View v;
    private TextView card_holder, number, expiry, cvv;
    private Button submit_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.card_frag, container, false);

        CreditCardView creditCardView = v.findViewById(R.id.card_5);

        card_holder = v.findViewById(R.id.credit_card_name);
        number = v.findViewById(R.id.credit_card_number);
        expiry = v.findViewById(R.id.credit_card_validity);
        cvv = v.findViewById(R.id.credit_card_cvv);
        submit_btn = v.findViewById(R.id.submit_registry);


        number.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    //flipToBlue();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 16) {
                    return;
                }
                creditCardView.setCardNumber(number.getText().toString());
            }
        });

        card_holder.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    //flipToBlue();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 17) {
                    return;
                }
                creditCardView.setCardHolderName(card_holder.getText().toString());
            }
        });

        expiry.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 5) {
                    return;
                }
                creditCardView.setCardExpiry(expiry.getText().toString());
            }
        });

        cvv.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 3) {
                    return;
                }
                creditCardView.setCVV(cvv.getText().toString());
            }
        });

        submit_btn.setOnClickListener((View v) -> {

            String number = this.number.getText().toString();
            if (number.length() != 16) {
                this.number.setError("Please fill a valid 16 digit card number!");
                return;
            }

            String card_holder = this.card_holder.getText().toString();
            if (card_holder.equals("")) {
                this.card_holder.setError("Please fill in this field");
                return;
            }

            String expiry = this.expiry.getText().toString();
            if (expiry.length() != 5) {
                this.expiry.setError("Please fill in this field with MM/YY");
                return;
            }

            String cvv = this.cvv.getText().toString();
            if (cvv.length() != 3) {
                this.cvv.setError("Please fill in this field");
                return;
            }

            ((RegisterActivity) getActivity()).onBtnSignUpClick(card_holder, number, expiry, cvv);

        });

        return v;
    }
}
