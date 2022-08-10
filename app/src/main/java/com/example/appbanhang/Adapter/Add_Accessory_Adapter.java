package com.example.appbanhang.Adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.Fragment.Fragment_Add_Accessory;
import com.example.appbanhang.MainActivity;
import com.example.appbanhang.R;

import java.io.IOException;
import java.util.ArrayList;

public class Add_Accessory_Adapter extends RecyclerView.Adapter<Add_Accessory_Adapter.phoToViewHolder>{

    ArrayList<Uri> mArrayList;
    Fragment_Add_Accessory fragment_add_accessory;

    public void setData(ArrayList<Uri> mArrayList){
        this.mArrayList = mArrayList;
        notifyDataSetChanged();
    }

    public Add_Accessory_Adapter(Fragment_Add_Accessory fragment_add_accessory) {
        this.fragment_add_accessory = fragment_add_accessory;
    }

    @NonNull
    @Override
    public phoToViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new phoToViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull phoToViewHolder holder, int position) {
        Uri uri = mArrayList.get(position);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(fragment_add_accessory.requireContext().getContentResolver(), uri);
            holder.imgPhoTo.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
       if (mArrayList != null){
           return mArrayList.size();
       }
       return 0;
    }

    public static class phoToViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoTo;
        public phoToViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoTo = itemView.findViewById(R.id.img_photo);
        }
    }
}
