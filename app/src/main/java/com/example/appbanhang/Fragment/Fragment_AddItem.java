package com.example.appbanhang.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.Adapter.PhoToAdapterAddItem;
import com.example.appbanhang.ListItemAddorEdit;
import com.example.appbanhang.Object.Item;
import com.example.appbanhang.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_AddItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_AddItem extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View mView;
    private RecyclerView rcvPhoto;
    private PhoToAdapterAddItem phoToAdapterAddItem;
    private ListItemAddorEdit listItemAddorEdit;
    private ArrayList<Uri> uriArrayList = new ArrayList<>();
    ProgressDialog progressDialog;

    private Button btnHuy, btnXacNhan, btnAddHinh;
    private EditText edtTenSP, edtTenThuongHieu, edtSSD, edtHDD, edtManHinh,
            edtRam, edtLoaiRam, edtBusRam, edtPin, edtSoCong, edtLoaiCong, edtCanNang,
            edtSoLuong, edtThongTin, edtHangCPU, edtTenCPU, edtThongSoCPU, edtGiaSanPham, edtCameraTrc, edtCameraSau;
    private TextView tvAnhDaChon;
    private TextView wrnTenSP, wrnTenThuongHieu, wrnBoNho, wrnManHinh, wrnGiaSP,
            wrnRam, wrnPin, wrnCong, wrnCanNang, wrnCameraTrc, wrnCameraSau,
            wrnSoLuong, wrnThongTin, wrnCpu, wrnAnh, tvGB, tvMHZ;
    String tenSP, tenThuongHieu, hangCPU, tenCPU, thongSoCPU, SSD,
            HDD, manHinh, tenRam, loaiRam, busRam, pin, soCong, cameraTrc, cameraSau,
            loaiCong, canNang, soLuong, thongTinThem, anhDaChon, loaiSp, giaSp, boNhoTrong, boNhoTrongPhone = null;
    String mUri;
    int i=0;

    ActivityResultLauncher activityResultLauncher, requestPermissionLauncher ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_AddItem() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_AddItem.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_AddItem newInstance(String param1, String param2) {
        Fragment_AddItem fragment = new Fragment_AddItem();
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
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment__add_item, container, false);
        initUI();


        listItemAddorEdit = (ListItemAddorEdit) getActivity();
        progressDialog = new ProgressDialog(mView.getContext());
        if (listItemAddorEdit.loaiSp.equals("Phone")){
            edtHDD.setVisibility(View.GONE);
            edtLoaiRam.setVisibility(View.GONE);
            edtBusRam.setVisibility(View.GONE);
            edtSSD.setHint("Nhập dung lượng bộ nhớ");
            tvGB.setVisibility(View.GONE);
            tvMHZ.setVisibility(View.GONE);
        }
        phoToAdapterAddItem = new PhoToAdapterAddItem(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mView.getContext(), 3, LinearLayoutManager.VERTICAL, false);
        rcvPhoto.setLayoutManager(gridLayoutManager);
        rcvPhoto.setFocusable(false);
        rcvPhoto.setAdapter(phoToAdapterAddItem);

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),result -> {
            requestPermission();
        });
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getData().getClipData() != null) {
                        uriArrayList.clear();
                        int count = result.getData().getClipData().getItemCount();
                        tvAnhDaChon.setText(count + "");
                        for (int i = 0; i < count; i++) {
                            Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                            uriArrayList.add(imageUri);
                        }
                        phoToAdapterAddItem.SetData(uriArrayList);


                    } else {
                        uriArrayList.clear();
                        tvAnhDaChon.setText(1 + "");
                        Uri imageUri = result.getData().getData();
                        uriArrayList.add(imageUri);
                        phoToAdapterAddItem.SetData(uriArrayList);
                    }
                }
        );

        btnAddHinh.setOnClickListener(view -> {
            requestPermission();
        });

        btnXacNhan.setOnClickListener(view -> {
            addNewItem();
        });

        btnHuy.setOnClickListener(view -> {
            listItemAddorEdit.RemoveFragmentAddLaptop();
        });

        return mView;
    }


    public void initUI (){
        edtTenSP         = mView.findViewById(R.id.editTextAddTenSP);
        edtTenThuongHieu = mView.findViewById(R.id.editTextAddTenThuongHieu);
        edtHangCPU       = mView.findViewById(R.id.editTextAddThuongHieuCPU);
        edtTenCPU        = mView.findViewById(R.id.editTextAddTenCPU);
        edtThongSoCPU    = mView.findViewById(R.id.editTextAddThongSoCPU);
        edtSSD           = mView.findViewById(R.id.editTextAddSSD);
        edtHDD           = mView.findViewById(R.id.editTextAddHDD);
        edtManHinh       = mView.findViewById(R.id.editTextAddThongSoManHinh);
        edtRam           = mView.findViewById(R.id.editTextAddRam);
        edtLoaiRam       = mView.findViewById(R.id.editTextAddLoaiRam);
        edtBusRam        = mView.findViewById(R.id.editTextAddBusRam);
        edtPin           = mView.findViewById(R.id.editTextAddPin);
        edtSoCong        = mView.findViewById(R.id.editTextAddSoCong);
        edtLoaiCong      = mView.findViewById(R.id.editTextAddLoaiCong);
        edtCanNang       = mView.findViewById(R.id.editTextAddCanNang);
        edtSoLuong       = mView.findViewById(R.id.editTextAddSoLuong);
        edtThongTin      = mView.findViewById(R.id.editTextAddThongTinThem);
        edtCameraTrc     = mView.findViewById(R.id.editTextAddCameraTruoc);
        edtCameraSau     = mView.findViewById(R.id.editTextAddCameraSau);
        edtGiaSanPham    = mView.findViewById(R.id.editTextAddGiaSanPham);

        tvGB             = mView.findViewById(R.id.add_item_GB);
        tvMHZ            = mView.findViewById(R.id.add_item_MHZ);
        tvAnhDaChon      = mView.findViewById(R.id.textViewAddSoAnhDaChon);

        wrnAnh           = mView.findViewById(R.id.warning_ChonAnhAdd);
        wrnCanNang       = mView.findViewById(R.id.warning_CanNangAdd);
        wrnCpu           = mView.findViewById(R.id.warning_thongTinCPUAdd);
        wrnManHinh       = mView.findViewById(R.id.warning_thongSoManHinhAdd);
        wrnBoNho         = mView.findViewById(R.id.warning_thongTinBoNhoAdd);
        wrnCong          = mView.findViewById(R.id.warning_thongTinCongAdd);
        wrnPin           = mView.findViewById(R.id.warning_thongSoPinAdd);
        wrnRam           = mView.findViewById(R.id.warning_thongSoRamAdd);
        wrnSoLuong       = mView.findViewById(R.id.warning_soLuongAdd);
        wrnTenSP         = mView.findViewById(R.id.warning_tenSpAdd);
        wrnTenThuongHieu = mView.findViewById(R.id.warning_tenThuongHieuAdd);
        wrnThongTin      = mView.findViewById(R.id.warning_thongTinThemAdd);
        wrnGiaSP         = mView.findViewById(R.id.warning_loaiSpAdd);
        wrnCameraTrc     = mView.findViewById(R.id.warning_CameraTruocAdd);
        wrnCameraSau     = mView.findViewById(R.id.warning_CameraSauAdd);

        btnHuy           = mView.findViewById(R.id.buttonAddHuy);
        btnAddHinh       = mView.findViewById(R.id.buttonAddHinhAnh);
        btnXacNhan       = mView.findViewById(R.id.buttonAddXacNhan);
        rcvPhoto         = mView.findViewById(R.id.recycleView_AnhDaChonAdd);
    }
    private void getUserInput(){
        tenSP         = edtTenSP.getText().toString().trim();
        tenThuongHieu = edtTenThuongHieu.getText().toString().trim();
        hangCPU       = edtHangCPU.getText().toString().trim();
        tenCPU        = edtTenCPU.getText().toString().trim();
        thongSoCPU    = edtThongSoCPU.getText().toString().trim();
        SSD           = edtSSD.getText().toString().trim();
        HDD           = edtHDD.getText().toString().trim();
        manHinh       = edtManHinh.getText().toString().trim();
        tenRam        = edtRam.getText().toString().trim();
        loaiRam       = edtLoaiRam.getText().toString().trim();
        busRam        = edtBusRam.getText().toString().trim();
        pin           = edtPin.getText().toString().trim();
        soCong        = edtSoCong.getText().toString().trim();
        loaiCong      = edtLoaiCong.getText().toString().trim();
        canNang       = edtCanNang.getText().toString().trim();
        soLuong       = edtSoLuong.getText().toString().trim();
        thongTinThem  = edtThongTin.getText().toString().trim();
        anhDaChon     = tvAnhDaChon.getText().toString().trim();
        giaSp         = edtGiaSanPham.getText().toString().trim();
        cameraTrc     = edtCameraTrc.getText().toString().trim();
        cameraSau     = edtCameraSau.getText().toString().trim();
        boNhoTrong    = SSD+","+HDD;
        boNhoTrongPhone = SSD;
    }
    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(mView.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(intent);
        }

    }
    private void addNewItem(){
        getUserInput();
        if (listItemAddorEdit.loaiSp.equals("Laptop") && !(tenSP.isEmpty() || tenThuongHieu.isEmpty() || hangCPU.isEmpty() || tenCPU.isEmpty() || thongSoCPU.isEmpty()
                || SSD.isEmpty() || HDD.isEmpty() || manHinh.isEmpty() || tenRam.isEmpty() || busRam.isEmpty() || loaiRam.isEmpty()
                || pin.isEmpty() || soCong.isEmpty() || loaiCong.isEmpty() || canNang.isEmpty() || soLuong.isEmpty() || thongTinThem.isEmpty()
                || anhDaChon.equals("0") || giaSp.isEmpty() || cameraTrc.isEmpty() || cameraSau.isEmpty())){
            progressDialog.show();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mdata = firebaseDatabase.getReference();
            String key = mdata.child("Item").child("Laptop").push().getKey();
            for (i=0; i< uriArrayList.size(); i++){
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference(key);
                StorageReference fileRef = storageRef.child(i+".png");
                fileRef.putFile(uriArrayList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                mUri = uri.toString();
                                if (uriArrayList.size() == i){
                                    Item item = new Item("Laptop", tenSP, key, tenThuongHieu, hangCPU+","+tenCPU+","+thongSoCPU, boNhoTrong, manHinh,
                                            tenRam+","+loaiRam+","+busRam,pin, soCong+","+ loaiCong, thongTinThem, soLuong, 0, cameraTrc, cameraSau, mUri, Integer.parseInt(giaSp), Integer.parseInt(anhDaChon), Float.parseFloat(canNang));


                                    mdata.child("Item").child("Laptop").child(key).setValue(item, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            progressDialog.dismiss();
                                            Toast.makeText(listItemAddorEdit, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    listItemAddorEdit.RemoveFragmentAddLaptop();
                                }
                            }
                        });

                    }
                });
            }

        }
        else if (listItemAddorEdit.loaiSp.equals("Phone") &&!(tenSP.isEmpty() || tenThuongHieu.isEmpty() || hangCPU.isEmpty() || tenCPU.isEmpty() || thongSoCPU.isEmpty()
                || SSD.isEmpty() || manHinh.isEmpty() || tenRam.isEmpty()
                || pin.isEmpty() || soCong.isEmpty() || loaiCong.isEmpty() || canNang.isEmpty() || soLuong.isEmpty() || thongTinThem.isEmpty()
                || anhDaChon.equals("0") || giaSp.isEmpty() || cameraTrc.isEmpty() || cameraSau.isEmpty())){
            progressDialog.show();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mdata = firebaseDatabase.getReference();
            String key = mdata.child("Item").child("Phone").push().getKey();
            for (i = 0; i< uriArrayList.size(); i++){
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference(key);
                StorageReference fileRef = storageRef.child(i+".png");
                fileRef.putFile(uriArrayList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                mUri = uri.toString();
                                if (uriArrayList.size() == i){
                                    Item item = new Item("Phone", tenSP, key, tenThuongHieu, hangCPU+","+tenCPU+","+thongSoCPU, boNhoTrongPhone, manHinh,
                                            tenRam,pin, soCong+","+ loaiCong, thongTinThem, soLuong, 0, cameraTrc, cameraSau, mUri,Integer.parseInt(giaSp), Integer.parseInt(anhDaChon), Float.parseFloat(canNang));


                                    mdata.child("Item").child("Phone").child(key).setValue(item, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            progressDialog.dismiss();
                                            Toast.makeText(listItemAddorEdit, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    listItemAddorEdit.RemoveFragmentAddLaptop();
                                }
                            }
                        });

                    }
                });
            }

        }
        else {
            warningUserInputEmpty();
        }
    }


    private void warningUserInputEmpty(){
        if (tenSP.isEmpty()) {
            wrnTenSP.setVisibility(View.VISIBLE);
        } else {
            wrnTenSP.setVisibility(View.GONE);
        }
        if (tenThuongHieu.isEmpty()) {
            wrnTenThuongHieu.setVisibility(View.VISIBLE);
        } else {
            wrnTenThuongHieu.setVisibility(View.GONE);
        }

        if (hangCPU.isEmpty() || tenCPU.isEmpty() || thongSoCPU.isEmpty()) {
            wrnCpu.setVisibility(View.VISIBLE);
        } else {
            wrnCpu.setVisibility(View.GONE);
        }

        if (listItemAddorEdit.loaiSp.equals("Laptop") && (SSD.isEmpty() || HDD.isEmpty())) {
            wrnBoNho.setVisibility(View.VISIBLE);
        }
        else if (listItemAddorEdit.loaiSp.equals("Phone") && SSD.isEmpty()){
            wrnBoNho.setVisibility(View.VISIBLE);
        }
        else {
            wrnBoNho.setVisibility(View.GONE);
        }

        if (manHinh.isEmpty()) {
            wrnManHinh.setVisibility(View.VISIBLE);
        } else {
            wrnManHinh.setVisibility(View.GONE);
        }

        if (listItemAddorEdit.loaiSp.equals("Laptop") &&(tenRam.isEmpty() || busRam.isEmpty() || loaiRam.isEmpty())) {
            wrnRam.setVisibility(View.VISIBLE);
        }
        else if (listItemAddorEdit.loaiSp.equals("Phone") && tenRam.isEmpty()){
            wrnRam.setVisibility(View.VISIBLE);
        }
        else {
            wrnRam.setVisibility(View.GONE);
        }

        if (pin.isEmpty()) {
            wrnPin.setVisibility(View.VISIBLE);
        } else {
            wrnPin.setVisibility(View.GONE);
        }

        if (soCong.isEmpty() || loaiCong.isEmpty()) {
            wrnCong.setVisibility(View.VISIBLE);
        } else {
            wrnCong.setVisibility(View.GONE);
        }

        if (canNang.isEmpty()) {
            wrnCanNang.setVisibility(View.VISIBLE);
        } else {
            wrnCanNang.setVisibility(View.GONE);
        }

        if (soLuong.isEmpty()) {
            wrnSoLuong.setVisibility(View.VISIBLE);
        } else {
            wrnSoLuong.setVisibility(View.GONE);
        }

        if (thongTinThem.isEmpty()) {
            wrnThongTin.setVisibility(View.VISIBLE);
        } else {
            wrnThongTin.setVisibility(View.GONE);
        }
        if (anhDaChon.equals("0")) {
            wrnAnh.setVisibility(View.VISIBLE);
        } else {
            wrnAnh.setVisibility(View.GONE);
        }
        if (giaSp.isEmpty()){
            wrnGiaSP.setVisibility(View.VISIBLE);
        }else {
            wrnGiaSP.setVisibility(View.GONE);
        }
        if (cameraTrc.isEmpty()){
            wrnCameraTrc.setVisibility(View.VISIBLE);
        }else {
            wrnCameraTrc.setVisibility(View.GONE);
        }
        if (cameraSau.isEmpty()){
            wrnCameraSau.setVisibility(View.VISIBLE);
        }else {
            wrnCameraSau.setVisibility(View.GONE);
        }

    }

}