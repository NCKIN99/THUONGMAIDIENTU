package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appbanhang.Adapter.List_Warranty_Adapter;
import com.example.appbanhang.Fragment.Fragment_Add_Warranty;
import com.example.appbanhang.Object.Promotion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ListWarrantyActivity extends AppCompatActivity {

    ImageView add, exit;
    ProgressDialog progressDialog;
    ArrayList<Promotion> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    List_Warranty_Adapter list_warranty_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_warranty);
        initUI();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setFocusable(false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        list_warranty_adapter = new List_Warranty_Adapter(this, arrayList);
        recyclerView.setAdapter(list_warranty_adapter);
        list_warranty_adapter.setOnClick(this::removeItem);
        loadData();

        add.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.framelayout_list_warranty, new Fragment_Add_Warranty(), "FragmentAddWarranty").commit();
        });
        exit.setOnClickListener(view -> super.onBackPressed());
    }
    private void initUI(){
        add             = findViewById(R.id.Add_Warranty);
        exit            = findViewById(R.id.Add_Warranty_Exit);
        recyclerView    = findViewById(R.id.recycleView_list_warranty);
    }
    public void RemoveFragmentAddWarranty(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment_Add_Warranty fragment_add_warranty = (Fragment_Add_Warranty) getSupportFragmentManager().findFragmentByTag("FragmentAddWarranty");
        if (fragment_add_warranty != null){
            fragmentTransaction.remove(fragment_add_warranty).commit();
        }
    }
    private void loadData(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đang tải.....");
        progressDialog.show();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Promotional");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Promotion promotion = ds.getValue(Promotion.class);
                    arrayList.add(promotion);
                }
                list_warranty_adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void removeItem(String keyid){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đang xóa.....");
        alertDialog.setMessage("Bạn có thực muốn xóa gói khuyến mãi này không?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progressDialog.show();
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference mData = firebaseDatabase.getReference("Promotional");
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference(keyid);
                storageRef.child("0.png").delete();
                mData.child(keyid).removeValue((error, ref) -> {
                    progressDialog.dismiss();
                    Toast.makeText(ListWarrantyActivity.this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                });
            }
        });
        alertDialog.setNegativeButton("Không", (dialogInterface, i) -> {});
        alertDialog.show();
    }

}