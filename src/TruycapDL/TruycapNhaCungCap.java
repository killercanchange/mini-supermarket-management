package TruycapDL;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.NhaCungCap;

public class TruycapNhaCungCap {

    private NhaCungCap mapResultSet(ResultSet rs) throws SQLException {
        NhaCungCap ncc = new NhaCungCap();
        ncc.setMaNCC(rs.getString("maNCC"));
        ncc.setTenNCC(rs.getString("tenNCC"));
        ncc.setSoDienThoai(rs.getString("soDienThoai"));
        ncc.setDiaChi(rs.getString("diaChi"));
        ncc.setEmail(rs.getString("email"));
        return ncc;
    }

    // ============ LẤY TẤT CẢ NHÀ CUNG CẤP ============
    public List<NhaCungCap> getAllNhaCungCap() {
        List<NhaCungCap> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM nhacungcap";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) danhSach.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return danhSach;
    }

    // ============ TÌM THEO MÃ NCC ============
    public NhaCungCap getNhaCungCapByMa(String maNCC) {
        String sql = "SELECT * FROM nhacungcap WHERE maNCC = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNCC);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSet(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // ============ TÌM THEO TÊN ============
    public List<NhaCungCap> timTheoTen(String ten) {
        List<NhaCungCap> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM nhacungcap WHERE tenNCC LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + ten + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) danhSach.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return danhSach;
    }

    // ============ THÊM NHÀ CUNG CẤP ============
    public boolean themNhaCungCap(NhaCungCap ncc) {
        String sql = "INSERT INTO nhacungcap (maNCC, tenNCC, soDienThoai, diaChi, email) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ncc.getMaNCC());
            ps.setString(2, ncc.getTenNCC());
            ps.setString(3, ncc.getSoDienThoai());
            ps.setString(4, ncc.getDiaChi());
            ps.setString(5, ncc.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ============ SỬA NHÀ CUNG CẤP ============
    public boolean suaNhaCungCap(NhaCungCap ncc) {
        String sql = "UPDATE nhacungcap SET tenNCC=?, soDienThoai=?, diaChi=?, email=? WHERE maNCC=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ncc.getTenNCC());
            ps.setString(2, ncc.getSoDienThoai());
            ps.setString(3, ncc.getDiaChi());
            ps.setString(4, ncc.getEmail());
            ps.setString(5, ncc.getMaNCC());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // ============ XÓA NHÀ CUNG CẤP ============
    public boolean xoaNhaCungCap(String maNCC) {
        String sql = "DELETE FROM nhacungcap WHERE maNCC = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNCC);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}