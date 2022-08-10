package com.example.appbanhang.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.Adapter.Add_Accessory_Adapter;
import com.example.appbanhang.Adapter.PhoToAdapterAddItem;
import com.example.appbanhang.ListItemAddorEdit;
import com.example.appbanhang.Object.Accessory;
import com.example.appbanhang.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Add_Accessory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Add_Accessory extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ActivityResultLauncher activityResultLauncher, PermissionResultLauncher;
    private EditText edtLoaiPK, edtHangPK, edtTenPK, edtGiaPK, edtThongTinPK, edtSoLuongPK;
    private TextView tvSoAnhPK, wrnLoaiPK, wrnTenPK, wrnGiaPK, wrnHangPK, wrnThongTinPK, wrnAnhPK, wrnSoLuongPK;
    private RecyclerView rcvAnhPK;
    private Button btnChonAnhPK, btnXacNhanPK, btnHuyPK;
    private String loaiPK, hangPK, tenPK, giaPK, thongTinPK, soAnh, soLuongPK = null;
    private ProgressDialog progressDialog;
    private ArrayList<Uri> uriArrayList = new ArrayList<>();
    public View mView;
    ListItemAddorEdit listItemAddorEdit;
    Add_Accessory_Adapter add_accessory_adapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Add_Accessory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Add_Accessory.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Add_Accessory newInstance(String param1, String param2) {
        Fragment_Add_Accessory fragment = new Fragment_Add_Accessory();
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
        mView = inflater.inflate(R.layout.fragment__add__accessory, container, false);
        initUI();
        progressDialog = new ProgressDialog(mView.getContext());
        listItemAddorEdit = (ListItemAddorEdit) getActivity();
        add_accessory_adapter = new Add_Accessory_Adapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mView.getContext(), 3, LinearLayoutManager.VERTICAL, false);
        rcvAnhPK.setLayoutManager(gridLayoutManager);
        rcvAnhPK.setFocusable(false);
        rcvAnhPK.setAdapter(add_accessory_adapter);
        PermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result ->
                requestPermisstion());
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getData().getClipData() != null) {
                            uriArrayList.clear();
                            int count = result.getData().getClipData().getItemCount();
                            tvSoAnhPK.setText(count + "");
                            for (int i = 0; i < count; i++) {
                                Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                                uriArrayList.add(imageUri);
                            }
                            add_accessory_adapter.setData(uriArrayList);


                        } else {
                            uriArrayList.clear();
                            tvSoAnhPK.setText(1 + "");
                            Uri imageUri = result.getData().getData();
                            uriArrayList.add(imageUri);
                            add_accessory_adapter.setData(uriArrayList);
                        }
                    }
                });

        btnChonAnhPK.setOnClickListener(view -> {
            requestPermisstion();
        });
        btnXacNhanPK.setOnClickListener(view -> {
            addNewAccessory();
        });
        btnHuyPK.setOnClickListener(view -> {listItemAddorEdit.removeFragmentAddAccessory();});
        return mView;
    }
    private void initUI(){
        edtThongTinPK       = mView.findViewById(R.id.editTextAddThongTinPhuKien);
        edtTenPK            = mView.findViewById(R.id.editTextAddTenPhuKien);
        edtGiaPK            = mView.findViewById(R.id.editTextAddGiaPhuKien);
        edtHangPK           = mView.findViewById(R.id.editTextAddHangPhuKien);
        edtLoaiPK           = mView.findViewById(R.id.editTextAddLoaiPhuKien);
        edtSoLuongPK        = mView.findViewById(R.id.editTextAddSoLuongPhuKien);

        tvSoAnhPK           = mView.findViewById(R.id.textViewAddSoAnhPhuKienDaChon);

        btnChonAnhPK        = mView.findViewById(R.id.buttonAddHinhAnhPhuKien);
        btnXacNhanPK        = mView.findViewById(R.id.buttonAddPhuKienXacNhan);
        btnHuyPK            = mView.findViewById(R.id.buttonAddPhuKienHuy);

        wrnLoaiPK           = mView.findViewById(R.id.warning_LoaiPhuKienAdd);
        wrnHangPK           = mView.findViewById(R.id.warning_HangPhuKienAdd);
        wrnGiaPK            = mView.findViewById(R.id.warning_GiaPhuKienAdd);
        wrnTenPK            = mView.findViewById(R.id.warning_tenPhuKienAdd);
        wrnThongTinPK       = mView.findViewById(R.id.warning_ThongTinPhuKienAdd);
        wrnAnhPK            = mView.findViewById(R.id.warning_ChonAnhAdd);
        wrnSoLuongPK        = mView.findViewById(R.id.warning_SoLuongPhuKienAdd);

        rcvAnhPK            = mView.findViewById(R.id.recycleView_AnhPhuKienDaChonAdd);
    }
    private void getUserInput(){
        thongTinPK      = edtThongTinPK.getText().toString().trim();
        tenPK           = edtTenPK.getText().toString().trim();
        giaPK           = edtGiaPK.getText().toString().trim();
        hangPK          = edtHangPK.getText().toString().trim();
        loaiPK          = edtLoaiPK.getText().toString().trim();
        soLuongPK       = edtSoLuongPK.getText().toString().trim();
        soAnh           = tvSoAnhPK.getText().toString().trim();
    }
    private void addNewAccessory(){
        getUserInput();
        if (!(thongTinPK.isEmpty() || tenPK.isEmpty() || giaPK.isEmpty() || loaiPK.isEmpty()
            || hangPK.isEmpty() || soAnh.equals("0") || soLuongPK.isEmpty())){
            progressDialog.show();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mData = firebaseDatabase.getReference();
            String key = mData.child("Item").child("Accessory").push().getKey();
            for (int i=0; i< uriArrayList.size(); i++){
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference(key);
                StorageReference fileRef = storageRef.child(i+".png");
                fileRef.putFile(uriArrayList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String mUri = uri.toString();
                                Accessory accessory = new Accessory(tenPK, hangPK, loaiPK, thongTinPK, key,
                                        Integer.parseInt(giaPK), Integer.parseInt(soAnh), 0, Integer.parseInt(soLuongPK), mUri);
                                mData.child("Item").child("Accessory").child(key).setValue(accessory, (error, ref) -> {
                                    Toast.makeText(listItemAddorEdit, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                });
                                listItemAddorEdit.removeFragmentAddAccessory();
                            }
                        });
                    }
                });
            }

        }
        else {
            warningUserInput();
        }
    }
    private void requestPermisstion(){
       if (ContextCompat.checkSelfPermission(mView.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
           PermissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
       }
       else {
           Intent intent = new Intent();
           intent.setType("image/*");
           intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
           intent.setAction(Intent.ACTION_GET_CONTENT);
           activityResultLauncher.launch(intent);
       }
    }


    private void warningUserInput(){
        getUserInput();
        if (thongTinPK.isEmpty()){
            wrnThongTinPK.setVisibility(View.VISIBLE);
        }
        else {
            wrnThongTinPK.setVisibility(View.GONE);
        }

        if (tenPK.isEmpty()){
            wrnTenPK.setVisibility(View.VISIBLE);
        }
        else {
            wrnTenPK.setVisibility(View.GONE);
        }

        if (giaPK.isEmpty()){
            wrnGiaPK.setVisibility(View.VISIBLE);
        }
        else {
            wrnGiaPK.setVisibility(View.GONE);
        }

        if (loaiPK.isEmpty()){
            wrnLoaiPK.setVisibility(View.VISIBLE);
        }
        else {
            wrnLoaiPK.setVisibility(View.GONE);
        }

        if (hangPK.isEmpty()){
            wrnHangPK.setVisibility(View.VISIBLE);
        }
        else {
            wrnHangPK.setVisibility(View.GONE);
        }

        if (soLuongPK.isEmpty()){
            wrnSoLuongPK.setVisibility(View.VISIBLE);
        }
        else {
            wrnSoLuongPK.setVisibility(View.GONE);
        }

        if (soAnh.equals("0")){
            wrnAnhPK.setVisibility(View.VISIBLE);
        }
        else {
            wrnAnhPK.setVisibility(View.GONE);
        }
    }
}