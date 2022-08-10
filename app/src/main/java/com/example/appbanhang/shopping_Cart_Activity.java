package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appbanhang.Adapter.RecycleView_Shopping_Cart_Adapter;
import com.example.appbanhang.Object.Accessory;
import com.example.appbanhang.Object.Item;
import com.example.appbanhang.Object.User;
import com.example.appbanhang.Object.Users_Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class shopping_Cart_Activity extends AppCompatActivity {

    RecyclerView rcvShoppingCart;
    RecycleView_Shopping_Cart_Adapter recycleView_shopping_cart_adapter;
    ArrayList<Users_Item> users_itemArrayList = new ArrayList<>();
    ImageView imgExit, imgShoppingCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        initUI();
        imgExit.setOnClickListener(view -> super.onBackPressed());
        imgShoppingCart.setVisibility(View.GONE);
        Intent nhandl = getIntent();
        Bundle bundle = nhandl.getBundleExtra("data_main");
        if (bundle != null){
            User user = (User) bundle.getSerializable("User_inf");
            loadData(user.getUserName());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rcvShoppingCart.setLayoutManager(linearLayoutManager);
            rcvShoppingCart.setFocusable(false);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            rcvShoppingCart.addItemDecoration(dividerItemDecoration);
            recycleView_shopping_cart_adapter = new RecycleView_Shopping_Cart_Adapter(users_itemArrayList, getApplicationContext(), new RecycleView_Shopping_Cart_Adapter.onClickItemShoppingCart() {
                @Override
                public void onDeleteItemSPC(Users_Item users_item) {
                    deleteItemSPC(users_item.getKeyID(), user.getUserName());
                }

                @Override
                public void onClickItemSPC(Users_Item users_item) {
                    Intent intent = new Intent(shopping_Cart_Activity.this, item_Information.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("User_inf", user);
                    if (users_item.getItem() != null){
                        bundle.putSerializable("Item_inf", users_item.getItem());
                    }else {
                        bundle.putSerializable("Item_inf", users_item.getAccessory());
                    }
                    intent.putExtra("data_frgLap", bundle);
                    startActivity(intent);
                }
            });
            rcvShoppingCart.setAdapter(recycleView_shopping_cart_adapter);

        }


    }

    private void deleteItemSPC(String keyID, String userName) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Bạn có thực sự muốn xóa sản phẩm này không?");
        alertDialog.setNegativeButton("Không", (dialogInterface, i) -> {
        });
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference mdata = firebaseDatabase.getReference("Shopping_Cart");
                mdata.child(userName).child(keyID).removeValue((error, ref) -> Toast.makeText(shopping_Cart_Activity.this, "Đã xóa", Toast.LENGTH_SHORT).show());
            }
        });
        alertDialog.show();
    }

    private void loadData(String userName) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata = firebaseDatabase.getReference("Shopping_Cart");
        mdata.child(userName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users_itemArrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Users_Item users_item = ds.getValue(Users_Item.class);
                    users_itemArrayList.add(users_item);
                }
                recycleView_shopping_cart_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initUI() {
        rcvShoppingCart     = findViewById(R.id.recycleView_Shopping_Cart);
        imgExit             = findViewById(R.id.item_Information_Exit);
        imgShoppingCart     = findViewById(R.id.img_ShoppingCart);
    }
}