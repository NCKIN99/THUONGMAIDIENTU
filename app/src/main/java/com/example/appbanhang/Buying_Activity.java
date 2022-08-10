package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appbanhang.Object.Accessory;
import com.example.appbanhang.Object.Item;
import com.example.appbanhang.Object.Promotion;
import com.example.appbanhang.Object.User;
import com.example.appbanhang.Object.Users_Item;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Calendar;

public class Buying_Activity extends AppCompatActivity {

    ImageView imgBack, imgShoppingCart, imgItem;
    TextView tvTenUser, tvSDTuser, tvAddresUser, tvItemName, tvItemBrand, tvItemCost
            , tvPromotionCost, tvSumCost, tvPromotionName, tvKM1, tvKM2, tvKM3, tvKM4
            , tvKM5, tvDatHang, tvItemCost2;
    LinearLayout linearLayout;
    RadioButton rdbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying);
        initUI();
        rdbtn.setChecked(true);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data_item_inf");
        Intent intent2 = getIntent();
        Bundle bundle2 = intent2.getBundleExtra("data_accessory_inf");
        if (bundle != null && bundle2 == null){
            Promotion promotion = (Promotion) bundle.getSerializable("promotion_item_inf");
            User user = (User) bundle.getSerializable("user_item_inf");
            Item item = (Item) bundle.getSerializable("item_item_inf");
            loadData(promotion, user, item, null);
            tvDatHang.setOnClickListener(view -> {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference mdata = firebaseDatabase.getReference("Item");
                mdata.child(item.getLoaiSp()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Item item1 = ds.getValue(Item.class);
                            if (item1.getKeyId().equals(item.getKeyId()) && Integer.parseInt(item1.getSoLuong()) > 0){
                                buyItem(promotion, user, item, null);
                            }else if (item1.getKeyId().equals(item.getKeyId()) && Integer.parseInt(item1.getSoLuong()) <= 0){
                                Toast.makeText(Buying_Activity.this, "Sản phẩm đã hết hàng", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            });


        }
        if (bundle == null && bundle2 != null){
            Promotion promotion = (Promotion) bundle2.getSerializable("promotion_accessory_inf");
            User user = (User) bundle2.getSerializable("user_accessory_inf");
            Accessory accessory = (Accessory) bundle2.getSerializable("accessory_item_inf");
            loadData(promotion, user, null, accessory);
            tvDatHang.setOnClickListener(view -> {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference mdata = firebaseDatabase.getReference("Item");
                mdata.child("Accessory").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Accessory accessory1 = ds.getValue(Accessory.class);
                            if (accessory.getKeyID().equals(accessory1.getKeyID()) && accessory.getSoLuongPK() > 0){
                                buyItem(promotion, user, null, accessory1);
                            }else if (accessory.getKeyID().equals(accessory1.getKeyID()) && accessory.getSoLuongPK() <= 0){
                                Toast.makeText(Buying_Activity.this, "Sản phẩm đã hết hàng", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            });

        }
        imgBack.setOnClickListener(view -> finish());
        imgShoppingCart.setVisibility(View.GONE);
    }

    private void buyItem(Promotion promotion, User user, Item item, Accessory accessory) {
        if (item != null && accessory == null){
            AlertDialog.Builder alertDialog =new AlertDialog.Builder(this);
            alertDialog.setMessage("Bạn có muốn mua sản phẩm này không?");
            alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Calendar calendar = Calendar.getInstance();
                    String DateTime = calendar.get(Calendar.DATE)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR);
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference mdata = firebaseDatabase.getReference("User_Item");
                    String key = mdata.child(user.getUserName()).push().getKey();
                    Users_Item users_item = new Users_Item(key, DateTime, user, item, null, promotion,
                            tvSumCost.getText().toString().trim(), "Offline","0","no");
                    mdata.child(user.getUserName()).child(key).setValue(users_item).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Buying_Activity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            });
            alertDialog.show();
        }
        if (item == null && accessory != null){
            AlertDialog.Builder alertDialog =new AlertDialog.Builder(this);
            alertDialog.setMessage("Bạn có muốn mua sản phẩm này không?");
            alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Calendar calendar = Calendar.getInstance();
                    String DateTime = calendar.get(Calendar.DATE)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR);
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference mdata = firebaseDatabase.getReference("User_Item");
                    String key = mdata.child(user.getUserName()).push().getKey();
                    Users_Item users_item = new Users_Item(key, DateTime, user, item, accessory, promotion,
                            tvSumCost.getText().toString().trim(), "Offline","0","no");
                    mdata.child(user.getUserName()).child(key).setValue(users_item).addOnSuccessListener(unused -> {
                        Toast.makeText(Buying_Activity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            });
            alertDialog.show();
        }
    }

    private void loadData(Promotion promotion, User user, Item item, Accessory accessory) {
        if (item != null && accessory == null) {
            if (promotion != null) {
                linearLayout.setVisibility(View.VISIBLE);
                tvPromotionName.setText(promotion.getTenGoiKM());
                if (!promotion.getKM1().equals("Ko")) {
                    tvKM1.setText(MessageFormat.format("Khuyến mãi 1: {0}", promotion.getKM1()));
                }
                if (!promotion.getKM2().equals("Ko")) {
                    tvKM2.setText(MessageFormat.format("Khuyến mãi 2: {0}", promotion.getKM2()));
                } else {
                    tvKM2.setVisibility(View.GONE);
                }
                if (!promotion.getKM3().equals("Ko")) {
                    tvKM3.setText(MessageFormat.format("Khuyến mãi 3: {0}", promotion.getKM3()));
                } else {
                    tvKM3.setVisibility(View.GONE);
                }
                if (!promotion.getKM4().equals("Ko")) {
                    tvKM4.setText(MessageFormat.format("Khuyến mãi 4: {0}", promotion.getKM4()));
                } else {
                    tvKM4.setVisibility(View.GONE);
                }
                if (!promotion.getKM5().equals("Ko")) {
                    tvKM5.setText(MessageFormat.format("Khuyến mãi 5: {0}", promotion.getKM5()));
                } else {
                    tvKM5.setVisibility(View.GONE);
                }
                if (promotion.getKieuGoiKM().equals("GiamGia")) {
                    tvKM1.setText(MessageFormat.format("Khuyến mãi 1: {0}", formatMoney(Integer.parseInt(promotion.getKM1()))));
                    tvPromotionCost.setText(MessageFormat.format("Tổng gói khuyến mãi: {0}", formatMoney(Integer.parseInt(promotion.getKM1()))));
                } else {
                    tvPromotionCost.setText("Tổng gói khuyến mãi: 0đ");
                }
            } else {
                tvPromotionCost.setText("Tổng gói khuyến mãi: 0đ");
            }
            tvItemName.setText(item.getTenItem());
            tvItemBrand.setText(item.getTenThuongHieu());
            tvItemCost.setText(formatMoney(item.getGiaSp()));
            Glide.with(getApplicationContext()).load(Uri.parse(item.getHinhAnhDaiDien())).into(imgItem);
            tvItemCost2.setText(MessageFormat.format("Tổng tiền hàng: {0}đ", item.getGiaSp()));
            if (promotion != null && promotion.getKieuGoiKM().equals("GiamGia")) {
                if ((item.getGiaSp() - Integer.parseInt(promotion.getKM1())) <= 0) {
                    tvSumCost.setText("0đ");
                } else {
                    tvSumCost.setText(formatMoney(item.getGiaSp() - Integer.parseInt(promotion.getKM1())));
                }
            } else {
                tvSumCost.setText(formatMoney(item.getGiaSp()));

            }
            tvTenUser.setText(MessageFormat.format("Người mua: {0}", user.getUserFullName()));
            tvSDTuser.setText(MessageFormat.format("Số điện thoại: {0}", user.getUserPhoneNumber()));
            tvAddresUser.setText(MessageFormat.format("Địa chỉ: {0}", user.getUserAddress()));
        }
        if (item == null && accessory != null) {
            if (promotion != null) {
                linearLayout.setVisibility(View.VISIBLE);
                tvPromotionName.setText(promotion.getTenGoiKM());
                if (!promotion.getKM1().equals("Ko")) {
                    tvKM1.setText(MessageFormat.format("Khuyến mãi 1: {0}", promotion.getKM1()));
                }
                if (!promotion.getKM2().equals("Ko")) {
                    tvKM2.setText(MessageFormat.format("Khuyến mãi 2: {0}", promotion.getKM2()));
                } else {
                    tvKM2.setVisibility(View.GONE);
                }
                if (!promotion.getKM3().equals("Ko")) {
                    tvKM3.setText(MessageFormat.format("Khuyến mãi 3: {0}", promotion.getKM3()));
                } else {
                    tvKM3.setVisibility(View.GONE);
                }
                if (!promotion.getKM4().equals("Ko")) {
                    tvKM4.setText(MessageFormat.format("Khuyến mãi 4: {0}", promotion.getKM4()));
                } else {
                    tvKM4.setVisibility(View.GONE);
                }
                if (!promotion.getKM5().equals("Ko")) {
                    tvKM5.setText(MessageFormat.format("Khuyến mãi 5: {0}", promotion.getKM5()));
                } else {
                    tvKM5.setVisibility(View.GONE);
                }
                if (promotion.getKieuGoiKM().equals("GiamGia")) {
                    tvKM1.setText(MessageFormat.format("Khuyến mãi 1: {0}", formatMoney(Integer.parseInt(promotion.getKM1()))));
                    tvPromotionCost.setText(MessageFormat.format("Tổng gói khuyến mãi: {0}đ", promotion.getKM1()));
                } else {
                    tvPromotionCost.setText("Tổng gói khuyến mãi: 0đ");
                }
            } else {
                tvPromotionCost.setText("Tổng gói khuyến mãi: 0đ");
            }
            tvItemName.setText(accessory.getTenPhuKien());
            tvItemBrand.setText(accessory.getHangPhuKien());
            tvItemCost.setText(formatMoney(accessory.getGiaPhuKien()));
            Glide.with(getApplicationContext()).load(Uri.parse(accessory.getHinhAnhPK())).into(imgItem);
            tvItemCost2.setText(MessageFormat.format("Tổng tiền hàng: {0}đ", accessory.getGiaPhuKien()));
            if (promotion != null && promotion.getKieuGoiKM().equals("GiamGia")) {
                if ((accessory.getGiaPhuKien() - Integer.parseInt(promotion.getKM1())) <= 0) {
                    tvSumCost.setText("0");
                } else {
                    tvSumCost.setText(accessory.getGiaPhuKien() - Integer.parseInt(promotion.getKM1())+"");
                }
            } else {
                tvSumCost.setText(formatMoney(accessory.getGiaPhuKien()));

            }
            tvTenUser.setText(MessageFormat.format("Người mua: {0}", user.getUserFullName()));
            tvSDTuser.setText(MessageFormat.format("Số điện thoại: {0}", user.getUserPhoneNumber()));
            tvAddresUser.setText(MessageFormat.format("Địa chỉ: {0}", user.getUserAddress()));
        }
    }
    public String formatMoney(int  i){String pattern = "###,###.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(i)+"đ";


    }
    private void initUI() {
    tvPromotionCost     = findViewById(R.id.tvItemPromotionCost_item_buying);
    tvPromotionName     = findViewById(R.id.tvPromotion_Name);
    tvKM1               = findViewById(R.id.tvPromotion_KM1);
    tvKM2               = findViewById(R.id.tvPromotion_KM2);
    tvKM3               = findViewById(R.id.tvPromotion_KM3);
    tvKM4               = findViewById(R.id.tvPromotion_KM4);
    tvKM5               = findViewById(R.id.tvPromotion_KM5);
    tvTenUser           = findViewById(R.id.tvUserName_item_buying);
    tvSDTuser           = findViewById(R.id.tvUserPhoneNumber_item_buying);
    tvAddresUser        = findViewById(R.id.tvUserAddress_item_buying);
    tvItemName          = findViewById(R.id.tvItemName_item_buying);
    tvItemBrand         = findViewById(R.id.tvItemBrand_item_buying);
    tvItemCost          = findViewById(R.id.tvItemCost_item_buying);
    tvSumCost           = findViewById(R.id.tvItemSumCost_item_buying);
    tvItemCost2         = findViewById(R.id.tvItemCost2_item_buying);
    tvDatHang           = findViewById(R.id.textView_Bottom_Buy_Now);

    imgBack             = findViewById(R.id.item_Information_Exit);
    imgShoppingCart     = findViewById(R.id.img_ShoppingCart);
    imgItem             = findViewById(R.id.imgItem_item_buying);

    linearLayout        = findViewById(R.id.linear_Promotion);

    rdbtn               = findViewById(R.id.rdbtn_item_buying);
    }

}