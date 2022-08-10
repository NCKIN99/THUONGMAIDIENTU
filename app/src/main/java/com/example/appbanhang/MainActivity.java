package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.appbanhang.Adapter.ViewPagerAdpterMain;
import com.example.appbanhang.Fragment.Fragment_AddItem;
import com.example.appbanhang.Fragment.Fragment_User_Item_State;
import com.example.appbanhang.Object.User;
import com.example.appbanhang.transformer.DepthPageTransformer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;
import java.util.zip.Inflater;


public class MainActivity extends AppCompatActivity {

    public ViewPager2 mViewPager2;
    public BottomNavigationView mBottomNavigationView;
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        Intent nhandl = getIntent();
        Bundle bundle = nhandl.getBundleExtra("DuLieu");
        if (bundle != null){
            user = (User) bundle.getSerializable("User_Object");
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mdata = firebaseDatabase.getReference("Users");
            mdata.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds: snapshot.getChildren()){
                        User user2 = ds.getValue(User.class);
                        if (user2.getUserName().equals(user.getUserName())){
                            user = user2;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
            String name = user1.getPhoneNumber().toString();
            String sdt = "0"+name.substring(3,12);
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mdata = firebaseDatabase.getReference("Users");
            mdata.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds: snapshot.getChildren()){
                        User user2 = ds.getValue(User.class);
                        if (sdt.equals(user2.getUserName())){
                            user = user2;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        ViewPagerAdpterMain viewPagerAdpterMain = new ViewPagerAdpterMain(this);
        mViewPager2.setAdapter(viewPagerAdpterMain);
        mViewPager2.setOffscreenPageLimit(1);// Load trước fragment
        mViewPager2.setPageTransformer(new DepthPageTransformer());
        mViewPager2.setUserInputEnabled(false);
        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_home){
                    mViewPager2.setCurrentItem(0);
                }
                else if (id == R.id.menu_MobilePhone){
                    mViewPager2.setCurrentItem(1);
                }
                else if (id == R.id.menu_Laptop){
                    mViewPager2.setCurrentItem(2);
                }
                else if (id == R.id.menu_accessory){
                    mViewPager2.setCurrentItem(3);
                }
                else {
                    mViewPager2.setCurrentItem(4);
                }

                return true;
            }
        });
        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_MobilePhone).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_Laptop).setChecked(true);
                        break;
                    case 3:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_accessory).setChecked(true);
                        break;
                    case 4:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_user).setChecked(true);
                        break;

                }
            }
        });



    }

    private void initUI(){
        mViewPager2           = findViewById(R.id.viewPager_main);
        mBottomNavigationView = findViewById(R.id.bottom_navigationMain);
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_search, menu);
//        MenuItem menuItem = menu.findItem(R.id.menu_shopping);
//        View actionView = menuItem.getActionView();
//        TextView tv = actionView.findViewById(R.id.textView_Shopping_cart_number);
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference mdata = firebaseDatabase.getReference("Shopping_Cart");
//        mdata.child(user.getUserName()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.getChildrenCount() == 0){
//                    tv.setVisibility(View.VISIBLE);
//                    tv.setText("0");
//                }else {
//                    tv.setVisibility(View.VISIBLE);
//                    tv.setText(snapshot.getChildrenCount()+"");
//                }
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//        actionView.setOnClickListener(view -> {
//            onOptionsItemSelected(menuItem);
//            Bundle bundle = new Bundle();
//            Intent intent = new Intent(this, shopping_Cart_Activity.class);
//            bundle.putSerializable("User_inf", user);
//            intent.putExtra("data_main", bundle);
//            startActivity(intent);
//        });
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }



}