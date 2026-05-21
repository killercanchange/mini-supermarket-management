package TruycapDL;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.HoaDon;

public class TruycapHoaDon {

    private HoaDon mapResultSet(ResultSet rs) throws SQLException {
        // Đồng bộ ánh xạ đúng theo cấu trúc database thực tế của bạn
        HoaDon hd = new HoaDon();
        hd.setMaHD(rs.getString("maHD"));
        hd.setMaNV(rs.getString("maNV"));
        hd.setMaKH(rs.getString("maKH"));
        hd.setNgayTao(rs.getDate("ngayTao") != null ? rs.getDate("ngayTao").toLocalDate() : null);
        hd.setTongTien(rs.getDouble("tongTien"));
        hd.setTrangThai(rs.getString("trangThai"));
        hd.setGhiChu(rs.getString("ghiChu"));
        return hd;
    }

    // LẤY TẤT CẢ HÓA ĐƠN
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

    // TÌM THEO MÃ HÓA ĐƠN
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

    // LẤY DANH SÁCH HÓA ĐƠN THEO NGÀY
    public List<HoaDon> getHoaDonTheoNgay(java.time.LocalDate ngay) {
        List<HoaDon> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM hoadon WHERE ngayTao = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(ngay));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) danhSach.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return danhSach;
    }

    // LẤY TỔNG DOANH THU THEO NGÀY
    public double getTongDoanhThuTheoNgay(java.time.LocalDate ngay) {
        String sql = "SELECT SUM(tongTien) AS tongDoanhThu FROM hoadon WHERE ngayTao = ? "
                   + "AND (trangThai = N'Đã thanh toán' OR trangThai = 'Da thanh toan' OR trangThai IS NULL)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(ngay));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double doanhThu = rs.getDouble("tongDoanhThu");
                    return rs.wasNull() ? 0.0 : doanhThu;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    // =========================================================
    // VỪA BỔ SUNG: LẤY TỔNG DOANH THU THEO THÁNG (Chuẩn hóa 100%)
    // =========================================================
    public double getTongDoanhThuTheoThang(int thang, int nam) {
        String sql = "SELECT SUM(tongTien) AS tongDoanhThu FROM hoadon "
                   + "WHERE MONTH(ngayTao) = ? AND YEAR(ngayTao) = ?";
                   
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            // Đặt đúng vị trí tham số: Tháng số 1, Năm số 2
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double doanhThu = rs.getDouble("tongDoanhThu");
                    // Tránh lỗi nếu tháng đó chưa có dữ liệu bán hàng (Null)
                    return rs.wasNull() ? 0.0 : doanhThu;
                }
            }
        } catch (SQLException e) { 
            System.out.println("Lỗi tại hàm getTongDoanhThuTheoThang: " + e.getMessage());
            e.printStackTrace(); 
        }
        return 0.0;
    }

    // THÊM HÓA ĐƠN
    public boolean themHoaDon(HoaDon hd) {
        String sql = "INSERT INTO hoadon (maHD, maNV, maKH, ngayTao, tongTien, trangThai, ghiChu) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, hd.getMaHD());
            ps.setString(2, hd.getMaNV());
            ps.setString(3, hd.getMaKH());
            ps.setDate(4, hd.getNgayTao() != null ? Date.valueOf(hd.getNgayTao()) : null);
            ps.setDouble(5, hd.getTongTien());
            ps.setString(6, hd.getTrangThai());
            ps.setString(7, hd.getGhiChu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // SỬA HÓA ĐƠN (Đã kiểm tra và sửa lại thứ tự set dữ liệu chống lỗi lệch cột)
    public boolean suaHoaDon(HoaDon hd) {
        String sql = "UPDATE hoadon SET maNV=?, maKH=?, ngayTao=?, tongTien=?, trangThai=?, ghiChu=? WHERE maHD=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, hd.getMaNV());
            ps.setString(2, hd.getMaKH());
            ps.setDate(3, hd.getNgayTao() != null ? Date.valueOf(hd.getNgayTao()) : null);
            ps.setDouble(4, hd.getTongTien());
            ps.setString(5, hd.getTrangThai()); // Đã sửa từ vị trí 6 về vị trí 5
            ps.setString(6, hd.getGhiChu());    // Đã sửa từ vị trí 5 về vị trí 6
            ps.setString(7, hd.getMaHD());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // XÓA HÓA ĐƠN
    public boolean xoaHoaDon(String maHD) {
        String sql = "DELETE FROM hoadon WHERE maHD = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // TÌM THEO MÃ KHÁCH HÀNG HOẶC MÃ HÓA ĐƠN
    public List<HoaDon> timHoaDon(String tuKhoa) {
        List<HoaDon> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM hoadon WHERE maHD LIKE ? OR maKH LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + tuKhoa + "%");
            ps.setString(2, "%" + tuKhoa + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) danhSach.add(mapResultSet(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return danhSach;
    }
}