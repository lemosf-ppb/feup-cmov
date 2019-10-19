package com.example.acmesupermarket;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cooltechworks.creditcarddesign.CreditCardView;

public class RegistryFragment2 extends Fragment {

    View v;
    TextView card_holder, number, expiry, cvv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.card_frag, container, false);

        CreditCardView creditCardView = v.findViewById(R.id.card_5);

        card_holder = v.findViewById(R.id.credit_card_name);
        number = v.findViewById(R.id.credit_card_number);
        expiry = v.findViewById(R.id.credit_card_validity);
        cvv = v.findViewById(R.id.credit_card_cvv);

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

        return v;
    }
}
