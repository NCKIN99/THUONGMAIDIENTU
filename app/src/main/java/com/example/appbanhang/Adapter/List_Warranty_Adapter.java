package com.example.appbanhang.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.Object.Promotion;
import com.example.appbanhang.R;

import java.util.ArrayList;

public class List_Warranty_Adapter extends RecyclerView.Adapter<List_Warranty_Adapter.ItemViewHolder>{

    Context mContext;
    ArrayList<Promotion> mArrayList;
    private OnItemClickListener listener;

    public List_Warranty_Adapter(Context mContext, ArrayList<Promotion> mArrayList) {
        this.mContext = mContext;
        this.mArrayList = mArrayList;
    }

    public interface OnItemClickListener {
        void onDeleteClick(String KeyId);
    }

    public void setOnClick(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_warranty, parent, false);
        return new ItemViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Promotion promotion = mArrayList.get(position);
        Glide.with(mContext).load(Uri.parse(promotion.getHinhAnhKM())).into(holder.imgHinh);
        holder.tvTenGoi.setText(promotion.getTenGoiKM());
        holder.tvNgayBD.setText(promotion.getNgayBDKM());
        holder.tvNgayKT.setText(promotion.getNgayKTKM());
        holder.imgEdit.setOnClickListener(view -> {
            Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_bottom_sheet);
            ImageView img = dialog.findViewById(R.id.imageView_btmsht);
            TextView tvTensp = dialog.findViewById(R.id.textView_Tensp_btmsht);
            TextView tvGiaSp = dialog.findViewById(R.id.textView_Giasp_btmsht);
            TextView tvEdit = dialog.findViewById(R.id.textView_Edit_btmsht);
            TextView tvRemove = dialog.findViewById(R.id.textView_Remove_btmsht);
            TextView tvConLai = dialog.findViewById(R.id.textView_SpConLai_btmsht);
            TextView tvTenGoi = dialog.findViewById(R.id.textViewTenSp_btmsht);
            TextView tvNgayBD = dialog.findViewById(R.id.textViewGiaSp_btmsht);
            TextView tvNgayKT = dialog.findViewById(R.id.textViewSpConLai_btmsht);
            Glide.with(mContext).load(Uri.parse(promotion.getHinhAnhKM())).into(img);
            tvTenGoi.setText("Gói: ");
            tvNgayBD.setText("Từ: ");
            tvNgayKT.setText("Đến: ");
            tvTensp.setText(promotion.getTenGoiKM());
            tvGiaSp.setText(promotion.getNgayBDKM());
            tvConLai.setText(promotion.getNgayKTKM());
            tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            tvRemove.setOnClickListener(view1 -> {
                if (listener != null) {
                    listener.onDeleteClick(promotion.getIdKM());
                    dialog.dismiss();
                }
            });
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.show();
        });

    }

    @Override
    public int getItemCount() {
        if (mArrayList != null){
            return mArrayList.size();
        }
        return 0;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHinh, imgEdit;
        TextView tvTenGoi, tvNgayBD, tvNgayKT;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinh     = itemView.findViewById(R.id.img_ListWarranty);
            imgEdit     = itemView.findViewById(R.id.imageView_EditWarranty);
            tvTenGoi    = itemView.findViewById(R.id.textView_KieuGoiKM);
            tvNgayBD    = itemView.findViewById(R.id.textView_ListNgayBDKM);
            tvNgayKT    = itemView.findViewById(R.id.textView_ListNgayKTKM);
        }
    }
}
