package view;

import TruycapDL.TruycapKho;
import model.PhieuNhapKho;
import model.Sanpham;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * QuanLyKho – Màn hình quản lý kho hàng.
 *
 * Gồm 4 panel chuyển đổi qua thanh nút bên trên:
 *   [1] Danh sách phiếu nhập
 *   [2] Tạo phiếu nhập mới
 *   [3] Cảnh báo tồn kho thấp
 *   [4] Cảnh báo hàng sắp / đã hết hạn
 *
 * Tên biến dùng đúng theo PhieuNhapKho.java và Sanpham.java của nhóm.
 */
public class QuanLyKho extends JPanel {

    // ================================================================
    // FIELDS
    // ================================================================
    private final TruycapKho truycapKho = new TruycapKho();
    private final DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Màu chủ đạo
    private static final Color COLOR_PRIMARY   = new Color(41, 128, 185);
    private static final Color COLOR_DANGER    = new Color(192, 57, 43);
    private static final Color COLOR_WARNING   = new Color(211, 84, 0);
    private static final Color COLOR_SUCCESS   = new Color(39, 174, 96);
    private static final Color COLOR_BG        = new Color(245, 247, 250);
    private static final Color COLOR_BTN_HOVER = new Color(31, 97, 141);

    // Panel chứa nội dung chính (CardLayout)
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel     cardPanel  = new JPanel(cardLayout);

    // Các nút điều hướng
    private JButton btnDanhSachPhieu;
    private JButton btnTaoPhieu;
    private JButton btnTonKhoThap;
    private JButton btnHetHan;

    // ── Panel 1: Danh sách phiếu nhập ────────────────────────────────
    private DefaultTableModel modelPhieu;
    private JTable             tblPhieu;
    private JComboBox<String>  cboThang;
    private JComboBox<String>  cboNam;

    // ── Panel 2: Tạo phiếu nhập mới ─────────────────────────────────
    private JTextField   txtMaPN, txtMaNV, txtMaNCC, txtGhiChu;
    private JComboBox<String> cboTrangThai;
    // Bảng chi tiết sản phẩm trong phiếu đang tạo
    private DefaultTableModel modelChiTiet;
    private JTable             tblChiTiet;
    private JTextField   txtMaSP, txtSoLuongNhap, txtGiaNhap;
    private JLabel       lblTongTien;

    // ── Panel 3: Tồn kho thấp ────────────────────────────────────────
    private DefaultTableModel modelTonKho;
    private JTable             tblTonKho;

    // ── Panel 4: Hết hạn ────────────────────────────────────────────
    private DefaultTableModel modelHetHan;
    private JTable             tblHetHan;
    private JComboBox<String>  cboLocHetHan; // "Sắp hết hạn" / "Đã hết hạn"

