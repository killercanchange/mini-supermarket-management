package TruycapDL;

import config.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Kho;
import model.PhieuNhapKho;
import model.Sanpham;

public class TruycapKho {

    // ============================================================
    // MAP RESULT SET
    // ============================================================
    private Kho mapKho(ResultSet rs) throws SQLException {
        Kho kho = new Kho();
        kho.setMaKho(rs.getString("maKho"));
        kho.setTenKho(rs.getString("tenKho"));
        kho.setDiaChi(rs.getString("diachi"));
        kho.setSucChua(rs.getInt("sucChua"));
        kho.setGhiChu(rs.getString("ghiChu"));
        return kho;
    }

    private PhieuNhapKho mapPhieu(ResultSet rs) throws SQLException {
        PhieuNhapKho p = new PhieuNhapKho();
        p.setMaPhieuNhap(rs.getString("maPhieuNhap"));
        p.setMaNCC(rs.getString("maNCC"));
        p.setTenNCC(rs.getString("tenNCC"));
        p.setMaSP(rs.getString("maSP"));
        p.setTenSP(rs.getString("tenSP"));
        p.setSoLuongNhap(rs.getInt("soLuongNhap"));
        p.setGiaNhap(rs.getDouble("giaNhap"));
        p.setNgayNhap(rs.getDate("ngayNhap") != null
                ? rs.getDate("ngayNhap").toLocalDate() : null);
        return p;
    }

    private Sanpham mapSanpham(ResultSet rs) throws SQLException {
        Sanpham sp = new Sanpham();
        sp.setMaSP(rs.getString("maSP"));
        sp.setTenSP(rs.getString("tenSP"));
        sp.setSoLuong(rs.getInt("soLuong"));
        sp.setGiaBan(rs.getDouble("giaBan"));
        sp.setLoaiSP(rs.getString("loaiSP"));
        sp.setNgayHetHan(rs.getDate("ngayHetHan") != null
                ? rs.getDate("ngayHetHan").toLocalDate() : null);
        sp.setSoLuongToiThieu(rs.getInt("soLuongToiThieu"));
        return sp;
    }

