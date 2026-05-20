package TruycapDL;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.KhachHang;

public class TruycapKH {

    private KhachHang mapResultSet(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();
        kh.setMaKH(rs.getString("maKH"));
        kh.setHoten(rs.getString("hoten"));
        kh.setSDT(rs.getString("SDT"));
        kh.setNgaysinh(rs.getDate("Ngaysinh") != null ? rs.getDate("Ngaysinh").toLocalDate() : null);
        kh.setDiachi(rs.getString("diachi"));
        return kh;
    }

    // LẤY TẤT CẢ KHÁCH HÀNG 
    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM khachhang";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) danhSach.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return danhSach;
    }

    //  TÌM THEO MÃ KH 
    public KhachHang getKhachHangByMa(String maKH) {
        String sql = "SELECT * FROM khachhang WHERE maKH = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKH);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSet(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    //  TÌM THEO SĐT 
    public KhachHang getKhachHangBySDT(String SDT) {
        String sql = "SELECT * FROM khachhang WHERE SDT = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, SDT);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSet(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // THÊM KHÁCH HÀNG 
    public boolean themKhachHang(KhachHang kh) {
        String sql = "INSERT INTO khachhang (maKH, hoten, SDT, Ngaysinh, diachi) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getHoten());
            ps.setString(3, kh.getSDT());
            ps.setDate(4, kh.getNgaysinh() != null ? Date.valueOf(kh.getNgaysinh()) : null);
            ps.setString(5, kh.getDiachi());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    //  SỬA KHÁCH HÀNG 
    public boolean suaKhachHang(KhachHang kh) {
        String sql = "UPDATE khachhang SET hoten=?, SDT=?, Ngaysinh=?, diachi=? WHERE maKH=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kh.getHoten());
            ps.setString(2, kh.getSDT());
            ps.setDate(3, kh.getNgaysinh() != null ? Date.valueOf(kh.getNgaysinh()) : null);
            ps.setString(4, kh.getDiachi());
            ps.setString(5, kh.getMaKH());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    //  XÓA KHÁCH HÀNG 
    public boolean xoaKhachHang(String maKH) {
        String sql = "DELETE FROM khachhang WHERE maKH = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    //  TÌM THEO TÊN 
    public List<KhachHang> timKhachHangTheoTen(String ten) {
        List<KhachHang> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM khachhang WHERE hoten LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + ten + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) danhSach.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return danhSach;
    }
}