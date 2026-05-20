package TruycapDL;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Nhanvien;

public class TruycapNV {

    private Nhanvien mapResultSet(ResultSet rs) throws SQLException {
        Nhanvien nv = new Nhanvien();
        nv.setMaNV(rs.getString("maNV"));
        nv.sethoten(rs.getString("hoten"));
        nv.setSDT(rs.getString("SDT"));
        nv.setdiachi(rs.getString("diachi"));
        nv.setLuong(rs.getDouble("luong"));
        nv.setEmail(rs.getString("email"));
        nv.setNgaysinh(rs.getDate("Ngaysinh") != null ? rs.getDate("Ngaysinh").toLocalDate() : null);
        nv.setGioitinh(rs.getString("Gioitinh"));
        nv.setTaikhoan(rs.getString("Taikhoan"));
        nv.setMatkhau(rs.getString("Matkhau"));
        nv.setVaitro(rs.getString("Vaitro"));
        return nv;
    }

    //  LẤY TẤT CẢ NHÂN VIÊN 
    public List<Nhanvien> getAllNhanvien() {
        List<Nhanvien> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM nhanvien";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) danhSach.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return danhSach;
    }

    //  TÌM THEO MÃ NV 
    public Nhanvien getNhanvienByMa(String maNV) {
        String sql = "SELECT * FROM nhanvien WHERE maNV = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNV);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSet(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    //  ĐĂNG NHẬP
    public Nhanvien dangNhap(String taikhoan, String matkhau) {
        String sql = "SELECT * FROM nhanvien WHERE Taikhoan = ? AND Matkhau = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, taikhoan);
            ps.setString(2, matkhau);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSet(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // THÊM NHÂN VIÊN 
    public boolean themNhanvien(Nhanvien nv) {
        String sql = "INSERT INTO nhanvien (maNV, hoten, SDT, diachi, luong, email, Ngaysinh, Gioitinh, Taikhoan, Matkhau, Vaitro) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.gethoten());
            ps.setString(3, nv.getSDT());
            ps.setString(4, nv.getdiachi());
            ps.setDouble(5, nv.getLuong());
            ps.setString(6, nv.getEmail());
            ps.setDate(7, nv.getNgaysinh() != null ? Date.valueOf(nv.getNgaysinh()) : null);
            ps.setString(8, nv.getGioitinh());
            ps.setString(9, nv.getTaikhoan());
            ps.setString(10, nv.getMatkhau());
            ps.setString(11, nv.getVaitro());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // SỬA NHÂN VIÊN
    public boolean suaNhanvien(Nhanvien nv) {
        String sql = "UPDATE nhanvien SET hoten=?, SDT=?, diachi=?, luong=?, email=?, "
                   + "Ngaysinh=?, Gioitinh=?, Taikhoan=?, Matkhau=?, Vaitro=? WHERE maNV=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.gethoten());
            ps.setString(2, nv.getSDT());
            ps.setString(3, nv.getdiachi());
            ps.setDouble(4, nv.getLuong());
            ps.setString(5, nv.getEmail());
            ps.setDate(6, nv.getNgaysinh() != null ? Date.valueOf(nv.getNgaysinh()) : null);
            ps.setString(7, nv.getGioitinh());
            ps.setString(8, nv.getTaikhoan());
            ps.setString(9, nv.getMatkhau());
            ps.setString(10, nv.getVaitro());
            ps.setString(11, nv.getMaNV());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    //  XÓA NHÂN VIÊN 
    public boolean xoaNhanvien(String maNV) {
        String sql = "DELETE FROM nhanvien WHERE maNV = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNV);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // TÌM THEO TÊN 
    public List<Nhanvien> timNhanvienTheoTen(String ten) {
        List<Nhanvien> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM nhanvien WHERE hoten LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + ten + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) danhSach.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return danhSach;
    }
}