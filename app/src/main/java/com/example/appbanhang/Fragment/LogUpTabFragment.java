package com.example.appbanhang.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appbanhang.Object.User;
import com.example.appbanhang.R;
import com.example.appbanhang.SignInActivity;
import com.example.appbanhang.VerifyNumberActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LogUpTabFragment extends Fragment {

    private View mView;
    private EditText UserPhoneLogup, UserPassWordLogup, UserEmailLogup, UserPassWord2Logup;
    private Button btnLogUp;
    public SignInActivity signInActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.logup_tab_fragment, container, false);
        signInActivity = (SignInActivity) getActivity();

        initUI();

        btnLogUp.setOnClickListener(view -> SendtoVerifyNumber());
        return mView;
    }
    private void initUI(){
        UserPhoneLogup  = mView.findViewById(R.id.EditText_UserNameLogup);
        UserEmailLogup  = mView.findViewById(R.id.EditText_UserEmaillogup);
        UserPassWordLogup   = mView.findViewById(R.id.EditText_UserPasswordlogup);
        UserPassWord2Logup  = mView.findViewById(R.id.EditText_UserPasswordlogup2);
        btnLogUp            = mView.findViewById(R.id.button_logup);
    }
    private void SendtoVerifyNumber(){
        String userPhone = UserPhoneLogup.getText().toString().trim();
        String userEmail = UserEmailLogup.getText().toString().trim();
        String userPassword = UserPassWordLogup.getText().toString().trim();
        String userPassword2 = UserPassWord2Logup.getText().toString().trim();
        if (userPhone.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || userPassword2.isEmpty()){
            Toast.makeText(signInActivity, "Vui lòng nhập đầy đủ thông tin" , Toast.LENGTH_SHORT).show();
        }
        else if (!userPassword.equals(userPassword2) || userPassword.length()<6){
            Toast.makeText(signInActivity, "Mật khẩu không trùng hoặc nhỏ hơn 6 ký tự", Toast.LENGTH_SHORT).show();
        }
        else {
            CheckUserLogup(userPhone, userEmail, userPassword);
        }
    }
    private void CheckUserLogup(String userPhone, String userEmail, String userPassword){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata =   firebaseDatabase.getReference("Users");
        mdata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    String key = ds.getKey();
                    if (key!=null && key.equals(userPhone)){
                        Toast.makeText(signInActivity, "Tài khoản đã được sử dụng", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                User user = new User(userPhone, userPassword, "user", userEmail, "No Input", "No Input", "No Input", "No Input", "No Input", "No Input", "No Input");
                Intent intent = new Intent(signInActivity, VerifyNumberActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userObject",user);
                intent.putExtra("dulieuLogupFragment", bundle);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
