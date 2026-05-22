package view;

import TruycapDL.TruycapKho;
<<<<<<< HEAD
import model.Kho;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * QuanLyKho – Màn hình quản lý danh mục kho hàng dành cho siêu thị Miniti.
 * Giao diện được thiết kế dạng JPanel chuyên nghiệp, đồng bộ với hệ thống.
=======
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.PhieuNhapKho;
import model.Sanpham;

/**
 * QuanLyKho – Màn hình quản lý kho hàng.
 *
 * Dựa theo PhieuNhapKho.java thực tế:
 *   1 phiếu = 1 dòng SP (maPhieuNhap, maNCC, tenNCC,
 *                         maSP, tenSP, soLuongNhap, giaNhap, ngayNhap)
 *
 * 4 tab:
 *   Tab 0 – Danh sách phiếu nhập (có thể xóa phiếu)
 *   Tab 1 – Tạo phiếu nhập mới
 *   Tab 2 – Cảnh báo tồn kho thấp
 *   Tab 3 – Cảnh báo hàng hết hạn
 *
 * Pattern đồng nhất với QuanLyNhanVien.java (extends JFrame).
>>>>>>> a122a7f490af7ae9e1ee76a7a274b868b869e597
 */
public class QuanLyKho extends JFrame {


<<<<<<< HEAD
    // ================================================================
    // FIELDS & COMPONENTS
    // ================================================================
    private final TruycapKho truycapKho;
    
    private JTextField txtMaKho, txtTenKho, txtDiaChi, txtSucChua, txtGhiChu, txtTimKiem;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JTable tableKho;
    private DefaultTableModel tableModel;

    // Bảng màu giao diện đồng bộ flat-design
    private static final Color COLOR_PRIMARY   = new Color(41, 128, 185); // Xanh dương chủ đạo
    private static final Color COLOR_SUCCESS   = new Color(39, 174, 96);  // Xanh lá (Thêm/Làm mới)
    private static final Color COLOR_WARNING   = new Color(241, 196, 15); // Vàng (Sửa)
    private static final Color COLOR_DANGER    = new Color(192, 57, 43);  // Đỏ (Xóa)
    private static final Color COLOR_BG        = new Color(245, 247, 250); // Màu nền nhẹ
=======
    // FIELDS

    private TruycapKho truycapKho;

    // --- Tab 0: Danh sách phiếu ---
    private DefaultTableModel modelPhieu;
    private JTable            tablePhieu;
    private JComboBox<String> cmbThang;
    private JComboBox<String> cmbNam;

    // --- Tab 1: Tạo phiếu mới ---
    private JTextField txtMaPhieuNhap; // tự sinh, không cho sửa
    private JTextField txtMaNCC;
    private JTextField txtTenNCC;
    private JTextField txtMaSP;
    private JTextField txtTenSP;
    private JTextField txtSoLuongNhap;
    private JTextField txtGiaNhap;
    private JLabel     lblTongTien;   // = soLuongNhap * giaNhap, tự tính

    // --- Tab 2: Tồn kho thấp ---
    private DefaultTableModel modelTonKho;
    private JTable            tableTonKho;

    // --- Tab 3: Hàng hết hạn ---
    private DefaultTableModel modelHetHan;
    private JTable            tableHetHan;
    private JComboBox<String> cmbLoaiHetHan;

>>>>>>> a122a7f490af7ae9e1ee76a7a274b868b869e597

    // CONSTRUCTOR

    public QuanLyKho() {
<<<<<<< HEAD
        this.truycapKho = new TruycapKho();
        initComponents();
        loadDataToTable();
    }

    // ================================================================
    // INITIALIZE COMPONENTS
    // ================================================================
    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // ------------------------------------------------------------
        // 1. PANEL PHÍA TRÊN: TIÊU ĐỀ & FORM NHẬP LIỆU
        // ------------------------------------------------------------
        JPanel pnlTop = new JPanel(new BorderLayout(10, 10));
        pnlTop.setBackground(COLOR_BG);

