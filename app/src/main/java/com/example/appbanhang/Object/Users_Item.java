package com.example.appbanhang.Object;

import java.io.Serializable;

public class Users_Item implements Serializable {
    private String KeyID;
    private String ThoiGianMua;
    private User user;
    private Item item;
    private Accessory accessory;
    private Promotion promotion;
    private String SumCost;
    private String KieuTT;
    private String ItemState;
    private String Huy;


    public Users_Item(String keyID, String thoiGianMua, User user, Item item, Accessory accessory, Promotion promotion, String sumCost, String kieuTT, String itemState, String huy) {
        KeyID = keyID;
        ThoiGianMua = thoiGianMua;
        this.user = user;
        this.item = item;
        this.accessory = accessory;
        this.promotion = promotion;
        SumCost = sumCost;
        KieuTT = kieuTT;
        ItemState = itemState;
        Huy = huy;
    }

    public Users_Item() {
    }



    public String getKeyID() {
        return KeyID;
    }

    public void setKeyID(String keyID) {
        KeyID = keyID;
    }


    public String getThoiGianMua() {
        return ThoiGianMua;
    }

    public void setThoiGianMua(String thoiGianMua) {
        ThoiGianMua = thoiGianMua;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Accessory getAccessory() {
        return accessory;
    }

    public void setAccessory(Accessory accessory) {
        this.accessory = accessory;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public String getSumCost() {
        return SumCost;
    }

    public void setSumCost(String sumCost) {
        SumCost = sumCost;
    }

    public String getKieuTT() {
        return KieuTT;
    }

    public void setKieuTT(String kieuTT) {
        KieuTT = kieuTT;
    }

    public Users_Item(String itemState) {
        ItemState = itemState;
    }

    public String getItemState() {
        return ItemState;
    }

    public void setItemState(String itemState) {
        ItemState = itemState;
    }


    public String getHuy() {
        return Huy;
    }

    public void setHuy(String huy) {
        Huy = huy;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
