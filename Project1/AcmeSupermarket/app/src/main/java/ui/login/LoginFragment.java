package ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.acmesupermarket.R;
import com.google.android.material.snackbar.Snackbar;

public class LoginFragment extends Fragment {

    private EditText usernameEditText, passwordEditText;

    private LoginViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(LoginViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(LoginViewModel.class);

        usernameEditText = view.findViewById(R.id.username_edit_text);
        passwordEditText = view.findViewById(R.id.password_edit_text);

        Button loginButton = view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> mViewModel.authenticate(usernameEditText.getText().toString(), passwordEditText.getText().toString()));

        final NavController navController = Navigation.findNavController(view);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        mViewModel.refuseAuthentication();
                        navController.popBackStack(R.id.homeFragment, false);
                    }
                });

        final View root = view;
        mViewModel.authenticationState.observe(getViewLifecycleOwner(),
                authenticationState -> {
                    switch (authenticationState) {
                        case AUTHENTICATED:
                            navController.navigate(R.id.shopFragment);
                            break;
                        case INVALID_AUTHENTICATION:
                            Snackbar.make(root,
                                    R.string.invalid_credentials,
                                    Snackbar.LENGTH_SHORT
                            ).show();
                            break;
                    }
                });
    }
}

