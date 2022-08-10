package com.example.appbanhang.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appbanhang.Fragment.TabFragmentAccessory;
import com.example.appbanhang.Fragment.TabFragmentHome;
import com.example.appbanhang.Fragment.TabFragmentLaptop;
import com.example.appbanhang.Fragment.TabFragmentPhone;
import com.example.appbanhang.Fragment.TabFragmentUser;

public class ViewPagerAdpterMain extends FragmentStateAdapter {

    public ViewPagerAdpterMain(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new TabFragmentPhone();
            case 2:
                return new TabFragmentLaptop();
            case 3:
                return new TabFragmentAccessory();
            case 4:
                return new TabFragmentUser();
            default:
                return new TabFragmentHome();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
