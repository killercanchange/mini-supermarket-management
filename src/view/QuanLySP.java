package view;

import TruycapDL.TruycapSP;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import model.Nhanvien;
import model.Sanpham;

public class QuanLySP extends JFrame {

    //  CONSTANTS 
    private static final String[] LOAI_SP_LOC = {
        "-- Tất cả --", "Thực phẩm", "Đồ uống", "Hóa mỹ phẩm", "Gia dụng", "Văn phòng phẩm", "Khác"
    };
    private static final String[] LOAI_SP_FORM = {
        "Thực phẩm", "Đồ uống", "Hóa mỹ phẩm", "Gia dụng", "Văn phòng phẩm", "Khác"
    };
    private static final String[] COT_BANG = {
        "Mã SP", "Tên sản phẩm", "Số lượng", "Giá bán (VNĐ)", "Loại SP",
        "Ngày hết hạn", "Tồn kho tối thiểu", "Trạng thái"
    };
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    //  DATA LAYER 
    private final TruycapSP truycapSP = new TruycapSP();
    private final Nhanvien nguoiDung;
    private final boolean isAdmin;

    //  COMPONENTS — Tab 1 
    private JTabbedPane tabbedPane;
    private JTable bangSanpham;
    private DefaultTableModel modelBang;

    private JTextField txtMaSP, txtTenSP, txtGiaBan, txtNgayHetHan, txtSoLuong, txtSoLuongToiThieu;
    private JComboBox<String> cboLoaiSP;
    private JTextField txtTimKiem;
    private JComboBox<String> cboLocLoai;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem, btnLocLoai;

    // COMPONENTS — Tab 2 
    private JTable bangTonKhoThap, bangSapHetHan, bangDaHetHan;
    private DefaultTableModel modelTonKho, modelSapHetHan, modelDaHetHan;
    private JButton btnLamMoiCanhBao;

    //  CONSTRUCTOR 
    public QuanLySP(Nhanvien nguoiDung) {
        this.nguoiDung = nguoiDung;
        this.isAdmin = (nguoiDung != null && nguoiDung.isAdmin());

        setTitle("Quản lý sản phẩm — Siêu thị Miniti");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        applyNimbus();
        buildUI();
        loadDataToTable();

        setVisible(true);
    }

