package com.example.acmesupermarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class RegistryFragment1 extends Fragment {

    private View v;
    private EditText name;
    private EditText username;
    private EditText password;
    private EditText confirm_password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_registry_1, container, false);

        username = v.findViewById(R.id.username);
        name = v.findViewById(R.id.name);
        password = v.findViewById(R.id.password);
        confirm_password = v.findViewById(R.id.confirm_password);

        name.setOnFocusChangeListener((v, hasFocus) -> {
            if(username.getText().toString().equals("")){
                username.setError("Please fill in this field");
            }
            if(name.getText().toString().equals("")){
                name.setError("Please fill in this field");
            }
            if(password.getText().toString().equals("")){
                password.setError("Please fill in this field");
            }
            if(confirm_password.getText().toString().equals("")){
                confirm_password.setError("Please fill in this field");
            }
            else if(!confirm_password.getText().toString().equals(password.getText().toString())){
                confirm_password.setError("Please make sure the passwords match");
            }
        });

        Button next_btn = v.findViewById(R.id.next);
        next_btn.setOnClickListener(view -> {

            RegistryFragment2  secondFragment= new RegistryFragment2();

            Bundle user_basic_info = new Bundle();
            user_basic_info.putString("username", username.getText().toString());
            user_basic_info.putString("name", name.getText().toString());
            user_basic_info.putString("password", password.getText().toString());

            secondFragment.setArguments(user_basic_info);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.register_frame, secondFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        });

        return v;
    }
}
