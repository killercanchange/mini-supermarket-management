package model;

import java.time.LocalDate;

public class Nhanvien {

    private String maNV;
    private String hoten;
    private String SDT;
    private String diachi;
    private double luong;
    private String email;
    private LocalDate Ngaysinh;
    private String Gioitinh;
    private String Taikhoan;
    private String Matkhau;
    private String Vaitro;

    public Nhanvien() {}

    public Nhanvien(String maNV, String hoten, String SDT, String diachi,
                    double luong, String email, LocalDate Ngaysinh, String Gioitinh,
                    String Taikhoan, String Matkhau, String Vaitro) {
        this.maNV = maNV;
        this.hoten = hoten;
        this.SDT = SDT;
        this.diachi = diachi;
        this.luong = luong;
        this.email = email;
        this.Ngaysinh = Ngaysinh;
        this.Gioitinh = Gioitinh;
        this.Taikhoan = Taikhoan;
        this.Matkhau = Matkhau;
        this.Vaitro = Vaitro;
    }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String gethoten() { return hoten; }
    public void sethoten(String hoten) { this.hoten = hoten; }

    public String getSDT() { return SDT; }
    public void setSDT(String SDT) { this.SDT = SDT; }

    public String getdiachi() { return diachi; }
    public void setdiachi(String diachi) { this.diachi = diachi; }

    public double getLuong() { return luong; }
    public void setLuong(double luong) { this.luong = luong; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getNgaysinh() { return Ngaysinh; }
    public void setNgaysinh(LocalDate Ngaysinh) { this.Ngaysinh = Ngaysinh; }

    public String getGioitinh() { return Gioitinh; }
    public void setGioitinh(String Gioitinh) { this.Gioitinh = Gioitinh; }

    public String getTaikhoan() { return Taikhoan; }
    public void setTaikhoan(String Taikhoan) { this.Taikhoan = Taikhoan; }

    public String getMatkhau() { return Matkhau; }
    public void setMatkhau(String Matkhau) { this.Matkhau = Matkhau; }

    public String getVaitro() { return Vaitro; }
    public void setVaitro(String Vaitro) { this.Vaitro = Vaitro; }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(this.Vaitro);
    }

    @Override
    public String toString() {
        return "Nhanvien{" +
                "maNV='" + maNV + '\'' +
                ", hoten='" + hoten + '\'' +
                ", SDT='" + SDT + '\'' +
                ", email='" + email + '\'' +
                ", Vaitro='" + Vaitro + '\'' +
                '}';
    }
}