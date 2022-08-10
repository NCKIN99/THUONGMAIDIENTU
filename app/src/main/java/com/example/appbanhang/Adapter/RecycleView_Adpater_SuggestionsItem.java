package com.example.appbanhang.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
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

public class RecycleView_Adpater_SuggestionsItem extends RecyclerView.Adapter<RecycleView_Adpater_SuggestionsItem.PhotoViewHolder>{

    private ArrayList<Item> arrayList;
    private Context context;
    private OnItemClick mOnItemClick;

    public RecycleView_Adpater_SuggestionsItem(ArrayList<Item> arrayList, Context context, OnItemClick mOnItemClick) {
        this.arrayList = arrayList;
        this.context = context;
        this.mOnItemClick = mOnItemClick;
    }

    public interface OnItemClick{
        void OnSendDataItem(Item item);
    }


    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inf, parent,  false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Item item = arrayList.get(position);
        Glide.with(context).load(Uri.parse(item.getHinhAnhDaiDien())).into(holder.img);
        holder.tvTen.setText(formatString(item.getTenItem()));
        holder.tvGia.setText(formatMoney(item.getGiaSp()));
        holder.tvDaBan.setText(formatValue(item.getDaBan()));
        holder.img.setOnClickListener(view -> {
            mOnItemClick.OnSendDataItem(item);
        });
    }

    @Override
    public int getItemCount() {
        if (arrayList != null){
            return arrayList.size();
        }
        return 0;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView tvTen, tvGia, tvDaBan;
        public PhotoViewHolder(@NonNull View itemView) {
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
            String format = decimalFormat.format(i) + " đ";
            return format;
        }

    }
}
