package model;

public class Sanpham {

    private String maSP;
    private String tenSP;
    private int soLuong;
    private double giaBan;

    // Constructor rỗng
    public Sanpham() {
    }

    // Constructor đầy đủ
    public Sanpham(String maSP, String tenSP, int soLuong, double giaBan) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
    }

    // GETTER & SETTER

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

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    // toString()

    @Override
    public String toString() {
        return "Sanpham{" +
                "maSP='" + maSP + '\'' +
                ", tenSP='" + tenSP + '\'' +
                ", soLuong=" + soLuong +
                ", giaBan=" + giaBan +
                '}';
    }
}