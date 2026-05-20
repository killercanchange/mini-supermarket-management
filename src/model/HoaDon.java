package model;

import java.time.LocalDate;

public class HoaDon {

    private String maHD;
    private String maNV; // Mã nhân viên lập hóa đơn
    private String maKH; // Mã khách hàng (có thể null nếu khách lẻ)
    private LocalDate ngayTao;
    private double tongTien;
    private String trangThai; // Ví dụ: "Đã thanh toán", "Chưa thanh toán"
    private String ghiChu;

    // Constructor rỗng
    public HoaDon() {}

    // Constructor đầy đủ
    public HoaDon(String maHD, String maNV, String maKH, LocalDate ngayTao, 
                  double tongTien, String trangThai, String ghiChu) {
        this.maHD = maHD;
        this.maNV = maNV;
        this.maKH = maKH;
        this.ngayTao = ngayTao;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
    }

    // GETTER & SETTER

    public String getMaHD() { return maHD; }
    public void setMaHD(String maHD) { this.maHD = maHD; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public LocalDate getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDate ngayTao) { this.ngayTao = ngayTao; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    // PHƯƠNG THỨC TIỆN ÍCH 

    // Kiểm tra xem hóa đơn đã được thanh toán hay chưa
    public boolean isDaThanhToan() {
        return "Đã thanh toán".equalsIgnoreCase(this.trangThai) || 
               "Da thanh toan".equalsIgnoreCase(this.trangThai);
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHD='" + maHD + '\'' +
                ", maNV='" + maNV + '\'' +
                ", maKH='" + maKH + '\'' +
                ", ngayTao=" + ngayTao +
                ", tongTien=" + tongTien +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}
