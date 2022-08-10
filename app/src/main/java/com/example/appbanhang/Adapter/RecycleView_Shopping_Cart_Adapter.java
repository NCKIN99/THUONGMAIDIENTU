package com.example.appbanhang.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.Object.Accessory;
import com.example.appbanhang.Object.Item;
import com.example.appbanhang.Object.Users_Item;
import com.example.appbanhang.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class RecycleView_Shopping_Cart_Adapter extends RecyclerView.Adapter<RecycleView_Shopping_Cart_Adapter.PhoToviewHolder>{

    private ArrayList<Users_Item> users_itemArrayList;
    private Context mContext;
    private onClickItemShoppingCart onClickSPC;

    public RecycleView_Shopping_Cart_Adapter(ArrayList<Users_Item> users_itemArrayList, Context mContext, onClickItemShoppingCart onClickSPC) {
        this.users_itemArrayList = users_itemArrayList;
        this.mContext = mContext;
        this.onClickSPC = onClickSPC;
    }

    public interface onClickItemShoppingCart{
        void onDeleteItemSPC(Users_Item users_item);
        void onClickItemSPC(Users_Item users_item);
    }



    @NonNull
    @Override
    public PhoToviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_or_edit, parent, false);
        return new PhoToviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoToviewHolder holder, int position) {
        Users_Item users_item = users_itemArrayList.get(position);
        if (users_item.getAccessory() == null && users_item.getItem()!= null){
            holder.imgEditOrRemove.setImageResource(R.drawable.ic_baseline_remove_shopping_cart_24);
            Glide.with(mContext).load(Uri.parse(users_item.getItem().getHinhAnhDaiDien())).into(holder.imgHInh);
            holder.tvTenSp.setText(users_item.getItem().getTenItem());
            holder.tvGiaSp.setText(formatMoney(users_item.getItem().getGiaSp()));
            holder.tvSlSp.setText(formatValue(Double.parseDouble(users_item.getItem().getSoLuong())));
            holder.imgEditOrRemove.setOnClickListener(view -> onClickSPC.onDeleteItemSPC(users_item));
            holder.imgHInh.setOnClickListener(view -> onClickSPC.onClickItemSPC(users_item));
        }else if (users_item.getAccessory() != null && users_item.getItem()== null){
            holder.imgEditOrRemove.setImageResource(R.drawable.ic_baseline_remove_shopping_cart_24);
            Glide.with(mContext).load(Uri.parse(users_item.getAccessory().getHinhAnhPK())).into(holder.imgHInh);
            holder.tvTenSp.setText(users_item.getAccessory().getTenPhuKien());
            holder.tvGiaSp.setText(formatMoney(users_item.getAccessory().getGiaPhuKien()));
            holder.tvSlSp.setText(formatValue(users_item.getAccessory().getSoLuongPK()));
            holder.imgEditOrRemove.setOnClickListener(view -> onClickSPC.onDeleteItemSPC(users_item));
            holder.imgHInh.setOnClickListener(view -> onClickSPC.onClickItemSPC(users_item));
        }
    }

    @Override
    public int getItemCount() {
        if (users_itemArrayList != null){
            return users_itemArrayList.size();
        }
        return 0;
    }


    public static class PhoToviewHolder extends RecyclerView.ViewHolder {
        ImageView imgEditOrRemove, imgHInh;
        TextView tvTenSp, tvGiaSp, tvSlSp;
        public PhoToviewHolder(@NonNull View itemView) {
            super(itemView);
            imgEditOrRemove   = itemView.findViewById(R.id.imageView_EditOrRemove);
            imgHInh     = itemView.findViewById(R.id.img_ListAddOrRemove);
            tvTenSp     = itemView.findViewById(R.id.textView_TenSp_ListAddOrRemove);
            tvGiaSp     = itemView.findViewById(R.id.textView_GiaSp_ListAddOrRemove);
            tvSlSp      = itemView.findViewById(R.id.textView_SlSp_ListAddOrRemove);
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
