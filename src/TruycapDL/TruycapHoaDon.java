package TruycapDL;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.HoaDon;

public class TruycapHoaDon {

    // ============ MAP RESULT SET ============
    private HoaDon mapResultSet(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();
        hd.setMaHD(rs.getString("maHD"));
        hd.setMaSP(rs.getString("maSP"));
        hd.setTenSP(rs.getString("tenSP"));
        hd.setSoLuongMua(rs.getInt("soLuongMua"));
        hd.setGiaBan(rs.getDouble("giaBan"));
        // tongTien tự cập nhật qua setGiaBan(), nhưng đọc thẳng từ DB cho chắc
        // dùng setter thủ công để tránh tính lại sai nếu 2 setter chưa đồng bộ
        hd.setNgayLap(rs.getDate("ngayLap") != null
                ? rs.getDate("ngayLap").toLocalDate()
                : null);
        return hd;
    }

    // ============ LẤY TẤT CẢ HÓA ĐƠN ============
    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM hoadon ORDER BY ngayLap DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) danhSach.add(mapResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // ============ TÌM THEO MÃ HÓA ĐƠN ============
    public HoaDon getHoaDonByMa(String maHD) {
        String sql = "SELECT * FROM hoadon WHERE maHD = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ============ TÌM THEO MÃ SẢN PHẨM ============
    public List<HoaDon> getHoaDonByMaSP(String maSP) {
        List<HoaDon> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM hoadon WHERE maSP = ? ORDER BY ngayLap DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maSP);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) danhSach.add(mapResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // ============ TÌM THEO TÊN SẢN PHẨM ============
    public List<HoaDon> timTheoTenSP(String tenSP) {
        List<HoaDon> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM hoadon WHERE tenSP LIKE ? ORDER BY ngayLap DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + tenSP + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) danhSach.add(mapResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // ============ LỌC THEO THÁNG / NĂM ============
    public List<HoaDon> layHoaDonTheoThang(int thang, int nam) {
        List<HoaDon> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM hoadon "
                   + "WHERE MONTH(ngayLap) = ? AND YEAR(ngayLap) = ? "
                   + "ORDER BY ngayLap DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) danhSach.add(mapResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            ps.setDate(7, hd.getNgayLap() != null
                    ? Date.valueOf(hd.getNgayLap())
                    : null);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ============ SỬA HÓA ĐƠN ============
    public boolean suaHoaDon(HoaDon hd) {
        String sql = "UPDATE hoadon SET maSP=?, tenSP=?, soLuongMua=?, giaBan=?, tongTien=?, ngayLap=? "
                   + "WHERE maHD=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, hd.getMaSP());
            ps.setString(2, hd.getTenSP());
            ps.setInt(3, hd.getSoLuongMua());
            ps.setDouble(4, hd.getGiaBan());
            ps.setDouble(5, hd.getTongTien());
            ps.setDate(6, hd.getNgayLap() != null
                    ? Date.valueOf(hd.getNgayLap())
                    : null);
            ps.setString(7, hd.getMaHD());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ============ XÓA HÓA ĐƠN ============
    public boolean xoaHoaDon(String maHD) {
        String sql = "DELETE FROM hoadon WHERE maHD = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ============ TÍNH TỔNG DOANH THU THEO THÁNG ============
    // Trả về tổng tongTien của tất cả hóa đơn trong tháng/năm
    public double tinhDoanhThuTheoThang(int thang, int nam) {
        String sql = "SELECT SUM(tongTien) FROM hoadon "
                   + "WHERE MONTH(ngayLap) = ? AND YEAR(ngayLap) = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // ============ SINH MÃ HÓA ĐƠN MỚI ============
    // Định dạng: HD2025001, HD2025002, ...
    public String taoMaHoaDonMoi() {
        int nam = java.time.LocalDate.now().getYear();
        String sql = "SELECT maHD FROM hoadon WHERE maHD LIKE ? ORDER BY maHD DESC LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "HD" + nam + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String maHienTai = rs.getString("maHD"); // VD: HD2025003
                int soThuTu = Integer.parseInt(maHienTai.substring(6)) + 1;
                return String.format("HD%d%03d", nam, soThuTu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return String.format("HD%d001", nam); // Hóa đơn đầu tiên của năm
    }
}