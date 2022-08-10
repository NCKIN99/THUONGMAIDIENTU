package com.example.appbanhang.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appbanhang.MainActivity;
import com.example.appbanhang.Object.User;
import com.example.appbanhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginTabFragment extends Fragment{

    private EditText edtUserPhone, edtUserPassword;
    private Button btnLogin;
    private View mView;
    private CheckBox cbSave;
    SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.login_tab_fragment, container, false);
        initUI();
        sharedPreferences = getActivity().getSharedPreferences("UserName_Pass", Context.MODE_PRIVATE);
        edtUserPhone.setText(sharedPreferences.getString("UserName", ""));
        edtUserPassword.setText(sharedPreferences.getString("UserPassword", ""));
        cbSave.setChecked(sharedPreferences.getBoolean("Status", false));

        btnLogin.setOnClickListener(view -> {
            String UserPhone = edtUserPhone.getText().toString().trim();
            String UserPassword = edtUserPassword.getText().toString().trim();
            if (UserPhone.isEmpty() || UserPassword.isEmpty()){
                Toast.makeText(mView.getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }
            else {
                UserLogIn(UserPhone, UserPassword);
            }
        });
        return mView;
    }
    private void initUI(){
        edtUserPhone    = mView.findViewById(R.id.EditText_UserName);
        edtUserPassword = mView.findViewById(R.id.EditText_UserPassword);
        btnLogin        = mView.findViewById(R.id.button_login);
        cbSave          = mView.findViewById(R.id.checkbox_SaveUsrNamePass);
    }
    private void UserLogIn(String usrphone, String usrPass){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mData = firebaseDatabase.getReference("Users");
        mData.child(usrphone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getChildren();
                User user = snapshot.getValue(User.class);
                if (user == null){
                    Toast.makeText(mView.getContext(), "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (user.getUserName().equals(usrphone) && user.getUserPassword().equals(usrPass)) {
                        Intent intent = new Intent(mView.getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        SharePrerences(usrphone, usrPass);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("User_Object", user);
                        intent.putExtra("DuLieu", bundle);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(mView.getContext(), "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                    }
                }

                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void SharePrerences(String usrphone, String usrPass){
        if (cbSave.isChecked()){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("UserName", usrphone);
            editor.putString("UserPassword", usrPass);
            editor.putBoolean("Status", true);
            editor.apply();
        }
        else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("UserName");
            editor.remove("UserPassword");
            editor.remove("Status");
            editor.apply();
        }
    }
}
