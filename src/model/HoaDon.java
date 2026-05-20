package model;

import java.time.LocalDate;

public class HoaDon {

    private String maHD;
    private String maSP;
    private String tenSP;
    private int soLuongMua;
    private double giaBan;
    private double tongTien;
    private LocalDate ngayLap;

    // Constructor rỗng
    public HoaDon() {
    }

    // Constructor đầy đủ
    public HoaDon(String maHD, String maSP, String tenSP,
                  int soLuongMua, double giaBan,
                  LocalDate ngayLap) {

        this.maHD = maHD;
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.soLuongMua = soLuongMua;
        this.giaBan = giaBan;
        this.ngayLap = ngayLap;

        // tự tính tổng tiền
        this.tongTien = soLuongMua * giaBan;
    }

    // GETTER & SETTER

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public int getSoLuongMua() {
        return soLuongMua;
    }

    public void setSoLuongMua(int soLuongMua) {
        this.soLuongMua = soLuongMua;

        // cập nhật lại tổng tiền
        this.tongTien = this.soLuongMua * this.giaBan;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;

        // cập nhật lại tổng tiền
        this.tongTien = this.soLuongMua * this.giaBan;
    }

    public double getTongTien() {
        return tongTien;
    }

    public LocalDate getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDate ngayLap) {
        this.ngayLap = ngayLap;
    }

    // toString()

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHD='" + maHD + '\'' +
                ", maSP='" + maSP + '\'' +
                ", tenSP='" + tenSP + '\'' +
                ", soLuongMua=" + soLuongMua +
                ", giaBan=" + giaBan +
                ", tongTien=" + tongTien +
                ", ngayLap=" + ngayLap +
                '}';
    }
}