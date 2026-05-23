-- ================================================================
-- MINITI_DB – Schema phần Kho
-- Tên cột khớp 100% với PhieuNhapKho.java và Sanpham.java
-- Chạy file này SAU KHI đã tạo bảng: sanpham, nhanvien, nhacungcap
-- ================================================================

-- ------------------------------------------------------------
-- Bảng 1: Đầu phiếu nhập kho
-- Khớp với PhieuNhapKho.java
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS phieunhapkho (
    maPN        VARCHAR(20)     NOT NULL PRIMARY KEY,   -- getMaPN()
    maNV        VARCHAR(20)     NOT NULL,               -- getMaNV()
    maNCC       VARCHAR(20)     NOT NULL,               -- getMaNCC()
    ngayNhap    DATE            NOT NULL,               -- getNgayNhap()
    tongTien    DOUBLE          NOT NULL DEFAULT 0,     -- getTongTien()
    trangThai   VARCHAR(50)     NOT NULL DEFAULT 'Chờ duyệt', -- getTrangThai()
    ghiChu      TEXT,                                   -- getGhiChu()
    CONSTRAINT fk_pnk_nv  FOREIGN KEY (maNV)  REFERENCES nhanvien(maNV),
    CONSTRAINT fk_pnk_ncc FOREIGN KEY (maNCC) REFERENCES nhacungcap(maNCC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- Bảng 2: Chi tiết từng sản phẩm trong phiếu nhập
-- Khớp với ChiTietNhapKho (dùng trong TruycapKho)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS chitiet_nhapkho (
    id              INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    maPN            VARCHAR(20) NOT NULL,               -- FK → phieunhapkho
    maSP            VARCHAR(20) NOT NULL,               -- FK → sanpham
    soLuongNhap     INT         NOT NULL,               -- số lượng nhập
    giaNhap         DOUBLE      NOT NULL,               -- giá nhập (có thể khác giaBan)
    thanhTien       DOUBLE      NOT NULL,               -- soLuongNhap * giaNhap
    CONSTRAINT fk_ct_pnk FOREIGN KEY (maPN) REFERENCES phieunhapkho(maPN)
                          ON DELETE CASCADE,
    CONSTRAINT fk_ct_sp  FOREIGN KEY (maSP) REFERENCES sanpham(maSP)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================================
-- Dữ liệu mẫu để test (bỏ comment -- để chạy)
-- ================================================================
-- INSERT INTO phieunhapkho (maPN, maNV, maNCC, ngayNhap, tongTien, trangThai, ghiChu)
-- VALUES ('PN001', 'NV001', 'NCC001', '2025-05-01', 5000000, 'Hoàn thành', 'Lô hàng tháng 5');

-- INSERT INTO chitiet_nhapkho (maPN, maSP, soLuongNhap, giaNhap, thanhTien)
-- VALUES ('PN001', 'SP001', 100, 25000, 2500000),
--        ('PN001', 'SP002',  50, 50000, 2500000);
