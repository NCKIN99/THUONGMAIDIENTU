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
import com.example.appbanhang.Object.Item;
import com.example.appbanhang.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class RecyclerView_Adapter_BestSell extends RecyclerView.Adapter<RecyclerView_Adapter_BestSell.PhotoViewHolder>{

    private ArrayList<Item> mItems;
    private Context context;
    public OnItemClickListener onItemClickListener;

    public RecyclerView_Adapter_BestSell(ArrayList<Item> mItems, Context context, OnItemClickListener onItemClickListener) {
        this.mItems = mItems;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void OnClickItem(Item item);
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inf_mini, parent, false);
        return new PhotoViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Item item = mItems.get(position);
        Glide.with(context).load(Uri.parse(item.getHinhAnhDaiDien())).into(holder.img);
        holder.tvTen.setText(formatString(item.getTenItem()));
        holder.tvGia.setText(formatMoney(item.getGiaSp()));
        holder.tvDaBan.setText(formatValue(Double.parseDouble(item.getDaBan()+"")));
        holder.img.setOnClickListener(view -> onItemClickListener.OnClickItem(item));
    }

    @Override
    public int getItemCount() {
        if (mItems != null){
            return mItems.size();
        }
        return 0;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
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
            return String.format("%.2f tr", i/ 1000000.0);
        }else {
            String pattern = "###,###.###";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            return decimalFormat.format(i) + " đ";
        }

    }

}
