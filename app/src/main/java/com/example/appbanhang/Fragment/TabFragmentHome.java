package com.example.appbanhang.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import me.relex.circleindicator.CircleIndicator3;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabFragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragmentHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View mView;
    ViewPager2 mViewPager2;
    TextView tvFilter, tv;
    ImageView imgShoppingCart;
    MainActivity mainActivity;
    private RecyclerView_Adapter_BestSell recyclerView_adapter_bestSell;
    private RecycleView_Adpater_SuggestionsItem recycleView_adpater_suggestionsItem;
    private RecyclerView rcvBestSell, rcvSuggestionItem;
    CircleIndicator3 circleIndicator3;
    ArrayList<Promotion> mListPromotion = new ArrayList<>();
    ViewPager_Adapter_QC viewPager_adapter_home;
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

    public TabFragmentHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabFragmentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFragmentHome newInstance(String param1, String param2) {
        TabFragmentHome fragment = new TabFragmentHome();
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
        mView = inflater.inflate(R.layout.fragment_tab_home, container, false);
        initUI();
        loadData();
        mainActivity = (MainActivity) getActivity();
        shoppingCart();
        viewPager_adapter_home = new ViewPager_Adapter_QC(mListPromotion, getContext());
        mViewPager2.setAdapter(viewPager_adapter_home);
        circleIndicator3.setViewPager(mViewPager2);
        viewPager_adapter_home.registerAdapterDataObserver(circleIndicator3.getAdapterDataObserver());
        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,3000);
            }
        });

        recyclerView_adapter_bestSell = new RecyclerView_Adapter_BestSell(loadDataPhone(), getContext(), new RecyclerView_Adapter_BestSell.OnItemClickListener() {
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
        rcvBestSell.setFocusable(false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        rcvBestSell.addItemDecoration(dividerItemDecoration);
        rcvBestSell.setLayoutManager(linearLayoutManager);
        rcvBestSell.setAdapter(recyclerView_adapter_bestSell);
        rcvBestSell.setNestedScrollingEnabled(false);

        recycleView_adpater_suggestionsItem = new RecycleView_Adpater_SuggestionsItem(getSuggestionItem(), getContext(), item -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            User user = mainActivity.user;

            Bundle bundle = new Bundle();
            Intent intent = new Intent(getContext(), item_Information.class);
            bundle.putSerializable("User_inf", user);
            bundle.putSerializable("Item_inf", item);
            intent.putExtra("data_frgLap", bundle);
            startActivity(intent);
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rcvSuggestionItem.setFocusable(false);
        rcvSuggestionItem.setLayoutManager(gridLayoutManager);
        rcvSuggestionItem.setAdapter(recycleView_adpater_suggestionsItem);
        rcvSuggestionItem.setNestedScrollingEnabled(false);

        return mView;
    }

    private void initUI(){
        mViewPager2 = mView.findViewById(R.id.view_pager2_QC_Home);
        circleIndicator3 = mView.findViewById(R.id.circle_indicator_QC_Home);
        rcvBestSell     = mView.findViewById(R.id.recycleView_tab_home);
        rcvSuggestionItem   = mView.findViewById(R.id.recycleView_tab_home_2);
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
                        mListPromotion.add(promotion);
                }
                viewPager_adapter_home.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private ArrayList<Item> loadDataPhone(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata = firebaseDatabase.getReference("Item");
        ArrayList<Item> itemsPhone  = new ArrayList<>();
        mdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemsPhone.clear();
                for (DataSnapshot ds: snapshot.child("Laptop").getChildren()){
                    Item item = ds.getValue(Item.class);
                    itemsPhone.add(item);
                }
                Collections.sort(itemsPhone, (item, t1) -> t1.getDaBan() - item.getDaBan());
                for (DataSnapshot ds: snapshot.child("Phone").getChildren()){
                    Item item = ds.getValue(Item.class);
                    itemsPhone.add(item);
                }
                Collections.sort(itemsPhone, (item, t1) -> t1.getDaBan() - item.getDaBan());
                recyclerView_adapter_bestSell.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return itemsPhone;
    }
    private ArrayList<Item> getSuggestionItem(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata = firebaseDatabase.getReference("Item");
        ArrayList<Item> itemsPhone  = new ArrayList<>();
        mdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemsPhone.clear();
                for (DataSnapshot ds: snapshot.child("Laptop").getChildren()){
                    Item item = ds.getValue(Item.class);
                    itemsPhone.add(item);
                }
                for (DataSnapshot ds: snapshot.child("Phone").getChildren()){
                    Item item = ds.getValue(Item.class);
                    itemsPhone.add(item);
                }
                Collections.shuffle(itemsPhone);
                recycleView_adpater_suggestionsItem.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return itemsPhone;
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