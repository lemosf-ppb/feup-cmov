package com.example.acmesupermarket;

import android.os.Bundle;
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

        card_holder.setOnFocusChangeListener((v, hasFocus) -> {
            if(card_holder.getText().toString().equals("")){
                card_holder.setError("Please fill in this field");
            }
            else{
                creditCardView.setCardHolderName(card_holder.getText().toString());
            }
        });

        number.setOnFocusChangeListener((v, hasFocus) -> {
            if(number.getText().toString().equals("")){
                number.setError("Please fill in this field");
            }
            else{
                creditCardView.setCardNumber(number.getText().toString());
            }
        });

        expiry.setOnFocusChangeListener((v, hasFocus) -> {
            if(expiry.getText().toString().equals("")){
                expiry.setError("Please fill in this field");
            }
            else{
                creditCardView.setCardExpiry(expiry.getText().toString());
            }
        });

        cvv.setOnFocusChangeListener((v, hasFocus) -> {
            if(cvv.getText().toString().equals("")){
                cvv.setError("Please fill in this field");
            }
            else{
                creditCardView.setCVV(cvv.getText().toString());
            }
        });

        return v;
    }
}
