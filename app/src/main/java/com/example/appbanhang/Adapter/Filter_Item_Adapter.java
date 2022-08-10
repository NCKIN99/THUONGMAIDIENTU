package com.example.appbanhang.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.Object.Accessory;
import com.example.appbanhang.Object.Item;
import com.example.appbanhang.R;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;

public class Filter_Item_Adapter extends RecyclerView.Adapter<Filter_Item_Adapter.ViewHolderItem> implements Filterable {



    private Context mContext;
    private ArrayList<Item> itemArrayList;
    private ArrayList<Item> itemArrayListOLD;
    private onClickItemFilter onClickItemFilter;

    public Filter_Item_Adapter(Context mContext, ArrayList<Item> itemArrayList, Filter_Item_Adapter.onClickItemFilter onClickItemFilter) {
        this.mContext = mContext;
        this.itemArrayList = itemArrayList;
        this.itemArrayListOLD = itemArrayList;
        this.onClickItemFilter = onClickItemFilter;
    }

    public interface onClickItemFilter{
        void onClickFilter(Item item);
    }


    @NonNull
    @Override
    public ViewHolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_or_edit, parent, false);
        return new ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderItem holder, int position) {
        Item item = itemArrayList.get(position);
        holder.imgEditOrRemove.setVisibility(View.GONE);
        Glide.with(mContext).load(Uri.parse(item.getHinhAnhDaiDien())).into(holder.imgHInh);
        holder.tvTenSp.setText(item.getTenItem());
        holder.tvGiaSp.setText(formatMoney(item.getGiaSp()));
        holder.tvSlSp.setText(item.getSoLuong());
        holder.imgHInh.setOnClickListener(view -> onClickItemFilter.onClickFilter(item));
    }


    @Override
    public int getItemCount() {
        if (itemArrayList != null) {
            return itemArrayList.size();
        }
        return 0;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if (strSearch.isEmpty()){
                    itemArrayList = itemArrayListOLD;
                }else {
                    ArrayList<Item> itemList = new ArrayList<>();
                    for (Item item : itemArrayListOLD){
                        if (item.getTenItem().toLowerCase().contains(strSearch.toLowerCase())){
                            itemList.add(item);
                        }
                    }
                    itemArrayList = itemList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            itemArrayList = (ArrayList<Item>) filterResults.values;
            notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolderItem extends RecyclerView.ViewHolder{
        ImageView imgEditOrRemove, imgHInh;
        TextView tvTenSp, tvGiaSp, tvSlSp;
        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            imgEditOrRemove   = itemView.findViewById(R.id.imageView_EditOrRemove);
            imgHInh     = itemView.findViewById(R.id.img_ListAddOrRemove);
            tvTenSp     = itemView.findViewById(R.id.textView_TenSp_ListAddOrRemove);
            tvGiaSp     = itemView.findViewById(R.id.textView_GiaSp_ListAddOrRemove);
            tvSlSp      = itemView.findViewById(R.id.textView_SlSp_ListAddOrRemove);
        }
    }
    public String formatMoney(int  i){
        if (i/1000000 > 0){
            return String.format("%.2f tr", i/ 1000000.0);
        }else {
            String pattern = "###,###.###";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            String format = decimalFormat.format(i) + " đ";
            return format;
        }

    }



}
