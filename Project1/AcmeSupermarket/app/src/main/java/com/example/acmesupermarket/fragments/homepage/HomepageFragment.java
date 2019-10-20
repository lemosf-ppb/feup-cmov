package com.example.acmesupermarket.fragments.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.acmesupermarket.CartActivity;
import com.example.acmesupermarket.R;

public class HomepageFragment extends Fragment {

    private static final String TAG = "HomepageFragment";

    private EditText username;
    private EditText password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_homepage, container, false);

        username = v.findViewById(R.id.edit_username);
        password = v.findViewById(R.id.editSignInPass);

        v.findViewById(R.id.sign_in).setOnClickListener(view -> {

            if(username.getText().toString().equals("")){
                username.setError("Please fill in this field");
                return;
            }
            if(password.getText().toString().equals("")){
                password.setError("Please fill in this field");
                return;
            }
            //TODO: Validate user with DB

            Intent intent = new Intent(getActivity(), CartActivity.class);
            startActivity(intent);

        });

        return v;
    }
}
