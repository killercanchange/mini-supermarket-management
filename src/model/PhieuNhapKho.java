package model;

import java.time.LocalDate;

public class PhieuNhapKho {

    private String maPN;
    private String maNV; // Mã nhân viên lập phiếu nhập
    private String maNCC; // Mã nhà cung cấp
    private LocalDate ngayNhap;
    private double tongTien; // Tổng giá trị của lô hàng nhập vào
    private String trangThai; // Ví dụ: "Hoàn thành", "Chờ duyệt", "Đã hủy"
    private String ghiChu;

    // Constructor rỗng
    public PhieuNhapKho() {}

    // Constructor đầy đủ
    public PhieuNhapKho(String maPN, String maNV, String maNCC, LocalDate ngayNhap, 
                     double tongTien, String trangThai, String ghiChu) {
        this.maPN = maPN;
        this.maNV = maNV;
        this.maNCC = maNCC;
        this.ngayNhap = ngayNhap;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
    }

    // GETTER & SETTER

    public String getMaPN() { return maPN; }
    public void setMaPN(String maPN) { this.maPN = maPN; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getMaNCC() { return maNCC; }
    public void setMaNCC(String maNCC) { this.maNCC = maNCC; }

    public LocalDate getNgayNhap() { return ngayNhap; }
    public void setNgayNhap(LocalDate ngayNhap) { this.ngayNhap = ngayNhap; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    // PHƯƠNG THỨC TIỆN ÍCH 

    // Kiểm tra xem phiếu nhập đã hoàn thành hay chưa
    public boolean isHoanThanh() {
        return "Hoàn thành".equalsIgnoreCase(this.trangThai) || 
               "Hoan thanh".equalsIgnoreCase(this.trangThai);
    }

    @Override
    public String toString() {
        return "PhieuNhap{" +
                "maPN='" + maPN + '\'' +
                ", maNV='" + maNV + '\'' +
                ", maNCC='" + maNCC + '\'' +
                ", ngayNhap=" + ngayNhap +
                ", tongTien=" + tongTien +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}