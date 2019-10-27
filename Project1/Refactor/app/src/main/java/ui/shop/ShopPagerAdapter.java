package ui.shop;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ShopPagerAdapter extends FragmentStatePagerAdapter {
    private static final String[] TAB_TITLES = new String[]{"Cart", "Vouchers"};

    public ShopPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        Fragment fragment;
        switch (i) {
            case 0:
                fragment = new CartFragment();
                break;
            case 1:
                fragment = new VoucherFragment();
                break;
            default:
                return new Fragment();
        }
        Bundle args = new Bundle();
        args.putInt(CartFragment.ARG_OBJECT, i + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

}