    // ============================================================
    // QUẢN LÝ KHO
    // ============================================================
    public List<Kho> getAllKho() {
        List<Kho> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM kho";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) danhSach.add(mapKho(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public Kho getKhoByMa(String maKho) {
        String sql = "SELECT * FROM kho WHERE maKho = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKho);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapKho(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Kho> timTheoTenKho(String tenKho) {
        List<Kho> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM kho WHERE tenKho LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + tenKho + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) danhSach.add(mapKho(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    public boolean themKho(Kho kho) {
        String sql = "INSERT INTO kho (maKho, tenKho, diachi, sucChua, ghiChu) VALUES (?, ?, ?, ?, ?)";
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

    // ============================================================
    // PHIẾU NHẬP KHO
    // ============================================================

    /**
     * Thêm phiếu nhập + cộng tồn kho trong 1 transaction.
     * Nếu 1 trong 2 bước lỗi thì rollback toàn bộ.
     */
    public boolean themPhieuNhap(PhieuNhapKho phieu) {
        String sqlInsert = "INSERT INTO phieunhapkho "
                + "(maPhieuNhap, maNCC, tenNCC, maSP, tenSP, soLuongNhap, giaNhap, ngayNhap) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlUpdate = "UPDATE sanpham SET soLuong = soLuong + ? WHERE maSP = ?";
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // Bước 1: insert phiếu
            try (PreparedStatement ps1 = con.prepareStatement(sqlInsert)) {
                ps1.setString(1, phieu.getMaPhieuNhap());
                ps1.setString(2, phieu.getMaNCC());
                ps1.setString(3, phieu.getTenNCC());
                ps1.setString(4, phieu.getMaSP());
                ps1.setString(5, phieu.getTenSP());
                ps1.setInt(6, phieu.getSoLuongNhap());
                ps1.setDouble(7, phieu.getGiaNhap());
                ps1.setDate(8, phieu.getNgayNhap() != null
                        ? Date.valueOf(phieu.getNgayNhap()) : null);
                ps1.executeUpdate();
            }

            // Bước 2: cộng tồn kho
            try (PreparedStatement ps2 = con.prepareStatement(sqlUpdate)) {
                ps2.setInt(1, phieu.getSoLuongNhap());
                ps2.setString(2, phieu.getMaSP());
                ps2.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
        return false;
    }

    /** Lấy tất cả phiếu nhập, mới nhất trước */
    public List<PhieuNhapKho> getAllPhieuNhap() {
        List<PhieuNhapKho> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM phieunhapkho ORDER BY ngayNhap DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) danhSach.add(mapPhieu(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    /** Lọc phiếu theo tháng / năm */
    public List<PhieuNhapKho> layPhieuTheoThang(int thang, int nam) {
        List<PhieuNhapKho> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM phieunhapkho "
                + "WHERE MONTH(ngayNhap) = ? AND YEAR(ngayNhap) = ? "
                + "ORDER BY ngayNhap DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) danhSach.add(mapPhieu(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    /** Lọc phiếu theo nhà cung cấp */
    public List<PhieuNhapKho> layPhieuTheoMaNCC(String maNCC) {
        List<PhieuNhapKho> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM phieunhapkho WHERE maNCC = ? ORDER BY ngayNhap DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNCC);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) danhSach.add(mapPhieu(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    /**
     * Xóa phiếu nhập + hoàn lại tồn kho trong 1 transaction.
     */
    public boolean xoaPhieuNhap(String maPhieuNhap) {
        String sqlSelect = "SELECT maSP, soLuongNhap FROM phieunhapkho WHERE maPhieuNhap = ?";
        String sqlDelete = "DELETE FROM phieunhapkho WHERE maPhieuNhap = ?";
        String sqlUpdate = "UPDATE sanpham SET soLuong = soLuong - ? WHERE maSP = ?";
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // Lấy thông tin phiếu trước khi xóa
            String maSP = null;
            int soLuong = 0;
            try (PreparedStatement ps = con.prepareStatement(sqlSelect)) {
                ps.setString(1, maPhieuNhap);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    maSP = rs.getString("maSP");
                    soLuong = rs.getInt("soLuongNhap");
                }
            }
            if (maSP == null) {
                con.rollback();
                return false;
            }

            // Bước 1: xóa phiếu
            try (PreparedStatement ps = con.prepareStatement(sqlDelete)) {
                ps.setString(1, maPhieuNhap);
                ps.executeUpdate();
            }

            // Bước 2: hoàn lại tồn kho (trừ số lượng đã nhập)
            try (PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
                ps.setInt(1, soLuong);
                ps.setString(2, maSP);
                ps.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
        return false;
    }

    /** Sinh mã phiếu nhập mới: PN2025001, PN2025002, ... */
    public String taoMaPhieuMoi() {
        int nam = LocalDate.now().getYear();
        String sql = "SELECT maPhieuNhap FROM phieunhapkho WHERE maPhieuNhap LIKE ? ORDER BY maPhieuNhap DESC LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "PN" + nam + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String maHienTai = rs.getString("maPhieuNhap"); // VD: PN2025003
                int soThuTu = Integer.parseInt(maHienTai.substring(6)) + 1;
                return String.format("PN%d%03d", nam, soThuTu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return String.format("PN%d001", nam);
    }

    // ============================================================
    // CẢNH BÁO TỒN KHO — trả về List<Sanpham>
    // ============================================================

    /** Sản phẩm có soLuong <= soLuongToiThieu */
    public List<Sanpham> laySPTonKhoThap() {
        List<Sanpham> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM sanpham WHERE soLuong <= soLuongToiThieu";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) danhSach.add(mapSanpham(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    /** Sản phẩm sắp hết hạn trong 30 ngày tới (chưa hết hạn) */
    public List<Sanpham> laySPSapHetHan() {
        List<Sanpham> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM sanpham "
                + "WHERE ngayHetHan > CURDATE() AND ngayHetHan <= DATE_ADD(CURDATE(), INTERVAL 30 DAY)";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) danhSach.add(mapSanpham(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    /** Sản phẩm đã hết hạn (ngayHetHan < ngày hôm nay) */
    public List<Sanpham> laySPDaHetHan() {
        List<Sanpham> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM sanpham WHERE ngayHetHan < CURDATE()";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) danhSach.add(mapSanpham(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }
}