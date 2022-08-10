package com.example.appbanhang;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.Adapter.PhoToAdapterAddItem;
import com.example.appbanhang.Object.Accessory;
import com.example.appbanhang.Object.Item;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class Edit_Item_Activity extends AppCompatActivity {

    public Button btnHuy, btnXacNhan, btnEditHinh;
    public EditText edtTenSP, edtTenThuongHieu, edtSSD, edtHDD, edtManHinh,
            edtRam, edtLoaiRam, edtBusRam, edtPin, edtSoCong, edtLoaiCong, edtCanNang,
            edtSoLuong, edtThongTin, edtHangCPU, edtTenCPU, edtThongSoCPU, edtGiaSanPham, edtCameraTrc, edtCameraSau;
    public TextView tvAnhDaChon, tvGB, tvMHZ;
    public TextView wrnTenSP, wrnTenThuongHieu, wrnBoNho, wrnManHinh, wrnGiaSP,
            wrnRam, wrnPin, wrnCong, wrnCanNang, wrnCameraTrc, wrnCameraSau,
            wrnSoLuong, wrnThongTin, wrnCpu, wrnAnh;
    String mUri;
    int i =0;
    ProgressDialog progressDialog;
    String tenSP, tenThuongHieu, hangCPU, tenCPU, thongSoCPU, SSD,
            HDD, manHinh, tenRam, loaiRam, busRam, pin, soCong, cameraTrc, cameraSau,
            loaiCong, canNang, soLuong, thongTinThem, anhDaChon, giaSp, boNhoTrong, boNhoTrongPhone = null;
    public RecyclerView rcvPhoto;
    LinearLayout ln1, ln2, ln3, ln4, ln5, ln6;
    ActivityResultLauncher activityResultLauncher, requestPermissionLauncher;
    public ArrayList<Uri> uriArrayList = new ArrayList<>();
    public ArrayList<Uri> mList = new ArrayList<>();
    PhoToAdapterAddItem phoToAdapterAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        initUI();
        progressDialog = new ProgressDialog(this);
        Intent nhandl = getIntent();
        Bundle bundle = nhandl.getBundleExtra("data_Edit");
        if (bundle != null){
            String loaiSp = bundle.getString("LoaiSPEdit");
            String keyID = bundle.getString("keyIDEdit");
            phoToAdapterAddItem = new PhoToAdapterAddItem(this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
            rcvPhoto.setLayoutManager(gridLayoutManager);
            rcvPhoto.setFocusable(false);
            rcvPhoto.setAdapter(phoToAdapterAddItem);
            setLayout(loaiSp);
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
            if (loaiSp.equals("Accessory")){
                loadDataItem(keyID, loaiSp);
                btnHuy.setOnClickListener(view -> super.onBackPressed());
                btnXacNhan.setOnClickListener(view -> {
                    getUserInput();
                    checkUserInput(loaiSp);
                });
                btnEditHinh.setOnClickListener(view -> requestPermission());
            }else if (loaiSp.equals("Laptop")){
                btnHuy.setOnClickListener(view -> super.onBackPressed());
                loadDataItem(keyID, loaiSp);
                btnXacNhan.setOnClickListener(view -> {
                    getUserInput();
                    checkUserInput(loaiSp);
                });
                btnEditHinh.setOnClickListener(view -> requestPermission());
            }else {
                loadDataItem(keyID, loaiSp);
                btnHuy.setOnClickListener(view -> super.onBackPressed());
                btnXacNhan.setOnClickListener(view -> {
                    getUserInput();
                    checkUserInput(loaiSp);
                });
                btnEditHinh.setOnClickListener(view -> requestPermission());
            }
        }
    }
    private void updateItem(String keyID, String loaiSP){
        getUserInput();
        if (loaiSP.equals("Laptop") && !(tenSP.isEmpty() || tenThuongHieu.isEmpty() || hangCPU.isEmpty() || tenCPU.isEmpty() || thongSoCPU.isEmpty()
                || SSD.isEmpty() || HDD.isEmpty() || manHinh.isEmpty() || tenRam.isEmpty() || busRam.isEmpty() || loaiRam.isEmpty()
                || pin.isEmpty() || soCong.isEmpty() || loaiCong.isEmpty() || canNang.isEmpty() || soLuong.isEmpty() || thongTinThem.isEmpty()
                || anhDaChon.equals("0") || giaSp.isEmpty() || cameraTrc.isEmpty() || cameraSau.isEmpty())){
            progressDialog.show();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mdata = firebaseDatabase.getReference();
            for (i=0; i< uriArrayList.size(); i++){
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference(keyID);
                StorageReference fileRef = storageRef.child(i+".png");
                fileRef.putFile(uriArrayList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                mUri = uri.toString();
                                if (uriArrayList.size() == i){
                                    Item item = new Item("Laptop", tenSP, keyID, tenThuongHieu, hangCPU+","+tenCPU+","+thongSoCPU, boNhoTrong, manHinh,
                                            tenRam+","+loaiRam+","+busRam,pin, soCong+","+ loaiCong, thongTinThem, soLuong, 0, cameraTrc, cameraSau, mUri, Integer.parseInt(giaSp), Integer.parseInt(anhDaChon), Integer.parseInt(canNang));

                                }
                            }
                        });

                    }
                });
            }

        }
        else if (loaiSP.equals("Phone") &&!(tenSP.isEmpty() || tenThuongHieu.isEmpty() || hangCPU.isEmpty() || tenCPU.isEmpty() || thongSoCPU.isEmpty()
                || SSD.isEmpty() || manHinh.isEmpty() || tenRam.isEmpty()
                || pin.isEmpty() || soCong.isEmpty() || loaiCong.isEmpty() || canNang.isEmpty() || soLuong.isEmpty() || thongTinThem.isEmpty()
                || anhDaChon.equals("0") || giaSp.isEmpty() || cameraTrc.isEmpty() || cameraSau.isEmpty())){
            progressDialog.show();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mdata = firebaseDatabase.getReference("Item");
            for (i = 0; i< uriArrayList.size(); i++){
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference(keyID);
                StorageReference fileRef = storageRef.child(i+".png");
                fileRef.putFile(uriArrayList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                mUri = uri.toString();
                                if (uriArrayList.size() == i){
                                    Item item = new Item("Phone", tenSP, keyID, tenThuongHieu, hangCPU+","+tenCPU+","+thongSoCPU, boNhoTrongPhone, manHinh,
                                            tenRam,pin, soCong+","+ loaiCong, thongTinThem, soLuong, 0, cameraTrc, cameraSau, mUri,Integer.parseInt(giaSp), Integer.parseInt(anhDaChon), Integer.parseInt(canNang));
//                                    mdata.child(loaiSP).child(keyID).updateChildren(item.toMap(), new DatabaseReference.CompletionListener() {
//                                        @Override
//                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                                            Toast.makeText(Edit_Item_Activity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
                                }
                            }
                        });

                    }
                });
            }

        }else if (loaiSP.equals("Accessory")){


        }else {

        }
    }
    private void loadDataItem(String keyid, String loaisp){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata = firebaseDatabase.getReference("Item");
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference(keyid);

        if (loaisp.equals("Accessory")){
            mdata.child(loaisp).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds: snapshot.getChildren()){
                        Accessory accessory = ds.getValue(Accessory.class);
                        if (accessory.getKeyID().equals(keyid)){
                            edtTenSP.setText(accessory.getLoaiPhuKien());
                            edtTenThuongHieu.setText(accessory.getHangPhuKien());
                            edtGiaSanPham.setText(accessory.getTenPhuKien());
                            edtCameraTrc.setText(accessory.getGiaPhuKien()+"");
                            edtCameraSau.setText(accessory.getThongTinPhuKien());
                            edtManHinh.setText(accessory.getSoLuongPK()+"");
                            mList.clear();
                            for (int i=0; i<accessory.getSoHinhAnh();i++){
                                storageReference.child(i+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        mList.add(uri);
                                        phoToAdapterAddItem.SetData(mList);
                                    }
                                });
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if (loaisp.equals("Laptop")){
            mdata.child(loaisp).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds: snapshot.getChildren()){
                        Item item = ds.getValue(Item.class);
                        if (item.getKeyId().equals(keyid)){
                            String[] Cpu = item.getCpu().split(",");
                            String hangCPU = Cpu[0];
                            String tenCPU = Cpu[1];
                            String thongSoCPU = Cpu[2];
                            String[] boNho = item.getBoNhoTrong().split(",");
                            String SSD = boNho[0];
                            String HDD = boNho[1];
                            String[] ram = item.getRam().split(",");
                            String dungluongRam = ram[0];
                            String loaiRam = ram[1];
                            String busRam = ram[2];
                            String[] soCong = item.getCongKetNoi().split(",");
                            String soCongkn = soCong[0];
                            String loaiCong = soCong[1];
                            edtTenSP.setText(item.getTenItem());
                            edtManHinh.setText(item.getKichThuocManHinh());
                            edtCameraSau.setText(item.getCameraSau());
                            edtCameraTrc.setText(item.getCameraTrc());
                            edtTenThuongHieu.setText(item.getTenThuongHieu());
                            edtPin.setText(item.getPin());
                            edtGiaSanPham.setText(item.getGiaSp()+"");
                            edtSoLuong.setText(item.getSoLuong());
                            edtThongTin.setText(item.getThongTin());
                            edtHangCPU.setText(hangCPU);
                            edtTenCPU.setText(tenCPU);
                            edtThongSoCPU.setText(thongSoCPU);
                            edtSSD.setText(SSD);
                            edtHDD.setText(HDD);
                            edtRam.setText(dungluongRam);
                            edtLoaiRam.setText(loaiRam);
                            edtBusRam.setText(busRam);
                            edtSoCong.setText(soCongkn);
                            edtLoaiCong.setText(loaiCong);
                            mList.clear();
                            for (int i=0; i<item.getSoHinhAnh();i++){
                                storageReference.child(i+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        mList.add(uri);
                                        phoToAdapterAddItem.SetData(mList);
                                    }
                                });
                            }
                            //edtCanNang.setText(item.getCanNang());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }else {
            mdata.child(loaisp).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds: snapshot.getChildren()){
                        Item item = ds.getValue(Item.class);
                        if (item.getKeyId().equals(keyid)){
                            mList.clear();
                            for (int i=0; i<item.getSoHinhAnh();i++){
                                storageReference.child(i+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        mList.add(uri);
                                        phoToAdapterAddItem.SetData(mList);
                                    }
                                });
                            }
                            String[] Cpu = item.getCpu().split(",");
                            String hangCPU = Cpu[0];
                            String tenCPU = Cpu[1];
                            String thongSoCPU = Cpu[2];
                            String[] soCong = item.getCongKetNoi().split(",");
                            String soCongkn = soCong[0];
                            String loaiCong = soCong[1];
                            edtTenSP.setText(item.getTenItem());
                            edtManHinh.setText(item.getKichThuocManHinh());
                            edtCameraSau.setText(item.getCameraSau());
                            edtCameraTrc.setText(item.getCameraTrc());
                            edtTenThuongHieu.setText(item.getTenThuongHieu());
                            edtPin.setText(item.getPin());
                            edtGiaSanPham.setText(item.getGiaSp()+"");
                            edtSoLuong.setText(item.getSoLuong());
                            edtThongTin.setText(item.getThongTin());
                            edtHangCPU.setText(hangCPU);
                            edtTenCPU.setText(tenCPU);
                            edtThongSoCPU.setText(thongSoCPU);
                            edtSSD.setText(item.getBoNhoTrong());
                            edtRam.setText(item.getRam());
                            edtSoCong.setText(soCongkn);
                            edtLoaiCong.setText(loaiCong);
                            //edtCanNang.setText(item.getCanNang());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

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
        boNhoTrong    = SSD+" GB SSD, "+HDD +" GB HDD";
        boNhoTrongPhone = SSD+" GB";
    }
    private void setLayout(String loaiSP) {
        if (loaiSP.equals("Accessory")) {
            ln1.setVisibility(View.GONE);
            ln2.setVisibility(View.GONE);
            ln3.setVisibility(View.GONE);
            ln4.setVisibility(View.GONE);
            ln5.setVisibility(View.GONE);
            ln6.setVisibility(View.GONE);
            edtThongTin.setVisibility(View.GONE);
            edtPin.setVisibility(View.GONE);
            edtTenSP.setHint("Nhập loại phụ kiện");
            edtTenThuongHieu.setHint("Nhập hãng phụ kiện");
            edtGiaSanPham.setHint("Nhập tên phụ kiện");
            edtCameraTrc.setHint("Nhập giá phụ kiện");
            edtCameraSau.setHint("Nhập thông tin phụ kiện");
            edtManHinh.setHint("Nhập số lượng phụ kiện");
        }else if (loaiSP.equals("Phone")){
            edtHDD.setVisibility(View.GONE);
            edtLoaiRam.setVisibility(View.GONE);
            edtBusRam.setVisibility(View.GONE);
            tvGB.setVisibility(View.GONE);
            tvMHZ.setVisibility(View.GONE);
        }
    }
    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
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
    private void getUserInputedit(){
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
    private void checkUserInput(String loaiSp){
        if (loaiSp.equals("Accessory")){
            if (tenSP.isEmpty()){
                wrnTenSP.setText("Nhập loại phụ kiện");
                wrnTenSP.setVisibility(View.VISIBLE);
            }else {
                wrnTenSP.setVisibility(View.GONE);
            }

            if (tenThuongHieu.isEmpty()){
                wrnTenThuongHieu.setText("Nhập hãng phụ kiện");
                wrnTenThuongHieu.setVisibility(View.VISIBLE);
            }else {
                wrnTenThuongHieu.setVisibility(View.GONE);
            }
            if (giaSp.isEmpty()){
                wrnGiaSP.setText("Nhập tên phụ kiện");
                wrnGiaSP.setVisibility(View.VISIBLE);
            }else {
                wrnGiaSP.setVisibility(View.GONE);
            }
            if (cameraTrc.isEmpty()){
                wrnCameraTrc.setText("Nhập giá phụ kiện");
                wrnCameraTrc.setVisibility(View.VISIBLE);
            }else {
                wrnCameraTrc.setVisibility(View.GONE);
            }
            if (cameraSau.isEmpty()){
                wrnCameraSau.setText("Nhập thông tin phụ kiện");
                wrnCameraSau.setVisibility(View.VISIBLE);
            }else {
                wrnCameraSau.setVisibility(View.GONE);
            }
            if (manHinh.isEmpty()){
                wrnManHinh.setText("Nhập số luọng phụ kiện");
                wrnManHinh.setVisibility(View.VISIBLE);
            }else {
                wrnManHinh.setVisibility(View.GONE);
            }
            if (anhDaChon.equals("0")) {
                wrnAnh.setVisibility(View.VISIBLE);
            } else {
                wrnAnh.setVisibility(View.GONE);
            }

        }else if (loaiSp.equals("Laptop")){
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

            if (SSD.isEmpty() || HDD.isEmpty()) {
                wrnBoNho.setVisibility(View.VISIBLE);
            }

            if (manHinh.isEmpty()) {
                wrnManHinh.setVisibility(View.VISIBLE);
            } else {
                wrnManHinh.setVisibility(View.GONE);
            }

            if (tenRam.isEmpty() || busRam.isEmpty() || loaiRam.isEmpty()) {
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

        }else {
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

            if (SSD.isEmpty()){
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

            if (tenRam.isEmpty()){
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


    private void initUI(){
        edtTenSP         = findViewById(R.id.edt_editTenSp);
        edtTenThuongHieu = findViewById(R.id.edt_editTenThuongHieu);
        edtHangCPU       = findViewById(R.id.edt_editThuongHieuCPU);
        edtTenCPU        = findViewById(R.id.edt_editTenCPU);
        edtThongSoCPU    = findViewById(R.id.edt_editThongSoCPU);
        edtSSD           = findViewById(R.id.edt_editSSD);
        edtHDD           = findViewById(R.id.edt_editHDD);
        edtManHinh       = findViewById(R.id.edt_editThongSoManHinh);
        edtRam           = findViewById(R.id.edt_editRam);
        edtLoaiRam       = findViewById(R.id.edt_editLoaiRam);
        edtBusRam        = findViewById(R.id.edt_editBusRam);
        edtPin           = findViewById(R.id.edt_editPin);
        edtSoCong        = findViewById(R.id.edt_editSoCong);
        edtLoaiCong      = findViewById(R.id.edt_editLoaiCong);
        edtCanNang       = findViewById(R.id.edt_editCanNang);
        edtSoLuong       = findViewById(R.id.edt_editSoLuong);
        edtThongTin      = findViewById(R.id.edt_editThongTinThem);
        edtCameraTrc     = findViewById(R.id.edt_editCameraTruoc);
        edtCameraSau     = findViewById(R.id.edt_editCameraSau);
        edtGiaSanPham    = findViewById(R.id.edt_editGiaSanPham);

        tvAnhDaChon      = findViewById(R.id.textViewEditSoAnhDaChon);

        wrnAnh           = findViewById(R.id.warning_ChonAnhEdit);
        wrnCanNang       = findViewById(R.id.warning_CanNangEdit);
        wrnCpu           = findViewById(R.id.warning_thongTinCPUEdit);
        wrnManHinh       = findViewById(R.id.warning_thongSoManHinhEdit);
        wrnBoNho         = findViewById(R.id.warning_thongTinBoNhoEdit);
        wrnCong          = findViewById(R.id.warning_thongTinCongEdit);
        wrnPin           = findViewById(R.id.warning_thongSoPinEdit);
        wrnRam           = findViewById(R.id.warning_thongSoRamEdit);
        wrnSoLuong       = findViewById(R.id.warning_soLuongEdit);
        wrnTenSP         = findViewById(R.id.warning_tenSpEdit);
        wrnTenThuongHieu = findViewById(R.id.warning_tenThuongHieuEdit);
        wrnThongTin      = findViewById(R.id.warning_thongTinThemEdit);
        wrnGiaSP         = findViewById(R.id.warning_loaiSpEdit);
        wrnCameraTrc     = findViewById(R.id.warning_CameraTruocEdit);
        wrnCameraSau     = findViewById(R.id.warning_CameraSauEdit);

        tvGB             = findViewById(R.id.edit_item_GB);
        tvMHZ            = findViewById(R.id.edit_item_MHZ);

        btnHuy           = findViewById(R.id.buttonEditHuy);
        btnEditHinh      = findViewById(R.id.buttonEditHinhAnh);
        btnXacNhan       = findViewById(R.id.buttonEditXacNhan);
        rcvPhoto         = findViewById(R.id.recycleView_AnhDaChonEdit);

        ln1              = findViewById(R.id.lnl_editbonho);
        ln2              = findViewById(R.id.lnl_editCANNANG);
        ln3              = findViewById(R.id.lnl_editCPU);
        ln4              = findViewById(R.id.lnl_editRAM);
        ln5              = findViewById(R.id.lnl_editSOCONG);
        ln6              = findViewById(R.id.lnl_editSOLUONG);
    }
}