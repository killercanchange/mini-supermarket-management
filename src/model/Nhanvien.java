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
    private String Vaitro; // "admin" hoặc "nhanvien"

    // Constructor rỗng
    public Nhanvien() {}

    // Constructor đầy đủ
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

    //  GETTER & SETTER

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getHoTen() { return hoten; }
    public void setHoTen(String hoten) { this.hoten = hoten; }

    public String getSoDienThoai() { return SDT; }
    public void setSoDienThoai(String SDT) { this.SDT = SDT; }

    public String getDiaChi() { return diachi; }
    public void setDiaChi(String diachi) { this.diachi = diachi; }

    public double getLuong() { return luong; }
    public void setLuong(double luong) { this.luong = luong; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getNgaySinh() { return Ngaysinh; }
    public void setNgaySinh(LocalDate Ngaysinh) { this.Ngaysinh = Ngaysinh; }

    public String getGioiTinh() { return Gioitinh; }
    public void setGioiTinh(String Gioitinh) { this.Gioitinh = Gioitinh; }

    public String getTaiKhoan() { return Taikhoan; }
    public void setTaiKhoan(String Taikhoan) { this.Taikhoan = Taikhoan; }

    public String getMatKhau() { return Matkhau; }
    public void setMatKhau(String Matkhau) { this.Matkhau = Matkhau; }

    public String getVaiTro() { return Vaitro; }
    public void setVaiTro(String Vaitro) { this.Vaitro = Vaitro; }

    //  PHƯƠNG THỨC TIỆN ÍCH 

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(this.Vaitro);
    }

    @Override
    public String toString() {
        return "Nhanvien{" +
                "maNV='" + maNV + '\'' +
                ", hoTen='" + hoten + '\'' +
                ", soDienThoai='" + SDT + '\'' +
                ", email='" + email + '\'' +
                ", vaiTro='" + Vaitro + '\'' +
                '}';
    }
}