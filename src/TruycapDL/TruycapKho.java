package TruycapDL;

import config.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.PhieuNhapKho;
import model.Sanpham;

/**
 * TruycapKho – Data Access Object cho nghiệp vụ kho.
 *
 * Dựa theo PhieuNhapKho.java thực tế của nhóm:
 *   fields: maPhieuNhap, maNCC, tenNCC, maSP, tenSP,
 *           soLuongNhap, giaNhap, ngayNhap
 *
 * Pattern viết theo chuẩn TruycapNhaCungCap.java của nhóm:
 *   - mapResultSet() private helper
 *   - try-with-resources
 *   - return boolean cho thêm/sửa/xóa
 *
 * Danh sách phương thức:
 *   1. themPhieuNhap()        – Thêm 1 dòng phiếu nhập, tự cộng tồn kho SP
 *   2. getAllPhieuNhap()       – Lấy toàn bộ phiếu, mới nhất lên đầu
 *   3. layPhieuTheoThang()    – Lọc phiếu theo tháng/năm
 *   4. layPhieuTheoMaNCC()    – Lọc phiếu theo nhà cung cấp
 *   5. xoaPhieuNhap()         – Xóa phiếu, hoàn lại tồn kho SP
 *   6. laySPTonKhoThap()      – SP có soLuong <= soLuongToiThieu
 *   7. laySPSapHetHan()       – SP hết hạn trong 30 ngày tới
 *   8. laySPDaHetHan()        – SP đã quá ngày hết hạn
 *   9. taoMaPhieuMoi()        – Sinh mã tự động dạng PN2025001
 */
public class TruycapKho {


    // PRIVATE HELPER – mapResultSet (chuẩn nhóm)


    /**
     * Map ResultSet → PhieuNhapKho.
     * Tên cột khớp bảng phieunhapkho và getter PhieuNhapKho.java.
     */
    private PhieuNhapKho mapResultSet(ResultSet rs) throws SQLException {
        PhieuNhapKho p = new PhieuNhapKho();
        p.setMaPhieuNhap(rs.getString("maPhieuNhap"));
        p.setMaNCC      (rs.getString("maNCC"));
        p.setTenNCC     (rs.getString("tenNCC"));
        p.setMaSP       (rs.getString("maSP"));
        p.setTenSP      (rs.getString("tenSP"));
        p.setSoLuongNhap(rs.getInt   ("soLuongNhap"));
        p.setGiaNhap    (rs.getDouble("giaNhap"));
        p.setNgayNhap   (rs.getDate  ("ngayNhap").toLocalDate());
        return p;
    }


    // 1. THÊM PHIẾU NHẬP (transaction: chèn phiếu + cộng tồn kho)


