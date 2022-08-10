package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.Adapter.Item_State_Adapter;
import com.example.appbanhang.Fragment.Fragment_Add_Warranty;
import com.example.appbanhang.Fragment.Fragment_User_Item_State;
import com.example.appbanhang.Object.User;
import com.example.appbanhang.Object.Users_Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Item_State_Activity extends AppCompatActivity {

    RecyclerView rcvItemState;
    ImageView imgSPC, imgBack;
    ArrayList<String> mArray = new ArrayList<>();
    Item_State_Adapter item_state_adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_state);
        initUI();
        imgSPC.setVisibility(View.GONE);
        Intent nhanDL = getIntent();
        Bundle bundle = nhanDL.getBundleExtra("Dulieu");
        if (bundle != null){
            User user = (User) bundle.getSerializable("User_Information");
            String itemState = bundle.getString("ItemState");
            loadItem(itemState);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            if (user.getAccountType().equals("user")){
                Fragment_User_Item_State fragment_user_item_state = new Fragment_User_Item_State();
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("user_item_state", user);
                bundle1.putString("keyID", user.getUserName());
                bundle1.putString("itemState", itemState);
                bundle1.putString("userPermission", user.getAccountType());
                fragment_user_item_state.setArguments(bundle1);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.frameLayout_Item_State, fragment_user_item_state, "FragmentUserItemState").commit();
            }

            item_state_adapter = new Item_State_Adapter(mArray, keyID -> {
                Fragment_User_Item_State fragment_user_item_state = new Fragment_User_Item_State();
                Bundle bundle1 = new Bundle();
                bundle1.putString("keyID", keyID);
                bundle1.putString("itemState", itemState);
                bundle1.putString("userPermission", user.getAccountType());
                fragment_user_item_state.setArguments(bundle1);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.frameLayout_Item_State, fragment_user_item_state, "FragmentUserItemState").commit();
            });
            rcvItemState.setLayoutManager(linearLayoutManager);
            rcvItemState.setFocusable(false);
            rcvItemState.setAdapter(item_state_adapter);
        }
        imgBack.setOnClickListener(view -> super.onBackPressed());
    }

    private void loadItem(String state) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mData = firebaseDatabase.getReference("User_Item");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    String keyID = ds.getKey();
                    mArray.clear();
                    mData.child(keyID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int i = 0;
                            long i2 = snapshot.getChildrenCount();
                            for (DataSnapshot ds: snapshot.getChildren()){
                                Users_Item users_item = ds.getValue(Users_Item.class);
                                    if (users_item.getItemState().equals(state)){
                                    i++;
                                }
                            }
                            mArray.add(keyID+"-"+i2+"-"+i+"-"+state+"-"+keyID);
                            item_state_adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initUI() {
        rcvItemState    = findViewById(R.id.recycleView_Item_State);
        imgBack         = findViewById(R.id.item_Information_Exit);
        imgSPC          = findViewById(R.id.img_ShoppingCart);
    }

}