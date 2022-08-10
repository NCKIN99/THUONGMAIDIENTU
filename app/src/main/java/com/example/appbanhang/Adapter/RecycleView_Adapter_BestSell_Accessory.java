package com.example.appbanhang.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.Object.Accessory;
import com.example.appbanhang.Object.Item;
import com.example.appbanhang.R;
import com.example.appbanhang.my_Interface.ItemClickAccessory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class RecycleView_Adapter_BestSell_Accessory extends RecyclerView.Adapter<RecycleView_Adapter_BestSell_Accessory.PhotoViewHolder>{

    ArrayList<Accessory> accessories;
    Context context;
    private ItemClickAccessory accessoryClick;


    public RecycleView_Adapter_BestSell_Accessory(ArrayList<Accessory> accessories, Context context, ItemClickAccessory accessoryClick) {
        this.accessories = accessories;
        this.context = context;
        this.accessoryClick = accessoryClick;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inf_mini, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Accessory accessory = accessories.get(position);
        Glide.with(context).load(Uri.parse(accessory.getHinhAnhPK())).into(holder.img);
        holder.tvTen.setText(formatString(accessory.getTenPhuKien()));
        holder.tvGia.setText(formatMoney(accessory.getGiaPhuKien()));
        holder.tvDaBan.setText(formatValue(Double.parseDouble(accessory.getPhuKienDaBan()+"")));
        holder.img.setOnClickListener(view -> accessoryClick.OnClickAccessory(accessory));
    }

    @Override
    public int getItemCount() {
        if (accessories != null){
            return accessories.size();
        }
        return 0;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView tvTen, tvGia, tvDaBan;
        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            img     = itemView.findViewById(R.id.imageView_item_inf_mini);
            tvTen   = itemView.findViewById(R.id.textView_item_inf_ten_mini);
            tvGia   = itemView.findViewById(R.id.textView_item_inf_gia_mini);
            tvDaBan = itemView.findViewById(R.id.textView_item_inf_daban_mini);
        }
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
    public String formatString(String s){
        if (s.length()>9){
            return s.substring(0,9)+"...";
        }
        return s ;
    }
    public String formatMoney(int  i){
        if (i/1000000 > 0){
            DecimalFormat df = new DecimalFormat("0.00 tr");
            return String.format("%.2f tr", i/ 1000000.0);
        }else {
            String pattern = "###,###.###";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            return decimalFormat.format(i) + " đ";
        }

    }
}