    /**
     * Thêm 1 dòng phiếu nhập kho, đồng thời cộng soLuongNhap vào tồn kho SP.
     * Dùng transaction: nếu 1 bước lỗi → rollback toàn bộ.
     */
    public boolean themPhieuNhap(PhieuNhapKho phieu) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Bước 1: Chèn phiếu nhập
            String sqlPhieu = "INSERT INTO phieunhapkho "
                    + "(maPhieuNhap, maNCC, tenNCC, maSP, tenSP, "
                    + " soLuongNhap, giaNhap, ngayNhap) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(sqlPhieu)) {
                ps.setString(1, phieu.getMaPhieuNhap());
                ps.setString(2, phieu.getMaNCC());
                ps.setString(3, phieu.getTenNCC());
                ps.setString(4, phieu.getMaSP());
                ps.setString(5, phieu.getTenSP());
                ps.setInt   (6, phieu.getSoLuongNhap());
                ps.setDouble(7, phieu.getGiaNhap());
                ps.setDate  (8, Date.valueOf(phieu.getNgayNhap()));
                ps.executeUpdate();
            }

            // Bước 2: Cộng soLuongNhap vào tồn kho bảng sanpham
            String sqlCapNhatKho = "UPDATE sanpham "
                    + "SET soLuong = soLuong + ? "
                    + "WHERE maSP = ?";

            try (PreparedStatement ps = conn.prepareStatement(sqlCapNhatKho)) {
                ps.setInt   (1, phieu.getSoLuongNhap());
                ps.setString(2, phieu.getMaSP());
                ps.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }


    // 2. LẤY TOÀN BỘ PHIẾU NHẬP


    public List<PhieuNhapKho> getAllPhieuNhap() {
        List<PhieuNhapKho> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM phieunhapkho ORDER BY ngayNhap DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement st   = conn.createStatement();
             ResultSet rs   = st.executeQuery(sql)) {

            while (rs.next()) danhSach.add(mapResultSet(rs));

        } catch (SQLException e) { e.printStackTrace(); }
        return danhSach;
    }


    // 3. LỌC PHIẾU THEO THÁNG / NĂM


    public List<PhieuNhapKho> layPhieuTheoThang(int thang, int nam) {
        List<PhieuNhapKho> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM phieunhapkho "
                   + "WHERE MONTH(ngayNhap) = ? AND YEAR(ngayNhap) = ? "
                   + "ORDER BY ngayNhap DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) danhSach.add(mapResultSet(rs));

        } catch (SQLException e) { e.printStackTrace(); }
        return danhSach;
    }


    // 4. LỌC PHIẾU THEO MÃ NHÀ CUNG CẤP


    public List<PhieuNhapKho> layPhieuTheoMaNCC(String maNCC) {
        List<PhieuNhapKho> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM phieunhapkho "
                   + "WHERE maNCC = ? "
                   + "ORDER BY ngayNhap DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNCC);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) danhSach.add(mapResultSet(rs));

        } catch (SQLException e) { e.printStackTrace(); }
        return danhSach;
    }


    // 5. XÓA PHIẾU NHẬP (transaction: xóa phiếu + hoàn lại tồn kho)


    /**
     * Xóa 1 phiếu nhập, đồng thời trừ lại soLuongNhap khỏi tồn kho SP.
     */
    public boolean xoaPhieuNhap(String maPhieuNhap) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Bước 1: Lấy thông tin phiếu trước khi xóa (cần maSP, soLuongNhap)
            String sqlLayPhieu = "SELECT maSP, soLuongNhap FROM phieunhapkho "
                               + "WHERE maPhieuNhap = ?";
            String maSP = null;
            int    soLuongNhap = 0;

            try (PreparedStatement ps = conn.prepareStatement(sqlLayPhieu)) {
                ps.setString(1, maPhieuNhap);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    maSP        = rs.getString("maSP");
                    soLuongNhap = rs.getInt("soLuongNhap");
                } else {
                    conn.rollback();
                    return false; // Phiếu không tồn tại
                }
            }

            // Bước 2: Xóa phiếu
            String sqlXoa = "DELETE FROM phieunhapkho WHERE maPhieuNhap = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlXoa)) {
                ps.setString(1, maPhieuNhap);
                ps.executeUpdate();
            }

            // Bước 3: Hoàn lại tồn kho
            String sqlHoanKho = "UPDATE sanpham "
                              + "SET soLuong = soLuong - ? "
                              + "WHERE maSP = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlHoanKho)) {
                ps.setInt   (1, soLuongNhap);
                ps.setString(2, maSP);
                ps.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }


    // 6. LẤY SP TỒN KHO THẤP (soLuong <= soLuongToiThieu)


    public List<Sanpham> laySPTonKhoThap() {
        List<Sanpham> danhSach = new ArrayList<>();
        String sql = "SELECT maSP, tenSP, soLuong, giaBan, loaiSP, "
                   + "       ngayHetHan, soLuongToiThieu "
                   + "FROM sanpham "
                   + "WHERE soLuong <= soLuongToiThieu "
                   + "ORDER BY soLuong ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement st   = conn.createStatement();
             ResultSet rs   = st.executeQuery(sql)) {

            while (rs.next()) danhSach.add(mapSanpham(rs));

        } catch (SQLException e) { e.printStackTrace(); }
        return danhSach;
    }


    // 7. LẤY SP SẮP HẾT HẠN (trong vòng 30 ngày tới)


    public List<Sanpham> laySPSapHetHan() {
        List<Sanpham> danhSach = new ArrayList<>();
        String sql = "SELECT maSP, tenSP, soLuong, giaBan, loaiSP, "
                   + "       ngayHetHan, soLuongToiThieu "
                   + "FROM sanpham "
                   + "WHERE ngayHetHan > CURDATE() "
                   + "  AND ngayHetHan <= DATE_ADD(CURDATE(), INTERVAL 30 DAY) "
                   + "ORDER BY ngayHetHan ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement st   = conn.createStatement();
             ResultSet rs   = st.executeQuery(sql)) {

            while (rs.next()) danhSach.add(mapSanpham(rs));

        } catch (SQLException e) { e.printStackTrace(); }
        return danhSach;
    }


    // 8. LẤY SP ĐÃ HẾT HẠN


    public List<Sanpham> laySPDaHetHan() {
        List<Sanpham> danhSach = new ArrayList<>();
        String sql = "SELECT maSP, tenSP, soLuong, giaBan, loaiSP, "
                   + "       ngayHetHan, soLuongToiThieu "
                   + "FROM sanpham "
                   + "WHERE ngayHetHan < CURDATE() "
                   + "ORDER BY ngayHetHan ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement st   = conn.createStatement();
             ResultSet rs   = st.executeQuery(sql)) {

            while (rs.next()) danhSach.add(mapSanpham(rs));

        } catch (SQLException e) { e.printStackTrace(); }
        return danhSach;
    }


    // 9. SINH MÃ PHIẾU TỰ ĐỘNG – dạng PN2025001


    public String taoMaPhieuMoi() {
        String nam    = String.valueOf(LocalDate.now().getYear());
        String prefix = "PN" + nam;
        String sql    = "SELECT COUNT(*) FROM phieunhapkho WHERE maPhieuNhap LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, prefix + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int soThuTu = rs.getInt(1) + 1;
                return String.format("%s%03d", prefix, soThuTu);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return prefix + "001";
    }


    // PRIVATE HELPER – mapSanpham
    // Tên getter khớp Sanpham.java thực tế của nhóm


    private Sanpham mapSanpham(ResultSet rs) throws SQLException {
        Sanpham sp = new Sanpham();
        sp.setMaSP           (rs.getString("maSP"));
        sp.setTenSP          (rs.getString("tenSP"));
        sp.setSoLuong        (rs.getInt   ("soLuong"));
        sp.setGiaBan         (rs.getDouble("giaBan"));
        sp.setLoaiSP         (rs.getString("loaiSP"));
        sp.setSoLuongToiThieu(rs.getInt   ("soLuongToiThieu"));
        Date ngayHetHanSQL = rs.getDate("ngayHetHan");
        if (ngayHetHanSQL != null) {
            sp.setNgayHetHan(ngayHetHanSQL.toLocalDate());
        }
        return sp;
    }
}