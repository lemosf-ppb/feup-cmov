package com.example.acmesupermarket;

import android.content.Intent;
import android.os.Bundle;

import com.example.acmesupermarket.fragments.SectionsStatePagerAdapter;
import com.example.acmesupermarket.fragments.registry.RegistryFragment1;
import com.example.acmesupermarket.fragments.registry.RegistryFragment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private ViewPager mViewPager;
    Toolbar toolbar;

    private String username, name, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = findViewById(R.id.registry_viewPager);
        setupViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager mViewPager){
        SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RegistryFragment1(), "RegistryFragment1");
        adapter.addFragment(new RegistryFragment2(), "RegistryFragment2");
        mViewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber){
        mViewPager.setCurrentItem(fragmentNumber);
    }

    public void setUserDetails(String username, String name, String password){
        this.username = username;
        this.name = name;
        this.password = password;
    }

    public void onBtnSignUpClick(String card_holder,String number,String expiry,String cvv){

        Customer customer = new Customer(this.name, this.username, card_holder, number, expiry, cvv);
        Intent intent = new Intent(this, CartActivity.class);
        intent.putExtra("Customer", customer);
        startActivity(intent);
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
