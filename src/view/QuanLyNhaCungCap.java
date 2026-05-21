package view;

import TruycapDL.TruycapNhaCungCap;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.NhaCungCap;

public class QuanLyNhaCungCap extends JFrame {

    private JTextField txtMaNCC, txtTenNCC, txtSDT, txtDiaChi, txtEmail, txtTimKiem;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JTable tableNCC;
    private DefaultTableModel tableModel;
    private TruycapNhaCungCap truycapNCC;

    public QuanLyNhaCungCap() {
        truycapNCC = new TruycapNhaCungCap();
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Nhà Cung Cấp");
        setSize(900, 580);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. VÙNG NHẬP LIỆU (NORTH)
        JPanel pnlNorth = new JPanel(new BorderLayout(5, 5));
        pnlNorth.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JPanel pnlForm = new JPanel(new GridLayout(3, 4, 10, 8));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin nhà cung cấp"));

        // Hàng 1
        pnlForm.add(new JLabel("Mã Nhà Cung Cấp:"));
        txtMaNCC = new JTextField();
        pnlForm.add(txtMaNCC);

        pnlForm.add(new JLabel("Tên Nhà Cung Cấp:"));
        txtTenNCC = new JTextField();
        pnlForm.add(txtTenNCC);

        // Hàng 2
        pnlForm.add(new JLabel("Số Điện Thoại:"));
        txtSDT = new JTextField();
        pnlForm.add(txtSDT);

        pnlForm.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        pnlForm.add(txtEmail);

        // Hàng 3
        pnlForm.add(new JLabel("Địa Chỉ:"));
        txtDiaChi = new JTextField();
        pnlForm.add(txtDiaChi);

        // Ô trống để cân bằng GridLayout
        pnlForm.add(new JLabel(""));
        pnlForm.add(new JLabel(""));

        pnlNorth.add(pnlForm, BorderLayout.CENTER);

        // Thanh công cụ nút bấm và tìm kiếm
        JPanel pnlToolbar = new JPanel(new BorderLayout());

        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnThem   = new JButton("Thêm NCC");
        btnSua    = new JButton("Sửa Thông Tin");
        btnXoa    = new JButton("Xóa NCC");
        btnLamMoi = new JButton("Làm Mới");
        pnlActions.add(btnThem);
        pnlActions.add(btnSua);
        pnlActions.add(btnXoa);
        pnlActions.add(btnLamMoi);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        txtTimKiem = new JTextField(15);
        btnTimKiem = new JButton("Tìm Kiếm");
        pnlSearch.add(new JLabel("Nhập tên NCC:"));
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTimKiem);

        pnlToolbar.add(pnlActions, BorderLayout.WEST);
        pnlToolbar.add(pnlSearch, BorderLayout.EAST);
        pnlNorth.add(pnlToolbar, BorderLayout.SOUTH);

        add(pnlNorth, BorderLayout.NORTH);

        // =========================================================
        // 2. VÙNG BẢNG HIỂN THỊ (CENTER)
        // =========================================================
        String[] columns = {"Mã NCC", "Tên Nhà Cung Cấp", "Số Điện Thoại", "Email", "Địa Chỉ"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tableNCC = new JTable(tableModel);
        tableNCC.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableNCC.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tableNCC);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách nhà cung cấp"));
        add(scrollPane, BorderLayout.CENTER);

        // =========================================================
        // XỬ LÝ SỰ KIỆN (LISTENERS)
        // =========================================================

        // Click dòng bảng → đổ dữ liệu ngược lại Form
        tableNCC.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableNCC.getSelectedRow();
                if (row < 0) return;

                txtMaNCC.setText(tableModel.getValueAt(row, 0).toString());
                txtTenNCC.setText(tableModel.getValueAt(row, 1).toString());
                txtSDT.setText(tableModel.getValueAt(row, 2) != null ? tableModel.getValueAt(row, 2).toString() : "");
                txtEmail.setText(tableModel.getValueAt(row, 3) != null ? tableModel.getValueAt(row, 3).toString() : "");
                txtDiaChi.setText(tableModel.getValueAt(row, 4) != null ? tableModel.getValueAt(row, 4).toString() : "");

