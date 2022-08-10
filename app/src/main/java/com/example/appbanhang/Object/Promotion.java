package com.example.appbanhang.Object;

import java.io.Serializable;

public class Promotion implements Serializable {
    private String IdKM;
    private String TenGoiKM;
    private String LoaiSp;
    private String NgayBDKM;
    private String NgayKTKM;
    private String KieuGoiKM;
    private String KM1;
    private String KM2;
    private String KM3;
    private String KM4;
    private String KM5;
    private String HinhAnhKM;
    private String SpKm;

    public Promotion(String idKM, String tenGoiKM, String loaiSp, String ngayBDKM, String ngayKTKM, String kieuGoiKM, String KM1, String KM2, String KM3, String KM4, String KM5, String hinhAnhKM, String spKm) {
        IdKM = idKM;
        TenGoiKM = tenGoiKM;
        LoaiSp = loaiSp;
        NgayBDKM = ngayBDKM;
        NgayKTKM = ngayKTKM;
        KieuGoiKM = kieuGoiKM;
        this.KM1 = KM1;
        this.KM2 = KM2;
        this.KM3 = KM3;
        this.KM4 = KM4;
        this.KM5 = KM5;
        HinhAnhKM = hinhAnhKM;
        SpKm = spKm;
    }

    public Promotion() {
    }

    public String getIdKM() {
        return IdKM;
    }

    public void setIdKM(String idKM) {
        IdKM = idKM;
    }

    public String getTenGoiKM() {
        return TenGoiKM;
    }

    public void setTenGoiKM(String tenGoiKM) {
        TenGoiKM = tenGoiKM;
    }

    public String getLoaiSp() {
        return LoaiSp;
    }

    public void setLoaiSp(String loaiSp) {
        LoaiSp = loaiSp;
    }

    public String getNgayBDKM() {
        return NgayBDKM;
    }

    public void setNgayBDKM(String ngayBDKM) {
        NgayBDKM = ngayBDKM;
    }

    public String getNgayKTKM() {
        return NgayKTKM;
    }

    public void setNgayKTKM(String ngayKTKM) {
        NgayKTKM = ngayKTKM;
    }

    public String getKieuGoiKM() {
        return KieuGoiKM;
    }

    public void setKieuGoiKM(String kieuGoiKM) {
        KieuGoiKM = kieuGoiKM;
    }

    public String getKM1() {
        return KM1;
    }

    public void setKM1(String KM1) {
        this.KM1 = KM1;
    }

    public String getKM2() {
        return KM2;
    }

    public void setKM2(String KM2) {
        this.KM2 = KM2;
    }

    public String getKM3() {
        return KM3;
    }

    public void setKM3(String KM3) {
        this.KM3 = KM3;
    }

    public String getKM4() {
        return KM4;
    }

    public void setKM4(String KM4) {
        this.KM4 = KM4;
    }

    public String getKM5() {
        return KM5;
    }

    public void setKM5(String KM5) {
        this.KM5 = KM5;
    }

    public String getHinhAnhKM() {
        return HinhAnhKM;
    }

    public void setHinhAnhKM(String hinhAnhKM) {
        HinhAnhKM = hinhAnhKM;
    }

    public String getSpKm() {
        return SpKm;
    }

    public void setSpKm(String spKm) {
        SpKm = spKm;
    }
}