    //  NIMBUS LOOK & FEEL 
    private void applyNimbus() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }
    }

    //  BUILD UI 
    private void buildUI() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.addTab("Danh sach & CRUD", buildTabDanhSach());
        tabbedPane.addTab("Canh bao ton kho", buildTabCanhBao());
        add(tabbedPane);
    }

  
    // TAB 1: DANH SÁCH & CRUD

    private JPanel buildTabDanhSach() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(buildPanelForm(),     BorderLayout.NORTH);
        panel.add(buildPanelBang(),     BorderLayout.CENTER);
        panel.add(buildPanelTimKiem(),  BorderLayout.SOUTH);
        return panel;
    }

    // --- Form nhập liệu ---
    private JPanel buildPanelForm() {
        JPanel outer = new JPanel(new BorderLayout(5, 5));

        JLabel title = new JLabel("THONG TIN SAN PHAM", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(new Color(33, 97, 140));
        title.setBorder(new EmptyBorder(0, 0, 8, 0));
        outer.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(33, 97, 140), 1), ""));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaSP             = new JTextField(12);
        txtTenSP            = new JTextField(22);
        txtGiaBan           = new JTextField(12);
        txtSoLuong          = new JTextField(12);
        cboLoaiSP           = new JComboBox<>(LOAI_SP_FORM);
        txtSoLuongToiThieu  = new JTextField(12);
        txtNgayHetHan       = new JTextField(12);
        txtNgayHetHan.setToolTipText("Dinh dang: dd/MM/yyyy");

        addFormRow(grid, gbc, 0, 0, "Ma SP (*)",           txtMaSP);
        addFormRow(grid, gbc, 0, 2, "Ten san pham (*)",    txtTenSP);
        addFormRow(grid, gbc, 1, 0, "Gia ban (VND) (*)",   txtGiaBan);
        addFormRow(grid, gbc, 1, 2, "So luong (*)",        txtSoLuong);
        addFormRow(grid, gbc, 2, 0, "Loai san pham (*)",   cboLoaiSP);
        addFormRow(grid, gbc, 2, 2, "Ton kho toi thieu",   txtSoLuongToiThieu);
        addFormRow(grid, gbc, 3, 0, "Ngay het han (dd/MM/yyyy)", txtNgayHetHan);

        gbc.gridx = 2; gbc.gridy = 3; gbc.gridwidth = 2;
        JLabel hint = new JLabel("(*) Bat buoc nhap");
        hint.setForeground(Color.GRAY);
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        grid.add(hint, gbc);
        gbc.gridwidth = 1;

        outer.add(grid,            BorderLayout.CENTER);
        outer.add(buildPanelNut(), BorderLayout.SOUTH);

        if (!isAdmin) disableFormCRUD();

        return outer;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc,
                            int row, int col, String label, JComponent field) {
        gbc.gridx = col; gbc.gridy = row; gbc.weightx = 0;
        JLabel lbl = new JLabel(label + ":");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(lbl, gbc);

        gbc.gridx = col + 1; gbc.weightx = 1;
        panel.add(field, gbc);
    }

    // --- Panel nút bấm ---
    private JPanel buildPanelNut() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));

        btnThem   = createButton("Them",    new Color(39, 174, 96));
        btnSua    = createButton("Sua",     new Color(41, 128, 185));
        btnXoa    = createButton("Xoa",     new Color(192, 57, 43));
        btnLamMoi = createButton("Lam moi", new Color(127, 140, 141));

        btnThem.addActionListener(e -> themSanpham());
        btnSua.addActionListener(e  -> suaSanpham());
        btnXoa.addActionListener(e  -> xoaSanpham());
        btnLamMoi.addActionListener(e -> { clearForm(); loadDataToTable(); });

        panel.add(btnThem);
        panel.add(btnSua);
        panel.add(btnXoa);
        panel.add(btnLamMoi);
        return panel;
    }

    // --- Bảng sản phẩm ---
    private JPanel buildPanelBang() {
        modelBang = new DefaultTableModel(COT_BANG, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        bangSanpham = new JTable(modelBang);
        styleTable(bangSanpham);
        bangSanpham.setDefaultRenderer(Object.class, new CanhBaoRenderer());

        int[] widths = {70, 200, 70, 110, 100, 100, 110, 120};
        for (int i = 0; i < widths.length; i++) {
            bangSanpham.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        bangSanpham.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromTable();
        });

        JScrollPane scroll = new JScrollPane(bangSanpham);
        scroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(33, 97, 140), 1),
            "Danh sach san pham", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), new Color(33, 97, 140)));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // --- Panel tìm kiếm & lọc ---
    private JPanel buildPanelTimKiem() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "Tim kiem & Loc"));

        txtTimKiem = new JTextField(20);
        btnTimKiem = createButton("Tim", new Color(142, 68, 173));

        cboLocLoai = new JComboBox<>(LOAI_SP_LOC);
        btnLocLoai = createButton("Loc loai", new Color(22, 160, 133));

        JButton btnXoaLoc = createButton("Xoa loc", new Color(127, 140, 141));

        btnTimKiem.addActionListener(e -> timKiemTheoTen());
        btnLocLoai.addActionListener(e -> locTheoLoai());
        btnXoaLoc.addActionListener(e  -> loadDataToTable());

        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) timKiemTheoTen();
            }
        });

        panel.add(new JLabel("Tim ten:"));
        panel.add(txtTimKiem);
        panel.add(btnTimKiem);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(new JLabel("Loc loai:"));
        panel.add(cboLocLoai);
        panel.add(btnLocLoai);
        panel.add(btnXoaLoc);
        return panel;
    }


    // TAB 2: CẢNH BÁO

    private JPanel buildTabCanhBao() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnLamMoiCanhBao = createButton("Lam moi canh bao", new Color(41, 128, 185));
        btnLamMoiCanhBao.addActionListener(e -> loadCanhBao());
        topPanel.add(btnLamMoiCanhBao);

        JLabel note = new JLabel("  Du lieu duoc cap nhat theo thoi gian thuc tu database");
        note.setForeground(new Color(192, 57, 43));
        note.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        topPanel.add(note);
        panel.add(topPanel, BorderLayout.NORTH);

        // Bảng tồn kho thấp
        String[] cotTonKho = {"Ma SP", "Ten san pham", "So luong hien tai", "Ton kho toi thieu", "Can nhap them"};
        modelTonKho = new DefaultTableModel(cotTonKho, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bangTonKhoThap = new JTable(modelTonKho);
        styleTable(bangTonKhoThap);
        bangTonKhoThap.setBackground(new Color(255, 249, 235));
        JScrollPane scrollTonKho = new JScrollPane(bangTonKhoThap);
        scrollTonKho.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(243, 156, 18), 2),
            "Ton kho thap (so luong <= toi thieu)",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), new Color(180, 100, 0)));

        // Bảng sắp hết hạn
        String[] cotHetHan = {"Ma SP", "Ten san pham", "So luong", "Ngay het han", "Con lai (ngay)"};
        modelSapHetHan = new DefaultTableModel(cotHetHan, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bangSapHetHan = new JTable(modelSapHetHan);
        styleTable(bangSapHetHan);
        bangSapHetHan.setBackground(new Color(255, 243, 230));
        JScrollPane scrollSapHetHan = new JScrollPane(bangSapHetHan);
        scrollSapHetHan.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(230, 126, 34), 2),
            "Sap het han (trong vong 30 ngay)",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), new Color(180, 80, 0)));

        // Bảng đã hết hạn
        modelDaHetHan = new DefaultTableModel(cotHetHan, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bangDaHetHan = new JTable(modelDaHetHan);
        styleTable(bangDaHetHan);
        bangDaHetHan.setBackground(new Color(255, 235, 235));
        JScrollPane scrollDaHetHan = new JScrollPane(bangDaHetHan);
        scrollDaHetHan.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(192, 57, 43), 2),
            "Da het han",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), new Color(150, 0, 0)));

        JSplitPane split1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollTonKho, scrollSapHetHan);
        split1.setResizeWeight(0.5);
        JSplitPane split2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, split1, scrollDaHetHan);
        split2.setResizeWeight(0.67);
        panel.add(split2, BorderLayout.CENTER);

        loadCanhBao();
        return panel;
    }


    // LOAD DATA

    private void loadDataToTable() {
        fillTable(truycapSP.getAllSanpham());
    }

    private void fillTable(List<Sanpham> danhSach) {
        modelBang.setRowCount(0);
        for (Sanpham sp : danhSach) {
            modelBang.addRow(new Object[]{
                sp.getMaSP(),
                sp.getTenSP(),
                sp.getSoLuong(),
                String.format("%,.0f", sp.getGiaBan()),
                sp.getLoaiSP(),
                sp.getNgayHetHan() != null ? sp.getNgayHetHan().format(DATE_FMT) : "Khong co",
                sp.getSoLuongToiThieu(),
                getTrangThai(sp)
            });
        }
        setTitle("Quan ly san pham - Miniti (" + danhSach.size() + " san pham)");
    }

    private String getTrangThai(Sanpham sp) {
        if (sp.isHetHan())     return "Da het han";
        if (sp.isSapHetHan())  return "Sap het han";
        if (sp.isTonKhoThap()) return "Ton kho thap";
        return "Binh thuong";
    }

    private void loadCanhBao() {
        List<Sanpham> all = truycapSP.getAllSanpham();

        // Tồn kho thấp
        modelTonKho.setRowCount(0);
        List<Sanpham> tonKhoThap = new ArrayList<>();
        for (Sanpham sp : all) {
            if (sp.isTonKhoThap()) tonKhoThap.add(sp);
        }
        for (Sanpham sp : tonKhoThap) {
            int canNhapThem = Math.max(sp.getSoLuongToiThieu() - sp.getSoLuong(), 0);
            modelTonKho.addRow(new Object[]{
                sp.getMaSP(), sp.getTenSP(),
                sp.getSoLuong(), sp.getSoLuongToiThieu(), canNhapThem
            });
        }

        // Sắp hết hạn
        modelSapHetHan.setRowCount(0);
        for (Sanpham sp : all) {
            if (sp.isSapHetHan()) {
                long conLai = ChronoUnit.DAYS.between(LocalDate.now(), sp.getNgayHetHan());
                modelSapHetHan.addRow(new Object[]{
                    sp.getMaSP(), sp.getTenSP(), sp.getSoLuong(),
                    sp.getNgayHetHan().format(DATE_FMT), conLai + " ngay"
                });
            }
        }

        // Đã hết hạn
        modelDaHetHan.setRowCount(0);
        for (Sanpham sp : all) {
            if (sp.isHetHan()) {
                long quaHan = ChronoUnit.DAYS.between(sp.getNgayHetHan(), LocalDate.now());
                modelDaHetHan.addRow(new Object[]{
                    sp.getMaSP(), sp.getTenSP(), sp.getSoLuong(),
                    sp.getNgayHetHan().format(DATE_FMT), "Qua " + quaHan + " ngay"
                });
            }
        }
    }


    // FILL FORM TỪ BẢNG

    private void fillFormFromTable() {
        int row = bangSanpham.getSelectedRow();
        if (row < 0) return;

        txtMaSP.setText(modelBang.getValueAt(row, 0).toString());
        txtTenSP.setText(modelBang.getValueAt(row, 1).toString());
        txtSoLuong.setText(modelBang.getValueAt(row, 2).toString());
        // Giá bán trong bảng đã format có dấu phẩy, cần bỏ trước khi điền
        txtGiaBan.setText(modelBang.getValueAt(row, 3).toString().replace(",", ""));
        cboLoaiSP.setSelectedItem(modelBang.getValueAt(row, 4).toString());
        String ngay = modelBang.getValueAt(row, 5).toString();
        txtNgayHetHan.setText(ngay.equals("Khong co") ? "" : ngay);
        txtSoLuongToiThieu.setText(modelBang.getValueAt(row, 6).toString());

        txtMaSP.setEditable(false); // Không cho sửa mã SP
    }


    // CLEAR FORM
  
    private void clearForm() {
        txtMaSP.setText("");
        txtMaSP.setEditable(true);
        txtTenSP.setText("");
        txtGiaBan.setText("");
        txtSoLuong.setText("");
        txtNgayHetHan.setText("");
        txtSoLuongToiThieu.setText("");
        cboLoaiSP.setSelectedIndex(0);
        bangSanpham.clearSelection();
    }


    // CRUD OPERATIONS

    private void themSanpham() {
        Sanpham sp = docForm();
        if (sp == null) return;
        if (truycapSP.getSanphamByMa(sp.getMaSP()) != null) {
            showError("Ma san pham \"" + sp.getMaSP() + "\" da ton tai!");
            return;
        }
        if (truycapSP.themSanpham(sp)) {
            showSuccess("Them san pham thanh cong!");
            clearForm();
            loadDataToTable();
        } else {
            showError("Them san pham that bai. Kiem tra ket noi database.");
        }
    }

    private void suaSanpham() {
        if (bangSanpham.getSelectedRow() < 0) {
            showWarning("Vui long chon san pham can sua!");
            return;
        }
        Sanpham sp = docForm();
        if (sp == null) return;
        if (truycapSP.suaSanpham(sp)) {
            showSuccess("Cap nhat san pham thanh cong!");
            clearForm();
            loadDataToTable();
        } else {
            showError("Cap nhat that bai. Kiem tra ket noi database.");
        }
    }

    private void xoaSanpham() {
        int row = bangSanpham.getSelectedRow();
        if (row < 0) {
            showWarning("Vui long chon san pham can xoa!");
            return;
        }
        String maSP  = modelBang.getValueAt(row, 0).toString();
        String tenSP = modelBang.getValueAt(row, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
            "Ban co chac muon xoa san pham:\n\"" + tenSP + "\" (" + maSP + ")?",
            "Xac nhan xoa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (truycapSP.xoaSanpham(maSP)) {
                showSuccess("Xoa san pham thanh cong!");
                clearForm();
                loadDataToTable();
            } else {
                showError("Xoa that bai. San pham co the dang duoc tham chieu.");
            }
        }
    }


    // TÌM KIẾM & LỌC

    private void timKiemTheoTen() {
        String keyword = txtTimKiem.getText().trim();
        if (keyword.isEmpty()) {
            loadDataToTable();
            return;
        }
        fillTable(truycapSP.timTheoTen(keyword));
    }

    private void locTheoLoai() {
        String loai = (String) cboLocLoai.getSelectedItem();
        if (loai == null || loai.equals("-- Tat ca --")) {
            loadDataToTable();
            return;
        }
        fillTable(truycapSP.timTheoLoai(loai));
    }


    // ĐỌC FORM — validate + tạo object Sanpham

    private Sanpham docForm() {
        String maSP         = txtMaSP.getText().trim();
        String tenSP        = txtTenSP.getText().trim();
        String giaStr       = txtGiaBan.getText().trim();
        String soLuongStr   = txtSoLuong.getText().trim();
        String tolThieuStr  = txtSoLuongToiThieu.getText().trim();
        String loaiSP       = (String) cboLoaiSP.getSelectedItem();
        String ngayStr      = txtNgayHetHan.getText().trim();

        if (maSP.isEmpty() || tenSP.isEmpty() || giaStr.isEmpty() || soLuongStr.isEmpty()) {
            showWarning("Vui long nhap day du cac truong bat buoc (*)!");
            return null;
        }

        double giaBan;
        try {
            giaBan = Double.parseDouble(giaStr);
            if (giaBan < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showWarning("Gia ban phai la so duong hop le!");
            return null;
        }

        int soLuong;
        try {
            soLuong = Integer.parseInt(soLuongStr);
            if (soLuong < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showWarning("So luong phai la so nguyen khong am!");
            return null;
        }

        int soLuongToiThieu = 0;
        if (!tolThieuStr.isEmpty()) {
            try {
                soLuongToiThieu = Integer.parseInt(tolThieuStr);
                if (soLuongToiThieu < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                showWarning("Ton kho toi thieu phai la so nguyen khong am!");
                return null;
            }
        }

        LocalDate ngayHetHan = null;
        if (!ngayStr.isEmpty()) {
            try {
                ngayHetHan = LocalDate.parse(ngayStr, DATE_FMT);
            } catch (DateTimeParseException e) {
                showWarning("Ngay het han khong dung dinh dang dd/MM/yyyy!");
                return null;
            }
        }

        return new Sanpham(maSP, tenSP, soLuong, giaBan, loaiSP, ngayHetHan, soLuongToiThieu);
    }


    // PHÂN QUYỀN

    private void disableFormCRUD() {
        txtMaSP.setEditable(false);
        txtTenSP.setEditable(false);
        txtGiaBan.setEditable(false);
        txtSoLuong.setEditable(false);
        txtNgayHetHan.setEditable(false);
        txtSoLuongToiThieu.setEditable(false);
        cboLoaiSP.setEnabled(false);
        btnThem.setEnabled(false);
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
    }


    // HELPER: style table

    private void styleTable(JTable table) {
        table.setRowHeight(24);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(33, 97, 140));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(200, 200, 200));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setAutoCreateRowSorter(true);
    }


    // HELPER: tạo nút

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 32));
        return btn;
    }


    // HELPER: dialog

    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thanh cong", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Loi", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Canh bao", JOptionPane.WARNING_MESSAGE);
    }


    // RENDERER MÀU CẢNH BÁO (cột Trạng thái)

    private static class CanhBaoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                Object trangThaiObj = table.getModel().getValueAt(row, 7);
                String trangThai = trangThaiObj != null ? trangThaiObj.toString() : "";
                if (trangThai.contains("het han")) {
                    c.setBackground(new Color(255, 204, 204));   // đỏ nhạt
                } else if (trangThai.contains("Sap het han")) {
                    c.setBackground(new Color(255, 230, 179));   // cam nhạt
                } else if (trangThai.contains("thap")) {
                    c.setBackground(new Color(255, 255, 179));   // vàng nhạt
                } else {
                    c.setBackground(Color.WHITE);
                }
            }
            return c;
        }
    }


    // MAIN — test độc lập

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuanLySP(null));
    }
}