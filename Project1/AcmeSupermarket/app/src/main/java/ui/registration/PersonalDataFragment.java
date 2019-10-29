package ui.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;

import com.example.acmesupermarket.R;

import static androidx.navigation.Navigation.findNavController;
import static ui.registration.RegistrationViewModel.RegistrationState.COLLECT_CREDIT_CARD_DATA;

public class PersonalDataFragment extends Fragment {

    private EditText nameEditText, usernameEditText, passwordEditText, confirmPasswordEditText;

    private RegistrationViewModel registrationViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.registry_personal_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registrationViewModel = ViewModelProviders.of(requireActivity()).get(RegistrationViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        registrationViewModel = ViewModelProviders.of(requireActivity()).get(RegistrationViewModel.class);

        setupEventListeners(view);

        setupObserverToChangeScreen(view);
    }

    private void setupEventListeners(View view) {
        nameEditText = view.findViewById(R.id.editText_name);
        usernameEditText = view.findViewById(R.id.editText_username);
        passwordEditText = view.findViewById(R.id.editText_password);
        confirmPasswordEditText = view.findViewById(R.id.editText_confirmPassword);

        confirmPasswordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            checkPassword();
        });


        // When the next button is clicked, collect the current values from the edit texts
        // and pass to the ViewModel to store.
        view.findViewById(R.id.btn_next).setOnClickListener(v -> {
            if (!validateInputs()) return;

            registrationViewModel.collectProfileData(
                    nameEditText.getText().toString(),
                    usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
        });
    }

    private boolean validateInputs() {
        if (nameEditText.getText().toString().equals("")) {
            nameEditText.setError("Please fill in this field");
            return false;
        }
        if (usernameEditText.getText().toString().equals("")) {
            usernameEditText.setError("Please fill in this field");
            return false;
        }

        if (passwordEditText.getText().toString().equals("")) {
            passwordEditText.setError("Please fill in this field");
            return false;
        }

        if (confirmPasswordEditText.getText().toString().equals("")) {
            confirmPasswordEditText.setError("Please fill in this field");
            return false;
        }

        return checkPassword();
    }

    private void setupObserverToChangeScreen(View view) {
        final NavController navController = findNavController(view);

        // RegistrationViewModel updates the registrationState to
        // COLLECT_CREDIT_CARD_DATA when ready to move to the creditCard screen.
        registrationViewModel.getRegistrationState().observe(getViewLifecycleOwner(), state -> {
            if (state == COLLECT_CREDIT_CARD_DATA) {
                navController.navigate(R.id.creditCardFragment);
            }
        });

        // If the user presses back, cancel the user registration and pop back
        // to the home fragment. Since this ViewModel is shared at the activity
        // scope, its state must be reset so that it is in the initial state if
        // the user comes back to register later.
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        registrationViewModel.userCancelledRegistration();
                        navController.popBackStack(R.id.homeFragment, false);
                    }
                });
    }

    private boolean checkPassword() {
        if (!confirmPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())) {
            confirmPasswordEditText.setError("Please make sure the passwords match");
            return false;
        }
        return true;
    }
}
