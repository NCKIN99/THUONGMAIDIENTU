package com.example.appbanhang.Fragment;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.Adapter.RecyclerView_Adapter_fragment_User_Item_State;
import com.example.appbanhang.Item_State_Activity;
import com.example.appbanhang.Object.User;
import com.example.appbanhang.Object.Users_Item;
import com.example.appbanhang.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_User_Item_State#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_User_Item_State extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mView;
    private RecyclerView rcv;
    private ArrayList<Users_Item> arrlUserItem = new ArrayList<>();
    RecyclerView_Adapter_fragment_User_Item_State recyclerView_adapter_fragment_user_item_state;
    private ImageView imgBack, imgShoppingCart;

    public Fragment_User_Item_State() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_User_Item_State.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_User_Item_State newInstance(String param1, String param2) {
        Fragment_User_Item_State fragment = new Fragment_User_Item_State();
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
        mView = inflater.inflate(R.layout.fragment__user__item__state, container, false);
        initUI();
        imgShoppingCart.setVisibility(View.GONE);

        String keyID = getArguments().getString("keyID");
        String itemState = getArguments().getString("itemState");
        String usrP = getArguments().getString("userPermission");
        loadData(keyID, itemState);
        imgBack.setOnClickListener(view -> {
            User user = (User) getArguments().getSerializable("user_item_state");
            if (user != null){
                getActivity().onBackPressed();
            }else {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment_User_Item_State fragment_user_item_state = (Fragment_User_Item_State) getActivity().getSupportFragmentManager().findFragmentByTag("FragmentUserItemState");
                if (fragment_user_item_state != null) {
                    fragmentTransaction.remove(fragment_user_item_state).commit();
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView_adapter_fragment_user_item_state = new RecyclerView_Adapter_fragment_User_Item_State(arrlUserItem, getContext(), new RecyclerView_Adapter_fragment_User_Item_State.onClickUserItemState() {
            @Override
            public void HuyDon(Users_Item users_item) {
                huyDon(users_item, keyID);
            }

            @Override
            public void XacNhanDon(Users_Item users_item) {
                xacNhanDon(users_item, keyID);
            }
        }, usrP);
        rcv.setFocusable(false);
        rcv.setLayoutManager(linearLayoutManager);
        rcv.setAdapter(recyclerView_adapter_fragment_user_item_state);
        return mView;
    }

    private void huyDon(Users_Item users_item, String keyid){
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_huydon);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        EditText textView = dialog.findViewById(R.id.edt_dialog_huydon);
        Button  btn       = dialog.findViewById(R.id.btn_dialog_huydon);
        btn.setOnClickListener(view -> {
            String lyDoHuyDon = textView.getText().toString().trim();
            if (!lyDoHuyDon.isEmpty()) {
                AlertDialog.Builder alerDialog = new AlertDialog.Builder(getContext());
                alerDialog.setMessage("Bạn có thực sự muốn hủy đơn?");
                alerDialog.setNegativeButton("Không", (dialogInterface, i) -> {
                });
                alerDialog.setPositiveButton("Có", (dialogInterface, i) -> {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference mdata = firebaseDatabase.getReference("User_Item");
                    mdata.child(keyid).child(users_item.getKeyID()).child("itemState").setValue("4").addOnSuccessListener(unused -> {
                        mdata.child(keyid).child(users_item.getKeyID()).child("huy").setValue(lyDoHuyDon);
                        dialog.dismiss();
                    });
                });
                alerDialog.show();
            }else {
                Toast.makeText(getContext(), "Nhập lý do hủy đơn hàng", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void xacNhanDon(Users_Item users_item, String keyid){
        AlertDialog.Builder alerDialog = new AlertDialog.Builder(getContext());
        alerDialog.setMessage("Xác nhận đơn hàng");
        alerDialog.setNegativeButton("Không", (dialogInterface, i) -> {
        });
        alerDialog.setPositiveButton("Có", (dialogInterface, i) -> {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mdata = firebaseDatabase.getReference("User_Item");
            switch (users_item.getItemState()) {
                case "0":
                    mdata.child(keyid).child(users_item.getKeyID()).child("itemState").setValue("1");
                    break;
                case "1":
                    mdata.child(keyid).child(users_item.getKeyID()).child("itemState").setValue("2");
                    break;
                case "2":
                    mdata.child(keyid).child(users_item.getKeyID()).child("itemState").setValue("3");
                    break;
            }
        });
        alerDialog.show();
    }

    private void loadData(String key, String itst) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata = firebaseDatabase.getReference("User_Item");
        mdata.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrlUserItem.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Users_Item users_item = ds.getValue(Users_Item.class);
                    if (users_item.getItemState().equals(itst)){
                        arrlUserItem.add(users_item);
                    }
                }
                recyclerView_adapter_fragment_user_item_state.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initUI() {
        imgShoppingCart     = mView.findViewById(R.id.img_ShoppingCart);
        imgBack             = mView.findViewById(R.id.item_Information_Exit);
        rcv                 = mView.findViewById(R.id.recycleView_User_Item_State);
    }


}