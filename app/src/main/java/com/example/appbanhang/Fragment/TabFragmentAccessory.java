package com.example.appbanhang.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.Adapter.RecycleView_Adapter_BestSell_Accessory;
import com.example.appbanhang.Adapter.RecycleView_Adapter_Suggestions_Accesory;
import com.example.appbanhang.Adapter.RecyclerView_Adapter_BestSell;
import com.example.appbanhang.Adapter.ViewPager_Adapter_QC;
import com.example.appbanhang.Filter_Item;
import com.example.appbanhang.MainActivity;
import com.example.appbanhang.Object.Accessory;
import com.example.appbanhang.Object.Item;
import com.example.appbanhang.Object.Promotion;
import com.example.appbanhang.Object.User;
import com.example.appbanhang.R;
import com.example.appbanhang.item_Information;
import com.example.appbanhang.my_Interface.ItemClickAccessory;
import com.example.appbanhang.shopping_Cart_Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabFragmentAccessory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragmentAccessory extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View mView;
    ArrayList<Accessory> accessories = new ArrayList<>();
    public ViewPager2 mViewPager2;
    MainActivity mainActivity;
    TextView tvFilter, tv;
    ImageView imgShoppingCart;
    RecyclerView rcvBestSell, rcvSuggestionItem;
    RecycleView_Adapter_Suggestions_Accesory recycleView_adapter_suggestions_accesory;
    RecycleView_Adapter_BestSell_Accessory recycleView_adapter_bestSell_accessory;
    public CircleIndicator3 mCircleIndicator3;
    List<Promotion> mListPromotion = new ArrayList<>();
    ViewPager_Adapter_QC viewPager_adapter_accessory;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mViewPager2.getCurrentItem() == mListPromotion.size()-1){
                mViewPager2.setCurrentItem(0);
            }else {
                mViewPager2.setCurrentItem(mViewPager2.getCurrentItem() + 1);
            }
        }
    };

    public TabFragmentAccessory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabFragmentAccessory.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFragmentAccessory newInstance(String param1, String param2) {
        TabFragmentAccessory fragment = new TabFragmentAccessory();
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

        mView = inflater.inflate(R.layout.fragment_tab_accessory, container, false);
        initUI();
        loadData();
        loadItemBestSell();
        mainActivity = (MainActivity) getActivity();
        shoppingCart();
        viewPager_adapter_accessory = new ViewPager_Adapter_QC(mListPromotion, getContext());
        mViewPager2.setAdapter(viewPager_adapter_accessory);
        mCircleIndicator3.setViewPager(mViewPager2);
        viewPager_adapter_accessory.registerAdapterDataObserver(mCircleIndicator3.getAdapterDataObserver());
        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,3000);
            }
        });
        recycleView_adapter_bestSell_accessory = new RecycleView_Adapter_BestSell_Accessory(accessories, getContext(), new ItemClickAccessory() {
            @Override
            public void OnClickAccessory(Accessory accessory) {
                MainActivity mainActivity = (MainActivity) getActivity();
                User user = mainActivity.user;

                Bundle bundle = new Bundle();
                Intent intent = new Intent(getContext(), item_Information.class);
                bundle.putSerializable("User_inf", user);
                bundle.putSerializable("Accessory_inf", accessory);
                intent.putExtra("data_frgAccessory", bundle);
                startActivity(intent);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rcvBestSell.setFocusable(false);
        rcvBestSell.setLayoutManager(linearLayoutManager);
        rcvBestSell.setAdapter(recycleView_adapter_bestSell_accessory);

        recycleView_adapter_suggestions_accesory = new RecycleView_Adapter_Suggestions_Accesory(getSuggestionItem(), getContext(), new ItemClickAccessory() {
            @Override
            public void OnClickAccessory(Accessory accessory) {
                MainActivity mainActivity = (MainActivity) getActivity();
                User user = mainActivity.user;

                Bundle bundle = new Bundle();
                Intent intent = new Intent(getContext(), item_Information.class);
                bundle.putSerializable("User_inf", user);
                bundle.putSerializable("Accessory_inf", accessory);
                intent.putExtra("data_frgAccessory", bundle);
                startActivity(intent);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rcvSuggestionItem.setFocusable(false);
        rcvSuggestionItem.setLayoutManager(gridLayoutManager);
        rcvSuggestionItem.setAdapter(recycleView_adapter_suggestions_accesory);
        rcvSuggestionItem.setNestedScrollingEnabled(false);
        return mView;
    }
    private void initUI(){
        mViewPager2         = mView.findViewById(R.id.view_pager2_QC_Accessory);
        mCircleIndicator3   = mView.findViewById(R.id.circle_indicator_QC_Accessory);
        rcvBestSell         = mView.findViewById(R.id.recycleView_tab_accessory);
        rcvSuggestionItem   = mView.findViewById(R.id.recycleView_tab_accessory_2);
        tvFilter            = mView.findViewById(R.id.tv_Search_tab);
        imgShoppingCart     = mView.findViewById(R.id.img_ShoppingCart_tab);
        tv                  = mView.findViewById(R.id.number_Shopping_Cart_tab);
    }
    private void loadData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata = firebaseDatabase.getReference("Promotional");
        mdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListPromotion.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Promotion promotion = ds.getValue(Promotion.class);
                    if (promotion.getLoaiSp().equals("Accessory")) {
                        mListPromotion.add(promotion);
                    }
                }
                viewPager_adapter_accessory.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadItemBestSell(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata = firebaseDatabase.getReference("Item/Accessory");
        Query query = mdata.orderByChild("phuKienDaBan");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                accessories.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Accessory accessory = ds.getValue(Accessory.class);
                    if (accessories.size()<=10) {
                        accessories.add(0, accessory);
                    }
                }
                recycleView_adapter_bestSell_accessory.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private ArrayList<Accessory> getSuggestionItem(){
        ArrayList<Accessory> itemArrayList = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata = firebaseDatabase.getReference("Item/Accessory");
        mdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemArrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Accessory accessory = ds.getValue(Accessory.class);
                    itemArrayList.add(0,accessory);
                }
                recycleView_adapter_suggestions_accesory.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return itemArrayList;
    }
    private void shoppingCart(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata = firebaseDatabase.getReference("Shopping_Cart");
        mdata.child(mainActivity.user.getUserName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() == 0){
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("0");
                }else {
                    tv.setVisibility(View.VISIBLE);
                    tv.setText(snapshot.getChildrenCount()+"");
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        imgShoppingCart.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(getContext(), shopping_Cart_Activity.class);
            bundle.putSerializable("User_inf", mainActivity.user);
            intent.putExtra("data_main", bundle);
            startActivity(intent);
        });
        tvFilter.setOnClickListener(view -> {

        });
    }
}