                // Khóa Mã NCC (Primary Key) khi đang chỉnh sửa
                txtMaNCC.setEditable(false);
            }
        });

        // Nút Thêm Nhà Cung Cấp
        btnThem.addActionListener(e -> {
            String maNCC = txtMaNCC.getText().trim();
            if (maNCC.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mã nhà cung cấp không được để trống!");
                return;
            }
            if (truycapNCC.getNhaCungCapByMa(maNCC) != null) {
                JOptionPane.showMessageDialog(this, "Mã nhà cung cấp này đã tồn tại trên hệ thống!");
                return;
            }

            NhaCungCap ncc = getNhaCungCapFromForm();
            if (ncc != null) {
                if (truycapNCC.themNhaCungCap(ncc)) {
                    JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thành công!");
                    clearForm();
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại. Vui lòng kiểm tra lại dữ liệu!");
                }
            }
        });

        // Nút Sửa Nhà Cung Cấp
        btnSua.addActionListener(e -> {
            String maNCC = txtMaNCC.getText().trim();
            if (maNCC.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp cần sửa thông tin!");
                return;
            }

            NhaCungCap ncc = getNhaCungCapFromForm();
            if (ncc != null) {
                if (truycapNCC.suaNhaCungCap(ncc)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
                    clearForm();
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }
            }
        });

        // Nút Xóa Nhà Cung Cấp
        btnXoa.addActionListener(e -> {
            String maNCC = txtMaNCC.getText().trim();
            if (maNCC.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp cần xóa!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa nhà cung cấp này?\n(Lưu ý: Nếu nhà cung cấp đang liên kết với sản phẩm, việc xóa có thể bị lỗi)",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (truycapNCC.xoaNhaCungCap(maNCC)) {
                    JOptionPane.showMessageDialog(this, "Đã xóa nhà cung cấp thành công!");
                    clearForm();
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại! Nhà cung cấp này có thể đang liên kết với dữ liệu khác.");
                }
            }
        });

        // Nút Làm Mới Form
        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadDataToTable();
        });

        // Nút Tìm Kiếm Theo Tên
        btnTimKiem.addActionListener(e -> {
            String keyword = txtTimKiem.getText().trim();
            if (keyword.isEmpty()) {
                loadDataToTable();
            } else {
                List<NhaCungCap> ketQua = truycapNCC.timTheoTen(keyword);
                fillTable(ketQua);
            }
        });
    }

    // ==== CÁC HÀM TRỢ GIÚP (HELPER METHODS) ====

    private void loadDataToTable() {
        fillTable(truycapNCC.getAllNhaCungCap());
    }

    private void fillTable(List<NhaCungCap> list) {
        tableModel.setRowCount(0);
        for (NhaCungCap ncc : list) {
            tableModel.addRow(new Object[]{
                    ncc.getMaNCC(),
                    ncc.getTenNCC(),
                    ncc.getSoDienThoai(),
                    ncc.getEmail(),
                    ncc.getDiaChi()
            });
        }
    }

    private NhaCungCap getNhaCungCapFromForm() {
        String maNCC  = txtMaNCC.getText().trim();
        String tenNCC = txtTenNCC.getText().trim();
        String sdt    = txtSDT.getText().trim();
        String email  = txtEmail.getText().trim();
        String diaChi = txtDiaChi.getText().trim();

        if (tenNCC.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhà cung cấp không được để trống!");
            return null;
        }
        if (sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống!");
            return null;
        }

        return new NhaCungCap(maNCC, tenNCC, sdt, diaChi, email);
    }

    private void clearForm() {
        txtMaNCC.setText("");
        txtTenNCC.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtTimKiem.setText("");
        txtMaNCC.setEditable(true);
        tableNCC.clearSelection();
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

        SwingUtilities.invokeLater(() -> new QuanLyNhaCungCap().setVisible(true));
    }
}