package view;

import TruycapDL.TruycapKho;
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
 */
public class QuanLyKho extends JPanel {

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

    // ================================================================
    // CONSTRUCTOR
    // ================================================================
    public QuanLyKho() {
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
    }
}