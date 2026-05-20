package view;

import TruycapDL.TruycapKH;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.KhachHang;

public class QuanLyKhachHang extends JFrame {

    private JTextField txtMaKH, txtHoTen, txtSDT, txtNgaySinh, txtDiaChi, txtTimKiem;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JTable tableKH;
    private DefaultTableModel tableModel;
    private TruycapKH truycapKH;

    public QuanLyKhachHang() {
        truycapKH = new TruycapKH();
        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Khách Hàng");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Tắt cửa sổ này không làm tắt toàn bộ app chính
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));


        // 1. VÙNG NHẬP LIỆU (NORTH)
      
        JPanel pnlNorth = new JPanel(new BorderLayout(5, 5));
        pnlNorth.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JPanel pnlForm = new JPanel(new GridLayout(3, 4, 10, 10));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));

        pnlForm.add(new JLabel("Mã Khách Hàng:"));   txtMaKH = new JTextField(); pnlForm.add(txtMaKH);
        pnlForm.add(new JLabel("Họ và Tên:"));      txtHoTen = new JTextField(); pnlForm.add(txtHoTen);
        pnlForm.add(new JLabel("Số Điện Thoại:"));   txtSDT = new JTextField(); pnlForm.add(txtSDT);
        pnlForm.add(new JLabel("Ngày Sinh (yyyy-mm-dd):")); txtNgaySinh = new JTextField(); pnlForm.add(txtNgaySinh);
        pnlForm.add(new JLabel("Địa Chỉ:"));        txtDiaChi = new JTextField(); pnlForm.add(txtDiaChi);
        
        // Thêm ô trống để giữ layout GridLayout cân đối
        pnlForm.add(new JLabel("")); pnlForm.add(new JLabel(""));

        pnlNorth.add(pnlForm, BorderLayout.CENTER);

        // Thanh công cụ nút bấm và tìm kiếm
        JPanel pnlToolbar = new JPanel(new BorderLayout());
        
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnThem = new JButton("Thêm Khách");
        btnSua = new JButton("Sửa Thông Tin");
        btnXoa = new JButton("Xóa Khách");
        btnLamMoi = new JButton("Làm Mới");
        pnlActions.add(btnThem); pnlActions.add(btnSua); pnlActions.add(btnXoa); pnlActions.add(btnLamMoi);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        txtTimKiem = new JTextField(15);
        btnTimKiem = new JButton("Tìm Kiếm");
        pnlSearch.add(new JLabel("Nhập tên KH:"));
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTimKiem);

        pnlToolbar.add(pnlActions, BorderLayout.WEST);
        pnlToolbar.add(pnlSearch, BorderLayout.EAST);
        pnlNorth.add(pnlToolbar, BorderLayout.SOUTH);

        add(pnlNorth, BorderLayout.NORTH);

        // =========================================================
        // 2. VÙNG BẢNG HIỂN THỊ (CENTER)
        // =========================================================
        String[] columns = {"Mã KH", "Họ và Tên", "Số Điện Thoại", "Ngày Sinh", "Địa Chỉ"};
        tableModel = new DefaultTableModel(columns, 0);
        tableKH = new JTable(tableModel);
        
        JScrollPane scrollPane = new JScrollPane(tableKH);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách khách hàng"));
        add(scrollPane, BorderLayout.CENTER);

        // =========================================================
        // XỬ LÝ SỰ KIỆN (LISTENERS)
        // =========================================================

        // Sự kiện click dòng trên Table đổ dữ liệu ngược lại Form
        tableKH.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableKH.getSelectedRow();
                if (row >= 0) {
                    txtMaKH.setText(tableModel.getValueAt(row, 0).toString());
                    txtHoTen.setText(tableModel.getValueAt(row, 1).toString());
                    txtSDT.setText(tableModel.getValueAt(row, 2).toString());
                    
                    Object ngaySinhObj = tableModel.getValueAt(row, 3);
                    txtNgaySinh.setText(ngaySinhObj != null ? ngaySinhObj.toString() : "");
                    
                    Object diaChiObj = tableModel.getValueAt(row, 4);
                    txtDiaChi.setText(diaChiObj != null ? diaChiObj.toString() : "");

                    // Khóa không cho sửa Mã KH (Primary Key) khi đang chỉnh sửa
                    txtMaKH.setEditable(false);
                }
            }
        });

        // Nút Thêm Khách Hàng
        btnThem.addActionListener(e -> {
            String maKH = txtMaKH.getText().trim();
            if (maKH.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mã khách hàng không được để trống!");
                return;
            }
            if (truycapKH.getKhachHangByMa(maKH) != null) {
                JOptionPane.showMessageDialog(this, "Mã khách hàng này đã tồn tại trên hệ thống!");
                return;
            }

            KhachHang kh = getKhachHangFromForm();
            if (kh != null) {
                if (truycapKH.themKhachHang(kh)) {
                    JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                    clearForm();
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại. Vui lòng kiểm tra lại dữ liệu!");
                }
            }
        });

        // Nút Sửa Khách Hàng
        btnSua.addActionListener(e -> {
            String maKH = txtMaKH.getText().trim();
            if (maKH.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa thông tin!");
                return;
            }

            KhachHang kh = getKhachHangFromForm();
            if (kh != null) {
                if (truycapKH.suaKhachHang(kh)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
                    clearForm();
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }
            }
        });

        // Nút Xóa Khách Hàng
        btnXoa.addActionListener(e -> {
            String maKH = txtMaKH.getText().trim();
            if (maKH.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn có chắc chắn muốn xóa khách hàng này?\n(Lưu ý: Nếu khách hàng đã có hóa đơn, việc xóa có thể bị lỗi liên kết dữ liệu)", 
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (truycapKH.xoaKhachHang(maKH)) {
                    JOptionPane.showMessageDialog(this, "Đã xóa khách hàng thành công!");
                    clearForm();
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại! Khách hàng này có thể đang tồn tại trong các hóa đơn cũ.");
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
                List<KhachHang> ketQua = truycapKH.timKhachHangTheoTen(keyword);
                fillTable(ketQua);
            }
        });
    }

    // ==== CÁC HÀM TRỢ GIÚP (HELPER METHODS) ====

    // Đổ dữ liệu từ Database lên Table
    private void loadDataToTable() {
        fillTable(truycapKH.getAllKhachHang());
    }

    // Nạp danh sách khách hàng vào Table Model
    private void fillTable(List<KhachHang> list) {
        tableModel.setRowCount(0); // Xóa sạch dữ liệu cũ trên bảng
        for (KhachHang kh : list) {
            tableModel.addRow(new Object[]{
                    kh.getMaKH(),
                    kh.getHoten(),
                    kh.getSDT(),
                    kh.getNgaysinh(),
                    kh.getDiachi()
            });
        }
    }

    // Đọc dữ liệu từ các ô nhập trên Form tạo ra đối tượng KhachHang
    private KhachHang getKhachHangFromForm() {
        String maKH = txtMaKH.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String sdt = txtSDT.getText().trim();
        String ngaySinhStr = txtNgaySinh.getText().trim();
        String diaChi = txtDiaChi.getText().trim();

        if (hoTen.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên và Số điện thoại không được để trống!");
            return null;
        }

        LocalDate ngaySinh = null;
        if (!ngaySinhStr.isEmpty()) {
            try {
                ngaySinh = LocalDate.parse(ngaySinhStr);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Ngày sinh không đúng định dạng (Năm-tháng-ngày, ví dụ: 1995-10-25)!");
                return null;
            }
        }

        return new KhachHang(maKH, hoTen, sdt, ngaySinh, diaChi);
    }

    // Reset sạch các trường dữ liệu nhập trên form
    private void clearForm() {
        txtMaKH.setText("");
        txtHoTen.setText("");
        txtSDT.setText("");
        txtNgaySinh.setText("");
        txtDiaChi.setText("");
        txtTimKiem.setText("");
        txtMaKH.setEditable(true);
        tableKH.clearSelection();
    }

    // Khởi chạy độc lập màn hình quản lý khách hàng để test
    public static void main(String[] args) {
        // Cài đặt giao diện hệ thống nhìn mượt hơn (Nimbus Look and Feel)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) { /** Default fallback */ }

        SwingUtilities.invokeLater(() -> new QuanLyKhachHang().setVisible(true));
    }
}