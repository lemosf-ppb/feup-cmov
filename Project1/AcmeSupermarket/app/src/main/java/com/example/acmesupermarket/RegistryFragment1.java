package com.example.acmesupermarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class RegistryFragment1 extends Fragment {

    private static final String TAG = "RegistryFragment1";

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

        confirm_password.setOnFocusChangeListener((v, hasFocus) -> {
            checkPassword();
        });

        Button next_btn = v.findViewById(R.id.next);
        next_btn.setOnClickListener(view -> {

            if(username.getText().toString().equals("")){
                username.setError("Please fill in this field");
                return;
            }
            if(name.getText().toString().equals("")){
                name.setError("Please fill in this field");
                return;
            }
            if(password.getText().toString().equals("")){
                password.setError("Please fill in this field");
                return;
            }
            checkPassword();


            RegisterActivity registerActivity = (RegisterActivity)getActivity();
            registerActivity.setUserDetails(username.getText().toString(), name.getText().toString(), password.getText().toString());
            registerActivity.setViewPager(1);
        });

        return v;
    }

    private void checkPassword(){
        if(confirm_password.getText().toString().equals("")){
            confirm_password.setError("Please fill in this field");
            return;
        }
        else if(!confirm_password.getText().toString().equals(password.getText().toString())){
            confirm_password.setError("Please make sure the passwords match");
            return;
        }
    }
}
