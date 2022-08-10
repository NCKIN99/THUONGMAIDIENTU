package com.example.appbanhang.Fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.appbanhang.ListWarrantyActivity;
import com.example.appbanhang.Object.Accessory;
import com.example.appbanhang.Object.Item;
import com.example.appbanhang.Object.Promotion;
import com.example.appbanhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Add_Warranty#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Add_Warranty extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Button btnAddWarranty, btnHuy, btnChonAnh;
    private ImageView imgHinh, imgChonSpKM;
    private RadioGroup rdg, rdgLoaiSp;
    public EditText edtTenKM, edtKM1, edtKM2,
            edtKM3, edtKM4, edtKM5;
    public AutoCompleteTextView actSpKM;
    private RadioButton rdbLap, rdbPhone, rdbAccessory;
    public TextView tv_WrnTenKM, tv_WrnNgatBDKM, tv_WrnNgayKTKM, tvSpKM,
            tv_WrnKieuGoi, edtNgayBDKM, edtNgayKTKM, tv_WrnHinhanh, tv_WrnNhapKM, tv_WrnLoaiSpKM, tv_WrnSpKM;
    private View mView;
    DatePickerDialog.OnDateSetListener dateSetListener;
    private ProgressDialog progressDialog;
    ListWarrantyActivity listWarrantyActivity;
    private static final String[] LoaiSp = new String[]{"Laptop", "Phone", "Accessory"};
    ActivityResultLauncher requestPermissionOpenGallery, activityResultLauncher;
    public String LoaiGoiKM = null;
    Uri imgUri = null;
    ArrayAdapter<String> adapter1;
    public String TenGoiKM, NgayBDKM, NgayKTKM, SpKm, KM1, KM2, KM3, KM4, KM5 = null;
    public String LoaiSpKM = "a";
    int i=0;
    public Fragment_Add_Warranty() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Add_Warranty.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Add_Warranty newInstance(String param1, String param2) {
        Fragment_Add_Warranty fragment = new Fragment_Add_Warranty();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment__add__warranty, container, false);
        // Inflate the layout for this fragment
        initUI();
        edtKM1.setVisibility(View.GONE);
        edtKM2.setVisibility(View.GONE);
        edtKM3.setVisibility(View.GONE);
        edtKM4.setVisibility(View.GONE);
        edtKM5.setVisibility(View.GONE);
        requestPermissionOpenGallery = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> requestPermissionsAndOpenGallery());
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getData() != null) {
                        imgUri = result.getData().getData();
                        imgHinh.setImageURI(imgUri);
                    }
                });
        listWarrantyActivity = (ListWarrantyActivity) getActivity();
        btnHuy.setOnClickListener(view -> listWarrantyActivity.RemoveFragmentAddWarranty());
        rdgLoaiSp.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.add_Promotion_Laptop){
                LoaiSpKM = "Laptop";
                adapter1 = new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_dropdown_item_1line, loadSpKM("Laptop"));
                actSpKM.setAdapter(adapter1);
                rdbPhone.setClickable(false);
                rdbAccessory.setClickable(false);
            }else if (i == R.id.add_Promotion_Phone){
                LoaiSpKM = "Phone";
                adapter1 = new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_dropdown_item_1line, loadSpKM("Phone"));
                actSpKM.setAdapter(adapter1);
                rdbLap.setClickable(false);
                rdbAccessory.setClickable(false);
            }else {
                adapter1 = new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_dropdown_item_1line, loadSpKM("Accessory"));
                actSpKM.setAdapter(adapter1);
                LoaiSpKM = "Accessory";
                rdbPhone.setClickable(false);
                rdbLap.setClickable(false);
            }
        });
        rdg.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i){
                case R.id.radio_GoiGiamGia:
                    LoaiGoiKM = "GiamGia";
                    edtKM1.setVisibility(View.VISIBLE);
                    edtKM2.setVisibility(View.GONE);
                    edtKM3.setVisibility(View.GONE);
                    edtKM4.setVisibility(View.GONE);
                    edtKM5.setVisibility(View.GONE);
                    break;
                case R.id.radio_GoiTangKem:
                    LoaiGoiKM = "KhuyenMai";
                    edtKM1.setVisibility(View.VISIBLE);
                    edtKM2.setVisibility(View.VISIBLE);
                    edtKM3.setVisibility(View.VISIBLE);
                    edtKM4.setVisibility(View.VISIBLE);
                    edtKM5.setVisibility(View.VISIBLE);
                    break;
            }
        });

        actSpKM.setOnClickListener(view ->
            actSpKM.showDropDown());
        imgChonSpKM.setOnClickListener(view -> {
            if (actSpKM.getText().toString().trim().equals("")){
                Toast.makeText(listWarrantyActivity, "Nhập id sản phẩm", Toast.LENGTH_SHORT).show();
            }else {
                i++;
                tvSpKM.append(actSpKM.getText().toString() + "\n");
                actSpKM.setText("");
            }
        });

        edtNgayBDKM.setOnClickListener(view -> {
            dateSetListener = (datePicker, i, i1, i2) -> {
                i1 = i1 +1;
                String date = i2 + "/" + i1 + "/" + i;
                edtNgayBDKM.setText(date);
            };
            getDateTime();});
        edtNgayKTKM.setOnClickListener(view -> {
            dateSetListener = (datePicker, i, i1, i2) -> {
                i1 = i1 +1;
                String date = i2 + "/" + i1 + "/" + i;
                edtNgayKTKM.setText(date);
            };
            getDateTime();});
        btnChonAnh.setOnClickListener(view -> {
            requestPermissionsAndOpenGallery();
        });
        btnAddWarranty.setOnClickListener(view -> {
            if (checkUserInput()){
                upLoadNewWarranty();
            }else {
                warningUserInput();
            }
        });
        return mView;
    }
    public void initUI(){
        btnAddWarranty      = mView.findViewById(R.id.button_XacNhanAddKM);
        btnHuy              = mView.findViewById(R.id.button_HuyAddKM);
        btnChonAnh          = mView.findViewById(R.id.button_ChonAnhWarranty);
        rdg                 = mView.findViewById(R.id.radioGroup_kieuGoiKM);
        rdgLoaiSp           = mView.findViewById(R.id.add_Promotion_RadioGroup);
        edtTenKM            = mView.findViewById(R.id.editTextAddTenKhuyenMai);
        edtNgayBDKM         = mView.findViewById(R.id.editText_NgayBatDauKM);
        edtNgayKTKM         = mView.findViewById(R.id.editText_NgayKetThucKM);
        actSpKM             = mView.findViewById(R.id.editTextAddSpKM);
        edtKM1              = mView.findViewById(R.id.EditText_khuyenMai1);
        edtKM2              = mView.findViewById(R.id.EditText_khuyenMai2);
        edtKM3              = mView.findViewById(R.id.EditText_khuyenMai3);
        edtKM4              = mView.findViewById(R.id.EditText_khuyenMai4);
        edtKM5              = mView.findViewById(R.id.EditText_khuyenMai5);
        tv_WrnTenKM         = mView.findViewById(R.id.warning_AddTenKhuyenMai);
        tv_WrnNgatBDKM      = mView.findViewById(R.id.warning_NgayBatDauKM);
        tv_WrnNgayKTKM      = mView.findViewById(R.id.warning_NgayKetThucKM);
        tv_WrnKieuGoi       = mView.findViewById(R.id.warning_AddKieuGoi);
        tv_WrnHinhanh       = mView.findViewById(R.id.warning_ChonAnhKM);
        tv_WrnNhapKM        = mView.findViewById(R.id.warning_nhapKhuyenMai);
        tv_WrnLoaiSpKM      = mView.findViewById(R.id.warning_AddLoaiSpKhuyenMai);
        tv_WrnSpKM          = mView.findViewById(R.id.warning_SpKM);
        tvSpKM              = mView.findViewById(R.id.textView_SpKM);
        imgHinh             = mView.findViewById(R.id.ImageView_hinhAnhWarranty);
        imgChonSpKM         = mView.findViewById(R.id.ImageChonSPKM);
        rdbPhone            = mView.findViewById(R.id.add_Promotion_Phone);
        rdbLap              = mView.findViewById(R.id.add_Promotion_Laptop);
        rdbAccessory        = mView.findViewById(R.id.add_Promotion_Accessory);
    }
    public void requestPermissionsAndOpenGallery(){
        if (ContextCompat.checkSelfPermission(mView.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissionOpenGallery.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            activityResultLauncher.launch(intent);
        }
    }
    private ArrayList<String> loadSpKM(String loaiSp){
        ArrayList<String> mString = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata = firebaseDatabase.getReference("Item");
        mdata.child(loaiSp).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mString.clear();
                if (loaiSp.equals("Accessory")){
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Accessory accessory = ds.getValue(Accessory.class);
                        mString.add(accessory.getKeyID());
                    }
                }else {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Item item = ds.getValue(Item.class);
                        mString.add(item.getKeyId());
                    }
                }
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return mString;
    }
    private void getDateTime(){
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(mView.getContext()
                , android.R.style.Theme_Holo_Light_Dialog_MinWidth
                , dateSetListener, year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    private void getUserInput(){
        TenGoiKM    = edtTenKM.getText().toString().trim();
        NgayBDKM    = edtNgayBDKM.getText().toString().trim();
        NgayKTKM    = edtNgayKTKM.getText().toString().trim();
        SpKm        = tvSpKM.getText().toString().trim();
        KM1         = edtKM1.getText().toString().trim();
        KM2         = edtKM2.getText().toString().trim();
        KM3         = edtKM3.getText().toString().trim();
        KM4         = edtKM4.getText().toString().trim();
        KM5         = edtKM5.getText().toString().trim();

    }
    public Boolean checkUserInput(){
        getUserInput();
        if (TenGoiKM.isEmpty() || LoaiSpKM.isEmpty() || !(LoaiSpKM.equals("Laptop") || LoaiSpKM.equals("Phone")  || LoaiSpKM.equals("Accessory") )
            || NgayBDKM.isEmpty() || NgayKTKM.isEmpty() || LoaiGoiKM == null || imgUri == null || KM1.isEmpty() && KM2.isEmpty() && KM3.isEmpty() &&
                KM4.isEmpty() && KM5.isEmpty() || SpKm.equals("")){
            return false;
        }
        return true;
    }
    public void warningUserInput(){
        getUserInput();
        if (SpKm.equals("")){
            tv_WrnSpKM.setVisibility(View.VISIBLE);
        }
        else {
            tv_WrnSpKM.setVisibility(View.GONE);
        }
        if (TenGoiKM.isEmpty()){
            tv_WrnTenKM.setVisibility(View.VISIBLE);
        }
        else {
            tv_WrnTenKM.setVisibility(View.GONE);
        }
        if (LoaiSpKM.isEmpty() || !(LoaiSpKM.equals("Laptop") || LoaiSpKM.equals("Phone")  || LoaiSpKM.equals("Accessory") )){
            tv_WrnLoaiSpKM.setVisibility(View.VISIBLE);
        }
        else {
            tv_WrnLoaiSpKM.setVisibility(View.GONE);
        }
        if (NgayBDKM.isEmpty()){
            tv_WrnNgatBDKM.setVisibility(View.VISIBLE);
        }
        else {
            tv_WrnNgatBDKM.setVisibility(View.GONE);
        }
        if (NgayKTKM.isEmpty()){
            tv_WrnNgayKTKM.setVisibility(View.VISIBLE);
        }
        else {
            tv_WrnNgayKTKM.setVisibility(View.GONE);
        }
        if (LoaiGoiKM == null){
            tv_WrnKieuGoi.setVisibility(View.VISIBLE);
        }
        else {
            tv_WrnKieuGoi.setVisibility(View.GONE);
        }
        if (imgUri == null){
            tv_WrnHinhanh.setVisibility(View.VISIBLE);
        }
        else {
            tv_WrnHinhanh.setVisibility(View.GONE);
        }
        if (KM1.isEmpty() && KM2.isEmpty() && KM3.isEmpty() && KM4.isEmpty() && KM5.isEmpty()){
            tv_WrnNhapKM.setVisibility(View.VISIBLE);
        }
        else {
            tv_WrnNhapKM.setVisibility(View.GONE);
        }
    }
    public void upLoadNewWarranty(){
        getUserInput();
        if (KM1.isEmpty()){
            KM1 = "Ko";
        }
        if (KM2.isEmpty()){
            KM2 = "Ko";
        }
        if (KM3.isEmpty()){
            KM3 = "Ko";
        }
        if (KM4.isEmpty()){
            KM4 = "Ko";
        }
        if (KM5.isEmpty()){
            KM5 = "Ko";
        }
        progressDialog = new ProgressDialog(mView.getContext());
        progressDialog.setTitle("Đang tải......");
        progressDialog.show();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mData = firebaseDatabase.getReference();
        String mKey = mData.child("Promotional").push().getKey();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference(mKey);
        StorageReference fileRef = storageRef.child(0+".png");
        fileRef.putFile(imgUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Promotion promotion = new Promotion(mKey, TenGoiKM, LoaiSpKM, NgayBDKM, NgayKTKM,
                    LoaiGoiKM, KM1, KM2, KM3, KM4, KM5, uri.toString(), i+SpKm);
            mData.child("Promotional").child(mKey).setValue(promotion);
            progressDialog.dismiss();
            listWarrantyActivity.RemoveFragmentAddWarranty();
        }));

    }
}