package com.example.appbanhang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {


    Animation logoAnim, sloganAnim;
    TextView tvTenShop, tvSlogan;
    ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        Init();

        imgLogo.setAnimation(logoAnim);
        tvTenShop.setAnimation(sloganAnim);
        tvSlogan.setAnimation(sloganAnim);

        new Handler().postDelayed(this::nextActivity, 2000);
    }

    private void Init(){
        logoAnim    = AnimationUtils.loadAnimation(this, R.anim.splash_icon);
        sloganAnim  = AnimationUtils.loadAnimation(this, R.anim.splash_icon);
        tvTenShop   = findViewById(R.id.textViewNameShop);
        tvSlogan    = findViewById(R.id.textViewSlogan);
        imgLogo     = findViewById(R.id.imageViewLogo);
    }

    private void nextActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            //Chưa login
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        }
        else {
            //đã login
            Intent intent2 = new Intent(this, MainActivity.class);
            startActivity(intent2);
        }
        finish();
    }
}