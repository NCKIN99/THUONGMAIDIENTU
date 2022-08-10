package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.appbanhang.Adapter.Filter_Item_Adapter;
import com.example.appbanhang.Adapter.ListItemAddorEditAdapter;
import com.example.appbanhang.Object.Accessory;
import com.example.appbanhang.Object.Item;
import com.example.appbanhang.Object.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Filter_Item extends AppCompatActivity {

    RecyclerView rcvFilterItem;
    Filter_Item_Adapter filter_item_adapter;
    ArrayList<Item> arrItem = new ArrayList<>();
    SearchView searchView;
    MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_item);
        Intent nhandl = getIntent();
        Bundle bundle = nhandl.getBundleExtra("data_main");
        if (bundle != null){
            initUI();
            loadData();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            rcvFilterItem.addItemDecoration(dividerItemDecoration);
            rcvFilterItem.setLayoutManager(linearLayoutManager);
            rcvFilterItem.setFocusable(false);
            filter_item_adapter = new Filter_Item_Adapter(getApplicationContext(), arrItem, item -> {
                Bundle bundle2 = new Bundle();
                Intent intent = new Intent(this, item_Information.class);
                bundle2.putSerializable("User_inf", bundle.getSerializable("User_inf"));
                bundle2.putSerializable("Item_inf", item);
                intent.putExtra("data_frgLap", bundle2);
                startActivity(intent);
            });

            rcvFilterItem.setAdapter(filter_item_adapter);
        }

    }

    private void loadData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata = firebaseDatabase.getReference("Item");
        mdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mdata.child("Phone").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrItem.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Item item = ds.getValue(Item.class);
                            arrItem.add(item);
                        }
                        filter_item_adapter.notifyDataSetChanged();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                mdata.child("Laptop").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Item item = ds.getValue(Item.class);
                            arrItem.add(item);
                        }
                    filter_item_adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initUI() {
        rcvFilterItem   = findViewById(R.id.recyclerView_filter_item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter_item_adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter_item_adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_Exit){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}