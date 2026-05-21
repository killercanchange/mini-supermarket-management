package view;

import TruycapDL.TruycapHoaDon;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.HoaDon;

public class QuanLyHoaDon extends JFrame {

    private JTextField txtMaHD, txtMaSP, txtTenSP, txtSoLuong,
                       txtGiaBan, txtTongTien, txtNgayLap, txtTimKiem;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimKiem;
    private JLabel lblDoanhThuNgay, lblDoanhThuThang;
    private JTable tableHD;
    private DefaultTableModel tableModel;
    private TruycapHoaDon truycapHD;

    private static final NumberFormat CURRENCY_FORMAT =
            NumberFormat.getNumberInstance(new Locale("vi", "VN"));

    public QuanLyHoaDon() {
        truycapHD = new TruycapHoaDon();
        initComponents();
        loadDataToTable();
        updateDoanhThuLabels();
    }

    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Hóa Đơn");
        setSize(1050, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // =========================================================
        // 1. VÙNG NHẬP LIỆU (NORTH)
        // =========================================================
        JPanel pnlNorth = new JPanel(new BorderLayout(5, 5));
        pnlNorth.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        // Form nhập liệu – 3 hàng x 4 cột
        JPanel pnlForm = new JPanel(new GridLayout(3, 4, 10, 8));
        pnlForm.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));

        // Hàng 1
        pnlForm.add(new JLabel("Mã Hóa Đơn:"));
        txtMaHD = new JTextField();
        pnlForm.add(txtMaHD);

        pnlForm.add(new JLabel("Mã Sản Phẩm:"));
        txtMaSP = new JTextField();
        pnlForm.add(txtMaSP);

        // Hàng 2
        pnlForm.add(new JLabel("Tên Sản Phẩm:"));
        txtTenSP = new JTextField();
        pnlForm.add(txtTenSP);

        pnlForm.add(new JLabel("Số Lượng Mua:"));
        txtSoLuong = new JTextField();
        pnlForm.add(txtSoLuong);

        // Hàng 3
        pnlForm.add(new JLabel("Giá Bán (VNĐ):"));
        txtGiaBan = new JTextField();
        pnlForm.add(txtGiaBan);

        pnlForm.add(new JLabel("Tổng Tiền (tự tính):"));
        txtTongTien = new JTextField();
        txtTongTien.setEditable(false);
        txtTongTien.setBackground(new Color(240, 240, 240));
        pnlForm.add(txtTongTien);

        pnlNorth.add(pnlForm, BorderLayout.CENTER);

        // Tự động tính Tổng Tiền khi nhập Số Lượng hoặc Giá Bán
        txtSoLuong.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { tinhTongTien(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { tinhTongTien(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { tinhTongTien(); }
        });
        txtGiaBan.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { tinhTongTien(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { tinhTongTien(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { tinhTongTien(); }
        });

        // Thanh công cụ: nút bấm + tìm kiếm theo ngày + thống kê doanh thu
        JPanel pnlToolbar = new JPanel(new BorderLayout(5, 5));

        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnThem   = new JButton("Thêm HĐ");
        btnSua    = new JButton("Sửa HĐ");
        btnXoa    = new JButton("Xóa HĐ");
        btnLamMoi = new JButton("Làm Mới");
        pnlActions.add(btnThem);
        pnlActions.add(btnSua);
        pnlActions.add(btnXoa);
        pnlActions.add(btnLamMoi);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        txtTimKiem = new JTextField(12);
        btnTimKiem = new JButton("Lọc Theo Ngày");
        pnlSearch.add(new JLabel("Ngày (yyyy-mm-dd):"));
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTimKiem);

        pnlToolbar.add(pnlActions, BorderLayout.WEST);
        pnlToolbar.add(pnlSearch, BorderLayout.EAST);
        pnlNorth.add(pnlToolbar, BorderLayout.SOUTH);

        add(pnlNorth, BorderLayout.NORTH);

        // =========================================================
        // 2. VÙNG BẢNG HIỂN THỊ (CENTER)
        // =========================================================
        String[] columns = {"Mã HĐ", "Mã SP", "Tên Sản Phẩm", "Số Lượng", "Giá Bán", "Tổng Tiền", "Ngày Lập"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tableHD = new JTable(tableModel);
        tableHD.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableHD.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tableHD);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách hóa đơn"));
        add(scrollPane, BorderLayout.CENTER);

        // =========================================================
        // 3. VÙNG THỐNG KÊ DOANH THU (SOUTH)
        // =========================================================
        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 8));
        pnlSouth.setBorder(BorderFactory.createTitledBorder("Thống kê doanh thu hôm nay & tháng này"));
        pnlSouth.setBackground(new Color(230, 245, 255));

        lblDoanhThuNgay   = new JLabel("Doanh thu hôm nay: --");
        lblDoanhThuThang  = new JLabel("Doanh thu tháng này: --");

        Font fontStat = new Font("Segoe UI", Font.BOLD, 13);
        lblDoanhThuNgay.setFont(fontStat);
        lblDoanhThuThang.setFont(fontStat);
        lblDoanhThuNgay.setForeground(new Color(0, 100, 180));
        lblDoanhThuThang.setForeground(new Color(0, 130, 80));

        pnlSouth.add(lblDoanhThuNgay);
        pnlSouth.add(new JSeparator(SwingConstants.VERTICAL));
        pnlSouth.add(lblDoanhThuThang);

        add(pnlSouth, BorderLayout.SOUTH);

        // =========================================================
        // XỬ LÝ SỰ KIỆN (LISTENERS)
        // =========================================================

        // Click dòng bảng → đổ dữ liệu ngược lại Form
        tableHD.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableHD.getSelectedRow();
                if (row < 0) return;

                txtMaHD.setText(tableModel.getValueAt(row, 0).toString());
                txtMaSP.setText(tableModel.getValueAt(row, 1).toString());
                txtTenSP.setText(tableModel.getValueAt(row, 2).toString());
                txtSoLuong.setText(tableModel.getValueAt(row, 3).toString());
                txtGiaBan.setText(tableModel.getValueAt(row, 4).toString());
                txtTongTien.setText(tableModel.getValueAt(row, 5).toString());
                Object ngay = tableModel.getValueAt(row, 6);
                txtTimKiem.setText(""); // giữ nguyên ô tìm kiếm
                // Điền ngày lập vào ô ngày tìm kiếm để tiện xem
                // (không ghi đè ô tìm kiếm – dùng field riêng)
                // Ghi vào txtNgayLap nếu bạn thêm field; hiện lưu tạm trong txtTimKiem
                // => Thay bằng txtNgayLap (field riêng) ở dưới
                txtMaHD.setEditable(false);
            }
        });

        // Nút Thêm Hóa Đơn
        btnThem.addActionListener(e -> {
            String maHD = txtMaHD.getText().trim();
            if (maHD.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mã hóa đơn không được để trống!");
                return;
            }
            if (truycapHD.getHoaDonByMa(maHD) != null) {
                JOptionPane.showMessageDialog(this, "Mã hóa đơn này đã tồn tại trên hệ thống!");
                return;
            }

            HoaDon hd = getHoaDonFromForm();
            if (hd != null) {
                if (truycapHD.themHoaDon(hd)) {
                    JOptionPane.showMessageDialog(this, "Thêm hóa đơn thành công!");
                    clearForm();
                    loadDataToTable();
                    updateDoanhThuLabels();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại. Vui lòng kiểm tra lại dữ liệu!");
                }
            }
        });

        // Nút Sửa Hóa Đơn
        btnSua.addActionListener(e -> {
            String maHD = txtMaHD.getText().trim();
            if (maHD.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần sửa!");
                return;
            }

            HoaDon hd = getHoaDonFromForm();
            if (hd != null) {
                if (truycapHD.suaHoaDon(hd)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật hóa đơn thành công!");
                    clearForm();
                    loadDataToTable();
                    updateDoanhThuLabels();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }
            }
        });

        // Nút Xóa Hóa Đơn
        btnXoa.addActionListener(e -> {
            String maHD = txtMaHD.getText().trim();
            if (maHD.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xóa!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa hóa đơn [" + maHD + "] này?",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (truycapHD.xoaHoaDon(maHD)) {
                    JOptionPane.showMessageDialog(this, "Đã xóa hóa đơn thành công!");
                    clearForm();
                    loadDataToTable();
                    updateDoanhThuLabels();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                }
            }
        });

        // Nút Làm Mới
        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadDataToTable();
        });

        // Nút Lọc Theo Ngày
        btnTimKiem.addActionListener(e -> {
            String ngayStr = txtTimKiem.getText().trim();
            if (ngayStr.isEmpty()) {
                loadDataToTable();
                return;
            }
            try {
                LocalDate ngay = LocalDate.parse(ngayStr);
                List<HoaDon> ketQua = truycapHD.getHoaDonTheoNgay(ngay);
                fillTable(ketQua);

                // Hiển thị doanh thu của ngày đang lọc
                double dt = truycapHD.getTongDoanhThuTheoNgay(ngay);
                lblDoanhThuNgay.setText("Doanh thu " + ngayStr + ": " + CURRENCY_FORMAT.format(dt) + " đ");
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Ngày không đúng định dạng (yyyy-mm-dd, ví dụ: 2024-05-20)!");
            }
        });
    }

    // ==== CÁC HÀM TRỢ GIÚP (HELPER METHODS) ====

    private void loadDataToTable() {
        fillTable(truycapHD.getAllHoaDon());
    }

    private void fillTable(List<HoaDon> list) {
        tableModel.setRowCount(0);
        for (HoaDon hd : list) {
            tableModel.addRow(new Object[]{
                    hd.getMaHD(),
                    hd.getMaSP(),
                    hd.getTenSP(),
                    hd.getSoLuongMua(),
                    hd.getGiaBan(),
                    hd.getTongTien(),
                    hd.getNgayLap()
            });
        }
    }

    private HoaDon getHoaDonFromForm() {
        String maHD    = txtMaHD.getText().trim();
        String maSP    = txtMaSP.getText().trim();
        String tenSP   = txtTenSP.getText().trim();
        String slStr   = txtSoLuong.getText().trim();
        String giaStr  = txtGiaBan.getText().trim();
        String ngayStr = txtTimKiem.getText().trim(); // dùng chung ô ngày

        if (maSP.isEmpty() || tenSP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã sản phẩm và Tên sản phẩm không được để trống!");
            return null;
        }

        int soLuong;
        try {
            soLuong = Integer.parseInt(slStr);
            if (soLuong <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên dương!");
            return null;
        }

        double giaBan;
        try {
            giaBan = Double.parseDouble(giaStr);
            if (giaBan < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá bán phải là số hợp lệ và không âm!");
            return null;
        }

        LocalDate ngayLap = LocalDate.now(); // mặc định hôm nay
        if (!ngayStr.isEmpty()) {
            try {
                ngayLap = LocalDate.parse(ngayStr);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Ngày lập không đúng định dạng (yyyy-mm-dd)!\nĐể trống sẽ tự dùng ngày hôm nay.");
                return null;
            }
        }

        return new HoaDon(maHD, maSP, tenSP, soLuong, giaBan, ngayLap);
    }

    // Tự động tính và hiển thị Tổng Tiền khi nhập Số Lượng / Giá Bán
    private void tinhTongTien() {
        try {
            int sl    = Integer.parseInt(txtSoLuong.getText().trim());
            double gia = Double.parseDouble(txtGiaBan.getText().trim());
            txtTongTien.setText(CURRENCY_FORMAT.format(sl * gia) + " đ");
        } catch (NumberFormatException e) {
            txtTongTien.setText("");
        }
    }

    // Cập nhật nhãn thống kê doanh thu hôm nay & tháng này
    private void updateDoanhThuLabels() {
        LocalDate today = LocalDate.now();
        double dtNgay   = truycapHD.getTongDoanhThuTheoNgay(today);
        double dtThang  = truycapHD.getTongDoanhThuTheoThang(today.getMonthValue(), today.getYear());

        lblDoanhThuNgay.setText("Doanh thu hôm nay: " + CURRENCY_FORMAT.format(dtNgay) + " đ");
        lblDoanhThuThang.setText("Doanh thu tháng " + today.getMonthValue() + "/" + today.getYear()
                + ": " + CURRENCY_FORMAT.format(dtThang) + " đ");
    }

    private void clearForm() {
        txtMaHD.setText("");
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtSoLuong.setText("");
        txtGiaBan.setText("");
        txtTongTien.setText("");
        txtTimKiem.setText("");
        txtMaHD.setEditable(true);
        tableHD.clearSelection();
        updateDoanhThuLabels();
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

        SwingUtilities.invokeLater(() -> new QuanLyHoaDon().setVisible(true));
    }
}