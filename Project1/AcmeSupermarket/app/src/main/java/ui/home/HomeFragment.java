package ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.acmesupermarket.R;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.home_fragment, container, false);

        final NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        Button loginButton = root.findViewById(R.id.login_btn);
        loginButton.setOnClickListener(v -> navController.navigate(R.id.loginFragment));

        Button registerButton = root.findViewById(R.id.register_btn);
        registerButton.setOnClickListener(v -> navController.navigate(R.id.registration_graph));
        return root;
    }
}