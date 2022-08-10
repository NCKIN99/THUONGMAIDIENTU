package com.example.appbanhang.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.Object.Promotion;
import com.example.appbanhang.R;

import java.util.List;

public class ViewPager_Adapter_QC extends RecyclerView.Adapter<ViewPager_Adapter_QC.PhotoViewHolder>{

    public List<Promotion> mList;
    public Context mContext;


    public ViewPager_Adapter_QC(List<Promotion> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_qc, parent, false);
        return new PhotoViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Promotion promotion = mList.get(position);
        if (promotion == null){
            return;
        }
        Glide.with(mContext).load(Uri.parse(promotion.getHinhAnhKM())).into(holder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        if (mList != null){
            return mList.size();
        }
        return 0;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photo_qc);
        }
    }
}
