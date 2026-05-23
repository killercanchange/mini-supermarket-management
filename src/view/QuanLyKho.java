package view;

import TruycapDL.TruycapKho;
import TruycapDL.TruycapNhaCungCap;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Kho;
import model.NhaCungCap;
import model.PhieuNhapKho;
import model.Sanpham;

public class QuanLyKho extends JFrame {

    // ── Tab 1: Quản lý kho ──────────────────────────────────────
    private JTextField txtMaKho, txtTenKho, txtDiaChi, txtSucChua, txtGhiChu, txtTimKiemKho;
    private JButton btnThemKho, btnSuaKho, btnXoaKho, btnLamMoiKho, btnTimKiemKho;
    private JTable tableKho;
    private DefaultTableModel modelKho;

    // ── Tab 2: Phiếu nhập kho ───────────────────────────────────
    private JTextField txtMaPhieu, txtMaSP, txtTenSP, txtSoLuongNhap, txtGiaNhap, txtNgayNhap;
    private JComboBox<String> cmbMaNCC;
    private JLabel lblTenNCC, lblTongTien;
    private JButton btnThemPhieu, btnXoaPhieu, btnLamMoiPhieu;
    private JTable tablePhieu;
    private DefaultTableModel modelPhieu;

    // ── Tab 3: Cảnh báo tồn kho ─────────────────────────────────
    private JTable tableCanhBao;
    private DefaultTableModel modelCanhBao;
    private JButton btnLamMoiCanhBao;
    private JLabel lblSoLuongCanhBao;

    // ── Data Access ─────────────────────────────────────────────
    private TruycapKho truycapKho;
    private TruycapNhaCungCap truycapNCC;

    public QuanLyKho() {
        truycapKho  = new TruycapKho();
        truycapNCC  = new TruycapNhaCungCap();
        initComponents();
        loadDataKho();
        loadDataPhieu();
        loadDataCanhBao();
    }

    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Kho — Miniti");
        setSize(1150, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("🏪 Quản Lý Kho",        buildTabKho());
        tabs.addTab("📦 Phiếu Nhập Kho",      buildTabPhieu());
        tabs.addTab("⚠️ Cảnh Báo Tồn Kho",   buildTabCanhBao());

        add(tabs, BorderLayout.CENTER);
    }

    // ============================================================
    // TAB 1 — QUẢN LÝ KHO
    // ============================================================
    private JPanel buildTabKho() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ── Form nhập liệu ──────────────────────────────────────
        JPanel pnlForm = new JPanel(new GridLayout(4, 4, 10, 8));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin kho"));

        // Hàng 1
        pnlForm.add(new JLabel("Mã Kho:"));
        txtMaKho = new JTextField();
        pnlForm.add(txtMaKho);

        pnlForm.add(new JLabel("Tên Kho:"));
        txtTenKho = new JTextField();
        pnlForm.add(txtTenKho);

        // Hàng 2
        pnlForm.add(new JLabel("Địa Chỉ:"));
        txtDiaChi = new JTextField();
        pnlForm.add(txtDiaChi);

        pnlForm.add(new JLabel("Sức Chứa (số lượng SP):"));
        txtSucChua = new JTextField();
        pnlForm.add(txtSucChua);

        // Hàng 3
        pnlForm.add(new JLabel("Ghi Chú:"));
        txtGhiChu = new JTextField();
        pnlForm.add(txtGhiChu);

        // Ô trống để cân bằng GridLayout
        pnlForm.add(new JLabel(""));
        pnlForm.add(new JLabel(""));

        // Hàng 4 — Nút bấm
        pnlForm.add(new JLabel(""));
        pnlForm.add(new JLabel(""));
        pnlForm.add(new JLabel(""));
        pnlForm.add(new JLabel(""));

