package com.example.appbanhang;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.appbanhang.Object.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AccountSetting extends AppCompatActivity {

    TextView tvHoTen, tvSDT, tvMatKhau, tvGoiTinh, tvNgaySinh,
            tvEmail, tvDiaChi, tvToolbar;
    ImageView imgToolbar, imgHinhUser, imgExit, imgBackground;
    Button btnDangXuat;
    User user;
    DatePickerDialog.OnDateSetListener dateSetListener;
    ProgressDialog progressDialog;
    String CheckOnClickimage, urlphoto, urlback = null;
    Uri imgUri = null;
    Uri imgUriBack = null;
    String CheckOnClick, Gender = null;
    ActivityResultLauncher activityResultLauncher, requestPermissionOpenGallery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        initUI();
        Intent nhandl = getIntent();
        requestPermissionOpenGallery = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> requestPermissionsAndOpenGallery());
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (CheckOnClickimage.equals("IMG_USER")){
                    imgUri = result;
                    imgHinhUser.setImageURI(imgUri);
                }else {
                    imgUriBack = result;
                    imgBackground.setImageURI(imgUriBack);
                }

            }
        });
        Bundle bundle = nhandl.getBundleExtra("Dulieu");
        if (bundle != null){
            user = (User) bundle.getSerializable("User_Information");
            LoadUserInformation();
        }
        imgToolbar.setImageResource(R.drawable.ic_baseline_save_24);
        tvToolbar.setText("Thiết lập tài khoản");
        btnDangXuat.setOnClickListener(view -> {
            Intent intent = new Intent(AccountSetting.this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            FirebaseAuth.getInstance().signOut();
            startActivity(intent);
        });
        imgHinhUser.setOnClickListener(view -> {
            CheckOnClickimage = "IMG_USER";
            activityResultLauncher.launch("image/*");

        });
        imgBackground.setOnClickListener(view -> {
            CheckOnClickimage = "IMG_BACKUSER";
            activityResultLauncher.launch("image/*");

        });

        imgExit.setOnClickListener(view -> super.onBackPressed());
        tvHoTen.setOnClickListener(view -> {
            CheckOnClick = "HOTEN";
            DialogChangeInformation();
        });
        tvSDT.setOnClickListener(view -> {
            CheckOnClick = "SDT";
            DialogChangeInformation();
        });
        tvMatKhau.setOnClickListener(view -> {
            CheckOnClick = "MATKHAU";
            DialogChangeInformation();
        });
        tvGoiTinh.setOnClickListener(view -> {
            changeGender();
        });
        tvNgaySinh.setOnClickListener(view -> {
            dateSetListener = (datePicker, i, i1, i2) -> {
                i1 = i1 +1;
                String date = i2 + "/" + i1 + "/" + i;
                tvNgaySinh.setText(date);
            };
            getDateTime();
        });
        tvEmail.setOnClickListener(view -> {
            CheckOnClick = "EMAIL";
            DialogChangeInformation();
        });
        tvDiaChi.setOnClickListener(view -> {
            CheckOnClick = "DIACHI";
            DialogChangeInformation();
        });
        imgToolbar.setOnClickListener(view -> {
            updateUser();
        });
    }
    private void initUI(){
        tvHoTen     = findViewById(R.id.acst_tv_userName);
        tvSDT       = findViewById(R.id.acst_tv_userPhone);
        tvMatKhau   = findViewById(R.id.acst_tv_userPassWord);
        tvGoiTinh   = findViewById(R.id.acst_tv_userGioiTinh);
        tvNgaySinh  = findViewById(R.id.acst_tv_UserDate);
        tvEmail     = findViewById(R.id.acst_tv_userEmail);
        tvDiaChi    = findViewById(R.id.acst_tv_userAddress);
        tvToolbar   = findViewById(R.id.textView_add_warranty);

        imgToolbar  = findViewById(R.id.Add_Warranty);
        imgHinhUser = findViewById(R.id.acst_img_user);
        imgExit     = findViewById(R.id.Add_Warranty_Exit);

        imgBackground   =   findViewById(R.id.acst_background_user);
        btnDangXuat     = findViewById(R.id.acst_btn_logOut);
    }
    public void LoadUserInformation(){
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        if (!user.getUserPhoto().equals("No Input")){
            Glide.with(getApplicationContext()).load(Uri.parse(user.getUserPhoto())).into(imgHinhUser);
        }
        if (!user.getUserBackground().equals("No Input")){
            Glide.with(getApplicationContext()).load(Uri.parse(user.getUserBackground())).into(imgBackground);
        }
        if (user.getUserFullName().equals("No Input")){
            tvHoTen.setText("Chưa nhập họ tên");
        }
        else {
            tvHoTen.setText(user.getUserFullName());
        }
        if (user.getUserPhoneNumber().equals("No Input")){
            tvSDT.setText("Chưa nhập SĐT");
        }
        else {
            tvSDT.setText(user.getUserPhoneNumber());
        }
        if (user.getUserGender().equals("No Input")){
            tvGoiTinh.setText("Chưa nhập giới tính");
        }
        else {
            tvGoiTinh.setText(user.getUserGender());
        }
        if (user.getUserDate().equals("No Input")){
            tvNgaySinh.setText("Chưa nhập ngày sinh");
        }
        else {
            tvNgaySinh.setText(user.getUserDate());
        }
        tvEmail.setText(user.getUserEmail());
        if (user.getUserAddress().equals("No Input")){
            tvDiaChi.setText("Chưa nhập địa chỉ");
        }
        else {
            tvDiaChi.setText(user.getUserAddress());
        }
        progressDialog.dismiss();
    }
    private void DialogChangeInformation(){
        View view = getLayoutInflater().inflate(R.layout.dialog_change_information, null);
        Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.setContentView(view);
        TextView tvToolbar   = dialog.findViewById(R.id.textView_add_warranty);
        ImageView imgToolbar  = dialog.findViewById(R.id.Add_Warranty);
        ImageView imgExit     = dialog.findViewById(R.id.Add_Warranty_Exit);
        EditText edt1 = dialog.findViewById(R.id.textView_changeI_1);
        EditText edt2 = dialog.findViewById(R.id.textView_changeI_2);
        EditText edt3 = dialog.findViewById(R.id.textView_changeI_3);
        TextView wrn1 = dialog.findViewById(R.id.wrn_textView_changeI_1);
        TextView wrn2 = dialog.findViewById(R.id.wrn_textView_changeI_2);
        TextView wrn3 = dialog.findViewById(R.id.wrn_textView_changeI_3);
        tvToolbar.setText("Chỉnh sửa thông tin");
        imgToolbar.setImageResource(R.drawable.ic_baseline_save_24);
        imgExit.setOnClickListener(view1 -> dialog.dismiss());
        if (!CheckOnClick.isEmpty() && CheckOnClick.equals("HOTEN")){
            edt2.setVisibility(View.GONE);
            edt3.setVisibility(View.GONE);
            edt1.setText(tvHoTen.getText());
        }
        else if (!CheckOnClick.isEmpty() && CheckOnClick.equals("SDT")){
            edt2.setVisibility(View.GONE);
            edt3.setVisibility(View.GONE);
            edt1.setText(tvSDT.getText());
        }
        else if (!CheckOnClick.isEmpty() && CheckOnClick.equals("MATKHAU")){
            edt1.setHint("Nhập mật khẩu hiện tại");
            edt2.setHint("Nhập mật khẩu mới");
            edt3.setHint("Nhập lại mật khẩu mới");
        }
        else if (!CheckOnClick.isEmpty() && CheckOnClick.equals("EMAIL")){
            edt2.setVisibility(View.GONE);
            edt3.setVisibility(View.GONE);
            edt1.setText(tvEmail.getText());
        }
        else if (!CheckOnClick.isEmpty() && CheckOnClick.equals("DIACHI")){
            edt2.setVisibility(View.GONE);
            edt3.setVisibility(View.GONE);
            edt1.setText(tvDiaChi.getText());
        }
        imgToolbar.setOnClickListener(view2 -> {
            if (!CheckOnClick.isEmpty() && CheckOnClick.equals("HOTEN")){
                String hoten = edt1.getText().toString().trim();
                tvHoTen.setText(hoten);
                dialog.dismiss();
            }
            else if (!CheckOnClick.isEmpty() && CheckOnClick.equals("SDT")){
                String sdt = edt1.getText().toString().trim();
                tvSDT.setText(sdt);
                dialog.dismiss();
            }
            else if (!CheckOnClick.isEmpty() && CheckOnClick.equals("MATKHAU")){
                String mkcu   = edt1.getText().toString().trim();
                String mkmoi  = edt2.getText().toString().trim();
                String mkmoi2 = edt3.getText().toString().trim();
                if (mkcu.equals(user.getUserPassword()) && mkmoi.equals(mkmoi2)){
                    wrn1.setVisibility(View.GONE);
                    wrn2.setVisibility(View.GONE);
                    wrn3.setVisibility(View.GONE);
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference mdata = firebaseDatabase.getReference("Users");
                    mdata.child(user.getUserName()).child("userPassword").setValue(mkmoi);
                    Toast.makeText(this, "Đã thay đổi mật khẩu", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else if (mkcu.equals(user.getUserPassword()) && !mkmoi.equals(mkmoi2)){
                    wrn1.setVisibility(View.GONE);
                    wrn2.setVisibility(View.VISIBLE);
                    wrn3.setVisibility(View.VISIBLE);
                }
                else if (!mkcu.equals(user.getUserPassword()) && !mkmoi.equals(mkmoi2)){
                    wrn1.setVisibility(View.VISIBLE);
                    wrn2.setVisibility(View.VISIBLE);
                    wrn3.setVisibility(View.VISIBLE);
                }
                else {
                    wrn1.setVisibility(View.VISIBLE);
                    wrn2.setVisibility(View.GONE);
                    wrn3.setVisibility(View.GONE);
                }
            }
            else if (!CheckOnClick.isEmpty() && CheckOnClick.equals("EMAIL")){
                String email = edt1.getText().toString().trim();
                tvEmail.setText(email);
                dialog.dismiss();
            }
            else if (!CheckOnClick.isEmpty() && CheckOnClick.equals("DIACHI")){
                String diachi = edt1.getText().toString().trim();
                tvDiaChi.setText(diachi);
                dialog.dismiss();
            }

        });
        dialog.show();
    }
    private void getDateTime(){
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog2 = new DatePickerDialog(this
                , android.R.style.Theme_Holo_Light_Dialog_MinWidth
                , dateSetListener, year-18, month, day);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog2.show();
    }
    private void changeGender(){
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_change_gender);
        RadioGroup rdg = dialog.findViewById(R.id.rdgroup_changeGender);
        Button  btnLuu = dialog.findViewById(R.id.button_LuuChangeGender);
        Button  btnHuy = dialog.findViewById(R.id.button_HuyChangeGender);
        rdg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rdbtn_changeGender1:
                        Gender = "Nam";
                        break;
                    case R.id.rdbtn_changeGender2:
                        Gender = "Nữ";
                        break;
                    case R.id.rdbtn_changeGender3:
                        Gender = "Khác";
                        break;
                    default:
                        Gender = "Chưa nhập giới tính";
                }
            }
        });

        btnLuu.setOnClickListener(view -> {
            tvGoiTinh.setText(Gender);
            dialog.dismiss();
        });
        btnHuy.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }
    private void updateUser(){
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata = firebaseDatabase.getReference("Users");
        String hoten = tvHoTen.getText().toString().trim();
        String sdt = tvSDT.getText().toString().trim();
        String gioitinh = tvGoiTinh.getText().toString().trim();
        String ngaysinh = tvNgaySinh.getText().toString().trim();
        String email = tvEmail.getText().toString().trim();
        String diachi = tvDiaChi.getText().toString().trim();
        user.setUserFullName(hoten);
        user.setUserPhoneNumber(sdt);
        user.setUserGender(gioitinh);
        user.setUserDate(ngaysinh);
        user.setUserEmail(email);
        user.setUserAddress(diachi);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference(user.getUserName());
        StorageReference fileback = storageRef.child("UserPhoto").child(0+".png");
        StorageReference fileuserphoto = storageRef.child("UserPhoto").child(1+".png");
        if (imgUriBack!=null && imgUri!=null) {
            fileback.putFile(imgUriBack).addOnSuccessListener(taskSnapshot -> fileback.getDownloadUrl().addOnSuccessListener(uri -> {
                user.setUserBackground(uri.toString());
                fileuserphoto.putFile(imgUri).addOnSuccessListener(taskSnapshot1 -> fileuserphoto.getDownloadUrl().addOnSuccessListener(uri1 -> {
                    user.setUserPhoto(uri1.toString());
                    mdata.child(user.getUserName()).setValue(user);
                    Toast.makeText(AccountSetting.this, "Đã thay đổi thông tin", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }));
            }));
        }else if (imgUriBack!=null && imgUri==null){
            fileback.putFile(imgUriBack).addOnSuccessListener(taskSnapshot -> fileback.getDownloadUrl().addOnSuccessListener(uri -> {
                user.setUserBackground(uri.toString());
                mdata.child(user.getUserName()).setValue(user);
                Toast.makeText(AccountSetting.this, "Đã thay đổi thông tin", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }));
        }else if (imgUriBack==null && imgUri!=null){
            fileuserphoto.putFile(imgUri).addOnSuccessListener(taskSnapshot1 -> fileuserphoto.getDownloadUrl().addOnSuccessListener(uri1 -> {
                user.setUserPhoto(uri1.toString());
                mdata.child(user.getUserName()).setValue(user);
                Toast.makeText(AccountSetting.this, "Đã thay đổi thông tin", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }));
        }else {
            mdata.child(user.getUserName()).setValue(user);
            Toast.makeText(AccountSetting.this, "Đã thay đổi thông tin", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }


    }
    public void requestPermissionsAndOpenGallery(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissionOpenGallery.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }
}