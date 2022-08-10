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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.ListItemAddorEdit;
import com.example.appbanhang.Object.Accessory;
import com.example.appbanhang.Object.Item;
import com.example.appbanhang.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ListItemAddorEditAdapter extends RecyclerView.Adapter<ListItemAddorEditAdapter.ItemViewHolder>{


    Context mContext;
    ArrayList<Item> mList;
    ArrayList<Accessory> mAccessories;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private OnItemClickListener mListener;


    public ListItemAddorEditAdapter(Context mContext, ArrayList<Item> mList, ArrayList<Accessory> mAccessories) {
        this.mContext = mContext;
        this.mList = mList;
        this.mAccessories = mAccessories;
    }


    public interface OnItemClickListener {
        void onDeleteClick(String loaiSp, int soHinhAnh, String keyId);
        void onEditItemClick(String loaiSp, String keyID);
    }

    public void setOnClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_or_edit, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        if (mAccessories.size() > 0){
            Accessory accessory = mAccessories.get(position);
            Glide.with(mContext).load(Uri.parse(accessory.getHinhAnhPK())).into(holder.imgHInh);
            holder.tvTenSp.setText(String.format("%s - %s", accessory.getTenPhuKien(), accessory.getHangPhuKien()));
            holder.tvGiaSp.setText(accessory.getGiaPhuKien() + "");
            holder.tvSlSp.setText(accessory.getSoLuongPK()+"");
            holder.imgEditOrRemove.setOnClickListener(view -> {
                Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_bottom_sheet);
                ImageView img = dialog.findViewById(R.id.imageView_btmsht);
                TextView tvTensp = dialog.findViewById(R.id.textView_Tensp_btmsht);
                TextView tvGiaSp = dialog.findViewById(R.id.textView_Giasp_btmsht);
                TextView tvEdit = dialog.findViewById(R.id.textView_Edit_btmsht);
                TextView tvRemove = dialog.findViewById(R.id.textView_Remove_btmsht);
                TextView tvConLai = dialog.findViewById(R.id.textView_SpConLai_btmsht);
                Glide.with(mContext).load(Uri.parse(accessory.getHinhAnhPK())).into(img);
                tvTensp.setText(accessory.getTenPhuKien());
                tvGiaSp.setText(accessory.getGiaPhuKien() + "");
                tvConLai.setText(accessory.getSoLuongPK() + "");
                tvEdit.setOnClickListener(view13 -> {
                    if (mListener != null){
                        mListener.onEditItemClick("Accessory", accessory.getKeyID());
                        dialog.dismiss();
                    }
                });
                tvRemove.setOnClickListener(view1 -> {
                    if (mListener != null) {
                        mListener.onDeleteClick("Accessory", accessory.getSoHinhAnh(), accessory.getKeyID());
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
        else {
            Item item = mList.get(position);
//        storageRef.child(item.getKeyId()).child("0.png").getDownloadUrl().addOnSuccessListener(uri ->
            Glide.with(mContext).load(Uri.parse(item.getHinhAnhDaiDien())).into(holder.imgHInh);
            holder.tvTenSp.setText(item.getTenItem() +" - "+ item.getTenThuongHieu() );
            holder.tvGiaSp.setText(item.getGiaSp() + "");
            holder.tvSlSp.setText(item.getSoLuong());
            holder.imgEditOrRemove.setOnClickListener(view -> {
                Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_bottom_sheet);
                ImageView img = dialog.findViewById(R.id.imageView_btmsht);
                TextView tvTensp = dialog.findViewById(R.id.textView_Tensp_btmsht);
                TextView tvGiaSp = dialog.findViewById(R.id.textView_Giasp_btmsht);
                TextView tvEdit = dialog.findViewById(R.id.textView_Edit_btmsht);
                TextView tvRemove = dialog.findViewById(R.id.textView_Remove_btmsht);
                TextView tvConLai = dialog.findViewById(R.id.textView_SpConLai_btmsht);
                Glide.with(mContext).load(Uri.parse(item.getHinhAnhDaiDien())).into(img);
                tvTensp.setText(item.getTenItem());
                tvGiaSp.setText(item.getGiaSp() + "");
                tvConLai.setText(item.getSoLuong() + "");
                tvEdit.setOnClickListener(view12 -> {
                    if (mListener != null){
                        mListener.onEditItemClick(item.getLoaiSp(), item.getKeyId());
                        dialog.dismiss();
                    }
                });
                tvRemove.setOnClickListener(view1 -> {
                    if (mListener != null) {
                        mListener.onDeleteClick(item.getLoaiSp(), item.getSoHinhAnh(), item.getKeyId());
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

    }

    @Override
    public int getItemCount() {

        if (mList.size()>0 && mAccessories.size() ==0) {
            return mList.size();
        }
        else if (mList.size()==0 && mAccessories.size()>0){
            return mAccessories.size();
        }
//        else if (mList.size()>0 && mAccessories.size()>0){
//            return mList.size();
//        }
        return 0;
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgEditOrRemove, imgHInh;
        TextView tvTenSp, tvGiaSp, tvSlSp;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgEditOrRemove   = itemView.findViewById(R.id.imageView_EditOrRemove);
            imgHInh     = itemView.findViewById(R.id.img_ListAddOrRemove);
            tvTenSp     = itemView.findViewById(R.id.textView_TenSp_ListAddOrRemove);
            tvGiaSp     = itemView.findViewById(R.id.textView_GiaSp_ListAddOrRemove);
            tvSlSp      = itemView.findViewById(R.id.textView_SlSp_ListAddOrRemove);
        }
    }


}
