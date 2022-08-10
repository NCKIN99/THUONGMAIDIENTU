package com.example.appbanhang.Object;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Item implements Serializable {

    private String LoaiSp;
    private String TenItem;
    private String KeyId;
    private String TenThuongHieu;
    private String Cpu;
    private String BoNhoTrong;
    private String KichThuocManHinh;
    private String Ram;
    private String Pin;
    private String CongKetNoi;
    private String ThongTin;
    private String SoLuong;
    private int DaBan;
    private String CameraTrc;
    private String CameraSau;
    private String HinhAnhDaiDien;
    private int GiaSp;
    private int SoHinhAnh;
    private float CanNang;

    public Item() {
    }


    public Item(String loaiSp, String tenItem, String keyId, String tenThuongHieu, String cpu, String boNhoTrong, String kichThuocManHinh, String ram, String pin, String congKetNoi, String thongTin, String soLuong, int daBan, String cameraTrc, String cameraSau, String hinhAnhDaiDien, int giaSp, int soHinhAnh, float canNang) {
        LoaiSp = loaiSp;
        TenItem = tenItem;
        KeyId = keyId;
        TenThuongHieu = tenThuongHieu;
        Cpu = cpu;
        BoNhoTrong = boNhoTrong;
        KichThuocManHinh = kichThuocManHinh;
        Ram = ram;
        Pin = pin;
        CongKetNoi = congKetNoi;
        ThongTin = thongTin;
        SoLuong = soLuong;
        DaBan = daBan;
        CameraTrc = cameraTrc;
        CameraSau = cameraSau;
        HinhAnhDaiDien = hinhAnhDaiDien;
        GiaSp = giaSp;
        SoHinhAnh = soHinhAnh;
        CanNang = canNang;
    }

    public void setHinhAnhDaiDien(String hinhAnhDaiDien) {
        HinhAnhDaiDien = hinhAnhDaiDien;
    }

    public float getCanNang() {
        return CanNang;
    }

    public void setCanNang(float canNang) {
        CanNang = canNang;
    }

    public String getCameraTrc() {
        return CameraTrc;
    }

    public void setCameraTrc(String cameraTrc) {
        CameraTrc = cameraTrc;
    }

    public String getCameraSau() {
        return CameraSau;
    }

    public void setCameraSau(String cameraSau) {
        CameraSau = cameraSau;
    }

    public String getLoaiSp() {
        return LoaiSp;
    }

    public void setLoaiSp(String loaiSp) {
        LoaiSp = loaiSp;
    }

    public String getTenItem() {
        return TenItem;
    }

    public void setTenItem(String tenItem) {
        TenItem = tenItem;
    }

    public String getKeyId() {
        return KeyId;
    }

    public void setKeyId(String keyId) {
        KeyId = keyId;
    }

    public String getTenThuongHieu() {
        return TenThuongHieu;
    }

    public void setTenThuongHieu(String tenThuongHieu) {
        TenThuongHieu = tenThuongHieu;
    }

    public String getCpu() {
        return Cpu;
    }

    public void setCpu(String cpu) {
        Cpu = cpu;
    }

    public String getBoNhoTrong() {
        return BoNhoTrong;
    }

    public void setBoNhoTrong(String boNhoTrong) {
        BoNhoTrong = boNhoTrong;
    }

    public String getKichThuocManHinh() {
        return KichThuocManHinh;
    }

    public void setKichThuocManHinh(String kichThuocManHinh) {
        KichThuocManHinh = kichThuocManHinh;
    }

    public String getRam() {
        return Ram;
    }

    public void setRam(String ram) {
        Ram = ram;
    }

    public String getPin() {
        return Pin;
    }

    public void setPin(String pin) {
        Pin = pin;
    }

    public String getCongKetNoi() {
        return CongKetNoi;
    }

    public void setCongKetNoi(String congKetNoi) {
        CongKetNoi = congKetNoi;
    }

    public String getThongTin() {
        return ThongTin;
    }

    public void setThongTin(String thongTin) {
        ThongTin = thongTin;
    }

    public String getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(String soLuong) {
        SoLuong = soLuong;
    }

    public int getDaBan() {
        return DaBan;
    }

    public void setDaBan(int daBan) {
        DaBan = daBan;
    }

    public int getGiaSp() {
        return GiaSp;
    }

    public void setGiaSp(int giaSp) {
        GiaSp = giaSp;
    }

    public int getSoHinhAnh() {
        return SoHinhAnh;
    }

    public void setSoHinhAnh(int soHinhAnh) {
        SoHinhAnh = soHinhAnh;
    }

    public String getHinhAnhDaiDien() {
        return HinhAnhDaiDien;
    }

//    public void setHinhAnhDaiDien(String hinhAnhDaiDien) {
//        HinhAnhDaiDien = hinhAnhDaiDien;
//    }
//        public Map<String, Object> toMap(){
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("loaiSp", LoaiSp);
//        result.put("tenItem", TenItem);
//        result.put("keyId", KeyId);
//        result.put("tenThuongHieu", TenThuongHieu);
//        result.put("cpu", Cpu);
//        result.put("boNhoTrong", BoNhoTrong);
//        result.put("kichThuocManHinh", KichThuocManHinh);
//        result.put("ram", Ram);
//        result.put("pin", Pin);
//        result.put("congKetNoi", CongKetNoi);
//        result.put("thongTin", ThongTin);
//        result.put("soLuong", SoLuong);
//        result.put("daBan", DaBan);
//        result.put("giaSp", GiaSp);
//        result.put("soHinhAnh", SoHinhAnh);
//        result.put("hinhAnhDaiDien", HinhAnhDaiDien);
//        result.put("cameraSau", CameraSau);
//        result.put("cameraTrc", CameraTrc);
//        result.put("canNang", CanNang);
//        return result;
//    }
}

