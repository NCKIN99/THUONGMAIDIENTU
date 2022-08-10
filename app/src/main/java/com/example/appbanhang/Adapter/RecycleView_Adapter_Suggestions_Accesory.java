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
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class RecycleView_Adapter_Suggestions_Accesory extends RecyclerView.Adapter<RecycleView_Adapter_Suggestions_Accesory.PhoToViewHolder>{

    private ArrayList<Accessory> accessories;
    private Context context;
    private ItemClickAccessory itemClickAccessory;

    public RecycleView_Adapter_Suggestions_Accesory(ArrayList<Accessory> accessories, Context context, ItemClickAccessory itemClickAccessory) {
        this.accessories = accessories;
        this.context = context;
        this.itemClickAccessory = itemClickAccessory;
    }

    @NonNull
    @Override
    public PhoToViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inf, parent, false);
        return new PhoToViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoToViewHolder holder, int position) {
        Accessory accessory = accessories.get(position);
        Glide.with(context).load(Uri.parse(accessory.getHinhAnhPK())).into(holder.img);
        holder.tvTen.setText(formatString(accessory.getTenPhuKien()));
        holder.tvGia.setText(formatMoney(accessory.getGiaPhuKien()));
        holder.tvDaBan.setText(formatValue(accessory.getPhuKienDaBan()));
        holder.img.setOnClickListener(view -> itemClickAccessory.OnClickAccessory(accessory));
    }

    @Override
    public int getItemCount() {
        if (accessories != null){
            return accessories.size();
        }
        return 0;
    }

    public static class PhoToViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvTen, tvGia, tvDaBan;
        public PhoToViewHolder(@NonNull View itemView) {
            super(itemView);
            img     = itemView.findViewById(R.id.imageView_item_inf);
            tvTen   = itemView.findViewById(R.id.textView_item_inf_ten);
            tvGia   = itemView.findViewById(R.id.textView_item_inf_gia);
            tvDaBan = itemView.findViewById(R.id.textView_item_inf_daban);
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
