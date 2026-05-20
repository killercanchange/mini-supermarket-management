package model;

import java.time.LocalDate;

public class Sanpham {

    private String maSP;
    private String tenSP;
    private int soLuong;
    private double giaBan;
    private String loaiSP;          // thực phẩm, đồ uống, hóa mỹ phẩm...
    private LocalDate ngayHetHan;   // hạn sử dụng
    private int soLuongToiThieu;    // ngưỡng cảnh báo tồn kho thấp

    // Constructor rỗng
    public Sanpham() {}

    // Constructor đầy đủ
    public Sanpham(String maSP, String tenSP, int soLuong, double giaBan,
                   String loaiSP, LocalDate ngayHetHan, int soLuongToiThieu) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
        this.loaiSP = loaiSP;
        this.ngayHetHan = ngayHetHan;
        this.soLuongToiThieu = soLuongToiThieu;
    }

    // ============ GETTER & SETTER ============

    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }

    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public double getGiaBan() { return giaBan; }
    public void setGiaBan(double giaBan) { this.giaBan = giaBan; }

    public String getLoaiSP() { return loaiSP; }
    public void setLoaiSP(String loaiSP) { this.loaiSP = loaiSP; }

    public LocalDate getNgayHetHan() { return ngayHetHan; }
    public void setNgayHetHan(LocalDate ngayHetHan) { this.ngayHetHan = ngayHetHan; }

    public int getSoLuongToiThieu() { return soLuongToiThieu; }
    public void setSoLuongToiThieu(int soLuongToiThieu) { this.soLuongToiThieu = soLuongToiThieu; }

    // ============ PHƯƠNG THỨC TIỆN ÍCH ============

    // kiểm tra tồn kho thấp
    public boolean isTonKhoThap() {
        return this.soLuong <= this.soLuongToiThieu;
    }

    // kiểm tra sắp hết hạn (trong vòng 30 ngày)
    public boolean isSapHetHan() {
        if (this.ngayHetHan == null) return false;
        return !LocalDate.now().isAfter(this.ngayHetHan)
                && LocalDate.now().plusDays(30).isAfter(this.ngayHetHan);
    }

    // kiểm tra đã hết hạn
    public boolean isHetHan() {
        if (this.ngayHetHan == null) return false;
        return LocalDate.now().isAfter(this.ngayHetHan);
    }

    @Override
    public String toString() {
        return "Sanpham{" +
                "maSP='" + maSP + '\'' +
                ", tenSP='" + tenSP + '\'' +
                ", soLuong=" + soLuong +
                ", giaBan=" + giaBan +
                ", loaiSP='" + loaiSP + '\'' +
                ", ngayHetHan=" + ngayHetHan +
                ", soLuongToiThieu=" + soLuongToiThieu +
                '}';
    }
}