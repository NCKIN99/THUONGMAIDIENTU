package com.example.appbanhang.Object;

import java.io.Serializable;

public class Accessory implements Serializable {
    private String TenPhuKien;
    private String HangPhuKien;
    private String LoaiPhuKien;
    private String ThongTinPhuKien;
    private String keyID;
    private int GiaPhuKien;
    private int SoHinhAnh;
    private int PhuKienDaBan;
    private int SoLuongPK;
    private String HinhAnhPK;

    public Accessory() {
    }

    public Accessory(String tenPhuKien, String hangPhuKien, String loaiPhuKien, String thongTinPhuKien, String keyID, int giaPhuKien, int soHinhAnh, int phuKienDaBan, int soLuongPK, String hinhAnhPK) {
        TenPhuKien = tenPhuKien;
        HangPhuKien = hangPhuKien;
        LoaiPhuKien = loaiPhuKien;
        ThongTinPhuKien = thongTinPhuKien;
        this.keyID = keyID;
        GiaPhuKien = giaPhuKien;
        SoHinhAnh = soHinhAnh;
        PhuKienDaBan = phuKienDaBan;
        SoLuongPK = soLuongPK;
        HinhAnhPK = hinhAnhPK;
    }

    public int getSoLuongPK() {
        return SoLuongPK;
    }

    public void setSoLuongPK(int soLuongPK) {
        SoLuongPK = soLuongPK;
    }

    public String getKeyID() {
        return keyID;
    }

    public void setKeyID(String keyID) {
        this.keyID = keyID;
    }

    public String getTenPhuKien() {
        return TenPhuKien;
    }

    public void setTenPhuKien(String tenPhuKien) {
        TenPhuKien = tenPhuKien;
    }

    public String getHangPhuKien() {
        return HangPhuKien;
    }

    public void setHangPhuKien(String hangPhuKien) {
        HangPhuKien = hangPhuKien;
    }

    public String getLoaiPhuKien() {
        return LoaiPhuKien;
    }

    public void setLoaiPhuKien(String loaiPhuKien) {
        LoaiPhuKien = loaiPhuKien;
    }

    public String getThongTinPhuKien() {
        return ThongTinPhuKien;
    }

    public void setThongTinPhuKien(String thongTinPhuKien) {
        ThongTinPhuKien = thongTinPhuKien;
    }

    public int getGiaPhuKien() {
        return GiaPhuKien;
    }

    public void setGiaPhuKien(int giaPhuKien) {
        GiaPhuKien = giaPhuKien;
    }

    public int getSoHinhAnh() {
        return SoHinhAnh;
    }

    public void setSoHinhAnh(int soHinhAnh) {
        SoHinhAnh = soHinhAnh;
    }

    public int getPhuKienDaBan() {
        return PhuKienDaBan;
    }

    public void setPhuKienDaBan(int phuKienDaBan) {
        PhuKienDaBan = phuKienDaBan;
    }

    public String getHinhAnhPK() {
        return HinhAnhPK;
    }

    public void setHinhAnhPK(String hinhAnhPK) {
        HinhAnhPK = hinhAnhPK;
    }
}
