package model;

import java.time.LocalDate;

public class KhachHang {

    private String maKH;
    private String hoten;
    private String SDT;
    private LocalDate Ngaysinh;
    private String diachi;

    // Constructor rỗng
    public KhachHang() {}

    // Constructor đầy đủ
    public KhachHang(String maKH, String hoten, String SDT,
                     LocalDate Ngaysinh, String diachi) {
        this.maKH = maKH;
        this.hoten = hoten;
        this.SDT = SDT;
        this.Ngaysinh = Ngaysinh;
        this.diachi = diachi;
    } 

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public String getHoten() { return hoten; }
    public void setHoten(String hoten) { this.hoten = hoten; }

    public String getSDT() { return SDT; }
    public void setSDT(String SDT) { this.SDT = SDT; }

    public LocalDate getNgaysinh() { return Ngaysinh; }
    public void setNgaysinh(LocalDate Ngaysinh) { this.Ngaysinh = Ngaysinh; }

    public String getDiachi() { return diachi; }
    public void setDiachi(String diachi) { this.diachi = diachi; }

    // PHƯƠNG THỨC TIỆN ÍCH 

    @Override
    public String toString() {
        return "KhachHang{" +
                "maKH='" + maKH + '\'' +
                ", hoten='" + hoten + '\'' +
                ", SDT='" + SDT + '\'' +
                ", diachi='" + diachi + '\'' +
                '}';
    }
}