    // ================================================================
    // CONSTRUCTOR
    // ================================================================
    public QuanLyKho() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);

        add(taoThanhDieuHuong(), BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        cardPanel.add(taoPanel1_DanhSachPhieu(), "DANH_SACH");
        cardPanel.add(taoPanel2_TaoPhieu(),      "TAO_PHIEU");
        cardPanel.add(taoPanel3_TonKhoThap(),    "TON_KHO");
        cardPanel.add(taoPanel4_HetHan(),        "HET_HAN");

        // Hiện panel 1 mặc định
        chuyenPanel("DANH_SACH", btnDanhSachPhieu);
        taiDuLieuPhieu();
    }

    // ================================================================
    // THANH ĐIỀU HƯỚNG (4 nút)
    // ================================================================
    private JPanel taoThanhDieuHuong() {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nav.setBackground(COLOR_PRIMARY);
        nav.setBorder(new EmptyBorder(8, 12, 8, 12));

        btnDanhSachPhieu = taoNutNav("📋  Danh sách phiếu nhập");
        btnTaoPhieu      = taoNutNav("➕  Tạo phiếu nhập mới");
        btnTonKhoThap    = taoNutNav("⚠️  Tồn kho thấp");
        btnHetHan        = taoNutNav("🗓️  Hàng hết hạn");

        btnDanhSachPhieu.addActionListener(e -> { chuyenPanel("DANH_SACH", btnDanhSachPhieu); taiDuLieuPhieu(); });
        btnTaoPhieu     .addActionListener(e -> { chuyenPanel("TAO_PHIEU",  btnTaoPhieu);      khoiTaoFormPhieu(); });
        btnTonKhoThap   .addActionListener(e -> { chuyenPanel("TON_KHO",   btnTonKhoThap);    taiDuLieuTonKho(); });
        btnHetHan       .addActionListener(e -> { chuyenPanel("HET_HAN",   btnHetHan);        taiDuLieuHetHan(); });

        nav.add(btnDanhSachPhieu);
        nav.add(Box.createHorizontalStrut(6));
        nav.add(btnTaoPhieu);
        nav.add(Box.createHorizontalStrut(6));
        nav.add(btnTonKhoThap);
        nav.add(Box.createHorizontalStrut(6));
        nav.add(btnHetHan);
        return nav;
    }

    private JButton taoNutNav(String ten) {
        JButton btn = new JButton(ten);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(COLOR_PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { if (!btn.isEnabled()) return; btn.setBackground(COLOR_BTN_HOVER); }
            public void mouseExited (MouseEvent e) { btn.setBackground(btn.isEnabled() ? COLOR_PRIMARY : COLOR_BTN_HOVER); }
        });
        return btn;
    }

    /** Chuyển CardLayout và highlight nút đang active */
    private void chuyenPanel(String key, JButton nutActive) {
        cardLayout.show(cardPanel, key);
        for (JButton b : new JButton[]{btnDanhSachPhieu, btnTaoPhieu, btnTonKhoThap, btnHetHan}) {
            b.setBackground(COLOR_PRIMARY);
        }
        nutActive.setBackground(COLOR_BTN_HOVER);
    }

    // ================================================================
    // PANEL 1 – DANH SÁCH PHIẾU NHẬP
    // ================================================================
    private JPanel taoPanel1_DanhSachPhieu() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        // ── Thanh lọc theo tháng/năm ──
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        filterBar.setBackground(COLOR_BG);

        cboThang = new JComboBox<>(new String[]{"Tất cả","01","02","03","04","05","06","07","08","09","10","11","12"});
        cboNam   = new JComboBox<>();
        int namHienTai = LocalDate.now().getYear();
        for (int y = namHienTai; y >= namHienTai - 4; y--) cboNam.addItem(String.valueOf(y));

        JButton btnLoc    = taoNutHanh("Lọc", COLOR_PRIMARY);
        JButton btnLamMoi = taoNutHanh("↺  Làm mới", COLOR_SUCCESS);

        btnLoc   .addActionListener(e -> taiDuLieuPhieu());
        btnLamMoi.addActionListener(e -> { cboThang.setSelectedIndex(0); taiDuLieuPhieu(); });

        filterBar.add(new JLabel("Tháng:"));  filterBar.add(cboThang);
        filterBar.add(new JLabel("Năm:"));    filterBar.add(cboNam);
        filterBar.add(btnLoc);
        filterBar.add(btnLamMoi);

        // ── Bảng phiếu ──
        String[] cols = {"Mã phiếu", "Mã NV", "Mã NCC", "Ngày nhập", "Tổng tiền (VNĐ)", "Trạng thái", "Ghi chú"};
        modelPhieu = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblPhieu = new JTable(modelPhieu);
        taoStyleBang(tblPhieu);
        // Tô màu dòng theo trạng thái
        tblPhieu.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                String tt = (String) t.getValueAt(row, 5);
                if (!sel) {
                    if ("Đã hủy".equalsIgnoreCase(tt))      setBackground(new Color(253, 237, 236));
                    else if ("Chờ duyệt".equalsIgnoreCase(tt)) setBackground(new Color(254, 249, 231));
                    else                                        setBackground(Color.WHITE);
                }
                return this;
            }
        });

        panel.add(filterBar,                   BorderLayout.NORTH);
        panel.add(new JScrollPane(tblPhieu),   BorderLayout.CENTER);
        return panel;
    }

    private void taiDuLieuPhieu() {
        modelPhieu.setRowCount(0);
        List<PhieuNhapKho> ds;
        String chonThang = (String) cboThang.getSelectedItem();

        if ("Tất cả".equals(chonThang)) {
            ds = truycapKho.layTatCaPhieu();
        } else {
            int thang = Integer.parseInt(chonThang);
            int nam   = Integer.parseInt((String) cboNam.getSelectedItem());
            ds = truycapKho.layPhieuTheoThang(thang, nam);
        }

        for (PhieuNhapKho p : ds) {
            modelPhieu.addRow(new Object[]{
                p.getMaPN(),
                p.getMaNV(),
                p.getMaNCC(),
                p.getNgayNhap() != null ? p.getNgayNhap().format(dtf) : "",
                String.format("%,.0f", p.getTongTien()),
                p.getTrangThai(),
                p.getGhiChu()
            });
        }
    }

    // ================================================================
    // PANEL 2 – TẠO PHIẾU NHẬP MỚI
    // ================================================================
    private JPanel taoPanel2_TaoPhieu() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        // ── Form thông tin đầu phiếu ──
        JPanel formPhieu = new JPanel(new GridBagLayout());
        formPhieu.setBackground(Color.WHITE);
        formPhieu.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARY, 1),
                "Thông tin phiếu nhập", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13), COLOR_PRIMARY));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(6, 10, 6, 10);
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        txtMaPN     = new JTextField(15); txtMaPN.setEditable(false);
        txtMaPN.setBackground(new Color(236, 240, 241));
        txtMaNV     = new JTextField(15);
        txtMaNCC    = new JTextField(15);
        txtGhiChu   = new JTextField(30);
        cboTrangThai = new JComboBox<>(new String[]{"Chờ duyệt", "Hoàn thành", "Đã hủy"});
        lblTongTien  = new JLabel("0 VNĐ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTongTien.setForeground(COLOR_DANGER);

        Object[][] formRows = {
            {"Mã phiếu (tự động):", txtMaPN},
            {"Mã nhân viên:",       txtMaNV},
            {"Mã nhà cung cấp:",    txtMaNCC},
            {"Trạng thái:",         cboTrangThai},
            {"Ghi chú:",            txtGhiChu},
            {"Tổng tiền:",          lblTongTien},
        };
        for (int i = 0; i < formRows.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            formPhieu.add(new JLabel((String) formRows[i][0]), gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            formPhieu.add((Component) formRows[i][1], gbc);
        }

        // ── Khu vực thêm dòng chi tiết SP ──
        JPanel formCT = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        formCT.setBackground(Color.WHITE);
        formCT.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
                "Thêm sản phẩm vào phiếu", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.PLAIN, 12)));

        txtMaSP        = new JTextField(8);
        txtSoLuongNhap = new JTextField(6);
        txtGiaNhap     = new JTextField(10);
        JButton btnThemDong   = taoNutHanh("+ Thêm dòng", COLOR_SUCCESS);
        JButton btnXoaDong    = taoNutHanh("✕ Xóa dòng",  COLOR_DANGER);

        for (Object[] lbl_field : new Object[][]{
                {"Mã SP:", txtMaSP}, {"Số lượng:", txtSoLuongNhap}, {"Giá nhập:", txtGiaNhap}}) {
            formCT.add(new JLabel((String) lbl_field[0]));
            formCT.add((Component) lbl_field[1]);
        }
        formCT.add(btnThemDong);
        formCT.add(btnXoaDong);

        btnThemDong.addActionListener(e -> themDongChiTiet());
        btnXoaDong .addActionListener(e -> xoaDongChiTiet());

        // ── Bảng chi tiết ──
        String[] colsCT = {"Mã SP", "Số lượng nhập", "Giá nhập (VNĐ)", "Thành tiền (VNĐ)"};
        modelChiTiet = new DefaultTableModel(colsCT, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblChiTiet = new JTable(modelChiTiet);
        taoStyleBang(tblChiTiet);
        JScrollPane scrollCT = new JScrollPane(tblChiTiet);
        scrollCT.setPreferredSize(new Dimension(0, 200));

        // ── Nút lưu phiếu ──
        JButton btnLuu = taoNutHanh("💾  Lưu phiếu nhập", COLOR_PRIMARY);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLuu.setPreferredSize(new Dimension(200, 40));
        btnLuu.addActionListener(e -> luuPhieuNhap());

        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomBar.setBackground(COLOR_BG);
        bottomBar.add(btnLuu);

        JPanel centerPanel = new JPanel(new BorderLayout(8, 8));
        centerPanel.setBackground(COLOR_BG);
        centerPanel.add(formCT,          BorderLayout.NORTH);
        centerPanel.add(scrollCT,        BorderLayout.CENTER);

        panel.add(formPhieu,   BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomBar,   BorderLayout.SOUTH);
        return panel;
    }

    /** Khởi tạo form tạo phiếu mới: sinh mã tự động, xóa bảng cũ */
    private void khoiTaoFormPhieu() {
        txtMaPN.setText(truycapKho.taoMaPhieuMoi());
        txtMaNV.setText("");
        txtMaNCC.setText("");
        txtGhiChu.setText("");
        cboTrangThai.setSelectedIndex(0);
        modelChiTiet.setRowCount(0);
        lblTongTien.setText("0 VNĐ");
        txtMaSP.setText("");
        txtSoLuongNhap.setText("");
        txtGiaNhap.setText("");
    }

    /** Thêm 1 dòng chi tiết vào bảng tạm, cập nhật tổng tiền */
    private void themDongChiTiet() {
        String maSP = txtMaSP.getText().trim();
        String slStr = txtSoLuongNhap.getText().trim();
        String gnStr = txtGiaNhap.getText().trim();

        if (maSP.isEmpty() || slStr.isEmpty() || gnStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập đầy đủ Mã SP, Số lượng và Giá nhập!",
                "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int    soLuongNhap = Integer.parseInt(slStr);
            double giaNhap     = Double.parseDouble(gnStr);
            if (soLuongNhap <= 0 || giaNhap < 0) throw new NumberFormatException();

            double thanhTien = soLuongNhap * giaNhap;
            modelChiTiet.addRow(new Object[]{
                maSP,
                soLuongNhap,
                String.format("%,.0f", giaNhap),
                String.format("%,.0f", thanhTien)
            });
            capNhatTongTien();
            txtMaSP.setText(""); txtSoLuongNhap.setText(""); txtGiaNhap.setText("");
            txtMaSP.requestFocus();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Số lượng và giá nhập phải là số dương hợp lệ!",
                "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Xóa dòng đang chọn trong bảng chi tiết */
    private void xoaDongChiTiet() {
        int row = tblChiTiet.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!",
                "Chưa chọn dòng", JOptionPane.WARNING_MESSAGE);
            return;
        }
        modelChiTiet.removeRow(row);
        capNhatTongTien();
    }

    /** Tính lại tổng tiền từ bảng chi tiết và cập nhật lblTongTien */
    private void capNhatTongTien() {
        double tong = 0;
        for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
            String tt = (String) modelChiTiet.getValueAt(i, 3);
            tong += Double.parseDouble(tt.replace(",", "").replace(".", ""));
        }
        lblTongTien.setText(String.format("%,.0f VNĐ", tong));
    }

    /** Validate và lưu phiếu nhập vào DB */
    private void luuPhieuNhap() {
        String maPN  = txtMaPN.getText().trim();
        String maNV  = txtMaNV.getText().trim();
        String maNCC = txtMaNCC.getText().trim();

        if (maNV.isEmpty() || maNCC.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập Mã nhân viên và Mã nhà cung cấp!",
                "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (modelChiTiet.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng thêm ít nhất 1 sản phẩm vào phiếu!",
                "Phiếu trống", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Gom dữ liệu từ bảng chi tiết
        int n = modelChiTiet.getRowCount();
        String[] dsMaSP    = new String[n];
        int[]    dsSoLuong = new int[n];
        double[] dsGiaNhap = new double[n];
        double   tongTien  = 0;

        for (int i = 0; i < n; i++) {
            dsMaSP[i]    = (String) modelChiTiet.getValueAt(i, 0);
            dsSoLuong[i] = (int) modelChiTiet.getValueAt(i, 1);
            String gnStr = (String) modelChiTiet.getValueAt(i, 2);
            dsGiaNhap[i] = Double.parseDouble(gnStr.replace(",", "").replace(".", ""));
            tongTien    += dsSoLuong[i] * dsGiaNhap[i];
        }

        // Tạo object đầu phiếu – dùng đúng setter của PhieuNhapKho.java
        PhieuNhapKho phieu = new PhieuNhapKho();
        phieu.setMaPN(maPN);
        phieu.setMaNV(maNV);
        phieu.setMaNCC(maNCC);
        phieu.setNgayNhap(LocalDate.now());
        phieu.setTongTien(tongTien);
        phieu.setTrangThai((String) cboTrangThai.getSelectedItem());
        phieu.setGhiChu(txtGhiChu.getText().trim());

        boolean ok = truycapKho.themPhieuNhapKho(phieu, dsMaSP, dsSoLuong, dsGiaNhap);

        if (ok) {
            JOptionPane.showMessageDialog(this,
                "Tạo phiếu nhập " + maPN + " thành công!\nTồn kho đã được cập nhật.",
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
            khoiTaoFormPhieu();
        } else {
            JOptionPane.showMessageDialog(this,
                "Lưu phiếu thất bại!\nKiểm tra lại Mã NV, Mã NCC, Mã SP có tồn tại trong hệ thống không.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ================================================================
    // PANEL 3 – CẢNH BÁO TỒN KHO THẤP
    // ================================================================
    private JPanel taoPanel3_TonKhoThap() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel lblTitle = new JLabel("⚠️  Danh sách sản phẩm có tồn kho thấp (số lượng ≤ tối thiểu)");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(COLOR_WARNING);

        JButton btnLamMoi = taoNutHanh("↺  Làm mới", COLOR_PRIMARY);
        btnLamMoi.addActionListener(e -> taiDuLieuTonKho());

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(COLOR_BG);
        top.add(lblTitle,  BorderLayout.WEST);
        top.add(btnLamMoi, BorderLayout.EAST);

        String[] cols = {"Mã SP", "Tên SP", "Loại SP", "Tồn kho", "Tối thiểu", "Cần nhập thêm"};
        modelTonKho = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblTonKho = new JTable(modelTonKho);
        taoStyleBang(tblTonKho);
        // Tô đỏ nếu hết hàng (soLuong == 0)
        tblTonKho.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                try {
                    int tonKho = Integer.parseInt(t.getValueAt(row, 3).toString());
                    if (!sel) setBackground(tonKho == 0 ? new Color(253, 237, 236) : new Color(254, 249, 231));
                } catch (Exception ex) { setBackground(Color.WHITE); }
                return this;
            }
        });

        panel.add(top,                      BorderLayout.NORTH);
        panel.add(new JScrollPane(tblTonKho), BorderLayout.CENTER);
        return panel;
    }

    private void taiDuLieuTonKho() {
        modelTonKho.setRowCount(0);
        List<Sanpham> ds = truycapKho.laySPTonKhoThap();
        for (Sanpham sp : ds) {
            int canNhapThem = Math.max(0, sp.getSoLuongToiThieu() - sp.getSoLuong());
            modelTonKho.addRow(new Object[]{
                sp.getMaSP(),
                sp.getTenSP(),
                sp.getLoaiSP(),
                sp.getSoLuong(),
                sp.getSoLuongToiThieu(),
                canNhapThem
            });
        }
        if (ds.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Không có sản phẩm nào dưới mức tồn kho tối thiểu. Kho hàng ổn định!",
                "Kho ổn định ✅", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ================================================================
    // PANEL 4 – CẢNH BÁO HÀNG SẮP / ĐÃ HẾT HẠN
    // ================================================================
    private JPanel taoPanel4_HetHan() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        cboLocHetHan = new JComboBox<>(new String[]{"Sắp hết hạn (trong 30 ngày)", "Đã hết hạn"});
        JButton btnLoc    = taoNutHanh("Lọc",        COLOR_PRIMARY);
        JButton btnLamMoi = taoNutHanh("↺  Làm mới", COLOR_SUCCESS);

        btnLoc   .addActionListener(e -> taiDuLieuHetHan());
        btnLamMoi.addActionListener(e -> { cboLocHetHan.setSelectedIndex(0); taiDuLieuHetHan(); });

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        top.setBackground(COLOR_BG);
        top.add(new JLabel("Hiển thị:")); top.add(cboLocHetHan);
        top.add(btnLoc); top.add(btnLamMoi);

        String[] cols = {"Mã SP", "Tên SP", "Loại SP", "Tồn kho", "Ngày hết hạn", "Tình trạng"};
        modelHetHan = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblHetHan = new JTable(modelHetHan);
        taoStyleBang(tblHetHan);
        // Tô màu: đỏ = đã hết hạn, cam = sắp hết hạn
        tblHetHan.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) {
                    String tt = t.getValueAt(row, 5).toString();
                    if (tt.contains("Đã hết"))       setBackground(new Color(253, 237, 236));
                    else if (tt.contains("Sắp hết")) setBackground(new Color(254, 249, 231));
                    else                              setBackground(Color.WHITE);
                }
                return this;
            }
        });

        panel.add(top,                       BorderLayout.NORTH);
        panel.add(new JScrollPane(tblHetHan), BorderLayout.CENTER);
        return panel;
    }

    private void taiDuLieuHetHan() {
        modelHetHan.setRowCount(0);
        boolean chonSap = cboLocHetHan.getSelectedIndex() == 0;
        List<Sanpham> ds = chonSap
                ? truycapKho.laySPSapHetHan()
                : truycapKho.laySPDaHetHan();

        for (Sanpham sp : ds) {
            String tinhTrang = chonSap ? "⚠️ Sắp hết hạn" : "❌ Đã hết hạn";
            modelHetHan.addRow(new Object[]{
                sp.getMaSP(),
                sp.getTenSP(),
                sp.getLoaiSP(),
                sp.getSoLuong(),
                sp.getNgayHetHan() != null ? sp.getNgayHetHan().format(dtf) : "N/A",
                tinhTrang
            });
        }
        if (ds.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                chonSap ? "Không có sản phẩm nào sắp hết hạn trong 30 ngày tới."
                        : "Không có sản phẩm nào đã hết hạn.",
                "Không có dữ liệu", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ================================================================
    // HELPERS UI
    // ================================================================
    private JButton taoNutHanh(String ten, Color mauNen) {
        JButton btn = new JButton(ten);
        btn.setBackground(mauNen);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void taoStyleBang(JTable table) {
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(COLOR_PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    // ================================================================
    // MAIN – Test độc lập (xóa khi tích hợp vào Mainframe)
    // ================================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý Kho – Miniti");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 700);
            frame.setLocationRelativeTo(null);
            frame.add(new QuanLyKho());
            frame.setVisible(true);
        });
    }
}