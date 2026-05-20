package model;

import java.time.LocalDate;

public class PhieuNhapKho {

    private String maPhieuNhap;
    private String maNCC;
    private String tenNCC;
    private String maSP;
    private String tenSP;
    private int soLuongNhap;
    private double giaNhap;
    private LocalDate ngayNhap;

    // Constructor rỗng
    public PhieuNhapKho() {
    }

    // Constructor đầy đủ
    public PhieuNhapKho(String maPhieuNhap, String maNCC, String tenNCC,
                        String maSP, String tenSP,
                        int soLuongNhap, double giaNhap,
                        LocalDate ngayNhap) {

        this.maPhieuNhap = maPhieuNhap;
        this.maNCC = maNCC;
        this.tenNCC = tenNCC;
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.soLuongNhap = soLuongNhap;
        this.giaNhap = giaNhap;
        this.ngayNhap = ngayNhap;
    }

    // GETTER & SETTER

    public String getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(String maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public String getTenNCC() {
        return tenNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
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

    public int getSoLuongNhap() {
        return soLuongNhap;
    }

    public void setSoLuongNhap(int soLuongNhap) {
        this.soLuongNhap = soLuongNhap;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }

    public LocalDate getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(LocalDate ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    // Tính tổng tiền nhập

    public double tinhTongTien() {
        return soLuongNhap * giaNhap;
    }

    // toString()

    @Override
    public String toString() {
        return "PhieuNhapKho{" +
                "maPhieuNhap='" + maPhieuNhap + '\'' +
                ", maNCC='" + maNCC + '\'' +
                ", tenNCC='" + tenNCC + '\'' +
                ", maSP='" + maSP + '\'' +
                ", tenSP='" + tenSP + '\'' +
                ", soLuongNhap=" + soLuongNhap +
                ", giaNhap=" + giaNhap +
                ", ngayNhap=" + ngayNhap +
                '}';
    }
}