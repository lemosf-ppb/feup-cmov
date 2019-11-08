package ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.cooltechworks.creditcarddesign.CreditCardView;
import com.example.acmesupermarket.R;

import java.text.DecimalFormat;

import models.Client;
import models.CreditCard;
import ui.login.LoginViewModel;

public class ProfileFragment extends Fragment {

    private LoginViewModel mViewModel;

    private DecimalFormat df = new DecimalFormat("#.##");


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(LoginViewModel.class);

        displayClientInfo(view, mViewModel.getClient());
    }

    private void displayClientInfo(View view, Client client) {

        TextView username = view.findViewById(R.id.username_text);
        username.setText(String.format("Username: %s", client.getUsername()));

        TextView full_name = view.findViewById(R.id.full_name_textView);
        full_name.setText(String.format("Full Name: %s", client.getName()));

        TextView total_spent = view.findViewById(R.id.money_spent);
        String money_spent = String.format("%s", df.format(client.getTotalValueSpent()));
        total_spent.setText(String.format("You have already spent %s€!", money_spent));

        TextView available_discount = view.findViewById(R.id.available_discount);
        String discount = String.format("%s", df.format(client.getDiscountValueAvailable()));
        available_discount.setText(String.format("You still have %s€ to discount on your future purchases!", discount));


        CreditCard creditCard = client.getCreditCard();
        CreditCardView creditCardView = view.findViewById(R.id.credit_card_profile);
        creditCardView.setCardNumber(creditCard.getNumber());
        creditCardView.setCardExpiry(creditCard.getExpiry());
        creditCardView.setCardHolderName(creditCard.getHolder());
        creditCardView.setCVV(creditCard.getCvv());

    }


}
