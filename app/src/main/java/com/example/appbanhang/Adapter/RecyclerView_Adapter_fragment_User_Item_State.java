package com.example.appbanhang.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.Item_State_Activity;
import com.example.appbanhang.MainActivity;
import com.example.appbanhang.Object.Item;
import com.example.appbanhang.Object.User;
import com.example.appbanhang.Object.Users_Item;
import com.example.appbanhang.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RecyclerView_Adapter_fragment_User_Item_State extends RecyclerView.Adapter<RecyclerView_Adapter_fragment_User_Item_State.ViewHolder>{

    private ArrayList<Users_Item> users_itemArrayList;
    private Context mContext;
    private onClickUserItemState onClickUserItemState;
    private String userPermission;

    public RecyclerView_Adapter_fragment_User_Item_State(ArrayList<Users_Item> users_itemArrayList, Context mContext, RecyclerView_Adapter_fragment_User_Item_State.onClickUserItemState onClickUserItemState, String userPermission) {
        this.users_itemArrayList = users_itemArrayList;
        this.mContext = mContext;
        this.onClickUserItemState = onClickUserItemState;
        this.userPermission = userPermission;
    }

    public interface onClickUserItemState{
        void HuyDon(Users_Item users_item);
        void XacNhanDon(Users_Item users_item);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_state2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users_Item  users_item = users_itemArrayList.get(position);
        if (users_item.getItem() != null && users_item.getAccessory() == null){
            holder.tvid.setText(users_item.getKeyID());
            holder.tvTen.setText(users_item.getItem().getTenItem());
            holder.tvGiaGoc.setText(formatMoney(users_item.getItem().getGiaSp()));
            holder.tvGiaKM.setText(users_item.getSumCost());
            holder.tvDiaChi.setText(users_item.getUser().getUserAddress());
            holder.tvNgayMua.setText(users_item.getThoiGianMua());
            holder.tvNguoiMua.setText(users_item.getUser().getUserFullName());
            holder.tvSdt.setText(users_item.getUser().getUserPhoneNumber());
            switch (users_item.getItemState()) {
                case "0":
                    if (userPermission.equals("admin")) {
                        holder.tvTrangthai.setText("Chờ xác nhận đơn hàng");
                        holder.tvTrangthai.setTextColor(Color.parseColor("#EF0A0A"));
                    }else {
                        holder.tvTrangthai.setText("Chờ xác nhận đơn hàng");
                        holder.tvTrangthai.setTextColor(Color.parseColor("#EF0A0A"));
                        holder.tvHuyDon.setVisibility(View.GONE);
                        holder.tvXacNhanDon.setVisibility(View.GONE);
                    }
                    break;
                case "1":
                    if (userPermission.equals("admin")) {
                        holder.tvTrangthai.setText("Chờ Giao hàng");
                        holder.tvXacNhanDon.setText("Xác nhận Giao hàng");
                        holder.tvTrangthai.setTextColor(Color.parseColor("#EF0A0A"));
                    }else {
                        holder.tvTrangthai.setText("Chờ Giao hàng");
                        holder.tvXacNhanDon.setText("Xác nhận Giao hàng");
                        holder.tvTrangthai.setTextColor(Color.parseColor("#EF0A0A"));
                        holder.tvHuyDon.setVisibility(View.GONE);
                        holder.tvXacNhanDon.setVisibility(View.GONE);
                    }
                    break;
                case "2":
                    if (userPermission.equals("admin")) {
                        holder.tvTrangthai.setText("Đang giao hàng");
                        holder.tvXacNhanDon.setText("Khách đã nhận hàng");
                        holder.tvTrangthai.setTextColor(Color.parseColor("#EF0A0A"));
                    }else {
                        holder.tvTrangthai.setText("Đang giao hàng");
                        holder.tvXacNhanDon.setText("Khách đã nhận hàng");
                        holder.tvTrangthai.setTextColor(Color.parseColor("#EF0A0A"));
                        holder.tvHuyDon.setVisibility(View.GONE);
                        holder.tvXacNhanDon.setVisibility(View.GONE);
                    }
                    break;
                case "3":
                    if (userPermission.equals("admin")) {
                        holder.tvTrangthai.setText("Đã giao hàng");
                        holder.tvTrangthai.setTextColor(Color.parseColor("#03A9F4"));
                        holder.tvHuyDon.setVisibility(View.GONE);
                        holder.tvXacNhanDon.setVisibility(View.GONE);
                    }else {
                        holder.tvTrangthai.setText("Đã giao hàng");
                        holder.tvTrangthai.setTextColor(Color.parseColor("#03A9F4"));
                        holder.tvXacNhanDon.setVisibility(View.GONE);
                        holder.tvHuyDon.setVisibility(View.GONE);
                    }
                    break;
                case "4":
                    if (userPermission.equals("admin")) {
                        holder.tvTrangthai.setText("đơn hàng bị hủy");
                        holder.tvTrangthai.setTextColor(Color.parseColor("#EF0A0A"));
                        holder.tvLyDoHuyDon.setTextColor(Color.parseColor("#EF0A0A"));
                        holder.lnHuyDon.setVisibility(View.VISIBLE);
                        holder.tvLyDoHuyDon.setText(users_item.getHuy());
                        holder.tvHuyDon.setVisibility(View.GONE);
                        holder.tvXacNhanDon.setVisibility(View.GONE);
                    }else {
                        holder.tvTrangthai.setText("đơn hàng bị hủy");
                        holder.tvTrangthai.setTextColor(Color.parseColor("#EF0A0A"));
                        holder.lnHuyDon.setVisibility(View.VISIBLE);
                        holder.tvLyDoHuyDon.setTextColor(Color.parseColor("#EF0A0A"));
                        holder.tvLyDoHuyDon.setText(users_item.getHuy());
                        holder.tvXacNhanDon.setVisibility(View.GONE);
                        holder.tvHuyDon.setVisibility(View.GONE);
                    }
                    break;
            }
            Glide.with(mContext).load(users_item.getItem().getHinhAnhDaiDien()).into(holder.imgHinh);
            if (users_item.getPromotion() != null &&users_item.getPromotion().getKieuGoiKM().equals("KhuyenMai")) {
                holder.tvGoiKM.setText(users_item.getPromotion().getTenGoiKM()+"\n"+"Gói khuyến mãi tặng kèm"+"\n"+
                        users_item.getPromotion().getNgayBDKM()+" - "+users_item.getPromotion().getNgayKTKM());
            }else if (users_item.getPromotion() != null &&users_item.getPromotion().getKieuGoiKM().equals("GiamGia")){
                holder.tvGoiKM.setText(users_item.getPromotion().getTenGoiKM()+"\n"+"Giảm giá "+
                        formatMoney(Integer.parseInt(users_item.getPromotion().getKM1()))+"\n"+
                        users_item.getPromotion().getNgayBDKM()+" - "+users_item.getPromotion().getNgayKTKM());

            }else {
                holder.tvGoiKM.setText("Không");

            }
            holder.tvHuyDon.setOnClickListener(view -> onClickUserItemState.HuyDon(users_item));
            holder.tvXacNhanDon.setOnClickListener(view -> onClickUserItemState.XacNhanDon(users_item));
        }
        if (users_item.getAccessory() != null && users_item.getItem()==null){
            Glide.with(mContext).load(users_item.getAccessory().getHinhAnhPK()).into(holder.imgHinh);
            holder.tvid.setText(users_item.getKeyID());
            holder.tvTen.setText(users_item.getAccessory().getTenPhuKien());
            holder.tvGiaGoc.setText(formatMoney(users_item.getAccessory().getGiaPhuKien()));
            holder.tvGiaKM.setText(users_item.getSumCost());
            holder.tvDiaChi.setText(users_item.getUser().getUserAddress());
            holder.tvNgayMua.setText(users_item.getThoiGianMua());
            holder.tvNguoiMua.setText(users_item.getUser().getUserFullName());
            holder.tvSdt.setText(users_item.getUser().getUserPhoneNumber());
            switch (users_item.getItemState()) {
                case "0":
                    if (userPermission.equals("admin")) {
                        holder.tvTrangthai.setText("Chờ xác nhận đơn hàng");
                        holder.tvTrangthai.setTextColor(Color.parseColor("#EF0A0A"));
                    }else {
                        holder.tvTrangthai.setText("Chờ xác nhận đơn hàng");
                        holder.tvTrangthai.setTextColor(Color.parseColor("#EF0A0A"));
                        holder.tvHuyDon.setVisibility(View.GONE);
                        holder.tvXacNhanDon.setVisibility(View.GONE);
                    }
                    break;
                case "1":
                    if (userPermission.equals("admin")) {
                        holder.tvTrangthai.setText("Chờ Giao hàng");
                        holder.tvXacNhanDon.setText("Xác nhận Giao hàng");
                        holder.tvTrangthai.setTextColor(Color.parseColor("#EF0A0A"));
                    }else {
                        holder.tvTrangthai.setText("Chờ Giao hàng");
                        holder.tvXacNhanDon.setText("Xác nhận Giao hàng");
                        holder.tvTrangthai.setTextColor(Color.parseColor("#EF0A0A"));
                        holder.tvHuyDon.setVisibility(View.GONE);
                        holder.tvXacNhanDon.setVisibility(View.GONE);
                    }
                    break;
                case "2":
                    if (userPermission.equals("admin")) {
                        holder.tvTrangthai.setText("Đang giao hàng");
                        holder.tvXacNhanDon.setText("Khách đã nhận hàng");
                        holder.tvTrangthai.setTextColor(Color.parseColor("#EF0A0A"));
                    }else {
                        holder.tvTrangthai.setText("Đang giao hàng");
                        holder.tvXacNhanDon.setText("Khách đã nhận hàng");
                        holder.tvTrangthai.setTextColor(Color.parseColor("#EF0A0A"));
                        holder.tvHuyDon.setVisibility(View.GONE);
                        holder.tvXacNhanDon.setVisibility(View.GONE);
                    }
                    break;
                case "3":
                    if (userPermission.equals("admin")) {
                        holder.tvTrangthai.setText("Đã giao hàng");
                        holder.tvTrangthai.setTextColor(Color.parseColor("#03A9F4"));
                        holder.tvHuyDon.setVisibility(View.GONE);
                        holder.tvXacNhanDon.setVisibility(View.GONE);
                    }else {
                        holder.tvTrangthai.setText("Đã giao hàng");
                        holder.tvTrangthai.setTextColor(Color.parseColor("#03A9F4"));
                        holder.tvXacNhanDon.setVisibility(View.GONE);
                        holder.tvHuyDon.setVisibility(View.GONE);
                    }
                    break;
                case "4":
                    if (userPermission.equals("admin")) {
                        holder.tvTrangthai.setText("đơn hàng bị hủy");
                        holder.tvTrangthai.setTextColor(Color.parseColor("#EF0A0A"));
                        holder.tvLyDoHuyDon.setTextColor(Color.parseColor("#EF0A0A"));
                        holder.lnHuyDon.setVisibility(View.VISIBLE);
                        holder.tvLyDoHuyDon.setText(users_item.getHuy());
                        holder.tvHuyDon.setVisibility(View.GONE);
                        holder.tvXacNhanDon.setVisibility(View.GONE);
                    }else {
                        holder.tvTrangthai.setText("đơn hàng bị hủy");
                        holder.tvTrangthai.setTextColor(Color.parseColor("#EF0A0A"));
                        holder.tvLyDoHuyDon.setTextColor(Color.parseColor("#EF0A0A"));
                        holder.lnHuyDon.setVisibility(View.VISIBLE);
                        holder.tvLyDoHuyDon.setText(users_item.getHuy());
                        holder.tvXacNhanDon.setVisibility(View.GONE);
                        holder.tvHuyDon.setVisibility(View.GONE);
                    }
                    break;
            }
            if (users_item.getPromotion() != null &&users_item.getPromotion().getKieuGoiKM().equals("KhuyenMai")) {
                holder.tvGoiKM.setText(users_item.getPromotion().getTenGoiKM()+"\n"+"Gói khuyến mãi tặng kèm"+"\n"+
                        users_item.getPromotion().getNgayBDKM()+" - "+users_item.getPromotion().getNgayKTKM());
            }else if (users_item.getPromotion() != null &&users_item.getPromotion().getKieuGoiKM().equals("GiamGia")){
                holder.tvGoiKM.setText(users_item.getPromotion().getTenGoiKM()+"\n"+"Giảm giá "+
                        formatMoney(Integer.parseInt(users_item.getPromotion().getKM1()))+"\n"+
                        users_item.getPromotion().getNgayBDKM()+" - "+users_item.getPromotion().getNgayKTKM());

            }else {
                holder.tvGoiKM.setText("Không");

            }
            holder.tvHuyDon.setOnClickListener(view -> onClickUserItemState.HuyDon(users_item));
            holder.tvXacNhanDon.setOnClickListener(view -> onClickUserItemState.XacNhanDon(users_item));
        }

    }

    @Override
    public int getItemCount() {
        if (users_itemArrayList != null){
            return users_itemArrayList.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHinh;
        TextView tvid, tvTen, tvGiaGoc, tvGiaKM, tvDiaChi, tvNgayMua, tvNguoiMua
                , tvSdt, tvHuyDon, tvXacNhanDon, tvGoiKM, tvTrangthai, tvLyDoHuyDon;
        LinearLayout lnHuyDon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinh      = itemView.findViewById(R.id.img_user_item_state2);
            tvid         = itemView.findViewById(R.id.id_item_user_state);
            tvTen        = itemView.findViewById(R.id.ten_item_user_state);
            tvGiaGoc     = itemView.findViewById(R.id.gia_item_user_state);
            tvGiaKM      = itemView.findViewById(R.id.gia_km_item_user_state);
            tvDiaChi     = itemView.findViewById(R.id.diachi_item_user_state);
            tvNgayMua    = itemView.findViewById(R.id.ngaymua_item_user_state);
            tvHuyDon     = itemView.findViewById(R.id.huydon_item_user_state);
            tvXacNhanDon = itemView.findViewById(R.id.xacnhandon_item_user_state);
            tvNguoiMua   = itemView.findViewById(R.id.nguoimua_item_user_state);
            tvSdt        = itemView.findViewById(R.id.sdt_item_user_state);
            tvGoiKM      = itemView.findViewById(R.id.goi_km_item_user_state);
            tvTrangthai  = itemView.findViewById(R.id.trangthai_item_user_state);
            tvLyDoHuyDon = itemView.findViewById(R.id.lydohuy_item_user_state);
            lnHuyDon     = itemView.findViewById(R.id.lnLyDoHuyDon);
        }
    }
    public String formatMoney(int  i){
        String pattern = "###,###.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(i) + " đ";

    }

}
