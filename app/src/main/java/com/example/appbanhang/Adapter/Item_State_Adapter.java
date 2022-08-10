package com.example.appbanhang.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;

import java.util.ArrayList;

public class Item_State_Adapter extends RecyclerView.Adapter<Item_State_Adapter.itemViewHolder>{

    ArrayList<String> arrayList;
    private onItemClickListener onItemClickListener;

    public Item_State_Adapter(ArrayList<String> arrayList, Item_State_Adapter.onItemClickListener onItemClickListener) {
        this.arrayList = arrayList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_state, parent, false);
        return new itemViewHolder(mView);
    }
    public interface onItemClickListener{
        void OnClickUserItem(String keyID);
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position) {
        String data = arrayList.get(position);
        String[] parts = data.split("-");
        String tk = parts[0];
        String sumItem = parts[1];
        String itemState0 = parts[2];
        String state = parts[3];
        String keyid = parts[4];
        if (state.equals("0")){
            holder.tv.setText("Chờ xác nhận:");
        }
        if (state.equals("1")){
            holder.tv.setText("Chờ giao:");
        }
        if (state.equals("2")){
            holder.tv.setText("Đang giao:");
        }
        if (state.equals("3")){
            holder.tv.setText("Đã giao:");
        }
        holder.tvTK.setText(tk);
        holder.tvSumItem.setText(sumItem);
        holder.tvItemState0.setText(itemState0);
        holder.ln.setOnClickListener(view -> onItemClickListener.OnClickUserItem(keyid));

    }

    @Override
    public int getItemCount() {
        if (arrayList != null){
            return arrayList.size();
        }
        return 0;
    }

    public static class itemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTK, tvSumItem, tvItemState0, tv;
        LinearLayout ln;
        public itemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTK            = itemView.findViewById(R.id.item_State_TK);
            tvSumItem       = itemView.findViewById(R.id.item_State_SUMITEM);
            tvItemState0    = itemView.findViewById(R.id.item_State_ITEM);
            tv              = itemView.findViewById(R.id.tv_ItemState);
            ln              = itemView.findViewById(R.id.user_item_state);
        }
    }
}
