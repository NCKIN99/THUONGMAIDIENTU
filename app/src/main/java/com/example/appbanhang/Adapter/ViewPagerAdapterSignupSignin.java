package com.example.appbanhang.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appbanhang.Fragment.LoginTabFragment;
import com.example.appbanhang.Fragment.LogUpTabFragment;

public class ViewPagerAdapterSignupSignin extends FragmentStateAdapter {
    public ViewPagerAdapterSignupSignin(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new LogUpTabFragment();
        }
        return new LoginTabFragment();

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
