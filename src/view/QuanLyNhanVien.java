package view;

import TruycapDL.TruycapNV;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List; 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Nhanvien;

public class QuanLyNhanVien extends JFrame {

    private JTextField txtMaNV, txtHoTen, txtSDT, txtDiaChi, txtLuong,
                       txtEmail, txtNgaySinh, txtTaikhoan, txtMatkhau, txtTimKiem;
    private JComboBox<String> cmbGioitinh, cmbVaitro;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JTable tableNV;
    private DefaultTableModel tableModel;
    private TruycapNV truycapNV;

    public QuanLyNhanVien() {
        truycapNV = new TruycapNV();
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Nhân Viên");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // =========================================================
        // 1. VÙNG NHẬP LIỆU (NORTH)
        // =========================================================
        JPanel pnlNorth = new JPanel(new BorderLayout(5, 5));
        pnlNorth.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        // Form nhập liệu – 6 hàng x 4 cột
        JPanel pnlForm = new JPanel(new GridLayout(6, 4, 10, 8));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));

        // Hàng 1
        pnlForm.add(new JLabel("Mã Nhân Viên:"));
        txtMaNV = new JTextField();
        pnlForm.add(txtMaNV);

        pnlForm.add(new JLabel("Họ và Tên:"));
        txtHoTen = new JTextField();
        pnlForm.add(txtHoTen);

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

        pnlForm.add(new JLabel("Lương:"));
        txtLuong = new JTextField();
        pnlForm.add(txtLuong);

        // Hàng 4
        pnlForm.add(new JLabel("Ngày Sinh (yyyy-mm-dd):"));
        txtNgaySinh = new JTextField();
        pnlForm.add(txtNgaySinh);

        pnlForm.add(new JLabel("Giới Tính:"));
        cmbGioitinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        pnlForm.add(cmbGioitinh);

        // Hàng 5
        pnlForm.add(new JLabel("Tài Khoản:"));
        txtTaikhoan = new JTextField();
        pnlForm.add(txtTaikhoan);

        pnlForm.add(new JLabel("Mật Khẩu:"));
        txtMatkhau = new JPasswordField();
        pnlForm.add(txtMatkhau);

        // Hàng 6
        pnlForm.add(new JLabel("Vai Trò:"));
        cmbVaitro = new JComboBox<>(new String[]{"nhanvien", "admin", "quanly"});
        pnlForm.add(cmbVaitro);

        // Ô trống để cân bằng GridLayout
        pnlForm.add(new JLabel(""));
        pnlForm.add(new JLabel(""));

        pnlNorth.add(pnlForm, BorderLayout.CENTER);

        // Thanh công cụ nút bấm và tìm kiếm
        JPanel pnlToolbar = new JPanel(new BorderLayout());

        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnThem    = new JButton("Thêm NV");
        btnSua     = new JButton("Sửa Thông Tin");
        btnXoa     = new JButton("Xóa NV");
        btnLamMoi  = new JButton("Làm Mới");
        pnlActions.add(btnThem);
        pnlActions.add(btnSua);
        pnlActions.add(btnXoa);
        pnlActions.add(btnLamMoi);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        txtTimKiem = new JTextField(15);
        btnTimKiem = new JButton("Tìm Kiếm");
        pnlSearch.add(new JLabel("Nhập tên NV:"));
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTimKiem);

        pnlToolbar.add(pnlActions, BorderLayout.WEST);
        pnlToolbar.add(pnlSearch, BorderLayout.EAST);
        pnlNorth.add(pnlToolbar, BorderLayout.SOUTH);

        add(pnlNorth, BorderLayout.NORTH);

        // =========================================================
        // 2. VÙNG BẢNG HIỂN THỊ (CENTER)
        // =========================================================
        String[] columns = {"Mã NV", "Họ và Tên", "SĐT", "Email", "Địa Chỉ",
                             "Lương", "Ngày Sinh", "Giới Tính", "Tài Khoản", "Vai Trò"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // Không cho sửa trực tiếp trên bảng
            }
        };
        tableNV = new JTable(tableModel);
        tableNV.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableNV.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tableNV);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách nhân viên"));
        add(scrollPane, BorderLayout.CENTER);

        // =========================================================
        // XỬ LÝ SỰ KIỆN (LISTENERS)
        // =========================================================

        // Click dòng bảng → đổ dữ liệu ngược lại Form
        tableNV.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableNV.getSelectedRow();
                if (row < 0) return;

                txtMaNV.setText(tableModel.getValueAt(row, 0).toString());
                txtHoTen.setText(tableModel.getValueAt(row, 1).toString());
                txtSDT.setText(tableModel.getValueAt(row, 2).toString());
                txtEmail.setText(tableModel.getValueAt(row, 3) != null ? tableModel.getValueAt(row, 3).toString() : "");
                txtDiaChi.setText(tableModel.getValueAt(row, 4) != null ? tableModel.getValueAt(row, 4).toString() : "");
                txtLuong.setText(tableModel.getValueAt(row, 5) != null ? tableModel.getValueAt(row, 5).toString() : "");
                txtNgaySinh.setText(tableModel.getValueAt(row, 6) != null ? tableModel.getValueAt(row, 6).toString() : "");

                String gioitinh = tableModel.getValueAt(row, 7) != null ? tableModel.getValueAt(row, 7).toString() : "Nam";
                cmbGioitinh.setSelectedItem(gioitinh);

                txtTaikhoan.setText(tableModel.getValueAt(row, 8) != null ? tableModel.getValueAt(row, 8).toString() : "");
                txtMatkhau.setText(""); // Không hiển thị mật khẩu từ bảng vì lý do bảo mật

                String vaitro = tableModel.getValueAt(row, 9) != null ? tableModel.getValueAt(row, 9).toString() : "nhanvien";
                cmbVaitro.setSelectedItem(vaitro);

                // Khóa Mã NV (Primary Key) khi đang chọn để sửa
                txtMaNV.setEditable(false);
            }
        });

        // Nút Thêm Nhân Viên
        btnThem.addActionListener(e -> {
            String maNV = txtMaNV.getText().trim();
            if (maNV.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mã nhân viên không được để trống!");
                return;
            }
            if (truycapNV.getNhanvienByMa(maNV) != null) {
                JOptionPane.showMessageDialog(this, "Mã nhân viên này đã tồn tại trên hệ thống!");
                return;
            }

            Nhanvien nv = getNhanvienFromForm();
            if (nv != null) {
                if (truycapNV.themNhanvien(nv)) {
                    JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                    clearForm();
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại. Vui lòng kiểm tra lại dữ liệu!");
                }
            }
        });

        // Nút Sửa Nhân Viên
        btnSua.addActionListener(e -> {
            String maNV = txtMaNV.getText().trim();
            if (maNV.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa thông tin!");
                return;
            }

            Nhanvien nv = getNhanvienFromForm();
            if (nv != null) {
                if (truycapNV.suaNhanvien(nv)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
                    clearForm();
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }
            }
        });

        // Nút Xóa Nhân Viên
        btnXoa.addActionListener(e -> {
            String maNV = txtMaNV.getText().trim();
            if (maNV.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa nhân viên này?\n(Lưu ý: Nếu nhân viên đã có dữ liệu liên quan, việc xóa có thể bị lỗi liên kết)",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (truycapNV.xoaNhanvien(maNV)) {
                    JOptionPane.showMessageDialog(this, "Đã xóa nhân viên thành công!");
                    clearForm();
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại! Nhân viên này có thể đang liên kết với dữ liệu khác.");
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
                List<Nhanvien> ketQua = truycapNV.timNhanvienTheoTen(keyword);
                fillTable(ketQua);
            }
        });
    }

    // ==== CÁC HÀM TRỢ GIÚP (HELPER METHODS) ====

    // Đổ dữ liệu từ Database lên Table
    private void loadDataToTable() {
        fillTable(truycapNV.getAllNhanvien());
    }

    // Nạp danh sách nhân viên vào Table Model
    private void fillTable(List<Nhanvien> list) {
        tableModel.setRowCount(0);
        for (Nhanvien nv : list) {
            tableModel.addRow(new Object[]{
                    nv.getMaNV(),
                    nv.gethoten(),
                    nv.getSDT(),
                    nv.getEmail(),
                    nv.getdiachi(),
                    nv.getLuong(),
                    nv.getNgaysinh(),
                    nv.getGioitinh(),
                    nv.getTaikhoan(),
                    nv.getVaitro()
            });
        }
    }

    // Đọc dữ liệu từ Form, tạo đối tượng Nhanvien
    private Nhanvien getNhanvienFromForm() {
        String maNV    = txtMaNV.getText().trim();
        String hoTen   = txtHoTen.getText().trim();
        String sdt     = txtSDT.getText().trim();
        String diaChi  = txtDiaChi.getText().trim();
        String luongStr = txtLuong.getText().trim();
        String email   = txtEmail.getText().trim();
        String ngaySinhStr = txtNgaySinh.getText().trim();
        String gioitinh = (String) cmbGioitinh.getSelectedItem();
        String taikhoan = txtTaikhoan.getText().trim();
        String matkhau  = new String(((JPasswordField) txtMatkhau).getPassword()).trim();
        String vaitro   = (String) cmbVaitro.getSelectedItem();

        if (hoTen.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên và Số điện thoại không được để trống!");
            return null;
        }

        if (taikhoan.isEmpty() || matkhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tài khoản và Mật khẩu không được để trống!");
            return null;
        }

        double luong = 0;
        if (!luongStr.isEmpty()) {
            try {
                luong = Double.parseDouble(luongStr);
                if (luong < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Lương phải là số hợp lệ và không âm!");
                return null;
            }
        }

        LocalDate ngaySinh = null;
        if (!ngaySinhStr.isEmpty()) {
            try {
                ngaySinh = LocalDate.parse(ngaySinhStr);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Ngày sinh không đúng định dạng (yyyy-mm-dd, ví dụ: 1995-10-25)!");
                return null;
            }
        }

        return new Nhanvien(maNV, hoTen, sdt, diaChi, luong, email,
                            ngaySinh, gioitinh, taikhoan, matkhau, vaitro);
    }

    // Reset sạch tất cả trường trên Form
    private void clearForm() {
        txtMaNV.setText("");
        txtHoTen.setText("");
        txtSDT.setText("");
        txtDiaChi.setText("");
        txtLuong.setText("");
        txtEmail.setText("");
        txtNgaySinh.setText("");
        txtTaikhoan.setText("");
        ((JPasswordField) txtMatkhau).setText("");
        txtTimKiem.setText("");
        cmbGioitinh.setSelectedIndex(0);
        cmbVaitro.setSelectedIndex(0);
        txtMaNV.setEditable(true);
        tableNV.clearSelection();
    }

    // Khởi chạy độc lập để test
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) { /* Default fallback */ }

        SwingUtilities.invokeLater(() -> new QuanLyNhanVien().setVisible(true));
    }
}