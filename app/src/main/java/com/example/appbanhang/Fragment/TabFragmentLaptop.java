package com.example.appbanhang.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appbanhang.Adapter.RecycleView_Adpater_SuggestionsItem;
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
import com.example.appbanhang.shopping_Cart_Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabFragmentLaptop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragmentLaptop extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public ViewPager2 mViewPager2;
    public CircleIndicator3 mCircleIndicator3;
    TextView tvFilter, tv;
    ImageView imgShoppingCart;
    View mView;
    RecyclerView rcvbestSell, revSuggsetItem;
    List<Promotion> mListPromotion = new ArrayList<>();
    MainActivity mainActivity;
    ArrayList<Item> items = new ArrayList<>();
    ViewPager_Adapter_QC photoViewPager_qc;
    RecyclerView_Adapter_BestSell recyclerView_adapter_bestSell;
    RecycleView_Adpater_SuggestionsItem recycleView_adpater_suggestionsItem;

    int i = 10;
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TabFragmentLaptop() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabFragmentLaptop.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFragmentLaptop newInstance(String param1, String param2) {
        TabFragmentLaptop fragment = new TabFragmentLaptop();
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
        mView = inflater.inflate(R.layout.fragment_tab_laptop, container, false);
        initUI();
        loadData();
        loadItemBestSell();
        mainActivity = (MainActivity) getActivity();
        shoppingCart();
        photoViewPager_qc = new ViewPager_Adapter_QC(mListPromotion, getContext());
        mViewPager2.setAdapter(photoViewPager_qc);
        mCircleIndicator3.setViewPager(mViewPager2);
        photoViewPager_qc.registerAdapterDataObserver(mCircleIndicator3.getAdapterDataObserver());
        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,3000);
            }
        });
        recyclerView_adapter_bestSell = new RecyclerView_Adapter_BestSell(items, getContext(), new RecyclerView_Adapter_BestSell.OnItemClickListener() {
            @Override
            public void OnClickItem(Item item) {
                MainActivity mainActivity = (MainActivity) getActivity();
                User user = mainActivity.user;

                Bundle bundle = new Bundle();
                Intent intent = new Intent(getContext(), item_Information.class);
                bundle.putSerializable("User_inf", user);
                bundle.putSerializable("Item_inf", item);
                intent.putExtra("data_frgLap", bundle);
                startActivity(intent);
            }


        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rcvbestSell.setFocusable(false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        rcvbestSell.addItemDecoration(dividerItemDecoration);
        rcvbestSell.setLayoutManager(linearLayoutManager);
        rcvbestSell.setAdapter(recyclerView_adapter_bestSell);
        rcvbestSell.setNestedScrollingEnabled(false);


        recycleView_adpater_suggestionsItem = new RecycleView_Adpater_SuggestionsItem(getSuggestionItem(), getContext(), new RecycleView_Adpater_SuggestionsItem.OnItemClick() {
            @Override
            public void OnSendDataItem(Item item) {
                MainActivity mainActivity = (MainActivity) getActivity();
                User user = mainActivity.user;

                Bundle bundle = new Bundle();
                Intent intent = new Intent(getContext(), item_Information.class);
                bundle.putSerializable("User_inf", user);
                bundle.putSerializable("Item_inf", item);
                intent.putExtra("data_frgLap", bundle);
                startActivity(intent);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        revSuggsetItem.setFocusable(false);
        revSuggsetItem.setLayoutManager(gridLayoutManager);
        revSuggsetItem.setAdapter(recycleView_adpater_suggestionsItem);
        revSuggsetItem.setNestedScrollingEnabled(false);
        return mView;

    }
    private void initUI(){
        mViewPager2         = mView.findViewById(R.id.view_pager2_QC);
        mCircleIndicator3   = mView.findViewById(R.id.circle_indicator_QC);
        rcvbestSell         = mView.findViewById(R.id.recycleView_tab_laptop);
        revSuggsetItem      = mView.findViewById(R.id.recycleView_tab_laptop_2);
        tvFilter            = mView.findViewById(R.id.tv_Search_tab);
        imgShoppingCart     = mView.findViewById(R.id.img_ShoppingCart_tab);
        tv                  = mView.findViewById(R.id.number_Shopping_Cart_tab);
    }
    private void loadData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata = firebaseDatabase.getReference("Promotional");
        mdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListPromotion.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Promotion promotion = ds.getValue(Promotion.class);
                    if (promotion.getLoaiSp().equals("Laptop")){
                        mListPromotion.add(promotion);
                    }
                }
                photoViewPager_qc.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadItemBestSell(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata = firebaseDatabase.getReference("Item/Laptop");
        Query query = mdata.orderByChild("daBan");
        query.limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Item item = ds.getValue(Item.class);
                    items.add(0, item);
                }
                recyclerView_adapter_bestSell.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private ArrayList<Item> getSuggestionItem(){
        ArrayList<Item> itemArrayList = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata = firebaseDatabase.getReference("Item/Laptop");
        mdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemArrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Item item = ds.getValue(Item.class);
                    itemArrayList.add(0,item);
                }
                recycleView_adpater_suggestionsItem.notifyDataSetChanged();
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
            Bundle bundle = new Bundle();
            Intent intent = new Intent(getContext(), Filter_Item.class);
            bundle.putSerializable("User_inf", mainActivity.user);
            intent.putExtra("data_main", bundle);
            startActivity(intent);
        });

    }


}