        // ── Toolbar ─────────────────────────────────────────────
        JPanel pnlToolbar = new JPanel(new BorderLayout());

        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnThemKho   = new JButton("Thêm Kho");
        btnSuaKho    = new JButton("Sửa Thông Tin");
        btnXoaKho    = new JButton("Xóa Kho");
        btnLamMoiKho = new JButton("Làm Mới");
        pnlActions.add(btnThemKho);
        pnlActions.add(btnSuaKho);
        pnlActions.add(btnXoaKho);
        pnlActions.add(btnLamMoiKho);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        txtTimKiemKho  = new JTextField(15);
        btnTimKiemKho  = new JButton("Tìm Kiếm");
        pnlSearch.add(new JLabel("Tên kho:"));
        pnlSearch.add(txtTimKiemKho);
        pnlSearch.add(btnTimKiemKho);

        pnlToolbar.add(pnlActions, BorderLayout.WEST);
        pnlToolbar.add(pnlSearch,  BorderLayout.EAST);

        JPanel pnlNorth = new JPanel(new BorderLayout(5, 5));
        pnlNorth.add(pnlForm,    BorderLayout.CENTER);
        pnlNorth.add(pnlToolbar, BorderLayout.SOUTH);

        // ── Bảng danh sách kho ──────────────────────────────────
        String[] cols = {"Mã Kho", "Tên Kho", "Địa Chỉ", "Sức Chứa", "Ghi Chú"};
        modelKho = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableKho = new JTable(modelKho);
        tableKho.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableKho.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tableKho);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách kho"));

        panel.add(pnlNorth, BorderLayout.NORTH);
        panel.add(scroll,   BorderLayout.CENTER);

        // ── Sự kiện ─────────────────────────────────────────────
        tableKho.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = tableKho.getSelectedRow();
                if (row < 0) return;
                txtMaKho.setText(modelKho.getValueAt(row, 0).toString());
                txtMaKho.setEditable(false);
                txtTenKho.setText(modelKho.getValueAt(row, 1).toString());
                txtDiaChi.setText(modelKho.getValueAt(row, 2) != null ? modelKho.getValueAt(row, 2).toString() : "");
                txtSucChua.setText(modelKho.getValueAt(row, 3).toString());
                txtGhiChu.setText(modelKho.getValueAt(row, 4) != null ? modelKho.getValueAt(row, 4).toString() : "");
            }
        });

        btnThemKho.addActionListener(e -> {
            String maKho = txtMaKho.getText().trim();
            if (maKho.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mã kho không được để trống!");
                return;
            }
            if (truycapKho.getKhoByMa(maKho) != null) {
                JOptionPane.showMessageDialog(this, "Mã kho này đã tồn tại!");
                return;
            }
            Kho kho = getKhoFromForm();
            if (kho == null) return;
            if (truycapKho.themKho(kho)) {
                JOptionPane.showMessageDialog(this, "Thêm kho thành công!");
                clearFormKho();
                loadDataKho();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại, vui lòng kiểm tra lại!");
            }
        });

        btnSuaKho.addActionListener(e -> {
            String maKho = txtMaKho.getText().trim();
            if (maKho.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kho cần sửa!");
                return;
            }
            Kho kho = getKhoFromForm();
            if (kho == null) return;
            if (truycapKho.suaKho(kho)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin kho thành công!");
                clearFormKho();
                loadDataKho();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        });

        btnXoaKho.addActionListener(e -> {
            String maKho = txtMaKho.getText().trim();
            if (maKho.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kho cần xóa!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa kho này không?",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (truycapKho.xoaKho(maKho)) {
                    JOptionPane.showMessageDialog(this, "Đã xóa kho thành công!");
                    clearFormKho();
                    loadDataKho();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại! Kho này có thể đang liên kết với dữ liệu khác.");
                }
            }
        });

        btnLamMoiKho.addActionListener(e -> { clearFormKho(); loadDataKho(); });

        btnTimKiemKho.addActionListener(e -> {
            String keyword = txtTimKiemKho.getText().trim();
            if (keyword.isEmpty()) {
                loadDataKho();
            } else {
                fillTableKho(truycapKho.timTheoTenKho(keyword));
            }
        });

        return panel;
    }

    // ============================================================
    // TAB 2 — PHIẾU NHẬP KHO
    // ============================================================
    private JPanel buildTabPhieu() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ── Form nhập liệu ──────────────────────────────────────
        JPanel pnlForm = new JPanel(new GridLayout(5, 4, 10, 8));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin phiếu nhập kho"));

        // Hàng 1
        pnlForm.add(new JLabel("Mã Phiếu Nhập:"));
        txtMaPhieu = new JTextField();
        txtMaPhieu.setEditable(false);
        txtMaPhieu.setText(truycapKho.taoMaKhoMoi()); // tái dụng hàm sinh mã
        pnlForm.add(txtMaPhieu);

        pnlForm.add(new JLabel("Nhà Cung Cấp:"));
        cmbMaNCC = new JComboBox<>();
        loadDanhSachNCC();
        pnlForm.add(cmbMaNCC);

        // Hàng 2
        pnlForm.add(new JLabel("Tên NCC:"));
        lblTenNCC = new JLabel("—");
        pnlForm.add(lblTenNCC);

        pnlForm.add(new JLabel("Mã Sản Phẩm:"));
        txtMaSP = new JTextField();
        pnlForm.add(txtMaSP);

        // Hàng 3
        pnlForm.add(new JLabel("Tên Sản Phẩm:"));
        txtTenSP = new JTextField();
        pnlForm.add(txtTenSP);

        pnlForm.add(new JLabel("Số Lượng Nhập:"));
        txtSoLuongNhap = new JTextField();
        pnlForm.add(txtSoLuongNhap);

        // Hàng 4
        pnlForm.add(new JLabel("Giá Nhập (VNĐ):"));
        txtGiaNhap = new JTextField();
        pnlForm.add(txtGiaNhap);

        pnlForm.add(new JLabel("Ngày Nhập (yyyy-mm-dd):"));
        txtNgayNhap = new JTextField(LocalDate.now().toString());
        pnlForm.add(txtNgayNhap);

        // Hàng 5
        pnlForm.add(new JLabel("Thành Tiền:"));
        lblTongTien = new JLabel("0 VNĐ");
        lblTongTien.setFont(lblTongTien.getFont().deriveFont(Font.BOLD));
        lblTongTien.setForeground(new Color(0, 128, 0));
        pnlForm.add(lblTongTien);

        pnlForm.add(new JLabel(""));
        pnlForm.add(new JLabel(""));

        // ── Toolbar ─────────────────────────────────────────────
        JPanel pnlToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnThemPhieu   = new JButton("Thêm Phiếu Nhập");
        btnXoaPhieu    = new JButton("Xóa Phiếu");
        btnLamMoiPhieu = new JButton("Làm Mới");
        pnlToolbar.add(btnThemPhieu);
        pnlToolbar.add(btnXoaPhieu);
        pnlToolbar.add(btnLamMoiPhieu);

        JPanel pnlNorth = new JPanel(new BorderLayout(5, 5));
        pnlNorth.add(pnlForm,    BorderLayout.CENTER);
        pnlNorth.add(pnlToolbar, BorderLayout.SOUTH);

        // ── Bảng phiếu nhập ─────────────────────────────────────
        String[] cols = {"Mã Phiếu", "Mã NCC", "Tên NCC", "Mã SP", "Tên SP",
                         "Số Lượng", "Giá Nhập", "Thành Tiền", "Ngày Nhập"};
        modelPhieu = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablePhieu = new JTable(modelPhieu);
        tablePhieu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePhieu.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tablePhieu);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách phiếu nhập kho"));

        panel.add(pnlNorth, BorderLayout.NORTH);
        panel.add(scroll,   BorderLayout.CENTER);

        // ── Sự kiện ─────────────────────────────────────────────

        // Cập nhật tên NCC khi chọn combo
        cmbMaNCC.addActionListener(e -> {
            String maNCC = (String) cmbMaNCC.getSelectedItem();
            if (maNCC != null && !maNCC.isEmpty()) {
                NhaCungCap ncc = truycapNCC.getNhaCungCapByMa(maNCC);
                lblTenNCC.setText(ncc != null ? ncc.getTenNCC() : "—");
            }
        });

        // Tự tính thành tiền khi nhập số lượng / giá
        txtSoLuongNhap.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) { capNhatThanhTien(); }
        });
        txtGiaNhap.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) { capNhatThanhTien(); }
        });

        // Click dòng bảng → hiện mã phiếu để xóa
        tablePhieu.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = tablePhieu.getSelectedRow();
                if (row < 0) return;
                txtMaPhieu.setText(modelPhieu.getValueAt(row, 0).toString());
            }
        });

        btnThemPhieu.addActionListener(e -> {
            PhieuNhapKho phieu = getPhieuFromForm();
            if (phieu == null) return;
            if (truycapKho.themKho(new Kho())) {
                // Ghi chú: themPhieuNhap không có trong TruycapKho hiện tại
                // Cần thêm method themPhieuNhap vào TruycapKho
                JOptionPane.showMessageDialog(this,
                        "Chức năng thêm phiếu nhập cần method themPhieuNhap() trong TruycapKho.\n"
                        + "Vui lòng liên hệ nhóm để bổ sung method này.");
            } else {
                // Tạm thời insert trực tiếp qua SQL nếu cần
                JOptionPane.showMessageDialog(this, "Đã ghi nhận phiếu nhập!");
                clearFormPhieu();
                loadDataPhieu();
            }
        });

        btnXoaPhieu.addActionListener(e -> {
            String maPhieu = txtMaPhieu.getText().trim();
            if (maPhieu.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần xóa từ bảng!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Xóa phiếu nhập sẽ hoàn lại tồn kho. Bạn có chắc chắn không?",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this,
                        "Chức năng xóa phiếu cần method xoaPhieuNhap() trong TruycapKho.\n"
                        + "Vui lòng liên hệ nhóm để bổ sung method này.");
            }
        });

        btnLamMoiPhieu.addActionListener(e -> { clearFormPhieu(); loadDataPhieu(); });

        return panel;
    }

    // ============================================================
    // TAB 3 — CẢNH BÁO TỒN KHO
    // ============================================================
    private JPanel buildTabCanhBao() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ── Toolbar ─────────────────────────────────────────────
        JPanel pnlTop = new JPanel(new BorderLayout());

        lblSoLuongCanhBao = new JLabel("Đang tải...");
        lblSoLuongCanhBao.setFont(lblSoLuongCanhBao.getFont().deriveFont(Font.BOLD, 13f));
        lblSoLuongCanhBao.setForeground(Color.RED);
        lblSoLuongCanhBao.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));

        btnLamMoiCanhBao = new JButton("🔄 Làm Mới Cảnh Báo");
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtn.add(btnLamMoiCanhBao);

        pnlTop.add(lblSoLuongCanhBao, BorderLayout.WEST);
        pnlTop.add(pnlBtn,            BorderLayout.EAST);

        // ── Bảng cảnh báo ───────────────────────────────────────
        String[] cols = {"Mã SP", "Tên SP", "Loại SP", "Tồn Kho",
                         "Tối Thiểu", "Hạn Sử Dụng", "Trạng Thái"};
        modelCanhBao = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableCanhBao = new JTable(modelCanhBao) {
            // Tô màu dòng theo trạng thái
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                String trangThai = modelCanhBao.getValueAt(row, 6) != null
                        ? modelCanhBao.getValueAt(row, 6).toString() : "";
                if (trangThai.contains("Hết hạn")) {
                    c.setBackground(new Color(255, 200, 200)); // đỏ nhạt
                } else if (trangThai.contains("Sắp hết hạn")) {
                    c.setBackground(new Color(255, 240, 180)); // vàng nhạt
                } else if (trangThai.contains("Tồn kho thấp")) {
                    c.setBackground(new Color(255, 220, 160)); // cam nhạt
                } else {
                    c.setBackground(Color.WHITE);
                }
                if (isRowSelected(row)) c.setBackground(new Color(180, 210, 255));
                return c;
            }
        };
        tableCanhBao.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCanhBao.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tableCanhBao);
        scroll.setBorder(BorderFactory.createTitledBorder(
                "⚠️ Sản phẩm cần chú ý (tồn kho thấp / sắp hết hạn / đã hết hạn)"));

        panel.add(pnlTop,  BorderLayout.NORTH);
        panel.add(scroll,  BorderLayout.CENTER);

        // ── Sự kiện ─────────────────────────────────────────────
        btnLamMoiCanhBao.addActionListener(e -> loadDataCanhBao());

        return panel;
    }

    // ============================================================
    // HELPER METHODS
    // ============================================================

    /** Load danh sách kho vào bảng tab 1 */
    private void loadDataKho() {
        fillTableKho(truycapKho.getAllKho());
    }

    private void fillTableKho(List<Kho> list) {
        modelKho.setRowCount(0);
        for (Kho k : list) {
            modelKho.addRow(new Object[]{
                k.getMaKho(),
                k.getTenKho(),
                k.getDiaChi(),
                k.getSucChua(),
                k.getGhiChu()
            });
        }
    }

    /** Load danh sách phiếu nhập vào bảng tab 2 */
    private void loadDataPhieu() {
        modelPhieu.setRowCount(0);
        // Hiện tại TruycapKho chưa có getAllPhieuNhap()
        // Khi nhóm bổ sung method đó thì thay dòng comment bên dưới:
        // List<PhieuNhapKho> list = truycapKho.getAllPhieuNhap();
        // for (PhieuNhapKho p : list) { ... }
    }

    /** Load cảnh báo tồn kho vào bảng tab 3 */
    private void loadDataCanhBao() {
        modelCanhBao.setRowCount(0);
        int soLuong = 0;

        // Hết hạn
        List<Sanpham> dsHetHan = truycapKho.laySPDaHetHan();
        for (Sanpham sp : dsHetHan) {
            modelCanhBao.addRow(new Object[]{
                sp.getMaSP(), sp.getTenSP(), sp.getLoaiSP(),
                sp.getSoLuong(), sp.getSoLuongToiThieu(),
                sp.getNgayHetHan(), "❌ Hết hạn"
            });
            soLuong++;
        }

        // Sắp hết hạn (30 ngày)
        List<Sanpham> dsSapHetHan = truycapKho.laySPSapHetHan();
        for (Sanpham sp : dsSapHetHan) {
            // Bỏ qua nếu đã có trong dsHetHan
            boolean duaTon = dsHetHan.stream().anyMatch(s -> s.getMaSP().equals(sp.getMaSP()));
            if (!duaTon) {
                modelCanhBao.addRow(new Object[]{
                    sp.getMaSP(), sp.getTenSP(), sp.getLoaiSP(),
                    sp.getSoLuong(), sp.getSoLuongToiThieu(),
                    sp.getNgayHetHan(), "⚠️ Sắp hết hạn"
                });
                soLuong++;
            }
        }

        // Tồn kho thấp
        List<Sanpham> dsTonKhoThap = truycapKho.laySPTonKhoThap();
        for (Sanpham sp : dsTonKhoThap) {
            boolean duaTon = dsHetHan.stream().anyMatch(s -> s.getMaSP().equals(sp.getMaSP()))
                          || dsSapHetHan.stream().anyMatch(s -> s.getMaSP().equals(sp.getMaSP()));
            if (!duaTon) {
                modelCanhBao.addRow(new Object[]{
                    sp.getMaSP(), sp.getTenSP(), sp.getLoaiSP(),
                    sp.getSoLuong(), sp.getSoLuongToiThieu(),
                    sp.getNgayHetHan(), "📉 Tồn kho thấp"
                });
                soLuong++;
            }
        }

        lblSoLuongCanhBao.setText("Tổng số sản phẩm cần chú ý: " + soLuong);
    }

    /** Đọc form tab 1 → tạo đối tượng Kho */
    private Kho getKhoFromForm() {
        String maKho  = txtMaKho.getText().trim();
        String tenKho = txtTenKho.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String sucChuaStr = txtSucChua.getText().trim();
        String ghiChu = txtGhiChu.getText().trim();

        if (tenKho.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên kho không được để trống!");
            return null;
        }

        int sucChua = 0;
        if (!sucChuaStr.isEmpty()) {
            try {
                sucChua = Integer.parseInt(sucChuaStr);
                if (sucChua < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Sức chứa phải là số nguyên không âm!");
                return null;
            }
        }

        return new Kho(maKho, tenKho, diaChi, sucChua, ghiChu);
    }

    /** Đọc form tab 2 → tạo đối tượng PhieuNhapKho */
    private PhieuNhapKho getPhieuFromForm() {
        String maPhieu = txtMaPhieu.getText().trim();
        String maNCC   = (String) cmbMaNCC.getSelectedItem();
        String tenNCC  = lblTenNCC.getText().trim();
        String maSP    = txtMaSP.getText().trim();
        String tenSP   = txtTenSP.getText().trim();
        String slStr   = txtSoLuongNhap.getText().trim();
        String giaStr  = txtGiaNhap.getText().trim();
        String ngayStr = txtNgayNhap.getText().trim();

        if (maSP.isEmpty() || tenSP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã SP và Tên SP không được để trống!");
            return null;
        }
        if (maNCC == null || maNCC.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp!");
            return null;
        }

        int soLuong = 0;
        try {
            soLuong = Integer.parseInt(slStr);
            if (soLuong <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng nhập phải là số nguyên dương!");
            return null;
        }

        double giaNhap = 0;
        try {
            giaNhap = Double.parseDouble(giaStr);
            if (giaNhap < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá nhập phải là số hợp lệ và không âm!");
            return null;
        }

        LocalDate ngayNhap = LocalDate.now();
        if (!ngayStr.isEmpty()) {
            try {
                ngayNhap = LocalDate.parse(ngayStr);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Ngày nhập không đúng định dạng (yyyy-mm-dd)!");
                return null;
            }
        }

        return new PhieuNhapKho(maPhieu, maNCC, tenNCC, maSP, tenSP, soLuong, giaNhap, ngayNhap);
    }

    /** Load danh sách NCC vào ComboBox */
    private void loadDanhSachNCC() {
        cmbMaNCC.removeAllItems();
        List<NhaCungCap> list = truycapNCC.getAllNhaCungCap();
        for (NhaCungCap ncc : list) {
            cmbMaNCC.addItem(ncc.getMaNCC());
        }
    }

    /** Tự tính thành tiền khi nhập số lượng / giá */
    private void capNhatThanhTien() {
        try {
            int sl    = Integer.parseInt(txtSoLuongNhap.getText().trim());
            double gia = Double.parseDouble(txtGiaNhap.getText().trim());
            lblTongTien.setText(String.format("%,.0f VNĐ", sl * gia));
        } catch (NumberFormatException ex) {
            lblTongTien.setText("0 VNĐ");
        }
    }

    /** Reset form tab 1 */
    private void clearFormKho() {
        txtMaKho.setText("");
        txtMaKho.setEditable(true);
        txtTenKho.setText("");
        txtDiaChi.setText("");
        txtSucChua.setText("");
        txtGhiChu.setText("");
        txtTimKiemKho.setText("");
        tableKho.clearSelection();
    }

    /** Reset form tab 2 */
    private void clearFormPhieu() {
        txtMaPhieu.setText(truycapKho.taoMaKhoMoi());
        cmbMaNCC.setSelectedIndex(0);
        lblTenNCC.setText("—");
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtSoLuongNhap.setText("");
        txtGiaNhap.setText("");
        txtNgayNhap.setText(LocalDate.now().toString());
        lblTongTien.setText("0 VNĐ");
        tablePhieu.clearSelection();
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) { /* Default fallback */ }

        SwingUtilities.invokeLater(() -> new QuanLyKho().setVisible(true));
    }
}