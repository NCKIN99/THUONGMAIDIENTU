package com.example.appbanhang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.Adapter.ListItemAddorEditAdapter;
import com.example.appbanhang.Fragment.Fragment_AddItem;
import com.example.appbanhang.Fragment.Fragment_Add_Accessory;
import com.example.appbanhang.Object.Accessory;
import com.example.appbanhang.Object.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ListItemAddorEdit extends AppCompatActivity {

    ListItemAddorEditAdapter listItemAddorEditAdapter;
    RecyclerView rcvListItem;
    ArrayList<Item>  mArrayList ;
    ArrayList<Accessory> mArrAccessory ;
    ImageView  add, exit;
    public String loaiSp = null;
    ProgressDialog progressDialog;
    TextView tvFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item_addor_edit);
        initUI();
        progressDialog = new ProgressDialog(this);
        mArrayList = new ArrayList<>();
        mArrAccessory = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvListItem.setLayoutManager(linearLayoutManager);
        rcvListItem.setFocusable(false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvListItem.addItemDecoration(dividerItemDecoration);
        listItemAddorEditAdapter = new ListItemAddorEditAdapter(this, mArrayList, mArrAccessory);
        rcvListItem.setAdapter(listItemAddorEditAdapter);
        listItemAddorEditAdapter.setOnClickListener(new ListItemAddorEditAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(String loaiSp, int soHinhAnh, String keyId) {
                removeItem(loaiSp, soHinhAnh, keyId);
            }

            @Override
            public void onEditItemClick(String loaiSp, String keyID) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(getApplicationContext(), Edit_Item_Activity.class);
                bundle.putString("LoaiSPEdit", loaiSp);
                bundle.putString("keyIDEdit", keyID);
                intent.putExtra("data_Edit", bundle);
                startActivity(intent);
            }
        });

        loadData("Laptop");
        exit.setOnClickListener(view -> super.onBackPressed());
        add.setOnClickListener(view -> {
            showMenu();
        });
        tvFilter.setOnClickListener(view -> showMenuFilter());

    }

    public void initUI(){
        rcvListItem     = findViewById(R.id.recycleView_Add_or_Edit);
        add             = findViewById(R.id.Add_ItemAdd);
        exit            = findViewById(R.id.Add_ItemExit);
        tvFilter        = findViewById(R.id.Add_ItemFilter);
    }
    private void loadData(String loaiSp){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mData = firebaseDatabase.getReference();
        progressDialog.show();
        mData.child("Item").child(loaiSp).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mArrayList.clear();
                mArrAccessory.clear();
                if (loaiSp.equals("Accessory")){
                    for (DataSnapshot ds: snapshot.getChildren()){
                        Accessory accessory = ds.getValue(Accessory.class);
                        mArrAccessory.add(accessory);
                    }
                    listItemAddorEditAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
                else {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Item item = ds.getValue(Item.class);
                        mArrayList.add(item);
                    }
                    listItemAddorEditAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showMenu(){
        PopupMenu popupMenu = new PopupMenu(this, add);
        popupMenu.getMenuInflater().inflate(R.menu.menu_add_item, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.menu_add_laptop:
                    loaiSp = "Laptop";
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.framelayout_list_add_or_remove, new Fragment_AddItem(), "FragmentAdd").commit();
                    break;
                case R.id.menu_add_phone:
                    loaiSp = "Phone";
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.add(R.id.framelayout_list_add_or_remove, new Fragment_AddItem(), "FragmentAdd").commit();
                    break;
                case R.id.menu_add_accessory:
                    loaiSp = "Accessory";
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.add(R.id.framelayout_list_add_or_remove, new Fragment_Add_Accessory(), "FragmentAddAccessory").commit();
                    break;
            }
            return false;
        });
        popupMenu.show();

    }
    private void showMenuFilter(){
        PopupMenu popupMenu = new PopupMenu(this, tvFilter);
        popupMenu.getMenuInflater().inflate(R.menu.menu_add_item_filter, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.filter_Laptop:
                    loadData("Laptop");
                    tvFilter.setText("Máy tính");
                    break;
                case R.id.filter_Phone:
                    loadData("Phone");
                    tvFilter.setText("Điện thoại");
                    break;
                case R.id.filter_Accessory:
                    loadData("Accessory");
                    tvFilter.setText("Phụ kiện");
                    break;
            }
            return false;
        });
        popupMenu.show();
    }
    private void removeItem(String loaiSp, int SoHinhAnh, String keyID){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Bạn có thực sự muốn xóa sản phẩm này không?");
        alertDialog.setPositiveButton("Có", (dialogInterface, i) -> {
            progressDialog.show();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mData = firebaseDatabase.getReference();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference(keyID);
            for (int j=0; j<SoHinhAnh; j++){
                storageRef.child(j+".png").delete();
            }
            mData.child("Item").child(loaiSp).child(keyID).removeValue((error, ref) -> {
                progressDialog.dismiss();
                Toast.makeText(ListItemAddorEdit.this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
            });

        });
        alertDialog.setNegativeButton("Không", (dialogInterface, i) -> {
        });
        alertDialog.show();
    }
    public void RemoveFragmentAddLaptop(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment_AddItem fragment_addItem = (Fragment_AddItem) getSupportFragmentManager().findFragmentByTag("FragmentAdd");
        if (fragment_addItem != null) {
            fragmentTransaction.remove(fragment_addItem).commit();
        }
    }

    public void removeFragmentAddAccessory(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment_Add_Accessory fragment_add_accessory = (Fragment_Add_Accessory) getSupportFragmentManager().findFragmentByTag("FragmentAddAccessory");
        if (fragment_add_accessory != null) {
            fragmentTransaction.remove(fragment_add_accessory).commit();
        }
    }

}