package TruycapDL;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.HoaDon;

public class TruycapHoaDon {

    private HoaDon mapResultSet(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();
        hd.setMaHD(rs.getString("maHD"));
        hd.setMaSP(rs.getString("maSP"));
        hd.setTenSP(rs.getString("tenSP"));
        hd.setSoLuongMua(rs.getInt("soLuongMua"));
        hd.setGiaBan(rs.getDouble("giaBan"));
        hd.setNgayLap(rs.getDate("ngayLap") != null ? rs.getDate("ngayLap").toLocalDate() : null);
        // tongTien tự tính lại trong setter setGiaBan / setSoLuongMua
        return hd;
    }

    // ============ LẤY TẤT CẢ HÓA ĐƠN ============
    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM hoadon";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) danhSach.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return danhSach;
    }

    // ============ TÌM THEO MÃ HĐ ============
    public HoaDon getHoaDonByMa(String maHD) {
        String sql = "SELECT * FROM hoadon WHERE maHD = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSet(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // ============ LẤY HÓA ĐƠN THEO NGÀY ============
    public List<HoaDon> getHoaDonTheoNgay(java.time.LocalDate ngay) {
        List<HoaDon> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM hoadon WHERE ngayLap = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(ngay));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) danhSach.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return danhSach;
    }

    // ============ THÊM HÓA ĐƠN ============
    public boolean themHoaDon(HoaDon hd) {
        String sql = "INSERT INTO hoadon (maHD, maSP, tenSP, soLuongMua, giaBan, tongTien, ngayLap) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, hd.getMaHD());
            ps.setString(2, hd.getMaSP());
            ps.setString(3, hd.getTenSP());
            ps.setInt(4, hd.getSoLuongMua());
            ps.setDouble(5, hd.getGiaBan());
            ps.setDouble(6, hd.getTongTien());
            ps.setDate(7, hd.getNgayLap() != null ? Date.valueOf(hd.getNgayLap()) : null);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ============ SỬA HÓA ĐƠN ============
    public boolean suaHoaDon(HoaDon hd) {
        String sql = "UPDATE hoadon SET maSP=?, tenSP=?, soLuongMua=?, giaBan=?, tongTien=?, ngayLap=? WHERE maHD=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, hd.getMaSP());
            ps.setString(2, hd.getTenSP());
            ps.setInt(3, hd.getSoLuongMua());
            ps.setDouble(4, hd.getGiaBan());
            ps.setDouble(5, hd.getTongTien());
            ps.setDate(6, hd.getNgayLap() != null ? Date.valueOf(hd.getNgayLap()) : null);
            ps.setString(7, hd.getMaHD());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ============ XÓA HÓA ĐƠN ============
    public boolean xoaHoaDon(String maHD) {
        String sql = "DELETE FROM hoadon WHERE maHD = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ============ TÍNH TỔNG DOANH THU THEO NGÀY ============
    public double getTongDoanhThuTheoNgay(java.time.LocalDate ngay) {
        String sql = "SELECT SUM(tongTien) FROM hoadon WHERE ngayLap = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(ngay));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    // ============ TÍNH TỔNG DOANH THU THEO THÁNG ============
    public double getTongDoanhThuTheoThang(int thang, int nam) {
        String sql = "SELECT SUM(tongTien) FROM hoadon WHERE MONTH(ngayLap) = ? AND YEAR(ngayLap) = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
}