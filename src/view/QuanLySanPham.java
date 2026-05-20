package view;

import TruycapDL.TruycapSP;
import model.Sanpham;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class QuanLySanPham extends JFrame {

    private JTextField txtMaSP, txtTenSP, txtSoLuong, txtGiaBan, txtNgayHetHan, txtSoLuongToiThieu, txtTimKiem;
    private JComboBox<String> cbLoaiSP, cbLocLoaiSP;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JTable tableSP;
    private DefaultTableModel tableModel;
    private TruycapSP truycapSP;

    private final String[] DANH_SACH_LOAI = {"Thực phẩm", "Đồ uống", "Hóa mỹ phẩm", "Đồ gia dụng", "Khác"};

    public QuanLySanPham() {
        truycapSP = new TruycapSP();
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Sản Phẩm - Kho Hàng");
        setSize(1150, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // =========================================================
        // 1. VÙNG FORM NHẬP LIỆU & ĐIỀU KHIỂN (NORTH)
        // =========================================================
        JPanel pnlNorth = new JPanel(new BorderLayout(5, 5));
        pnlNorth.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JPanel pnlForm = new JPanel(new GridLayout(3, 6, 10, 10));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết sản phẩm"));

        pnlForm.add(new JLabel("Mã Sản Phẩm:")); txtMaSP = new JTextField(); pnlForm.add(txtMaSP);
        pnlForm.add(new JLabel("Tên Sản Phẩm:")); txtTenSP = new JTextField(); pnlForm.add(txtTenSP);
        pnlForm.add(new JLabel("Loại Sản Phẩm:")); cbLoaiSP = new JComboBox<>(DANH_SACH_LOAI); pnlForm.add(cbLoaiSP);

        pnlForm.add(new JLabel("Số Lượng Tồn:")); txtSoLuong = new JTextField(); pnlForm.add(txtSoLuong);
        pnlForm.add(new JLabel("Giá Bán (VNĐ):")); txtGiaBan = new JTextField(); pnlForm.add(txtGiaBan);
        pnlForm.add(new JLabel("Ngưỡng Cảnh Báo Tồn:")); txtSoLuongToiThieu = new JTextField(); pnlForm.add(txtSoLuongToiThieu);

        pnlForm.add(new JLabel("Hạn Sử Dụng (yyyy-mm-dd):")); txtNgayHetHan = new JTextField(); pnlForm.add(txtNgayHetHan);
        pnlForm.add(new JLabel("")); pnlForm.add(new JLabel("")); // Giữ ô trống layout
        pnlForm.add(new JLabel("")); pnlForm.add(new JLabel(""));

        pnlNorth.add(pnlForm, BorderLayout.CENTER);

        // Thanh công cụ nút bấm và bộ lọc tìm kiếm
        JPanel pnlToolbar = new JPanel(new BorderLayout());
        
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnThem = new JButton("Thêm Sản Phẩm");
        btnSua = new JButton("Cập Nhật (Sửa)");
        btnXoa = new JButton("Xóa Sản Phẩm");
        btnLamMoi = new JButton("Làm Mới Form");
        pnlActions.add(btnThem); pnlActions.add(btnSua); pnlActions.add(btnXoa); pnlActions.add(btnLamMoi);

        JPanel pnlFilterSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        
        // Bộ lọc theo phân loại sản phẩm
        pnlFilterSearch.add(new JLabel("Lọc loại:"));
        cbLocLoaiSP = new JComboBox<>();
        cbLocLoaiSP.addItem("Tất cả");
        for (String loai : DANH_SACH_LOAI) cbLocLoaiSP.addItem(loai);
        pnlFilterSearch.add(cbLocLoaiSP);

        // Thanh tìm kiếm theo tên
        pnlFilterSearch.add(new JLabel("Tìm tên:"));
        txtTimKiem = new JTextField(12);
        btnTimKiem = new JButton("Tìm Kiếm");
        pnlFilterSearch.add(txtTimKiem);
        pnlFilterSearch.add(btnTimKiem);

        pnlToolbar.add(pnlActions, BorderLayout.WEST);
        pnlToolbar.add(pnlFilterSearch, BorderLayout.EAST);
        pnlNorth.add(pnlToolbar, BorderLayout.SOUTH);

        add(pnlNorth, BorderLayout.NORTH);

        // =========================================================
        // 2. VÙNG BẢNG HIỂN THỊ DỮ LIỆU (CENTER)
        // =========================================================
        String[] columns = {"Mã SP", "Tên Sản Phẩm", "Loại", "Số Lượng", "Giá Bán", "Hạn Sử Dụng", "Ngưỡng Cảnh Báo", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa trực tiếp trên ô của Table
            }
        };
        tableSP = new JTable(tableModel);
        setupTableRenderer(); // Kích hoạt chức năng tô màu cảnh báo
        
        JScrollPane scrollPane = new JScrollPane(tableSP);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm trong kho (Đỏ: Hết hạn | Vàng: Sắp hết hạn hoặc Tồn kho thấp)"));
        add(scrollPane, BorderLayout.CENTER);

        // =========================================================
        // XỬ LÝ SỰ KIỆN (LISTENERS)
        // =========================================================

        // Sự kiện đổ ngược dữ liệu từ dòng được click trên Table lên Form nhập liệu
        tableSP.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableSP.getSelectedRow();
                if (row >= 0) {
                    txtMaSP.setText(tableModel.getValueAt(row, 0).toString());
                    txtTenSP.setText(tableModel.getValueAt(row, 1).toString());
                    cbLoaiSP.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                    txtSoLuong.setText(tableModel.getValueAt(row, 3).toString());
                    txtGiaBan.setText(tableModel.getValueAt(row, 4).toString());
                    
                    Object ngayHetHanObj = tableModel.getValueAt(row, 5);
                    txtNgayHetHan.setText(ngayHetHanObj != null ? ngayHetHanObj.toString() : "");
                    
                    txtSoLuongToiThieu.setText(tableModel.getValueAt(row, 6).toString());
                    
                    txtMaSP.setEditable(false); // Khóa không cho sửa Mã sản phẩm
                }
            }
        });

        // Xử lý nút Thêm sản phẩm
        btnThem.addActionListener(e -> {
            String maSP = txtMaSP.getText().trim();
            if (maSP.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mã sản phẩm không được để trống!"); return;
            }
            if (truycapSP.getSanphamByMa(maSP) != null) {
                JOptionPane.showMessageDialog(this, "Mã sản phẩm này đã tồn tại!"); return;
            }

            Sanpham sp = getSanphamFromForm();
            if (sp != null) {
                if (truycapSP.themSanpham(sp)) {
                    JOptionPane.showMessageDialog(this, "Thêm sản phẩm vào kho thành công!");
                    clearForm();
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại. Vui lòng kiểm tra lại dữ liệu!");
                }
            }
        });

        // Xử lý nút Sửa thông tin sản phẩm
        btnSua.addActionListener(e -> {
            String maSP = txtMaSP.getText().trim();
            if (maSP.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm trong danh sách để sửa!"); return;
            }

            Sanpham sp = getSanphamFromForm();
            if (sp != null) {
                if (truycapSP.suaSanpham(sp)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thông tin sản phẩm thành công!");
                    clearForm();
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }
            }
        });

        // Xử lý nút Xóa sản phẩm
        btnXoa.addActionListener(e -> {
            String maSP = txtMaSP.getText().trim();
            if (maSP.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa dữ liệu!"); return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn chắc chắn muốn xóa vĩnh viễn sản phẩm này khỏi hệ thống?", 
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (truycapSP.xoaSanpham(maSP)) {
                    JOptionPane.showMessageDialog(this, "Đã gỡ bỏ sản phẩm thành công!");
                    clearForm();
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại! Sản phẩm này có liên quan đến hóa đơn hoặc phiếu nhập kho.");
                }
            }
        });

        // Xử lý nút Làm Mới Form nhập liệu
        btnLamMoi.addActionListener(e -> {
            clearForm();
            cbLocLoaiSP.setSelectedIndex(0);
            loadDataToTable();
        });

        // Xử lý nút Tìm Kiếm Theo Tên Sản Phẩm
        btnTimKiem.addActionListener(e -> {
            String keyword = txtTimKiem.getText().trim();
            if (keyword.isEmpty()) {
                loadDataToTable();
            } else {
                fillTable(truycapSP.timTheoTen(keyword));
            }
        });

        // Sự kiện lọc danh sách theo Loại sản phẩm khi JComboBox thay đổi
        cbLocLoaiSP.addActionListener(e -> {
            String selectedLoai = cbLocLoaiSP.getSelectedItem().toString();
            if ("Tất cả".equals(selectedLoai)) {
                loadDataToTable();
            } else {
                fillTable(truycapSP.timTheoLoai(selectedLoai));
            }
        });
    }

    // ==== CÁC HÀM TRỢ GIÚP (HELPER METHODS) ====

    private void loadDataToTable() {
        fillTable(truycapSP.getAllSanpham());
    }

    private void fillTable(List<Sanpham> list) {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ trên bảng
        for (Sanpham sp : list) {
            // Xác định chuỗi hiển thị Trạng thái dựa trên các hàm tiện ích của model
            String trangThai = "Bình thường";
            if (sp.isHetHan()) {
                trangThai = "Đã hết hạn";
            } else if (sp.isSapHetHan() && sp.isTonKhoThap()) {
                trangThai = "Sắp hết hạn & Tồn thấp";
            } else if (sp.isSapHetHan()) {
                trangThai = "Sắp hết hạn (<30 ngày)";
            } else if (sp.isTonKhoThap()) {
                trangThai = "Tồn kho thấp!";
            }

            tableModel.addRow(new Object[]{
                    sp.getMaSP(),
                    sp.getTenSP(),
                    sp.getLoaiSP(),
                    sp.getSoLuong(),
                    sp.getGiaBan(),
                    sp.getNgayHetHan() != null ? sp.getNgayHetHan() : "Không có",
                    sp.getSoLuongToiThieu(),
                    trangThai
            });
        }
    }

    private Sanpham getSanphamFromForm() {
        String maSP = txtMaSP.getText().trim();
        String tenSP = txtTenSP.getText().trim();
        String loaiSP = cbLoaiSP.getSelectedItem().toString();
        String soLuongStr = txtSoLuong.getText().trim();
        String giaBanStr = txtGiaBan.getText().trim();
        String ngayHetHanStr = txtNgayHetHan.getText().trim();
        String toithieuStr = txtSoLuongToiThieu.getText().trim();

        if (tenSP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được bỏ trống!");
            return null;
        }

        int soLuong = 0;
        double giaBan = 0;
        int soLuongToiThieu = 0;

        try {
            if (!soLuongStr.isEmpty()) soLuong = Integer.parseInt(soLuongStr);
            if (!giaBanStr.isEmpty()) giaBan = Double.parseDouble(giaBanStr);
            if (!toithieuStr.isEmpty()) soLuongToiThieu = Integer.parseInt(toithieuStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng, Giá bán và Ngưỡng cảnh báo phải nhập định dạng số!");
            return null;
        }

        LocalDate ngayHetHan = null;
        if (!ngayHetHanStr.isEmpty() && !"Không có".equals(ngayHetHanStr)) {
            try {
                ngayHetHan = LocalDate.parse(ngayHetHanStr);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Hạn sử dụng sai định dạng cấu trúc (Yêu cầu nhập: yyyy-mm-dd, ví dụ: 2026-12-31)!");
                return null;
            }
        }

        return new Sanpham(maSP, tenSP, soLuong, giaBan, loaiSP, ngayHetHan, soLuongToiThieu);
    }

    private void clearForm() {
        txtMaSP.setText("");
        txtTenSP.setText("");
        cbLoaiSP.setSelectedIndex(0);
        txtSoLuong.setText("");
        txtGiaBan.setText("");
        txtNgayHetHan.setText("");
        txtSoLuongToiThieu.setText("");
        txtTimKiem.setText("");
        txtMaSP.setEditable(true);
        tableSP.clearSelection();
    }

    /**
     * Hàm cấu hình JTable để tự động đổi màu nền các hàng dựa trên cột Trạng Thái
     */
    private void setupTableRenderer() {
        tableSP.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Lấy chuỗi trạng thái ở cột thứ 7 (chỉ số index bắt đầu từ 0)
                String trangThai = table.getModel().getValueAt(row, 7).toString();
                
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                } else {
                    c.setForeground(Color.BLACK);
                    if (trangThai.contains("Đã hết hạn")) {
                        c.setBackground(new Color(255, 204, 204)); // Đỏ hồng nhạt nguy cấp
                    } else if (trangThai.contains("Sắp hết hạn") || trangThai.contains("Tồn kho thấp")) {
                        c.setBackground(new Color(255, 255, 204)); // Vàng chanh nhạt cảnh báo
                    } else {
                        c.setBackground(Color.WHITE); // Trạng thái bình thường
                    }
                }
                return c;
            }
        });
    }

    public static void main(String[] args) {
        // Thiết lập giao diện mẫu Nimbus cho hiện đại hơn mặc định
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) { /** Fallback template */ }

        SwingUtilities.invokeLater(() -> new QuanLySanPham().setVisible(true));
    }
}