        // Form nhập liệu sử dụng GridBagLayout để căn chỉnh đẹp mắt
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARY, 1),
                "Thông Tin Kho Hàng", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), COLOR_PRIMARY));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Khởi tạo các trường nhập liệu
        txtMaKho = new JTextField(15);
        txtTenKho = new JTextField(20);
        txtDiaChi = new JTextField(25);
        txtSucChua = new JTextField(15);
        txtGhiChu = new JTextField(25);

        // Thêm các trường vào Form
        addFormField(pnlForm, "Mã Kho:", txtMaKho, gbc, 0, 0);
        addFormField(pnlForm, "Tên Kho:", txtTenKho, gbc, 1, 0);
        addFormField(pnlForm, "Sức Chứa:", txtSucChua, gbc, 0, 1);
        addFormField(pnlForm, "Địa Chỉ:", txtDiaChi, gbc, 1, 1);
        
        // Dòng ghi chú kéo dài hết kích thước form
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0;
        pnlForm.add(new JLabel("Ghi Chú:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        pnlForm.add(txtGhiChu, gbc);

        pnlTop.add(pnlForm, BorderLayout.CENTER);

        // ------------------------------------------------------------
        // 2. PANEL THANH CÔNG CỤ (TÌM KIẾM & NÚT CHỨC NĂNG)
        // ------------------------------------------------------------
        JPanel pnlActionToolbar = new JPanel(new BorderLayout(10, 0));
        pnlActionToolbar.setBackground(COLOR_BG);
        pnlActionToolbar.setBorder(new EmptyBorder(5, 0, 5, 0));

        // Nhóm các nút chức năng trái (Thêm, Sửa, Xóa, Làm mới)
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlButtons.setBackground(COLOR_BG);

        btnThem = createStyledButton("Thêm Kho", COLOR_SUCCESS);
        btnSua = createStyledButton("Sửa Kho", COLOR_WARNING);
        btnSua.setForeground(Color.BLACK); // Chữ đen trên nền vàng cho rõ
        btnXoa = createStyledButton("Xóa Kho", COLOR_DANGER);
        btnLamMoi = createStyledButton("Làm Mới", COLOR_PRIMARY);

        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLamMoi);

        // Nhóm tìm kiếm bên phải
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pnlSearch.setBackground(COLOR_BG);
        txtTimKiem = new JTextField(15);
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnTimKiem = createStyledButton("Tìm Kiếm", Color.DARK_GRAY);
        
        pnlSearch.add(new JLabel("Tìm theo tên: "));
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTimKiem);

        pnlActionToolbar.add(pnlButtons, BorderLayout.WEST);
        pnlActionToolbar.add(pnlSearch, BorderLayout.EAST);
        pnlTop.add(pnlActionToolbar, BorderLayout.SOUTH);

        add(pnlTop, BorderLayout.NORTH);

        // ------------------------------------------------------------
        // 3. PANEL TRUNG TÂM: BẢNG DỮ LIỆU
        // ------------------------------------------------------------
        String[] columnNames = {"Mã Kho", "Tên Kho", "Địa Chỉ", "Sức Chứa", "Ghi Chú"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa trực tiếp trên ô của bảng
            }
        };
        
        tableKho = new JTable(tableModel);
        styleTable(tableKho);
        
        JScrollPane scrollPane = new JScrollPane(tableKho);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(215, 219, 221)));
        add(scrollPane, BorderLayout.CENTER);

        // ================================================================
        // EVENT HANDLING (XỬ LÝ SỰ KIỆN)
        // ================================================================

        // Sự kiện click chọn dòng trên Table
        tableKho.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableKho.getSelectedRow();
                if (row >= 0) {
                    txtMaKho.setText(tableModel.getValueAt(row, 0).toString());
                    txtTenKho.setText(tableModel.getValueAt(row, 1).toString());
                    txtDiaChi.setText(tableModel.getValueAt(row, 2).toString());
                    txtSucChua.setText(tableModel.getValueAt(row, 3).toString());
                    
                    Object ghiChuObj = tableModel.getValueAt(row, 4);
                    txtGhiChu.setText(ghiChuObj != null ? ghiChuObj.toString() : "");
                    
                    txtMaKho.setEditable(false); // Khóa không cho sửa Mã Kho hiện tại
                    txtMaKho.setBackground(new Color(235, 240, 242));
                }
            }
        });

        // Nút Thêm Kho
        btnThem.addActionListener(e -> {
            Kho kho = getKhoFromForm();
            if (kho != null) {
                // Kiểm tra trùng mã trước khi thêm bằng hàm có sẵn trong DB
                if (truycapKho.getKhoByMa(kho.getMaKho()) != null) {
                    JOptionPane.showMessageDialog(this, "Mã kho này đã tồn tại trong hệ thống!", "Trùng mã lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (truycapKho.themKho(kho)) {
                    JOptionPane.showMessageDialog(this, "Thêm mới kho hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadDataToTable();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm kho thất bại! Kiểm tra lại kết nối Cơ sở dữ liệu.", "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Nút Sửa Kho
        btnSua.addActionListener(e -> {
            if (txtMaKho.isEditable()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một kho hàng từ bảng danh sách để thực hiện sửa thông tin!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Kho kho = getKhoFromForm();
            if (kho != null) {
                if (truycapKho.suaKho(kho)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thông tin kho hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadDataToTable();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật dữ liệu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Nút Xóa Kho
        btnXoa.addActionListener(e -> {
            String maKho = txtMaKho.getText().trim();
            if (maKho.isEmpty() || txtMaKho.isEditable()) {
                JOptionPane.showMessageDialog(this, "Vui lòng click chọn kho hàng trên bảng để tiến hành xóa!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa dữ liệu kho [" + maKho + "] không?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                if (truycapKho.xoaKho(maKho)) {
                    JOptionPane.showMessageDialog(this, "Đã xóa kho hàng ra khỏi hệ thống!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadDataToTable();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại! Kho có thể đang ràng buộc dữ liệu hàng hóa nhập xuất.", "Lỗi ràng buộc", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Nút Làm Mới
        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadDataToTable();
        });

        // Nút Tìm Kiếm
        btnTimKiem.addActionListener(e -> {
            String tuKhoa = txtTimKiem.getText().trim();
            List<Kho> list = truycapKho.timKhoTheoTen(tuKhoa);
            fillDataToTable(list);
        });
    }

    // ================================================================
    // AUXILIARY HELPER METHODS (CÁC HÀM TRỢ GIÚP)
    // ================================================================

    private void addFormField(JPanel panel, String label, JTextField field, GridBagConstraints gbc, int x, int y) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        gbc.gridx = x * 2;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = x * 2 + 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(baseColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(7, 15, 7, 15));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(COLOR_PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(230, 233, 235));
    }

    private void loadDataToTable() {
        List<Kho> list = truycapKho.getAllKho();
        fillDataToTable(list);
    }

    private void fillDataToTable(List<Kho> list) {
        tableModel.setRowCount(0);
        for (Kho k : list) {
            tableModel.addRow(new Object[]{
                    k.getMaKho(),
                    k.getTenKho(),
                    k.getDiaChi(),
                    k.getSucChua(),
                    k.getGhiChu()
            });
        }
    }

    private void clearForm() {
        txtMaKho.setText("");
        txtTenKho.setText("");
        txtDiaChi.setText("");
        txtSucChua.setText("");
        txtGhiChu.setText("");
        txtTimKiem.setText("");
        
        txtMaKho.setEditable(true);
        txtMaKho.setBackground(Color.WHITE);
        tableKho.clearSelection();
    }

    private Kho getKhoFromForm() {
        String ma = txtMaKho.getText().trim();
        String ten = txtTenKho.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String sucChuaStr = txtSucChua.getText().trim();
        String ghiChu = txtGhiChu.getText().trim();

        // Kiểm tra validation dữ liệu cơ bản
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã kho không được phép để trống!", "Lỗi nhập dữ liệu", JOptionPane.WARNING_MESSAGE);
            txtMaKho.requestFocus();
            return null;
        }
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên kho không được phép để trống!", "Lỗi nhập dữ liệu", JOptionPane.WARNING_MESSAGE);
            txtTenKho.requestFocus();
            return null;
        }

        int sucChua = 0;
        if (!sucChuaStr.isEmpty()) {
            try {
                sucChua = Integer.parseInt(sucChuaStr);
                if (sucChua < 0) {
                    JOptionPane.showMessageDialog(this, "Sức chứa tối đa của kho phải là số lớn hơn hoặc bằng 0!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
                    txtSucChua.requestFocus();
                    return null;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Trường Sức Chứa phải nhập định dạng số nguyên!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
                txtSucChua.requestFocus();
                return null;
            }
        }

        return new Kho(ma, ten, diaChi, sucChua, ghiChu);
=======
        truycapKho = new TruycapKho();
        initComponents();
        loadDataPhieu();
    }


    // KHỞI TẠO GIAO DIỆN

    private void initComponents() {
        setTitle("Quản Lý Kho Hàng – Miniti");
        setSize(1100, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("📋 Danh Sách Phiếu Nhập", taoTab0_DanhSachPhieu());
        tabbedPane.addTab("➕ Tạo Phiếu Nhập Mới",   taoTab1_TaoPhieu());
        tabbedPane.addTab("⚠ Tồn Kho Thấp",          taoTab2_TonKhoThap());
        tabbedPane.addTab("⏰ Hàng Hết Hạn",          taoTab3_HetHan());

        tabbedPane.addChangeListener(e -> {
            switch (tabbedPane.getSelectedIndex()) {
                case 0: loadDataPhieu();    break;
                case 1: khoiTaoFormPhieu(); break;
                case 2: loadDataTonKho();   break;
                case 3: loadDataHetHan();   break;
            }
        });

        add(tabbedPane, BorderLayout.CENTER);
    }


    // TAB 0 – DANH SÁCH PHIẾU NHẬP

    private JPanel taoTab0_DanhSachPhieu() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Thanh lọc + nút xóa
        JPanel pnlTop = new JPanel(new BorderLayout());

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlFilter.setBorder(BorderFactory.createTitledBorder("Lọc theo thời gian"));

        cmbThang = new JComboBox<>(new String[]{
            "Tất cả","01","02","03","04","05","06",
            "07","08","09","10","11","12"
        });
        cmbNam = new JComboBox<>();
        int namHienTai = LocalDate.now().getYear();
        for (int y = namHienTai; y >= namHienTai - 4; y--) {
            cmbNam.addItem(String.valueOf(y));
        }

        JButton btnLoc    = new JButton("Lọc");
        JButton btnLamMoi = new JButton("Làm Mới");
        JButton btnXoa    = new JButton("Xóa Phiếu Đã Chọn");

        btnLoc   .addActionListener(e -> loadDataPhieu());
        btnLamMoi.addActionListener(e -> { cmbThang.setSelectedIndex(0); loadDataPhieu(); });
        btnXoa   .addActionListener(e -> xoaPhieuDaChon());

        pnlFilter.add(new JLabel("Tháng:")); pnlFilter.add(cmbThang);
        pnlFilter.add(new JLabel("Năm:"));   pnlFilter.add(cmbNam);
        pnlFilter.add(btnLoc);
        pnlFilter.add(btnLamMoi);
        pnlFilter.add(btnXoa);

        pnlTop.add(pnlFilter, BorderLayout.CENTER);

        // Bảng phiếu nhập
        String[] cols = {
            "Mã Phiếu Nhập", "Mã NCC", "Tên NCC",
            "Mã SP", "Tên SP", "Số Lượng Nhập",
            "Giá Nhập (VNĐ)", "Thành Tiền (VNĐ)", "Ngày Nhập"
        };
        modelPhieu = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablePhieu = new JTable(modelPhieu);
        tablePhieu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePhieu.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tablePhieu);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách phiếu nhập kho"));

        panel.add(pnlTop, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void loadDataPhieu() {
        modelPhieu.setRowCount(0);
        List<PhieuNhapKho> ds;

        String chonThang = (String) cmbThang.getSelectedItem();
        if ("Tất cả".equals(chonThang)) {
            ds = truycapKho.getAllPhieuNhap();
        } else {
            int thang = Integer.parseInt(chonThang);
            int nam   = Integer.parseInt((String) cmbNam.getSelectedItem());
            ds = truycapKho.layPhieuTheoThang(thang, nam);
        }

        for (PhieuNhapKho p : ds) {
            // tinhTongTien() = soLuongNhap * giaNhap — method có sẵn trong PhieuNhapKho
            modelPhieu.addRow(new Object[]{
                p.getMaPhieuNhap(),
                p.getMaNCC(),
                p.getTenNCC(),
                p.getMaSP(),
                p.getTenSP(),
                p.getSoLuongNhap(),
                String.format("%,.0f", p.getGiaNhap()),
                String.format("%,.0f", p.tinhTongTien()),
                p.getNgayNhap() != null ? p.getNgayNhap().toString() : ""
            });
        }
    }

    /** Xóa phiếu đang chọn trên bảng, hoàn lại tồn kho SP */
    private void xoaPhieuDaChon() {
        int row = tablePhieu.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn phiếu cần xóa trên bảng!",
                "Chưa chọn phiếu", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maPhieuNhap = modelPhieu.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
            "Xóa phiếu " + maPhieuNhap + "?\n"
            + "Tồn kho sản phẩm sẽ được hoàn lại tự động.",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (truycapKho.xoaPhieuNhap(maPhieuNhap)) {
                JOptionPane.showMessageDialog(this,
                    "Đã xóa phiếu " + maPhieuNhap + " và hoàn lại tồn kho thành công!",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadDataPhieu();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Xóa thất bại!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    // TAB 1 – TẠO PHIẾU NHẬP MỚI

    private JPanel taoTab1_TaoPhieu() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form nhập liệu – 5 hàng x 4 cột (giống pattern QuanLyNhanVien)
        JPanel pnlForm = new JPanel(new GridLayout(5, 4, 10, 8));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin phiếu nhập kho"));

        // Hàng 1
        pnlForm.add(new JLabel("Mã Phiếu Nhập (tự động):"));
        txtMaPhieuNhap = new JTextField();
        txtMaPhieuNhap.setEditable(false);
        txtMaPhieuNhap.setBackground(new Color(230, 230, 230));
        pnlForm.add(txtMaPhieuNhap);

        pnlForm.add(new JLabel("Ngày Nhập:"));
        JTextField txtNgayNhap = new JTextField(
            LocalDate.now().toString());
        txtNgayNhap.setEditable(false);
        txtNgayNhap.setBackground(new Color(230, 230, 230));
        pnlForm.add(txtNgayNhap);

        // Hàng 2
        pnlForm.add(new JLabel("Mã NCC:"));
        txtMaNCC = new JTextField();
        pnlForm.add(txtMaNCC);

        pnlForm.add(new JLabel("Tên NCC:"));
        txtTenNCC = new JTextField();
        pnlForm.add(txtTenNCC);

        // Hàng 3
        pnlForm.add(new JLabel("Mã SP:"));
        txtMaSP = new JTextField();
        pnlForm.add(txtMaSP);

        pnlForm.add(new JLabel("Tên SP:"));
        txtTenSP = new JTextField();
        pnlForm.add(txtTenSP);

        // Hàng 4
        pnlForm.add(new JLabel("Số Lượng Nhập:"));
        txtSoLuongNhap = new JTextField();
        pnlForm.add(txtSoLuongNhap);

        pnlForm.add(new JLabel("Giá Nhập (VNĐ):"));
        txtGiaNhap = new JTextField();
        pnlForm.add(txtGiaNhap);

        // Hàng 5
        pnlForm.add(new JLabel("Thành Tiền:"));
        lblTongTien = new JLabel("0 VNĐ");
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongTien.setForeground(new Color(192, 57, 43));
        pnlForm.add(lblTongTien);

        // Cân bằng GridLayout
        pnlForm.add(new JLabel(""));
        pnlForm.add(new JLabel(""));

        // Tự tính thành tiền khi nhập số lượng hoặc giá
        txtSoLuongNhap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) { capNhatThanhTien(); }
        });
        txtGiaNhap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) { capNhatThanhTien(); }
        });

        // Thanh nút
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton btnLuu    = new JButton("💾 Lưu Phiếu");
        JButton btnLamMoi = new JButton("Làm Mới");

        btnLuu   .addActionListener(e -> luuPhieuNhap());
        btnLamMoi.addActionListener(e -> khoiTaoFormPhieu());

        pnlActions.add(btnLuu);
        pnlActions.add(btnLamMoi);

        panel.add(pnlForm,    BorderLayout.NORTH);
        panel.add(pnlActions, BorderLayout.SOUTH);

        khoiTaoFormPhieu();
        return panel;
    }

    /** Reset toàn bộ form, sinh mã phiếu mới */
    private void khoiTaoFormPhieu() {
        txtMaPhieuNhap.setText(truycapKho.taoMaPhieuMoi());
        txtMaNCC.setText("");
        txtTenNCC.setText("");
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtSoLuongNhap.setText("");
        txtGiaNhap.setText("");
        lblTongTien.setText("0 VNĐ");
    }

    /** Tự tính thành tiền = soLuongNhap * giaNhap */
    private void capNhatThanhTien() {
        try {
            int    sl = Integer.parseInt(txtSoLuongNhap.getText().trim());
            double gn = Double.parseDouble(txtGiaNhap.getText().trim());
            lblTongTien.setText(String.format("%,.0f VNĐ", sl * gn));
        } catch (NumberFormatException e) {
            lblTongTien.setText("0 VNĐ");
        }
    }

    /** Validate và lưu phiếu vào DB */
    private void luuPhieuNhap() {
        String maPhieuNhap = txtMaPhieuNhap.getText().trim();
        String maNCC       = txtMaNCC.getText().trim();
        String tenNCC      = txtTenNCC.getText().trim();
        String maSP        = txtMaSP.getText().trim();
        String tenSP       = txtTenSP.getText().trim();
        String slStr       = txtSoLuongNhap.getText().trim();
        String gnStr       = txtGiaNhap.getText().trim();

        // Validate rỗng
        if (maNCC.isEmpty() || tenNCC.isEmpty()
                || maSP.isEmpty() || tenSP.isEmpty()
                || slStr.isEmpty() || gnStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập đầy đủ tất cả các trường!",
                "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate số
        int    soLuongNhap;
        double giaNhap;
        try {
            soLuongNhap = Integer.parseInt(slStr);
            giaNhap     = Double.parseDouble(gnStr);
            if (soLuongNhap <= 0) throw new NumberFormatException();
            if (giaNhap < 0)      throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Số lượng phải là số nguyên dương!\nGiá nhập phải là số không âm!",
                "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tạo object PhieuNhapKho – dùng đúng setter thực tế
        PhieuNhapKho phieu = new PhieuNhapKho();
        phieu.setMaPhieuNhap(maPhieuNhap);
        phieu.setMaNCC      (maNCC);
        phieu.setTenNCC     (tenNCC);
        phieu.setMaSP       (maSP);
        phieu.setTenSP      (tenSP);
        phieu.setSoLuongNhap(soLuongNhap);
        phieu.setGiaNhap    (giaNhap);
        phieu.setNgayNhap   (LocalDate.now());

        if (truycapKho.themPhieuNhap(phieu)) {
            JOptionPane.showMessageDialog(this,
                "Tạo phiếu " + maPhieuNhap + " thành công!\n"
                + "Tồn kho SP [" + maSP + "] đã được cộng thêm " + soLuongNhap + ".",
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
            khoiTaoFormPhieu();
        } else {
            JOptionPane.showMessageDialog(this,
                "Lưu phiếu thất bại! Kiểm tra lại:\n"
                + "  • Mã NCC có tồn tại trong bảng nhacungcap không?\n"
                + "  • Mã SP có tồn tại trong bảng sanpham không?",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }


    // TAB 2 – TỒN KHO THẤP

    private JPanel taoTab2_TonKhoThap() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton btnLamMoi = new JButton("Làm Mới");
        btnLamMoi.addActionListener(e -> loadDataTonKho());
        pnlToolbar.add(new JLabel(
            "Sản phẩm có tồn kho ≤ mức tối thiểu cần nhập thêm:"));
        pnlToolbar.add(btnLamMoi);

        String[] cols = {
            "Mã SP", "Tên SP", "Loại SP",
            "Tồn Kho Hiện Tại", "Mức Tối Thiểu", "Cần Nhập Thêm"
        };
        modelTonKho = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tableTonKho = new JTable(modelTonKho);
        tableTonKho.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableTonKho.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tableTonKho);
        scroll.setBorder(BorderFactory.createTitledBorder(
            "Sản phẩm cần nhập thêm hàng"));

        panel.add(pnlToolbar, BorderLayout.NORTH);
        panel.add(scroll,     BorderLayout.CENTER);
        return panel;
    }

    private void loadDataTonKho() {
        modelTonKho.setRowCount(0);
        List<Sanpham> ds = truycapKho.laySPTonKhoThap();

        for (Sanpham sp : ds) {
            int canNhapThem = Math.max(0,
                sp.getSoLuongToiThieu() - sp.getSoLuong());
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
                "Tất cả sản phẩm đều còn đủ hàng. Kho ổn định!",
                "Kho ổn định ✅", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    // TAB 3 – HÀNG HẾT HẠN

    private JPanel taoTab3_HetHan() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlFilter.setBorder(BorderFactory.createTitledBorder("Lọc loại cảnh báo"));

        cmbLoaiHetHan = new JComboBox<>(new String[]{
            "Sắp hết hạn (trong 30 ngày)",
            "Đã hết hạn"
        });
        JButton btnLoc    = new JButton("Lọc");
        JButton btnLamMoi = new JButton("Làm Mới");

        btnLoc   .addActionListener(e -> loadDataHetHan());
        btnLamMoi.addActionListener(e -> {
            cmbLoaiHetHan.setSelectedIndex(0);
            loadDataHetHan();
        });

        pnlFilter.add(new JLabel("Hiển thị:")); pnlFilter.add(cmbLoaiHetHan);
        pnlFilter.add(btnLoc);
        pnlFilter.add(btnLamMoi);

        String[] cols = {
            "Mã SP", "Tên SP", "Loại SP",
            "Tồn Kho", "Ngày Hết Hạn", "Tình Trạng"
        };
        modelHetHan = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tableHetHan = new JTable(modelHetHan);
        tableHetHan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableHetHan.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tableHetHan);
        scroll.setBorder(BorderFactory.createTitledBorder(
            "Danh sách sản phẩm cần chú ý"));

        panel.add(pnlFilter, BorderLayout.NORTH);
        panel.add(scroll,    BorderLayout.CENTER);
        return panel;
    }

    private void loadDataHetHan() {
        modelHetHan.setRowCount(0);
        boolean chonSap = cmbLoaiHetHan.getSelectedIndex() == 0;

        List<Sanpham> ds = chonSap
                ? truycapKho.laySPSapHetHan()
                : truycapKho.laySPDaHetHan();

        String tinhTrang = chonSap ? "Sắp hết hạn" : "Đã hết hạn";

        for (Sanpham sp : ds) {
            modelHetHan.addRow(new Object[]{
                sp.getMaSP(),
                sp.getTenSP(),
                sp.getLoaiSP(),
                sp.getSoLuong(),
                sp.getNgayHetHan() != null
                    ? sp.getNgayHetHan().toString() : "N/A",
                tinhTrang
            });
        }

        if (ds.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                chonSap
                    ? "Không có sản phẩm nào sắp hết hạn trong 30 ngày tới."
                    : "Không có sản phẩm nào đã hết hạn.",
                "Không có dữ liệu", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    // MAIN – Chạy thử độc lập

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) { /* dùng look and feel mặc định */ }

        SwingUtilities.invokeLater(() -> new QuanLyKho().setVisible(true));
>>>>>>> a122a7f490af7ae9e1ee76a7a274b868b869e597
    }
}