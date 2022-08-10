package com.example.appbanhang;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appbanhang.Adapter.ViewPagerAdapterSignupSignin;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SignInActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initUI();
        ViewPagerAdapterSignupSignin myViewPagerAdapterSignupSignin = new ViewPagerAdapterSignupSignin(this);
        mViewPager.setAdapter(myViewPagerAdapterSignupSignin);
        new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Đăng nhập");
                    break;
                case 1:
                    tab.setText("Đăng ký");
                    break;
            }
        }).attach();


    }
    private void initUI(){
        mTabLayout      = findViewById(R.id.tab_layout);
        mViewPager      = findViewById(R.id.view_pager);
    }
}