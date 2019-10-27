package com.example.acmesupermarket.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.acmesupermarket.R;
import com.example.acmesupermarket.ui.login.LoginViewModel;

public class ProfileFragment extends Fragment {

    private LoginViewModel mViewModel;

    private TextView welcomeTextView;


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
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


        welcomeTextView = view.findViewById(R.id.homeFragment);

        final NavController navController = Navigation.findNavController(view);
        mViewModel.authenticationState.observe(getViewLifecycleOwner(),
                authenticationState -> {
                    switch (authenticationState) {
                        case AUTHENTICATED:
                            showWelcomeMessage();
                            break;
                        case UNAUTHENTICATED:
                            navController.navigate(R.id.loginFragment);
                            break;
                    }
                });

    }

    private void showWelcomeMessage() {
//        welcomeTextView.setText("Ola pussy boy");
        Log.e("lol", "pussy");
    }


}
