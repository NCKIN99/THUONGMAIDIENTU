package com.example.appbanhang.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.Fragment.Fragment_AddItem;
import com.example.appbanhang.R;

import java.io.IOException;
import java.util.ArrayList;

public class PhoToAdapterAddItem extends RecyclerView.Adapter<PhoToAdapterAddItem.PhotoViewHolder>{



    private ArrayList<Uri> mListPhotos;
    public Context mContext;

    public void SetData(ArrayList<Uri> mListPhotos) {
        this.mListPhotos = mListPhotos;
        notifyDataSetChanged();
    }

    public PhoToAdapterAddItem(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Uri uri = mListPhotos.get(position);
       try {
           Glide.with(mContext).load(uri).into(holder.imgPhoto);
           Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
           holder.imgPhoto.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (mListPhotos == null){
            return 0;
        } else {
            return mListPhotos.size();
        }
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhoto;
        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photo);
        }
    }
}
