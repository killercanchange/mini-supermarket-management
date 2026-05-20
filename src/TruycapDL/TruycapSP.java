package TruycapDL;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Sanpham;

public class TruycapSP {

    private Sanpham mapResultSet(ResultSet rs) throws SQLException {
        Sanpham sp = new Sanpham();
        sp.setMaSP(rs.getString("maSP"));
        sp.setTenSP(rs.getString("tenSP"));
        sp.setSoLuong(rs.getInt("soLuong"));
        sp.setGiaBan(rs.getDouble("giaBan"));
        sp.setLoaiSP(rs.getString("loaiSP"));
        sp.setNgayHetHan(rs.getDate("ngayHetHan") != null ? rs.getDate("ngayHetHan").toLocalDate() : null);
        sp.setSoLuongToiThieu(rs.getInt("soLuongToiThieu"));
        return sp;
    }

    // LẤY TẤT CẢ SẢN PHẨM 
    public List<Sanpham> getAllSanpham() {
        List<Sanpham> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM sanpham";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) danhSach.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return danhSach;
    }

    // TÌM THEO MÃ SP 
    public Sanpham getSanphamByMa(String maSP) {
        String sql = "SELECT * FROM sanpham WHERE maSP = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maSP);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSet(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // TÌM THEO TÊN 
    public List<Sanpham> timTheoTen(String ten) {
        List<Sanpham> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM sanpham WHERE tenSP LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + ten + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) danhSach.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return danhSach;
    }

    // TÌM THEO LOẠI SP 
    public List<Sanpham> timTheoLoai(String loaiSP) {
        List<Sanpham> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM sanpham WHERE loaiSP = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, loaiSP);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) danhSach.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return danhSach;
    }

    // THÊM SẢN PHẨM 
    public boolean themSanpham(Sanpham sp) {
        String sql = "INSERT INTO sanpham (maSP, tenSP, soLuong, giaBan, loaiSP, ngayHetHan, soLuongToiThieu) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sp.getMaSP());
            ps.setString(2, sp.getTenSP());
            ps.setInt(3, sp.getSoLuong());
            ps.setDouble(4, sp.getGiaBan());
            ps.setString(5, sp.getLoaiSP());
            ps.setDate(6, sp.getNgayHetHan() != null ? Date.valueOf(sp.getNgayHetHan()) : null);
            ps.setInt(7, sp.getSoLuongToiThieu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    //  SỬA SẢN PHẨM 
    public boolean suaSanpham(Sanpham sp) {
        String sql = "UPDATE sanpham SET tenSP=?, soLuong=?, giaBan=?, loaiSP=?, ngayHetHan=?, soLuongToiThieu=? WHERE maSP=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSP());
            ps.setInt(2, sp.getSoLuong());
            ps.setDouble(3, sp.getGiaBan());
            ps.setString(4, sp.getLoaiSP());
            ps.setDate(5, sp.getNgayHetHan() != null ? Date.valueOf(sp.getNgayHetHan()) : null);
            ps.setInt(6, sp.getSoLuongToiThieu());
            ps.setString(7, sp.getMaSP());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    //  XÓA SẢN PHẨM 
    public boolean xoaSanpham(String maSP) {
        String sql = "DELETE FROM sanpham WHERE maSP = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    //  CẬP NHẬT TỒN KHO 
    public boolean capNhatTonKho(String maSP, int soLuongMoi) {
        String sql = "UPDATE sanpham SET soLuong=? WHERE maSP=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, soLuongMoi);
            ps.setString(2, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}