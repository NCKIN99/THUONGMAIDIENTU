package com.example.appbanhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang.Adapter.ViewPager_Adapter_QC;
import com.example.appbanhang.Adapter.viewPager2_Adapter_Item_Information;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class item_Information extends AppCompatActivity {

    TextView tvTenSp, tvGiaKMSp, tvGiaSp, tvTenSp2, tvHangSP, tvCUP, tvBoNho
            , tvManHinh, tvRam, tvPin, tvCongKN, tvCamera, tvMuaNgay, tvDaban
            , tvThongTin, tvImgNumber, tvKM1, tvKM2, tvcpu, tvShoppingCart, tvConLai;
    LinearLayout tvbonho, tvmanhinh, tvram, tvpin, tvcongkn, tvcamera;
    private RadioGroup rdg;
    private RadioButton rd1, rd2;
    ImageView imgBack, imgbtmShoppingCart, imgShoppingCart;
    ViewPager2 viewPager2;
    List<Uri> mList = new ArrayList<>();
    List<Uri> mList2 = new ArrayList<>();
    ProgressDialog progressBar;
    viewPager2_Adapter_Item_Information viewPager2_adapter_item_information;
    User user;
    Item item;
    Accessory accessory;
    Promotion promotion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_information);
        intUI();
        Intent nhandl = getIntent();
        Bundle bundle = nhandl.getBundleExtra("data_frgLap");
        Intent nhandl2 = getIntent();
        Bundle bundle2 = nhandl2.getBundleExtra("data_frgAccessory");
        if (bundle != null && bundle2 == null) {
            user = (User) bundle.getSerializable("User_inf");
            item = (Item) bundle.getSerializable("Item_inf");
            ShoppingCartNumber(user.getUserName());
            loadData(bundle, bundle2);
            tvMuaNgay.setOnClickListener(view -> {
                if (rd1.isChecked()){
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference mdata = firebaseDatabase.getReference("Promotional");
                    mdata.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds: snapshot.getChildren()){
                                Promotion promotion1 = ds.getValue(Promotion.class);
                                if (promotion1.getTenGoiKM().equals(rd1.getText().toString().trim())) {
                                    Promotion promotion2 = promotion1;
                                    Intent intent = new Intent(item_Information.this, Buying_Activity.class);
                                    Bundle bundle1 = new Bundle();
                                    bundle1.putSerializable("promotion_item_inf", promotion2);
                                    bundle1.putSerializable("user_item_inf", user);
                                    bundle1.putSerializable("item_item_inf", item);
                                    intent.putExtra("data_item_inf", bundle1);
                                    startActivity(intent);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else if (rd2.isChecked()){
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference mdata = firebaseDatabase.getReference("Promotional");
                    mdata.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds: snapshot.getChildren()){
                                Promotion promotion1 = ds.getValue(Promotion.class);
                                if (promotion1.getTenGoiKM().equals(rd2.getText().toString().trim())) {
                                    Promotion promotion2 = promotion1;
                                    Intent intent = new Intent(item_Information.this, Buying_Activity.class);
                                    Bundle bundle1 = new Bundle();
                                    bundle1.putSerializable("promotion_item_inf", promotion2);
                                    bundle1.putSerializable("user_item_inf", user);
                                    bundle1.putSerializable("item_item_inf", item);
                                    intent.putExtra("data_item_inf", bundle1);
                                    startActivity(intent);

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                else{
                        Intent intent = new Intent(item_Information.this, Buying_Activity.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("user_item_inf", user);
                        bundle1.putSerializable("item_item_inf", item);
                        intent.putExtra("data_item_inf", bundle1);
                        startActivity(intent);

                }
            });
            tvConLai.setText(item.getSoLuong());
            tvTenSp.setText(item.getTenItem());
            tvGiaKMSp.setText(formatMoney(item.getGiaSp()));
            tvTenSp2.setText(item.getTenItem());
            tvHangSP.setText(item.getTenThuongHieu());
            tvDaban.setText(formatValue(item.getDaBan()));
            tvCUP.setText(item.getCpu());
            tvBoNho.setText(item.getBoNhoTrong());
            tvManHinh.setText(item.getKichThuocManHinh());
            tvRam.setText(item.getRam());
            tvPin.setText(item.getPin());
            tvCongKN.setText(item.getCongKetNoi());
            tvCamera.setText(MessageFormat.format("Camera sau: {0}\nCamera truoc: {1}", item.getCameraSau(), item.getCameraTrc()));
            tvThongTin.setText(item.getThongTin());
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference(item.getKeyId());
            mList.clear();
            for (int i=0; i<item.getSoHinhAnh();i++){
                storageReference.child(i+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        mList.add(uri);
                        viewPager2_adapter_item_information.notifyDataSetChanged();
                    }
                });
            }
            viewPager2_adapter_item_information = new viewPager2_Adapter_Item_Information(mList, getApplicationContext());
            viewPager2.setAdapter(viewPager2_adapter_item_information);
            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    int nb= position+1;
                    tvImgNumber.setText(nb+"/"+item.getSoHinhAnh());
                }
            });
            imgbtmShoppingCart.setOnClickListener(view -> {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setMessage("Thêm vào giỏ hàng?");
                alertDialog.setNegativeButton("Không", (dialogInterface, i) -> {

                });
                alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
                        DatabaseReference mdata = firebaseDatabase.getReference("Shopping_Cart");
                        String key = mdata.child(user.getUserName()).push().getKey();
                        Users_Item users_item = new Users_Item(key, null, user, item, null, promotion,
                                null, "Offline","0","no");
                        mdata.child(user.getUserName()).child(key).setValue(users_item).addOnSuccessListener(unused -> {
                            Toast.makeText(item_Information.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
                alertDialog.show();
            });
        }
        if (bundle == null && bundle2 != null) {
            user = (User) bundle2.getSerializable("User_inf");
            accessory = (Accessory) bundle2.getSerializable("Accessory_inf");
            loadData(bundle, bundle2);
            ShoppingCartNumber(user.getUserName());
            tvMuaNgay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rd1.isChecked()){
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference mdata = firebaseDatabase.getReference("Promotional");
                        mdata.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds: snapshot.getChildren()){
                                    Promotion promotion1 = ds.getValue(Promotion.class);
                                    if (promotion1.getTenGoiKM().equals(rd1.getText().toString().trim())) {
                                        Promotion promotion2 = promotion1;
                                        Intent intent = new Intent(item_Information.this, Buying_Activity.class);
                                        Bundle bundle1 = new Bundle();
                                        bundle1.putSerializable("promotion_accessory_inf", promotion2);
                                        bundle1.putSerializable("user_accessory_inf", user);
                                        bundle1.putSerializable("accessory_item_inf", accessory);
                                        intent.putExtra("data_accessory_inf", bundle1);
                                        startActivity(intent);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }else if (rd2.isChecked()){
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference mdata = firebaseDatabase.getReference("Promotional");
                        mdata.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds: snapshot.getChildren()){
                                    Promotion promotion1 = ds.getValue(Promotion.class);
                                    if (promotion1.getTenGoiKM().equals(rd2.getText().toString().trim())) {
                                        Promotion promotion2 = promotion1;
                                        Intent intent = new Intent(item_Information.this, Buying_Activity.class);
                                        Bundle bundle1 = new Bundle();
                                        bundle1.putSerializable("promotion_accessory_inf", promotion2);
                                        bundle1.putSerializable("user_accessory_inf", user);
                                        bundle1.putSerializable("accessory_item_inf", accessory);
                                        intent.putExtra("data_accessory_inf", bundle1);
                                        startActivity(intent);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }else {
                        Intent intent = new Intent(item_Information.this, Buying_Activity.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("user_accessory_inf", user);
                        bundle1.putSerializable("accessory_item_inf", accessory);
                        intent.putExtra("data_accessory_inf", bundle1);
                        startActivity(intent);
                    }
                }
            });
            tvConLai.setText(accessory.getSoLuongPK()+"");
            tvTenSp.setText(accessory.getTenPhuKien());
            tvGiaKMSp.setText(formatMoney(accessory.getGiaPhuKien()));
            tvTenSp2.setText(accessory.getTenPhuKien());
            tvHangSP.setText(accessory.getHangPhuKien());
            tvDaban.setText(formatValue(accessory.getPhuKienDaBan()));
            tvCUP.setText(accessory.getLoaiPhuKien());
            tvcpu.setText("Loại phụ kiện:");
            tvbonho.setVisibility(View.GONE);
            tvmanhinh.setVisibility(View.GONE);
            tvram.setVisibility(View.GONE);
            tvpin.setVisibility(View.GONE);
            tvcongkn.setVisibility(View.GONE);
            tvcamera.setVisibility(View.GONE);
            tvThongTin.setText(accessory.getThongTinPhuKien());
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference(accessory.getKeyID());
            mList2.clear();
            for (int i=0; i<accessory.getSoHinhAnh();i++){
                storageReference.child(i+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        mList2.add(uri);
                        viewPager2_adapter_item_information.notifyDataSetChanged();
                    }
                });
            }
            viewPager2_adapter_item_information = new viewPager2_Adapter_Item_Information(mList2, getApplicationContext());
            viewPager2.setAdapter(viewPager2_adapter_item_information);
            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    int nb= position+1;
                    tvImgNumber.setText(nb+"/"+accessory.getSoHinhAnh());
                }
            });
            imgbtmShoppingCart.setOnClickListener(view -> {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setMessage("Thêm vào giỏ hàng?");
                alertDialog.setNegativeButton("Không", (dialogInterface, i) -> {
                });
                alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
                        DatabaseReference mdata = firebaseDatabase.getReference("Shopping_Cart");
                        String key = mdata.child(user.getUserName()).push().getKey();
                        Users_Item users_item = new Users_Item(key, null, user, item, null, promotion,
                                null, "Offline","0","no");
                        mdata.child(user.getUserName()).child(key).setValue(users_item).addOnSuccessListener(unused -> {
                            Toast.makeText(item_Information.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
                alertDialog.show();
            });
        }
        imgBack.setOnClickListener(view -> super.onBackPressed());


    }

    private void intUI() {
        tvTenSp         = findViewById(R.id.textView_TenSp_Item_Information);
        tvGiaKMSp       = findViewById(R.id.textView_GiaSp_Item_Information);
        tvGiaSp         = findViewById(R.id.textView_GiaCu_Item_Information);
        tvTenSp2        = findViewById(R.id.textView_TenSp2_Item_Information);
        tvHangSP        = findViewById(R.id.textView_HangSp_Item_Information);
        tvCUP           = findViewById(R.id.textView_CPU_Item_Information);
        tvBoNho         = findViewById(R.id.textView_BoNho_Item_Information);
        tvManHinh       = findViewById(R.id.textView_ManHinh_Item_Information);
        tvRam           = findViewById(R.id.textView_Ram_Item_Information);
        tvPin           = findViewById(R.id.textView_Pin_Item_Information);
        tvCongKN        = findViewById(R.id.textView_CongKetNoi_Item_Information);
        tvCamera        = findViewById(R.id.textView_Camera_Item_Information);
        tvMuaNgay       = findViewById(R.id.textView_Bottom_Buy_Now);
        tvDaban         = findViewById(R.id.Daban_Item_Information);
        tvThongTin      = findViewById(R.id.textView_ThongTin_Item_Information);
        tvImgNumber     = findViewById(R.id.textView_img_Count);
        rdg             = findViewById(R.id.radioGroup_Item_Information);
        rd1             = findViewById(R.id.radioButton_KM1_Item_Information);
        rd2             = findViewById(R.id.radioButton_KM2_Item_Information);
        tvKM1           = findViewById(R.id.textView_KM1_Item_Information);
        tvKM2           = findViewById(R.id.textView_KM2_Item_Information);
        tvcpu           = findViewById(R.id.CPU_Item_Information);
        tvbonho         = findViewById(R.id.BoNho_Item_Information);
        tvmanhinh       = findViewById(R.id.ManHinh_Item_Information);
        tvram           = findViewById(R.id.Ram_Item_Information);
        tvpin           = findViewById(R.id.Pin_Item_Information);
        tvcongkn        = findViewById(R.id.CongKetNoi_Item_Information);
        tvcamera        = findViewById(R.id.Camera_Item_Information);
        tvShoppingCart  = findViewById(R.id.number_Shopping_Cart);
        tvConLai        = findViewById(R.id.ConLai_Item_Information);

        imgBack                 = findViewById(R.id.item_Information_Exit);
        imgbtmShoppingCart      = findViewById(R.id.ImageView_Bottom_Shopping_Cart);
        viewPager2              = findViewById(R.id.view_pager2_Item_Information);
        imgShoppingCart         = findViewById(R.id.img_ShoppingCart);
    }

    private void ShoppingCartNumber(String userName){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata = firebaseDatabase.getReference("Shopping_Cart");
        mdata.child(userName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() == 0){
                    tvShoppingCart.setVisibility(View.GONE);
                }else {
                    tvShoppingCart.setVisibility(View.VISIBLE);
                    tvShoppingCart.setText(snapshot.getChildrenCount()+"");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        imgShoppingCart.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(this, shopping_Cart_Activity.class);
            bundle.putSerializable("User_inf", user);
            intent.putExtra("data_main", bundle);
            startActivity(intent);
        });
    }


    public String formatValue(double value) {
        int power;
        if (value == 0){
            return "0";
        }
        String suffix = " kmbt";
        String formattedNumber = "";

        NumberFormat formatter = new DecimalFormat("#,###.#");
        power = (int)StrictMath.log10(value);
        value = value/(Math.pow(10,(power/3)*3));
        formattedNumber=formatter.format(value);
        formattedNumber = formattedNumber + suffix.charAt(power/3);
        return formattedNumber.length()>4 ?  formattedNumber.replaceAll("\\.[0-9]+", "") : formattedNumber;
    }
    public String formatMoney(int  i){
        String pattern = "###,###.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String format = decimalFormat.format(i) + " đ";
        return format;
    }
    public void loadData(Bundle bundle, Bundle bundle2){
        progressBar = new ProgressDialog(this);
        //progressBar.show();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mdata = firebaseDatabase.getReference("Promotional");
        mdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    promotion = ds.getValue(Promotion.class);
                    String bd = promotion.getNgayBDKM();
                    String kt = promotion.getNgayKTKM();
                    String[] S2 = bd.split("/");
                    String[] P2 = kt.split("/");
                    Calendar calendar2 = Calendar.getInstance();
                    Calendar calendar = Calendar.getInstance();
                    Calendar calendar1 = Calendar.getInstance();
                    calendar.set(Integer.parseInt(S2[2]), Integer.parseInt(S2[1])-1, Integer.parseInt(S2[0]));
                    calendar1.set(Integer.parseInt(P2[2]), Integer.parseInt(P2[1])-1, Integer.parseInt(P2[0]));
                    if ((calendar2.getTimeInMillis()-calendar1.getTimeInMillis())/(1000*60*60*24) <= 0 &&
                            (calendar2.getTimeInMillis() - calendar.getTimeInMillis())/(1000*60*60*24) >=0) {
                        if (bundle != null && bundle2 == null) {
                            if (promotion.getLoaiSp().equals(item.getLoaiSp())) {
                                String string = promotion.getSpKm();
                                String[] parts = string.split("-");
                                String p0 = parts[0];
                                for (int i = 1; i < Integer.parseInt(p0) + 1; i++) {
                                    String p = parts[i].trim();
                                    if (item.getKeyId().equals("-" + p)) {
                                        if (promotion.getKieuGoiKM().equals("GiamGia")) {
                                            rd1.setVisibility(View.VISIBLE);
                                            tvKM1.setVisibility(View.VISIBLE);
                                            tvKM1.setText("");
                                            rd1.setChecked(true);
                                            rd1.setText(promotion.getTenGoiKM());
                                            tvKM1.setText("Giảm: " + formatMoney(Integer.parseInt(promotion.getKM1())));
                                            tvKM1.append("\n" + "Từ: " + promotion.getNgayBDKM() + " - " + promotion.getNgayKTKM());

                                        }
                                        if (promotion.getKieuGoiKM().equals("KhuyenMai")) {
                                            rd2.setVisibility(View.VISIBLE);
                                            tvKM2.setVisibility(View.VISIBLE);
                                            tvKM2.setText("");
                                            rd2.setText(promotion.getTenGoiKM());
                                            if (!promotion.getKM1().equals("Ko")) {
                                                tvKM2.append("Tặng: " + promotion.getKM1() + "\n");
                                            }
                                            if (!promotion.getKM2().equals("Ko")) {
                                                tvKM2.append("Tặng: " + promotion.getKM2() + "\n");
                                            }
                                            if (!promotion.getKM3().equals("Ko")) {
                                                tvKM2.append("Tặng: " + promotion.getKM3() + "\n");
                                            }
                                            if (!promotion.getKM4().equals("Ko")) {
                                                tvKM2.append("Tặng: " + promotion.getKM4() + "\n");
                                            }
                                            if (!promotion.getKM5().equals("Ko")) {
                                                tvKM2.append("Tặng: " + promotion.getKM5());
                                            }
                                            tvKM2.append("Từ: " + promotion.getNgayBDKM() + " - " + promotion.getNgayKTKM());
                                        }
                                        progressBar.dismiss();
                                    }
                                }
                            }
                        }
                        if (bundle==null && bundle2 != null) {
                            if (!promotion.getLoaiSp().equals("Laptop") && !promotion.getLoaiSp().equals("Phone")) {
                                String string = promotion.getSpKm();
                                String[] parts = string.split("-");
                                String p0 = parts[0];
                                for (int i = 1; i < Integer.parseInt(p0) + 1; i++) {
                                    String p = parts[i].trim();
                                    if (accessory.getKeyID().equals("-" + p)) {
                                        if (promotion.getKieuGoiKM().equals("GiamGia")) {
                                            rd1.setVisibility(View.VISIBLE);
                                            tvKM1.setVisibility(View.VISIBLE);
                                            tvKM1.setText("");
                                            rd1.setChecked(true);
                                            rd1.setText(promotion.getTenGoiKM());
                                            tvKM1.setText("Giảm: " + formatMoney(Integer.parseInt(promotion.getKM1())));
                                            tvKM1.append("\n" + "Từ: " + promotion.getNgayBDKM() + " - " + promotion.getNgayKTKM());

                                        }
                                        if (promotion.getKieuGoiKM().equals("KhuyenMai")) {
                                            rd2.setVisibility(View.VISIBLE);
                                            tvKM2.setVisibility(View.VISIBLE);
                                            tvKM2.setText("");
                                            rd2.setText(promotion.getTenGoiKM());
                                            if (!promotion.getKM1().equals("Ko")) {
                                                tvKM2.append("Tặng: " + promotion.getKM1() + "\n");
                                            }
                                            if (!promotion.getKM2().equals("Ko")) {
                                                tvKM2.append("Tặng: " + promotion.getKM2() + "\n");
                                            }
                                            if (!promotion.getKM3().equals("Ko")) {
                                                tvKM2.append("Tặng: " + promotion.getKM3() + "\n");
                                            }
                                            if (!promotion.getKM4().equals("Ko")) {
                                                tvKM2.append("Tặng: " + promotion.getKM4() + "\n");
                                            }
                                            if (!promotion.getKM5().equals("Ko")) {
                                                tvKM2.append("Tặng: " + promotion.getKM5());
                                            }
                                            tvKM2.append("Từ: " + promotion.getNgayBDKM() + " - " + promotion.getNgayKTKM());
                                        }
                                        progressBar.dismiss();
                                    }
                                }
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}