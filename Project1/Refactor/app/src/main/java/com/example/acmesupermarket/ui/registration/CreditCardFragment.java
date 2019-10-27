package com.example.acmesupermarket.ui.registration;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;

import com.cooltechworks.creditcarddesign.CreditCardView;
import com.example.acmesupermarket.R;
import com.example.acmesupermarket.ui.login.LoginViewModel;

import static androidx.navigation.Navigation.findNavController;
import static com.example.acmesupermarket.ui.registration.RegistrationViewModel.RegistrationState.REGISTRATION_COMPLETED;

public class CreditCardFragment extends Fragment {

    private EditText cardNumberTextView, cardHolderTextView, cardExpiryTextView, cardCvvTextView;

    private LoginViewModel loginViewModel;
    private RegistrationViewModel registrationViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.registry_credit_card_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registrationViewModel = ViewModelProviders.of(requireActivity()).get(RegistrationViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ViewModelProvider provider = ViewModelProviders.of(requireActivity());
        registrationViewModel = provider.get(RegistrationViewModel.class);
        loginViewModel = provider.get(LoginViewModel.class);

        setupEventListeners(view);

        setupObserverToChangeScreen(view);
    }

    private void setupEventListeners(View view) {
        cardNumberTextView = view.findViewById(R.id.editText_credit_card_number);
        cardHolderTextView = view.findViewById(R.id.editText_credit_card_name);
        cardExpiryTextView = view.findViewById(R.id.editText_credit_card_validity);
        cardCvvTextView = view.findViewById(R.id.editText_credit_card_cvv);
        CreditCardView creditCardView = view.findViewById(R.id.credit_card_view);

        setupCreditCardEventListeners(creditCardView);

        // When the register button is clicked, collect the current values from
        // the two edit texts and pass to the ViewModel to complete registration.
        view.findViewById(R.id.btn_submitRegistry).setOnClickListener(v -> {
            if (!validateInputs()) return;

            registrationViewModel.createCreditCard(
                    cardNumberTextView.getText().toString(),
                    cardHolderTextView.getText().toString(),
                    cardExpiryTextView.getText().toString(),
                    cardCvvTextView.getText().toString()
            );
        });

    }

    private void setupCreditCardEventListeners(CreditCardView creditCardView) {
        // TODO: Change to LiveData types
        cardNumberTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 16) {
                    return;
                }
                creditCardView.setCardNumber(cardNumberTextView.getText().toString());
            }
        });

        cardHolderTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 17) {
                    return;
                }
                creditCardView.setCardHolderName(cardHolderTextView.getText().toString());
            }
        });

        cardExpiryTextView.addTextChangedListener(new TextWatcher() {


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
                creditCardView.setCardExpiry(cardExpiryTextView.getText().toString());
            }
        });

        cardCvvTextView.addTextChangedListener(new TextWatcher() {

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
                creditCardView.setCVV(cardCvvTextView.getText().toString());
            }
        });
    }

    private boolean validateInputs() {
        if (cardNumberTextView.getText().toString().length() != 16) {
            cardNumberTextView.setError("Please fill a valid 16 digit card number!");
            return false;
        }

        if (cardHolderTextView.getText().toString().equals("")) {
            cardHolderTextView.setError("Please fill in this field");
            return false;
        }

        if (cardExpiryTextView.getText().toString().length() != 5) {
            cardExpiryTextView.setError("Please fill in this field with MM/YY");
            return false;
        }

        if (cardCvvTextView.getText().toString().length() != 3) {
            cardCvvTextView.setError("Please fill in this field");
            return false;
        }

        return true;
    }

    private void setupObserverToChangeScreen(View view) {
        final NavController navController = findNavController(view);

        // RegistrationViewModel updates the registrationState to
        // REGISTRATION_COMPLETED when ready, and for this example, the username
        // is accessed as a read-only property from RegistrationViewModel and is
        // used to directly authenticate with loginViewModel.
        registrationViewModel.getRegistrationState().observe(
                getViewLifecycleOwner(), state -> {
                    if (state == REGISTRATION_COMPLETED) {
                        // Here we authenticate with the token provided by the ViewModel
                        // then pop back to the profile_fragment, where the user authentication
                        // status will be tested and should be authenticated.
                        String authToken = registrationViewModel.getAuthToken();
                        loginViewModel.authenticate(authToken);
                        navController.navigate(R.id.shopFragment);
                    }
                }
        );

        // If the user presses back, cancel the user registration and pop back
        // to the login fragment. Since this ViewModel is shared at the activity
        // scope, its state must be reset so that it will be in the initial
        // state if the user comes back to register later.
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        registrationViewModel.userCancelledRegistration();
                        navController.popBackStack(R.id.personalDataFragment, false);
                    }
                });
    }

}
