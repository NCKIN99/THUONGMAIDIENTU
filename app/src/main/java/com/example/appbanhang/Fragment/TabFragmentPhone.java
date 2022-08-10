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
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabFragmentPhone#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragmentPhone extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View mView;
    RecyclerView recyclerView, recyclerView2;
    TextView tvFilter, tv;
    ImageView imgShoppingCart;
    RecyclerView_Adapter_BestSell recyclerView_adapter_bestSell;
    private RecycleView_Adpater_SuggestionsItem recycleView_adpater_suggestionsItem;
    ArrayList<Item> items = new ArrayList<>();
    public ViewPager2 mViewPager2;
    public CircleIndicator3 mCircleIndicator3;
    MainActivity mainActivity;
    List<Promotion> mListPromotion = new ArrayList<>();
    ViewPager_Adapter_QC photoViewPager_qc;
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

    public TabFragmentPhone() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabFragmentPhone.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFragmentPhone newInstance(String param1, String param2) {
        TabFragmentPhone fragment = new TabFragmentPhone();
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
        mainActivity = (MainActivity) getActivity();
        mView= inflater.inflate(R.layout.fragment_tab_phone, container, false);
        initUI();
        loadData();
        loadItemBestSell();
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
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerView_adapter_bestSell);
        recyclerView.setNestedScrollingEnabled(false);

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
        recyclerView2.setFocusable(false);
        recyclerView2.setLayoutManager(gridLayoutManager);
        recyclerView2.setAdapter(recycleView_adpater_suggestionsItem);
        recyclerView2.setNestedScrollingEnabled(false);
        return mView;
    }
    private void initUI(){
        mViewPager2         = mView.findViewById(R.id.view_pager2_QC_Phone);
        mCircleIndicator3   = mView.findViewById(R.id.circle_indicator_QC_Phone);
        recyclerView        = mView.findViewById(R.id.recycleView_tab_phone);
        recyclerView2       = mView.findViewById(R.id.recycleView_tab_phone_2);
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
                    if (promotion.getLoaiSp().equals("Phone")){
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
        DatabaseReference mdata = firebaseDatabase.getReference("Item/Phone");
        Query query = mdata.orderByChild("daBan");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Item item = ds.getValue(Item.class);
                    if (items.size()<=10) {
                        items.add(0, item);
                    }


                }
                recyclerView_adapter_bestSell.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private ArrayList<Item> getSuggestionItem() {
        ArrayList<Item> itemArrayList = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata = firebaseDatabase.getReference("Item/Phone");
        mdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
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