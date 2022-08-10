package com.example.appbanhang.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appbanhang.AccountSetting;
import com.example.appbanhang.Item_State_Activity;
import com.example.appbanhang.ListWarrantyActivity;
import com.example.appbanhang.ListItemAddorEdit;
import com.example.appbanhang.MainActivity;
import com.example.appbanhang.Object.User;
import com.example.appbanhang.Object.Users_Item;
import com.example.appbanhang.R;
import com.example.appbanhang.shopping_Cart_Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabFragmentUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragmentUser extends Fragment {
    private TextView addItem, addWarranty, accountSetting, tvTen, tvTK, tvState0, tvState1
            , tvState2, tvState3, tvState4;
    private View mView;
    MainActivity mainActivity;
    private FrameLayout frameLayout;
    private ImageView imgHinh, imgBack;
    int is0 = 0;
    int is1 = 0;
    int is2 = 0;
    int is3 = 0;
    int is4 = 0;
    ConstraintLayout csLayout0, csLayout1, csLayout2, csLayout3, csLayout4;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TabFragmentUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabFragmentUser.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFragmentUser newInstance(String param1, String param2) {
        TabFragmentUser fragment = new TabFragmentUser();
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
        mView = inflater.inflate(R.layout.fragment_tab_user, container, false);
        mainActivity = (MainActivity) getActivity();
        initUI();
        loadInformation();
        if (mainActivity.user.getAccountType().equals("admin")){
            ItemStateAdmin();
        }else {
            ItemState(mainActivity.user.getUserName());
            frameLayout.setVisibility(View.GONE);
        }
        addItem.setOnClickListener(view -> {
            Intent intent = new Intent(mView.getContext(), ListItemAddorEdit.class);
            startActivity(intent);
        });
        addWarranty.setOnClickListener(view -> {
            Intent intent = new Intent(mView.getContext(), ListWarrantyActivity.class);
            startActivity(intent);
        });
        accountSetting.setOnClickListener(view -> accountSettingOnClick());
        imgHinh.setOnClickListener(view -> accountSettingOnClick());
        csLayout0.setOnClickListener(view -> {
            User user = mainActivity.user;
            Intent intent = new Intent(mView.getContext(), Item_State_Activity.class);
            Bundle bundle = new Bundle();
            bundle.putString("ItemState", "0");
            bundle.putSerializable("User_Information", user);
            intent.putExtra("Dulieu", bundle);
            startActivity(intent);

        });
        csLayout1.setOnClickListener(view -> {
            User user = mainActivity.user;
            Intent intent = new Intent(mView.getContext(), Item_State_Activity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("User_Information", user);
            bundle.putString("ItemState","1");
            intent.putExtra("Dulieu", bundle);
            startActivity(intent);

        });
        csLayout2.setOnClickListener(view -> {
            User user = mainActivity.user;
            Intent intent = new Intent(mView.getContext(), Item_State_Activity.class);
            Bundle bundle = new Bundle();
            bundle.putString("ItemState","2");
            bundle.putSerializable("User_Information", user);
            intent.putExtra("Dulieu", bundle);
            startActivity(intent);
        });
        csLayout3.setOnClickListener(view -> {
            User user = mainActivity.user;
            Intent intent = new Intent(mView.getContext(), Item_State_Activity.class);
            Bundle bundle = new Bundle();
            bundle.putString("ItemState","3");
            bundle.putSerializable("User_Information", user);
            intent.putExtra("Dulieu", bundle);
            startActivity(intent);

        });
        csLayout4.setOnClickListener(view -> {
            User user = mainActivity.user;
            Intent intent = new Intent(mView.getContext(), Item_State_Activity.class);
            Bundle bundle = new Bundle();
            bundle.putString("ItemState","4");
            bundle.putSerializable("User_Information", user);
            intent.putExtra("Dulieu", bundle);
            startActivity(intent);
        });
        return mView;
    }
    private void initUI(){
        addWarranty     = mView.findViewById(R.id.textView_AddWarranty_Device);
        addItem         = mView.findViewById(R.id.textView_AddItem);
        accountSetting  = mView.findViewById(R.id.textView_AccountSetting);
        imgHinh         = mView.findViewById(R.id.circleImageView);
        imgBack         = mView.findViewById(R.id.background_user);
        tvTen           = mView.findViewById(R.id.textView2);
        tvTK            = mView.findViewById(R.id.textView_tenTK);
        csLayout0       = mView.findViewById(R.id.ItemState_0);
        csLayout1       = mView.findViewById(R.id.ItemState_1);
        csLayout2       = mView.findViewById(R.id.ItemState_2);
        csLayout3       = mView.findViewById(R.id.ItemState_3);
        csLayout4       = mView.findViewById(R.id.ItemState_4);
        tvState0        = mView.findViewById(R.id.textView_Item_State_0);
        tvState1        = mView.findViewById(R.id.textView_Item_State_1);
        tvState2        = mView.findViewById(R.id.textView_Item_State_2);
        tvState3        = mView.findViewById(R.id.textView_Item_State_3);
        tvState4        = mView.findViewById(R.id.textView_Item_State_4);
        frameLayout     = mView.findViewById(R.id.tabUserAdmin);

    }
    private void loadInformation(){
        User user = mainActivity.user;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata = firebaseDatabase.getReference("Users");
        mdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    User user1 = ds.getValue(User.class);
                    if (user.getUserName().equals(user1.getUserName())){
                        if (!user1.getUserPhoto().equals("No Input")){
                            Glide.with(mView.getContext()).load(Uri.parse(user1.getUserPhoto())).into(imgHinh);
                        }
                        if (!user1.getUserBackground().equals("No Input")){
                            Glide.with(mView.getContext()).load(Uri.parse(user1.getUserBackground())).into(imgBack);
                        }
                        if (user1.getUserFullName().equals("No Input")){
                            tvTen.setText("Chưa nhập họ tên");
                        }
                        else {
                            tvTen.setText(user1.getUserFullName());
                        }
                        tvTK.setText(user1.getUserName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void ItemState(String username){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata = firebaseDatabase.getReference("User_Item");
        mdata.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i0 = 0;
                int i1 = 0;
                int i2 = 0;
                int i3 = 0;
                int i4 = 0;
                for (DataSnapshot ds: snapshot.getChildren()){
                    Users_Item users_item = ds.getValue(Users_Item.class);
                    if (users_item.getItemState().equals("0")){
                        i0++;
                    }
                    if (users_item.getItemState().equals("1")){
                        i1++;
                    }
                    if (users_item.getItemState().equals("2")){
                        i2++;
                    }
                    if (users_item.getItemState().equals("3")){
                        i3++;
                    }
                    if (users_item.getItemState().equals("4")){
                        i4++;
                    }
                }
                tvState0.setText(String.valueOf(i0));
                tvState1.setText(String.valueOf(i1));
                tvState2.setText(String.valueOf(i2));
                tvState3.setText(String.valueOf(i3));
                tvState4.setText(String.valueOf(i4));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void accountSettingOnClick(){
        Intent intent = new Intent(mView.getContext(), AccountSetting.class);
        Bundle bundle = new Bundle();
        User user = mainActivity.user;
        bundle.putSerializable("User_Information", user);
        intent.putExtra("Dulieu", bundle);
        startActivity(intent);
    }
    private void ItemStateAdmin(){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mData = firebaseDatabase.getReference("User_Item");
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    is0 = 0;
                    is1 = 0;
                    is2 = 0;
                    is3 = 0;
                    is4 = 0;
                    String keyID = ds.getKey();
                    mData.child(keyID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int i0 = 0;
                            int i1 = 0;
                            int i2 = 0;
                            int i3 = 0;
                            int i4 = 0;
                            for (DataSnapshot ds: snapshot.getChildren()){
                                Users_Item users_item = ds.getValue(Users_Item.class);
                                if (users_item.getItemState().equals("0")){
                                    i0++;
                                }
                                if (users_item.getItemState().equals("1")){
                                    i1++;
                                }
                                if (users_item.getItemState().equals("2")){
                                    i2++;
                                }
                                if (users_item.getItemState().equals("3")){
                                    i3++;
                                }
                                if (users_item.getItemState().equals("4")){
                                    i4++;
                                }
                            }
                            is0+=i0;
                            is1+=i1;
                            is2+=i2;
                            is3+=i3;
                            is4+=i4;
                            tvState0.setText(String.valueOf(is0));
                            tvState1.setText(String.valueOf(is1));
                            tvState2.setText(String.valueOf(is2));
                            tvState3.setText(String.valueOf(is3));
                            tvState4.setText(String.valueOf(is4));
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


}