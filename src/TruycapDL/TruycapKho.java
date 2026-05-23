package TruycapDL;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Kho;

public class TruycapKho {

    // ============ MAP RESULT SET ============
    private Kho mapResultSet(ResultSet rs) throws SQLException {
        Kho kho = new Kho();
        kho.setMaKho(rs.getString("maKho"));
        kho.setTenKho(rs.getString("tenKho"));
        kho.setDiaChi(rs.getString("diachi"));
        kho.setSucChua(rs.getInt("sucChua"));
        kho.setGhiChu(rs.getString("ghiChu"));
        return kho;
    }

    // ============ LẤY TẤT CẢ KHO ============
    public List<Kho> getAllKho() {
        List<Kho> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM kho";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) danhSach.add(mapResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // ============ TÌM THEO MÃ KHO ============
    public Kho getKhoByMa(String maKho) {
        String sql = "SELECT * FROM kho WHERE maKho = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKho);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ============ TÌM THEO TÊN KHO ============
    public List<Kho> timTheoTenKho(String tenKho) {
        List<Kho> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM kho WHERE tenKho LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + tenKho + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) danhSach.add(mapResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // ============ THÊM KHO ============
    public boolean themKho(Kho kho) {
        String sql = "INSERT INTO kho (maKho, tenKho, diachi, sucChua, ghiChu) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kho.getMaKho());
            ps.setString(2, kho.getTenKho());
            ps.setString(3, kho.getDiaChi());
            ps.setInt(4, kho.getSucChua());
            ps.setString(5, kho.getGhiChu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ============ SỬA KHO ============
    public boolean suaKho(Kho kho) {
        String sql = "UPDATE kho SET tenKho=?, diachi=?, sucChua=?, ghiChu=? WHERE maKho=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kho.getTenKho());
            ps.setString(2, kho.getDiaChi());
            ps.setInt(3, kho.getSucChua());
            ps.setString(4, kho.getGhiChu());
            ps.setString(5, kho.getMaKho());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ============ XÓA KHO ============
    public boolean xoaKho(String maKho) {
        String sql = "DELETE FROM kho WHERE maKho = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKho);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ============ KIỂM TRA KHO CÒN CHỖ KHÔNG ============
    public boolean kiemTraConCho(String maKho) {
        String sql = "SELECT sucChua FROM kho WHERE maKho = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKho);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("sucChua") > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ============ SINH MÃ KHO MỚI ============
    public String taoMaKhoMoi() {
        String sql = "SELECT maKho FROM kho ORDER BY maKho DESC LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                String maHienTai = rs.getString("maKho");
                int soThuTu = Integer.parseInt(maHienTai.substring(3)) + 1;
                return String.format("KHO%03d", soThuTu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "KHO001";
    }
}