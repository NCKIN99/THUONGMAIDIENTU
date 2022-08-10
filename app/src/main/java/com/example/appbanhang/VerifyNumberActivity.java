package com.example.appbanhang;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.Object.User;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyNumberActivity extends AppCompatActivity {

    Button btnVerifyNB;
    EditText edtVerifyNB;
    ProgressBar progressBar;
    String verificationCodeBySystem;
    TextView tvResend;
    String userName, userAddress, userEmail, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);
        initUI();

        Intent Nhandl = getIntent();
        Bundle bundle = Nhandl.getBundleExtra("dulieuLogupFragment");
        User user = (User) bundle.getSerializable("userObject");
        userName = user.getUserName();
        userPassword = user.getUserPassword();
        userAddress = user.getUserAddress();
        userEmail   = user.getUserEmail();


        SendVerifyPhoneNumber(user.getUserName());
        btnVerifyNB.setOnClickListener(view -> {
            String code = edtVerifyNB.getText().toString().trim();
            if (code.isEmpty() || code.length()<6){
                Toast.makeText(VerifyNumberActivity.this, "Mã OTP sai", Toast.LENGTH_SHORT).show();
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }

        });
        tvResend.setOnClickListener(view -> ReSendOTP(user.getUserName()));


    }
    private void initUI() {
        btnVerifyNB = findViewById(R.id.button_verifynumber);
        edtVerifyNB = findViewById(R.id.editText_verifynumber);
        progressBar = findViewById(R.id.progress_circular);
        tvResend    = findViewById(R.id.textView_ResendVertifyNB);
    }

    private void SendVerifyPhoneNumber(String phone){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("vn");

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84"+ phone)       // Phone number to verify
                        .setTimeout(55L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(VerifyNumberActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                progressBar.setVisibility(View.GONE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyNumberActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
    private void verifyCode(String verificationCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, verificationCode);
        signInTheUserByCredentials(credential);
    }
    private void signInTheUserByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyNumberActivity.this, task -> {
                    if(task.isSuccessful()){
                        CreateNewUser();
                        Intent intent = new Intent(VerifyNumberActivity.this, SignInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(VerifyNumberActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void ReSendOTP(String userPhone){
        CountDownTimer countDownTimer = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long l) {
                SendVerifyPhoneNumber(userPhone);
                tvResend.setText(l/1000+"");
                tvResend.setEnabled(false);
            }

            @Override
            public void onFinish() {
                tvResend.setText("Gửi lại OTP");
                tvResend.setEnabled(true);
            }
        };
        countDownTimer.start();
    }
    private void CreateNewUser(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mData = firebaseDatabase.getReference("Users");
        mData.child(userName).setValue(new User(userName, userPassword, "user", userEmail, "No Input", "No Input", "No Input", "No Input", "No Input", "No Input", "No Input"));
        Toast.makeText(this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
    }



}