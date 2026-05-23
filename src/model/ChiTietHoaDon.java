package model;

public class ChiTietHoaDon {
    private String maHD;
    private String maSP; // Mã sản phẩm
    private int soLuong;
    private double donGia;
    private double thanhTien;

    // Constructor rỗng
    public ChiTietHoaDon() {}

    // Constructor đầy đủ
    public ChiTietHoaDon(String maHD, String maSP, int soLuong, double donGia) {
        this.maHD = maHD;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = soLuong * donGia; // Tự động tính thành tiền
    }

    // GETTER & SETTER
    public String getMaHD() { return maHD; }
    public void setMaHD(String maHD) { this.maHD = maHD; }

    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { 
        this.soLuong = soLuong; 
        this.thanhTien = this.soLuong * this.donGia;
    }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { 
        this.donGia = donGia; 
        this.thanhTien = this.soLuong * this.donGia;
    }

    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }

    @Override
    public String toString() {
        return "ChiTietHoaDon{" +
                "maHD='" + maHD + '\'' +
                ", maSP='" + maSP + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", thanhTien=" + thanhTien +
                '}';
    }
}