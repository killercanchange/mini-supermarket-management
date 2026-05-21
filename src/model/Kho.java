package model;

import java.time.LocalDate;

/**
 * File này chứa 2 class phục vụ nghiệp vụ kho:
 *
 *  1. Kho              – Tồn kho của 1 sản phẩm (dùng cho bảng cảnh báo tồn kho)
 *  2. ChiTietPhieuNhap – 1 dòng sản phẩm trong phiếu nhập (dùng khi tạo phiếu nhập mới)
 *
 * Lưu ý Java: chỉ class Kho được khai báo public (trùng tên file).
 *             ChiTietPhieuNhap là package-private (không có từ khóa public).
 */


// CLASS 1: KHO – thông tin tồn kho của 1 sản phẩm

public class Kho {

    private String    maSP;             // Khóa ngoại → Sanpham.maSP
    private String    tenSP;            // Lấy thêm để hiển thị, không cần join
    private String    loaiSP;           // Loại sản phẩm (thực phẩm, đồ uống...)
    private int       soLuong;          // Tồn kho hiện tại
    private int       soLuongToiThieu;  // Ngưỡng cảnh báo
    private LocalDate ngayCapNhat;      // Ngày cập nhật tồn kho gần nhất

    // Constructor rỗng

    public Kho() {}


    // Constructor đầy đủ

    public Kho(String maSP, String tenSP, String loaiSP,
               int soLuong, int soLuongToiThieu, LocalDate ngayCapNhat) {
        this.maSP            = maSP;
        this.tenSP           = tenSP;
        this.loaiSP          = loaiSP;
        this.soLuong         = soLuong;
        this.soLuongToiThieu = soLuongToiThieu;
        this.ngayCapNhat     = ngayCapNhat;
    }


    // Getters & Setters

    public String getMaSP()                       { return maSP; }
    public void   setMaSP(String maSP)            { this.maSP = maSP; }

    public String getTenSP()                      { return tenSP; }
    public void   setTenSP(String tenSP)          { this.tenSP = tenSP; }

    public String getLoaiSP()                     { return loaiSP; }
    public void   setLoaiSP(String loaiSP)        { this.loaiSP = loaiSP; }

    public int  getSoLuong()                      { return soLuong; }
    public void setSoLuong(int soLuong)           { this.soLuong = soLuong; }

    public int  getSoLuongToiThieu()                        { return soLuongToiThieu; }
    public void setSoLuongToiThieu(int soLuongToiThieu)     { this.soLuongToiThieu = soLuongToiThieu; }

    public LocalDate getNgayCapNhat()                       { return ngayCapNhat; }
    public void      setNgayCapNhat(LocalDate ngayCapNhat)  { this.ngayCapNhat = ngayCapNhat; }


    // Phương thức tiện ích — đồng nhất với Sanpham.java của nhóm


    /** true khi tồn kho ở mức báo động (soLuong <= soLuongToiThieu) */
    public boolean isTonKhoThap() {
        return this.soLuong <= this.soLuongToiThieu;
    }

    /** Số lượng cần nhập thêm để đạt mức tối thiểu */
    public int soLuongCanNhapThem() {
        return Math.max(0, this.soLuongToiThieu - this.soLuong);
    }

    @Override
    public String toString() {
        return "Kho{" +
                "maSP='"           + maSP            + '\'' +
                ", tenSP='"        + tenSP            + '\'' +
                ", soLuong="       + soLuong          +
                ", soLuongToiThieu=" + soLuongToiThieu +
                ", ngayCapNhat="   + ngayCapNhat      +
                '}';
    }
}


// CLASS 2: ChiTietPhieuNhap – 1 dòng sản phẩm trong phiếu nhập

class ChiTietPhieuNhap {

    private String maPN;          // FK → PhieuNhapKho.maPN
    private String maSP;          // FK → Sanpham.maSP
    private String tenSP;         // Lấy thêm để hiển thị trên bảng
    private int    soLuongNhap;   // Số lượng nhập trong dòng này
    private double giaNhap;       // Giá nhập (có thể khác giaBan)
    private double thanhTien;     // = soLuongNhap * giaNhap (tự tính)


    // Constructor rỗng



    // Constructor đầy đủ — thanhTien tự tính

    ChiTietPhieuNhap(String maPN, String maSP, String tenSP,
                     int soLuongNhap, double giaNhap) {
        this.maPN        = maPN;
        this.maSP        = maSP;
        this.tenSP       = tenSP;
        this.soLuongNhap = soLuongNhap;
        this.giaNhap     = giaNhap;
        this.thanhTien   = soLuongNhap * giaNhap; // tự tính như HoaDon.java
    }


    // Getters & Setters

    public String getMaPN()               { return maPN; }
    public void   setMaPN(String maPN)    { this.maPN = maPN; }

    public String getMaSP()               { return maSP; }
    public void   setMaSP(String maSP)    { this.maSP = maSP; }

    public String getTenSP()              { return tenSP; }
    public void   setTenSP(String tenSP)  { this.tenSP = tenSP; }

    public int  getSoLuongNhap()                    { return soLuongNhap; }
    public void setSoLuongNhap(int soLuongNhap) {
        this.soLuongNhap = soLuongNhap;
        this.thanhTien   = this.soLuongNhap * this.giaNhap; // cập nhật tự động
    }

    public double getGiaNhap()                  { return giaNhap; }
    public void   setGiaNhap(double giaNhap) {
        this.giaNhap   = giaNhap;
        this.thanhTien = this.soLuongNhap * this.giaNhap;   // cập nhật tự động
    }

    public double getThanhTien()  { return thanhTien; } // chỉ có getter, không set trực tiếp

    @Override
    public String toString() {
        return "ChiTietPhieuNhap{" +
                "maPN='"         + maPN        + '\'' +
                ", maSP='"       + maSP        + '\'' +
                ", tenSP='"      + tenSP       + '\'' +
                ", soLuongNhap=" + soLuongNhap +
                ", giaNhap="     + giaNhap     +
                ", thanhTien="   + thanhTien   +
                '}';
    }
}